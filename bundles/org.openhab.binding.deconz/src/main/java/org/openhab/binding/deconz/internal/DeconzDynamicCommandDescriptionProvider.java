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
package org.openhab.binding.deconz.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseDynamicCommandDescriptionProvider;
import org.openhab.core.thing.type.DynamicCommandDescriptionProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic channel command description provider.
 * Overrides the command description for the controls, which receive its configuration in the runtime.
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
@Component(service = { DynamicCommandDescriptionProvider.class, DeconzDynamicCommandDescriptionProvider.class })
public class DeconzDynamicCommandDescriptionProvider extends BaseDynamicCommandDescriptionProvider {
    private final Logger logger = LoggerFactory.getLogger(DeconzDynamicCommandDescriptionProvider.class);

    /**
     * remove all descriptions for a given thing
     *
     * @param thingUID the thing's UID
     */
    public void removeDescriptionsForThing(ThingUID thingUID) {
        logger.trace("removing state description for thing {}", thingUID);
        channelOptionsMap.entrySet().removeIf(entry -> entry.getKey().getThingUID().equals(thingUID));
    }
}
