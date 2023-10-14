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
package org.openhab.binding.mybmw.internal.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.openhab.binding.mybmw.internal.utils.HTTPConstants.*;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mybmw.internal.MyBMWConfiguration;
import org.openhab.binding.mybmw.internal.dto.auth.AuthQueryResponse;
import org.openhab.binding.mybmw.internal.dto.auth.AuthResponse;
import org.openhab.binding.mybmw.internal.dto.auth.ChinaPublicKeyResponse;
import org.openhab.binding.mybmw.internal.dto.auth.ChinaTokenExpiration;
import org.openhab.binding.mybmw.internal.dto.auth.ChinaTokenResponse;
import org.openhab.binding.mybmw.internal.util.FileReader;
import org.openhab.binding.mybmw.internal.utils.BimmerConstants;
import org.openhab.binding.mybmw.internal.utils.Constants;
import org.openhab.binding.mybmw.internal.utils.Converter;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AuthTest} test authorization flow
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
class AuthTest {
    private final Logger logger = LoggerFactory.getLogger(AuthTest.class);

    void testAuth() {
        String user = "usr";
        String pwd = "pwd";

        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        HttpClient authHttpClient = new HttpClient(sslContextFactory);
        try {
            authHttpClient.start();
            Request firstRequest = authHttpClient
                    .newRequest("https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_ROW)
                            + "/eadrax-ucs/v1/presentation/oauth/config");
            firstRequest.header("ocp-apim-subscription-key",
                    BimmerConstants.OCP_APIM_KEYS.get(BimmerConstants.REGION_ROW));
            firstRequest.header("x-user-agent", "android(v1.07_20200330);bmw;1.7.0(11152)");

            ContentResponse firstResponse = firstRequest.send();
            logger.info(firstResponse.getContentAsString());
            AuthQueryResponse aqr = Converter.getGson().fromJson(firstResponse.getContentAsString(),
                    AuthQueryResponse.class);

            // String verifier_bytes = RandomStringUtils.randomAlphanumeric(64);
            String verifierBytes = Converter.getRandomString(64);
            String codeVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(verifierBytes.getBytes());

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

            // String state_bytes = RandomStringUtils.randomAlphanumeric(16);
            String stateBytes = Converter.getRandomString(16);
            String state = Base64.getUrlEncoder().withoutPadding().encodeToString(stateBytes.getBytes());

            String authUrl = aqr.gcdmBaseUrl + BimmerConstants.OAUTH_ENDPOINT;
            logger.info(authUrl);
            Request loginRequest = authHttpClient.POST(authUrl);
            loginRequest.header("Content-Type", "application/x-www-form-urlencoded");

            MultiMap<String> baseParams = new MultiMap<String>();
            baseParams.put("client_id", aqr.clientId);
            baseParams.put("response_type", "code");
            baseParams.put("redirect_uri", aqr.returnUrl);
            baseParams.put("state", state);
            baseParams.put("nonce", "login_nonce");
            baseParams.put("scope", String.join(" ", aqr.scopes));
            baseParams.put("code_challenge", codeChallenge);
            baseParams.put("code_challenge_method", "S256");

            MultiMap<String> loginParams = new MultiMap<String>(baseParams);
            loginParams.put("grant_type", "authorization_code");
            loginParams.put("username", user);
            loginParams.put("password", pwd);
            loginRequest.content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED,
                    UrlEncoded.encode(loginParams, StandardCharsets.UTF_8, false), StandardCharsets.UTF_8));
            ContentResponse secondResonse = loginRequest.send();
            logger.info(secondResonse.getContentAsString());
            String authCode = getAuthCode(secondResonse.getContentAsString());
            logger.info(authCode);

