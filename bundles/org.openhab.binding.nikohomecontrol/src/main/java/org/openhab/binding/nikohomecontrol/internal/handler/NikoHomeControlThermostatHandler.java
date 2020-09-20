/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.nikohomecontrol.internal.handler;

import static org.openhab.binding.nikohomecontrol.internal.NikoHomeControlBindingConstants.*;
import static org.openhab.core.library.unit.SIUnits.CELSIUS;
import static org.openhab.core.types.RefreshType.REFRESH;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.nikohomecontrol.internal.protocol.NhcThermostat;
import org.openhab.binding.nikohomecontrol.internal.protocol.NhcThermostatEvent;
import org.openhab.binding.nikohomecontrol.internal.protocol.NikoHomeControlCommunication;
import org.openhab.binding.nikohomecontrol.internal.protocol.nhc2.NhcThermostat2;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link NikoHomeControlThermostatHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Mark Herwege - Initial Contribution
 */
@NonNullByDefault
public class NikoHomeControlThermostatHandler extends BaseThingHandler implements NhcThermostatEvent {

    private final Logger logger = LoggerFactory.getLogger(NikoHomeControlThermostatHandler.class);

    private volatile @NonNullByDefault({}) NhcThermostat nhcThermostat;

    private String thermostatId = "";
    private int overruleTime;

    private volatile @Nullable ScheduledFuture<?> refreshTimer; // used to refresh the remaining overrule time every
                                                                // minute

    public NikoHomeControlThermostatHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        NikoHomeControlCommunication nhcComm = getCommunication();
        if (nhcComm == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Niko Home Control: bridge communication not initialized when trying to execute thermostat command "
                            + thermostatId);
            return;
        }

        // This can be expensive, therefore do it in a job.
        scheduler.submit(() -> {
            if (!nhcComm.communicationActive()) {
                restartCommunication(nhcComm);
            }

            if (nhcComm.communicationActive()) {
                handleCommandSelection(channelUID, command);
            }
        });
    }

    private void handleCommandSelection(ChannelUID channelUID, Command command) {
        logger.debug("Niko Home Control: handle command {} for {}", command, channelUID);

        if (REFRESH.equals(command)) {
            thermostatEvent(nhcThermostat.getMeasured(), nhcThermostat.getSetpoint(), nhcThermostat.getMode(),
                    nhcThermostat.getOverrule(), nhcThermostat.getDemand());
            return;
        }

        switch (channelUID.getId()) {
            case CHANNEL_MEASURED:
            case CHANNEL_DEMAND:
                updateStatus(ThingStatus.ONLINE);
                break;

            case CHANNEL_MODE:
                if (command instanceof DecimalType) {
                    nhcThermostat.executeMode(((DecimalType) command).intValue());
                }
                updateStatus(ThingStatus.ONLINE);
                break;

            case CHANNEL_SETPOINT:
                QuantityType<Temperature> setpoint = null;
                if (command instanceof QuantityType) {
                    setpoint = ((QuantityType<Temperature>) command).toUnit(CELSIUS);
                    // Always set the new setpoint temperature as an overrule
                    // If no overrule time is given yet, set the overrule time to the configuration parameter
                    int time = nhcThermostat.getOverruletime();
                    if (time <= 0) {
                        time = overruleTime;
                    }
                    if (setpoint != null) {
                        nhcThermostat.executeOverrule(Math.round(setpoint.floatValue() * 10), time);
                    }
                }
                updateStatus(ThingStatus.ONLINE);
                break;

            case CHANNEL_OVERRULETIME:
                if (command instanceof DecimalType) {
                    int overruletime = ((DecimalType) command).intValue();
                    int overrule = nhcThermostat.getOverrule();
                    if (overruletime <= 0) {
                        overruletime = 0;
                        overrule = 0;
                    }
                    nhcThermostat.executeOverrule(overrule, overruletime);
                }
                updateStatus(ThingStatus.ONLINE);
                break;

            default:
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "Niko Home Control: channel unknown " + channelUID.getId());
        }
    }

    @Override
    public void initialize() {
        NikoHomeControlThermostatConfig config = getConfig().as(NikoHomeControlThermostatConfig.class);

        thermostatId = config.thermostatId;
        overruleTime = config.overruleTime;

        NikoHomeControlCommunication nhcComm = getCommunication();
        if (nhcComm == null) {
            return;
        }

        // We need to do this in a separate thread because we may have to wait for the
        // communication to become active
        scheduler.submit(() -> {
            if (!nhcComm.communicationActive()) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "Niko Home Control: no connection with Niko Home Control, could not initialize thermostat "
                                + thermostatId);
                return;
            }

            nhcThermostat = nhcComm.getThermostats().get(thermostatId);
            if (nhcThermostat == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Niko Home Control: thermostatId does not match a thermostat in the controller "
                                + thermostatId);
                return;
            }

            nhcThermostat.setEventHandler(this);

            updateProperties();

            String thermostatLocation = nhcThermostat.getLocation();
            if (thing.getLocation() == null) {
                thing.setLocation(thermostatLocation);
            }

            thermostatEvent(nhcThermostat.getMeasured(), nhcThermostat.getSetpoint(), nhcThermostat.getMode(),
                    nhcThermostat.getOverrule(), nhcThermostat.getDemand());

            logger.debug("Niko Home Control: thermostat intialized {}", thermostatId);

            Bridge bridge = getBridge();
            if ((bridge != null) && (bridge.getStatus() == ThingStatus.ONLINE)) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
            }
        });
    }

    private void updateProperties() {
        Map<String, String> properties = new HashMap<>();

        if (nhcThermostat instanceof NhcThermostat2) {
            NhcThermostat2 thermostat = (NhcThermostat2) nhcThermostat;
            properties.put("model", thermostat.getModel());
            properties.put("technology", thermostat.getTechnology());
        }

        thing.setProperties(properties);
    }

    @Override
    public void thermostatEvent(int measured, int setpoint, int mode, int overrule, int demand) {
        updateState(CHANNEL_MEASURED, new QuantityType<>(nhcThermostat.getMeasured() / 10.0, CELSIUS));

        int overruletime = nhcThermostat.getRemainingOverruletime();
        updateState(CHANNEL_OVERRULETIME, new DecimalType(overruletime));
        // refresh the remaining time every minute
        scheduleRefreshOverruletime(nhcThermostat);

        // If there is an overrule temperature set, use this in the setpoint channel, otherwise use the original
        // setpoint temperature
        if (overruletime == 0) {
            updateState(CHANNEL_SETPOINT, new QuantityType<>(setpoint / 10.0, CELSIUS));
        } else {
            updateState(CHANNEL_SETPOINT, new QuantityType<>(overrule / 10.0, CELSIUS));
        }

        updateState(CHANNEL_MODE, new DecimalType(mode));

        updateState(CHANNEL_DEMAND, new DecimalType(demand));

        updateStatus(ThingStatus.ONLINE);
    }

    /**
     * Method to update state of overruletime channel every minute with remaining time.
     *
     * @param NhcThermostat object
     *
     */
    private void scheduleRefreshOverruletime(NhcThermostat nhcThermostat) {
        cancelRefreshTimer();

        if (nhcThermostat.getRemainingOverruletime() <= 0) {
            return;
        }

        refreshTimer = scheduler.scheduleWithFixedDelay(() -> {
            int remainingTime = nhcThermostat.getRemainingOverruletime();
            updateState(CHANNEL_OVERRULETIME, new DecimalType(remainingTime));
            if (remainingTime <= 0) {
                cancelRefreshTimer();
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private void cancelRefreshTimer() {
        ScheduledFuture<?> timer = refreshTimer;
        if (timer != null) {
            timer.cancel(true);
        }
        refreshTimer = null;
    }

    @Override
    public void thermostatRemoved() {
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                "Niko Home Control: thermostat has been removed from the controller " + thermostatId);
    }

    private void restartCommunication(NikoHomeControlCommunication nhcComm) {
        // We lost connection but the connection object is there, so was correctly started.
        // Try to restart communication.
        nhcComm.restartCommunication();
        // If still not active, take thing offline and return.
        if (!nhcComm.communicationActive()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Niko Home Control: communication socket error");
            return;
        }
        // Also put the bridge back online
        NikoHomeControlBridgeHandler nhcBridgeHandler = getBridgeHandler();
        if (nhcBridgeHandler != null) {
            nhcBridgeHandler.bridgeOnline();
        }
    }

    private @Nullable NikoHomeControlCommunication getCommunication() {
        NikoHomeControlBridgeHandler nhcBridgeHandler = getBridgeHandler();
        if (nhcBridgeHandler == null) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                    "Niko Home Control: no bridge initialized for thermostat " + thermostatId);
            return null;
        }
        NikoHomeControlCommunication nhcComm = nhcBridgeHandler.getCommunication();
        return nhcComm;
    }

    private @Nullable NikoHomeControlBridgeHandler getBridgeHandler() {
        Bridge nhcBridge = getBridge();
        if (nhcBridge == null) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                    "Niko Home Control: no bridge initialized for thermostat " + thermostatId);
            return null;
        }
        NikoHomeControlBridgeHandler nhcBridgeHandler = (NikoHomeControlBridgeHandler) nhcBridge.getHandler();
        return nhcBridgeHandler;
    }
}
