/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.renault.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * MyRenault registered car for parsing HTTP responses and collecting data and
 * information.
 *
 * @author Doug Culnane - Initial contribution
 */
@NonNullByDefault
public class Car {

    public static final String CHARGING_MODE_SCHEDULE = "schedule_mode";
    public static final String CHARGING_MODE_ALWAYS = "always_charging";
    public static final String HVAC_STATUS_ON = "ON";
    public static final String HVAC_STATUS_OFF = "OFF";
    public static final String HVAC_STATUS_PENDING = "PENDING";

    private final Logger logger = LoggerFactory.getLogger(Car.class);

    private boolean disableLocation = false;
    private boolean disableBattery = false;
    private boolean disableCockpit = false;
    private boolean disableHvac = false;

    private @Nullable Double batteryLevel;
    private @Nullable Double batteryAvailableEnergy;
    private @Nullable Integer chargingRemainingTime;
    private @Nullable String chargingStatus;
    private @Nullable String chargingMode;
    private @Nullable Boolean hvacstatus;
    private Double hvacTargetTemperature = 20.0;
    private @Nullable Double odometer;
    private @Nullable Double estimatedRange;
    private @Nullable String imageURL;
    private @Nullable String locationUpdated;
    private @Nullable Double gpsLatitude;
    private @Nullable Double gpsLongitude;
    private @Nullable Double externalTemperature;
    private @Nullable String plugStatus;

    public void setBatteryStatus(JsonObject responseJson) {
        try {
            JsonObject attributes = getAttributes(responseJson);
            if (attributes != null) {
                if (attributes.get("batteryLevel") != null) {
                    batteryLevel = attributes.get("batteryLevel").getAsDouble();
                }
                if (attributes.get("batteryAutonomy") != null) {
                    estimatedRange = attributes.get("batteryAutonomy").getAsDouble();
                }
                if (attributes.get("plugStatus") != null) {
                    plugStatus = mapPlugStatus(attributes.get("plugStatus").getAsString());
                }
                if (attributes.get("chargingStatus") != null) {
                    chargingStatus = mapChargingStatus(attributes.get("chargingStatus").getAsString());
                }
                if (attributes.get("batteryAvailableEnergy") != null) {
                    batteryAvailableEnergy = attributes.get("batteryAvailableEnergy").getAsDouble();
                }
                if (attributes.get("chargingRemainingTime") != null) {
                    chargingRemainingTime = attributes.get("chargingRemainingTime").getAsInt();
                }
            }
        } catch (IllegalStateException | ClassCastException e) {
            logger.warn("Error {} parsing Battery Status: {}", e.getMessage(), responseJson);
        }
    }

    public void resetHVACStatus() {
        this.hvacstatus = null;
    }

    public void setHVACStatus(JsonObject responseJson) {
        try {
            JsonObject attributes = getAttributes(responseJson);
            if (attributes != null) {
                if (attributes.get("hvacStatus") != null) {
                    hvacstatus = attributes.get("hvacStatus").getAsString().equals("on");
                }
                if (attributes.get("externalTemperature") != null) {
                    externalTemperature = attributes.get("externalTemperature").getAsDouble();
                }
            }
        } catch (IllegalStateException | ClassCastException e) {
            logger.warn("Error {} parsing HVAC Status: {}", e.getMessage(), responseJson);
        }
    }

    public void setCockpit(JsonObject responseJson) {
        try {
            JsonObject attributes = getAttributes(responseJson);
            if (attributes != null && attributes.get("totalMileage") != null) {
                odometer = attributes.get("totalMileage").getAsDouble();
            }
        } catch (IllegalStateException | ClassCastException e) {
            logger.warn("Error {} parsing Cockpit: {}", e.getMessage(), responseJson);
        }
    }

    public void setLocation(JsonObject responseJson) {
        try {
            JsonObject attributes = getAttributes(responseJson);
            if (attributes != null) {
                if (attributes.get("gpsLatitude") != null) {
                    gpsLatitude = attributes.get("gpsLatitude").getAsDouble();
                }
                if (attributes.get("gpsLongitude") != null) {
                    gpsLongitude = attributes.get("gpsLongitude").getAsDouble();
                }
                if (attributes.get("lastUpdateTime") != null) {
                    locationUpdated = attributes.get("lastUpdateTime").getAsString();
                }
            }
        } catch (IllegalStateException | ClassCastException e) {
            logger.warn("Error {} parsing Location: {}", e.getMessage(), responseJson);
        }
    }