            MultiMap<String> authParams = new MultiMap<String>(baseParams);
            authParams.put("authorization", authCode);
            Request authRequest = authHttpClient.POST(authUrl).followRedirects(false);
            authRequest.header("Content-Type", "application/x-www-form-urlencoded");
            authRequest.content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED,
                    UrlEncoded.encode(authParams, StandardCharsets.UTF_8, false), StandardCharsets.UTF_8));
            ContentResponse authResponse = authRequest.send();
            logger.info("{}", authResponse.getHeaders());
            logger.info("Response " + authResponse.getHeaders().get(HttpHeader.LOCATION));
            String code = AuthTest.codeFromUrl(authResponse.getHeaders().get(HttpHeader.LOCATION));
            logger.info("Code " + code);
            logger.info("Auth");

            logger.info(aqr.tokenEndpoint);
            // AuthenticationStore authenticationStore = authHttpClient.getAuthenticationStore();
            // BasicAuthentication ba = new BasicAuthentication(new URI(aqr.tokenEndpoint), Authentication.ANY_REALM,
            // aqr.clientId, aqr.clientSecret);
            // authenticationStore.addAuthentication(ba);
            Request codeRequest = authHttpClient.POST(aqr.tokenEndpoint);
            String basicAuth = "Basic "
                    + Base64.getUrlEncoder().encodeToString((aqr.clientId + ":" + aqr.clientSecret).getBytes());
            logger.info(basicAuth);
            codeRequest.header("Content-Type", "application/x-www-form-urlencoded");
            codeRequest.header(AUTHORIZATION, basicAuth);

            MultiMap<String> codeParams = new MultiMap<String>();
            codeParams.put("code", code);
            codeParams.put("code_verifier", codeVerifier);
            codeParams.put("redirect_uri", aqr.returnUrl);
            codeParams.put("grant_type", "authorization_code");
            codeRequest.content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED,
                    UrlEncoded.encode(codeParams, StandardCharsets.UTF_8, false), StandardCharsets.UTF_8));
            ContentResponse codeResponse = codeRequest.send();
            logger.info(codeResponse.getContentAsString());
            AuthResponse ar = Converter.getGson().fromJson(codeResponse.getContentAsString(), AuthResponse.class);
            Token t = new Token();
            t.setType(ar.tokenType);
            t.setToken(ar.accessToken);
            t.setExpiration(ar.expiresIn);
            logger.info(t.getBearerToken());

            /**
             * REQUEST CONTENT
             */
            HttpClient apiHttpClient = new HttpClient(sslContextFactory);
            apiHttpClient.start();

            MultiMap<String> vehicleParams = new MultiMap<String>();
            vehicleParams.put("tireGuardMode", "ENABLED");
            vehicleParams.put("appDateTime", Long.toString(System.currentTimeMillis()));
            vehicleParams.put("apptimezone", "60");
            // vehicleRequest.param("tireGuardMode", "ENABLED");
            // vehicleRequest.param("appDateTime", Long.toString(System.currentTimeMillis()));
            // vehicleRequest.param("apptimezone", "60.0");
            // vehicleRequest.
            // // logger.info(vehicleParams);
            // vehicleRequest.content(new StringContentProvider(CONTENT_TYPE_JSON_ENCODED, vehicleParams.toString(),
            // StandardCharsets.UTF_8));
            // logger.info(vehicleRequest.getHeaders());
            String params = UrlEncoded.encode(vehicleParams, StandardCharsets.UTF_8, false);

            String vehicleUrl = "https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_ROW)
                    + "/eadrax-vcs/v1/vehicles";
            logger.info(vehicleUrl);
            Request vehicleRequest = apiHttpClient.newRequest(vehicleUrl + "?" + params);//
            // .param("tireGuardMode", "ENABLED")
            // .param("appDateTime", Long.toString(System.currentTimeMillis())).param("apptimezone", "60.0");
            // vehicleRequest.header("Content-Type", "application/x-www-form-urlencoded");
            vehicleRequest.header(HttpHeader.AUTHORIZATION, t.getBearerToken());
            vehicleRequest.header("accept", "application/json");
            vehicleRequest.header("accept-language", "de");
            vehicleRequest.header("x-user-agent", "android(v1.07_20200330);bmw;1.7.0(11152)");
            // vehicleRequest.header(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded");
            // vehicleRequest.content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED,
            // UrlEncoded.encode(vehicleParams, StandardCharsets.UTF_8, false), StandardCharsets.UTF_8));
            ContentResponse vehicleResponse = vehicleRequest.send();
            logger.info("Vehicle Status {} {}", vehicleResponse.getStatus(), vehicleResponse.getContentAsString());

            /**
             * CHARGE STATISTICS
             */
            MultiMap<String> chargeStatisticsParams = new MultiMap<String>();
            chargeStatisticsParams.put("vin", "WBY1Z81040V905639");
            chargeStatisticsParams.put("currentDate", Converter.getCurrentISOTime());
            params = UrlEncoded.encode(chargeStatisticsParams, StandardCharsets.UTF_8, false);

            String chargeStatisticsUrl = "https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_ROW)
                    + "/eadrax-chs/v1/charging-statistics";
            Request chargeStatisticsRequest = apiHttpClient.newRequest(chargeStatisticsUrl)
                    .param("vin", "WBY1Z81040V905639").param("currentDate", Converter.getCurrentISOTime());
            logger.info("{}", chargeStatisticsUrl);
            // vehicleRequest.header("Content-Type", "application/x-www-form-urlencoded");
            chargeStatisticsRequest.header(HttpHeader.AUTHORIZATION, t.getBearerToken());
            chargeStatisticsRequest.header("accept", "application/json");
            chargeStatisticsRequest.header("x-user-agent", "android(v1.07_20200330);bmw;1.7.0(11152)");
            chargeStatisticsRequest.header("accept-language", "de");

            // MultiMap<String> chargeStatisticsParams = new MultiMap<String>();
            // chargeStatisticsParams.put("vin", "WBY1Z81040V905639");
            // chargeStatisticsParams.put("currentDate", Converter.getCurrentISOTime());
            //
            // params = UrlEncoded.encode(chargeStatisticsParams, StandardCharsets.UTF_8, false);
            logger.info("{}", params);
            chargeStatisticsRequest
                    .content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED, params, StandardCharsets.UTF_8));

            ContentResponse chargeStatisticsResponse = chargeStatisticsRequest.send();
            logger.info("{}", chargeStatisticsResponse.getStatus());
            logger.info("{}", chargeStatisticsResponse.getReason());
            logger.info("{}", chargeStatisticsResponse.getContentAsString());

            /**
             * CHARGE SESSIONS
             */
            MultiMap<String> chargeSessionsParams = new MultiMap<String>();
            chargeSessionsParams.put("vin", "WBY1Z81040V905639");
            chargeSessionsParams.put("maxResults", "40");
            chargeSessionsParams.put("include_date_picker", "true");

            params = UrlEncoded.encode(chargeSessionsParams, StandardCharsets.UTF_8, false);

            String chargeSessionsUrl = "https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_ROW)
                    + "/eadrax-chs/v1/charging-sessions";
            Request chargeSessionsRequest = apiHttpClient.newRequest(chargeSessionsUrl + "?" + params);
            logger.info("{}", chargeSessionsUrl);
            // vehicleRequest.header("Content-Type", "application/x-www-form-urlencoded");
            chargeSessionsRequest.header(HttpHeader.AUTHORIZATION, t.getBearerToken());
            chargeSessionsRequest.header("accept", "application/json");
            chargeSessionsRequest.header("x-user-agent", "android(v1.07_20200330);bmw;1.7.0(11152)");
            chargeSessionsRequest.header("accept-language", "de");

            // MultiMap<String> chargeStatisticsParams = new MultiMap<String>();
            // chargeStatisticsParams.put("vin", "WBY1Z81040V905639");
            // chargeStatisticsParams.put("currentDate", Converter.getCurrentISOTime());
            //
            // params = UrlEncoded.encode(chargeStatisticsParams, StandardCharsets.UTF_8, false);
            logger.info("{}", params);
            // chargeStatisticsRequest
            // .content(new StringContentProvider(CONTENT_TYPE_URL_ENCODED, params, StandardCharsets.UTF_8));

            ContentResponse chargeSessionsResponse = chargeSessionsRequest.send();
            logger.info("{}", chargeSessionsResponse.getStatus());
            logger.info("{}", chargeSessionsResponse.getReason());
            logger.info("{}", chargeSessionsResponse.getContentAsString());

            String chargingControlUrl = "https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_ROW)
                    + "/eadrax-vrccs/v2/presentation/remote-commands/WBY1Z81040V905639/charging-control";
            Request chargingControlRequest = apiHttpClient.POST(chargingControlUrl);
            logger.info("{}", chargingControlUrl);
            // vehicleRequest.header("Content-Type", "application/x-www-form-urlencoded");
            chargingControlRequest.header(HttpHeader.AUTHORIZATION, t.getBearerToken());
            chargingControlRequest.header("accept", "application/json");
            chargingControlRequest.header("x-user-agent", "android(v1.07_20200330);bmw;1.7.0(11152)");
            chargingControlRequest.header("accept-language", "de");
            chargingControlRequest.header("Content-Type", CONTENT_TYPE_JSON_ENCODED);

            // String content = FileReader.readFileInString("src/test/resources/responses/charging-profile.json");
            // logger.info("{}", content);
            // ChargeProfile cpc = Converter.getGson().fromJson(content, ChargeProfile.class);
            // String contentTranfsorm = Converter.getGson().toJson(cpc);
            // String profile = "{chargingProfile:" + contentTranfsorm + "}";
            // logger.info("{}", profile);
            // chargingControlRequest
            // .content(new StringContentProvider(CONTENT_TYPE_JSON_ENCODED, params, StandardCharsets.UTF_8));

            // chargeStatisticsParams.put("vin", "WBY1Z81040V905639");
            // chargeStatisticsParams.put("currentDate", Converter.getCurrentISOTime());
            //
            // params = UrlEncoded.encode(chargeStatisticsParams, StandardCharsets.UTF_8, false);

            // ContentResponse chargingControlResponse = chargingControlRequest.send();
            // logger.info("{}", chargingControlResponse.getStatus());
            // logger.info("{}", chargingControlResponse.getReason());
            // logger.info("{}", chargingControlResponse.getContentAsString());

        } catch (Exception e) {
            logger.error("{}", e.getMessage());
        }
    }

    private String getAuthCode(String response) {
        String[] keys = response.split("&");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].startsWith(AUTHORIZATION)) {
                String authCode = keys[i].split("=")[1];
                authCode = authCode.split("\"")[0];
                return authCode;
            }
        }
        return Constants.EMPTY;
    }

    public static String codeFromUrl(String encodedUrl) {
        final MultiMap<String> tokenMap = new MultiMap<String>();
        UrlEncoded.decodeTo(encodedUrl, tokenMap, StandardCharsets.US_ASCII);
        final StringBuilder codeFound = new StringBuilder();
        tokenMap.forEach((key, value) -> {
            if (!value.isEmpty()) {
                String val = value.get(0);
                if (key.endsWith(CODE)) {
                    codeFound.append(val);
                }
            }
        });
        return codeFound.toString();
    }

    @Test
    public void testJWTDeserialze() {
        String accessTokenResponseStr = FileReader
                .readFileInString("src/test/resources/responses/auth/auth_cn_login_pwd.json");
        ChinaTokenResponse cat = Converter.getGson().fromJson(accessTokenResponseStr, ChinaTokenResponse.class);

        // https://www.baeldung.com/java-jwt-token-decode
        String token = cat.data.accessToken;
        String[] chunks = token.split("\\.");
        String tokenJwtDecodeStr = new String(Base64.getUrlDecoder().decode(chunks[1]));
        ChinaTokenExpiration cte = Converter.getGson().fromJson(tokenJwtDecodeStr, ChinaTokenExpiration.class);
        Token t = new Token();
        t.setToken(token);
        t.setType(cat.data.tokenType);
        t.setExpirationTotal(cte.exp);
        assertEquals(
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJEVU1NWSQxJEEkMTYzNzcwNzkxNjc4MiIsIm5iZiI6MTYzNzcwNzkxNiwiZXhwIjoxNjM3NzExMjE2LCJpYXQiOjE2Mzc3MDc5MTZ9.hpi-P97W68g7avGwu9dcBRapIsaG4F8MwOdPHe6PuTA",
                t.getBearerToken(), "Token");
    }

    public void testChina() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        HttpClient authHttpClient = new HttpClient(sslContextFactory);
        try {
            authHttpClient.start();
            HttpClientFactory mockHCF = mock(HttpClientFactory.class);
            when(mockHCF.getCommonHttpClient()).thenReturn(authHttpClient);
            MyBMWConfiguration config = new MyBMWConfiguration();
            config.region = BimmerConstants.REGION_CHINA;
            config.userName = "Hello User";
            config.password = "Hello Password";
            MyBMWProxy bmwProxy = new MyBMWProxy(mockHCF, config);
            bmwProxy.updateTokenChina();
        } catch (Exception e) {
            logger.warn("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testPublicKey() {
        String publicKeyResponseStr = FileReader.readFileInString("src/test/resources/responses/auth/china-key.json");
        ChinaPublicKeyResponse pkr = Converter.getGson().fromJson(publicKeyResponseStr, ChinaPublicKeyResponse.class);
        String publicKeyStr = pkr.data.value;
        String publicKeyPEM = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "").replace("-----END PUBLIC KEY-----", "").replace("\\r", "")
                .replace("\\n", "").trim();
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        KeyFactory kf;
        try {
            kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);
            // https://www.thexcoders.net/java-ciphers-rsa/
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal("Hello World".getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
            logger.info(encodedPassword);
        } catch (Exception e) {
            assertTrue(false, "Excpetion: " + e.getMessage());
        }
    }

    public void testChinaToken() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        HttpClient authHttpClient = new HttpClient(sslContextFactory);
        try {
            authHttpClient.start();
            String url = "https://" + BimmerConstants.EADRAX_SERVER_MAP.get(BimmerConstants.REGION_CHINA)
                    + BimmerConstants.CHINA_PUBLIC_KEY;
            Request oauthQueryRequest = authHttpClient.newRequest(url);
            oauthQueryRequest.header(X_USER_AGENT,
                    String.format(BimmerConstants.BRAND_BMW, BimmerConstants.BRAND_BMW, BimmerConstants.REGION_ROW));

            ContentResponse publicKeyResponse = oauthQueryRequest.send();
            ChinaPublicKeyResponse pkr = Converter.getGson().fromJson(publicKeyResponse.getContentAsString(),
                    ChinaPublicKeyResponse.class);
            // https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file

            String publicKeyStr = pkr.data.value;
            // String cleanPublicKeyStr = pkr.data.value.replaceAll("(\r\n|\n)", Constants.EMPTY);
            String publicKeyPEM = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "").replace("-----END PUBLIC KEY-----", "");
            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);
            // https://www.thexcoders.net/java-ciphers-rsa/
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal("Hello World".getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
            logger.info(encodedPassword);
        } catch (Exception e) {
            assertTrue(false, "Excpetion: " + e.getMessage());
        }
    }
}
