/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.networkupstools.internal.nut;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Exception thrown in case of errors related to NUT data reading/writing.
 *
 * @author Hilbrand Bouwkamp - Initial contribution
 */
@NonNullByDefault
public class NutException extends Exception {

    private static final long serialVersionUID = 1L;

    public NutException(final String message) {
        super(message);
    }

    public NutException(final Exception e) {
        super(e);
    }
}
