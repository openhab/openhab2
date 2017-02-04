/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal.protocol.field;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates all Plugwise message types. Many are still missing, and require further protocol analysis.
 *
 * @author Karel Goderis
 * @author Wouter Born - Initial contribution
 */
public enum MessageType {

    ACKNOWLEDGEMENT_V1(0x0000),
    NODE_AVAILABLE(0x0006),
    NODE_AVAILABLE_RESPONSE(0x0007),
    NETWORK_RESET_REQUEST(0x0008),
    NETWORK_STATUS_REQUEST(0x000A),
    PING_REQUEST(0x000D),
    PING_RESPONSE(0x000E),
    NETWORK_STATUS_RESPONSE(0x0011),
    POWER_INFORMATION_REQUEST(0x0012),
    POWER_INFORMATION_RESPONSE(0x0013),
    CLOCK_SET_REQUEST(0x0016),
    POWER_CHANGE_REQUEST(0x0017),
    DEVICE_ROLE_CALL_REQUEST(0x0018),
    DEVICE_ROLE_CALL_RESPONSE(0x0019),
    DEVICE_INFORMATION_REQUEST(0x0023),
    DEVICE_INFORMATION_RESPONSE(0x0024),
    POWER_CALIBRATION_REQUEST(0x0026),
    POWER_CALIBRATION_RESPONSE(0x0027),
    REAL_TIME_CLOCK_GET_REQUEST(0x0029),
    REAL_TIME_CLOCK_GET_RESPONSE(0x003A),
    CLOCK_GET_REQUEST(0x003E),
    CLOCK_GET_RESPONSE(0x003F),
    POWER_BUFFER_REQUEST(0x0048),
    POWER_BUFFER_RESPONSE(0x0049),
    ANNOUNCE_AWAKE_REQUEST(0x004F),
    SLEEP_SET_REQUEST(0x0050),
    POWER_LOG_INTERVAL_SET_REQUEST(0x0057),
    BROADCAST_GROUP_SWITCH_RESPONSE(0x0056),
    MODULE_JOINED_NETWORK_REQUEST(0x0061),
    ACKNOWLEDGEMENT_V2(0x0100),
    SCAN_PARAMETERS_SET_REQUEST(0x0101),
    LIGHT_CALIBRATION_REQUEST(0x0102),
    SENSE_REPORT_INTERVAL_SET_REQUEST(0x0103),
    SENSE_BOUNDARIES_SET_REQUEST(0x0104),
    SENSE_REPORT_REQUEST(0x0105);

    private static final Map<Integer, MessageType> TYPES_BY_VALUE = new HashMap<>();

    static {
        for (MessageType type : MessageType.values()) {
            TYPES_BY_VALUE.put(type.identifier, type);
        }
    }

    public static MessageType forValue(int value) {
        return TYPES_BY_VALUE.get(value);
    }

    private int identifier;

    private MessageType(int value) {
        identifier = value;
    }

    public int toInt() {
        return identifier;
    }
};
