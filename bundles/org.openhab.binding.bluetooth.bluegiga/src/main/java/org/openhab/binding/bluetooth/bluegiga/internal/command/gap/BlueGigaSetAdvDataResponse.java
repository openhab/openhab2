/**
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
package org.openhab.binding.bluetooth.bluegiga.internal.command.gap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaResponse;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.BgApiResponse;

/**
 * Class to implement the BlueGiga command <b>setAdvData</b>.
 * <p>
 * This commands set advertisement or scan response data used in the advertisement and scan
 * response packets. The command allows application specific data to be broadcasts either in
 * advertisement or scan response packets. The data set with this command is only used when the
 * GAP discoverable mode is set to gap_user_data. Notice that advertisement or scan response
 * data must be formatted in accordance to the Bluetooth Core Specification. See BLUETOOTH
 * SPECIFICATION Version 4.0 [Vol 3 - Part C - Chapter 11].
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaSetAdvDataResponse extends BlueGigaResponse {
    public static final int COMMAND_CLASS = 0x06;
    public static final int COMMAND_METHOD = 0x09;

    /**
     * 0: Command was successfully executed. Non-zero: An error occurred
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     */
    private BgApiResponse result;

    /**
     * Response constructor
     */
    public BlueGigaSetAdvDataResponse(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        result = deserializeBgApiResponse();
    }

    /**
     * 0: Command was successfully executed. Non-zero: An error occurred
     * <p>
     * BlueGiga API type is <i>BgApiResponse</i> - Java type is {@link BgApiResponse}
     *
     * @return the current result as {@link BgApiResponse}
     */
    public BgApiResponse getResult() {
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaSetAdvDataResponse [result=");
        builder.append(result);
        builder.append(']');
        return builder.toString();
    }
}
