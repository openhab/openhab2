/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mihome.handler;

import static org.openhab.binding.mihome.XiaomiGatewayBindingConstants.*;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * Handles the Xiaomi smart door/window sensor
 *
 * @author Patrick Boos - Initial contribution
 * @author Dieter Schmidt - Refactor
 */
public class XiaomiSensorMagnetHandler extends XiaomiSensorBaseHandlerWithTimer {

    private static final int DEFAULT_TIMER = 300;
    private static final int MIN_TIMER = 30;

    private final Logger logger = LoggerFactory.getLogger(XiaomiSensorMagnetHandler.class);

    public XiaomiSensorMagnetHandler(Thing thing) {
        super(thing, DEFAULT_TIMER, MIN_TIMER, CHANNEL_OPEN_ALARM_TIMER);
    }

    @Override
    void parseReport(JsonObject data) {
        if (data.has("status")) {
            boolean isOpen = "open".equals(data.get("status").getAsString());
            updateState(CHANNEL_IS_OPEN, isOpen ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
            synchronized (this) {
                if (isOpen) {
                    updateState(CHANNEL_LAST_OPENED, new DateTimeType());
                    startTimer();
                } else {
                    cancelRunningTimer();
                }
            }
        }
    }

    @Override
    void execute(ChannelUID channelUID, Command command) {
        if (CHANNEL_OPEN_ALARM_TIMER.equals(channelUID.getId())) {
            if (command != null && command instanceof DecimalType) {
                setTimerFromDecimalType((DecimalType) command);
                return;
            }
            // Only gets here, if no condition was met
            logger.error("Can't handle command {} on channel {}", command, channelUID);
        } else {
            logger.error("Channel {} does not exist", channelUID);
        }
    }

    @Override
    void onTimer() {
        triggerChannel(CHANNEL_OPEN_ALARM, "ALARM");
    }

}
