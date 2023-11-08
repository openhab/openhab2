/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.tasmotaplug.internal.handler;

import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.openhab.binding.tasmotaplug.internal.TasmotaPlugBindingConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.openhab.binding.tasmotaplug.internal.TasmotaPlugConfiguration;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TasmotaPlugHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Michael Lobstein - Initial contribution
 */
@NonNullByDefault
public class TasmotaPlugHandler extends BaseThingHandler {
    private static final int DEFAULT_REFRESH_PERIOD_SEC = 30;

    private static final String PASSWORD_REGEX = "&password=(.*)&";
    private static final String PASSWORD_MASK = "&password=xxxx&";

    private final Logger logger = LoggerFactory.getLogger(TasmotaPlugHandler.class);
    private final HttpClient httpClient;

    private @Nullable ScheduledFuture<?> refreshJob;

    private String plugHost = BLANK;
    private int refreshPeriod = DEFAULT_REFRESH_PERIOD_SEC;
    private int numChannels = 1;
    private boolean isAuth = false;
    private String user = BLANK;
    private String pass = BLANK;

    public TasmotaPlugHandler(Thing thing, HttpClient httpClient) {
        super(thing);
        this.httpClient = httpClient;
    }

    @Override
    public void initialize() {
        logger.debug("Initializing TasmotaPlug handler.");
        TasmotaPlugConfiguration config = getConfigAs(TasmotaPlugConfiguration.class);

        final String hostName = config.hostName;
        final Integer refresh = config.refresh;
        final Integer numChannels = config.numChannels;
        final String username = config.username;
        final String password = config.password;

        if (hostName.isBlank()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.configuration-error-hostname");
            return;
        }

        if (refresh != null) {
            this.refreshPeriod = refresh;
        }

        if (numChannels != null) {
            this.numChannels = numChannels;
        }

        if (!username.isBlank() && !password.isBlank()) {
            isAuth = true;
            user = username;
            pass = password;
        }

        plugHost = "http://" + hostName;

        // remove the channels we are not using
        if (this.numChannels < SUPPORTED_CHANNEL_IDS.size()) {
            List<Channel> channels = new ArrayList<>(this.getThing().getChannels());

            List<Integer> channelsToRemove = IntStream.range(this.numChannels + 1, SUPPORTED_CHANNEL_IDS.size() + 1)
                    .boxed().collect(Collectors.toList());

            channelsToRemove.forEach(channel -> {
                channels.removeIf(c -> (c.getUID().getId().equals(POWER + channel)));
            });
            updateThing(editThing().withChannels(channels).build());
        }

        updateStatus(ThingStatus.UNKNOWN);
        startAutomaticRefresh();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing the TasmotaPlug handler.");

        ScheduledFuture<?> refreshJob = this.refreshJob;
        if (refreshJob != null) {
            refreshJob.cancel(true);
            this.refreshJob = null;
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().contains(POWER)) {
            if (command instanceof OnOffType) {
                getCommand(channelUID.getId(), command);
            } else {
                updateChannelState(channelUID.getId());
            }
        } else {
            logger.warn("Unsupported command: {}", command.toString());
        }
    }

    /**
     * Start the job to periodically update the state of the plug
     */
    private void startAutomaticRefresh() {
        ScheduledFuture<?> refreshJob = this.refreshJob;
        if (refreshJob == null || refreshJob.isCancelled()) {
            refreshJob = null;
            this.refreshJob = scheduler.scheduleWithFixedDelay(() -> {
                SUPPORTED_CHANNEL_IDS.stream().limit(numChannels).forEach(channelId -> {
                    updateChannelState(channelId);
                });
            }, 0, refreshPeriod, TimeUnit.SECONDS);
        }
    }

    private void updateChannelState(String channelId) {
        final String plugState = getCommand(channelId, null);
        if (plugState.contains(ON)) {
            updateState(channelId, OnOffType.ON);
        } else if (plugState.contains(OFF)) {
            updateState(channelId, OnOffType.OFF);
        }
    }

    private String getCommand(String channelId, @Nullable Command command) {
        final String plugChannel = channelId.substring(0, 1).toUpperCase() + channelId.substring(1);
        String url;

        if (isAuth) {
            url = String.format(CMD_URI_AUTH, user, pass, plugChannel);
        } else {
            url = String.format(CMD_URI, plugChannel);
        }

        if (command != null) {
            url += "%20" + command;
        }

        try {
            logger.trace("Sending GET request to {}{}", plugHost, maskPassword(url));
            ContentResponse contentResponse = httpClient.GET(plugHost + url);
            logger.trace("Response: {}", contentResponse.getContentAsString());

            if (contentResponse.getStatus() != OK_200) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "@text/offline.communication-error.http-failure [\"" + contentResponse.getStatus() + "\"]");
                return BLANK;
            }

            updateStatus(ThingStatus.ONLINE);
            return contentResponse.getContentAsString();
        } catch (TimeoutException | ExecutionException e) {
            logger.debug("Error executing Tasmota GET request: '{}{}', {}", plugHost, maskPassword(url),
                    e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
        } catch (InterruptedException e) {
            logger.debug("InterruptedException executing Tasmota GET request: '{}{}', {}", plugHost, maskPassword(url),
                    e.getMessage());
            Thread.currentThread().interrupt();
        }
        return BLANK;
    }

    private String maskPassword(String input) {
        return isAuth ? input.replaceAll(PASSWORD_REGEX, PASSWORD_MASK) : input;
    }
}
