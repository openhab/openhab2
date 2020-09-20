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
package org.openhab.binding.airquality.internal.discovery;

import static org.openhab.binding.airquality.internal.AirQualityBindingConstants.*;
import static org.openhab.binding.airquality.internal.AirQualityConfiguration.LOCATION;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.i18n.LocationProvider;
import org.openhab.core.library.types.PointType;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AirQualityDiscoveryService} creates things based on the configured location.
 *
 * @author Gaël L'hopital - Initial Contribution
 */
@Component(service = DiscoveryService.class, configurationPid = "discovery.airquality")
@NonNullByDefault
public class AirQualityDiscoveryService extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(AirQualityDiscoveryService.class);

    private static final int DISCOVER_TIMEOUT_SECONDS = 10;
    private static final int LOCATION_CHANGED_CHECK_INTERVAL = 60;

    private final LocationProvider locationProvider;
    private @Nullable ScheduledFuture<?> discoveryJob;
    private @Nullable PointType previousLocation;

    /**
     * Creates a AirQualityDiscoveryService with enabled autostart.
     */
    @Activate
    public AirQualityDiscoveryService(@Reference LocationProvider locationProvider) {
        super(SUPPORTED_THING_TYPES, DISCOVER_TIMEOUT_SECONDS, true);
        this.locationProvider = locationProvider;
    }

    @Override
    protected void activate(@Nullable Map<String, @Nullable Object> configProperties) {
        super.activate(configProperties);
    }

    @Override
    @Modified
    protected void modified(@Nullable Map<String, @Nullable Object> configProperties) {
        super.modified(configProperties);
    }

    @Override
    protected void startScan() {
        logger.debug("Starting Air Quality discovery scan");
        PointType location = locationProvider.getLocation();
        if (location == null) {
            logger.debug("LocationProvider.getLocation() is not set -> Will not provide any discovery results");
            return;
        }
        createResults(location);
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (discoveryJob == null) {
            discoveryJob = scheduler.scheduleWithFixedDelay(() -> {
                PointType currentLocation = locationProvider.getLocation();
                if (currentLocation != null && !Objects.equals(currentLocation, previousLocation)) {
                    logger.debug("Location has been changed from {} to {}: Creating new discovery results",
                            previousLocation, currentLocation);
                    createResults(currentLocation);
                    previousLocation = currentLocation;
                }
            }, 0, LOCATION_CHANGED_CHECK_INTERVAL, TimeUnit.SECONDS);
            logger.debug("Scheduled Air Qualitylocation-changed job every {} seconds", LOCATION_CHANGED_CHECK_INTERVAL);
        }
    }

    public void createResults(PointType location) {
        ThingUID localAirQualityThing = new ThingUID(THING_TYPE_AQI, LOCAL);
        Map<String, Object> properties = new HashMap<>();
        properties.put(LOCATION, String.format("%s,%s", location.getLatitude(), location.getLongitude()));
        thingDiscovered(DiscoveryResultBuilder.create(localAirQualityThing).withLabel("Local Air Quality")
                .withProperties(properties).build());
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.debug("Stopping Air Quality background discovery");
        ScheduledFuture<?> job = this.discoveryJob;
        if (job != null && !job.isCancelled()) {
            if (job.cancel(true)) {
                discoveryJob = null;
                logger.debug("Stopped Air Quality background discovery");
            }
        }
    }
}
