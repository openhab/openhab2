/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.RFXComBindingConstants.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComRFXSensorMessage.SubType.*;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.junit.Test;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;
import org.openhab.binding.rfxcom.internal.handler.DeviceState;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden
 */
public class RFXComRFXSensorMessageTest {
    private final MockDeviceState mockedDeviceState = new MockDeviceState();

    private void testMessage(String hexMsg, RFXComRFXSensorMessage.SubType subType, int seqNbr, String deviceId,
            Double temperature, Double voltage, Double referenceVoltage, Double expectedPressure,
            Double expectedHumidity, int signalLevel, DeviceState deviceState) throws RFXComException {
        final RFXComRFXSensorMessage msg = (RFXComRFXSensorMessage) RFXComMessageFactory
                .createMessage(DatatypeConverter.parseHexBinary(hexMsg));
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", seqNbr, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", deviceId, msg.getDeviceId());
        assertEquals("Signal Level", signalLevel, msg.signalLevel);
        assertEquals("Temperature", temperature, getMessageTemperature(msg, deviceState));
        assertEquals("Voltage", voltage, getChannelAsDouble(CHANNEL_VOLTAGE, msg, deviceState));
        assertEquals("Reference Voltage", referenceVoltage,
                getChannelAsDouble(CHANNEL_REFERENCE_VOLTAGE, msg, deviceState));
        assertEquals("Humidity", expectedHumidity, getChannelAsDouble(CHANNEL_HUMIDITY, msg, deviceState));
        assertEquals("Pressure", expectedPressure, getChannelAsDouble(CHANNEL_PRESSURE, msg, deviceState));

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("0770000008080270", TEMPERATURE, 0, "8", 20.5d, null, null, null, null, 7, mockedDeviceState);
        testMessage("0770000208809650", TEMPERATURE, 2, "8", -1.5d, null, null, null, null, 5, mockedDeviceState);
        testMessage("077002010801F270", VOLTAGE, 1, "8", null, null, 4.98, null, null, 7, mockedDeviceState);
        testMessage("077001020800F470", A_D, 2, "8", null, 2.44, null, null, null, 7, mockedDeviceState);
    }

    @Test
    public void testPressure() throws RFXComException {
        MockDeviceState deviceState = new MockDeviceState();
        deviceState.set(CHANNEL_REFERENCE_VOLTAGE, new DecimalType(4.98));

        testMessage("077001020800F470", A_D, 2, "8", null, 2.44, null, 650.0, null, 7, deviceState);
    }

    @Test
    public void testHumidity() throws RFXComException {
        MockDeviceState deviceState = new MockDeviceState();
        deviceState.set(CHANNEL_TEMPERATURE, new DecimalType(20.5));
        deviceState.set(CHANNEL_REFERENCE_VOLTAGE, new DecimalType(4.98));

        testMessage("077001020800F470", A_D, 2, "8", null, 2.44, null, 650.0, 52.6821, 7, deviceState);
    }

    private Double getChannelAsDouble(String channelId, RFXComRFXSensorMessage msg, DeviceState deviceState)
            throws RFXComException {
        return getStateAsDouble(msg.convertToState(channelId, deviceState));
    }

    private Double getMessageTemperature(RFXComRFXSensorMessage msg, DeviceState deviceState) throws RFXComException {
        return getChannelAsDouble(CHANNEL_TEMPERATURE, msg, deviceState);
    }

    private Double getStateAsDouble(State state) {
        if (state instanceof DecimalType) {
            return ((DecimalType) state).doubleValue();
        } else {
            return null;
        }
    }
}
