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
package org.openhab.binding.sensibo.internal.dto.poddetails;

import javax.measure.Unit;
import javax.measure.quantity.Temperature;

import org.eclipse.smarthome.core.library.unit.ImperialUnits;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.openhab.binding.sensibo.internal.model.AcState;

/**
 * All classes in the ..binding.sensibo.dto are data transfer classes used by the GSON mapper. This class reflects a
 * part of a request/response data structure.
 *
 * @author Arne Seime - Initial contribution.
 */
public class AcStateDTO {
    public boolean on;
    public final String fanLevel;
    public final String temperatureUnit;
    public final Integer targetTemperature;
    public final String mode;
    public final String swing;

    public AcStateDTO(boolean on, String fanLevel, String temperatureUnit, Integer targetTemperature, String mode,
            String swing) {
        this.on = on;
        this.fanLevel = fanLevel;
        this.temperatureUnit = temperatureUnit;
        this.targetTemperature = targetTemperature;
        this.mode = mode;
        this.swing = swing;
    }

    public AcStateDTO(AcState acState) {
        this.on = acState.isOn();
        this.fanLevel = acState.getFanLevel();
        this.targetTemperature = acState.getTargetTemperature();
        this.mode = acState.getMode();
        this.swing = acState.getSwing();

        Unit<Temperature> unit = acState.getTemperatureUnit();

        if (SIUnits.CELSIUS.equals(unit)) {
            this.temperatureUnit = "C";
        } else if (ImperialUnits.FAHRENHEIT.equals(unit)) {
            this.temperatureUnit = "F";
        } else {
            throw new IllegalArgumentException("Unexpected temperature unit " + unit);
        }
    }
}
