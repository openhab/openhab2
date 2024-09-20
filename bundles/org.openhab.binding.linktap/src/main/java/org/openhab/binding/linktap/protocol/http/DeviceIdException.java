/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.linktap.protocol.http;

import java.io.Serial;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.linktap.protocol.frames.GatewayDeviceResponse;

/**
 * The {@link DeviceIdException} should be thrown when the endpoint being communicated with
 * does not appear to be a Tap Link Gateway device.
 *
 * @author David Goodyear - Initial contribution
 */
@NonNullByDefault
public class DeviceIdException extends Exception {
    @Serial
    private static final long serialVersionUID = -7786449325604153947L;

    private String i18Key = "";
    // case RET_DEVICE_ID_ERROR:
    // case RET_DEVICE_NOT_FOUND:

    public DeviceIdException() {
        super();
    }

    public DeviceIdException(final String message) {
        super(message);
    }

    public DeviceIdException(final Throwable cause) {
        super(cause);
    }

    public DeviceIdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DeviceIdException(final GatewayDeviceResponse.ResultStatus rs) {
        super(rs.getDesc());
        this.i18Key = rs.getI18Key();
    }

    public String getI18Key() {
        if (!i18Key.isBlank()) {
            return i18Key;
        }
        return Objects.requireNonNullElse(getMessage(), "Device ID Exception");
    }
}
