/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sleepiq.handler;

import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_ALERT_DETAILED_MESSAGE;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_ALERT_ID;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_IN_BED;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_LAST_LINK;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_PRESSURE;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_LEFT_SLEEP_NUMBER;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_ALERT_DETAILED_MESSAGE;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_ALERT_ID;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_IN_BED;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_LAST_LINK;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_PRESSURE;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.CHANNEL_RIGHT_SLEEP_NUMBER;
import static org.openhab.binding.sleepiq.SleepIQBindingConstants.THING_TYPE_DUAL_BED;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.BridgeHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.sleepiq.config.SleepIQBedConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.syphr.sleepiq.api.SleepIQ;
import org.syphr.sleepiq.api.model.BedSideStatus;
import org.syphr.sleepiq.api.model.BedStatus;

/**
 * The {@link SleepIQDualBedHandler} is responsible for handling channel state updates from the cloud service.
 *
 * @author Gregory Moyer - Initial contribution
 */
public class SleepIQDualBedHandler extends BaseThingHandler implements BedStatusListener {

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPE_UIDS = Collections.singleton(THING_TYPE_DUAL_BED);

    private final Logger logger = LoggerFactory.getLogger(SleepIQDualBedHandler.class);

    private volatile String bedId;

    public SleepIQDualBedHandler(final Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {

        logger.debug("Verifying SleepIQ cloud/bridge configuration");

        Bridge bridge = getBridge();
        if (bridge == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "No cloud service bridge has been configured");
            return;
        }

        if (!ThingStatus.ONLINE.equals(bridge.getStatus())) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
            return;
        }

        ThingHandler handler = bridge.getHandler();
        if (!(handler instanceof SleepIQCloudHandler)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Incorrect bridge thing found");
            return;
        }

        SleepIQCloudHandler cloudHandler = (SleepIQCloudHandler) handler;

        logger.debug("Reading SleepIQ bed binding configuration");
        bedId = getConfigAs(SleepIQBedConfiguration.class).bedId;

        logger.debug("Updating SleepIQ bed properties for bed {}", bedId);
        updateProperties(cloudHandler.updateProperties(cloudHandler.getBed(bedId), editProperties()));
        updateStatus(ThingStatus.ONLINE);

        logger.debug("Registering SleepIQ bed status listener");
        cloudHandler.registerBedStatusListener(this);
    }

    @Override
    public void dispose() {

        logger.debug("Disposing bed handler for bed {}", bedId);

        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler bridgeHandler = bridge.getHandler();
            if (bridgeHandler instanceof SleepIQCloudHandler) {
                ((SleepIQCloudHandler) bridgeHandler).unregisterBedStatusListener(this);
            }
        }

        bedId = null;
    }

    @Override
    public void onBedStateChanged(final SleepIQ cloud, final BedStatus status) {

        if (!status.getBedId().equals(bedId)) {
            return;
        }

        logger.debug("Updating left side status for bed {}", bedId);
        BedSideStatus left = status.getLeftSide();
        updateState(CHANNEL_LEFT_IN_BED, left.isInBed() ? OnOffType.ON : OnOffType.OFF);
        updateState(CHANNEL_LEFT_SLEEP_NUMBER, new DecimalType(left.getSleepNumber()));
        updateState(CHANNEL_LEFT_PRESSURE, new DecimalType(left.getPressure()));
        updateState(CHANNEL_LEFT_LAST_LINK, new StringType(left.getLastLink().toString()));
        updateState(CHANNEL_LEFT_ALERT_ID, new DecimalType(left.getAlertId()));
        updateState(CHANNEL_LEFT_ALERT_DETAILED_MESSAGE, new StringType(left.getAlertDetailedMessage()));

        logger.debug("Updating right side status for bed {}", bedId);
        BedSideStatus right = status.getRightSide();
        updateState(CHANNEL_RIGHT_IN_BED, right.isInBed() ? OnOffType.ON : OnOffType.OFF);
        updateState(CHANNEL_RIGHT_SLEEP_NUMBER, new DecimalType(right.getSleepNumber()));
        updateState(CHANNEL_RIGHT_PRESSURE, new DecimalType(right.getPressure()));
        updateState(CHANNEL_RIGHT_LAST_LINK, new StringType(right.getLastLink().toString()));
        updateState(CHANNEL_RIGHT_ALERT_ID, new DecimalType(right.getAlertId()));
        updateState(CHANNEL_RIGHT_ALERT_DETAILED_MESSAGE, new StringType(right.getAlertDetailedMessage()));
    }

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command command) {
        // all channels are read-only
    }
}
