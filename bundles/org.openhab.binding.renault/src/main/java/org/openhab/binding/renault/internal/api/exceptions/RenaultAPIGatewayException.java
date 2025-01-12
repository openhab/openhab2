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
package org.openhab.binding.renault.internal.api.exceptions;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Exception thrown while trying to access the My Renault service which
 * is unavailable temporarily
 * 
 * @author Doug Culnane - Initial contribution
 */
@NonNullByDefault
public class RenaultAPIGatewayException extends RenaultException {

    private static final long serialVersionUID = 1L;

    public RenaultAPIGatewayException(String message) {
        super(message);
    }
}
