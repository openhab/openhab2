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
package org.openhab.binding.unifi.internal.api;

/**
 * The {@link UniFiException} represents a binding specific {@link Exception}.
 *
 * @author Matthew Bowman - Initial contribution
 */
public class UniFiException extends Exception {

    private static final long serialVersionUID = -7422254981644510570L;

    public UniFiException(String message) {
        super(message);
    }

    public UniFiException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniFiException(Throwable cause) {
        super(cause);
    }

}
