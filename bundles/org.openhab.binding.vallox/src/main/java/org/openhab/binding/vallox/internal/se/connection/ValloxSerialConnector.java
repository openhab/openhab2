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
package org.openhab.binding.vallox.internal.se.connection;

import static org.openhab.binding.vallox.internal.se.ValloxSEConstants.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.transport.serial.PortInUseException;
import org.eclipse.smarthome.io.transport.serial.SerialPort;
import org.eclipse.smarthome.io.transport.serial.SerialPortEvent;
import org.eclipse.smarthome.io.transport.serial.SerialPortEventListener;
import org.eclipse.smarthome.io.transport.serial.SerialPortIdentifier;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.eclipse.smarthome.io.transport.serial.UnsupportedCommOperationException;
import org.openhab.binding.vallox.internal.se.configuration.ValloxSEConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ValloxSerialConnector} is responsible for creating serial connection to vallox.
 *
 * @author Miika Jukka - Initial contribution
 */
@NonNullByDefault
public class ValloxSerialConnector extends ValloxBaseConnector implements SerialPortEventListener {

    private final Logger logger = LoggerFactory.getLogger(ValloxSerialConnector.class);

    private @Nullable SerialPort serialPort;

    public ValloxSerialConnector(SerialPortManager portManager, ScheduledExecutorService scheduler) {
        super(portManager, scheduler);
        logger.debug("Serial connector initialized");
    }

    @SuppressWarnings("null") // After setting serial port parameters
    @Override
    public void connect(ValloxSEConfiguration config) throws IOException {
        if (isConnected()) {
            return;
        }
        try {
            logger.debug("Connecting to {}", config.serialPort);
            SerialPortIdentifier portIdentifier = portManager.getIdentifier(config.serialPort);
            if (portIdentifier == null) {
                throw new IOException("No such port " + config.serialPort);
            }
            serialPort = portIdentifier.open("vallox", SERIAL_PORT_READ_TIMEOUT);
            serialPort.setSerialPortParams(SERIAL_BAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            inputStream = new BufferedInputStream(serialPort.getInputStream());
            outputStream = serialPort.getOutputStream();
            panelNumber = config.getPanelAsByte();
            connected = true;

            serialPort.addEventListener(this);

            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);
            serialPort.notifyOnFramingError(true);
            serialPort.notifyOnOverrunError(true);
            serialPort.notifyOnParityError(true);

            serialPort.enableReceiveThreshold(1);
            serialPort.enableReceiveTimeout(2000);

            logger.debug("Connected to {}", config.serialPort);
        } catch (TooManyListenersException e) {
            throw new IOException("Too many listeners", e);
        } catch (PortInUseException e) {
            throw new IOException("Port in use", e);
        } catch (UnsupportedCommOperationException | IOException e) {
            throw new IOException("Unsupported com operation -> " + e.getMessage(), e);
        }
    }

    /**
     * Closes the serial port.
     */
    @SuppressWarnings("null") // serialPort.close()
    @Override
    public void close() {
        super.close();
        connected = false;
        if (serialPort != null) {
            serialPort.removeEventListener();
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                logger.debug("Failed to close serial port inputstream", ioe);
            }
            serialPort.close();
        }
        serialPort = null;
        connected = false;
        logger.debug("Serial connection closed");
    }

    @Override
    public void serialEvent(@Nullable SerialPortEvent seEvent) {
        if (seEvent == null) {
            return;
        }
        if (logger.isTraceEnabled() && SerialPortEvent.DATA_AVAILABLE != seEvent.getEventType()) {
            logger.trace("Serial event: {}, value:{}", seEvent.getEventType(), seEvent.getNewValue());
        }
        try {
            switch (seEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE:
                    handleDataAvailable();
                    break;
                case SerialPortEvent.BI:
                    sendErrorToListeners("Break interrupt " + seEvent.toString(), null);
                    break;
                case SerialPortEvent.FE:
                    sendErrorToListeners("Frame error " + seEvent.toString(), null);
                    break;
                case SerialPortEvent.OE:
                    sendErrorToListeners("Overrun error " + seEvent.toString(), null);
                    break;
                case SerialPortEvent.PE:
                    sendErrorToListeners("Parity error " + seEvent.toString(), null);
                    break;
                default: // do nothing
            }
        } catch (RuntimeException e) {
            logger.warn("RuntimeException during handling serial event: {}", seEvent.getEventType(), e);
        }
    }

    /**
     * Read available data from input stream if its not null
     */
    @SuppressWarnings("null") // inputStream
    private void handleDataAvailable() {
        try {
            if (inputStream != null) {
                while (inputStream.available() > 0) {
                    buffer.add((byte) inputStream.read());
                }
                handleBuffer();
            }
        } catch (IOException | InterruptedException e) {
            logger.debug("Exception while handling available data ", e);
        } catch (IllegalStateException e) {
            logger.warn("Read buffer full. Cleaning.");
            buffer.clear();
        }
    }
}
