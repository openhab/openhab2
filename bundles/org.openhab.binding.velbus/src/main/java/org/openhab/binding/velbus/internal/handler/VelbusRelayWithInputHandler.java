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
package org.openhab.binding.velbus.internal.handler;

import static org.openhab.binding.velbus.internal.VelbusBindingConstants.THING_TYPE_VMB1RYS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.velbus.internal.packets.VelbusButtonPacket;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.types.Command;

/**
 * The {@link VelbusRelayWithInputHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Rosengarten - Initial contribution
 */
@NonNullByDefault
public class VelbusRelayWithInputHandler extends VelbusRelayHandler {
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = new HashSet<>(Arrays.asList(THING_TYPE_VMB1RYS));

    private static final StringType PRESSED = new StringType("PRESSED");
    private static final StringType LONG_PRESSED = new StringType("LONG_PRESSED");

    public VelbusRelayWithInputHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        super.handleCommand(channelUID, command);

        VelbusBridgeHandler velbusBridgeHandler = getVelbusBridgeHandler();
        if (velbusBridgeHandler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
            return;
        }

        if (isButtonChannel(channelUID) && command instanceof StringType) {
            StringType stringTypeCommand = (StringType) command;

            if (stringTypeCommand.equals(PRESSED) || stringTypeCommand.equals(LONG_PRESSED)) {
                VelbusButtonPacket packet = new VelbusButtonPacket(getModuleAddress().getChannelIdentifier(channelUID));

                try {
                    packet.Pressed();
                    velbusBridgeHandler.sendPacket(packet.getBytes());
                    Thread.sleep(20);

                    if (stringTypeCommand.equals(LONG_PRESSED)) {
                        packet.LongPressed();
                        velbusBridgeHandler.sendPacket(packet.getBytes());
                        Thread.sleep(20);
                    }

                    packet.Released();
                    velbusBridgeHandler.sendPacket(packet.getBytes());
                } catch (InterruptedException e) {
                    // do nothing
                }
            } else {
                throw new UnsupportedOperationException(
                        "The command '" + command + "' is not supported on channel '" + channelUID + "'.");
            }
        }
    }

    private boolean isButtonChannel(ChannelUID channelUID) {
        return "CH6".equals(channelUID.toString().substring(channelUID.toString().length() - 3));
    }

    @Override
    public void onPacketReceived(byte[] packet) {
        super.onPacketReceived(packet);
    }
}
