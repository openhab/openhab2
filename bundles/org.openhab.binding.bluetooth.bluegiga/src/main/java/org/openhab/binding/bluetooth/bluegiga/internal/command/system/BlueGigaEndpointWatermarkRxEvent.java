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
package org.openhab.binding.bluetooth.bluegiga.internal.command.system;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaResponse;

/**
 * Class to implement the BlueGiga command <b>endpointWatermarkRxEvent</b>.
 * <p>
 * This event is generated if the receive (incoming) buffer of the endpoint has been filled with
 * a number of bytes equal or higher than the value defined by the command Endpoint Set
 * Watermarks. Data from the receive buffer can then be read (and consequently cleared) with
 * the command Endpoint Rx
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaEndpointWatermarkRxEvent extends BlueGigaResponse {
    public static int COMMAND_CLASS = 0x00;
    public static int COMMAND_METHOD = 0x02;

    /**
     * Endpoint index where data was received
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int endpoint;

    /**
     * Space available
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int data;

    /**
     * Event constructor
     */
    public BlueGigaEndpointWatermarkRxEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        endpoint = deserializeUInt8();
        data = deserializeUInt8();
    }

    /**
     * Endpoint index where data was received
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current endpoint as {@link int}
     */
    public int getEndpoint() {
        return endpoint;
    }

    /**
     * Space available
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current data as {@link int}
     */
    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaEndpointWatermarkRxEvent [endpoint=");
        builder.append(endpoint);
        builder.append(", data=");
        builder.append(data);
        builder.append(']');
        return builder.toString();
    }
}
