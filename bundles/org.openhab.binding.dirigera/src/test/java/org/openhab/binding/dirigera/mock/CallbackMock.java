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
package org.openhab.binding.dirigera.mock;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.config.core.ConfigDescription;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelGroupUID;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandlerCallback;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelGroupTypeUID;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link CallbackMock} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class CallbackMock implements ThingHandlerCallback {
    private final Logger logger = LoggerFactory.getLogger(CallbackMock.class);

    private @Nullable Bridge bridge;
    private ThingStatus status = ThingStatus.OFFLINE;
    private Map<String, State> stateMap = new HashMap<>();

    public @Nullable State getState(String channel) {
        return stateMap.get(channel);
    }

    @Override
    public void stateUpdated(ChannelUID channelUID, State state) {
        stateMap.put(channelUID.getAsString(), state);
        logger.warn("Update {} state {}", channelUID.getAsString(), state.toFullString());
    }

    @Override
    public void postCommand(ChannelUID channelUID, Command command) {
        // TODO Auto-generated method stub
    }

    @Override
    public void sendTimeSeries(ChannelUID channelUID, TimeSeries timeSeries) {
        // TODO Auto-generated method stub
    }

    @Override
    public void statusUpdated(Thing thing, ThingStatusInfo thingStatus) {
        synchronized (this) {
            status = thingStatus.getStatus();
            this.notifyAll();
        }
        logger.warn("Update status {}", thingStatus.getStatus());
    }

    public void waitForOnline() {
        synchronized (this) {
            Instant start = Instant.now();
            Instant check = Instant.now();
            while (!ThingStatus.ONLINE.equals(status) && Duration.between(start, check).getSeconds() < 10) {
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    fail("Interruppted waiting for ONLINE");
                }
                check = Instant.now();
            }
        }
        // if method is exited without reaching ONLINE e.g. through timeout fail
        if (!ThingStatus.ONLINE.equals(status)) {
            fail("waitForOnline just reached status " + status);
        } else {
            logger.warn("Callback reached {}", status);
        }
    }

    @Override
    public void thingUpdated(Thing thing) {
        // TODO Auto-generated method stub
    }

    @Override
    public void validateConfigurationParameters(Thing thing, Map<String, Object> configurationParameters) {
        // TODO Auto-generated method stub
    }

    @Override
    public void validateConfigurationParameters(Channel channel, Map<String, Object> configurationParameters) {
        // TODO Auto-generated method stub
    }

    @Override
    public @Nullable ConfigDescription getConfigDescription(ChannelTypeUID channelTypeUID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @Nullable ConfigDescription getConfigDescription(ThingTypeUID thingTypeUID) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void configurationUpdated(Thing thing) {
        // TODO Auto-generated method stub
    }

    @Override
    public void migrateThingType(Thing thing, ThingTypeUID thingTypeUID, Configuration configuration) {
        // TODO Auto-generated method stub
    }

    @Override
    public void channelTriggered(Thing thing, ChannelUID channelUID, String event) {
        // TODO Auto-generated method stub
    }

    @Override
    public ChannelBuilder createChannelBuilder(ChannelUID channelUID, ChannelTypeUID channelTypeUID) {
        // TODO Auto-generated method stub
        return ChannelBuilder.create(new ChannelUID("handler:test"));
    }

    @Override
    public ChannelBuilder editChannel(Thing thing, ChannelUID channelUID) {
        // TODO Auto-generated method stub
        return ChannelBuilder.create(new ChannelUID("handler:test"));
    }

    @Override
    public List<ChannelBuilder> createChannelBuilders(ChannelGroupUID channelGroupUID,
            ChannelGroupTypeUID channelGroupTypeUID) {
        // TODO Auto-generated method stub
        return List.of();
    }

    @Override
    public boolean isChannelLinked(ChannelUID channelUID) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public @Nullable Bridge getBridge(ThingUID bridgeUID) {
        return bridge;
    }

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }
}
