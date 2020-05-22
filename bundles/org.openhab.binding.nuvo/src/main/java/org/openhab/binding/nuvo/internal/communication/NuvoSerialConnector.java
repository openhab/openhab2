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
package org.openhab.binding.nuvo.internal.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.transport.serial.PortInUseException;
import org.eclipse.smarthome.io.transport.serial.SerialPort;
import org.eclipse.smarthome.io.transport.serial.SerialPortIdentifier;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.eclipse.smarthome.io.transport.serial.UnsupportedCommOperationException;
import org.openhab.binding.nuvo.internal.NuvoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for communicating with the Nuvo device through a serial connection
 *
 * @author Laurent Garnier - Initial contribution
 * @author Michael Lobstein - Adapted for the Nuvo binding
 */
@NonNullByDefault
public class NuvoSerialConnector extends NuvoConnector {

    private final Logger logger = LoggerFactory.getLogger(NuvoSerialConnector.class);

    private String serialPortName;
    private SerialPortManager serialPortManager;

    private @Nullable SerialPort serialPort;

    /**
     * Constructor
     *
     * @param serialPortManager the serial port manager
     * @param serialPortName the serial port name to be used
     */
    public NuvoSerialConnector(SerialPortManager serialPortManager, String serialPortName) {
        this.serialPortManager = serialPortManager;
        this.serialPortName = serialPortName;
    }

    @Override
    public synchronized void open() throws NuvoException {
        logger.info("Opening serial connection on port {}", serialPortName);
        try {
            SerialPortIdentifier portIdentifier = serialPortManager.getIdentifier(serialPortName);
            if (portIdentifier == null) {
                setConnected(false);
                logger.warn("Opening serial connection failed: No Such Port: {}", serialPortName);
                throw new NuvoException("Opening serial connection failed: No Such Port");
            }

            SerialPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            commPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            commPort.enableReceiveThreshold(1);
            commPort.enableReceiveTimeout(100);
            commPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

            InputStream dataIn = commPort.getInputStream();
            OutputStream dataOut = commPort.getOutputStream();

            if (dataOut != null) {
                dataOut.flush();
            }
            if (dataIn != null && dataIn.markSupported()) {
                try {
                    dataIn.reset();
                } catch (IOException e) {
                }
            }

            Thread thread = new NuvoReaderThread(this);
            setReaderThread(thread);
            thread.start();

            this.serialPort = commPort;
            this.dataIn = dataIn;
            this.dataOut = dataOut;

            setConnected(true);

            logger.debug("Serial connection opened");
        } catch (PortInUseException e) {
            setConnected(false);
            logger.warn("Opening serial connection failed: Port in Use Exception: {}", e.getMessage(), e);
            throw new NuvoException("Opening serial connection failed: Port in Use Exception");
        } catch (UnsupportedCommOperationException e) {
            setConnected(false);
            logger.warn("Opening serial connection failed: Unsupported Comm Operation Exception: {}", e.getMessage(),
                    e);
            throw new NuvoException("Opening serial connection failed: Unsupported Comm Operation Exception");
        } catch (UnsupportedEncodingException e) {
            setConnected(false);
            logger.warn("Opening serial connection failed: Unsupported Encoding Exception: {}", e.getMessage(), e);
            throw new NuvoException("Opening serial connection failed: Unsupported Encoding Exception");
        } catch (IOException e) {
            setConnected(false);
            logger.warn("Opening serial connection failed: IO Exception: {}", e.getMessage(), e);
            throw new NuvoException("Opening serial connection failed: IO Exception");
        }
    }

    @Override
    public synchronized void close() {
        logger.debug("Closing serial connection");
        SerialPort serialPort = this.serialPort;
        if (serialPort != null) {
            serialPort.removeEventListener();
        }
        super.cleanup();
        if (serialPort != null) {
            serialPort.close();
            this.serialPort = null;
        }
        setConnected(false);
        logger.debug("Serial connection closed");
    }
}
