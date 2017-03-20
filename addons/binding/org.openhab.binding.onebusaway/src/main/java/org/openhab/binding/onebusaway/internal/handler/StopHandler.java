/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onebusaway.internal.handler;

import static org.openhab.binding.onebusaway.OneBusAwayBindingConstants.THING_TYPE_STOP;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.onebusaway.internal.config.StopConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

/**
 * The {@link StopHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Shawn Wilsher - Initial contribution
 */
public class StopHandler extends BaseBridgeHandler {

    public static final ThingTypeUID SUPPORTED_THING_TYPE = THING_TYPE_STOP;

    private StopConfiguration config;
    private Gson gson;
    private HttpClient httpClient;
    private Logger logger = LoggerFactory.getLogger(StopHandler.class);
    private ScheduledFuture<?> pollingJob;
    private Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            fetchAndUpdateStopData();
        }
    };
    private long routeDataLastUpdateMs = 0;
    private Multimap<String, ObaStopArrivalResponse.ArrivalAndDeparture> routeData = ArrayListMultimap.create();
    private List<RouteDataListener> routeDataListeners = new CopyOnWriteArrayList<>();

    public StopHandler(Bridge bridge) {
        super(bridge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (RefreshType.REFRESH == command) {
            logger.debug("Refreshing {}...", channelUID);
            forceUpdate();
        } else {
            logger.warn("The OneBusAway binding is a read-only binding and can not handle commands.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUpdate(ChannelUID channelUID, State newState) {
        logger.warn("The OneBusAway binding is a read-only binding and can not handle channel updates.");
        super.handleUpdate(channelUID, newState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        logger.debug("Initializing OneBusAway stop bridge...");
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Checking configuration...");

        config = loadAndCheckConfiguration();
        if (config == null) {
            logger.debug("Initialization of OneBusAway bridge failed!");
            return;
        }

        // Do the rest of the work asynchronously because it can take a while.
        scheduler.submit(new Runnable() {
            @Override
            public void run() {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Initializing...");
                httpClient = new HttpClient();
                try {
                    httpClient.start();
                } catch (Exception e) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
                    httpClient = null;
                    return;
                }
                gson = new Gson();

                if (!fetchAndUpdateStopData()) {
                    return;
                }

                scheduleJob();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        if (pollingJob != null && !pollingJob.isCancelled()) {
            pollingJob.cancel(true);
            pollingJob = null;
        }
        if (httpClient != null) {
            httpClient.destroy();
            httpClient = null;
        }
    }

    /**
     * Registers the listener to receive updates about arrival and departure times for its route.
     *
     * @param listener
     * @return true if successful.
     */
    protected boolean registerRouteDataListener(RouteDataListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("It makes no sense to register a null listener!");
        }
        boolean added = routeDataListeners.add(listener);
        if (added) {
            String routeId = listener.getRouteId();
            List<ObaStopArrivalResponse.ArrivalAndDeparture> copiedRouteData;
            synchronized (routeData) {
                copiedRouteData = Lists.newArrayList(routeData.get(routeId));
            }
            Collections.sort(copiedRouteData);
            listener.onNewRouteData(routeDataLastUpdateMs, copiedRouteData);
        }
        return added;
    }

    /**
     * Unregisters the listener so it no longer receives updates about arrival and departure times for its route.
     *
     * @param listener
     * @return true if successful.
     */
    protected boolean unregisterRouteDataListener(RouteDataListener listener) {
        return routeDataListeners.remove(listener);
    }

    /**
     * Forced an update to be scheduled immediately, and reschedules our normal task to run again on our normal interval
     * starting now.
     */
    protected void forceUpdate() {
        if (pollingJob != null) {
            pollingJob.cancel(true);
            pollingJob = null;
        }
        scheduler.execute(pollingRunnable);
        scheduleJob();
    }

    private ApiHandler getApiHandler() {
        Bridge bridge = getBridge();
        if (bridge == null || !(bridge.getHandler() instanceof ApiHandler)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "No API bridge available");
            return null;
        }
        return (ApiHandler) bridge.getHandler();
    }

    private StopConfiguration loadAndCheckConfiguration() {
        StopConfiguration config = getConfigAs(StopConfiguration.class);
        if (config.getInterval() == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "interval is not set");
            return null;
        }
        if (config.getStopId() == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "stopId is not set");
            return null;
        }
        return config;
    }

    private void scheduleJob() {
        assert (pollingJob == null);
        pollingJob = scheduler.scheduleWithFixedDelay(pollingRunnable, config.getInterval(), config.getInterval(),
                TimeUnit.SECONDS);
    }

    private boolean fetchAndUpdateStopData() {
        ApiHandler apiHandler = getApiHandler();
        if (apiHandler == null) {
            // We must be offline.
            return false;
        }
        String url = String.format("http://%s/api/where/arrivals-and-departures-for-stop/%s.json?key=%s",
                apiHandler.getApiServer(), config.getStopId(), apiHandler.getApiKey());
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            logger.error("Unable to parse '%s' as a URI.", url);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                    "stopId or apiKey is set to a bogus value");
            return false;
        }
        ContentResponse response;
        try {
            response = httpClient.newRequest(uri).send();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
            return false;
        }
        if (response.getStatus() != HttpStatus.OK_200) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                    String.format("While fetching stop data: %d: %s", response.getStatus(), response.getReason()));
            return false;
        }
        ObaStopArrivalResponse data = gson.fromJson(response.getContentAsString(), ObaStopArrivalResponse.class);
        routeDataLastUpdateMs = data.currentTime;
        updateStatus(ThingStatus.ONLINE);

        ArrayListMultimap<String, ObaStopArrivalResponse.ArrivalAndDeparture> copiedRouteData = ArrayListMultimap
                .create();
        synchronized (routeData) {
            routeData = ArrayListMultimap.create();
            for (ObaStopArrivalResponse.ArrivalAndDeparture d : data.data.arrivalsAndDepartures) {
                routeData.put(d.routeId, d);
            }
            for (String key : routeData.keySet()) {
                List<ObaStopArrivalResponse.ArrivalAndDeparture> copy = Lists.newArrayList(routeData.get(key));
                Collections.sort(copy);
                copiedRouteData.putAll(key, copy);
            }
        }
        for (RouteDataListener listener : routeDataListeners) {
            listener.onNewRouteData(routeDataLastUpdateMs, copiedRouteData.get(listener.getRouteId()));
        }
        return true;
    }
}
