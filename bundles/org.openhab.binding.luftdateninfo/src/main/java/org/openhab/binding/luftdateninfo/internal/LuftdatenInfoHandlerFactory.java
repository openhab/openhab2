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
package org.openhab.binding.luftdateninfo.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.net.http.HttpClientFactory;
import org.openhab.binding.luftdateninfo.internal.handler.ConditionsHandler;
import org.openhab.binding.luftdateninfo.internal.handler.HTTPHandler;
import org.openhab.binding.luftdateninfo.internal.handler.NoiseHandler;
import org.openhab.binding.luftdateninfo.internal.handler.PMHandler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link LuftdatenInfoHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.luftdateninfo", service = ThingHandlerFactory.class)
public class LuftdatenInfoHandlerFactory extends BaseThingHandlerFactory {

    @Activate
    public LuftdatenInfoHandlerFactory(final @Reference HttpClientFactory httpClientFactory) {
        HTTPHandler.init(httpClientFactory.getCommonHttpClient());
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        System.out.println(thingTypeUID);
        if (thingTypeUID.equals(LuftdatenInfoBindingConstants.THING_TYPE_PARTICULATE)
                || thingTypeUID.equals(LuftdatenInfoBindingConstants.THING_TYPE_CONDITIONS)
                || thingTypeUID.equals(LuftdatenInfoBindingConstants.THING_TYPE_NOISE)) {
            System.out.println("found");
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        // one handler for all
        if (thing.getThingTypeUID().equals(LuftdatenInfoBindingConstants.THING_TYPE_PARTICULATE)) {
            return new PMHandler(thing);
        } else if (thing.getThingTypeUID().equals(LuftdatenInfoBindingConstants.THING_TYPE_CONDITIONS)) {
            return new ConditionsHandler(thing);
        } else if (thing.getThingTypeUID().equals(LuftdatenInfoBindingConstants.THING_TYPE_NOISE)) {
            return new NoiseHandler(thing);
        }
        return null;
    }
}
