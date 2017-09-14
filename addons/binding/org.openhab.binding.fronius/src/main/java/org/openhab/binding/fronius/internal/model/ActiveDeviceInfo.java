/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fronius.internal.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Access object for device information.
 *
 * @author Gerrit Beine
 */
public class ActiveDeviceInfo {

    private final Logger logger = LoggerFactory.getLogger(ActiveDeviceInfo.class);
    private final Set<Integer> inverters = new HashSet<>();
    private final Set<Integer> storages = new HashSet<>();
    private final Set<Integer> meters = new HashSet<>();

    private DecimalType code = DecimalType.ZERO;
    private DateTimeType timestamp = new DateTimeType();
    private boolean empty = true;

    public static ActiveDeviceInfo createActiveDeviceInfo(final JsonObject json) {
        final ActiveDeviceInfo adi = new ActiveDeviceInfo();
        adi.deconstruct(json);
        return adi;
    }

    private ActiveDeviceInfo() {
        super();
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public int inverterCount() {
        return inverters.size();
    }

    public int meterCount() {
        return meters.size();
    }

    public int storageCount() {
        return storages.size();
    }

    public Set<Integer> inverters() {
        return ImmutableSet.copyOf(inverters);
    }

    public Set<Integer> meters() {
        return ImmutableSet.copyOf(meters);
    }

    public Set<Integer> storages() {
        return ImmutableSet.copyOf(storages);
    }

    public DateTimeType getTimestamp() {
        return timestamp;
    }

    private synchronized void deconstruct(final JsonObject json) {
        try {
            if (json.has("Body")) {
                final JsonObject body = json.get("Body").getAsJsonObject();
                logger.trace("{}", body.toString());
                if (body.has("Data")) {
                    final JsonObject data = body.get("Data").getAsJsonObject();
                    logger.trace("{}", data.toString());
                    if (data.has("Inverter")) {
                        final JsonObject inverter = data.get("Inverter").getAsJsonObject();
                        logger.trace("{}", inverter.toString());
                        final Set<Map.Entry<String, JsonElement>> entries = inverter.entrySet();
                        for (final Map.Entry<String, JsonElement> entry : entries) {
                            logger.debug("Inverter {}", entry.getKey());
                            inverters.add(Integer.valueOf(entry.getKey()));
                        }
                    }
                    if (data.has("Storage")) {
                        final JsonObject storage = data.get("Storage").getAsJsonObject();
                        logger.trace("{}", storage.toString());
                        final Set<Map.Entry<String, JsonElement>> entries = storage.entrySet();
                        for (final Map.Entry<String, JsonElement> entry : entries) {
                            logger.debug("Storage {}", entry.getKey());
                            storages.add(Integer.valueOf(entry.getKey()));
                        }
                    }
                    if (data.has("Meter")) {
                        final JsonObject meter = data.get("Meter").getAsJsonObject();
                        logger.trace("{}", meter.toString());
                        final Set<Map.Entry<String, JsonElement>> entries = meter.entrySet();
                        for (final Map.Entry<String, JsonElement> entry : entries) {
                            logger.debug("Meter {}", entry.getKey());
                            meters.add(Integer.valueOf(entry.getKey()));
                        }
                    }
                    empty = false;
                }
            }
            if (json.has("Head")) {
                final JsonObject head = json.get("Head").getAsJsonObject();
                logger.trace("{}", head.toString());
                if (head.has("Status")) {
                    final JsonObject status = head.get("Status").getAsJsonObject();
                    logger.trace("{}", status.toString());
                    if (status.has("Code")) {
                        code = new DecimalType(status.get("Code").getAsString());
                        logger.debug("Status Code: {}", code);
                    }
                }
                if (head.has("Timestamp")) {
                    timestamp = new DateTimeType(head.get("Timestamp").getAsString());
                    logger.debug("Timestamp: {}", timestamp);
                }
            }
        } catch (Exception e) {
            logger.warn("{}", e.toString());
        }
    }
}
