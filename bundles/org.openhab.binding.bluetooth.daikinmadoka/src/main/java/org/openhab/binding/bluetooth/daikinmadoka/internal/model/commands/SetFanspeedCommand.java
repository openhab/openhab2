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
package org.openhab.binding.bluetooth.daikinmadoka.internal.model.commands;

import java.util.concurrent.Executor;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.util.HexUtils;
import org.openhab.binding.bluetooth.daikinmadoka.internal.model.MadokaMessage;
import org.openhab.binding.bluetooth.daikinmadoka.internal.model.MadokaProperties.FanSpeed;
import org.openhab.binding.bluetooth.daikinmadoka.internal.model.MadokaValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author blafois
 *
 */
public class SetFanspeedCommand extends BRC1HCommand {

    private final Logger logger = LoggerFactory.getLogger(SetFanspeedCommand.class);

    private FanSpeed coolingFanSpeed;
    private FanSpeed heatingFanSpeed;

    public SetFanspeedCommand(FanSpeed coolingFanSpeed, FanSpeed heatingFanSpeed) {
        this.coolingFanSpeed = coolingFanSpeed;
        this.heatingFanSpeed = heatingFanSpeed;
    }

    @Override
    public byte[] getRequest() {
        MadokaValue paramCoolingFanSpeed = new MadokaValue(0x20, 1, new byte[] { (byte) coolingFanSpeed.value() });
        MadokaValue paramHeatingFanSpeed = new MadokaValue(0x21, 1, new byte[] { (byte) heatingFanSpeed.value() });

        return MadokaMessage.createRequest(this, paramCoolingFanSpeed, paramHeatingFanSpeed);
    }

    @Override
    public boolean handleResponse(Executor executor, ResponseListener listener, MadokaMessage mm) {
        if (logger.isDebugEnabled() && mm.getRawMessage() != null) {
            byte @NonNull [] msg = (byte @NonNull []) mm.getRawMessage();
            logger.debug("Got response for {} : {}", this.getClass().getSimpleName(), HexUtils.bytesToHex(msg));
        }

        setState(State.SUCCEEDED);
        executor.execute(() -> listener.receivedResponse(this));

        return true;
    }

    @Override
    public int getCommandId() {
        return 16464;
    }

    public FanSpeed getCoolingFanSpeed() {
        return coolingFanSpeed;
    }

    public FanSpeed getHeatingFanSpeed() {
        return heatingFanSpeed;
    }

}
