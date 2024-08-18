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
package org.openhab.binding.mqtt.generic.internal.handler;

import static org.openhab.binding.mqtt.generic.internal.MqttBindingConstants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.mqtt.generic.internal.MqttBindingConstants;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;

/**
 * Static test definitions, like thing, bridge and channel definitions
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class ThingChannelConstants {
    // Common ThingUID and ChannelUIDs
    public static final ThingUID TEST_GENERIC_THING = new ThingUID(GENERIC_MQTT_THING, "genericthing");

    public static final ChannelTypeUID TEXT_CHANNEL = new ChannelTypeUID(BINDING_ID, MqttBindingConstants.STRING);
    public static final ChannelTypeUID TEXT_WITH_JSON_CHANNEL = new ChannelTypeUID(BINDING_ID,
            MqttBindingConstants.STRING);
    public static final ChannelTypeUID ON_OFF_CHANNEL = new ChannelTypeUID(BINDING_ID, MqttBindingConstants.SWITCH);
    public static final ChannelTypeUID NUMBER_CHANNEL = new ChannelTypeUID(BINDING_ID, MqttBindingConstants.NUMBER);
    public static final ChannelTypeUID PERCENTAGE_CHANNEL = new ChannelTypeUID(BINDING_ID, MqttBindingConstants.DIMMER);
    public static final ChannelTypeUID UNKNOWN_CHANNEL = new ChannelTypeUID(BINDING_ID, "unknown");

    public static final ChannelUID TEXT_CHANNEL_UID = new ChannelUID(TEST_GENERIC_THING, "mytext");

    public static final String JSON_PATH_JSON = "{ \"device\": { \"status\": { \"temperature\": 23.2 }}}";
    public static final String JSON_PATH_PATTERN = "$.device.status.temperature";
    public static final String TRANSFORMATION_SERVICE1 = "REGEX";
    public static final String TRANSFORMATION_PATTERN1 = ": ([.0-9]+)";
    public static final String TRANSFORMATION_SERVICE2 = "XPATH";
    public static final String TRANSFORMATION_PATTERN2 = "/device/status/temperature/text()";

    public static final List<Channel> THING_CHANNEL_LIST = new ArrayList<>();
    public static final List<Channel> THING_CHANNEL_LIST_WITH_JSON_COLON = new ArrayList<>();
    public static final List<Channel> THING_CHANNEL_LIST_WITH_REGEX_PARENTHESES = new ArrayList<>();

    /**
     * Create a channel with exact the parameters we need for the tests
     *
     * @param id Channel ID
     * @param acceptedType Accept type
     * @param config The configuration
     * @param channelTypeUID ChannelTypeUID provided by the static definitions
     * @return
     */
    public static Channel cb(String id, String acceptedType, Configuration config, ChannelTypeUID channelTypeUID) {
        return ChannelBuilder.create(new ChannelUID(TEST_GENERIC_THING, id), acceptedType).withConfiguration(config)
                .withType(channelTypeUID).build();
    }

    static {
        THING_CHANNEL_LIST.add(cb("mytext", "String", textConfiguration(), TEXT_CHANNEL));
        THING_CHANNEL_LIST.add(cb("onoff", "Switch", onoffConfiguration(), ON_OFF_CHANNEL));
        THING_CHANNEL_LIST.add(cb("num", "Number", numberConfiguration(), NUMBER_CHANNEL));
        THING_CHANNEL_LIST.add(cb("percent", "Number:Dimensionless", percentageConfiguration(), PERCENTAGE_CHANNEL));

        THING_CHANNEL_LIST_WITH_JSON_COLON
                .add(cb("mytext", "String", textConfigurationWithJson(false), TEXT_WITH_JSON_CHANNEL));
        THING_CHANNEL_LIST_WITH_JSON_COLON.add(cb("onoff", "Switch", onoffConfiguration(), ON_OFF_CHANNEL));
        THING_CHANNEL_LIST_WITH_JSON_COLON.add(cb("num", "Number", numberConfiguration(), NUMBER_CHANNEL));
        THING_CHANNEL_LIST_WITH_JSON_COLON
                .add(cb("percent", "Number:Dimensionless", percentageConfiguration(), PERCENTAGE_CHANNEL));

        THING_CHANNEL_LIST_WITH_REGEX_PARENTHESES
                .add(cb("mytext", "String", textConfigurationWithJson(true), TEXT_WITH_JSON_CHANNEL));
        THING_CHANNEL_LIST_WITH_REGEX_PARENTHESES.add(cb("onoff", "Switch", onoffConfiguration(), ON_OFF_CHANNEL));
        THING_CHANNEL_LIST_WITH_REGEX_PARENTHESES.add(cb("num", "Number", numberConfiguration(), NUMBER_CHANNEL));
        THING_CHANNEL_LIST_WITH_REGEX_PARENTHESES
                .add(cb("percent", "Number:Dimensionless", percentageConfiguration(), PERCENTAGE_CHANNEL));
    }

    static Configuration textConfiguration() {
        Map<String, Object> data = new HashMap<>();
        data.put("stateTopic", "test/state");
        data.put("commandTopic", "test/command");
        return new Configuration(data);
    }

    static Configuration textConfigurationWithJson(boolean parentheses) {
        Map<String, Object> data = new HashMap<>();
        data.put("stateTopic", "test/state");
        data.put("commandTopic", "test/command");
        if (parentheses) {
            data.put("transformationPattern", TRANSFORMATION_SERVICE1 + "(" + TRANSFORMATION_PATTERN1 + ") ∩ "
                    + TRANSFORMATION_SERVICE2 + "(" + TRANSFORMATION_PATTERN2 + ")");
        } else {
            data.put("transformationPattern", "JSONPATH:" + JSON_PATH_PATTERN);
        }
        return new Configuration(data);
    }

    private static Configuration numberConfiguration() {
        Map<String, Object> data = new HashMap<>();
        data.put("stateTopic", "test/state");
        data.put("commandTopic", "test/command");
        data.put("min", BigDecimal.valueOf(1));
        data.put("max", BigDecimal.valueOf(99));
        data.put("step", BigDecimal.valueOf(2));
        data.put("isDecimal", true);
        return new Configuration(data);
    }

    private static Configuration percentageConfiguration() {
        Map<String, Object> data = new HashMap<>();
        data.put("stateTopic", "test/state");
        data.put("commandTopic", "test/command");
        data.put("on", "ON");
        data.put("off", "OFF");
        return new Configuration(data);
    }

    private static Configuration onoffConfiguration() {
        Map<String, Object> data = new HashMap<>();
        data.put("stateTopic", "test/state");
        data.put("commandTopic", "test/command");
        data.put("on", "ON");
        data.put("off", "OFF");
        data.put("inverse", true);
        return new Configuration(data);
    }
}
