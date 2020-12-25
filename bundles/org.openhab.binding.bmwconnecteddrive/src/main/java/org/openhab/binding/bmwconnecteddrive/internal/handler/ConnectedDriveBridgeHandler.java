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
package org.openhab.binding.bmwconnecteddrive.internal.handler;

import static org.openhab.binding.bmwconnecteddrive.internal.utils.Constants.ANONYMOUS;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.bmwconnecteddrive.internal.ConnectedDriveConfiguration;
import org.openhab.binding.bmwconnecteddrive.internal.discovery.VehicleDiscovery;
import org.openhab.binding.bmwconnecteddrive.internal.dto.NetworkError;
import org.openhab.binding.bmwconnecteddrive.internal.dto.discovery.Dealer;
import org.openhab.binding.bmwconnecteddrive.internal.dto.discovery.VehiclesContainer;
import org.openhab.binding.bmwconnecteddrive.internal.utils.Constants;
import org.openhab.binding.bmwconnecteddrive.internal.utils.Converter;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ConnectedDriveBridgeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ConnectedDriveBridgeHandler extends BaseBridgeHandler implements StringResponseCallback {
    private final Logger logger = LoggerFactory.getLogger(ConnectedDriveBridgeHandler.class);
    private @Nullable VehicleDiscovery discoveryService;
    private HttpClientFactory httpClientFactory;
    private Optional<ConnectedDriveProxy> proxy = Optional.empty();
    private Optional<ConnectedDriveConfiguration> configuration = Optional.empty();
    private Optional<ScheduledFuture<?>> initializerJob = Optional.empty();
    private Optional<String> troubleshootFingerprint = Optional.empty();

    public ConnectedDriveBridgeHandler(Bridge bridge, HttpClientFactory hcf, BundleContext bc) {
        super(bridge);
        httpClientFactory = hcf;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // no commands available
    }

    @Override
    public void initialize() {
        troubleshootFingerprint = Optional.empty();
        updateStatus(ThingStatus.UNKNOWN);
        configuration = Optional.of(getConfigAs(ConnectedDriveConfiguration.class));
        if (configuration.isPresent()) {
            proxy = Optional.of(new ConnectedDriveProxy(httpClientFactory, configuration.get()));
            // give the system some time to create all predefined Vehicles
            initializerJob = Optional.of(scheduler.schedule(this::requestVehicles, 5, TimeUnit.SECONDS));
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);
        }
    }

    @Override
    public void dispose() {
        if (initializerJob.isPresent()) {
            initializerJob.get().cancel(true);
        }
    }

    public void requestVehicles() {
        if (proxy.isPresent()) {
            proxy.get().requestVehicles(this);
        }
    }

    public String getDiscoveryFingerprint() {
        if (troubleshootFingerprint.isPresent()) {
            VehiclesContainer container = Converter.getGson().fromJson(troubleshootFingerprint.get(),
                    VehiclesContainer.class);
            if (container.vehicles != null) {
                if (container.vehicles.isEmpty()) {
                    return Constants.EMPTY_VEHICLES;
                } else {
                    container.vehicles.forEach(entry -> {
                        entry.vin = ANONYMOUS;
                        entry.breakdownNumber = ANONYMOUS;
                        if (entry.dealer != null) {
                            Dealer d = entry.dealer;
                            d.city = ANONYMOUS;
                            d.country = ANONYMOUS;
                            d.name = ANONYMOUS;
                            d.phone = ANONYMOUS;
                            d.postalCode = ANONYMOUS;
                            d.street = ANONYMOUS;
                        }
                    });
                    return Converter.getGson().toJson(container);
                }
            } else {
                // Vehicles is empty so deliver fingerprint as it is
                return troubleshootFingerprint.get();
            }
        } else {
            return Constants.INVALID;
        }
    }

    private void logFingerPrint() {
        logger.debug("###### BMW ConnectedDrive Binding - Discovery Troubleshoot Fingerprint Data - BEGIN ######");
        logger.debug("### Discovery Result ###");
        logger.debug("{}", getDiscoveryFingerprint());
        logger.debug("###### BMW ConnectedDrive Binding - Discovery Troubleshoot Fingerprint Data - END ######");
    }

    /**
     * There's only the Vehicles response available
     */
    @Override
    public void onResponse(Optional<String> response) {
        boolean firstResponse = troubleshootFingerprint.isEmpty();
        if (response.isPresent()) {
            troubleshootFingerprint = response;
            VehiclesContainer container = Converter.getGson().fromJson(response.get(), VehiclesContainer.class);
            updateStatus(ThingStatus.ONLINE);
            if (container.vehicles != null) {
                discoveryService.onResponse(container);
                container.vehicles.forEach(entry -> {
                    entry.vin = ANONYMOUS;
                    entry.breakdownNumber = ANONYMOUS;
                    if (entry.dealer != null) {
                        Dealer d = entry.dealer;
                        d.city = ANONYMOUS;
                        d.country = ANONYMOUS;
                        d.name = ANONYMOUS;
                        d.phone = ANONYMOUS;
                        d.postalCode = ANONYMOUS;
                        d.street = ANONYMOUS;
                    }
                });
                troubleshootFingerprint = Optional.of(Converter.getGson().toJson(container));
            }
        } else {
            troubleshootFingerprint = Optional.of(Constants.EMPTY_VEHICLES);
        }
        if (firstResponse) {
            logFingerPrint();
        }
    }

    @Override
    public void onError(NetworkError error) {
        boolean firstResponse = troubleshootFingerprint.isEmpty();
        troubleshootFingerprint = Optional.of(error.toJson());
        if (firstResponse) {
            logFingerPrint();
        }
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, error.reason);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(VehicleDiscovery.class);
    }

    public Optional<ConnectedDriveProxy> getProxy() {
        return proxy;
    }

    public void setDiscoveryService(VehicleDiscovery discoveryService) {
        this.discoveryService = discoveryService;
    }
}
