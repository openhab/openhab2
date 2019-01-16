/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import static org.openhab.binding.russound.internal.rio.RioConstants.*;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.russound.internal.discovery.RioSystemDeviceDiscoveryService;
import org.openhab.binding.russound.internal.rio.controller.RioControllerHandler;
import org.openhab.binding.russound.internal.rio.source.RioSourceHandler;
import org.openhab.binding.russound.internal.rio.system.RioSystemHandler;
import org.openhab.binding.russound.internal.rio.zone.RioZoneHandler;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RussoundHandlerFactory} is responsible for creating bridge and thing
 * handlers.
 *
 * @author Tim Roberts - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.russound")
public class RussoundHandlerFactory extends BaseThingHandlerFactory {
    private final Logger logger = LoggerFactory.getLogger(RussoundHandlerFactory.class);

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .unmodifiableSet(Stream.of(BRIDGE_TYPE_RIO, BRIDGE_TYPE_CONTROLLER, THING_TYPE_SOURCE, THING_TYPE_ZONE)
                    .collect(Collectors.toSet()));

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(BRIDGE_TYPE_RIO)) {
            final RioSystemHandler sysHandler = new RioSystemHandler((Bridge) thing);
            registerThingDiscovery(sysHandler);
            return sysHandler;
        } else if (thingTypeUID.equals(BRIDGE_TYPE_CONTROLLER)) {
            return new RioControllerHandler((Bridge) thing);
        } else if (thingTypeUID.equals(THING_TYPE_SOURCE)) {
            return new RioSourceHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_ZONE)) {
            return new RioZoneHandler(thing);
        }

        return null;
    }

    /**
     * Registers a {@link RioSystemDeviceDiscoveryService} from the passed {@link RioSystemHandler} and activates it.
     *
     * @param bridgeHandler the {@link RioSystemHandler} for discovery services
     */
    private synchronized void registerThingDiscovery(RioSystemHandler bridgeHandler) {
        RioSystemDeviceDiscoveryService discoveryService = new RioSystemDeviceDiscoveryService(bridgeHandler);
        logger.trace("Try to register Discovery service on BundleID: {} Service: {}",
                bundleContext.getBundle().getBundleId(), DiscoveryService.class.getName());

        bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>());
        discoveryService.activate();
    }

}
