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
package org.openhab.binding.linky.internal.api;

import java.net.HttpCookie;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.Fields;
import org.openhab.binding.linky.internal.LinkyException;
import org.openhab.binding.linky.internal.dto.ConsumptionReport;
import org.openhab.binding.linky.internal.dto.Contact;
import org.openhab.binding.linky.internal.dto.Contract;
import org.openhab.binding.linky.internal.dto.Identity;
import org.openhab.binding.linky.internal.dto.MeterReading;
import org.openhab.binding.linky.internal.dto.PrmDetail;
import org.openhab.binding.linky.internal.dto.PrmInfo;
import org.openhab.binding.linky.internal.dto.ResponseContact;
import org.openhab.binding.linky.internal.dto.ResponseContract;
import org.openhab.binding.linky.internal.dto.ResponseIdentity;
import org.openhab.binding.linky.internal.dto.ResponseMeter;
import org.openhab.binding.linky.internal.dto.ResponseTempo;
import org.openhab.binding.linky.internal.dto.UsagePoint;
import org.openhab.binding.linky.internal.dto.UserInfo;
import org.openhab.binding.linky.internal.handler.EnedisWebBridgeHandler;
import org.openhab.binding.linky.internal.handler.LinkyBridgeHandler;
import org.openhab.binding.linky.internal.handler.LinkyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * {@link EnedisHttpApi} wraps the Enedis Webservice.
 *
 * @author Gaël L'hopital - Initial contribution
 * @author Laurent Arnal - Rewrite addon to use official dataconect API
 */
@NonNullByDefault
public class EnedisHttpApi {

    private final Logger logger = LoggerFactory.getLogger(EnedisHttpApi.class);
    private final Gson gson;
    private final HttpClient httpClient;
    private final LinkyBridgeHandler linkyBridgeHandler;

    public EnedisHttpApi(LinkyBridgeHandler linkyBridgeHandler, Gson gson, HttpClient httpClient) {
        this.gson = gson;
        this.httpClient = httpClient;
        this.linkyBridgeHandler = linkyBridgeHandler;
    }

    public FormContentProvider getFormContent(String fieldName, String fieldValue) {
        Fields fields = new Fields();
        fields.put(fieldName, fieldValue);
        return new FormContentProvider(fields);
    }

    public void addCookie(String key, String value) {
        HttpCookie cookie = new HttpCookie(key, value);
        cookie.setDomain(EnedisWebBridgeHandler.ENEDIS_DOMAIN);
        cookie.setPath("/");
        httpClient.getCookieStore().add(EnedisWebBridgeHandler.COOKIE_URI, cookie);
    }

    public String getLocation(ContentResponse response) {
        return response.getHeaders().get(HttpHeader.LOCATION);
    }

    public String getContent(LinkyHandler handler, String url) throws LinkyException {
        return getContent(logger, linkyBridgeHandler, url, httpClient, linkyBridgeHandler.getToken(handler));
    }

    public String getContent(String url) throws LinkyException {
        return getContent(logger, linkyBridgeHandler, url, httpClient, "");
    }

    private static String getContent(Logger logger, LinkyBridgeHandler linkyBridgeHandler, String url,
            HttpClient httpClient, String token) throws LinkyException {
        try {
            Request request = httpClient.newRequest(url);

            request = request.agent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
            request = request.method(HttpMethod.GET);
            if (!token.isEmpty()) {
                request = request.header("Authorization", "" + token);
                request = request.header("Accept", "application/json");
            }

            ContentResponse result = request.send();
            if (result.getStatus() == 307) {
                String loc = result.getHeaders().get("Location");
                String newUrl = linkyBridgeHandler.getBaseUrl() + loc.substring(1);
                request = httpClient.newRequest(newUrl);
                request = request.method(HttpMethod.GET);
                result = request.send();

                if (result.getStatus() == 307) {
                    loc = result.getHeaders().get("Location");
                    String[] urlParts = loc.split("/");
                    if (urlParts.length < 4) {
                        throw new LinkyException("malformed url : %s", loc);
                    }
                    return urlParts[3];
                }
            }
            if (result.getStatus() != 200) {
                throw new LinkyException("Error requesting '%s': %s", url, result.getContentAsString());
            }
            String content = result.getContentAsString();
            logger.trace("getContent returned {}", content);
            return content;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new LinkyException(e, "Error getting url: '%s'", url);
        }
    }

    private <T> T getData(LinkyHandler handler, String url, Class<T> clazz) throws LinkyException {
        if (!linkyBridgeHandler.isConnected()) {
            linkyBridgeHandler.initialize();
        }

        String data = getContent(handler, url);

        if (data.isEmpty()) {
            throw new LinkyException("Requesting '%s' returned an empty response", url);
        }

        try {
            return Objects.requireNonNull(gson.fromJson(data, clazz));
        } catch (JsonSyntaxException e) {
            logger.debug("Invalid JSON response not matching {}: {}", clazz.getName(), data);
            throw new LinkyException(e, "Requesting '%s' returned an invalid JSON response", url);
        }
    }

