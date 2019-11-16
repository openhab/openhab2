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
package org.openhab.binding.lghombot.internal;

import static org.openhab.binding.lghombot.internal.LGHomBotBindingConstants.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.RawType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LGHomBotHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Fredrik Ahlström - Initial contribution
 */
@NonNullByDefault
public class LGHomBotHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(LGHomBotHandler.class);

    // This is setup in initialize().
    private LGHomBotConfiguration config = new LGHomBotConfiguration();

    private @Nullable ScheduledFuture<?> refreshTimer;

    // State of HomBot
    private String currentState = "";
    private String currentMode = "";
    private String currentNickname = "";
    private String currentSrvMem = "";
    private DecimalType currentBattery = DecimalType.ZERO;
    private DecimalType currentCPULoad = DecimalType.ZERO;
    private OnOffType currentCleanState = OnOffType.OFF;
    private OnOffType currentStartState = OnOffType.OFF;
    private OnOffType currentHomeState = OnOffType.OFF;
    private OnOffType currentTurbo = OnOffType.OFF;
    private OnOffType currentRepeat = OnOffType.OFF;
    private State currentImage = UnDefType.UNDEF;
    private State currentMap = UnDefType.UNDEF;
    private DateTimeType currentLastClean = new DateTimeType();
    private String currentMonday = "";
    private String currentTuesday = "";
    private String currentWednesday = "";
    private String currentThursday = "";
    private String currentFriday = "";
    private String currentSaturday = "";
    private String currentSunday = "";

    private final DateTimeFormatter formatterLG = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");
    private boolean disposed = false;
    private int refreshCounter = 0;

    public LGHomBotHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void dispose() {
        super.dispose();
        disposed = true;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command.equals(RefreshType.REFRESH)) {
            refreshFromState(channelUID);
        } else {
            switch (channelUID.getId()) {
                case CHANNEL_CLEAN:
                    if (command instanceof OnOffType) {
                        if (((OnOffType) command) == OnOffType.ON) {
                            if (currentState.equals(HBSTATE_HOMING)) {
                                sendHomBotCommand("PAUSE");
                            }
                            sendHomBotCommand("CLEAN_START");
                        } else if (((OnOffType) command) == OnOffType.OFF) {
                            sendHomBotCommand("HOMING");
                        }
                    }
                    break;
                case CHANNEL_START:
                    if (command == OnOffType.ON) {
                        sendHomBotCommand("CLEAN_START");
                    }
                    break;
                case CHANNEL_HOME:
                    if (command == OnOffType.ON) {
                        sendHomBotCommand("HOMING");
                    }
                    break;
                case CHANNEL_PAUSE:
                    if (command instanceof OnOffType) {
                        sendHomBotCommand("PAUSE");
                    }
                    break;
                case CHANNEL_TURBO:
                    if (command instanceof OnOffType) {
                        if (((OnOffType) command) == OnOffType.ON) {
                            sendHomBotCommand("TURBO", "true");
                        } else if (((OnOffType) command) == OnOffType.OFF) {
                            sendHomBotCommand("TURBO", "false");
                        }
                    }
                    break;
                case CHANNEL_REPEAT:
                    if (command instanceof OnOffType) {
                        if (((OnOffType) command) == OnOffType.ON) {
                            sendHomBotCommand("REPEAT", "true");
                        } else if (((OnOffType) command) == OnOffType.OFF) {
                            sendHomBotCommand("REPEAT", "false");
                        }
                    }
                    break;
                case CHANNEL_MODE:
                    if (command instanceof StringType) {
                        switch (((StringType) command).toString()) {
                            case "SB":
                                sendHomBotCommand("CLEAN_MODE", "CLEAN_SB");
                                break;
                            case "ZZ":
                                sendHomBotCommand("CLEAN_MODE", "CLEAN_ZZ");
                                break;
                            case "SPOT":
                                sendHomBotCommand("CLEAN_MODE", "CLEAN_SPOT");
                                break;
                            case "MACRO_SECTOR":
                                sendHomBotCommand("CLEAN_MODE", "CLEAN_MACRO_SECTOR");
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case CHANNEL_MOVE:
                    if (command instanceof StringType) {
                        if (((StringType) command).toString().equals("FORWARD")) {
                            sendHomBotJoystick("FORWARD");
                        } else if (((StringType) command).toString().equals("FORWARD_LEFT")) {
                            sendHomBotJoystick("FORWARD_LEFT");
                        } else if (((StringType) command).toString().equals("FORWARD_RIGHT")) {
                            sendHomBotJoystick("FORWARD_RIGHT");
                        } else if (((StringType) command).toString().equals("LEFT")) {
                            sendHomBotJoystick("LEFT");
                        } else if (((StringType) command).toString().equals("RIGHT")) {
                            sendHomBotJoystick("RIGHT");
                        } else if (((StringType) command).toString().equals("BACKWARD")) {
                            sendHomBotJoystick("BACKWARD");
                        } else if (((StringType) command).toString().equals("BACKWARD_LEFT")) {
                            sendHomBotJoystick("BACKWARD_LEFT");
                        } else if (((StringType) command).toString().equals("BACKWARD_RIGHT")) {
                            sendHomBotJoystick("BACKWARD_RIGHT");
                        } else if (((StringType) command).toString().equals("RELEASE")) {
                            sendHomBotJoystick("RELEASE");
                        }
                    }
                    break;
                default:
                    logger.debug("Command received for unknown channel {}: {}", channelUID.getId(), command);
                    break;
            }
        }
    }

    @Override
    public void initialize() {
        disposed = false;
        logger.debug("Initializing handler for LG HomBot");
        config = getConfigAs(LGHomBotConfiguration.class);

        updateAllChannels();
        setupRefreshTimer(config.getPollingPeriod());

    }

    /**
     * Sets up a refresh timer (using the scheduler) with the given interval.
     *
     * @param initialWaitTime The delay before the first refresh. Maybe 0 to immediately
     *            initiate a refresh.
     */
    private void setupRefreshTimer(int initialWaitTime) {
        ScheduledFuture<?> localTimer = refreshTimer;
        if (localTimer != null) {
            localTimer.cancel(false);
        }
        refreshTimer = scheduler.scheduleWithFixedDelay(this::updateAllChannels, initialWaitTime,
                config.getPollingPeriod(), TimeUnit.SECONDS);
    }

    private String buildHttpAddress(String path) {
        return "http://" + config.getIpAddress() + ":" + config.getPort() + path;
    }

    private void sendHomBotCommand(String command) {
        String fullCmd = UrlEncoded.encodeString("/json.cgi?{\"COMMAND\":\"" + command + "\"}");
        sendCommand(fullCmd);
    }

    private void sendHomBotCommand(String command, String argument) {
        String fullCmd = UrlEncoded.encodeString("/json.cgi?{\"COMMAND\":{\"" + command + "\":\"" + argument + "\"}}");
        sendCommand(fullCmd);
    }

    private void sendHomBotJoystick(String command) {
        String fullCmd = UrlEncoded.encodeString("/json.cgi?{\"JOY\":\"" + command + "\"}");
        sendCommand(fullCmd);
    }

    private void sendCommand(String path) {
        String url = buildHttpAddress(path);
        String status = null;
        try {
            status = HttpUtil.executeUrl("GET", url, 1000);
            if (getThing().getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
        logger.debug("Status received: {}", status);
    }

    private void refreshFromState(ChannelUID channelUID) {
        switch (channelUID.getId()) {
            case CHANNEL_STATE:
                updateState(channelUID, StringType.valueOf(currentState));
                break;
            case CHANNEL_CLEAN:
                updateState(channelUID, currentCleanState);
                break;
            case CHANNEL_START:
                updateState(channelUID, currentStartState);
                break;
            case CHANNEL_HOME:
                updateState(channelUID, currentHomeState);
                break;
            case CHANNEL_BATTERY:
                updateState(channelUID, currentBattery);
                break;
            case CHANNEL_CPU_LOAD:
                updateState(channelUID, currentCPULoad);
                break;
            case CHANNEL_SRV_MEM:
                updateState(channelUID, StringType.valueOf(currentSrvMem));
                break;
            case CHANNEL_TURBO:
                updateState(channelUID, currentTurbo);
                break;
            case CHANNEL_REPEAT:
                updateState(channelUID, currentRepeat);
                break;
            case CHANNEL_MODE:
                updateState(channelUID, StringType.valueOf(currentMode));
                break;
            case CHANNEL_NICKNAME:
                updateState(channelUID, StringType.valueOf(currentNickname));
                break;
            case CHANNEL_CAMERA:
                parseImage();
                updateState(channelUID, currentImage);
                break;
            case CHANNEL_LAST_CLEAN:
                updateState(channelUID, currentLastClean);
                break;
            case CHANNEL_MAP:
                parseMap();
                updateState(channelUID, currentMap);
                break;
            case CHANNEL_MONDAY:
            case CHANNEL_TUESDAY:
            case CHANNEL_WEDNESDAY:
            case CHANNEL_THURSDAY:
            case CHANNEL_FRIDAY:
            case CHANNEL_SATURDAY:
            case CHANNEL_SUNDAY:
                refreshCounter = 1;
                break;
            default:
                logger.warn("Channel refresh for {} not implemented!", channelUID.getId());
        }
    }

    private void updateAllChannels() {
        if (disposed) {
            return;
        }
        if (refreshCounter > 0) {
            refreshCounter--;
            if (refreshCounter == 0) {
                fetchSchedule();
                return;
            }
            if (refreshCounter % 5 == 1) {
                parseImage();
                updateState(new ChannelUID(getThing().getUID(), CHANNEL_CAMERA), currentImage);
                return;
            }
        }
        String status = null;
        String url = buildHttpAddress("/status.txt");
        try {
            status = HttpUtil.executeUrl("GET", url, 1000);
            if (getThing().getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
        if (status != null && !status.isEmpty()) {
            boolean parsingOk = true;
            String[] rows = status.split("\\r?\\n");
            ChannelUID channel;
            for (String row : rows) {
                int idx = row.indexOf('=');
                if (idx == -1) {
                    continue;
                }
                final String key = row.substring(0, idx);
                String value = row.substring(idx + 1).replace("\"", "");
                switch (key) {
                    case "JSON_ROBOT_STATE":
                        if (value.isEmpty()) {
                            value = HBSTATE_UNKNOWN;
                        }
                        if (!value.equals(currentState)) {
                            currentState = value;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_STATE);
                            updateState(channel, StringType.valueOf(value));

                            if (value.equals(HBSTATE_WORKING) || value.equals(HBSTATE_BACKMOVING)
                                    || value.equals(HBSTATE_BACKMOVING_INIT)) {
                                currentCleanState = OnOffType.ON;
                                currentStartState = OnOffType.ON;
                                currentHomeState = OnOffType.OFF;
                            } else if (value.equals(HBSTATE_HOMING) || value.equals(HBSTATE_DOCKING)) {
                                currentCleanState = OnOffType.OFF;
                                currentStartState = OnOffType.OFF;
                                currentHomeState = OnOffType.ON;
                            } else {
                                currentCleanState = OnOffType.OFF;
                                currentStartState = OnOffType.OFF;
                                currentHomeState = OnOffType.OFF;
                            }
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_CLEAN);
                            updateState(channel, currentCleanState);
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_START);
                            updateState(channel, currentStartState);
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_HOME);
                            updateState(channel, currentHomeState);
                        }
                        break;
                    case "JSON_BATTPERC":
                        try {
                            DecimalType battery = DecimalType.valueOf(value);
                            if (!battery.equals(currentBattery)) {
                                currentBattery = battery;
                                channel = new ChannelUID(getThing().getUID(), CHANNEL_BATTERY);
                                updateState(channel, battery);
                            }
                        } catch (NumberFormatException e) {
                            logger.debug("Couldn't parse Battery Percent.");
                            parsingOk = false;
                        }
                        break;
                    case "CPU_IDLE":
                        if (isLinked(CHANNEL_CPU_LOAD)) {
                            try {
                                DecimalType cpuLoad = new DecimalType(100 - Double.valueOf(value).longValue());
                                if (!cpuLoad.equals(currentCPULoad)) {
                                    currentCPULoad = cpuLoad;
                                    channel = new ChannelUID(getThing().getUID(), CHANNEL_CPU_LOAD);
                                    updateState(channel, cpuLoad);
                                }
                            } catch (NumberFormatException e) {
                                logger.debug("Couldn't parse CPU Idle.");
                                parsingOk = false;
                            }
                        }
                        break;
                    case "LGSRV_MEMUSAGE":
                        if (!value.equals(currentSrvMem)) {
                            currentSrvMem = value;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_SRV_MEM);
                            updateState(channel, StringType.valueOf(value));
                        }
                        break;
                    case "JSON_TURBO":
                        OnOffType turbo = value.equalsIgnoreCase("true") ? OnOffType.ON : OnOffType.OFF;
                        if (!turbo.equals(currentTurbo)) {
                            currentTurbo = turbo;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_TURBO);
                            updateState(channel, turbo);
                        }
                        break;
                    case "JSON_REPEAT":
                        OnOffType repeat = value.equalsIgnoreCase("true") ? OnOffType.ON : OnOffType.OFF;
                        if (!repeat.equals(currentRepeat)) {
                            currentRepeat = repeat;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_REPEAT);
                            updateState(channel, repeat);
                        }
                        break;
                    case "JSON_MODE":
                        if (!value.equals(currentMode)) {
                            currentMode = value;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_MODE);
                            updateState(channel, StringType.valueOf(value));
                        }
                        break;
                    case "JSON_NICKNAME":
                        if (!value.equals(currentNickname)) {
                            currentNickname = value;
                            channel = new ChannelUID(getThing().getUID(), CHANNEL_NICKNAME);
                            updateState(channel, StringType.valueOf(value));
                        }
                        break;
                    case "CLREC_LAST_CLEAN":
                        try {
                            final String stringDate = value.substring(0, 19);
                            LocalDateTime localDateTime = LocalDateTime.parse(stringDate, formatterLG);
                            ZonedDateTime date = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
                            DateTimeType lastClean = new DateTimeType(date);
                            if (!lastClean.equals(currentLastClean)) {
                                currentLastClean = lastClean;
                                channel = new ChannelUID(getThing().getUID(), CHANNEL_LAST_CLEAN);
                                updateState(channel, lastClean);
                            }
                        } catch (IndexOutOfBoundsException | DateTimeException e) {
                            logger.debug("Couldn't parse Last Clean. String length: {}", value.length());
                        }
                        break;
                    default:
                        break;
                }
            }
            if (!parsingOk) {
                logger.debug("Couldn't parse response;\n {}", status);
            }
        }
    }

    private void fetchSchedule() {
        String status = null;
        String url = buildHttpAddress("/.../usr/data/htdocs/timer.txt");
        try {
            status = HttpUtil.executeUrl("GET", url, 1000);
            if (getThing().getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            logger.info("Schedule file not found, probably nothing set.", e);
            return;
        }
        if (status != null && !status.isEmpty()) {
            String monday = "";
            String tuesday = "";
            String wednesday = "";
            String thursday = "";
            String friday = "";
            String saturday = "";
            String sunday = "";
            String[] rows = status.split("\\r?\\n");
            ChannelUID channel;
            for (String row : rows) {
                int idx = row.indexOf('=');
                String name = row.substring(0, idx);
                String state = row.substring(idx + 1);
                switch (name) {
                    case "MONDAY":
                        monday = state;
                        break;
                    case "TUESDAY":
                        tuesday = state;
                        break;
                    case "WEDNESDAY":
                        wednesday = state;
                        break;
                    case "THURSDAY":
                        thursday = state;
                        break;
                    case "FRIDAY":
                        friday = state;
                        break;
                    case "SATURDAY":
                        saturday = state;
                        break;
                    case "SUNDAY":
                        sunday = state;
                        break;
                    default:
                        break;
                }

            }
            if (!currentMonday.equals(monday)) {
                currentMonday = monday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_MONDAY);
                updateState(channel, StringType.valueOf(monday));
            }
            if (!currentTuesday.equals(tuesday)) {
                currentTuesday = tuesday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_TUESDAY);
                updateState(channel, StringType.valueOf(tuesday));
            }
            if (!currentWednesday.equals(wednesday)) {
                currentWednesday = wednesday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_WEDNESDAY);
                updateState(channel, StringType.valueOf(wednesday));
            }
            if (!currentThursday.equals(thursday)) {
                currentThursday = thursday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_THURSDAY);
                updateState(channel, StringType.valueOf(thursday));
            }
            if (!currentFriday.equals(friday)) {
                currentFriday = friday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_FRIDAY);
                updateState(channel, StringType.valueOf(friday));
            }
            if (!currentSaturday.equals(saturday)) {
                currentSaturday = saturday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_SATURDAY);
                updateState(channel, StringType.valueOf(saturday));
            }
            if (!currentSunday.equals(sunday)) {
                currentSunday = sunday;
                channel = new ChannelUID(getThing().getUID(), CHANNEL_SUNDAY);
                updateState(channel, StringType.valueOf(sunday));
            }
        }
    }

    private void parseImage() {
        if (!isLinked(CHANNEL_CAMERA)) {
            return;
        }
        final int width = 320;
        final int height = 240;
        final int size = width * height;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        String url = buildHttpAddress("/images/snapshot.yuv");
        byte[] yuvData = HttpUtil.downloadData(url, null, false, size * 2).getBytes();

        for (int i = 0; i < size; i++) {
            double y = yuvData[i] & 0xFF;
            double u = yuvData[size + i / 2] & 0xFF;
            double v = yuvData[(int) (size * 1.5 + i / 2.0)] & 0xFF;

            int r = Math.min(Math.max((int) (y + 1.371 * (v - 128)), 0), 255); // red
            int g = Math.min(Math.max((int) (y - 0.336 * (u - 128) - 0.698 * (v - 128)), 0), 255); // green
            int b = Math.min(Math.max((int) (y + 1.732 * (u - 128)), 0), 255); // blue

            int p = (r << 16) | (g << 8) | b; // pixel
            image.setRGB(i % width, i / width, p);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!ImageIO.write(image, "jpg", baos)) {
                logger.debug("Couldn't find JPEG writer.");
            }
        } catch (IOException e) {
            logger.info("IOException creating JPEG image.", e);
        }
        byte[] byteArray = baos.toByteArray();
        if (byteArray != null && byteArray.length > 0) {
            currentImage = new RawType(byteArray, "image/jpeg");
        } else {
            currentImage = UnDefType.UNDEF;
        }
    }

    /** Parse the maps.html file to find the black-box filename. */
    private String findBlackBoxFile() {
        String url = buildHttpAddress("/sites/maps.html");
        try {
            String htmlString = HttpUtil.executeUrl("GET", url, 1000);
            int idx = htmlString.indexOf("blkfiles");
            return "/.../usr/data/blackbox/" + htmlString.substring(idx + 13, idx + 50);
        } catch (IOException e1) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e1.getMessage());
        }
        return "";
    }

    private void parseMap() {
        if (!isLinked(CHANNEL_MAP)) {
            return;
        }
        final int tileSize = 10;
        final int tileArea = tileSize * tileSize;
        final int rowLength = 100;
        final int scale = 1;

        String blackBox = findBlackBoxFile();
        String url = buildHttpAddress(blackBox);
        RawType dlData = HttpUtil.downloadData(url, null, false, -1);
        if (dlData == null) {
            return;
        }
        byte[] mapData = dlData.getBytes();

        final int tileCount = mapData[32];
        int maxX = 0;
        int maxY = 0;
        int minX = 0x10000;
        int minY = 0x10000;
        int pixPos;

        for (int i = 0; i < tileCount; i++) {
            pixPos = (mapData[52 + i * 16] & 0xFF) + (mapData[52 + 1 + i * 16] << 8);
            int xPos = (pixPos % rowLength) * tileSize;
            int yPos = (pixPos / rowLength) * tileSize;
            if (xPos < minX) {
                minX = xPos;
            }
            if (xPos > maxX) {
                maxX = xPos;
            }
            if (yPos > maxY) {
                maxY = yPos;
            }
            if (yPos < minY) {
                minY = yPos;
            }
        }

        final int width = (tileSize + maxX - minX) * scale;
        final int height = (tileSize + maxY - minY) * scale;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j, i, 0xFFFFFF);
            }
        }
        for (int i = 0; i < tileCount; i++) {
            pixPos = (mapData[52 + i * 16] & 0xFF) + (mapData[52 + 1 + i * 16] << 8);
            int xPos = ((pixPos % rowLength) * tileSize - minX) * scale;
            int yPos = (maxY - (pixPos / rowLength) * tileSize) * scale;
            int indexTab = 16044 + i * tileArea;
            for (int j = 0; j < tileSize; j++) {
                for (int k = 0; k < tileSize; k++) {
                    int p = 0xFFFFFF;
                    if ((mapData[indexTab] & 0xF0) != 0) {
                        p = 0xFF0000;
                    } else if (mapData[indexTab] != 0) {
                        p = 0xBFBFBF;
                    }
                    image.setRGB(xPos + k * scale, yPos + (9 - j) * scale, p);
                    indexTab++;
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (!ImageIO.write(image, "png", baos)) {
                logger.debug("Couldn't find PNG writer.");
            }
        } catch (IOException e) {
            logger.info("IOException creating PNG image.", e);
        }
        byte[] byteArray = baos.toByteArray();
        if (byteArray != null && byteArray.length > 0) {
            currentMap = new RawType(byteArray, "image/png");
        } else {
            currentMap = UnDefType.UNDEF;
        }
    }
}
