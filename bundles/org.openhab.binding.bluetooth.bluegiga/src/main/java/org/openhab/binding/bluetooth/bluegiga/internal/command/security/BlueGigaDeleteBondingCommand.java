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
package org.openhab.binding.bluetooth.bluegiga.internal.command.security;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaCommand;

/**
 * Class to implement the BlueGiga command <b>deleteBonding</b>.
 * <p>
 * This command deletes a bonding from the local security database. There can be a maximum of 8
 * bonded devices stored at the same time, and one of them must be deleted if you need bonding with
 * a 9th device.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaDeleteBondingCommand extends BlueGigaCommand {
    public static int COMMAND_CLASS = 0x05;
    public static int COMMAND_METHOD = 0x02;

    /**
     * Bonding handle of a device. This handle can be obtained for example from events like: Scan
     * Response Status - If handle is 0xFF, all bondings will be deleted
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int handle;

    /**
     * Bonding handle of a device. This handle can be obtained for example from events like: Scan
     * Response Status - If handle is 0xFF, all bondings will be deleted
     *
     * @param handle the handle to set as {@link int}
     */
    public void setHandle(int handle) {
        this.handle = handle;
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);

        // Serialize the fields
        serializeUInt8(handle);

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaDeleteBondingCommand [handle=");
        builder.append(handle);
        builder.append(']');
        return builder.toString();
    }
}
