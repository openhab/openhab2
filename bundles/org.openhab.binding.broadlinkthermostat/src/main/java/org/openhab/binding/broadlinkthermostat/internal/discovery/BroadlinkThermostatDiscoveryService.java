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
package org.openhab.binding.broadlinkthermostat.internal.discovery;

import static org.openhab.binding.broadlinkthermostat.internal.BroadlinkThermostatBindingConstants.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.broadlinkthermostat.internal.BroadlinkThermostatBindingConstants;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.net.NetworkAddressService;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mob41.blapi.BLDevice;

/**
 * The {@link BroadlinkThermostatDiscoveryService} is responsible for discovering Broadlinkthermostat devices through
 * Broadcast.
 *
 * @author Florian Mueller - Initial contribution
 */
@Component(service = DiscoveryService.class, configurationPid = "discovery.broadlinkthermostat")
@NonNullByDefault
public class BroadlinkThermostatDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(BroadlinkThermostatDiscoveryService.class);

    private final NetworkAddressService networkAddressService;

    private static final Set<ThingTypeUID> DISCOVERABLE_THING_TYPES_UIDS = Set.of(FLOUREON_THERMOSTAT_THING_TYPE,
            UNKNOWN_BROADLINKTHERMOSTAT_THING_TYPE);
    private static final int DISCOVERY_TIMEOUT = 30;
    private final Runnable scanner;
    private @Nullable ScheduledFuture<?> backgroundDiscoveryFuture;

    @Activate
    public BroadlinkThermostatDiscoveryService(@Reference NetworkAddressService networkAddressService) {
        super(DISCOVERABLE_THING_TYPES_UIDS, DISCOVERY_TIMEOUT);
        this.networkAddressService = networkAddressService;
        scanner = createScanner();
    }

    private Runnable createScanner() {
        return () -> {
            long timestampOfLastScan = getTimestampOfLastScan();
            BLDevice[] blDevices = new BLDevice[0];
            try {
                @Nullable
                InetAddress sourceAddress = getIpAddress();
                if (sourceAddress != null) {
                    logger.debug("Using source address {} for sending out broadcast request.", sourceAddress);
                    blDevices = BLDevice.discoverDevices(sourceAddress, 0, DISCOVERY_TIMEOUT * 1000);
                } else {
                    blDevices = BLDevice.discoverDevices(DISCOVERY_TIMEOUT * 1000);
                }
            } catch (IOException e) {
                logger.debug("Error while trying to discover broadlinkthermostat devices: {}", e.getMessage());
            }
            logger.debug("Discovery service found {} broadlinkthermostat devices.", blDevices.length);

            for (BLDevice dev : blDevices) {
                logger.debug("Broadlinkthermostat device {} of type {} with Host {} and MAC {}",
                        dev.getDeviceDescription(), Integer.toHexString(dev.getDeviceType()), dev.getHost(),
                        dev.getMac());

                ThingUID thingUID;
                String id = dev.getHost().replaceAll("\\.", "-");
                logger.debug("Device ID with IP address replacement: {}", id);
                try {
                    id = getHostnameWithoutDomain(InetAddress.getByName(dev.getHost()).getHostName());
                    logger.debug("Device ID with DNS name: {}", id);
                } catch (UnknownHostException e) {
                    logger.debug("Discovered device with IP {} does not have a DNS name, using IP as thing UID.",
                            dev.getHost());
                }

                switch (dev.getDeviceDescription()) {
                    case "Floureon Thermostat":
                        thingUID = new ThingUID(FLOUREON_THERMOSTAT_THING_TYPE, id);
                        break;
                    case "Hysen Thermostat":
                        thingUID = new ThingUID(HYSEN_THERMOSTAT_THING_TYPE, id);
                        break;
                    default:
                        thingUID = new ThingUID(UNKNOWN_BROADLINKTHERMOSTAT_THING_TYPE, id);
                }

                Map<String, Object> properties = new HashMap<>();
                properties.put(BroadlinkThermostatBindingConstants.HOST, dev.getHost());
                properties.put(Thing.PROPERTY_MAC_ADDRESS, dev.getMac().getMacString());
                properties.put(BroadlinkThermostatBindingConstants.DESCRIPTION, dev.getDeviceDescription());

                logger.debug("Property map: {}", properties);

                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                        .withLabel(dev.getDeviceDescription() + " (" + id + ")")
                        .withRepresentationProperty(Thing.PROPERTY_MAC_ADDRESS).build();

                thingDiscovered(discoveryResult);

            }
            removeOlderResults(timestampOfLastScan);
        };
    }

    @Override
    protected void startScan() {
        scheduler.execute(scanner);
    }

    @Override
    protected void startBackgroundDiscovery() {
        logger.trace("Starting background scan for Broadlinkthermostat devices");
        @Nullable
        ScheduledFuture<?> currentBackgroundDiscoveryFuture = backgroundDiscoveryFuture;
        if (currentBackgroundDiscoveryFuture != null && !currentBackgroundDiscoveryFuture.isDone()) {
            currentBackgroundDiscoveryFuture.cancel(true);
        }
        backgroundDiscoveryFuture = scheduler.scheduleWithFixedDelay(scanner, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.trace("Stopping background scan for Broadlinkthermostat devices");
        @Nullable
        ScheduledFuture<?> backgroundDiscoveryFuture = this.backgroundDiscoveryFuture;
        if (backgroundDiscoveryFuture != null && !backgroundDiscoveryFuture.isCancelled()) {
            if (backgroundDiscoveryFuture.cancel(true)) {
                this.backgroundDiscoveryFuture = null;
            }
        }
        stopScan();
    }

    private @Nullable InetAddress getIpAddress() {
        return getIpFromNetworkAddressService().orElse(null);
    }

    /**
     * Uses openHAB's NetworkAddressService to determine the local primary network interface.
     *
     * @return local ip or <code>empty</code> if configured primary IP is not set or could not be parsed.
     */
    private Optional<InetAddress> getIpFromNetworkAddressService() {
        @Nullable
        String ipAddress = networkAddressService.getPrimaryIpv4HostAddress();
        if (ipAddress == null) {
            logger.warn("No network interface could be found.");
            return Optional.empty();
        }
        try {
            return Optional.of(InetAddress.getByName(ipAddress));
        } catch (UnknownHostException e) {
            logger.warn("Configured primary IP cannot be parsed: {} Details: {}", ipAddress, e.getMessage());
            return Optional.empty();
        }
    }

    private String getHostnameWithoutDomain(String hostname) {
        String broadlinkthermostatRegex = "BroadLink-OEM[-A-Za-z0-9]{12}.*";
        if (hostname.matches(broadlinkthermostatRegex)) {
            String[] dotSeparatedString = hostname.split("\\.");
            logger.debug("Found original broadlink DNS name {}, removing domain", hostname);
            return dotSeparatedString[0].replaceAll("\\.", "-");
        } else {
            logger.debug("DNS name does not match original broadlink name: {}, using it without modification. ",
                    hostname);
            return hostname.replaceAll("\\.", "-");
        }
    }
}
