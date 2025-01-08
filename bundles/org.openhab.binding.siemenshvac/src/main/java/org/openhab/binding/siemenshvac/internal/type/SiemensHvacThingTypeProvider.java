/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.siemenshvac.internal.type;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.ThingTypeProvider;
import org.openhab.core.thing.type.ThingType;

/**
 * Extends the ThingTypeProvider to manually add a ThingType.
 *
 * @author Laurent Arnal - Initial contribution
 */
@NonNullByDefault
public interface SiemensHvacThingTypeProvider extends ThingTypeProvider {

    /**
     * Adds the ThingType to this provider.
     */
    void addThingType(ThingType thingType);

    /**
     * Use this method to lookup a ThingType which was generated by the
     * binding. Other than {@link #getThingType(ThingTypeUID)}
     * of this provider, it will return also those {@link ThingType}s which are
     * excluded by {@link ThingTypeExcluder}
     *
     * @param thingTypeUID
     * @return ThingType that was added to SiemensHvacThingTypeProvider, identified
     *         by its thingTypeUID<br>
     *         <i>null</i> if no ThingType with the given thingTypeUID was added
     *         before
     */
    @Nullable
    ThingType getInternalThingType(ThingTypeUID thingTypeUID);

    void invalidate();
}
