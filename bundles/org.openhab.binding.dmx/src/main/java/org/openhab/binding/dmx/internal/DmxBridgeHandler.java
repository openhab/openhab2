/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.dmx.internal;

import static org.openhab.binding.dmx.internal.DmxBindingConstants.CHANNEL_MUTE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.dmx.action.DmxActions;
import org.openhab.binding.dmx.internal.action.FadeAction;
import org.openhab.binding.dmx.internal.action.ResumeAction;
import org.openhab.binding.dmx.internal.config.DmxBridgeHandlerConfiguration;
import org.openhab.binding.dmx.internal.multiverse.BaseDmxChannel;
import org.openhab.binding.dmx.internal.multiverse.DmxChannel;
import org.openhab.binding.dmx.internal.multiverse.Universe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DmxBridgeHandler} is an abstract class with base functions
 * for DMX Bridges
 *
 * @author Jan N. Klug - Initial contribution
 */

public abstract class DmxBridgeHandler extends BaseBridgeHandler {
    public static final int DEFAULT_REFRESH_RATE = 20;

    private final Logger logger = LoggerFactory.getLogger(DmxBridgeHandler.class);

    protected Universe universe;

    private ScheduledFuture<?> senderJob;
    private boolean isMuted = false;
    private int refreshTime = 1000 / DEFAULT_REFRESH_RATE;

