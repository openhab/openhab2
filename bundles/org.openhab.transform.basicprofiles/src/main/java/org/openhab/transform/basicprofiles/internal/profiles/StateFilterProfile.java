/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.transform.basicprofiles.internal.profiles;

import static java.util.function.Predicate.not;
import static org.openhab.transform.basicprofiles.internal.factory.BasicProfilesFactory.STATE_FILTER_UID;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.measure.Unit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.profiles.ProfileCallback;
import org.openhab.core.thing.profiles.ProfileContext;
import org.openhab.core.thing.profiles.ProfileTypeUID;
import org.openhab.core.thing.profiles.StateProfile;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.types.UnDefType;
import org.openhab.core.util.Statistics;
import org.openhab.transform.basicprofiles.internal.config.StateFilterProfileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accepts updates to state as long as conditions are met. Support for sending fixed state if conditions are *not*
 * met.
 *
 * @author Arne Seime - Initial contribution
 * @author Jimmy Tanagra - Expanded the comparison types
 * @author Jimmy Tanagra - Added support for functions
 * @author Andrew Fiddian-Green - Normalise calculations based on the Unit of the linked Item
 */
@NonNullByDefault
public class StateFilterProfile implements StateProfile {

    private static final String OPERATOR_NAME_PATTERN = Stream.of(StateCondition.ComparisonType.values())
            .map(StateCondition.ComparisonType::name)
            // We want to match the longest operator first, e.g. `GTE` before `GT`
            .sorted(Comparator.comparingInt(String::length).reversed())
            // Require a leading space only when it is preceded by a non-space character, e.g. `Item1 GTE 0`
            // so we can have conditions against input data without needing a leading space, e.g. `GTE 0`
            .collect(Collectors.joining("|", "(?:(?<=\\S)\\s+|^\\s*)(?:", ")\\s"));

