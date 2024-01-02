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
package org.openhab.binding.salus.internal.handler;

import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Objects.requireNonNull;
import static org.openhab.binding.salus.internal.SalusBindingConstants.BINDING_ID;
import static org.openhab.binding.salus.internal.SalusBindingConstants.SalusDevice.DSN;
import static org.openhab.core.thing.ThingStatus.OFFLINE;
import static org.openhab.core.thing.ThingStatus.ONLINE;
import static org.openhab.core.thing.ThingStatusDetail.BRIDGE_UNINITIALIZED;
import static org.openhab.core.thing.ThingStatusDetail.COMMUNICATION_ERROR;
import static org.openhab.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;
import static org.openhab.core.types.RefreshType.REFRESH;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.salus.internal.SalusBindingConstants;
import org.openhab.binding.salus.internal.rest.DeviceProperty;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Grześlowski - Initial contribution
 */
@NonNullByDefault
public class DeviceHandler extends BaseThingHandler {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private final Logger logger;
    @NonNullByDefault({})
    private String dsn;
    @NonNullByDefault({})
    private CloudApi cloudApi;
    private final Map<String, String> channelUidMap = new HashMap<>();
    private final Map<String, String> channelX100UidMap = new HashMap<>();

    public DeviceHandler(Thing thing) {
        super(thing);
        logger = LoggerFactory.getLogger(DeviceHandler.class.getName() + "[" + thing.getUID().getId() + "]");
    }

    @Override
    public void initialize() {
        try {
            logger.debug("Initializing thing...");
            internalInitialize();
        } catch (Exception e) {
            logger.error("Error occurred while initializing Salus device!", e);
            updateStatus(OFFLINE, CONFIGURATION_ERROR,
                    "Error occurred while initializing Salus device! " + e.getLocalizedMessage());
        }
    }

    private void internalInitialize() {
        var bridge = getBridge();
        if (bridge == null) {
            logger.debug("No bridge for thing with UID {}", thing.getUID());
            updateStatus(OFFLINE, BRIDGE_UNINITIALIZED,
                    "There is no bridge for this thing. Remove it and add it again.");
            return;
        }
        var bridgeHandler = bridge.getHandler();
        if (!(bridgeHandler instanceof CloudBridgeHandler cloudHandler)) {
            logger.debug("Bridge is not instance of {}! Current bridge class {}, Thing UID {}",
                    CloudBridgeHandler.class.getSimpleName(), CloudBridgeHandler.class.getSimpleName(), thing.getUID());
            updateStatus(OFFLINE, BRIDGE_UNINITIALIZED, "There is wrong type of bridge for cloud device!");
            return;
        }
        this.cloudApi = cloudHandler;

        dsn = (String) getConfig().get(DSN);

        if ("".equals(dsn)) {
            logger.debug("No {} for thing with UID {}", DSN, thing.getUID());
            updateStatus(OFFLINE, CONFIGURATION_ERROR,
                    "There is no " + DSN + " for this thing. Remove it and add it again.");
            return;
        }

        try {
            var device = this.cloudApi.findDevice(dsn);
            if (device.isEmpty()) {
                var msg = "Device with DSN " + dsn + " not found!";
                logger.error(msg);
                updateStatus(OFFLINE, COMMUNICATION_ERROR, msg);
                return;
            }
            if (!device.get().isConnected()) {
                var msg = "Device with DSN " + dsn + " is not connected!";
                logger.error(msg);
                updateStatus(OFFLINE, COMMUNICATION_ERROR, msg);
                return;
            }
            var channels = findDeviceProperties().stream().map(this::buildChannel).toList();
            if (channels.isEmpty()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "There are no channels for " + dsn + ".");
                return;
            }
            updateChannels(channels);
        } catch (Exception e) {
            var msg = "Error when connecting to Salus Cloud!";
            logger.error(msg, e);
            updateStatus(OFFLINE, COMMUNICATION_ERROR, msg);
            return;
        }

