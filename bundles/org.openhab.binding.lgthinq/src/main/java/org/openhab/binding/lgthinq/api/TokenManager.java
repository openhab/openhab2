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

import static org.openhab.binding.lgthinq.internal.LGThinqBindingConstants.GATEWAY_URL;
import static org.openhab.binding.lgthinq.internal.LGThinqBindingConstants.THINQ_CONNECTION_DATA_FILE;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.openhab.binding.lgthinq.errors.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@link TokenManager} Principal facade to manage all token handles
 *
 * @author Nemer Daud - Initial contribution
 */
public class TokenManager {
    private static final int EXPIRICY_TOLERANCE_SEC = 60;
    private final OauthLgEmpAuthenticator oAuthAuthenticator;
    private TokenResult tokenCached;
    private static final TokenManager instance;
    static {
        instance = new TokenManager();
    }

    private TokenManager() {
        oAuthAuthenticator = OauthLgEmpAuthenticator.getInstance();
    }

    public static TokenManager getInstance() {
        return instance;
    }

    public boolean isTokenExpired(TokenResult token) {
        Calendar c = Calendar.getInstance();
        c.setTime(token.getGeneratedTime());
        c.add(Calendar.SECOND, token.getExpiresIn() - EXPIRICY_TOLERANCE_SEC);
        Date expiricyDate = c.getTime();
        return expiricyDate.before(new Date());
    }

    public TokenResult refreshToken(TokenResult currentToken) throws RefreshTokenException {
        try {
            TokenResult token = oAuthAuthenticator.doRefreshToken(currentToken);
            new ObjectMapper().writeValue(new File(THINQ_CONNECTION_DATA_FILE), token);
            return token;
        } catch (IOException e) {
            throw new RefreshTokenException("Error refreshing LGThinq token", e);
        }
    }

    public boolean isOauthTokenRegistered() {
        File tokenFile = new File(THINQ_CONNECTION_DATA_FILE);
        // TODO - check if the file content is valid.
        return tokenFile.isFile();
    }

    public TokenResult oauthFirstRegistration(String language, String country, String username, String password)
            throws LGGatewayException, PreLoginException, AccountLoginException, TokenException, IOException {
        Gateway gw;
        OauthLgEmpAuthenticator.PreLoginResult preLogin;
        OauthLgEmpAuthenticator.LoginAccountResult accountLogin;
        TokenResult token;
        UserInfo userInfo;
        try {
            gw = oAuthAuthenticator.discoverGatewayConfiguration(GATEWAY_URL, language, country);
        } catch (Exception ex) {
            throw new LGGatewayException("Error trying to discovery the LG Gateway Setting for the region informed",
                    ex);
        }
        try {
            preLogin = oAuthAuthenticator.preLoginUser(gw, username, password);
        } catch (Exception ex) {
            throw new PreLoginException("Error doing pre-login of the user in the Emp LG Server", ex);
        }
        try {
            accountLogin = oAuthAuthenticator.loginUser(gw, preLogin);
        } catch (Exception ex) {
            throw new AccountLoginException("Error doing user's account login on the Emp LG Server", ex);
        }
        try {
            token = oAuthAuthenticator.getToken(gw, accountLogin);
        } catch (Exception ex) {
            throw new TokenException("Error getting Token", ex);
        }
        try {
            userInfo = oAuthAuthenticator.getUserInfo(token);
            token.setUserInfo(userInfo);
            token.setGatewayInfo(gw);
        } catch (Exception ex) {
            throw new TokenException("Error getting UserInfo from Token", ex);
        }

        // persist the token information generated in file
        new ObjectMapper().writeValue(new File(THINQ_CONNECTION_DATA_FILE), token);
        return token;
    }

    public TokenResult getValidRegisteredToken() throws IOException, RefreshTokenException {
        if (tokenCached == null) {
            tokenCached = new ObjectMapper().readValue(new File(THINQ_CONNECTION_DATA_FILE), TokenResult.class);
        }
        if (isTokenExpired(tokenCached)) {
            tokenCached = refreshToken(tokenCached);
        }
        return tokenCached;
    }

    /**
     * Remove the toke file registered for the bridge. Must be called only if the bridge is removed
     */
    public void cleanupTokenRegistry() {
        File f = new File(THINQ_CONNECTION_DATA_FILE);
        if (f.isFile()) {
            f.delete();
        }
    }
}
