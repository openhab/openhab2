/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.bluetooth.bluegiga.internal.command.system;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaResponse;

/**
 * Class to implement the BlueGiga command <b>noLicenseKeyEvent</b>.
 * <p>
 * This error is produced when no valid license key found form the Smart hardware. When there is
 * no Bluetooth valid license key the Bluetooth radio will not be operational. A new license key
 * can be requested from the Bluegiga Technical Support.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaNoLicenseKeyEvent extends BlueGigaResponse {
    public static final int COMMAND_CLASS = 0x00;
    public static final int COMMAND_METHOD = 0x05;

    /**
     * Event constructor
     */
    public BlueGigaNoLicenseKeyEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
    }

    @Override
    public String toString() {
        return "BlueGigaNoLicenseKeyEvent []";
    }
}
