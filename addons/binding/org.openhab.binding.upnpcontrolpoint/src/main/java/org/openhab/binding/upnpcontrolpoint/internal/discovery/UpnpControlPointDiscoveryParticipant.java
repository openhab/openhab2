/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcontrolpoint.internal.discovery;

import static org.openhab.binding.upnpcontrolpoint.UpnpControlPointBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.upnp.UpnpDiscoveryParticipant;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.jupnp.model.meta.RemoteDevice;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mark Herwege - Initial contribution
 */
@Component(service = { UpnpDiscoveryParticipant.class }, immediate = true)
public class UpnpControlPointDiscoveryParticipant implements UpnpDiscoveryParticipant {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return SUPPORTED_THING_TYPES_UIDS;
    }

    @Override
    public DiscoveryResult createResult(RemoteDevice device) {
        DiscoveryResult result = null;
        ThingUID thingUid = getThingUID(device);
        if (thingUid != null) {
            String label = device.getDetails().getFriendlyName().isEmpty() ? device.getDisplayString()
                    : device.getDetails().getFriendlyName();
            Map<String, Object> properties = new HashMap<>();
            properties.put("ipAddress", device.getIdentity().getDescriptorURL().getHost());
            properties.put("udn", device.getIdentity().getUdn().getIdentifierString());
            result = DiscoveryResultBuilder.create(thingUid).withLabel(label).withProperties(properties)
                    .withRepresentationProperty("udn").build();
        }
        return result;
    }

    @Override
    public ThingUID getThingUID(RemoteDevice device) {
        ThingUID result = null;
        String deviceType = device.getType().getType();
        String manufacturer = device.getDetails().getManufacturerDetails().getManufacturer();
        String model = device.getDetails().getModelDetails().getModelName();
        String serialNumber = device.getDetails().getSerialNumber();

        logger.debug("Device type {}, manufacturer {}, model {}, SN# {}",
                new Object[] { deviceType, manufacturer, model, serialNumber });

        if (deviceType.equalsIgnoreCase("MediaRenderer")) {
            this.logger.debug("Media renderer found: {}, {}", manufacturer, model);
            ThingTypeUID thingTypeUID = THING_TYPE_RENDERER;
            result = new ThingUID(thingTypeUID, device.getIdentity().getUdn().getIdentifierString());
        } else if (deviceType.equalsIgnoreCase("MediaServer")) {
            this.logger.debug("Media server found: {}, {}", manufacturer, model);
            ThingTypeUID thingTypeUID = THING_TYPE_SERVER;
            result = new ThingUID(thingTypeUID, device.getIdentity().getUdn().getIdentifierString());
        }
        return result;
    }
}
