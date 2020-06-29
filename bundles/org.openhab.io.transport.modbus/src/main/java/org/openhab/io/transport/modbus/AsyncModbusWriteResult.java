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
package org.openhab.io.transport.modbus;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Encapsulates result of modbus write operations
 *
 * @author Sami Salonen - Initial contribution
 */
@NonNullByDefault
public class AsyncModbusWriteResult {

    private ModbusWriteRequestBlueprint request;

    @Nullable
    private ModbusResponse response;

    public AsyncModbusWriteResult(ModbusWriteRequestBlueprint request, ModbusResponse response) {
        Objects.requireNonNull(request, "Request must not be null!");
        Objects.requireNonNull(response, "Response must not be null!");
        this.request = request;
        this.response = response;
    }

    /**
     * Get request matching this response
     *
     * @return request object
     */
    public ModbusWriteRequestBlueprint getRequest() {
        return request;
    }

    /**
     * Get response
     *
     * @return response
     */
    @Nullable
    public ModbusResponse getResponse() {
        return response;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("AsyncModbusWriteResult(");
        builder.append("request = ");
        builder.append(request);
        if (response != null) {
            builder.append(", response = ");
            builder.append(response);
        }
        builder.append(")");
        return builder.toString();
    }
}
