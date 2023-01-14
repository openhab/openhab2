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
package org.openhab.binding.millheat.internal.dto;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * This DTO class wraps the login response
 * 
 * @author Arne Seime - Initial contribution
 */
public class LoginResponse extends AbstractResponse {
    public String email;
    @SerializedName("nickName")
    public String nickname;
    public String phone;
    public String refreshToken;
    public Date refreshTokenExpire;
    public String token;
    public Date tokenExpire;
    public Integer userId;
}
