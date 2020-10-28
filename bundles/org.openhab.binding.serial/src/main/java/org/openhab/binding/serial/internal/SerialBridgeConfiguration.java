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
package org.openhab.binding.serial.internal;

import org.openhab.core.io.transport.serial.SerialPort;

/**
 * Class describing the serial bridge user configuration
 *
 * @author Mike Major - Initial contribution
 */
public class SerialBridgeConfiguration {
    /**
     * Serial port name
     */
    public String serialPort;

    /**
     * Serial port baud rate
     */
    public int baudRate;

    /**
     * Serial port data bits
     */
    public int dataBits;

    /**
     * Serial port parity
     */
    public String parity;

    /**
     * Serial port stop bits
     */
    public String stopBits;

    /**
     * Charset
     */
    public String charset;

    /**
     * Convert the config parity value to an int required for serial port configuration
     */
    public int getParityAsInt() {
        int parityInt;

        switch (parity) {
            case "N":
                parityInt = SerialPort.PARITY_NONE;
                break;
            case "O":
                parityInt = SerialPort.PARITY_ODD;
                break;
            case "E":
                parityInt = SerialPort.PARITY_EVEN;
                break;
            case "M":
                parityInt = SerialPort.PARITY_MARK;
                break;
            case "S":
                parityInt = SerialPort.PARITY_SPACE;
                break;
            default:
                parityInt = SerialPort.PARITY_NONE;
                break;
        }

        return parityInt;
    }

    /**
     * Convert the config stop bits value to an int required for serial port configuration
     */
    public int getStopBitsAsInt() {
        int stopBitsAsInt;

        switch (stopBits) {
            case "1":
                stopBitsAsInt = SerialPort.STOPBITS_1;
                break;
            case "1.5":
                stopBitsAsInt = SerialPort.STOPBITS_1_5;
                break;
            case "2":
                stopBitsAsInt = SerialPort.STOPBITS_2;
                break;
            default:
                stopBitsAsInt = SerialPort.STOPBITS_1;
                break;
        }

        return stopBitsAsInt;
    }

    @Override
    public String toString() {
        return "SerialBridgeConfiguration [serialPort=" + serialPort + ", Baudrate=" + baudRate + ", Databits="
                + dataBits + ", Parity=" + parity + ", Stopbits=" + stopBits + "charset=" + charset + "]";
    }
}
