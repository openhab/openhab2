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
package org.openhab.binding.yioremote.internal;

import static org.openhab.binding.yioremote.internal.YIOremoteBindingConstants.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.yioremote.internal.YIOremoteBindingConstants.YIOREMOTEDOCKHANDLESTATUS;
import org.openhab.binding.yioremote.internal.YIOremoteBindingConstants.YIOREMOTEMESSAGETYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link YIOremoteDockHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Michael Loercher - Initial contribution
 */
@NonNullByDefault
public class YIOremoteDockHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(YIOremoteDockHandler.class);

    YIOremoteConfiguration localConfig = getConfigAs(YIOremoteConfiguration.class);
    private @Nullable YIOremoteDockHandler yioremotedockhandler = null;
    private WebSocketClient yioremoteDockHandlerwebSocketClient = new WebSocketClient();
    private YIOremoteDockWebsocket yioremoteDockwebSocketClient = new YIOremoteDockWebsocket(this);
    private ClientUpgradeRequest yioremoteDockwebSocketClientrequest = new ClientUpgradeRequest();
    private @Nullable URI uriyiodockwebsocketaddress;
    private YIOREMOTEDOCKHANDLESTATUS yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.UNINITIALIZED_STATE;
    private @Nullable Future<?> pollingJob;
    public String stringreceivedmessage = "";
    private JsonObject jsonobjectrecievedJsonObject = new JsonObject();
    private boolean booleanauthenticationrequired = false;
    private boolean booleanheartbeat = false;
    private boolean booleanauthenticationok = false;
    private boolean booleansendirstatus = false;
    private boolean booleanwebsocketconnected = false;
    private String stringreceivedstatus = "";
    private String stringlastsendircode = "";

    public YIOremoteDockHandler(Thing thing) {
        super(thing);
        yioremotedockhandler = this;
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);
        scheduler.execute(() -> {
            try {
                uriyiodockwebsocketaddress = new URI("ws://" + localConfig.host + ":946");
                yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_PROCESS;
            } catch (URISyntaxException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.OFFLINE.COMMUNICATION_ERROR,
                        "Initialize web socket failed");
            }

            yioremoteDockwebSocketClient.addMessageHandler(new YIOremoteDockWebsocketInterface() {

                @Override
                public void onConnect(Boolean booleanconnectedflag) {
                    yioremotedockhandler.booleanwebsocketconnected = booleanconnectedflag;
                    if (booleanconnectedflag) {
                        yioremotedockhandler.yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.CONNECTION_ESTABLISHED;
                        yioremotedockhandler.startwebsocketpollingthread(booleanconnectedflag);
                    } else {
                        yioremotedockhandler.yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.CONNECTION_FAILED;
                    }
                }

                @Override
                public void onMessage(String message) {
                    yioremotedockhandler.stringreceivedmessage = message;
                    logger.debug("Message recieved {}", message);
                }
            });
            try {
                yioremoteDockHandlerwebSocketClient.start();

                yioremoteDockHandlerwebSocketClient.connect(yioremoteDockwebSocketClient, uriyiodockwebsocketaddress,
                        yioremoteDockwebSocketClientrequest);
            } catch (Exception e) {
                logger.debug("Connection error");
            }

        });
    }

    private boolean decodereceivedMessage(@Nullable JsonObject JsonObject_recievedJsonObject) {
        boolean booleanresult = false;

        if (JsonObject_recievedJsonObject.has("type")) {
            if (JsonObject_recievedJsonObject.get("type").toString().equalsIgnoreCase("\"auth_required\"")) {
                yioremotedockhandler.booleanauthenticationrequired = true;
                yioremotedockhandler.booleanheartbeat = true;
                booleanresult = true;
            } else if (JsonObject_recievedJsonObject.get("type").toString().equalsIgnoreCase("\"auth_ok\"")) {
                yioremotedockhandler.booleanauthenticationrequired = false;
                yioremotedockhandler.booleanauthenticationok = true;
                yioremotedockhandler.booleanheartbeat = true;
                booleanresult = true;
            } else if (JsonObject_recievedJsonObject.get("type").toString().equalsIgnoreCase("\"dock\"")
                    && JsonObject_recievedJsonObject.has("message"))

            {
                if (JsonObject_recievedJsonObject.get("message").toString().equalsIgnoreCase("\"ir_send\"")) {
                    if (JsonObject_recievedJsonObject.get("success").toString().equalsIgnoreCase("true")) {
                        yioremotedockhandler.stringreceivedstatus = "Send IR Code successfully";
                        yioremotedockhandler.booleansendirstatus = true;
                        yioremotedockhandler.booleanheartbeat = true;
                        booleanresult = true;
                    } else {
                        if (stringlastsendircode.equalsIgnoreCase("\"0;0x0;0;0\"")) {
                            logger.debug("Send heartbeat Code success");
                        } else {
                            stringreceivedstatus = "Send IR Code failure";
                        }
                        yioremotedockhandler.booleansendirstatus = true;
                        yioremotedockhandler.booleanheartbeat = true;
                        booleanresult = true;
                    }
                } else {
                    logger.warn("No known message {}", stringreceivedmessage);
                    yioremotedockhandler.booleanheartbeat = false;
                    booleanresult = false;
                }
            } else if (JsonObject_recievedJsonObject.get("command").toString().equalsIgnoreCase("\"ir_receive\"")) {
                yioremotedockhandler.stringreceivedstatus = JsonObject_recievedJsonObject.get("code").toString()
                        .replace("\"", "");
                if (stringreceivedstatus.matches("[0-9][;]0[xX][0-9a-fA-F]+[;][0-9]+[;][0-9]")) {
                    yioremotedockhandler.stringreceivedstatus = JsonObject_recievedJsonObject.get("code").toString()
                            .replace("\"", "");
                } else {
                    yioremotedockhandler.stringreceivedstatus = "";
                }
                logger.debug("ir_receive message {}", stringreceivedstatus);
                yioremotedockhandler.booleanheartbeat = true;
                booleanresult = true;
            } else {
                logger.warn("No known message {}", stringreceivedmessage);
                yioremotedockhandler.booleanheartbeat = false;
                booleanresult = false;
            }
        } else

        {
            logger.warn("No known message {}", stringreceivedmessage);
            yioremotedockhandler.booleanheartbeat = false;
            booleanresult = false;
        }
        return booleanresult;
    }

    private JsonObject convertStringtoJsonObject(String jsonString) {
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonString);
            JsonObject result;

            if (jsonElement instanceof JsonObject) {
                result = jsonElement.getAsJsonObject();
            } else {
                logger.debug("{} is not valid JSON stirng", jsonString);
                result = new JsonObject();
                throw new IllegalArgumentException(jsonString + "{} is not valid JSON stirng");
            }
            return result;
        } catch (IllegalArgumentException e) {
            JsonObject result = new JsonObject();
            return result;
        }
    }

    protected void updateState(String group, String channelId, State value) {
        ChannelUID id = new ChannelUID(getThing().getUID(), group, channelId);
        updateState(id, value);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(YIOremoteDockActions.class);
    }

    @Override
    public void dispose() {
        Future<?> localPollingJob = pollingJob;
        if (localPollingJob != null) {
            localPollingJob.cancel(true);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (YIODOCKRECEIVERSWITCH.equals(channelUID.getIdWithoutGroup())) {
            if (yioremotedockactualstatus.equals(YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_COMPLETE)) {
                if (command.toString().equals("ON")) {
                    logger.debug("YIODOCKRECEIVERSWITCH ON procedure: Switching IR Receiver on");
                    yioremoteDockwebSocketClient.sendMessage(YIOREMOTEMESSAGETYPE.IRRECEIVERON, "");
                } else if (command.toString().equals("OFF")) {
                    logger.debug("YIODOCKRECEIVERSWITCH OFF procedure: Switching IR Receiver off");
                    yioremoteDockwebSocketClient.sendMessage(YIOREMOTEMESSAGETYPE.IRRECEIVEROFF, "");
                } else {
                    logger.debug("YIODOCKRECEIVERSWITCH no procedure");
                }
            }
        }
    }

    public void sendircode(@Nullable String stringIRCode) {
        if (stringIRCode != null) {
            if (stringIRCode.matches("[0-9][;]0[xX][0-9a-fA-F]+[;][0-9]+[;][0-9]")) {
                yioremoteDockwebSocketClient.sendMessage(YIOREMOTEMESSAGETYPE.IRSEND, stringIRCode);
            } else {
                logger.warn("Wrong ir code format {}", stringIRCode);
            }
        }
    }

    private ChannelUID getChannelUuid(String group, String typeId) {
        return new ChannelUID(getThing().getUID(), group, typeId);
    }

    private void updateChannelString(String group, String channelId, String value) {
        ChannelUID id = new ChannelUID(getThing().getUID(), group, channelId);
        updateState(id, new StringType(value));
    }

    private void updateChannelString(ChannelUID channelId, String value) {
        updateState(channelId, new StringType(value));
    }

    void startwebsocketpollingthread(boolean booleanconnectedflag) {
        Runnable websocketpollingthread = new Runnable() {
            @Override
            public void run() {
                jsonobjectrecievedJsonObject = convertStringtoJsonObject(stringreceivedmessage);
                if (jsonobjectrecievedJsonObject.size() > 0) {
                    if (yioremotedockhandler.decodereceivedMessage(jsonobjectrecievedJsonObject)) {
                        logger.debug("Message {} decoded", stringreceivedmessage);
                    } else {
                        logger.debug("Error during message {} decoding", stringreceivedmessage);
                    }
                }

                if (yioremotedockactualstatus.equals(YIOREMOTEDOCKHANDLESTATUS.CONNECTION_ESTABLISHED)) {
                    yioremoteDockwebSocketClient.sendMessage(YIOREMOTEMESSAGETYPE.AUTHENTICATE,
                            yioremotedockhandler.localConfig.accesstoken);
                    yioremotedockhandler.yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_PROCESS;
                } else if (yioremotedockactualstatus.equals(YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_PROCESS)) {
                    if (yioremotedockhandler.getbooleanauthenticationok()) {
                        yioremotedockhandler.yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_COMPLETE;
                        logger.debug("IF getbooleanauthenticationok {}",
                                yioremotedockhandler.getbooleanauthenticationok());
                    } else {
                        yioremotedockhandler.yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_FAILED;
                        logger.debug("ELSE getbooleanauthenticationok {}",
                                yioremotedockhandler.getbooleanauthenticationok());
                    }
                } else if (yioremotedockactualstatus.equals(YIOREMOTEDOCKHANDLESTATUS.AUTHENTICATION_COMPLETE)) {
                    if (yioremotedockhandler.getbooleanheartbeat()) {
                        logger.debug("heartbeat ok");
                        triggerChannel(getChannelUuid(GROUP_OUTPUT, YIODOCKSTATUS));
                        updateChannelString(GROUP_OUTPUT, YIODOCKSTATUS, yioremotedockhandler.stringreceivedmessage);
                        yioremoteDockwebSocketClient.sendMessage(YIOREMOTEMESSAGETYPE.HEARTBEAT, "");
                    } else {
                        yioremotedockactualstatus = YIOREMOTEDOCKHANDLESTATUS.CONNECTION_FAILED;
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                                "Connection lost no ping from YIO DOCK");
                        updateState(GROUP_OUTPUT, YIODOCKSTATUS, UnDefType.UNDEF);
                    }
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "Connection lost no ping from YIO DOCK");
                    updateState(GROUP_OUTPUT, YIODOCKSTATUS, UnDefType.UNDEF);
                }
            }
        };
        pollingJob = scheduler.scheduleWithFixedDelay(websocketpollingthread, 0, 30, TimeUnit.SECONDS);
    }

    protected boolean getbooleanauthenticationok() {
        return yioremotedockhandler.booleanauthenticationok;
    }

    protected boolean getbooleanheartbeat() {
        boolean booleanresult = yioremotedockhandler.booleanheartbeat;
        yioremotedockhandler.booleanheartbeat = false;
        return booleanresult;
    }

    public YIOREMOTEDOCKHANDLESTATUS getyioremotedockactualstatus() {
        return yioremotedockhandler.yioremotedockactualstatus;
    }
}
