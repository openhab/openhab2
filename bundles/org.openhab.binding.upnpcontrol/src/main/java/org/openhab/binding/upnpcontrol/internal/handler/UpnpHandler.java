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
package org.openhab.binding.upnpcontrol.internal.handler;

import static org.openhab.binding.upnpcontrol.internal.UpnpControlBindingConstants.BINDING_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.registry.RegistryListener;
import org.openhab.binding.upnpcontrol.internal.UpnpChannelName;
import org.openhab.binding.upnpcontrol.internal.UpnpChannelTypeProvider;
import org.openhab.binding.upnpcontrol.internal.UpnpDynamicCommandDescriptionProvider;
import org.openhab.binding.upnpcontrol.internal.UpnpDynamicStateDescriptionProvider;
import org.openhab.binding.upnpcontrol.internal.config.UpnpControlBindingConfiguration;
import org.openhab.binding.upnpcontrol.internal.config.UpnpControlConfiguration;
import org.openhab.binding.upnpcontrol.internal.queue.UpnpPlaylistsListener;
import org.openhab.binding.upnpcontrol.internal.util.UpnpControlUtil;
import org.openhab.core.common.ThreadPoolManager;
import org.openhab.core.io.transport.upnp.UpnpIOParticipant;
import org.openhab.core.io.transport.upnp.UpnpIOService;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelType;
import org.openhab.core.thing.type.ChannelTypeBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.CommandDescription;
import org.openhab.core.types.CommandDescriptionBuilder;
import org.openhab.core.types.CommandOption;
import org.openhab.core.types.StateDescription;
import org.openhab.core.types.StateDescriptionFragmentBuilder;
import org.openhab.core.types.StateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UpnpHandler} is the base class for {@link UpnpRendererHandler} and {@link UpnpServerHandler}. The base
 * class implements UPnPConnectionManager service actions.
 *
 * @author Mark Herwege - Initial contribution
 * @author Karel Goderis - Based on UPnP logic in Sonos binding
 */
@NonNullByDefault
public abstract class UpnpHandler extends BaseThingHandler implements UpnpIOParticipant, UpnpPlaylistsListener {

    private final Logger logger = LoggerFactory.getLogger(UpnpHandler.class);

    // UPnP constants
    protected static final Pattern PROTOCOL_PATTERN = Pattern.compile("(?:.*):(?:.*):(.*):(?:.*)");

    protected UpnpIOService upnpIOService;

    protected volatile @Nullable RemoteDevice device;

    // The handlers can potentially create an important number of tasks, therefore put them in a separate thread pool
    protected ScheduledExecutorService upnpScheduler = ThreadPoolManager.getScheduledPool("binding-upnpcontrol");

    private boolean updateChannels;
    private final List<Channel> updatedChannels = new ArrayList<>();
    private final List<ChannelUID> updatedChannelUIDs = new ArrayList<>();

    protected volatile int connectionId = 0; // UPnP Connection Id
    protected volatile int avTransportId = 0; // UPnP AVTtransport Id
    protected volatile int rcsId = 0; // UPnP Rendering Control Id

    protected UpnpControlBindingConfiguration bindingConfig;
    protected UpnpControlConfiguration config;

    protected final Object invokeActionLock = new Object();

    protected @Nullable ScheduledFuture<?> pollingJob;
    protected final Object jobLock = new Object();

    protected volatile @Nullable CompletableFuture<Boolean> isConnectionIdSet;
    protected volatile @Nullable CompletableFuture<Boolean> isAvTransportIdSet;
    protected volatile @Nullable CompletableFuture<Boolean> isRcsIdSet;

    protected static final int SUBSCRIPTION_DURATION_SECONDS = 3600;
    protected List<String> serviceSubscriptions = new ArrayList<>();
    protected volatile @Nullable ScheduledFuture<?> subscriptionRefreshJob;
    protected final Runnable subscriptionRefresh = () -> {
        for (String subscription : serviceSubscriptions) {
            removeSubscription(subscription);
            addSubscription(subscription, SUBSCRIPTION_DURATION_SECONDS);
        }
    };
    protected volatile boolean upnpSubscribed;

    protected UpnpDynamicStateDescriptionProvider upnpStateDescriptionProvider;
    protected UpnpDynamicCommandDescriptionProvider upnpCommandDescriptionProvider;

    protected @Nullable UpnpChannelTypeProvider channelTypeProvider;

