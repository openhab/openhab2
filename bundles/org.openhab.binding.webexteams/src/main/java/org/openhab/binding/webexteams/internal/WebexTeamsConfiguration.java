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
package org.openhab.binding.webexteams.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link WebexTeamsConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Tom Deckers - Initial contribution
 */
@NonNullByDefault
public class WebexTeamsConfiguration {

    // static strings used when interacting with Configuration.
    public static final String TOKEN = "token";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String AUTH_CODE = "authCode";
    public static final String REDIRECT_URL = "redirectUrl";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ROOM_ID = "roomId";

    /**
     * Webex team configuration
     */
    public String token = "";
    public String clientId = "";
    public String clientSecret = "";
    public String authCode = "";
    public String redirectUrl = "";
    public String refreshToken = "";
    public String roomId = "";
}
