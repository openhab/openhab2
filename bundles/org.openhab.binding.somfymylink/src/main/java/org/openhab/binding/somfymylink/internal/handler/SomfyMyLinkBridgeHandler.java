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
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.common.NamedThreadFactory;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
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
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
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

    private @Nullable SomfyMyLinkConfiguration config;

    private static final int HEARTBEAT_MINUTES = 2;
    private static final int MYLINK_PORT = 44100;
    private static final int MYLINK_DEFAULT_TIMEOUT = 5000;
    private static final int CONNECTION_DELAY = 1000;
    private static final SomfyMyLinkShade[] EMPTY_SHADE_LIST = new SomfyMyLinkShade[0];

    private @Nullable ScheduledFuture<?> heartbeat;

    private @Nullable ServiceRegistration<DiscoveryService> discoveryServiceRegistration;

    private SomfyMyLinkDeviceDiscoveryService discovery;

    private @Nullable SomfyMyLinkStateDescriptionOptionsProvider stateDescriptionProvider;

    private @Nullable ExecutorService commandExecutor;

    // Gson & parser
    private final Gson gson = new Gson();

    public SomfyMyLinkBridgeHandler(Bridge bridge,
            @Nullable SomfyMyLinkStateDescriptionOptionsProvider stateDescriptionProvider) {
        super(bridge);

        this.discovery = new SomfyMyLinkDeviceDiscoveryService(this);
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
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @Override
    public void initialize() {
        logger.debug("Initializing mylink");
        config = getThing().getConfiguration().as(SomfyMyLinkConfiguration.class);

        commandExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory(thing.getUID().getAsString(), true));

        if (validConfiguration(config)) {
            this.discoveryServiceRegistration = FrameworkUtil.getBundle(SomfyMyLinkBridgeHandler.class)
                    .getBundleContext().registerService(DiscoveryService.class, this.discovery, null);
            this.discovery.activate(null);

            // kick off the bridge connection process
            this.scheduler.execute(this::connect);
        }
    }

    private boolean validConfiguration(@Nullable SomfyMyLinkConfiguration config) {
        if (config == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "mylink configuration missing");
            return false;
        }

        if (StringUtils.isEmpty(config.ipAddress)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "mylink address not specified");
            return false;
        }

        return true;
    }

    private void connect() {
        try {
            SomfyMyLinkConfiguration config = this.config;

            if (config == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "mylink config not specified");
                return;
            }

            if (StringUtils.isEmpty(config.ipAddress) || StringUtils.isEmpty(config.systemId)) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "mylink config not specified");
                return;
            }

            // start the keepalive process
            ensureKeepAlive();

            logger.debug("Connecting to mylink at {}", config.ipAddress);

            // send a ping
            sendPing();

            logger.debug("Connected to mylink at {}", config.ipAddress);

            updateStatus(ThingStatus.ONLINE);
        } catch (SomfyMyLinkException e) {
            logger.debug("Problem connecting to mylink, bridge OFFLINE");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    private void ensureKeepAlive() {
        if (heartbeat == null) {
            logger.debug("Starting keepalive job in {} min, every {} min", HEARTBEAT_MINUTES, HEARTBEAT_MINUTES);
            heartbeat = this.scheduler.scheduleWithFixedDelay(this::sendKeepAlive, 1, 1, TimeUnit.MINUTES);
        }
    }

    private void disconnect() {
        logger.debug("Disconnecting from mylink");
        ScheduledFuture<?> heartbeat = this.heartbeat;

        if (heartbeat != null) {
            logger.debug("Cancelling keepalive job");
            heartbeat.cancel(true);
            heartbeat = null;
        } else {
            logger.debug("Keepalive was not active");
        }
    }

    private void sendKeepAlive() {
        try {
            logger.debug("Keep alive triggered");

            if (getThing().getStatus() != ThingStatus.ONLINE) {
                // try connecting
                logger.debug("Bridge offline, trying to connect");
                connect();
            } else {
                // send a ping
                sendPing();
                logger.debug("Keep alive succeeded");
            }
        } catch (SomfyMyLinkException e) {
            logger.debug("Problem pinging mylink during keepalive");
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
            SomfyMyLinkShadesResponse response = sendCommandWithResponse(command, SomfyMyLinkShadesResponse.class).get();
            if (response != null) {
                return response.getResult();
            } else {
                return EMPTY_SHADE_LIST;
            }
        }
        catch (Exception e) {
            logger.debug("Exception while getting shade list. Message: {}", e.getMessage());
            return EMPTY_SHADE_LIST;
        }
    }

    public void sendPing() throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        SomfyMyLinkCommandShadePing command = new SomfyMyLinkCommandShadePing(config.systemId);

        try {
            sendCommandWithResponse(command, SomfyMyLinkPingResponse.class).get();
        } 
        catch (InterruptedException e) {
            throw new SomfyMyLinkException("Problem pinging.", e);
        } catch (ExecutionException e) {
            throw new SomfyMyLinkException("Problem pinging.", e);
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
            SomfyMyLinkScenesResponse response = sendCommandWithResponse(command,
                    SomfyMyLinkScenesResponse.class).get();

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
                return new SomfyMyLinkScene[0];
            }
        }
        catch (InterruptedException e) {
            logger.debug("Exception while getting scene list. Message: {}", e.getMessage());
            throw new SomfyMyLinkException("Problem getting scene list.", e);
        }
        catch (ExecutionException e) {
            logger.debug("Exception while getting scene list. Message: {}", e.getMessage());
            throw new SomfyMyLinkException("Problem getting scene list.", e);
        }
    }

    public void commandShadeUp(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        try {
            SomfyMyLinkCommandShadeUp cmd = new SomfyMyLinkCommandShadeUp(targetId, config.systemId);
            sendCommand(cmd);
        } catch (SomfyMyLinkException e) {
            logger.info("Error commanding shade up: {}", e.getMessage());
            throw new SomfyMyLinkException("Error commanding shade up", e);
        }
    }

    public void commandShadeDown(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        try {
            SomfyMyLinkCommandShadeDown cmd = new SomfyMyLinkCommandShadeDown(targetId, config.systemId);
            sendCommand(cmd);
        } catch (SomfyMyLinkException e) {
            logger.info("Error commanding shade down: {}", e.getMessage());
            throw new SomfyMyLinkException("Error commanding shade down", e);
        }
    }

    public void commandShadeStop(String targetId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }

        try {
            SomfyMyLinkCommandShadeStop cmd = new SomfyMyLinkCommandShadeStop(targetId, config.systemId);
            sendCommand(cmd);
        } catch (SomfyMyLinkException e) {
            logger.info("Error commanding shade stop: {}", e.getMessage());
            throw new SomfyMyLinkException("Error commanding shade stop", e);
        }
    }

    public void commandScene(Integer sceneId) throws SomfyMyLinkException {
        SomfyMyLinkConfiguration config = this.config;
        if (config == null || StringUtils.isEmpty(config.systemId)) {
            throw new SomfyMyLinkException("Config not setup correctly. System Id is not set.");
        }
        
        try {
            SomfyMyLinkCommandSceneSet cmd = new SomfyMyLinkCommandSceneSet(sceneId, config.systemId);
            sendCommand(cmd);
        } catch (SomfyMyLinkException e) {
            logger.info("Error commanding scene run: {}", e.getMessage());
            throw new SomfyMyLinkException("Error commanding scene run", e);
        }
    }

    private CompletableFuture<@Nullable Void> sendCommand(SomfyMyLinkCommandBase command) {
        String json = gson.toJson(command);
        return sendCommand(json);
    }

    private CompletableFuture<@Nullable Void> sendCommand(String command) {
        CompletableFuture<@Nullable Void> future = new CompletableFuture<>();
        ExecutorService commandExecutor = this.commandExecutor;
        if (commandExecutor != null) {
            commandExecutor.execute(() -> {
                try {
                    try (Socket socket = getConnection(); OutputStream out = socket.getOutputStream()) {
                        byte[] sendBuffer = command.getBytes(StandardCharsets.US_ASCII);
                        // send the command
                        logger.debug("Sending: {}", command);
                        out.write(sendBuffer);
                        out.flush();
                        logger.debug("Sent: {}", command);
                    }
                    // give time for mylink to process
                    Thread.sleep(CONNECTION_DELAY);
                } catch (SocketTimeoutException e) {
                    logger.warn("Timeout sending command to mylink: {} Message: {}", command, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Timeout sending command to mylink", e));
                } catch (IOException e) {
                    logger.warn("Problem sending command to mylink: {} Message: {}", command, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Problem sending command to mylink", e));
                } catch (InterruptedException e) {
                    logger.debug("Interrupted while waiting after sending command to mylink: {} Message: {}", command,
                            e.getMessage());
                }
                future.complete(null);
            });
            return future;
        } else {
            future.complete(null);
            return future;
        }
    }

    private <T extends SomfyMyLinkResponseBase> CompletableFuture<@Nullable T> sendCommandWithResponse(SomfyMyLinkCommandBase command, Class<T> responseType) {
        String json = gson.toJson(command);
        return sendCommandWithResponse(json, responseType);
    }

    private <T extends SomfyMyLinkResponseBase> CompletableFuture<@Nullable T> sendCommandWithResponse(String command, Class<T> responseType)
            throws SomfyMyLinkException {
        CompletableFuture<@Nullable T> future = new CompletableFuture<>();
        ExecutorService commandExecutor = this.commandExecutor;
        if (commandExecutor != null) {
            commandExecutor.submit(() -> {
                try {
                    try (Socket socket = getConnection();
                            OutputStream out = socket.getOutputStream();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII))) {
                        byte[] sendBuffer = command.getBytes(StandardCharsets.US_ASCII);

                        // send the command
                        logger.debug("Sending: {}", command);
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
                    logger.info("Timeout sending command to mylink: {} Message: {}", command, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Timeout sending command to mylink", e));
                } catch (IOException e) {
                    logger.info("Problem sending command to mylink: {} Message: {}", command, e.getMessage());
                    future.completeExceptionally(new SomfyMyLinkException("Problem sending command to mylink", e));
                } catch (InterruptedException e) {
                    logger.debug("Interrupted while waiting after sending command to mylink: {} Message: {}", command,
                            e.getMessage());
                    future.complete(null);
                }
            });
            return future;
        } else {
            future.complete(null);
            return future;
        }
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

    private Socket getConnection() throws UnknownHostException, IOException {
        SomfyMyLinkConfiguration config = this.config;
        
        if (config == null) {
            throw new SomfyMyLinkException("Config not setup correctly");
        }

        logger.debug("Getting connection to mylink on: {}  Post: {}", config.ipAddress, MYLINK_PORT);
        String myLinkAddress = config.ipAddress;
        Socket socket = new Socket(myLinkAddress, MYLINK_PORT);
        socket.setSoTimeout(MYLINK_DEFAULT_TIMEOUT);
        return socket;
    }

    @Override
    public void thingUpdated(Thing thing) {
        SomfyMyLinkConfiguration newConfig = thing.getConfiguration().as(SomfyMyLinkConfiguration.class);
        config = newConfig;
    }

    @Override
    public void dispose() {
        logger.debug("Dispose called on {}", SomfyMyLinkBridgeHandler.class);
        disconnect();

        dispose(commandExecutor);

        ServiceRegistration<DiscoveryService> discoveryServiceRegistration = this.discoveryServiceRegistration;
        if (discoveryServiceRegistration != null) {
            discovery.deactivate();
            discoveryServiceRegistration.unregister();
            discoveryServiceRegistration = null;
        }

        logger.debug("Dispose finishing on {}", SomfyMyLinkBridgeHandler.class);
    }

    private static void dispose(@Nullable ExecutorService executor) {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
