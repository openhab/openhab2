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
package org.openhab.binding.somfytahoma.internal.handler;

import static org.openhab.binding.somfytahoma.internal.SomfyTahomaBindingConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.somfytahoma.internal.model.SomfyTahomaState;

/**
 * The {@link SomfyTahomaMyfoxCameraHandler} is responsible for handling commands,
 * which are sent to one of the channels of the Myfox camera.
 *
 * @author Ondrej Pecta - Initial contribution
 */
@NonNullByDefault
public class SomfyTahomaMyfoxCameraHandler extends SomfyTahomaCameraHandler {

    public SomfyTahomaMyfoxCameraHandler(Thing thing) {
        super(thing);
        stateNames.put(SHUTTER, MYFOX_SHUTTER_STATUS_STATE);
    }

    @Override
    public void updateThingChannels(List<SomfyTahomaState> states) {
        Map<String, String> properties = new HashMap<>();
        for (SomfyTahomaState state : states) {
            getLogger().trace("processing state: {} with value: {}", state.getName(), state.getValue());
            properties.put(state.getName(), state.getValue().toString());
            if (MYFOX_SHUTTER_STATUS_STATE.equals(state.getName())) {
                Channel ch = thing.getChannel(SHUTTER);
                if (ch != null) {
                    //we need to covert opened/closed values to ON/OFF
                    boolean open = "opened".equals(state.getValue());
                    updateState(ch.getUID(), open ? OnOffType.ON : OnOffType.OFF);
                }
            }
        }
        updateProperties(properties);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        super.handleCommand(channelUID, command);
        if (!SHUTTER.equals(channelUID.getId())) {
            return;
        }

        if (RefreshType.REFRESH.equals(command)) {
            return;
        } else {
            if (command instanceof OnOffType) {
                sendCommand(command.equals(OnOffType.ON) ? "open" : "close");
            }
        }
    }
}
