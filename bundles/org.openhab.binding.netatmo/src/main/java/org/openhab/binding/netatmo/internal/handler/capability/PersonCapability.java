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
package org.openhab.binding.netatmo.internal.handler.capability;

import static org.openhab.binding.netatmo.internal.NetatmoBindingConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.netatmo.internal.api.data.EventType;
import org.openhab.binding.netatmo.internal.api.data.ModuleType;
import org.openhab.binding.netatmo.internal.api.dto.NAEvent;
import org.openhab.binding.netatmo.internal.api.dto.NAHomeDataModule;
import org.openhab.binding.netatmo.internal.api.dto.NAHomeEvent;
import org.openhab.binding.netatmo.internal.api.dto.NAObject;
import org.openhab.binding.netatmo.internal.handler.NACommonInterface;
import org.openhab.binding.netatmo.internal.providers.NetatmoDescriptionProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.StateOption;

/**
 * {@link PersonCapability} give the ability to read weather station api
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public class PersonCapability extends Capability {
    private final NetatmoDescriptionProvider descriptionProvider;
    private final ChannelUID cameraChannelUID;

    public PersonCapability(NACommonInterface handler, NetatmoDescriptionProvider descriptionProvider) {
        super(handler);
        this.descriptionProvider = descriptionProvider;
        this.cameraChannelUID = new ChannelUID(thing.getUID(), GROUP_PERSON_EVENT, CHANNEL_EVENT_CAMERA_ID);
    }

    @Override
    protected void beforeNewData() {
        super.beforeNewData();
        handler.getHomeCapability(HomeCapability.class).ifPresent(cap -> {
            Stream<NAHomeDataModule> cameras = cap.getModules().values().stream()
                    .filter(module -> module.getType() == ModuleType.NACamera);
            descriptionProvider.setStateOptions(cameraChannelUID,
                    cameras.map(p -> new StateOption(p.getId(), p.getName())).collect(Collectors.toList()));
        });
    }

    @Override
    public void handleCommand(String channelName, Command command) {
        if ((command instanceof OnOffType) && CHANNEL_PERSON_AT_HOME.equals(channelName)) {
            handler.getHomeCapability(SecurityCapability.class)
                    .ifPresent(cap -> cap.setPersonAway(handlerId, OnOffType.OFF.equals(command)));
        }
    }

    @Override
    public void updateEvent(NAEvent newData) {
        super.updateEvent(newData);
        EventType eventType = newData.getEventType();
        if (eventType.appliesOn(ModuleType.NAPerson)) {
            handler.triggerChannel(CHANNEL_HOME_EVENT, eventType.name());
        }
    }

    @Override
    public List<NAObject> updateReadings() {
        List<NAObject> result = new ArrayList<>();
        handler.getHomeCapability(SecurityCapability.class).ifPresent(cap -> {
            Collection<NAHomeEvent> events = cap.getPersonEvents(handlerId);
            if (!events.isEmpty()) {
                result.add(events.iterator().next());
            }
        });
        return result;
    }
}
