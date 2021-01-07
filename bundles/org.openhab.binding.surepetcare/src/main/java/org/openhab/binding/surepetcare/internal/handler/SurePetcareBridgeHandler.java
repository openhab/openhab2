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
package org.openhab.binding.surepetcare.internal.handler;

import static org.openhab.binding.surepetcare.internal.SurePetcareConstants.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.surepetcare.internal.AuthenticationException;
import org.openhab.binding.surepetcare.internal.SurePetcareAPIHelper;
import org.openhab.binding.surepetcare.internal.discovery.SurePetcareDiscoveryService;
import org.openhab.binding.surepetcare.internal.dto.SurePetcareBridgeConfiguration;
import org.openhab.binding.surepetcare.internal.dto.SurePetcareDevice;
import org.openhab.binding.surepetcare.internal.dto.SurePetcareHousehold;
import org.openhab.binding.surepetcare.internal.dto.SurePetcarePet;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SurePetcareBridgeHandler} is responsible for handling the bridge things created to use the Sure Petcare
 * API. This way, the user credentials may be entered only once.
 *
 * It also spawns 2 background polling threads to update the more static data (topology) and the pet locations at
 * different time intervals.
 *
 * @author Rene Scherer - Initial Contribution
 */
@NonNullByDefault
public class SurePetcareBridgeHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(SurePetcareBridgeHandler.class);

    private final SurePetcareAPIHelper petcareAPI;
    private @Nullable ScheduledFuture<?> topologyPollingJob;
    private @Nullable ScheduledFuture<?> petStatusPollingJob;

    public SurePetcareBridgeHandler(Bridge bridge, SurePetcareAPIHelper petcareAPI) {
        super(bridge);
        this.petcareAPI = petcareAPI;
    }

    @Override
    // @SuppressWarnings("null")
    public void initialize() {
        logger.debug("Initializing Sure Petcare bridge handler.");
        SurePetcareBridgeConfiguration config = getConfigAs(SurePetcareBridgeConfiguration.class);

        if (config.username != null && config.password != null) {
            updateStatus(ThingStatus.UNKNOWN);
            try {
                logger.debug("Login to SurePetcare API with username: {}", config.username);
                petcareAPI.login(config.username, config.password);
                logger.debug("Login successful, updating topology cache");
                petcareAPI.updateTopologyCache();
                logger.debug("Cache update successful, setting bridge status to ONLINE");
                updateStatus(ThingStatus.ONLINE);
                updateThings();
            } catch (AuthenticationException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "@text/offline.conf-error-authentication");
            }
        } else {
            logger.warn("Setting thing '{}' to OFFLINE: Parameter 'password' and 'username' must be configured.",
                    getThing().getUID());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.conf-error-missing-username-or-password");
        }

        if (config.refreshIntervalTopology != null) {
            ScheduledFuture<?> job = topologyPollingJob;
            if (job == null || job.isCancelled()) {
                topologyPollingJob = scheduler.scheduleWithFixedDelay(() -> {
                    petcareAPI.updateTopologyCache();
                    updateThings();
                }, config.refreshIntervalTopology, config.refreshIntervalTopology, TimeUnit.SECONDS);
                logger.debug("Bridge topology polling job every {} seconds", config.refreshIntervalTopology);
            }
        } else {
            logger.warn("Invalid setting for topology refresh interval");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.conf-error-invalid-refresh-intervals");
        }
        if (config.refreshIntervalStatus != null) {
            ScheduledFuture<?> job = petStatusPollingJob;
            if (job == null || job.isCancelled()) {
                petStatusPollingJob = scheduler.scheduleWithFixedDelay(this::pollAndUpdatePetStatus,
                        config.refreshIntervalStatus, config.refreshIntervalStatus, TimeUnit.SECONDS);
                logger.debug("Pet status polling job every {} seconds", config.refreshIntervalStatus);
            }
        } else {
            logger.warn("Invalid setting for status refresh interval");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.conf-error-invalid-refresh-intervals");
        }
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(SurePetcareDiscoveryService.class);
    }

    @SuppressWarnings("null")
    @Override
    public void dispose() {
        if (topologyPollingJob != null && !topologyPollingJob.isCancelled()) {
            topologyPollingJob.cancel(true);
            topologyPollingJob = null;
            logger.debug("Stopped topology background polling process");
        }
        if (petStatusPollingJob != null && !petStatusPollingJob.isCancelled()) {
            petStatusPollingJob.cancel(true);
            petStatusPollingJob = null;
            logger.debug("Stopped pet status background polling process");
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            updateState(BRIDGE_CHANNEL_REFRESH, OnOffType.OFF);
        } else {
            switch (channelUID.getId()) {
                case BRIDGE_CHANNEL_REFRESH:
                    if (OnOffType.ON.equals(command)) {
                        petcareAPI.updateTopologyCache();
                        updateThings();
                        updateState(BRIDGE_CHANNEL_REFRESH, OnOffType.OFF);
                    }
                    break;
            }
        }
    }

    public ThingUID getUID() {
        return thing.getUID();
    }

    public Iterable<SurePetcareHousehold> listHouseholds() {
        return petcareAPI.getTopology().households;
    }

    public Iterable<SurePetcarePet> listPets() {
        return petcareAPI.getTopology().pets;
    }

    public Iterable<SurePetcareDevice> listDevices() {
        return petcareAPI.getTopology().devices;
    }

    protected synchronized void updateThings() {
        logger.debug("Updating {} connected things", getThing().getThings().size());
        // update existing things
        for (Thing th : getThing().getThings()) {
            String tid = th.getUID().getId();
            Map<String, String> properties = null;
            ThingHandler handler = th.getHandler();
            if (handler instanceof SurePetcarePetHandler) {
                ((SurePetcarePetHandler) handler).updateThing();
                SurePetcarePet pet = petcareAPI.getTopology().getById(petcareAPI.getTopology().pets, tid);
                if (pet != null) {
                    properties = pet.getThingProperties();
                }
            } else if (handler instanceof SurePetcareHouseholdHandler) {
                ((SurePetcareHouseholdHandler) handler).updateThing();
                SurePetcareHousehold household = petcareAPI.getTopology().getById(petcareAPI.getTopology().households,
                        tid);
                if (household != null) {
                    properties = household.getThingProperties();
                }
            } else if (handler instanceof SurePetcareDeviceHandler) {
                ((SurePetcareDeviceHandler) handler).updateThing();
                SurePetcareDevice device = petcareAPI.getTopology().getById(petcareAPI.getTopology().devices, tid);
                if (device != null) {
                    properties = device.getThingProperties();
                }
            }
            if ((properties != null) && (handler instanceof SurePetcareBaseObjectHandler)) {
                ((SurePetcareBaseObjectHandler) handler).updateProperties(properties);
            }
        }
    }

    private synchronized void pollAndUpdatePetStatus() {
        petcareAPI.updatePetStatus();
        for (Thing th : getThing().getThings()) {
            if (th.getThingTypeUID().equals(THING_TYPE_PET)) {
                ThingHandler handler = th.getHandler();
                if (handler != null) {
                    ((SurePetcarePetHandler) handler).updateThing();
                }
            }
        }
    }
}
