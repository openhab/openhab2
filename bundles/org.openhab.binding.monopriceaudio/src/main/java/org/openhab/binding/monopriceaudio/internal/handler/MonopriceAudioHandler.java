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
package org.openhab.binding.monopriceaudio.internal.handler;

import static org.openhab.binding.monopriceaudio.internal.MonopriceAudioBindingConstants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.StateOption;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.openhab.binding.monopriceaudio.internal.MonopriceAudioException;
import org.openhab.binding.monopriceaudio.internal.MonopriceAudioStateDescriptionOptionProvider;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioCommand;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioConnector;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioIpConnector;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioMessageEvent;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioMessageEventListener;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioSerialConnector;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioZone;
import org.openhab.binding.monopriceaudio.internal.communication.MonopriceAudioZoneData;
import org.openhab.binding.monopriceaudio.internal.configuration.MonopriceAudioThingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MonopriceAudioHandler} is responsible for handling commands, which are sent to one of the channels.
 *
 * Based on the Rotel binding by Laurent Garnier
 *
 * @author Michael Lobstein - Initial contribution
 */
@NonNullByDefault
public class MonopriceAudioHandler extends BaseThingHandler implements MonopriceAudioMessageEventListener {

    private final Logger logger = LoggerFactory.getLogger(MonopriceAudioHandler.class);

    private static final long RECON_POLLING_INTERVAL = TimeUnit.SECONDS.toSeconds(60);
    private long pollingInterval = TimeUnit.SECONDS.toSeconds(15);
    private static final long INITIAL_POLLING_DELAY = TimeUnit.SECONDS.toSeconds(5);
    private static final long SLEEP_BETWEEN_CMD = TimeUnit.MILLISECONDS.toMillis(30);

    private static final String ZONE = "zone";
    private static final String ALL = "all";
    private static final String CHANNEL_DELIMIT = "#";
    private static final String ON = "01";
    private static final String OFF = "00";

    private static final Integer ONE = 1;
    private static final Integer MAX_ZONES = 18;
    private static final Integer MAX_SRC = 6;
    private static final Integer MIN_VOLUME = 0;
    private static final Integer MAX_VOLUME = 38;
    private static final Integer MIN_TONE = -7;
    private static final Integer MAX_TONE = 7;
    private static final Integer MIN_BALANCE = -10;
    private static final Integer MAX_BALANCE = 10;
    private static final Integer BALANCE_OFFSET = 10;
    private static final Integer TONE_OFFSET = 7;

    private @Nullable ScheduledFuture<?> reconnectJob;
    private @Nullable ScheduledFuture<?> pollingJob;

    private MonopriceAudioStateDescriptionOptionProvider stateDescriptionProvider;
    private SerialPortManager serialPortManager;

    private @Nullable MonopriceAudioConnector connector;

    private Integer numZones = 1;
    private ArrayList<String> ignoreZones = new ArrayList<String>();
    private Integer allVolume = 1; 
    private Integer initialAllVolume = 1;

    private static Map<String, MonopriceAudioZoneData> zoneDataMap = new HashMap<>();
    static {
        zoneDataMap.put(MonopriceAudioZone.ZONE1.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE2.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE3.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE4.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE5.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE6.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE7.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE8.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE9.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE10.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE11.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE12.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE13.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE14.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE15.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE16.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE17.getZoneId(), new MonopriceAudioZoneData());
        zoneDataMap.put(MonopriceAudioZone.ZONE18.getZoneId(), new MonopriceAudioZoneData());
    }

    private Long lastPollingUpdate = System.currentTimeMillis();

    private Object sequenceLock = new Object();

    /**
     * Constructor
     */
    public MonopriceAudioHandler(Thing thing, MonopriceAudioStateDescriptionOptionProvider stateDescriptionProvider,
            SerialPortManager serialPortManager) {
        super(thing);
        this.stateDescriptionProvider = stateDescriptionProvider;
        this.serialPortManager = serialPortManager;
    }

