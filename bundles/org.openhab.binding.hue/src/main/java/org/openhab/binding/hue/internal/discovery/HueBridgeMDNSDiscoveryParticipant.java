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
package org.openhab.binding.hue.internal.discovery;

import static org.openhab.binding.hue.internal.HueBindingConstants.HOST;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.jmdns.ServiceInfo;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hue.internal.HueBindingConstants;
import org.openhab.binding.hue.internal.connection.Clip2Bridge;
import org.openhab.binding.hue.internal.handler.HueBridgeHandler;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.config.discovery.mdns.MDNSDiscoveryParticipant;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link HueBridgeMDNSDiscoveryParticipant} is responsible for discovering new and removed Hue Bridges. It uses the
 * central MDNSDiscoveryService.
 *
 * @author Kai Kreuzer - Initial contribution
 * @author Thomas Höfer - Added representation
 * @author Christoph Weitkamp - Change discovery protocol to mDNS
 * @author Andrew Fiddian-Green - Added support for CLIP 2 bridge discovery
 */
@Component(configurationPid = "discovery.hue")
@NonNullByDefault
public class HueBridgeMDNSDiscoveryParticipant implements MDNSDiscoveryParticipant {

    private static final String SERVICE_TYPE = "_hue._tcp.local.";
    private static final String MDNS_PROPERTY_BRIDGE_ID = "bridgeid";
    private static final String MDNS_PROPERTY_MODEL_ID = "modelid";

    private static final String CONFIG_PROPERTY_REMOVAL_GRACE_PERIOD = "removalGracePeriod";

    private final Logger logger = LoggerFactory.getLogger(HueBridgeMDNSDiscoveryParticipant.class);
    protected final ThingRegistry thingRegistry;

    private long removalGracePeriod = 0L;
    private boolean isAutoDiscoveryEnabled = true;

    @Activate
    public HueBridgeMDNSDiscoveryParticipant(final @Reference ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    @Activate
    protected void activate(ComponentContext componentContext) {
        activateOrModifyService(componentContext);
    }

    @Modified
    protected void modified(ComponentContext componentContext) {
        activateOrModifyService(componentContext);
    }

    private void activateOrModifyService(ComponentContext componentContext) {
        Dictionary<String, @Nullable Object> properties = componentContext.getProperties();
        String autoDiscoveryPropertyValue = (String) properties
                .get(DiscoveryService.CONFIG_PROPERTY_BACKGROUND_DISCOVERY);
        if (autoDiscoveryPropertyValue != null && !autoDiscoveryPropertyValue.isBlank()) {
            isAutoDiscoveryEnabled = Boolean.valueOf(autoDiscoveryPropertyValue);
        }
        String removalGracePeriodPropertyValue = (String) properties.get(CONFIG_PROPERTY_REMOVAL_GRACE_PERIOD);
        if (removalGracePeriodPropertyValue != null && !removalGracePeriodPropertyValue.isBlank()) {
            try {
                removalGracePeriod = Long.parseLong(removalGracePeriodPropertyValue);
            } catch (NumberFormatException e) {
                logger.warn("Configuration property '{}' has invalid value: {}", CONFIG_PROPERTY_REMOVAL_GRACE_PERIOD,
                        removalGracePeriodPropertyValue);
            }
        }
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return HueBridgeHandler.SUPPORTED_THING_TYPES;
    }

    @Override
    public String getServiceType() {
        return SERVICE_TYPE;
    }

    @Override
    public @Nullable DiscoveryResult createResult(ServiceInfo service) {
        if (isAutoDiscoveryEnabled) {
            ThingUID uid = getThingUID(service);
            if (Objects.nonNull(uid)) {
                String host = service.getHostAddresses()[0];
                String serial = service.getPropertyString(MDNS_PROPERTY_BRIDGE_ID);
                String label = String.format("%s (%s)", service.getName(), host);
                String legacyThingUID = null;

                if (new ThingUID(HueBindingConstants.THING_TYPE_CLIP2, uid.getId()).equals(uid)) {
                    Optional<Thing> legacyThingOptional = getLegacyThing(host);
                    if (legacyThingOptional.isPresent()) {
                        Thing legacyThing = legacyThingOptional.get();
                        legacyThingUID = legacyThing.getUID().getAsString();
                        String label2 = legacyThing.getLabel();
                        label = Objects.nonNull(label2) ? label2 : label;
                    }
                    serial = serial + HueBindingConstants.API2_PROPERTY_SUFFIX;
                    label = label + HueBindingConstants.API2_PROPERTY_SUFFIX;
                }

                DiscoveryResultBuilder builder = DiscoveryResultBuilder.create(uid) //
                        .withLabel(label) //
                        .withProperty(HOST, host) //
                        .withProperty(Thing.PROPERTY_MODEL_ID, service.getPropertyString(MDNS_PROPERTY_MODEL_ID)) //
                        .withProperty(Thing.PROPERTY_SERIAL_NUMBER, serial.toLowerCase()) //
                        .withRepresentationProperty(Thing.PROPERTY_SERIAL_NUMBER) //
                        .withTTL(120L);

                if (Objects.nonNull(legacyThingUID)) {
                    builder = builder.withProperty(HueBindingConstants.PROPERTY_LEGACY_THING_UID, legacyThingUID);
                }
                return builder.build();
            }
        }
        return null;
    }

    /**
     * Get the legacy Hue bridge (if any) on the given IP address.
     *
     * @param ipAddress the IP address.
     * @return Optional of a legacy bridge thing.
     */
    private Optional<Thing> getLegacyThing(String ipAddress) {
        return thingRegistry.getAll().stream()
                .filter(thing -> HueBindingConstants.THING_TYPE_BRIDGE.equals(thing.getThingTypeUID())
                        && ipAddress.equals(thing.getConfiguration().get(HOST)))
                .findFirst();
    }

    @Override
    public @Nullable ThingUID getThingUID(ServiceInfo service) {
        String id = service.getPropertyString(MDNS_PROPERTY_BRIDGE_ID);
        if (id != null && !id.isBlank()) {
            id = id.toLowerCase();
            try {
                return Clip2Bridge.isClip2Supported(service.getHostAddresses()[0])
                        ? new ThingUID(HueBindingConstants.THING_TYPE_CLIP2, id)
                        : new ThingUID(HueBindingConstants.THING_TYPE_BRIDGE, id);
            } catch (IOException e) {
                // fall through
            }
        }
        return null;
    }

    @Override
    public long getRemovalGracePeriodSeconds(ServiceInfo service) {
        return removalGracePeriod;
    }
}
