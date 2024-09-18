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
package org.openhab.binding.electroluxappliances.internal.handler;

import static org.openhab.binding.electroluxappliances.internal.ElectroluxAppliancesBindingConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.electroluxappliances.internal.ElectroluxAppliancesBindingConstants;
import org.openhab.binding.electroluxappliances.internal.ElectroluxAppliancesConfiguration;
import org.openhab.binding.electroluxappliances.internal.api.ElectroluxGroupAPI;
import org.openhab.binding.electroluxappliances.internal.dto.AirPurifierStateDTO;
import org.openhab.binding.electroluxappliances.internal.dto.ApplianceDTO;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ElectroluxAirPurifierHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jan Gustafsson - Initial contribution
 */
@NonNullByDefault
public class ElectroluxAirPurifierHandler extends ElectroluxAppliancesHandler {

    private final Logger logger = LoggerFactory.getLogger(ElectroluxAirPurifierHandler.class);

    private ElectroluxAppliancesConfiguration config = new ElectroluxAppliancesConfiguration();

    public ElectroluxAirPurifierHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Command received: {} on channelID: {}", command, channelUID);
        if (CHANNEL_STATUS.equals(channelUID.getId()) || command instanceof RefreshType) {
            super.handleCommand(channelUID, command);
        } else {
            ApplianceDTO dto = getApplianceDTO();
            ElectroluxGroupAPI api = getElectroluxGroupAPI();
            if (api != null && dto != null) {
                if (CHANNEL_WORK_MODE.equals(channelUID.getId())) {
                    if (command.toString().equals(COMMAND_WORKMODE_POWEROFF)) {
                        api.workModePowerOff(dto.getApplianceId());
                    } else if (command.toString().equals(COMMAND_WORKMODE_AUTO)) {
                        api.workModeAuto(dto.getApplianceId());
                    } else if (command.toString().equals(COMMAND_WORKMODE_MANUAL)) {
                        api.workModeManual(dto.getApplianceId());
                    }
                } else if (CHANNEL_FAN_SPEED.equals(channelUID.getId())) {
                    api.setFanSpeedLevel(dto.getApplianceId(), Integer.parseInt(command.toString()));
                } else if (CHANNEL_IONIZER.equals(channelUID.getId())) {
                    if (command == OnOffType.OFF) {
                        api.setIonizer(dto.getApplianceId(), "false");
                    } else if (command == OnOffType.ON) {
                        api.setIonizer(dto.getApplianceId(), "true");
                    } else {
                        logger.debug("Unknown command! {}", command);
                    }
                } else if (CHANNEL_UI_LIGHT.equals(channelUID.getId())) {
                    if (command == OnOffType.OFF) {
                        api.setUILight(dto.getApplianceId(), "false");
                    } else if (command == OnOffType.ON) {
                        api.setUILight(dto.getApplianceId(), "true");
                    } else {
                        logger.debug("Unknown command! {}", command);
                    }
                } else if (CHANNEL_SAFETY_LOCK.equals(channelUID.getId())) {
                    if (command == OnOffType.OFF) {
                        api.setSafetyLock(dto.getApplianceId(), "false");
                    } else if (command == OnOffType.ON) {
                        api.setSafetyLock(dto.getApplianceId(), "true");
                    } else {
                        logger.debug("Unknown command! {}", command);
                    }
                }

                Bridge bridge = getBridge();
                if (bridge != null) {
                    BridgeHandler bridgeHandler = bridge.getHandler();
                    if (bridgeHandler != null) {
                        bridgeHandler.handleCommand(new ChannelUID(this.thing.getUID(),
                                ElectroluxAppliancesBindingConstants.CHANNEL_STATUS), RefreshType.REFRESH);
                    }
                }
            }
        }
    }

    @Override
    public void update(@Nullable ApplianceDTO dto) {
        if (dto != null) {
            // Update all channels from the updated data
            getThing().getChannels().stream().map(Channel::getUID).filter(channelUID -> isLinked(channelUID))
                    .forEach(channelUID -> {
                        State state = getValue(channelUID.getId(), dto);
                        logger.trace("Channel: {}, State: {}", channelUID, state);
                        updateState(channelUID, state);
                    });
            updateStatus(ThingStatus.ONLINE);
        }
    }

    private State getValue(String channelId, ApplianceDTO dto) {
        switch (channelId) {
            case CHANNEL_TEMPERATURE:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getTemp(),
                        SIUnits.CELSIUS);
            case CHANNEL_HUMIDITY:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getHumidity(),
                        Units.PERCENT);
            case CHANNEL_TVOC:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getTvoc(),
                        Units.PARTS_PER_BILLION);
            case CHANNEL_PM1:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getPm1(),
                        Units.MICROGRAM_PER_CUBICMETRE);
            case CHANNEL_PM25:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getPm25(),
                        Units.MICROGRAM_PER_CUBICMETRE);
            case CHANNEL_PM10:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getPm10(),
                        Units.MICROGRAM_PER_CUBICMETRE);
            case CHANNEL_CO2:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getCo2(),
                        Units.PARTS_PER_MILLION);
            case CHANNEL_FAN_SPEED:
                return new StringType(Integer.toString(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getFanspeed()));
            case CHANNEL_FILTER_LIFE:
                return new QuantityType<>(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getFilterLife(),
                        Units.PERCENT);
            case CHANNEL_IONIZER:
                return OnOffType.from(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().isIonizer());
            case CHANNEL_UI_LIGHT:
                return OnOffType.from(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().isUiLight());
            case CHANNEL_SAFETY_LOCK:
                return OnOffType.from(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().isSafetyLock());
            case CHANNEL_WORK_MODE:
                return new StringType(
                        ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().getWorkmode());
            case CHANNEL_DOOR_OPEN:
                return ((AirPurifierStateDTO) dto.getApplianceState()).getProperties().getReported().isDoorOpen()
                        ? OpenClosedType.OPEN
                        : OpenClosedType.CLOSED;
            case CONNECTION_STATE:
                return "Connected".equals(((AirPurifierStateDTO) dto.getApplianceState()).getConnectionState())
                        ? OnOffType.from(true)
                        : OnOffType.from(false);
        }
        return UnDefType.UNDEF;
    }

    @Override
    public Map<String, String> refreshProperties() {
        Map<String, String> properties = new HashMap<>();
        Bridge bridge = getBridge();
        if (bridge != null) {
            ElectroluxAppliancesBridgeHandler bridgeHandler = (ElectroluxAppliancesBridgeHandler) bridge.getHandler();
            if (bridgeHandler != null) {
                ApplianceDTO dto = bridgeHandler.getElectroluxAppliancesThings().get(config.getSerialNumber());
                if (dto != null) {
                    properties.put(Thing.PROPERTY_VENDOR, dto.getApplianceInfo().getApplianceInfo().getBrand());
                    properties.put(PROPERTY_COLOUR, dto.getApplianceInfo().getApplianceInfo().getColour());
                    properties.put(PROPERTY_DEVICE, dto.getApplianceInfo().getApplianceInfo().getDeviceType());
                    properties.put(Thing.PROPERTY_MODEL_ID, dto.getApplianceInfo().getApplianceInfo().getModel());
                    properties.put(Thing.PROPERTY_SERIAL_NUMBER,
                            dto.getApplianceInfo().getApplianceInfo().getSerialNumber());
                    properties.put(Thing.PROPERTY_FIRMWARE_VERSION, ((AirPurifierStateDTO) dto.getApplianceState())
                            .getProperties().getReported().getFrmVerNIU());

                }
            }
        }
        return properties;
    }
}
