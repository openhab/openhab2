/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.growatt.internal.dto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.growatt.internal.GrowattBindingConstants;
import org.openhab.binding.growatt.internal.GrowattBindingConstants.UoM;
import org.openhab.core.library.types.QuantityType;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link GrottValues} is a DTO containing inverter value fields received from the Grott application.
 *
 * @author Andrew Fiddian-Green - Initial contribution
 */
@NonNullByDefault
public class GrottValues {

    /**
     * Convert Java field name to openHAB channel id
     */
    public static String getChannelId(String fieldName) {
        return fieldName.replace("_", "-");
    }

    /**
     * Convert openHAB channel id to Java field name
     */
    public static String getFieldName(String channelId) {
        return channelId.replace("-", "_");
    }

    // @formatter:off

    // inverter state
    public @Nullable @SerializedName(value = "pvstatus") Integer system_status;

    // solar AC and DC generation
    public @Nullable @SerializedName(value = "pvpowerin") Integer pv_power_in; // from DC solar
    public @Nullable @SerializedName(value = "pvpowerout") Integer pv_power_out; // to AC mains

    // DC electric data for strings #1 and #2
    public @Nullable @SerializedName(value = "pv1voltage", alternate = { "vpv1" }) Integer pv1_voltage;
    public @Nullable @SerializedName(value = "pv1current", alternate = { "buck1curr" }) Integer pv1_current;
    public @Nullable @SerializedName(value = "pv1watt", alternate = { "ppv1" }) Integer pv1_power;

    public @Nullable @SerializedName(value = "pv2voltage", alternate = { "vpv2" }) Integer pv2_voltage;
    public @Nullable @SerializedName(value = "pv2current", alternate = { "buck2curr" }) Integer pv2_current;
    public @Nullable @SerializedName(value = "pv2watt", alternate = { "ppv2" }) Integer pv2_power;

    // AC mains electric data (1-phase resp. 3-phase)
    public @Nullable @SerializedName(value = "pvfrequentie", alternate = { "line_freq", "outputfreq" }) Integer grid_frequency;
    public @Nullable @SerializedName(value = "pvgridvoltage", alternate = { "grid_volt", "outputvolt" }) Integer grid_voltage;
    public @Nullable @SerializedName(value = "pvgridvoltage2") Integer grid_voltage_s;
    public @Nullable @SerializedName(value = "pvgridvoltage3") Integer grid_voltage_t;
    public @Nullable @SerializedName(value = "Vac_RS") Integer grid_voltage_rs;
    public @Nullable @SerializedName(value = "Vac_ST") Integer grid_voltage_st;
    public @Nullable @SerializedName(value = "Vac_TR") Integer grid_voltage_tr;

    // solar AC mains power
    public @Nullable @SerializedName(value = "pvgridcurrent", alternate = { "OP_Curr", "Inv_Curr" }) Integer solar_current;
    public @Nullable @SerializedName(value = "pvgridcurrent2") Integer solar_current_s;
    public @Nullable @SerializedName(value = "pvgridcurrent3") Integer solar_current_t;

    public @Nullable @SerializedName(value = "pvgridpower", alternate = { "op_watt", "AC_InWatt" }) Integer solar_power;
    public @Nullable @SerializedName(value = "pvgridpower2") Integer solar_power_s;
    public @Nullable @SerializedName(value = "pvgridpower3") Integer solar_power_t;

    public @Nullable @SerializedName(value = "op_va", alternate = { "AC_InVA" }) Integer solar_va;

    // battery discharge / charge power
    public @Nullable @SerializedName(value = "p1charge1", alternate = { "acchr_watt", "BatWatt" }) Integer charge_power;
    public @Nullable @SerializedName(value = "pdischarge1", alternate = { "ACDischarWatt", "BatDischarWatt" }) Integer discharge_power;

    // miscellaneous battery
    public @Nullable @SerializedName(value = "ACCharCurr") Integer charge_current;
    public @Nullable @SerializedName(value = "ACDischarVA", alternate = { "BatDischarVA", "acchar_VA" }) Integer discharge_va;

