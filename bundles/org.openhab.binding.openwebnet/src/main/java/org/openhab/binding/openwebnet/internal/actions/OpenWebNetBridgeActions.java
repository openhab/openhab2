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
package org.openhab.binding.openwebnet.internal.actions;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openwebnet.internal.handler.OpenWebNetBridgeHandler;
import org.openhab.core.automation.annotation.ActionInput;
import org.openhab.core.automation.annotation.RuleAction;
import org.openhab.core.thing.binding.ThingActions;
import org.openhab.core.thing.binding.ThingActionsScope;
import org.openhab.core.thing.binding.ThingHandler;
import org.openwebnet4j.OpenGateway;
import org.openwebnet4j.communication.OWNException;
import org.openwebnet4j.communication.Response;
import org.openwebnet4j.message.FrameException;
import org.openwebnet4j.message.OpenMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenWebNetBridgeActions} defines the thing actions for the openwebnet binding.
 *
 * @author Massimo Valla - Initial contribution
 */

@ThingActionsScope(name = "openwebnet")
@NonNullByDefault
public class OpenWebNetBridgeActions implements ThingActions {

    private final Logger logger = LoggerFactory.getLogger(OpenWebNetBridgeActions.class);
    private @Nullable OpenWebNetBridgeHandler bridgeHandler;

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        this.bridgeHandler = (OpenWebNetBridgeHandler) handler;
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return bridgeHandler;
    }

    @RuleAction(label = "sendMessage", description = "Sends a message to the gateway")
    public boolean sendMessage(
            @ActionInput(name = "message", label = "message", description = "The message to send") @Nullable String message) {
        OpenWebNetBridgeHandler handler = bridgeHandler;
        if (handler == null) {
            logger.warn("openwebnet Action service ThingHandler is null!");
            return false;
        }
        OpenMessage msg;
        try {
            msg = org.openwebnet4j.message.BaseOpenMessage.parse(message);
        } catch (FrameException e) {
            logger.warn("Skipping openwebnet sendMessage: message '{}' is invalid.", message);
            return false;
        }
        OpenGateway gw = handler.getGateway();
        if (gw != null && gw.isConnected()) {
            try {
                Response res = gw.send(msg);
                logger.debug("Sent message '{}' to gateway. Response: {}", msg, res.getResponseMessages());
                return res.isSuccess();
            } catch (OWNException e) {
                logger.warn("Exception while sending message '{}' to gateway: {}", msg, e.getMessage());
                return false;
            }
        } else {
            logger.warn("Skipping openwebnet sendMessage for bridge {}: gateway is not connected.",
                    handler.getThing().getUID());
            return false;
        }

        // If you return values, you do so by returning a Map<String,Object> and annotate the method itself with as many
        // @ActionOutputs as you will return map entries.
    }

    // legacy delegate methods
    public static void sendMessage(@Nullable ThingActions actions, @Nullable String message) {
        if (actions instanceof OpenWebNetBridgeActions) {
            ((OpenWebNetBridgeActions) actions).sendMessage(message);
        } else {
            throw new IllegalArgumentException("Instance is not an OpenWebNetBridgeActions class.");
        }
    }
}
