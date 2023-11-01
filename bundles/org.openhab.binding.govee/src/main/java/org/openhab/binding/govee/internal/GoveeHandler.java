/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.govee.internal;

import static org.openhab.binding.govee.internal.GoveeBindingConstants.BRIGHTNESS;
import static org.openhab.binding.govee.internal.GoveeBindingConstants.COLOR;
import static org.openhab.binding.govee.internal.GoveeBindingConstants.COLOR_TEMPERATURE;
import static org.openhab.binding.govee.internal.GoveeBindingConstants.COLOR_TEMPERATURE_ABS;
import static org.openhab.binding.govee.internal.GoveeBindingConstants.COLOR_TEMPERATURE_MAX_VALUE;
import static org.openhab.binding.govee.internal.GoveeBindingConstants.COLOR_TEMPERATURE_MIN_VALUE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.govee.internal.model.Color;
import org.openhab.binding.govee.internal.model.ColorData;
import org.openhab.binding.govee.internal.model.EmptyValueQueryStatusData;
import org.openhab.binding.govee.internal.model.GenericGoveeMsg;
import org.openhab.binding.govee.internal.model.GenericGoveeRequest;
import org.openhab.binding.govee.internal.model.StatusResponse;
import org.openhab.binding.govee.internal.model.ValueIntData;
import org.openhab.core.common.ThreadPoolManager;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link GoveeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * Any device has its own job that triggers a refresh of retrieving the external state from the device.
 * However, there must be only one job that listens for all devices in a singleton thread because
 * all devices send their udp packet response to the same port on openHAB. Based on the sender IP address
 * of the device we can detect to which thing the status answer needs to be assigned to and updated.
 *
 * <ul>
 * <li>The job per thing that triggers a new update is called <i>triggerStatusJob</i>. There are as many instances
 * as things.</li>
 * <li>The job that receives the answers and applies that to the respective thing is called <i>refreshStatusJob</i> and
 * there is only one for all instances. It may be stopped and restarted by the DiscoveryService (see below).</li>
 * </ul>
 *
 * The other topic that needs to be managed is that device discovery responses are also sent to openHAB at the same port
 * like status updates. Therefore, when scanning new devices that job that listens to status devices must
 * be stopped while scanning new devices. Otherwise, the status job will receive the scan discover UDB packages.
 *
 * @author Stefan Höhn - Initial contribution
 */
@NonNullByDefault
public class GoveeHandler extends BaseThingHandler {

    /*
     * Messages to be sent to the Govee Devices
     */
    private static final Gson GSON = new Gson();

    // Holds a list of all thing handlers to send them thing updates via the receiver-Thread
    public static final Map<String, GoveeHandler> THING_HANDLERS = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(GoveeHandler.class);
    private static final int SENDTODEVICE_PORT = 4003;
    public static final int RECEIVEFROMDEVICE_PORT = 4002;

    // Semaphores to suppress further processing if already running
    public static boolean refreshJobRunning = false;
    private static boolean refreshRunning = false;

    @Nullable
    private static ScheduledFuture<?> refreshStatusJob; // device response receiver job
    @Nullable
    private ScheduledFuture<?> triggerStatusJob; // send device status update job
    private GoveeConfiguration goveeConfiguration = new GoveeConfiguration();
    private int lastBrightness;

    /*
     * Common Receiver job for the status answers of the devices
     */
    public static boolean isRefreshJobRunning() {
        return refreshJobRunning && THING_HANDLERS.isEmpty();
    }

    /**
     * This thing related job <i>thingRefreshSender</i> triggers an update to the Govee device.
     * The device sends it back to the common port and the response is
     * then received by the common #refreshStatusReceiver
     */
    private final Runnable thingRefreshSender = () -> {
        try {
            triggerDeviceStatusRefresh();
            if (!thing.getStatus().equals(ThingStatus.ONLINE)) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/offline.communication-error.could-not-query-device [\"" + goveeConfiguration.hostname
                            + "\"]");
        }
    };

    public GoveeHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        goveeConfiguration = getConfigAs(GoveeConfiguration.class);

        final String ipAddress = goveeConfiguration.hostname;
        if (!ipAddress.isEmpty()) {
            THING_HANDLERS.put(ipAddress, this);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.configuration-error.ip-address.missing");
            return;
        }
        if (!THING_HANDLERS.isEmpty()) {
            startRefreshStatusJob();
        }

