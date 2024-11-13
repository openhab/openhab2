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
package org.openhab.binding.dirigera.internal.handler;

import static org.openhab.binding.dirigera.internal.Constants.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.DirigeraCommandProvider;
import org.openhab.binding.dirigera.internal.config.DirigeraConfiguration;
import org.openhab.binding.dirigera.internal.discovery.DirigeraDiscoveryManager;
import org.openhab.binding.dirigera.internal.exception.ApiMissingException;
import org.openhab.binding.dirigera.internal.exception.ModelMissingException;
import org.openhab.binding.dirigera.internal.interfaces.DirigeraAPI;
import org.openhab.binding.dirigera.internal.interfaces.Gateway;
import org.openhab.binding.dirigera.internal.interfaces.Model;
import org.openhab.binding.dirigera.internal.model.DirigeraModel;
import org.openhab.binding.dirigera.internal.network.DirigeraAPIImpl;
import org.openhab.binding.dirigera.internal.network.Websocket;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.storage.Storage;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.UnDefType;
import org.openhab.core.util.StringUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DirigeraHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class DirigeraHandler extends BaseBridgeHandler implements Gateway {

    private final Logger logger = LoggerFactory.getLogger(DirigeraHandler.class);

    // Can be overwritten by Unit test for mocking API
    protected Class<?> apiProvider = DirigeraAPIImpl.class;

    private final Map<String, BaseHandler> deviceTree = new HashMap<>();
    private final DirigeraDiscoveryManager discoveryManager;
    private final DirigeraCommandProvider commandProvider;
    private final TimeZoneProvider timeZoneProvider;
    private final BundleContext bundleContext;

    private Optional<Websocket> websocket = Optional.empty();
    private Optional<DirigeraAPI> api = Optional.empty();
    private Optional<Model> model = Optional.empty();
    private Optional<ScheduledFuture<?>> heartbeat = Optional.empty();
    private Optional<ScheduledFuture<?>> detectionSchedule = Optional.empty();

    // private ScheduledExecutorService sequentialScheduler;
    private List<Object> knownDevices = new ArrayList<>();
    private ArrayList<String> websocketQueue = new ArrayList<>();
    private ArrayList<DeviceUpdate> deviceModificationQueue = new ArrayList<>();
    private DirigeraConfiguration config;
    private Storage<String> storage;
    private HttpClient httpClient;
    private String token = PROPERTY_EMPTY;
    private Instant sunriseInstant = Instant.MAX;
    private Instant sunsetInstant = Instant.MIN;
    private Instant peakRecognitionTime = Instant.MIN;
    private int websocketQueueSizePeak = 0;
    private int deviceUpdateQueueSizePeak = 0;
    private boolean updateRunning = false;

    public static final DeviceUpdate LINK_UPDATE = new DeviceUpdate(null, "", DeviceUpdate.Action.LINKS);
    public static long detectionTimeSeonds = 5;

    public DirigeraHandler(Bridge bridge, HttpClient insecureClient, Storage<String> bindingStorage,
            DirigeraDiscoveryManager discoveryManager, TimeZoneProvider timeZoneProvider,
            DirigeraCommandProvider commandProvider, BundleContext bundleContext) {
        super(bridge);
        this.discoveryManager = discoveryManager;
        this.timeZoneProvider = timeZoneProvider;
        this.httpClient = insecureClient;
        this.storage = bindingStorage;
        this.commandProvider = commandProvider;
        this.bundleContext = bundleContext;
        config = new DirigeraConfiguration();
        /**
         * structural changes like adding, removing devices and update links needs to be done in a sequential way.
         * Pressure during startup is causing java.util.ConcurrentModificationException
         */
        // sequentialScheduler = ThreadPoolManager
        // .getPoolBasedSequentialScheduledExecutorService(this.getClass().getName(), BINDING_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String channel = channelUID.getIdWithoutGroup();
        if (command instanceof RefreshType) {
            JSONObject values = model().getAllFor(config.id, PROPERTY_DEVICES);
            handleUpdate(values);
            updateState(new ChannelUID(thing.getUID(), CHANNEL_JSON), StringType.valueOf(model().getModelString()));
        } else if (CHANNEL_PAIRING.equals(channel)) {
            JSONObject permissionAttributes = new JSONObject();
            permissionAttributes.put(PROPERTY_PERMIT_JOIN, OnOffType.ON.equals(command));
            api().sendAttributes(config.id, permissionAttributes);
        }
    }

    @Override
    public void initialize() {
        // do this asynchronous in case of token and other parameters needs to be obtained via Rest API calls
        scheduler.execute(this::doInitialize);
    }

    private void doInitialize() {
        config = getConfigAs(DirigeraConfiguration.class);

        // for discovery known device are stored in storage in order not to report them again and again through
        // DiscoveryService
        getKnownDevicesFromStorage();

        // get token from storage
        token = getTokenFromStorage();
        if (token.isBlank()) {
            logger.warn("DIRIGERA HANDLER no token in storage");
        } else {
            logger.info("DIRIGERA HANDLER obtained token {} from storage", token);
        }

        // Step 2 - check if token is available or stored
        if (token.isBlank()) {
            // Step 2.2 - if token wasn't recovered from storage begin pairing process
            String codeVerifier = generateCodeVerifier();
            String challenge = generateCodeChallenge(codeVerifier);
            String code = getCode(challenge);
            if (!code.isBlank()) {
                updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NOT_YET_READY,
                        "@text/dirigera.gateway.status.pairing-button");
                /**
                 * Approx 3 minutes possible to push DIRIGERA button
                 */
                Instant stopAuth = Instant.now().plusSeconds(180);
                while (Instant.now().isBefore(stopAuth) && token.isBlank()) {
                    try {
                        logger.info("DIRIGERA HANDLER press button on DIRIGERA gateway - wait 3 secs");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        logger.info("DIRIGERA HANDLER error during waiting {}", e.getMessage());
                    }
                    token = getToken(code, codeVerifier);
                    if (token.isBlank()) {
                        logger.info("DIRIGERA HANDLER no token received");
                    } else {
                        logger.info("DIRIGERA HANDLER token {} received", token);
                    }
                }
                if (token.isBlank()) {
                    logger.info("DIRIGERA HANDLER pairing failed - Stop/Start bridge to initialize new pairing");
                    updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NOT_YET_READY,
                            "@text/dirigera.gateway.status.pairing-retry");
                    return;
                } else {
                    storeToken(token);
                }
            }
        } else {
            logger.info("DIRIGERA HANDLER token available");
        }

        // Step 3 - ip address and token fine, now start initializing
        logger.info("DIRIGERA HANDLER Step 3 with {}", config.toString());
        if (!config.ipAddress.isBlank() && !token.isBlank()) {
            // looks good, ip and token fine, now create api and model

            DirigeraAPI apiProviderInstance = null;
            try {
                apiProviderInstance = (DirigeraAPI) apiProvider.getConstructor(HttpClient.class, Gateway.class)
                        .newInstance(httpClient, this);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                logger.error("DIRIGERA HANDLER unable to create API {}", apiProvider.descriptorString());
            }
            if (apiProviderInstance != null) {
                api = Optional.of(apiProviderInstance);
            } else {
                // this will not happen - DirirgeraAPIIMpl tested with mocks in unit tests
                logger.error("DIRIGERA HANDLER unable to create API {}", apiProvider.descriptorString());
                return;
            }
            Model houseModel = new DirigeraModel(this);
            model = Optional.of(houseModel);
            int status = houseModel.update();
            if (status != 200) {
                logger.error("DIRIGERA HANDLER Model init failed {}", status);
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "@text/dirigera.gateway.status.comm-error" + " [\"" + status + "\"]");
                return;
            }
            // Step 3.1 - check if id is already obtained
            if (config.id.isBlank()) {
                List<String> gatewayList = houseModel.getDevicesForTypes(List.of(DEVICE_TYPE_GATEWAY));
                logger.info("DIRIGERA HANDLER try to get gateway id {}", gatewayList);
                if (gatewayList.isEmpty()) {
                    logger.warn("DIRIGERA HANDLER no Gateway found in model");
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE,
                            "@text/dirigera.gateway.status.no-gateway");
                    return;
                } else if (gatewayList.size() > 1) {
                    logger.warn("DIRIGERA HANDLER found {} Gateways - don't choose, ambigious result",
                            gatewayList.size());
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE,
                            "@text/dirigera.gateway.status.ambiguous-gateway");
                    return;
                } else {
                    String id = gatewayList.get(0);
                    Configuration configUpdate = editConfiguration();
                    configUpdate.put(PROPERTY_DEVICE_ID, id);
                    updateConfiguration(configUpdate);
                    // get fresh config after update
                    config = getConfigAs(DirigeraConfiguration.class);
                }
            }
            updateProperties();
            // now start websocket an listen to changes
            logger.info("DIRIGERA HANDLER start websocket");
            websocket = Optional.of(new Websocket(this, httpClient));
            websocket.get().start();
            heartbeat = Optional.of(scheduler.scheduleWithFixedDelay(this::heartbeat, 1, 1, TimeUnit.MINUTES));

            // update latest model data
            JSONObject values = model().getAllFor(config.id, PROPERTY_DEVICES);
            handleUpdate(values);

            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        heartbeat.ifPresent(refresher -> {
            refresher.cancel(false);
        });
        websocket.ifPresent(client -> {
            client.dispose();
        });
    }

    @Override
    public void handleRemoval() {
        super.handleRemoval();
        // sequentialScheduler.shutdownNow();
        // todo clear storage
    }

    private void updateProperties() {
        Map<String, Object> propertiesMap = model().getPropertiesFor(config.id);
        Map<String, String> currentProperties = editProperties();
        propertiesMap.forEach((key, value) -> {
            currentProperties.put(key, value.toString());
        });
        updateProperties(currentProperties);
    }

    private String getTokenFromStorage() {
        logger.info("DIRIGERA HANDLER try to get token from storage");
        JSONObject gatewayStorageJson = getStorageJson();
        if (gatewayStorageJson.has(PROPERTY_TOKEN)) {
            String token = gatewayStorageJson.getString(PROPERTY_TOKEN);
            if (!token.isBlank()) {
                return token;
            }
        }
        logger.info("DIRIGERA HANDLER no token in storage");
        return PROPERTY_EMPTY;
    }

    private void storeToken(String token) {
        if (!config.ipAddress.isBlank()) {
            JSONObject tokenStore = new JSONObject();
            tokenStore.put(PROPERTY_TOKEN, token);
            logger.info("DIRIGERA HANDLER write into store {}", tokenStore.toString());
            storage.put(config.ipAddress, tokenStore.toString());
        } else {
            logger.info("DIRIGERA HANDLER Cannnot store token, device id missing");
        }
    }

    private void getKnownDevicesFromStorage() {
        JSONObject gatewayStorageJson = getStorageJson();
        if (gatewayStorageJson.has(PROPERTY_DEVICES)) {
            String knownDeviceString = gatewayStorageJson.getString(PROPERTY_DEVICES);
            JSONArray arr = new JSONArray(knownDeviceString);
            knownDevices = arr.toList();
        } else {
            logger.info("DIRIGERA HANDLER no known devices stored");
        }
    }

    private void storeKnownDevices() {
        JSONObject gatewayStorageJson = getStorageJson();
        JSONArray toStoreArray = new JSONArray(knownDevices);
        gatewayStorageJson.put(PROPERTY_DEVICES, toStoreArray.toString());
        storage.put(config.ipAddress, gatewayStorageJson.toString());
    }

    private JSONObject getStorageJson() {
        if (!config.ipAddress.isBlank()) {
            String gatewayStorageObject = storage.get(config.ipAddress);
            if (gatewayStorageObject != null) {
                JSONObject gatewayStorageJson = new JSONObject(gatewayStorageObject.toString());
                return gatewayStorageJson;
            }
        }
        // fallback copy from id to ip
        if (!config.id.isBlank()) {
            String gatewayStorageObject = storage.get(config.id);
            if (gatewayStorageObject != null) {
                JSONObject gatewayStorageJson = new JSONObject(gatewayStorageObject.toString());
                storage.put(config.ipAddress, gatewayStorageJson.toString());
                return gatewayStorageJson;
            }
        }
        logger.info("DIRIGERA HANDLER nothing found in storage for Gateway {}", config.id);
        return new JSONObject();
    }

    private void heartbeat() {
        JSONObject gatewayInfo = api().readDevice(config.id);
        if (gatewayInfo.has(DirigeraAPI.HTTP_ERROR_FLAG)) {
            String message = gatewayInfo.getInt(DirigeraAPI.HTTP_ERROR_STATUS) + " - "
                    + gatewayInfo.getInt(DirigeraAPI.HTTP_ERROR_MESSAGE);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/dirigera.gateway.status.comm-error" + " [\"" + message + "\"]");
        } else {
            updateStatus(ThingStatus.ONLINE);
            websocket.ifPresentOrElse((wsClient) -> {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_STATISTICS),
                        StringType.valueOf(wsClient.getStatistics().toString()));
                if (!wsClient.isRunning()) {
                    logger.trace("DIRIGERA HANDLER WS restart necessary");
                    wsClient.start();
                } else {
                    Map<String, Instant> pingPnogMap = wsClient.getPingPongMap();
                    if (pingPnogMap.size() > 1) {
                        logger.warn("DIRIGERA HANDLER WS HEARTBEAT PANIC! - {} pings not answered", pingPnogMap.size());
                        wsClient.dispose();
                        Websocket ws = new Websocket(this, httpClient);
                        ws.start();
                        websocket = Optional.of(ws);
                    } else {
                        wsClient.ping();
                    }
                }
            }, () -> {
                logger.warn("DIRIGERA HANDLER WS no websocket present - start new one");
                Websocket ws = new Websocket(this, httpClient);
                ws.start();
                websocket = Optional.of(ws);
            });
        }
    }

    /**
     * Everything to handle pairing process
     */

    private String getCode(String challenge) {
        try {
            MultiMap<@Nullable String> baseParams = new MultiMap<>();
            baseParams.put("audience", "homesmart.local");
            baseParams.put("response_type", "code");
            baseParams.put("code_challenge", challenge);
            baseParams.put("code_challenge_method", "S256");

            String url = String.format(OAUTH_URL, config.ipAddress);
            Request codeRequest = httpClient.newRequest(url).param("audience", "homesmart.local")
                    .param("response_type", "code").param("code_challenge", challenge)
                    .param("code_challenge_method", "S256");
            logger.info("DIRIGERA HANDLER Call {}", url);

            ContentResponse response = codeRequest.timeout(10, TimeUnit.SECONDS).send();
            int responseStatus = response.getStatus();
            if (responseStatus != 200) {
                String reason = response.getReason();
                logger.warn("DIRIGERA HANDLER exception during code request {} {}", responseStatus, reason);
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "@text/dirigera.gateway.status.comm-error" + " [\"" + responseStatus + " - " + reason + "\"]");
                return "";
            }
            String responseString = response.getContentAsString();
            logger.info("DIRIGERA HANDLER code challenge {} : {}", responseStatus, responseString);
            JSONObject codeResponse = new JSONObject(responseString);
            String code = codeResponse.getString("code");
            logger.info("DIRIGERA HANDLER got code {}", code);
            return code;
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.warn("DIRIGERA HANDLER exception during code request {}", e.getMessage());
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/dirigera.gateway.status.comm-error" + " [\"" + e.getMessage() + "\"]");
            return "";
        }
    }

    private String getToken(String code, String codeVerifier) {
        try {
            MultiMap<@Nullable String> baseParams = new MultiMap<>();
            baseParams.put("code", code);
            baseParams.put("name", "openHAB");
            baseParams.put("grant_type", "authorization_code");
            baseParams.put("code_verifier", codeVerifier);

            String url = String.format(TOKEN_URL, config.ipAddress);
            String urlEncoded = UrlEncoded.encode(baseParams, StandardCharsets.UTF_8, false);
            Request tokenRequest = httpClient.POST(url)
                    .header(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .content(new StringContentProvider("application/x-www-form-urlencoded", urlEncoded,
                            StandardCharsets.UTF_8))
                    .followRedirects(true);

            ContentResponse response = tokenRequest.timeout(10, TimeUnit.SECONDS).send();
            logger.info("DIRIGERA HANDLER token response {} : {}", response.getStatus(), response.getContentAsString());
            int responseStatus = response.getStatus();
            if (responseStatus != 200) {
                return "";
            }
            String responseString = response.getContentAsString();
            JSONObject tokenResponse = new JSONObject(responseString);
            String accessToken = tokenResponse.getString("access_token");
            logger.info("DIRIGERA HANDLER got token {}", accessToken);
            return accessToken;
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            logger.warn("DIRIGERA HANDLER exception fetching token {}", e.getMessage());
            return "";
        }
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("sha256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("DIRIGERA HANDLER error creating code challenge {}", e.getMessage());
            return "";
        }
    }

    private String generateCodeVerifier() {
        return StringUtils.getRandomAlphanumeric(128);
    }

    /**
     * Distributing the device update towards the correct function
     */
    /**
     * Distributing the device update towards the correct function
     */
    private void doDeviceUpdate() {
        DeviceUpdate deviceUpdate = null;
        synchronized (deviceModificationQueue) {
            if (deviceUpdateQueueSizePeak < deviceModificationQueue.size()) {
                deviceUpdateQueueSizePeak = deviceModificationQueue.size();
                logger.trace("DIRIGERA HANDLER Peak device updates {}", deviceUpdateQueueSizePeak);
                peakRecognitionTime = Instant.now();
            }
            if (!deviceModificationQueue.isEmpty()) {
                deviceUpdate = deviceModificationQueue.remove(0);
                /**
                 * check if other link update is still in queue. If yes dismiss this request and wait for latest one
                 */
                if (deviceUpdate.action.equals(DeviceUpdate.Action.LINKS)
                        && deviceModificationQueue.contains(deviceUpdate)) {
                    deviceUpdate = null;
                    logger.warn("DIRIGERA HANDLER Dismiss link update, there's a later one scheduled");
                }
            }
        }
        if (deviceUpdate != null) {
            BaseHandler handler = deviceUpdate.handler;
            try {
                switch (deviceUpdate.action) {
                    case ADD:
                        if (handler != null) {
                            startUpdate();
                            doRegisterDevice(handler, deviceUpdate.deviceId);
                        }
                        break;
                    case DISPOSE:
                        if (handler != null) {
                            startUpdate();
                            doUnregisterDevice(handler, deviceUpdate.deviceId);
                        }
                        break;
                    case REMOVE:
                        if (handler != null) {
                            startUpdate();
                            doDeleteDevice(handler, deviceUpdate.deviceId);
                        }
                        break;
                    case LINKS:
                        startUpdate();
                        doUpdateLinks();
                        break;
                }
            } finally {
                endUpdate();
            }
        }
        if (deviceModificationQueue.isEmpty() && !Instant.MIN.equals(peakRecognitionTime)) {
            logger.trace("DIRIGERA HANDLER Peak to zero time {} ms",
                    Duration.between(peakRecognitionTime, Instant.now()).toMillis());
            peakRecognitionTime = Instant.MIN;
        }
    }

    private void startUpdate() {
        synchronized (this) {
            while (updateRunning) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            updateRunning = true;
        }
    }

    private void endUpdate() {
        synchronized (this) {
            updateRunning = false;
            this.notifyAll();
        }
    }

    @Override
    public void registerDevice(BaseHandler deviceHandler, String deviceId) {
        synchronized (deviceModificationQueue) {
            deviceModificationQueue.add(new DeviceUpdate(deviceHandler, deviceId, DeviceUpdate.Action.ADD));
        }
        scheduler.execute(this::doDeviceUpdate);
    }

    /**
     * register a running device
     */
    private void doRegisterDevice(BaseHandler deviceHandler, String deviceId) {
        logger.debug("DIRIGERA HANDLER device registered {}", deviceHandler.getThing().getThingTypeUID());
        if (!deviceId.isBlank()) {
            // if id isn't known yet - store it
            if (!knownDevices.contains(deviceId)) {
                knownDevices.add(deviceId);
                storeKnownDevices();
            }
        } else {
            logger.info("DIRIGERA HANDLER device {} regsitered without id", deviceHandler.getThing().getThingTypeUID());
        }
        deviceTree.put(deviceId, deviceHandler);
    }

    @Override
    public void unregisterDevice(BaseHandler deviceHandler, String deviceId) {
        synchronized (deviceModificationQueue) {
            deviceModificationQueue.add(new DeviceUpdate(deviceHandler, deviceId, DeviceUpdate.Action.DISPOSE));
        }
        scheduler.execute(this::doDeviceUpdate);
    }

    /**
     * unregister device, not running but still available
     */
    private void doUnregisterDevice(BaseHandler deviceHandler, String deviceId) {
        logger.debug("DIRIGERA HANDLER device unregistered {}", deviceHandler.getThing().getThingTypeUID());
        deviceTree.remove(deviceId);
        // unregister from dispose but don't remove it from known devices
    }

    @Override
    public void deleteDevice(BaseHandler deviceHandler, String deviceId) {
        synchronized (deviceModificationQueue) {
            deviceModificationQueue.add(new DeviceUpdate(deviceHandler, deviceId, DeviceUpdate.Action.REMOVE));
        }
        scheduler.execute(this::doDeviceUpdate);
    }

    /**
     * Called by all device on handleRe
     */
    private void doDeleteDevice(BaseHandler deviceHandler, String deviceId) {
        logger.warn("DIRIGERA HANDLER device deleted {}", deviceHandler.getThing().getThingTypeUID());
        deviceTree.remove(deviceId);
        // removal of handler - store known devices
        knownDevices.remove(deviceId);
        storeKnownDevices();
        // before new detection the handler needs to be removed - now were in removing state
        // for complex devices several removes are done so don't trigger detection every time
        detectionSchedule.ifPresentOrElse(previousSchedule -> {
            if (!previousSchedule.isDone()) {
                previousSchedule.cancel(true);
            }
            detectionSchedule = Optional
                    .of(scheduler.schedule(model.get()::detection, detectionTimeSeonds, TimeUnit.SECONDS));
        }, () -> {
            detectionSchedule = Optional
                    .of(scheduler.schedule(model.get()::detection, detectionTimeSeonds, TimeUnit.SECONDS));
        });
    }

    /**
     * Interface to Model called if device isn't found anymore
     */
    @Override
    public void deleteDevice(String deviceId) {
        BaseHandler activeHandler = deviceTree.remove(deviceId);
        if (activeHandler != null) {
            // if a handler is attached the check will fail and update the status to GONE
            activeHandler.checkHandler();
        }
        // removal of handler - store new known devices
        if (knownDevices.contains(deviceId)) {
            knownDevices.remove(deviceId);
            storeKnownDevices();
        }
    }

    @Override
    public DirigeraAPI api() throws ApiMissingException {
        if (api.isEmpty()) {
            throw new ApiMissingException("No API available yet");
        }
        return api.get();
    }

    @Override
    public Model model() throws ModelMissingException {
        if (model.isEmpty()) {
            throw new ModelMissingException("No Model available yet");
        }
        return model.get();
    }

    @Override
    public DirigeraDiscoveryManager discovery() {
        return discoveryManager;
    }

    @Override
    public BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public boolean isKnownDevice(String id) {
        return knownDevices.contains(id);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public DirigeraCommandProvider getCommandProvider() {
        return commandProvider;
    }

    @Override
    public TimeZoneProvider getTimeZoneProvider() {
        return timeZoneProvider;
    }

    @Override
    public String getIpAddress() {
        return config.ipAddress;
    }

    @Override
    public boolean discoveryEnabled() {
        return config.discovery;
    }

    @Override
    public void updateLinks() {
        synchronized (deviceModificationQueue) {
            deviceModificationQueue.add(LINK_UPDATE);
        }
        scheduler.execute(this::doDeviceUpdate);
    }

    private void doUpdateLinks() {
        logger.debug("DIRIGERA HANDLER ####### update links took cycle");
        Instant startTime = Instant.now();
        // first clear start update cycle, softlinks are cleared before
        synchronized (deviceTree) {
            deviceTree.forEach((id, handler) -> {
                handler.updateLinksStart();
            });
            // then update all links
            deviceTree.forEach((id, handler) -> {
                List<String> links = handler.getLinks();
                if (!links.isEmpty()) {
                    logger.trace("DIRIGERA HANDLER links found for {} {}", handler.getThing().getLabel(), links.size());
                }
                links.forEach(link -> {
                    // assure investigated handler is different from target handler
                    if (!id.equals(link)) {
                        BaseHandler targetHandler = deviceTree.get(link);
                        if (targetHandler != null) {
                            targetHandler.addSoftlink(id);
                        } else {
                            logger.trace("DIRIGERA HANDLER no targethandler found to link {} to {}", id, link);
                        }
                    }
                });
            });
            // finish update cycle so handler can update states
            deviceTree.forEach((id, handler) -> {
                handler.updateLinksDone();
            });
        }
        logger.debug("DIRIGERA HANDLER ####### update links took {}",
                Duration.between(startTime, Instant.now()).toMillis());
    }

    @Override
    public void websocketUpdate(String update) {
        synchronized (websocketQueue) {
            websocketQueue.add(update);
        }
        scheduler.execute(this::doUpdate);
    }

    private void doUpdate() {
        String json = "";
        synchronized (websocketQueue) {
            if (websocketQueueSizePeak < websocketQueue.size()) {
                websocketQueueSizePeak = websocketQueue.size();
                logger.trace("DIRIGERA HANDLER Websocket update queue size peak {}", websocketQueueSizePeak);
            }
            if (!websocketQueue.isEmpty()) {
                json = websocketQueue.remove(0);
            }
        }
        if (!json.isBlank()) {
            JSONObject update = new JSONObject(json);
            JSONObject data = update.getJSONObject("data");
            String targetId = data.getString("id");
            // First update device
            String type = update.getString(PROPERTY_TYPE);
            switch (type) {
                case EVENT_TYPE_SCENE_CREATED:
                case EVENT_TYPE_SCENE_DELETED:
                    if (data.has(PROPERTY_TYPE)) {
                        String dataType = data.getString(PROPERTY_TYPE);
                        if (dataType.equals(TYPE_CUSTOM_SCENE)) {
                            logger.trace("DIRIGERA HANDLER don't update model for customScene");
                            break;
                        }
                    }
                case EVENT_TYPE_DEVICE_ADDED:
                case EVENT_TYPE_DEVICE_REMOVED:
                    logger.trace("DIRIGERA HANDLER update {}", update.toString());
                    // update model - it will take control on newly added, changed and removed devices
                    modelUpdate();
                    break;
                case EVENT_TYPE_DEVICE_CHANGE:
                    if (targetId != null) {
                        if (targetId.equals(config.id)) {
                            this.handleUpdate(data);
                        } else {
                            BaseHandler targetHandler = deviceTree.get(targetId);
                            if (targetHandler != null) {
                                targetHandler.handleUpdate(data);
                            } else {
                                // special case: if custom name is changed in attributes force model update
                                // in order to present the updated name in discovery
                                if (data.has(PROPERTY_ATTRIBUTES)) {
                                    JSONObject attributes = data.getJSONObject(PROPERTY_ATTRIBUTES);
                                    if (attributes.has(Model.CUSTOM_NAME)) {
                                        logger.info("DIRIGERA HANDLER possible device name change detected {}",
                                                attributes.getString(Model.CUSTOM_NAME));
                                        modelUpdate();
                                    }
                                }
                            }
                        }
                    }
                    // logger.info("DIRIGERA HANDLER update performance - device update {}",
                    // Duration.between(startTime, Instant.now()).toMillis());
                    break;
                case EVENT_TYPE_SCENE_UPDATE:
                    if (targetId != null) {
                        BaseHandler targetHandler = deviceTree.get(targetId);
                        if (targetHandler != null) {
                            targetHandler.handleUpdate(data);
                        } else {
                            // special case: if custom name is changed in attributes force model update
                            // in order to present the updated name in discovery
                            if (data.has(PROPERTY_ATTRIBUTES)) {
                                JSONObject attributes = data.getJSONObject(PROPERTY_ATTRIBUTES);
                                if (attributes.has(Model.CUSTOM_NAME)) {
                                    logger.info("DIRIGERA HANDLER possible scene name change detected {}",
                                            attributes.getString(Model.CUSTOM_NAME));
                                    modelUpdate();
                                }
                            }
                        }
                    }
                    break;
                default:
                    logger.info("DIRIGERA HANDLER unkown type {} for websocket update {}", type, update);
            }
        }
    }

    private void modelUpdate() {
        Instant modelUpdateStartTime = Instant.now();
        int status = model().update();
        if (status != 200) {
            logger.error("DIRIGERA HANDLER Model update failed {}", status);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/dirigera.gateway.status.comm-error" + " [\"" + status + "\"]");
            return;
        }
        long durationUpdateTime = Duration.between(modelUpdateStartTime, Instant.now()).toMillis();
        websocket.get().increase(Websocket.MODEL_UPDATES);
        websocket.get().getStatistics().put(Websocket.MODEL_UPDATE_TIME, durationUpdateTime + " ms");
        websocket.get().getStatistics().put(Websocket.MODEL_UPDATE_LAST,
                Instant.now().atZone(timeZoneProvider.getTimeZone()));
        updateState(new ChannelUID(thing.getUID(), CHANNEL_JSON), StringType.valueOf(model().getModelString()));
    }

    private void handleUpdate(JSONObject data) {
        if (data.has(Model.ATTRIBUTES)) {
            JSONObject attributes = data.getJSONObject(Model.ATTRIBUTES);
            // check ota for each device
            if (attributes.has(PROPERTY_CUSTOM_NAME)) {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_CUSTOM_NAME),
                        StringType.valueOf(attributes.getString(PROPERTY_CUSTOM_NAME)));
            }
            if (attributes.has(PROPERTY_PERMIT_JOIN)) {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_PAIRING),
                        OnOffType.from(attributes.getBoolean(PROPERTY_PERMIT_JOIN)));
            }
            if (attributes.has(PROPERTY_OTA_STATUS)) {
                String otaStatusString = attributes.getString(PROPERTY_OTA_STATUS);
                if (OTA_STATUS_MAP.containsKey(otaStatusString)) {
                    Integer otaStatus = OTA_STATUS_MAP.get(otaStatusString);
                    if (otaStatus != null) {
                        updateState(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATUS), new DecimalType(otaStatus));
                    } else {
                        logger.warn("Cannot decode ota status {}", otaStatusString);
                    }
                } else {
                    logger.warn("Cannot decode ota status {}", otaStatusString);
                }
            }
            if (attributes.has(PROPERTY_OTA_STATE)) {
                String otaStateString = attributes.getString(PROPERTY_OTA_STATE);
                if (OTA_STATE_MAP.containsKey(otaStateString)) {
                    Integer otaState = OTA_STATE_MAP.get(otaStateString);
                    if (otaState != null) {
                        updateState(new ChannelUID(thing.getUID(), CHANNEL_OTA_STATE), new DecimalType(otaState));
                    } else {
                        logger.warn("Cannot decode ota state {}", otaStateString);
                    }
                } else {
                    logger.warn("Cannot decode ota state {}", otaStateString);
                }
            }
            if (attributes.has(PROPERTY_OTA_PROGRESS)) {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_OTA_PROGRESS),
                        QuantityType.valueOf(attributes.getInt(PROPERTY_OTA_PROGRESS), Units.PERCENT));
            }
            // sunrise & sunset
            if (!attributes.isNull("nextSunRise")) {
                String sunRiseString = attributes.getString("nextSunRise");
                if (sunRiseString != null) {
                    sunriseInstant = Instant.parse(sunRiseString);
                    logger.debug("SunriseTest at Zone {} {}", timeZoneProvider.getTimeZone(),
                            sunriseInstant.atZone(timeZoneProvider.getTimeZone()));
                    updateState(new ChannelUID(thing.getUID(), CHANNEL_SUNRISE),
                            new DateTimeType(sunriseInstant.atZone(timeZoneProvider.getTimeZone())));
                }
            } else {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_SUNRISE), UnDefType.UNDEF);
            }
            if (!attributes.isNull("nextSunSet")) {
                String sunsetString = attributes.getString("nextSunSet");
                if (sunsetString != null) {
                    sunsetInstant = Instant.parse(attributes.getString("nextSunSet"));
                    updateState(new ChannelUID(thing.getUID(), CHANNEL_SUNSET),
                            new DateTimeType(sunsetInstant.atZone(timeZoneProvider.getTimeZone())));
                }
            } else {
                updateState(new ChannelUID(thing.getUID(), CHANNEL_SUNSET), UnDefType.UNDEF);
            }
        }
    }

    @Override
    public @Nullable ZonedDateTime getSunriseDateTime() {
        if (sunriseInstant.equals(Instant.MAX)) {
            return null;
        }
        return sunriseInstant.atZone(timeZoneProvider.getTimeZone());
    }

    @Override
    public @Nullable ZonedDateTime getSunsetDateTime() {
        if (sunsetInstant.equals(Instant.MIN)) {
            return null;
        }
        return sunsetInstant.atZone(timeZoneProvider.getTimeZone());
    }
}
