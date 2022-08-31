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
package org.openhab.binding.hdpowerview.internal;

import java.lang.reflect.Type;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.hdpowerview.internal.api.Color;
import org.openhab.binding.hdpowerview.internal.api.HubFirmware;
import org.openhab.binding.hdpowerview.internal.api.ShadeData;
import org.openhab.binding.hdpowerview.internal.api.ShadeDataV1;
import org.openhab.binding.hdpowerview.internal.api.ShadePosition;
import org.openhab.binding.hdpowerview.internal.api.ShadePositionV1;
import org.openhab.binding.hdpowerview.internal.api.SurveyData;
import org.openhab.binding.hdpowerview.internal.api.UserData;
import org.openhab.binding.hdpowerview.internal.api.requests.RepeaterBlinking;
import org.openhab.binding.hdpowerview.internal.api.requests.RepeaterColor;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeCalibrate;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeJog;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeMove;
import org.openhab.binding.hdpowerview.internal.api.requests.ShadeStop;
import org.openhab.binding.hdpowerview.internal.api.responses.FirmwareVersion;
import org.openhab.binding.hdpowerview.internal.api.responses.Repeater;
import org.openhab.binding.hdpowerview.internal.api.responses.RepeaterData;
import org.openhab.binding.hdpowerview.internal.api.responses.Repeaters;
import org.openhab.binding.hdpowerview.internal.api.responses.SceneCollections;
import org.openhab.binding.hdpowerview.internal.api.responses.SceneCollections.SceneCollection;
import org.openhab.binding.hdpowerview.internal.api.responses.Scenes;
import org.openhab.binding.hdpowerview.internal.api.responses.Scenes.Scene;
import org.openhab.binding.hdpowerview.internal.api.responses.ScheduledEvent;
import org.openhab.binding.hdpowerview.internal.api.responses.ScheduledEventV1;
import org.openhab.binding.hdpowerview.internal.api.responses.ScheduledEvents;
import org.openhab.binding.hdpowerview.internal.api.responses.Shades;
import org.openhab.binding.hdpowerview.internal.api.responses.Survey;
import org.openhab.binding.hdpowerview.internal.api.responses.UserDataResponse;
import org.openhab.binding.hdpowerview.internal.exceptions.HubInvalidResponseException;
import org.openhab.binding.hdpowerview.internal.exceptions.HubMaintenanceException;
import org.openhab.binding.hdpowerview.internal.exceptions.HubProcessingException;
import org.openhab.binding.hdpowerview.internal.exceptions.HubShadeTimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * JAX-RS targets for communicating with an HD PowerView hub Generation 1/2
 *
 * @author Andy Lintner - Initial contribution
 * @author Andrew Fiddian-Green - Added support for secondary rail positions
 * @author Jacob Laursen - Added support for scene groups and automations
 */
@NonNullByDefault
public class HDPowerViewWebTargetsV1 extends HDPowerViewWebTargets {

    private final String firmwareVersion;
    private final String shades;
    private final String sceneActivate;
    private final String scenes;
    private final String sceneCollectionActivate;
    private final String sceneCollections;
    private final String scheduledEvents;
    private final String repeaters;
    private final String userData;

    /**
     * Initialize the web targets
     *
     * @param httpClient the HTTP client (the binding)
     * @param ipAddress the IP address of the server (the hub)
     */
    public HDPowerViewWebTargetsV1(HttpClient httpClient, String ipAddress) {
        super(httpClient, ipAddress);

        String base = "http://" + ipAddress + "/api/";

        shades = base + "shades/";
        firmwareVersion = base + "fwversion";
        sceneActivate = base + "scenes";
        scenes = base + "scenes/";

        // Hub v1 only supports "scenecollections". Hub v2 will redirect to "sceneCollections".
        sceneCollectionActivate = base + "scenecollections";
        sceneCollections = base + "scenecollections/";

        scheduledEvents = base + "scheduledevents";
        repeaters = base + "repeaters/";
        userData = base + "userdata";

    }

