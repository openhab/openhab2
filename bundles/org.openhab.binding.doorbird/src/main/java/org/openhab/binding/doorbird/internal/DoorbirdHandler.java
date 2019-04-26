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
package org.openhab.binding.doorbird.internal;

import static org.openhab.binding.doorbird.internal.DoorbirdBindingConstants.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.smarthome.core.common.ThreadPoolManager;
import org.eclipse.smarthome.core.i18n.TimeZoneProvider;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.RawType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.io.net.http.HttpRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DoorbirdHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Mark Hilbush - Initial contribution
 */
@NonNullByDefault
public class DoorbirdHandler extends BaseThingHandler {
    private static final long API_REQUEST_TIMEOUT = 3L;
    private static final long MONTAGE_UPDATE_DELAY = 5L;

    private final Logger logger = LoggerFactory.getLogger(DoorbirdHandler.class);

    // Get a dedicated threadpool for the listener thread
    private final ScheduledExecutorService doorbirdScheduler = ThreadPoolManager.getScheduledPool("doorbirdHandler");
    private @Nullable ScheduledFuture<?> listenerJob;
    private DoorbirdUdpListener udpListener;

    private @Nullable ScheduledFuture<?> imageRefreshJob;
    private @Nullable ScheduledFuture<?> doorbellOffJob;
    private @Nullable ScheduledFuture<?> motionOffJob;

    private @Nullable String doorbirdId;
    private @Nullable String doorbirdHost;
    private @Nullable String userId;
    private @Nullable String userPassword;
    private @Nullable String authorization;

    private TimeZoneProvider timeZoneProvider;
    private HttpClient httpClient;

    public DoorbirdHandler(Thing thing, TimeZoneProvider timeZoneProvider, HttpClient httpClient) {
        super(thing);
        this.timeZoneProvider = timeZoneProvider;
        this.httpClient = httpClient;
        udpListener = new DoorbirdUdpListener(this);
        logger.debug("Handler created for {}", thing.getUID());
    }

