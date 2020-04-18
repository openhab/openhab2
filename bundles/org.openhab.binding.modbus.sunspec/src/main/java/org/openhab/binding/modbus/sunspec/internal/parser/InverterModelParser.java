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
package org.openhab.binding.modbus.sunspec.internal.parser;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.modbus.sunspec.internal.SunSpecConstants;
import org.openhab.binding.modbus.sunspec.internal.dto.InverterModelBlock;
import org.openhab.io.transport.modbus.ModbusRegisterArray;

/**
 * Parses inverter modbus data into an InverterModelBlock
 *
 * @author Nagy Attila Gabor - Initial contribution
 *
 */
@NonNullByDefault
public class InverterModelParser extends AbstractBaseParser implements SunspecParser<InverterModelBlock> {

    @Override
    public InverterModelBlock parse(ModbusRegisterArray raw) {
        InverterModelBlock block = new InverterModelBlock();

        block.phaseConfiguration = extractUInt16(raw, 0, SunSpecConstants.INVERTER_SINGLE_PHASE);
        block.length = extractUInt16(raw, 1, raw.size());
        block.acCurrentTotal = extractUInt16(raw, 2, 0);
        block.acCurrentSF = extractSunSSF(raw, 6);
        block.acPower = extractInt16(raw, 14, (short) 0);
        block.acPowerSF = extractSunSSF(raw, 15);
        block.acFrequency = extractUInt16(raw, 16, 0);
        block.acFrequencySF = extractSunSSF(raw, 17);
        block.acApparentPower = extractOptionalInt16(raw, 18);
        block.acApparentPowerSF = extractOptionalSunSSF(raw, 19);
        block.acReactivePower = extractOptionalInt16(raw, 20);
        block.acReactivePowerSF = extractOptionalSunSSF(raw, 21);
        block.acPowerFactor = extractOptionalInt16(raw, 22);
        block.acPowerFactorSF = extractOptionalSunSSF(raw, 23);
        block.acEnergyLifetime = extractAcc32(raw, 24, 0);
        block.acEnergyLifetimeSF = extractSunSSF(raw, 26);
        block.temperatureCabinet = extractInt16(raw, 33, (short) 0);
        block.temperatureHeatsink = extractOptionalInt16(raw, 34);
        block.temperatureTransformer = extractOptionalInt16(raw, 35);
        block.temperatureOther = extractOptionalInt16(raw, 36);
        block.temperatureSF = extractSunSSF(raw, 37);
        block.status = extractUInt16(raw, 38, 1);
        block.statusVendor = extractOptionalUInt16(raw, 39);

        return block;
    }

}
