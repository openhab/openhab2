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
package org.openhab.binding.digitalstrom.internal.lib.event.types;

import java.util.Map;

import org.openhab.binding.digitalstrom.internal.lib.event.constants.EventResponseEnum;

/**
 * The {@link EventItem} represents a event item of an digitalSTROM-Event.
 *
 * @author Alexander Betker
 * @author Michael Ochel - add getSource()
 * @author Matthias Siegele - add getSource()
 */
public interface EventItem {

    /**
     * Returns the name of this {@link EventItem}.
     *
     * @return name of this {@link EventItem}
     */
    String getName();

    /**
     * Returns {@link HashMap} with the properties fiels of this {@link EventItem}.
     * The key is a {@link EventResponseEnum} and represents the property name
     * and the value is the property value.
     *
     * @return the properties of this {@link EventItem}
     */
    Map<EventResponseEnum, String> getProperties();

    /**
     * Returns {@link HashMap} with the source fields of this {@link EventItem}.
     * The key is a {@link EventResponseEnum} and represents the property name
     * and the value is the property value.
     *
     * @return the properties of this {@link EventItem}
     */
    Map<EventResponseEnum, String> getSource();
}
