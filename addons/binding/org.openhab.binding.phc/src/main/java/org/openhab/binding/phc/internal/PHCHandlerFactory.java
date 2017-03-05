/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.phc.internal;

import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.phc.PHCBindingConstants;
import org.openhab.binding.phc.handler.PHCBridgeHandler;
import org.openhab.binding.phc.handler.PHCHandler;

import com.google.common.collect.Sets;

/**
 * The {@link PHCHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Jonas Hohaus - Initial contribution
 */
public class PHCHandlerFactory extends BaseThingHandlerFactory {

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Sets.newHashSet(
            PHCBindingConstants.THING_TYPE_BRIDGE, PHCBindingConstants.THING_TYPE_AM, PHCBindingConstants.THING_TYPE_EM,
            PHCBindingConstants.THING_TYPE_JRM);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    public Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration, ThingUID thingUID,
            ThingUID bridgeUID) {

        Thing thing = null;

        if (SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID)) {

            if (thingTypeUID.equals(PHCBindingConstants.THING_TYPE_BRIDGE)) {
                thing = super.createThing(thingTypeUID, configuration, thingUID, null);

            } else {
                ThingUID phcThingUID = new ThingUID(thingTypeUID,
                        configuration.get(PHCBindingConstants.ADDRESS).toString().toUpperCase());
                thing = super.createThing(thingTypeUID, configuration, phcThingUID, bridgeUID);
            }

        } else {
            throw new IllegalArgumentException(
                    "The thing type " + thingTypeUID + " is not supported by the phc binding.");
        }

        return thing;
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        ThingHandler handler = null;

        if (thingTypeUID.equals(PHCBindingConstants.THING_TYPE_BRIDGE)) {
            handler = new PHCBridgeHandler((Bridge) thing);
        } else if (SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID)) {
            handler = new PHCHandler(thing);
        }

        return handler;
    }
}