        // done
        updateStatus(ONLINE);
    }

    private Channel buildChannel(DeviceProperty<?> property) {
        String channelId;
        String acceptedItemType;
        if (property instanceof DeviceProperty.BooleanDeviceProperty booleanProperty) {
            channelId = inOrOut(property.getDirection(), SalusBindingConstants.Channels.GENERIC_INPUT_BOOL_CHANNEL,
                    SalusBindingConstants.Channels.GENERIC_OUTPUT_BOOL_CHANNEL);
            acceptedItemType = "Switch";
        } else if (property instanceof DeviceProperty.LongDeviceProperty longDeviceProperty) {
            if (SalusBindingConstants.Channels.TEMPERATURE_CHANNELS.contains(longDeviceProperty.getName())) {
                // a temp channel
                channelId = inOrOut(property.getDirection(),
                        SalusBindingConstants.Channels.TEMPERATURE_INPUT_NUMBER_CHANNEL,
                        SalusBindingConstants.Channels.TEMPERATURE_OUTPUT_NUMBER_CHANNEL);
            } else {
                channelId = inOrOut(property.getDirection(),
                        SalusBindingConstants.Channels.GENERIC_INPUT_NUMBER_CHANNEL,
                        SalusBindingConstants.Channels.GENERIC_OUTPUT_NUMBER_CHANNEL);
            }
            acceptedItemType = "Number";
        } else if (property instanceof DeviceProperty.StringDeviceProperty stringDeviceProperty) {
            channelId = inOrOut(property.getDirection(), SalusBindingConstants.Channels.GENERIC_INPUT_CHANNEL,
                    SalusBindingConstants.Channels.GENERIC_OUTPUT_CHANNEL);
            acceptedItemType = "String";
        } else {
            throw new UnsupportedOperationException(
                    "Property class " + property.getClass().getSimpleName() + " is not supported!");
        }

        var channelUid = new ChannelUID(thing.getUID(), buildChannelUid(property.getName()));
        var channelTypeUID = new ChannelTypeUID(BINDING_ID, channelId);
        return ChannelBuilder.create(channelUid, acceptedItemType).withType(channelTypeUID)
                .withLabel(buildChannelDisplayName(property.getDisplayName())).build();
    }

    private String buildChannelUid(final String name) {
        String uid = name;
        var map = channelUidMap;
        if (name.contains("x100")) {
            map = channelX100UidMap;
            uid = removeX100(uid);
        }
        uid = uid.replaceAll("[^[\\w-]*]", "_");
        final var firstUid = uid;
        var idx = 1;
        while (map.containsKey(uid)) {
            uid = firstUid + "_" + idx++;
        }
        map.put(uid, name);
        return uid;
    }

    private String buildChannelDisplayName(final String displayName) {
        if (displayName.contains("x100")) {
            return removeX100(displayName);
        }
        return displayName;
    }

    private static String removeX100(String name) {
        var withoutSuffix = name.replace("_x100", "").replace("x100", "");
        if (withoutSuffix.endsWith("_")) {
            withoutSuffix = withoutSuffix.substring(0, withoutSuffix.length() - 2);
        }
        return withoutSuffix;
    }

    private String inOrOut(@Nullable String direction, String in, String out) {
        if ("output".equalsIgnoreCase(direction)) {
            return out;
        }
        if ("input".equalsIgnoreCase(direction)) {
            return in;
        }

        logger.warn("Direction [{}] is unknown!", direction);
        return out;
    }

    private void updateChannels(final List<Channel> channels) {
        var thingBuilder = editThing();
        thingBuilder.withChannels(channels);
        updateThing(thingBuilder.build());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Accepting command {} for channel {}", command, channelUID.getId());
        try {
            if (command instanceof RefreshType) {
                handleRefreshCommand(channelUID);
            } else if (command instanceof OnOffType typedCommand) {
                handleBoolCommand(channelUID, typedCommand == OnOffType.ON);
            } else if (command instanceof UpDownType typedCommand) {
                handleBoolCommand(channelUID, typedCommand == UpDownType.UP);
            } else if (command instanceof OpenClosedType typedCommand) {
                handleBoolCommand(channelUID, typedCommand == OpenClosedType.OPEN);
            } else if (command instanceof PercentType typedCommand) {
                handleDecimalCommand(channelUID, typedCommand.as(DecimalType.class));
            } else if (command instanceof DecimalType typedCommand) {
                handleDecimalCommand(channelUID, typedCommand);
            } else if (command instanceof StringType typedCommand) {
                handleStringCommand(channelUID, typedCommand);
            } else {
                logger.warn("Does not know how to handle command `{}` ({}) on channel `{}`!", command,
                        command.getClass().getSimpleName(), channelUID);
            }
        } catch (Exception ex) {
            logger.error("Error occurred while handling command `{}` ({}) on channel `{}`!", command,
                    command.getClass().getSimpleName(), channelUID.getId(), ex);
        }
    }

    private void handleRefreshCommand(ChannelUID channelUID) {
        var id = channelUID.getId();
        String salusId;
        boolean isX100;
        if (channelUidMap.containsKey(id)) {
            salusId = channelUidMap.get(id);
            isX100 = false;
        } else if (channelX100UidMap.containsKey(id)) {
            salusId = channelX100UidMap.get(id);
            isX100 = true;
        } else {
            logger.warn("Channel {} not found in channelUidMap and channelX100UidMap!", id);
            return;
        }

        var propertyOptional = findDeviceProperties().stream().filter(property -> property.getName().equals(salusId))
                .findFirst();
        if (propertyOptional.isEmpty()) {
            logger.warn("Property {} not found in response!", salusId);
            return;
        }
        var property = propertyOptional.get();
        State state;
        if (property instanceof DeviceProperty.BooleanDeviceProperty booleanProperty) {
            var value = booleanProperty.getValue();
            if (value != null && value) {
                state = OnOffType.ON;
            } else {
                state = OnOffType.OFF;
            }
        } else if (property instanceof DeviceProperty.LongDeviceProperty longDeviceProperty) {
            var value = longDeviceProperty.getValue();
            if (value == null) {
                value = 0L;
            }
            if (isX100) {
                state = new DecimalType(new BigDecimal(value).divide(ONE_HUNDRED, new MathContext(5, HALF_EVEN)));
            } else {
                state = new DecimalType(value);
            }
        } else if (property instanceof DeviceProperty.StringDeviceProperty stringDeviceProperty) {
            state = new StringType(stringDeviceProperty.getValue());
        } else {
            logger.warn("Property class {} is not supported!", property.getClass().getSimpleName());
            return;
        }
        logger.debug("Setting value {}{} for channel {}", state, isX100 ? " (x100)" : "", channelUID);
        updateState(channelUID, state);
    }

    private SortedSet<DeviceProperty<?>> findDeviceProperties() {
        return this.cloudApi.findPropertiesForDevice(dsn);
    }

    private void handleBoolCommand(ChannelUID channelUID, boolean command) {
        var id = channelUID.getId();
        String salusId;
        if (channelUidMap.containsKey(id)) {
            salusId = requireNonNull(channelUidMap.get(id));
        } else {
            logger.warn("Channel {} not found in channelUidMap!", id);
            return;
        }
        cloudApi.setValueForProperty(dsn, salusId, command);
        handleCommand(channelUID, REFRESH);
    }

    private void handleDecimalCommand(ChannelUID channelUID, @Nullable DecimalType command) {
        if (command == null) {
            return;
        }
        var id = channelUID.getId();
        String salusId;
        long value;
        if (channelUidMap.containsKey(id)) {
            salusId = requireNonNull(channelUidMap.get(id));
            value = command.toBigDecimal().longValue();
        } else if (channelX100UidMap.containsKey(id)) {
            salusId = requireNonNull(channelX100UidMap.get(id));
            value = command.toBigDecimal().multiply(ONE_HUNDRED).longValue();
        } else {
            logger.warn("Channel {} not found in channelUidMap and channelX100UidMap!", id);
            return;
        }
        cloudApi.setValueForProperty(dsn, salusId, value);
        handleCommand(channelUID, REFRESH);
    }

    private void handleStringCommand(ChannelUID channelUID, StringType command) {
        var id = channelUID.getId();
        String salusId;
        if (channelUidMap.containsKey(id)) {
            salusId = requireNonNull(channelUidMap.get(id));
        } else {
            logger.warn("Channel {} not found in channelUidMap!", id);
            return;
        }
        var value = command.toFullString();
        cloudApi.setValueForProperty(dsn, salusId, value);
        handleCommand(channelUID, REFRESH);
    }
}
