/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.e3dc.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;

/**
 * The {@link ThingMock} Thing Mock for creating handlers
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ThingMock implements Thing {
    private Configuration config = new Configuration(); // = new HashMap<String, Object>();

    @Override
    public @Nullable String getLabel() {
        return null;
    }

    @Override
    public void setLabel(@Nullable String label) {
    }

    @Override
    public List<Channel> getChannels() {
        return new ArrayList<Channel>();
    }

    @Override
    public List<Channel> getChannelsOfGroup(String channelGroupId) {
        return new ArrayList<Channel>();
    }

    @Override
    public @Nullable Channel getChannel(String channelId) {
        return null;
    }

    @Override
    public @Nullable Channel getChannel(ChannelUID channelUID) {
        return null;
    }

    @Override
    public ThingStatus getStatus() {
        return ThingStatus.UNKNOWN;
    }

    @Override
    public ThingStatusInfo getStatusInfo() {
        return new ThingStatusInfo(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "");
    }

    @Override
    public void setStatusInfo(ThingStatusInfo status) {
    }

    @Override
    public void setHandler(@Nullable ThingHandler thingHandler) {
    }

    @Override
    public @Nullable ThingHandler getHandler() {
        return null;
    }

    @Override
    public @Nullable ThingUID getBridgeUID() {
        return null;
    }

    @Override
    public void setBridgeUID(@Nullable ThingUID bridgeUID) {
    }

    @Override
    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Map<String, Object> m) {
        config = new Configuration(m);
    }

    @Override
    public ThingUID getUID() {
        return new ThingUID("", "");
    }

    @Override
    public ThingTypeUID getThingTypeUID() {
        return new ThingTypeUID("");
    }

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<String, String>();
    }

    @Override
    public @Nullable String setProperty(String name, @Nullable String value) {
        return null;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
    }

    @Override
    public @Nullable String getLocation() {
        return null;
    }

    @Override
    public void setLocation(@Nullable String location) {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
