/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import org.openhab.binding.netatmo.internal.messages.NetatmoResponse;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class NetatmoException extends RuntimeException {

    private static final long serialVersionUID = 8172415590396198122L;
    protected NetatmoResponse<?> response;

    public NetatmoException(String message) {
        super(message);
    }

    public NetatmoException(final Throwable cause) {
        super(cause);
    }

    public NetatmoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetatmoException(NetatmoResponse<?> response) {
        super(response.getError().getMessage());
        this.response = response;
    }

    public NetatmoResponse<?> getResponse() {
        return response;
    }

}
