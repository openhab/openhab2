/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.airvisualnode;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.DefaultSystemChannelTypeProvider;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link AirVisualNodeBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Victor Antonovich - Initial contribution
 */
@NonNullByDefault
public class AirVisualNodeBindingConstants {

    public static final String BINDING_ID = "airvisualnode";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_AVNODE = new ThingTypeUID(BINDING_ID, "avnode");

    // List of all Channel ids
    public static final String CHANNEL_CO2 = "co2";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_AQI_US = "aqi";
    public static final String CHANNEL_PM_25 = "pm_25";
    public static final String CHANNEL_TEMP_CELSIUS = "temperature";
    public static final String CHANNEL_TIMESTAMP = "timestamp";
    public static final String CHANNEL_USED_MEMORY = "used_memory";
    public static final String CHANNEL_BATTERY_LEVEL = DefaultSystemChannelTypeProvider.SYSTEM_CHANNEL_BATTERY_LEVEL
            .getUID().getId();
    public static final String CHANNEL_WIFI_STRENGTH = DefaultSystemChannelTypeProvider.SYSTEM_CHANNEL_SIGNAL_STRENGTH
            .getUID().getId();

    // List of all supported Thing UIDs
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(THING_TYPE_AVNODE)));

    // List of all supported Channel ids
    public static final Set<String> SUPPORTED_CHANNEL_IDS = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList(CHANNEL_CO2, CHANNEL_HUMIDITY, CHANNEL_AQI_US,
                    CHANNEL_PM_25, CHANNEL_TEMP_CELSIUS, CHANNEL_BATTERY_LEVEL,
                    CHANNEL_WIFI_STRENGTH, CHANNEL_TIMESTAMP, CHANNEL_USED_MEMORY)));
}
