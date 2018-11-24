/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.paradoxalarm.internal;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.paradoxalarm.internal.communication.EvoCommunicator;
import org.openhab.binding.paradoxalarm.internal.communication.IParadoxCommunicator;
import org.openhab.binding.paradoxalarm.internal.exceptions.ParadoxBindingException;
import org.openhab.binding.paradoxalarm.internal.model.ParadoxPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ParadoxPanelHandler} This is the handler that takes care of communication to/from Paradox alarm system.
 *
 * @author Konstantin_Polihronov - Initial contribution
 */
@NonNullByDefault
public class ParadoxPanelHandler extends BaseThingHandler {

    private static final long INITIAL_DELAY = 0;

    private final Logger logger = LoggerFactory.getLogger(ParadoxPanelHandler.class);

    @Nullable
    ScheduledFuture<?> refreshMemoryMapSchedule;

    @Nullable
    ScheduledFuture<?> reinitializeParadoxPanelSchedule;

    @Nullable
    IParadoxCommunicator communicator;

    @Nullable
    private ParadoxPanelConfiguration config;

    public ParadoxPanelHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (ParadoxAlarmBindingConstants.PANEL_COMMUNICATION_THING_TYPE_UID.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {

            }

            // TODO: handle command

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        }
    }

    @Override
    public void initialize() {
        logger.debug("Start initializing!");
        updateStatus(ThingStatus.UNKNOWN);
        try {
            initializeModel();
            setupSchedule();
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Error initializing panel handler. Exception: " + e);
        }
        logger.debug("Finished initializing!");
    }

    @Override
    public void dispose() {
        if (refreshMemoryMapSchedule != null) {
            boolean cancelingResult = refreshMemoryMapSchedule.cancel(true);
            String cancelingSuccessful = cancelingResult ? "successful" : "failed";
            logger.debug("Canceling schedule of " + refreshMemoryMapSchedule.toString() + " in class "
                    + getClass().getName() + cancelingSuccessful);
        }
        super.dispose();
    }

    private void initializeModel() throws Exception, ParadoxBindingException {
        config = getConfigAs(ParadoxPanelConfiguration.class);

        String ipAddress = config.getIpAddress();
        int port = config.getPort();
        String ip150Password = config.getIp150Password();
        String pcPassword = config.getPcPassword();
        communicator = new EvoCommunicator(ipAddress, port, ip150Password, pcPassword);

        ParadoxPanel panel = ParadoxPanel.getInstance();
        panel.init(communicator);
    }

    private void setupSchedule() {
        logger.debug("Scheduling cache update. Refresh interval: " + config.getRefresh() + "s.");
        refreshMemoryMapSchedule = scheduler.scheduleWithFixedDelay(() -> {
            try {
                communicator.refreshMemoryMap();
            } catch (Exception e) {
                logger.error("Unable to retrieve memory map. {}", e);
            }
        }, INITIAL_DELAY, config.getRefresh(), TimeUnit.SECONDS);

        reinitializeParadoxPanelSchedule = scheduler.scheduleWithFixedDelay(() -> {
            try {
                communicator.refreshMemoryMap();
            } catch (Exception e) {
                logger.error("Unable to retrieve memory map. {}", e);
            }
        }, INITIAL_DELAY, config.getRefresh(), TimeUnit.SECONDS);
    }
}
