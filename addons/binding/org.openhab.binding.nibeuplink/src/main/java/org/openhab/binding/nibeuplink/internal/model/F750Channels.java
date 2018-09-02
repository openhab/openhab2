/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeuplink.internal.model;

import org.eclipse.smarthome.core.library.unit.MetricPrefix;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;

/**
 * list of all available channels
 *
 * @author Alexander Friese - initial contribution
 */
public final class F750Channels extends BaseChannels {

    /**
     * singleton
     */
    private static final F750Channels INSTANCE = new F750Channels();

    /**
     * Returns the unique instance of this class.
     *
     * @return the Units instance.
     */
    public static F750Channels getInstance() {
        return INSTANCE;
    }

    /**
     * singleton should not be instantiated from outside
     */
    private F750Channels() {
    }

    // General
    // currently no general channels

    // Compressor
    public static final Channel CH_43181 = INSTANCE.addChannel(
            new QuantityChannel("43181", "Chargepump speed", ChannelGroup.COMPRESSOR, SmartHomeUnits.PERCENT));
    public static final Channel CH_43424 = INSTANCE.addChannel(
            new QuantityChannel("43424", "EB100-EP14 Tot. HW op.time compr", ChannelGroup.COMPRESSOR, SIUnits.HOUR));
    public static final Channel CH_43420 = INSTANCE.addChannel(
            new QuantityChannel("43420", "EB100-EP14 Tot. op.time compr", ChannelGroup.COMPRESSOR, SIUnits.HOUR));
    public static final Channel CH_43416 = INSTANCE
            .addChannel(new Channel("43416", "EB100-EP14 Compressor starts", ChannelGroup.COMPRESSOR));
    public static final Channel CH_40022 = INSTANCE.addChannel(new QuantityChannel("40022", "EB100-EP14-BT17 Suction",
            ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_40019 = INSTANCE.addChannel(new QuantityChannel("40019",
            "EB100-EP14-BT15 Liquid Line", ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_40018 = INSTANCE.addChannel(new QuantityChannel("40018",
            "EB100-EP14-BT14 Hot Gas Temp", ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_40017 = INSTANCE.addChannel(new QuantityChannel("40017",
            "EB100-EP14-BT12 Condensor Out", ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_40020 = INSTANCE.addChannel(new QuantityChannel("40020",
            "EB100-EP14-BT16 Evaporator", ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_43136 = INSTANCE.addChannel(new QuantityChannel("43136",
            "Compressor Frequency, Actual", ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SmartHomeUnits.HERTZ)));
    public static final Channel CH_43122 = INSTANCE.addChannel(new QuantityChannel("43122", "Compr. current min.freq.",
            ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SmartHomeUnits.HERTZ)));
    public static final Channel CH_43123 = INSTANCE.addChannel(new QuantityChannel("43123", "Compr. current max.freq.",
            ChannelGroup.COMPRESSOR, MetricPrefix.DECI(SmartHomeUnits.HERTZ)));

    // Airsupply
    public static final Channel CH_40025 = INSTANCE.addChannel(new QuantityChannel("40025", "BT20 Exhaust air temp. 1",
            ChannelGroup.AIRSUPPLY, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_40026 = INSTANCE.addChannel(new QuantityChannel("40026", "BT21 Vented air temp. 1",
            ChannelGroup.AIRSUPPLY, MetricPrefix.DECI(SIUnits.CELSIUS)));
    public static final Channel CH_43124 = INSTANCE
            .addChannel(new ScaledChannel("43124", "Air flow ref.", ChannelGroup.AIRSUPPLY, ScaledChannel.F01));
    public static final Channel CH_41026 = INSTANCE
            .addChannel(new Channel("41026", "EB100-Adjusted BS1 Air flow", ChannelGroup.AIRSUPPLY));
    public static final Channel CH_47260 = INSTANCE
            .addChannel(new Channel("47260", "Current Fan speed", ChannelGroup.AIRSUPPLY, "/Manage/1.2", "[01234]"));

}
