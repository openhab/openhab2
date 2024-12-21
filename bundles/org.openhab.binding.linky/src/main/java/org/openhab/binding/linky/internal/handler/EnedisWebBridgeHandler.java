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

import java.net.HttpCookie;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openhab.binding.linky.internal.LinkyConfiguration;
import org.openhab.binding.linky.internal.LinkyException;
import org.openhab.binding.linky.internal.dto.AuthData;
import org.openhab.binding.linky.internal.dto.AuthResult;
import org.openhab.core.auth.client.oauth2.OAuthFactory;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ThingRegistry;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * {@link EnedisBridgeHandler} is the base handler to access enedis data.
 *
 * @author Laurent Arnal - Initial contribution
 *
 */
@NonNullByDefault
public class EnedisWebBridgeHandler extends LinkyBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(EnedisWebBridgeHandler.class);

    public static final String ENEDIS_DOMAIN = ".enedis.fr";

    private static final String BASE_URL = "https://alex.microapplications" + ENEDIS_DOMAIN;

    public static final String URL_MON_COMPTE = "https://mon-compte" + ENEDIS_DOMAIN;
    public static final String URL_COMPTE_PART = URL_MON_COMPTE.replace("compte", "compte-particulier");
    public static final URI COOKIE_URI = URI.create(URL_COMPTE_PART);

    private static final String USER_INFO_CONTRACT_URL = BASE_URL + "/mon-compte-client/api/private/v1/userinfos";
    private static final String USER_INFO_URL = BASE_URL + "/userinfos";
    private static final String PRM_INFO_BASE_URL = BASE_URL + "/mes-mesures-prm/api/private/v1/personnes/";
    private static final String PRM_INFO_URL = BASE_URL + "/mes-prms-part/api/private/v2/personnes/%s/prms";

    private static final String MEASURE_DAILY_CONSUMPTION_URL = PRM_INFO_BASE_URL
            + "%s/prms/%s/donnees-energetiques?mesuresTypeCode=ENERGIE&mesuresCorrigees=false&typeDonnees=CONS";

    private static final String MEASURE_MAX_POWER_URL = PRM_INFO_BASE_URL
            + "%s/prms/%s/donnees-energetiques?mesuresTypeCode=PMAX&mesuresCorrigees=false&typeDonnees=CONS";

    private static final String LOAD_CURVE_CONSUMPTION_URL = PRM_INFO_BASE_URL
            + "%s/prms/%s/donnees-energetiques?mesuresTypeCode=COURBE&mesuresCorrigees=false&typeDonnees=CONS&dateDebut=%s";

    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter API_DATE_FORMAT_YEAR_FIRST = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String URL_ENEDIS_AUTHENTICATE = BASE_URL + "/authenticate?target=" + URL_COMPTE_PART;

    private static final Pattern REQ_PATTERN = Pattern.compile("ReqID%(.*?)%26");

    private static final String BASE_MYELECT_URL = "https://www.myelectricaldata.fr/";
    private static final String TEMPO_URL = BASE_MYELECT_URL + "rte/tempo/%s/%s";

    public EnedisWebBridgeHandler(Bridge bridge, final @Reference HttpClientFactory httpClientFactory,
            final @Reference OAuthFactory oAuthFactory, final @Reference HttpService httpService,
            final @Reference ThingRegistry thingRegistry, ComponentContext componentContext, Gson gson) {
        super(bridge, httpClientFactory, oAuthFactory, httpService, thingRegistry, componentContext, gson);
    }

    @Override
    public String getToken(LinkyHandler handler) throws LinkyException {
        return "";
    }

    @Override
    public double getDivider() {
        return 1.00;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public String getContactUrl() {
        return USER_INFO_URL;
    }

    @Override
    public String getContractUrl() {
        return PRM_INFO_URL;
    }

    @Override
    public String getIdentityUrl() {
        return USER_INFO_URL;
    }

    @Override
    public String getAddressUrl() {
        return "";
    }

    @Override
    public String getDailyConsumptionUrl() {
        return MEASURE_DAILY_CONSUMPTION_URL;
    }

    @Override
    public String getMaxPowerUrl() {
        return MEASURE_MAX_POWER_URL;
    }

    @Override
    public String getLoadCurveUrl() {
        return LOAD_CURVE_CONSUMPTION_URL;
    }

    @Override
    public String getTempoUrl() {
        return TEMPO_URL;
    }

    @Override
    public DateTimeFormatter getApiDateFormat() {
        return API_DATE_FORMAT;
    }

    @Override
    public DateTimeFormatter getApiDateFormatYearsFirst() {
        return API_DATE_FORMAT_YEAR_FIRST;
    }

    @Override
    public synchronized void connectionInit() throws LinkyException {
        LinkyConfiguration lcConfig = config;
        if (lcConfig == null) {
            return;
        }

        logger.debug("Starting login process for user: {}", lcConfig.username);

        try {
            enedisApi.addCookie(LinkyConfiguration.INTERNAL_AUTH_ID, lcConfig.internalAuthId);
            logger.debug("Step 1: getting authentification");
            String data = enedisApi.getContent(URL_ENEDIS_AUTHENTICATE);

            logger.debug("Reception request SAML");
            Document htmlDocument = Jsoup.parse(data);
            Element el = htmlDocument.select("form").first();
            Element samlInput = el.select("input[name=SAMLRequest]").first();

            logger.debug("Step 2: send SSO SAMLRequest");
            ContentResponse result = httpClient.POST(el.attr("action"))
                    .content(enedisApi.getFormContent("SAMLRequest", samlInput.attr("value"))).send();
            if (result.getStatus() != HttpStatus.FOUND_302) {
                throw new LinkyException("Connection failed step 2");
            }

            logger.debug("Get the location and the ReqID");
            Matcher m = REQ_PATTERN.matcher(enedisApi.getLocation(result));
            if (!m.find()) {
                throw new LinkyException("Unable to locate ReqId in header");
            }

            String reqId = m.group(1);
            String authenticateUrl = URL_MON_COMPTE
                    + "/auth/json/authenticate?realm=/enedis&forward=true&spEntityID=SP-ODW-PROD&goto=/auth/SSOPOST/metaAlias/enedis/providerIDP?ReqID%"
                    + reqId + "%26index%3Dnull%26acsURL%3D" + BASE_URL
                    + "/saml/SSO%26spEntityID%3DSP-ODW-PROD%26binding%3Durn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&AMAuthCookie=";

            logger.debug("Step 3: auth1 - retrieve the template, thanks to cookie internalAuthId user is already set");
            result = httpClient.POST(authenticateUrl).header("X-NoSession", "true").header("X-Password", "anonymous")
                    .header("X-Requested-With", "XMLHttpRequest").header("X-Username", "anonymous").send();
            if (result.getStatus() != HttpStatus.OK_200) {
                throw new LinkyException("Connection failed step 3 - auth1: %s", result.getContentAsString());
            }

            AuthData authData = gson.fromJson(result.getContentAsString(), AuthData.class);
            if (authData == null || authData.callbacks.size() < 2 || authData.callbacks.get(0).input.isEmpty()
                    || authData.callbacks.get(1).input.isEmpty() || !lcConfig.username
                            .equals(Objects.requireNonNull(authData.callbacks.get(0).input.get(0)).valueAsString())) {
                logger.debug("auth1 - invalid template for auth data: {}", result.getContentAsString());
                throw new LinkyException("Authentication error, the authentication_cookie is probably wrong");
            }

            authData.callbacks.get(1).input.get(0).value = lcConfig.password;
            logger.debug("Step 4: auth2 - send the auth data");
            result = httpClient.POST(authenticateUrl).header(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header("X-NoSession", "true").header("X-Password", "anonymous")
                    .header("X-Requested-With", "XMLHttpRequest").header("X-Username", "anonymous")
                    .content(new StringContentProvider(gson.toJson(authData))).send();
            if (result.getStatus() != HttpStatus.OK_200) {
                throw new LinkyException("Connection failed step 3 - auth2 : %s", result.getContentAsString());
            }

            AuthResult authResult = gson.fromJson(result.getContentAsString(), AuthResult.class);
            if (authResult == null) {
                throw new LinkyException("Invalid authentication result data");
            }

            logger.debug("Add the tokenId cookie");
            enedisApi.addCookie("enedisExt", authResult.tokenId);

            logger.debug("Step 5: retrieve the SAMLresponse");
            data = enedisApi.getContent(URL_MON_COMPTE + "/" + authResult.successUrl);
            htmlDocument = Jsoup.parse(data);
            el = htmlDocument.select("form").first();
            samlInput = el.select("input[name=SAMLResponse]").first();

            logger.debug("Step 6: post the SAMLresponse to finish the authentication");
            result = httpClient.POST(el.attr("action"))
                    .content(enedisApi.getFormContent("SAMLResponse", samlInput.attr("value"))).send();
            if (result.getStatus() != HttpStatus.FOUND_302) {
                throw new LinkyException("Connection failed step 6");
            }

            logger.debug("Step 7: retrieve ");
            result = httpClient.GET(USER_INFO_CONTRACT_URL);

            @SuppressWarnings("unchecked")
            HashMap<String, String> hashRes = gson.fromJson(result.getContentAsString(), HashMap.class);

            String cookieKey;
            if (hashRes != null && hashRes.containsKey("cnAlex")) {
                cookieKey = "personne_for_" + hashRes.get("cnAlex");
            } else {
                throw new LinkyException("Connection failed step 7, missing cookieKey");
            }

            List<HttpCookie> lCookie = httpClient.getCookieStore().getCookies();
            Optional<HttpCookie> cookie = lCookie.stream().filter(it -> it.getName().contains(cookieKey)).findFirst();

            String cookieVal = cookie.map(HttpCookie::getValue)
                    .orElseThrow(() -> new LinkyException("Connection failed step 7, missing cookieVal"));

            enedisApi.addCookie(cookieKey, cookieVal);

            connected = true;
        } catch (InterruptedException | TimeoutException | ExecutionException | JsonSyntaxException e) {
            throw new LinkyException(e, "Error opening connection with Enedis webservice");
        }
    }

    @Override
    public boolean supportNewApiFormat() {
        return false;
    }
}