    /**
     * Fetches a JSON package with firmware information for the hub.
     *
     * @return FirmwareVersions class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public HubFirmware getFirmwareVersions()
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, firmwareVersion, null, null);
        try {
            FirmwareVersion firmwareVersion = gson.fromJson(json, FirmwareVersion.class);
            if (firmwareVersion == null) {
                throw new HubInvalidResponseException("Missing firmware response");
            }
            HubFirmware firmwareVersions = firmwareVersion.firmware;
            if (firmwareVersions == null) {
                throw new HubInvalidResponseException("Missing 'firmware' element");
            }
            return firmwareVersions;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing firmware response", e);
        }
    }

    /**
     * Fetches a JSON package with user data information for the hub.
     *
     * @return {@link UserData} class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public UserData getUserData() throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, userData, null, null);
        try {
            UserDataResponse userDataResponse = gson.fromJson(json, UserDataResponse.class);
            if (userDataResponse == null) {
                throw new HubInvalidResponseException("Missing userData response");
            }
            UserData userData = userDataResponse.userData;
            if (userData == null) {
                throw new HubInvalidResponseException("Missing 'userData' element");
            }
            return userData;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing userData response", e);
        }
    }

    /**
     * Fetches a JSON package that describes all shades in the hub, and wraps it in
     * a Shades class instance
     *
     * @return Shades class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public Shades getShades() throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, shades, null, null);
        try {
            Shades shades = gson.fromJson(json, Shades.class);
            if (shades == null) {
                throw new HubInvalidResponseException("Missing shades response");
            }
            List<ShadeData> shadeData = shades.shadeData;
            if (shadeData == null) {
                throw new HubInvalidResponseException("Missing 'shades.shadeData' element");
            }
            return shades;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing shades response", e);
        }
    }

    /**
     * Instructs the hub to move a specific shade
     *
     * @param shadeId id of the shade to be moved
     * @param position instance of ShadePosition containing the new position
     * @return ShadeData class instance (with new position)
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData moveShade(int shadeId, ShadePosition position) throws HubInvalidResponseException,
            HubProcessingException, HubMaintenanceException, HubShadeTimeoutException {
        String jsonRequest = gson.toJson(new ShadeMove(position));
        String jsonResponse = invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, jsonRequest);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Instructs the hub to stop movement of a specific shade
     *
     * @param shadeId id of the shade to be stopped
     * @return ShadeData class instance (new position cannot be relied upon)
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData stopShade(int shadeId) throws HubInvalidResponseException, HubProcessingException,
            HubMaintenanceException, HubShadeTimeoutException {
        String jsonRequest = gson.toJson(new ShadeStop());
        String jsonResponse = invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, jsonRequest);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Instructs the hub to jog a specific shade
     *
     * @param shadeId id of the shade to be jogged
     * @return ShadeData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData jogShade(int shadeId) throws HubInvalidResponseException, HubProcessingException,
            HubMaintenanceException, HubShadeTimeoutException {
        String jsonRequest = gson.toJson(new ShadeJog());
        String jsonResponse = invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, jsonRequest);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Instructs the hub to calibrate a specific shade
     *
     * @param shadeId id of the shade to be calibrated
     * @return ShadeData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData calibrateShade(int shadeId) throws HubInvalidResponseException, HubProcessingException,
            HubMaintenanceException, HubShadeTimeoutException {
        String jsonRequest = gson.toJson(new ShadeCalibrate());
        String jsonResponse = invoke(HttpMethod.PUT, shades + Integer.toString(shadeId), null, jsonRequest);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Fetches a JSON package that describes all scenes in the hub, and wraps it in
     * a Scenes class instance
     *
     * @return Scenes class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public Scenes getScenes() throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, scenes, null, null);
        try {
            Scenes scenes = gson.fromJson(json, Scenes.class);
            if (scenes == null) {
                throw new HubInvalidResponseException("Missing scenes response");
            }
            List<Scene> sceneData = scenes.sceneData;
            if (sceneData == null) {
                throw new HubInvalidResponseException("Missing 'scenes.sceneData' element");
            }
            return scenes;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing scenes response", e);
        }
    }

    /**
     * Instructs the hub to execute a specific scene
     *
     * @param sceneId id of the scene to be executed
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public void activateScene(int sceneId) throws HubProcessingException, HubMaintenanceException {
        invoke(HttpMethod.GET, sceneActivate, Query.of("sceneId", Integer.toString(sceneId)), null);
    }

    /**
     * Fetches a JSON package that describes all scene collections in the hub, and wraps it in
     * a SceneCollections class instance
     *
     * @return SceneCollections class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public SceneCollections getSceneCollections()
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, sceneCollections, null, null);
        try {
            SceneCollections sceneCollections = gson.fromJson(json, SceneCollections.class);
            if (sceneCollections == null) {
                throw new HubInvalidResponseException("Missing sceneCollections response");
            }
            List<SceneCollection> sceneCollectionData = sceneCollections.sceneCollectionData;
            if (sceneCollectionData == null) {
                throw new HubInvalidResponseException("Missing 'sceneCollections.sceneCollectionData' element");
            }
            return sceneCollections;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing sceneCollections response", e);
        }
    }

    /**
     * Instructs the hub to execute a specific scene collection
     *
     * @param sceneCollectionId id of the scene collection to be executed
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public void activateSceneCollection(int sceneCollectionId) throws HubProcessingException, HubMaintenanceException {
        invoke(HttpMethod.GET, sceneCollectionActivate,
                Query.of("sceneCollectionId", Integer.toString(sceneCollectionId)), null);
    }

    /**
     * Fetches a JSON package that describes all scheduled events in the hub, and wraps it in
     * a ScheduledEvents class instance
     *
     * @return ScheduledEvents class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public ScheduledEvents getScheduledEvents()
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, scheduledEvents, null, null);
        try {
            ScheduledEvents scheduledEvents = gson.fromJson(json, ScheduledEvents.class);
            if (scheduledEvents == null) {
                throw new HubInvalidResponseException("Missing scheduledEvents response");
            }
            List<ScheduledEvent> scheduledEventData = scheduledEvents.scheduledEventData;
            if (scheduledEventData == null) {
                throw new HubInvalidResponseException("Missing 'scheduledEvents.scheduledEventData' element");
            }
            return scheduledEvents;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing scheduledEvents response", e);
        }
    }

    /**
     * Enables or disables a scheduled event in the hub.
     *
     * @param scheduledEventId id of the scheduled event to be enabled or disabled
     * @param enable true to enable scheduled event, false to disable
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public void enableScheduledEvent(int scheduledEventId, boolean enable)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String uri = scheduledEvents + "/" + scheduledEventId;
        String jsonResponse = invoke(HttpMethod.GET, uri, null, null);
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonElement scheduledEventElement = jsonObject.get("scheduledEvent");
            if (scheduledEventElement == null) {
                throw new HubInvalidResponseException("Missing 'scheduledEvent' element");
            }
            JsonObject scheduledEventObject = scheduledEventElement.getAsJsonObject();
            scheduledEventObject.addProperty("enabled", enable);
            invoke(HttpMethod.PUT, uri, null, jsonObject.toString());
        } catch (JsonParseException | IllegalStateException e) {
            throw new HubInvalidResponseException("Error parsing scheduledEvent response", e);
        }
    }

    /**
     * Fetches a JSON package that describes all repeaters in the hub, and wraps it in
     * a Repeaters class instance
     *
     * @return Repeaters class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public Repeaters getRepeaters()
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String json = invoke(HttpMethod.GET, repeaters, null, null);
        try {
            Repeaters repeaters = gson.fromJson(json, Repeaters.class);
            if (repeaters == null) {
                throw new HubInvalidResponseException("Missing repeaters response");
            }
            List<RepeaterData> repeaterData = repeaters.repeaterData;
            if (repeaterData == null) {
                throw new HubInvalidResponseException("Missing 'repeaters.repeaterData' element");
            }
            return repeaters;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing repeaters response", e);
        }
    }

    /**
     * Fetches a JSON package that describes a specific repeater in the hub, and wraps it
     * in a RepeaterData class instance
     *
     * @param repeaterId id of the repeater to be fetched
     * @return RepeaterData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public RepeaterData getRepeater(int repeaterId)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String jsonResponse = invoke(HttpMethod.GET, repeaters + Integer.toString(repeaterId), null, null);
        return repeaterDataFromJson(jsonResponse);
    }

    private RepeaterData repeaterDataFromJson(String json) throws HubInvalidResponseException {
        try {
            Repeater repeater = gson.fromJson(json, Repeater.class);
            if (repeater == null) {
                throw new HubInvalidResponseException("Missing repeater response");
            }
            RepeaterData repeaterData = repeater.repeater;
            if (repeaterData == null) {
                throw new HubInvalidResponseException("Missing 'repeater.repeater' element");
            }
            return repeaterData;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing repeater response", e);
        }
    }

    /**
     * Instructs the hub to identify a specific repeater by blinking
     *
     * @param repeaterId id of the repeater to be identified
     * @return RepeaterData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public RepeaterData identifyRepeater(int repeaterId)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String jsonResponse = invoke(HttpMethod.GET, repeaters + repeaterId,
                Query.of("identify", Boolean.toString(true)), null);
        return repeaterDataFromJson(jsonResponse);
    }

    /**
     * Enables or disables blinking for a repeater
     *
     * @param repeaterId id of the repeater for which to be enable or disable blinking
     * @param enable true to enable blinking, false to disable
     * @return RepeaterData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public RepeaterData enableRepeaterBlinking(int repeaterId, boolean enable)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String jsonRequest = gson.toJson(new RepeaterBlinking(repeaterId, enable));
        String jsonResponse = invoke(HttpMethod.PUT, repeaters + repeaterId, null, jsonRequest);
        return repeaterDataFromJson(jsonResponse);
    }

    /**
     * Sets color and brightness for a repeater
     *
     * @param repeaterId id of the repeater for which to set color and brightness
     * @return RepeaterData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public RepeaterData setRepeaterColor(int repeaterId, Color color)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String jsonRequest = gson.toJson(new RepeaterColor(repeaterId, color));
        String jsonResponse = invoke(HttpMethod.PUT, repeaters + repeaterId, null, jsonRequest);
        return repeaterDataFromJson(jsonResponse);
    }

    /**
     * Fetches a JSON package that describes a specific shade in the hub, and wraps it
     * in a Shade class instance
     *
     * @param shadeId id of the shade to be fetched
     * @return ShadeData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData getShade(int shadeId) throws HubInvalidResponseException, HubProcessingException,
            HubMaintenanceException, HubShadeTimeoutException {
        String jsonResponse = invoke(HttpMethod.GET, shades + Integer.toString(shadeId), null, null);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Instructs the hub to do a hard refresh (discovery on the hubs RF network) on
     * a specific shade's position; fetches a JSON package that describes that shade,
     * and wraps it in a Shade class instance
     *
     * @param shadeId id of the shade to be refreshed
     * @return ShadeData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData refreshShadePosition(int shadeId)
            throws JsonParseException, HubProcessingException, HubMaintenanceException, HubShadeTimeoutException {
        String jsonResponse = invoke(HttpMethod.GET, shades + Integer.toString(shadeId),
                Query.of("refresh", Boolean.toString(true)), null);
        return shadeDataFromJson(jsonResponse);
    }

    /**
     * Instructs the hub to do a hard refresh (discovery on the hubs RF network) on
     * a specific shade's survey data, which will also refresh signal strength;
     * fetches a JSON package that describes that survey, and wraps it in a Survey
     * class instance
     *
     * @param shadeId id of the shade to be surveyed
     * @return List of SurveyData class instances
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     */
    @Override
    public List<SurveyData> getShadeSurvey(int shadeId)
            throws HubInvalidResponseException, HubProcessingException, HubMaintenanceException {
        String jsonResponse = invoke(HttpMethod.GET, shades + Integer.toString(shadeId),
                Query.of("survey", Boolean.toString(true)), null);
        try {
            Survey survey = gson.fromJson(jsonResponse, Survey.class);
            if (survey == null) {
                throw new HubInvalidResponseException("Missing survey response");
            }
            List<SurveyData> surveyData = survey.surveyData;
            if (surveyData == null) {
                throw new HubInvalidResponseException("Missing 'survey.surveyData' element");
            }
            return surveyData;
        } catch (JsonParseException e) {
            throw new HubInvalidResponseException("Error parsing survey response", e);
        }
    }

