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
package org.openhab.binding.lgthinq.api;

import java.io.Serializable;
import java.util.Date;

/**
 * The {@link TokenResult} Hold information about token and related entities
 *
 * @author Nemer Daud - Initial contribution
 */
public class TokenResult implements Serializable {
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private Date generatedTime;
    private String oauthBackendUrl;
    private UserInfo userInfo;
    private Gateway gatewayInfo;

    public TokenResult(String accessToken, String refreshToken, int expiresIn, Date generatedTime,
            String ouathBackendUrl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.generatedTime = generatedTime;
        this.oauthBackendUrl = ouathBackendUrl;
    }

    public TokenResult() {
    };

    public Gateway getGatewayInfo() {
        return gatewayInfo;
    }

    public void setGatewayInfo(Gateway gatewayInfo) {
        this.gatewayInfo = gatewayInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public Date getGeneratedTime() {
        return generatedTime;
    }

    public String getOauthBackendUrl() {
        return oauthBackendUrl;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setGeneratedTime(Date generatedTime) {
        this.generatedTime = generatedTime;
    }

    public void setOauthBackendUrl(String ouathBackendUrl) {
        this.oauthBackendUrl = ouathBackendUrl;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
