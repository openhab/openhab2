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
package org.openhab.binding.hdpowerview.internal;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.ProcessingException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.openhab.binding.hdpowerview.internal.api.ShadePosition;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeMove;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeStop;
import org.openhab.binding.hdpowerview.internal.api.responses.Scenes;
import org.openhab.binding.hdpowerview.internal.api.responses.Shade;
import org.openhab.binding.hdpowerview.internal.api.responses.Shades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * JAX-RS targets for communicating with an HD PowerView hub
 *
 * @author Andy Lintner - Initial contribution
 * @author Andrew Fiddian-Green - Added support for secondary rail positions
 */
@NonNullByDefault
public class HDPowerViewWebTargets {

    private final Logger logger = LoggerFactory.getLogger(HDPowerViewWebTargets.class);

    /*
     * the hub returns a 423 error (resource locked) daily just after midnight;
     * which means it is temporarily undergoing maintenance; so we use "soft"
     * exception handling during the five minute maintenance period after a 423
     * error is received
     */
    private final int maintenancePeriod = 300;
    private Instant maintenanceScheduledEnd = Instant.now().minusSeconds(2 * maintenancePeriod);

    private final String base;
    private final String shades;
    private final String sceneActivate;
    private final String scenes;

    private final Gson gson = new Gson();
    private final HttpClient httpClient;

    /**
     * private helper class for passing http url query parameters
     *
     * @author AndrewFG
     */
    private static class Query {
        private final String key_;
        private final String val_;

        private Query(String key, String value) {
            key_ = key;
            val_ = value;
        }

        public static Query of(String key, String value) {
            return new Query(key, value);
        }

        public String key() {
            return key_;
        }

        public String value() {
            return val_;
        }
    }

    /**
     * Initialize the web targets
     *
     * @param httpClient the HTTP client (the binding)
     * @param ipAddress the IP address of the server (the hub)
     */
    public HDPowerViewWebTargets(HttpClient httpClient, String ipAddress) {
        base = "http://" + ipAddress + "/api/";
        shades = base + "shades/";
        sceneActivate = base + "scenes";
        scenes = base + "scenes/";
        this.httpClient = httpClient;
    }

    /**
     * Fetches a JSON package that describes all shades in the hub, and wraps it in
     * a Shades class instance
     *
     * @return Shades class instance
     * @throws JsonParseException if there is a JSON parsing error
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public @Nullable Shades getShades() throws JsonParseException, ProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, shades, null, null);
        return gson.fromJson(json, Shades.class);
    }

    /**
     * Instructs the hub to move a specific shade
     *
     * @param shadeId id of the shade to be moved
     * @param position instance of ShadePosition containing the new position
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public void moveShade(int shadeId, ShadePosition position) throws ProcessingException, HubMaintenanceException {
        String json = gson.toJson(new ShadeMove(shadeId, position));
        invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, json);
    }

    /**
     * Fetches a JSON package that describes all scenes in the hub, and wraps it in
     * a Scenes class instance
     *
     * @return Scenes class instance
     * @throws JsonParseException if there is a JSON parsing error
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public @Nullable Scenes getScenes() throws JsonParseException, ProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, scenes, null, null);
        return gson.fromJson(json, Scenes.class);
    }

    /**
     * Instructs the hub to execute a specific scene
     *
     * @param sceneId id of the scene to be executed
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public void activateScene(int sceneId) throws ProcessingException, HubMaintenanceException {
        invoke(HttpMethod.GET, sceneActivate, Query.of("sceneId", Integer.toString(sceneId)), null);
    }

    /**
     * Invoke a call on the hub server to retrieve information or send a command
     *
     * @param method GET or PUT
     * @param url the host url to be called
     * @param query the http query parameter
     * @param jsonCommand the request command content (as a json string)
     * @return the response content (as a json string)
     * @throws ProcessingException
     * @throws HubMaintenanceException
     */
    private synchronized String invoke(HttpMethod method, String url, @Nullable Query query,
            @Nullable String jsonCommand) throws ProcessingException, HubMaintenanceException {
        if (logger.isTraceEnabled()) {
            logger.trace("API command {} {}", method, url);
            if (jsonCommand != null) {
                logger.trace("JSON command = {}", jsonCommand);
            }
        }
        if (!httpClient.isRunning()) {
            try {
                httpClient.start();
            } catch (Exception e) {
                throw new ProcessingException(e.getMessage());
            }
        }
        Request request = httpClient.newRequest(url).method(method).header("Connection", "close").accept("*/*");
        if (query != null) {
            request.param(query.key(), query.value());
        }
        if (jsonCommand != null) {
            request.header(HttpHeader.CONTENT_TYPE, "application/json").content(new StringContentProvider(jsonCommand));
        }
        ContentResponse response;
        try {
            response = request.send();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            if (Instant.now().isBefore(maintenanceScheduledEnd)) {
                // throw "softer" exception during maintenance window
                logger.debug("Hub still undergoing maintenance");
                throw new HubMaintenanceException("Hub still undergoing maintenance");
            }
            throw new ProcessingException(e.getMessage());
        }
        int statusCode = response.getStatus();
        if (statusCode == HttpStatus.LOCKED_423) {
            // set end of maintenance window, and throw a "softer" exception
            maintenanceScheduledEnd = Instant.now().plusSeconds(maintenancePeriod);
            logger.debug("Hub undergoing maintenance");
            throw new HubMaintenanceException("Hub undergoing maintenance");
        }
        if (statusCode != HttpStatus.OK_200) {
            logger.warn("Hub returned HTTP {} {}", statusCode, response.getReason());
            throw new ProcessingException(String.format("HTTP %d error", statusCode));
        }
        String jsonResponse = response.getContentAsString();
        if ("".equals(jsonResponse)) {
            logger.warn("Hub returned no content");
            throw new ProcessingException("Missing response entity");
        }
        if (logger.isTraceEnabled()) {
            logger.trace("JSON response = {}", jsonResponse);
        }
        return jsonResponse;
    }

    /**
     * Fetches a JSON package that describes a specific shade in the hub, and wraps it
     * in a Shade class instance
     *
     * @param shadeId id of the shade to be fetched
     * @return Shade class instance
     * @throws JsonParseException if there is a JSON parsing error
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public @Nullable Shade getShade(int shadeId)
            throws JsonParseException, ProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, shades + Integer.toString(shadeId), null, null);
        return gson.fromJson(json, Shade.class);
    }

    /**
     * Instructs the hub to do a hard refresh (discovery on the hubs RF network) on
     * a specific shade; fetches a JSON package that describes that shade, and wraps
     * it in a Shade class instance
     *
     * @param shadeId id of the shade to be refreshed
     * @return Shade class instance
     * @throws JsonParseException if there is a JSON parsing error
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public @Nullable Shade refreshShade(int shadeId)
            throws JsonParseException, ProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, shades + Integer.toString(shadeId),
                Query.of("refresh", Boolean.toString(true)), null);
        return gson.fromJson(json, Shade.class);
    }

    /**
     * Tells the hub to stop movement of a specific shade
     *
     * @param shadeId id of the shade to be stopped
     * @throws ProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    public void stopShade(int shadeId) throws ProcessingException, HubMaintenanceException {
        String json = gson.toJson(new ShadeStop(shadeId));
        invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, json);
    }
}
