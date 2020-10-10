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
package org.openhab.binding.deconz.internal.types;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ResourceType} defines an enum for websocket messages
 *
 * @author Jan N. Klug - Initial contribution
 */
public enum ResourceType {
    GROUPS("groups"),
    LIGHTS("lights"),
    SENSORS("sensors"),
    UNKNOWN("");

    private static final Map<String, ResourceType> MAPPING = Arrays.stream(ResourceType.values())
            .collect(Collectors.toMap(v -> v.type, v -> v));
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceType.class);

    private String type;

    ResourceType(String type) {
        this.type = type;
    }

    public static ResourceType fromString(String s) {
        ResourceType lightType = MAPPING.getOrDefault(s, UNKNOWN);
        if (lightType == UNKNOWN) {
            LOGGER.debug("Unknown resource type '{}' found. This should be reported.", s);
        }
        return lightType;
    }
}
