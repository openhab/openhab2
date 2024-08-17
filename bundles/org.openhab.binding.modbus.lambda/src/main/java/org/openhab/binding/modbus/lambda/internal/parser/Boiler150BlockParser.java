/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.modbus.lambda.internal.parser;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.io.transport.modbus.ModbusRegisterArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses inverter modbus data into an Energy Block
 *
 * @author Paul Frank - Initial contribution
 * @author Christian Koch - modified for lambda heat pump based on stiebeleltron binding for modbus
 *
 */
@NonNullByDefault
public class Boiler150BlockParser extends AbstractBaseParser {
    private final Logger logger = LoggerFactory.getLogger(Boiler150BlockParser.class);

    public Boiler150Block parse(ModbusRegisterArray raw) {
        // logger.trace("Boiler150BlockParser");
        Boiler150Block block = new Boiler150Block();

        block.boiler150MaximumBoilerTemperature = extractUInt16(raw, 2, (short) 0);
        return block;
    }
}
