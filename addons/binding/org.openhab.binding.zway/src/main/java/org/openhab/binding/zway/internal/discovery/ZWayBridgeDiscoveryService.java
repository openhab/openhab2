/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zway.internal.discovery;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.net.util.SubnetUtils;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.zway.ZWayBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ZWayBridgeDiscoveryService} is responsible for device discovery.
 *
 * @author Patrick Hecker - Initial contribution
 */
public class ZWayBridgeDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static int SEARCH_TIME = 240;
    private final static int INITIAL_DELAY = 15;
    private final static int SCAN_INTERVAL = 3600;

    private ZWayDeviceScan mZWayServerScanningRunnable;
    private ScheduledFuture<?> mZWayServerScanningJob;

    public ZWayBridgeDiscoveryService() {
        super(ZWayBindingConstants.SUPPORTED_DEVICE_THING_TYPES_UIDS, SEARCH_TIME);
        logger.debug("Initializing ZWayBridgeDiscoveryService");

        mZWayServerScanningRunnable = new ZWayDeviceScan();
        activate(null);
    }

    private void scan() {
        logger.debug("Starting scan for Z-Way Server");

        ValidateIPV4 validator = new ValidateIPV4();

        try {
            Enumeration<NetworkInterface> enumNetworkInterface = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterface.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterface.nextElement();
                if (networkInterface.isUp() && !networkInterface.isVirtual() && !networkInterface.isLoopback()) {
                    for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                        if (validator.isValidIPV4(address.getAddress().getHostAddress())) {
                            String ipAddress = address.getAddress().getHostAddress();
                            Short prefix = address.getNetworkPrefixLength();

                            logger.debug("Scan IP address for Z-Way Server: {}", ipAddress);

                            // Search on localhost first
                            scheduler.execute(new ZWayServerScan(ipAddress));

                            String subnet = ipAddress + "/" + prefix;
                            SubnetUtils utils = new SubnetUtils(subnet);
                            String[] addresses = utils.getInfo().getAllAddresses();

                            for (String addressInSubnet : addresses) {
                                scheduler.execute(new ZWayServerScan(addressInSubnet));
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn("Error occured while searching Z-Way servers ({})", e.getMessage());
        }
    }

    public boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public class ZWayServerScan implements Runnable {
        private String ipAddress;

        public ZWayServerScan(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        @Override
        public void run() {
            if (!pingHost(ipAddress, 8083, 500)) {
                return; // Error occured while searching Z-Way servers (Unreachable)
            }

            try {
                URL url = new URL("http://" + ipAddress + ":8083/ZAutomation/api/v1/status");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == 401) {
                    logger.info("(DiscoverySuccess) Z-Way server found on host: {}", ipAddress);

                    ThingUID thingUID = new ThingUID(ZWayBindingConstants.THING_TYPE_BRIDGE,
                            ipAddress.replaceAll("\\.", "_"));

                    // Attention: if is already present as thing in the ThingRegistry
                    // the configuration for thing will be updated!
                    DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                            .withProperty(ZWayBindingConstants.BRIDGE_CONFIG_ZWAY_SERVER_IP_ADDRESS, ipAddress)
                            .withLabel("Z-Way Server " + ipAddress).build();
                    thingDiscovered(discoveryResult);
                } else {
                    logger.info("No Z-Way server found on host: {}", ipAddress);
                }
            } catch (Exception e) {
                logger.info(e.getClass().toString());
                logger.info("Z-Way server not found on host: {} ({})", ipAddress, e.getMessage());
            }
        }
    }

    @Override
    protected void startScan() {
        scan();
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (mZWayServerScanningJob == null || mZWayServerScanningJob.isCancelled()) {
            logger.debug("Starting background scanning job");
            mZWayServerScanningJob = AbstractDiscoveryService.scheduler.scheduleWithFixedDelay(
                    mZWayServerScanningRunnable, INITIAL_DELAY, SCAN_INTERVAL, TimeUnit.SECONDS);
        } else {
            logger.debug("Scanning job is allready active");
        }
    }

    @Override
    protected void stopBackgroundDiscovery() {
        if (mZWayServerScanningJob != null && !mZWayServerScanningJob.isCancelled()) {
            mZWayServerScanningJob.cancel(false);
            mZWayServerScanningJob = null;
        }
    }

    public class ZWayDeviceScan implements Runnable {
        @Override
        public void run() {
            scan();
        }
    }

    class ValidateIPV4 {
        private final String ipV4Regex = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        private Pattern ipV4Pattern = Pattern.compile(ipV4Regex);

        public boolean isValidIPV4(final String s) {
            return ipV4Pattern.matcher(s).matches();
        }
    }
}
