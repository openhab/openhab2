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
package org.openhab.binding.smartthings.internal.discovery;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.smartthings.internal.SmartthingsBindingConstants;
import org.openhab.binding.smartthings.internal.SmartthingsHandlerFactory;
import org.openhab.binding.smartthings.internal.dto.SmartthingsDeviceData;
import org.openhab.binding.smartthings.internal.dto.SmartthingsDiscoveryData;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Smartthings Discovery service
 *
 * @author Bob Raker - Initial contribution
 */
@NonNullByDefault
@Component(service = { DiscoveryService.class,
        EventHandler.class }, immediate = true, configurationPid = "discovery.smartthings", property = "event.topics=org/openhab/binding/smartthings/discovery")
public class SmartthingsDiscoveryService extends AbstractDiscoveryService implements EventHandler {
    private static final int DISCOVERY_TIMEOUT_SEC = 30;
    private static final int INITIAL_DELAY_SEC = 10; // Delay 10 sec to give time for bridge and things to be created
    private static final int SCAN_INTERVAL_SEC = 600;

    private final Pattern findIllegalChars = Pattern.compile("[^A-Za-z0-9_-]");

    private final Logger logger = LoggerFactory.getLogger(SmartthingsDiscoveryService.class);

    public Gson gson;

    @Nullable
    private SmartthingsHandlerFactory smartthingsHandlerFactory;

    @Nullable
    private ScheduledFuture<?> scanningJob;

    /*
     * default constructor
     */
    public SmartthingsDiscoveryService() {
        super(SmartthingsBindingConstants.SUPPORTED_THING_TYPES_UIDS, DISCOVERY_TIMEOUT_SEC);
        gson = new Gson();
    }

    @Reference
    protected void setThingHandlerFactory(ThingHandlerFactory handlerFactory) {
        if (handlerFactory instanceof SmartthingsHandlerFactory) {
            smartthingsHandlerFactory = (SmartthingsHandlerFactory) handlerFactory;
        }
    }

    protected void unsetThingHandlerFactory(ThingHandlerFactory handlerFactory) {
        // Make sure it is this handleFactory that should be unset
        if (handlerFactory == smartthingsHandlerFactory) {
            this.smartthingsHandlerFactory = null;
        }
    }

    /**
     * Called from the UI when starting a search.
     */
    @Override
    public void startScan() {
        sendSmartthingsDiscoveryRequest();
    }

    /**
     * Stops a running scan.
     */
    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    /**
     * Starts background scanning for attached devices.
     */
    @Override
    protected void startBackgroundDiscovery() {
        if (scanningJob == null) {
            this.scanningJob = scheduler.scheduleWithFixedDelay(this::sendSmartthingsDiscoveryRequest,
                    INITIAL_DELAY_SEC, SCAN_INTERVAL_SEC, TimeUnit.SECONDS);
            logger.debug("Discovery background scanning job started");
        }
    }

    /**
     * Stops background scanning for attached devices.
     */
    @Override
    protected void stopBackgroundDiscovery() {
        final ScheduledFuture<?> currentScanningJob = scanningJob;
        if (currentScanningJob != null) {
            currentScanningJob.cancel(false);
            scanningJob = null;
        }
    }

    /**
     * Start the discovery process by sending a discovery request to the Smartthings Hub
     */
    private void sendSmartthingsDiscoveryRequest() {
        final SmartthingsHandlerFactory currentSmartthingsHandlerFactory = smartthingsHandlerFactory;
        if (currentSmartthingsHandlerFactory != null) {
            try {
                String discoveryMsg = String.format("{\"discovery\": \"yes\", \"openHabStartTime\": %d}",
                        System.currentTimeMillis());
                currentSmartthingsHandlerFactory.sendDeviceCommand("/discovery", discoveryMsg);
                // Smartthings will not return a response to this message but will send it's response message
                // which will get picked up by the SmartthingBridgeHandler.receivedPushMessage handler
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                logger.warn("Attempt to send command to the Smartthings hub failed with: {}", e.getMessage());
            }
        }
    }

    /**
     * Handle discovery data returned from the Smartthings hub.
     * The data is delivered into the SmartthingServlet. From there it is sent here via the Event service
     */
    @Override
    public void handleEvent(@Nullable Event event) {
        if (event == null) {
            logger.info("SmartthingsDiscoveryService.handleEvent: event is uexpectedly null");
            return;
        }
        String topic = event.getTopic();
        String data = (String) event.getProperty("data");
        if (data == null) {
            logger.debug("Event received on topic: {} but the data field is null", topic);
            return;
        } else {
            logger.debug("Event received on topic: {}", topic);
        }

        // Two classes are required.
        // 1. SmarthingsDiscoveryData contains timing info and the discovery data which is sent as an array of Strings
        // 2. SmartthingDeviceData contains the device data for one device.
        // First the SmarthingsDiscoveryData is converted from json to java. Then each data string is converted into
        // device data
        SmartthingsDiscoveryData discoveryData = gson.fromJson(data, SmartthingsDiscoveryData.class);

        if (discoveryData.data != null) {
            for (String deviceStr : discoveryData.data) {
                SmartthingsDeviceData deviceData = gson.fromJson(deviceStr, SmartthingsDeviceData.class);
                createDevice(deviceData);
            }
        }
    }

    /**
     * Create a device with the data from the Smartthings hub
     *
     * @param deviceData Device data from the hub
     */
    private void createDevice(SmartthingsDeviceData deviceData) {
        logger.trace("Discovery: Creating device: ThingType {} with name {}", deviceData.capability, deviceData.name);

        // Build the UID as a string smartthings:{ThingType}:{BridgeName}:{DeviceName}
        String name = deviceData.name; // Note: this is necessary for null analysis to work
        if (name == null) {
            logger.info(
                    "Unexpectedly received data for a device with no name. Check the Smartthings hub devices and make sure every device has a name");
            return;
        }
        String deviceNameNoSpaces = name.replaceAll("\\s", "_");
        String smartthingsDeviceName = findIllegalChars.matcher(deviceNameNoSpaces).replaceAll("");
        final SmartthingsHandlerFactory currentSmartthingsHandlerFactory = smartthingsHandlerFactory;
        if (currentSmartthingsHandlerFactory == null) {
            logger.info(
                    "SmartthingsDiscoveryService: smartthingshandlerfactory is unexpectedly null, could not create device {}",
                    deviceData);
            return;
        }
        ThingUID bridgeUid = currentSmartthingsHandlerFactory.getBridgeHandler().getThing().getUID();
        String bridgeId = bridgeUid.getId();
        String uidStr = String.format("smartthings:%s:%s:%s", deviceData.capability, bridgeId, smartthingsDeviceName);

        Map<String, Object> properties = new HashMap<>();
        properties.put("smartthingsName", name);
        properties.put("deviceId", deviceData.id);

        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(new ThingUID(uidStr)).withProperties(properties)
                .withRepresentationProperty("deviceId").withBridge(bridgeUid).withLabel(name).build();

        thingDiscovered(discoveryResult);
    }

}
