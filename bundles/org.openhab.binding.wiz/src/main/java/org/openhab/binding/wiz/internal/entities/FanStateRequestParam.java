/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.wiz.internal.entities;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.Expose;

/**
 * This POJO represents Fan State Request Param
 *
 * @author Stefan Fussenegger - Initial Contribution
 */
@NonNullByDefault
public class FanStateRequestParam implements Param {
    @Expose(serialize = true, deserialize = true)
    private boolean fanState; // true = 1, false = 0

    public FanStateRequestParam(boolean fanState) {
        this.fanState = fanState;
    }

    public boolean getFanState() {
        return fanState;
    }

    public void setFanState(boolean fanState) {
        this.fanState = fanState;
    }
}
