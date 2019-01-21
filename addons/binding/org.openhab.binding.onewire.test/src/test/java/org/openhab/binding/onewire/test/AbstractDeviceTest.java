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
package org.openhab.binding.onewire.test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.openhab.binding.onewire.internal.OwBindingConstants.CHANNEL_PRESENT;

import java.lang.reflect.Constructor;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.junit.Assert;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.openhab.binding.onewire.internal.OwException;
import org.openhab.binding.onewire.internal.SensorId;
import org.openhab.binding.onewire.internal.device.AbstractOwDevice;
import org.openhab.binding.onewire.internal.handler.OwBaseBridgeHandler;
import org.openhab.binding.onewire.internal.handler.OwBaseThingHandler;

/**
 * Abtract test class for onewire devices.
 *
 * @author Jan N. Klug - Initial contribution
 */
public abstract class AbstractDeviceTest {

    protected Class<? extends AbstractOwDevice> deviceTestClazz;
    protected AbstractOwDevice testDevice;

    protected OwBaseThingHandler mockThingHandler;
    protected OwBaseBridgeHandler mockBridgeHandler;

    protected Thing mockThing;
    protected InOrder inOrder;

    protected SensorId testSensorId = new SensorId("00.000000000000");

    public void setupMocks(ThingTypeUID thingTypeUID) {
        mockThingHandler = mock(OwBaseThingHandler.class);
        mockBridgeHandler = mock(OwBaseBridgeHandler.class);
        mockThing = mock(Thing.class);

        inOrder = Mockito.inOrder(mockThingHandler, mockBridgeHandler);

        Mockito.when(mockThingHandler.getThing()).thenReturn(mockThing);
        Mockito.when(mockThing.getUID()).thenReturn(new ThingUID(thingTypeUID, "testsensor"));

        addChannel(CHANNEL_PRESENT, "Switch");
    }

    public void addChannel(String channelId, String itemType) {
        Channel channel = ChannelBuilder.create(new ChannelUID(mockThing.getUID(), channelId), itemType).build();
        Mockito.when(mockThing.getChannel(channelId)).thenReturn(channel);
    }

    public void addChannel(String channelId, String itemType, Configuration channelConfiguration) {
        Channel channel = ChannelBuilder.create(new ChannelUID(mockThing.getUID(), channelId), itemType)
                .withConfiguration(channelConfiguration).build();
        Mockito.when(mockThing.getChannel(channelId)).thenReturn(channel);
    }

    public @Nullable AbstractOwDevice instantiateDevice() {
        try {
            Constructor<?> constructor = deviceTestClazz.getConstructor(SensorId.class, OwBaseThingHandler.class);
            testDevice = (AbstractOwDevice) constructor.newInstance(new Object[] { testSensorId, mockThingHandler });
            return testDevice;
        } catch (Exception e) {
            Assert.fail("Couldn't create test device: " + e.getMessage());
            return null;
        }
    }

    public void presenceTest(OnOffType state) {
        instantiateDevice();
        try {
            Mockito.when(mockBridgeHandler.checkPresence(testSensorId)).thenReturn(state);
            testDevice.checkPresence(mockBridgeHandler);

            inOrder.verify(mockThingHandler).updatePresenceStatus(eq(state));
        } catch (OwException e) {
            Assert.fail("caught unexpected OwException");
        }
    }
}