    @Override
    public void initialize() {
        logger.debug("Handler initializing for {}", getThing().getUID());
        updateStatus(ThingStatus.UNKNOWN);

        DoorbirdConfiguration config = getConfigAs(DoorbirdConfiguration.class);
        doorbirdId = config.doorbirdId;
        if (StringUtils.isEmpty(doorbirdId)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Doorbird id not provided");
            return;
        }
        doorbirdHost = config.doorbirdHost;
        if (StringUtils.isEmpty(doorbirdHost)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Doorbird host not provided");
            return;
        }
        userId = config.userId;
        if (StringUtils.isEmpty(userId)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "User ID not provided");
            return;
        }
        userPassword = config.userPassword;
        if (StringUtils.isEmpty(userPassword)) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "User password not provided");
            return;
        }
        // Create the basic authorization string for the HTTP requests
        authorization = new String(Base64.getEncoder().encode((userId + ":" + userPassword).getBytes()),
                StandardCharsets.UTF_8);

        startImageRefreshJob();
        startUDPListenerJob();
    }

    @Override
    public void dispose() {
        logger.debug("Handler disposing for {}", getThing().getUID());
        stopUDPListenerJob();
        stopImageRefreshJob();
        stopDoorbellOffJob();
        stopMotionOffJob();
    }

    // Callback used by listener to get Doorbird ID
    public @Nullable String getDoorbirdId() {
        return doorbirdId;
    }

    // Callback used by listener to get Doorbird host name
    public @Nullable String getDoorbirdHost() {
        return doorbirdHost;
    }

    // Callback used by listener to get Doorbird password
    public @Nullable String getUserId() {
        return userId;
    }

    // Callback used by listener to get Doorbird password
    public @Nullable String getUserPassword() {
        return userPassword;
    }

    // Callback used by listener to update doorbell channel
    public synchronized void updateDoorbellChannel(long timestamp) {
        logger.debug("Handler: Update DOORBELL channels for thing {}", getThing().getUID());
        updateState(CHANNEL_DOORBELL, OnOffType.ON);
        DoorbirdImage dbImage = downloadImage(buildUrl("/bha-api/image.cgi"));
        if (dbImage != null) {
            RawType image = dbImage.getImage();
            updateState(CHANNEL_DOORBELL_IMAGE, image != null ? image : UnDefType.UNDEF);
            updateState(CHANNEL_DOORBELL_TIMESTAMP, getLocalDateTimeType(dbImage.getTimestamp()));
        }
        startDoorbellOffJob();
        updateDoorbellMontage();
    }

    // Callback used by listener to update motion channel
    public synchronized void updateMotionChannel(long timestamp) {
        logger.debug("Handler: Update MOTION channels for thing {}", getThing().getUID());
        updateState(CHANNEL_MOTION, OnOffType.ON);
        DoorbirdImage dbImage = downloadImage(buildUrl("/bha-api/image.cgi"));
        if (dbImage != null) {
            RawType image = dbImage.getImage();
            updateState(CHANNEL_MOTION_IMAGE, image != null ? image : UnDefType.UNDEF);
            updateState(CHANNEL_MOTION_TIMESTAMP, getLocalDateTimeType(dbImage.getTimestamp()));
        }
        startMotionOffJob();
        updateMotionMontage();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Got command {} for channel {} of thing {}", command, channelUID, getThing().getUID());

        switch (channelUID.getId()) {
            case CHANNEL_DOORBELL_IMAGE:
                if (command instanceof RefreshType) {
                    refreshDoorbellImageFromHistory();
                }
                break;

            case CHANNEL_MOTION_IMAGE:
                if (command instanceof RefreshType) {
                    refreshMotionImageFromHistory();
                }
                break;
            case CHANNEL_LIGHT:
                handleLight(command);
                break;

            case CHANNEL_OPENDOOR1:
                handleOpenDoor(command, "1");
                break;

            case CHANNEL_OPENDOOR2:
                handleOpenDoor(command, "2");
                break;

            case CHANNEL_RESTART:
                handleRestart(command);
                break;

            case CHANNEL_GETIMAGE:
                handleGetImage(command);
                break;

            case CHANNEL_DOORBELL_HISTORY_INDEX:
            case CHANNEL_MOTION_HISTORY_INDEX:
                handleHistoryImage(channelUID, command);
                break;

            case CHANNEL_GET_DOORBELL_MONTAGE:
                if (command instanceof RefreshType || (command instanceof OnOffType && command.equals(OnOffType.ON))) {
                    updateDoorbellMontage();
                }
                break;

            case CHANNEL_GET_MOTION_MONTAGE:
                if (command instanceof RefreshType || (command instanceof OnOffType && command.equals(OnOffType.ON))) {
                    updateMotionMontage();
                }
                break;
        }
    }

    private void refreshDoorbellImageFromHistory() {
        logger.debug("Handler: REFRESH doorbell image channel using most recent doorbell history image");
        scheduler.schedule(() -> {
            String url = buildUrl("/bha-api/history.cgi", "?event=doorbell&index=1");
            DoorbirdImage dbImage = downloadImage(url);
            if (dbImage != null) {
                RawType image = dbImage.getImage();
                updateState(CHANNEL_DOORBELL_IMAGE, image != null ? image : UnDefType.UNDEF);
                updateState(CHANNEL_DOORBELL_TIMESTAMP, getLocalDateTimeType(dbImage.getTimestamp()));
            }
            updateState(CHANNEL_DOORBELL, OnOffType.OFF);
        }, 0, TimeUnit.SECONDS);
    }

    private void refreshMotionImageFromHistory() {
        logger.debug("Handler: REFRESH motion image channel using most recent motion history image");
        scheduler.schedule(() -> {
            String url = buildUrl("/bha-api/history.cgi", "?event=motionsensor&index=1");
            DoorbirdImage dbImage = downloadImage(url);
            if (dbImage != null) {
                RawType image = dbImage.getImage();
                updateState(CHANNEL_MOTION_IMAGE, image != null ? image : UnDefType.UNDEF);
                updateState(CHANNEL_MOTION_TIMESTAMP, getLocalDateTimeType(dbImage.getTimestamp()));
            }
            updateState(CHANNEL_MOTION, OnOffType.OFF);
        }, 0, TimeUnit.SECONDS);
    }

    private void handleLight(Command command) {
        if (command instanceof OnOffType && command.equals(OnOffType.ON)) {
            String url = buildUrl("/bha-api/light-on.cgi");
            logger.debug("Turn light on using url={}", url);
            try {
                String response = executeGetRequest(url);
                logger.debug("Response={}", response);
            } catch (IOException e) {
                logger.debug("IOException turning on light: {}", e.getMessage());
            }
        }
    }

    private void handleOpenDoor(Command command, String doorNumber) {
        if (command instanceof OnOffType && command.equals(OnOffType.ON)) {
            String url = buildUrl("/bha-api/open-door.cgi", "?r=" + doorNumber);
            logger.debug("Open door using url={}", url);
            try {
                String response = executeGetRequest(url);
                logger.debug("Response={}", response);
            } catch (IOException e) {
                logger.debug("IOException opening door: {}", e.getMessage());
            }
        }
    }

    private void handleRestart(Command command) {
        if (command instanceof OnOffType && command.equals(OnOffType.ON)) {
            String url = buildUrl("/bha-api/restart.cgi");
            logger.debug("Restart device using url={}", url);
            try {
                String response = executeGetRequest(url);
                logger.debug("Response={}", response);
            } catch (IOException e) {
                logger.debug("IOException restarting device: {}", e.getMessage());
            }
        }
    }

    private void handleGetImage(Command command) {
        if (command instanceof OnOffType && command.equals(OnOffType.ON)) {
            scheduler.schedule(() -> {
                updateImageAndTimestamp();
            }, 0, TimeUnit.SECONDS);
        }
    }

    private void handleHistoryImage(ChannelUID channelUID, Command command) {
        if (!(command instanceof DecimalType)) {
            logger.debug("History index must be of type DecimalType");
            return;
        }
        int value = ((DecimalType) command).intValue();
        if (value < 0 || value > 50) {
            logger.debug("History index must be in range 1 to 50");
            return;
        }
        boolean isDoorbell = CHANNEL_DOORBELL_HISTORY_INDEX.equals(channelUID.getId());
        String event = isDoorbell ? "doorbell" : "motionsensor";
        String imageChannelId = isDoorbell ? CHANNEL_DOORBELL_HISTORY_IMAGE : CHANNEL_MOTION_HISTORY_IMAGE;
        String timestampChannelId = isDoorbell ? CHANNEL_DOORBELL_HISTORY_TIMESTAMP : CHANNEL_MOTION_HISTORY_TIMESTAMP;
        String url = buildUrl("/bha-api/history.cgi", "?event=" + event + "&index=" + command.toString());

        DoorbirdImage dbImage = downloadImage(url);
        if (dbImage != null) {
            RawType image = dbImage.getImage();
            updateState(imageChannelId, image != null ? image : UnDefType.UNDEF);
            updateState(timestampChannelId, getLocalDateTimeType(dbImage.getTimestamp()));
        }
    }

    private void startImageRefreshJob() {
        Integer imageRefreshRate = getConfigAs(DoorbirdConfiguration.class).imageRefreshRate;
        if (imageRefreshRate != null) {
            imageRefreshJob = scheduler.scheduleWithFixedDelay(() -> {
                try {
                    updateImageAndTimestamp();
                } catch (RuntimeException e) {
                    logger.debug("Refresh image job got unhandled exception: {}", e.getMessage(), e);
                }
            }, 8L, imageRefreshRate, TimeUnit.SECONDS);
            logger.debug("Scheduled job to refresh image channel every {} seconds", imageRefreshRate);
        }
    }

    private void stopImageRefreshJob() {
        if (imageRefreshJob != null) {
            imageRefreshJob.cancel(true);
            imageRefreshJob = null;
            logger.debug("Canceling image refresh job");
        }
    }

    private void startUDPListenerJob() {
        logger.debug("Scheduled listener job to start in 5 seconds");
        listenerJob = doorbirdScheduler.schedule(udpListener, 5, TimeUnit.SECONDS);
        updateStatus(ThingStatus.ONLINE);
    }

    private void stopUDPListenerJob() {
        if (listenerJob != null) {
            listenerJob.cancel(true);
            udpListener.shutdown();
            logger.debug("Canceling listener job");
        }
    }

    private void startDoorbellOffJob() {
        Integer offDelay = getConfigAs(DoorbirdConfiguration.class).doorbellOffDelay;
        if (offDelay == null) {
            return;
        }
        if (doorbellOffJob != null) {
            doorbellOffJob.cancel(true);
        }
        doorbellOffJob = scheduler.schedule(() -> {
            logger.debug("Update channel 'doorbell' to OFF for thing {}", getThing().getUID());
            updateState(CHANNEL_DOORBELL, OnOffType.OFF);
        }, offDelay, TimeUnit.SECONDS);
    }

    private void stopDoorbellOffJob() {
        if (doorbellOffJob != null) {
            doorbellOffJob.cancel(true);
            doorbellOffJob = null;
            logger.debug("Canceling doorbell off job");
        }
    }

    private void startMotionOffJob() {
        Integer offDelay = getConfigAs(DoorbirdConfiguration.class).motionOffDelay;
        if (offDelay == null) {
            return;
        }
        if (motionOffJob != null) {
            motionOffJob.cancel(true);
        }
        motionOffJob = scheduler.schedule(() -> {
            logger.debug("Update channel 'motion' to OFF for thing {}", getThing().getUID());
            updateState(CHANNEL_MOTION, OnOffType.OFF);
        }, offDelay, TimeUnit.SECONDS);
    }

    private void stopMotionOffJob() {
        if (motionOffJob != null) {
            motionOffJob.cancel(true);
            motionOffJob = null;
            logger.debug("Canceling motion off job");
        }
    }

    private void updateDoorbellMontage() {
        if (getConfigAs(DoorbirdConfiguration.class).montageNumImages == 0) {
            return;
        }
        logger.debug("Scheduling DOORBELL montage update to run in {} seconds", MONTAGE_UPDATE_DELAY);
        scheduler.schedule(() -> {
            updateMontage("doorbell", CHANNEL_DOORBELL_IMAGE_MONTAGE);
        }, MONTAGE_UPDATE_DELAY, TimeUnit.SECONDS);
    }

    private void updateMotionMontage() {
        if (getConfigAs(DoorbirdConfiguration.class).montageNumImages == 0) {
            return;
        }
        logger.debug("Scheduling MOTION montage update to run in {} seconds", MONTAGE_UPDATE_DELAY);
        scheduler.schedule(() -> {
            updateMontage("motionsensor", CHANNEL_MOTION_IMAGE_MONTAGE);
        }, MONTAGE_UPDATE_DELAY, TimeUnit.SECONDS);
    }

    private void updateMontage(String event, String channelId) {
        logger.debug("Update '{}' montage for channel '{}'", event, channelId);
        ArrayList<BufferedImage> images = getImages(event);
        if (images.size() > 0) {
            State state = createMontage(images);
            if (state != null) {
                logger.debug("Got a '{}' montage. Updating channel '{}' with image montage", event, channelId);
                updateState(channelId, state);
                return;
            }
        }
        logger.debug("Updating channel '{}' with NULL image montage", channelId);
        updateState(channelId, UnDefType.NULL);
    }

    // Get an array list of history images
    private ArrayList<BufferedImage> getImages(String event) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        int numberOfImages = getConfigAs(DoorbirdConfiguration.class).montageNumImages;
        String url = buildUrl("/bha-api/history.cgi", "?event=" + event + "&index=");
        for (int imageNumber = 1; imageNumber <= numberOfImages; imageNumber++) {
            String imageUrl = url + String.valueOf(imageNumber);
            logger.debug("Downloading '{}' montage image {}", event, imageNumber);
            DoorbirdImage historyImage = downloadImage(imageUrl);
            if (historyImage != null) {
                RawType image = historyImage.getImage();
                if (image != null) {
                    try {
                        BufferedImage i = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
                        images.add(i);
                    } catch (IOException e) {
                        logger.debug("IOException creating BufferedImage from downloaded image: {}", e.getMessage());
                    }
                }
            }
        }
        if (images.size() < numberOfImages) {
            logger.debug("Some images could not be downloaded: wanted={}, actual={}", numberOfImages, images.size());
        }
        return images;
    }

    // Assemble the array of images into a single scaled image
    private @Nullable State createMontage(ArrayList<BufferedImage> images) {
        int height = (int) (images.get(0).getHeight()
                * (getConfigAs(DoorbirdConfiguration.class).montageScaleFactor / 100.0));
        int width = (int) (images.get(0).getWidth()
                * (getConfigAs(DoorbirdConfiguration.class).montageScaleFactor / 100.0));
        int widthTotal = width * images.size();
        logger.debug("Dimensions of final montage image: w={}, h={}", widthTotal, height);

        // Create concatenated image
        int currentWidth = 0;
        BufferedImage concatImage = new BufferedImage(widthTotal, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = concatImage.createGraphics();
        logger.debug("Concatenating images array into single image");
        for (int j = 0; j < images.size(); j++) {
            g2d.drawImage(images.get(j), currentWidth, 0, width, height, null);
            currentWidth += width;
        }
        g2d.dispose();

        // Convert image to a state
        logger.debug("Rendering image to byte array and converting to RawType state");
        byte[] imageBytes = convertImageToByteArray(concatImage);
        State state = null;
        if (imageBytes != null) {
            state = new RawType(imageBytes, "image/png");
        }
        return state;
    }

    private byte @Nullable [] convertImageToByteArray(BufferedImage image) {
        byte[] data = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            data = out.toByteArray();
            out.close();
        } catch (IOException ioe) {
            logger.debug("IOException occurred converting image to byte array", ioe);
        }
        return data;
    }

    private void updateImageAndTimestamp() {
        DoorbirdImage dbImage = downloadImage(buildUrl("/bha-api/image.cgi"));
        if (dbImage != null) {
            RawType image = dbImage.getImage();
            updateState(CHANNEL_IMAGE, image != null ? image : UnDefType.UNDEF);
            updateState(CHANNEL_IMAGE_TIMESTAMP, getLocalDateTimeType(dbImage.getTimestamp()));
        }
    }

    private String buildUrl(String path) {
        return buildUrl(path, null);
    }

    private String buildUrl(String path, @Nullable String parameters) {
        String url = "http://" + getDoorbirdHost() + path;
        if (parameters != null) {
            url = url + parameters;
        }
        return url;
    }

    private String executeGetRequest(String url) throws IOException {
        // @formatter:off
        return HttpRequestBuilder.getFrom(url)
            .withTimeout(Duration.ofSeconds(API_REQUEST_TIMEOUT))
            .withHeader("Authorization", "Basic " + authorization)
            .withHeader("charset", "utf-8")
            .withHeader("Accept-language", "en-us")
            .getContentAsString();
        // @formatter:on
    }

    private @Nullable synchronized DoorbirdImage downloadImage(String url) {
        logger.debug("Downloading image using url={}", url);
        Request request = httpClient.newRequest(url);
        request.method(HttpMethod.GET);
        request.header("Authorization", "Basic " + authorization);
        request.timeout(6, TimeUnit.SECONDS);

        String errorMsg;
        try {
            ContentResponse contentResponse = request.send();
            switch (contentResponse.getStatus()) {
                case HttpStatus.OK_200:
                    DoorbirdImage doorbirdImage = new DoorbirdImage();
                    doorbirdImage.setImage(new RawType(contentResponse.getContent(),
                            contentResponse.getHeaders().get(HttpHeader.CONTENT_TYPE)));
                    doorbirdImage.setTimestamp(convertXTimestamp(contentResponse.getHeaders().get("X-Timestamp")));
                    return doorbirdImage;

                default:
                    errorMsg = String.format("HTTP GET failed: %d, %s", contentResponse.getStatus(),
                            contentResponse.getReason());
                    break;
            }
        } catch (TimeoutException e) {
            errorMsg = "TimeoutException: Call to Doorbird API timed out";
        } catch (ExecutionException e) {
            errorMsg = String.format("ExecutionException: %s", e.getMessage());
        } catch (InterruptedException e) {
            errorMsg = String.format("InterruptedException: %s", e.getMessage());
        }
        logger.debug(errorMsg);
        return null;
    }

    private long convertXTimestamp(@Nullable String timestamp) {
        // Convert Unix Epoch string timestamp to long value
        // Use current time if passed null string
        long value = ZonedDateTime.now().toEpochSecond();
        if (timestamp != null) {
            try {
                value = Integer.parseInt(timestamp);
            } catch (NumberFormatException e) {
                logger.debug("X-Timestamp header is not a number: {}", timestamp);
            }
        }
        return value;
    }

    private DateTimeType getLocalDateTimeType(long dateTimeSeconds) {
        Instant instant = Instant.ofEpochMilli(dateTimeSeconds * 1000);
        ZonedDateTime localDateTime = instant.atZone(timeZoneProvider.getTimeZone());
        DateTimeType dateTimeType = new DateTimeType(localDateTime);
        return dateTimeType;
    }
}
