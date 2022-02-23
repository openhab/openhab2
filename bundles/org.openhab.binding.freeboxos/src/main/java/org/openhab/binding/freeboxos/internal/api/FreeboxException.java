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
package org.openhab.binding.freeboxos.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.freeboxos.internal.api.Response.ErrorCode;

/**
 * Exception for errors when using the Freebox API
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class FreeboxException extends Exception {
    private static final long serialVersionUID = 9197365222439228186L;
    private @Nullable ErrorCode errorCode;

    public FreeboxException(String msg) {
        super(msg);
    }

    public FreeboxException(String format, Object... args) {
        super(String.format(format, args));
    }

    public FreeboxException(Exception cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

    public FreeboxException(ErrorCode errorCode, String message, @Nullable Exception cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public FreeboxException(ErrorCode errorCode) {
        this(errorCode, errorCode.toString(), null);
    }

    public @Nullable ErrorCode getErrorCode() {
        return errorCode;
    }
}
