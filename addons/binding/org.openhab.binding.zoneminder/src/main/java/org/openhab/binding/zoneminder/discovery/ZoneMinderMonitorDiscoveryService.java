/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zoneminder.discovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.zoneminder.ZoneMinderConstants;
import org.openhab.binding.zoneminder.handler.ZoneMinderServerBridgeHandler;
import org.openhab.binding.zoneminder.handler.ZoneMinderThingMonitorHandler;
import org.openhab.binding.zoneminder.internal.api.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When a {@link ZoneMinderMonitorDiscoveryService} finds a new Monitor we will
 * add it to the system.
 *
 * @author Martin S. Eskildsen - Highly inspired by Dan Cunningham's SqueezeBox binding
 *
 */
public class ZoneMinderMonitorDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(ZoneMinderMonitorDiscoveryService.class);

    private final static int TIMEOUT = 60;

    private ZoneMinderServerBridgeHandler zoneMinderServerHandler;
    private ScheduledFuture<?> requestMonitorJob;

    /**
     * Discovers ZoneMinder Monitors attached to a ZoneMinder Server
     *
     * @param soneMinderServerHandler
     */
    public ZoneMinderMonitorDiscoveryService(ZoneMinderServerBridgeHandler zoneMinderServerHandler) {
        super(ZoneMinderThingMonitorHandler.SUPPORTED_THING_TYPES, TIMEOUT, true);
        this.zoneMinderServerHandler = zoneMinderServerHandler;

        setupRequestMonitorJob();
    }

    @Override
    protected void startScan() {
        logger.debug("startScan invoked in ZoneMonitorDiscoveryService");
        // this.zoneMinderServerHandler.getMonitors();
    }

    /*
     * Allows request player job to be canceled when server handler is removed
     */
    public void cancelRequestMonitorJob() {
        logger.debug("canceling RequestMonitorJob");
        if (requestMonitorJob != null) {
            requestMonitorJob.cancel(true);
            requestMonitorJob = null;
        }
    }

    public void monitorAdded(MonitorData monitor) {

        try {
            ThingUID bridgeUID = zoneMinderServerHandler.getThing().getUID();
            // TODO:: THis is not good, fix the hardcoding
            String monitorUID = "monitor-" + monitor.getId();
            ThingUID thingUID = new ThingUID(ZoneMinderConstants.THING_TYPE_THING_ZONEMINDER_MONITOR, bridgeUID,
                    monitorUID);

            if (!monitorThingExists(thingUID)) {
                logger.debug("monitor added {} : {} ", monitor.getOpenHABId(), monitor.getDisplayName());

                Map<String, Object> properties = new HashMap<>(0);
                properties.put(ZoneMinderConstants.PARAMETER_MONITOR_ID, Integer.getInteger(monitor.getId()));
                properties.put(ZoneMinderConstants.PARAMETER_MONITOR_TRIGGER_TIMEOUT,
                        ZoneMinderConstants.PARAMETER_MONITOR_TRIGGER_TIMEOUT_DEFAULTVALUE);
                properties.put(ZoneMinderConstants.PARAMETER_MONITOR_EVENTTEXT,
                        ZoneMinderConstants.PARAMETER_MONITOR_EVENTTEXT_DEFAULTVALUE);

                DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                        .withBridge(bridgeUID).withLabel(monitor.getDisplayName()).build();

                thingDiscovered(discoveryResult);
            }
        } catch (Exception ex) {
            logger.error("Error occurred when calling 'monitorAdded' from Discovery. Exception={}", ex.getMessage());
        }
    }

    private boolean monitorThingExists(ThingUID newThingUID) {
        return zoneMinderServerHandler.getThingByUID(newThingUID) != null ? true : false;
    }

    /**
     * Tells the bridge to request a list of Monitors
     */
    private void setupRequestMonitorJob() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ArrayList<MonitorData> monitors = zoneMinderServerHandler.getMonitors();

                for (MonitorData monitor : monitors) {
                    monitorAdded(monitor);
                }
            }
        };

        logger.debug("request monitor discovery job scheduled to run every {} seconds", TIMEOUT);
        requestMonitorJob = scheduler.scheduleWithFixedDelay(runnable, 10, TIMEOUT, TimeUnit.SECONDS);
    }

}
