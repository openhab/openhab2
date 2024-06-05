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
package org.openhab.binding.teslapowerwall.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link TeslaPowerwallBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Paul Smedley - Initial contribution
 */
@NonNullByDefault
public class TeslaPowerwallBindingConstants {

    private static final String BINDING_ID = "teslapowerwall";

    // List of all Thing Type UIDs
    public static final ThingTypeUID TESLAPOWERWALL_THING = new ThingTypeUID(BINDING_ID, "teslapowerwall");

    // List of all Channel ids
    public static final String CHANNEL_TESLAPOWERWALL_GRIDSTATUS = "gridstatus";
    public static final String CHANNEL_TESLAPOWERWALL_GRIDSERVICES = "gridservices";
    public static final String CHANNEL_TESLAPOWERWALL_BATTERYSOE = "batterysoe";
    public static final String CHANNEL_TESLAPOWERWALL_MODE = "mode";
    public static final String CHANNEL_TESLAPOWERWALL_RESERVE = "reserve";
    public static final String CHANNEL_TESLAPOWERWALL_GRID_INSTPOWER = "grid-instpower";
    public static final String CHANNEL_TESLAPOWERWALL_GRID_ENERGYEXPORTED = "grid-energyexported";
    public static final String CHANNEL_TESLAPOWERWALL_GRID_ENERGYIMPORTED = "grid-energyimported";
    public static final String CHANNEL_TESLAPOWERWALL_BATTERY_INSTPOWER = "battery-instpower";
    public static final String CHANNEL_TESLAPOWERWALL_BATTERY_ENERGYIMPORTED = "battery-energyimported";
    public static final String CHANNEL_TESLAPOWERWALL_BATTERY_ENERGYEXPORTED = "battery-energyexported";
    public static final String CHANNEL_TESLAPOWERWALL_HOME_INSTPOWER = "home-instpower";
    public static final String CHANNEL_TESLAPOWERWALL_HOME_ENERGYEXPORTED = "home-energyexported";
    public static final String CHANNEL_TESLAPOWERWALL_HOME_ENERGYIMPORTED = "home-energyimported";
    public static final String CHANNEL_TESLAPOWERWALL_SOLAR_INSTPOWER = "solar-instpower";
    public static final String CHANNEL_TESLAPOWERWALL_SOLAR_ENERGYEXPORTED = "solar-energyexported";
    public static final String CHANNEL_TESLAPOWERWALL_SOLAR_ENERGYIMPORTED = "solar-energyimported";
    public static final String CHANNEL_TESLAPOWERWALL_FULL_PACK_ENERGY = "full-pack-energy";
    public static final String CHANNEL_TESLAPOWERWALL_DEGRADATION = "degradation";

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(TESLAPOWERWALL_THING);
}
