/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.netatmo.internal.handler.security;

import static org.openhab.binding.netatmo.internal.NetatmoBindingConstants.*;
import static org.openhab.binding.netatmo.internal.utils.ChannelTypeUtils.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.netatmo.internal.api.dto.NAThing;
import org.openhab.binding.netatmo.internal.api.home.NAPerson;
import org.openhab.binding.netatmo.internal.api.home.NASnapshot;
import org.openhab.binding.netatmo.internal.channelhelper.AbstractChannelHelper;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.State;

/**
 * The {@link NAPersonChannelHelper} handle specific behavior
 * of modules using batteries
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public class NAPersonChannelHelper extends AbstractChannelHelper {

    public NAPersonChannelHelper(Thing thing, TimeZoneProvider timeZoneProvider) {
        super(thing, timeZoneProvider, GROUP_PERSON);
    }

    @Override
    protected @Nullable State internalGetProperty(NAThing naThing, String channelId) {
        NAPerson naPerson = (NAPerson) naThing;
        if (CHANNEL_PERSON_AT_HOME.equals(channelId)) {
            return OnOffType.from(!naPerson.isOutOfSight());
        } else if (CHANNEL_LAST_SEEN.equals(channelId)) {
            return toDateTimeType(naPerson.getLastSeen(), zoneId);
        }
        NASnapshot avatar = naPerson.getFace();
        return avatar != null ? internalGetAvatar(avatar, channelId) : null;
    }

    private State internalGetAvatar(NASnapshot avatar, String channelId) {
        return CHANNEL_PERSON_AVATAR_URL.equals(channelId) ? toStringType(avatar.getUrl())
                : CHANNEL_PERSON_AVATAR.equals(channelId) ? toRawType(avatar.getUrl()) : null;
    }
}