    public UpnpHandler(Thing thing, UpnpIOService upnpIOService, UpnpControlBindingConfiguration configuration,
            UpnpDynamicStateDescriptionProvider upnpStateDescriptionProvider,
            UpnpDynamicCommandDescriptionProvider upnpCommandDescriptionProvider) {
        super(thing);

        this.upnpIOService = upnpIOService;

        this.bindingConfig = configuration;

        this.upnpStateDescriptionProvider = upnpStateDescriptionProvider;
        this.upnpCommandDescriptionProvider = upnpCommandDescriptionProvider;

        // Get this in constructor, so the UDN is immediately available from the config. The concrete classes should
        // update the config from the initialize method.
        config = getConfigAs(UpnpControlConfiguration.class);
    }

    @Override
    public void initialize() {
        upnpIOService.registerParticipant(this);
        // This action should exist on all media devices and return a result, so a good candidate for testing.
        upnpIOService.addStatusListener(this, "ConnectionManager", "GetCurrentConnectionIDs", config.refresh);

        UpnpControlUtil.updatePlaylistsList(bindingConfig.path);
        UpnpControlUtil.playlistsSubscribe(this);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(UpnpChannelTypeProvider.class);
    }

    @Override
    public void dispose() {
        removeSubscriptions();
        cancelPollingJob();

        UpnpControlUtil.playlistsUnsubscribe(this);

        CompletableFuture<Boolean> connectionIdFuture = isConnectionIdSet;
        if (connectionIdFuture != null) {
            connectionIdFuture.complete(false);
            isConnectionIdSet = null;
        }
        CompletableFuture<Boolean> avTransportIdFuture = isAvTransportIdSet;
        if (avTransportIdFuture != null) {
            avTransportIdFuture.complete(false);
            isAvTransportIdSet = null;
        }
        CompletableFuture<Boolean> rcsIdFuture = isRcsIdSet;
        if (rcsIdFuture != null) {
            rcsIdFuture.complete(false);
            isRcsIdSet = null;
        }

        updateChannels = false;
        updatedChannels.clear();
        updatedChannelUIDs.clear();

        upnpIOService.removeStatusListener(this);
        upnpIOService.unregisterParticipant(this);
    }

    private void cancelPollingJob() {
        ScheduledFuture<?> job = pollingJob;

        if (job != null) {
            job.cancel(true);
        }
        pollingJob = null;
    }

