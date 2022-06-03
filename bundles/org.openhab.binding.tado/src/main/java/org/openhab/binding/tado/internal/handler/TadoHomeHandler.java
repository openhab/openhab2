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
package org.openhab.binding.tado.internal.handler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.tado.internal.TadoBindingConstants;
import org.openhab.binding.tado.internal.TadoBindingConstants.TemperatureUnit;
import org.openhab.binding.tado.internal.api.ApiException;
import org.openhab.binding.tado.internal.api.HomeApiFactory;
import org.openhab.binding.tado.internal.api.client.HomeApi;
import org.openhab.binding.tado.internal.api.model.HomeInfo;
import org.openhab.binding.tado.internal.api.model.HomePresence;
import org.openhab.binding.tado.internal.api.model.HomeState;
import org.openhab.binding.tado.internal.api.model.PresenceState;
import org.openhab.binding.tado.internal.api.model.User;
import org.openhab.binding.tado.internal.api.model.UserHomes;
import org.openhab.binding.tado.internal.config.TadoHomeConfig;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TadoHomeHandler} is the bridge of all home-based things.
 *
 * @author Dennis Frommknecht - Initial contribution
 */
@NonNullByDefault
public class TadoHomeHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(TadoHomeHandler.class);

    private TadoHomeConfig configuration;
    private final HomeApi api;

    private @Nullable Long homeId;
    private @Nullable TadoBatteryChecker batteryChecker;
    private @Nullable ScheduledFuture<?> initializationFuture;

    public TadoHomeHandler(Bridge bridge) {
        super(bridge);
        batteryChecker = new TadoBatteryChecker(this);
        configuration = getConfigAs(TadoHomeConfig.class);
        api = new HomeApiFactory().create(configuration.username, configuration.password);
    }

    public TemperatureUnit getTemperatureUnit() {
        String temperatureUnitStr = this.thing.getProperties()
                .getOrDefault(TadoBindingConstants.PROPERTY_HOME_TEMPERATURE_UNIT, "CELSIUS");
        return TemperatureUnit.valueOf(temperatureUnitStr);
    }

    @Override
    public void initialize() {
        configuration = getConfigAs(TadoHomeConfig.class);
        ScheduledFuture<?> initializationFuture = this.initializationFuture;
        if (initializationFuture == null || initializationFuture.isDone()) {
            this.initializationFuture = scheduler.scheduleWithFixedDelay(
                    this::initializeBridgeStatusAndPropertiesIfOffline, 0, 300, TimeUnit.SECONDS);
        }
    }

    private void initializeBridgeStatusAndPropertiesIfOffline() {
        if (getThing().getStatus() == ThingStatus.ONLINE) {
            for (Thing thing : getThing().getThings()) {
                ThingHandler handler = thing.getHandler();
                if ((handler instanceof BaseHomeThingHandler)
                        && ((BaseHomeThingHandler) handler).shallTryReconnecting(configuration.maxReconnectAttempts)) {
                    scheduler.submit(() -> handler.bridgeStatusChanged(getThing().getStatusInfo()));
                }
            }
            return;
        }

        try {
            // Get user info to verify successful authentication and connection to server
            User user = api.showUser();
            if (user == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Cannot connect to server. Username and/or password might be invalid");
                return;
            }

            List<UserHomes> homes = user.getHomes();
            if (homes == null || homes.isEmpty()) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "User does not have access to any home");
                return;
            }

            Integer firstHomeId = homes.get(0).getId();
            if (firstHomeId == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Missing Home Id");
                return;
            }

            homeId = firstHomeId.longValue();

            HomeInfo homeInfo = api.showHome(homeId);
            TemperatureUnit temperatureUnit = org.openhab.binding.tado.internal.api.model.TemperatureUnit.FAHRENHEIT == homeInfo
                    .getTemperatureUnit() ? TemperatureUnit.FAHRENHEIT : TemperatureUnit.CELSIUS;
            updateProperty(TadoBindingConstants.PROPERTY_HOME_TEMPERATURE_UNIT, temperatureUnit.name());
        } catch (IOException | ApiException e) {
            logger.debug("Error accessing tado server: {}", e.getMessage(), e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Could not connect to server due to " + e.getMessage());
            return;
        }

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void dispose() {
        super.dispose();
        ScheduledFuture<?> initializationFuture = this.initializationFuture;
        if (initializationFuture != null && !initializationFuture.isCancelled()) {
            initializationFuture.cancel(true);
        }
    }

    public HomeApi getApi() {
        return api;
    }

    public @Nullable Long getHomeId() {
        return homeId;
    }

    public HomeState getHomeState() throws IOException, ApiException {
        return api.homeState(getHomeId());
    }

    public void updateHomeState() {
        try {
            updateState(TadoBindingConstants.CHANNEL_HOME_PRESENCE_MODE,
                    getHomeState().getPresence() == PresenceState.HOME ? OnOffType.ON : OnOffType.OFF);
        } catch (IOException | ApiException e) {
            logger.debug("Error accessing tado server: {}", e.getMessage(), e);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String id = channelUID.getId();

        if (command == RefreshType.REFRESH) {
            updateHomeState();
            return;
        }

        switch (id) {
            case TadoBindingConstants.CHANNEL_HOME_PRESENCE_MODE:
                HomePresence presence = new HomePresence();
                presence.setHomePresence(command.toFullString().toUpperCase().equals("ON")
                        || command.toFullString().toUpperCase().equals("HOME") ? PresenceState.HOME
                                : PresenceState.AWAY);
                try {
                    api.updatePresenceLock(homeId, presence);
                } catch (IOException | ApiException e) {
                    logger.warn("Error setting home presence: {}", e.getMessage(), e);
                }

                break;

        }
    }

    public State getBatteryLowAlarm(long zoneId) {
        TadoBatteryChecker batteryChecker = this.batteryChecker;
        return batteryChecker != null ? batteryChecker.getBatteryLowAlarm(zoneId) : UnDefType.UNDEF;
    }
}
