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
package org.openhab.binding.netatmo.internal.handler.channelhelper;

import static org.openhab.binding.netatmo.internal.NetatmoBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.netatmo.internal.api.data.EventType;
import org.openhab.binding.netatmo.internal.api.dto.Event;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;

/**
 * The {@link EventPersonChannelHelper} handles specific channels of person events
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public class EventPersonChannelHelper extends EventChannelHelper {
    public static final Set<EventType> AT_HOME_MARKERS = Set.of(EventType.PERSON, EventType.PERSON_HOME);

    public EventPersonChannelHelper() {
        super(GROUP_PERSON_LAST_EVENT);
    }

    @Override
    protected @Nullable State internalGetEvent(String channelId, Event event) {
        EventType eventType = event.getEventType();
        return eventType.validFor(moduleType) && CHANNEL_PERSON_AT_HOME.equals(channelId)
                ? OnOffType.from(AT_HOME_MARKERS.contains(eventType))
                : super.internalGetEvent(channelId, event);
    }
}
