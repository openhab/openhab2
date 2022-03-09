/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.tplinkrouter.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link TpLinkRouterBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Olivier Marceau - Initial contribution
 */
@NonNullByDefault
public class TpLinkRouterBindingConstants {

    private static final String BINDING_ID = "tplinkrouter";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_TDW9970 = new ThingTypeUID(BINDING_ID, "TD-W9970");

    // List of all Channel ids
    public static final String WIFI_STATUS = "wifi#Status";
    public static final String WIFI_SSID = "wifi#SSID";
    public static final String WIFI_BANDWIDTH = "wifi#bandWidth";
    public static final String WIFI_QSS = "wifi#QSS";
    public static final String WIFI_SECMODE = "wifi#SecMode";
    public static final String WIFI_KEY = "wifi#Key";
}
