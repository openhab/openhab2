/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.icloud.discovery;

import static org.openhab.binding.icloud.BindingConstants.*;

import java.util.List;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.icloud.handler.BridgeHandler;
import org.openhab.binding.icloud.internal.json.icloud.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device discovery creates a thing in the inbox for each icloud device
 * found in the data received from {@link BridgeHandler}.
 *
 * @author Patrik Gfeller - Initial Contribution
 *
 */
public class DeviceDiscovery extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(DeviceDiscovery.class);
    private static final int TIMEOUT = 10;
    private ThingUID bridgeUID;

    public DeviceDiscovery(BridgeHandler iCloudBridgeHandler) {
        super(SUPPORTED_THING_TYPES_UIDS, TIMEOUT);
        this.bridgeUID = iCloudBridgeHandler.getThing().getUID();
    }

    public void discover(List<Content> content) {
        if (content != null) {
            for (Content record : content) {
                try {
                    String deviceTypeName = record.getDeviceDisplayName();
                    String deviceOwnerName = record.getName();

                    String thingLabel = deviceOwnerName + " (" + deviceTypeName + ")";
                    String deviceId = record.getId();
                    String deviceIdHash = Integer.toHexString(deviceId.hashCode());

                    logger.debug("iCloud device discovery for [{}]", record.getDeviceDisplayName());

                    ThingUID uid = new ThingUID(THING_TYPE_ICLOUDDEVICE, bridgeUID, deviceIdHash);
                    DiscoveryResult result = DiscoveryResultBuilder.create(uid).withBridge(bridgeUID)
                            .withProperty(DEVICE_PROPERTY_ID, deviceId).withRepresentationProperty(DEVICE_PROPERTY_ID)
                            .withLabel(thingLabel).build();

                    logger.debug("Device [{}, {}] found.", deviceIdHash, deviceId);

                    thingDiscovered(result);
                } catch (Exception exception) {
                    logger.warn("Failed to process discovery result", exception);
                }
            }
        }
    }

    @Override
    protected void startScan() {
    }
}
