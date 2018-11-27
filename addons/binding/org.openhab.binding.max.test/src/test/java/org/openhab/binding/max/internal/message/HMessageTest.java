/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.max.internal.message;

import static org.junit.Assert.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.max.internal.Utils;

/**
 * Tests cases for {@link HMessage}.
 *
 * @author Marcel Verpaalen - Initial contribution
 */
public class HMessageTest {

    public static final String RAW_DATA = "H:KEQ0565026,0b5951,0113,00000000,4eed6795,01,32,12080a,070f,03,0000";

    private HMessage message;

    @Before
    public void before() {
        message = new HMessage(RAW_DATA);
    }

    @Test
    public void getMessageTypeTest() {
        MessageType messageType = ((Message) message).getType();
        assertEquals(MessageType.H, messageType);
    }

    @Test
    public void getRFAddressTest() {
        String rfAddress = message.getRFAddress();
        assertEquals("0b5951", rfAddress);
    }

    @Test
    public void getFirmwareTest() {
        String firmware = message.getFirmwareVersion();
        assertEquals("01.13", firmware);
    }

    @Test
    public void getConnectionIdTest() {
        String connectionId = message.getConnectionId();
        assertEquals("4eed6795", connectionId);
    }

    @Test
    public void getCubeTimeStateTest() {
        String cubeTimeState = message.getCubeTimeState();
        assertEquals("03", cubeTimeState);
    }

    @Test
    public void testParseDateTime() {
        String[] tokens = RAW_DATA.split(Message.DELIMETER);

        String hexDate = tokens[7];
        String hexTime = tokens[8];

        int year = Utils.fromHex(hexDate.substring(0, 2));
        int month = Utils.fromHex(hexDate.substring(2, 4));
        int dayOfMonth = Utils.fromHex(hexDate.substring(4, 6));
        assertEquals(18, year);
        assertEquals(8, month);
        assertEquals(10, dayOfMonth);

        int hours = Utils.fromHex(hexTime.substring(0, 2));
        int minutes = Utils.fromHex(hexTime.substring(2, 4));
        assertEquals(7, hours);
        assertEquals(15, minutes);
    }

    @Test
    public void testGetDateTime() {
        Date dateTime = message.getDateTime();
        assertEquals(Date.from(ZonedDateTime.of(2018, 8, 10, 7, 15, 0, 0, ZoneId.systemDefault()).toInstant()),
                dateTime);
    }

    @Test
    public void getNTPCounterTest() {
        String ntpCounter = message.getNTPCounter();
        assertEquals("0", ntpCounter);
    }

    @Test
    public void getSerialNumberTest() {
        String serialNumber = message.getSerialNumber();
        assertEquals("KEQ0565026", serialNumber);
    }
}
