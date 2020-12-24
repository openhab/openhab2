/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

package org.openhab.binding.plugwiseha.internal.handler;

import static org.openhab.binding.plugwiseha.internal.PlugwiseHABindingConstants.*;
import static org.openhab.core.thing.ThingStatus.OFFLINE;
import static org.openhab.core.thing.ThingStatus.ONLINE;
import static org.openhab.core.thing.ThingStatusDetail.*;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHACommunicationException;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAException;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAInvalidHostException;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHANotAuthorizedException;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHATimeoutException;
import org.openhab.binding.plugwiseha.internal.api.exception.PlugwiseHAUnauthorizedException;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAController;
import org.openhab.binding.plugwiseha.internal.api.model.PlugwiseHAModel;
import org.openhab.binding.plugwiseha.internal.api.model.dto.GatewayInfo;
import org.openhab.binding.plugwiseha.internal.config.PlugwiseHABridgeThingConfig;
import org.openhab.binding.plugwiseha.internal.config.PlugwiseHAThingConfig;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.builder.ThingStatusInfoBuilder;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PlugwiseHABridgeHandler} class is responsible for handling
 * commands and status updates for the Plugwise Home Automation bridge.
 * Extends @{link BaseBridgeHandler}
 *
 * @author Bas van Wetten - Initial contribution
 * @author Leo Siepel - finish initial contribution
 * 
 */

@NonNullByDefault
public class PlugwiseHABridgeHandler extends BaseBridgeHandler {

    // Private Static error messages

    private static final String STATUS_DESCRIPTION_COMMUNICATION_ERROR = "Error communicating with the Plugwise Home Automation controller";
    private static final String STATUS_DESCRIPTION_TIMEOUT = "Communication timeout while communicating with the Plugwise Home Automation controller";
    private static final String STATUS_DESCRIPTION_CONFIGURATION_ERROR = "Invalid or missing configuration";
    private static final String STATUS_DESCRIPTION_INVALID_CREDENTIALS = "Invalid username and/or password - please double-check your configuration";
    private static final String STATUS_DESCRIPTION_INVALID_HOSTNAME = "Invalid hostname - please double-check your configuration";

    // Private member variables/constants

    @Nullable
    private PlugwiseHABridgeThingConfig config;
    @Nullable
    private GatewayInfo gatewayInfo;
    @Nullable
    private ScheduledFuture<?> refreshJob;
    @Nullable
    private volatile PlugwiseHAController controller;

    private final HttpClient httpClient;
    private final Logger logger = LoggerFactory.getLogger(PlugwiseHABridgeHandler.class);

    // Constructor

    public PlugwiseHABridgeHandler(Bridge bridge, HttpClient httpClient) {
        super(bridge);
        this.httpClient = httpClient;
    }

    // Public methods

