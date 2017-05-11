/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.discovery;

import static org.openhab.binding.satel.SatelBindingConstants.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.satel.handler.SatelBridgeHandler;
import org.openhab.binding.satel.internal.BundleTextTranslator;
import org.openhab.binding.satel.internal.command.ReadDeviceInfoCommand;
import org.openhab.binding.satel.internal.command.ReadDeviceInfoCommand.DeviceType;
import org.openhab.binding.satel.internal.command.SatelCommand;
import org.openhab.binding.satel.internal.config.SatelThingConfig;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * The {@link SatelDeviceDiscoveryService} searches for available Satel
 * devices and modules connected to the API console
 *
 * @author Krzysztof Goworek - Initial contribution
 *
 */
public class SatelDeviceDiscoveryService extends AbstractDiscoveryService {

    private static final int OUTPUT_FUNCTION_SHUTTER = 105;

    private final Logger logger = LoggerFactory.getLogger(SatelDeviceDiscoveryService.class);

    private SatelBridgeHandler bridgeHandler;
    private BundleContext bundleContext;
    private volatile boolean scanStopped;

    public SatelDeviceDiscoveryService(SatelBridgeHandler bridgeHandler, BundleContext bundleContext)
            throws IllegalArgumentException {
        super(Sets.union(DEVICE_THING_TYPES_UIDS, VIRTUAL_THING_TYPES_UIDS), 60, true);
        this.bridgeHandler = bridgeHandler;
        this.bundleContext = bundleContext;
    }

    @Override
    protected void startScan() {
        try (BundleTextTranslator translator = new BundleTextTranslator(bundleContext)) {
            scanStopped = false;
            if (bridgeHandler.isInitialized()) {
                addThing(THING_TYPE_SYSTEM, null, translator.getText("system.label", "System"), Collections.emptyMap());
            }
            if (!scanStopped) {
                scanForDevices(DeviceType.PARTITION_WITH_OBJECT, bridgeHandler.getIntegraType().getPartitions());
            }
            if (!scanStopped) {
                scanForDevices(DeviceType.ZONE_WITH_PARTITION, bridgeHandler.getIntegraType().getZones());
            }
            if (!scanStopped) {
                scanForDevices(DeviceType.OUTPUT, bridgeHandler.getIntegraType().getZones());
            }
        }
    }

    @Override
    protected synchronized void stopScan() {
        scanStopped = true;
        super.stopScan();
    }

    private void scanForDevices(DeviceType deviceType, int maxNumber) {
        logger.debug("Scanning for {} started", deviceType.name());
        for (int i = 1; i <= maxNumber && !scanStopped; ++i) {
            try {
                ReadDeviceInfoCommand cmd = new ReadDeviceInfoCommand(deviceType, i);
                cmd.ignoreResponseError();
                if (bridgeHandler.sendCommand(cmd, false)) {
                    String name = cmd.getName(bridgeHandler.getEncoding());
                    int deviceKind = cmd.getDeviceKind();
                    logger.debug("Found device: type={}, id={}, name={}, kind/function={}", deviceType.name(), i, name,
                            deviceKind);
                    if (isDeviceAvailable(deviceType, deviceKind)) {
                        addDevice(deviceType, deviceKind, i, name);
                    }
                } else if (cmd.getState() != SatelCommand.State.FAILED) {
                    // serious failure, disconnection or so
                    scanStopped = true;
                    logger.error("Unexpected failure during scan for {} using {}", deviceType.name(),
                            bridgeHandler.getThing().getUID().toString());
                }
            } catch (UnsupportedEncodingException e) {
                scanStopped = true;
                logger.error("Unsupported encoding '{}' configured for the bridge: {}", bridgeHandler.getEncoding(),
                        bridgeHandler.getThing().getUID().toString());
            }
        }
        if (scanStopped) {
            logger.debug("Scanning stopped");
        } else {
            logger.debug("Scanning for {} finished", deviceType.name());
        }
    }

    private void addDevice(DeviceType deviceType, int deviceKind, int deviceId, String deviceName) {
        ThingTypeUID thingTypeUID = getThingTypeUID(deviceType, deviceKind);

        if (thingTypeUID == null) {
            logger.warn("Unknown device found: type={}, kind={}, name={}", deviceType.name(), deviceKind, deviceName);
        } else if (!getSupportedThingTypes().contains(thingTypeUID)) {
            logger.warn("Unsupported device: {}", thingTypeUID.toString());
        } else {
            Map<String, Object> properties = new HashMap<>();

            if (THING_TYPE_SHUTTER.equals(thingTypeUID)) {
                properties.put(SatelThingConfig.UP_ID, deviceId);
                properties.put(SatelThingConfig.DOWN_ID, deviceId + 1);
            } else {
                properties.put(SatelThingConfig.ID, deviceId);
            }

            addThing(thingTypeUID, String.valueOf(deviceId), deviceName, properties);
        }
    }

    private void addThing(ThingTypeUID thingTypeUID, String deviceId, String label, Map<String, Object> properties) {
        ThingUID bridgeUID = bridgeHandler.getThing().getUID();
        ThingUID thingUID;
        if (deviceId == null) {
            thingUID = new ThingUID(thingTypeUID, bridgeUID.getId());
        } else {
            thingUID = new ThingUID(thingTypeUID, bridgeUID, deviceId);
        }
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withThingType(thingTypeUID)
                .withBridge(bridgeUID).withLabel(label).withProperties(properties).build();
        thingDiscovered(discoveryResult);
    }

    private static ThingTypeUID getThingTypeUID(DeviceType deviceType, int deviceKind) {
        switch (deviceType) {
            case OUTPUT:
                return (deviceKind == OUTPUT_FUNCTION_SHUTTER) ? THING_TYPE_SHUTTER : THING_TYPE_OUTPUT;
            case PARTITION:
            case PARTITION_WITH_OBJECT:
                return THING_TYPE_PARTITION;
            case ZONE:
            case ZONE_WITH_PARTITION:
                return THING_TYPE_ZONE;
            default:
                return null;
        }
    }

    private static boolean isDeviceAvailable(DeviceType deviceType, int deviceKind) {
        switch (deviceType) {
            case OUTPUT:
                return deviceKind != 0 && deviceKind != OUTPUT_FUNCTION_SHUTTER
                        && (deviceKind != OUTPUT_FUNCTION_SHUTTER + 1);
            case PARTITION:
            case PARTITION_WITH_OBJECT:
            case ZONE:
            case ZONE_WITH_PARTITION:
                return true;
            default:
                return false;
        }
    }

}
