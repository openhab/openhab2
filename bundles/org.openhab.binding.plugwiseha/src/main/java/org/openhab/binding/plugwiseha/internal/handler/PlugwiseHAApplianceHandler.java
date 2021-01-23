/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.plugwiseha.internal.handler;

import static org.openhab.binding.plugwiseha.internal.PlugwiseHABindingConstants.*;
import static org.openhab.core.library.unit.MetricPrefix.*;
import static org.openhab.core.thing.ThingStatus.*;
import static org.openhab.core.thing.ThingStatusDetail.BRIDGE_OFFLINE;
import static org.openhab.core.thing.ThingStatusDetail.COMMUNICATION_ERROR;
import static org.openhab.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;

import java.util.List;
import java.util.Map;

import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Power;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.plugwiseha.internal.PlugwiseHABindingConstants;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.dto.*;
import org.openhab.binding.plugwiseha.internal.config.PlugwiseHAThingConfig;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.ImperialUnits;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelKind;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PlugwiseHAApplianceHandler} class is responsible for handling
 * commands and status updates for the Plugwise Home Automation appliances.
 * Extends @{link PlugwiseHABaseHandler}
 *
 * @author Bas van Wetten - Initial contribution
 * @author Leo Siepel - finish initial contribution
 *
 */
@NonNullByDefault
public class PlugwiseHAApplianceHandler extends PlugwiseHABaseHandler<Appliance, PlugwiseHAThingConfig> {

    private @Nullable Appliance appliance;
    private final Logger logger = LoggerFactory.getLogger(PlugwiseHAApplianceHandler.class);

    // Constructor

    public PlugwiseHAApplianceHandler(Thing thing) {
        super(thing);
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_VALVE.equals(thingTypeUID)
                || PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_PUMP.equals(thingTypeUID)
                || PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_BOILER.equals(thingTypeUID)
                || PlugwiseHABindingConstants.THING_TYPE_APPLIANCE_THERMOSTAT.equals(thingTypeUID);
    }

    // Overrides

