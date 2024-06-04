/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.linktap.protocol.frames;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The {@link PauseWateringPlanReq} requests the watering plan is disabled for a duration of hours.
 *
 * @author David Goodyear - Initial contribution
 */
@NonNullByDefault
public class PauseWateringPlanReq extends DeviceCmdReq {

    public PauseWateringPlanReq() {
    }

    /**
     * Defines the duration the watering plan is to be paused for.
     * Acceptable range is between 0.1 to 240
     * Units is hours
     */
    @SerializedName("duration")
    @Expose
    public Double duration = 0.0;

    public String isValid() {
        final String superRst = super.isValid();
        if (!superRst.isEmpty()) {
            return superRst;
        }

        if (duration < 0.1 || duration > 240) {
            return "duration not in range 0.1 -> 240";
        }
        return EMPTY_STRING;
    }
}
