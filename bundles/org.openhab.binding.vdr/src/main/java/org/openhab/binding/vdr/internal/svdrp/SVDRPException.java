/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.vdr.internal.svdrp;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link SVDRPException} is thrown in case of Failure of SVDRP Handling
 *
 * @author Matthias Klocke - Initial contribution
 */
@NonNullByDefault
public abstract class SVDRPException extends Exception {

    private static final long serialVersionUID = 3816136415994156427L;

    public SVDRPException(@Nullable String message) {
        super(message);
    }

    public SVDRPException(@Nullable String message, Throwable cause) {
        super(message, cause);
    }
}
