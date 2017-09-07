/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.avmfritz.internal.discovery;

import static org.openhab.binding.avmfritz.BindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.avmfritz.BindingConstants;
import org.openhab.binding.avmfritz.handler.BoxHandler;
import org.openhab.binding.avmfritz.internal.ahamodel.DeviceModel;
import org.openhab.binding.avmfritz.internal.hardware.callbacks.FritzAhaDiscoveryCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discover all AHA (AVM Home Automation) devices connected to a FRITZ!Box
 * device.
 *
 * @author Robert Bausdorf - Initial contribution
 *
 */
public class AVMFritzDiscoveryService extends AbstractDiscoveryService {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(AVMFritzDiscoveryService.class);

    /**
     * Maximum time to search for devices.
     */
    private static final int SEARCH_TIME = 30;

    /**
     * Initial delay in s for scanning job.
     */
    private static final int INITIAL_DELAY = 5;

    /**
     * Scan interval in s for scanning job.
     */
    private static final int SCAN_INTERVAL = 180;
    /**
     * Handler of the bridge of which devices have to be discovered.
     */
    private BoxHandler bridgeHandler;
    /**
     * Job which will do the FRITZ!Box background scanning
     */
    private FritzScan scanningRunnable;
    /**
     * Schedule for scanning
     */
    private ScheduledFuture<?> scanningJob;

    public AVMFritzDiscoveryService(BoxHandler bridgeHandler) {
        super(BindingConstants.SUPPORTED_DEVICE_THING_TYPES_UIDS, SEARCH_TIME);
        logger.debug("initialize discovery service");
        this.bridgeHandler = bridgeHandler;
        this.scanningRunnable = new FritzScan(this);
        if (bridgeHandler == null) {
            logger.warn("no bridge handler for scan given");
        }
        this.activate(null);
    }

    public void deactivate() {
        super.deactivate();
    }

    /**
     * Called from the UI when starting a search.
     */
    @Override
    public void startScan() {
        logger.debug("start manual scan on bridge {}", bridgeHandler.getThing().getUID());
        FritzAhaDiscoveryCallback callback = new FritzAhaDiscoveryCallback(bridgeHandler.getWebInterface(), this);
        bridgeHandler.getWebInterface().asyncGet(callback);
    }

    /**
     * Stops a running scan.
     */
    @Override
    protected synchronized void stopScan() {
        logger.debug("stop manual scan on bridge {}", bridgeHandler.getThing().getUID());
        super.stopScan();
    }

    /**
     * Add one discovered AHA device to inbox.
     *
     * @param device Device model received from a FRITZ!Box
     */
    public void onDeviceAddedInternal(DeviceModel device) {
        final ThingUID thingUID = bridgeHandler.getThingUID(device);
        if (thingUID != null) {
            final ThingUID bridgeUID = bridgeHandler.getThing().getUID();
            final Map<String, Object> properties = new HashMap<>();
            properties.put(THING_AIN, device.getIdentifier());
            properties.put(Thing.PROPERTY_VENDOR, device.getManufacturer());
            properties.put(Thing.PROPERTY_SERIAL_NUMBER, device.getIdentifier());
            properties.put(Thing.PROPERTY_FIRMWARE_VERSION, device.getFirmwareVersion());

            final DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                    .withRepresentationProperty(device.getIdentifier()).withBridge(bridgeUID)
                    .withLabel(device.getName()).build();

            thingDiscovered(discoveryResult);
        } else {
            logger.debug("discovered unsupported device with id {}", device.getIdentifier());
        }
    }

    /**
     * Starts background scanning for attached devices.
     */
    @Override
    protected void startBackgroundDiscovery() {
        if (scanningJob == null || scanningJob.isCancelled()) {
            logger.debug("start background scanning job at intervall {}s", SCAN_INTERVAL);
            scanningJob = AbstractDiscoveryService.scheduler.scheduleWithFixedDelay(scanningRunnable, INITIAL_DELAY,
                    SCAN_INTERVAL, TimeUnit.SECONDS);
        } else {
            logger.debug("scanningJob active");
        }
    }

    /**
     * Stops background scanning for attached devices.
     */
    @Override
    protected void stopBackgroundDiscovery() {
        if (scanningJob != null && !scanningJob.isCancelled()) {
            logger.debug("stop background scanning job");
            scanningJob.cancel(true);
            scanningJob = null;
        }
    }

    /**
     * Scanning worker class.
     */
    public class FritzScan implements Runnable {
        /**
         * Handler for delegation to callbacks.
         */
        private AVMFritzDiscoveryService service;

        /**
         * Constructor.
         *
         * @param handler
         */
        public FritzScan(AVMFritzDiscoveryService service) {
            this.service = service;
        }

        /**
         * Poll the FRITZ!Box websevice one time.
         */
        @Override
        public void run() {
            logger.debug("start background scan on bridge {}", bridgeHandler.getThing().getUID());
            FritzAhaDiscoveryCallback callback = new FritzAhaDiscoveryCallback(bridgeHandler.getWebInterface(),
                    service);
            bridgeHandler.getWebInterface().asyncGet(callback);
        }
    }
}
