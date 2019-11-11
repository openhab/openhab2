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
package org.openhab.binding.upb.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.upb.Constants;
import org.openhab.binding.upb.internal.message.MessageBuilder;
import org.openhab.binding.upb.internal.message.UPBMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thing handler for a virtual device.
 *
 * @author Marcus Better - Initial contribution
 *
 */
@NonNullByDefault
public class VirtualThingHandler extends UPBThingHandler {

    private final Logger logger = LoggerFactory.getLogger(VirtualThingHandler.class);

    public VirtualThingHandler(final Thing device, final @Nullable Byte defaultNetworkId) {
        super(device, defaultNetworkId);
    }

    @Override
    protected void pingDevice() {
        // always succeeds for virtual device
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command cmd) {
        if (controllerHandler == null) {
            logger.info("DEV {}: received cmd {} but no bridge handler", unitId, cmd);
            return;
        }

        if (cmd == RefreshType.REFRESH) {
            return;
        } else if (!(cmd instanceof DecimalType)) {
            logger.info("channel {}: unsupported cmd {}", channelUID, cmd);
            return;
        }

        final byte[] cmdBytes;
        if (channelUID.getId().equals(Constants.LINK_ACTIVATE_CHANNEL_ID)) {
            cmdBytes = ACTIVATE_CMD;
        } else if (channelUID.getId().equals(Constants.LINK_DEACTIVATE_CHANNEL_ID)) {
            cmdBytes = DEACTIVATE_CMD;
        } else {
            logger.warn("channel {}: unexpected channel type", channelUID);
            return;
        }
        final byte dst = ((DecimalType) cmd).byteValue();
        final MessageBuilder message = MessageBuilder.create().network(networkId).destination(dst).link(true)
                .command(cmdBytes);
        controllerHandler.sendPacket(message);
    }

    @Override
    public void onMessageReceived(final UPBMessage msg) {
        final byte linkId = msg.getDestination();
        final String channelId;
        switch (msg.getCommand()) {
            case ACTIVATE:
                channelId = Constants.LINK_ACTIVATE_CHANNEL_ID;
                break;

            case DEACTIVATE:
                channelId = Constants.LINK_DEACTIVATE_CHANNEL_ID;
                break;

            default:
                logger.info("DEV {}: Message {} ignored for link {}", unitId, linkId & 0xff, msg.getCommand());
                return;
        }
        final Channel ch = getThing().getChannel(channelId);
        if (ch == null) {
            return;
        }
        updateState(ch.getUID(), new DecimalType(linkId));
    }

}
