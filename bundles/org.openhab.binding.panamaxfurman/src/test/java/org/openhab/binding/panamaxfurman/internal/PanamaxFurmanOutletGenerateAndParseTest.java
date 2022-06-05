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
package org.openhab.binding.panamaxfurman.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Dave Badia - Initial contribution
 */
@NonNullByDefault
public class PanamaxFurmanOutletGenerateAndParseTest {

    @ParameterizedTest
    @CsvSource(value = { "1,outlet1#power", "null, blah" }, nullValues = { "null" })
    public void testParse(Integer expected, String channelUID) {
        Integer result = PanamaxFurmanAbstractHandler.getOutletFromChannelUID(channelUID);
        assertEquals(expected, result);
    }
}
