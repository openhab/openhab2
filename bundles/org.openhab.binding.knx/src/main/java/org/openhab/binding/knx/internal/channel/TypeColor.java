/**
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.knx.internal.channel;

import static org.openhab.binding.knx.internal.KNXBindingConstants.*;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.Channel;

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
class TypeColor extends KNXChannel {
    public static final Set<String> SUPPORTED_CHANNEL_TYPES = Set.of(CHANNEL_COLOR, CHANNEL_COLOR_CONTROL);

    TypeColor(Channel channel) {
        super(List.of(SWITCH_GA, POSITION_GA, INCREASE_DECREASE_GA, HSB_GA),
                List.of(HSBType.class, PercentType.class, OnOffType.class, IncreaseDecreaseType.class), channel);
    }

    @Override
    protected String getDefaultDPT(String gaConfigKey) {
        return switch (gaConfigKey) {
            case HSB_GA -> DPTXlatorRGB.DPT_RGB.getID();
            case INCREASE_DECREASE_GA -> DPTXlator3BitControlled.DPT_CONTROL_DIMMING.getID();
            case SWITCH_GA -> DPTXlatorBoolean.DPT_SWITCH.getID();
            case POSITION_GA -> DPTXlator8BitUnsigned.DPT_SCALING.getID();
            default -> throw new IllegalArgumentException("GA configuration '" + gaConfigKey + "' is not supported");
        };
    }
}