    /**
     * Instructs the hub to do a hard refresh (discovery on the hubs RF network) on
     * a specific shade's battery level; fetches a JSON package that describes that shade,
     * and wraps it in a Shade class instance
     *
     * @param shadeId id of the shade to be refreshed
     * @return ShadeData class instance
     * @throws HubInvalidResponseException if response is invalid
     * @throws HubProcessingException if there is any processing error
     * @throws HubMaintenanceException if the hub is down for maintenance
     * @throws HubShadeTimeoutException if the shade did not respond to a request
     */
    @Override
    public ShadeData refreshShadeBatteryLevel(int shadeId) throws HubInvalidResponseException, HubProcessingException,
            HubMaintenanceException, HubShadeTimeoutException {
        String jsonResponse = invoke(HttpMethod.GET, shades + Integer.toString(shadeId),
                Query.of("updateBatteryLevel", Boolean.toString(true)), null);
        return shadeDataFromJson(jsonResponse);
    }

    private static class ShadeDataDeserializer implements JsonDeserializer<ShadeData> {
        @Override
        public @Nullable ShadeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return context.deserialize(jsonObject, ShadeDataV1.class);
        }
    }

    private static class ShadePositionDeserializer implements JsonDeserializer<ShadePosition> {
        @Override
        public @Nullable ShadePosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return context.deserialize(jsonObject, ShadePositionV1.class);
        }
    }

    private static class ScheduledEventDeserializer implements JsonDeserializer<ScheduledEvent> {
        @Override
        public @Nullable ScheduledEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return context.deserialize(jsonObject, ScheduledEventV1.class);
        }
    }

    @Override
    protected Gson getGsonObject() {
        return getGson();
    }

    /**
     * Public static method to get Gson object. e.g. used for JUnit testing.
     *
     * @return an instance of the Gson class.
     */
    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(ShadeData.class, new ShadeDataDeserializer())
                .registerTypeAdapter(ShadePosition.class, new ShadePositionDeserializer())
                .registerTypeAdapter(ScheduledEvent.class, new ScheduledEventDeserializer()).create();
    }
}
