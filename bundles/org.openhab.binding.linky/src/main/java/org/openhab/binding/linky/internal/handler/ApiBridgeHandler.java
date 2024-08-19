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
package org.openhab.binding.linky.internal.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.linky.internal.LinkyAuthServlet;
import org.openhab.binding.linky.internal.LinkyBindingConstants;
import org.openhab.binding.linky.internal.LinkyException;
import org.openhab.binding.linky.internal.dto.Contracts;
import org.openhab.binding.linky.internal.dto.CustomerIdResponse;
import org.openhab.binding.linky.internal.dto.CustomerReponse;
import org.openhab.binding.linky.internal.dto.IdentityInfo;
import org.openhab.core.auth.client.oauth2.AccessTokenResponse;
import org.openhab.core.auth.client.oauth2.OAuthClientService;
import org.openhab.core.auth.client.oauth2.OAuthException;
import org.openhab.core.auth.client.oauth2.OAuthFactory;
import org.openhab.core.auth.client.oauth2.OAuthResponseException;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingStatus;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * {@link ApiBridgeHandler} is the base handler to access enedis data.
 *
 * @author Laurent Arnal - Initial contribution
 */
@NonNullByDefault
public abstract class ApiBridgeHandler extends LinkyBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(ApiBridgeHandler.class);

    private final OAuthClientService oAuthService;

    private static @Nullable HttpServlet servlet;

    public ApiBridgeHandler(Bridge bridge, final @Reference HttpClientFactory httpClientFactory,
            final @Reference OAuthFactory oAuthFactory, final @Reference HttpService httpService,
            final @Reference ThingRegistry thingRegistry, ComponentContext componentContext, Gson gson) {
        super(bridge, httpClientFactory, oAuthFactory, httpService, thingRegistry, componentContext, gson);

        String tokenUrl = "";
        String authorizeUrl = "";
        if (this instanceof MyElectricalDataBridgeHandler) {
            tokenUrl = LinkyBindingConstants.LINKY_MYELECTRICALDATA_API_TOKEN_URL;
            authorizeUrl = LinkyBindingConstants.LINKY_MYELECTRICALDATA_AUTHORIZE_URL;
        } else if (this instanceof EnedisBridgeHandler) {
            tokenUrl = LinkyBindingConstants.ENEDIS_API_TOKEN_URL_PREPROD;
            authorizeUrl = LinkyBindingConstants.ENEDIS_AUTHORIZE_URL_PREPROD;
        }

        this.oAuthService = oAuthFactory.createOAuthClientService(LinkyBindingConstants.BINDING_ID, tokenUrl,
                authorizeUrl, getClientId(), getClientSecret(), LinkyBindingConstants.LINKY_SCOPES, true);

        registerServlet();

        updateStatus(ThingStatus.UNKNOWN);
    }

    public abstract String getClientId();

    public abstract String getClientSecret();

    private void registerServlet() {
        try {
            if (servlet == null) {
                servlet = createServlet();

                httpService.registerServlet(LinkyBindingConstants.LINKY_ALIAS, servlet, new Hashtable<>(),
                        httpService.createDefaultHttpContext());
                httpService.registerResources(LinkyBindingConstants.LINKY_ALIAS + LinkyBindingConstants.LINKY_IMG_ALIAS,
                        "web", null);
            }
        } catch (NamespaceException | ServletException | LinkyException e) {
            logger.warn("Error during linky servlet startup", e);
        }
    }

    @Override
    public void dispose() {
        httpService.unregister(LinkyBindingConstants.LINKY_ALIAS);
        httpService.unregister(LinkyBindingConstants.LINKY_ALIAS + LinkyBindingConstants.LINKY_IMG_ALIAS);

        super.dispose();
    }

    /**
     * Creates a new {@link LinkyAuthServlet}.
     *
     * @return the newly created servlet
     * @throws IOException thrown when an HTML template could not be read
     */
    private HttpServlet createServlet() throws LinkyException {
        return new LinkyAuthServlet(this);
    }

    public String authorize(String redirectUri, String reqState, String reqCode) throws LinkyException {
        // Will work only in case of direct oAuth2 authentification to enedis
        // this is not the case in v1 as we go trough MyElectricalData

        try {
            logger.debug("Make call to Enedis to get access token.");
            final AccessTokenResponse credentials = oAuthService
                    .getAccessTokenByClientCredentials(LinkyBindingConstants.LINKY_SCOPES);

            String accessToken = credentials.getAccessToken();

            logger.debug("Acces token: {}", accessToken);
            return accessToken;
        } catch (RuntimeException | OAuthException | IOException e) {
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, e.getMessage());
            throw new LinkyException("Error during oAuth authorize :" + e.getMessage(), e);
        } catch (final OAuthResponseException e) {
            throw new LinkyException("\"Error during oAuth authorize :" + e.getMessage(), e);
        }
    }

    public boolean isAuthorized() {
        final AccessTokenResponse accessTokenResponse = getAccessTokenResponse();

        return accessTokenResponse != null && accessTokenResponse.getAccessToken() != null
                && accessTokenResponse.getRefreshToken() != null;
    }

    protected @Nullable AccessTokenResponse getAccessTokenByClientCredentials() {
        try {
            return oAuthService.getAccessTokenByClientCredentials(LinkyBindingConstants.LINKY_SCOPES);
        } catch (OAuthException | IOException | OAuthResponseException | RuntimeException e) {
            logger.debug("Exception checking authorization: ", e);
            return null;
        }
    }

    protected @Nullable AccessTokenResponse getAccessTokenResponse() {
        try {
            return oAuthService.getAccessTokenResponse();
        } catch (OAuthException | IOException | OAuthResponseException | RuntimeException e) {
            logger.debug("Exception checking authorization: ", e);
            return null;
        }
    }

    public String formatAuthorizationUrl(String redirectUri) {
        try {
            String uri = this.oAuthService.getAuthorizationUrl(redirectUri, LinkyBindingConstants.LINKY_SCOPES,
                    LinkyBindingConstants.BINDING_ID);
            return uri;
        } catch (final OAuthException e) {
            logger.debug("Error constructing AuthorizationUrl: ", e);
            return "";
        }
    }

    public List<String> getAllPrmId() {
        List<String> result = new ArrayList<>();

        Collection<Thing> col = this.thingRegistry.getAll();

        for (Thing thing : col) {
            if (LinkyBindingConstants.THING_TYPE_LINKY.equals(thing.getThingTypeUID())) {
                Configuration config = thing.getConfiguration();

                String prmId = (String) config.get("prmId");
                result.add(prmId);
            }
        }

        return result;
    }

    @Override
    public Contracts decodeCustomerResponse(String data, String prmId) throws LinkyException {
        try {
            CustomerReponse cResponse = gson.fromJson(data, CustomerReponse.class);
            if (cResponse == null) {
                throw new LinkyException("Invalid customer data received");
            }
            return cResponse.customer;
        } catch (JsonSyntaxException e) {
            logger.debug("invalid JSON response not matching CustomerReponse.class: {}", data);
            throw new LinkyException(e, "Requesting '%s' returned an invalid JSON response");
        }
    }

    @Override
    public IdentityInfo decodeIdentityResponse(String data, String prmId) throws LinkyException {
        try {
            CustomerIdResponse iResponse = gson.fromJson(data, CustomerIdResponse.class);
            if (iResponse == null) {
                throw new LinkyException("Invalid customer data received");
            }
            return iResponse.identity.naturalPerson;
        } catch (JsonSyntaxException e) {
            logger.debug("invalid JSON response not matching CustomerIdResponse.class: {}", data);
            throw new LinkyException(e, "Requesting '%s' returned an invalid JSON response");
        }
    }
}
