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
package org.openhab.binding.hdpowerview.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link HDPowerViewBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Andy Lintner - Initial contribution
 * @author Andrew Fiddian-Green - Added support for secondary rail positions
 */
@NonNullByDefault
public class HDPowerViewBindingConstants {

    public static final String BINDING_ID = "hdpowerview";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_HUB = new ThingTypeUID(BINDING_ID, "hub");
    public static final ThingTypeUID THING_TYPE_SHADE = new ThingTypeUID(BINDING_ID, "shade");

    // List of all Channel ids
    public static final String CHANNEL_SHADE_POSITION = "position";
    public static final String CHANNEL_SHADE_VANE = "vane";
    public static final String CHANNEL_SHADE_LOW_BATTERY = "lowBattery";
    public static final String CHANNEL_SHADE_SECONDARY_POSITION = "secondary";

    public static final String CHANNELTYPE_SCENE_ACTIVATE = "scene-activate";

    public static final List<String> NETBIOS_NAMES = Arrays.asList("PDBU-Hub3.0", "PowerView-Hub");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<>();

    static {
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_HUB);
        SUPPORTED_THING_TYPES_UIDS.add(THING_TYPE_SHADE);
    }
}
