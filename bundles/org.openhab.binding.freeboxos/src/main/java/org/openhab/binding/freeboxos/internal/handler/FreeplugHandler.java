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
package org.openhab.binding.freeboxos.internal.handler;

import static org.openhab.binding.freeboxos.internal.FreeboxOsBindingConstants.*;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.freeboxos.internal.action.FreeplugActions;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.freeplug.FreeplugManager;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link FreeplugHandler} is responsible for handling everything associated to a CPL gateway managed by the freebox
 * server
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class FreeplugHandler extends ApiConsumerHandler implements NetworkHostIntf {
    private final Logger logger = LoggerFactory.getLogger(FreeplugHandler.class);

    public FreeplugHandler(Thing thing) {
        super(thing);
    }

    @Override
    void initializeProperties(Map<String, String> properties) throws FreeboxException {
        getManager(FreeplugManager.class).getPlug(getMac()).ifPresent(plug -> {
            properties.put(Thing.PROPERTY_MODEL_ID, plug.getModel());
            properties.put(ROLE, plug.getNetRole().name());
            properties.put(NET_ID, plug.getNetId());
            properties.put(ETHERNET_SPEED, String.format("%d Mb/s", plug.getEthSpeed()));
            properties.put(LOCAL, Boolean.valueOf(plug.isLocal()).toString());
            properties.put(FULL_DUPLEX, Boolean.valueOf(plug.isEthFullDuplex()).toString());
        });
    }

    @Override
    protected void internalPoll() throws FreeboxException {
        getManager(FreeplugManager.class).getPlug(getMac()).ifPresent(plug -> {
            ZonedDateTime lastSeen = ZonedDateTime.now().minusSeconds(plug.getInactive());
            updateChannelDateTimeState(CONNECTION_STATUS, LAST_SEEN, lastSeen);

            updateChannelString(CONNECTION_STATUS, LINE_STATUS, plug.getEthPortStatus());
            updateChannelOnOff(CONNECTION_STATUS, REACHABLE, plug.hasNetwork());

            updateRateChannel(RATE_DOWN, plug.getRxRate());
            updateRateChannel(RATE_UP, plug.getTxRate());
        });
    }

    private void updateRateChannel(String channel, int rate) {
        QuantityType<?> qtty = rate != -1 ? new QuantityType<>(rate, Units.MEGABIT_PER_SECOND) : null;
        updateChannelQuantity(CONNECTION_STATUS, channel, qtty);
    }

    public void reset() {
        try {
            getManager(FreeplugManager.class).reboot(getMac());
            logger.info("Freeplug {} succesfully restarted", getMac());
        } catch (FreeboxException e) {
            logger.warn("Error restarting freeplug : {}", e.getMessage());
        }
    }

    @Override
    public Configuration getConfig() {
        return super.getConfig();
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(FreeplugActions.class);
    }

}
