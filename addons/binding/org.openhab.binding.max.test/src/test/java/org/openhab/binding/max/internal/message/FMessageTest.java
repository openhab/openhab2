/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.max.internal.message;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests cases for {@link FMessage}.
 *
 * @author Marcel Verpaalen - Initial contribution
 */
public class FMessageTest {

    public static final String RAW_DATA = "F:nl.ntp.pool.org,ntp.homematic.com";

    private FMessage message;

    @Before
    public void before() {
        message = new FMessage(RAW_DATA);
    }

    @Test
    public void getMessageTypeTest() {
        MessageType messageType = ((Message) message).getType();
        assertEquals(MessageType.F, messageType);
    }

    @Test
    public void getServer1Test() {
        String ntpServer1 = message.getNtpServer1();
        assertEquals("nl.ntp.pool.org", ntpServer1);
    }

    @Test
    public void getServer2Test() {
        String ntpServer1 = message.getNtpServer2();
        assertEquals("ntp.homematic.com", ntpServer1);
    }
}
