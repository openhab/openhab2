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
package org.openhab.binding.juicenet.internal.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.openhab.binding.juicenet.internal.api.dto.JuiceNetApiDevice;
import org.openhab.binding.juicenet.internal.api.dto.JuiceNetApiDeviceStatus;
import org.openhab.binding.juicenet.internal.api.dto.JuiceNetApiInfo;
import org.openhab.binding.juicenet.internal.api.dto.JuiceNetApiTouSchedule;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link JuiceNetApi} is responsible for implementing the api interface to the JuiceNet cloud server
 *
 * @author Jeff James - Initial contribution
 */
@NonNullByDefault
public class JuiceNetApi {
    private final Logger logger = LoggerFactory.getLogger(JuiceNetApi.class);

    private static final String API_HOST = "https://jbv1-api.emotorwerks.com/";
    private static final String API_ACCOUNT = API_HOST + "box_pin";
    private static final String API_DEVICE = API_HOST + "box_api_secure";

    protected String apiToken = "";
    protected JuiceNetHttp httpApi;
    @Nullable
    protected ThingUID bridgeUID;

    public JuiceNetApi(HttpClient httpClient) {
        this.httpApi = new JuiceNetHttp(httpClient);
    }

    public void initialize(String apiToken, ThingUID bridgeUID) {
        this.apiToken = apiToken;
        this.bridgeUID = bridgeUID;
    }

    public List<JuiceNetApiDevice> queryDeviceList()
            throws JuiceNetApiException, IOException, InterruptedException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "get_account_units");
        params.put("device_id", bridgeUID.getAsString());
        params.put("account_token", apiToken);

        response = httpApi.httpPost(API_ACCOUNT, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException("Unable to retrieve device list, please check configuration (HTTP code: "
                    + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());

        JsonObject jsonResponse = JsonParser.parseString(response.getContentAsString()).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();

        if (!success) {
            throw new JuiceNetApiException("getDevices from JuiceNet failed, please check configuration.");
        }

        final JuiceNetApiDevice[] listDevices = new Gson().fromJson(jsonResponse.get("units").getAsJsonArray(),
                JuiceNetApiDevice[].class);

        return Arrays.asList(listDevices);
    }

    public JuiceNetApiDeviceStatus queryDeviceStatus(String token)
            throws JuiceNetApiException, IOException, InterruptedException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "get_state");
        params.put("account_token", apiToken);
        params.put("device_id", bridgeUID.getAsString());
        params.put("token", token);

        response = httpApi.httpPost(API_DEVICE, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException("Unable to retrieve device status, please check configuration (HTTP code: "
                    + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());

        JsonObject jsonResponse = JsonParser.parseString(response.getContentAsString()).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();

        if (!success) {
            throw new JuiceNetApiException("getDeviceStatus from JuiceNet failed, please check configuration.");
        }

        final JuiceNetApiDeviceStatus deviceStatus = new Gson().fromJson(jsonResponse, JuiceNetApiDeviceStatus.class);

        return Objects.requireNonNull(deviceStatus);
    }

    public JuiceNetApiInfo queryInfo(String token)
            throws IOException, InterruptedException, JuiceNetApiException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "get_info");
        params.put("account_token", apiToken);
        params.put("device_id", bridgeUID.getAsString());
        params.put("token", token);

        response = httpApi.httpPost(API_DEVICE, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException("Unable to retrieve device info, please check configuration (HTTP code: "
                    + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());

        final JuiceNetApiInfo info = new Gson().fromJson(response.getContentAsString(), JuiceNetApiInfo.class);

        return Objects.requireNonNull(info);
    }

    public JuiceNetApiTouSchedule queryTOUSchedule(String token)
            throws IOException, InterruptedException, JuiceNetApiException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "get_schedule");
        params.put("account_token", apiToken);
        params.put("device_id", bridgeUID.getAsString());
        params.put("token", token);

        response = httpApi.httpPost(API_DEVICE, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException(
                    "Unable to retrieve device TOU Schedule, please check configuration (HTTP code: "
                            + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());

        JsonObject jsonResponse = JsonParser.parseString(response.getContentAsString()).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();

        if (!success) {
            throw new JuiceNetApiException("get_schedule from JuiceNet failed, please check configuration.");
        }

        final JuiceNetApiTouSchedule deviceTouSchedule = new Gson().fromJson(jsonResponse,
                JuiceNetApiTouSchedule.class);

        return Objects.requireNonNull(deviceTouSchedule);
    }

    public void setOverride(String token, int energy_at_plugin, Long override_time, int energy_to_add)
            throws IOException, InterruptedException, JuiceNetApiException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "set_override");
        params.put("account_token", apiToken);
        params.put("device_id", bridgeUID.getAsString());
        params.put("token", token);
        params.put("energy_at_plugin", energy_at_plugin);
        params.put("override_time", override_time);
        params.put("energy_to_add", energy_to_add);

        response = httpApi.httpPost(API_DEVICE, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException(
                    "Unable to set Override, please check configuration (HTTP code: " + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());
    }

    public void setCurrentLimit(String token, int limit)
            throws IOException, InterruptedException, JuiceNetApiException, TimeoutException, ExecutionException {
        Map<String, Object> params = new HashMap<>();
        ContentResponse response;

        params.put("cmd", "set_limit");
        params.put("account_token", apiToken);
        params.put("device_id", bridgeUID.getAsString());
        params.put("token", token);
        params.put("amperage", limit);

        response = httpApi.httpPost(API_DEVICE, params);

        if (response.getStatus() != HttpStatus.OK_200) {
            throw new JuiceNetApiException("Unable to set current limit, please check configuration (HTTP code: "
                    + response.getStatus() + ").");
        }

        logger.trace("{}", response.getContentAsString());
    }
}
