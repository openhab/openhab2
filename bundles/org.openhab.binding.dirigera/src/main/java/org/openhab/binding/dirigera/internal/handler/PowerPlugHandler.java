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
package org.openhab.binding.dirigera.internal.handler;

import static org.openhab.binding.dirigera.internal.Constants.*;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.model.Model;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;

/**
 * The {@link PowerPlugHandler} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class PowerPlugHandler extends SimplePlugHandler {
    public PowerPlugHandler(Thing thing, Map<String, String> mapping) {
        super(thing, mapping);
        super.setChildHandler(this);
    }

    @Override
    public void initialize() {
        // handle general initialize like setting bridge
        super.initialize();
        if (super.checkHandler()) {
            JSONObject values = gateway().api().readDevice(config.id);
            handleUpdate(values);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            super.handleCommand(channelUID, command);
        } else {
            String channel = channelUID.getIdWithoutGroup();
            String targetProperty = channel2PropertyMap.get(channel);
            if (targetProperty != null) {
                if (CHANNEL_CHILD_LOCK.equals(channel) || CHANNEL_DISABLE_STATUS_LIGHT.equals(channel)) {
                    if (command instanceof OnOffType onOff) {
                        JSONObject attributes = new JSONObject();
                        attributes.put(targetProperty, onOff.equals(OnOffType.ON));
                        gateway().api().sendPatch(config.id, attributes);
                    }
                }
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
                    if (CHANNEL_CHILD_LOCK.equals(targetChannel)
                            || CHANNEL_DISABLE_STATUS_LIGHT.equals(targetChannel)) {
                        updateState(new ChannelUID(thing.getUID(), targetChannel),
                                OnOffType.from(attributes.getBoolean(key)));
                    }
                }
            }
        }
    }
}
