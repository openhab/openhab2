/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.ecovacs.internal.api.impl.dto.request.portal;

import com.google.gson.annotations.SerializedName;

/**
 * @author Danny Baumann - Initial contribution
 */
public class PortalIotProductRequest {

    @SerializedName("todo")
    final String todo = "";

    @SerializedName("channel")
    final String channel = "";

    @SerializedName("auth")
    final PortalAuthRequestParameter auth;

    public PortalIotProductRequest(PortalAuthRequestParameter auth) {
        this.auth = auth;
    }
}
