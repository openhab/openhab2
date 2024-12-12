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
package org.openhab.binding.smartthings.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.UrlEncoded;
import org.openhab.binding.smartthings.internal.api.SmartthingsApi;
import org.openhab.binding.smartthings.internal.dto.AppResponse;
import org.openhab.binding.smartthings.internal.dto.SmartthingsLocation;
import org.openhab.binding.smartthings.internal.handler.SmartthingsBridgeHandler;
import org.openhab.binding.smartthings.internal.type.SmartthingsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SpotifyAuthServlet} manages the authorization with the Smartthings Web API. The servlet implements the
 * Authorization Code flow and saves the resulting refreshToken with the bridge.
 *
 * @author Laurent Arnal - Initial contribution
 */
@NonNullByDefault
public class SmartthingsAuthServlet extends HttpServlet {

    private static final long serialVersionUID = -4719613645562518231L;

    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    // Simple HTML templates for inserting messages.
    private static final String HTML_EMPTY_PLAYERS = "<p class='block'>Manually add a Smarthings Bridge to authorize it here.<p>";
    private static final String HTML_USER_AUTHORIZED = "<p class='block authorized'>Bridge authorized for user %s.</p>";
    private static final String HTML_ERROR = "<p class='block error'>Call to Smartthings failed with error: %s</p>";

    private static final Pattern MESSAGE_KEY_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");

    // Keys present in the index.html
    private static final String KEY_PAGE_REFRESH = "pageRefresh";
    private static final String HTML_META_REFRESH_CONTENT = "<meta http-equiv='refresh' content='10; url=%s'>";
    private static final String KEY_AUTHORIZED_USER = "authorizedUser";
    private static final String KEY_ERROR = "error";
    private static final String KEY_SETUP_URI = "setup.uri";
    private static final String KEY_REDIRECT_URI = "redirectUri";
    private static final String KEY_LOCATIONID_OPTION = "locationId.Option";
    private static final String KEY_POOL_STATUS = "poolStatus";

    // Keys present in the player.html

    private final Logger logger = LoggerFactory.getLogger(SmartthingsAuthServlet.class);
    private final SmartthingsAuthService smartthingsAuthService;
    private final SmartthingsBridgeHandler bridgeHandler;

    private final String indexTemplate;
    private final String selectLocationTemplate;
    private final String poolTemplate;
    private final String confirmationTemplate;
    private int idx = 0;

    private static final String TEMPLATE_PATH = "templates/";

    public SmartthingsAuthServlet(SmartthingsBridgeHandler bridgeHandler, SmartthingsAuthService smartthingsAuthService)
            throws SmartthingsException {

        this.bridgeHandler = bridgeHandler;
        this.smartthingsAuthService = smartthingsAuthService;

        try {
            this.indexTemplate = readTemplate("index.html");
            this.selectLocationTemplate = readTemplate("selectlocation.html");
            this.poolTemplate = readTemplate("pool.ajax");
            this.confirmationTemplate = readTemplate("confirmation.html");

        } catch (IOException e) {
            throw new SmartthingsException("unable to initialize auth servlet", e);
        }
    }

