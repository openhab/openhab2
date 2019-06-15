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
package org.openhab.binding.rotel.internal.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.rotel.internal.RotelException;
import org.openhab.binding.rotel.internal.RotelModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for communicating with the Rotel device through a IP connection or a serial over IP connection
 *
 * @author Laurent Garnier - Initial contribution
 */
@NonNullByDefault
public class RotelIpConnector extends RotelConnector {

    private final Logger logger = LoggerFactory.getLogger(RotelIpConnector.class);

    private String address;
    private int port;

    private @NonNullByDefault({}) Socket clientSocket;

    /**
     * Constructor
     *
     * @param address the IP address of the projector
     * @param port the TCP port to be used
     * @param model the projector model in use
     * @param protocol the protocol to be used
     */
    public RotelIpConnector(String address, Integer port, RotelModel model, RotelProtocol protocol) {
        super(model, protocol, false);

        this.address = address;
        this.port = port;
    }

    @Override
    public synchronized void open() throws RotelException {
        logger.debug("Opening IP connection on IP {} port {}", this.address, this.port);
        try {
            clientSocket = new Socket(this.address, this.port);
            clientSocket.setSoTimeout(100);

            dataOut = new DataOutputStream(clientSocket.getOutputStream());
            dataIn = new DataInputStream(clientSocket.getInputStream());

            setReaderThread(new RotelReaderThread(this));
            getReaderThread().start();

            setConnected(true);

            logger.debug("IP connection opened");
        } catch (IOException | SecurityException | IllegalArgumentException e) {
            setConnected(false);
            logger.debug("Opening IP connection failed: {}", e.getMessage());
            throw new RotelException("Opening IP connection failed: " + e.getMessage());
        }
    }

    @Override
    public synchronized void close() {
        logger.debug("Closing IP connection");
        super.cleanup();
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
            clientSocket = null;
        }
        setConnected(false);
        logger.debug("IP connection closed");
    }

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b. The number of bytes
     * actually read is returned as an integer.
     * In case of socket timeout, the returned value is 0.
     *
     * @param dataBuffer the buffer into which the data is read.
     *
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     *         stream has been reached.
     * 
     * @throws RotelException - If the input stream is null, if the first byte cannot be read for any reason
     *             other than the end of the file, if the input stream has been closed, or if some other I/O error
     *             occurs.
     * @throws InterruptedIOException - if the thread was interrupted during the reading of the input stream
     */
    @Override
    protected int readInput(byte[] dataBuffer) throws RotelException, InterruptedIOException {
        if (dataIn == null) {
            throw new RotelException("readInput failed: input stream is null");
        }
        try {
            return dataIn.read(dataBuffer);
        } catch (SocketTimeoutException e) {
            return 0;
        } catch (IOException e) {
            logger.debug("readInput failed: {}", e.getMessage());
            throw new RotelException("readInput failed: " + e.getMessage());
        }
    }
}
