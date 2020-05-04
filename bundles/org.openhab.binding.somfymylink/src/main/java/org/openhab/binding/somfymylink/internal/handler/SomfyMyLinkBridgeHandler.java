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
package org.openhab.binding.somfymylink.internal.handler;

import static org.openhab.binding.somfymylink.internal.SomfyMyLinkBindingConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.common.NamedThreadFactory;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.StateOption;
import org.openhab.binding.somfymylink.internal.SomfyMyLinkBindingConstants;
import org.openhab.binding.somfymylink.internal.config.SomfyMyLinkConfiguration;
import org.openhab.binding.somfymylink.internal.discovery.SomfyMyLinkDeviceDiscoveryService;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandBase;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandSceneList;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandSceneSet;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandShadeDown;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandShadeList;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandShadePing;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandShadeStop;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkCommandShadeUp;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkErrorResponse;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkPingResponse;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkResponseBase;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkScene;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkScenesResponse;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkShade;
import org.openhab.binding.somfymylink.internal.model.SomfyMyLinkShadesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link SomfyMyLinkBridgeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Chris Johnson - Initial contribution
 */
@NonNullByDefault
public class SomfyMyLinkBridgeHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(SomfyMyLinkBridgeHandler.class);
    private static final int HEARTBEAT_MINUTES = 2;
    private static final int MYLINK_PORT = 44100;
    private static final int MYLINK_DEFAULT_TIMEOUT = 5000;
    private static final int CONNECTION_DELAY = 1000;
    private static final SomfyMyLinkShade[] EMPTY_SHADE_LIST = new SomfyMyLinkShade[0];
    private static final SomfyMyLinkScene[] EMPTY_SCENE_LIST = new SomfyMyLinkScene[0];

    private @Nullable SomfyMyLinkConfiguration config;
    private @Nullable ScheduledFuture<?> heartbeat;
    private @Nullable SomfyMyLinkStateDescriptionOptionsProvider stateDescriptionProvider;
    private @Nullable ExecutorService commandExecutor;

    // Gson & parser
    private final Gson gson = new Gson();

    public SomfyMyLinkBridgeHandler(Bridge bridge,
            @Nullable SomfyMyLinkStateDescriptionOptionsProvider stateDescriptionProvider) {
        super(bridge);
        this.stateDescriptionProvider = stateDescriptionProvider;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Command received on mylink {}", command);

        try {
            if (CHANNEL_SCENES.equals(channelUID.getId())) {
                if (command instanceof RefreshType) {
                    return;
                }

                if (command instanceof StringType) {
                    Integer sceneId = Integer.decode(command.toString());
                    commandScene(sceneId);
                }
            }
        } catch (SomfyMyLinkException e) {
            logger.info("Error handling command: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @Override
    public void initialize() {
        logger.debug("Initializing mylink");
        config = getThing().getConfiguration().as(SomfyMyLinkConfiguration.class);

        commandExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory(thing.getUID().getAsString(), true));

        if (validConfiguration(config)) {
            // start the keepalive process
            if (heartbeat == null) {
                logger.debug("Starting heartbeat job every {} min", HEARTBEAT_MINUTES);
                heartbeat = this.scheduler.scheduleWithFixedDelay(this::sendHeartbeat, 0, HEARTBEAT_MINUTES,
                        TimeUnit.MINUTES);
            }
        }
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(SomfyMyLinkDeviceDiscoveryService.class);
    }

    private boolean validConfiguration(@Nullable SomfyMyLinkConfiguration config) {
        if (config == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "mylink configuration missing");
            return false;
        }

        if (StringUtils.isEmpty(config.ipAddress) || StringUtils.isEmpty(config.systemId)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "mylink address or system id not specified");
            return false;
        }

        return true;
    }

    private void cancelHeartbeat() {
        logger.debug("Stopping heartbeat");
        ScheduledFuture<?> heartbeat = this.heartbeat;

        if (heartbeat != null) {
            logger.debug("Cancelling heartbeat job");
            heartbeat.cancel(true);
            this.heartbeat = null;
        } else {
            logger.debug("Heartbeat was not active");
        }
    }

    private void sendHeartbeat() {
        try {
            logger.debug("Sending heartbeat");

            SomfyMyLinkConfiguration config = this.config;
            if (config == null) {
                throw new SomfyMyLinkException("Config not setup correctly");
            }

            SomfyMyLinkCommandShadePing command = new SomfyMyLinkCommandShadePing(config.systemId);
            sendCommandWithResponse(command, SomfyMyLinkPingResponse.class).get();
            updateStatus(ThingStatus.ONLINE);

        } catch (SomfyMyLinkException | InterruptedException | ExecutionException e) {
            logger.warn("Problem with mylink during heartbeat: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    public SomfyMyLinkShade[] getShadeList() throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandShadeList command = new SomfyMyLinkCommandShadeList(config.systemId);

        try {
            SomfyMyLinkShadesResponse response = sendCommandWithResponse(command, SomfyMyLinkShadesResponse.class)
                    .get();
            if (response != null) {
                return response.getResult();
            } else {
                return EMPTY_SHADE_LIST;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new SomfyMyLinkException("Problem while getting shade list.", e);
        }
    }

    public SomfyMyLinkScene[] getSceneList() throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandSceneList command = new SomfyMyLinkCommandSceneList(config.systemId);
        SomfyMyLinkStateDescriptionOptionsProvider stateDescriptionProvider = this.stateDescriptionProvider;

        try {
            SomfyMyLinkScenesResponse response = sendCommandWithResponse(command, SomfyMyLinkScenesResponse.class)
                    .get();

            if (response != null && stateDescriptionProvider != null) {
                List<StateOption> options = new ArrayList<>();
                for (SomfyMyLinkScene scene : response.result) {
                    options.add(new StateOption(scene.getTargetID(), scene.getName()));
                }

                logger.debug("Setting {} options on bridge", options.size());

                stateDescriptionProvider.setStateOptions(
                        new ChannelUID(getThing().getUID(), SomfyMyLinkBindingConstants.CHANNEL_SCENES), options);

                return response.getResult();
            } else {
                return EMPTY_SCENE_LIST;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new SomfyMyLinkException("Problem getting scene list.", e);
        }
    }

    public void commandShadeUp(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandShadeUp cmd = new SomfyMyLinkCommandShadeUp(targetId, config.systemId);
        sendCommand(cmd);
    }

    public void commandShadeDown(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandShadeDown cmd = new SomfyMyLinkCommandShadeDown(targetId, config.systemId);
        sendCommand(cmd);
    }

    public void commandShadeStop(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandShadeStop cmd = new SomfyMyLinkCommandShadeStop(targetId, config.systemId);
        sendCommand(cmd);
    }

    public void commandScene(Integer sceneId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandSceneSet cmd = new SomfyMyLinkCommandSceneSet(sceneId, config.systemId);
        sendCommand(cmd);
    }

    private CompletableFuture<@Nullable Void> sendCommand(SomfyMyLinkCommandBase command) {
        CompletableFuture<@Nullable Void> future = new CompletableFuture<>();
        ExecutorService commandExecutor = this.commandExecutor;
        if (commandExecutor != null) {
            commandExecutor.execute(() -> {
                String json = gson.toJson(command);
                try {
                    try (Socket socket = getConnection(); OutputStream out = socket.getOutputStream()) {
                        byte[] sendBuffer = json.getBytes(StandardCharsets.US_ASCII);
                        // send the command
                        logger.debug("Sending: {}", json);
                        out.write(sendBuffer);
                        out.flush();
                        logger.debug("Sent: {}", json);
                        // give time for mylink to process
                        Thread.sleep(CONNECTION_DELAY);
                    }
                } catch (SocketTimeoutException e) {
                    logger.warn("Timeout sending command to mylink: {} Message: {}", json, e.getMessage());
                } catch (IOException e) {
                    logger.warn("Problem sending command to mylink: {} Message: {}", json, e.getMessage());
                } catch (InterruptedException e) {
                    logger.warn("Interrupted while waiting after sending command to mylink: {} Message: {}", json, e.getMessage());
                } catch (Exception e) {
                    logger.warn("Unexpected exception while sending command to mylink: {} Message: {}", json, e.getMessage());
                }
                future.complete(null);
            });
        } else {
            future.complete(null);
        }

        return future;
    }

    private <T extends SomfyMyLinkResponseBase> CompletableFuture<@Nullable T> sendCommandWithResponse(
            SomfyMyLinkCommandBase command, Class<T> responseType) throws SomfyMyLinkException {
        CompletableFuture<@Nullable T> future = new CompletableFuture<>();
        ExecutorService commandExecutor = this.commandExecutor;
        if (commandExecutor != null) {
            commandExecutor.submit(() -> {
                String json = gson.toJson(command);

                try {
                    try (Socket socket = getConnection();
                            OutputStream out = socket.getOutputStream();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII))) {
                        byte[] sendBuffer = json.getBytes(StandardCharsets.US_ASCII);

                        // send the command
                        logger.debug("Sending: {}", json);
                        out.write(sendBuffer);
                        out.flush();

                        // now read the reply
                        long startTime = System.currentTimeMillis();
                        long elapsedTime = 0L;
                        char[] readBuff = new char[1024];
                        int readCount;
                        StringBuilder message = new StringBuilder();

                        // while we are getting data ...
                        while (((readCount = in.read(readBuff)) != -1)) {
                            message.append(new String(readBuff, 0, readCount));

                            // try and parse the message
                            try {
                                T response = parseResponse(message.toString(), responseType);
                                logger.debug("Got full message: {}", message);
                                future.complete(response);
                                return;
                            } catch (SomfyMyLinkException e) {
                                // trouble parsing the message, probably incomplete
                                // if its been over 1 min waiting for a correct and full response then abort
                                elapsedTime = (new Date()).getTime() - startTime;
                                if (elapsedTime >= 60000) {
                                    future.completeExceptionally(
                                            new SomfyMyLinkException("Timeout waiting for reply from mylink"));
                                    return;
                                }
                                Thread.sleep(250); // pause for 250ms
                            }
                        }
                    }

                    // only if we didn't already get a response give time for mylink to process
                    Thread.sleep(CONNECTION_DELAY);

                    future.complete(null);
                } catch (SocketTimeoutException e) {
                    logger.warn("Timeout sending command to mylink: {} Message: {}", json, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Timeout sending command to mylink", e));
                } catch (IOException e) {
                    logger.warn("Problem sending command to mylink: {} Message: {}", json, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Problem sending command to mylink", e));
                } catch (InterruptedException e) {
                    logger.warn("Interrupted while waiting after sending command to mylink: {} Message: {}", json,
                            e.getMessage());
                    future.complete(null);
                } catch (Exception e) {
                    logger.warn("Unexpected exception while sending command to mylink: {} Message: {}", json,
                            e.getMessage());
                    future.completeExceptionally(e);
                }
            });
        } else {
            future.complete(null);
        }
        return future;
    }

    private <T extends SomfyMyLinkResponseBase> T parseResponse(String message, Class<T> responseType) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(message).getAsJsonObject();

        if (jsonObj != null && jsonObj.has("error")) {
            SomfyMyLinkErrorResponse errorResponse = gson.fromJson(jsonObj, SomfyMyLinkErrorResponse.class);
            logger.info("Error parsing mylink response: {}", errorResponse.error.message);
            throw new SomfyMyLinkException("Incomplete message.");
        }

        return gson.fromJson(jsonObj, responseType);
    }

    private Socket getConnection() throws IOException, SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;

        if (config == null) {
            throw new SomfyMyLinkException("Config not setup correctly");
        }

        try {
            logger.debug("Getting connection to mylink on: {}  Post: {}", config.ipAddress, MYLINK_PORT);
            String myLinkAddress = config.ipAddress;
            Socket socket = new Socket(myLinkAddress, MYLINK_PORT);
            socket.setSoTimeout(MYLINK_DEFAULT_TIMEOUT);
            return socket;
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            throw e;
        }
    }

    @Override
    public void thingUpdated(Thing thing) {
        SomfyMyLinkConfiguration newConfig = thing.getConfiguration().as(SomfyMyLinkConfiguration.class);
        config = newConfig;
    }

    @Override
    public void dispose() {
        cancelHeartbeat();
        dispose(commandExecutor);
    }

    private static void dispose(@Nullable ExecutorService executor) {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
