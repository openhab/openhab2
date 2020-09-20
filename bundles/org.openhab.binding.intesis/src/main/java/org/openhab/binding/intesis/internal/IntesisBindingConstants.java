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
package org.openhab.binding.intesis.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link IntesisBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Hans-Jörg Merk - Initial contribution
 */
@NonNullByDefault
public class IntesisBindingConstants {

    public static final String BINDING_ID = "intesis";

    public static final int INTESIS_HTTP_API_TIMEOUT_MS = 5000;
    public static final int INTESIS_REFRESH_INTERVAL_SEC = 30;

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_INTESISHOME = new ThingTypeUID(BINDING_ID, "intesisHome");

    // List of all Channel ids
    public static final String CHANNEL_TYPE_POWER = "power";
    public static final String CHANNEL_TYPE_MODE = "mode";
    public static final String CHANNEL_TYPE_FANSPEED = "fanSpeed";
    public static final String CHANNEL_TYPE_VANESUD = "vanesUpDown";
    public static final String CHANNEL_TYPE_VANESLR = "vanesLeftRight";
    public static final String CHANNEL_TYPE_TARGETTEMP = "targetTemperature";
    public static final String CHANNEL_TYPE_AMBIENTTEMP = "ambientTemperature";
    public static final String CHANNEL_TYPE_OUTDOORTEMP = "outdoorTemperature";
}
