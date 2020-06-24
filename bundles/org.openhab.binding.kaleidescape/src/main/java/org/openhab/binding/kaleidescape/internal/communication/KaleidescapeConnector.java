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
package org.openhab.binding.kaleidescape.internal.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.kaleidescape.internal.KaleidescapeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for communicating with the Kaleidescape component
 *
 * @author Laurent Garnier - Initial contribution
 * @author Michael Lobstein - Adapted for the Kaleidescape binding
 */
@NonNullByDefault
public abstract class KaleidescapeConnector {
    public static final String SUCCESS_MSG = "01/1/000:/89";
    public static final String BEGIN_CMD = "01/1/";
    public static final String END_CMD = ":\r";
    public static final String STANDBY_MSG = "Device is in standby";

    private final Pattern PATTERN = Pattern.compile("^(\\d{2})/./(\\d{3})\\:([^:^/]*)\\:(.*?)\\:/(\\d{2})$");

    private final Logger logger = LoggerFactory.getLogger(KaleidescapeConnector.class);

    /** The output stream */
    protected @Nullable OutputStream dataOut;

    /** The input stream */
    protected @Nullable InputStream dataIn;

    /** true if the connection is established, false if not */
    private boolean connected;

    private @Nullable Thread readerThread;

    private List<KaleidescapeMessageEventListener> listeners = new ArrayList<>();

    /**
     * Get whether the connection is established or not
     *
     * @return true if the connection is established
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Set whether the connection is established or not
     *
     * @param connected true if the connection is established
     */
    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Set the thread that handles the feedback messages
     *
     * @param readerThread the thread
     */
    protected void setReaderThread(Thread readerThread) {
        this.readerThread = readerThread;
    }

    /**
     * Open the connection with the Kaleidescape component
     *
     * @throws KaleidescapeException - In case of any problem
     */
    public abstract void open() throws KaleidescapeException;

    /**
     * Close the connection with the Kaleidescape component
     */
    public abstract void close();

    /**
     * Stop the thread that handles the feedback messages and close the opened input and output streams
     */
    protected void cleanup() {
        Thread readerThread = this.readerThread;
        OutputStream dataOut = this.dataOut;
        if (dataOut != null) {
            try {
                dataOut.close();
            } catch (IOException e) {
                logger.debug("Error closing dataOut: {}", e.getMessage());
            }
            this.dataOut = null;
        }
        InputStream dataIn = this.dataIn;
        if (dataIn != null) {
            try {
                dataIn.close();
            } catch (IOException e) {
                logger.debug("Error closing dataIn: {}", e.getMessage());
            }
            this.dataIn = null;
        }
        if (readerThread != null) {
            readerThread.interrupt();
            try {
                readerThread.join(3000);
            } catch (InterruptedException e) {
                logger.warn("Error joining readerThread: {}", e.getMessage());
            }
            this.readerThread = null;
        }
    }

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b. The number of bytes
     * actually read is returned as an integer.
     *
     * @param dataBuffer the buffer into which the data is read.
     *
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     *         stream has been reached.
     *
     * @throws KaleidescapeException - If the input stream is null, if the first byte cannot be read for any reason
     *             other than the end of the file, if the input stream has been closed, or if some other I/O error
     *             occurs.
     * @throws InterruptedIOException - if the thread was interrupted during the reading of the input stream
     */
    protected int readInput(byte[] dataBuffer) throws KaleidescapeException {
        InputStream dataIn = this.dataIn;
        if (dataIn == null) {
            throw new KaleidescapeException("readInput failed: input stream is null");
        }
        try {
            return dataIn.read(dataBuffer);
        } catch (IOException e) {
            throw new KaleidescapeException("readInput failed: " + e.getMessage());
        }
    }

    /**
     * Ping the connection by requesting the time from the component
     *
     * @throws KaleidescapeException - In case of any problem
     */
    public void ping() throws KaleidescapeException {
        sendCommand("GET_TIME");
    }

    /**
     * Request the Kaleidescape component to execute a command
     *
     * @param cmd the command to execute
     *
     * @throws KaleidescapeException - In case of any problem
     */
    public void sendCommand(@Nullable String cmd) throws KaleidescapeException {
        String messageStr = BEGIN_CMD + cmd + END_CMD;

        logger.debug("Send command {}", messageStr);

        OutputStream dataOut = this.dataOut;
        if (dataOut == null) {
            throw new KaleidescapeException("Send command \"" + messageStr + "\" failed: output stream is null");
        }
        try {
            dataOut.write(messageStr.getBytes(StandardCharsets.US_ASCII));
            dataOut.flush();
        } catch (IOException e) {
            throw new KaleidescapeException("Send command \"" + cmd + "\" failed: " + e.getMessage());
        }
    }

    /**
     * Add a listener to the list of listeners to be notified with events
     *
     * @param listener the listener
     */
    public void addEventListener(KaleidescapeMessageEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener from the list of listeners to be notified with events
     *
     * @param listener the listener
     */
    public void removeEventListener(KaleidescapeMessageEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Analyze an incoming message and dispatch corresponding event (key, value) to the event listeners
     *
     * @param incomingMessage the received message
     */
    public void handleIncomingMessage(byte[] incomingMessage) {
        String message = new String(incomingMessage, StandardCharsets.US_ASCII).trim();

        // ignore empty success messages
        if (!SUCCESS_MSG.equals(message)) {
            logger.debug("handleIncomingMessage: {}", message);

            // Kaleidescape message ie: 01/!/000:TITLE_NAME:Office Space:/79
            // or: 01/!/000:PLAY_STATUS:2:0:01:07124:00138:001:00311:00138:/27
            // or: 01/1/000:TIME:2020:04:27:11:38:52:CDT:/84
            // g1=zoneid, g2=sequence, g3=message name, g4=message, g5=checksum
            // pattern : "^(\\d{2})/./(\\d{3})\\:([^:^/]*)\\:(.*?)\\:/(\\d{2})$");

            Matcher matcher = PATTERN.matcher(message);
            if (matcher.find()) {
                dispatchKeyValue(matcher.group(3), matcher.group(4));
            } else {
                logger.debug("no match on message: {}", message);
                if (message.contains(STANDBY_MSG)) {
                    dispatchKeyValue(STANDBY_MSG, "");
                }
            }
        }
    }

    /**
     * Dispatch an event (key, value) to the event listeners
     *
     * @param key the key
     * @param value the value
     */
    private void dispatchKeyValue(String key, String value) {
        KaleidescapeMessageEvent event = new KaleidescapeMessageEvent(this, key, value);
        listeners.forEach(l -> l.onNewMessageEvent(event));
    }
}
