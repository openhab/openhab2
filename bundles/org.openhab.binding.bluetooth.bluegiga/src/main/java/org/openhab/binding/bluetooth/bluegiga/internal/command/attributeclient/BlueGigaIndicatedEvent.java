/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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

/**
 * Class to implement the BlueGiga command <b>indicatedEvent</b>.
 * <p>
 * This event is produced at the GATT server side when an attribute is successfully indicated to
 * the GATT client. This means the event is only produced at the GATT server if the indication is
 * acknowledged by the GATT client (the remote device).
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaIndicatedEvent extends BlueGigaDeviceResponse {
    public static final int COMMAND_CLASS = 0x04;
    public static final int COMMAND_METHOD = 0x00;

    /**
     * Attribute handle
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int attrhandle;

    /**
     * Event constructor
     */
    public BlueGigaIndicatedEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        attrhandle = deserializeUInt16();
    }

    /**
     * Attribute handle
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current attrhandle as {@link int}
     */
    public int getAttrhandle() {
        return attrhandle;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaIndicatedEvent [connection=");
        builder.append(connection);
        builder.append(", attrhandle=");
        builder.append(attrhandle);
        builder.append(']');
        return builder.toString();
    }
}
