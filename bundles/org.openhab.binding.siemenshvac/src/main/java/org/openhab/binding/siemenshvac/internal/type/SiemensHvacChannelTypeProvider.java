/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.thing.type.ChannelTypeProvider;
import org.openhab.core.thing.type.ChannelTypeUID;

/**
 * Extends the ChannelTypeProvider to manually add a ChannelType.
 *
 * @author Laurent Arnal - Initial contribution
 */
@NonNullByDefault
public interface SiemensHvacChannelTypeProvider extends ChannelTypeProvider {

    /**
     * Adds the ChannelType to this provider.
     */
    public void addChannelType(ChannelType channelType);

    /**
     * Use this method to lookup a ChannelType which was generated by the
     * siemensHvac binding. Other than {@link #getChannelType(ChannelTypeUID)}
     * of this provider, it will return also those {@link ChannelType}s
     * which are excluded by {@link HomematicThingTypeExcluder}
     *
     * @param channelTypeUID
     * @return ChannelType that was added to HomematicChannelTypeProvider, identified
     *         by its config-description-uri<br>
     *         <i>null</i> if no ChannelType with the given UID was added
     *         before
     */
    @Nullable
    public ChannelType getInternalChannelType(@Nullable ChannelTypeUID channelTypeUID);
}
