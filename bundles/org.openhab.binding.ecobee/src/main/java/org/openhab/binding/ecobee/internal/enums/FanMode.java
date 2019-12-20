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
package org.openhab.binding.ecobee.internal.enums;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link FanMode} represents the possible fan modes.
 *
 * @author John Cocula - Initial contribution
 * @author Mark Hilbush - Adapt for OH2/3
 */
public enum FanMode {

    @SerializedName("auto")
    AUTO("auto"),

    @SerializedName("on")
    ON("on");

    private final String mode;

    private FanMode(final String mode) {
        this.mode = mode;
    }

    public String value() {
        return mode;
    }

    public static FanMode forValue(String v) {
        for (FanMode fm : FanMode.values()) {
            if (fm.mode.equals(v)) {
                return fm;
            }
        }
        throw new IllegalArgumentException("Invalid fan mode: " + v);
    }

    @Override
    public String toString() {
        return this.mode;
    }
}
