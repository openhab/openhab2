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
package org.openhab.binding.alarmdecoder.internal.handler;

import static org.openhab.binding.alarmdecoder.internal.AlarmDecoderBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.alarmdecoder.internal.config.RFZoneConfig;
import org.openhab.binding.alarmdecoder.internal.protocol.RFXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RFZoneHandler} is responsible for handling wired zones (i.e. RFX messages).
 *
 * @author Bob Adair - Initial contribution
 * @author Bill Forsyth - Initial contribution
 */
@NonNullByDefault
public class RFZoneHandler extends ADThingHandler {

    private final Logger logger = LoggerFactory.getLogger(RFZoneHandler.class);

    private @NonNullByDefault({}) RFZoneConfig config;

    public RFZoneHandler(Thing thing) {
        super(thing);
    }

    /**
     * Returns true if this handler is responsible for the zone with the supplied address and channel.
     */
    public boolean responsibleFor(final int serial) {
        return (config.serial != null && config.serial.equals(serial));
    }

    @Override
    public void initialize() {
        config = getConfigAs(RFZoneConfig.class);

        if (config.serial == null || config.serial < 0) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);
            return;
        }
        logger.debug("RF Zone handler initializing for serial {}", config.serial);

        initDeviceState();

        logger.trace("RF Zone handler finished initializing");
    }

    @Override
    protected void initDeviceState() {
        logger.trace("Initializing device state for RF Zone {}", config.serial);
        Bridge bridge = getBridge();
        if (bridge == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "No bridge configured");
        } else if (bridge.getStatus() == ThingStatus.ONLINE) {
            initChannelState();
            firstUpdateReceived.set(false);
            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        }
    }

    /**
     * Set contact channel states to "UNDEF" at init time. The real states will be set either when the first message
     * arrives for the zone, or they will be set to "CLOSED" the first time the panel goes into the "READY" state.
     */
    @Override
    public void initChannelState() {
        UnDefType state = UnDefType.UNDEF;
        updateState(CHANNEL_RF_LOWBAT, state);
        updateState(CHANNEL_RF_SUPERVISION, state);
        updateState(CHANNEL_RF_LOOP1, state);
        updateState(CHANNEL_RF_LOOP2, state);
        updateState(CHANNEL_RF_LOOP3, state);
        updateState(CHANNEL_RF_LOOP4, state);
    }

    @Override
    public void notifyPanelReady() {
        logger.trace("RF Zone handler for {} received panel ready notification.", config.serial);
        if (firstUpdateReceived.compareAndSet(false, true)) {
            updateState(CHANNEL_RF_LOOP1, OpenClosedType.CLOSED);
            updateState(CHANNEL_RF_LOOP2, OpenClosedType.CLOSED);
            updateState(CHANNEL_RF_LOOP3, OpenClosedType.CLOSED);
            updateState(CHANNEL_RF_LOOP4, OpenClosedType.CLOSED);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Does not accept any commands
    }

    public void handleUpdate(int data) {
        logger.trace("RF Zone handler for serial {} received update: {}", config.serial, data);
        firstUpdateReceived.set(true);

        updateState(CHANNEL_RF_LOWBAT, (data & RFXMessage.BIT_LOWBAT) == 0 ? OnOffType.OFF : OnOffType.ON);
        updateState(CHANNEL_RF_SUPERVISION, (data & RFXMessage.BIT_SUPER) == 0 ? OnOffType.OFF : OnOffType.ON);

        updateState(CHANNEL_RF_LOOP1, (data & RFXMessage.BIT_LOOP1) == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
        updateState(CHANNEL_RF_LOOP2, (data & RFXMessage.BIT_LOOP2) == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
        updateState(CHANNEL_RF_LOOP3, (data & RFXMessage.BIT_LOOP3) == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
        updateState(CHANNEL_RF_LOOP4, (data & RFXMessage.BIT_LOOP4) == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
    }
}
