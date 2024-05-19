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
package org.openhab.binding.sunsynk.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.openhab.binding.sunsynk.internal.classes.APIdata;
import org.openhab.binding.sunsynk.internal.classes.Battery;
import org.openhab.binding.sunsynk.internal.classes.Daytemps;
import org.openhab.binding.sunsynk.internal.classes.Grid;
import org.openhab.binding.sunsynk.internal.classes.RealTimeInData;
import org.openhab.binding.sunsynk.internal.classes.Settings;
import org.openhab.binding.sunsynk.internal.config.SunSynkInverterConfig;
import org.openhab.core.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link SunSynkInverter} class defines methods that contol
 * communication with the inverter.
 *
 * @author Lee Charlton - Initial contribution
 */

public class SunSynkInverter {

    private final Logger logger = LoggerFactory.getLogger(SunSynkInverter.class);
    private String uid;
    private String access_token;
    private String gsn;
    private String sn;
    private String alias;
    private int refresh;
    private Settings batterySettings; // common settings (inverter charge / discharge etc)
    private Battery realTimeBattery; // realtime battery
    private Grid grid; // realtime grid
    private Daytemps inverter_day_temperatures; // inverter temperatures history
    private RealTimeInData realTimeDataIn; // realtime data

    public SunSynkInverter(SunSynkInverterConfig config) {
        this.sn = config.getsn();
        this.refresh = config.getRefresh();
        this.alias = config.getAlias();
    }

    public String sendGetState(Boolean batterySettingsUpdate) { // Class entry method to update internal inverter state
        logger.debug("Will get STATE for Inverter {} serial {}", this.alias, this.sn);
        // Get inverter infos
        String response = null;
        try {
            if (batterySettingsUpdate == null) {
                batterySettingsUpdate = false;
            }
            if (!batterySettingsUpdate) { // normally get settings to track changes made by other UIs
                logger.debug("Trying Common Settings");
                response = getCommonSettings(); // battery charge settings
            }
            logger.debug("Trying Grid Real Time Settings");
            response = getGridRealTime(); // grid status
            logger.debug("Trying Battery Real Time Settings");
            response = getBatteryRealTime(); // battery status
            logger.debug("Trying Temperature History");
            response = getInverterACDCTemperatures(); // get Inverter temperatures
            logger.debug("Trying Real Time Solar");
            response = getRealTimeIn(); // Used for solar power now
        } catch (Exception e) {
            logger.debug("Failed to get Inverter API information: {} ", e.getMessage());
            int found = e.getMessage().indexOf("Authentication challenge without WWW-Authenticate header");
            if (found > -1) {
                return "Authentication Fail";
            }
            return "Failed";
        }
        logger.debug("Successfully got and parsed new data for Inverter {} serial {}", this.alias, this.sn);
        return response;
    }

    // ------ GETTERS ------ //
    public Settings getBatteryChargeSettings() {
        return this.batterySettings;
    }

    public Battery getRealTimeBatteryState() {
        return this.realTimeBattery;
    }

    public Grid getRealTimeGridStatus() {
        return this.grid;
    }

    public Daytemps getInverterTemperatureHistory() {
        return this.inverter_day_temperatures;
    }

    public RealTimeInData getRealtimeSolarStatus() {
        return this.realTimeDataIn;
    }

    // ------ Battery charge settings ------ //
    // https://api.sunsynk.net/api/v1/common/setting/{inverter_sn}/read

    String getCommonSettings() throws IOException {
        String response = apiGetMethod(makeURL("api/v1/common/setting/" + this.sn + "/read"),
                APIdata.static_access_token);
        if ("Failed".equals(response) | "Authentication Fail".equals(response)) {
            return response;
        }
        Gson gson = new Gson();
        this.batterySettings = gson.fromJson(response, Settings.class);
        this.batterySettings.buildLists();
        return response;
    }

    // ------ Realtime Grid ------ //
    // https://api.sunsynk.net/api/v1/inverter/grid/{inverter_sn}/realtime?sn={inverter_sn}

