/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.handler;

import static org.openhab.binding.satel.SatelBindingConstants.PROPERTY_REQUIRES_REFRESH;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.satel.internal.command.IntegraStateCommand;
import org.openhab.binding.satel.internal.command.SatelCommand;
import org.openhab.binding.satel.internal.config.SatelThingConfig;
import org.openhab.binding.satel.internal.event.ConnectionStatusEvent;
import org.openhab.binding.satel.internal.event.IntegraStateEvent;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.event.SatelEventListener;
import org.openhab.binding.satel.internal.types.ObjectType;
import org.openhab.binding.satel.internal.types.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SatelThingHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Krzysztof Goworek - Initial contribution
 */
public abstract class SatelThingHandler extends BaseThingHandler implements SatelEventListener {

    private final Logger logger = LoggerFactory.getLogger(SatelThingHandler.class);

    protected SatelThingConfig thingConfig;
    protected SatelBridgeHandler bridgeHandler;
    protected ObjectType thingType;

    public SatelThingHandler(Thing thing, ObjectType thingType) {
        super(thing);
        this.thingType = thingType;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("New command for {}: {}", channelUID, command.toFullString());

        if (bridgeHandler != null && StringUtils.isNotEmpty(bridgeHandler.getUserCode())) {
            SatelCommand satelCommand = convertCommand(channelUID, command);
            if (satelCommand != null) {
                bridgeHandler.sendCommand(satelCommand, true);
            }
        }
    }

    @Override
    public void dispose() {
        if (bridgeHandler != null) {
            bridgeHandler.removeEventListener(this);
        }
    }

    @Override
    public void initialize() {
        thingConfig = getConfig().as(SatelThingConfig.class);

        Bridge bridge = getBridge();
        if (bridge != null) {
            ThingHandler handler = bridge.getHandler();
            if (handler != null && handler instanceof SatelBridgeHandler) {
                bridgeHandler = (SatelBridgeHandler) handler;
                bridgeHandler.addEventListener(this);
            }
        }
    }

    @Override
    public void incomingEvent(SatelEvent event) {
        logger.trace("Handling incoming event: {}", event);
        if (event instanceof ConnectionStatusEvent) {
            ConnectionStatusEvent statusEvent = (ConnectionStatusEvent) event;
            // we have just connected, force refresh state
            if (statusEvent.isConnected()) {
                getThing().setProperty(PROPERTY_REQUIRES_REFRESH, Boolean.toString(true));
            }
        } else if (event instanceof NewStatesEvent) {
            // refresh all states that have changed
            for (SatelCommand command : getRefreshCommands((NewStatesEvent) event)) {
                bridgeHandler.sendCommand(command, true);
            }
            if (getThing().getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
        } else if (event instanceof IntegraStateEvent) {
            // update thing's state unless it should accept commands only
            IntegraStateEvent stateEvent = (IntegraStateEvent) event;
            if (thingConfig.isCommandOnly()) {
                return;
            }
            for (Channel channel : getThing().getChannels()) {
                StateType stateType = getStateType(channel.getUID().getId());
                if (stateType != null && stateEvent.hasDataForState(stateType)) {
                    int bitNbr = thingConfig.getId() - 1;
                    boolean invertState = thingConfig.isStateInverted();
                    updateSwitch(channel, stateEvent.isSet(stateType, bitNbr) ^ invertState);
                }
            }
        }
    }

    protected abstract SatelCommand convertCommand(ChannelUID channel, Command command);

    protected abstract StateType getStateType(String channelId);

    protected Channel getChannel(StateType stateType) {
        String channelId = stateType.toString().toLowerCase();
        Channel channel = getThing().getChannel(channelId);
        if (channel == null) {
            logger.warn("Missing channel for {}", stateType);
        }
        return channel;
    }

    protected Collection<SatelCommand> getRefreshCommands(NewStatesEvent event) {
        Collection<SatelCommand> result = new LinkedList<>();
        boolean requiresRefresh = requiresRefresh();
        for (Channel channel : getThing().getChannels()) {
            boolean isChannelLinked = !linkRegistry.getLinks(channel.getUID()).isEmpty();
            if (isChannelLinked) {
                StateType stateType = getStateType(channel.getUID().getId());
                if (requiresRefresh || event.isNew(stateType.getRefreshCommand())) {
                    result.add(new IntegraStateCommand(stateType, bridgeHandler.getIntegraType().hasExtPayload()));
                }
            }
        }
        return result;
    }

    protected void updateSwitch(Channel channel, boolean switchOn) {
        State state = switchOn ? OnOffType.ON : OnOffType.OFF;
        updateState(channel.getUID(), state);
    }

    protected byte[] getObjectBitset(int size, int... ids) {
        byte[] bitset = new byte[size];
        for (int id : ids) {
            int bitNbr = id - 1;
            bitset[bitNbr / 8] |= (byte) (1 << (bitNbr % 8));
        }
        return bitset;
    }

    protected boolean requiresRefresh() {
        return getThing().getStatus() == ThingStatus.INITIALIZING
                || Boolean.valueOf(getThing().setProperty(PROPERTY_REQUIRES_REFRESH, null));
    }

}
