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
package org.openhab.binding.loxone.internal.controls;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.types.UnDefType;
import org.junit.Before;
import org.junit.Test;

public class LxControlInfoOnlyDigitalTest extends LxControlTest {
    @Before
    public void setup() {
        setupControl("101b50f7-0306-98fb-ffff403fb0c34b9e", "0e368d32-014f-4604-ffff403fb0c34b9e",
                "101b563d-0302-78bd-ffff403fb0c34b9e", "Info Only Digital");
    }

    @Test
    public void testControlCreation() {
        testControlCreation(LxControlInfoOnlyDigital.class, 0, 1, 1, 1);
    }

    @Test
    public void testChannels() {
        testChannel("Switch");
    }

    @Test
    public void testLoxoneStateChanges() {
        for (Double i = 2.0; i < 100.0; i++) {
            changeLoxoneState("active", 0.0);
            testChannelState(OnOffType.OFF);
            changeLoxoneState("active", 1.0);
            testChannelState(OnOffType.ON);
            changeLoxoneState("active", 1.0 / i);
            testChannelState(UnDefType.UNDEF);
        }
    }
}
