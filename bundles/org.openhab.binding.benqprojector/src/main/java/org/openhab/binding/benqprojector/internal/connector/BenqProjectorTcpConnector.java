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
package org.openhab.binding.benqprojector.internal.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.benqprojector.internal.BenqProjectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for TCP communication.
 *
 * @author Michael Lobstein - Initial contribution
 */
@NonNullByDefault
public class BenqProjectorTcpConnector implements BenqProjectorConnector {

    private final Logger logger = LoggerFactory.getLogger(BenqProjectorTcpConnector.class);
    private final String ip;
    private final int port;

    private @Nullable Socket socket = null;
    private @Nullable InputStream in = null;
    private @Nullable OutputStream out = null;

    public BenqProjectorTcpConnector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void connect() throws BenqProjectorException {
        logger.debug("Open connection to address'{}:{}'", ip, port);

        try {
            Socket socket = new Socket(ip, port);
            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            throw new BenqProjectorException(e);
        }
    }

    @Override
    public void disconnect() throws BenqProjectorException {
        OutputStream out = this.out;

        if (out != null) {
            logger.debug("Close tcp out stream");
            try {
                out.close();
            } catch (IOException e) {
                logger.debug("Error occurred when closing tcp out stream: {}", e.getMessage());
            }
        }

        InputStream in = this.in;
        if (in != null) {
            logger.debug("Close tcp in stream");
            try {
                in.close();
            } catch (IOException e) {
                logger.debug("Error occurred when closing tcp in stream: {}", e.getMessage());
            }
        }

        Socket socket = this.socket;
        if (socket != null) {
            logger.debug("Closing socket");
            try {
                socket.close();
            } catch (IOException e) {
                logger.debug("Error occurred when closing tcp socket: {}", e.getMessage());
            }
        }

        this.socket = null;
        this.out = null;
        this.in = null;

        logger.debug("Closed");
    }

    @Override
    public String sendMessage(String data, int timeout) throws BenqProjectorException {
        InputStream in = this.in;
        OutputStream out = this.out;

        if (in == null || out == null) {
            connect();
            in = this.in;
            out = this.out;
        }

        try {
            if (in != null) {
                // flush input stream
                if (in.markSupported()) {
                    in.reset();
                } else {
                    while (in.available() > 0) {
                        int availableBytes = in.available();

                        if (availableBytes > 0) {
                            byte[] tmpData = new byte[availableBytes];
                            in.read(tmpData, 0, availableBytes);
                        }
                    }
                }
                return sendMmsg(data, timeout);
            } else {
                return "";
            }
        } catch (IOException e) {
            logger.debug("IO error occurred...reconnect and resend once: {}", e.getMessage());
            disconnect();
            connect();

            try {
                return sendMmsg(data, timeout);
            } catch (IOException e1) {
                throw new BenqProjectorException(e);
            }
        }
    }

    private String sendMmsg(String data, int timeout) throws IOException, BenqProjectorException {
        String resp = "";

        InputStream in = this.in;
        OutputStream out = this.out;

        if (in != null && out != null) {
            data = START + data + END;
            out.write(data.getBytes(StandardCharsets.US_ASCII));
            out.flush();

            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;

            while (elapsedTime < timeout) {
                int availableBytes = in.available();
                if (availableBytes > 0) {
                    byte[] tmpData = new byte[availableBytes];
                    int readBytes = in.read(tmpData, 0, availableBytes);
                    resp = resp.concat(new String(tmpData, 0, readBytes, StandardCharsets.US_ASCII));
                    if (resp.contains(END)) {
                        return resp.replace(RESP_START, "").replace(END, "").replace("\r", "").replace("\n", "");
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new BenqProjectorException(e);
                    }
                }

                elapsedTime = System.currentTimeMillis() - startTime;
            }
        }
        return resp;
    }
}
