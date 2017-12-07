/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 *
 */
package org.openhab.binding.icloud.internal;

import org.openhab.binding.icloud.internal.json.icloud.JSONRootObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Extracts iCloud device information from a given JSON string
 *
 * @author Patrik Gfeller - Initial Contribution
 *
 */
public class DeviceInformationParser {
    private final Logger logger = LoggerFactory.getLogger(DeviceInformationParser.class);
    public JSONRootObject data;

    public DeviceInformationParser(String json) {
        try {
            Gson gson = new GsonBuilder().create();
            data = gson.fromJson(json, JSONRootObject.class);
        } catch (Exception e) {
            logger.warn("Unable to parse json {}", json, e);
        }
    }
}
