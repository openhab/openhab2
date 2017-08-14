/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.discovery;

import static org.openhab.binding.plclogo.PLCLogoBindingConstants.THING_TYPE_DEVICE;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.net.util.SubnetUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.model.script.actions.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PLCDiscoveryService} is responsible for discovering devices on
 * the current Network. It uses every Network Interface which is connected to a network.
 * Based on network binding discovery service.
 *
 * @author Alexander Falkenstern - Initial contribution
 */
public class PLCDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(PLCDiscoveryService.class);

    // Bridge config properties
    private static final @NonNull String LOGO_HOST = "address";
    private static final int LOGO_PORT = 102;

    private static final int DISCOVERY_TIMEOUT = 30;
    private static final Set<@NonNull ThingTypeUID> THING_TYPES_UIDS = Collections.singleton(THING_TYPE_DEVICE);

    private static final int CONNECTION_TIMEOUT = 500;
    private @NonNull TreeSet<@NonNull String> addresses = new TreeSet<@NonNull String>();

    private ExecutorService executor;

    private class Runner implements Runnable {
        private final ReentrantLock lock = new ReentrantLock();
        private @NonNull String host;

        public Runner(final @NonNull String address) {
            Objects.requireNonNull(address, "IP may not be null");
            this.host = address;
        }

        @Override
        public void run() {
            try {
                if (Ping.checkVitality(host, LOGO_PORT, CONNECTION_TIMEOUT)) {
                    logger.info("LOGO! device found at: {}.", host);

                    final ThingUID thingUID = new ThingUID(THING_TYPE_DEVICE, host.replace('.', '_'));
                    DiscoveryResultBuilder builder = DiscoveryResultBuilder.create(thingUID);
                    builder.withProperty(LOGO_HOST, host);
                    builder.withLabel(host);

                    lock.lock();
                    try {
                        thingDiscovered(builder.build());
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (IOException exception) {
                logger.debug("LOGO! device not found at: {}.", host);
            }
        }
    }

    /**
     * Constructor.
     */
    public PLCDiscoveryService() {
        super(THING_TYPES_UIDS, DISCOVERY_TIMEOUT);
    }

    @Override
    protected void startScan() {
        stopScan();

        try {
            Enumeration<NetworkInterface> devices = NetworkInterface.getNetworkInterfaces();
            while (devices.hasMoreElements()) {
                final NetworkInterface device = devices.nextElement();
                if (device.isLoopback()) {
                    continue;
                }
                for (InterfaceAddress iface : device.getInterfaceAddresses()) {
                    final InetAddress address = iface.getAddress();
                    if (address instanceof Inet4Address) {
                        final String prefix = String.valueOf(iface.getNetworkPrefixLength());
                        SubnetUtils utilities = new SubnetUtils(address.getHostAddress() + "/" + prefix);
                        addresses.addAll(Arrays.asList(utilities.getInfo().getAllAddresses()));
                    }
                }
            }
        } catch (SocketException exception) {
            addresses.clear();
            logger.warn("LOGO! bridge discovering: {}.", exception.toString());
        }

        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (final @NonNull String address : addresses) {
            executor.execute(new Runner(address));
        }
        stopScan();
    }

    @Override
    protected synchronized void stopScan() {
        logger.debug("Stop scan for LOGO! bridge");
        super.stopScan();

        if (executor != null) {
            try {
                executor.awaitTermination(CONNECTION_TIMEOUT * addresses.size(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException exception) {
                logger.warn("LOGO! bridge discovering: {}.", exception.toString());
            }
            executor.shutdown();
            executor = null;
        }
        addresses.clear();
    }

}
