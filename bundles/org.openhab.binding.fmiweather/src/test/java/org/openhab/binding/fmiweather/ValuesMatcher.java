/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.fmiweather;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@NonNullByDefault
public class ValuesMatcher extends TypeSafeMatcher<@Nullable BigDecimal[]> {

    private Object[] values;

    public ValuesMatcher(String... values) {
        this.values = Stream.of(values).map(s -> s == null ? null : new BigDecimal(s)).toArray();
    }

    @SuppressWarnings("null")
    @Override
    public void describeTo(@Nullable Description description) {
        description.appendText(Arrays.deepToString(values));
    }

    @Override
    protected boolean matchesSafely(@Nullable BigDecimal[] data) {
        return Objects.deepEquals(data, values);
    }

}
