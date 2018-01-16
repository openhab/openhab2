/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mihome.handler;

import static org.openhab.binding.mihome.XiaomiGatewayBindingConstants.*;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Thing;

import com.google.gson.JsonObject;

/**
 * Handles the Xiaomi temperature & humidity sensor
 *
 * @author Patrick Boos - Initial contribution
 * @author Daniel Walters - Add pressure support
 */
public class XiaomiSensorHtHandler extends XiaomiSensorBaseHandler {

    private static final String HUMIDITY = "humidity";
    private static final String TEMPERATURE = "temperature";
    private static final String VOLTAGE = "voltage";
    private static final String PRESSURE = "pressure";

    public XiaomiSensorHtHandler(Thing thing) {
        super(thing);
    }

    @Override
    void parseReport(JsonObject data) {
        parseDefault(data);
    }

    @Override
    void parseHeartbeat(JsonObject data) {
        parseDefault(data);
    }

    @Override
    void parseReadAck(JsonObject data) {
        parseDefault(data);
    }

    @Override
    void parseDefault(JsonObject data) {
        if (data.has(HUMIDITY)) {
            float humidity = data.get(HUMIDITY).getAsFloat() / 100;
            updateState(CHANNEL_HUMIDITY, new DecimalType(humidity));
        }
        if (data.has(TEMPERATURE)) {
            float temperature = data.get(TEMPERATURE).getAsFloat() / 100;
            updateState(CHANNEL_TEMPERATURE, new DecimalType(temperature));
        }
        if (data.has(VOLTAGE)) {
            Integer voltage = data.get(VOLTAGE).getAsInt();
            calculateBatteryLevelFromVoltage(voltage);
        }
        if (data.has(PRESSURE)) {
            float pressure = (float) (data.get(PRESSURE).getAsFloat() / 1000.0);
            updateState(CHANNEL_PRESSURE, new DecimalType(pressure));
        }
    }
}
