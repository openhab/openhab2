/**
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
package org.openhab.binding.solarmax.internal.connector;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link SolarMaxException} Exception is used for connection problems trying to communicate with the SolarMax
 * device.
 *
 * @author Jamie Townsend - Initial contribution
 */
@NonNullByDefault
public class SolarMaxConnectionException extends SolarMaxException {

    private static final long serialVersionUID = 1L;

    public SolarMaxConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SolarMaxConnectionException(final Throwable cause) {
        super(cause);
    }
}
