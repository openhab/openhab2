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
package org.openhab.binding.caso.internal;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.core.library.types.DateTimeType;

/**
 * The {@link TestHandler} is testing handler functions
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
class TestHandler {

    @Test
    void test() {
        String dtUTC = "2024-08-14T11:52:43.7170897Z";
        Instant timestamp = Instant.parse(dtUTC);
        ZoneId zone = ZoneId.of("Europe/Berlin");
        ZonedDateTime zdt = timestamp.atZone(zone);
        System.out.println(zdt);
        System.out.println(new DateTimeType(zdt));
        // String dtUTC = "2024-08-13T23:25:32.2382092Z";
        // DateTimeType dtt = DateTimeType.valueOf(dtUTC);
        // System.out.println(dtt);
    }
}
