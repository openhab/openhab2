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
package org.openhab.binding.easee.internal.command.circuit;

import static org.openhab.binding.easee.internal.EaseeBindingConstants.DYNAMIC_CIRCUIT_CURRENT_URL;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.easee.internal.command.AbstractWriteCommand;
import org.openhab.binding.easee.internal.command.EaseeCommand;
import org.openhab.binding.easee.internal.handler.EaseeThingHandler;
import org.openhab.core.thing.Channel;
import org.openhab.core.types.Command;

/**
 * implements the dynamicCurrent api call of the circuit.
 *
 * @author Alexander Friese - initial contribution
 */
@NonNullByDefault
public class SetDynamicCircuitCurrents extends AbstractWriteCommand implements EaseeCommand {
    private final String PHASE1 = "phase1";
    private final String PHASE2 = "phase2";
    private final String PHASE3 = "phase3";
    private final String url;

    public SetDynamicCircuitCurrents(EaseeThingHandler handler, Channel channel, Command command, String circuitId) {
        super(handler, channel, command, RetryOnFailure.YES, ProcessFailureResponse.YES);
        String siteId = handler.getBridgeConfiguration().getSiteId();
        this.url = DYNAMIC_CIRCUIT_CURRENT_URL.replaceAll("\\{siteId\\}", siteId).replaceAll("\\{circuitId\\}",
                circuitId);
    }

    /**
     * helper that transforms channelId + commandvalue in a JSON string that can be added as content to a POST request.
     *
     * @return converted JSON string
     */
    @Override
    protected String getJsonContent() {
        Map<String, String> content = new HashMap<String, String>(3);
        String rawCommand = getCommandValue();
        String[] tokens = rawCommand.split(";");
        if (tokens.length == 3) {
            content.put(PHASE1, tokens[0]);
            content.put(PHASE2, tokens[1]);
            content.put(PHASE3, tokens[2]);
        } else {
            throw new IllegalArgumentException("malformed command string: " + rawCommand);
        }
        return gson.toJson(content);
    }

    @Override
    protected Request prepareWriteRequest(Request requestToPrepare) {
        requestToPrepare.method(HttpMethod.POST);
        StringContentProvider cp = new StringContentProvider(getJsonContent());
        requestToPrepare.content(cp);

        return requestToPrepare;
    }

    @Override
    protected String getURL() {
        return url;
    }
}
