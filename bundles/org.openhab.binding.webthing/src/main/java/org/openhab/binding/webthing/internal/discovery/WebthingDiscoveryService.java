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
package org.openhab.binding.webthing.internal.discovery;

import static org.openhab.binding.webthing.internal.WebThingBindingConstants.MDNS_SERVICE_TYPE;
import static org.openhab.binding.webthing.internal.WebThingBindingConstants.THING_TYPE_UID;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.webthing.internal.client.DescriptionLoader;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.io.transport.mdns.MDNSClient;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebThing discovery service based on mDNS. Refer https://iot.mozilla.org/wot/#web-thing-discovery
 *
 * @author Gregor Roth - Initial contribution
 */
@NonNullByDefault
@Component(service = DiscoveryService.class, configurationPid = "webthingdiscovery.mdns")
public class WebthingDiscoveryService extends AbstractDiscoveryService implements ServiceListener {
    private static final Duration FOREGROUND_SCAN_TIMEOUT = Duration.ofMillis(200);
    private final Logger logger = LoggerFactory.getLogger(WebthingDiscoveryService.class);
    private final DescriptionLoader descriptionLoader = new DescriptionLoader();
    private final MDNSClient mdnsClient;

    /**
     * constructor
     *
     * @param configProperties the config props
     * @param mdnsClient the underlying mDNS client
     */
    @Activate
    public WebthingDiscoveryService(@Nullable Map<String, Object> configProperties, @Reference MDNSClient mdnsClient) {
        super(30);
        this.mdnsClient = mdnsClient;

        super.activate(configProperties);
        if (isBackgroundDiscoveryEnabled()) {
            mdnsClient.addServiceListener(MDNS_SERVICE_TYPE, this);
        }
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypes() {
        return Set.of(THING_TYPE_UID);
    }

    @Deactivate
    @Override
    protected void deactivate() {
        super.deactivate();
        mdnsClient.removeServiceListener(MDNS_SERVICE_TYPE, this);
    }

    @Override
    public void serviceAdded(@NonNullByDefault({}) ServiceEvent serviceEvent) {
        considerService(serviceEvent);
    }

    @Override
    public void serviceResolved(@NonNullByDefault({}) ServiceEvent serviceEvent) {
        considerService(serviceEvent);
    }

    @Override
    public void serviceRemoved(@NonNullByDefault({}) ServiceEvent serviceEvent) {
        for (var discoveryResult : discoverWebThing(serviceEvent.getInfo())) {
            thingRemoved(discoveryResult.getThingUID());
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        mdnsClient.addServiceListener(MDNS_SERVICE_TYPE, this);
        startScan(true);
    }

    @Override
    protected void stopBackgroundDiscovery() {
        mdnsClient.removeServiceListener(MDNS_SERVICE_TYPE, this);
    }

    private void startScan(boolean isBackground) {
        scheduler.schedule(() -> scan(isBackground), 0, TimeUnit.SECONDS);
    }

    @Override
    protected void startScan() {
        startScan(false);
    }

    @Override
    protected synchronized void stopScan() {
        removeOlderResults(Instant.now().minus(Duration.ofMinutes(10)).toEpochMilli());
        super.stopScan();
    }

    /**
     * scans the network via mDNS
     *
     * @param isBackground true, if is background task
     */
    private void scan(boolean isBackground) {
        var serviceInfos = isBackground ? mdnsClient.list(MDNS_SERVICE_TYPE)
                : mdnsClient.list(MDNS_SERVICE_TYPE, FOREGROUND_SCAN_TIMEOUT);
        logger.debug("got {} mDNS entries", serviceInfos.length);

        // create discovery task for each detected service and process these in parallel to increase total discovery
        // speed
        var discoveryTasks = Arrays.stream(serviceInfos).map(DiscoveryTask::new).collect(Collectors.toList());
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            for (var future : exec.invokeAll(discoveryTasks, 5, TimeUnit.MINUTES)) {
                future.get(5, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            logger.warn("error occurred by discovering", e);
        } finally {
            exec.shutdown();
        }
    }

    private class DiscoveryTask implements Callable<Set<DiscoveryResult>> {
        private final ServiceInfo serviceInfo;

        DiscoveryTask(ServiceInfo serviceInfo) {
            this.serviceInfo = serviceInfo;
        }

        @Override
        public Set<DiscoveryResult> call() {
            var results = new HashSet<DiscoveryResult>();
            try {
                for (var discoveryResult : discoverWebThing(serviceInfo)) {
                    results.add(discoveryResult);
                    thingDiscovered(discoveryResult);
                    logger.debug("WebThing '{}' ({}) discovered", discoveryResult.getLabel(),
                            discoveryResult.getProperties().get("webThingURI"));
                }
            } catch (Exception e) {
                logger.warn("error occurred by discovering {}", serviceInfo.getNiceTextString(), e);
            }
            return results;
        }
    }

    /**
     * convert the serviceInfo result of the mDNS scan to discovery results
     *
     * @param serviceInfo the service info
     * @return the associated discovery result
     */
    private Set<DiscoveryResult> discoverWebThing(ServiceInfo serviceInfo) {
        var discoveryResults = new HashSet<DiscoveryResult>();

        if (serviceInfo.getHostAddresses().length > 0) {
            var host = serviceInfo.getHostAddresses()[0];
            var port = serviceInfo.getPort();
            var path = "/";
            if (Collections.list(serviceInfo.getPropertyNames()).contains("path")) {
                path = serviceInfo.getPropertyString("path");
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
            }

            // There are two kinds of WebThing endpoints: Endpoints supporting a single WebThing as well as
            // endpoints supporting multiple WebThings.
            //
            // In the routine below the enpoint will be checked for single WebThings first, than for multiple
            // WebThings if a ingle WebTHing has not been found.
            // Furthermore, first it will be tried to connect the endppint using https. If this fails, as fallback
            // plain http is used.

            // check single WebThing path via https (e.g. https://192.168.0.23:8433/)
            var optionalDiscoveryResult = discoverWebThing(toURI(host, port, path, true));
            if (optionalDiscoveryResult.isPresent()) {
                discoveryResults.add(optionalDiscoveryResult.get());
            } else {
                // check single WebThing path via plain http (e.g. http://192.168.0.23:8433/)
                optionalDiscoveryResult = discoverWebThing(toURI(host, port, path, false));
                if (optionalDiscoveryResult.isPresent()) {
                    discoveryResults.add(optionalDiscoveryResult.get());
                } else {
                    // check multiple WebThing path via https (e.g. https://192.168.0.23:8433/0,
                    // https://192.168.0.23:8433/1,...)
                    outer: for (int i = 0; i < 50; i++) { // search 50 entries at maximum
                        optionalDiscoveryResult = discoverWebThing(toURI(host, port, path + i, true));
                        if (optionalDiscoveryResult.isPresent()) {
                            discoveryResults.add(optionalDiscoveryResult.get());
                        } else if (i == 0) {
                            // check multiple WebThing path via plain http (e.g. http://192.168.0.23:8433/0,
                            // http://192.168.0.23:8433/1,...)
                            for (int j = 0; j < 50; j++) { // search 50 entries at maximum
                                optionalDiscoveryResult = discoverWebThing(toURI(host, port, path + j, false));
                                if (optionalDiscoveryResult.isPresent()) {
                                    discoveryResults.add(optionalDiscoveryResult.get());
                                } else {
                                    break outer;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return discoveryResults;
    }

    private Optional<DiscoveryResult> discoverWebThing(URI uri) {
        try {
            var description = descriptionLoader.loadWebthingDescription(uri, Duration.ofSeconds(5));

            var id = (uri.getHost() + uri.getPort() + uri.getPath()).replaceAll("\\W", "");
            var thingUID = new ThingUID(THING_TYPE_UID, id);
            return Optional.of(DiscoveryResultBuilder.create(thingUID).withThingType(THING_TYPE_UID)
                    .withProperty("webThingURI", uri).withLabel(description.title).build());
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    private URI toURI(String host, int port, String path, boolean isHttps) {
        return isHttps ? URI.create("https://" + host + ":" + port + path)
                : URI.create("http://" + host + ":" + port + path);
    }

    private void considerService(ServiceEvent serviceEvent) {
        if (isBackgroundDiscoveryEnabled()) {
            for (var discoveryResult : discoverWebThing(serviceEvent.getInfo())) {
                thingDiscovered(discoveryResult);
            }
        }
    }
}
