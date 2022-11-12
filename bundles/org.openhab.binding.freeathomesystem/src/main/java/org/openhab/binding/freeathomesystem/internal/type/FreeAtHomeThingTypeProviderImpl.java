/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.freeathomesystem.internal.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.ThingTypeProvider;
import org.openhab.core.thing.type.ThingType;
import org.osgi.service.component.annotations.Component;

/**
 * @author andras
 *
 */

@Component(service = { FreeAtHomeThingTypeProvider.class, ThingTypeProvider.class })

public class FreeAtHomeThingTypeProviderImpl implements FreeAtHomeThingTypeProvider {
    private final Map<ThingTypeUID, ThingType> thingTypesByUID = new HashMap<>();

    @Override
    public @NonNull Collection<@NonNull ThingType> getThingTypes(@Nullable Locale locale) {
        Map<ThingTypeUID, ThingType> copy = new HashMap<>(thingTypesByUID);
        return copy.values();
    }

    @Override
    public @Nullable ThingType getThingType(ThingTypeUID thingTypeUID, @Nullable Locale locale) {
        return thingTypesByUID.get(thingTypeUID);
    }

    @Override
    public void addThingType(ThingType thingType) {
        thingTypesByUID.put(thingType.getUID(), thingType);
    }
}
