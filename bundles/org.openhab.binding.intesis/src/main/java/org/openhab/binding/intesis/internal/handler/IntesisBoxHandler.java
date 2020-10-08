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
package org.openhab.binding.intesis.internal.handler;

import static org.openhab.binding.intesis.internal.IntesisBindingConstants.*;
import static org.openhab.binding.intesis.internal.api.IntesisBoxMessage.*;
import static org.openhab.core.thing.Thing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.intesis.internal.IntesisConfiguration;
import org.openhab.binding.intesis.internal.IntesisDynamicStateDescriptionProvider;
import org.openhab.binding.intesis.internal.api.IntesisBoxChangeListener;
import org.openhab.binding.intesis.internal.api.IntesisBoxIdentity;
import org.openhab.binding.intesis.internal.api.IntesisBoxMessage;
import org.openhab.binding.intesis.internal.api.IntesisBoxSocketApi;
import org.openhab.binding.intesis.internal.enums.IntesisBoxFunctionEnum;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelKind;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.StateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link IntesisBoxHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Cody Cutrer - Initial contribution
 * @author Rocky Amatulli - additions to include id message handling, dynamic channel options based on limits.
 * @author Hans-Jörg Merk - refactored for openHAB 3.0 compatibility
 *
 */
@NonNullByDefault
public class IntesisBoxHandler extends BaseThingHandler implements IntesisBoxChangeListener {

    private final Logger logger = LoggerFactory.getLogger(IntesisBoxHandler.class);
    private @Nullable IntesisBoxSocketApi intesisBoxSocketApi;

    private final Map<String, String> properties = new HashMap<>();
    private final Map<String, List<String>> limits = new HashMap<String, List<String>>();

    private final IntesisDynamicStateDescriptionProvider intesisStateDescriptionProvider;

    private IntesisConfiguration config = new IntesisConfiguration();

    private double minTemp = 0.0d, maxTemp = 0.0d;

    private @Nullable ScheduledFuture<?> pollingTask;

    public IntesisBoxHandler(Thing thing, IntesisDynamicStateDescriptionProvider intesisStateDescriptionProvider) {
        super(thing);
        this.intesisStateDescriptionProvider = intesisStateDescriptionProvider;
    }

