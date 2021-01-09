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
package org.openhab.binding.wlanthermo.internal.api.mini;

import java.net.URISyntaxException;
import java.util.concurrent.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.wlanthermo.internal.WlanThermoConfiguration;
import org.openhab.binding.wlanthermo.internal.WlanThermoException;
import org.openhab.binding.wlanthermo.internal.WlanThermoUnknownChannelException;
import org.openhab.binding.wlanthermo.internal.api.mini.dto.builtin.App;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link WlanThermoMiniHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Christian Schlipp - Initial contribution
 */
@NonNullByDefault
public class WlanThermoMiniHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(WlanThermoMiniHandler.class);

    private WlanThermoConfiguration config = new WlanThermoConfiguration();
    private final HttpClient httpClient;
    private @Nullable ScheduledFuture<?> pollingScheduler;
    private final Gson gson = new Gson();
    private @Nullable App app = new App();

    public WlanThermoMiniHandler(Thing thing, HttpClient httpClient) {
        super(thing);
        this.httpClient = httpClient;
    }

    @Override
    public void initialize() {
        config = getConfigAs(WlanThermoConfiguration.class);

        updateStatus(ThingStatus.UNKNOWN);
        pollingScheduler = scheduler.schedule(this::checkConnection, config.getPollingInterval(), TimeUnit.SECONDS);
    }

    private void checkConnection() {
        try {
            if (httpClient.GET(config.getUri("/app.php")).getStatus() == 200) {
                updateStatus(ThingStatus.ONLINE);
                ScheduledFuture<?> oldScheduler = pollingScheduler;
                if (oldScheduler != null) {
                    oldScheduler.cancel(false);
                }
                pollingScheduler = scheduler.scheduleWithFixedDelay(this::update, 0, config.getPollingInterval(),
                        TimeUnit.SECONDS);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "WlanThermo not found under given address.");
            }
        } catch (URISyntaxException | InterruptedException | ExecutionException | TimeoutException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Could not connect to WlanThermo at " + config.getIpAddress() + ": " + e.getMessage());
            ScheduledFuture<?> oldScheduler = pollingScheduler;
            if (oldScheduler != null) {
                oldScheduler.cancel(false);
            }
            pollingScheduler = scheduler.schedule(this::checkConnection, config.getPollingInterval(), TimeUnit.SECONDS);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Mini is read only!
        if (command instanceof RefreshType) {
            try {
                assert app != null;
                State s = WlanThermoMiniCommandHandler.getState(channelUID, app);
                updateState(channelUID, s);
            } catch (WlanThermoException e) {
                logger.debug("Could not handle command of type {} for channel {}!",
                        command.getClass().toGenericString(), channelUID.getId());
            }
        }
    }

    private void update() {
        try {
            // Update objects with data from device
            String json = httpClient.GET(config.getUri("/app.php")).getContentAsString();
            app = gson.fromJson(json, App.class);
            logger.debug("Received at /app.php: {}", json);

            // Update channels
            for (Channel channel : thing.getChannels()) {
                try {
                    State state = WlanThermoMiniCommandHandler.getState(channel.getUID(), app);
                    updateState(channel.getUID(), state);
                } catch (WlanThermoUnknownChannelException e) {
                    // if we could not obtain a state, try trigger instead
                    String trigger = WlanThermoMiniCommandHandler.getTrigger(channel.getUID(), app);
                    triggerChannel(channel.getUID(), trigger);
                }
            }

        } catch (URISyntaxException | ExecutionException | TimeoutException | WlanThermoException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Update failed: " + e.getMessage());
            ScheduledFuture<?> oldScheduler = pollingScheduler;
            if (oldScheduler != null) {
                oldScheduler.cancel(false);
            }
            for (Channel channel : thing.getChannels()) {
                updateState(channel.getUID(), UnDefType.UNDEF);
            }
            checkConnection();
        } catch (InterruptedException e) {
            logger.debug("Update interrupted. {}", e.getMessage());
        }
    }

    @Override
    public void dispose() {
        ScheduledFuture<?> oldScheduler = pollingScheduler;
        if (oldScheduler != null) {
            boolean stopped = oldScheduler.cancel(true);
            logger.debug("Stopped polling: {}", stopped);
        }
        pollingScheduler = null;
    }
}
