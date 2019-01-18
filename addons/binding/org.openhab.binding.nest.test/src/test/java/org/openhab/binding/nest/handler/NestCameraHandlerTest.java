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
package org.openhab.binding.nest.handler;

import static org.eclipse.smarthome.core.library.types.OnOffType.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openhab.binding.nest.internal.NestBindingConstants.*;
import static org.openhab.binding.nest.internal.data.NestDataUtil.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.junit.Test;
import org.openhab.binding.nest.internal.config.NestDeviceConfiguration;
import org.openhab.binding.nest.internal.handler.NestCameraHandler;

/**
 * Tests for {@link NestCameraHandler}.
 *
 * @author Wouter Born - Increase test coverage
 */
public class NestCameraHandlerTest extends NestThingHandlerOSGiTest {

    private static final ThingUID CAMERA_UID = new ThingUID(THING_TYPE_CAMERA, "camera1");
    private static final int CHANNEL_COUNT = 20;

    public NestCameraHandlerTest() {
        super(NestCameraHandler.class);
    }

    @Override
    protected Thing buildThing(Bridge bridge) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(NestDeviceConfiguration.DEVICE_ID, CAMERA1_DEVICE_ID);

        return ThingBuilder.create(THING_TYPE_CAMERA, CAMERA_UID).withLabel("Test Camera").withBridge(bridge.getUID())
                .withChannels(buildChannels(THING_TYPE_CAMERA, CAMERA_UID))
                .withConfiguration(new Configuration(properties)).build();
    }

    @Test
    public void completeCameraUpdate() throws IOException {
        assertThat(thing.getChannels().size(), is(CHANNEL_COUNT));
        assertThat(thing.getStatus(), is(ThingStatus.OFFLINE));

        waitForAssert(() -> assertThat(bridge.getStatus(), is(ThingStatus.ONLINE)));
        putStreamingEventData(fromFile(COMPLETE_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.ONLINE)));

        // Camera channel group
        assertThatItemHasState(CHANNEL_CAMERA_APP_URL, new StringType("https://camera_app_url"));
        assertThatItemHasState(CHANNEL_CAMERA_AUDIO_INPUT_ENABLED, ON);
        assertThatItemHasState(CHANNEL_CAMERA_LAST_ONLINE_CHANGE, parseDateTimeType("2017-01-22T08:19:20.000Z"));
        assertThatItemHasState(CHANNEL_CAMERA_PUBLIC_SHARE_ENABLED, OFF);
        assertThatItemHasState(CHANNEL_CAMERA_PUBLIC_SHARE_URL, new StringType("https://camera_public_share_url"));
        assertThatItemHasState(CHANNEL_CAMERA_SNAPSHOT_URL, new StringType("https://camera_snapshot_url"));
        assertThatItemHasState(CHANNEL_CAMERA_STREAMING, OFF);
        assertThatItemHasState(CHANNEL_CAMERA_VIDEO_HISTORY_ENABLED, OFF);
        assertThatItemHasState(CHANNEL_CAMERA_WEB_URL, new StringType("https://camera_web_url"));

        // Last event channel group
        assertThatItemHasState(CHANNEL_LAST_EVENT_ACTIVITY_ZONES, new StringType("id1,id2"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_ANIMATED_IMAGE_URL,
                new StringType("https://last_event_animated_image_url"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_APP_URL, new StringType("https://last_event_app_url"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_END_TIME, parseDateTimeType("2017-01-22T07:40:38.680Z"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_HAS_MOTION, ON);
        assertThatItemHasState(CHANNEL_LAST_EVENT_HAS_PERSON, OFF);
        assertThatItemHasState(CHANNEL_LAST_EVENT_HAS_SOUND, OFF);
        assertThatItemHasState(CHANNEL_LAST_EVENT_IMAGE_URL, new StringType("https://last_event_image_url"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_START_TIME, parseDateTimeType("2017-01-22T07:40:19.020Z"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_URLS_EXPIRE_TIME, parseDateTimeType("2017-02-05T07:40:19.020Z"));
        assertThatItemHasState(CHANNEL_LAST_EVENT_WEB_URL, new StringType("https://last_event_web_url"));

        assertThatAllItemStatesAreNotNull();
    }

    @Test
    public void incompleteCameraUpdate() throws IOException {
        assertThat(thing.getChannels().size(), is(CHANNEL_COUNT));
        assertThat(thing.getStatus(), is(ThingStatus.OFFLINE));

        waitForAssert(() -> assertThat(bridge.getStatus(), is(ThingStatus.ONLINE)));
        putStreamingEventData(fromFile(COMPLETE_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.ONLINE)));
        assertThatAllItemStatesAreNotNull();

        putStreamingEventData(fromFile(INCOMPLETE_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.UNKNOWN)));
        assertThatAllItemStatesAreNull();
    }

    @Test
    public void cameraGone() throws IOException {
        waitForAssert(() -> assertThat(bridge.getStatus(), is(ThingStatus.ONLINE)));
        putStreamingEventData(fromFile(COMPLETE_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.ONLINE)));

        putStreamingEventData(fromFile(EMPTY_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.OFFLINE)));
        assertThat(thing.getStatusInfo().getStatusDetail(), is(ThingStatusDetail.GONE));
    }

    @Test
    public void channelRefresh() throws IOException {
        waitForAssert(() -> assertThat(bridge.getStatus(), is(ThingStatus.ONLINE)));
        putStreamingEventData(fromFile(COMPLETE_DATA_FILE_NAME));
        waitForAssert(() -> assertThat(thing.getStatus(), is(ThingStatus.ONLINE)));
        assertThatAllItemStatesAreNotNull();

        updateAllItemStatesToNull();
        assertThatAllItemStatesAreNull();

        refreshAllChannels();
        assertThatAllItemStatesAreNotNull();
    }

    @Test
    public void handleStreamingCommands() throws IOException {
        handleCommand(CHANNEL_CAMERA_STREAMING, ON);
        assertNestApiPropertyState(CAMERA1_DEVICE_ID, "is_streaming", "true");

        handleCommand(CHANNEL_CAMERA_STREAMING, OFF);
        assertNestApiPropertyState(CAMERA1_DEVICE_ID, "is_streaming", "false");

        handleCommand(CHANNEL_CAMERA_STREAMING, ON);
        assertNestApiPropertyState(CAMERA1_DEVICE_ID, "is_streaming", "true");
    }
}
