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
package org.openhab.binding.rachio.internal.api;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.rachio.internal.api.json.RachioCloudEvent;

import com.google.gson.Gson;

/**
 * {@link RachioEventString} store some relevant information from API events.
 *
 * @author Markus Michels - Initial contribution
 */
@NonNullByDefault
@SuppressWarnings("unused")
public class RachioEventString {
    @Nullable
    private genericEvent gEvent;
    @Nullable
    private zoneEvent    zEvent;
    private Gson         gson = new Gson();

    private class genericEvent {
        private final String timetstamp;
        private final String summary;
        private final String topic;
        private final String type;
        private final String subType;

        public genericEvent(RachioCloudEvent event) {
            timetstamp = event.timestamp;
            summary = event.summary;
            topic = event.topic;
            type = event.type;
            subType = event.subType;
        }
    }

    private class zoneEvent {
        private final String timestamp;
        private final String summary;
        private final String type;
        private final String subType;

        private final String zoneName;
        private final int    zoneNumber;
        private final String zoneRunState;
        private final String scheduleType;
        private final String startTime;
        private final String endTime;
        private final int    duration;

        @SuppressWarnings("null")
        public zoneEvent(RachioCloudEvent event) {
            timestamp = event.timestamp;
            summary = event.summary;
            type = event.type;
            subType = event.subType;

            zoneName = event.zoneName;
            zoneNumber = event.zoneNumber;
            zoneRunState = event.zoneRunState;
            scheduleType = event.zoneRunStatus.scheduleType;
            startTime = event.zoneRunStatus.startTime;
            endTime = event.zoneRunStatus.endTime;
            duration = event.duration;
        }
    }

    public RachioEventString(RachioCloudEvent event) {
        Validate.notNull(event);
        if (event.type.equals("ZONE_STATUS")) {
            zEvent = new zoneEvent(event);
        } else {
            gEvent = new genericEvent(event);
        }
    }

    @Nullable
    public String toJson() {
        Validate.isTrue(zEvent != null || gEvent != null);
        return zEvent != null ? gson.toJson(zEvent) : gson.toJson(gEvent);
    }
}
