/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
 *
>>>>>>> Project skeleton.
=======
 *
>>>>>>> Added Chamberlain MyQ skeleton
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wink.handler;

import static org.openhab.binding.wink.WinkBindingConstants.*;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.openhab.binding.wink.config.WinkDeviceConfig;
import org.openhab.binding.wink.handler.WinkHub2Handler.RequestCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

/**
 * The {@link WinkHandler} provides a base implementation for a Wink device, it
 * contains all the common logic and declares some abstract method that should
 * be implemented by every Wink device.
 *
 * @author Sebastien Marchand - Initial contribution
 */
public abstract class WinkHandler extends BaseThingHandler {
    /**
     * Base configuration of this device.
     */
    protected WinkDeviceConfig deviceConfig;

    /**
     * Logger for this class. Can be used by the derived classes.
     */
    protected Logger logger = LoggerFactory.getLogger(WinkHandler.class);

    /**
     * The PubNub object used by this class.
     */
    protected PubNub pubnub;

    /**
     * Creates a new instance of this class for the {@link Thing}.
     *
     * @param thing the thing that should be handled, not null.
     */
    public WinkHandler(Thing thing) {
        super(thing);
        String config = (String) getThing().getConfiguration().get(WINK_DEVICE_CONFIG);
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
        String id = (String) getThing().getConfiguration().get(WINK_DEVICE_ID);
        this.deviceConfig = new WinkDeviceConfig(id);
        parseConfig(config);
=======
        logger.trace("Initializing a thing with the following config: " + config);
=======
        logger.trace("Initializing a thing with the following config: {}", config);
>>>>>>> Fixes
=======
        logger.trace("Initializing a thing with the following config: {}", config);
>>>>>>> Added Chamberlain MyQ skeleton
        String id = (String) getThing().getConfiguration().get(WINK_DEVICE_ID);
        logger.trace("Thing ID: {}", id);
        this.deviceConfig = new WinkDeviceConfig(id);
        parseConfig(config);
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
        logger.info("Initializing a Wink device: \n" + deviceConfig.asString());
>>>>>>> Project skeleton.
=======
        logger.info("Initializing a Wink device: \n{}", deviceConfig.asString());
>>>>>>> Fixes
=======
        logger.info("Initializing a Wink device: \n{}", deviceConfig.asString());
>>>>>>> Added Chamberlain MyQ skeleton
        registerToPubNub();
    }

    @Override
    public void dispose() {
        super.dispose();
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997

=======
>>>>>>> Project skeleton.
=======
>>>>>>> Added Chamberlain MyQ skeleton
        pubnub.unsubscribe().channels(Arrays.asList(deviceConfig.getPubnubChannel()));
        pubnub.destroy();
    }

    /**
     * Parse the configuration of this thing.
     *
     * @param jsonConfigString the string containing the configuration of this thing as returned by the hub (in JSON).
     */
    protected void parseConfig(String jsonConfigString) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
        logger.trace("Parsing a thing's config: " + jsonConfigString);
>>>>>>> Project skeleton.
=======
        logger.trace("Parsing a thing's config: {}", jsonConfigString);
>>>>>>> Fixes
=======
        logger.trace("Parsing a thing's config: {}", jsonConfigString);
>>>>>>> Added Chamberlain MyQ skeleton
        JsonParser parser = new JsonParser();
        deviceConfig.readConfigFromJson(parser.parse(jsonConfigString).getAsJsonObject());
    }

    /**
     * Used to retrieve the {@link WinkHub2Handler} controlling this thing.
     *
     * @return this thing hub handler, null if it hasn't been set yet.
     */
    protected WinkHub2Handler getHubHandler() {
        Bridge hub = getBridge();
        return hub == null ? null : (WinkHub2Handler) hub.getHandler();
    }

    /**
     * Function called when a communication error between the hub and the thing has been detected.
     */
    protected void HandleCommunicationError(String errorMessage) {
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, errorMessage);
    }

    /**
     * Abstract method that should return the device path that get appended to the
     * Wink server URL when making a request.
     *
     * It usually has the form "{device_type}/{device_id}".
     *
     * @return this thing REST request path.
     */
    protected abstract String getDeviceRequestPath();

    private abstract class WinkDeviceRequestCallback implements RequestCallback {
        /**
         * Handler of the thing that should for which the configuration should be set.
         */
        protected WinkHandler handler;

        /**
         * Creates a new instance of this class.
         *
         * @param handler The handler for which the configuration should be read.
         */
        public WinkDeviceRequestCallback(WinkHandler handler) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
            Preconditions.checkArgument(handler != null, "The argument 'handler' must not be null.");
=======
            Preconditions.checkArgument(handler != null, "The argument |handler| must not be null.");
>>>>>>> Project skeleton.
=======
            Preconditions.checkArgument(handler != null, "The argument |handler| must not be null.");
>>>>>>> Added Chamberlain MyQ skeleton
            this.handler = handler;
        }

        @Override
        public void OnError(String error) {
            handler.HandleCommunicationError(error);
        }
    }

    //////////// Read State functions ////////////

    /**
     * Specialization of a {link RequestCallback} to read a device configuration.
     *
     * @author Sebastien Marchand
     */
    private class ReadDeviceStateCallback extends WinkDeviceRequestCallback {
        /**
         * Creates a new instance of this class.
         *
         * @param handler The handler for which the state should be read.
         */
        public ReadDeviceStateCallback(WinkHandler handler) {
            super(handler);
        }

        @Override
        public void parseRequestResult(JsonObject jsonResult) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
            logger.trace("Parsing a ReadDeviceState request result: " + jsonResult);
>>>>>>> Project skeleton.
=======
            logger.trace("Parsing a ReadDeviceState request result: {}", jsonResult);
>>>>>>> Fixes
=======
            logger.trace("Parsing a ReadDeviceState request result: {}", jsonResult);
>>>>>>> Added Chamberlain MyQ skeleton
            // The response from the server is a JSON object containing the device information and state.
            handler.updateDeviceStateCallback(jsonResult.get("data").getAsJsonObject());
        }
    }

    /**
     * Query the {@link WinkHub2Handler} for this device's state.
     */
    protected void ReadDeviceState() {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
        logger.trace("Querying the device state for: \n" + deviceConfig.asString());
>>>>>>> Project skeleton.
=======
        logger.trace("Querying the device state for: \n{}", deviceConfig.asString());
>>>>>>> Fixes
=======
        logger.trace("Querying the device state for: \n{}", deviceConfig.asString());
>>>>>>> Added Chamberlain MyQ skeleton
        try {
            getHubHandler().sendRequestToServer(getDeviceRequestPath(), new ReadDeviceStateCallback(this));
        } catch (IOException e) {
            logger.error("Error while querying the hub for {}", getDeviceRequestPath(), e);
        }
    }

    /**
     * Callback called once the device state has been updated.
     *
     * @param jsonDataBlob the reply from the hub.
     */
    abstract protected void updateDeviceStateCallback(JsonObject jsonDataBlob);

    /////////////////////////////////////////////////

    //////////// Send commands functions ////////////

    private class SendCommandCallback extends WinkDeviceRequestCallback {
        public SendCommandCallback(WinkHandler handler) {
            super(handler);
        }

        @Override
        public void parseRequestResult(JsonObject jsonResult) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
            logger.trace("Parsing a SendCommandCallback request result: " + jsonResult);
>>>>>>> Project skeleton.
=======
            logger.trace("Parsing a SendCommandCallback request result: {}", jsonResult);
>>>>>>> Fixes
=======
            logger.trace("Parsing a SendCommandCallback request result: {}", jsonResult);
>>>>>>> Added Chamberlain MyQ skeleton
            handler.sendCommandCallback(jsonResult);
        }
    }

    public void sendCommand(String payLoad) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
        logger.trace("Sending a command with the following payload: " + payLoad +
                     "\nto device: \n" + deviceConfig.asString());
