/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.enocean.internal.eep.A5_09;

import static org.openhab.binding.enocean.internal.EnOceanBindingConstants.*;

import java.util.function.Function;

import org.openhab.binding.enocean.internal.eep.EEPHelper;
import org.openhab.binding.enocean.internal.messages.ERP1Message;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.State;

/**
 *
 * @author Zhivka Dimova - Initial contribution
 */
public class A5_09_0D extends A5_09 {

    public A5_09_0D(ERP1Message packet) {
        super(packet);
    }

    @Override
    protected double getUnscaledTemperatureMax() {
        return 250;
    }

    @Override
    protected State convertToStateImpl(String channelId, String channelTypeId,
            Function<String, State> getCurrentStateFunc, Configuration config) {

        if (CHANNEL_TVOC.equals(channelId)) {
            return new QuantityType<>(EEPHelper.calculateScaledValue(getDB_2Value(), 0, 100, 0, 200), Units.PERCENT);
        }

        return super.convertToStateImpl(channelId, channelTypeId, getCurrentStateFunc, config);
    }
}
