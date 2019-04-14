/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.onewire.internal;

import static org.openhab.binding.onewire.internal.OwBindingConstants.SUPPORTED_THING_TYPES;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.onewire.internal.discovery.OwDiscoveryService;
import org.openhab.binding.onewire.internal.handler.AdvancedMultisensorThingHandler;
import org.openhab.binding.onewire.internal.handler.BAE091xSensorThingHandler;
import org.openhab.binding.onewire.internal.handler.BasicMultisensorThingHandler;
import org.openhab.binding.onewire.internal.handler.BasicThingHandler;
import org.openhab.binding.onewire.internal.handler.EDSSensorThingHandler;
import org.openhab.binding.onewire.internal.handler.OwserverBridgeHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link OwHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.onewire", configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class OwHandlerFactory extends BaseThingHandlerFactory {

    private final Map<ThingUID, @Nullable ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @NonNullByDefault({})
    private OwDynamicStateDescriptionProvider dynamicStateDescriptionProvider;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (OwserverBridgeHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            OwserverBridgeHandler owserverBridgeHandler = new OwserverBridgeHandler((Bridge) thing);
            registerDiscoveryService(owserverBridgeHandler);
            return owserverBridgeHandler;
        } else if (BasicMultisensorThingHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new BasicMultisensorThingHandler(thing, dynamicStateDescriptionProvider);
        } else if (AdvancedMultisensorThingHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new AdvancedMultisensorThingHandler(thing, dynamicStateDescriptionProvider);
        } else if (BasicThingHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new BasicThingHandler(thing, dynamicStateDescriptionProvider);
        } else if (EDSSensorThingHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new EDSSensorThingHandler(thing, dynamicStateDescriptionProvider);
        } else if (BAE091xSensorThingHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            return new BAE091xSensorThingHandler(thing, dynamicStateDescriptionProvider);
        }

        return null;
    }

    private synchronized void registerDiscoveryService(OwserverBridgeHandler owserverBridgeHandler) {
        OwDiscoveryService owDiscoveryService = new OwDiscoveryService(owserverBridgeHandler);

        this.discoveryServiceRegs.put(owserverBridgeHandler.getThing().getUID(), bundleContext.registerService(
                DiscoveryService.class.getName(), owDiscoveryService, new Hashtable<String, Object>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof OwserverBridgeHandler) {
            // remove discovery service, if bridge handler is removed
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                OwDiscoveryService service = (OwDiscoveryService) bundleContext.getService(serviceReg.getReference());
                serviceReg.unregister();
                if (service != null) {
                    service.deactivate();
                }
            }
        }
    }

    @Reference
    protected void setDynamicStateDescriptionProvider(OwDynamicStateDescriptionProvider provider) {
        this.dynamicStateDescriptionProvider = provider;
    }

    protected void unsetDynamicStateDescriptionProvider(OwDynamicStateDescriptionProvider provider) {
        this.dynamicStateDescriptionProvider = null;
    }
}
