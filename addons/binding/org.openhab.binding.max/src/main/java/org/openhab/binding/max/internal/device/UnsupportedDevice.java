/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.max.internal.device;

/**
 * Unsupported devices.
 *
 * @author Andreas Heil (info@aheil.de)
 * @author Marcel Verpaalen
 * @since 1.4.0
 */

public class UnsupportedDevice extends Device {

    public UnsupportedDevice(DeviceConfiguration c) {
        super(c);
    }

    @Override
    public DeviceType getType() {
        return DeviceType.Invalid;
    }

    @Override
    public String getName() {
        return "Unsupported device";
    }

}
