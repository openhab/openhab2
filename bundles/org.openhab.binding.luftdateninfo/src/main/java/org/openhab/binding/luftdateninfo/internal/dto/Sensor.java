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
package org.openhab.binding.luftdateninfo.internal.dto;

/**
 * The {@link Sensor} Data Transfer Object
 *
 * @author Bernd Weymann - Initial contribution
 */
public class Sensor {
    private Integer id;
    private String pin;
    private SensorType sensor_type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public SensorType getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(SensorType sensor_type) {
        this.sensor_type = sensor_type;
    }
}
