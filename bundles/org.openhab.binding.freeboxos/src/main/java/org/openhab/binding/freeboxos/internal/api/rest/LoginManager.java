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
package org.openhab.binding.freeboxos.internal.api.rest;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.Response;
import org.openhab.binding.freeboxos.internal.api.rest.LoginManager.AuthorizationStatus.Status;
import org.openhab.binding.freeboxos.internal.api.rest.LoginManager.Session.Permission;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * The {@link LoginManager} is the Java class used to handle api requests related to session handling and login
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class LoginManager extends RestManager {
    private static final Bundle BUNDLE = FrameworkUtil.getBundle(LoginManager.class);
    private static final String APP_ID = BUNDLE.getSymbolicName();
    private static final String ALGORITHM = "HmacSHA1";
    private static final String PATH = "login";
    private static final String SESSION = "session";
    private static final String AUTHORIZE_ACTION = "authorize";
    private static final String LOGOUT = "logout";

    private final Mac mac;

    private static class AuthStatus extends Response<AuthorizationStatus> {
    }

    public static record AuthorizationStatus(Status status, boolean loggedIn, String challenge,
            @Nullable String passwordSalt, boolean passwordSet) {
        public static enum Status {
            PENDING, // the user has not confirmed the autorization request yet
            TIMEOUT, // the user did not confirmed the authorization within the given time
            GRANTED, // the app_token is valid and can be used to open a session
            DENIED, // the user denied the authorization request
            UNKNOWN; // the app_token is invalid or has been revoked
        }
    }

    private static class AuthResponse extends Response<Authorization> {
    }

    private static record Authorization(String appToken, int trackId) {
    }

    private static class SessionResponse extends Response<Session> {
    }

    public static record Session(Map<Permission, @Nullable Boolean> permissions, @Nullable String sessionToken) {

        public static enum Permission {
            PARENTAL,
            CONTACTS,
            EXPLORER,
            TV,
            WDO,
            DOWNLOADER,
            PROFILE,
            CAMERA,
            SETTINGS,
            CALLS,
            HOME,
            PVR,
            VM,
            PLAYER,
            NONE,
            UNKNOWN;
        }

        public boolean hasPermission(Permission checked) {
            return Boolean.TRUE.equals(permissions.get(checked));
        }
    }

    private static record OpenSessionData(String appId, String password) {
    }

    private static record AuthorizeData(String appId, String appName, String appVersion, String deviceName) {

        AuthorizeData(String appId, Bundle bundle) {
            this(appId, bundle.getHeaders().get("Bundle-Name"), bundle.getVersion().toString(),
                    bundle.getHeaders().get("Bundle-Vendor"));
        }
    }

    public LoginManager(FreeboxOsSession session) throws FreeboxException {
        super(session, Permission.NONE, session.getUriBuilder().path(PATH));
        try {
            mac = Mac.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Session openSession(String appToken) throws FreeboxException {
        AuthorizationStatus authorization = getSingle(AuthStatus.class);

        try {
            // Initialize mac with the signing key
            mac.init(new SecretKeySpec(appToken.getBytes(), mac.getAlgorithm()));
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(authorization.challenge().getBytes());
            // Convert raw bytes to Hex
            String password = printHexBinary(rawHmac).toLowerCase();
            return postSingle(new OpenSessionData(APP_ID, password), SessionResponse.class, SESSION);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void closeSession() throws FreeboxException {
        post(LOGOUT);
    }

    private Status trackAuthorize(int trackId) throws FreeboxException {
        return getSingle(AuthStatus.class, AUTHORIZE_ACTION, Integer.toString(trackId)).status();
    }

    public String grant() throws FreeboxException {
        Authorization authorize = postSingle(new AuthorizeData(APP_ID, BUNDLE), AuthResponse.class, AUTHORIZE_ACTION);
        Status track = Status.PENDING;
        try {
            while (Status.PENDING.equals(track)) {
                Thread.sleep(2000);
                track = trackAuthorize(authorize.trackId());
            }
            if (Status.GRANTED.equals(track)) {
                return authorize.appToken();
            }
            throw new FreeboxException("Unable to grant session");
        } catch (InterruptedException e) {
            throw new FreeboxException(e, "Granting process interrupted");
        }
    }
}