    @Override
    public void initialize() {
        // This method is also called whenever config changes
        cancelRefreshJob();
        this.config = getConfig().as(PlugwiseHABridgeThingConfig.class);

        if (this.checkConfig()) {
            logger.debug("Initializing the Plugwise Home Automation bridge handler with config = {}", this.config);
            try {
                this.controller = new PlugwiseHAController(httpClient, config.getHost(), config.getPort(),
                        config.getUsername(), config.getsmileId());
                this.controller.start(() -> {
                    setBridgeProperties();
                    updateStatus(ONLINE);
                });
            } catch (PlugwiseHAInvalidHostException e) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, STATUS_DESCRIPTION_INVALID_HOSTNAME);
            } catch (PlugwiseHAUnauthorizedException | PlugwiseHANotAuthorizedException e) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, STATUS_DESCRIPTION_INVALID_CREDENTIALS);
            } catch (PlugwiseHACommunicationException e) {
                updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_COMMUNICATION_ERROR);
            } catch (PlugwiseHAException e) {
                logger.error("Unknown error while configuring the Plugwise Home Automation Controller", e);
                updateStatus(OFFLINE, CONFIGURATION_ERROR, e.getMessage());
            }
        } else {
            logger.warn("Invalid config for the Plugwise Home Automation bridge handler with config = {}", this.config);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        this.logger.warn(
                "Ignoring command = {} for channel = {} - this channel for the Plugwise Home Automation binding is read-only!",
                command, channelUID);
    }

    @Override
    public void dispose() {
        cancelRefreshJob();
        if (this.controller != null) {
            this.controller.stop();
            this.controller = null;
        }
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_BRIDGE_TYPES_UIDS.contains(thingTypeUID);
    }

    // Getters & setters

    public @Nullable PlugwiseHAController getController() {
        return this.controller;
    }

    // Protected and private methods

    /**
     * Checks the configuration for validity, result is reflected in the status of
     * the Thing
     */
    private boolean checkConfig() {
        if (this.config == null || !this.config.isValid()) {
            updateStatus(OFFLINE, CONFIGURATION_ERROR, STATUS_DESCRIPTION_CONFIGURATION_ERROR);
            return false;
        } else {
            return true;
        }
    }

    private void scheduleRefreshJob() {
        synchronized (this) {
            if (this.refreshJob == null) {
                logger.debug("Scheduling refresh job every {}s", config.getRefresh());
                this.refreshJob = scheduler.scheduleWithFixedDelay(this::run, 0, config.getRefresh(), TimeUnit.SECONDS);
            }
        }
    }

    private void run() {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Executing refresh job");
            }

            refresh();
            updateStatus(ONLINE);
        } catch (PlugwiseHAInvalidHostException e) {
            updateStatus(OFFLINE, CONFIGURATION_ERROR, STATUS_DESCRIPTION_INVALID_HOSTNAME);
        } catch (PlugwiseHAUnauthorizedException | PlugwiseHANotAuthorizedException e) {
            updateStatus(OFFLINE, CONFIGURATION_ERROR, STATUS_DESCRIPTION_INVALID_CREDENTIALS);
        } catch (PlugwiseHACommunicationException e) {
            updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_COMMUNICATION_ERROR);
        } catch (PlugwiseHATimeoutException e) {
            updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_TIMEOUT);
        } catch (Exception e) {
            logger.debug("Unhandled exception while refreshing the Plugwise Home Automation Controller {} - {}",
                    getThing().getUID(), e.getMessage());
            updateStatus(OFFLINE, COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void refresh() throws PlugwiseHAException {
        if (this.getController() != null) {
            logger.debug("Refreshing the Plugwise Home Automation Controller {}", getThing().getUID());
            this.config = getConfig().as(PlugwiseHABridgeThingConfig.class);

            PlugwiseHAController controller = this.getController();
            if (controller != null) {
                controller.refresh();
            }

            getThing().getThings().forEach((thing) -> {
                ThingHandler thingHandler = thing.getHandler();
                if (thingHandler instanceof PlugwiseHABaseHandler) {
                    ((PlugwiseHABaseHandler<PlugwiseHAModel, PlugwiseHAThingConfig>) thingHandler).refresh();
                }
            });
        }
    }

    private void cancelRefreshJob() {
        synchronized (this) {
            if (this.refreshJob != null) {
                logger.debug("Cancelling refresh job");
                this.refreshJob.cancel(true);
                this.refreshJob = null;
            }
        }
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        if (status == ONLINE || (status == OFFLINE && statusDetail == COMMUNICATION_ERROR)) {
            scheduleRefreshJob();
        } else if (status == OFFLINE && statusDetail == CONFIGURATION_ERROR) {
            cancelRefreshJob();
        }

        // Only update bridge status if statusInfo has changed
        ThingStatusInfo statusInfo = ThingStatusInfoBuilder.create(status, statusDetail).withDescription(description)
                .build();
        if (!statusInfo.equals(getThing().getStatusInfo())) {
            super.updateStatus(status, statusDetail, description);
        }
    }

    protected void setBridgeProperties() {
        try {
            PlugwiseHAController controller = this.getController();
            if (controller != null) {
                this.gatewayInfo = controller.getGatewayInfo();
            }

            Map<String, String> properties = editProperties();
            properties.put(Thing.PROPERTY_FIRMWARE_VERSION, this.gatewayInfo.getFirmwareVersion());
            properties.put(Thing.PROPERTY_HARDWARE_VERSION, this.gatewayInfo.getHardwareVersion());
            properties.put(Thing.PROPERTY_MAC_ADDRESS, this.gatewayInfo.getMacAddress());
            properties.put(Thing.PROPERTY_VENDOR, this.gatewayInfo.getVendorName());
            properties.put(Thing.PROPERTY_MODEL_ID, this.gatewayInfo.getVendorModel());

            updateProperties(properties);
        } catch (PlugwiseHAException e) {
            updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_COMMUNICATION_ERROR);
        }
    }
}
