/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.Sets;

/**
 * The {@link PlugwiseBinding} class defines common constants, which are used across the whole binding.
 *
 * @author Wouter Born - Initial contribution
 */
@NonNullByDefault
public class PlugwiseBindingConstants {

    public static final String BINDING_ID = "plugwise";

    // List of all Channel IDs
    public static final String CHANNEL_CLOCK = "clock";
    public static final String CHANNEL_ENERGY = "energy";
    public static final String CHANNEL_ENERGY_STAMP = "energystamp";
    public static final String CHANNEL_HUMIDITY = "humidity";
    public static final String CHANNEL_LAST_SEEN = "lastseen";
    public static final String CHANNEL_LEFT_BUTTON_STATE = "leftbuttonstate";
    public static final String CHANNEL_POWER = "power";
    public static final String CHANNEL_REAL_TIME_CLOCK = "realtimeclock";
    public static final String CHANNEL_RIGHT_BUTTON_STATE = "rightbuttonstate";
    public static final String CHANNEL_STATE = "state";
    public static final String CHANNEL_TEMPERATURE = "temperature";
    public static final String CHANNEL_TRIGGERED = "triggered";

    // List of all configuration properties
    public static final String CONFIG_PROPERTY_MAC_ADDRESS = "macAddress";
    public static final String CONFIG_PROPERTY_RECALIBRATE = "recalibrate";
    public static final String CONFIG_PROPERTY_SERIAL_PORT = "serialPort";
    public static final String CONFIG_PROPERTY_UPDATE_CONFIGURATION = "updateConfiguration";
    public static final String CONFIG_PROPERTY_UPDATE_INTERVAL = "updateInterval";

    // List of all property IDs
    public static final String PROPERTY_HERTZ = "hertz";
    public static final String PROPERTY_MAC_ADDRESS = "macAddress";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_CIRCLE = new ThingTypeUID(BINDING_ID, "circle");
    public static final ThingTypeUID THING_TYPE_CIRCLE_PLUS = new ThingTypeUID(BINDING_ID, "circleplus");
    public static final ThingTypeUID THING_TYPE_SCAN = new ThingTypeUID(BINDING_ID, "scan");
    public static final ThingTypeUID THING_TYPE_SENSE = new ThingTypeUID(BINDING_ID, "sense");
    public static final ThingTypeUID THING_TYPE_STEALTH = new ThingTypeUID(BINDING_ID, "stealth");
    public static final ThingTypeUID THING_TYPE_STICK = new ThingTypeUID(BINDING_ID, "stick");
    public static final ThingTypeUID THING_TYPE_SWITCH = new ThingTypeUID(BINDING_ID, "switch");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Sets.newHashSet(THING_TYPE_CIRCLE,
            THING_TYPE_CIRCLE_PLUS, THING_TYPE_SCAN, THING_TYPE_SENSE, THING_TYPE_STEALTH, THING_TYPE_STICK,
            THING_TYPE_SWITCH);

}
