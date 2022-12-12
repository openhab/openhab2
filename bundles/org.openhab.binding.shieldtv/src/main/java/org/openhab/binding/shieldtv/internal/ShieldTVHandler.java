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
package org.openhab.binding.shieldtv.internal;

import static org.openhab.binding.shieldtv.internal.ShieldTVBindingConstants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.shieldtv.internal.protocol.shieldtv.ShieldTVCommand;
import org.openhab.binding.shieldtv.internal.protocol.shieldtv.ShieldTVMessageParser;
import org.openhab.binding.shieldtv.internal.protocol.shieldtv.ShieldTVMessageParserCallbacks;
import org.openhab.binding.shieldtv.internal.protocol.shieldtv.ShieldTVRequest;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ShieldTVHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * Significant portions reused from Lutron binding with permission from Bob A.
 *
 * @author Ben Rosenblum - Initial contribution
 */
@NonNullByDefault
public class ShieldTVHandler extends BaseThingHandler implements ShieldTVMessageParserCallbacks {

    private static final int DEFAULT_RECONNECT_MINUTES = 5;
    private static final int DEFAULT_HEARTBEAT_MINUTES = 5;
    private static final long KEEPALIVE_TIMEOUT_SECONDS = 30;

    private static final String STATUS_INITIALIZING = "Initializing";

    private final Logger logger = LoggerFactory.getLogger(ShieldTVHandler.class);

    private @Nullable ShieldTVConfiguration config;
    private int reconnectInterval;
    private int heartbeatInterval;
    private int sendDelay;

    private @NonNullByDefault({}) SSLSocketFactory sslsocketfactory;
    private @Nullable SSLSocket sslsocket;
    private @Nullable BufferedWriter writer;
    private @Nullable BufferedReader reader;

    private @NonNullByDefault({}) ShieldTVMessageParser shieldtvMessageParser;

    private final BlockingQueue<ShieldTVCommand> sendQueue = new LinkedBlockingQueue<>();

    private @Nullable Future<?> asyncInitializeTask;

    private @Nullable Thread senderThread;
    private @Nullable Thread readerThread;

    private @Nullable ScheduledFuture<?> keepAliveJob;
    private @Nullable ScheduledFuture<?> keepAliveReconnectJob;
    private @Nullable ScheduledFuture<?> connectRetryJob;
    private final Object keepAliveReconnectLock = new Object();

    public ShieldTVHandler(Thing thing) {
        super(thing);
        shieldtvMessageParser = new ShieldTVMessageParser(this);
    }

    @Override
    public void initialize() {
        SSLContext sslContext;

        config = getConfigAs(ShieldTVConfiguration.class);

        String ipAddress = config.ipAddress;
        String keystorePassword = (config.keystorePassword == null) ? "" : config.keystorePassword;

        if (ipAddress == null || ipAddress.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "shieldtv address not specified");
            return;
        }

        reconnectInterval = (config.reconnect > 0) ? config.reconnect : DEFAULT_RECONNECT_MINUTES;
        heartbeatInterval = (config.heartbeat > 0) ? config.heartbeat : DEFAULT_HEARTBEAT_MINUTES;
        sendDelay = (config.delay < 0) ? 0 : config.delay;

