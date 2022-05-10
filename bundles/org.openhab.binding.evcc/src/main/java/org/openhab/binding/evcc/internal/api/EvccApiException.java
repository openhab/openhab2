/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.evcc.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link EvccApiException} signals that an API request by {@link EvccAPI} failed.
 * 
 * @author Florian Hotze - Initial contribution
 */
@NonNullByDefault
public class EvccApiException extends Exception {

    private static final long serialVersionUID = 0L; // uniquely generated UID

    public EvccApiException(String message) {
        super(message);
    }

    public EvccApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