    @Override
    protected synchronized void initialize(PlugwiseHAThingConfig config, PlugwiseHABridgeHandler bridgeHandler) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing Plugwise Home Automation appliance handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR,
                        "Invalid configuration for Plugwise Home Automation appliance handler.");
                return;
            }

            try {
                PlugwiseHAController controller = bridgeHandler.getController();
                if (controller != null) {
                    this.appliance = getEntity(controller, true);
                    if (this.appliance != null) {
                        if (this.appliance.isBatteryOperated()) {
                            addBatteryChannels();
                        }
                        setApplianceProperties();
                        updateStatus(ONLINE);
                    } else {
                        updateStatus(OFFLINE);
                    }
                } else {
                    updateStatus(OFFLINE, BRIDGE_OFFLINE);
                }
            } catch (PlugwiseHAException e) {
                updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_COMMUNICATION_ERROR);
            }
        }
    }

    @Override
    protected @Nullable Appliance getEntity(PlugwiseHAController controller, Boolean forceRefresh)
            throws PlugwiseHAException {
        PlugwiseHAThingConfig config = getPlugwiseThingConfig();
        Appliance appliance = controller.getAppliance(config.getId(), forceRefresh);

        return appliance;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleCommand(Appliance entity, ChannelUID channelUID, Command command) throws PlugwiseHAException {
        String channelID = channelUID.getIdWithoutGroup();

        PlugwiseHABridgeHandler bridge = this.getPlugwiseHABridge();
        if (bridge == null) {
            return;
        }

        PlugwiseHAController controller = bridge.getController();
        if (controller == null) {
            return;
        }

        switch (channelID) {
            case APPLIANCE_LOCK_CHANNEL:
                if (command instanceof OnOffType) {
                    try {
                        if (command == OnOffType.ON) {
                            controller.switchRelayLockOn(entity);
                        } else {
                            controller.switchRelayLockOff(entity);
                        }
                    } catch (PlugwiseHAException e) {
                        logger.warn("Unable to switch relay lock {} for appliance '{}'", (State) command,
                                entity.getName());
                    }
                }
                break;
            case APPLIANCE_OFFSET_CHANNEL:
                if (command instanceof QuantityType) {
                    QuantityType<Temperature> state = (QuantityType<Temperature>) command;
                    try {
                        Unit<Temperature> unit = entity.getOffsetTemperatureUnit().orElse(UNIT_CELSIUS)
                                .equals(UNIT_CELSIUS) ? SIUnits.CELSIUS : ImperialUnits.FAHRENHEIT;
                        controller.setOffsetTemperature(entity, state.toUnit(unit).doubleValue());
                    } catch (PlugwiseHAException e) {
                        logger.warn("Unable to update setpoint for zone '{}': {} -> {}", entity.getName(),
                                entity.getSetpointTemperature().orElse(null), state.doubleValue());
                    }
                }
                break;
            case APPLIANCE_POWER_CHANNEL:
                if (command instanceof OnOffType) {
                    try {
                        if (command == OnOffType.ON) {
                            controller.switchRelayOn(entity);
                        } else {
                            controller.switchRelayOff(entity);
                        }
                    } catch (PlugwiseHAException e) {
                        logger.warn("Unable to switch relay {} for appliance '{}'", (State) command, entity.getName());
                    }
                }
                break;
            case APPLIANCE_SETPOINT_CHANNEL:
                if (command instanceof QuantityType) {
                    QuantityType<Temperature> state = (QuantityType<Temperature>) command;
                    try {
                        Unit<Temperature> unit = entity.getSetpointTemperatureUnit().orElse(UNIT_CELSIUS)
                                .equals(UNIT_CELSIUS) ? SIUnits.CELSIUS : ImperialUnits.FAHRENHEIT;
                        controller.setThermostat(entity, state.toUnit(unit).doubleValue());
                    } catch (PlugwiseHAException e) {
                        logger.warn("Unable to update setpoint for appliance '{}': {} -> {}", entity.getName(),
                                entity.getSetpointTemperature().orElse(null), state.doubleValue());
                    }
                }
                break;
            default:
                logger.warn("Ignoring unsupported command = {} for channel = {}", command, channelUID);
        }
    }

    private State getDefaultState(String channelID) {
        State state = UnDefType.NULL;
        switch (channelID) {
            case APPLIANCE_BATTERYLEVEL_CHANNEL:
            case APPLIANCE_CHSTATE_CHANNEL:
            case APPLIANCE_DHWSTATE_CHANNEL:
            case APPLIANCE_OFFSET_CHANNEL:
            case APPLIANCE_POWER_USAGE_CHANNEL:
            case APPLIANCE_SETPOINT_CHANNEL:
            case APPLIANCE_TEMPERATURE_CHANNEL:
            case APPLIANCE_VALVEPOSITION_CHANNEL:
            case APPLIANCE_WATERPRESSURE_CHANNEL:
                state = UnDefType.NULL;
                break;
            case APPLIANCE_BATTERYLEVELLOW_CHANNEL:
            case APPLIANCE_LOCK_CHANNEL:
            case APPLIANCE_POWER_CHANNEL:
                state = UnDefType.UNDEF;
                break;
        }
        return state;
    }

    @Override
    protected void refreshChannel(Appliance entity, ChannelUID channelUID) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = getDefaultState(channelID);
        PlugwiseHAThingConfig config = getPlugwiseThingConfig();

        switch (channelID) {
            case APPLIANCE_BATTERYLEVEL_CHANNEL: {
                Double batteryLevel = entity.getBatteryLevel().orElse(null);

                if (batteryLevel != null) {
                    batteryLevel = batteryLevel * 100;
                    state = new QuantityType<Dimensionless>(batteryLevel.intValue(), Units.PERCENT);
                    if (batteryLevel <= config.getLowBatteryPercentage()) {
                        updateState(APPLIANCE_BATTERYLEVELLOW_CHANNEL, OnOffType.ON);
                    } else {
                        updateState(APPLIANCE_BATTERYLEVELLOW_CHANNEL, OnOffType.OFF);
                    }
                }
                break;
            }
            case APPLIANCE_BATTERYLEVELLOW_CHANNEL: {
                Double batteryLevel = entity.getBatteryLevel().orElse(null);

                if (batteryLevel != null) {
                    batteryLevel *= 100;
                    if (batteryLevel <= config.getLowBatteryPercentage()) {
                        state = OnOffType.ON;
                    } else {
                        state = OnOffType.OFF;
                    }
                }
                break;
            }
            case APPLIANCE_CHSTATE_CHANNEL:
                if (entity.getCHState().isPresent()) {
                    state = OnOffType.from(entity.getCHState().get());
                }
                break;
            case APPLIANCE_DHWSTATE_CHANNEL:
                if (entity.getDHWState().isPresent()) {
                    state = OnOffType.from(entity.getDHWState().get());
                }
                break;
            case APPLIANCE_LOCK_CHANNEL:
                Boolean relayLockState = entity.getRelayLockState().orElse(null);
                if (relayLockState != null) {
                    state = OnOffType.from(relayLockState);
                }
                break;
            case APPLIANCE_OFFSET_CHANNEL:
                if (entity.getOffsetTemperature().isPresent()) {
                    Unit<Temperature> unit = entity.getOffsetTemperatureUnit().orElse(UNIT_CELSIUS).equals(UNIT_CELSIUS)
                            ? SIUnits.CELSIUS
                            : ImperialUnits.FAHRENHEIT;
                    state = new QuantityType<Temperature>(entity.getOffsetTemperature().get(), unit);
                }
                break;
            case APPLIANCE_POWER_CHANNEL:
                if (entity.getRelayState().isPresent()) {
                    state = OnOffType.from(entity.getRelayState().get());
                }
                break;
            case APPLIANCE_POWER_USAGE_CHANNEL:
                if (entity.getPowerUsage().isPresent()) {
                    state = new QuantityType<Power>(entity.getPowerUsage().get(), Units.WATT);
                }
                break;
            case APPLIANCE_SETPOINT_CHANNEL:
                if (entity.getSetpointTemperature().isPresent()) {
                    Unit<Temperature> unit = entity.getSetpointTemperatureUnit().orElse(UNIT_CELSIUS)
                            .equals(UNIT_CELSIUS) ? SIUnits.CELSIUS : ImperialUnits.FAHRENHEIT;
                    state = new QuantityType<Temperature>(entity.getSetpointTemperature().get(), unit);
                }
                break;
            case APPLIANCE_TEMPERATURE_CHANNEL:
                if (entity.getTemperature().isPresent()) {
                    Unit<Temperature> unit = entity.getTemperatureUnit().orElse(UNIT_CELSIUS).equals(UNIT_CELSIUS)
                            ? SIUnits.CELSIUS
                            : ImperialUnits.FAHRENHEIT;
                    state = new QuantityType<Temperature>(entity.getTemperature().get(), unit);
                }
                break;
            case APPLIANCE_VALVEPOSITION_CHANNEL:
                if (entity.getValvePosition().isPresent()) {
                    Double valvePosition = entity.getValvePosition().get() * 100;
                    state = new QuantityType<Dimensionless>(valvePosition.intValue(), Units.PERCENT);
                }
                break;
            case APPLIANCE_WATERPRESSURE_CHANNEL:
                if (entity.getWaterPressure().isPresent()) {
                    Unit<Pressure> unit = HECTO(SIUnits.PASCAL);
                    state = new QuantityType<Pressure>(entity.getWaterPressure().get(), unit);
                }
                break;
            default:
                break;
        }

        if (state != UnDefType.NULL) {
            updateState(channelID, state);
        }
    }

    protected synchronized void addBatteryChannels() {
        logger.debug("Battery operated appliance detected: adding 'Battery level' and 'Battery low level' channels");

        ChannelUID channelUIDBatteryLevel = new ChannelUID(getThing().getUID(), APPLIANCE_BATTERYLEVEL_CHANNEL);
        ChannelUID channelUIDBatteryLevelLow = new ChannelUID(getThing().getUID(), APPLIANCE_BATTERYLEVELLOW_CHANNEL);

        boolean channelBatteryLevelExists = false;
        boolean channelBatteryLowExists = false;

        List<Channel> channels = getThing().getChannels();
        for (Channel channel : channels) {
            if (channel.getUID().equals(channelUIDBatteryLevel)) {
                channelBatteryLevelExists = true;
            } else if (channel.getUID().equals(channelUIDBatteryLevelLow)) {
                channelBatteryLowExists = true;
            }
            if (channelBatteryLevelExists && channelBatteryLowExists) {
                break;
            }
        }

        if (!channelBatteryLevelExists) {
            ThingBuilder thingBuilder = editThing();

            Channel channelBatteryLevel = ChannelBuilder.create(channelUIDBatteryLevel, "Number")
                    .withType(CHANNEL_TYPE_BATTERYLEVEL).withKind(ChannelKind.STATE).withLabel("Battery Level")
                    .withDescription("Represents the battery level as a percentage (0-100%)").build();

            thingBuilder.withChannel(channelBatteryLevel);

            updateThing(thingBuilder.build());
        }

        if (!channelBatteryLowExists) {
            ThingBuilder thingBuilder = editThing();

            Channel channelBatteryLow = ChannelBuilder.create(channelUIDBatteryLevelLow, "Switch")
                    .withType(CHANNEL_TYPE_BATTERYLEVELLOW).withKind(ChannelKind.STATE).withLabel("Battery Low Level")
                    .withDescription("Switches ON when battery level gets below threshold level").build();

            thingBuilder.withChannel(channelBatteryLow);

            updateThing(thingBuilder.build());
        }
    }

    protected void setApplianceProperties() {
        Map<String, String> properties = editProperties();
        logger.debug("Setting thing properties");
        Appliance localAppliance = this.appliance;
        if (localAppliance != null) {
            properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_DESCRIPTION, localAppliance.getDescription());
            properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_TYPE, localAppliance.getType());
            properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_FUNCTIONALITIES,
                    String.join(", ", localAppliance.getActuatorFunctionalities().keySet()));

            if (localAppliance.isZigbeeDevice()) {
                properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_ZB_TYPE,
                        localAppliance.getZigbeeNode().getType());
                properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_ZB_REACHABLE,
                        localAppliance.getZigbeeNode().getReachable());
                properties.put(PlugwiseHABindingConstants.APPLIANCE_PROPERTY_ZB_POWERSOURCE,
                        localAppliance.getZigbeeNode().getPowerSource());
                properties.put(Thing.PROPERTY_MAC_ADDRESS, localAppliance.getZigbeeNode().getMacAddress());
            }

            properties.put(Thing.PROPERTY_FIRMWARE_VERSION, localAppliance.getModule().getFirmwareVersion());
            properties.put(Thing.PROPERTY_HARDWARE_VERSION, localAppliance.getModule().getHardwareVersion());
            properties.put(Thing.PROPERTY_VENDOR, localAppliance.getModule().getVendorName());
            properties.put(Thing.PROPERTY_MODEL_ID, localAppliance.getModule().getVendorModel());
        }
        updateProperties(properties);
    }
}
