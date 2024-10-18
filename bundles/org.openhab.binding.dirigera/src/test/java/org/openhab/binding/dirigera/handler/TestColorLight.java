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
package org.openhab.binding.dirigera.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.openhab.binding.dirigera.internal.Constants.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.dirigera.internal.handler.ColorLightHandler;
import org.openhab.binding.dirigera.mock.CallbackMock;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.internal.ThingImpl;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;

/**
 * {@link TestColorLight} Tests device handler creation, initializing and refresh of channels
 *
 * @author Bernd Weymann - Initial Contribution
 */
@NonNullByDefault
class TestColorLight {

    @Test
    void testColorLightDevice() {
        Bridge hubBridge = DirigeraBridgeProvider.prepareBridge();
        ThingImpl thing = new ThingImpl(THING_TYPE_COLOR_LIGHT, "test-device");
        thing.setBridgeUID(hubBridge.getBridgeUID());
        ColorLightHandler handler = new ColorLightHandler(thing, COLOR_LIGHT_MAP);
        CallbackMock callback = new CallbackMock();
        callback.setBridge(hubBridge);
        handler.setCallback(callback);

        // set the right id
        Map<String, Object> config = new HashMap<>();
        config.put("id", "3c8b0049-eb5c-4ea1-9da3-cdedc50366ef_1");
        handler.handleConfigurationUpdate(config);

        handler.initialize();
        callback.waitForOnline();
        checkColorLightStates(callback);

        callback.clear();
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATUS), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATE), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_PROGRESS), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_STATE), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_LIGHT_HSB), RefreshType.REFRESH);
        checkColorLightStates(callback);
    }

    void checkColorLightStates(CallbackMock callback) {
        State onOffState = callback.getState("dirigera:color-light:test-device:state");
        assertNotNull(onOffState);
        assertTrue(onOffState instanceof OnOffType);
        assertTrue(OnOffType.ON.equals((onOffState)), "On");
        State hsbState = callback.getState("dirigera:color-light:test-device:hsb");
        assertNotNull(hsbState);
        assertTrue(hsbState instanceof HSBType);
        assertEquals(119, ((HSBType) hsbState).getHue().intValue(), "Hue");
        assertEquals(70, ((HSBType) hsbState).getSaturation().intValue(), "Saturation");
        assertEquals(92, ((HSBType) hsbState).getBrightness().intValue(), "Brightness");

        // test ota
        State otaStatus = callback.getState("dirigera:color-light:test-device:ota-status");
        assertNotNull(otaStatus);
        assertTrue(otaStatus instanceof DecimalType);
        assertEquals(0, ((DecimalType) otaStatus).intValue(), "OTA Status");
        State otaState = callback.getState("dirigera:color-light:test-device:ota-state");
        assertNotNull(otaState);
        assertTrue(otaState instanceof DecimalType);
        assertEquals(0, ((DecimalType) otaState).intValue(), "OTA State");
        State otaProgess = callback.getState("dirigera:color-light:test-device:ota-progress");
        assertNotNull(otaProgess);
        assertTrue(otaProgess instanceof QuantityType);
        assertTrue(((QuantityType<?>) otaProgess).getUnit().equals(Units.PERCENT));
        assertEquals(0, ((QuantityType<?>) otaProgess).intValue(), "OTA Progress");
    }
}