    @Override
    public void initialize() {
        logger.debug("Start initializing handler for thing {}", getThing().getUID());

        MonopriceAudioThingConfiguration config = getConfigAs(MonopriceAudioThingConfiguration.class);

        // Check configuration settings
        String configError = null;
        if ((config.serialPort == null || config.serialPort.isEmpty())
                && (config.host == null || config.host.isEmpty())) {
            configError = "undefined serialPort and host configuration settings; please set one of them";
        } else if (config.host == null || config.host.isEmpty()) {
            if (config.serialPort.toLowerCase().startsWith("rfc2217")) {
                configError = "use host and port configuration settings for a serial over IP connection";
            }
        } else {
            if (config.port == null) {
                configError = "undefined port configuration setting";
            } else if (config.port <= 0) {
                configError = "invalid port configuration setting";
            }
        }

        if (configError != null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, configError);
        } else {
            if (config.serialPort != null) {
                connector = new MonopriceAudioSerialConnector(serialPortManager, config.serialPort);
            } else {
                connector = new MonopriceAudioIpConnector(config.host, config.port);
            }

            pollingInterval = TimeUnit.SECONDS.toSeconds(config.pollingInterval);
            numZones = config.numZones;
            initialAllVolume = config.initialAllVolume;

            // If zones were specified to be ignored by the 'all*' commands, use the specified binding
            // zone ids to get the controller's internal zone ids and save those to a list 
            if (config.ignoreZones != null) {
                ArrayList<String> ignore = new ArrayList<String>(Arrays.asList(config.ignoreZones.split(",")));
                ignore.forEach(zone -> {
                    ignoreZones.add(MonopriceAudioZone.bindingZoneMap.get(zone));
                });
            }

            // Build a state option list for the source labels
            List<StateOption> sourcesLabels = new ArrayList<>();
            sourcesLabels.add(new StateOption("1", config.inputLabel1));
            sourcesLabels.add(new StateOption("2", config.inputLabel2));
            sourcesLabels.add(new StateOption("3", config.inputLabel3));
            sourcesLabels.add(new StateOption("4", config.inputLabel4));
            sourcesLabels.add(new StateOption("5", config.inputLabel5));
            sourcesLabels.add(new StateOption("6", config.inputLabel6));

            // Put the source labels on all active zones
            List<Integer> activeZones = 
                    IntStream.range((1), (numZones+1)).boxed().collect(Collectors.toList());
                       
            stateDescriptionProvider.setStateOptions(new ChannelUID(getThing().getUID(), ALL + 
                                                            CHANNEL_DELIMIT + CHANNEL_TYPE_ALLSOURCE), sourcesLabels);
            activeZones.forEach(zoneNum -> {
                stateDescriptionProvider.setStateOptions(new ChannelUID(getThing().getUID(), ZONE + zoneNum.toString() +
                                                            CHANNEL_DELIMIT + CHANNEL_TYPE_SOURCE), sourcesLabels);
            });

            // remove the channels for the zones we are not using
            if (numZones < MAX_ZONES) {
                List<Channel> channels = new ArrayList<>(this.getThing().getChannels());

                List<Integer> zonesToRemove = 
                        IntStream.range((numZones+1), (MAX_ZONES+1)).boxed().collect(Collectors.toList());
                           
                zonesToRemove.forEach(zone -> {
                    if (channels.removeIf(c -> (c.getUID().getId().contains("zone" + zone.toString())))) {
                        logger.debug("Removed channels for zone: {}", zone);
                    } else {
                        logger.debug("Could NOT remove channels for zone: {}", zone);
                    }
                });
                updateThing(editThing().withChannels(channels).build());
            }

            //initialize the all volume state
            allVolume = initialAllVolume;
            long allVolumePct = Math
                    .round((double) (initialAllVolume - MIN_VOLUME) / (double) (MAX_VOLUME - MIN_VOLUME) * 100.0);
            updateState(ALL + CHANNEL_DELIMIT + CHANNEL_TYPE_ALLVOLUME, new PercentType(BigDecimal.valueOf(allVolumePct)));

            updateStatus(ThingStatus.UNKNOWN);

            scheduleReconnectJob();
            schedulePollingJob();
        }

