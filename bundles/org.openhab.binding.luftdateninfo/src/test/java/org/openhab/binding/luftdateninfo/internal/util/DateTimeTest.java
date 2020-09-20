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
package org.openhab.binding.luftdateninfo.internal.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Test;
import org.openhab.binding.luftdateninfo.internal.utils.DateTimeUtils;

/**
 * The {@link DateTimeTest} Test DateTimeFormatter provided in utils package
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class DateTimeTest {

    @Test
    public void testJSonTime() {
        String jsonDateString = "2020-08-14 14:53:21";
        try {
            LocalDateTime dt = LocalDateTime.from(DateTimeUtils.DTF.parse(jsonDateString));
            assertEquals("Day ", 14, dt.getDayOfMonth());
            assertEquals("Month ", 8, dt.getMonthValue());
            assertEquals("Year ", 2020, dt.getYear());

            String s = dt.format(DateTimeUtils.DTF);
            assertEquals("String ", jsonDateString, s);
        } catch (DateTimeParseException e) {
            assertFalse(true);
        }
    }
}
