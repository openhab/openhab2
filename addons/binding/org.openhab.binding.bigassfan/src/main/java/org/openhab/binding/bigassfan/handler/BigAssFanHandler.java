/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bigassfan.handler;

import static org.openhab.binding.bigassfan.BigAssFanBindingConstants.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.net.NetUtil;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.bigassfan.internal.BigAssFanConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BigAssFanHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Mark Hilbush - Initial contribution
 */
public class BigAssFanHandler extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(BigAssFanHandler.class);

    private BigAssFanConfig config;
    private String label = null;
    private String ipAddress = null;
    private String macAddress = null;

    private FanListener fanListener;

    protected Map<String, State> fanStateMap = Collections.synchronizedMap(new HashMap<String, State>());

    private final StringType OFF = new StringType("OFF");
    private final StringType COOLING = new StringType("COOLING");
    private final StringType HEATING = new StringType("HEATING");

    public BigAssFanHandler(Thing thing) {
        super(thing);
        this.thing = thing;

        logger.debug("Creating FanListener object for thing {}", thingID());
        fanListener = new FanListener();
    }

    @Override
    public void initialize() {
        logger.debug("BigAssFan initializing handler for thing {}", thingID());

        config = getConfig().as(BigAssFanConfig.class);
        logger.debug("BigAssFanHandler config is {}", config);

        if (!config.isValid()) {
            logger.debug("BigAssFanHandler config is invalid. Check configuration of thing {}", thingID());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Invalid BigAssFan config. Check configuration.");
            return;
        }
        label = config.getLabel();
        ipAddress = config.getIpAddress();
        macAddress = config.getMacAddress();

        fanListener.startFanListener();
    }

    @Override
    public void dispose() {
        logger.debug("BigAssFan disposing handler for thing {}", thingID());
        fanListener.stopFanListener();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handle fan command {} on channel {}", command.toString(), channelUID.toString());

        if (command instanceof RefreshType) {
            handleRefresh(channelUID);
            return;
        }

        if (channelUID.getId().equals(CHANNEL_FAN_POWER)) {
            handleFanPower(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_SPEED)) {
            handleFanSpeed(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_DIRECTION)) {
            handleFanDirection(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_AUTO)) {
            handleFanAuto(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_WHOOSH)) {
            handleFanWhoosh(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_SMARTMODE)) {
            handleFanSmartmode(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_SPEED_MIN)) {
            handleFanSpeedMin(command);
        } else if (channelUID.getId().equals(CHANNEL_FAN_SPEED_MAX)) {
            handleFanSpeedMax(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_POWER)) {
            handleLightPower(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_LEVEL)) {
            handleLightLevel(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_AUTO)) {
            handleLightAuto(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_SMARTER)) {
            handleLightSmarter(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_LEVEL_MIN)) {
            handleLightLevelMin(command);
        } else if (channelUID.getId().equals(CHANNEL_LIGHT_LEVEL_MAX)) {
            handleLightLevelMax(command);
        } else {
            logger.debug("Received command on unknown channel {}", channelUID.getId());
        }
    }

    private void handleRefresh(ChannelUID channelUID) {
        if (channelUID.getId().equals(CHANNEL_FAN_SPEED)) {
            logger.debug("Handling REFRESH of fan thing {}", thingID());
            sendCommand(macAddress, ";GETALL");
            sendCommand(macAddress, ";SNSROCC;STATUS;GET");
        }
    }

    private void handleFanPower(Command command) {
        logger.debug("Handling fan power command {}", command.toString());

        // <mac;FAN;PWR;ON|OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";FAN;PWR;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";FAN;PWR;ON");
            }
        }
    }

    private void handleFanSpeed(Command command) {
        logger.debug("Handling fan speed command {}", command.toString());

        // <mac;FAN;SPD;SET;0..7>
        if (command instanceof PercentType) {
            sendCommand(macAddress, ";FAN;SPD;SET;".concat(convertPercentToSpeed((PercentType) command)));
        }
    }

    private void handleFanDirection(Command command) {
        logger.debug("Handling fan direction command {}", command.toString());

        // <mac;FAN;DIR;FWD|REV>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";FAN;DIR;FWD");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";FAN;DIR;REV");
            }
        }
    }

    private void handleFanAuto(Command command) {
        logger.debug("Handling fan auto command {}", command.toString());

        // <mac;FAN;AUTO;ON|OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";FAN;AUTO;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";FAN;AUTO;ON");
            }
        }
    }

    private void handleFanWhoosh(Command command) {
        logger.debug("Handling fan whoosh command {}", command.toString());

        // <mac;FAN;WHOOSH;ON|OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";FAN;WHOOSH;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";FAN;WHOOSH;ON");
            }
        }
    }

    private void handleFanSmartmode(Command command) {
        logger.debug("Handling smartmode command {}", command.toString());

        // <mac;SMARTMODE;SET;OFF/COOLING/HEATING>
        if (command instanceof StringType) {
            if (command.equals(OFF)) {
                sendCommand(macAddress, ";SMARTMODE;STATE;SET;OFF");
            } else if (command.equals(COOLING)) {
                sendCommand(macAddress, ";SMARTMODE;STATE;SET;COOLING");
            } else if (command.equals(HEATING)) {
                sendCommand(macAddress, ";SMARTMODE;STATE;SET;HEATING");
            } else {
                logger.debug("Unknown Smartmode command: {}", command.toString());
            }
        }
    }

    private void handleFanSpeedMin(Command command) {
        logger.debug("Handling fan speed minimum command {}", command.toString());
        // Add sample command format
        if (command instanceof PercentType) {
            // Send min speed set command
            sendCommand(macAddress, ";LEARN;MINSPEED;SET;".concat(convertPercentToSpeed((PercentType) command)));
            // Don't let max be less than min
            adjustMaxSpeed((PercentType) command);
        }
    }

    private void handleFanSpeedMax(Command command) {
        logger.debug("Handling fan speed maximum command {}", command.toString());
        // Add sample command format
        if (command instanceof PercentType) {
            // Send max speed set command
            sendCommand(macAddress, ";LEARN;MAXSPEED;SET;".concat(convertPercentToSpeed((PercentType) command)));
            // Don't let min be greater than max
            adjustMinSpeed((PercentType) command);
        }
    }

    private void adjustMaxSpeed(PercentType command) {
        int newMin = command.intValue();
        int currentMax = PercentType.ZERO.intValue();
        if (fanStateMap.get(CHANNEL_FAN_SPEED_MAX) != null) {
            currentMax = ((PercentType) fanStateMap.get(CHANNEL_FAN_SPEED_MAX)).intValue();
        }
        if (newMin > currentMax) {
            updateState(CHANNEL_FAN_SPEED_MAX, command);
            sendCommand(macAddress, ";FAN;SPD;MAX;".concat(convertPercentToSpeed(command)));
        }
    }

    private void adjustMinSpeed(PercentType command) {
        int newMax = command.intValue();
        int currentMin = PercentType.HUNDRED.intValue();
        if (fanStateMap.get(CHANNEL_FAN_SPEED_MIN) != null) {
            currentMin = ((PercentType) fanStateMap.get(CHANNEL_FAN_SPEED_MIN)).intValue();
        }
        if (newMax < currentMin) {
            updateState(CHANNEL_FAN_SPEED_MIN, command);
            sendCommand(macAddress, ";FAN;SPD;MIN;".concat(convertPercentToSpeed(command)));
        }
    }

    private void handleLightPower(Command command) {
        logger.debug("Handling light power command {}", command.toString());
        // <mac;LIGHT;PWR;ON|OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";LIGHT;PWR;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";LIGHT;PWR;ON");
            }
        }
    }

    private void handleLightLevel(Command command) {
        logger.debug("Handling light level command {}", command.toString());
        // <mac;LIGHT;LEVEL;SET;0..16>
        if (command instanceof PercentType) {
            sendCommand(macAddress, ";LIGHT;LEVEL;SET;".concat(convertPercentToLevel((PercentType) command)));
        }
    }

    private void handleLightAuto(Command command) {
        logger.debug("Handling light auto command {}", command.toString());
        // <mac;LIGHT;AUTO;ON|OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";LIGHT;AUTO;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";LIGHT;AUTO;ON");
            }
        }
    }

    private void handleLightSmarter(Command command) {
        logger.debug("Handling smartmode command {}", command.toString());
        // Add sample command format <mac;;;ON/OFF>
        if (command instanceof OnOffType) {
            if (command.equals(OnOffType.OFF)) {
                sendCommand(macAddress, ";LIGHT;SMART;OFF");
            } else if (command.equals(OnOffType.ON)) {
                sendCommand(macAddress, ";LIGHT;SMART;ON");
            }
        }
    }

    private void handleLightLevelMin(Command command) {
        logger.debug("Handling light level minimum command {}", command.toString());
        // <mac;LIGHT;LEVEL;MIN;0-16>
        if (command instanceof PercentType) {
            // Send min light level set command
            sendCommand(macAddress, ";LIGHT;LEVEL;MIN;".concat(convertPercentToLevel((PercentType) command)));
            // Don't let max be less than min
            adjustMaxLevel((PercentType) command);
        }
    }

    private void handleLightLevelMax(Command command) {
        logger.debug("Handling light level maximum command {}", command.toString());
        // <mac;LIGHT;LEVEL;MAX;0-16>
        if (command instanceof PercentType) {
            // Send max light level set command
            sendCommand(macAddress, ";LIGHT;LEVEL;MAX;".concat(convertPercentToLevel((PercentType) command)));
            // Don't let min be greater than max
            adjustMinLevel((PercentType) command);
        }
    }

    private void adjustMaxLevel(PercentType command) {
        int newMin = command.intValue();
        int currentMax = PercentType.ZERO.intValue();
        if (fanStateMap.get(CHANNEL_LIGHT_LEVEL_MAX) != null) {
            currentMax = ((PercentType) fanStateMap.get(CHANNEL_LIGHT_LEVEL_MAX)).intValue();
        }
        if (newMin > currentMax) {
            updateState(CHANNEL_LIGHT_LEVEL_MAX, command);
            sendCommand(macAddress, ";LIGHT;LEVEL;MAX;".concat(convertPercentToLevel(command)));
        }
    }

    private void adjustMinLevel(PercentType command) {
        int newMax = command.intValue();
        int currentMin = PercentType.HUNDRED.intValue();
        if (fanStateMap.get(CHANNEL_LIGHT_LEVEL_MIN) != null) {
            currentMin = ((PercentType) fanStateMap.get(CHANNEL_LIGHT_LEVEL_MIN)).intValue();
        }
        if (newMax < currentMin) {
            updateState(CHANNEL_LIGHT_LEVEL_MIN, command);
            sendCommand(macAddress, ";LIGHT;LEVEL;MIN;".concat(convertPercentToLevel(command)));
        }
    }

    /*
     * Convert from fan range (0-7) and light range (0-16) to dimmer range (0-100).
     */
    private static final double SPEED_CONVERSION_FACTOR = 14.2857;
    private static final double BRIGHTNESS_CONVERSION_FACTOR = 6.25;

    private String convertPercentToSpeed(PercentType command) {
        // Dimmer item will produce PercentType value, which is 0-100
        // Convert that value to what the fan expects, which is 0-7
        return String.valueOf((int) Math.round(command.doubleValue() / SPEED_CONVERSION_FACTOR));
    }

    private PercentType convertSpeedToPercent(String speed) {
        // Fan will supply fan speed value in range of 0-7
        // Convert that value to a PercentType in range 0-100, which is what Dimmer item expects
        return new PercentType((int) Math.round(Integer.parseInt(speed) * SPEED_CONVERSION_FACTOR));
    }

    private String convertPercentToLevel(PercentType command) {
        // Dimmer item will produce PercentType value, which is 0-100
        // Convert that value to what the light expects, which is 0-16
        return String.valueOf((int) Math.round(command.doubleValue() / BRIGHTNESS_CONVERSION_FACTOR));
    }

    private PercentType convertLevelToPercent(String level) {
        // Light will supply brightness value in range of 0-16
        // Convert that value to a PercentType in range 0-100, which is what Dimmer item expects
        return new PercentType((int) Math.round(Integer.parseInt(level) * BRIGHTNESS_CONVERSION_FACTOR));
    }

    /*
     * Send a command to the fan
     */
    private void sendCommand(String mac, String commandFragment) {
        if (fanListener == null) {
            logger.error("Unable to send message because fanListener is null!!!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<").append(mac).append(commandFragment).append(">");
        String message = sb.toString();
        logger.debug("Sending message to fan at {}: {}", ipAddress, message);
        fanListener.send(message);
    }

    private void updateChannel(String channelName, State state) {
        Channel channel = thing.getChannel(channelName);
        if (channel != null) {
            updateState(channel.getUID(), state);
        }
    }

    private String thingID() {
        return thing.getUID().getId();
    }

    /*
     * Manage the ONLINE/OFFLINE status of the thing
     */
    private void markOnline() {
        if (!isOnline()) {
            logger.debug("Changing status of {} from {}({}) to ONLINE", thingID(), getStatus(), getDetail());
            updateStatus(ThingStatus.ONLINE);
        }
    }

    private void markOffline() {
        if (isOnline()) {
            logger.debug("Changing status of {} from {}({}) to OFFLINE", thingID(), getStatus(), getDetail());
            updateStatus(ThingStatus.OFFLINE);
        }
    }

    private void markOfflineWithMessage(ThingStatusDetail statusDetail, String statusMessage) {
        // If it's offline with no detail or if it's not offline, mark it offline with detailed status
        if ((isOffline() && getDetail() == ThingStatusDetail.NONE) || !isOffline()) {
            logger.debug("Changing status of {} from {}({}) to OFFLINE({})", thingID(), getStatus(), getDetail(),
                    statusDetail);
            updateStatus(ThingStatus.OFFLINE, statusDetail, statusMessage);
            return;
        }
    }

    private boolean isOnline() {
        return thing.getStatus().equals(ThingStatus.ONLINE);
    }

    private boolean isOffline() {
        return thing.getStatus().equals(ThingStatus.OFFLINE);
    }

    private ThingStatus getStatus() {
        return thing.getStatus();
    }

    private ThingStatusDetail getDetail() {
        return thing.getStatusInfo().getStatusDetail();
    }

    /**
     * The {@link FanListener} is responsible for sending and receiving messages to a fan.
     *
     * @author Mark Hilbush - Initial contribution
     */
    public class FanListener {
        private final Logger logger = LoggerFactory.getLogger(FanListener.class);

        // Our own thread pool for the long-running listener job
        private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        private ScheduledFuture<?> listenerJob;

        private final long FAN_LISTENER_DELAY = 2L;
        private boolean terminate;

        private ConnectionManager conn;

        private Runnable fanListenerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    listener();
                } catch (RuntimeException e) {
                    logger.warn("FanListener had unhandled exception: ", e.getMessage(), e);
                }
            }
        };

        public FanListener() {
            conn = new ConnectionManager();
        }

        public void startFanListener() {
            conn.connect();
            conn.scheduleConnectionMonitorJob();

            if (listenerJob == null) {
                terminate = false;
                logger.debug("Starting fan listener in {} sec for {} at {}", FAN_LISTENER_DELAY, thingID(), ipAddress);
                listenerJob = scheduledExecutorService.schedule(fanListenerRunnable, FAN_LISTENER_DELAY,
                        TimeUnit.SECONDS);
            }
        }

        public void stopFanListener() {
            if (listenerJob != null) {
                logger.debug("Stopping fan listener for {} at {}", FAN_LISTENER_DELAY, thingID(), ipAddress);
                terminate = true;
                listenerJob.cancel(true);
                listenerJob = null;
            }
            conn.cancelConnectionMonitorJob();
            conn.disconnect();
        }

        public void send(String command) {
            if (!conn.isConnected()) {
                logger.debug("Unable to send message; no connection to fan. Trying to reconnect: {}", command);
                conn.connect();
                if (!conn.isConnected()) {
                    return;
                }
            }

            logger.debug("Sending command to fan: {}", command);
            byte[] buffer;
            try {
                buffer = command.getBytes(CHARSET);
            } catch (UnsupportedEncodingException e) {
                logger.warn("Unable to convert to string using {} charset: {}", CHARSET, e.getMessage(), e);
                return;
            }
            try {
                conn.write(buffer);
            } catch (IOException e) {
                logger.warn("IO exception writing message to socket: {}", e.getMessage(), e);
                conn.disconnect();
            }
        }

        private void listener() {
            logger.debug("Fan listener thread is running");

            while (!terminate) {
                try {
                    // Wait for a message
                    processMessage(waitForMessage());
                } catch (IOException ioe) {
                    logger.warn("Listener got IO exception waiting for message: {}", ioe.getMessage(), ioe);
                    break;
                }
            }
            logger.debug("Fan listener thread is exiting");
        }

        private String waitForMessage() throws IOException {
            if (!conn.isConnected()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("FanListener can't receive message. No connection to fan");
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
                return null;
            }
            return readMessage();
        }

        private String readMessage() {
            logger.debug("Waiting for message from fan at {}", ipAddress);
            String message = conn.read();
            logger.debug("FanListener received message of length {}: {}", message.length(), message);
            return message;
        }

        private void processMessage(String message) {
            if (StringUtils.isEmpty(message)) {
                return;
            }

            // Match on (msg)
            logger.debug("FanListener processing received message {}", message);
            Pattern pattern = Pattern.compile("[(](.*)");
            Matcher matcher = pattern.matcher(message);
            if (!matcher.find()) {
                logger.debug("Unable to process message, doesn't match expected format: {}", message);
                return;
            }

            message = matcher.group(1);
            String[] messageParts = message.split(";");

            // Check to make sure it is my MAC address or my label
            if (!isMe(messageParts[0])) {
                logger.trace("Message not for me: {} - {}", messageParts[0], macAddress);
                return;
            }

            logger.debug("Message is for me!!!  Process the message: {}", message);
            String messageUpperCase = message.toUpperCase();
            if (messageUpperCase.contains(";FAN;PWR;")) {
                updateFanPower(messageParts);

            } else if (messageUpperCase.contains(";FAN;SPD;ACTUAL;")) {
                updateFanSpeed(messageParts);

            } else if (messageUpperCase.contains(";FAN;DIR;")) {
                updateFanDirection(messageParts);

            } else if (messageUpperCase.contains(";FAN;AUTO;")) {
                updateFanAuto(messageParts);

            } else if (messageUpperCase.contains(";FAN;WHOOSH;STATUS;")) {
                updateFanWhoosh(messageParts);

            } else if (messageUpperCase.contains(";SMARTMODE;ACTUAL;")) {
                updateFanSmartmode(messageParts);

            } else if (messageUpperCase.contains(";FAN;SPD;MIN;")) {
                updateFanSpeedMin(messageParts);

            } else if (messageUpperCase.contains(";FAN;SPD;MAX;")) {
                updateFanSpeedMax(messageParts);

            } else if (messageUpperCase.contains(";LIGHT;PWR;")) {
                updateLightPower(messageParts);

            } else if (messageUpperCase.contains(";LIGHT;LEVEL;ACTUAL;")) {
                updateLightLevel(messageParts);

            } else if (messageUpperCase.contains(";LIGHT;AUTO;")) {
                updateLightAuto(messageParts);

            } else if (messageUpperCase.contains(";LIGHT;LEVEL;MIN;")) {
                updateLightLevelMin(messageParts);

            } else if (messageUpperCase.contains(";LIGHT;LEVEL;MAX;")) {
                updateLightLevelMax(messageParts);

            } else if (messageUpperCase.contains(";SNSROCC;STATUS;")) {
                updateMotion(messageParts);

            } else if (messageUpperCase.contains(";TIME;VALUE;")) {
                updateTime(messageParts);

            } else {
                logger.debug("Received unsupported message from fan: {}", message);
            }
        }

        private boolean isMe(String idFromDevice) {
            // Check match on MAC address
            if (StringUtils.equalsIgnoreCase(idFromDevice, macAddress)) {
                return true;
            }
            // Didn't match MAC address, check match for label
            if (StringUtils.equalsIgnoreCase(idFromDevice, label)) {
                return true;
            }
            return false;
        }

        private void updateFanPower(String[] messageParts) {
            logger.debug("Process fan power update (ON/OFF)");
            if (messageParts.length != 4) {
                if (logger.isDebugEnabled()) {
                    logger.debug("FAN;PWR has unexpected number of parameters: {}", Arrays.toString(messageParts));
                }
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_FAN_POWER, state);
            fanStateMap.put(CHANNEL_FAN_POWER, state);
        }

        private void updateFanSpeed(String[] messageParts) {
            logger.debug("Process fan speed update (0..7)");
            if (messageParts.length != 5) {
                logger.debug("FAN;SPD;ACTUAL has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertSpeedToPercent(messageParts[4]);
            updateChannel(CHANNEL_FAN_SPEED, state);
            fanStateMap.put(CHANNEL_FAN_SPEED, state);
        }

        private void updateFanDirection(String[] messageParts) {
            logger.debug("Process fan direction update (FWD/REV)");
            if (messageParts.length != 4) {
                logger.debug("FAN;DIR has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            StringType state = new StringType(messageParts[3]);
            updateChannel(CHANNEL_FAN_DIRECTION, state);
            fanStateMap.put(CHANNEL_FAN_DIRECTION, state);
        }

        private void updateFanAuto(String[] messageParts) {
            logger.debug("Process fan auto update (ON/OFF)");
            if (messageParts.length != 4) {
                logger.debug("FAN;AUTO has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_FAN_AUTO, state);
            fanStateMap.put(CHANNEL_FAN_AUTO, state);
        }

        private void updateFanWhoosh(String[] messageParts) {
            logger.debug("Process fan whoosh update (ON/OFF)");
            if (messageParts.length != 5) {
                logger.debug("FAN;WHOOSH has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_FAN_WHOOSH, state);
            fanStateMap.put(CHANNEL_FAN_WHOOSH, state);
        }

        private void updateFanSmartmode(String[] messageParts) {
            logger.debug("Process smartmode update (OFF/COOLING/HEATING)");
            if (messageParts.length != 5) {
                logger.debug("Smartmode has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            StringType state = new StringType(messageParts[4]);
            updateChannel(CHANNEL_FAN_SMARTMODE, state);
            fanStateMap.put(CHANNEL_FAN_DIRECTION, state);
        }

        private void updateFanSpeedMin(String[] messageParts) {
            logger.debug("Process fan speed min update");
            if (messageParts.length != 5) {
                logger.debug("FanSpeedMin has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertSpeedToPercent(messageParts[4]);
            updateChannel(CHANNEL_FAN_SPEED_MIN, state);
            fanStateMap.put(CHANNEL_FAN_SPEED_MIN, state);
        }

        private void updateFanSpeedMax(String[] messageParts) {
            logger.debug("Process fan speed max update");
            if (messageParts.length != 5) {
                logger.debug("FanSpeedMax has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertSpeedToPercent(messageParts[4]);
            updateChannel(CHANNEL_FAN_SPEED_MAX, state);
            fanStateMap.put(CHANNEL_FAN_SPEED_MAX, state);
        }

        private void updateLightPower(String[] messageParts) {
            logger.debug("Process light power update (ON/OFF)");
            if (messageParts.length != 4) {
                logger.debug("LIGHT;PWR has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_LIGHT_POWER, state);
            fanStateMap.put(CHANNEL_LIGHT_POWER, state);
        }

        private void updateLightLevel(String[] messageParts) {
            logger.debug("Process light level update (0..16)");
            if (messageParts.length != 5) {
                logger.debug("LIGHT;LEVEL has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertLevelToPercent(messageParts[4]);
            updateChannel(CHANNEL_LIGHT_LEVEL, state);
            fanStateMap.put(CHANNEL_LIGHT_LEVEL, state);
        }

        private void updateLightAuto(String[] messageParts) {
            logger.debug("Process light auto update (ON/OFF)");
            if (messageParts.length != 4) {
                logger.debug("LIGHT;AUTO has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_LIGHT_AUTO, state);
            fanStateMap.put(CHANNEL_LIGHT_AUTO, state);
        }

        private void updateLightLevelMin(String[] messageParts) {
            logger.debug("Process fan speed min update");
            if (messageParts.length != 5) {
                logger.debug("LightLevelMin has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertSpeedToPercent(messageParts[4]);
            updateChannel(CHANNEL_LIGHT_LEVEL_MIN, state);
            fanStateMap.put(CHANNEL_LIGHT_LEVEL_MIN, state);
        }

        private void updateLightLevelMax(String[] messageParts) {
            logger.debug("Process fan speed max update");
            if (messageParts.length != 5) {
                logger.debug("LightLevelMax has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            PercentType state = convertSpeedToPercent(messageParts[4]);
            updateChannel(CHANNEL_LIGHT_LEVEL_MAX, state);
            fanStateMap.put(CHANNEL_LIGHT_LEVEL_MAX, state);
        }

        private void updateMotion(String[] messageParts) {
            logger.debug("Process motion update (OCCUPIED/UNOCCUPIED)");
            if (messageParts.length != 4) {
                logger.debug("SNSROCC has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            OnOffType state = messageParts[3].toUpperCase().equals("OCCUPIED") ? OnOffType.ON : OnOffType.OFF;
            updateChannel(CHANNEL_MOTION, state);
            fanStateMap.put(CHANNEL_MOTION, state);
        }

        private void updateTime(String[] messageParts) {
            logger.debug("Process time update");
            if (messageParts.length != 4) {
                logger.debug("TIME has unexpected number of parameters: {}", Arrays.toString(messageParts));
                return;
            }
            // (mac|name;TIME;VALUE;2017-03-26T14:06:27Z)
            try {
                Calendar cal = Calendar.getInstance();
                Instant instant = Instant.parse(messageParts[3]);
                cal.setTime(Date.from(instant));
                DateTimeType state = new DateTimeType(cal);
                updateChannel(CHANNEL_TIME, state);
                fanStateMap.put(CHANNEL_TIME, state);
            } catch (DateTimeParseException e) {
                logger.info("Failed to parse date received from fan:{}", messageParts[3]);
            }
        }
    }

    /*
     * The {@link ConnectionManager} class is responsible for managing the state of the TCP connection to the
     * fan.
     *
     * @author Mark Hilbush - Initial contribution
     */
    private class ConnectionManager {
        private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

        private boolean deviceIsConnected;

        private InetAddress ifAddress;
        private Socket fanSocket;
        private Scanner fanScanner;
        private DataOutputStream fanWriter;
        private final int SOCKET_CONNECT_TIMEOUT = 1500;

        ScheduledFuture<?> connectionMonitorJob;
        private final long CONNECTION_MONITOR_FREQ = 60L;
        private final long CONNECTION_MONITOR_DELAY = 30L;

        Runnable connectionMonitorRunnable = new Runnable() {
            @Override
            public void run() {
                logger.trace("Performing connection check for thing {} at IP {}", thingID(), ipAddress);
                checkConnection();
            }
        };

        public ConnectionManager() {
            deviceIsConnected = false;
            try {
                ifAddress = InetAddress.getByName(NetUtil.getLocalIpv4HostAddress());
                logger.debug("Handler using address {} on network interface {}", ifAddress.getHostAddress(),
                        NetworkInterface.getByInetAddress(ifAddress).getName());
            } catch (UnknownHostException e) {
                logger.warn("Handler got UnknownHostException getting local IPv4 net interface: {}", e.getMessage(), e);
                markOfflineWithMessage(ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "No suitable network interface");
            } catch (SocketException e) {
                logger.warn("Handler got SocketException getting local IPv4 network interface: {}", e.getMessage(), e);
                markOfflineWithMessage(ThingStatusDetail.OFFLINE.CONFIGURATION_ERROR, "No suitable network interface");
            }
        }

        /*
         * Connect to the command and serial port(s) on the device. The serial connections are established only for
         * devices that support serial.
         */
        protected synchronized void connect() {
            if (isConnected()) {
                return;
            }
            logger.trace("Connecting to fan thing {} at IP {}", thingID(), ipAddress);

            // Open socket
            try {
                fanSocket = new Socket();
                fanSocket.bind(new InetSocketAddress(ifAddress, 0));
                fanSocket.connect(new InetSocketAddress(ipAddress, BAF_PORT), SOCKET_CONNECT_TIMEOUT);
            } catch (IOException e) {
                logger.debug("IOException connecting to fan thing {} at {}: {}", thingID(), ipAddress, e.getMessage());
                markOfflineWithMessage(ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
                disconnect();
                return;
            }

            // Create streams
            try {
                fanWriter = new DataOutputStream(fanSocket.getOutputStream());
                fanScanner = new Scanner(fanSocket.getInputStream());
                fanScanner.useDelimiter("[)]");
            } catch (IOException e) {
                logger.warn("IOException getting streams for thing {} at {}: {}", thingID(), ipAddress, e.getMessage(),
                        e);
                markOfflineWithMessage(ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR, e.getMessage());
                disconnect();
                return;
            }
            logger.info("Got a connection to fan thing {} at {}", thingID(), ipAddress);
            deviceIsConnected = true;
            markOnline();
        }

        protected synchronized void disconnect() {
            if (!isConnected()) {
                return;
            }
            logger.debug("Disconnecting from fan thing {} at IP {}", thingID(), ipAddress);

            try {
                if (fanWriter != null) {
                    fanWriter.close();
                }
                if (fanScanner != null) {
                    fanScanner.close();
                }
                if (fanSocket != null) {
                    fanSocket.close();
                }
            } catch (IOException e) {
                logger.warn("IOException closing fan connection at IP {}: {}", thingID(), ipAddress, e.getMessage(), e);
            }
            deviceIsConnected = false;
            fanSocket = null;
            fanScanner = null;
            fanWriter = null;
            markOffline();
        }

        public String read() {
            if (fanScanner == null) {
                logger.warn("fanScanner is null when trying to scan from {}!!!", ipAddress);
                return null;
            }
            return fanScanner.next();
        }

        public void write(byte[] buffer) throws IOException {
            if (fanWriter == null) {
                logger.warn("fanWriter is null when trying to write to {}!!!", ipAddress);
                return;
            }
            fanWriter.write(buffer, 0, buffer.length);
        }

        private boolean isConnected() {
            return deviceIsConnected;
        }

        /*
         * Periodically validate the command connection to the device by executing a getversion command.
         */
        private void scheduleConnectionMonitorJob() {
            if (connectionMonitorJob == null) {
                logger.debug("Starting connection monitor job in {} seconds for thing {} at IP {}",
                        CONNECTION_MONITOR_DELAY, thingID(), ipAddress);
                connectionMonitorJob = scheduler.scheduleWithFixedDelay(connectionMonitorRunnable,
                        CONNECTION_MONITOR_DELAY, CONNECTION_MONITOR_FREQ, TimeUnit.SECONDS);
            }
        }

        private void cancelConnectionMonitorJob() {
            if (connectionMonitorJob != null) {
                logger.debug("Canceling connection monitor job for thing {} at IP {}", thingID(), ipAddress);
                connectionMonitorJob.cancel(true);
                connectionMonitorJob = null;
            }
        }

        private void checkConnection() {
            logger.trace("Checking status of connection for thing {} at IP {}", thingID(), ipAddress);
            if (!isConnected()) {
                logger.debug("Connection check FAILED for thing {} at IP {}", thingID(), ipAddress);
                connect();
            } else {
                logger.debug("Connection check OK for thing {} at IP {}", thingID(), ipAddress);
            }
        }
    }
}
