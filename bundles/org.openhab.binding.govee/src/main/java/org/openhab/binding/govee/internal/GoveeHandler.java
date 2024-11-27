/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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

import static org.openhab.binding.govee.internal.GoveeBindingConstants.*;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.govee.internal.model.Color;
import org.openhab.binding.govee.internal.model.ColorData;
import org.openhab.binding.govee.internal.model.EmptyValueQueryStatusData;
import org.openhab.binding.govee.internal.model.GenericGoveeData;
import org.openhab.binding.govee.internal.model.GenericGoveeMsg;
import org.openhab.binding.govee.internal.model.GenericGoveeRequest;
import org.openhab.binding.govee.internal.model.StatusResponse;
import org.openhab.binding.govee.internal.model.ValueIntData;
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
import com.google.gson.JsonSyntaxException;

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
 * as status updates. Therefore, when scanning new devices that job that listens to status devices must
 * be stopped while scanning new devices. Otherwise, the status job will receive the scan discover UDB packages.
 *
 * Controlling the lights is done via the Govee LAN API (cloud is not supported):
 * https://app-h5.govee.com/user-manual/wlan-guide
 *
 * @author Stefan Höhn - Initial contribution
 */
@NonNullByDefault
public class GoveeHandler extends BaseThingHandler {

    /*
     * Messages to be sent to the Govee devices
     */
    private static final Gson GSON = new Gson();

    private final Logger logger = LoggerFactory.getLogger(GoveeHandler.class);
    protected ScheduledExecutorService executorService = scheduler;
    @Nullable
    private ScheduledFuture<?> triggerStatusJob; // send device status update job
    private GoveeConfiguration goveeConfiguration = new GoveeConfiguration();

    private CommunicationManager communicationManager;

    private HSBType lastColor = new HSBType();
    private int lastKelvin = COLOR_TEMPERATURE_MIN_VALUE.intValue();

    /**
     * This thing related job <i>thingRefreshSender</i> triggers an update to the Govee device.
     * The device sends it back to the common port and the response is
     * then received by the common #refreshStatusReceiver
     */
    private final Runnable thingRefreshSender = () -> {
        try {
            triggerDeviceStatusRefresh();
            updateStatus(ThingStatus.ONLINE);
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/offline.communication-error.could-not-query-device [\"" + goveeConfiguration.hostname
                            + "\"]");
        }
    };

    public GoveeHandler(Thing thing, CommunicationManager communicationManager) {
        super(thing);
        this.communicationManager = communicationManager;
    }

    public String getHostname() {
        return goveeConfiguration.hostname;
    }

