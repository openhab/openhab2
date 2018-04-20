/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcontrolpoint;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link UpnpControlPointBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Mark Herwege - Initial contribution
 */
@NonNullByDefault
public class UpnpControlPointBindingConstants {

    public static final String BINDING_ID = "upnpcontrolpoint";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_RENDERER = new ThingTypeUID(BINDING_ID, "upnprenderer");
    public static final ThingTypeUID THING_TYPE_SERVER = new ThingTypeUID(BINDING_ID, "upnpserver");
    public static final ThingTypeUID THING_TYPE_CONTROLPOINT = new ThingTypeUID(BINDING_ID, "upnpcontrolpoint");
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(THING_TYPE_RENDERER, THING_TYPE_SERVER, THING_TYPE_CONTROLPOINT).collect(Collectors.toSet());

    // List of thing parameters names
    public static final String HOST_PARAMETER = "ipAddress";
    public static final String TCP_PORT_PARAMETER = "port";
    public static final String UDN_PARAMETER = "udn";
    public static final String REFRESH_INTERVAL = "refreshInterval";

    // List of all Channel ids
    public static final String VOLUME = "volume";
    public static final String MUTE = "mute";
    public static final String CONTROL = "control";
    public static final String STOP = "stop";
    public static final String CURRENTTITLE = "currenttitle";
    public static final String UPNPRENDERER = "upnprenderer";
    public static final String UPNPSERVER = "upnpserver";

}
