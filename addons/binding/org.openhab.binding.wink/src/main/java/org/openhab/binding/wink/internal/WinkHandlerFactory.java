/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
 *
>>>>>>> Project skeleton.
=======
 *
>>>>>>> Added Chamberlain MyQ skeleton
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wink.internal;

import static org.openhab.binding.wink.WinkBindingConstants.*;

import java.util.Set;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.wink.handler.LightBulbHandler;
import org.openhab.binding.wink.handler.RemoteHandler;
import org.openhab.binding.wink.handler.WinkHub2Handler;
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 8b97becdef5db102e58dcf125a75329676144f43
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
import org.openhab.binding.wink.handler.BinarySwitchHandler;
=======
>>>>>>> Project skeleton.
=======
import org.openhab.binding.wink.handler.BinarySwitchHandler;
>>>>>>> added binary switch type
=======
import org.openhab.binding.wink.handler.BinarySwitchHandler;
>>>>>>> Added Chamberlain MyQ skeleton
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link WinkHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Sebastien Marchand - Initial contribution
 */
public class WinkHandlerFactory extends BaseThingHandlerFactory {

    private Logger logger = LoggerFactory.getLogger(WinkHandlerFactory.class);

    public final static Set<ThingTypeUID> DISCOVERABLE_DEVICE_TYPES_UIDS = ImmutableSet.of(THING_TYPE_LIGHT_BULB,
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 8b97becdef5db102e58dcf125a75329676144f43
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
            THING_TYPE_REMOTE, THING_TYPE_BINARY_SWITCH);
=======
            THING_TYPE_REMOTE);
>>>>>>> Project skeleton.
=======
            THING_TYPE_REMOTE, THING_TYPE_BINARY_SWITCH);
>>>>>>> added binary switch type
=======
            THING_TYPE_REMOTE, THING_TYPE_BINARY_SWITCH);
>>>>>>> Added Chamberlain MyQ skeleton

    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = ImmutableSet.of(THING_TYPE_WINK_HUB_2);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        logger.info("Checking if the factory supports {}", thingTypeUID.toString());
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID)
                || DISCOVERABLE_DEVICE_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_WINK_HUB_2)) {
            return new WinkHub2Handler((Bridge) thing);
        } else if (thingTypeUID.equals(THING_TYPE_LIGHT_BULB)) {
            return new LightBulbHandler(thing);
        } else if (thingTypeUID.equals(THING_TYPE_REMOTE)) {
            return new RemoteHandler(thing);
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 8b97becdef5db102e58dcf125a75329676144f43
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
        } else if (thingTypeUID.equals(THING_TYPE_BINARY_SWITCH)) {
            return new BinarySwitchHandler(thing);
=======
>>>>>>> Project skeleton.
=======
        } else if (thingTypeUID.equals(THING_TYPE_BINARY_SWITCH)) {
            return new BinarySwitchHandler(thing);
>>>>>>> added binary switch type
=======
        } else if (thingTypeUID.equals(THING_TYPE_BINARY_SWITCH)) {
            return new BinarySwitchHandler(thing);
>>>>>>> Added Chamberlain MyQ skeleton
        }

        return null;
    }
}
