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
package org.openhab.binding.smarther.internal.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link DateUtil} class defines common Date utility functions, which are used across the whole binding.
 *
 * @author Fabio Possieri - Initial contribution
 */
@NonNullByDefault
public class DateUtil {

    private static final String RANGE_FORMAT = "%s/%s";

    public static LocalDateTime parse(@Nullable String str, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, dtf);
    }

    public static String format(LocalDateTime date, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return date.format(dtf);
    }

    public static String formatRange(LocalDateTime date1, LocalDateTime date2, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return String.format(RANGE_FORMAT, date1.format(dtf), date2.format(dtf));
    }

}
