/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.loxone.internal.controls;

import static org.openhab.binding.loxone.internal.LxBindingConstants.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.StateDescription;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.loxone.internal.LxServerHandler;
import org.openhab.binding.loxone.internal.types.LxCategory;
import org.openhab.binding.loxone.internal.types.LxContainer;
import org.openhab.binding.loxone.internal.types.LxUuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An UpDownAnalog type of control on Loxone Miniserver.
 * <p>
 * According to Loxone API documentation, UpDownAnalog control is a virtual input that is analog and has an input type
 * up-down buttons. The analog buttons are simulated as a single analog number value.
 *
 * @author Pawel Pieczul - initial contribution
 *
 */
public class LxControlUpDownAnalog extends LxControl {

    static class Factory extends LxControlInstance {
        @Override
        LxControl create(LxUuid uuid) {
            return new LxControlUpDownAnalog(uuid);
        }

        @Override
        String getType() {
            return TYPE_NAME;
        }
    }

    static final String TYPE_NAME = "updownanalog";

    private static final String STATE_VALUE = "value";
    private static final String STATE_ERROR = "error";

    private final Logger logger = LoggerFactory.getLogger(LxControlUpDownAnalog.class);

    private Double minValue;
    private Double maxValue;
    private ChannelUID channelId;

    LxControlUpDownAnalog(LxUuid uuid) {
        super(uuid);
    }

    @Override
    public void initialize(LxServerHandler thingHandler, LxContainer room, LxCategory category) {
        initialize(thingHandler, room, category, "Up/Down Analog");
    }

    void initialize(LxServerHandler thingHandler, LxContainer room, LxCategory category, String channelDescription) {
        super.initialize(thingHandler, room, category);
        channelId = addChannel("Number", new ChannelTypeUID(BINDING_ID, MINISERVER_CHANNEL_TYPE_NUMBER),
                defaultChannelLabel, channelDescription, tags, this::handleCommands, this::getChannelState);
        if (details != null && details.min != null && details.max != null) {
            if (details.min <= details.max) {
                minValue = details.min;
                maxValue = details.max;
                if (details.step != null) {
                    addChannelStateDescription(channelId,
                            new StateDescription(new BigDecimal(minValue), new BigDecimal(maxValue),
                                    new BigDecimal(details.step), details.format != null ? details.format : "%.1f",
                                    false, null));
                }
            } else {
                logger.warn("Received min value > max value: {}, {}", minValue, maxValue);
            }
        }
    }

    private void handleCommands(Command command) throws IOException {
        if (command instanceof DecimalType) {
            Double value = ((DecimalType) command).doubleValue();
            if (minValue != null && maxValue != null && value >= minValue && value <= maxValue) {
                sendAction(value.toString());
            } else {
                // we'll update the state value to reflect current real value that has not been changed
                thingHandler.setChannelState(channelId, getChannelState());
            }
        }
    }

    private State getChannelState() {
        Double error = getStateDoubleValue(STATE_ERROR);
        if (error == null || error == 0.0) {
            Double value = getStateDoubleValue(STATE_VALUE);
            if (value != null) {
                return new DecimalType(value);
            }
        } else {
            return UnDefType.UNDEF;
        }
        return null;
    }
}
