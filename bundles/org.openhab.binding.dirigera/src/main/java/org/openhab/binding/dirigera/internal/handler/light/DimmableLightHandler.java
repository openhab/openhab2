/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.dirigera.internal.handler.light;

import static org.openhab.binding.dirigera.internal.Constants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.handler.BaseHandler;
import org.openhab.binding.dirigera.internal.model.Model;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DimmableLightHandler} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class DimmableLightHandler extends BaseHandler {
    private final Logger logger = LoggerFactory.getLogger(DimmableLightHandler.class);

    public DimmableLightHandler(Thing thing, Map<String, String> mapping) {
        super(thing, mapping);
        super.setChildHandler(this);
        // links of types which can be established towards this device
        linkCandidateTypes = List.of(DEVICE_TYPE_LIGHT_CONTROLLER, DEVICE_TYPE_MOTION_SENSOR);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (super.checkHandler()) {
            JSONObject values = gateway().api().readDevice(config.id);
            handleUpdate(values);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("DIRIGERA DIMMABLE_LIGHT handleCommand {} {}", channelUID, command);
        super.handleCommand(channelUID, command);
        String channel = channelUID.getIdWithoutGroup();
        String targetProperty = channel2PropertyMap.get(channel);
        if (targetProperty != null) {
            switch (channel) {
                case CHANNEL_LIGHT_BRIGHTNESS:
                    if (command instanceof PercentType percent) {
                        JSONObject attributes = new JSONObject();
                        attributes.put(targetProperty, percent.intValue());
                        logger.trace("DIRIGERA DIMMABLE_LIGHT send to API {}", attributes);
                        gateway().api().sendAttributes(config.id, attributes);
                    }
                    break;
            }
        }
    }

    @Override
    public void handleUpdate(JSONObject update) {
        // handle reachable flag
        super.handleUpdate(update);
        // now device specific
        if (update.has(Model.ATTRIBUTES)) {
            JSONObject attributes = update.getJSONObject(Model.ATTRIBUTES);
            Iterator<String> attributesIterator = attributes.keys();
            while (attributesIterator.hasNext()) {
                String key = attributesIterator.next();
                String targetChannel = property2ChannelMap.get(key);
                if (targetChannel != null) {
                    switch (targetChannel) {
                        case CHANNEL_LIGHT_BRIGHTNESS:
                            updateState(new ChannelUID(thing.getUID(), targetChannel),
                                    new PercentType(attributes.getInt(key)));
                            break;
                    }
                }
            }
        }
    }
}
