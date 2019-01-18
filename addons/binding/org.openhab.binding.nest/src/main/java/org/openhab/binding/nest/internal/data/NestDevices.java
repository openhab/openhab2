/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.nest.internal.data;

import java.util.Map;

/**
 * All the Nest devices broken up by type.
 *
 * @author David Bennett - Initial contribution
 */
public class NestDevices {

    private Map<String, Thermostat> thermostats;
    private Map<String, SmokeDetector> smokeCoAlarms;
    private Map<String, Camera> cameras;

    /** Id to thermostat mapping */
    public Map<String, Thermostat> getThermostats() {
        return thermostats;
    }

    /** Id to camera mapping */
    public Map<String, Camera> getCameras() {
        return cameras;
    }

    /** Id to smoke detector */
    public Map<String, SmokeDetector> getSmokeCoAlarms() {
        return smokeCoAlarms;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NestDevices other = (NestDevices) obj;
        if (cameras == null) {
            if (other.cameras != null) {
                return false;
            }
        } else if (!cameras.equals(other.cameras)) {
            return false;
        }
        if (smokeCoAlarms == null) {
            if (other.smokeCoAlarms != null) {
                return false;
            }
        } else if (!smokeCoAlarms.equals(other.smokeCoAlarms)) {
            return false;
        }
        if (thermostats == null) {
            if (other.thermostats != null) {
                return false;
            }
        } else if (!thermostats.equals(other.thermostats)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cameras == null) ? 0 : cameras.hashCode());
        result = prime * result + ((smokeCoAlarms == null) ? 0 : smokeCoAlarms.hashCode());
        result = prime * result + ((thermostats == null) ? 0 : thermostats.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NestDevices [thermostats=").append(thermostats).append(", smokeCoAlarms=").append(smokeCoAlarms)
                .append(", cameras=").append(cameras).append("]");
        return builder.toString();
    }

}
