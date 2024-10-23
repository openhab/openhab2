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
import org.openhab.binding.dirigera.internal.handler.plug.PowerPlugHandler;
import org.openhab.binding.dirigera.mock.CallbackMock;
import org.openhab.binding.dirigera.mock.HandlerFactoryMock;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.internal.ThingImpl;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;

/**
 * {@link TestPowerPlug} Tests device handler creation, initializing and refresh of channels
 *
 * @author Bernd Weymann - Initial Contribution
 */
@NonNullByDefault
class TestPowerPlug {
    String deviceId = "a4c6a33a-9c6a-44bf-bdde-f99aff00eca4_1";
    ThingTypeUID thingTypeUID = THING_TYPE_POWER_PLUG;

    @Test
    void testHandlerCreation() {
        HandlerFactoryMock hfm = new HandlerFactoryMock();
        assertTrue(hfm.supportsThingType(thingTypeUID));
        ThingImpl thing = new ThingImpl(thingTypeUID, "test-device");
        ThingHandler th = hfm.createHandler(thing);
        assertNotNull(th);
        assertTrue(th instanceof PowerPlugHandler);
    }

    @Test
    void testInitialization() {
        Bridge hubBridge = DirigeraBridgeProvider.prepareSimuBridge();
        ThingImpl thing = new ThingImpl(thingTypeUID, "test-device");
        thing.setBridgeUID(hubBridge.getBridgeUID());
        PowerPlugHandler handler = new PowerPlugHandler(thing, SMART_PLUG_MAP);
        CallbackMock callback = new CallbackMock();
        callback.setBridge(hubBridge);
        handler.setCallback(callback);

        // set the right id
        Map<String, Object> config = new HashMap<>();
        config.put("id", deviceId);
        handler.handleConfigurationUpdate(config);

        handler.initialize();
        callback.waitForOnline();
        checkPowerPlugStates(callback);

        callback.clear();
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATUS), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATE), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_OTA_PROGRESS), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_STATE), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_CHILD_LOCK), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_DISABLE_STATUS_LIGHT), RefreshType.REFRESH);
        handler.handleCommand(new ChannelUID(thing.getUID(), CHANNEL_STARTUP_BEHAVIOR), RefreshType.REFRESH);
        checkPowerPlugStates(callback);
    }

    void checkPowerPlugStates(CallbackMock callback) {
        State otaStatus = callback.getState("dirigera:power-plug:test-device:ota-status");
        assertNotNull(otaStatus);
        assertTrue(otaStatus instanceof DecimalType);
        assertEquals(0, ((DecimalType) otaStatus).intValue(), "OTA Status");
        State otaState = callback.getState("dirigera:power-plug:test-device:ota-state");
        assertNotNull(otaState);
        assertTrue(otaState instanceof DecimalType);
        assertEquals(0, ((DecimalType) otaState).intValue(), "OTA State");
        State otaProgess = callback.getState("dirigera:power-plug:test-device:ota-progress");
        assertNotNull(otaProgess);
        assertTrue(otaProgess instanceof QuantityType);
        assertTrue(((QuantityType<?>) otaProgess).getUnit().equals(Units.PERCENT));
        assertEquals(0, ((QuantityType<?>) otaProgess).intValue(), "OTA Progress");

        State disableLightState = callback.getState("dirigera:power-plug:test-device:disable-light");
        assertNotNull(disableLightState);
        assertTrue(disableLightState instanceof OnOffType);
        assertTrue(OnOffType.ON.equals((disableLightState)), "Disable Light On");
        State childlockState = callback.getState("dirigera:power-plug:test-device:child-lock");
        assertNotNull(childlockState);
        assertTrue(childlockState instanceof OnOffType);
        assertTrue(OnOffType.OFF.equals((childlockState)), "Child Lock Off");
        State onOffState = callback.getState("dirigera:power-plug:test-device:state");
        assertNotNull(onOffState);
        assertTrue(onOffState instanceof OnOffType);
        assertTrue(OnOffType.OFF.equals((onOffState)), "Power Off");
        State startupState = callback.getState("dirigera:power-plug:test-device:startup");
        assertNotNull(startupState);
        assertTrue(startupState instanceof DecimalType);
        assertEquals(0, ((DecimalType) startupState).intValue(), "Startup Behavior");
    }
}
