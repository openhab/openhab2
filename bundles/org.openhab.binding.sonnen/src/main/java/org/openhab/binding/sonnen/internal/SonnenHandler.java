/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.sonnen.internal;

import static org.openhab.binding.sonnen.internal.SonnenBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Power;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.sonnen.internal.communication.SonnenJSONCommunication;
import org.openhab.binding.sonnen.internal.communication.SonnenJsonDataDTO;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SonnenHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Christian Feininger - Initial contribution
 */
@NonNullByDefault
public class SonnenHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(SonnenHandler.class);

    private SonnenConfiguration config = new SonnenConfiguration();

    private @Nullable ScheduledFuture<?> refreshJob;

    private SonnenJSONCommunication serviceCommunication;

    private boolean automaticRefreshing = false;

    private Map<String, Boolean> linkedChannels = new HashMap<>();

    public SonnenHandler(Thing thing) {
        super(thing);
        serviceCommunication = new SonnenJSONCommunication();
    }

    @Override
    public void initialize() {
        logger.debug("Initializing sonnen handler for thing {}", getThing().getUID());
        config = getConfigAs(SonnenConfiguration.class);
        boolean validConfig = true;
        String statusDescr = null;
        if (config.refreshInterval < 0 && config.refreshInterval > 1000) {
            statusDescr = "Parameter 'refresh Rate' greater then 0 and less then 1001.";
            validConfig = false;
        }
        if (config.hostIP == null) {
            statusDescr = "IP Address must be configured!";
            validConfig = false;
        }
        updateStatus(ThingStatus.UNKNOWN);

        Helper message = new Helper();
        message.setStatusDescription(statusDescr);
        if (validConfig) {
            serviceCommunication.setConfig(config);
            scheduler.submit(() -> {
                if (updateBatteryData()) {
                    updateLinkedChannels();
                }
            });
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, message.getStatusDesription());
        }
    }

    /**
     * Calls the service to update the battery data
     *
     */
    private boolean updateBatteryData() {
        Helper message = new Helper();
        if (serviceCommunication.refreshBatteryConnection(message, this.getThing().getUID().toString())) {
            updateStatus(ThingStatus.ONLINE);
            return true;
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                    message.getStatusDesription());
            return false;
        }
    }

    private void updateLinkedChannels() {

        for (Channel channel : getThing().getChannels()) {
            verifyLinkedChannel(channel.getUID().getId());
        }

        if (!linkedChannels.isEmpty()) {
            updateBatteryData();
            startAutomaticRefresh();
            automaticRefreshing = true;
        }
    }

    private void verifyLinkedChannel(String channelID) {
        if (isLinked(channelID) && !linkedChannels.containsKey(channelID)) {
            linkedChannels.put(channelID, true);
        }
    }

    @Override
    public void dispose() {
        stopAutomaticRefresh();
        linkedChannels.clear();
    }

    private void stopAutomaticRefresh() {
        ScheduledFuture<?> job = refreshJob;
        if (job != null) {
            job.cancel(true);
        }
        refreshJob = null;
    }

    /**
     * Start the job refreshing the oven status
     */
    private void startAutomaticRefresh() {
        ScheduledFuture<?> job = refreshJob;
        if (job == null || job.isCancelled()) {
            int period = config.refreshInterval;
            refreshJob = scheduler.scheduleWithFixedDelay(this::refreshChannels, 0, period, TimeUnit.SECONDS);
        }
    }

    private void refreshChannels() {
        updateBatteryData();
        for (Channel channel : getThing().getChannels()) {
            updateChannel(channel.getUID().getId());
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        if (!automaticRefreshing) {
            logger.debug("Start automatic refreshing");
            startAutomaticRefresh();
            automaticRefreshing = true;
        }
        verifyLinkedChannel(channelUID.getId());
        updateChannel(channelUID.getId());
    }

    @Override
    public void channelUnlinked(ChannelUID channelUID) {
        linkedChannels.remove(channelUID.getId());
        if (linkedChannels.isEmpty()) {
            automaticRefreshing = false;
            stopAutomaticRefresh();
            logger.debug("Stop automatic refreshing");
        }
    }

    private void updateChannel(String channelId) {
        if (isLinked(channelId)) {
            State state = null;
            SonnenJsonDataDTO data = serviceCommunication.getBatteryData();
            if (data != null) {
                switch (channelId) {
                    case CHANNELBATTERYDISCHARGING:
                        update(OnOffType.from(data.isBatteryDischarging()), channelId);
                        break;
                    case CHANNELBATTERYCHARGING:
                        update(OnOffType.from(data.isBatteryCharging()), channelId);
                        break;
                    case CHANNELCONSUMPTION:
                        state = new QuantityType<Power>(data.getConsumptionHouse(), Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELBATTERYDISPENSE:
                        state = new QuantityType<Power>(data.getbatteryCurrent() > 0 ? data.getbatteryCurrent() : 0,
                                Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELBATTERYFEEDIN:
                        state = new QuantityType<Power>(
                                data.getbatteryCurrent() <= 0 ? (data.getbatteryCurrent() * -1) : 0, Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELGRIDFEEDIN:
                        state = new QuantityType<Power>(data.getGridValue() > 0 ? data.getGridValue() : 0, Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELGRIDRECEIVE:
                        state = new QuantityType<Power>(data.getGridValue() <= 0 ? (data.getGridValue() * -1) : 0,
                                Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELSOLARPRODUCTION:
                        state = new QuantityType<Power>(data.getSolarProduction(), Units.WATT);
                        update(state, channelId);
                        break;
                    case CHANNELBATTERYLEVEL:
                        state = new QuantityType<Dimensionless>(data.getBatteryChargingLevel(), Units.PERCENT);
                        update(state, channelId);
                        break;
                    case CHANNELFLOWCONSUMPTIONBATTERY:
                        update(OnOffType.from(data.isFlowConsumptionBattery()), channelId);
                        break;
                    case CHANNELFLOWCONSUMPTIONGRID:
                        update(OnOffType.from(data.isFlowConsumptionGrid()), channelId);
                        break;
                    case CHANNELFLOWCONSUMPTIONPRODUCTION:
                        update(OnOffType.from(data.isFlowConsumptionProduction()), channelId);
                        break;
                    case CHANNELFLOWGRIDBATTERY:
                        update(OnOffType.from(data.isFlowGridBattery()), channelId);
                        break;
                    case CHANNELFLOWPRODUCTIONBATTERY:
                        update(OnOffType.from(data.isFlowProductionBattery()), channelId);
                        break;
                    case CHANNELFLOWPRODUCTIONGRID:
                        update(OnOffType.from(data.isFlowProductionGrid()), channelId);
                        break;
                }
            }
        }
    }

    /**
     * Updates the State of the given channel
     *
     * @param state Given state
     * @param channelId the refereed channelID
     */
    private void update(@Nullable State state, String channelId) {
        logger.debug("Update channel {} with state {}", channelId, (state == null) ? "null" : state.toString());

        if (state != null) {
            updateState(channelId, state);

        } else {
            updateState(channelId, UnDefType.UNDEF);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }
}
