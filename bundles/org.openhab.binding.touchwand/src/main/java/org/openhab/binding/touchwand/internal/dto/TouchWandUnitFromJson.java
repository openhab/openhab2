/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

package org.openhab.binding.touchwand.internal.dto;

import static org.openhab.binding.touchwand.internal.TouchWandBindingConstants.*;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link TouchWandUnitFromJson} parse Json unit data
 *
 * @author Roie Geron - Initial contribution
 */
public class TouchWandUnitFromJson {

    public TouchWandUnitFromJson() {
    }

    public static TouchWandUnitData parseResponse(JsonObject JsonUnit) {
        Gson gson = new Gson();
        TouchWandUnitData touchWandUnit;
        String type = JsonUnit.get("type").getAsString();
        if (!Arrays.asList(SUPPORTED_TOUCHWAND_TYPES).contains(type)) {
            return null;
        }

        if (!JsonUnit.has("currStatus") || (JsonUnit.get("currStatus") == null)) {
            return null;
        }

        switch (type) {
            case TYPE_WALLCONTROLLER:
                touchWandUnit = gson.fromJson(JsonUnit, TouchWandUnitDataWallController.class);
                break;
            case TYPE_SWITCH:
                touchWandUnit = gson.fromJson(JsonUnit, TouchWandShutterSwitchUnitData.class);
                break;
            case TYPE_DIMMER:
                touchWandUnit = gson.fromJson(JsonUnit, TouchWandShutterSwitchUnitData.class);
                break;
            case TYPE_SHUTTER:
                touchWandUnit = gson.fromJson(JsonUnit, TouchWandShutterSwitchUnitData.class);
                break;
            default:
                return null;
        }

        return touchWandUnit;

    }

    public static TouchWandUnitData parseResponse(String JsonUnit) {
        JsonParser jsonParser = new JsonParser();
        JsonObject unitObj = jsonParser.parse(JsonUnit).getAsJsonObject();
        return parseResponse(unitObj);

    }

}
