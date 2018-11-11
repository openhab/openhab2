/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.transceiver;

import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.io.IOUtils;
import org.eclipse.smarthome.io.transport.serial.PortInUseException;
import org.eclipse.smarthome.io.transport.serial.SerialPort;
import org.eclipse.smarthome.io.transport.serial.SerialPortEvent;
import org.eclipse.smarthome.io.transport.serial.SerialPortEventListener;
import org.eclipse.smarthome.io.transport.serial.SerialPortIdentifier;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.eclipse.smarthome.io.transport.serial.UnsupportedCommOperationException;
import org.openhab.binding.enocean.internal.EnOceanBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel Weber - Initial contribution
 */
public class EnOceanSerialTransceiver extends EnOceanTransceiver implements SerialPortEventListener {

    protected String path;
    SerialPort serialPort;
    // gnu.io.SerialPort sp;
    private static final int ENOCEAN_DEFAULT_BAUD = 57600;

    private Logger logger = LoggerFactory.getLogger(EnOceanSerialTransceiver.class);
    private SerialPortManager serialPortManager;

    public EnOceanSerialTransceiver(String path, TransceiverErrorListener errorListener,
            ScheduledExecutorService scheduler, SerialPortManager serialPortManager) {
        super(errorListener, scheduler);
        this.path = path;
        this.serialPortManager = serialPortManager;
    }

    @Override
    public void Initialize()
            throws UnsupportedCommOperationException, PortInUseException, IOException, TooManyListenersException {

        // There is currently a bug in nrjavaserial (https://github.com/NeuronRobotics/nrjavaserial/pull/121) so
        // directly use RXTXCommDriver on windows os
        /*
         * if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1
         * && path.toLowerCase().indexOf("com") != -1) {
         * try {
         * RXTXCommDriver RXTXDriver = new RXTXCommDriver();
         * RXTXDriver.initialize();
         * sp = (gnu.io.SerialPort) RXTXDriver.getCommPort(path, CommPortIdentifier.PORT_SERIAL);
         *
         * sp.setSerialPortParams(ENOCEAN_DEFAULT_BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
         * SerialPort.PARITY_NONE);
         * sp.enableReceiveThreshold(1);
         * sp.enableReceiveTimeout(100); // In ms. Small values mean faster shutdown but more cpu usage.
         *
         * inputStream = sp.getInputStream();
         * outputStream = sp.getOutputStream();
         * } catch (gnu.io.UnsupportedCommOperationException e) {
         * throw new UnsupportedCommOperationException(e);
         * }
         * } else {
         */

        SerialPortIdentifier id = serialPortManager.getIdentifier(path);
        if (id == null) {
            throw new IOException("Could not find a gateway on given path '" + path + "', "
                    + serialPortManager.getIdentifiers().count() + " ports available.");
        }

        serialPort = id.open(EnOceanBindingConstants.BINDING_ID, 1000);
        serialPort.setSerialPortParams(ENOCEAN_DEFAULT_BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.enableReceiveThreshold(1);
        serialPort.enableReceiveTimeout(100); // In ms. Small values mean faster shutdown but more cpu usage.

        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
        // }
        logger.info("EnOceanSerialTransceiver initialized");
    }

    @Override
    public void ShutDown() {

        logger.debug("shutting down transceiver");
        super.ShutDown();

        if (outputStream != null) {
            logger.debug("Closing serial output stream");
            IOUtils.closeQuietly(outputStream);
        }
        if (inputStream != null) {
            logger.debug("Closeing serial input stream");
            IOUtils.closeQuietly(inputStream);
        }

        if (serialPort != null) {
            logger.debug("Closing serial port");
            serialPort.close();
        }

        /*
         * if (sp != null) {
         * logger.debug("Closing serial port");
         * sp.close();
         * }
         */

        // sp = null;

        serialPort = null;
        outputStream = null;
        inputStream = null;

        logger.info("Transceiver shutdown");

    }

    @Override
    public void serialEvent(SerialPortEvent event) {

        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            synchronized (this) {
                this.notify();
            }
        }
    }

    @Override
    protected int read(byte[] buffer, int length) {
        try {
            return this.inputStream.read(buffer, 0, length);
        } catch (IOException e) {
            return 0;
        }
    }
}
