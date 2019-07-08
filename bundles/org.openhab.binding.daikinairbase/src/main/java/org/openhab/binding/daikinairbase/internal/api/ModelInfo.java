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
package org.openhab.binding.daikinairbase.internal.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for holding the set of parameters used by get model info.
 *
 * @author Paul Smedley - Initial Contribution
 *
 */
public class ModelInfo {
    private static Logger LOGGER = LoggerFactory.getLogger(ModelInfo.class);

    public String ret;
    public Integer zonespresent;
    public Integer commonzone;

    private ModelInfo() {
    }

    public static ModelInfo parse(String response) {
        LOGGER.debug("Parsing string: \"{}\"", response);

        Map<String, String> responseMap = Arrays.asList(response.split(",")).stream().filter(kv -> kv.contains("="))
                .map(kv -> {
                    String[] keyValue = kv.split("=");
                    String key = keyValue[0];
                    String value = keyValue.length > 1 ? keyValue[1] : "";
                    return new String[] { key, value };
                }).collect(Collectors.toMap(x -> x[0], x -> x[1]));

        ModelInfo info = new ModelInfo();
        info.ret = responseMap.get("ret");
        info.zonespresent = Integer.parseInt(responseMap.get("en_zone"));
        info.commonzone = Integer.parseInt(responseMap.get("en_common_zone"));
        return info;
    }

}