        logger.debug("Finished initializing!");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing handler for thing {}", getThing().getUID());
        cancelReconnectJob();
        cancelPollingJob();
        closeConnection();
        super.dispose();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String channel = channelUID.getId();
        String[] channelSplit = channel.split(CHANNEL_DELIMIT);
        MonopriceAudioZone zone = MonopriceAudioZone.valueOf(channelSplit[0].toUpperCase());
        String channelType = channelSplit[1];

        if (getThing().getStatus() != ThingStatus.ONLINE) {
            logger.debug("Thing is not ONLINE; command {} from channel {} is ignored", command, channel);
            return;
        }

        if (!connector.isConnected()) {
            logger.debug("Command {} from channel {} is ignored: connection not established", command, channel);
            return;
        }

        boolean success = true;
        synchronized (sequenceLock) {
            Stream<String> zoneStream = MonopriceAudioZone.validZones.stream();
            try {
                switch (channelType) {
                    case CHANNEL_TYPE_POWER:
                        if (command instanceof OnOffType && command == OnOffType.ON) {
                            connector.sendCommand(zone, MonopriceAudioCommand.POWER_ON);
                            zoneDataMap.get(zone.getZoneId()).setPower(ON);
                        } else if (command instanceof OnOffType && command == OnOffType.OFF) {
                            connector.sendCommand(zone, MonopriceAudioCommand.POWER_OFF);
                            zoneDataMap.get(zone.getZoneId()).setPower(OFF);
                        }
                        break;
                    case CHANNEL_TYPE_SOURCE:
                        if (command instanceof DecimalType) {
                            Integer value = ((DecimalType) command).intValue();
                            if (value >= ONE && value <= MAX_SRC) {
                                logger.debug("Got source command {} zone {}", value, zone);
                                connector.sendCommand(zone, MonopriceAudioCommand.SOURCE, value);
                                zoneDataMap.get(zone.getZoneId()).setSource(String.format("%02d", value));
                            }
                        }
                        break;
                    case CHANNEL_TYPE_VOLUME:
                        if (command instanceof PercentType) {
                            Integer value = (int) Math.round(((PercentType) command).doubleValue() / 100.0 * (MAX_VOLUME - MIN_VOLUME))
                                    + MIN_VOLUME;
                            logger.debug("Got volume command {} zone {}", value, zone);
                            connector.sendCommand(zone, MonopriceAudioCommand.VOLUME, value);
                            zoneDataMap.get(zone.getZoneId()).setVolume(String.format("%02d", value));
                        }
                        break;
                    case CHANNEL_TYPE_MUTE:
                        if (command instanceof OnOffType && command == OnOffType.ON) {
                            connector.sendCommand(zone, MonopriceAudioCommand.MUTE_ON);
                            zoneDataMap.get(zone.getZoneId()).setMute(ON);
                        } else if (command instanceof OnOffType && command == OnOffType.OFF) {
                            connector.sendCommand(zone, MonopriceAudioCommand.MUTE_OFF);
                            zoneDataMap.get(zone.getZoneId()).setMute(OFF);
                        }
                        break;
                    case CHANNEL_TYPE_TREBLE:
                        if (command instanceof DecimalType) {
                            Integer value = ((DecimalType) command).intValue();
                            if (value >= MIN_TONE && value <= MAX_TONE) {
                                logger.debug("Got treble command {} zone {}", value, zone);
                                connector.sendCommand(zone, MonopriceAudioCommand.TREBLE, value+TONE_OFFSET);
                                zoneDataMap.get(zone.getZoneId()).setTreble(String.format("%02d", value+TONE_OFFSET));
                            }
                        }
                        break;
                    case CHANNEL_TYPE_BASS:
                        if (command instanceof DecimalType) {
                            Integer value = ((DecimalType) command).intValue();
                            if (value >= MIN_TONE && value <= MAX_TONE) {
                                logger.debug("Got bass command {} zone {}", value, zone);
                                connector.sendCommand(zone, MonopriceAudioCommand.BASS, value+TONE_OFFSET);
                                zoneDataMap.get(zone.getZoneId()).setBass(String.format("%02d", value+TONE_OFFSET));
                            }
                        }
                        break;
                    case CHANNEL_TYPE_BALANCE:
                        if (command instanceof DecimalType) {
                            Integer value = ((DecimalType) command).intValue();
                            if (value >= MIN_BALANCE && value <= MAX_BALANCE) {
                                logger.debug("Got balance command {} zone {}", value, zone);
                                connector.sendCommand(zone, MonopriceAudioCommand.BALANCE, value+BALANCE_OFFSET);
                                zoneDataMap.get(zone.getZoneId()).setBalance(String.format("%02d", value+BALANCE_OFFSET));
                            }
                        }
                        break;
                    case CHANNEL_TYPE_DND:
                        if (command instanceof OnOffType && command == OnOffType.ON) {
                            connector.sendCommand(zone, MonopriceAudioCommand.DND_ON);
                            zoneDataMap.get(zone.getZoneId()).setDnd(ON);
                        } else if (command instanceof OnOffType && command == OnOffType.OFF) {
                            connector.sendCommand(zone, MonopriceAudioCommand.DND_OFF);
                            zoneDataMap.get(zone.getZoneId()).setDnd(OFF);
                        }
                        break;
                    case CHANNEL_TYPE_ALLON:
                        zoneStream.limit(numZones).forEach((zoneId) -> {
                            if (!ignoreZones.contains(zoneId)) {
                                try {
                                    connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), MonopriceAudioCommand.POWER_ON);
                                    Thread.sleep(SLEEP_BETWEEN_CMD);
                                    //reset the volume of each zone to allVolume
                                    connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), MonopriceAudioCommand.VOLUME, allVolume);
                                    Thread.sleep(SLEEP_BETWEEN_CMD);
                                } catch (MonopriceAudioException | InterruptedException e) {
                                    logger.debug("Error Turning All Zones On: {}", e.getMessage());
                                }
                            }

                        });
                        break;
                    case CHANNEL_TYPE_ALLOFF:
                        // set allVolume back to initial volume
                        allVolume = initialAllVolume;
                        long allVolumePct = Math
                                .round((double) (allVolume - MIN_VOLUME) / (double) (MAX_VOLUME - MIN_VOLUME) * 100.0);
                        updateState(ALL + CHANNEL_DELIMIT + CHANNEL_TYPE_ALLVOLUME, new PercentType(BigDecimal.valueOf(allVolumePct)));

                        zoneStream.limit(numZones).forEach((zoneId) -> {
                            try {
                                connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), MonopriceAudioCommand.POWER_OFF);
                                Thread.sleep(SLEEP_BETWEEN_CMD);
                            } catch (MonopriceAudioException | InterruptedException e) {
                                logger.debug("Error Turning All Zones Off: {}", e.getMessage());
                            }

                        });
                        break;
                    case CHANNEL_TYPE_ALLSOURCE:
                        if (command instanceof DecimalType) {
                            Integer value = ((DecimalType) command).intValue();
                            if (value >= ONE && value <= MAX_SRC) {
                                zoneStream.limit(numZones).forEach((zoneId) -> {
                                    if (!ignoreZones.contains(zoneId)) {
                                        try {
                                            connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), MonopriceAudioCommand.SOURCE, value);
                                            Thread.sleep(SLEEP_BETWEEN_CMD);
                                        } catch (MonopriceAudioException | InterruptedException e) {
                                            logger.debug("Error Setting Source for  All Zones: {}", e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                        break;
                    case CHANNEL_TYPE_ALLVOLUME:
                        if (command instanceof PercentType) {
                            Integer value = (int) Math.round(((PercentType) command).doubleValue() / 100.0 * (MAX_VOLUME - MIN_VOLUME))
                                    + MIN_VOLUME;
                            allVolume = value;
                            zoneStream.limit(numZones).forEach((zoneId) -> {
                                if (!ignoreZones.contains(zoneId)) {
                                    try {
                                        connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), MonopriceAudioCommand.VOLUME, value);
                                        Thread.sleep(SLEEP_BETWEEN_CMD);
                                    } catch (MonopriceAudioException | InterruptedException e) {
                                        logger.debug("Error Setting Volume for All Zones: {}", e.getMessage());
                                    }
                                }
                            });
                        }
                        break;
                    case CHANNEL_TYPE_ALLMUTE:
                        MonopriceAudioCommand cmd;
                        if (command instanceof OnOffType && command == OnOffType.ON) {
                            cmd = MonopriceAudioCommand.MUTE_ON;
                        } else if  (command instanceof OnOffType && command == OnOffType.OFF) {
                            cmd = MonopriceAudioCommand.MUTE_OFF;
                        } else {
                            cmd = null;
                        }

                        if (cmd != null) {
                            zoneStream.limit(numZones).forEach((zoneId) -> {
                                if (!ignoreZones.contains(zoneId)) {
                                    try {
                                        connector.sendCommand(MonopriceAudioZone.zoneMap.get(zoneId), cmd);
                                        Thread.sleep(SLEEP_BETWEEN_CMD);
                                    } catch (MonopriceAudioException | InterruptedException e) {
                                        logger.debug("Error Setting Mute for All Zones: {}", e.getMessage());
                                    }
                                }
                            });
                        }
                        break;
                    default:
                        success = false;
                        logger.debug("Command {} from channel {} failed: unexpected command", command, channel);
                        break;
                }

                if (success) {
                    logger.debug("Command {} from channel {} succeeded", command, channel);
                }
            } catch (MonopriceAudioException e) {
                logger.warn("Command {} from channel {} failed: {}", command, channel, e.getMessage());
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Sending command failed");
                closeConnection();
                scheduleReconnectJob();
            }
        }
    }

    /**
     * Open the connection with the MonopriceAudio device
     *
     * @return true if the connection is opened successfully or false if not
     */
    private synchronized boolean openConnection() {
        connector.addEventListener(this);
        try {
            connector.open();
        } catch (MonopriceAudioException e) {
            logger.debug("openConnection() failed: {}", e.getMessage());
        }
        logger.debug("openConnection(): {}", connector.isConnected() ? "connected" : "disconnected");
        return connector.isConnected();
    }

    /**
     * Close the connection with the MonopriceAudio device
     */
    private synchronized void closeConnection() {
        if (connector !=null && connector.isConnected()) {
            connector.close();
            connector.removeEventListener(this);
            logger.debug("closeConnection(): disconnected");
        }
    }

    @Override
    public void onNewMessageEvent(EventObject event) {

        MonopriceAudioMessageEvent evt = (MonopriceAudioMessageEvent) event;
        logger.debug("onNewMessageEvent: key {} = {}", evt.getKey(), evt.getValue());

        String key = evt.getKey();
        String updateData = evt.getValue().trim();
        if (!MonopriceAudioConnector.KEY_ERROR.equals(key)) {
            updateStatus(ThingStatus.ONLINE);
        }
        try {
            switch (key) {
                case MonopriceAudioConnector.KEY_ERROR:
                    logger.debug("Reading feedback message failed");
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Reading thread ended");
                    closeConnection();
                    break;


                case MonopriceAudioConnector.KEY_ZONE_UPDATE:
                    String zoneId = updateData.substring(0, 2);

                    if (MonopriceAudioZone.validZones.contains(zoneId)) {
                        MonopriceAudioZone targetZone = MonopriceAudioZone.zoneMap.get(zoneId);
                        processZoneUpdate(targetZone, zoneDataMap.get(zoneId), updateData);
                    } else {
                        logger.warn("invalid event: {} for key: {}", evt.getValue(), key);
                    }
                    break;
                default:
                    logger.debug("onNewMessageEvent: unhandled key {}", key);
                    break;
            }
        } catch (NumberFormatException e) {
            logger.debug("Invalid value {} for key {}", updateData, key);
        }
    }


    /**
     * Schedule the reconnection job
     */
    private void scheduleReconnectJob() {
        logger.debug("Schedule reconnect job");
        cancelReconnectJob();
        reconnectJob = scheduler.scheduleWithFixedDelay(() -> {
            if (!connector.isConnected()) {
                logger.debug("Trying to reconnect...");
                closeConnection();
                String error = null;
                if (openConnection()) {
                    synchronized (sequenceLock) {
                        try {
                            Long prevUpdateTime = lastPollingUpdate;
                            connector.queryZone(MonopriceAudioZone.ZONE1);
                            Thread.sleep(150);

                            // prevUpdateTime should have changed if a zone update was received
                            if (prevUpdateTime.equals(lastPollingUpdate)) {
                                error = "Controller not responding to status requests";
                            }

                        } catch (MonopriceAudioException | InterruptedException e) {
                            error = "First command after connection failed";
                            logger.warn("{}: {}", error, e.getMessage());
                            closeConnection();
                        }
                    }
                } else {
                    error = "Reconnection failed";
                }
                if (error != null) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, error);
                } else {
                    updateStatus(ThingStatus.ONLINE);
                    lastPollingUpdate = System.currentTimeMillis();
                }
            }
        }, 1, RECON_POLLING_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Cancel the reconnection job
     */
    private void cancelReconnectJob() {
        ScheduledFuture<?> reconnectJob = this.reconnectJob;
        if (reconnectJob != null && !reconnectJob.isCancelled()) {
            reconnectJob.cancel(true);
            this.reconnectJob = null;
        }
    }

    /**
     * Schedule the polling job
     */
    private void schedulePollingJob() {
        logger.debug("Schedule polling job");
        cancelPollingJob();

        pollingJob = scheduler.scheduleWithFixedDelay(() -> {
            if (connector.isConnected()) {
                logger.debug("Polling the controller for updated status...");

                synchronized (sequenceLock) {
                    Stream<String> zoneStream = MonopriceAudioZone.validZones.stream();

                    // poll each zone up to the number of zones specified in the configuration
                    zoneStream.limit(numZones).forEach((zoneId) -> {
                        try {
                            connector.queryZone(MonopriceAudioZone.zoneMap.get(zoneId));
                            Thread.sleep(SLEEP_BETWEEN_CMD);
                        } catch (MonopriceAudioException | InterruptedException e) {
                            logger.debug("Polling error: {}", e.getMessage());
                        }
                    });

                    // if the last successful polling update was more than 2.25 intervals ago, the controller
                    // is either switched off or not responding even though the connection is still good
                    if ((System.currentTimeMillis() - lastPollingUpdate) > (pollingInterval * 2.25 * 1000)) {
                        logger.warn("Controller not responding to status requests");
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Controller not responding to status requests");
                        closeConnection();
                        scheduleReconnectJob();
                    } 
                }
            }
        }, INITIAL_POLLING_DELAY, pollingInterval, TimeUnit.SECONDS);
    }

    /**
     * Cancel the polling job
     */
    private void cancelPollingJob() {
        ScheduledFuture<?> pollingJob = this.pollingJob;
        if (pollingJob != null && !pollingJob.isCancelled()) {
            pollingJob.cancel(true);
            this.pollingJob = null;
        }
    }

    /**
     * Update the state of a channel
     *
     * @param channel the channel
     */
    private void updateChannelState(MonopriceAudioZone zone, String channelType, MonopriceAudioZoneData zoneData) {
        String channel = zone.name().toLowerCase() + CHANNEL_DELIMIT + channelType;
        
        if (!isLinked(channel)) {
            return;
        }

        State state = UnDefType.UNDEF;
        switch (channelType) {
            case CHANNEL_TYPE_POWER:
                state = zoneData.isPowerOn() ? OnOffType.ON : OnOffType.OFF;
                break;
            case CHANNEL_TYPE_SOURCE:
                state = new DecimalType(zoneData.getSource());
                break;
            case CHANNEL_TYPE_VOLUME:
                long volumePct = Math
                        .round((double) (Integer.parseInt(zoneData.getVolume()) - MIN_VOLUME) / (double) (MAX_VOLUME - MIN_VOLUME) * 100.0);
                state = new PercentType(BigDecimal.valueOf(volumePct));
                break;
            case CHANNEL_TYPE_MUTE:
                state = zoneData.isMuted() ? OnOffType.ON : OnOffType.OFF;
                break;
            case CHANNEL_TYPE_TREBLE:
                state = new DecimalType(BigDecimal.valueOf(Integer.parseInt(zoneData.getTreble()) - TONE_OFFSET));
                break;
            case CHANNEL_TYPE_BASS:
                state = new DecimalType(BigDecimal.valueOf(Integer.parseInt(zoneData.getBass()) - TONE_OFFSET));
                break;
            case CHANNEL_TYPE_BALANCE:
                state = new DecimalType(BigDecimal.valueOf(Integer.parseInt(zoneData.getBalance()) - BALANCE_OFFSET));
                break;
            case CHANNEL_TYPE_DND:
                state = zoneData.isDndOn() ? OnOffType.ON : OnOffType.OFF;
                break;
            case CHANNEL_TYPE_PAGE:
                state = new DecimalType(zoneData.isPageActive() ? 1 : 0);
                break;
            case CHANNEL_TYPE_KEYPAD:
                state = new DecimalType(zoneData.isKeypadActive() ? 1 : 0);
                break;
            default:
                break;
        }
        updateState(channel, state);
    }

    private void processZoneUpdate(MonopriceAudioZone zone, MonopriceAudioZoneData zoneData, String newZoneData) {
        // only process the update if something actually changed in this zone since the last time through
        if (!newZoneData.equals(zoneData.toString())) {
            //example status string: 1200010000130809100601
            Pattern p=Pattern.compile
                    ("^(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})");
            try {
                Matcher matcher=p.matcher(newZoneData);
                matcher.find();

                zoneData.setZone(matcher.group(1));

                if (!matcher.group(2).equals(zoneData.getPage())) {
                    zoneData.setPage(matcher.group(2));
                    updateChannelState(zone, CHANNEL_TYPE_PAGE, zoneData); 
                }

                if (!matcher.group(3).equals(zoneData.getPower())) {
                    zoneData.setPower(matcher.group(3));
                    updateChannelState(zone, CHANNEL_TYPE_POWER, zoneData); 
                }

                if (!matcher.group(4).equals(zoneData.getMute())) {
                    zoneData.setMute(matcher.group(4));
                    updateChannelState(zone, CHANNEL_TYPE_MUTE, zoneData); 
                }

                if (!matcher.group(5).equals(zoneData.getDnd())) {
                    zoneData.setDnd(matcher.group(5));
                    updateChannelState(zone, CHANNEL_TYPE_DND, zoneData); 
                }

                if (!matcher.group(6).equals(zoneData.getVolume())) {
                    zoneData.setVolume(matcher.group(6));
                    updateChannelState(zone, CHANNEL_TYPE_VOLUME, zoneData); 
                }

                if (!matcher.group(7).equals(zoneData.getTreble())) {
                    zoneData.setTreble(matcher.group(7));
                    updateChannelState(zone, CHANNEL_TYPE_TREBLE, zoneData); 
                }

                if (!matcher.group(8).equals(zoneData.getBass())) {
                    zoneData.setBass(matcher.group(8));
                    updateChannelState(zone, CHANNEL_TYPE_BASS, zoneData); 
                }

                if (!matcher.group(9).equals(zoneData.getBalance())) {
                    zoneData.setBalance(matcher.group(9));
                    updateChannelState(zone, CHANNEL_TYPE_BALANCE, zoneData); 
                }

                if (!matcher.group(10).equals(zoneData.getSource())) {
                    zoneData.setSource(matcher.group(10));
                    updateChannelState(zone, CHANNEL_TYPE_SOURCE, zoneData); 
                }

                if (!matcher.group(11).equals(zoneData.getKeypad())) {
                    zoneData.setKeypad(matcher.group(11));
                    updateChannelState(zone, CHANNEL_TYPE_KEYPAD, zoneData); 
                }
                
            } catch (IllegalStateException e) {
                logger.warn("Invalid zone update message: {}", newZoneData);
            }

        }
        lastPollingUpdate = System.currentTimeMillis();
    }

}
