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
/**
 *
 */
package org.openhab.binding.gpio.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.gpio.internal.configuration.PigpioBridgeConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;

import eu.xeli.jpigpio.JPigpio;
import eu.xeli.jpigpio.PigpioException;
import eu.xeli.jpigpio.PigpioSocket;

/**
 * Remote JPigpio Bridge Hanlder
 *
 * This bridge is used to control remote pigpio instances.
 *
 * @author Nils Bauer - Initial contribution
 */
@NonNullByDefault
public class PigpioRemoteBridgeHandler extends BaseBridgeHandler implements PigpioBridgeHandler {

    /** The JPigpio instance. */
    private @Nullable JPigpio jPigpio;

    /**
     * Instantiates a new pigpio remote bridge handler.
     *
     * @param bridge the bridge
     */
    public PigpioRemoteBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void initialize() {
        PigpioBridgeConfiguration config = getConfigAs(PigpioBridgeConfiguration.class);
        String ipAddress = config.ipAddress;
        Integer port = config.port;

        if (ipAddress == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                    "Cannot connect to PiGPIO Service on remote raspberry. IP address not set.");
        } else {
            try {
                jPigpio = new PigpioSocket(ipAddress, port);
                updateStatus(ThingStatus.ONLINE);
            } catch (NumberFormatException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "Port not numeric");
            } catch (PigpioException e) {
                if (e.getErrorCode() == PigpioException.PI_BAD_SOCKET_PORT) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR,
                            "Port out of range");
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                            e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public @Nullable JPigpio getJPiGpio() {
        return jPigpio;
    }
}
