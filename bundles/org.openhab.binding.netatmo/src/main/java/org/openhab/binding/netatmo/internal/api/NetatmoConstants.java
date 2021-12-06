/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.netatmo.internal.api;

import static org.openhab.binding.netatmo.internal.NetatmoBindingConstants.*;
import static org.openhab.core.library.unit.MetricPrefix.*;

import java.net.URI;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.measure.Unit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.library.CoreItemFactory;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.util.UnitUtils;

import com.google.gson.annotations.SerializedName;

/**
 * This class holds various definitions and settings provided by the Netatmo
 * API documentation
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class NetatmoConstants {
    public static class Measure {
        public final double minValue;
        public final int maxValue;
        public final int scale;
        public final Unit<?> unit;

        private Measure(double minValue, int maxValue, double precision, Unit<?> unit) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.unit = unit;
            String[] splitter = Double.valueOf(precision).toString().split("\\.");
            if (splitter.length > 1) {
                int dec = Integer.parseInt(splitter[1]);
                this.scale = dec > 0 ? Integer.toString(dec).length() : 0;
            } else {
                this.scale = 0;
            }
        }
    }

    public static class MeasureChannelDetails {
        public final URI configURI;
        public final String itemType;
        public final String pattern;

        public MeasureChannelDetails(String measureType, String itemType, String pattern) {
            this.configURI = URI.create(String.join(":", BINDING_ID, measureType, "config"));
            this.itemType = itemType;
            this.pattern = pattern;
        }
    }

    public enum MeasureClass {
        INTERIOR_TEMPERATURE(new Measure(0, 50, 0.3, SIUnits.CELSIUS), "temp", true),
        EXTERIOR_TEMPERATURE(new Measure(-40, 65, 0.3, SIUnits.CELSIUS), "temp", true),
        PRESSURE(new Measure(260, 1260, 1, HECTO(SIUnits.PASCAL)), "pressure", true),
        CO2(new Measure(0, 5000, 50, Units.PARTS_PER_MILLION), "co2", true),
        NOISE(new Measure(35, 120, 1, Units.DECIBEL), "noise", true),
        RAIN_QUANTITY(new Measure(0, 150, 0.1, MILLI(SIUnits.METRE)), "sum_rain", false),
        RAIN_INTENSITY(new Measure(0, 150, 0.1, Units.MILLIMETRE_PER_HOUR)),
        WIND_SPEED(new Measure(0, 160, 1.8, SIUnits.KILOMETRE_PER_HOUR)),
        WIND_ANGLE(new Measure(0, 360, 5, Units.DEGREE_ANGLE)),
        HUMIDITY(new Measure(0, 100, 3, Units.PERCENT), "hum", true);

        public static final EnumSet<MeasureClass> asSet = EnumSet.allOf(MeasureClass.class);

        public final Measure measureDefinition;
        public final String apiDescriptor;
        public final Map<String, MeasureChannelDetails> channels = new HashMap<>(2);

        MeasureClass(Measure measureDef) {
            this(measureDef, "", false);
        }

        MeasureClass(Measure measureDef, String apiDescriptor, boolean canScale) {
            this.measureDefinition = measureDef;
            this.apiDescriptor = apiDescriptor;
            if (!apiDescriptor.isBlank()) {
                String dimension = (Units.DECIBEL.equals(measureDef.unit)
                        || Units.PARTS_PER_MILLION.equals(measureDef.unit) || Units.PERCENT.equals(measureDef.unit))
                                ? "Dimensionless" // strangely it is given as an Angle
                                : UnitUtils.getDimensionName(measureDef.unit);

                channels.put(String.join("-", apiDescriptor, "measurement"),
                        new MeasureChannelDetails(apiDescriptor, String.join(":", CoreItemFactory.NUMBER, dimension),
                                String.format("%%.%df %s", measureDefinition.scale, UnitUtils.UNIT_PLACEHOLDER)));
                if (canScale) {
                    channels.put(String.join("-", apiDescriptor, GROUP_TIMESTAMP), new MeasureChannelDetails(
                            GROUP_TIMESTAMP, CoreItemFactory.DATETIME, "%1$tA, %1$td.%1$tm. %1$tH:%1$tM"));
                }
            }
        }
    }

    // Netatmo API urls
    public static final String URL_API = "https://api.netatmo.com/";
    public static final String URL_APP = "https://app.netatmo.net/";
    public static final String PATH_OAUTH = "oauth2/token";
    public static final String PATH_API = "api";
    public static final String PATH_COMMAND = "command";
    public static final String SPATH_PERSON_AWAY = "setpersonsaway";
    public static final String SPATH_PERSON_HOME = "setpersonshome";
    public static final String SPATH_HOMES = "homesdata";
    public static final String SPATH_GETHOME = "gethomedata";
    public static final String SPATH_GETCAMERAPICTURE = "getcamerapicture";
    public static final String SPATH_ADDWEBHOOK = "addwebhook";
    public static final String SPATH_DROPWEBHOOK = "dropwebhook";
    public static final String SPATH_SETROOMTHERMPOINT = "setroomthermpoint";
    public static final String SPATH_SETTHERMMODE = "setthermmode";
    public static final String SPATH_SWITCHSCHEDULE = "switchschedule";
    public static final String SPATH_GETTHERMOSTAT = "getthermostatsdata";
    public static final String SPATH_GETSTATION = "getstationsdata";
    public static final String SPATH_GETMEASURE = "getmeasure";
    public static final String SPATH_HOMESTATUS = "homestatus";
    public static final String SPATH_HOMECOACH = "gethomecoachsdata";
    public static final String SPATH_GETLASTEVENT = "getlasteventof";
    public static final String SPATH_PING = "ping";

    public static final String PARM_DEVICEID = "device_id";
    public static final String PARM_MODULEID = "module_id";
    public static final String PARM_HOMEID = "home_id";
    public static final String PARM_ROOMID = "room_id";
    public static final String PARM_PERSONID = "person_id";
    public static final String PARM_SCHEDULEID = "schedule_id";
    public static final String PARM_DEVICETYPE = "device_types";
    public static final String PARM_GATEWAYTYPE = "gateway_types";
    public static final String PARM_MODE = "mode";
    public static final String PARM_URL = "url";
    public static final String PARM_CHANGESTATUS = "changestatus";
    public static final String PARM_FLOODLIGHTSET = "floodlight_set_config";

    // Token scopes
    public static enum Scope {
        @SerializedName("read_station")
        READ_STATION,
        @SerializedName("read_thermostat")
        READ_THERMOSTAT,
        @SerializedName("write_thermostat")
        WRITE_THERMOSTAT,
        @SerializedName("read_camera")
        READ_CAMERA,
        @SerializedName("write_camera")
        WRITE_CAMERA,
        @SerializedName("access_camera")
        ACCESS_CAMERA,
        @SerializedName("read_presence")
        READ_PRESENCE,
        @SerializedName("access_presence")
        ACCESS_PRESENCE,
        @SerializedName("read_smokedetector")
        READ_SMOKEDETECTOR,
        @SerializedName("read_homecoach")
        READ_HOMECOACH,
        @SerializedName("read_doorbell")
        READ_DOORBELL,
        @SerializedName("write_doorbell")
        WRITE_DOORBELL,
        @SerializedName("access_doorbell")
        ACCESS_DOORBELL,
        UNKNOWN;
    }

    private static final Set<Scope> SMOKE_SCOPES = Set.of(Scope.READ_SMOKEDETECTOR);
    private static final Set<Scope> WELCOME_SCOPES = Set.of(Scope.READ_CAMERA, Scope.WRITE_CAMERA, Scope.ACCESS_CAMERA);
    private static final Set<Scope> DOORBELL_SCOPES = Set.of(Scope.READ_DOORBELL, Scope.WRITE_DOORBELL,
            Scope.ACCESS_DOORBELL);
    private static final Set<Scope> PRESENCE_SCOPES = Set.of(Scope.READ_PRESENCE, Scope.ACCESS_PRESENCE);

    // Radio signal quality thresholds
    static final int[] WIFI_SIGNAL_LEVELS = new int[] { 99, 84, 69, 54 }; // Resp : bad, average, good, full
    static final int[] RADIO_SIGNAL_LEVELS = new int[] { 90, 80, 70, 60 }; // Resp : low, medium, high, full

    public static enum FeatureArea {
        AIR_CARE(Set.of(Scope.READ_HOMECOACH)),
        WEATHER(Set.of(Scope.READ_STATION)),
        ENERGY(Set.of(Scope.READ_THERMOSTAT, Scope.WRITE_THERMOSTAT)),
        SECURITY(Stream.of(WELCOME_SCOPES, PRESENCE_SCOPES, SMOKE_SCOPES, DOORBELL_SCOPES).flatMap(Set::stream)
                .collect(Collectors.toSet())),
        NONE(Set.of());

        public static final EnumSet<FeatureArea> asSet = EnumSet.allOf(FeatureArea.class);

        public final Set<Scope> scopes;

        FeatureArea(Set<Scope> scopes) {
            this.scopes = scopes;
        }
    }

    // Thermostat definitions
    public static enum SetpointMode {
        @SerializedName("program")
        PROGRAM("program"),
        @SerializedName("away")
        AWAY("away"),
        @SerializedName("hg")
        FROST_GUARD("hg"),
        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("off")
        OFF("off"),
        @SerializedName("max")
        MAX("max"),
        @SerializedName("schedule")
        SCHEDULE("schedule"),
        HOME("home"),
        UNKNOWN("");

        public final String apiDescriptor;

        SetpointMode(String descriptor) {
            this.apiDescriptor = descriptor;
        }
    }

    public static enum ThermostatZoneType {
        @SerializedName("0")
        DAY("0"),
        @SerializedName("1")
        NIGHT("1"),
        @SerializedName("2")
        AWAY("2"),
        @SerializedName("3")
        FROST_GUARD("3"),
        @SerializedName("4")
        CUSTOM("4"),
        @SerializedName("5")
        ECO("5"),
        @SerializedName("8")
        COMFORT("8"),
        UNKNOWN("");

        public final String zoneId;

        private ThermostatZoneType(String id) {
            zoneId = id;
        }

        public static ThermostatZoneType fromId(String id) {
            return Arrays.stream(values()).filter(value -> value.zoneId.equals(id)).findFirst().orElse(UNKNOWN);
        }
    }

    public enum PresenceLightMode {
        @SerializedName("on")
        ON,
        @SerializedName("off")
        OFF,
        @SerializedName("auto")
        AUTO,
        UNKNOWN;
    }

    public enum EventCategory {
        @SerializedName("human")
        HUMAN,
        @SerializedName("animal")
        ANIMAL,
        @SerializedName("vehicle")
        VEHICLE,
        UNKNOWN;
    }

    public enum TrendDescription {
        @SerializedName("up")
        UP,
        @SerializedName("stable")
        STABLE,
        @SerializedName("down")
        DOWN,
        UNKNOWN;
    }

    public enum VideoStatus {
        @SerializedName("recording")
        RECORDING,
        @SerializedName("available")
        AVAILABLE,
        @SerializedName("deleted")
        DELETED,
        UNKNOWN;
    }

    public enum BatteryState {
        @SerializedName("full")
        FULL(100),
        @SerializedName("high")
        HIGH(80),
        @SerializedName("medium")
        MEDIUM(50),
        @SerializedName("low")
        LOW(15),
        UNKNOWN(-1);

        public final int level;

        BatteryState(int i) {
            this.level = i;
        }
    }
}
