/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.mqtt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openhab.binding.mqtt.generic.AvailabilityTracker;
import org.openhab.binding.mqtt.generic.ChannelStateUpdateListener;
import org.openhab.binding.mqtt.generic.TransformationServiceProvider;
import org.openhab.binding.mqtt.homeassistant.internal.ChannelConfigurationTypeAdapterFactory;
import org.openhab.binding.mqtt.homeassistant.internal.DiscoverComponents;
import org.openhab.binding.mqtt.homeassistant.internal.DiscoverComponents.ComponentDiscovered;
import org.openhab.binding.mqtt.homeassistant.internal.HaID;
import org.openhab.binding.mqtt.homeassistant.internal.HandlerConfiguration;
import org.openhab.core.io.transport.mqtt.MqttBrokerConnection;
import org.openhab.core.test.java.JavaOSGiTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Tests the {@link DiscoverComponents} class.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class DiscoverComponentsTest extends JavaOSGiTest {

    private @NonNullByDefault({}) AutoCloseable mocksCloseable;

    private @Mock @NonNullByDefault({}) MqttBrokerConnection connection;
    private @Mock @NonNullByDefault({}) ComponentDiscovered discovered;
    private @Mock @NonNullByDefault({}) TransformationServiceProvider transformationServiceProvider;
    private @Mock @NonNullByDefault({}) ChannelStateUpdateListener channelStateUpdateListener;
    private @Mock @NonNullByDefault({}) AvailabilityTracker availabilityTracker;

    @BeforeEach
    public void beforeEach() {
        mocksCloseable = openMocks(this);

        CompletableFuture<@Nullable Void> voidFutureComplete = new CompletableFuture<>();
        voidFutureComplete.complete(null);
        doReturn(voidFutureComplete).when(connection).unsubscribeAll();
        doReturn(CompletableFuture.completedFuture(true)).when(connection).subscribe(any(), any());
        doReturn(CompletableFuture.completedFuture(true)).when(connection).unsubscribe(any(), any());
        doReturn(CompletableFuture.completedFuture(true)).when(connection).publish(any(), any(), anyInt(),
                anyBoolean());
        doReturn(null).when(transformationServiceProvider).getTransformationService(any());
    }

    @AfterEach
    public void afterEach() throws Exception {
        mocksCloseable.close();
    }

    @Test
    public void discoveryTimeTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Create a scheduler
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new ChannelConfigurationTypeAdapterFactory()).create();

        DiscoverComponents discover = spy(new DiscoverComponents(ThingChannelConstants.testHomeAssistantThing,
                scheduler, channelStateUpdateListener, availabilityTracker, gson, transformationServiceProvider));

        HandlerConfiguration config = new HandlerConfiguration("homeassistant",
                Collections.singletonList("switch/object"));

        Set<HaID> discoveryIds = new HashSet<>();
        discoveryIds.addAll(HaID.fromConfig(config));

        discover.startDiscovery(connection, 50, discoveryIds, discovered).get(100, TimeUnit.MILLISECONDS);
    }
}
