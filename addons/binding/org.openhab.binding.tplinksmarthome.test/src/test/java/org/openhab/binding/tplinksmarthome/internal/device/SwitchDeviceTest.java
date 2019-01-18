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
package org.openhab.binding.tplinksmarthome.internal.device;

import static org.junit.Assert.*;
import static org.openhab.binding.tplinksmarthome.internal.TPLinkSmartHomeBindingConstants.*;

import java.io.IOException;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.types.UnDefType;
import org.junit.Test;

/**
 * Test class for {@link SwitchDevice} class.
 *
 * @author Hilbrand Bouwkamp - Initial contribution
 */
public class SwitchDeviceTest extends DeviceTestBase {

    private final SwitchDevice device = new SwitchDevice();

    public SwitchDeviceTest() throws IOException {
        super("plug_get_sysinfo_response");
    }

    @Test
    public void testHandleCommandSwitch() throws IOException {
        assertInput("plug_set_relay_state_on");
        setSocketReturnAssert("plug_set_relay_state_on");
        assertTrue("Switch channel should be handled",
                device.handleCommand(CHANNEL_SWITCH, connection, OnOffType.ON, configuration));
    }

    @Test
    public void testHandleCommandLed() throws IOException {
        assertInput("plug_set_led_on");
        setSocketReturnAssert("plug_set_led_on");
        assertTrue("Led channel should be handled",
                device.handleCommand(CHANNEL_LED, connection, OnOffType.ON, configuration));
    }

    @Test
    public void testUpdateChannelSwitch() {
        assertSame("Switch should be on", OnOffType.ON, device.updateChannel(CHANNEL_SWITCH, deviceState));
    }

    @Test
    public void testUpdateChannelLed() {
        assertSame("Led should be on", OnOffType.ON, device.updateChannel(CHANNEL_LED, deviceState));
    }

    @Test
    public void testUpdateChannelOther() {
        assertSame("Unknown channel should return UNDEF", UnDefType.UNDEF, device.updateChannel("OTHER", deviceState));
    }
}
