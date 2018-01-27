/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smappee;

import java.util.Set;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link SmappeeBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Niko Tanghe - Initial contribution
 */
public class SmappeeBindingConstants {

    public static final String BINDING_ID = "smappee";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_SMAPPEE = new ThingTypeUID(BINDING_ID, "smappee");
    public final static ThingTypeUID THING_TYPE_APPLIANCE = new ThingTypeUID(BINDING_ID, "smappee-appliance");
    public final static ThingTypeUID THING_TYPE_ACTUATOR = new ThingTypeUID(BINDING_ID, "smappee-actuator");
    public final static ThingTypeUID THING_TYPE_SENSOR = new ThingTypeUID(BINDING_ID, "smappee-sensor");

    // All supported Bridge types
    public static final Set<ThingTypeUID> SUPPORTED_BRIDGE_TYPES_UIDS = ImmutableSet.of(THING_TYPE_SMAPPEE);

    // All supported Thing types
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_APPLIANCE,
            THING_TYPE_ACTUATOR, THING_TYPE_SENSOR);

    // List of all Channel ids
    // Smappee
    public final static String CHANNEL_CONSUMPTION = "consumption";
    public final static String CHANNEL_SOLAR = "solar";
    public final static String CHANNEL_ALWAYSON = "alwayson";
    // Appliance
    public final static String CHANNEL_APPLIANCE_POWER = "appliance-power";
    public final static String CHANNEL_APPLIANCE_LASTUPDATE = "appliance-lastupdate";
    // Actuator
    public final static String CHANNEL_ACTUATOR_SWITCH = "actuator-switch";
    // Sensor
    public final static String CHANNEL_SENSOR_VALUE = "sensor-value";

    // List of all Parameters
    // Smappee
    public final static String PARAMETER_CLIENT_ID = "client_id";
    public final static String PARAMETER_CLIENT_SECRET = "client_secret";
    public final static String PARAMETER_USERNAME = "username";
    public final static String PARAMETER_PASSWORD = "password";
    public final static String PARAMETER_SERVICE_LOCATION_NAME = "service_location_name";
    public final static String PARAMETER_POLLTIME = "poll_time";
    // Appliance
    public final static String PARAMETER_APPLIANCE_ID = "id";
    public final static String PARAMETER_APPLIANCE_TYPE = "type";
    // Actuator
    public final static String PARAMETER_ACTUATOR_ID = "id";
    // Sensor
    public final static String PARAMETER_SENSOR_ID = "id";
    public final static String PARAMETER_SENSOR_CHANNEL_ID = "channel_id";
    public final static String PARAMETER_SENSOR_TYPE = "type";

}
