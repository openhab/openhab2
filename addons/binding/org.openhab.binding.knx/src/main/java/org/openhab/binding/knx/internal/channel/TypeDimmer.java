/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.channel;

import static java.util.stream.Collectors.toSet;
import static org.openhab.binding.knx.KNXBindingConstants.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

/**
 * dimmer channel type description
 *
 * @author Simon Kaufmann - initial contribution and API.
 *
 */
@NonNullByDefault
class TypeDimmer extends KNXChannelType {

    TypeDimmer() {
        super(CHANNEL_DIMMER, CHANNEL_DIMMER_CONTROL);
    }

    @Override
    protected Set<String> getAllGAKeys() {
        return Stream.of(SWITCH_GA, POSITION_GA, INCREASE_DECREASE_GA).collect(toSet());
    }

    @Override
    protected String getDefaultDPT(String gaConfigKey) {
        if (Objects.equals(gaConfigKey, INCREASE_DECREASE_GA)) {
            return DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID();
        }
        if (Objects.equals(gaConfigKey, SWITCH_GA)) {
            return DPTXlatorBoolean.DPT_SWITCH.getID();
        }
        if (Objects.equals(gaConfigKey, POSITION_GA)) {
            return DPTXlator8BitUnsigned.DPT_SCALING.getID();
        }
        throw new IllegalArgumentException("GA configuration '" + gaConfigKey + "' is not supported");
    }
}
