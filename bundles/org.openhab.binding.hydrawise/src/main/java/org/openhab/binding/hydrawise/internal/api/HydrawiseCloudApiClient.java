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
package org.openhab.binding.hydrawise.internal.api;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.hydrawise.internal.api.model.CustomerDetailsResponse;
import org.openhab.binding.hydrawise.internal.api.model.Response;
import org.openhab.binding.hydrawise.internal.api.model.SetControllerResponse;
import org.openhab.binding.hydrawise.internal.api.model.SetZoneResponse;
import org.openhab.binding.hydrawise.internal.api.model.StatusScheduleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Client for the Hydrawise API
 *
 * @author Dan Cunningham
 *
 */
public class HydrawiseCloudApiClient {
    private final Logger logger = LoggerFactory.getLogger(HydrawiseCloudApiClient.class);

    private static final String BASE_URL = "https://app.hydrawise.com/api/v1/";
    private static final String STATUS_SCHEDUE_URL = BASE_URL
            + "statusschedule.php?api_key=%s&controller_id=%d&hours=168";
    private static final String CUSTOMER_DETAILS_URL = BASE_URL + "customerdetails.php?api_key=%s&type=controllers";
    private static final String SET_CONTROLLER_URL = BASE_URL
            + "setcontroller.php?api_key=%s&controller_id=%d&json=true";
    private static final String SET_ZONE_URL = BASE_URL + "setzone.php?period_id=999";
    private static final int TIMEOUT = 30;
    private String apiKey;
    private HttpClient httpClient;
    // private Gson gson = new
    // GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private Gson gson = new GsonBuilder().create();

    /**
     * Initializes the API client with a HydraWise API key from a user's account
     *
     * @param apiKey
     */
    public HydrawiseCloudApiClient(String apiKey, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    /**
     * Retrieves the {@link StatusScheduleResponse} for a given controller
     *
     * @param controllerId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     */
    public StatusScheduleResponse getStatusSchedule(int controllerId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException {
        String json = doGet(String.format(STATUS_SCHEDUE_URL, apiKey, controllerId));
        StatusScheduleResponse response = gson.fromJson(json, StatusScheduleResponse.class);
        throwExceptionIfResponseError(response);
        return response;
    }

    /***
     * Retrieves the {@link CustomerDetailsResponse}
     *
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     */
    public CustomerDetailsResponse getCustomerDetails()
            throws HydrawiseConnectionException, HydrawiseAuthenticationException {
        String json = doGet(String.format(CUSTOMER_DETAILS_URL, apiKey));
        CustomerDetailsResponse response = gson.fromJson(json, CustomerDetailsResponse.class);
        throwExceptionIfResponseError(response);
        return response;
    }

    /***
     * Sets the controller with supplied {@value id} as the current controller
     *
     * @param id
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public SetControllerResponse setController(int id)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        String json = doGet(String.format(SET_CONTROLLER_URL, apiKey, id));
        SetControllerResponse response = gson.fromJson(json, SetControllerResponse.class);
        throwExceptionIfResponseError(response);
        if (!response.getMessage().equals("OK")) {
            throw new HydrawiseCommandException(response.getMessage());
        }
        return response;
    }

    /***
     * Stops a given relay
     *
     * @param relayId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String stopRelay(int relayId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(
                new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("stop").relayId(relayId).toString());
    }

    /**
     * Stops all relays on a given controller
     *
     * @param controllerId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String stopAllRelays(int controllerId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("stopall")
                .controllerId(controllerId).toString());
    }

    /**
     * Runs a relay for the default amount of time
     *
     * @param relayId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String runRelay(int relayId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(
                new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("run").relayId(relayId).toString());
    }

    /**
     * Runs a relay for the given amount of seconds
     *
     * @param seconds
     * @param relayId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String runRelay(int seconds, int relayId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("run").relayId(relayId)
                .duration(seconds).toString());
    }

    /**
     * Run all relays on a given controller for the default amount of time
     *
     * @param controllerId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String runAllRelays(int controllerId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("runall")
                .controllerId(controllerId).toString());
    }

    /***
     * Run all relays on a given controller for the amount of seconds
     *
     * @param seconds
     * @param controllerId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String runAllRelays(int seconds, int controllerId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("runall")
                .controllerId(controllerId).duration(seconds).toString());
    }

    /**
     * Suspends a given relay
     *
     * @param relayId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String suspendRelay(int relayId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(
                new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("suspend").relayId(relayId).toString());
    }

    /**
     * Suspends a given relay for an amount of seconds
     *
     * @param seconds
     * @param relayId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String suspendRelay(int seconds, int relayId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("suspend").relayId(relayId)
                .duration(seconds).toString());
    }

    /**
     * Suspend all relays on a given controller for an amount of seconds
     *
     * @param seconds
     * @param controllerId
     * @return
     * @throws HydrawiseConnectionException
     * @throws HydrawiseAuthenticationException
     * @throws HydrawiseCommandException
     */
    public String suspendAllRelays(int seconds, int controllerId)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        return relayCommand(new HydrawiseZoneCommandBuilder(SET_ZONE_URL, apiKey).action("suspendall")
                .controllerId(controllerId).duration(seconds).toString());
    }

    private String relayCommand(String url)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException, HydrawiseCommandException {
        String json = doGet(url);
        SetZoneResponse response = gson.fromJson(json, SetZoneResponse.class);
        throwExceptionIfResponseError(response);
        if (response.getMessageType().equals("error")) {
            throw new HydrawiseCommandException(response.getMessage());
        }
        return response.getMessage();
    }

    private String doGet(String url) throws HydrawiseConnectionException {
        logger.debug("Getting {}", url);
        ContentResponse response;
        try {
            response = httpClient.newRequest(url).method(HttpMethod.GET).timeout(TIMEOUT, TimeUnit.SECONDS).send();
        } catch (Exception e) {
            throw new HydrawiseConnectionException(e);
        }
        if (response.getStatus() != 200) {
            throw new HydrawiseConnectionException(
                    "Could not connect to Hydrawise API.  Response code " + response.getStatus());
        }
        String stringResponse = response.getContentAsString();
        logger.trace("Response: {}", stringResponse);
        return stringResponse;
    }

    private void throwExceptionIfResponseError(Response response)
            throws HydrawiseConnectionException, HydrawiseAuthenticationException {
        String error = response.getErrorMsg();
        if (error != null) {
            if (error.equalsIgnoreCase("unauthorised")) {
                throw new HydrawiseAuthenticationException();
            } else {
                throw new HydrawiseConnectionException(response.getErrorMsg());
            }
        }
    }
}
