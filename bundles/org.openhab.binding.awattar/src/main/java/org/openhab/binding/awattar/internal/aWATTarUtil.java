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
package org.openhab.binding.awattar.internal;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.library.types.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some utility methods
 *
 * @author Wolfgang Klimt - initial contribution
 */
@NonNullByDefault
public class aWATTarUtil {

    private static final Logger logger = LoggerFactory.getLogger(aWATTarUtil.class);

    public static long getMillisToNextMinute(int mod) {

        long now = Instant.now().toEpochMilli();
        ZonedDateTime dt = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        int min = dt.getMinute();
        int offset = min % mod;
        offset = offset == 0 ? mod : offset;
        dt = dt.plusMinutes(offset);
        long result = dt.toInstant().toEpochMilli() - now;
        logger.trace("Now: {}, mod: {}, Target Time: {}, difference: {}", now, mod, dt.toString(), result);

        return result;
    }

    public static ZonedDateTime getCalendarForHour(int hour, ZoneId zone) {
        return ZonedDateTime.now(zone).truncatedTo(ChronoUnit.DAYS).plus(hour, ChronoUnit.HOURS);
    }

    public static DateTimeType getDateTimeType(long time, TimeZoneProvider tz) {
        return new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), tz.getTimeZone()));
    }

    public static String getDuration(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static String formatDate(long date, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(date), zoneId).toString();
    }

    public static String getHourFrom(long timestamp, ZoneId zoneId) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        return String.format("%02d", zdt.getHour());
    }
}