    @Override
    public void initialize() {
        goveeConfiguration = getConfigAs(GoveeConfiguration.class);

        final String ipAddress = goveeConfiguration.hostname;
        if (ipAddress.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "@text/offline.configuration-error.ip-address.missing");
            return;
        }
        updateStatus(ThingStatus.UNKNOWN);
        communicationManager.registerHandler(this);
        if (triggerStatusJob == null) {
            logger.debug("Starting refresh trigger job for thing {} ", thing.getLabel());

            triggerStatusJob = executorService.scheduleWithFixedDelay(thingRefreshSender, 100,
                    goveeConfiguration.refreshInterval * 1000L, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        ScheduledFuture<?> triggerStatusJobFuture = triggerStatusJob;
        if (triggerStatusJobFuture != null) {
            triggerStatusJobFuture.cancel(true);
            triggerStatusJob = null;
        }
        communicationManager.unregisterHandler(this);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        try {
            logger.debug("handleCommand({}, {})", channelUID, command);
            if (command instanceof RefreshType) {
                // we are refreshing all channels at once, as we get all information at the same time
                triggerDeviceStatusRefresh();
            } else {
                switch (channelUID.getId()) {
                    case CHANNEL_COLOR:
                        Command doCommand = command;
                        if (doCommand instanceof HSBType hsb) {
                            sendColor(hsb);
                            doCommand = hsb.getBrightness(); // fall through
                        }
                        if (doCommand instanceof PercentType percent) {
                            sendBrightness(percent);
                            doCommand = OnOffType.from(percent.intValue() > 0); // fall through
                        }
                        if (doCommand instanceof OnOffType onOff) {
                            sendOnOff(onOff);
                        }
                        break;

                    case CHANNEL_COLOR_TEMPERATURE:
                        if (command instanceof PercentType percent) {
                            sendKelvin(percentToKelvin(percent));
                        }
                        break;

                    case CHANNEL_COLOR_TEMPERATURE_ABS:
                        if (command instanceof QuantityType<?> genericQuantity) {
                            QuantityType<?> kelvin = genericQuantity.toInvertibleUnit(Units.KELVIN);
                            if (kelvin == null) {
                                logger.warn("handleCommand() invalid QuantityType:{}", genericQuantity);
                                break;
                            }
                            sendKelvin(kelvin.intValue());
                        }
                        break;
                }
            }
            updateStatus(ThingStatus.ONLINE);
        } catch (IOException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/offline.communication-error.could-not-query-device [\"" + goveeConfiguration.hostname
                            + "\"]");
        }
    }

    /**
     * Initiate a refresh to our thing device
     */
    private void triggerDeviceStatusRefresh() throws IOException {
        logger.debug("trigger Refresh Status of device {}", thing.getLabel());
        GenericGoveeData data = new EmptyValueQueryStatusData();
        GenericGoveeRequest request = new GenericGoveeRequest(new GenericGoveeMsg("devStatus", data));
        communicationManager.sendRequest(this, request);
    }

    /**
     * Send the normalized RGB color parameters.
     */
    public void sendColor(HSBType color) throws IOException {
        logger.debug("sendColor({})", color);
        int[] normalRGB = ColorUtil.hsbToRgb(new HSBType(color.getHue(), color.getSaturation(), PercentType.HUNDRED));
        GenericGoveeData data = new ColorData(new Color(normalRGB[0], normalRGB[1], normalRGB[2]), 0);
        GenericGoveeRequest request = new GenericGoveeRequest(new GenericGoveeMsg("colorwc", data));
        communicationManager.sendRequest(this, request);
        lastColor = color;
    }

    /**
     * Send the brightness parameter.
     */
    public void sendBrightness(PercentType brightness) throws IOException {
        logger.debug("sendBrightness({})", brightness);
        GenericGoveeData data = new ValueIntData(brightness.intValue());
        GenericGoveeRequest request = new GenericGoveeRequest(new GenericGoveeMsg("brightness", data));
        communicationManager.sendRequest(this, request);
        lastColor = new HSBType(lastColor.getHue(), lastColor.getSaturation(), brightness);
    }

    /**
     * Send the on-off parameter.
     */
    private void sendOnOff(OnOffType onOff) throws IOException {
        logger.debug("sendOnOff({})", onOff);
        GenericGoveeData data = new ValueIntData(onOff == OnOffType.ON ? 1 : 0);
        GenericGoveeRequest request = new GenericGoveeRequest(new GenericGoveeMsg("turn", data));
        communicationManager.sendRequest(this, request);
    }

    /**
     * Set the color temperature (Kelvin) parameter.
     */
    private void sendKelvin(int kelvin) throws IOException {
        logger.debug("sendKelvin({})", kelvin);
        GenericGoveeData data = new ColorData(new Color(0, 0, 0), kelvin);
        GenericGoveeRequest request = new GenericGoveeRequest(new GenericGoveeMsg("colorwc", data));
        communicationManager.sendRequest(this, request);
        lastKelvin = kelvin;
    }

    /**
     * Build an {@link HSBType} from the given normalized {@link Color} record, brightness, and on state.
     *
     * @param normalColor record containing the lamp's normalized RGB parameters (0..255)
     * @param brightness the lamp brightness in range 0..100
     * @param on the lamp state
     *
     * @return the HSB presentation state
     */
    private static HSBType buildHSB(Color normalColor, int brightness, boolean on) {
        int[] normalRGB = { normalColor.r(), normalColor.g(), normalColor.b() };
        HSBType normalHSB = ColorUtil.rgbToHsb(normalRGB);
        PercentType brightnessPercent = on ? new PercentType(brightness) : PercentType.ZERO;
        return new HSBType(normalHSB.getHue(), normalHSB.getSaturation(), brightnessPercent);
    }

    void handleIncomingStatus(String response) {
        if (response.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/offline.communication-error.empty-response");
            return;
        }

        try {
            StatusResponse statusMessage = GSON.fromJson(response, StatusResponse.class);
            if (statusMessage != null) {
                updateDeviceState(statusMessage);
            }
            updateStatus(ThingStatus.ONLINE);
        } catch (JsonSyntaxException jse) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, jse.getMessage());
        }
    }

    public void updateDeviceState(@Nullable StatusResponse message) {
        if (message == null) {
            return;
        }

        logger.trace("Receiving Device State");

        boolean on = message.msg().data().onOff() == 1;
        logger.trace("on:{}", on);

        int brightness = message.msg().data().brightness();
        logger.trace("brightness:{}", brightness);

        Color normalRGB = message.msg().data().color();
        logger.trace("normalRGB:{}", normalRGB);

        int kelvin = message.msg().data().colorTemInKelvin();
        logger.trace("kelvin:{}", kelvin);

        HSBType color = buildHSB(normalRGB, brightness, on);

        kelvin = Math.min(COLOR_TEMPERATURE_MAX_VALUE.intValue(),
                Math.max(COLOR_TEMPERATURE_MIN_VALUE.intValue(), kelvin));

        logger.trace("Comparing color old:{} to new:{}", lastColor, color);
        if (!color.equals(lastColor)) {
            logger.trace("Updating color old:{} to new:{}", lastColor, color);
            updateState(CHANNEL_COLOR, color);
            lastColor = color;
        }

        logger.trace("Comparing color temperature old:{} to new:{}", lastKelvin, kelvin);
        if (kelvin != lastKelvin) {
            logger.trace("Updating color temperature old:{} to new:{}", lastKelvin, kelvin);
            updateState(CHANNEL_COLOR_TEMPERATURE_ABS, QuantityType.valueOf(kelvin, Units.KELVIN));
            updateState(CHANNEL_COLOR_TEMPERATURE, kelvinToPercent(kelvin));
            lastKelvin = kelvin;
        }
    }

    /**
     * Convert PercentType to Kelvin.
     */
    private static int percentToKelvin(PercentType percent) {
        return (int) Math
                .round((((COLOR_TEMPERATURE_MAX_VALUE - COLOR_TEMPERATURE_MIN_VALUE) * percent.doubleValue() / 100.0)
                        + COLOR_TEMPERATURE_MIN_VALUE));
    }

    /**
     * Convert Kelvin to PercentType.
     */
    private static PercentType kelvinToPercent(int kelvin) {
        return new PercentType((int) Math.round((kelvin - COLOR_TEMPERATURE_MIN_VALUE) * 100.0
                / (COLOR_TEMPERATURE_MAX_VALUE - COLOR_TEMPERATURE_MIN_VALUE)));
    }
}
