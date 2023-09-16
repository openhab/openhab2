/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

package org.openhab.binding.mqtt.awtrixlight.internal;

import static org.openhab.binding.mqtt.awtrixlight.internal.AwtrixLightBindingConstants.SUPPORTED_THING_TYPES;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.awtrixlight.internal.handler.AwtrixLightAppHandler;
import org.openhab.binding.mqtt.awtrixlight.internal.handler.AwtrixLightBridgeHandler;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link AwtrixLightHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Thomas Lauterbach - Initial contribution
 */
@Component(service = ThingHandlerFactory.class)
@NonNullByDefault
public class AwtrixLightHandlerFactory extends BaseThingHandlerFactory {
    private final ThingRegistry thingRegistry;

    @Activate
    public AwtrixLightHandlerFactory(final @Reference ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (AwtrixLightBridgeHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            AwtrixLightBridgeHandler bridgeHandler = new AwtrixLightBridgeHandler((Bridge) thing);
            return bridgeHandler;
        } else if (AwtrixLightAppHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            AwtrixLightAppHandler appHandler = new AwtrixLightAppHandler((Thing) thing);
            return appHandler;
        }

        return null;
    }
}
