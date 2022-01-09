/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.meater.internal.dto;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link MeaterProbeDTO} class defines the DTO for the Meater probe.
 *
 * @author Jan Gustafsson - Initial contribution
 */
@NonNullByDefault
public class MeaterProbeDTO {

    public String status = "";
    public long statusCode;
    public Data data = new Data();
    public Meta meta = new Meta();

    public Data getData() {
        return data;
    }

    public class Data {

        @Nullable
        public List<Device> devices;

        public @Nullable List<Device> getDevices() {
            return devices;
        }

        public @Nullable Device getDevice(String id) {
            if (devices != null) {
                for (Device meaterProbe : devices) {
                    if (id.equals(meaterProbe.id)) {
                        return meaterProbe;
                    }
                }
            }
            return null;
        }
    }

    public class Meta {
    }

    public class Device {
        public String id = "";
        public Temperature temperature = new Temperature();
        public @Nullable Cook cook = new Cook();
        @SerializedName("updated_at")
        private long lastConnection;

        public @Nullable ZonedDateTime getLastConnection() {
            if (lastConnection > 0) {
                Instant instant = Instant.ofEpochSecond(lastConnection);
                return ZonedDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
            }
            return null;
        }
    }

    public class Cook {
        public String id = "";
        public String name = "";
        public String state = "";
        public TemperatureCook temperature = new TemperatureCook();
        public Time time = new Time();
    }

    public class TemperatureCook {
        public double target;
        public double peak;
    }

    public class Temperature {
        public double internal;
        public double ambient;
    }

    public class Time {
        public long elapsed;
        public long remaining;
    }
}
