/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.modbus.internal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.thing.binding.generic.ChannelTransformation;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Class for performing transformations of a command or state.
 *
 * @author Jimmy Tanagra - Initial contribution
 *
 */
@NonNullByDefault
public class ModbusTransformation {

    public static final String TRANSFORM_DEFAULT = "default";

    /**
     * Ordered list of types that are tried out first when trying to parse transformed command
     */
    private static final List<Class<? extends Command>> DEFAULT_TYPES = List.of( //
            DecimalType.class, //
            OpenClosedType.class, //
            OnOffType.class //
    );

    private final Logger logger = LoggerFactory.getLogger(ModbusTransformation.class);
    private final @Nullable ChannelTransformation transformation;
    private final @Nullable String constantOutput;

    /**
     * @param transformations a list of transformations to apply. The transformations
     *            are chained and applied in the order they are given in the list. Each transformation
     *            can also contain the intersection symbol "∩" to separate multiple transformations in one line.
     * 
     *            If the list is null, or the first element is "default", or an empty string, the transformation
     *            will be considered as an identity transformation (and the input is returned as the output).
     * 
     *            If the first element is some other value, it is treated as constant and it
     *            will become the output of the transformation, regardless of the input.
     */
    public ModbusTransformation(@Nullable List<String> transformationList) {
        if (transformationList == null || transformationList.isEmpty()
                || transformationList.stream().allMatch(String::isBlank)) {
            transformation = null;
            constantOutput = "";
            return;
        }

        int size = transformationList.size();
        String firstLine = transformationList.get(0).trim();

        if (size == 1 && firstLine.equalsIgnoreCase(TRANSFORM_DEFAULT)) {
            // no-op (identity) transformation
            transformation = null;
            constantOutput = null;
            return;
        }

        if (transformationList.stream().allMatch(ChannelTransformation::isTransform)) {
            transformation = new ChannelTransformation(transformationList);
            constantOutput = null;
        } else {
            transformation = null;
            constantOutput = firstLine;
            if (size > 1) {
                logger.warn(
                        "Given transformation configuration {} did not match the correct pattern. Transformation output will be constant '{}'",
                        transformationList, constantOutput);
            } else {
                logger.debug("The output for transformation {} will be constant '{}'", transformationList,
                        constantOutput);
            }
        }
    }

    public String transform(String value) {
        if (transformation != null) {
            // return input if transformation failed
            return Objects.requireNonNull(transformation.apply(value).orElse(value));
        }

        return Objects.requireNonNullElse(constantOutput, value);
    }

    public boolean isIdentityTransform() {
        return transformation == null && constantOutput == null;
    }

    public static Optional<Command> tryConvertToCommand(String transformed) {
        return Optional.ofNullable(TypeParser.parseCommand(DEFAULT_TYPES, transformed));
    }

    /**
     * Transform state to another state using this transformation
     *
     * @param types types to used to parse the transformation result
     * @param state
     * @return Transformed command, or null if no transformation was possible
     */
    public @Nullable State transformState(List<Class<? extends State>> types, State state) {
        // Note that even identity transformations go through the State -> String -> State steps. This does add some
        // overhead but takes care of DecimalType -> PercentType conversions, for example.
        final String stateAsString = state.toString();
        final String transformed = transform(stateAsString);
        return TypeParser.parseState(types, transformed);
    }
}
