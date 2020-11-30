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
package org.openhab.binding.webthing.internal.link;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.webthing.internal.ChannelHandler;
import org.openhab.binding.webthing.internal.client.ConsumedThing;
import org.openhab.binding.webthing.internal.client.PropertyChangedListener;
import org.openhab.binding.webthing.internal.client.dto.Property;
import org.openhab.core.thing.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PropertyToChannelLink} represents a downstream link from a WebThing property to a Channel.
 *
 * @author Gregor Roth - Initial contribution
 */
@NonNullByDefault
public class PropertyToChannelLink implements PropertyChangedListener {
    private final Logger logger = LoggerFactory.getLogger(PropertyToChannelLink.class);
    private final ChannelHandler channelHandler;
    private final ConsumedThing webThing;
    private final Property property;
    private final Channel channel;
    private final TypeConverter typeConverter;


    public static void establish(ConsumedThing webThing, String propertyName, ChannelHandler thingHandler,
            Channel channel) throws IOException {
        new PropertyToChannelLink(webThing, propertyName, thingHandler, channel);
    }

    /**
     * establish downstream link from a WebTHing property to a Channel
     *
     * @param webThing the WebThing to be linked
     * @param propertyName the property name
     * @param channelHandler the channel handler that provides updating the Item state of a channel
     * @param channel the channel to be linked
     * @throws IOException if the WebThing can not be connected
     */
    private PropertyToChannelLink(ConsumedThing webThing, String propertyName, ChannelHandler channelHandler,
            Channel channel) throws IOException {
        this.webThing = webThing;
        this.channel = channel;
        var itemType = Optional.ofNullable(channel.getAcceptedItemType()).orElse("String");
        this.property = webThing.getThingDescription().properties.get(propertyName);
        this.typeConverter = TypeConverters.create(itemType, property.type);
        this.channelHandler = channelHandler;
        webThing.observeProperty(propertyName, this);
    }

    @Override
    public void onPropertyValueChanged(ConsumedThing webthing, String propertyName, Object value) {
        try {
            var stateCommand = typeConverter.toStateCommand(value);
            channelHandler.updateItemState(channel.getUID(), stateCommand);
            logger.debug("channel {} updated with {} ({})", channel.getUID().getAsString(), value,
                    channel.getAcceptedItemType());
        } catch (Exception e) {
            logger.warn("updating channel {} with {} ({}) failed", channel.getUID().getAsString(),
                    channel.getAcceptedItemType(), value, e);
        }
    }
}