    private static final String OPERATOR_SYMBOL_PATTERN = Stream.of(StateCondition.ComparisonType.values())
            .map(StateCondition.ComparisonType::symbol)
            // We want to match the longest operator first, e.g. `<=` before `<`
            .sorted(Comparator.comparingInt(String::length).reversed()) //
            .collect(Collectors.joining("|", "(?:", ")"));

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            // - Without the non-greedy operator in the first capture group,
            // it will match `Item<` when encountering `Item<>X` condition
            // - Symbols may be more prevalently used, so check them first
            "(.*?)(" + OPERATOR_SYMBOL_PATTERN + "|" + OPERATOR_NAME_PATTERN + ")(.*)", Pattern.CASE_INSENSITIVE);

    // Function pattern to match `$NAME` or `$NAME(5)`.
    // The number represents an optional window size that applies to the function.
    private final static Pattern FUNCTION_PATTERN = Pattern.compile("\\$(\\w+)(?:\\s*\\(\\s*(\\d+)\\s*\\))?\\s*");

    private final static int DEFAULT_WINDOW_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger(StateFilterProfile.class);

    private final ProfileCallback callback;

    private final ItemRegistry itemRegistry;

    private final List<StateCondition> conditions;

    private final @Nullable State configMismatchState;

    private @Nullable Item linkedItem = null;

    private State newState = UnDefType.UNDEF;
    private State acceptedState = UnDefType.UNDEF;
    private LinkedList<State> previousStates = new LinkedList<>();

    private final int windowSize;

    private @Nullable Unit<?> systemUnit = null; // reference (zero based) system unit for conversions
    private boolean systemUnitInitialized = false;

    public StateFilterProfile(ProfileCallback callback, ProfileContext context, ItemRegistry itemRegistry) {
        this.callback = callback;
        this.itemRegistry = itemRegistry;

        StateFilterProfileConfig config = context.getConfiguration().as(StateFilterProfileConfig.class);

        conditions = parseConditions(config.conditions, config.separator);
        int maxWindowSize = 0;

        if (conditions.isEmpty()) {
            logger.warn("No valid conditions defined for StateFilterProfile. Link: {}. Conditions: {}",
                    callback.getItemChannelLink(), config.conditions);
        } else {
            for (StateCondition condition : conditions) {
                if (condition.lhsState instanceof FunctionType function) {
                    int windowSize = function.getWindowSize();
                    if (windowSize > maxWindowSize) {
                        maxWindowSize = windowSize;
                    }
                }
                if (condition.rhsState instanceof FunctionType function) {
                    int windowSize = function.getWindowSize();
                    if (windowSize > maxWindowSize) {
                        maxWindowSize = windowSize;
                    }
                }
            }
        }

        windowSize = maxWindowSize;
        configMismatchState = parseState(config.mismatchState, context.getAcceptedDataTypes());
    }

    private List<StateCondition> parseConditions(List<String> conditions, String separator) {
        List<StateCondition> parsedConditions = new ArrayList<>();

        conditions.stream() //
                .flatMap(c -> Stream.of(c.split(separator))) //
                .map(String::trim) //
                .filter(not(String::isBlank)) //
                .forEach(expression -> {
                    Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
                    if (!matcher.matches()) {
                        logger.warn(
                                "Malformed condition expression: '{}' in link '{}'. Expected format ITEM_NAME OPERATOR ITEM_OR_STATE, where OPERATOR is one of: {}",
                                expression, callback.getItemChannelLink(),
                                StateCondition.ComparisonType.namesAndSymbols());
                        return;

                    }

                    String lhs = matcher.group(1).trim();
                    String operator = matcher.group(2).trim();
                    String rhs = matcher.group(3).trim();
                    try {
                        StateCondition.ComparisonType comparisonType = StateCondition.ComparisonType
                                .fromSymbol(operator).orElseGet(
                                        () -> StateCondition.ComparisonType.valueOf(operator.toUpperCase(Locale.ROOT)));
                        parsedConditions.add(new StateCondition(lhs, comparisonType, rhs));
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid comparison operator: '{}' in link '{}'. Expected one of: {}", operator,
                                callback.getItemChannelLink(), StateCondition.ComparisonType.namesAndSymbols());
                    }
                });

        return parsedConditions;
    }

    @Override
    public ProfileTypeUID getProfileTypeUID() {
        return STATE_FILTER_UID;
    }

    @Override
    public void onStateUpdateFromItem(State state) {
        // do nothing
    }

    @Override
    public void onCommandFromItem(Command command) {
        callback.handleCommand(command);
    }

    @Override
    public void onCommandFromHandler(Command command) {
        callback.sendCommand(command);
    }

    @Override
    public void onStateUpdateFromHandler(State state) {
        if (!isStateAllowed(state)) {
            logger.debug("Received non allowed state update from handler: {}, ignored", state);
            return;
        }
        newState = state;
        State resultState = checkCondition(state);
        if (resultState != null) {
            logger.debug("Received state update from handler: {}, forwarded as {}", state, resultState);
            acceptedState = resultState;
            callback.sendUpdate(resultState);
        } else {
            logger.debug("Received state update from handler: {}, not forwarded to item", state);
        }
        if (windowSize > 0 && ((state instanceof DecimalType) || ((state instanceof QuantityType)))) {
            previousStates.addLast(state);
            if (previousStates.size() > windowSize) {
                previousStates.removeFirst();
            }
        }
    }

    @Nullable
    private State checkCondition(State state) {
        if (conditions.isEmpty()) {
            logger.warn(
                    "No valid configuration defined for StateFilterProfile (check for log messages when instantiating profile) - skipping state update. Link: '{}'",
                    callback.getItemChannelLink());
            return null;
        }
        return conditions.stream().allMatch(c -> c.check(state)) ? state : configMismatchState;
    }

    private @Nullable Item getLinkedItem() {
        if (linkedItem == null) {
            linkedItem = getItemOrNull(callback.getItemChannelLink().getItemName());
        }
        return linkedItem;
    }

    @Nullable
    State parseState(@Nullable String stateString, List<Class<? extends State>> acceptedDataTypes) {
        // Quoted strings are parsed as StringType
        if (stateString == null || stateString.isEmpty()) {
            return null;
        } else if (isQuotedString(stateString)) {
            return new StringType(stateString.substring(1, stateString.length() - 1));
        } else if (parseFunction(stateString) instanceof FunctionType function) {
            return function;
        } else if (TypeParser.parseState(acceptedDataTypes, stateString) instanceof State state) {
            return state;
        }
        return null;
    }

    private boolean isQuotedString(String value) {
        return (value.startsWith("'") && value.endsWith("'")) || //
                (value.startsWith("\"") && value.endsWith("\""));
    }

    @Nullable
    FunctionType parseFunction(String functionDefinition) {
        if (!functionDefinition.startsWith("$")) {
            return null;
        }
        logger.debug("Parsing function: '{}'", functionDefinition);
        Matcher matcher = FUNCTION_PATTERN.matcher(functionDefinition);
        if (!matcher.matches()) {
            logger.warn("Invalid function definition: '{}'", functionDefinition);
            return null;
        }
        String functionName = matcher.group(1).toUpperCase(Locale.ROOT);
        try {
            FunctionType.Function type = FunctionType.Function.valueOf(functionName);
            Optional<Integer> windowSize = Optional.ofNullable(matcher.group(2)).map(Integer::parseInt);
            return new FunctionType(type, windowSize);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid function name: '{}'. Expected one of: {}", functionName,
                    Stream.of(FunctionType.Function.values()).map(Enum::name).collect(Collectors.joining(", ")));
            return null;
        }
    }

    private @Nullable Item getItemOrNull(String value) {
        try {
            return itemRegistry.getItem(value);
        } catch (ItemNotFoundException e) {
            return null;
        }
    }

    class StateCondition {
        private ComparisonType comparisonType;
        private String lhsString;
        private String rhsString;
        private @Nullable State lhsState;
        private @Nullable State rhsState;

        public StateCondition(String lhs, ComparisonType comparisonType, String rhs) {
            this.comparisonType = comparisonType;
            lhsString = lhs;
            rhsString = rhs;
            // Convert quoted strings to StringType, and UnDefTypes to UnDefType
            // UnDefType gets special treatment because we don't want `UNDEF` to be parsed as a string
            // Anything else, defer parsing until we're checking the condition
            // so we can try based on the item's accepted data types
            this.lhsState = parseState(lhsString, List.of(UnDefType.class));
            this.rhsState = parseState(rhsString, List.of(UnDefType.class));
        }

        /**
         * Check if the condition is met.
         *
         * If the lhs is empty, the condition is checked against the input state.
         *
         * @param input the state to check against
         * @return true if the condition is met, false otherwise
         */
        public boolean check(State input) {
            if (logger.isDebugEnabled()) {
                logger.debug("Evaluating {} with input: {} ({}). Link: '{}'", this, input,
                        input.getClass().getSimpleName(), callback.getItemChannelLink());
            }

            try {
                // Don't overwrite the object variables. These need to be re-evaluated for each check
                State lhsState = this.lhsState;
                State rhsState = this.rhsState;
                Item lhsItem = null;
                Item rhsItem = null;
                boolean isDeltaCheck = false;

                if (rhsState == null) {
                    rhsItem = getItemOrNull(rhsString);
                } else if (rhsState instanceof FunctionType rhsFunction) {
                    if (rhsFunction.alwaysAccept()) {
                        return true;
                    }
                    if (rhsFunction.getType() == FunctionType.Function.DELTA) {
                        isDeltaCheck = true;
                    }
                    rhsItem = getLinkedItem();
                }

                if (lhsString.isEmpty()) {
                    lhsItem = getLinkedItem();
                    // special handling for `> 50%` condition
                    // we need to calculate the delta percent between the input and the accepted state
                    // but if the input is an actual Percent Quantity, perform a direct comparison between input and rhs
                    if (rhsString.endsWith("%")) {
                        // late-parsing because now we have the input state and can determine its type
                        if (input instanceof QuantityType qty && "%".equals(qty.getUnit().getSymbol())) {
                            lhsState = input;
                        } else {
                            lhsString = "$DELTA_PERCENT";
                            // Override rhsString and this.lhsState to avoid re-parsing them later
                            rhsString = rhsString.substring(0, rhsString.length() - 1).trim();
                            this.lhsState = new FunctionType(FunctionType.Function.DELTA_PERCENT, Optional.empty());
                            lhsState = this.lhsState;
                        }
                    } else {
                        lhsState = input;
                    }
                } else if (lhsState == null) {
                    lhsItem = getItemOrNull(lhsString);
                    lhsState = itemStateOrParseState(lhsItem, lhsString, rhsItem);

                    if (lhsState == null) {
                        logger.debug(
                                "The left hand side of the condition '{}' is not compatible with the right hand side '{}'",
                                lhsString, rhsString);
                        return false;
                    }
                }

                if (lhsState instanceof FunctionType lhsFunction) {
                    if (lhsFunction.alwaysAccept()) {
                        return true;
                    }
                    lhsItem = getLinkedItem();
                    lhsState = lhsFunction.calculate();
                    if (lhsState == null) {
                        logger.debug("Couldn't calculate the left hand side function '{}'", lhsString);
                        return false;
                    }
                    if (lhsFunction.getType() == FunctionType.Function.DELTA) {
                        isDeltaCheck = true;
                    }
                }

                if (rhsState == null) {
                    rhsState = itemStateOrParseState(rhsItem, rhsString, lhsItem);
                }

                // Don't convert QuantityType to other types, so that 1500 != 1500 W
                if (rhsState != null && !(rhsState instanceof QuantityType)) {
                    // Try to convert it to the same type as the lhs
                    // This allows comparing compatible types, e.g. PercentType vs OnOffType
                    rhsState = rhsState.as(lhsState.getClass());
                }

                if (rhsState == null) {
                    if (comparisonType == ComparisonType.NEQ || comparisonType == ComparisonType.NEQ_ALT) {
                        // They're not even type compatible, so return true for NEQ comparison
                        return true;
                    } else {
                        logger.debug("RHS: '{}' is not compatible with LHS '{}' ({})", rhsString, lhsState,
                                lhsState.getClass().getSimpleName());
                        return false;
                    }
                }

                // Using Object because we could be comparing State or String objects
                Object lhs;
                Object rhs;

                // Java Enums (e.g. OnOffType) are inherently Comparable,
                // but we don't want to allow comparisons like "ON > OFF"
                if (lhsState instanceof Comparable && !(lhsState instanceof Enum)) {
                    lhs = lhsState;
                } else {
                    // Only allow EQ and NEQ for non-comparable states
                    if (!(comparisonType == ComparisonType.EQ || comparisonType == ComparisonType.NEQ
                            || comparisonType == ComparisonType.NEQ_ALT)) {
                        logger.debug("LHS: '{}' ({}) only supports '==' and '!==' comparisons", lhsState,
                                lhsState.getClass().getSimpleName());
                        return false;
                    }
                    lhs = lhsState instanceof Enum ? lhsState : lhsState.toString();
                }

                rhs = Objects.requireNonNull(rhsState instanceof StringType ? rhsState.toString() : rhsState);

                if ((rhs instanceof QuantityType rhsQty) && (lhs instanceof QuantityType lhsQty)) {
                    if (isDeltaCheck) {
                        if (rhsQty.toUnitRelative(lhsQty.getUnit()) instanceof QuantityType relativeRhs) {
                            rhs = relativeRhs;
                        }
                    } else if (hasSystemUnit()) {
                        lhs = systemUnitQuantityType(lhsQty) instanceof QuantityType lhsSU ? lhsSU : lhs;
                        rhs = systemUnitQuantityType(rhsQty) instanceof QuantityType rhsSU ? rhsSU : rhs;
                    }
                }

                if (logger.isDebugEnabled()) {
                    if (lhsString.isEmpty()) {
                        logger.debug("Performing a comparison between input '{}' ({}) and value '{}' ({})", lhs,
                                lhs.getClass().getSimpleName(), rhs, rhs.getClass().getSimpleName());
                    } else {
                        logger.debug("Performing a comparison between '{}' state '{}' ({}) and value '{}' ({})",
                                lhsString, lhs, lhs.getClass().getSimpleName(), rhs, rhs.getClass().getSimpleName());
                    }
                }

                @SuppressWarnings({ "rawtypes", "unchecked" })
                boolean result = switch (comparisonType) {
                    case EQ -> lhs.equals(rhs);
                    case NEQ, NEQ_ALT -> !lhs.equals(rhs);
                    case GT -> ((Comparable) lhs).compareTo(rhs) > 0;
                    case GTE -> ((Comparable) lhs).compareTo(rhs) >= 0;
                    case LT -> ((Comparable) lhs).compareTo(rhs) < 0;
                    case LTE -> ((Comparable) lhs).compareTo(rhs) <= 0;
                };

                return result;
            } catch (IllegalArgumentException | ClassCastException e) {
                logger.warn("Error evaluating condition: {} in link '{}': {}", this, callback.getItemChannelLink(),
                        e.getMessage());
            }
            return false;
        }

        private @Nullable State itemStateOrParseState(@Nullable Item item, String value, @Nullable Item oppositeItem) {
            if (item != null) {
                return item.getState();
            }

            if (oppositeItem != null) {
                List<Class<? extends State>> excludeStringType = oppositeItem.getAcceptedDataTypes().stream()
                        .filter(not(StringType.class::isAssignableFrom)).toList();
                return TypeParser.parseState(excludeStringType, value);
            }

            return null;
        }

        enum ComparisonType {
            EQ("=="),
            NEQ("!="),
            NEQ_ALT("<>"),
            GT(">"),
            GTE(">="),
            LT("<"),
            LTE("<=");

            private final String symbol;

            ComparisonType(String symbol) {
                this.symbol = symbol;
            }

            String symbol() {
                return symbol;
            }

            static Optional<ComparisonType> fromSymbol(String symbol) {
                for (ComparisonType type : values()) {
                    if (type.symbol.equals(symbol)) {
                        return Optional.of(type);
                    }
                }
                return Optional.empty();
            }

            static List<String> namesAndSymbols() {
                return Stream.of(values()).flatMap(entry -> Stream.of(entry.name(), entry.symbol())).toList();
            }
        }

        @Override
        public String toString() {
            return String.format("Condition('%s' %s %s '%s' %s)", lhsString,
                    Objects.requireNonNullElse(lhsState, "").toString(), comparisonType, rhsString,
                    Objects.requireNonNullElse(rhsState, "").toString());
        }
    }

    /**
     * Represents a function to be applied to the previous states.
     */
    class FunctionType implements State {
        enum Function {
            DELTA,
            DELTA_PERCENT,
            AVERAGE,
            AVG,
            MEDIAN,
            STDDEV,
            MIN,
            MAX
        }

        private final Function type;
        private final Optional<Integer> windowSize;

        public FunctionType(Function type, Optional<Integer> windowSize) {
            this.type = type;
            this.windowSize = windowSize;
        }

        public @Nullable State calculate() {
            logger.debug("Calculating function: {}", this);
            return switch (type) {
                case DELTA -> calculateDelta();
                case DELTA_PERCENT -> calculateDeltaPercent();
                default -> {
                    List<State> states = hasSystemUnit()
                            ? systemUnitQuantityTypes(previousStates).stream().map(q -> (State) q).toList()
                            : previousStates.stream().filter(s -> (s instanceof DecimalType)).toList();
                    int size = states.size();
                    int start = windowSize.map(w -> size - w).orElse(0);
                    states = start <= 0 ? states : states.subList(start, size);
                    yield switch (type) {
                        case AVG, AVERAGE -> calculateAverage(states);
                        case MEDIAN -> calculateMedian(states);
                        case STDDEV -> calculateStdDev(states);
                        case MIN -> calculateMin(states);
                        case MAX -> calculateMax(states);
                        default -> null;
                    };
                }
            };
        }

        public boolean alwaysAccept() {
            if ((type == Function.DELTA || type == Function.DELTA_PERCENT) && acceptedState == UnDefType.UNDEF) {
                return true;
            }
            if (type == Function.DELTA_PERCENT) {
                // avoid division by zero
                if (acceptedState instanceof QuantityType base) {
                    return base.toBigDecimal().compareTo(BigDecimal.ZERO) == 0;
                }
                if (acceptedState instanceof DecimalType base) {
                    return base.toBigDecimal().compareTo(BigDecimal.ZERO) == 0;
                }
            }
            return false;
        }

        @Override
        public <T extends State> @Nullable T as(@Nullable Class<T> target) {
            if (target == DecimalType.class || target == QuantityType.class) {
                return target.cast(calculate());
            }
            return null;
        }

        public int getWindowSize() {
            if (type == Function.DELTA || type == Function.DELTA_PERCENT) {
                // We don't need to keep previous states list to calculate the delta,
                // the previous state is kept in the acceptedState variable
                return 0;
            }
            return windowSize.orElse(DEFAULT_WINDOW_SIZE);
        }

        public Function getType() {
            return type;
        }

        @Override
        public String format(String _pattern) {
            return toFullString();
        }

        @Override
        public String toFullString() {
            return "$" + type.toString();
        }

        @Override
        public String toString() {
            return toFullString();
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private @Nullable State calculateAverage(List<State> states) {
            if (states.isEmpty()) {
                logger.debug("Not enough states to calculate sum");
                return null;
            }
            if (hasSystemUnit()) {
                List<QuantityType> systemUnitQuantityTypes = systemUnitQuantityTypes(states);
                return systemUnitQuantityTypes.isEmpty() ? null
                        : systemUnitQuantityTypes.stream()
                                .reduce(new QuantityType<>(0, Objects.requireNonNull(systemUnit)), QuantityType::add)
                                .divide(BigDecimal.valueOf(systemUnitQuantityTypes.size()));
            }
            BigDecimal sum = states.stream().map(s -> ((DecimalType) s).toBigDecimal()).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            return new DecimalType(sum.divide(BigDecimal.valueOf(states.size()), 2, RoundingMode.HALF_EVEN));
        }

        private @Nullable State calculateMedian(List<State> states) {
            if (states.isEmpty()) {
                logger.debug("Not enough states to calculate median");
                return null;
            }
            if (hasSystemUnit()) {
                BigDecimal median = Statistics
                        .median(systemUnitQuantityTypes(states).stream().map(q -> q.toBigDecimal()).toList());
                return median == null ? null : new QuantityType<>(median, Objects.requireNonNull(systemUnit));
            }
            List<BigDecimal> bdStates = states.stream().map(s -> ((DecimalType) s).toBigDecimal()).toList();
            return Optional.ofNullable(Statistics.median(bdStates)).map(median -> new DecimalType(median)).orElse(null);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private @Nullable State calculateStdDev(List<State> states) {
            if (states.isEmpty()) {
                logger.debug("Not enough states to calculate standard deviation");
                return null;
            }
            if (hasSystemUnit()) {
                QuantityType average = (QuantityType) calculateAverage(states);
                if (average == null) {
                    return null;
                }
                QuantityType variance = systemUnitQuantityTypes(states).stream().map(q -> {
                    QuantityType delta = q.subtract(average);
                    return delta.multiply(delta.toBigDecimal()); // don't square the unit
                }) //
                        .reduce(new QuantityType<>(0, Objects.requireNonNull(systemUnit)), QuantityType::add)
                        .divide(BigDecimal.valueOf(states.size()));
                return new QuantityType<>(variance.toBigDecimal().sqrt(MathContext.DECIMAL32), variance.getUnit());
            }
            BigDecimal average = Optional.ofNullable((DecimalType) calculateAverage(states))
                    .map(DecimalType::toBigDecimal).orElse(null);
            if (average == null) {
                return null;
            }
            BigDecimal variance = states.stream().map(s -> {
                BigDecimal delta = ((DecimalType) s).toBigDecimal().subtract(average);
                return delta.multiply(delta);
            }).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(states.size()),
                    MathContext.DECIMAL32);
            return new DecimalType(variance.sqrt(MathContext.DECIMAL32));
        }

        private @Nullable State calculateMin(List<State> states) {
            if (states.isEmpty()) {
                logger.debug("Not enough states to calculate min");
                return null;
            }
            if (hasSystemUnit()) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                Optional<QuantityType> min = systemUnitQuantityTypes(states).stream().min(QuantityType::compareTo);
                return min.isPresent() ? min.get() : null;
            }
            return states.stream().map(s -> ((DecimalType) s).toBigDecimal()).min(BigDecimal::compareTo)
                    .map(DecimalType::new).orElse(null);
        }

        private @Nullable State calculateMax(List<State> states) {
            if (states.isEmpty()) {
                logger.debug("Not enough states to calculate max");
                return null;
            }
            if (hasSystemUnit()) {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                Optional<QuantityType> max = systemUnitQuantityTypes(states).stream().max(QuantityType::compareTo);
                return max.isPresent() ? max.get() : null;
            }
            return states.stream().map(s -> ((DecimalType) s).toBigDecimal()).max(BigDecimal::compareTo)
                    .map(DecimalType::new).orElse(null);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private @Nullable State calculateDelta() {
            if (hasSystemUnit()) {
                QuantityType systemUnitNewState = systemUnitQuantityType(newState);
                QuantityType systemUnitAcceptedState = systemUnitQuantityType(acceptedState);
                if (systemUnitNewState != null && systemUnitAcceptedState != null) {
                    QuantityType result = systemUnitNewState.subtract(systemUnitAcceptedState);
                    return result.toBigDecimal().compareTo(BigDecimal.ZERO) < 0 ? result.negate() : result;
                }
                return null;
            }
            BigDecimal result = ((DecimalType) newState).toBigDecimal()
                    .subtract(((DecimalType) acceptedState).toBigDecimal()) //
                    .abs();
            return new DecimalType(result);
        }

        @SuppressWarnings({ "rawtypes" })
        private @Nullable State calculateDeltaPercent() {
            State calculatedDelta = calculateDelta();
            if (calculatedDelta == null) {
                return null;
            }
            BigDecimal bdDelta;
            BigDecimal bdBase;
            if (hasSystemUnit()) {
                QuantityType<?> acceptedStateReferenceQuantity = systemUnitQuantityType(acceptedState);
                if (acceptedStateReferenceQuantity instanceof QuantityType) {
                    bdDelta = ((QuantityType) calculatedDelta).toBigDecimal();
                    bdBase = acceptedStateReferenceQuantity.toBigDecimal();
                } else {
                    return null;
                }
            } else {
                bdDelta = ((DecimalType) calculatedDelta).toBigDecimal();
                bdBase = ((DecimalType) acceptedState).toBigDecimal();
            }
            bdBase = bdBase.abs();
            BigDecimal percent = bdDelta.multiply(BigDecimal.valueOf(100)).divide(bdBase, 2, RoundingMode.HALF_EVEN);
            return new DecimalType(percent);
        }
    }

    /**
     * Initialize the reference zero based system unit of the linked item. If there is no linked item, or it is not a
     * {@link NumberItem} or if the item does not have a unit, then the systemUnit is null.
     *
     * @return the systemUnit or null
     */
    protected @Nullable Unit<?> initSystemUnit() {
        if (systemUnitInitialized) {
            return systemUnit;
        }
        Unit<?> systemUnit = (getLinkedItem() instanceof NumberItem numberItem)
                && (numberItem.getUnit() instanceof Unit<?> numberItemUnit) ? numberItemUnit.getSystemUnit() : null;
        this.systemUnit = systemUnit;
        systemUnitInitialized = true;
        return systemUnit;
    }

    /**
     * Return true if 'systemUnit' is defined.
     */
    protected boolean hasSystemUnit() {
        return initSystemUnit() != null;
    }

    /**
     * Convert a {@link State} to a {@link QuantityType} with its value converted to the 'systemUnit'.
     * Returns null if the state is not a {@link QuantityType} or it does not convert to 'systemUnit'.
     *
     * The conversion can be made to both inverted and non-inverted units, so invertible type conversions
     * (e.g. Mirek <=> Kelvin) are supported.
     *
     * @return a {@link QuantityType} based on 'systemUnit'
     */
    protected @Nullable QuantityType<?> systemUnitQuantityType(State state) {
        return (state instanceof QuantityType<?> quantity) && hasSystemUnit()
                ? quantity.toInvertibleUnit(Objects.requireNonNull(systemUnit))
                : null;
    }

    /**
     * Convert a list of {@link State} to a list of {@link QuantityType} converted to 'systemUnit'. Exclude any
     * {@link State} that are not a {@link QuantityType}. Convert any remaining {@link QuantityType} to the 'systemUnit'
     * and exclude any values that did not convert.
     *
     * @param states list of {@link State} values
     * @return list of {@link QuantityType} values
     */
    @SuppressWarnings("rawtypes")
    protected List<QuantityType> systemUnitQuantityTypes(List<State> states) {
        return !hasSystemUnit() ? List.of()
                : states.stream().map(s -> systemUnitQuantityType(s)).filter(Objects::nonNull)
                        .map(s -> (QuantityType) s).toList();
    }

    /**
     * Check if the given {@link State} is allowed. Non allowed means that there is a 'systemUnit', the {@link State}
     * is a {@link QuantityType}, and the value is not compatible with 'systemUnit'.
     *
     * @param state the incoming state
     * @return true if allowed
     */
    protected boolean isStateAllowed(State state) {
        return !(state instanceof QuantityType<?>) || !hasSystemUnit()
                || Objects.nonNull(systemUnitQuantityType(state));
    }
}
