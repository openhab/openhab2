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
package org.openhab.binding.juicenet.internal.discovery;

import static org.openhab.binding.juicenet.internal.JuiceNetBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.juicenet.internal.handler.JuiceNetBridgeHandler;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link JuiceNetDiscoveryService} discovers all devices/zones reported by the FlumeTech Cloud. This requires the
 * api
 * key to get access to the cloud data.
 *
 * @author Jeff James - Initial contribution
 */
@NonNullByDefault
public class JuiceNetDiscoveryService extends AbstractDiscoveryService
        implements DiscoveryService, ThingHandlerService {
    private final Logger logger = LoggerFactory.getLogger(JuiceNetDiscoveryService.class);

    private @Nullable JuiceNetBridgeHandler bridgeHandler;

    private static final Set<ThingTypeUID> DISCOVERABLE_THING_TYPES_UIDS = Set.of(DEVICE_THING_TYPE);

    public JuiceNetDiscoveryService() {
        super(DISCOVERABLE_THING_TYPES_UIDS, 0, false);
        // String uids = SUPPORTED_THING_TYPES_UIDS.toString();
        // logger.debug("Thing types: {} registered.", uids);
    }

    /**
     * Activate the bundle: save properties
     *
     * @param componentContext
     * @param configProperties set of properties from cfg (use same names as in thing config)
     */
    @Override
    public void activate() {
        super.activate(null);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    @Override
    protected synchronized void startScan() {
        // ignore startScan requests
    }

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof JuiceNetBridgeHandler) {
            this.bridgeHandler = (JuiceNetBridgeHandler) handler;
            this.bridgeHandler.setDiscoveryService(this);
        } else {
            this.bridgeHandler = null;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return this.bridgeHandler;
    }

    public void notifyDiscoveryDevice(String id, String name, String token) {
        Objects.requireNonNull(bridgeHandler, "Discovery with null bridgehandler.");
        ThingUID bridgeUID = bridgeHandler.getThing().getUID();

        ThingUID uid = new ThingUID(DEVICE_THING_TYPE, bridgeUID, id);

        Map<@NonNull String, @NonNull Object> properties = new HashMap<>();
        properties.put(DEVICE_PROP_UNIT_ID, id);
        properties.put(DEVICE_PROP_NAME, name);
        properties.put(DEVICE_PROP_TOKEN, token);

        DiscoveryResult result = DiscoveryResultBuilder.create(uid).withBridge(bridgeUID).withProperties(properties)
                .withLabel(name).build();
        thingDiscovered(result);
        logger.debug("Discovered JuiceNetDevice {} - {}", uid, name);
    }
}