    String getGridRealTime() throws IOException {
        String response = apiGetMethod(makeURL("api/v1/inverter/grid/" + this.sn + "/realtime?sn=") + this.sn,
                APIdata.static_access_token);
        if ("Failed".equals(response) | "Authentication Fail".equals(response)) {
            return response;
        }
        Gson gson = new Gson();
        this.grid = gson.fromJson(response, Grid.class);
        this.grid.sumVIP();
        return response;
    }

    // ------ Realtime Battery ------ //
    // https://api.sunsynk.net/api/v1/inverter/grid/{inverter_sn}/realtime?sn={inverter_sn}

    String getBatteryRealTime() throws IOException {
        String response = apiGetMethod(
                makeURL("api/v1/inverter/battery/" + this.sn + "/realtime?sn=" + this.sn + "&lan"),
                APIdata.static_access_token);
        if ("Failed".equals(response) | "Authentication Fail".equals(response)) {
            return response;
        }
        Gson gson = new Gson();
        this.realTimeBattery = gson.fromJson(response, Battery.class);
        return response;
    }

    // ------ Realtime acdc temperatures ------ //
    // https://api.sunsynk.net/api/v1/inverter/{inverter_sn}/output/day?lan=en&date={date}&column=dc_temp,igbt_temp

    String getInverterACDCTemperatures() throws IOException {
        String date = getAPIFormatDate();
        String response = apiGetMethod(
                makeURL("api/v1/inverter/" + this.sn + "/output/day?lan=en&date=" + date + "&column=dc_temp,igbt_temp"),
                APIdata.static_access_token);
        if ("Failed".equals(response) | "Authentication Fail".equals(response)) {
            return response;
        }
        Gson gson = new Gson();
        this.inverter_day_temperatures = gson.fromJson(response, Daytemps.class);
        this.inverter_day_temperatures.getLastValue();
        return response;
    }

    // ------ Realtime Input ------ //
    // https://api.sunsynk.net/api/v1/inverter/grid/{inverter_sn}/realtime?sn={inverter_sn}

    String getRealTimeIn() throws IOException { // Get URL Respnse
        String response = apiGetMethod(makeURL("api/v1/inverter/" + this.sn + "/realtime/input"),
                APIdata.static_access_token);
        if ("Failed".equals(response) | "Authentication Fail".equals(response)) {
            return response;
        }
        Gson gson = new Gson();
        this.realTimeDataIn = gson.fromJson(response, RealTimeInData.class);
        this.realTimeDataIn.sumPVIV();
        return response;
    }
    // ------ COMMANDS ------ //
    // https://api.sunsynk.net/api/v1/common/setting/{inverter_sn}/set"

    public String sendCommandToSunSynk(String body, String access_token) {
        // GET URL
        String path = "api/v1/common/setting/" + this.sn + "/set";
        // POST COMMAND
        try {
            String postResponse = apiPostMethod(makeURL(path), body, access_token);
            return postResponse;
        } catch (Exception e) { // Don't think this will run
            logger.debug("Failed to send to Inverter API: {} ", e.getMessage());
            int found = e.getMessage().indexOf("Authentication challenge without WWW-Authenticate header");
            if (found > -1) {
                return "Authentication Fail";
            }
            return "Failed";
        }
    }

    // ------ Helpers ------ //

    private String apiPostMethod(String httpsURL, String body, String access_token) throws IOException {
        String response = "";
        Properties headers = new Properties();
        headers.setProperty("Accept", "application/json");
        headers.setProperty("Authorization", "Bearer " + access_token);
        InputStream stream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        response = HttpUtil.executeUrl("POST", httpsURL, headers, stream, "application/json", 2000);
        return response;
    }

    private String apiGetMethod(String httpsURL, String access_token) throws IOException {
        String response = "";
        Properties headers = new Properties();
        headers.setProperty("Accept", "application/json");
        headers.setProperty("Content-Type", "application/json"); // may not need this.
        headers.setProperty("Authorization", "Bearer " + access_token);
        response = HttpUtil.executeUrl("GET", httpsURL, headers, null, "application/json", 2000);
        return response;
    }

    private String makeURL(String path) {
        return "https://api.sunsynk.net" + "/" + path;
    }

    private String getAPIFormatDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter APIformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(APIformat);
        return formattedDate;
    }
}