    public DmxBridgeHandler(Bridge dmxBridge) {
        super(dmxBridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        switch (channelUID.getId()) {
            case CHANNEL_MUTE:
                if (command instanceof OnOffType) {
                    isMuted = ((OnOffType) command).equals(OnOffType.ON);
                } else {
                    logger.debug("command {} not supported in channel {}:mute", command.getClass(),
                            this.thing.getUID());
                }
                break;
            default:
                logger.warn("Channel {} not supported in bridge {}", channelUID.getId(), this.thing.getUID());
        }
    }

    /**
     * get a DMX channel from the bridge
     *
     * @param channel a BaseChannel that identifies the requested channel
     * @param thing the Thing that requests the channel to track channel usage
     * @return a Channel object
     */
    public DmxChannel getDmxChannel(BaseDmxChannel channel, Thing thing) {
        return universe.registerChannel(channel, thing);
    }

    /**
     * remove a thing from all channels in the universe
     *
     * @param thing the thing that shall be removed
     */
    public void unregisterDmxChannels(Thing thing) {
        universe.unregisterChannels(thing);
    }

    /**
     * get the universe associated with this bridge
     *
     * @return the DMX universe id
     */
    public int getUniverseId() {
        return universe.getUniverseId();
    }

    /**
     * rename the universe associated with this bridge
     *
     * @param universeId the new DMX universe id
     */
    protected void renameUniverse(int universeId) {
        universe.rename(universeId);
    }

    @Override
    public void thingUpdated(Thing thing) {
        updateConfiguration();
    }

    /**
     * open the connection to send DMX data to
     */
    protected abstract void openConnection();

    /**
     * close the connection to send DMX data to
     */
    protected abstract void closeConnection();

    /**
     * close the connection to send DMX data and update thing Status
     *
     * @param statusDetail ThingStatusDetail for thingStatus OFFLINE
     * @param description string giving the reason for closing the connection
     */
    protected void closeConnection(ThingStatusDetail statusDetail, String description) {
        updateStatus(ThingStatus.OFFLINE, statusDetail, description);
        closeConnection();
    }

    /**
     * send the buffer of the current universe
     */
    protected abstract void sendDmxData();

    /**
     * install the sending and updating scheduler
     */
    protected void installScheduler() {
        if (senderJob != null) {
            uninstallScheduler();
        }
        if (refreshTime > 0) {
            senderJob = scheduler.scheduleAtFixedRate(() -> {
                logger.trace("runnable packet sender for universe {} called, state {}/{}", universe.getUniverseId(),
                        getThing().getStatus(), isMuted);
                if (!isMuted) {
                    sendDmxData();
                } else {
                    logger.trace("bridge {} is muted", getThing().getUID());
                }
            }, 1, refreshTime, TimeUnit.MILLISECONDS);
            logger.trace("started scheduler for thing {}", this.thing.getUID());
        } else {
            logger.info("refresh disabled for thing {}", this.thing.getUID());
        }
    }

    /**
     * uninstall the sending and updating scheduler
     */
    protected void uninstallScheduler() {
        if (senderJob != null) {
            if (!senderJob.isCancelled()) {
                senderJob.cancel(true);
            }
            senderJob = null;
            closeConnection();
            logger.trace("stopping scheduler for thing {}", this.thing.getUID());
        }
    }

    @Override
    public void childHandlerDisposed(ThingHandler thingHandler, Thing thing) {
        universe.unregisterChannels(thing);
    }

    /**
     * get the configuration and update the bridge
     */
    protected void updateConfiguration() {
        DmxBridgeHandlerConfiguration configuration = getConfig().as(DmxBridgeHandlerConfiguration.class);

        if (!configuration.applycurve.isEmpty()) {
            universe.setDimCurveChannels(configuration.applycurve);
        }

        int refreshRate = configuration.refreshrate;
        if (refreshRate > 0) {
            refreshTime = (int) (1000.0 / refreshRate);
        } else {
            refreshTime = 0;
        }

        logger.debug("set refreshTime to {} ms in thing {}", refreshTime, this.thing.getUID());

        installScheduler();
    }

    @Override
    public void dispose() {
        uninstallScheduler();
    }

    /**
     * set the universe id and make sure it observes the limits
     *
     * @param universeConfig ConfigurationObject
     * @param minUniverseId the minimum id allowed by the bridge
     * @param maxUniverseId the maximum id allowed by the bridge
     **/
    protected void setUniverse(int universeConfig, int minUniverseId, int maxUniverseId) {
        int universeId = minUniverseId;
        universeId = Util.coerceToRange(universeConfig, minUniverseId, maxUniverseId, logger, "universeId");

        if (universe == null) {
            universe = new Universe(universeId);
        } else if (universe.getUniverseId() != universeId) {
            universe.rename(universeId);
        }
    }

    /**
     * sends an immediate fade to the DMX output (for rule actions)
     *
     * @param channelString a String containing the channels
     * @param fadeString a String containing the fade/chase definition
     * @param resumeAfter a boolean if the previous state should be restored
     */
    public void immediateFade(String channelString, String fadeString, Boolean resumeAfter) {
        // parse channel config
        List<DmxChannel> channels = new ArrayList<>();
        try {
            List<BaseDmxChannel> configChannels = BaseDmxChannel.fromString(channelString, getUniverseId());
            logger.trace("found {} channels in {}", configChannels.size(), this.thing.getUID());
            for (BaseDmxChannel channel : configChannels) {
                channels.add(getDmxChannel(channel, this.thing));
            }
        } catch (IllegalArgumentException e) {
            logger.warn("invalid channel configuration: {}", channelString);
            return;
        }

        // parse fade config
        List<ValueSet> value = ValueSet.parseChaseConfig(fadeString);
        if (value.isEmpty()) {
            logger.warn("invalid fade configuration: {}", fadeString);
            return;
        }

        // do action
        Integer channelCounter = 0;
        for (DmxChannel channel : channels) {
            if (resumeAfter) {
                channel.suspendAction();
            } else {
                channel.clearAction();
            }
            for (ValueSet step : value) {
                channel.addChannelAction(
                        new FadeAction(step.getFadeTime(), step.getValue(channelCounter), step.getHoldTime()));
            }
            if (resumeAfter) {
                channel.addChannelAction(new ResumeAction());
            }
            channelCounter++;
        }
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singletonList(DmxActions.class);
    }
}
