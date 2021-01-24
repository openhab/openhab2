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
package org.openhab.binding.mikrotik.internal.util;

import java.math.BigInteger;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.joda.time.DateTime;

/**
 * The {@link StateUtil} class holds static methods to cast Java native/class types to OpenHAB values
 *
 * @author Oleg Vivtash - Initial contribution
 */
@NonNullByDefault
public class StateUtil {

    public static State stringOrNull(@Nullable String value) {
        return value == null ? UnDefType.NULL : new StringType(value);
    }

    public static State intOrNull(@Nullable Integer value) {
        return value == null ? UnDefType.NULL : new DecimalType(value.floatValue());
    }

    public static State bigIntOrNull(@Nullable BigInteger value) {
        return value == null ? UnDefType.NULL : DecimalType.valueOf(value.toString());
    }

    public static State floatOrNull(@Nullable Float value) {
        return value == null ? UnDefType.NULL : new DecimalType(value);
    }

    public static State boolOrNull(@Nullable Boolean value) {
        if (value == null)
            return UnDefType.NULL;
        return value ? OnOffType.ON : OnOffType.OFF;
    }

    public static State timeOrNull(@Nullable DateTime value) {
        return value == null ? UnDefType.NULL : new DateTimeType(value.toGregorianCalendar().toZonedDateTime());
    }
}
