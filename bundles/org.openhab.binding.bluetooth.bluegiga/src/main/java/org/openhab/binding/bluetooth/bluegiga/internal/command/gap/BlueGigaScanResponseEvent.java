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
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.BluetoothAddressType;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.ScanResponseType;

/**
 * Class to implement the BlueGiga command <b>scanResponseEvent</b>.
 * <p>
 * This is a scan response event. This event is normally received by a Master which is scanning
 * for advertisement and scan response packets from Slaves.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaScanResponseEvent extends BlueGigaResponse {
    public static final int COMMAND_CLASS = 0x06;
    public static final int COMMAND_METHOD = 0x00;

    /**
     * RSSI value (dBm). Range: -103 to -38
     * <p>
     * BlueGiga API type is <i>int8</i> - Java type is {@link int}
     */
    private int rssi;

    /**
     * Scan response header. 0: Connectable Advertisement packet. 2: Non Connectable
     * Advertisement packet. 4: Scan response packet. 6: Discoverable advertisement packet
     * <p>
     * BlueGiga API type is <i>ScanResponseType</i> - Java type is {@link ScanResponseType}
     */
    private ScanResponseType packetType;

    /**
     * Advertisers address
     * <p>
     * BlueGiga API type is <i>bd_addr</i> - Java type is {@link String}
     */
    private String sender;

    /**
     * Advertiser address type. 1: random address. 0: public address
     * <p>
     * BlueGiga API type is <i>BluetoothAddressType</i> - Java type is {@link BluetoothAddressType}
     */
    private BluetoothAddressType addressType;

    /**
     * Bond handle if there is known bond for this device, 0xff otherwise
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     */
    private int bond;

    /**
     * Scan response data
     * <p>
     * BlueGiga API type is <i>uint8array</i> - Java type is {@link int[]}
     */
    private int[] data;

    /**
     * Event constructor
     */
    public BlueGigaScanResponseEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        rssi = deserializeInt8();
        packetType = deserializeScanResponseType();
        sender = deserializeAddress();
        addressType = deserializeBluetoothAddressType();
        bond = deserializeUInt8();
        data = deserializeUInt8Array();
    }

    /**
     * RSSI value (dBm). Range: -103 to -38
     * <p>
     * BlueGiga API type is <i>int8</i> - Java type is {@link int}
     *
     * @return the current rssi as {@link int}
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * Scan response header. 0: Connectable Advertisement packet. 2: Non Connectable
     * Advertisement packet. 4: Scan response packet. 6: Discoverable advertisement packet
     * <p>
     * BlueGiga API type is <i>ScanResponseType</i> - Java type is {@link ScanResponseType}
     *
     * @return the current packet_type as {@link ScanResponseType}
     */
    public ScanResponseType getPacketType() {
        return packetType;
    }

    /**
     * Advertisers address
     * <p>
     * BlueGiga API type is <i>bd_addr</i> - Java type is {@link String}
     *
     * @return the current sender as {@link String}
     */
    public String getSender() {
        return sender;
    }

    /**
     * Advertiser address type. 1: random address. 0: public address
     * <p>
     * BlueGiga API type is <i>BluetoothAddressType</i> - Java type is {@link BluetoothAddressType}
     *
     * @return the current address_type as {@link BluetoothAddressType}
     */
    public BluetoothAddressType getAddressType() {
        return addressType;
    }

    /**
     * Bond handle if there is known bond for this device, 0xff otherwise
     * <p>
     * BlueGiga API type is <i>uint8</i> - Java type is {@link int}
     *
     * @return the current bond as {@link int}
     */
    public int getBond() {
        return bond;
    }

    /**
     * Scan response data
     * <p>
     * BlueGiga API type is <i>uint8array</i> - Java type is {@link int[]}
     *
     * @return the current data as {@link int[]}
     */
    public int[] getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaScanResponseEvent [rssi=");
        builder.append(rssi);
        builder.append(", packetType=");
        builder.append(packetType);
        builder.append(", sender=");
        builder.append(sender);
        builder.append(", addressType=");
        builder.append(addressType);
        builder.append(", bond=");
        builder.append(bond);
        builder.append(", data=");
        for (int c = 0; c < data.length; c++) {
            if (c > 0) {
                builder.append(' ');
            }
            builder.append(String.format("%02X", data[c]));
        }
        builder.append(']');
        return builder.toString();
    }
}
