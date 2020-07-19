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
package org.openhab.binding.lcn.internal.converter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.lcn.internal.common.LcnException;

/**
 * Base class for all converters.
 *
 * @author Fabian Wolter - Initial Contribution
 */
@NonNullByDefault
public interface Converter {
    /**
     * Converts a human readable value into LCN native value.
     *
     * @param humanReadable value to convert
     * @return the native LCN value
     */
    public DecimalType onCommandFromItem(double humanReadable);

    /**
     * Converts a human readable value into LCN native value.
     *
     * @param humanReadable value to convert
     * @return the native LCN value
     * @throws LcnException when the value could not be converted to the base unit
     */
    public DecimalType onCommandFromItem(QuantityType<?> quantityType) throws LcnException;

    /**
     * Converts a state update from the Thing into a human readable unit.
     *
     * @param state from the Thing
     * @return human readable State
     */
    public State onStateUpdateFromHandler(State state);
}
