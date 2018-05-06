/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.amazonechocontrol.internal.discovery;

import static org.openhab.binding.amazonechocontrol.AmazonEchoControlBindingConstants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.amazonechocontrol.internal.jsons.JsonDevices.Device;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AmazonEchoDiscovery} is responsible for discovering echo devices on
 * the amazon account specified in the binding.
 *
 * @author Michael Geramb - Initial contribution
 */
@NonNullByDefault
@Component(service = DiscoveryService.class, immediate = true, configurationPid = "discovery.amazonechocontrol")
public class AmazonEchoDiscovery extends AbstractDiscoveryService {

    private @Nullable ThingRegistry thingRegistry;
    private boolean discoverAccount = true;
    private final Set<IAmazonAccountHandler> discoveryServices = new HashSet<>();
    public @Nullable static AmazonEchoDiscovery instance;

    private final Logger logger = LoggerFactory.getLogger(AmazonEchoDiscovery.class);
    private final Map<String, ThingUID> lastDeviceInformations = new HashMap<>();
    private final HashSet<String> discoverdFlashBriefings = new HashSet<String>();

    @Nullable
    ScheduledFuture<?> startScanStateJob;
    long activateTimeStamp;

    @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
    protected void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    protected void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    public void resetDiscoverAccount() {
        this.discoverAccount = false;
    }

    public void addAccountHandler(IAmazonAccountHandler discoveryService) {
        synchronized (discoveryServices) {
            discoveryServices.add(discoveryService);
        }
    }

    public void removeAccountHandler(IAmazonAccountHandler discoveryService) {
        synchronized (discoveryServices) {
            discoveryServices.remove(discoveryService);
        }
    }

    public AmazonEchoDiscovery() {
        super(SUPPORTED_THING_TYPES_UIDS, 10);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    @Override
    protected void startScan() {
        startScan(true);
    }

    protected void startAutomaticScan() {
        startScan(false);
    }

    void startScan(boolean manual) {
        stopScanJob();
        removeOlderResults(activateTimeStamp);
        if (discoverAccount) {

            discoverAccount = false;
            // No accounts created yet, create one
            ThingUID thingUID = new ThingUID(THING_TYPE_ACCOUNT, "account1");

            DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel("Amazon Account").build();
            logger.debug("Device [Amazon Account] found.");
            thingDiscovered(result);
        }

        IAmazonAccountHandler[] accounts;
        synchronized (discoveryServices) {
            accounts = new IAmazonAccountHandler[discoveryServices.size()];
            accounts = discoveryServices.toArray(accounts);
        }

        for (IAmazonAccountHandler discovery : accounts) {
            discovery.updateDeviceList(manual);
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        AmazonEchoDiscovery.instance = this;
        stopScanJob();
        startScanStateJob = scheduler.schedule(this::startAutomaticScan, 3000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void stopBackgroundDiscovery() {
        AmazonEchoDiscovery.instance = null;
        stopScanJob();
    }

    void stopScanJob() {
        @Nullable
        ScheduledFuture<?> currentStartScanStateJob = startScanStateJob;
        if (currentStartScanStateJob != null) {
            currentStartScanStateJob.cancel(false);
            startScanStateJob = null;
        }
    }

    @Override
    @Activate
    public void activate(@Nullable Map<String, @Nullable Object> config) {
        super.activate(config);
        if (config != null) {
            modified(config);
        }
        activateTimeStamp = new Date().getTime();
    };

    public synchronized void setDevices(ThingUID brigdeThingUID, List<Device> deviceList) {
        ThingRegistry thingRegistry = this.thingRegistry;
        if (thingRegistry == null) {
            return;
        }

        Set<String> toRemove = new HashSet<String>(lastDeviceInformations.keySet());
        for (Device device : deviceList) {
            String serialNumber = device.serialNumber;
            if (serialNumber != null) {
                boolean alreadyfound = toRemove.remove(serialNumber);
                // new
                String deviceFamily = device.deviceFamily;
                if (!alreadyfound && deviceFamily != null) {
                    ThingTypeUID thingTypeId;
                    if (deviceFamily.equals("ECHO")) {
                        thingTypeId = THING_TYPE_ECHO;
                    } else if (deviceFamily.equals("ROOK")) {
                        thingTypeId = THING_TYPE_ECHO_SPOT;
                    } else if (deviceFamily.equals("KNIGHT")) {
                        thingTypeId = THING_TYPE_ECHO_SHOW;
                    } else if (deviceFamily.equals("WHA")) {
                        thingTypeId = THING_TYPE_ECHO_WHA;
                    } else {
                        thingTypeId = THING_TYPE_UNKNOWN;
                    }

                    ThingUID thingUID = new ThingUID(thingTypeId, brigdeThingUID, serialNumber);
                    // Check if already created
                    if (thingRegistry.get(thingUID) == null) {
                        DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel(device.accountName)
                                .withProperty(DEVICE_PROPERTY_SERIAL_NUMBER, serialNumber)
                                .withProperty(DEVICE_PROPERTY_FAMILY, deviceFamily)
                                .withRepresentationProperty(DEVICE_PROPERTY_SERIAL_NUMBER).withBridge(brigdeThingUID)
                                .build();

                        logger.debug("Device [{}: {}] found. Mapped to thing type {}", device.deviceFamily,
                                serialNumber, thingTypeId.getAsString());

                        thingDiscovered(result);
                        lastDeviceInformations.put(serialNumber, thingUID);
                    }
                }
            }
        }
    }

    public synchronized void discoverFlashBriefingProfiles(ThingUID brigdeThingUID, String currentFlashBriefingJson) {
        if (currentFlashBriefingJson.isEmpty()) {
            return;
        }
        if (discoverdFlashBriefings.contains(currentFlashBriefingJson)) {
            return;
        }

        if (!discoverdFlashBriefings.contains(currentFlashBriefingJson)) {

            String id = UUID.randomUUID().toString();
            ThingUID thingUID = new ThingUID(THING_TYPE_FLASH_BRIEFING_PROFILE, brigdeThingUID, id);

            DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withLabel("FlashBriefing")
                    .withProperty(DEVICE_PROPERTY_FLASH_BRIEFING_PROFILE, currentFlashBriefingJson)
                    .withBridge(brigdeThingUID).build();
            logger.debug("Flash Briefing {} discovered", currentFlashBriefingJson);

            thingDiscovered(result);
            discoverdFlashBriefings.add(currentFlashBriefingJson);
        }
    }

    public synchronized void removeExistingEchoHandler(ThingUID uid) {
        for (String id : lastDeviceInformations.keySet()) {
            if (lastDeviceInformations.get(id).equals(uid)) {
                lastDeviceInformations.remove(id);
            }
        }
    }

    public synchronized void removeExistingFlashBriefingProfile(@Nullable String currentFlashBriefingJson) {
        if (currentFlashBriefingJson != null) {
            discoverdFlashBriefings.remove(currentFlashBriefingJson);
        }
    }
}
