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
package org.openhab.binding.dirigera.internal.mock;

import static org.junit.jupiter.api.Assertions.fail;
import static org.openhab.binding.dirigera.internal.Constants.PROPERTY_DEVICE_ID;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.FileReader;
import org.openhab.binding.dirigera.internal.interfaces.DirigeraAPI;
import org.openhab.binding.dirigera.internal.interfaces.Gateway;
import org.openhab.binding.dirigera.internal.interfaces.Model;
import org.openhab.core.library.types.RawType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DirigeraAPISimu} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class DirigeraAPISimu implements DirigeraAPI {
    private final Logger logger = LoggerFactory.getLogger(DirigeraAPISimu.class);
    private static JSONObject model = new JSONObject();

    public static String fileName = "src/test/resources/home/home.json";
    public static Map<String, String> patchMap = new HashMap<>();
    public static List<String> scenesAdded = new ArrayList<>();
    public static List<String> scenesDeleted = new ArrayList<>();

    public DirigeraAPISimu(HttpClient client, Gateway gateway) {
    }

    @Override
    public JSONObject readHome() {
        logger.info("read home");
        String modelString = FileReader.readFileInString(fileName);
        model = new JSONObject(modelString);
        return model;
    }

    @Override
    public JSONObject readDevice(String deviceId) {
        logger.info("read device");
        JSONObject returnObject = new JSONObject();
        if (model.has("devices")) {
            JSONArray devices = model.getJSONArray("devices");
            Iterator<Object> entries = devices.iterator();
            while (entries.hasNext()) {
                JSONObject entry = (JSONObject) entries.next();
                if (deviceId.equals(entry.get(PROPERTY_DEVICE_ID))) {
                    return entry;
                }
            }
        }
        return returnObject;
    }

    @Override
    public void triggerScene(String sceneId, String trigger) {
        logger.info("trigger scnene {}", trigger);
    }

    @Override
    public int sendAttributes(String id, JSONObject attributes) {
        JSONObject data = new JSONObject();
        data.put(Model.ATTRIBUTES, attributes);
        return sendPatch(id, data);
    }

    @Override
    public int sendPatch(String id, JSONObject attributes) {
        logger.warn("send patch {}", attributes);
        synchronized (patchMap) {
            patchMap.put(id, attributes.toString());
            patchMap.notifyAll();
        }
        return 200;
    }

    @Override
    public State getImage(String imageURL) {
        Path path = Paths.get("src/test/resources/coverart/sonos-radio-cocktail-hour.avif");
        try {
            byte[] imageData = Files.readAllBytes(path);
            return new RawType(imageData, RawType.DEFAULT_MIME_TYPE);
        } catch (IOException e) {
            fail("getting image");
        }
        return UnDefType.UNDEF;
    }

    @Override
    public JSONObject readScene(String sceneId) {
        logger.info("read scene");
        JSONObject returnObject = new JSONObject();
        if (model.has("devices")) {
            JSONArray devices = model.getJSONArray("scenes");
            Iterator<Object> entries = devices.iterator();
            while (entries.hasNext()) {
                JSONObject entry = (JSONObject) entries.next();
                if (sceneId.equals(entry.get(PROPERTY_DEVICE_ID))) {
                    return entry;
                }
            }
        }
        return returnObject;
    }

    @Override
    public String createScene(String uuid, String clickPattern, String controllerId) {
        logger.warn("createScene {} {}", uuid, clickPattern);
        scenesAdded.add(uuid);
        return uuid;
    }

    @Override
    public void deleteScene(String uuid) {
        logger.warn("deleteScene {}", uuid);
        scenesDeleted.add(uuid);
    }

    public static void waitForPatch() {
        synchronized (patchMap) {
            if (patchMap.isEmpty()) {
                try {
                    patchMap.wait(5000);
                } catch (InterruptedException e) {
                    fail();
                }
            }
        }
    }
}