    // power exported to utility company
    public @Nullable @SerializedName(value = "pactogridtot") Integer export_power;
    public @Nullable @SerializedName(value = "pactogridr") Integer export_power_r;
    public @Nullable @SerializedName(value = "pactogrids") Integer export_power_s;
    public @Nullable @SerializedName(value = "pactogridt") Integer export_power_t;

    // power imported from utility company
    public @Nullable @SerializedName(value = "pactousertot") Integer import_power;
    public @Nullable @SerializedName(value = "pactouserr") Integer import_power_r;
    public @Nullable @SerializedName(value = "pactousers") Integer import_power_s;
    public @Nullable @SerializedName(value = "pactousert") Integer import_power_t;

    // power delivered to internal load
    public @Nullable @SerializedName(value = "plocaloadtot") Integer load_power;
    public @Nullable @SerializedName(value = "plocaloadr") Integer load_power_r;
    public @Nullable @SerializedName(value = "plocaloads") Integer load_power_s;
    public @Nullable @SerializedName(value = "plocaloadt") Integer load_power_t;

    // solar AC grid energy
    public @Nullable @SerializedName(value = "eactoday", alternate = { "pvenergytoday" }) Integer solar_energy_today;
    public @Nullable @SerializedName(value = "eactotal", alternate = { "pvenergytotal" }) Integer solar_energy_total;

    // solar DC pv energy
    public @Nullable @SerializedName(value = "epvtoday") Integer pv_energy_today;
    public @Nullable @SerializedName(value = "epv1today", alternate = { "epv1tod" }) Integer pv1_energy_today;
    public @Nullable @SerializedName(value = "epv2today", alternate = { "epv2tod" }) Integer pv2_energy_today;

    public @Nullable @SerializedName(value = "epvtotal") Integer pv_energy_total;
    public @Nullable @SerializedName(value = "epv1total", alternate = { "epv1tot" }) Integer pv1_energy_total;
    public @Nullable @SerializedName(value = "epv2total", alternate = { "epv2tot" }) Integer pv2_energy_total;

    // energy exported to utility company
    public @Nullable @SerializedName(value = "etogrid_tod") Integer export_energy_today;
    public @Nullable @SerializedName(value = "etogrid_tot") Integer export_energy_total;

    // energy imported from utility company
    public @Nullable @SerializedName(value = "etouser_tod") Integer import_energy_today;
    public @Nullable @SerializedName(value = "etouser_tot") Integer import_energy_total;

    // energy supplied to local load
    public @Nullable @SerializedName(value = "elocalload_tod") Integer load_energy_today;
    public @Nullable @SerializedName(value = "elocalload_tot") Integer load_energy_total;

    // charging energy from import
    public @Nullable @SerializedName(value = "eharge1_tod") Integer import_charge_energy_today;
    public @Nullable @SerializedName(value = "eharge1_tot") Integer import_charge_energy_total;

    // charging energy from solar
    public @Nullable @SerializedName(value = "eacharge_today", alternate = { "eacCharToday" }) Integer solar_charge_energy_today;
    public @Nullable @SerializedName(value = "eacharge_total", alternate = { "eacCharTotal" }) Integer solar_charge_energy_total;

    // discharging energy
    public @Nullable @SerializedName(value = "edischarge1_tod", alternate = { "eacDischarToday", "ebatDischarToday" }) Integer discharge_energy_today;
    public @Nullable @SerializedName(value = "edischarge1_tot", alternate = { "eacDischarTotal","ebatDischarTotal" }) Integer discharge_energy_total;

    // inverter up time
    public @Nullable @SerializedName(value = "totworktime") Integer total_work_time;

    // bus voltages
    public @Nullable @SerializedName(value = "pbusvolt", alternate = { "bus_volt" }) Integer p_bus_voltage;
    public @Nullable @SerializedName(value = "nbusvolt") Integer n_bus_voltage;
    public @Nullable @SerializedName(value = "spbusvolt") Integer sp_bus_voltage;

