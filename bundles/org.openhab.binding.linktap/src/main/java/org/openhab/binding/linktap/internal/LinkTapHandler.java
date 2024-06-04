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
package org.openhab.binding.linktap.internal;

import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.BRIDGE_PROP_VOL_UNIT;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.ChildLockMode;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_ACTIVE_WATERING;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_BATTERY;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_CHILD_LOCK;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_CURRENT_VOLUME;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FAILSAFE_DURATION;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FAILSAFE_VOLUME;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FALL_STATUS;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FINAL_SEGMENT;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FLM_LINKED;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_FLOW_RATE;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_HIGH_FLOW;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_IS_MANUAL_MODE;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_LOW_FLOW;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_OH_DURATION_LIMIT;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_OH_VOLUME_LIMIT;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_REMAIN_DURATION;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_RF_LINKED;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_SHUTDOWN_FAILURE;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_SIGNAL;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_TOTAL_DURATION;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_WATERING_MODE;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_WATER_CUT;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_WSKIP_DATE_TIME;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_WSKIP_NEXT_RAIN;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.DEVICE_CHANNEL_WSKIP_PREV_RAIN;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.GSON;
import static org.openhab.binding.linktap.internal.LinkTapBindingConstants.WateringMode;
import static org.openhab.binding.linktap.protocol.frames.DismissAlertReq.ALERT_DEVICE_FALL;
import static org.openhab.binding.linktap.protocol.frames.DismissAlertReq.ALERT_UNEXPECTED_HIGH_FLOW;
import static org.openhab.binding.linktap.protocol.frames.DismissAlertReq.ALERT_UNEXPECTED_LOW_FLOW;
import static org.openhab.binding.linktap.protocol.frames.DismissAlertReq.ALERT_VALVE_SHUTDOWN_FAIL;
import static org.openhab.binding.linktap.protocol.frames.DismissAlertReq.ALERT_WATER_CUTOFF;
import static org.openhab.binding.linktap.protocol.frames.SetDeviceConfigReq.CONFIG_DURATION_LIMIT;
import static org.openhab.binding.linktap.protocol.frames.SetDeviceConfigReq.CONFIG_VOLUME_LIMIT;
import static org.openhab.binding.linktap.protocol.frames.TLGatewayFrame.CMD_DATETIME_SYNC;
import static org.openhab.binding.linktap.protocol.frames.TLGatewayFrame.CMD_IMMEDIATE_WATER_STOP;
import static org.openhab.binding.linktap.protocol.frames.TLGatewayFrame.CMD_NOTIFICATION_WATERING_SKIPPED;
import static org.openhab.binding.linktap.protocol.frames.TLGatewayFrame.CMD_RAINFALL_DATA;
import static org.openhab.binding.linktap.protocol.frames.TLGatewayFrame.CMD_UPDATE_WATER_TIMER_STATUS;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.linktap.protocol.frames.AlertStateReq;
import org.openhab.binding.linktap.protocol.frames.DeviceCmdReq;
import org.openhab.binding.linktap.protocol.frames.DismissAlertReq;
import org.openhab.binding.linktap.protocol.frames.LockReq;
import org.openhab.binding.linktap.protocol.frames.SetDeviceConfigReq;
import org.openhab.binding.linktap.protocol.frames.StartWateringReq;
import org.openhab.binding.linktap.protocol.frames.WaterMeterStatus;
import org.openhab.binding.linktap.protocol.frames.WateringSkippedNotification;
import org.openhab.binding.linktap.protocol.http.InvalidParameterException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.storage.Storage;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LinkTapHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author David Goodyear - Initial contribution
 */
@NonNullByDefault
public class LinkTapHandler extends PollingDeviceHandler {

    private final Logger logger = LoggerFactory.getLogger(LinkTapHandler.class);

    private static final String DEFAULT_INST_WATERING_VOL_LIMIT = "0";
    private static final String DEFAULT_INST_WATERING_TIME_LIMIT = "15";

    private final Storage<String> strStore;

    private static final List<String> READBACK_DISABLED_CHANNELS = List.of(DEVICE_CHANNEL_OH_VOLUME_LIMIT,
            DEVICE_CHANNEL_OH_DURATION_LIMIT);

    public LinkTapHandler(Thing thing, Storage<String> strStore) {
        super(thing);
        this.strStore = strStore;
    }

    /**
     * Abstract method implementations from PollingDeviceHandler
     * required for the lifecycle of LinkTap devices.
     */

