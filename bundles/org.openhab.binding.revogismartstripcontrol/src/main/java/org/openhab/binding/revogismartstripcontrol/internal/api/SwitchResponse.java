/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.revogismartstripcontrol.internal.api;

import java.util.Objects;

/**
 * The class {@link SwitchResponse} describes the response when you switch a plug
 *
 * @author Andi Bräu - Initial contribution
 */
public class SwitchResponse {
    private int response;
    private int code;

    public SwitchResponse() {
    }

    public SwitchResponse(int response, int code) {
        this.response = response;
        this.code = code;
    }

    public int getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwitchResponse that = (SwitchResponse) o;
        return response == that.response &&
                code == that.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, code);
    }
}
