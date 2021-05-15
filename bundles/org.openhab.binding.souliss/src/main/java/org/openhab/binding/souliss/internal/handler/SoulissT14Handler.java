/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.souliss.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.souliss.internal.SoulissBindingConstants;
import org.openhab.binding.souliss.internal.SoulissBindingProtocolConstants;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.types.Command;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.RefreshType;

/**
 * The {@link SoulissT14Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Tonino Fazio - Initial contribution
 * @author Luca Calcaterra - Refactor for OH3
 */

@NonNullByDefault
public class SoulissT14Handler extends SoulissGenericHandler {

    @Nullable
    Configuration gwConfigurationMap;
    byte t1nRawState;
    byte xSleepTime = 0;

    public SoulissT14Handler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            switch (channelUID.getId()) {
                case SoulissBindingConstants.PULSE_CHANNEL:
                    @Nullable
                    OnOffType valPulse = getOhStateOnOffFromSoulissVal(t1nRawState);
                    if (valPulse != null) {
                        updateState(channelUID, valPulse);
                    }
                    break;
            }
        } else {
            switch (channelUID.getId()) {
                case SoulissBindingConstants.PULSE_CHANNEL:
                    if (command.equals(OnOffType.ON)) {
                        commandSEND(SoulissBindingProtocolConstants.SOULISS_T1N_ON_CMD);
                    } else if (command.equals(OnOffType.OFF)) {
                        commandSEND(SoulissBindingProtocolConstants.SOULISS_T1N_OFF_CMD);
                    }
                    break;
            }
        }
    }

    public void setState(@Nullable PrimitiveType state) {
        super.setLastStatusStored();
        if (state != null) {
            this.updateState(SoulissBindingConstants.PULSE_CHANNEL, (OnOffType) state);
        }
    }

    @Override
    public void setRawState(byte rawState) {
        // update Last Status stored time
        super.setLastStatusStored();
        // update item state only if it is different from previous
        if (t1nRawState != rawState) {
            this.setState(getOhStateOnOffFromSoulissVal(rawState));
        }
        t1nRawState = rawState;
    }

    @Override
    public byte getRawState() {
        return t1nRawState;
    }

    @Override
    public byte getExpectedRawState(byte bCommand) {
        // Secure Send is disabled for T14
        return -1;
    }
}
