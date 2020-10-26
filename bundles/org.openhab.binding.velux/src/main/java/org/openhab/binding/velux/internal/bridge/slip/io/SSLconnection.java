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
package org.openhab.binding.velux.internal.bridge.slip.io;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.velux.internal.VeluxBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transport layer supported by the Velux bridge.
 * <P>
 * SLIP-based 2nd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides methods for pre- and post-communication
 * as well as a common method for the real communication.
 * <UL>
 * <LI>{@link SSLconnection#SSLconnection} for establishing the connection,</LI>
 * <LI>{@link SSLconnection#send} for sending a message to the bridge,</LI>
 * <LI>{@link SSLconnection#available} for observation whether there are bytes available,</LI>
 * <LI>{@link SSLconnection#receive} for receiving a message from the bridge,</LI>
 * <LI>{@link SSLconnection#close} for tearing down the connection.</LI>
 * <LI>{@link SSLconnection#setTimeout} for adapting communication parameters.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
@NonNullByDefault
class SSLconnection implements Closeable {
    private final Logger logger = LoggerFactory.getLogger(SSLconnection.class);

    // Public definition
    public static final SSLconnection UNKNOWN = new SSLconnection();

    /*
     * ***************************
     * ***** Private Objects *****
     */

    private boolean ready = false;
    private @Nullable SSLSocket socket;
    private @Nullable DataOutputStream dOut;
    private @Nullable DataInputStreamWithTimeout dIn;
    private int ioTimeoutMSecs = 60000;
    private int connectTimeoutMSecs = 5000;

    /**
     * Fake trust manager to suppress any certificate errors,
     * used within {@link #SSLconnection} for seamless operation
     * even on self-signed certificates like provided by <B>Velux</B>.
     */
    private final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate @Nullable [] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate @Nullable [] arg0, @Nullable String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate @Nullable [] arg0, @Nullable String arg1)
                throws CertificateException {
        }
    } };

    /*
     * ************************
     * ***** Constructors *****
     */

    /**
     * Constructor for initialization of an unfinished connectivity.
     */
    SSLconnection() {
        logger.debug("SSLconnection() called.");
        ready = false;
        logger.trace("SSLconnection() finished.");
    }

    /**
     * Constructor to setup and establish a connection.
     *
     * @param host as String describing the Service Access Point location i.e. hostname.
     * @param port as String describing the Service Access Point location i.e. TCP port.
     * @throws java.net.ConnectException in case of unrecoverable communication failures.
     * @throws java.io.IOException in case of continuous communication I/O failures.
     * @throws java.net.UnknownHostException in case of continuous communication I/O failures.
     */
    SSLconnection(String host, int port) throws ConnectException, IOException, UnknownHostException {
        logger.debug("SSLconnection({},{}) called.", host, port);
        logger.info("Starting {} bridge connection.", VeluxBindingConstants.BINDING_ID);
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IOException(String.format("create of an empty trust store failed: %s.", e.getMessage()));
        }
        logger.trace("SSLconnection(): creating socket...");
        // Just for avoidance of Potential null pointer access
        SSLSocket socketX = (SSLSocket) ctx.getSocketFactory().createSocket();
        if (socketX != null) {
            socketX.setSoTimeout(ioTimeoutMSecs);
            socketX.setKeepAlive(true);
            if (logger.isTraceEnabled()) {
                logger.trace(
                        "SSLconnection(): connecting... (ip={}, port={}, connectTimeout={}, soTimeout={}, soKeepAlive={})",
                        host, port, connectTimeoutMSecs, socketX.getSoTimeout(),
                        socketX.getKeepAlive() ? "true" : "false");
            }
            socketX.connect(new InetSocketAddress(host, port), connectTimeoutMSecs);
            logger.trace("SSLconnection(): starting SSL handshake...");
            socketX.startHandshake();
            dOut = new DataOutputStream(socketX.getOutputStream());
            dIn = new DataInputStreamWithTimeout(socketX.getInputStream());
            ready = true;
            socket = socketX;
        }
        logger.trace("SSLconnection() finished.");
    }

    /*
     * **************************
     * ***** Public Methods *****
     */

    /**
     * Method to query the readiness of the connection.
     *
     * @return <b>ready</b> as boolean for an established connection.
     */
    public synchronized boolean isReady() {
        return ready;
    }

    /**
     * Method to pass a message towards the bridge.
     * This method gets called when we are initiating a new SLIP transaction.
     * <p>
     * Note that DataOutputStream and DataInputStream are buffered I/O's. The SLIP protocol requires that prior requests
     * should have been fully sent over the socket, and their responses should have been fully read from the buffer
     * before the next request is initiated. i.e. Both read and write buffers should already be empty. Nevertheless,
     * just in case, we do the following..
     * <p>
     * 1) Flush from the read buffer any orphan response data that may have been left over from prior transactions, and
     * 2) Flush the write buffer directly to the socket to ensure that any exceptions are raised immediately, and the
     * KLF starts work immediately
     *
     * @param packet as Array of bytes to be transmitted towards the bridge via the established connection.
     * @throws java.io.IOException in case of a communication I/O failure, and sets 'ready' = false
     */
    public synchronized void send(byte[] packet) throws IOException {
        logger.trace("send() called, writing {} bytes.", packet.length);
        try {
            DataOutputStream dOutX = dOut;
            if (!ready || (dOutX == null)) {
                throw new IOException();
            }
            // copy packet data to the write buffer
            dOutX.write(packet, 0, packet.length);
            // force the write buffer data to be written to the socket
            dOutX.flush();
            if (logger.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();
                for (byte b : packet) {
                    sb.append(String.format("%02X ", b));
                }
                logger.trace("send() finished after having send {} bytes: {}", packet.length, sb.toString());
            }
        } catch (IOException e) {
            ready = false;
            throw e;
        }
    }

    /**
     * Method to verify that there is a message from the bridge.
     *
     * @return <b>true</b> if there are any messages ready to be queried using {@link SSLconnection#receive}.
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    public synchronized boolean available() throws IOException {
        logger.trace("available() called.");
        DataInputStreamWithTimeout dInX = dIn;
        if (!ready || (dInX == null)) {
            throw new IOException();
        }
        int availableMessages = dInX.available();
        logger.trace("available(): found {} messages ready to be read (> 0 means true).", availableMessages);
        return availableMessages > 0;
    }

    /**
     * Method to get a message from the bridge.
     *
     * @return <b>packet</b> as Array of bytes as received from the bridge via the established connection.
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    public synchronized byte[] receive() throws IOException {
        logger.trace("receive() called.");
        try {
            DataInputStreamWithTimeout dInX = dIn;
            if (!ready || (dInX == null)) {
                throw new IOException();
            }
            byte[] packet = dInX.readSlipMessage(ioTimeoutMSecs);
            if (logger.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();
                for (byte b : packet) {
                    sb.append(String.format("%02X ", b));
                }
                logger.trace("receive() finished after having read {} bytes: {}", packet.length, sb.toString());
            }
            return packet;
        } catch (IOException e) {
            ready = false;
            throw e;
        }
    }

    /**
     * Destructor to tear down a connection. Overridden method of {@link Closeable} interface. Closes the sockets.
     *
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    @Override
    public synchronized void close() throws IOException {
        logger.debug("close() called.");
        ready = false;
        logger.info("Shutting down Velux bridge connection.");
        // Just for avoidance of Potential null pointer access
        DataInputStreamWithTimeout dInX = dIn;
        if (dInX != null) {
            dInX.close();
            dIn = null;
        }
        // Just for avoidance of Potential null pointer access
        DataOutputStream dOutX = dOut;
        if (dOutX != null) {
            dOutX.close();
            dOut = null;
        }
        // Just for avoidance of Potential null pointer access
        SSLSocket socketX = socket;
        if (socketX != null) {
            socketX.close();
            socket = null;
        }
        logger.trace("close() finished.");
    }

    /**
     * Set the socket read time out.
     *
     * @param timeoutMSecs the maximum allowed duration in milliseconds for read operations.
     * @throws SocketException
     */
    public void setTimeout(int timeoutMSecs) throws SocketException {
        logger.debug("setTimeout() set timeout to {} milliseconds.", timeoutMSecs);
        ioTimeoutMSecs = timeoutMSecs;
        SSLSocket socketX = socket;
        if (socketX != null) {
            socketX.setSoTimeout(ioTimeoutMSecs);
            logger.trace("setTimeout() confirmed {} milliseconds.", socketX.getSoTimeout());
        }
    }
}
