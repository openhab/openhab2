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
package org.openhab.binding.broadlink.handler;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;

/**
 * Multiple power socket strip device
 *
 * @author John Marshall/Cato Sognen - Initial contribution
 */
@NonNullByDefault
public class BroadlinkStripModel1Handler extends BroadlinkBaseThingHandler {

    public BroadlinkStripModel1Handler(Thing thing) {
        super(thing);
    }

    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            updateItemStatus();
            return;
        }

        switch (channelUID.getId()) {
            case "s1powerOn":
                interpretCommandForSocket(1, command);
                break;
            case "s2powerOn":
                interpretCommandForSocket(2, command);
                break;
            case "s3powerOn":
                interpretCommandForSocket(3, command);
                break;
            case "s4powerOn":
                interpretCommandForSocket(4, command);
                break;
            default:
                break;
        }
    }

    private void interpretCommandForSocket(int sid, Command command) {
        try {
            if (command == OnOffType.ON) {
                setStatusOnDevice((byte) sid, (byte) 1);
            } else if (command == OnOffType.OFF) {
                setStatusOnDevice((byte) sid, (byte) 0);
            }
        } catch (IOException e) {
            logger.warn("Couldn't interpret command for strip device", e);
        }
    }

    private void setStatusOnDevice(byte sid, byte state) throws IOException {
        int sidMask = 1 << sid - 1;
        byte payload[] = new byte[16];
        payload[0] = 13;
        payload[2] = -91;
        payload[3] = -91;
        payload[4] = 90;
        payload[5] = 90;
        if (state == 1) {
            payload[6] = (byte) (178 + (sidMask << 1));
        } else {
            payload[6] = (byte) (byte) (178 + sidMask);
        }
        payload[7] = -64;
        payload[8] = 2;
        payload[10] = 3;
        payload[13] = (byte) sidMask;
        if (state == 1) {
            payload[14] = (byte) sidMask;
        } else {
            payload[14] = 0;
        }
        byte message[] = buildMessage((byte) 106, payload);
        sendAndReceiveDatagram(message, "Setting MPx status");
    }

    protected boolean getStatusFromDevice() {
        byte payload[] = new byte[16];
        payload[0] = 10;
        payload[2] = -91;
        payload[3] = -91;
        payload[4] = 90;
        payload[5] = 90;
        payload[6] = -82;
        payload[7] = -64;
        payload[8] = 1;
        try {
            byte message[] = buildMessage((byte) 106, payload);
            byte response[] = sendAndReceiveDatagram(message, "status for strip");
            if (response == null) {
                logger.warn("response from strip device was null");
                return false;
            }
            byte decodedPayload[] = decodeDevicePacket(response);
            final int status = decodedPayload[14];

            this.updateState("s1powerOn", (status & 1) == 1 ? OnOffType.ON : OnOffType.OFF);
            this.updateState("s2powerOn", (status & 2) == 2 ? OnOffType.ON : OnOffType.OFF);
            this.updateState("s3powerOn", (status & 4) == 4 ? OnOffType.ON : OnOffType.OFF);
            this.updateState("s4powerOn", (status & 8) == 8 ? OnOffType.ON : OnOffType.OFF);
        } catch (Exception ex) {
            logger.warn("Exception while getting status from device", ex);
            return false;
        }
        return true;
    }

    protected boolean onBroadlinkDeviceBecomingReachable() {
        return getStatusFromDevice();
    }
}
