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
package org.openhab.binding.dominoswiss.internal;

import static org.openhab.binding.dominoswiss.internal.DominoswissBindingConstants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EgateHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Frieso Aeschbacher - Initial contribution
 */
@NonNullByDefault
public class EGateHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(EGateHandler.class);
    private @Nullable Socket egateSocket;

    private int port;
    private @Nullable String host;
    private static final int SOCKET_TIMEOUT_SEC = 250;
    private final Object lock = new Object();
    private @Nullable BufferedWriter writer;
    private @Nullable BufferedReader reader;
    private @Nullable Future<?> refreshJob;
    private @Nullable Map<String, ThingUID> registeredBlinds;
    private @Nullable ScheduledFuture<?> pollingJob;

    public EGateHandler(Bridge thing) {
        super(thing);
        registeredBlinds = new HashMap<String, ThingUID>();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(GETCONFIG)) {
            sendCommand("EthernetGet;\r");
        }
    }

    @Override
    public void initialize() {
        DominoswissConfiguration config;
        config = this.getConfigAs(DominoswissConfiguration.class);
        host = config.ipAddress;
        port = config.port;

        if (host != null && port > 0) {
            // Create a socket to eGate
            pollingJob = scheduler.scheduleWithFixedDelay(this::pollingConfig, 0, 30, TimeUnit.SECONDS);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                    "Cannot connect to dominoswiss eGate gateway. host IP address or port are not set.");
        }
    }

    @Override
    public void dispose() {
        try {
            egateSocket.close();
            refreshJob.cancel(true);
            reader.close();
            if (pollingJob != null) {
                pollingJob.cancel(true);
                pollingJob = null;
            }
            logger.debug("EGate Handler connection closed, disposing");
        } catch (IOException e) {
            logger.debug("EGate Handler Error on dispose: {} ", e.toString());
        }
    }

    public synchronized boolean isConnected() {
        if (egateSocket == null) {
            return false;
        }

        // NOTE: isConnected() returns true once a connection is made and will
        // always return true even after the socket is closed
        // http://stackoverflow.com/questions/10163358/
        return egateSocket.isConnected() && !egateSocket.isClosed();
    }

    /**
     * Possible Instructions are:
     * FssTransmit 1 Kommandoabsetzung (Controller > eGate > Dominoswiss)
     * FssReceive 2 Empfangenes Funkpaket (Dominoswiss > eGate > Controller)
     *
     * @throws InterruptedException
     *
     */

    public void tiltUp(String id) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            pulseUp(id);
            Thread.sleep(150); // sleep to not confuse the blinds

        }
    }

    public void tiltDown(String id) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            pulseDown(id);
            Thread.sleep(150);// sleep to not confuse the blinds
        }
    }

    public void pulseUp(String id) {
        sendCommand("Instruction=1;ID=" + id + ";Command=1;Priority=1;CheckNr=3415347;" + CR);
    }

    public void pulseDown(String id) {
        sendCommand("Instruction=1;ID=" + id + ";Command=2;Priority=1;CheckNr=2764516;" + CR);
    }

    public void continuousUp(String id) {
        sendCommand("Instruction=1;ID=" + id + ";Command=3;Priority=1;CheckNr=2867016;" + CR, 20000);
    }

    public void continuousDown(String id) {
        sendCommand("Instruction=1;ID=" + id + ";Command=4;Priority=1;CheckNr=973898;" + CR, 20000);
    }

    public void stop(String id) {
        sendCommand("Instruction=1;ID=" + id + ";Command=5;Priority=1;CheckNr=5408219;" + CR);
    }

    // @SuppressWarnings("null")
    public void registerBlind(String id, ThingUID uid) {
        logger.debug("Registring Blind id {} with thingUID {}", id, uid);
        registeredBlinds.put(id, uid);
    }

    /**
     * Send a command to the eGate Server.
     */

    private void sendCommand(String command) {
        sendCommand(command, SOCKET_TIMEOUT_SEC);
    }

    @SuppressWarnings("null")
    private synchronized void sendCommand(String command, int timeout) {
        logger.debug("EGate got command: {}", command);

        if (!isConnected()) {
            logger.debug("no connection to Dominoswiss eGate server when trying to send command, returning...");
            return;
        }

        // Send plain string to eGate Server,
        try {
            egateSocket.setSoTimeout(SOCKET_TIMEOUT_SEC);
            writer.write(command);
            writer.flush();
        } catch (IOException e) {
            logger.error("Error while sending command {} to Dominoswiss eGate Server {} ", command, e.toString());
        }
    }

    private void pollingConfig() {
        if (!isConnected()) {
            synchronized (lock) {
                try {
                    egateSocket = new Socket();
                    logger.debug("before connect");
                    egateSocket.connect(new InetSocketAddress(host, port));
                    logger.debug("after connect");
                    egateSocket.setSoTimeout(SOCKET_TIMEOUT_SEC);
                    logger.debug("before writer");
                    writer = new BufferedWriter(new OutputStreamWriter(egateSocket.getOutputStream()));
                    writer.write("SilenceModeSet;Value=0;" + CR);
                    writer.flush();

                } catch (IOException e) {
                    logger.debug("IOException in pollingConfig: {} host {} port {}", e.toString(), host, port);
                    try {
                        egateSocket.close();
                        egateSocket = null;
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.toString());
                    } catch (IOException e1) {
                        logger.debug("EGate Socket not closed {}", e1.toString());
                    }
                    egateSocket = null;
                }
                if (egateSocket != null) {
                    updateStatus(ThingStatus.ONLINE);
                    startAutomaticRefresh();
                    logger.debug("EGate Handler started automatic refresh, status: {} ",
                            getThing().getStatus().toString());
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                    logger.debug("Something went wrong: Host {} Port {}", host, port);
                }
            }
        }
    }

    private void startAutomaticRefresh() {
        Runnable runnable = () -> {
            try {
                if (reader == null) {
                    reader = new BufferedReader(new InputStreamReader(egateSocket.getInputStream()));
                }
                logger.debug("Socket State: {} to: {}", egateSocket.isConnected(), egateSocket.toString());
                if (reader.ready()) {
                    String input = reader.readLine();
                    logger.debug("Reader got from EGATE: {}", input);
                    onData(input);
                }
            } catch (IOException e) {
                logger.debug("Error while reading command from Dominoswiss eGate Server {} ", e.toString());
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, e.toString());
            }
        };
        refreshJob = scheduler.submit(runnable);
    }

    /**
     * Finds and returns a child thing for a given UID of this bridge.
     *
     * @param uid uid of the child thing
     * @return child thing with the given uid or null if thing was not found
     */
    public @Nullable Thing getThingByUID(ThingUID uid) {
        Bridge bridge = getThing();

        List<Thing> things = bridge.getThings();

        for (Thing thing : things) {
            if (thing.getUID().equals(uid)) {
                return thing;
            }
        }

        return null;
    }

    @SuppressWarnings("null")
    protected void onData(String input) {
        // Instruction=2;ID=19;Command=1;Value=0;Priority=0;
        Map<String, String> map = new HashMap<String, String>();
        // split on ;
        String[] parts = input.split(";");
        if (parts.length >= 2) {
            for (int i = 0; i < parts.length; i += 2) {
                map.put(parts[i], parts[i + 1]);
            }
        }
        // only use FSSReceive Commands
        /*
         * Will be used in a Future Release
         * if (map.get("Instruction").toString() == "2") {
         * String id = map.get("ID");
         * ThingUID uid = registeredBlinds.get(map.get(id));
         * if (uid != null) {
         * blind = getThingByUID(uid);
         *
         * List<Channel> channels = blind.getChannels();
         * logger.debug("Channels: {} of Blind {}", channels.toString(), blind.getConfiguration());
         *
         * switch (map.get("Command")) {
         * case "1":
         * break;
         *
         * case "2":
         * break;
         *
         * case "3":
         * break;
         *
         * case "4":
         * break;
         * default:
         * break;
         * }
         *
         * } else {
         * logger.error("Error no blind registered in eGate Bridge. Please add some blinds");
         * }
         * }
         */
    }
}
