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
package org.openhab.binding.dali.internal.handler;

import static org.openhab.binding.dali.internal.DaliBindingConstants.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.dali.internal.protocol.DaliBackwardFrame;
import org.openhab.binding.dali.internal.protocol.DaliCommandBase;
import org.openhab.binding.dali.internal.protocol.DaliResponse;
import org.openhab.core.common.NamedThreadFactory;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DaliserverBridgeHandler} handles the lifecycle of daliserver connections.
 *
 * @author Robert Schmid - Initial contribution
 */
@NonNullByDefault
public class DaliserverBridgeHandler extends BaseBridgeHandler {
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(BRIDGE_TYPE);

    private final Logger logger = LoggerFactory.getLogger(DaliserverBridgeHandler.class);
    private static final int HEARTBEAT_MINUTES = 2;
    private static final int DALI_DEFAULT_TIMEOUT = 5000;

    private DaliserverConfig config = new DaliserverConfig();
    private @Nullable ScheduledFuture<?> heartbeat;
    private @Nullable ExecutorService commandExecutor;

    public DaliserverBridgeHandler(Bridge thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Command received on daliserver {}", command);

        try {
            // TODO: Send broadcast commands
        } catch (DaliException e) {
            logger.info("Error handling command: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @Override
    public void initialize() {
        logger.info("Initializing dali bridge");
        config = getThing().getConfiguration().as(DaliserverConfig.class);

        commandExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory(thing.getUID().getAsString(), true));

        if (validConfiguration(config)) {
            // start the keepalive process
            if (heartbeat == null) {
                logger.info("Starting heartbeat job every {} min", HEARTBEAT_MINUTES);
                heartbeat = this.scheduler.scheduleWithFixedDelay(this::sendHeartbeat, 0, HEARTBEAT_MINUTES,
                        TimeUnit.MINUTES);
            }
        }
    }

    private boolean validConfiguration(@Nullable DaliserverConfig config) {
        if (config == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "dali configuration missing");
            return false;
        }

        if (config.host.isEmpty() || config.port == 0) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "daliserver address not specified");
            return false;
        }

        return true;
    }

    private Socket getConnection() throws IOException {
        try {
            logger.debug("Getting connection to daliserver on: {}  Port: {}", config.host, config.port);
            Socket socket = new Socket(config.host, config.port);
            socket.setSoTimeout(DALI_DEFAULT_TIMEOUT);
            return socket;
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            throw e;
        }
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
        // TODO: Query states from time to time
        updateStatus(ThingStatus.ONLINE);
    }

    public CompletableFuture<@Nullable Void> sendCommand(DaliCommandBase command) {
        return sendCommandWithResponse(command, DaliResponse.class).thenApply(c -> (Void) null);
    }

    public <T extends DaliResponse> CompletableFuture<@Nullable T> sendCommandWithResponse(DaliCommandBase command,
            Class<T> responseType) {
        CompletableFuture<@Nullable T> future = new CompletableFuture<>();
        ExecutorService commandExecutor = this.commandExecutor;
        if (commandExecutor != null) {
            commandExecutor.submit(() -> {
                byte[] message = ArrayUtils.addAll(new byte[] { 0x2, 0x0 }, command.frame.pack());

                try (Socket socket = getConnection();
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream())) {

                    // send the command
                    logger.debug("Sending: {}", DatatypeConverter.printHexBinary(message));
                    out.write(message);
                    if (command.sendTwice) {
                        out.flush();
                        in.readNBytes(4); // discard
                        out.write(message);
                    }
                    out.flush();

                    // read the response
                    try {
                        @Nullable
                        T response = parseResponse(in, responseType);
                        future.complete(response);
                        return;
                    } catch (DaliException e) {
                        future.completeExceptionally(e);
                        return;
                    }
                } catch (SocketTimeoutException e) {
                    logger.warn("Timeout sending command to daliserver: {} Message: {}", message, e.getMessage());
                    future.completeExceptionally(new DaliException("Timeout sending command to daliserver", e));
                } catch (IOException e) {
                    logger.warn("Problem sending command to daliserver: {} Message: {}", message, e.getMessage());
                    future.completeExceptionally(new DaliException("Problem sending command to daliserver", e));
                } catch (Exception e) {
                    logger.warn("Unexpected exception while sending command to daliserver: {} Message: {}", message,
                            e.getMessage());
                    future.completeExceptionally(e);
                }
            });
        } else {
            future.complete(null);
        }
        return future;
    }

    private <T extends DaliResponse> @Nullable T parseResponse(DataInputStream reader, Class<T> responseType)
            throws IOException {
        try {
            T result = responseType.getDeclaredConstructor().newInstance();
            byte[] response = reader.readNBytes(4);
            logger.debug("Received: {}", DatatypeConverter.printHexBinary(response));
            byte status = response[1], rval = response[2];
            if (status == 0) {
                result.parse(null);
            } else if (status == 1) {
                result.parse(new DaliBackwardFrame(rval));
            } else if (status == 255) {
                // This is "failure" - daliserver reports
                // this for a garbled response when several ballasts
                // reply. It should be interpreted as "Yes".
                result.parse(null);
            } else {
                throw new DaliException("status was" + status);
            }

            return result;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            return null;
        }
    }
}
