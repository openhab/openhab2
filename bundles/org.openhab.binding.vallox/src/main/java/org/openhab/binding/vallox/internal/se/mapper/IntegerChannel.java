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
package org.openhab.binding.vallox.internal.se.mapper;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;

/**
 * Class for channels holding integer values.
 *
 * @author Miika Jukka - Initial contribution
 */
@NonNullByDefault
public class IntegerChannel extends ValloxChannel {

    /**
     * Create new instance.
     *
     * @param variable channel as byte
     */
    public IntegerChannel(byte variable) {
        super(variable);
    }

    @Override
    public State convertToState(byte value) {
        return new DecimalType(Byte.toUnsignedInt(value));
    }

    public byte convertFromState(Integer value) {
        return value.byteValue();
    }

    /**
     * Class for channels holding humidity values.
     *
     * @author Miika Jukka - Initial contributor
     */
    public static class Humidity extends IntegerChannel {

        /**
         * Create new instance.
         *
         * @param variable channel as byte
         */
        public Humidity(byte variable) {
            super(variable);
        }

        @Override
        public State convertToState(byte value) {
            int index = Byte.toUnsignedInt(value);
            return new DecimalType((index - 51) / 2.04);
        }

        @Override
        public byte convertFromState(byte value) {
            double index = value * 2.04;
            index += 51;
            return (byte) Math.round(index);
        }
    }

    /**
     * Class for channels holding counter values.
     *
     * @author Miika Jukka - Initial contributor
     */
    public static class Counter extends IntegerChannel {

        /**
         * Create new instance.
         *
         * @param variable channel as byte
         */
        public Counter(byte variable) {
            super(variable);
        }

        @Override
        public State convertToState(byte value) {
            return new DecimalType(Byte.toUnsignedInt(value));
        }

        @Override
        public byte convertFromState(byte value) {
            return 0x00; // Not used
        }
    }
}