        if (config.keystore == null || keystorePassword == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Keystore/keystore password not configured");
            return;
        } else {
            try (FileInputStream keystoreInputStream = new FileInputStream(config.keystore)) {
                logger.trace("Initializing keystore");
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

                keystore.load(keystoreInputStream, keystorePassword.toCharArray());

                logger.trace("Initializing SSL Context");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keystore, keystorePassword.toCharArray());

                TrustManager[] trustManagers;
                if (config.certValidate) {
                    // Use default trust manager which will attempt to validate server certificate from hub
                    TrustManagerFactory tmf = TrustManagerFactory
                            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(keystore);
                    trustManagers = tmf.getTrustManagers();
                } else {
                    // Use no-op trust manager which will not verify certificates
                    trustManagers = defineNoOpTrustManager();
                }

                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), trustManagers, null);

                sslsocketfactory = sslContext.getSocketFactory();
            } catch (FileNotFoundException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Keystore file not found");
                return;
            } catch (CertificateException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Certificate exception");
                return;
            } catch (UnrecoverableKeyException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Key unrecoverable with supplied password");
                return;
            } catch (KeyManagementException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Key management exception");
                logger.debug("Key management exception", e);
                return;
            } catch (KeyStoreException | NoSuchAlgorithmException | IOException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Error initializing keystore");
                logger.debug("Error initializing keystore", e);
                return;
            }
        }

        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Connecting");
        asyncInitializeTask = scheduler.submit(this::connect); // start the async connect task
    }

    private TrustManager[] defineNoOpTrustManager() {
        return new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate @Nullable [] chain, final @Nullable String authType) {
                logger.debug("Assuming client certificate is valid");
                if (chain != null && logger.isTraceEnabled()) {
                    for (int cert = 0; cert < chain.length; cert++) {
                        logger.trace("Subject DN: {}", chain[cert].getSubjectDN());
                        logger.trace("Issuer DN: {}", chain[cert].getIssuerDN());
                        logger.trace("Serial number {}:", chain[cert].getSerialNumber());
                    }
                }
            }

            @Override
            public void checkServerTrusted(final X509Certificate @Nullable [] chain, final @Nullable String authType) {
                logger.debug("Assuming server certificate is valid");
                if (chain != null && logger.isTraceEnabled()) {
                    for (int cert = 0; cert < chain.length; cert++) {
                        logger.trace("Subject DN: {}", chain[cert].getSubjectDN());
                        logger.trace("Issuer DN: {}", chain[cert].getIssuerDN());
                        logger.trace("Serial number: {}", chain[cert].getSerialNumber());
                    }
                }
            }

            @Override
            public X509Certificate @Nullable [] getAcceptedIssuers() {
                return null;
            }
        } };
    }

    private synchronized void connect() {

        try {
            logger.debug("Opening SSL connection to {}:{}", config.ipAddress, config.port);
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(config.ipAddress, config.port);
            sslsocket.startHandshake();
            writer = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
            this.sslsocket = sslsocket;
        } catch (UnknownHostException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Unknown host");
            return;
        } catch (IllegalArgumentException e) {
            // port out of valid range
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Invalid port number");
            return;
        } catch (InterruptedIOException e) {
            logger.debug("Interrupted while establishing connection");
            Thread.currentThread().interrupt();
            return;
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Error opening SSL connection. Check log.");
            logger.info("Error opening SSL connection: {}", e.getMessage());
            disconnect(false);
            scheduleConnectRetry(reconnectInterval); // Possibly a temporary problem. Try again later.
            return;
        }

        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, STATUS_INITIALIZING);

        Thread readerThread = new Thread(this::readerThreadJob, "Lutron reader");
        readerThread.setDaemon(true);
        readerThread.start();
        this.readerThread = readerThread;

        Thread senderThread = new Thread(this::senderThreadJob, "Lutron sender");
        senderThread.setDaemon(true);
        senderThread.start();
        this.senderThread = senderThread;

        logger.debug("Starting keepalive job with interval {}", heartbeatInterval);
        keepAliveJob = scheduler.scheduleWithFixedDelay(this::sendKeepAlive, heartbeatInterval, heartbeatInterval,
                TimeUnit.MINUTES);

        updateStatus(ThingStatus.ONLINE);
    }

    private void scheduleConnectRetry(long waitMinutes) {
        logger.debug("Scheduling connection retry in {} minutes", waitMinutes);
        connectRetryJob = scheduler.schedule(this::connect, waitMinutes, TimeUnit.MINUTES);
    }

    /**
     * Disconnect from bridge, cancel retry and keepalive jobs, stop reader and writer threads, and clean up.
     *
     * @param interruptAll Set if reconnect task should be interrupted if running. Should be false when calling from
     *            connect or reconnect, and true when calling from dispose.
     */
    private synchronized void disconnect(boolean interruptAll) {
        logger.debug("Disconnecting");

        ScheduledFuture<?> connectRetryJob = this.connectRetryJob;
        if (connectRetryJob != null) {
            connectRetryJob.cancel(true);
        }
        ScheduledFuture<?> keepAliveJob = this.keepAliveJob;
        if (keepAliveJob != null) {
            keepAliveJob.cancel(true);
        }

        Thread senderThread = this.senderThread;
        if (senderThread != null && senderThread.isAlive()) {
            senderThread.interrupt();
        }

        Thread readerThread = this.readerThread;
        if (readerThread != null && readerThread.isAlive()) {
            readerThread.interrupt();
        }
        SSLSocket sslsocket = this.sslsocket;
        if (sslsocket != null) {
            try {
                sslsocket.close();
            } catch (IOException e) {
                logger.debug("Error closing SSL socket: {}", e.getMessage());
            }
            this.sslsocket = null;
        }
        BufferedReader reader = this.reader;
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.debug("Error closing reader: {}", e.getMessage());
            }
        }
        BufferedWriter writer = this.writer;
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.debug("Error closing writer: {}", e.getMessage());
            }
        }
    }

    private synchronized void reconnect() {
        logger.debug("Attempting to reconnect to the bridge");
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "reconnecting");
        disconnect(false);
        connect();
    }

    /**
     * Method executed by the message sender thread (senderThread)
     */
    private void senderThreadJob() {
        logger.debug("Command sender thread started");
        try {
            while (!Thread.currentThread().isInterrupted() && writer != null) {
                ShieldTVCommand command = sendQueue.take();
                logger.trace("Sending command {}", command);

                try {
                    BufferedWriter writer = this.writer;
                    if (writer != null) {
                        writer.write(command.toString() + "\n");
                        writer.flush();
                    }
                } catch (InterruptedIOException e) {
                    logger.debug("Interrupted while sending");
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Interrupted");
                    break; // exit loop and terminate thread
                } catch (IOException e) {
                    logger.warn("Communication error, will try to reconnect. Error: {}", e.getMessage());
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                    sendQueue.add(command); // Requeue command
                    reconnect();
                    break; // reconnect() will start a new thread; terminate this one
                }
                if (sendDelay > 0) {
                    Thread.sleep(sendDelay); // introduce delay to throttle send rate
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            logger.debug("Command sender thread exiting");
        }
    }

    /**
     * Method executed by the message reader thread (readerThread)
     */
    private void readerThreadJob() {
        logger.debug("Message reader thread started");
        String msg = null;
        try {
            BufferedReader reader = this.reader;
            while (!Thread.interrupted() && reader != null && (msg = reader.readLine()) != null) {
                shieldtvMessageParser.handleMessage(msg);
            }
            if (msg == null) {
                logger.debug("End of input stream detected");
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Connection lost");
            }
        } catch (InterruptedIOException e) {
            logger.debug("Interrupted while reading");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Interrupted");
        } catch (IOException e) {
            logger.debug("I/O error while reading from stream: {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        } catch (RuntimeException e) {
            logger.warn("Runtime exception in reader thread", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        } finally {
            logger.debug("Message reader thread exiting");
        }
    }

    private void sendKeepAlive() {
        logger.trace("Sending keepalive query");
    }

    /*
     * private void checkInitialized() {
     * ThingStatusInfo statusInfo = getThing().getStatusInfo();
     * if (statusInfo.getStatus() == ThingStatus.OFFLINE && STATUS_INITIALIZING.equals(statusInfo.getDescription())) {
     * if (deviceDataLoaded && buttonDataLoaded) {
     * updateStatus(ThingStatus.ONLINE);
     * }
     * }
     * }
     */

    /**
     * Schedules the reconnect task keepAliveReconnectJob to execute in KEEPALIVE_TIMEOUT_SECONDS. This should be
     * cancelled by calling reconnectTaskCancel() if a valid response is received from the bridge.
     */
    private void reconnectTaskSchedule() {
        synchronized (keepAliveReconnectLock) {
            keepAliveReconnectJob = scheduler.schedule(this::keepaliveTimeoutExpired, KEEPALIVE_TIMEOUT_SECONDS,
                    TimeUnit.SECONDS);
        }
    }

    /**
     * Cancels the reconnect task keepAliveReconnectJob.
     */
    private void reconnectTaskCancel(boolean interrupt) {
        synchronized (keepAliveReconnectLock) {
            ScheduledFuture<?> keepAliveReconnectJob = this.keepAliveReconnectJob;
            if (keepAliveReconnectJob != null) {
                logger.trace("Canceling scheduled reconnect job.");
                keepAliveReconnectJob.cancel(interrupt);
                this.keepAliveReconnectJob = null;
            }
        }
    }

    /**
     * Executed by keepAliveReconnectJob if it is not cancelled by the LEAP message parser calling
     * validMessageReceived() which in turn calls reconnectTaskCancel().
     */
    private void keepaliveTimeoutExpired() {
        logger.debug("Keepalive response timeout expired. Initiating reconnect.");
        reconnect();
    }

    @Override
    public void validMessageReceived(String communiqueType) {
        reconnectTaskCancel(true); // Got a good message, so cancel reconnect task.
    }

    public void sendCommand(@Nullable ShieldTVCommand command) {
        if (command != null) {
            sendQueue.add(command);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("Command received: {}", channelUID.getId().toString());

        if (CHANNEL_KEYPRESS.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }

            // TODO: handle command

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        } else if (CHANNEL_PINCODE.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }

            // TODO: handle command

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        } else if (CHANNEL_RAW.equals(channelUID.getId())) {
            if (command instanceof StringType) {
                String message = ShieldTVRequest.encodeMessage(command.toString());
                logger.trace("Sending Raw Message: {}", message);
                logger.trace("Raw Message Decodes as: {}", ShieldTVRequest.decodeMessage(message));
                sendCommand(new ShieldTVCommand(message));
            }
        }
    }
}
