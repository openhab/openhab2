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

import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import tuwien.auto.calimero.dptxlator.DPTXlator3BitControlled;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorRGB;

/**
 * color channel type description
 *
 * @author Helmut Lehmeyer - initial contribution
 *
 */
@NonNullByDefault
class TypeColor extends KNXChannelType {

    TypeColor() {
        super(CHANNEL_COLOR, CHANNEL_COLOR_CONTROL);
    }

    @Override
    protected Set<String> getAllGAKeys() {
        return Stream.of(SWITCH_GA, POSITION_GA, INCREASE_DECREASE_GA, HSB_GA).collect(toSet());
    }

    @Override
    protected String getDefaultDPT(String gaConfigKey) {
        if (gaConfigKey.equals(HSB_GA)) {
            return DPTXlatorRGB.DPT_RGB.getID();
        }
        if (gaConfigKey.equals(INCREASE_DECREASE_GA)) {
            return DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID();
        }
        if (gaConfigKey.equals(SWITCH_GA)) {
            return DPTXlatorBoolean.DPT_SWITCH.getID();
        }
        if (gaConfigKey.equals(POSITION_GA)) {
            return DPTXlator8BitUnsigned.DPT_SCALING.getID();
        }
        throw new IllegalArgumentException("GA configuration '" + gaConfigKey + "' is not supported");
    }
}
