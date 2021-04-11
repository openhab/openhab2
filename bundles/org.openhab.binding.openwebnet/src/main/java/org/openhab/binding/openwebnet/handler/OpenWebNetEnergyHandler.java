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
package org.openhab.binding.openwebnet.handler;

import static org.openhab.binding.openwebnet.OpenWebNetBindingConstants.CHANNEL_POWER;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Power;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openwebnet.OpenWebNetBindingConstants;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.UnDefType;
import org.openwebnet4j.communication.OWNException;
import org.openwebnet4j.message.BaseOpenMessage;
import org.openwebnet4j.message.EnergyManagement;
import org.openwebnet4j.message.FrameException;
import org.openwebnet4j.message.Where;
import org.openwebnet4j.message.WhereEnergyManagement;
import org.openwebnet4j.message.Who;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenWebNetEnergyHandler} is responsible for handling commands/messages for a Energy Management OpenWebNet
 * device. It extends the abstract {@link OpenWebNetThingHandler}.
 *
 * @author Massimo Valla - Initial contribution
 * @author Andrea Conte - Energy management
 */
@NonNullByDefault
public class OpenWebNetEnergyHandler extends OpenWebNetThingHandler {

    private final Logger logger = LoggerFactory.getLogger(OpenWebNetEnergyHandler.class);

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = OpenWebNetBindingConstants.ENERGY_MANAGEMENT_SUPPORTED_THING_TYPES;

    // fix issue #10509
    public final int ENERGY_SUBSCRIPTION_PERIOD = 10; // minutes
    private @Nullable ScheduledFuture<?> notificationSchedule;
    public Boolean isFirstSchedulerLaunch = true;

    public OpenWebNetEnergyHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        super.bridgeStatusChanged(bridgeStatusInfo);

        // subscribe the scheduler only after the bridge is online
        if (bridgeStatusInfo.getStatus().equals(ThingStatus.ONLINE)) {
            notificationSchedule = scheduler.scheduleAtFixedRate(() -> {
                if (isFirstSchedulerLaunch) {
                    logger.debug(
                            "bridgeStatusChanged() For WHERE={} subscribing to active power changes notification for the next {}min",
                            deviceWhere, ENERGY_SUBSCRIPTION_PERIOD);
                } else {
                    logger.debug(
                            "bridgeStatusChanged() Refreshing subscription for the next {}min for WHERE={} to active power changes notification",
                            ENERGY_SUBSCRIPTION_PERIOD, deviceWhere);
                }

                try {
                    bridgeHandler.gateway.send(EnergyManagement.setActivePowerNotificationsTime(deviceWhere.value(),
                            ENERGY_SUBSCRIPTION_PERIOD));
                    isFirstSchedulerLaunch = false;
                } catch (Exception e) {
                    if (isFirstSchedulerLaunch) {
                        logger.warn(
                                "bridgeStatusChanged() For WHERE={} could not subscribe to active power changes notifications. Exception={}",
                                deviceWhere, e.getMessage());
                    } else {
                        logger.warn(
                                "bridgeStatusChanged() Unable to refresh subscription to active power changes notifications for WHERE={}. Exception={}",
                                deviceWhere, e.getMessage());
                    }
                }
            }, 0, ENERGY_SUBSCRIPTION_PERIOD - 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void dispose() {
        if (notificationSchedule != null) {
            notificationSchedule.cancel(false);
        }
        super.dispose();
        scheduler.schedule(() -> {
            try {
                // switch off active power updates
                bridgeHandler.gateway.send(EnergyManagement.setActivePowerNotificationsTime(deviceWhere.value(), 0));
            } catch (Exception e) {
                logger.warn(
                        "dispose() For WHERE={} could not UN-subscribe from active power changes notifications. Exception={}",
                        deviceWhere, e.getMessage());
            }
        }, 1, TimeUnit.MINUTES);
    }

    @Override
    protected Where buildBusWhere(String wStr) throws IllegalArgumentException {
        return new WhereEnergyManagement(wStr);
    }

    @Override
    protected void requestChannelState(ChannelUID channel) {
        logger.debug("requestChannelState() thingUID={} channel={}", thing.getUID(), channel.getId());
        try {
            bridgeHandler.gateway.send(EnergyManagement.requestActivePower(deviceWhere.value()));
        } catch (OWNException e) {
            logger.warn("requestChannelState() OWNException thingUID={} channel={}: {}", thing.getUID(),
                    channel.getId(), e.getMessage());
        }
    }

    @Override
    protected void handleChannelCommand(ChannelUID channel, Command command) {
        logger.warn("handleChannelCommand() Read only channel, unsupported command {}", command);
    }

    @Override
    protected String ownIdPrefix() {
        return Who.ENERGY_MANAGEMENT.value().toString();
    }

    @Override
    protected void handleMessage(BaseOpenMessage msg) {
        super.handleMessage(msg);

        if (msg.isCommand()) {
            logger.warn("handleMessage() Ignoring unsupported command for thing {}. Frame={}", getThing().getUID(),
                    msg);
            return;
        } else {
            updateActivePower(msg);
        }
    }

    /**
     * Updates energy power state based on a EnergyManagement message received from the OWN network
     *
     * @param msg the EnergyManagement message received
     * @throws FrameException
     */
    private void updateActivePower(BaseOpenMessage msg) {
        Integer activePower;
        try {
            activePower = Integer.parseInt(msg.getDimValues()[0]);
            updateState(CHANNEL_POWER, new QuantityType<Power>(activePower, Units.WATT));
        } catch (FrameException e) {
            logger.warn("FrameException on frame {}: {}", msg, e.getMessage());
            updateState(CHANNEL_POWER, UnDefType.UNDEF);
        }
    }
}
