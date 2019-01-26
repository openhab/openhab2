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
package org.openhab.binding.modbus.sunspec.internal.block;

/**
 * This class contains information parsed from the Common Model block
 *
 * @author Nagy Attila Gabor - Initial contribution
 *
 */
public class CommonModelBlock extends AbstractSunSpecMessageBlock {

    /**
     * Value = 0x0001. Uniquely identifies this as a SunSpec Common Model Block
     */
    private int sunSpecDID;

    /**
     * Length of block in 16-bit registers
     */
    private int length;

    /**
     * Manufacturer specific value
     */
    private String manufacturer;

    /**
     * Manufacturer specific value
     */
    private String model;

    /**
     * Manufacturer specific value
     */
    private String version;

    /**
     * Manufacturer specific value
     */
    private String serialNumber;

    /**
     * Modbus unit ID
     */
    private int deviceAddress;

    public int getSunSpecDID() {
        return sunSpecDID;
    }

    public void setSunSpecDID(int sunSpecDID) {
        this.sunSpecDID = sunSpecDID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

}
