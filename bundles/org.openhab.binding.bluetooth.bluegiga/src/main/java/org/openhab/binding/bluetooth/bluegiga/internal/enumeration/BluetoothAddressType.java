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
package org.openhab.binding.bluetooth.bluegiga.internal.enumeration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Class to implement the BlueGiga Enumeration <b>BluetoothAddressType</b>.
 * <p>
 * Bluetooth address types
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public enum BluetoothAddressType {
    /**
     * Default unknown value
     */
    UNKNOWN(-1),

    /**
     * [0] Public Address
     */
    GAP_ADDRESS_TYPE_PUBLIC(0x0000),

    /**
     * [1] Random Address
     */
    GAP_ADDRESS_TYPE_RANDOM(0x0001);

    /**
     * A mapping between the integer code and its corresponding type to
     * facilitate lookup by code.
     */
    private static @Nullable Map<Integer, BluetoothAddressType> codeMapping;

    private int key;

    private BluetoothAddressType(int key) {
        this.key = key;
    }

    /**
     * Lookup function based on the type code. Returns {@link UNKNOWN} if the code does not exist.
     *
     * @param bluetoothAddressType
     *            the code to lookup
     * @return enumeration value.
     */
    public static BluetoothAddressType getBluetoothAddressType(int bluetoothAddressType) {
        Map<Integer, BluetoothAddressType> localCodeMapping = codeMapping;
        if (localCodeMapping == null) {
            localCodeMapping = new HashMap<>();
            for (BluetoothAddressType s : values()) {
                localCodeMapping.put(s.key, s);
            }
            codeMapping = localCodeMapping;
        }

        return localCodeMapping.getOrDefault(bluetoothAddressType, UNKNOWN);
    }

    /**
     * Returns the BlueGiga protocol defined value for this enum
     *
     * @return the BGAPI enumeration key
     */
    public int getKey() {
        return key;
    }
}
