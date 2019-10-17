/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.pixometer.handler;

import static org.openhab.binding.pixometer.internal.PixometerBindingConstants.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.pixometer.internal.PixometerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link ApiserviceHandler} is responsible for handling the api connection and authorization (including token
 * refresh)
 *
 * @author Jerome Luckenbach - Initial contribution
 */
public class ApiserviceHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int TOKEN_MIN_DIFF = 172800000;

    private String authToken;
    private int refreshInterval;
    private long tokenExpiryDate;

    public ApiserviceHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Nothing to handle here currently
    }

    @Override
    public void initialize() {
        logger.debug("Initialize Pixometer Apiservice");
        updateStatus(ThingStatus.UNKNOWN, ThingStatusDetail.NONE, "Waiting to get access-token from api...");

        PixometerConfiguration config = getConfigAs(PixometerConfiguration.class);
        setRefreshInterval(config.refresh);
        String user = config.user;
        String password = config.password;
        String scope = config.scope;

        // Check expiry date every Day and obtain new access token if difference is less then or equal to 2 days
        scheduler.scheduleWithFixedDelay(() -> {
            logger.debug("Checking if new access token is needed...");
            try {
                long difference = getTokenExpiryDate() - System.nanoTime();
                if (difference <= TOKEN_MIN_DIFF) {
                    obtainAuthTokenAndExpiryDate(user, password, scope);
                }
            } catch (RuntimeException r) {
                logger.debug("Could not check token expiry date for Thing {}: ", getThing().getUID(), r);
            }
        }, 1, 1440, TimeUnit.MINUTES);

        logger.debug("Refresh job scheduled to run every {} days. for '{}'", 1, getThing().getUID());
    }

    @Override
    public void updateStatus(ThingStatus status) {
        super.updateStatus(status);
    }

    /**
     * Request auth token with read or write access.
     * (Write access is prepared for a possible later usage for updating meters.)
     *
     * @param user The username to use
     * @param password The corresponding password
     * @param scope The granted scope on the api for the binding
     */
    private void obtainAuthTokenAndExpiryDate(String user, String password, String scope) {
        try {
            String url = new StringBuilder(API_BASE_URL).append("v1/access-token/").toString();
            Properties urlHeader = (Properties) new Properties().put("CONTENT-TYPE", "application/json");

            JsonObject httpBody = new JsonObject();
            httpBody.addProperty("username", user);
            httpBody.addProperty("password", password);
            httpBody.addProperty("scope", scope);

            InputStream content = new ByteArrayInputStream(httpBody.toString().getBytes(StandardCharsets.UTF_8));
            String urlResponse = HttpUtil.executeUrl("POST", url, urlHeader, content, "application/json", 2000);
            JsonObject responseJson = (JsonObject) new JsonParser().parse(urlResponse);

            if (responseJson.has(CONFIG_BRIDGE_AUTH_TOKEN)) {
                // Store the expire date for automatic token refresh
                int expiresIn = Integer.parseInt(responseJson.get("expires_in").toString());
                setTokenExpiryDate(TimeUnit.SECONDS.toNanos(expiresIn));

                setAuthToken(responseJson.get(CONFIG_BRIDGE_AUTH_TOKEN).toString().replaceAll("\"", ""));

                // Save new auth token to config
                Configuration editConfig = editConfiguration();
                editConfig.put(CONFIG_BRIDGE_AUTH_TOKEN, getAuthToken());
                updateConfiguration(editConfig);

                updateStatus(ThingStatus.ONLINE);
                return;
            }

            String errorMsg = String.format("Invalid Api Response ( %s )", responseJson);

            throw new RuntimeException(errorMsg);
        } catch (RuntimeException | IOException e) {
            String errorMsg = String.format(
                    "Could not obtain auth token. Please check your configured account credentials. %s %s",
                    this.getThing().getUID(), e.getMessage());

            logger.debug(errorMsg, e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, errorMsg);
        }
    }

    /**
     * Getters and Setters
     */

    public String getAuthToken() {
        return authToken;
    }

    private void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    private void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public long getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    private void setTokenExpiryDate(long expiresIn) {
        this.tokenExpiryDate = System.nanoTime() + expiresIn;
    }

}
