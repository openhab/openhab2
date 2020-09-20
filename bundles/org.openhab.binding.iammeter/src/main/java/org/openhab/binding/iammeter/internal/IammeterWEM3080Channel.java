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
package org.openhab.binding.iammeter.internal;

import javax.measure.Unit;

import org.openhab.core.library.unit.SmartHomeUnits;

/**
 * The {@link IammeterWEM3080Channel} Enum defines common constants, which are
 * used across the whole binding.
 *
 * @author Yang Bo - Initial contribution
 */
public enum IammeterWEM3080Channel {

    CHANNEL_VOLTAGE("voltage", SmartHomeUnits.VOLT),
    CHANNEL_CURRENT("current", SmartHomeUnits.AMPERE),
    CHANNEL_POWER("power", SmartHomeUnits.WATT),
    CHANNEL_IMPORTENERGY("importenergy", SmartHomeUnits.KILOWATT_HOUR),
    CHANNEL_EXPORTGRID("exportgrid", SmartHomeUnits.KILOWATT_HOUR);

    private final String id;
    private final Unit<?> unit;

    IammeterWEM3080Channel(String id, Unit<?> unit) {
        this.id = id;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public Unit<?> getUnit() {
        return unit;
    }
}
