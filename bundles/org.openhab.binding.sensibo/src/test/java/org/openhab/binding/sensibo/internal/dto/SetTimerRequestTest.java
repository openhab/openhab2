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
package org.openhab.binding.sensibo.internal.dto;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.openhab.binding.sensibo.internal.dto.poddetails.AcState;
import org.openhab.binding.sensibo.internal.dto.settimer.SetTimerRequest;

/**
 * @author Arne Seime - Initial contribution
 */
public class SetTimerRequestTest extends AbstractSerializationDeserializationTest {

    @Test
    public void testSerializeDeserialize() throws IOException {
        AcState acState = new AcState(false, "fanLevel", "C", 21, "mode", "swing");
        SetTimerRequest req = new SetTimerRequest("PODID", 60, acState);
        String serializedJson = wireHelper.serialize(req);

        final SetTimerRequest deSerializedRequest = wireHelper.deSerializeFromString(serializedJson,
                SetTimerRequest.class);
        assertNotNull(deSerializedRequest.acState);
        assertEquals(60, deSerializedRequest.minutesFromNow);
        assertFalse(deSerializedRequest.acState.on);
    }

}