    public void setDetails(JsonObject responseJson) {
        try {
            if (responseJson.get("assets") != null) {
                JsonArray assetsJson = responseJson.get("assets").getAsJsonArray();
                String url = null;
                for (JsonElement asset : assetsJson) {
                    if (asset.getAsJsonObject().get("assetType") != null
                            && asset.getAsJsonObject().get("assetType").getAsString().equals("PICTURE")) {
                        if (asset.getAsJsonObject().get("renditions") != null) {
                            JsonArray renditions = asset.getAsJsonObject().get("renditions").getAsJsonArray();
                            for (JsonElement rendition : renditions) {
                                if (rendition.getAsJsonObject().get("resolutionType") != null
                                        && rendition.getAsJsonObject().get("resolutionType").getAsString()
                                                .equals("ONE_MYRENAULT_SMALL")) {
                                    url = rendition.getAsJsonObject().get("url").getAsString();
                                    break;
                                }
                            }
                        }
                    }
                    if (url != null && !url.isEmpty()) {
                        imageURL = url;
                        break;
                    }
                }
            }
        } catch (IllegalStateException | ClassCastException e) {
            logger.warn("Error {} parsing Details: {}", e.getMessage(), responseJson);
        }
    }

    public boolean isDisableLocation() {
        return disableLocation;
    }

    public boolean isDisableBattery() {
        return disableBattery;
    }

    public boolean isDisableCockpit() {
        return disableCockpit;
    }

    public boolean isDisableHvac() {
        return disableHvac;
    }

    public @Nullable Double getBatteryLevel() {
        return batteryLevel;
    }

    public @Nullable Boolean getHvacstatus() {
        return hvacstatus;
    }

    public @Nullable Double getOdometer() {
        return odometer;
    }

    public @Nullable String getImageURL() {
        return imageURL;
    }

    public @Nullable Double getGpsLatitude() {
        return gpsLatitude;
    }

    public @Nullable Double getGpsLongitude() {
        return gpsLongitude;
    }

    public @Nullable String getLocationUpdated() {
        return locationUpdated;
    }

    public @Nullable Double getExternalTemperature() {
        return externalTemperature;
    }

    public @Nullable Double getEstimatedRange() {
        return estimatedRange;
    }

    public @Nullable String getPlugStatus() {
        return plugStatus;
    }

    public @Nullable String getChargingStatus() {
        return chargingStatus;
    }

    public @Nullable String getChargingMode() {
        return chargingMode;
    }

    public @Nullable Integer getChargingRemainingTime() {
        return chargingRemainingTime;
    }

    public @Nullable Double getBatteryAvailableEnergy() {
        return batteryAvailableEnergy;
    }

    public Double getHvacTargetTemperature() {
        return hvacTargetTemperature;
    }

    public void setHvacTargetTemperature(Double hvacTargetTemperature) {
        this.hvacTargetTemperature = hvacTargetTemperature;
    }

    public void setDisableLocation(boolean disableLocation) {
        this.disableLocation = disableLocation;
    }

    public void setDisableBattery(boolean disableBattery) {
        this.disableBattery = disableBattery;
    }

    public void setDisableCockpit(boolean disableCockpit) {
        this.disableCockpit = disableCockpit;
    }

    public void setDisableHvac(boolean disableHvac) {
        this.disableHvac = disableHvac;
    }

    public void setChargeMode(String mode) {
        switch (mode) {
            case CHARGING_MODE_SCHEDULE:
                chargingMode = CHARGING_MODE_SCHEDULE;
                break;
            case CHARGING_MODE_ALWAYS:
                chargingMode = CHARGING_MODE_ALWAYS;
                break;
            default:
                break;
        }
    }

    private @Nullable JsonObject getAttributes(JsonObject responseJson)
            throws IllegalStateException, ClassCastException {
        if (responseJson.get("data") != null && responseJson.get("data").getAsJsonObject().get("attributes") != null) {
            return responseJson.get("data").getAsJsonObject().get("attributes").getAsJsonObject();
        }
        return null;
    }

    private String mapPlugStatus(String plugState) {
        // https://github.com/hacf-fr/renault-api/blob/main/src/renault_api/kamereon/enums.py
        switch (plugState) {
            case "0":
                return "UNPLUGGED";
            case "1":
                return "PLUGGED";
            case "-1":
                return "PLUG_ERROR";
            case "-2147483648":
                return "PLUG_UNKNOWN";
            default:
                return "UNKNOWEN";
        }
    }

    private String mapChargingStatus(String chargeState) {
        // https://github.com/hacf-fr/renault-api/blob/main/src/renault_api/kamereon/enums.py
        switch (chargeState) {
            case "0.0":
                return "NOT_IN_CHARGE";
            case "0.1":
                return "WAITING_FOR_A_PLANNED_CHARGE";
            case "0.2":
                return "CHARGE_ENDED";
            case "0.3":
                return "WAITING_FOR_CURRENT_CHARGE";
            case "0.4":
                return "ENERGY_FLAP_OPENED";
            case "1.0":
                return "CHARGE_IN_PROGRESS";
            case "-1.0":
                return "CHARGE_ERROR";
            case "-1.1":
                return "UNAVAILABLE";
            default:
                return "UNKNOWEN";
        }
    }
}
