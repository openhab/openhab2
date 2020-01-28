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
package org.openhab.binding.linky.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link LinkyBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class LinkyBindingConstants {

    public static final String BINDING_ID = "linky";
    public static final String LOCAL = "local";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_LINKY = new ThingTypeUID(BINDING_ID, "linky");

    // List of all Channel id's
    public static final String LAST_HOUR = "lastHour";
    public static final String TODAY = "today";
    public static final String YESTERDAY = "daily#yesterday";
    public static final String THIS_WEEK = "daily#thisWeek";
    public static final String LAST_WEEK = "daily#lastWeek";
    public static final String THIS_MONTH = "monthly#thisMonth";
    public static final String LAST_MONTH = "monthly#lastMonth";
    public static final String THIS_YEAR = "yearly#thisYear";
    public static final String LAST_YEAR = "yearly#lastYear";

}
