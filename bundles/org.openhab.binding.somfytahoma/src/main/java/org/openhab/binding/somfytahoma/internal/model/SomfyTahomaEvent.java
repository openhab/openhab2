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
package org.openhab.binding.somfytahoma.internal.model;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link SomfyTahomaEvent} holds information about Tahoma
 * event.
 *
 * @author Ondrej Pecta - Initial contribution
 */
@NonNullByDefault
public class SomfyTahomaEvent {
    private String name = "";
    private String deviceURL = "";
    private String newState = "";
    private String execId = "";
    private String gatewayId = "";
    private ArrayList<SomfyTahomaState> deviceStates = new ArrayList<>();
    private JsonElement action = new JsonObject();

    public String getName() {
        return name;
    }

    public String getDeviceUrl() {
        return deviceURL;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public String getNewState() {
        return newState;
    }

    public String getExecId() {
        return execId;
    }

    public ArrayList<SomfyTahomaState> getDeviceStates() {
        return deviceStates;
    }

    public JsonElement getAction() {
        return action;
    }
}
