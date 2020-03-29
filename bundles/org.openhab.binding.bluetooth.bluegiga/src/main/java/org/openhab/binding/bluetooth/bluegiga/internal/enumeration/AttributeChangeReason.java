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
package org.openhab.binding.bluetooth.bluegiga.internal.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to implement the BlueGiga Enumeration <b>AttributeChangeReason</b>.
 * <p>
 * This enumeration contains the reason for an attribute value change.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
public enum AttributeChangeReason {
    /**
     * Default unknown value
     */
    UNKNOWN(-1),

    /**
     * [0] Value was written by remote device using write request
     */
    ATTRIBUTES_ATTRIBUTE_CHANGE_REASON_WRITE_REQUEST(0x0000),

    /**
     * [1] Value was written by remote device using write command
     */
    ATTRIBUTES_ATTRIBUTE_CHANGE_REASON_WRITE_COMMAND(0x0001),

    /**
     * [2] Local attribute value was written by the remote device, but the Smart Bluetooth stack is
     * waiting for the write to be confirmed by the application. User Write Response command should
     * be used to send the confirmation. For this reason to appear the attribute in the GATT database
     * must have the user property enabled. See Profile Toolkit Developer Guide for more
     * information how to enable the user property for an attribute.
     */
    ATTRIBUTES_ATTRIBUTE_CHANGE_REASON_WRITE_REQUEST_USER(0x0002);

    /**
     * A mapping between the integer code and its corresponding type to
     * facilitate lookup by code.
     */
    private static Map<Integer, AttributeChangeReason> codeMapping;

    private int key;

    private AttributeChangeReason(int key) {
        this.key = key;
    }

    private static void initMapping() {
        codeMapping = new HashMap<Integer, AttributeChangeReason>();
        for (AttributeChangeReason s : values()) {
            codeMapping.put(s.key, s);
        }
    }

    /**
     * Lookup function based on the type code. Returns null if the code does not exist.
     *
     * @param attributeChangeReason
     *            the code to lookup
     * @return enumeration value.
     */
    public static AttributeChangeReason getAttributeChangeReason(int attributeChangeReason) {
        if (codeMapping == null) {
            initMapping();
        }

        if (codeMapping.get(attributeChangeReason) == null) {
            return UNKNOWN;
        }

        return codeMapping.get(attributeChangeReason);
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
