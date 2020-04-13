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
package org.openhab.binding.bluetooth.daikinmadoka.internal.model;

/**
 *
 * @author blafois
 *
 */
public class MadokaProperties {

    public enum FAN_SPEED {
        MAX(5),
        MEDIUM(3),
        LOW(1);

        private int v;

        FAN_SPEED(int v) {
            this.v = v;
        }

        public static FAN_SPEED valueOf(int v) {
            if (v == 5) {
                return MAX;
            } else if (v >= 2 && v <= 4) {
                return MEDIUM;
            } else if (v == 1) {
                return LOW;
            } else {
                return null;
            }
        }

        public int value() {
            return v;
        }
    }

    public enum OPERATION_MODE {
        FAN(0),
        DRY(1),
        AUTO(2),
        COOL(3),
        HEAT(4),
        VENTILATION(5);

        private int v;

        OPERATION_MODE(int v) {
            this.v = v;
        }

        public static OPERATION_MODE valueOf(int v) {
            for (OPERATION_MODE m : values()) {
                if (m.v == v) {
                    return m;
                }
            }
            return null;
        }

        public int value() {
            return v;
        }
    }

}
