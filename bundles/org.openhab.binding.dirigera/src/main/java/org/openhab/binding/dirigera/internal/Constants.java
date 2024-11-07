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
package org.openhab.binding.dirigera.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link Constants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class Constants {
    public static final String BINDING_ID = "dirigera";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_GATEWAY = new ThingTypeUID(BINDING_ID, "gateway");
    public static final ThingTypeUID THING_TYPE_UNKNNOWN = new ThingTypeUID(BINDING_ID, "unkown");
    public static final ThingTypeUID THING_TYPE_COLOR_LIGHT = new ThingTypeUID(BINDING_ID, "color-light");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_GATEWAY,
            THING_TYPE_COLOR_LIGHT);

    public static final Set<ThingTypeUID> DISCOVERABLE_DEVICE_TYPE_UIDS = Collections
            .unmodifiableSet(Stream.of(THING_TYPE_GATEWAY, THING_TYPE_COLOR_LIGHT).collect(Collectors.toSet()));

    public static final String WS_URL = "wss://%s:8443/v1";
    public static final String BASE_URL = "https://%s:8443/v1";
    public static final String OAUTH_URL = BASE_URL + "/oauth/authorize";
    public static final String TOKEN_URL = BASE_URL + "/oauth/token";
    public static final String HOME_URL = BASE_URL + "/home";
    public static final String DEVICE_URL = BASE_URL + "/devices/%s";

    public static final String PROPERTY_IP_ADDRESS = "ipAddress";
    public static final String PROPERTY_DEVICES = "devices";
    public static final String PROPERTY_DEVICE_ID = "id";
    public static final String PROPERTY_DEVICE_TYPE = "type";
    public static final String PROPERTY_TOKEN = "token";
    public static final String PROPERTY_EMPTY = "";
    public static final String PROPERTY_LIGHT = "light";
    public static final String PROPERTY_ATTRIBUTES = "attributes";
    public static final String PROPERTY_HTTP_ERROR_STATUS = "http-error-status";

    public static final String ATTRIBUTE_COLOR_MODE = "colorMode";

    public static final String DEVICE_TYPE_GATEWAY = "gateway";

    public static final String CHANNEL_STATISTICS = "statistics";
    public static final String CHANNEL_LIGHT_HSB = "hsb";

    public static final String EVENT_TYPE_STATE_CHANGE = "deviceStateChanged";

    public static final Map<String, String> COLOR_LIGHT_MAP = Map.of("isOn", "on", "lightLevel", CHANNEL_LIGHT_HSB,
            "colorHue", CHANNEL_LIGHT_HSB, "colorSaturation", CHANNEL_LIGHT_HSB, "colorTemperature",
            "color-temperature");
}
