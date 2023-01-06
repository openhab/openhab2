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
package org.openhab.binding.qolsysiq.internal.client.dto.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the status of a zone
 *
 * @author Dan Cunningham - Initial contribution
 */
public enum ZoneStatus {
    @SerializedName("Active")
    ACTIVE,
    @SerializedName("Closed")
    CLOSED,
    @SerializedName("Open")
    OPEN,
    @SerializedName("Failure")
    FAILURE,
    @SerializedName("Idle")
    IDlE,
    @SerializedName("Tamper")
    TAMPER;
}
