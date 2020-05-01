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
package org.openhab.io.imperihome.internal.model.device;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.io.imperihome.internal.model.param.NumericValueParam;
import org.openhab.io.imperihome.internal.model.param.ParamType;

/**
 * Noise sensor device.
 *
 * @author Pepijn de Geus - Initial contribution
 */
public class NoiseDevice extends AbstractNumericValueDevice {

    public NoiseDevice(Item item) {
        super(DeviceType.NOISE, item, "dB");
    }

    @Override
    public void stateUpdated(Item item, State newState) {
        super.stateUpdated(item, newState);

        DecimalType value = (DecimalType) item.getStateAs(DecimalType.class);
        addParam(new NumericValueParam(ParamType.NOISE_VALUE, getUnit(), value));
    }

}