    @Override
    public void initialize() {
        config = getConfigAs(IntesisConfiguration.class);

        if (!config.ipAddress.isEmpty()) {
            intesisBoxSocketApi = new IntesisBoxSocketApi(config.ipAddress, config.port);
            intesisBoxSocketApi.addIntesisBoxChangeListener(this);

            updateStatus(ThingStatus.UNKNOWN);
            scheduler.submit(() -> {
                try {
                    intesisBoxSocketApi.openConnection();
                    intesisBoxSocketApi.sendId();
                    intesisBoxSocketApi.sendLimitsQuery();
                    intesisBoxSocketApi.sendAlive();
                } catch (IOException e) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
                }
                pollingTask = scheduler.scheduleWithFixedDelay(this::polling, 3, 45, TimeUnit.SECONDS);
                updateStatus(ThingStatus.ONLINE);
            });
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "No IP address specified)");
        }
    }

    @Override
    public void dispose() {
        final ScheduledFuture<?> pollingTask = this.pollingTask;

        IntesisBoxSocketApi api = this.intesisBoxSocketApi;

        if (pollingTask != null) {
            pollingTask.cancel(true);
            this.pollingTask = null;
        }
        if (api != null) {
            api.closeConnection();
        }
        super.dispose();
    }

    private synchronized void polling() {
        IntesisBoxSocketApi api = this.intesisBoxSocketApi;
        if (api != null) {
            if (!api.isConnected()) {
                try {
                    api.openConnection();
                } catch (IOException e) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
                }
            }
            api.sendAlive();
            ;
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        IntesisBoxSocketApi api = this.intesisBoxSocketApi;
        if (api != null) {
            if (!api.isConnected()) {
                logger.trace("Sending command failed, not connected");
                return;
            }
            if (command instanceof RefreshType) {
                logger.warn("Refresh channel {}", channelUID.getId());
                api.sendQuery(channelUID.getId());
                return;
            }
        }
        String value = "";
        switch (channelUID.getId()) {
            case CHANNEL_TYPE_POWER:
                if (command instanceof OnOffType) {
                    value = ((OnOffType) command == OnOffType.ON) ? "ON" : "OFF";
                }
                break;
            case CHANNEL_TYPE_TARGETTEMP:
                if (command instanceof QuantityType) {
                    QuantityType<?> celsiusTemperature = (QuantityType<?>) command;
                    celsiusTemperature = celsiusTemperature.toUnit(SIUnits.CELSIUS);
                    if (celsiusTemperature != null) {
                        double doubleValue = celsiusTemperature.doubleValue();
                        if (doubleValue < minTemp) {
                            doubleValue = minTemp;
                        } else if (doubleValue > maxTemp) {
                            doubleValue = maxTemp;
                        }
                        value = String.valueOf((int) Math.round(doubleValue * 10));
                    }
                }
                break;
            case CHANNEL_TYPE_MODE:
            case CHANNEL_TYPE_FANSPEED:
            case CHANNEL_TYPE_VANESUD:
            case CHANNEL_TYPE_VANESLR:
                value = command.toString();
                break;
        }
        if (!value.isEmpty()) {
            String function = IntesisBoxFunctionEnum.valueOf(channelUID.getId()).getFunction();
            if (api != null) {
                logger.trace("Sending command {} to function {}", value, function);
                api.sendCommand(function, value);
            } else {
                logger.trace("Sending command failed, could not get API");
            }
        }
    }

    private void receivedId(String function, String value) {
        logger.trace("receivedID(): {} {}", function, value);
        properties.put(PROPERTY_VENDOR, "Intesis");
        switch (function) {
            case "MODEL":
                properties.put(PROPERTY_MODEL_ID, value);
                break;
            case "MAC":
                properties.put(PROPERTY_MAC_ADDRESS, value);
                break;
            case "IP":
                properties.put("ipAddress", value);
            case "PROTOCOL":
                properties.put("protocol", value);
                break;
            case "VERSION":
                properties.put(PROPERTY_FIRMWARE_VERSION, value);
                break;
            case "RSSI":
                properties.put("rssi", value);
                break;
            case "NAME":
                properties.put("hostname", value);
                break;
        }
    }

    private void receivedUpdate(String function, String value) {
        logger.trace("receivedUpdate(): {} {}", function, value);
        switch (function) {
            case "ONOFF":
                updateState(CHANNEL_TYPE_POWER, OnOffType.from(value));
                break;

            case "SETPTEMP":
                if (value.equals("32768")) {
                    value = "0";
                }
                updateState(CHANNEL_TYPE_TARGETTEMP,
                        new QuantityType<Temperature>(Double.valueOf(value) / 10.0d, SIUnits.CELSIUS));
                break;
            case "AMBTEMP":
                if (Double.valueOf(value).isNaN()) {
                    value = "0";
                }
                updateState(CHANNEL_TYPE_AMBIENTTEMP,
                        new QuantityType<Temperature>(Double.valueOf(value) / 10.0d, SIUnits.CELSIUS));
                break;
            case "MODE":
                updateState(CHANNEL_TYPE_MODE, new StringType(value));
                break;
            case "FANSP":
                updateState(CHANNEL_TYPE_FANSPEED, new StringType(value));
                break;
            case "VANEUD":
                updateState(CHANNEL_TYPE_VANESUD, new StringType(value));
                break;
            case "VANELR":
                updateState(CHANNEL_TYPE_VANESLR, new StringType(value));
                break;
            case "ERRCODE":
                properties.put("errorCode", value);
                updateProperties(properties);
                break;
            case "ERRSTATUS":
                properties.put("errorStatus", value);
                updateProperties(properties);
                if ("ERR".equals(value)) {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "device reported an error");
                }
                break;
        }
    }

    @SuppressWarnings("null")
    private void handleMessage(String data) {
        logger.trace("handleMessage(): Message received - {}", data);
        if (data.equals("ACK") || data.equals("")) {
            return;
        }
        if (data.startsWith(ID + ':')) {
            synchronized (this) {
                new IntesisBoxIdentity(data).value.forEach((name, value) -> receivedId(name, value));
                return;
            }
        }
        IntesisBoxMessage message = IntesisBoxMessage.parse(data);
        switch (message.getCommand()) {
            case LIMITS:
                logger.trace("handleMessage(): Limits received - {}", data);
                synchronized (this) {
                    String function = message.getFunction();
                    if (function.equals("SETPTEMP")) {
                        List<Double> limits = message.getLimitsValue().stream().map(l -> Double.valueOf(l) / 10.0d)
                                .collect(Collectors.toList());
                        if (limits.size() == 2) {
                            minTemp = limits.get(0);
                            maxTemp = limits.get(1);
                        }
                        logger.trace("Property target temperatures {} added", message.getValue());
                        properties.put("targetTemperature limits", "[" + minTemp + "," + maxTemp + "]");
                        addChannel(CHANNEL_TYPE_TARGETTEMP, "Number:Temperature");
                    } else {
                        switch (function) {
                            case "MODE":
                                properties.put("supported modes", message.getValue());
                                limits.put(CHANNEL_TYPE_MODE, message.getLimitsValue());
                                addChannel(CHANNEL_TYPE_MODE, "String");
                                break;
                            case "FANSP":
                                properties.put("supported fan levels", message.getValue());
                                limits.put(CHANNEL_TYPE_FANSPEED, message.getLimitsValue());
                                addChannel(CHANNEL_TYPE_FANSPEED, "String");
                                break;
                            case "VANEUD":
                                properties.put("supported vane up/down modes", message.getValue());
                                limits.put(CHANNEL_TYPE_VANESUD, message.getLimitsValue());
                                addChannel(CHANNEL_TYPE_VANESUD, "String");
                                break;
                            case "VANELR":
                                properties.put("supported vane left/right modes", message.getValue());
                                limits.put(CHANNEL_TYPE_VANESLR, message.getLimitsValue());
                                addChannel(CHANNEL_TYPE_VANESLR, "String");
                                break;
                        }
                    }
                    addChannel(CHANNEL_TYPE_AMBIENTTEMP, "Number:Temperature");

                }
                updateProperties(properties);
                break;
            case CHN:
                receivedUpdate(message.getFunction(), message.getValue());
                break;
        }
    }

    public void addChannel(String channelId, String itemType) {
        if (thing.getChannel(channelId) == null) {
            logger.trace("Channel '{}' for UID to be added", channelId);
            ThingBuilder thingBuilder = editThing();
            final ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, channelId);
            Channel channel = ChannelBuilder.create(new ChannelUID(getThing().getUID(), channelId), itemType)
                    .withType(channelTypeUID).withKind(ChannelKind.STATE).build();
            thingBuilder.withChannel(channel);
            updateThing(thingBuilder.build());

            if (limits.containsKey(channelId)) {
                List<StateOption> options = new ArrayList<>();
                for (String mode : limits.get(channelId)) {
                    options.add(new StateOption(mode,
                            (mode.substring(0, 1).toUpperCase() + mode.substring(1).toLowerCase())));
                }
                intesisStateDescriptionProvider.setStateOptions(new ChannelUID(getThing().getUID(), channelId),
                        options);
            }
        }
    }

    @Override
    public void messageReceived(String messageLine) {
        logger.trace("messageReceived() : {}", messageLine);
        handleMessage(messageLine);
    }

    @Override
    public void connectionStatusChanged(ThingStatus status) {
        this.updateStatus(status);
    }
}
