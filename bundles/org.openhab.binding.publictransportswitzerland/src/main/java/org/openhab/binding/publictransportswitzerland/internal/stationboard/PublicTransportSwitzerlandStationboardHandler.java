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
package org.openhab.binding.publictransportswitzerland.internal.stationboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.openhab.binding.publictransportswitzerland.internal.PublicTransportSwitzerlandBindingConstants.CHANNEL_CSV;
import static org.openhab.binding.publictransportswitzerland.internal.PublicTransportSwitzerlandBindingConstants.CHANNEL_JSON;

/**
 * The {@link PublicTransportSwitzerlandStationboardHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jeremy Stucki & Stefanie Jäger - Initial contribution
 */
@NonNullByDefault
public class PublicTransportSwitzerlandStationboardHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(PublicTransportSwitzerlandStationboardHandler.class);

    private @Nullable ScheduledFuture<?> updateDataJob;

    public PublicTransportSwitzerlandStationboardHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            // TODO?
        }
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);

        //noinspection ConstantConditions
        if (updateDataJob == null || updateDataJob.isCancelled()) {
            updateDataJob = scheduler.scheduleWithFixedDelay(this::updateData, 0, 60, TimeUnit.SECONDS);
        }
    }

    public void updateData() {
        PublicTransportSwitzerlandStationboardConfiguration config = getConfigAs(PublicTransportSwitzerlandStationboardConfiguration.class);

        try {
            String response = HttpUtil.executeUrl("GET", "https://transport.opendata.ch/v1/stationboard?station=" + config.station, 10_000);
            JsonElement jsonObject = new JsonParser().parse(response);

            updateCsvChannel(jsonObject);
            updateJsonChannel(jsonObject);
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            logger.warn("Unable to fetch stationboard data", e);

            updateStatus(ThingStatus.OFFLINE);
            updateState(CHANNEL_CSV, new StringType("No data available"));
            updateState(CHANNEL_JSON, new StringType("{}"));
        }
    }

    private void updateJsonChannel(JsonElement jsonObject) {
        updateState(CHANNEL_JSON, new StringType(jsonObject.toString()));
    }

    private void updateCsvChannel(JsonElement jsonObject) throws Exception {
        JsonArray stationboard = jsonObject.getAsJsonObject().get("stationboard").getAsJsonArray();

        List<String> departures = new ArrayList<>();

        for (JsonElement jsonElement : stationboard) {
            JsonObject departureObject = jsonElement.getAsJsonObject();
            JsonObject stopObject = departureObject.get("stop").getAsJsonObject();

            String departureTime = stopObject.get("departureTimestamp").getAsString();
            String destination = departureObject.get("to").getAsString();
            String track = stopObject.get("platform").getAsString();

            JsonElement delayElement = departureObject.get("delay");
            String delay = "";
            if (delayElement != null) {
                delay = delayElement.getAsString();
            }

            departures.add(String.join("\t", departureTime, destination, track, delay));
        }

        updateState(CHANNEL_CSV, new StringType(String.join("\n", departures)));
    }

}
