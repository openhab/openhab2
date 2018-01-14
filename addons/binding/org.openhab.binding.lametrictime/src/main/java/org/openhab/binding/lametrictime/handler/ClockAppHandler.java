/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lametrictime.handler;

import static org.openhab.binding.lametrictime.LaMetricTimeBindingConstants.*;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.lametrictime.handler.model.ParamsSetAlarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syphr.lametrictime.api.common.impl.GsonGenerator;
import org.syphr.lametrictime.api.model.CoreApps;

import com.google.gson.Gson;

/**
 * The {@link ClockAppHandler} represents an instance of the built-in clock app.
 *
 * @author Gregory Moyer - Initial contribution
 */
public class ClockAppHandler extends AbstractLaMetricTimeAppHandler {

    private final Logger logger = LoggerFactory.getLogger(ClockAppHandler.class);

    private final Gson gson = GsonGenerator.create();

    public ClockAppHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleAppCommand(ChannelUID channelUID, Command command) {
        try {
            switch (channelUID.getId()) {
                case CHANNEL_APP_SET_ALARM: {
                    ParamsSetAlarm params = gson.fromJson(command.toString(), ParamsSetAlarm.class);
                    getDevice().doAction(getWidget(),
                            CoreApps.clock().setAlarm(params.enabled, params.time, params.wakeWithRadio));
                    break;
                }
                case CHANNEL_APP_STOP_ALARM:
                    getDevice().doAction(getWidget(), CoreApps.clock().stopAlarm());
                    break;
                default:
                    logger.debug("Channel '{}' not supported", channelUID);
                    break;
            }
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            logger.debug("Failed to perform action - taking app offline", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }
}
