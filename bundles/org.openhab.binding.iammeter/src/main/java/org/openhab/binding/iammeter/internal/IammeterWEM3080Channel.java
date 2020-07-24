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
@org.eclipse.jdt.annotation.NonNullByDefault
package org.openhab.binding.iammeter.internal;

/**
 * The {@link IammeterWEM3080Channel} Enum defines common constants, which are
 * used across the whole binding.
 *
 * @author yangbo - Initial contribution
 */
public enum IammeterWEM3080Channel {

    CHANNEL_VOLTAGE("voltage_a", 0),
    CHANNEL_CURRENT("current_a", 1),
    CHANNEL_POWER("power_a", 2),
    CHANNEL_IMPORTENERGY("importenergy_a", 3),
    CHANNEL_EXPORTGRID("exportgrid_a", 4);

    private final String id;
    private final int index;

    IammeterWEM3080Channel(String id, int index) {
        this.id = id;
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }
}
