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
package org.openhab.binding.bluetooth.bluegiga.internal.command.attributeclient;

import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaCommand;

/**
 * Class to implement the BlueGiga command <b>readByHandle</b>.
 * <p>
 * This command reads a remote attribute's value with the given handle. Read by handle can be
 * used to read attributes up to 22 bytes long. For longer attributes command must be used.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public class BlueGigaReadByHandleCommand extends BlueGigaCommand {
    public static int COMMAND_CLASS = 0x04;
    public static int COMMAND_METHOD = 0x04;

    /**
     * Connection handle
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int connection;

    /**
     * Attribute handle
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int chrHandle;

    /**
     * Connection handle
     *
     * @param connection the connection to set as {@link int}
     */
    public void setConnection(int connection) {
        this.connection = connection;
    }
    /**
     * Attribute handle
     *
     * @param chrHandle the chrHandle to set as {@link int}
     */
    public void setChrHandle(int chrHandle) {
        this.chrHandle = chrHandle;
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);

        // Serialize the fields
        serializeUInt8(connection);
        serializeUInt16(chrHandle);

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaReadByHandleCommand [connection=");
        builder.append(connection);
        builder.append(", chrHandle=");
        builder.append(chrHandle);
        builder.append(']');
        return builder.toString();
    }
}
