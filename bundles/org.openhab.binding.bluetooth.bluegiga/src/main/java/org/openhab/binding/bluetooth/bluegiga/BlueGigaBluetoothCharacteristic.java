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
package org.openhab.binding.bluetooth.bluegiga;

import java.util.UUID;

import org.openhab.binding.bluetooth.BluetoothCharacteristic;

/**
 * The {@link BlueGigaBluetoothCharacteristic} class extends BluetoothCharacteristic
 * to provide write access to certain BluetoothCharacteristic fields that BlueGiga
 * may not be initially aware of during characteristic construction but must be discovered
 * later.
 *
 * @author Connor Petty - Initial contribution
 *
 */
public class BlueGigaBluetoothCharacteristic extends BluetoothCharacteristic {

    private boolean notificationEnabled;

    public BlueGigaBluetoothCharacteristic(int handle) {
        super(null, handle);
    }

    public void setProperties(int properties) {
        this.properties = properties;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean enable) {
        this.notificationEnabled = enable;
    }
}