    public PrmInfo getPrmInfo(LinkyHandler handler, String internId, String prmId) throws LinkyException {
        String prmInfoUrl = linkyBridgeHandler.getContractUrl().formatted(internId);
        PrmInfo[] prms = getData(handler, prmInfoUrl, PrmInfo[].class);
        if (prms.length < 1) {
            throw new LinkyException("Invalid prms data received");
        }
        return prms[0];
    }

    public PrmDetail getPrmDetails(LinkyHandler handler, String internId, String prmId) throws LinkyException {
        String prmInfoUrl = linkyBridgeHandler.getContractUrl();
        String url = prmInfoUrl.formatted(internId) + "/" + prmId
                + "?embed=SITALI&embed=SITCOM&embed=SITCON&embed=SYNCON";
        return getData(handler, url, PrmDetail.class);
    }

    public UserInfo getUserInfo(LinkyHandler handler) throws LinkyException {
        String userInfoUrl = linkyBridgeHandler.getContactUrl();
        return getData(handler, userInfoUrl, UserInfo.class);
    }

    public String formatUrl(String apiUrl, String prmId) {
        return apiUrl.formatted(prmId);
    }

    public Contract getContract(LinkyHandler handler, String prmId) throws LinkyException {
        String contractUrl = linkyBridgeHandler.getContractUrl().formatted(prmId);
        ResponseContract contractResponse = getData(handler, contractUrl, ResponseContract.class);
        return contractResponse.customer.usagePoint[0].contracts;
    }

    public UsagePoint getUsagePoint(LinkyHandler handler, String prmId) throws LinkyException {
        String addressUrl = linkyBridgeHandler.getAddressUrl().formatted(prmId);
        ResponseContract contractResponse = getData(handler, addressUrl, ResponseContract.class);
        return contractResponse.customer.usagePoint[0].usagePoint;
    }

    public Identity getIdentity(LinkyHandler handler, String prmId) throws LinkyException {
        String identityUrl = linkyBridgeHandler.getIdentityUrl().formatted(prmId);
        ResponseIdentity customerIdReponse = getData(handler, identityUrl, ResponseIdentity.class);
        String name = customerIdReponse.identity.naturalPerson.lastname;
        customerIdReponse.identity.naturalPerson.firstname = name.split(" ")[0];
        customerIdReponse.identity.naturalPerson.lastname = name.split(" ")[1];
        return customerIdReponse.identity.naturalPerson;
    }

    public Contact getContact(LinkyHandler handler, String prmId) throws LinkyException {
        String contactUrl = linkyBridgeHandler.getContactUrl().formatted(prmId);
        ResponseContact contactResponse = getData(handler, contactUrl, ResponseContact.class);
        return contactResponse.contact;
    }

    private MeterReading getMeasures(LinkyHandler handler, String apiUrl, String prmId, LocalDate from, LocalDate to)
            throws LinkyException {
        String dtStart = from.format(linkyBridgeHandler.getApiDateFormat());
        String dtEnd = to.format(linkyBridgeHandler.getApiDateFormat());
        String url = String.format(apiUrl, prmId, dtStart, dtEnd);

        if (handler.supportNewApiFormat()) {
            ResponseMeter meterResponse = getData(handler, url, ResponseMeter.class);
            return meterResponse.meterReading;
        } else {
            ConsumptionReport consomptionReport = getData(handler, url, ConsumptionReport.class);
            return MeterReading.convertFromComsumptionReport(consomptionReport);
        }
    }

    public MeterReading getEnergyData(LinkyHandler handler, String prmId, LocalDate from, LocalDate to)
            throws LinkyException {
        return getMeasures(handler, linkyBridgeHandler.getDailyConsumptionUrl(), prmId, from, to);
    }

    public MeterReading getLoadCurveData(LinkyHandler handler, String prmId, LocalDate from, LocalDate to)
            throws LinkyException {
        return getMeasures(handler, linkyBridgeHandler.getLoadCurveUrl(), prmId, from, to);
    }

    public MeterReading getPowerData(LinkyHandler handler, String prmId, LocalDate from, LocalDate to)
            throws LinkyException {
        return getMeasures(handler, linkyBridgeHandler.getMaxPowerUrl(), prmId, from, to);
    }

    public ResponseTempo getTempoData(LinkyHandler handler, LocalDate from, LocalDate to) throws LinkyException {
        String dtStart = from.format(linkyBridgeHandler.getApiDateFormatYearsFirst());
        String dtEnd = to.format(linkyBridgeHandler.getApiDateFormatYearsFirst());

        String url = String.format(linkyBridgeHandler.getTempoUrl(), dtStart, dtEnd);

        if (url.isEmpty()) {
            return new ResponseTempo();
        }

        ResponseTempo responseTempo = getData(handler, url, ResponseTempo.class);
        return responseTempo;
    }
}
