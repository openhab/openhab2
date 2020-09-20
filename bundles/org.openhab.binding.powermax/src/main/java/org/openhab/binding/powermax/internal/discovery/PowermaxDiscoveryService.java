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
package org.openhab.binding.powermax.internal.discovery;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.powermax.internal.PowermaxBindingConstants;
import org.openhab.binding.powermax.internal.config.PowermaxX10Configuration;
import org.openhab.binding.powermax.internal.config.PowermaxZoneConfiguration;
import org.openhab.binding.powermax.internal.handler.PowermaxBridgeHandler;
import org.openhab.binding.powermax.internal.handler.PowermaxThingHandler;
import org.openhab.binding.powermax.internal.state.PowermaxPanelSettings;
import org.openhab.binding.powermax.internal.state.PowermaxPanelSettingsListener;
import org.openhab.binding.powermax.internal.state.PowermaxX10Settings;
import org.openhab.binding.powermax.internal.state.PowermaxZoneSettings;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PowermaxDiscoveryService} is responsible for discovering
 * all enrolled zones and X10 devices
 *
 * @author Laurent Garnier - Initial contribution
 */
public class PowermaxDiscoveryService extends AbstractDiscoveryService implements PowermaxPanelSettingsListener {

    private final Logger logger = LoggerFactory.getLogger(PowermaxDiscoveryService.class);

    private static final int SEARCH_TIME = 5;

    private PowermaxBridgeHandler bridgeHandler;

    /**
     * Creates a PowermaxDiscoveryService with background discovery disabled.
     */
    public PowermaxDiscoveryService(PowermaxBridgeHandler bridgeHandler) {
        super(PowermaxBindingConstants.SUPPORTED_THING_TYPES_UIDS, SEARCH_TIME, true);
        this.bridgeHandler = bridgeHandler;
    }

    /**
     * Activates the Discovery Service.
     */
    public void activate() {
        bridgeHandler.registerPanelSettingsListener(this);
    }

    /**
     * Deactivates the Discovery Service.
     */
    @Override
    public void deactivate() {
        bridgeHandler.unregisterPanelSettingsListener(this);
    }

    @Override
    protected void startScan() {
        logger.debug("Updating discovered things (new scan)");
        updateFromSettings(bridgeHandler.getPanelSettings());
    }

    @Override
    public void onPanelSettingsUpdated(PowermaxPanelSettings settings) {
        logger.debug("Updating discovered things (global settings updated)");
        updateFromSettings(settings);
    }

    @Override
    public void onZoneSettingsUpdated(int zoneNumber, PowermaxPanelSettings settings) {
        logger.debug("Updating discovered things (zone {} updated)", zoneNumber);
        PowermaxZoneSettings zoneSettings = (settings == null) ? null : settings.getZoneSettings(zoneNumber);
        updateFromZoneSettings(zoneNumber, zoneSettings);
    }

    private void updateFromSettings(PowermaxPanelSettings settings) {
        if (settings != null) {
            long beforeUpdate = new Date().getTime();

            for (int i = 1; i <= settings.getNbZones(); i++) {
                PowermaxZoneSettings zoneSettings = settings.getZoneSettings(i);
                updateFromZoneSettings(i, zoneSettings);
            }

            for (int i = 1; i < settings.getNbPGMX10Devices(); i++) {
                PowermaxX10Settings deviceSettings = settings.getX10Settings(i);
                updateFromDeviceSettings(i, deviceSettings);
            }

            // Remove not updated discovered things
            removeOlderResults(beforeUpdate, bridgeHandler.getThing().getUID());
        }
    }

    private void updateFromZoneSettings(int zoneNumber, PowermaxZoneSettings zoneSettings) {
        if (zoneSettings != null) {
            // Prevent for adding already known zone
            for (Thing thing : bridgeHandler.getThing().getThings()) {
                ThingHandler thingHandler = thing.getHandler();
                if (thing.getThingTypeUID().equals(PowermaxBindingConstants.THING_TYPE_ZONE)
                        && thingHandler instanceof PowermaxThingHandler) {
                    PowermaxZoneConfiguration config = ((PowermaxThingHandler) thingHandler).getZoneConfiguration();
                    if (config.zoneNumber == zoneNumber) {
                        return;
                    }
                }
            }

            ThingUID bridgeUID = bridgeHandler.getThing().getUID();
            ThingUID thingUID = new ThingUID(PowermaxBindingConstants.THING_TYPE_ZONE, bridgeUID,
                    String.valueOf(zoneNumber));
            String sensorType = zoneSettings.getSensorType();
            if ("unknown".equalsIgnoreCase(sensorType)) {
                sensorType = "Sensor";
            }
            String name = zoneSettings.getName();
            if ("unknown".equalsIgnoreCase(name)) {
                name = "Alarm Zone " + zoneNumber;
            }
            name = sensorType + " " + name;
            logger.debug("Adding new Powermax alarm zone {} ({}) to inbox", thingUID, name);
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(PowermaxZoneConfiguration.ZONE_NUMBER, zoneNumber);
            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                    .withBridge(bridgeUID).withLabel(name).build();
            thingDiscovered(discoveryResult);
        }
    }

    private void updateFromDeviceSettings(int deviceNumber, PowermaxX10Settings deviceSettings) {
        if (deviceSettings != null && deviceSettings.isEnabled()) {
            // Prevent for adding already known X10 device
            for (Thing thing : bridgeHandler.getThing().getThings()) {
                ThingHandler thingHandler = thing.getHandler();
                if (thing.getThingTypeUID().equals(PowermaxBindingConstants.THING_TYPE_X10)
                        && thingHandler instanceof PowermaxThingHandler) {
                    PowermaxX10Configuration config = ((PowermaxThingHandler) thingHandler).getX10Configuration();
                    if (config.deviceNumber == deviceNumber) {
                        return;
                    }
                }
            }

            ThingUID bridgeUID = bridgeHandler.getThing().getUID();
            ThingUID thingUID = new ThingUID(PowermaxBindingConstants.THING_TYPE_X10, bridgeUID,
                    String.valueOf(deviceNumber));
            String name = (deviceSettings.getName() != null) ? deviceSettings.getName()
                    : ("X10 device " + deviceNumber);
            logger.debug("Adding new Powermax X10 device {} ({}) to inbox", thingUID, name);
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(PowermaxX10Configuration.DEVICE_NUMBER, deviceNumber);
            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                    .withBridge(bridgeUID).withLabel(name).build();
            thingDiscovered(discoveryResult);
        }
    }
}
