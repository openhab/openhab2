/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The KM200ErrorService representing a error service with its all capabilities
 *
 * @author Markus Eckhardt - Initial contribution
 *
 */
public class KM200ErrorService {

    private final Logger logger = LoggerFactory.getLogger(KM200ErrorService.class);

    private Integer activeError = 1;

    /* List for all errors */
    private final ArrayList<HashMap<String, String>> errorMap;

    KM200ErrorService() {
        errorMap = new ArrayList<HashMap<String, String>>();
    }

    /**
     * This function removes all errors from the list
     *
     */
    void removeAllErrors() {
        synchronized (errorMap) {
            if (errorMap != null) {
                errorMap.clear();
            }
        }
    }

    /**
     * This function updates the errors
     *
     */
    void updateErrors(JsonObject nodeRoot) {
        synchronized (errorMap) {
            /* Update the list of errors */
            try {
                removeAllErrors();
                JsonArray sPoints = nodeRoot.get("values").getAsJsonArray();
                for (int i = 0; i < sPoints.size(); i++) {
                    JsonObject subJSON = sPoints.get(i).getAsJsonObject();
                    HashMap<String, String> valMap = new HashMap<String, String>();
                    Set<Map.Entry<String, JsonElement>> oMap = subJSON.entrySet();
                    oMap.forEach(item -> {
                        logger.debug("Set: {} val: {}", item.getKey(), item.getValue().getAsString());
                        valMap.put(item.getKey(), item.getValue().getAsString());
                    });
                    errorMap.add(valMap);
                }
            } catch (Exception e) {
                logger.error("Error in parsing of the errorlist: {}", e.getMessage());
            }
        }
    }

    /**
     * This function returns the number of errors
     *
     */
    Integer getNbrErrors() {
        synchronized (errorMap) {
            return errorMap.size();
        }
    }

    /**
     * This function sets the actual errors
     *
     */
    void setActiveError(Integer error) {
        Integer actError;
        if (error < 1) {
            actError = 1;
        } else if (error > getNbrErrors()) {
            actError = getNbrErrors();
        } else {
            actError = error;
        }
        synchronized (activeError) {
            activeError = actError;
        }
    }

    /**
     * This function returns the selected error
     *
     */
    Integer getActiveError() {
        synchronized (activeError) {
            return activeError;
        }
    }

    /**
     * This function returns a error string with all parameters
     *
     */
    String getErrorString() {
        String value = "";
        synchronized (errorMap) {
            Integer actN = getActiveError();
            if (errorMap.size() < actN || errorMap.size() == 0) {
                return null;
            }
            /* is the time value existing ("t") the use it on the begin */
            if (errorMap.get(actN - 1).containsKey("t")) {
                value = errorMap.get(actN - 1).get("t");
                for (String para : errorMap.get(actN - 1).keySet()) {
                    if (!"t".equals(para)) {
                        value += " " + para + ":" + errorMap.get(actN - 1).get(para);
                    }
                }
            } else {
                for (String para : errorMap.get(actN - 1).keySet()) {
                    value += para + ":" + errorMap.get(actN - 1).get(para) + " ";
                }
            }
            return value;
        }
    }
}