        updateStatus(ThingStatus.UNKNOWN);
        if (triggerStatusJob == null) {
            logger.debug("Starting refresh trigger job for thing {} ", thing.getLabel());

            triggerStatusJob = scheduler.scheduleWithFixedDelay(thingRefreshSender, 100,
                    goveeConfiguration.refreshInterval * 1000L, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stop the Refresh Status Job, so the same socket can be used for something else (like discovery)
     */
    public static void stopRefreshStatusJob() {
        if (refreshStatusJob != null) {
            refreshStatusJob.cancel(true);
        }
        refreshStatusJob = null;
        refreshJobRunning = false;
    }

    /**
     * (re)start the refresh status job
     */
    public static synchronized void startRefreshStatusJob() {
        if (refreshStatusJob == null) {
            refreshStatusJob = ThreadPoolManager.getScheduledPool("goveeThingHandler")
                    .scheduleWithFixedDelay(new RefreshStatusReceiver(), 100, 1000, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (triggerStatusJob != null) {
            triggerStatusJob.cancel(true);
            triggerStatusJob = null;
        }
        if (!goveeConfiguration.hostname.isEmpty()) {
            THING_HANDLERS.remove(goveeConfiguration.hostname);
        }

        if (THING_HANDLERS.isEmpty()) {
            stopRefreshStatusJob();
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        try {
            if (command instanceof RefreshType) {
                // we are refreshing all channels at once, as we get all information at the same time
                triggerDeviceStatusRefresh();
            } else {
                switch (channelUID.getId()) {
                    case COLOR:
                        if (command instanceof HSBType hsbCommand) {
                            int[] rgb = ColorUtil.hsbToRgb(hsbCommand);
                            GenericGoveeRequest lightColor = new GenericGoveeRequest(new GenericGoveeMsg("colorwc",
                                    new ColorData(new Color(rgb[0], rgb[1], rgb[2]), 0)));
                            send(GSON.toJson(lightColor));
                        }
                        break;
                    case COLOR_TEMPERATURE:
                        if (command instanceof PercentType percent) {
                            Double brightness = COLOR_TEMPERATURE_MIN_VALUE + percent.floatValue()
                                    * (COLOR_TEMPERATURE_MAX_VALUE - COLOR_TEMPERATURE_MIN_VALUE) / 100.0;
                            GenericGoveeRequest lightColor = new GenericGoveeRequest(new GenericGoveeMsg("colorwc",
                                    new ColorData(new Color(0, 0, 0), brightness.intValue())));
                            send(GSON.toJson(lightColor));
                        }
                        break;
                    case COLOR_TEMPERATURE_ABS:
                        if (command instanceof QuantityType quantity) {
                            GenericGoveeRequest lightColor = new GenericGoveeRequest(new GenericGoveeMsg("colorwc",
                                    new ColorData(new Color(0, 0, 0), quantity.intValue())));
                            send(GSON.toJson(lightColor));
                        }
                        break;
                    case BRIGHTNESS:
                        if (command instanceof PercentType percent) {
                            GenericGoveeRequest lightBrightness = new GenericGoveeRequest(
                                    new GenericGoveeMsg("brightness", new ValueIntData(percent.intValue())));
                            send(GSON.toJson(lightBrightness));
                        } else if (command instanceof OnOffType) {
                            if (command.equals(OnOffType.ON)) {
                                GenericGoveeRequest lightOn = new GenericGoveeRequest(
                                        new GenericGoveeMsg("turn", new ValueIntData(1)));
                                send(GSON.toJson(lightOn));
                            } else {
                                GenericGoveeRequest lightOff = new GenericGoveeRequest(
                                        new GenericGoveeMsg("turn", new ValueIntData(0)));
                                send(GSON.toJson(lightOff));
                            }
                        }
                        break;
                }
            }
            if (!thing.getStatus().equals(ThingStatus.ONLINE)) {
                updateStatus(ThingStatus.ONLINE);
            }
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/offline.communication-error.could-not-query-device [\"" + goveeConfiguration.hostname
                            + "\"]");
        }
    }

    /**
     * Initiate a refresh to our thing devicee
     *
     */
    private void triggerDeviceStatusRefresh() throws IOException {
        if (refreshRunning) {
            return;
        }
        if (GoveeDiscoveryService.isDiscoveryActive()) {
            logger.debug("Not triggering refresh as Scan is currently active");
            return;
        }
        refreshRunning = true;

        logger.debug("trigger Refresh Status of device {}", thing.getLabel());

        try {
            GenericGoveeRequest lightQuery = new GenericGoveeRequest(
                    new GenericGoveeMsg("devStatus", new EmptyValueQueryStatusData()));
            send(GSON.toJson(lightQuery));
        } finally {
            refreshRunning = false;
        }
    }

    public void send(String message) throws IOException {
        DatagramSocket socket;
        socket = new DatagramSocket();
        socket.setReuseAddress(true);
        byte[] data = message.getBytes();

        InetAddress address = InetAddress.getByName(goveeConfiguration.hostname);
        logger.trace("Sending {} to {}", message, goveeConfiguration.hostname);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, SENDTODEVICE_PORT);
        socket.send(packet);
        socket.close();
    }

    public void updateDeviceState(@Nullable StatusResponse message) {
        if (message == null) {
            return;
        }

        int lastOnOff = message.msg().data().onOff();
        lastBrightness = message.msg().data().brightness();
        Color lastColor = message.msg().data().color();
        int lastColorTemperature = message.msg().data().colorTemInKelvin();

        updateState(COLOR, ColorUtil.rgbToHsb(new int[] { lastColor.r(), lastColor.g(), lastColor.b() }));
        updateState(COLOR_TEMPERATURE_ABS, new QuantityType(lastColorTemperature, Units.KELVIN));
        logger.debug("setting BRIGHTNESS to ONOFF {}", OnOffType.from(lastOnOff == 1));
        updateState(BRIGHTNESS, OnOffType.from(lastOnOff == 1));
        if (lastOnOff == 1) {
            logger.debug("setting BRIGHTNESS to PercentType {}", new PercentType(lastBrightness));
            updateState(BRIGHTNESS, new PercentType(lastBrightness));
        } else {
            logger.debug("not updating BRIGHTNESS percentage as device is OFF (would turn channel switch on)");
        }
    }

    public void statusUpdate(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        updateStatus(status, statusDetail, description);
    }

    public void statusUpdate(ThingStatus status) {
        updateStatus(status);
    }
}
