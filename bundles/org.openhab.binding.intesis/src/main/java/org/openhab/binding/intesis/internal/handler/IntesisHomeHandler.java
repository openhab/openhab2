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

import static org.eclipse.smarthome.core.thing.Thing.*;
import static org.openhab.binding.intesis.internal.IntesisBindingConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.StateOption;
import org.openhab.binding.intesis.internal.IntesisConfiguration;
import org.openhab.binding.intesis.internal.IntesisDynamicStateDescriptionProvider;
import org.openhab.binding.intesis.internal.IntesisHomeModeEnum;
import org.openhab.binding.intesis.internal.api.IntesisHomeHttpApi;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Data;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Datapoints;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Descr;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Dp;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Dpval;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Id;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Info;
import org.openhab.binding.intesis.internal.gson.IntesisHomeJSonDTO.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * The {@link IntesisHomeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Hans-Jörg Merk - Initial contribution
 */
@NonNullByDefault
public class IntesisHomeHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(IntesisHomeHandler.class);
    private final IntesisHomeHttpApi api;
    private IntesisConfiguration config = new IntesisConfiguration();

    private final Map<String, String> properties = new HashMap<>();

    private IntesisDynamicStateDescriptionProvider intesisStateDescriptionProvider;

    private @Nullable ScheduledFuture<?> refreshJob;

    private String ipAddress = "";
    private String password = "";

    final Gson gson = new Gson();

    public IntesisHomeHandler(final Thing thing, final HttpClient httpClient,
            IntesisDynamicStateDescriptionProvider intesisStateDescriptionProvider) {
        super(thing);
        this.api = new IntesisHomeHttpApi(config, httpClient);
        this.intesisStateDescriptionProvider = intesisStateDescriptionProvider;
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);
        final IntesisConfiguration config = getConfigAs(IntesisConfiguration.class);
        ipAddress = config.ipAddress;
        password = config.password;
        if (ipAddress.isEmpty() || password.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR);
        }

        // start background initialization:
        scheduler.submit(() -> {
            populateProperties();
            // query available dataPoints and build dynamic channels
            postRequest(sessionId -> "{\"command\":\"getavailabledatapoints\",\"data\":{\"sessionID\":\"" + sessionId
                    + "\"}}", this::handleDataPointsResponse);
            updateProperties(properties);

        });
    }

    @Override
    public void dispose() {
        logger.debug("IntesisHomeHandler disposed.");
        final ScheduledFuture<?> refreshJob = this.refreshJob;

        if (refreshJob != null) {
            refreshJob.cancel(true);
            this.refreshJob = null;
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        int uid = 0;
        int value = 0;
        String channelId = channelUID.getId();
        if (command instanceof RefreshType) {
            // Refresh command is not supported as the binding polls all values every 30 seconds
        } else {
            switch (channelId) {
                case CHANNEL_TYPE_POWER:
                    uid = 1;
                    value = command.equals(OnOffType.OFF) ? 0 : 1;
                    break;
                case CHANNEL_TYPE_MODE:
                    uid = 2;
                    value = IntesisHomeModeEnum.valueOf(command.toString()).getMode();
                    break;
                case CHANNEL_TYPE_FANSPEED:
                    uid = 4;
                    if (("AUTO").equals(command.toString())) {
                        value = 0;
                    } else {
                        value = Integer.parseInt(command.toString());
                    }
                    break;
                case CHANNEL_TYPE_VANESUD:
                case CHANNEL_TYPE_VANESLR:
                    switch (command.toString()) {
                        case "AUTO":
                            value = 0;
                            break;
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "9":
                            value = Integer.parseInt(command.toString());
                            break;
                        case "SWING":
                            value = 10;
                            break;
                        case "SWIRL":
                            value = 11;
                            break;
                        case "WIDE":
                            value = 12;
                            break;
                    }
                    switch (channelId) {
                        case CHANNEL_TYPE_VANESUD:
                            uid = 5;
                            break;
                        case CHANNEL_TYPE_VANESLR:
                            uid = 6;
                            break;
                    }
                    break;
                case CHANNEL_TYPE_TARGETTEMP:
                    uid = 9;
                    if (command instanceof QuantityType) {
                        QuantityType<?> newVal = (QuantityType<?>) command;
                        newVal = newVal.toUnit(SIUnits.CELSIUS);
                        if (newVal != null) {
                            value = newVal.intValue() * 10;
                        }
                    }
                    break;
            }
        }
        if (uid != 0) {
            final int uId = uid;
            final int newValue = value;
            scheduler.submit(() -> {
                login();
                String sessionId = login();
                if (sessionId != null) {
                    String contentString = "{\"command\":\"setdatapointvalue\",\"data\":{\"sessionID\":\"" + sessionId
                            + "\", \"uid\":" + uId + ",\"value\":" + newValue + "}}";
                    String response = api.postRequest(ipAddress, contentString);
                    if (response != null) {
                        try {
                            Response resp = gson.fromJson(response, Response.class);
                            boolean success = resp.success;
                            if (!success) {
                                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                            } else {
                                logout(sessionId);
                                updateStatus(ThingStatus.ONLINE);
                            }
                        } catch (JsonSyntaxException e) {
                            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                        }
                    }
                }

            });
        }
    }

    public @Nullable String login() {
        String contentString = "{\"command\":\"login\",\"data\":{\"username\":\"Admin\",\"password\":\"" + password
                + "\"}}";
        String response = api.postRequest(ipAddress, contentString);
        String sessionId = null;
        if (response != null) {
            try {
                Response resp = gson.fromJson(response, Response.class);
                boolean success = resp.success;
                if (success) {
                    Data data = gson.fromJson(resp.data, Data.class);
                    Id id = gson.fromJson(data.id, Id.class);
                    sessionId = id.sessionID.toString();
                    if (sessionId != null && !sessionId.isEmpty()) {
                        updateStatus(ThingStatus.ONLINE);
                        return sessionId;
                    } else {
                        updateStatus(ThingStatus.OFFLINE);
                    }
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                    return null;
                }
            } catch (JsonSyntaxException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
            return null;
        }
        return sessionId;
    }

    public @Nullable String logout(String sessionId) {
        String contentString = "{\"command\":\"logout\",\"data\":{\"sessionID\":\"" + sessionId + "\"}}";
        String response = api.postRequest(ipAddress, contentString);
        return response;
    }

    public void populateProperties() {
        String contentString = "{\"command\":\"getinfo\",\"data\":\"\"}";
        String response = api.postRequest(ipAddress, contentString);
        logger.trace("getInfo response : {}", response);
        if (response != null) {
            try {
                Response resp = gson.fromJson(response, Response.class);
                boolean success = resp.success;
                if (success) {
                    Data data = gson.fromJson(resp.data, Data.class);
                    Info info = gson.fromJson(data.info, Info.class);
                    properties.put(PROPERTY_VENDOR, "Intesis");
                    properties.put(PROPERTY_MODEL_ID, info.deviceModel);
                    properties.put(PROPERTY_SERIAL_NUMBER, info.sn);
                    properties.put(PROPERTY_FIRMWARE_VERSION, info.fwVersion);
                    properties.put(PROPERTY_MAC_ADDRESS, info.wlanSTAMAC);
                    updateStatus(ThingStatus.ONLINE);
                }
            } catch (JsonSyntaxException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
            }
        }
    }

    public void addChannel(String channelId, String itemType, @Nullable final Collection<String> options) {
        if (thing.getChannel(channelId) == null) {
            logger.trace("Channel '{}' for UID to be added", channelId);
            ThingBuilder thingBuilder = editThing();
            final ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, channelId);
            Channel channel = ChannelBuilder.create(new ChannelUID(getThing().getUID(), channelId), itemType)
                    .withType(channelTypeUID).withKind(ChannelKind.STATE).build();
            thingBuilder.withChannel(channel);
            updateThing(thingBuilder.build());

            if (options != null) {
                final List<StateOption> stateOptions = options.stream()
                        .map(e -> new StateOption(e, e.substring(0, 1) + e.toString().substring(1).toLowerCase()))
                        .collect(Collectors.toList());
                logger.trace("StateOptions : '{}'", stateOptions);
                intesisStateDescriptionProvider.setStateOptions(channel.getUID(), stateOptions);
            }
        }
    }

    private void postRequest(UnaryOperator<String> requestFactory, Consumer<Response> handler) {
        String sessionId = login();
        if (sessionId != null) {
            try {
                String request = requestFactory.apply(sessionId);
                logger.trace("request : '{}'", request);

                String response = api.postRequest(ipAddress, request);
                if (response != null) {

                    Response resp = gson.fromJson(response, Response.class);
                    boolean success = resp.success;
                    if (success) {
                        handler.accept(resp);
                    } else {
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                                "Request unsuccessful");
                    }
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "No Response");
                }
            } catch (JsonSyntaxException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            } finally {
                logout(sessionId);
            }
        }
    }

    private void handleDataPointsResponse(Response response) {
        try {
            Data data = gson.fromJson(response.data, Data.class);
            Dp dp = gson.fromJson(data.dp, Dp.class);
            Datapoints[] datapoints = gson.fromJson(dp.datapoints, Datapoints[].class);
            for (Datapoints datapoint : datapoints) {
                Descr descr = gson.fromJson(datapoint.descr, Descr.class);
                String channelId = "";
                String itemType = "String";
                switch (datapoint.uid) {
                    case 2:
                        List<String> opModes = new ArrayList<>();
                        for (String modString : descr.states) {
                            switch (modString) {
                                case "0":
                                    opModes.add("AUTO");
                                    break;
                                case "1":
                                    opModes.add("HEAT");
                                    break;
                                case "2":
                                    opModes.add("DRY");
                                    break;
                                case "3":
                                    opModes.add("FAN");
                                    break;
                                case "4":
                                    opModes.add("COOL");
                                    break;
                            }
                        }
                        properties.put("Supported modes", opModes.toString());
                        channelId = CHANNEL_TYPE_MODE;
                        addChannel(channelId, itemType, opModes);
                        break;
                    case 4:
                        List<String> fanLevels = new ArrayList<>();
                        for (String fanString : descr.states) {
                            if ("AUTO".contentEquals(fanString)) {
                                fanLevels.add("AUTO");
                            } else {
                                fanLevels.add(fanString);
                            }
                        }
                        properties.put("Supported fan levels", fanLevels.toString());
                        channelId = CHANNEL_TYPE_FANSPEED;
                        addChannel(channelId, itemType, fanLevels);
                        break;
                    case 5:
                    case 6:
                        List<String> swingModes = new ArrayList<>();
                        for (String swingString : descr.states) {
                            if ("AUTO".contentEquals(swingString)) {
                                swingModes.add("AUTO");
                            } else if ("10".contentEquals(swingString)) {
                                swingModes.add("SWING");
                            } else if ("11".contentEquals(swingString)) {
                                swingModes.add("SWIRL");
                            } else if ("12".contentEquals(swingString)) {
                                swingModes.add("WIDE");
                            } else {
                                swingModes.add(swingString);
                            }
                        }
                        switch (datapoint.uid) {
                            case 5:
                                channelId = CHANNEL_TYPE_VANESUD;
                                properties.put("Supported vane up/down modes", swingModes.toString());
                                addChannel(channelId, itemType, swingModes);
                                break;
                            case 6:
                                channelId = CHANNEL_TYPE_VANESLR;
                                properties.put("Supported vane left/right modes", swingModes.toString());
                                addChannel(channelId, itemType, swingModes);
                                break;
                        }
                        break;
                    case 9:
                        channelId = CHANNEL_TYPE_TARGETTEMP;
                        itemType = "Number:Temperature";
                        addChannel(channelId, itemType, null);
                        break;
                    case 10:
                        channelId = CHANNEL_TYPE_AMBIENTTEMP;
                        itemType = "Number:Temperature";
                        addChannel(channelId, itemType, null);
                        break;
                    case 37:
                        channelId = CHANNEL_TYPE_OUTDOORTEMP;
                        itemType = "Number:Temperature";
                        addChannel(channelId, itemType, null);
                        break;
                }
            }
        } catch (JsonSyntaxException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
        logger.trace("Start Refresh Job");
        refreshJob = scheduler.scheduleWithFixedDelay(this::getAllUidValues, 0, INTESIS_REFRESH_INTERVAL_SEC,
                TimeUnit.SECONDS);
    }

    /**
     * Update device status and all channels
     */
    private void getAllUidValues() {
        postRequest(sessionId -> "{\"command\":\"getdatapointvalue\",\"data\":{\"sessionID\":\"" + sessionId
                + "\", \"uid\":\"all\"}}", this::handleDataPointValues);
    }

    private void handleDataPointValues(Response response) {
        try {
            Data data = gson.fromJson(response.data, Data.class);
            Dpval[] dpval = gson.fromJson(data.dpval, Dpval[].class);
            for (Dpval element : dpval) {
                logger.trace("UID : {} ; value : {}", element.uid, element.value);
                switch (element.uid) {
                    case 1:
                        updateState(CHANNEL_TYPE_POWER,
                                String.valueOf(element.value).equals("0") ? OnOffType.OFF : OnOffType.ON);
                        break;
                    case 2:
                        switch (element.value) {
                            case 0:
                                updateState(CHANNEL_TYPE_MODE, StringType.valueOf("AUTO"));
                                break;
                            case 1:
                                updateState(CHANNEL_TYPE_MODE, StringType.valueOf("HEAT"));
                                break;
                            case 2:
                                updateState(CHANNEL_TYPE_MODE, StringType.valueOf("DRY"));
                                break;
                            case 3:
                                updateState(CHANNEL_TYPE_MODE, StringType.valueOf("FAN"));
                                break;
                            case 4:
                                updateState(CHANNEL_TYPE_MODE, StringType.valueOf("COOL"));
                                break;
                        }
                        break;
                    case 4:
                        if ((element.value) == 0) {
                            updateState(CHANNEL_TYPE_FANSPEED, StringType.valueOf("AUTO"));
                        } else {
                            updateState(CHANNEL_TYPE_FANSPEED, StringType.valueOf(String.valueOf(element.value)));
                        }
                        break;
                    case 5:
                    case 6:
                        State state;
                        if ((element.value) == 0) {
                            state = StringType.valueOf("AUTO");
                        } else if ((element.value) == 10) {
                            state = StringType.valueOf("SWING");
                        } else if ((element.value) == 11) {
                            state = StringType.valueOf("SWIRL");
                        } else if ((element.value) == 12) {
                            state = StringType.valueOf("WIDE");
                        } else {
                            state = StringType.valueOf(String.valueOf(element.value));
                        }
                        switch (element.uid) {
                            case 5:
                                updateState(CHANNEL_TYPE_VANESUD, state);
                                break;
                            case 6:
                                updateState(CHANNEL_TYPE_VANESLR, state);
                                break;
                        }
                        break;
                    case 9:
                        int unit = Math.round((element.value) / 10);
                        State stateValue = QuantityType.valueOf(unit, SIUnits.CELSIUS);
                        updateState(CHANNEL_TYPE_TARGETTEMP, stateValue);
                        break;
                    case 10:
                        unit = Math.round((element.value) / 10);
                        stateValue = QuantityType.valueOf(unit, SIUnits.CELSIUS);
                        updateState(CHANNEL_TYPE_AMBIENTTEMP, stateValue);
                        break;
                    case 37:
                        unit = Math.round((element.value) / 10);
                        stateValue = QuantityType.valueOf(unit, SIUnits.CELSIUS);
                        updateState(CHANNEL_TYPE_OUTDOORTEMP, stateValue);
                        break;
                }
            }
        } catch (JsonSyntaxException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }
}