    /**
     * Reads a template from file and returns the content as String.
     *
     * @param templateName name of the template file to read
     * @return The content of the template file
     * @throws IOException thrown when an HTML template could not be read
     */
    private String readTemplate(String templateName) throws IOException {
        final URL url = bridgeHandler.getBundleContext().getBundle().getEntry(TEMPLATE_PATH + templateName);

        if (url == null) {
            throw new FileNotFoundException(
                    String.format("Cannot find {}' - failed to initialize Linky servlet".formatted(templateName)));
        } else {
            try (InputStream inputStream = url.openStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

    @Override
    protected void doGet(@Nullable HttpServletRequest req, @Nullable HttpServletResponse resp)
            throws ServletException, IOException {
        logger.debug("Smartthings auth callback servlet received GET request {}.", req.getRequestURI());
        final Map<String, String> replaceMap = new HashMap<>();

        StringBuffer requestUrl = req.getRequestURL();
        String servletBaseUrl = requestUrl != null ? requestUrl.toString() : "";

        String template = "";
        if (servletBaseUrl.contains("index")) {
            template = indexTemplate;
        } else if (servletBaseUrl.contains("selectlocation")) {
            template = selectLocationTemplate;
        } else if (servletBaseUrl.contains("pool")) {
            template = poolTemplate;
        } else if (servletBaseUrl.contains("confirmation")) {
            template = confirmationTemplate;
        } else {
            template = indexTemplate;
        }

        StringBuffer optionBuffer = new StringBuffer();

        if (template == indexTemplate) {
            SetupApp();
        } else if (template == selectLocationTemplate) {
            SmartthingsApi api = bridgeHandler.getSmartthingsApi();
            SmartthingsLocation[] locationList = api.GetAllLocations();
            for (SmartthingsLocation loc : locationList) {
                optionBuffer.append("<option value=\"" + loc.locationId + "\">" + loc.name + "</option>");
            }
        } else if (template == poolTemplate) {
            idx++;

            if (idx < 10) {
                replaceMap.put(KEY_POOL_STATUS, "false");
            } else {
                replaceMap.put(KEY_POOL_STATUS, "true");
            }
        }

        String servletBaseURLSecure = servletBaseUrl.replace("http://", "https://").replace("8080", "8443");

        handleSmartthingsRedirect(replaceMap, servletBaseURLSecure, req.getQueryString());
        resp.setContentType(CONTENT_TYPE);
        SmartthingsAccountHandler accountHandler = smartthingsAuthService.getSmartthingsAccountHandler();

        replaceMap.put(KEY_REDIRECT_URI, servletBaseURLSecure);
        // replaceMap.put(KEY_BRIDGE_URI, accountHandler.formatAuthorizationUrl(servletBaseURLSecure));

        String locationId = "cb73e411-15b4-40e8-b6cd-f9a34f6ced4b";
        String uri = "https://account.smartthings.com/login?redirect=https%3A%2F%2Fstrongman-regional.api.smartthings.com%2F";
        uri = uri + "%3FappId%3D";
        uri = uri + bridgeHandler.getAppId();
        uri = uri + "%26locationId%3D" + locationId;
        uri = uri + "%26appType%3DENDPOINTAPP";
        uri = uri + "%26language%3Den";
        uri = uri + "%26clientOS%3Dweb";

        replaceMap.put(KEY_SETUP_URI, uri);
        replaceMap.put(KEY_LOCATIONID_OPTION, optionBuffer.toString());

        resp.getWriter().append(replaceKeysFromMap(template, replaceMap));
        resp.getWriter().close();
    }

    protected void SetupApp() {
        SmartthingsApi api = bridgeHandler.getSmartthingsApi();

        AppResponse appResponse = api.SetupApp();
        if (appResponse.oauthClientId != null && appResponse.oauthClientSecret != null) {
            bridgeHandler.updateConfig(appResponse.oauthClientId, appResponse.oauthClientSecret);
        }
    }

    /**
     * Handles a possible call from Spotify to the redirect_uri. If that is the case Spotify will pass the authorization
     * codes via the url and these are processed. In case of an error this is shown to the user. If the user was
     * authorized this is passed on to the handler. Based on all these different outcomes the HTML is generated to
     * inform the user.
     *
     * @param replaceMap a map with key String values that will be mapped in the HTML templates.
     * @param servletBaseURL the servlet base, which should be used as the Spotify redirect_uri value
     * @param queryString the query part of the GET request this servlet is processing
     */
    private void handleSmartthingsRedirect(Map<String, String> replaceMap, String servletBaseURL,
            @Nullable String queryString) {
        replaceMap.put(KEY_AUTHORIZED_USER, "");
        replaceMap.put(KEY_ERROR, "");
        replaceMap.put(KEY_PAGE_REFRESH, "");

        if (queryString != null) {
            final MultiMap<String> params = new MultiMap<>();
            UrlEncoded.decodeTo(queryString, params, StandardCharsets.UTF_8.name());
            final String reqCode = params.getString("code");
            final String reqState = params.getString("state");
            final String reqError = params.getString("error");

            replaceMap.put(KEY_PAGE_REFRESH,
                    params.isEmpty() ? "" : String.format(HTML_META_REFRESH_CONTENT, servletBaseURL));
            if (!StringUtil.isBlank(reqError)) {
                logger.debug("Spotify redirected with an error: {}", reqError);
                replaceMap.put(KEY_ERROR, String.format(HTML_ERROR, reqError));
            } else if (!StringUtil.isBlank(reqState)) {
                try {
                    replaceMap.put(KEY_AUTHORIZED_USER, String.format(HTML_USER_AUTHORIZED,
                            smartthingsAuthService.authorize(servletBaseURL, reqState, reqCode)));
                } catch (RuntimeException e) {
                    logger.debug("Exception during authorizaton: ", e);
                    replaceMap.put(KEY_ERROR, String.format(HTML_ERROR, e.getMessage()));
                }
            }
        }
    }

    /**
     * Replaces all keys from the map found in the template with values from the map. If the key is not found the key
     * will be kept in the template.
     *
     * @param template template to replace keys with values
     * @param map map with key value pairs to replace in the template
     * @return a template with keys replaced
     */
    private String replaceKeysFromMap(String template, Map<String, String> map) {
        final Matcher m = MESSAGE_KEY_PATTERN.matcher(template);
        final StringBuffer sb = new StringBuffer();

        while (m.find()) {
            try {
                final String key = m.group(1);
                m.appendReplacement(sb, Matcher.quoteReplacement(map.getOrDefault(key, "${" + key + '}')));
            } catch (RuntimeException e) {
                logger.debug("Error occurred during template filling, cause ", e);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
