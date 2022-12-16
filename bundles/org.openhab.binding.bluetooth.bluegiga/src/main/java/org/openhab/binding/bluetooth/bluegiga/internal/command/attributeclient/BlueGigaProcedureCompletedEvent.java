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
package org.openhab.binding.bluetooth.bluegiga.internal.command.attributeclient;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaDeviceResponse;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.BgApiResponse;

/**
 * Class to implement the BlueGiga command <b>procedureCompletedEvent</b>.
 * <p>
 * This event is produced at the GATT client when an attribute protocol event is completed and a
 * new operation can be issued
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaProcedureCompletedEvent extends BlueGigaDeviceResponse {
    public static final int COMMAND_CLASS = 0x04;
    public static final int COMMAND_METHOD = 0x01;

    /**
     * 0: The operation was successful. Otherwise: attribute protocol error code returned by
     * remote device
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     */
    private BgApiResponse result;

    /**
     * Characteristic handle at which the event ended
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int chrHandle;

    /**
     * Event constructor
     */
    public BlueGigaProcedureCompletedEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        result = deserializeBgApiResponse();
        chrHandle = deserializeUInt16();
    }

    /**
     * 0: The operation was successful. Otherwise: attribute protocol error code returned by
     * remote device
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     *
     * @return the current result as {@link BgApiResponse}
     */
    public BgApiResponse getResult() {
        return result;
    }

    /**
     * Characteristic handle at which the event ended
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current chr_handle as {@link int}
     */
    public int getChrHandle() {
        return chrHandle;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaProcedureCompletedEvent [connection=");
        builder.append(connection);
        builder.append(", result=");
        builder.append(result);
        builder.append(", chrHandle=");
        builder.append(chrHandle);
        builder.append(']');
        return builder.toString();
    }
}
