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
package org.openhab.binding.bluetooth.bluegiga.internal.command.attributedb;

import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaResponse;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.AttributeChangeReason;

/**
 * Class to implement the BlueGiga command <b>valueEvent</b>.
 * <p>
 * This event is produced at the GATT server when a local attribute value was written by a remote
 * device.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaValueEvent extends BlueGigaResponse {
    public static int COMMAND_CLASS = 0x02;
    public static int COMMAND_METHOD = 0x00;

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int connection;

    /**
     * Reason why value has changed see: enum Attribute Change Reason
     * <p>
     * BlueGiga API type is <i>AttributeChangeReason</i> - Java type is {@link AttributeChangeReason}
     */
    private AttributeChangeReason reason;

    /**
     * Attribute handle, which was changed
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int handle;

    /**
     * Offset into attribute value where data starts
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int offset;

    /**
     * Attribute value
     * <p>
     * BlueGiga API type is <i>uint8array</i> - Java type is {@link int[]}
     */
    private int[] value;

    /**
     * Event constructor
     */
    public BlueGigaValueEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        connection = deserializeUInt8();
        reason = deserializeAttributeChangeReason();
        handle = deserializeUInt16();
        offset = deserializeUInt16();
        value = deserializeUInt8Array();
    }

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current connection as {@link int}
     */
    public int getConnection() {
        return connection;
    }
    /**
     * Reason why value has changed see: enum Attribute Change Reason
     * <p>
     * BlueGiga API type is <i>AttributeChangeReason</i> - Java type is {@link AttributeChangeReason}
     *
     * @return the current reason as {@link AttributeChangeReason}
     */
    public AttributeChangeReason getReason() {
        return reason;
    }
    /**
     * Attribute handle, which was changed
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current handle as {@link int}
     */
    public int getHandle() {
        return handle;
    }
    /**
     * Offset into attribute value where data starts
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current offset as {@link int}
     */
    public int getOffset() {
        return offset;
    }
    /**
     * Attribute value
     * <p>
     * BlueGiga API type is <i>uint8array</i> - Java type is {@link int[]}
     *
     * @return the current value as {@link int[]}
     */
    public int[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaValueEvent [connection=");
        builder.append(connection);
        builder.append(", reason=");
        builder.append(reason);
        builder.append(", handle=");
        builder.append(handle);
        builder.append(", offset=");
        builder.append(offset);
        builder.append(", value=");
        for (int c = 0; c < value.length; c++) {
            if (c > 0) {
                builder.append(' ');
            }
            builder.append(String.format("%02X", value[c]));
        }
        builder.append(']');
        return builder.toString();
    }
}