    // temperatures
    public @Nullable @SerializedName(value = "pvtemperature", alternate = { "dcdctemp", "buck1_ntc" }) Integer pv_temperature;
    public @Nullable @SerializedName(value = "pvipmtemperature", alternate = { "invtemp" }) Integer pv_ipm_temperature;
    public @Nullable @SerializedName(value = "pvboosttemp", alternate = {"pvboottemperature" }) Integer pv_boost_temperature;
    public @Nullable @SerializedName(value = "temp4") Integer temperature_4;
    public @Nullable @SerializedName(value = "buck2_ntc") Integer pv2_temperature;

    // battery data
    public @Nullable @SerializedName(value = "batterytype") Integer battery_type;
    public @Nullable @SerializedName(value = "batttemp") Integer battery_temperature;
    public @Nullable @SerializedName(value = "vbat", alternate = { "uwBatVolt_DSP", "bat_Volt" }) Integer battery_voltage;
    public @Nullable @SerializedName(value = "bat_dsp") Integer battery_display;
    public @Nullable @SerializedName(value = "SOC", alternate = { "batterySOC" }) Integer battery_soc;

    // fault codes
    public @Nullable @SerializedName(value = "systemfaultword0", alternate = { "isof", "faultBit" }) Integer system_fault_0;
    public @Nullable @SerializedName(value = "systemfaultword1", alternate = { "gfcif", "faultValue" }) Integer system_fault_1;
    public @Nullable @SerializedName(value = "systemfaultword2", alternate = { "dcif", "warningBit" }) Integer system_fault_2;
    public @Nullable @SerializedName(value = "systemfaultword3", alternate = { "vpvfault", "warningValue" }) Integer system_fault_3;
    public @Nullable @SerializedName(value = "systemfaultword4", alternate = { "vacfault" }) Integer system_fault_4;
    public @Nullable @SerializedName(value = "systemfaultword5", alternate = { "facfault" }) Integer system_fault_5;
    public @Nullable @SerializedName(value = "systemfaultword6", alternate = { "tempfault" }) Integer system_fault_6;
    public @Nullable @SerializedName(value = "systemfaultword7", alternate = { "faultcode" }) Integer system_fault_7;

    // miscellaneous
    public @Nullable @SerializedName(value = "uwsysworkmode") Integer system_work_mode;
    public @Nullable @SerializedName(value = "spdspstatus") Integer sp_display_status;
    public @Nullable @SerializedName(value = "constantPowerOK") Integer constant_power_ok;
    public @Nullable @SerializedName(value = "loadpercent") Integer load_percent;

    // rac ??
    public @Nullable @SerializedName(value = "rac") Integer rac;
    public @Nullable @SerializedName(value = "eractoday") Integer erac_today;
    public @Nullable @SerializedName(value = "eractotal") Integer erac_total;

    // @formatter:on

    /**
     * Return the valid values from this DTO in a map between channel id and respective QuantityType states.
     *
     * @return a map of channel ids and respective QuantityType state values.
     * @throws NoSuchFieldException should not occur since we specifically tested this in JUnit tests.
     * @throws SecurityException should not occur since all fields are public.
     * @throws IllegalAccessException should not occur since all fields are public.
     * @throws IllegalArgumentException should not occur since we are specifically working with this class.
     */
    public Map<String, QuantityType<?>> getChannelStates()
            throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException {
        Map<String, QuantityType<?>> map = new HashMap<>();
        for (Entry<String, UoM> entry : GrowattBindingConstants.CHANNEL_ID_UOM_MAP.entrySet()) {
            String channelId = entry.getKey();
            UoM uom = entry.getValue();
            Field field = getClass().getField(getFieldName(channelId));
            Object obj = field.get(this);
            if (obj instanceof Integer) {
                map.put(channelId, QuantityType.valueOf(((Integer) obj).doubleValue() / uom.divisor, uom.units));
            }
        }
        return map;
    }
}
