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
package org.openhab.binding.modbus.sunspec.internal.detector;

/**
 * Descriptor for a model block found on the device
 *
 * @author Nagy Attila Gabor - Initial contribution
 */
public class ModelBlock {

    /**
     * Base address of this block in 16bit words
     */
    public int address;

    /**
     * Length of this block in 16bit words
     */
    public int length;

    /**
     * Module identifier
     */
    public int moduleID;

    @Override
    public String toString() {
        return String.format("ModelBlock type=%d address=%d length=%d", moduleID, address, length);
    }
}
