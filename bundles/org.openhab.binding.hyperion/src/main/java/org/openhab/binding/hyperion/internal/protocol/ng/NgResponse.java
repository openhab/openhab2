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
package org.openhab.binding.hyperion.internal.protocol.ng;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link NgResponse} is a POJO for a response from the Hyperion.ng server.
 *
 * @author Daniel Walters - Initial contribution
 */
public class NgResponse {

    @SerializedName("command")
    private String command;

    @SerializedName("success")
    private boolean success;

    @SerializedName("tan")
    private int tan;

    @SerializedName("info")
    private NgInfo info;

    @SerializedName("error")
    private String error;

    public String getCommand() {
        return command;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTan() {
        return tan;
    }

    public NgInfo getInfo() {
        return info;
    }

    public String getError() {
        return error;
    }
}
