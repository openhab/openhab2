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
package org.openhab.binding.comfoair.internal.datatypes;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.comfoair.internal.ComfoAirCommandType;

/**
 * Abstract class to convert binary hex values into openHAB states and vice
 * versa
 *
 * @author Holger Hees - Initial Contribution
 * @author Hans Böhm - Refactoring
 */
@NonNullByDefault
public interface ComfoAirDataType {
    /**
     * Generate a openHAB State object based on response data.
     *
     * @param response
     * @param commandType
     * @return converted State object
     */
    State convertToState(int[] response, ComfoAirCommandType commandType);

    /**
     * Generate byte array based on a openHAB State.
     *
     * @param value
     * @param commandType
     * @return converted byte array
     */
    int @Nullable [] convertFromState(State value, ComfoAirCommandType commandType);

    default int calculateNumberValue(int[] data, ComfoAirCommandType commandType) {
        int[] get_reply_data_pos = commandType.getGetReplyDataPos();
        int value = 0;
        if (get_reply_data_pos != null) {
            int base = 0;

            for (int i = get_reply_data_pos.length - 1; i >= 0; i--) {
                if (get_reply_data_pos[i] < data.length) {
                    value += data[get_reply_data_pos[i]] << base;
                    base += 8;
                } else {
                    return -1;
                }
            }
        } else {
            value = -1;
        }
        return value;
    }

    default String calculateStringValue(int[] data, ComfoAirCommandType commandType) {
        int[] get_reply_data_pos = commandType.getGetReplyDataPos();
        StringBuilder value = new StringBuilder();
        if (get_reply_data_pos != null) {
            for (int pos : get_reply_data_pos) {
                if (pos < data.length) {
                    value.append((char) data[pos]);
                }
            }
        }
        return value.toString();
    }
}
