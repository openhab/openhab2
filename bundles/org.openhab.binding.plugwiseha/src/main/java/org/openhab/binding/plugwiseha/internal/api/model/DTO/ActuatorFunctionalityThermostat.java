/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.plugwiseha.internal.api.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author B. van Wetten - Initial contribution
 */
@XStreamAlias("thermostat_functionality")
public class ActuatorFunctionalityThermostat extends ActuatorFunctionality {

    @SuppressWarnings("unused")
    private Double setpoint;

    @SuppressWarnings("unused")
    @XStreamAlias("preheating_allowed")
    private Boolean preheatingAllowed;

    public ActuatorFunctionalityThermostat(Double temperature) {
        this.setpoint = temperature;
    }

    public ActuatorFunctionalityThermostat(Boolean preheatingAllowed) {
        this.preheatingAllowed = preheatingAllowed;
    }
}
