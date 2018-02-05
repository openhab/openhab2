/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.device.connector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for connectors. Reads data from an input stream. Subclasses should implement connection specific methods
 * and trigger the reading of the data.
 *
 * @author M. Volaart - Initial contribution
 * @author Hilbrand Bouwkamp - Major refactoring. Code moved around from other classes.
 */
@NonNullByDefault
class DSMRBaseConnector {

    private final Logger logger = LoggerFactory.getLogger(DSMRBaseConnector.class);

    /**
     * Listener to send received data and errors to.
     */
    protected final DSMRConnectorListener dsmrConnectorListener;

    /**
     * 1Kbyte buffer for storing received data.
     */
    private final byte[] buffer = new byte[1024]; // 1K

    /**
     * Read lock to have 1 process reading at a time.
     */
    private final Object readLock = new Object();

    /**
     * Keeps track of the open state of the connector.
     */
    private boolean open;

    public DSMRBaseConnector(DSMRConnectorListener connectorListener) {
        this.dsmrConnectorListener = connectorListener;
    }

    /**
     * Input stream reading the Serial port.
     */
    @Nullable
    private BufferedInputStream inputStream;

    /**
     * Opens the connector with the given stream to read data from.
     *
     * @param inputStream input stream to read data from
     */
    protected void open(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
        open = true;
    }

    /**
     * @return Returns true if connector is in state open
     */
    protected boolean isOpen() {
        return open;
    }

    /**
     * Closes the connector.
     */
    protected void close() {
        open = false;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ioe) {
                logger.debug("Failed to close reader", ioe);
            }
        }
        inputStream = null;
    }

    /**
     * Reads available data from the input stream.
     */
    protected void handleDataAvailable() {
        try {
            synchronized (readLock) {
                // Read without lock on the connection status to permit fast closure
                // This could lead to a NPE on variable serialInputStream so we will catch the NPE
                int bytesAvailable = inputStream.available();
                while (bytesAvailable > 0) {
                    int bytesAvailableRead = inputStream.read(buffer, 0, Math.min(bytesAvailable, buffer.length));

                    if (open && bytesAvailableRead > 0) {
                        dsmrConnectorListener.handleData(buffer, bytesAvailableRead);
                    } else {
                        logger.debug("Expected bytes {} to read, but {} bytes were read", bytesAvailable,
                                bytesAvailableRead);
                    }
                    bytesAvailable = inputStream.available();
                }
            }
        } catch (IOException e) {
            dsmrConnectorListener.handleErrorEvent(DSMRConnectorErrorEvent.READ_ERROR);
            logger.debug("Exception on read data", e);
        } catch (NullPointerException e) { // NOPMD AvoidCatchingNPE Catching null pointer is by design
            // Don't call listener because error triggered while closing connection.
            logger.trace("Connection closed during read.", e);
        }
    }
}
