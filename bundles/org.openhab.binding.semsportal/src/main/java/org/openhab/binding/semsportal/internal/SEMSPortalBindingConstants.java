/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.semsportal.internal;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link SEMSPortalBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Iwan Bron - Initial contribution
 */
@NonNullByDefault
public class SEMSPortalBindingConstants {

    private static final String BINDING_ID = "semsportal";
    static final String DATE_FORMAT = "MM.dd.yyyy HH:mm:ss";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_STATION = new ThingTypeUID(BINDING_ID, "station");

    // List of all Channel ids
    public static final String CHANNEL_ONLINE_STATE = "online_state";
    public static final String CHANNEL_CURRENT_OUTPUT = "current_output";
    public static final String CHANNEL_LASTUPDATE = "last_update";
    public static final String CHANNEL_TODAY_TOTAL = "today_total";
    public static final String CHANNEL_MONTH_TOTAL = "month_total";
    public static final String CHANNEL_OVERALL_TOTAL = "overall_total";
    public static final String CHANNEL_TODAY_INCOME = "today_income";
    public static final String CHANNEL_TOTAL_INCOME = "total_income";

    protected static final List<String> ALL_CHANNELS = Arrays.asList(CHANNEL_ONLINE_STATE, CHANNEL_LASTUPDATE,
            CHANNEL_CURRENT_OUTPUT, CHANNEL_TODAY_TOTAL, CHANNEL_MONTH_TOTAL, CHANNEL_OVERALL_TOTAL,
            CHANNEL_TODAY_INCOME, CHANNEL_TOTAL_INCOME);
}
