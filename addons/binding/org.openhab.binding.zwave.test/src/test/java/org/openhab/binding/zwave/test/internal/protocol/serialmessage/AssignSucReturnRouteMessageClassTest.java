/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.test.internal.protocol.serialmessage;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.serialmessage.AssignSucReturnRouteMessageClass;

/**
 * Test cases for AssignSucReturnRouteMessageClass message.
 * This takes some example packets, processes them, and checks that the processing is correct.
 *
 * @author Chris Jackson
 *
 */
public class AssignSucReturnRouteMessageClassTest {
    @Test
    public void doRequest() {
        byte[] expectedResponse = { 1, 5, 0, 81, 12, 1, -90 };

        AssignSucReturnRouteMessageClass handler = new AssignSucReturnRouteMessageClass();
        SerialMessage msg = handler.doRequest(12, 1);

        assertTrue(Arrays.equals(msg.getMessageBuffer(), expectedResponse));
    }
}
