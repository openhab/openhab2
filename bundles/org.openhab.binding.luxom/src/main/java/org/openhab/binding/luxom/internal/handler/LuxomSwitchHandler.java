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
package org.openhab.binding.luxom.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.luxom.internal.LuxomBindingConstants;
import org.openhab.binding.luxom.internal.protocol.LuxomAction;
import org.openhab.binding.luxom.internal.protocol.LuxomCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LuxomSwitchHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Kris Jespers - Initial contribution
 */
@NonNullByDefault
public class LuxomSwitchHandler extends LuxomThingHandler {

    private final Logger logger = LoggerFactory.getLogger(LuxomSwitchHandler.class);

    public LuxomSwitchHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        super.initialize();

        logger.debug("Initializing Switch handler for address {}", getAddress());

        initDeviceState();
    }

    @Override
    protected void initDeviceState() {
        logger.debug("Initializing device state for Switch {}", getAddress());
        Bridge bridge = getBridge();
        if (bridge == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "No bridge configured");
        } else if (bridge.getStatus() == ThingStatus.ONLINE) {
            updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "Awaiting initial response");
            ping(); // handleUpdate() will set thing status to online when response arrives
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("switch at address {} received command {} for {}", this.getAddress(), command.toFullString(),
                channelUID);
        if (channelUID.getId().equals(LuxomBindingConstants.CHANNEL_SWITCH)) {
            if (command.equals(OnOffType.ON)) {
                set();
                ping(); // to make sure we know the current state
            } else if (command.equals(OnOffType.OFF)) {
                clear();
                ping(); // to make sure we know the current state
            }
        }
    }

    @Override
    public void handleCommandCommingFromBridge(LuxomCommand command) {
        if (command.getAction() == LuxomAction.CLEAR_RESPONSE) {
            updateState(LuxomBindingConstants.CHANNEL_SWITCH, OnOffType.OFF);
            updateStatus(ThingStatus.ONLINE);
        } else if (command.getAction() == LuxomAction.SET_RESPONSE) {
            updateState(LuxomBindingConstants.CHANNEL_SWITCH, OnOffType.ON);
            updateStatus(ThingStatus.ONLINE);
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        logger.debug("switch at address {} linked to channel {}", getAddress(), channelUID);
        if (channelUID.getId().equals(LuxomBindingConstants.CHANNEL_SWITCH)
                || channelUID.getId().equals(LuxomBindingConstants.CHANNEL_BRIGHTNESS)) {
            // Refresh state when new item is linked.
            ping();
        }
    }
}
