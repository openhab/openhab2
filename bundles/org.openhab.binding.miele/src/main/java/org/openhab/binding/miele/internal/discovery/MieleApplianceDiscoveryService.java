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
package org.openhab.binding.miele.internal.discovery;

import static org.openhab.binding.miele.internal.MieleBindingConstants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.miele.internal.handler.ApplianceStatusListener;
import org.openhab.binding.miele.internal.handler.MieleApplianceHandler;
import org.openhab.binding.miele.internal.handler.MieleBridgeHandler;
import org.openhab.binding.miele.internal.handler.MieleBridgeHandler.DeviceClassObject;
import org.openhab.binding.miele.internal.handler.MieleBridgeHandler.DeviceProperty;
import org.openhab.binding.miele.internal.handler.MieleBridgeHandler.HomeDevice;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

/**
 * The {@link MieleApplianceDiscoveryService} tracks appliances that are
 * associated with the Miele@Home gateway
 *
 * @author Karel Goderis - Initial contribution
 * @author Martin Lepsy - Added protocol information in order so support WiFi devices
 */
public class MieleApplianceDiscoveryService extends AbstractDiscoveryService implements ApplianceStatusListener {

    private final Logger logger = LoggerFactory.getLogger(MieleApplianceDiscoveryService.class);

    private static final int SEARCH_TIME = 60;

    private MieleBridgeHandler mieleBridgeHandler;

    public MieleApplianceDiscoveryService(MieleBridgeHandler mieleBridgeHandler) {
        super(MieleApplianceHandler.SUPPORTED_THING_TYPES, SEARCH_TIME, false);
        this.mieleBridgeHandler = mieleBridgeHandler;
    }

    public void activate() {
        mieleBridgeHandler.registerApplianceStatusListener(this);
    }

    @Override
    public void deactivate() {
        removeOlderResults(new Date().getTime());
        mieleBridgeHandler.unregisterApplianceStatusListener(this);
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return MieleApplianceHandler.SUPPORTED_THING_TYPES;
    }

    @Override
    public void startScan() {
        List<HomeDevice> appliances = mieleBridgeHandler.getHomeDevices();
        if (appliances != null) {
            for (HomeDevice l : appliances) {
                onApplianceAddedInternal(l);
            }
        }
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    public void onApplianceAdded(HomeDevice appliance) {
        onApplianceAddedInternal(appliance);
    }

    private void onApplianceAddedInternal(HomeDevice appliance) {
        ThingUID thingUID = getThingUID(appliance);
        if (thingUID != null) {
            ThingUID bridgeUID = mieleBridgeHandler.getThing().getUID();
            Map<String, Object> properties = new HashMap<>(2);

            properties.put(PROTOCOL_PROPERTY_NAME, appliance.getProtocol());
            properties.put(APPLIANCE_ID, appliance.getApplianceId());

            for (JsonElement dc : appliance.DeviceClasses) {
                if (dc.getAsString().contains("com.miele.xgw3000.gateway.hdm.deviceclasses.Miele")
                        && !dc.getAsString().equals("com.miele.xgw3000.gateway.hdm.deviceclasses.MieleAppliance")) {
                    properties.put(DEVICE_CLASS, StringUtils.right(dc.getAsString(), dc.getAsString().length()
                            - new String("com.miele.xgw3000.gateway.hdm.deviceclasses.Miele").length()));
                    break;
                }
            }

            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID).withProperties(properties)
                    .withBridge(bridgeUID).withLabel((String) properties.get(DEVICE_CLASS)).build();

            thingDiscovered(discoveryResult);
        } else {
            logger.debug("Discovered an unsupported appliance of vendor '{}' with id {}", appliance.Vendor,
                    appliance.UID);
        }
    }

    @Override
    public void onApplianceRemoved(HomeDevice appliance) {
        ThingUID thingUID = getThingUID(appliance);

        if (thingUID != null) {
            thingRemoved(thingUID);
        }
    }

    @Override
    public void onApplianceStateChanged(String uid, DeviceClassObject dco) {
        // nothing to do
    }

    @Override
    public void onAppliancePropertyChanged(String uid, DeviceProperty dp) {
        // nothing to do
    }

    private ThingUID getThingUID(HomeDevice appliance) {
        ThingUID bridgeUID = mieleBridgeHandler.getThing().getUID();
        String modelID = null;

        for (JsonElement dc : appliance.DeviceClasses) {
            if (dc.getAsString().contains("com.miele.xgw3000.gateway.hdm.deviceclasses.Miele")
                    && !dc.getAsString().equals("com.miele.xgw3000.gateway.hdm.deviceclasses.MieleAppliance")) {
                modelID = StringUtils.right(dc.getAsString(), dc.getAsString().length()
                        - new String("com.miele.xgw3000.gateway.hdm.deviceclasses.Miele").length());
                break;
            }
        }

        if (modelID != null) {
            ThingTypeUID thingTypeUID = new ThingTypeUID(BINDING_ID,
                    StringUtils.lowerCase(modelID.replaceAll("[^a-zA-Z0-9_]", "_")));

            if (getSupportedThingTypes().contains(thingTypeUID)) {
                ThingUID thingUID = new ThingUID(thingTypeUID, bridgeUID, appliance.getId());
                return thingUID;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
