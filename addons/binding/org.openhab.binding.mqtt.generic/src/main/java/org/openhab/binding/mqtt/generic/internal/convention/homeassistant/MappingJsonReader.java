package org.openhab.binding.mqtt.generic.internal.convention.homeassistant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.mqtt.generic.internal.tools.JsonReaderDelegate;

import com.google.gson.stream.JsonReader;

public class MappingJsonReader extends JsonReaderDelegate {

    private static final Map<String, String> ABBREVIATIONS = new HashMap<>();
    private static final Map<String, String> DEVICE_ABBREVIATIONS = new HashMap<>();

    static {
        ABBREVIATIONS.put("aux_cmd_t", "aux_command_topic");
        ABBREVIATIONS.put("aux_stat_tpl", "aux_state_template");
        ABBREVIATIONS.put("aux_stat_t", "aux_state_topic");
        ABBREVIATIONS.put("avty_t", "availability_topic");
        ABBREVIATIONS.put("away_mode_cmd_t", "away_mode_command_topic");
        ABBREVIATIONS.put("away_mode_stat_tpl", "away_mode_state_template");
        ABBREVIATIONS.put("away_mode_stat_t", "away_mode_state_topic");
        ABBREVIATIONS.put("bri_cmd_t", "brightness_command_topic");
        ABBREVIATIONS.put("bri_scl", "brightness_scale");
        ABBREVIATIONS.put("bri_stat_t", "brightness_state_topic");
        ABBREVIATIONS.put("bri_val_tpl", "brightness_value_template");
        ABBREVIATIONS.put("bat_lev_t", "battery_level_topic");
        ABBREVIATIONS.put("bat_lev_tpl", "battery_level_template");
        ABBREVIATIONS.put("chrg_t", "charging_topic");
        ABBREVIATIONS.put("chrg_tpl", "charging_template");
        ABBREVIATIONS.put("clr_temp_cmd_t", "color_temp_command_topic");
        ABBREVIATIONS.put("clr_temp_stat_t", "color_temp_state_topic");
        ABBREVIATIONS.put("clr_temp_val_tpl", "color_temp_value_template");
        ABBREVIATIONS.put("cln_t", "cleaning_topic");
        ABBREVIATIONS.put("cln_tpl", "cleaning_template");
        ABBREVIATIONS.put("cmd_t", "command_topic");
        ABBREVIATIONS.put("curr_temp_t", "current_temperature_topic");
        ABBREVIATIONS.put("dev_cla", "device_class");
        ABBREVIATIONS.put("dock_t", "docked_topic");
        ABBREVIATIONS.put("dock_tpl", "docked_template");
        ABBREVIATIONS.put("err_t", "error_topic");
        ABBREVIATIONS.put("err_tpl", "error_template");
        ABBREVIATIONS.put("fanspd_t", "fan_speed_topic");
        ABBREVIATIONS.put("fanspd_tpl", "fan_speed_template");
        ABBREVIATIONS.put("fanspd_lst", "fan_speed_list");
        ABBREVIATIONS.put("fx_cmd_t", "effect_command_topic");
        ABBREVIATIONS.put("fx_list", "effect_list");
        ABBREVIATIONS.put("fx_stat_t", "effect_state_topic");
        ABBREVIATIONS.put("fx_val_tpl", "effect_value_template");
        ABBREVIATIONS.put("exp_aft", "expire_after");
        ABBREVIATIONS.put("fan_mode_cmd_t", "fan_mode_command_topic");
        ABBREVIATIONS.put("fan_mode_stat_tpl", "fan_mode_state_template");
        ABBREVIATIONS.put("fan_mode_stat_t", "fan_mode_state_topic");
        ABBREVIATIONS.put("frc_upd", "force_update");
        ABBREVIATIONS.put("hold_cmd_t", "hold_command_topic");
        ABBREVIATIONS.put("hold_stat_tpl", "hold_state_template");
        ABBREVIATIONS.put("hold_stat_t", "hold_state_topic");
        ABBREVIATIONS.put("ic", "icon");
        ABBREVIATIONS.put("init", "initial");
        ABBREVIATIONS.put("json_attr", "json_attributes");
        ABBREVIATIONS.put("max_temp", "max_temp");
        ABBREVIATIONS.put("min_temp", "min_temp");
        ABBREVIATIONS.put("mode_cmd_t", "mode_command_topic");
        ABBREVIATIONS.put("mode_stat_tpl", "mode_state_template");
        ABBREVIATIONS.put("mode_stat_t", "mode_state_topic");
        ABBREVIATIONS.put("name", "name");
        ABBREVIATIONS.put("on_cmd_type", "on_command_type");
        ABBREVIATIONS.put("opt", "optimistic");
        ABBREVIATIONS.put("osc_cmd_t", "oscillation_command_topic");
        ABBREVIATIONS.put("osc_stat_t", "oscillation_state_topic");
        ABBREVIATIONS.put("osc_val_tpl", "oscillation_value_template");
        ABBREVIATIONS.put("pl_arm_away", "payload_arm_away");
        ABBREVIATIONS.put("pl_arm_home", "payload_arm_home");
        ABBREVIATIONS.put("pl_avail", "payload_available");
        ABBREVIATIONS.put("pl_cls", "payload_close");
        ABBREVIATIONS.put("pl_disarm", "payload_disarm");
        ABBREVIATIONS.put("pl_hi_spd", "payload_high_speed");
        ABBREVIATIONS.put("pl_lock", "payload_lock");
        ABBREVIATIONS.put("pl_lo_spd", "payload_low_speed");
        ABBREVIATIONS.put("pl_med_spd", "payload_medium_speed");
        ABBREVIATIONS.put("pl_not_avail", "payload_not_available");
        ABBREVIATIONS.put("pl_off", "payload_off");
        ABBREVIATIONS.put("pl_on", "payload_on");
        ABBREVIATIONS.put("pl_open", "payload_open");
        ABBREVIATIONS.put("pl_osc_off", "payload_oscillation_off");
        ABBREVIATIONS.put("pl_osc_on", "payload_oscillation_on");
        ABBREVIATIONS.put("pl_stop", "payload_stop");
        ABBREVIATIONS.put("pl_unlk", "payload_unlock");
        ABBREVIATIONS.put("pow_cmd_t", "power_command_topic");
        ABBREVIATIONS.put("ret", "retain");
        ABBREVIATIONS.put("rgb_cmd_tpl", "rgb_command_template");
        ABBREVIATIONS.put("rgb_cmd_t", "rgb_command_topic");
        ABBREVIATIONS.put("rgb_stat_t", "rgb_state_topic");
        ABBREVIATIONS.put("rgb_val_tpl", "rgb_value_template");
        ABBREVIATIONS.put("send_cmd_t", "send_command_topic");
        ABBREVIATIONS.put("send_if_off", "send_if_off");
        ABBREVIATIONS.put("set_pos_tpl", "set_position_template");
        ABBREVIATIONS.put("set_pos_t", "set_position_topic");
        ABBREVIATIONS.put("spd_cmd_t", "speed_command_topic");
        ABBREVIATIONS.put("spd_stat_t", "speed_state_topic");
        ABBREVIATIONS.put("spd_val_tpl", "speed_value_template");
        ABBREVIATIONS.put("spds", "speeds");
        ABBREVIATIONS.put("stat_clsd", "state_closed");
        ABBREVIATIONS.put("stat_off", "state_off");
        ABBREVIATIONS.put("stat_on", "state_on");
        ABBREVIATIONS.put("stat_open", "state_open");
        ABBREVIATIONS.put("stat_t", "state_topic");
        ABBREVIATIONS.put("stat_val_tpl", "state_value_template");
        ABBREVIATIONS.put("sup_feat", "supported_features");
        ABBREVIATIONS.put("swing_mode_cmd_t", "swing_mode_command_topic");
        ABBREVIATIONS.put("swing_mode_stat_tpl", "swing_mode_state_template");
        ABBREVIATIONS.put("swing_mode_stat_t", "swing_mode_state_topic");
        ABBREVIATIONS.put("temp_cmd_t", "temperature_command_topic");
        ABBREVIATIONS.put("temp_stat_tpl", "temperature_state_template");
        ABBREVIATIONS.put("temp_stat_t", "temperature_state_topic");
        ABBREVIATIONS.put("tilt_clsd_val", "tilt_closed_value");
        ABBREVIATIONS.put("tilt_cmd_t", "tilt_command_topic");
        ABBREVIATIONS.put("tilt_inv_stat", "tilt_invert_state");
        ABBREVIATIONS.put("tilt_max", "tilt_max");
        ABBREVIATIONS.put("tilt_min", "tilt_min");
        ABBREVIATIONS.put("tilt_opnd_val", "tilt_opened_value");
        ABBREVIATIONS.put("tilt_status_opt", "tilt_status_optimistic");
        ABBREVIATIONS.put("tilt_status_t", "tilt_status_topic");
        ABBREVIATIONS.put("t", "topic");
        ABBREVIATIONS.put("uniq_id", "unique_id");
        ABBREVIATIONS.put("unit_of_meas", "unit_of_measurement");
        ABBREVIATIONS.put("val_tpl", "value_template");
        ABBREVIATIONS.put("whit_val_cmd_t", "white_value_command_topic");
        ABBREVIATIONS.put("whit_val_stat_t", "white_value_state_topic");
        ABBREVIATIONS.put("whit_val_tpl", "white_value_template");
        ABBREVIATIONS.put("xy_cmd_t", "xy_command_topic");
        ABBREVIATIONS.put("xy_stat_t", "xy_state_topic");
        ABBREVIATIONS.put("xy_val_tpl", "xy_value_template");

        DEVICE_ABBREVIATIONS.put("cns", "connections");
        DEVICE_ABBREVIATIONS.put("ids", "identifiers");
        DEVICE_ABBREVIATIONS.put("name", "name");
        DEVICE_ABBREVIATIONS.put("mf", "manufacturer");
        DEVICE_ABBREVIATIONS.put("mdl", "model");
        DEVICE_ABBREVIATIONS.put("sw", "sw_version");
    }

    private final Map<String, String> mapping;

    public static MappingJsonReader getConfigMapper(JsonReader delegate) {
        return new MappingJsonReader(JsonReaderDelegate.getDelegate(delegate), ABBREVIATIONS);
    }

    public static MappingJsonReader getDeviceMapper(JsonReader delegate) {
        return new MappingJsonReader(JsonReaderDelegate.getDelegate(delegate), DEVICE_ABBREVIATIONS);
    }

    private MappingJsonReader(JsonReader delegate, Map<String, String> mapping) {
        super(delegate);
        this.mapping = mapping;
    }

    @Override
    public String nextName() throws IOException {
        String name = super.nextName();
        return mapping.getOrDefault(name, name);
    }

}