    @Override
    protected void runStartInit() {
        try {
            final LinkTapDeviceConfiguration config = getConfigAs(LinkTapDeviceConfiguration.class);
            if (config.enableAlerts) {
                sendRequest(new AlertStateReq(0, true));
            }
        } catch (final InvalidParameterException ipe) {
            logger.warn("Failed to enable all alerts");
        }
    }

    @Override
    protected void registerDevice() {
        LinkTapBridgeHandler.DEV_ID_LOOKUP.registerItem(registeredDeviceId, this, () -> {
        });
    }

    @Override
    protected void deregisterDevice() {
        LinkTapBridgeHandler.DEV_ID_LOOKUP.deregisterItem(registeredDeviceId, this, () -> {
        });
    }

    @Override
    protected String getPollResponseData() {
        try {
            return sendRequest(new DeviceCmdReq(CMD_UPDATE_WATER_TIMER_STATUS));
        } catch (final InvalidParameterException ipe) {
            logger.warn("Poll failure - invalid parameter");
            return "";
        }
    }

    @Override
    protected void processPollResponseData(String data) {
        processCommand3(data);
    }

    /**
     * OpenHab handlers
     */

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command command) {
        scheduler.submit(() -> {
            try {
                if (command instanceof RefreshType rt) {
                    switch (channelUID.getId()) {
                        case DEVICE_CHANNEL_OH_DURATION_LIMIT: {
                            final String savedVal = strStore.get(DEVICE_CHANNEL_OH_DURATION_LIMIT);
                            if (savedVal != null) {
                                updateState(DEVICE_CHANNEL_OH_DURATION_LIMIT,
                                        new QuantityType<>(Integer.valueOf(savedVal), Units.SECOND));
                            } else {
                                updateState(DEVICE_CHANNEL_OH_DURATION_LIMIT, new QuantityType<>(15, Units.SECOND));
                            }
                        }
                            break;
                        case DEVICE_CHANNEL_OH_VOLUME_LIMIT: {
                            final String savedVal = strStore.get(DEVICE_CHANNEL_OH_VOLUME_LIMIT);
                            if (savedVal != null) {
                                updateState(DEVICE_CHANNEL_OH_VOLUME_LIMIT,
                                        new QuantityType<>(Integer.valueOf(savedVal), Units.LITRE));
                            } else {
                                updateState(DEVICE_CHANNEL_OH_VOLUME_LIMIT, new QuantityType<>(10, Units.LITRE));
                            }
                        }
                            break;
                        default:
                            pollForUpdate(false);
                    }
                } else if (command instanceof QuantityType quantityCommand) {
                    int targetValue = quantityCommand.intValue();
                    switch (channelUID.getId()) {
                        case DEVICE_CHANNEL_OH_DURATION_LIMIT:
                            strStore.put(DEVICE_CHANNEL_OH_DURATION_LIMIT, String.valueOf(targetValue));
                            break;
                        case DEVICE_CHANNEL_OH_VOLUME_LIMIT:
                            strStore.put(DEVICE_CHANNEL_OH_VOLUME_LIMIT, String.valueOf(targetValue));
                            break;
                        case DEVICE_CHANNEL_FAILSAFE_VOLUME: {
                            sendRequest(new SetDeviceConfigReq(CONFIG_VOLUME_LIMIT, targetValue));
                        }
                            break;
                        case DEVICE_CHANNEL_TOTAL_DURATION: {
                            sendRequest(new SetDeviceConfigReq(CONFIG_DURATION_LIMIT, targetValue));
                        }
                            break;
                    }
                } else if (command instanceof StringType stringCmd) {
                    switch (channelUID.getId()) {
                        case DEVICE_CHANNEL_CHILD_LOCK: {
                            sendRequest(new LockReq(Integer.valueOf(command.toString())));
                        }
                            break;
                    }
                } else if (command instanceof OnOffType) {
                    // Alert dismiss events below
                    switch (channelUID.getId()) {
                        case DEVICE_CHANNEL_IS_MANUAL_MODE:
                            // We can pull from channels I suspect as they feed stateful representations of data
                            // therefore the data we need below - we need to cache from writes and reads from the
                            // relevant
                            // polls / writes, for the items / data points.
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DeviceCmdReq(CMD_IMMEDIATE_WATER_STOP));
                            }
                        case DEVICE_CHANNEL_ACTIVE_WATERING:
                            if (command.equals(OnOffType.ON)) {
                                String volLimit = strStore.get(DEVICE_CHANNEL_OH_VOLUME_LIMIT);
                                if (volLimit == null) {
                                    volLimit = DEFAULT_INST_WATERING_VOL_LIMIT;
                                }
                                String durLimit = strStore.get(DEVICE_CHANNEL_OH_DURATION_LIMIT);
                                if (durLimit == null) {
                                    durLimit = DEFAULT_INST_WATERING_TIME_LIMIT;
                                }
                                sendRequest(
                                        new StartWateringReq(Integer.parseInt(durLimit), Integer.parseInt(volLimit)));
                            } else if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DeviceCmdReq(CMD_IMMEDIATE_WATER_STOP));
                            }
                        case DEVICE_CHANNEL_FALL_STATUS: // 1
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DismissAlertReq(ALERT_DEVICE_FALL));
                            }
                            break;
                        case DEVICE_CHANNEL_SHUTDOWN_FAILURE: // 2
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DismissAlertReq(ALERT_VALVE_SHUTDOWN_FAIL));
                            }
                            break;
                        case DEVICE_CHANNEL_WATER_CUT: // 3
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DismissAlertReq(ALERT_WATER_CUTOFF));
                            }
                            break;
                        case DEVICE_CHANNEL_HIGH_FLOW: // 4
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DismissAlertReq(ALERT_UNEXPECTED_HIGH_FLOW));
                            }
                            break;
                        case DEVICE_CHANNEL_LOW_FLOW: // 5
                            if (command.equals(OnOffType.OFF)) {
                                sendRequest(new DismissAlertReq(ALERT_UNEXPECTED_LOW_FLOW));
                            }
                            break;
                    }
                }
                if (!READBACK_DISABLED_CHANNELS.contains(channelUID.getId())) {
                    requestReadbackPoll();
                }
            } catch (final InvalidParameterException ipe) {
                logger.warn("Parameter not accepted by device {} for command {}", getThing().getLabel(),
                        channelUID.getId());
            }
        });
    }

    /**
     * LinkTap communication protocol handlers
     */

    public void processDeviceCommand(final int commandId, final String frame) {
        receivedDataPush();
        logger.debug("{} processing device request with command {}", this.getThing().getLabel(), commandId);

        switch (commandId) {
            case CMD_UPDATE_WATER_TIMER_STATUS:
                // Store the latest value in the cache - to prevent unnecessary polls
                lastPollResultCache.putValue(frame);
                processCommand3(frame);
                break;
            case CMD_NOTIFICATION_WATERING_SKIPPED:
                processCommand9(frame);
                break;
            case CMD_RAINFALL_DATA:
            case CMD_DATETIME_SYNC:
                logger.trace("No implementation for command {} for processing the Device request", commandId);
        }
    }

    private void processCommand3(final String request) {
        // There are three different formats that can arrive in this method:
        // -> Unsolicited with is a WaterMeterStatus.DeviceStatus payload
        // -> Solicited with a WaterMeterStatus payload (*)
        // -> Solicited with a WaterMeterStatus payload within an array
        // (*) A GSON plugin normalises the non array wrapped version to the array based version
        // This is handled below before the normalised processing takes place.
        WaterMeterStatus.DeviceStatus devStatus;
        {
            WaterMeterStatus mStatus = GSON.fromJson(request, WaterMeterStatus.class);
            if (mStatus == null) {
                return;
            }

            if (!mStatus.deviceStatuses.isEmpty()) {
                devStatus = mStatus.deviceStatuses.get(0);
            } else {
                devStatus = GSON.fromJson(request, WaterMeterStatus.DeviceStatus.class);
            }
            if (devStatus == null) {
                return;
            }
        }

        // Normalized processing below which uses devStatus

        final LinkTapBridgeHandler bridgeHandler = (LinkTapBridgeHandler) getBridgeHandler();
        String volumeUnit = "L";
        if (bridgeHandler != null) {
            String volumeUnitProp = bridgeHandler.getThing().getProperties().get(BRIDGE_PROP_VOL_UNIT);
            if (volumeUnitProp != null) {
                volumeUnit = volumeUnitProp;
            }
        }
        final Integer planModeRaw = devStatus.planMode;
        if (planModeRaw != null) {
            updateState(DEVICE_CHANNEL_WATERING_MODE, new StringType(WateringMode.values()[planModeRaw].getDesc()));
        }

        final Integer childLockRaw = devStatus.childLock;
        if (childLockRaw != null) {
            updateState(DEVICE_CHANNEL_CHILD_LOCK, new StringType(ChildLockMode.values()[childLockRaw].getDesc()));
        }

        updateOnOffValue(DEVICE_CHANNEL_IS_MANUAL_MODE, devStatus.isManualMode);
        updateOnOffValue(DEVICE_CHANNEL_ACTIVE_WATERING, devStatus.isWatering);
        updateOnOffValue(DEVICE_CHANNEL_RF_LINKED, devStatus.isRfLinked);
        updateOnOffValue(DEVICE_CHANNEL_FLM_LINKED, devStatus.isFlmPlugin);
        updateOnOffValue(DEVICE_CHANNEL_FALL_STATUS, devStatus.isFall);
        updateOnOffValue(DEVICE_CHANNEL_SHUTDOWN_FAILURE, devStatus.isBroken);
        updateOnOffValue(DEVICE_CHANNEL_HIGH_FLOW, devStatus.isLeak);
        updateOnOffValue(DEVICE_CHANNEL_LOW_FLOW, devStatus.isClog);
        updateOnOffValue(DEVICE_CHANNEL_FINAL_SEGMENT, devStatus.isFinal);
        updateOnOffValue(DEVICE_CHANNEL_WATER_CUT, devStatus.isCutoff);

        final Integer signal = devStatus.signal;
        if (signal != null) {
            updateState(DEVICE_CHANNEL_SIGNAL, new QuantityType<>(signal, Units.PERCENT));
        }

        final Integer battery = devStatus.battery;
        if (battery != null) {
            updateState(DEVICE_CHANNEL_BATTERY, new QuantityType<>(battery, Units.PERCENT));
        }

        final Integer totalDuration = devStatus.totalDuration;
        if (totalDuration != null) {
            updateState(DEVICE_CHANNEL_TOTAL_DURATION, new QuantityType<>(totalDuration, Units.SECOND));
        }

        final Integer remainDuration = devStatus.remainDuration;
        if (remainDuration != null) {
            updateState(DEVICE_CHANNEL_REMAIN_DURATION, new QuantityType<>(remainDuration, Units.SECOND));
        }

        final Integer failsafeDuration = devStatus.failsafeDuration;
        if (failsafeDuration != null) {
            updateState(DEVICE_CHANNEL_FAILSAFE_DURATION, new QuantityType<>(failsafeDuration, Units.SECOND));
        }

        final Double speed = devStatus.speed;
        if (speed != null) {
            updateState(DEVICE_CHANNEL_FLOW_RATE,
                    new QuantityType<>("L".equals(volumeUnit) ? speed : (speed * 3.785), Units.LITRE_PER_MINUTE));
        }

        final Double volume = devStatus.volume;
        if (volume != null) {
            updateState(DEVICE_CHANNEL_CURRENT_VOLUME,
                    new QuantityType<>("L".equals(volumeUnit) ? volume : (volume * 3.785), Units.LITRE));
        }

        final Double volumeLimit = devStatus.volumeLimit;
        if (volumeLimit != null) {
            updateState(DEVICE_CHANNEL_FAILSAFE_VOLUME,
                    new QuantityType<>("L".equals(volumeUnit) ? volumeLimit : (volumeLimit * 3.785), Units.LITRE));
        }
    }

    private void updateOnOffValue(final String channelName, final @Nullable Boolean value) {
        if (value != null) {
            updateState(channelName, OnOffType.from(value));
        }
    }

    private void processCommand9(final String request) {
        final WateringSkippedNotification skippedNotice = GSON.fromJson(request, WateringSkippedNotification.class);
        if (skippedNotice == null) {
            return;
        }
        logger.trace("Received rainfall skipped notice - past hour {}, next hour {}", skippedNotice.getPastRainfall(),
                skippedNotice.getFutureRainfall());
        updateState(DEVICE_CHANNEL_WSKIP_PREV_RAIN,
                new QuantityType<>(skippedNotice.getPastRainfall(), Units.MILLIMETRE_PER_HOUR));
        updateState(DEVICE_CHANNEL_WSKIP_NEXT_RAIN,
                new QuantityType<>(skippedNotice.getFutureRainfall(), Units.MILLIMETRE_PER_HOUR));
        updateState(DEVICE_CHANNEL_WSKIP_DATE_TIME, new DateTimeType());
    }

    @Override
    public void handleBridgeDataUpdated() {
        switch (getThing().getStatus()) {
            case OFFLINE:
            case UNKNOWN:
                logger.trace("Handling new bridge data for {}", getThing().getLabel());
                final LinkTapBridgeHandler bridge = (LinkTapBridgeHandler) getBridgeHandler();
                if (bridge != null) {
                    initAfterBridge(bridge);
                }
                break;
            default:
                logger.trace("Handling new bridge data for {} not required", getThing().getLabel());
        }
    }
}
