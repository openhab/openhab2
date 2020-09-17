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
package org.openhab.binding.velux.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.velux.internal.VeluxBindingConstants;
import org.openhab.binding.velux.internal.VeluxItemType;
import org.openhab.binding.velux.internal.bridge.VeluxBridgeWLANConfig;
import org.openhab.binding.velux.internal.handler.utils.StateUtils;
import org.openhab.binding.velux.internal.handler.utils.ThingProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Channel-specific retrieval and modification.</B>
 * <P>
 * This class implements the Channels <B>WLANSSID</B> and <B>WLANPassword</B> of the Thing <B>klf200</B> :
 * <UL>
 * <LI><I>Velux</I> <B>bridge</B> &rarr; <B>OpenHAB</B>:
 * <P>
 * Information retrieval by method {@link #handleRefresh}.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
@NonNullByDefault
final class ChannelBridgeWLANconfig extends ChannelHandlerTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelBridgeWLANconfig.class);

    /*
     * ************************
     * ***** Constructors *****
     */

    // Suppress default constructor for non-instantiability

    private ChannelBridgeWLANconfig() {
        throw new AssertionError();
    }

    /**
     * Communication method to retrieve information to update the channel value.
     *
     * @param channelUID The item passed as type {@link ChannelUID} for which a refresh is intended.
     * @param channelId The same item passed as type {@link String} for which a refresh is intended.
     * @param thisBridgeHandler The Velux bridge handler with a specific communication protocol which provides
     *            information for this channel.
     * @return newState The value retrieved for the passed channel, or <I>null</I> in case if there is no (new) value.
     */
    static @Nullable State handleRefresh(ChannelUID channelUID, String channelId,
            VeluxBridgeHandler thisBridgeHandler) {
        LOGGER.debug("handleRefresh({},{},{}) called.", channelUID, channelId, thisBridgeHandler);
        State newState = null;
        thisBridgeHandler.bridgeParameters.wlanConfig = new VeluxBridgeWLANConfig()
                .retrieve(thisBridgeHandler.thisBridge);
        if (thisBridgeHandler.bridgeParameters.wlanConfig.isRetrieved) {
            VeluxItemType itemType = VeluxItemType.getByThingAndChannel(thisBridgeHandler.thingTypeUIDOf(channelUID),
                    channelUID.getId());
            final String msg = "Not supported";
            switch (itemType) {
                case BRIDGE_WLANSSID:
                    newState = StateUtils.createState(new StringType(msg));
                    ThingProperty.setValue(thisBridgeHandler, VeluxBindingConstants.PROPERTY_BRIDGE_WLANSSID, msg);
                    break;
                case BRIDGE_WLANPASSWORD:
                    newState = StateUtils.createState(new StringType(msg));
                    ThingProperty.setValue(thisBridgeHandler, VeluxBindingConstants.PROPERTY_BRIDGE_WLANPASSWORD, msg);
                    break;
                default:
            }
        }
        LOGGER.trace("handleRefresh() returns {}.", newState);
        return newState;
    }
}