    /**
     * To be called from implementing classes when initializing the device, to start initialization refresh
     */
    protected void initDevice() {
        String udn = getUDN();
        if ((udn != null) && !udn.isEmpty()) {
            if (config.refresh == 0) {
                upnpScheduler.submit(this::initJob);
            } else {
                pollingJob = upnpScheduler.scheduleWithFixedDelay(this::initJob, 0, config.refresh, TimeUnit.SECONDS);
            }

            if (!upnpIOService.isRegistered(this)) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "UPnP device with UDN " + getUDN() + " not yet registered");
                return;
            }
            if (!upnpSubscribed) {
                addSubscriptions();
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "No UDN configured for " + thing.getLabel());
        }
    }

    /**
     * Job to be executed in an asynchronous process when initializing a device. This checks if the connection id's are
     * correctly set up for the connection. It can also be called from a polling job to get the thing back online when
     * connection is lost.
     */
    protected abstract void initJob();

    /**
     * Method called when a the remote device represented by the thing for this handler is added to the jupnp
     * {@link RegistryListener} or is updated. Configuration info can be retrieved from the {@link RemoteDevice}.
     *
     * @param device
     */
    public void updateDeviceConfig(RemoteDevice device) {
        this.device = device;
    };

    public void setChannelTypeProvider(final UpnpChannelTypeProvider channelTypeProvider) {
        this.channelTypeProvider = channelTypeProvider;
    }

    protected void updateStateDescription(ChannelUID channelUID, List<StateOption> stateOptionList) {
        StateDescription stateDescription = StateDescriptionFragmentBuilder.create().withReadOnly(false)
                .withOptions(stateOptionList).build().toStateDescription();
        upnpStateDescriptionProvider.setDescription(channelUID, stateDescription);
    }

    protected void updateCommandDescription(ChannelUID channelUID, List<CommandOption> commandOptionList) {
        CommandDescription commandDescription = CommandDescriptionBuilder.create().withCommandOptions(commandOptionList)
                .build();
        upnpCommandDescriptionProvider.setDescription(channelUID, commandDescription);
    }

    protected void createChannel(@Nullable UpnpChannelName upnpChannelName) {
        if ((upnpChannelName != null)) {
            createChannel(upnpChannelName.getChannelId(), upnpChannelName.getLabel(), upnpChannelName.getItemType(),
                    upnpChannelName.getDescription(), upnpChannelName.getCategory(), upnpChannelName.isAdvanced());
        }
    }

    protected void createChannel(String channelId, String label, String itemType, String description, String category,
            boolean advanced) {
        UpnpChannelTypeProvider localChannelTypeProvider = channelTypeProvider;
        if (localChannelTypeProvider == null) {
            return;
        }

        ChannelUID channelUID = new ChannelUID(thing.getUID(), channelId);

        if (thing.getChannel(channelUID) != null) {
            // channel already exists
            return;
        }

        ChannelTypeUID channelTypeUID = new ChannelTypeUID(BINDING_ID, channelUID.getId());
        ChannelType channelType = ChannelTypeBuilder.state(channelTypeUID, label, itemType).withDescription(description)
                .withCategory(category).isAdvanced(advanced).build();
        localChannelTypeProvider.addChannelType(channelType);
        Channel channel = ChannelBuilder.create(channelUID, itemType).withType(channelTypeUID).build();
        logger.debug("UPnP device {}, created channel {}", thing.getLabel(), channelId);

        updatedChannels.add(channel);
        updatedChannelUIDs.add(channelUID);
        updateChannels = true;
    }

    protected void updateChannels() {
        if (updateChannels) {
            List<Channel> channels = thing.getChannels().stream().filter(c -> !updatedChannelUIDs.contains(c.getUID()))
                    .collect(Collectors.toList());
            channels.addAll(updatedChannels);
            final ThingBuilder thingBuilder = editThing();
            thingBuilder.withChannels(channels);
            updateThing(thingBuilder.build());
        }
        updatedChannels.clear();
        updatedChannelUIDs.clear();
        updateChannels = false;
    }

    /**
     * Invoke PrepareForConnection on the UPnP Connection Manager.
     * Result is received in {@link onValueReceived}.
     *
     * @param remoteProtocolInfo
     * @param peerConnectionManager
     * @param peerConnectionId
     * @param direction
     */
    protected void prepareForConnection(String remoteProtocolInfo, String peerConnectionManager, int peerConnectionId,
            String direction) {
        CompletableFuture<Boolean> settingConnection = isConnectionIdSet;
        CompletableFuture<Boolean> settingAVTransport = isAvTransportIdSet;
        CompletableFuture<Boolean> settingRcs = isRcsIdSet;
        if (settingConnection != null) {
            settingConnection.complete(false);
        }
        if (settingAVTransport != null) {
            settingAVTransport.complete(false);
        }
        if (settingRcs != null) {
            settingRcs.complete(false);
        }

        // Set new futures, so we don't try to use service when connection id's are not known yet
        isConnectionIdSet = new CompletableFuture<Boolean>();
        isAvTransportIdSet = new CompletableFuture<Boolean>();
        isRcsIdSet = new CompletableFuture<Boolean>();

        HashMap<String, String> inputs = new HashMap<String, String>();
        inputs.put("RemoteProtocolInfo", remoteProtocolInfo);
        inputs.put("PeerConnectionManager", peerConnectionManager);
        inputs.put("PeerConnectionID", Integer.toString(peerConnectionId));
        inputs.put("Direction", direction);

        invokeAction("ConnectionManager", "PrepareForConnection", inputs);
    }

    /**
     * Invoke ConnectionComplete on UPnP Connection Manager.
     */
    protected void connectionComplete() {
        Map<String, String> inputs = Collections.singletonMap("ConnectionId", Integer.toString(connectionId));

        invokeAction("ConnectionManager", "ConnectionComplete", inputs);
    }

    /**
     * Invoke GetCurrentConnectionIDs on the UPnP Connection Manager.
     * Result is received in {@link onValueReceived}.
     */
    protected void getCurrentConnectionIDs() {
        Map<String, String> inputs = Collections.emptyMap();

        invokeAction("ConnectionManager", "GetCurrentConnectionIDs", inputs);
    }

    /**
     * Invoke GetCurrentConnectionInfo on the UPnP Connection Manager.
     * Result is received in {@link onValueReceived}.
     */
    protected void getCurrentConnectionInfo() {
        CompletableFuture<Boolean> settingAVTransport = isAvTransportIdSet;
        CompletableFuture<Boolean> settingRcs = isRcsIdSet;
        if (settingAVTransport != null) {
            settingAVTransport.complete(false);
        }
        if (settingRcs != null) {
            settingRcs.complete(false);
        }

        // Set new futures, so we don't try to use service when connection id's are not known yet
        isAvTransportIdSet = new CompletableFuture<Boolean>();
        isRcsIdSet = new CompletableFuture<Boolean>();

        // ConnectionID will default to 0 if not set through prepareForConnection method
        Map<String, String> inputs = Collections.singletonMap("ConnectionId", Integer.toString(connectionId));

        invokeAction("ConnectionManager", "GetCurrentConnectionInfo", inputs);
    }

    /**
     * Invoke GetFeatureList on the UPnP Connection Manager.
     * Result is received in {@link onValueReceived}.
     */
    protected void getFeatureList() {
        Map<String, String> inputs = Collections.emptyMap();

        invokeAction("ConnectionManager", "GetFeatureList", inputs);
    }

    /**
     * Invoke GetProtocolInfo on UPnP Connection Manager.
     * Result is received in {@link onValueReceived}.
     */
    protected void getProtocolInfo() {
        Map<String, String> inputs = Collections.emptyMap();

        invokeAction("ConnectionManager", "GetProtocolInfo", inputs);
    }

    @Override
    public void onServiceSubscribed(@Nullable String service, boolean succeeded) {
        logger.debug("UPnP device {} received subscription reply {} from service {}", thing.getLabel(), succeeded,
                service);
        if (succeeded) {
            updateStatus(ThingStatus.ONLINE);
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Could not subscribe to service " + service + "for" + thing.getLabel());
        }
    }

    @Override
    public void onStatusChanged(boolean status) {
        logger.debug("UPnP device {} received status update {}", thing.getLabel(), status);
        if (status) {
            initJob();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Communication lost with " + thing.getLabel());
        }
    }

    /**
     * This method wraps {@link org.openhab.core.io.transport.upnp.UpnpIOService.invokeAction}. It schedules and
     * submits the call and calls {@link onValueReceived} upon completion. All state updates or other actions depending
     * on the results should be triggered from {@link onValueReceived} because the class fields with results will be
     * filled asynchronously.
     *
     * @param serviceId
     * @param actionId
     * @param inputs
     */
    protected void invokeAction(String serviceId, String actionId, Map<String, String> inputs) {
        upnpScheduler.submit(() -> {
            Map<String, String> result;
            synchronized (invokeActionLock) {
                if (logger.isDebugEnabled() && !"GetPositionInfo".equals(actionId)) {
                    // don't log position info refresh every second
                    logger.debug("UPnP device {} invoke upnp action {} on service {} with inputs {}", thing.getLabel(),
                            actionId, serviceId, inputs);
                }
                result = upnpIOService.invokeAction(this, serviceId, actionId, inputs);
                if (logger.isDebugEnabled() && !"GetPositionInfo".equals(actionId)) {
                    // don't log position info refresh every second
                    logger.debug("UPnP device {} invoke upnp action {} on service {} reply {}", thing.getLabel(),
                            actionId, serviceId, result);
                }

                if (!result.isEmpty()) {
                    // We can be sure a non-empty result means the device is online.
                    // An empty result could be expected for certain actions, but could also be hiding an exception.
                    updateStatus(ThingStatus.ONLINE);
                }

                result = preProcessInvokeActionResult(inputs, serviceId, actionId, result);
            }
            for (String variable : result.keySet()) {
                onValueReceived(variable, result.get(variable), serviceId);
            }
        });
    }

    /**
     * Some received values need info on inputs of action. Therefore we allow to pre-process in a separate step. The
     * method will return an adjusted result list. The default implementation will copy over the received result without
     * additional processing. Derived classes can add additional logic.
     *
     * @param inputs
     * @param service
     * @param result
     * @return
     */
    protected Map<String, String> preProcessInvokeActionResult(Map<String, String> inputs, @Nullable String service,
            @Nullable String action, Map<String, String> result) {
        Map<String, String> newResult = new HashMap<>();
        for (String variable : result.keySet()) {
            String newVariable = preProcessValueReceived(inputs, variable, result.get(variable), service, action);
            if (newVariable != null) {
                newResult.put(newVariable, result.get(variable));
            }
        }
        return newResult;
    }

    /**
     * Some received values need info on inputs of action. Therefore we allow to pre-process in a separate step. The
     * default implementation will return the original value. Derived classes can implement additional logic.
     *
     * @param inputs
     * @param variable
     * @param value
     * @param service
     * @return
     */
    protected @Nullable String preProcessValueReceived(Map<String, String> inputs, @Nullable String variable,
            @Nullable String value, @Nullable String service, @Nullable String action) {
        return variable;
    }

    @Override
    public void onValueReceived(@Nullable String variable, @Nullable String value, @Nullable String service) {
        if (variable == null || value == null) {
            return;
        }
        switch (variable) {
            case "ConnectionID":
                onValueReceivedConnectionId(value);
                break;
            case "AVTransportID":
                onValueReceivedAVTransportId(value);
                break;
            case "RcsID":
                onValueReceivedRcsId(value);
                break;
            case "Source":
            case "Sink":
                if (!((value == null) || (value.isEmpty()))) {
                    updateProtocolInfo(value);
                }
                break;
            default:
                break;
        }
    }

    private void onValueReceivedConnectionId(@Nullable String value) {
        try {
            connectionId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            connectionId = 0;
        }
        CompletableFuture<Boolean> connectionIdFuture = isConnectionIdSet;
        if (connectionIdFuture != null) {
            connectionIdFuture.complete(true);
        }
    }

    private void onValueReceivedAVTransportId(@Nullable String value) {
        try {
            avTransportId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            avTransportId = 0;
        }
        CompletableFuture<Boolean> avTransportIdFuture = isAvTransportIdSet;
        if (avTransportIdFuture != null) {
            avTransportIdFuture.complete(true);
        }
    }

    private void onValueReceivedRcsId(@Nullable String value) {
        try {
            rcsId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            rcsId = 0;
        }
        CompletableFuture<Boolean> rcsIdFuture = isRcsIdSet;
        if (rcsIdFuture != null) {
            rcsIdFuture.complete(true);
        }
    }

    @Override
    public @Nullable String getUDN() {
        return config.udn;
    }

    protected boolean checkForConnectionIds() {
        return checkForConnectionId(isConnectionIdSet) & checkForConnectionId(isAvTransportIdSet)
                & checkForConnectionId(isRcsIdSet);
    }

    private boolean checkForConnectionId(@Nullable CompletableFuture<Boolean> future) {
        try {
            if (future != null) {
                return future.get(config.responsetimeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * Update internal representation of supported protocols, needs to be implemented in derived classes.
     *
     * @param value
     */
    protected abstract void updateProtocolInfo(String value);

    /**
     * Subscribe this handler as a participant to a GENA subscription.
     *
     * @param serviceId
     * @param duration
     */
    protected void addSubscription(String serviceId, int duration) {
        if (upnpIOService.isRegistered(this)) {
            logger.debug("UPnP device {} add upnp subscription on {}", thing.getLabel(), serviceId);
            upnpIOService.addSubscription(this, serviceId, duration);
        }
    }

    /**
     * Remove this handler from the GENA subscriptions.
     *
     * @param serviceId
     */
    protected void removeSubscription(String serviceId) {
        if (upnpIOService.isRegistered(this)) {
            upnpIOService.removeSubscription(this, serviceId);
        }
    }

    protected void addSubscriptions() {
        for (String subscription : serviceSubscriptions) {
            addSubscription(subscription, SUBSCRIPTION_DURATION_SECONDS);
        }
        subscriptionRefreshJob = upnpScheduler.scheduleWithFixedDelay(subscriptionRefresh,
                SUBSCRIPTION_DURATION_SECONDS / 2, SUBSCRIPTION_DURATION_SECONDS / 2, TimeUnit.SECONDS);

        upnpSubscribed = true;
    }

    protected void removeSubscriptions() {
        cancelSubscriptionRefreshJob();

        for (String subscription : serviceSubscriptions) {
            removeSubscription(subscription);
        }

        upnpSubscribed = false;
    }

    private void cancelSubscriptionRefreshJob() {
        ScheduledFuture<?> refreshJob = subscriptionRefreshJob;

        if (refreshJob != null) {
            refreshJob.cancel(true);
        }
        subscriptionRefreshJob = null;
    }

    @Override
    public abstract void playlistsListChanged();

    /**
     * Get access to all device info through the UPnP {@link RemoteDevice}.
     *
     * @return UPnP RemoteDevice
     */
    protected @Nullable RemoteDevice getDevice() {
        return device;
    }
}
