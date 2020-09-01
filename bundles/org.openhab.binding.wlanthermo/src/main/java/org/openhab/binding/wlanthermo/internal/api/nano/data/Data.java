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

package org.openhab.binding.wlanthermo.internal.api.nano.data;

import static org.openhab.binding.wlanthermo.internal.WlanThermoBindingConstants.*;

import java.awt.Color;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.wlanthermo.internal.WlanThermoNanoHandler;
import org.openhab.binding.wlanthermo.internal.api.nano.UtilNano;
import org.openhab.binding.wlanthermo.internal.api.nano.settings.Settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This DTO is used to parse the JSON
 *
 * @author Christian Schlipp - Initial contribution
 */
public class Data {

    @SerializedName("system")
    @Expose
    private System system;
    @SerializedName("channel")
    @Expose
    private List<Channel> channel = new ArrayList<>();
    @SerializedName("pitmaster")
    @Expose
    private Pitmaster pitmaster;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    public Data(System system, List<Channel> channel, Pitmaster pitmaster) {
        super();
        this.system = system;
        this.channel = channel;
        this.pitmaster = pitmaster;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public Data withSystem(System system) {
        this.system = system;
        return this;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

    public Data withChannel(List<Channel> channel) {
        this.channel = channel;
        return this;
    }

    public Pitmaster getPitmaster() {
        return pitmaster;
    }

    public void setPitmaster(Pitmaster pitmaster) {
        this.pitmaster = pitmaster;
    }

    public Data withPitmaster(Pitmaster pitmaster) {
        this.pitmaster = pitmaster;
        return this;
    }

    public State getState(ChannelUID channelUID, WlanThermoNanoHandler wlanThermoHandler) {
        State state = null;
        if (channelUID.getId().startsWith("system#") && system != null) {
            switch (channelUID.getIdWithoutGroup()) {
                case SYSTEM_SOC:
                    state = new DecimalType(system.getSoc());
                    break;
                case SYSTEM_CHARGE:
                    state = OnOffType.from(system.getCharge());
                    break;
                case SYSTEM_RSSI_SIGNALSTRENGTH:
                    int dbm = system.getRssi();
                    if (dbm >= -80) {
                        state = new DecimalType(4);
                    } else if (dbm >= -95) {
                        state = new DecimalType(3);
                    } else if (dbm >= -105) {
                        state = new DecimalType(2);
                    } else {
                        state = new DecimalType(1);
                    }
                    break;
                case SYSTEM_RSSI:
                    state = new DecimalType(system.getRssi());
                    break;
            }
        } else if (channelUID.getId().startsWith("channel")) {
            int channelId = Integer.parseInt(channelUID.getGroupId().substring("channel".length())) - 1;
            if (channel.size() > 0 && channelId <= channel.size()) {
                switch (channelUID.getIdWithoutGroup()) {
                    case CHANNEL_NAME:
                        state = new StringType(channel.get(channelId).getName());
                        break;
                    case CHANNEL_TYP:
                        Settings settings = wlanThermoHandler.getSettings();
                        state = new StringType(settings.sensors.get(channel.get(channelId).getTyp()));
                        break;
                    case CHANNEL_TEMP:
                        if (channel.get(channelId).getTemp() == 999.0) {
                            state = UnDefType.UNDEF;
                        } else {
                            state = new DecimalType(channel.get(channelId).getTemp());
                        }
                        break;
                    case CHANNEL_MIN:
                        state = new DecimalType(channel.get(channelId).getMin());
                        break;
                    case CHANNEL_MAX:
                        state = new DecimalType(channel.get(channelId).getMax());
                        break;
                    case CHANNEL_ALARM_DEVICE:
                        state = OnOffType.from(BigInteger.valueOf(channel.get(channelId).getAlarm()).testBit(1));
                        break;
                    case CHANNEL_ALARM_PUSH:
                        state = OnOffType.from(BigInteger.valueOf(channel.get(channelId).getAlarm()).testBit(0));
                        break;
                    case CHANNEL_ALARM_OPENHAB_HIGH:
                        if (channel.get(channelId).getTemp() != 999
                                && channel.get(channelId).getTemp() > channel.get(channelId).getMax()) {
                            state = OnOffType.ON;
                        } else {
                            state = OnOffType.OFF;
                        }
                        break;
                    case CHANNEL_ALARM_OPENHAB_LOW:
                        if (channel.get(channelId).getTemp() != 999
                                && channel.get(channelId).getTemp() < channel.get(channelId).getMin()) {
                            state = OnOffType.ON;
                        } else {
                            state = OnOffType.OFF;
                        }
                        break;
                    case CHANNEL_COLOR:
                        String color = channel.get(channelId).getColor();
                        if (color != null && !color.isEmpty()) {
                            Color c = Color.decode(color);
                            state = HSBType.fromRGB(c.getRed(), c.getGreen(), c.getBlue());
                        }
                        break;
                    case CHANNEL_COLOR_NAME:
                        String colorHex = channel.get(channelId).getColor();
                        if (colorHex != null && !colorHex.isEmpty()) {
                            state = new StringType(UtilNano.toColorName(colorHex));
                        }
                        break;
                }
            }
        } else if (channelUID.getId().startsWith("pit1")) {
            if (getPitmaster() != null && getPitmaster().getPm() != null && getPitmaster().getPm().size() > 0) {
                Pm pm = getPitmaster().getPm().get(0);
                switch (channelUID.getIdWithoutGroup()) {
                    case CHANNEL_PITMASTER_CHANNEL_ID:
                        state = new DecimalType(pm.getChannel());
                        break;
                    case CHANNEL_PITMASTER_PIDPROFILE:
                        state = new DecimalType(pm.getPid());
                        break;
                    case CHANNEL_PITMASTER_DUTY_CYCLE:
                        state = new DecimalType(pm.getValue());
                        break;
                    case CHANNEL_PITMASTER_SETPOINT:
                        state = new DecimalType(pm.getSet());
                        break;
                    case CHANNEL_PITMASTER_STATE:
                        state = new StringType(pm.getTyp());
                }
            } else {
                return UnDefType.UNDEF;
            }
        }
        return state;
    }

    public boolean setState(ChannelUID channelUID, Command command) {
        boolean success = false;
        if (channelUID.getId().startsWith("channel")) {
            int channelId = Integer.parseInt(channelUID.getGroupId().substring("channel".length())) - 1;
            if (channel.size() > 0 && channelId <= channel.size()) {
                switch (channelUID.getIdWithoutGroup()) {
                    case CHANNEL_NAME:
                        if (command instanceof StringType) {
                            channel.get(channelId).setName(command.toFullString());
                            success = true;
                        }
                        break;
                    case CHANNEL_MIN:
                        if (command instanceof QuantityType) {
                            channel.get(channelId).setMin(((QuantityType) command).doubleValue());
                            success = true;
                        }
                        break;
                    case CHANNEL_MAX:
                        if (command instanceof QuantityType) {
                            channel.get(channelId).setMax(((QuantityType) command).doubleValue());
                            success = true;
                        }
                        break;
                    case CHANNEL_ALARM_DEVICE:
                        if (command instanceof OnOffType) {
                            BigInteger value;
                            if (command == OnOffType.ON) {
                                value = BigInteger.valueOf(channel.get(channelId).getAlarm()).setBit(1);
                            } else {
                                value = BigInteger.valueOf(channel.get(channelId).getAlarm()).clearBit(1);
                            }
                            channel.get(channelId).setAlarm(value.intValue());
                            success = true;
                        }
                        break;
                    case CHANNEL_ALARM_PUSH:
                        if (command instanceof OnOffType) {
                            BigInteger value;
                            if (command == OnOffType.ON) {
                                value = BigInteger.valueOf(channel.get(channelId).getAlarm()).setBit(0);
                            } else {
                                value = BigInteger.valueOf(channel.get(channelId).getAlarm()).clearBit(0);
                            }
                            channel.get(channelId).setAlarm(value.intValue());
                            success = true;
                        }
                        break;
                    case CHANNEL_COLOR_NAME:
                        if (command instanceof StringType) {
                            channel.get(channelId).setColor(UtilNano.toHex(((StringType) command).toString()));
                            success = true;
                        }
                        break;
                }
            }
        } else if (channelUID.getId().equals("pit1")) {
            if (getPitmaster() != null && getPitmaster().getPm() != null && getPitmaster().getPm().size() > 0) {
                Pm pm = getPitmaster().getPm().get(0);
                switch (channelUID.getIdWithoutGroup()) {
                    case CHANNEL_PITMASTER_CHANNEL_ID:
                        pm.setChannel(((QuantityType) command).intValue());
                        success = true;
                        break;
                    case CHANNEL_PITMASTER_PIDPROFILE:
                        pm.setPid(((QuantityType) command).intValue());
                        success = true;
                        break;
                    case CHANNEL_PITMASTER_SETPOINT:
                        pm.setSet(((QuantityType) command).doubleValue());
                        success = true;
                        break;
                    case CHANNEL_PITMASTER_STATE:
                        String state = ((StringType) command).toString();
                        if (state.equalsIgnoreCase("off") || state.equalsIgnoreCase("manual")
                                || state.equalsIgnoreCase("auto")) {
                            pm.setTyp(state);
                            success = true;
                        }
                }
            }
        }
        return success;
    }

    public String getTrigger(ChannelUID channelUID) {
        String trigger = null;
        if (channelUID.getId().startsWith("channel")) {
            int channelId = Integer.parseInt(channelUID.getGroupId().substring("channel".length())) - 1;
            if (channel.size() > 0 && channelId <= channel.size()) {
                if (CHANNEL_ALARM_OPENHAB.equals(channelUID.getIdWithoutGroup())) {
                    if (channel.get(channelId).getTemp() != 999) {
                        if (channel.get(channelId).getTemp() > channel.get(channelId).getMax()) {
                            trigger = TRIGGER_ALARM_MAX;
                        } else if (channel.get(channelId).getTemp() < channel.get(channelId).getMin()) {
                            trigger = TRIGGER_ALARM_MIN;
                        }
                    }
                }
            }
        }
        return trigger;
    }
}
