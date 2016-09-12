/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zway.config;

import static org.openhab.binding.zway.ZWayBindingConstants.DEVICE_CONFIG_VIRTUAL_DEVICE_ID;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The {@link ZWayZAutomationDeviceConfiguration} class defines the model for a Z-Way device configuration.
 *
 * @author Patrick Hecker - Initial contribution
 */
public class ZWayZAutomationDeviceConfiguration {
    private String mDeviceId;

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(DEVICE_CONFIG_VIRTUAL_DEVICE_ID, this.getDeviceId()).toString();
    }
}
