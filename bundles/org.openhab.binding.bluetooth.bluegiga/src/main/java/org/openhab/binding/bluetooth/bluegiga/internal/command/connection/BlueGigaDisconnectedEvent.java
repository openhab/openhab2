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
package org.openhab.binding.bluetooth.bluegiga.internal.command.connection;

import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaDeviceResponse;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.BgApiResponse;

/**
 * Class to implement the BlueGiga command <b>disconnectedEvent</b>.
 * <p>
 * This event is produced when a connection is disconnected.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaDisconnectedEvent extends BlueGigaDeviceResponse {
    public static int COMMAND_CLASS = 0x03;
    public static int COMMAND_METHOD = 0x04;

    /**
     * Disconnection reason code. 0 : disconnected by local user
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     */
    private BgApiResponse reason;

    /**
     * Event constructor
     */
    public BlueGigaDisconnectedEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        reason = deserializeBgApiResponse();
    }

    /**
     * Disconnection reason code. 0 : disconnected by local user
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     *
     * @return the current reason as {@link BgApiResponse}
     */
    public BgApiResponse getReason() {
        return reason;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaDisconnectedEvent [connection=");
        builder.append(connection);
        builder.append(", reason=");
        builder.append(reason);
        builder.append(']');
        return builder.toString();
    }
}
