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
package org.openhab.binding.bsblan.internal.handler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.bsblan.internal.api.BsbLanApiCaller;
import org.openhab.binding.bsblan.internal.api.models.BsbLanApiParameterQueryResponse;
import org.openhab.binding.bsblan.internal.configuration.BsbLanBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openhab.binding.bsblan.internal.BsbLanBindingConstants.*;

/**
 * Bridge for BSB-LAN devices.
 *
 * @author Peter Schraffl - Initial contribution
 */
public class BsbLanBridgeHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(BsbLanBridgeHandler.class);
    private final Set<BsbLanBaseThingHandler> things = new HashSet<>();
    private BsbLanBridgeConfiguration bridgeConfig;
    private ScheduledFuture<?> refreshJob;
    private BsbLanApiParameterQueryResponse cachedParameterQueryResponse;

    public BsbLanBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    public void registerThing(final BsbLanBaseThingHandler parameter) {
        this.things.add(parameter);
    }

    @Override
    public void initialize() {
        bridgeConfig = getConfigAs(BsbLanBridgeConfiguration.class);

        // validate 'host' configuration
        if (StringUtils.trimToNull(bridgeConfig.host) == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, 
                "Parameter 'host' is mandatory and must be configured");
            return;
        }

        // validate 'refreshInterval' configuration
        if (bridgeConfig.refreshInterval != null && bridgeConfig.refreshInterval <  MIN_REFRESH_INTERVAL) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                String.format("Parameter 'refreshInterval' must be at least %d seconds", MIN_REFRESH_INTERVAL));
            return;
        }

        if (bridgeConfig.port == null) {
            bridgeConfig.port = DEFAULT_API_PORT;
        }

        // all checks succeeded, start refreshing
        startAutomaticRefresh(bridgeConfig);
    }

    @Override
    public void dispose() {
        if (refreshJob != null) {
            refreshJob.cancel(true);
        }
        things.clear();
    }

    public BsbLanApiParameterQueryResponse getCachedParameterQueryResponse() {
        return cachedParameterQueryResponse;
    }

    public BsbLanBridgeConfiguration getBridgeConfiguration() {
        return bridgeConfig;
    }

    /**
     * Start the job refreshing the data
     */
    private void startAutomaticRefresh(BsbLanBridgeConfiguration config) {
        if (refreshJob == null || refreshJob.isCancelled()) {
            Runnable runnable = () -> {
                logger.debug("Refreshing parameter values");

                BsbLanApiCaller apiCaller = new BsbLanApiCaller(bridgeConfig);

                // refresh all parameters
                Set<Integer> parameterIds = things.stream()
                                            .filter(thing -> thing instanceof BsbLanParameterHandler)
                                            .map(thing -> (BsbLanParameterHandler) thing)
                                            .map(thing -> thing.getParameterId())
                                            .collect(Collectors.toSet());

                cachedParameterQueryResponse = apiCaller.queryParameters(parameterIds);

                // InetAddress.isReachable(...) check returned false on RPi although the device is reachable (worked on Windows).
                // Therefore we check status depending on the response.
                if (cachedParameterQueryResponse == null) {
                    boolean wasOffline = getBridge().getStatus() == ThingStatus.OFFLINE;
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                        "Did not receive a response from BSB-LAN device. Check your configuration and if device is online.");
                    // continue processing only if we were not offline before, so things can go to OFFLINE too
                    if (wasOffline) {
                        return;
                    }
                } else {
                    // resonse received, thread device as reachable, refresh state now
                    updateStatus(ThingStatus.ONLINE);
                }

                for (BsbLanBaseThingHandler parameter : things) {
                    parameter.refresh(bridgeConfig);
                }
            };

            int interval = (config.refreshInterval != null) ? config.refreshInterval.intValue() : DEFAULT_REFRESH_INTERVAL;
            refreshJob = scheduler.scheduleWithFixedDelay(runnable, 0, interval, TimeUnit.SECONDS);
        }
    }
}
