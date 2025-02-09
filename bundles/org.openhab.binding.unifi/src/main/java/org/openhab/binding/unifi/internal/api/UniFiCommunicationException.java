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
package org.openhab.binding.unifi.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiCommunicationException} signals there was a problem communicating with the controller.
 *
 * @author Matthew Bowman - Initial contribution
 */
@NonNullByDefault
public class UniFiCommunicationException extends UniFiException {

    private static final long serialVersionUID = 1L;

    public UniFiCommunicationException(final String message) {
        super(message);
    }

    public UniFiCommunicationException(final Throwable cause) {
        super(cause);
    }
}
