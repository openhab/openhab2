/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.amazonechocontrol.internal.smarthome;

import static org.openhab.binding.amazonechocontrol.internal.smarthome.Constants.CHANNEL_TYPE_POWER_STATE;
import static org.openhab.binding.amazonechocontrol.internal.smarthome.Constants.ITEM_TYPE_SWITCH;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.StateDescription;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.amazonechocontrol.internal.Connection;
import org.openhab.binding.amazonechocontrol.internal.smarthome.JsonSmartHomeCapabilities.SmartHomeCapability;
import org.openhab.binding.amazonechocontrol.internal.smarthome.JsonSmartHomeDevices.SmartHomeDevice;

import com.google.gson.JsonObject;

/**
 * The {@link HandlerPowerController} is responsible for the Alexa.PowerControllerInterface
 *
 * @author Lukas Knoeller, Michael Geramb
 */
public class HandlerPowerController extends HandlerBase {
    // Interface
    public static final String INTERFACE = "Alexa.PowerController";
    // Channel definitions
    static final String ALEXA_PROPERTY = "powerState";
    static final String CHANNEL_UID = "powerState";
    static final ChannelTypeUID CHANNEL_TYPE = CHANNEL_TYPE_POWER_STATE;
    static final String ITEM_TYPE = ITEM_TYPE_SWITCH;
    // List of all actions
    static final String ACTION_TURN_ON = "turnOn";
    static final String ACTION_TURN_OFF = "turnOff";

    @Override
    protected String[] GetSupportedInterface() {
        return new String[] { INTERFACE };
    }

    @Override
    protected @Nullable ChannelInfo[] FindChannelInfos(SmartHomeCapability capability, String property) {
        if (ALEXA_PROPERTY.equals(property)) {
            return new ChannelInfo[] { new ChannelInfo(ALEXA_PROPERTY, CHANNEL_UID, CHANNEL_TYPE, ITEM_TYPE) };
        }
        return null;
    }

    @Override
    protected void updateChannels(String interfaceName, List<JsonObject> stateList) {
        Boolean powerState = null;
        for (JsonObject state : stateList) {
            if (ALEXA_PROPERTY.equals(state.get("name").getAsString())) {
                String value = state.get("value").getAsString();
                // For groups take true if all true
                if ("ON".equals(value)) {
                    powerState = true;
                } else if (powerState == null) {
                    powerState = false;
                }

            }
        }
        updateState(CHANNEL_UID, powerState == null ? UnDefType.UNDEF : (powerState ? OnOffType.ON : OnOffType.OFF));
    }

    @Override
    protected boolean handleCommand(Connection connection, SmartHomeDevice shd, String entityId,
            SmartHomeCapability[] capabilties, String channelId, Command command) throws IOException {
        if (channelId.equals(CHANNEL_UID)) {

            if (ContainsCapabilityProperty(capabilties, ALEXA_PROPERTY)) {
                if (command.equals(OnOffType.ON)) {
                    connection.smartHomeCommand(entityId, ACTION_TURN_ON);
                    return true;
                } else if (command.equals(OnOffType.OFF)) {
                    connection.smartHomeCommand(entityId, ACTION_TURN_OFF);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable StateDescription findStateDescription(Connection connection, String channelId,
            StateDescription originalStateDescription, @Nullable Locale locale) {
        return null;
    }
}
