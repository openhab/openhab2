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
package org.openhab.binding.upb.internal;

import java.math.BigDecimal;
import java.util.Dictionary;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.openhab.binding.upb.Constants;
import org.openhab.binding.upb.handler.SerialPIMHandler;
import org.openhab.binding.upb.handler.UPBThingHandler;
import org.openhab.binding.upb.handler.VirtualThingHandler;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for UPB handlers.
 *
 * @author Marcus Better - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.upb")
@NonNullByDefault
public class UPBHandlerFactory extends BaseThingHandlerFactory {
    private final Logger logger = LoggerFactory.getLogger(UPBHandlerFactory.class);

    @Nullable
    private SerialPortManager serialPortManager;
    @Nullable
    private Byte networkId;

    @Override
    protected void activate(final ComponentContext componentContext) {
        super.activate(componentContext);
        final Dictionary<String, Object> config = componentContext.getProperties();
        final BigDecimal nid = (BigDecimal) config.get(Constants.CONFIGURATION_NETWORK_ID);
        if (nid != null) {
            if (nid.compareTo(BigDecimal.ZERO) < 0 || nid.compareTo(BigDecimal.valueOf(255)) > 0) {
                logger.warn("invalid network ID {}", nid);
                throw new IllegalArgumentException("network ID out of range");
            }
            networkId = nid.byteValue();
        }
    }

    @Override
    public boolean supportsThingType(final ThingTypeUID thingTypeUID) {
        return Constants.BINDING_ID.equals(thingTypeUID.getBindingId());
    }

    @Override
    protected @Nullable ThingHandler createHandler(final Thing thing) {
        logger.debug("Creating thing {}", thing.getUID());
        final ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(Constants.PIM_UID)) {
            assert serialPortManager != null;
            return new SerialPIMHandler((Bridge) thing, (@NonNull SerialPortManager) serialPortManager);
        } else if (thingTypeUID.equals(Constants.VIRTUAL_DEVICE_UID)) {
            return new VirtualThingHandler(thing, networkId);
        } else {
            return new UPBThingHandler(thing, networkId);
        }
    }

    @Reference
    protected void setSerialPortManager(final SerialPortManager serialPortManager) {
        this.serialPortManager = serialPortManager;
    }

    protected void unsetSerialPortManager(final SerialPortManager serialPortManager) {
        this.serialPortManager = null;
    }
}
