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
package org.openhab.binding.tivo.handler;

import static org.openhab.binding.tivo.TiVoBindingConstants.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.tivo.internal.service.TivoConfigData;
import org.openhab.binding.tivo.internal.service.TivoStatusData;
import org.openhab.binding.tivo.internal.service.TivoStatusData.ConnectionStatus;
import org.openhab.binding.tivo.internal.service.TivoStatusProvider;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link TiVoHandler} is the BaseThingHandler responsible for handling commands that are
 * sent to one of the Tivo's channels.
 *
 * @author Jayson Kubilis (DigitalBytes) - Initial contribution
 * @author Andrew Black (AndyXMB) - Updates / compilation corrections. Addition of channel scanning functionality.
 * @author Michael Lobstein - Updated for OH3
 */

@NonNullByDefault
public class TiVoHandler extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(TiVoHandler.class);
    private TivoConfigData tivoConfigData = new TivoConfigData();
    private ConnectionStatus lastConnectionStatus = ConnectionStatus.UNKNOWN;
    private Optional<TivoStatusProvider> tivoConnection = Optional.empty();
    private @Nullable ScheduledFuture<?> refreshJob;

    /**
     * Instantiates a new TiVo handler.
     *
     * @param thing the thing
     */
    public TiVoHandler(Thing thing) {
        super(thing);
        logger.debug("TiVoHandler '{}' - creating", getThing().getUID());
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // Handles the commands from the various TiVo channel objects
        logger.debug("handleCommand '{}', parameter: {}", channelUID, command);

        if (!isInitialized() || !tivoConnection.isPresent()) {
            logger.debug("handleCommand '{}' device is not intialised yet, command '{}' will be ignored.",
                    getThing().getUID(), channelUID + " " + command);
            return;
        }

        TivoStatusData currentStatus = tivoConnection.get().getServiceStatus();
        String commandKeyword = "";

        String commandParameter = command.toString().toUpperCase();
        if (command instanceof RefreshType) {
            // Future enhancement, if we can come up with a sensible set of actions when a REFRESH is issued
            logger.debug("TiVo '{}' skipping REFRESH command for channel: '{}'.", getThing().getUID(),
                    channelUID.getId());
            return;
        }

        switch (channelUID.getId()) {
            case CHANNEL_TIVO_CHANNEL_FORCE:
                commandKeyword = "FORCECH";
                break;
            case CHANNEL_TIVO_CHANNEL_SET:
                commandKeyword = "SETCH";
                break;
            case CHANNEL_TIVO_TELEPORT:
                commandKeyword = "TELEPORT";
                break;
            case CHANNEL_TIVO_IRCMD:
                commandKeyword = "IRCODE";
                break;
            case CHANNEL_TIVO_KBDCMD:
                commandKeyword = "KEYBOARD";
                break;
        }
        sendCommand(commandKeyword, commandParameter, currentStatus);
    }

    public void setStatusOffline() {
        this.updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                "Power on device or check network configuration/connection.");
    }

    private void sendCommand(String commandKeyword, String commandParameter, TivoStatusData currentStatus) {
        if (!tivoConnection.isPresent()) {
            return;
        }

        TivoStatusData deviceStatus = tivoConnection.get().getServiceStatus();
        TivoStatusData commandResult = null;
        logger.debug("handleCommand '{}' - {} found!", getThing().getUID(), commandKeyword);
        // Re-write command keyword if we are in STANDBY, as only IRCODE TIVO will wake the unit from
        // standby mode
        if (deviceStatus.getConnectionStatus() == ConnectionStatus.STANDBY && commandKeyword.contentEquals("TELEPORT")
                && commandParameter.contentEquals("TIVO")) {
            commandKeyword = "IRCODE " + commandParameter;
            logger.debug("TiVo '{}' TELEPORT re-mapped to IRCODE as we are in standby: '{}'", getThing().getUID(),
                    commandKeyword);
        }
        // Execute command
        if (commandKeyword.contentEquals("FORCECH") || commandKeyword.contentEquals("SETCH")) {
            commandResult = chChannelChange(commandKeyword, commandParameter);
        } else {
            commandResult = tivoConnection.get().cmdTivoSend(commandKeyword + " " + commandParameter);
        }

        // Post processing
        if (commandResult != null && commandParameter.contentEquals("STANDBY")) {
            // Force thing state into STANDBY as this command does not return a status when executed
            commandResult.setConnectionStatus(ConnectionStatus.STANDBY);
        }

        // Push status updates
        if (commandResult != null && commandResult.isCmdOk()) {
            updateTivoStatus(currentStatus, commandResult);
        }

        // disconnect once command is complete (really only disconnects if isCfgKeepConnOpen = false)
        tivoConnection.get().connTivoDisconnect(false);
    }

    int convertValueToInt(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        return (Integer) value;
    }

    boolean convertValueToBoolean(Object value) {
        return value instanceof Boolean ? ((Boolean) value) : Boolean.valueOf((String) value);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing a TiVo '{}' with config options", getThing().getUID());

        Configuration conf = this.getConfig();

        Object value;
        value = conf.get(CONFIG_ADDRESS);
        if (value != null) {
            tivoConfigData.setCfgHost(String.valueOf(value));
        }

        value = conf.get(CONFIG_PORT);
        if (value != null) {
            tivoConfigData.setCfgTcpPort(convertValueToInt(value));
        }

        value = conf.get(CONFIG_CONNECTION_RETRY);
        if (value != null) {
            tivoConfigData.setCfgNumConnRetry(convertValueToInt(value));
        }

        value = conf.get(CONFIG_POLL_INTERVAL);
        if (value != null) {
            tivoConfigData.setCfgPollInterval(convertValueToInt(value));
        }

        value = conf.get(CONFIG_POLL_FOR_CHANGES);
        if (value != null) {
            tivoConfigData.setCfgPollChanges(convertValueToBoolean(value));
        }

        value = conf.get(CONFIG_KEEP_CONNECTION_OPEN);
        if (value != null) {
            tivoConfigData.setCfgKeepConnOpen(convertValueToBoolean(value));
        }

        value = conf.get(CONFIG_CMD_WAIT_INTERVAL);
        if (value != null) {
            tivoConfigData.setCfgCmdWait(convertValueToInt(value));
        }

        tivoConfigData.setCfgIdentifier(String.valueOf(getThing().getUID()));
        logger.debug("TivoConfigData Obj: '{}'", tivoConfigData);
        tivoConnection = Optional.of(new TivoStatusProvider(tivoConfigData, this));

        updateStatus(ThingStatus.UNKNOWN);
        lastConnectionStatus = ConnectionStatus.UNKNOWN;
        logger.debug("Initializing a TiVo handler for thing '{}' - finished!", getThing().getUID());

        startPollStatus();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing of a TiVo handler for thing '{}'", getThing().getUID());

        ScheduledFuture<?> refreshJob = this.refreshJob;
        if (refreshJob != null) {
            logger.debug("'{}' - Polling cancelled by dispose()", getThing().getUID());
            refreshJob.cancel(false);
            this.refreshJob = null;
        }

        if (tivoConnection.isPresent()) {
            tivoConnection.get().connTivoDisconnect(true);
            tivoConnection = Optional.empty();
        }
    }

    /**
     * {@link startPollStatus} scheduled job to poll for changes in state.
     */
    private void startPollStatus() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug("startPollStatus '{}' @ rate of '{}' seconds", getThing().getUID(),
                        tivoConfigData.getCfgPollInterval());
                if (tivoConnection.isPresent())
                    tivoConnection.get().statusRefresh();
            }
        };

        if (tivoConfigData.isCfgKeepConnOpen()) {
            // Run once
            refreshJob = scheduler.schedule(runnable, INIT_POLLING_DELAY_S, TimeUnit.SECONDS);
            logger.debug("Status collection '{}' will start in '{}' seconds.", getThing().getUID(),
                    INIT_POLLING_DELAY_S);
        } else if (tivoConfigData.doPollChanges()) {
            // Run at intervals
            refreshJob = scheduler.scheduleWithFixedDelay(runnable, INIT_POLLING_DELAY_S,
                    tivoConfigData.getCfgPollInterval(), TimeUnit.SECONDS);
            logger.debug("Status polling '{}' will start in '{}' seconds.", getThing().getUID(), INIT_POLLING_DELAY_S);
        } else {
            // Just update the status now
            if (tivoConnection.isPresent())
                tivoConnection.get().statusRefresh();
        }
    }

    /**
     * {@link chChannelChange} performs channel changing operations.
     *
     * @param commandKeyword the TiVo command object.
     * @param command the command parameter.
     * @return TivoStatusData status of the command.
     */
    private TivoStatusData chChannelChange(String commandKeyword, String command) {
        int channel = -1;
        int subChannel = -1;

        TivoStatusData tmpStatus = tivoConnection.get().getServiceStatus();
        try {
            // If desired channel has decimal (OTA channels), parse the sub channel number
            if (command.contains(".")) {
                String[] channelArray = command.split("\\.");
                channel = Integer.valueOf(channelArray[0]).intValue();
                subChannel = Integer.valueOf(channelArray[1]).intValue();
            } else {
                channel = Integer.valueOf(command.toString()).intValue();
            }

            String tmpCommand = commandKeyword + " " + channel + ((subChannel != -1) ? (" " + subChannel) : "");
            logger.debug("chChannelChange '{}' sending command to tivo: '{}'", getThing().getUID(), tmpCommand);

            // Attempt to execute the command on the tivo
            tivoConnection.get().cmdTivoSend(tmpCommand);
            try {
                TimeUnit.MILLISECONDS.sleep(tivoConfigData.getCfgCmdWait() * 2);
            } catch (Exception e) {
            }

            tmpStatus = tivoConnection.get().getServiceStatus();

            // Check to see if the command was successful
            if (tmpStatus.getConnectionStatus() != ConnectionStatus.INIT && tmpStatus.isCmdOk()) {
                if (tmpStatus.getMsg().contains("CH_STATUS")) {
                    return tmpStatus;
                }
            } else if (tmpStatus.getConnectionStatus() != ConnectionStatus.INIT) {
                logger.warn("TiVo'{}' set channel command failed '{}' with msg '{}'", getThing().getUID(), tmpCommand,
                        tmpStatus.getMsg());
                switch (tmpStatus.getMsg()) {
                    case "CH_FAILED NO_LIVE":
                        tmpStatus.setChannelNum(channel);
                        tmpStatus.setSubChannelNum(subChannel);
                        return tmpStatus;
                    case "CH_FAILED RECORDING":
                        return tmpStatus;
                    case "CH_FAILED MISSING_CHANNEL":
                        return tmpStatus;
                    case "CH_FAILED MALFORMED_CHANNEL":
                        return tmpStatus;
                    case "CH_FAILED INVALID_CHANNEL":
                        return tmpStatus;
                    case "NO_STATUS_DATA_RETURNED":
                        tmpStatus.setChannelNum(-1);
                        tmpStatus.setSubChannelNum(-1);
                        tmpStatus.setRecording(false);
                        return tmpStatus;
                }
            }

        } catch (NumberFormatException e) {
            logger.warn("TiVo'{}' unable to parse channel integer from CHANNEL_TIVO_CHANNEL: '{}'", getThing().getUID(),
                    command.toString());
        }
        return tmpStatus;
    }

    /**
     * {@link updateTivoStatus} populates the items with the status / channel information.
     *
     * @param tivoStatusData the {@link TivoStatusData}
     */
    public void updateTivoStatus(TivoStatusData oldStatusData, TivoStatusData newStatusData) {
        if (newStatusData.getConnectionStatus() != ConnectionStatus.INIT) {
            // Update Item Status
            if (newStatusData.getPubToUI()) {
                if (oldStatusData.getConnectionStatus() == ConnectionStatus.INIT
                        || !(oldStatusData.getMsg().contentEquals(newStatusData.getMsg()))) {
                    updateState(CHANNEL_TIVO_STATUS, new StringType(newStatusData.getMsg()));
                }
                // If the cmd was successful, publish the channel channel numbers
                if (newStatusData.isCmdOk() && newStatusData.getChannelNum() != -1) {
                    if (oldStatusData.getConnectionStatus() == ConnectionStatus.INIT
                            || oldStatusData.getChannelNum() != newStatusData.getChannelNum()
                            || oldStatusData.getSubChannelNum() != newStatusData.getSubChannelNum()) {
                        if (newStatusData.getSubChannelNum() == -1) {
                            updateState(CHANNEL_TIVO_CHANNEL_FORCE, new DecimalType(newStatusData.getChannelNum()));
                            updateState(CHANNEL_TIVO_CHANNEL_SET, new DecimalType(newStatusData.getChannelNum()));
                        } else {
                            updateState(CHANNEL_TIVO_CHANNEL_FORCE, new DecimalType(
                                    newStatusData.getChannelNum() + "." + newStatusData.getSubChannelNum()));
                            updateState(CHANNEL_TIVO_CHANNEL_SET, new DecimalType(
                                    newStatusData.getChannelNum() + "." + newStatusData.getSubChannelNum()));
                        }
                    }
                    updateState(CHANNEL_TIVO_IS_RECORDING, newStatusData.isRecording() ? OnOffType.ON : OnOffType.OFF);
                }

                // Now set the pubToUI flag to false, as we have already published this status
                if (isLinked(CHANNEL_TIVO_STATUS) || isLinked(CHANNEL_TIVO_CHANNEL_FORCE)
                        || isLinked(CHANNEL_TIVO_CHANNEL_SET)) {
                    newStatusData.setPubToUI(false);
                    tivoConnection.get().setServiceStatus(newStatusData);
                }
            }

            // Update Thing status
            if (newStatusData.getConnectionStatus() != lastConnectionStatus) {
                switch (newStatusData.getConnectionStatus()) {
                    case OFFLINE:
                        this.setStatusOffline();
                        break;
                    case ONLINE:
                        updateStatus(ThingStatus.ONLINE);
                        break;
                    case STANDBY:
                        updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE,
                                "STANDBY MODE: Send command TIVO to Remote Control Button (IRCODE) item to wakeup.");
                        break;
                    case UNKNOWN:
                        updateStatus(ThingStatus.INITIALIZING);
                        break;
                    case INIT:
                        break;
                }
                lastConnectionStatus = newStatusData.getConnectionStatus();
            }
        }
    }
}
