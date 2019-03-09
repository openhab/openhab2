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
package org.openhab.binding.mqtt.generic.internal.convention.homeassistant;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mqtt.generic.internal.values.TextValue;

/**
 * A MQTT alarm control panel, following the https://www.home-assistant.io/components/alarm_control_panel.mqtt/
 * specification.
 *
 * The implemented provides three state-less switches (For disarming, arming@home, arming@away) and one alarm state
 * text.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class ComponentAlarmControlPanel extends AbstractComponent<ComponentAlarmControlPanel.Config> {
    public static final String stateChannelID = "alarm"; // Randomly chosen channel "ID"
    public static final String switchDisarmChannelID = "disarm"; // Randomly chosen channel "ID"
    public static final String switchArmHomeChannelID = "armhome"; // Randomly chosen channel "ID"
    public static final String switchArmAwayChannelID = "armaway"; // Randomly chosen channel "ID"

    /**
     * Configuration class for MQTT component
     */
    static class Config extends HAConfiguration {
        Config() {
            super("MQTT Alarm");
        }

        protected @Nullable String code;

        protected String state_topic = "";
        protected String state_disarmed = "disarmed";
        protected String state_armed_home = "armed_home";
        protected String state_armed_away = "armed_away";
        protected String state_pending = "pending";
        protected String state_triggered = "triggered";

        protected @Nullable String command_topic;
        protected String payload_disarm = "DISARM";
        protected String payload_arm_home = "ARM_HOME";
        protected String payload_arm_away = "ARM_AWAY";
    };

    public ComponentAlarmControlPanel(CFactory.ComponentConfiguration componentConfiguration) {
        super(componentConfiguration, Config.class);

        final String[] state_enum = { config.state_disarmed, config.state_armed_home, config.state_armed_away,
                config.state_pending, config.state_triggered };
        channels.put(stateChannelID, new CChannel(this, stateChannelID, new TextValue(state_enum), config.state_topic,
                null, config.name, "", componentConfiguration.getUpdateListener()));

        channels.put(switchDisarmChannelID,
                new CChannel(this, switchDisarmChannelID, new TextValue(new String[] { config.payload_disarm }),
                        config.state_topic, null, config.name, "", componentConfiguration.getUpdateListener()));

        channels.put(switchArmHomeChannelID,
                new CChannel(this, switchArmHomeChannelID, new TextValue(new String[] { config.payload_arm_home }),
                        config.state_topic, null, config.name, "", componentConfiguration.getUpdateListener()));

        channels.put(switchArmAwayChannelID,
                new CChannel(this, switchArmAwayChannelID, new TextValue(new String[] { config.payload_arm_away }),
                        config.state_topic, null, config.name, "", componentConfiguration.getUpdateListener()));
    }
}
