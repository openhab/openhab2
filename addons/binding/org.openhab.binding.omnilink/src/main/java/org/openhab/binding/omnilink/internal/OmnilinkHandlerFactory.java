/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import static org.openhab.binding.omnilink.OmnilinkBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.omnilink.discovery.OmnilinkDiscoveryService;
import org.openhab.binding.omnilink.handler.OmnilinkBridgeHandler;
import org.openhab.binding.omnilink.handler.UnitHandler;
import org.openhab.binding.omnilink.handler.ZoneHandler;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link OmnilinkHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Craig - Initial contribution
 */
public class OmnilinkHandlerFactory extends BaseThingHandlerFactory {
    private static final Logger logger = LoggerFactory.getLogger(OmnilinkHandlerFactory.class);
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_AREA,
            THING_TYPE_ZONE, THING_TYPE_UNIT, THING_TYPE_BRIDGE);

    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegistrations = new HashMap<ThingUID, ServiceRegistration<?>>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_UNIT)) {
            return new UnitHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_BRIDGE)) {
            OmnilinkBridgeHandler handler = new OmnilinkBridgeHandler((Bridge) thing);
            registerOmnilnkBridgeDiscoveryService(handler);
            return handler;
        } else if (thingTypeUID.equals(THING_TYPE_ZONE)) {
            return new ZoneHandler(thing);
        }

        return null;
    }

    /**
     * Register the Thing Discovery Service for a bridge.
     *
     * @param bridgeHandler
     */
    private void registerOmnilnkBridgeDiscoveryService(OmnilinkBridgeHandler bridgeHandler) {
        OmnilinkDiscoveryService discoveryService = new OmnilinkDiscoveryService(bridgeHandler);
        discoveryService.activate();

        ServiceRegistration<?> discoveryServiceRegistration = bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>());
        discoveryServiceRegistrations.put(bridgeHandler.getThing().getUID(), discoveryServiceRegistration);

        logger.debug(
                "registerOmnilinkBridgeDiscoveryService(): Bridge Handler - {}, Class Name - {}, Discovery Service - {}",
                bridgeHandler, DiscoveryService.class.getName(), discoveryService);
    }

}
