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
package org.openhab.binding.opensprinkler.internal;

import static org.openhab.binding.opensprinkler.internal.OpenSprinklerBindingConstants.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.opensprinkler.internal.handler.OpenSprinklerDeviceHandler;
import org.openhab.binding.opensprinkler.internal.handler.OpenSprinklerHttpBridgeHandler;
import org.openhab.binding.opensprinkler.internal.handler.OpenSprinklerPiBridgeHandler;
import org.openhab.binding.opensprinkler.internal.handler.OpenSprinklerStationHandler;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link OpenSprinklerHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Chris Graham - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.opensprinkler")
public class OpenSprinklerHandlerFactory extends BaseThingHandlerFactory {
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = new HashSet<>(Arrays
            .asList(OPENSPRINKLER_HTTP_BRIDGE, OPENSPRINKLER_PI_BRIDGE, OPENSPRINKLER_STATION, OPENSPRINKLER_DEVICE));

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(OPENSPRINKLER_HTTP_BRIDGE)) {
            return new OpenSprinklerHttpBridgeHandler((Bridge) thing);
        } else if (thingTypeUID.equals(OPENSPRINKLER_STATION)) {
            return new OpenSprinklerStationHandler(thing);
        } else if (thingTypeUID.equals(OPENSPRINKLER_PI_BRIDGE)) {
            return new OpenSprinklerPiBridgeHandler((Bridge) thing);
        } else if (thingTypeUID.equals(OPENSPRINKLER_DEVICE)) {
            return new OpenSprinklerDeviceHandler(thing);
        }

        return null;
    }
}