>>>>>>> Project skeleton.
=======
        logger.trace("Sending a command with the following payload: {}\nto device: \n", payLoad, deviceConfig.asString());
>>>>>>> Fixes
=======
        logger.trace("Sending a command with the following payload: {}\nto device: \n", payLoad, deviceConfig.asString());
>>>>>>> Added Chamberlain MyQ skeleton
        try {
            getHubHandler().sendRequestToServer(getDeviceRequestPath() + "/desired_state",
                    new SendCommandCallback(this), payLoad);
        } catch (IOException e) {
            logger.error("Error while querying the hub for {}", getDeviceRequestPath(), e);
        }
    }

    abstract public void sendCommandCallback(JsonObject jsonResult);

    /////////////////////////////////////////////////

    protected void registerToPubNub() {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
        logger.debug("Doing the PubNub registration for :\n" + deviceConfig.asString());
>>>>>>> Project skeleton.
=======
        logger.debug("Doing the PubNub registration for :\n{}", deviceConfig.asString());
>>>>>>> Fixes
=======
        logger.debug("Doing the PubNub registration for :\n{}", deviceConfig.asString());
>>>>>>> Added Chamberlain MyQ skeleton
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(this.deviceConfig.getPubnubSubscribeKey());

        this.pubnub = new PubNub(pnConfiguration);
        this.pubnub.addListener(new SubscribeCallback() {
            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 6896c1b5ebdafba80adb18a65e335753cd3668db
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
=======
                logger.trace("Received a reply from PubNub: " + message.getMessage().getAsString());
>>>>>>> Project skeleton.
=======
                logger.trace("Received a reply from PubNub: {}", message.getMessage().getAsString());
>>>>>>> Fixes
=======
                logger.trace("Received a reply from PubNub: {}", message.getMessage().getAsString());
>>>>>>> Added Chamberlain MyQ skeleton
                JsonParser parser = new JsonParser();
                JsonObject jsonMessage = parser.parse(message.getMessage().getAsString()).getAsJsonObject();
                pubNubMessageCallback(jsonMessage);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }

            @Override
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
<<<<<<< 22e7f0057024a151fbe7e0c2e676ca9e9bcf6997
            public void status(PubNub arg0, PNStatus arg1) {
=======
=======
>>>>>>> Added Chamberlain MyQ skeleton
            public void status(PubNub arg0, PNStatus status) {
              if (status.isError()) {
                logger.error("PubNub communication error: {}", status.getStatusCode());
              } else {
                logger.trace("PubNub status: no error.");
              }
<<<<<<< 60b2641262654f560ba41b55ecd404bec7547f0b
>>>>>>> Project skeleton.
=======
>>>>>>> Added Chamberlain MyQ skeleton
            }
        });

        this.pubnub.subscribe().channels(Arrays.asList(this.deviceConfig.getPubnubChannel())).execute();
    }

    abstract protected void pubNubMessageCallback(JsonObject jsonDataBlob);
}
