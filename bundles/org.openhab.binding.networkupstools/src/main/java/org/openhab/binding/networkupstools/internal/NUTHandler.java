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
package org.openhab.binding.networkupstools.internal;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.cache.ExpiringCache;
import org.eclipse.smarthome.core.library.CoreItemFactory;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerService;
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.networkupstools.internal.NUTBindingConstants.Parameters;
import org.openhab.binding.networkupstools.internal.nut.NutApi;
import org.openhab.binding.networkupstools.internal.nut.NutException;
import org.openhab.binding.networkupstools.internal.nut.NutFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link NUTHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Hilbrand Bouwkamp - Initial contribution
 */
@NonNullByDefault
public class NUTHandler extends BaseThingHandler {
    private static final String METADATA_NETWORKUPSTOOLS = "networkupstools";
    private static final int REFRESH_RATE_SECONDS = 3;

    private final Logger logger = LoggerFactory.getLogger(NUTHandler.class);
    /**
     * Map to cache user configured channels with their configuration. Channels are dynamically created at
     * initialization phase of the thing.
     */
    private final Map<ChannelUID, @Nullable NUTDynamicChannelConfiguration> userChannelToNutMap = new HashMap<>();
    /**
     * Cache of the UPS status. When expired makes a call to the NUT server is done to get the actual status. Expires at
     * the
     * short time refresh rate. Used to avoid triggering multiple calls to the server in a short time frame.
     */
    private final ExpiringCache<String> upsStatusCache = new ExpiringCache<>(Duration.ofSeconds(REFRESH_RATE_SECONDS),
            this::retrieveUpsStatus);
    /**
     * Cache of the NUT variables. When expired makes a call to the NUT server is done to get the variables. Expires at
     * the short time refresh rate. Used to avoid triggering multiple calls to the server in a short time frame.
     */
    private final ExpiringCache<Map<String, String>> variablesCache = new ExpiringCache<>(
            Duration.ofSeconds(REFRESH_RATE_SECONDS), this::retrieveVariables);
    /**
     * Cache used to manage update frequency of the thing properties. The properties are NUT variables that don't
     * change much or not at all. So updating can be done on a larger time frame. A call to get this cache will trigger
     * updating the properties when the cache is expired.
     */
    private final ExpiringCache<Boolean> refreshPropertiesCache = new ExpiringCache<>(Duration.ofHours(1),
            this::updateProperties);
    private final ChannelUID upsStatusChannelUID;

    private @Nullable NUTChannelTypeProvider channelTypeProvider;
    private @Nullable NUTDynamicChannelFactory dynamicChannelFactory;
    private @Nullable NUTConfiguration config;
    private @Nullable ScheduledFuture<?> poller;
    /**
     * Cache used to manage the update frequency of the thing channels. The channels are updated based on the user
     * configured refresh rate. The cache is called in the status update scheduled task and when the cache expires at
     * the user configured refresh rate it will trigger an update of the channels. This way no separate scheduled task
     * is needed.
     */
    private @Nullable ExpiringCache<Boolean> refreshVariablesCache;
    private @Nullable NutApi nutApi;

    /**
     * Keep track of the last ups status to avoid updating the status every 3 seconds when nothing changed.
     */
    private String lastUpsStatus = "";

    public NUTHandler(final Thing thing) {
        super(thing);
        upsStatusChannelUID = new ChannelUID(getThing().getUID(), NutName.UPS_STATUS.getChannelId());
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(NUTChannelTypeProvider.class);
    }

    public void setChannelTypeProvider(final NUTChannelTypeProvider channelTypeProvider) {
        this.channelTypeProvider = channelTypeProvider;
        dynamicChannelFactory = new NUTDynamicChannelFactory(channelTypeProvider);
    }

    @Override
    public void handleCommand(final ChannelUID channelUID, final Command command) {
        if (command instanceof RefreshType) {
            final Channel channel = getThing().getChannel(channelUID);

            if (channel == null) {
                logger.info("Trying to update a none existing channel: {}", channelUID);
            } else {
                updateChannel(channel, variablesCache.getValue());
            }
        }
    }

    @Override
    public void initialize() {
        final NUTConfiguration config = getConfigAs(NUTConfiguration.class);

        this.config = config;
        updateStatus(ThingStatus.UNKNOWN);
        initDynamicChannels();
        poller = scheduler.scheduleWithFixedDelay(this::refreshStatus, 0, REFRESH_RATE_SECONDS, TimeUnit.SECONDS);
        refreshVariablesCache = new ExpiringCache<>(Duration.ofSeconds(config.refresh), this::updateRefreshVariables);
        nutApi = new NutApi(config.host, config.port, config.username, config.password);
    }

    @Override
    public void dispose() {
        if (nutApi != null) {
            nutApi.close();
        }
        final ScheduledFuture<?> localPoller = poller;

        if (localPoller != null && !localPoller.isCancelled()) {
            localPoller.cancel(true);
            poller = null;
        }
    }

    /**
     * Initializes any channels configured by the user by creating complementary channel types and recreate the channels
     * of the thing.
     */
    private void initDynamicChannels() {
        final NUTChannelTypeProvider localChannelTypeProvider = channelTypeProvider;
        final NUTDynamicChannelFactory localDynamicChannelFactory = dynamicChannelFactory;

        if (localChannelTypeProvider == null || localDynamicChannelFactory == null) {
            return;
        }
        final List<Channel> updatedChannels = new ArrayList<>();
        boolean rebuildChannels = false;

        for (final Channel channel : thing.getChannels()) {
            if (channel.getConfiguration().get(METADATA_NETWORKUPSTOOLS) == null) {
                updatedChannels.add(channel);
            } else {
                rebuildChannels = true;
                final NUTDynamicChannelConfiguration channelConfig = channel.getConfiguration()
                        .as(NUTDynamicChannelConfiguration.class);
                final Channel dynamicChannel = localDynamicChannelFactory.createChannel(channel, channelConfig);

                if (dynamicChannel == null) {
                    logger.debug("Could not initialize the dynamic channel '{}'. This channel will be ignored ",
                            channel.getUID());
                } else {
                    logger.debug("Updating channel '{}' with dynamic channelType settings: {}", channel.getUID(),
                            channel.getChannelTypeUID());
                    updatedChannels.add(dynamicChannel);
                    userChannelToNutMap.put(channel.getUID(), channelConfig);
                }
            }
        }
        if (rebuildChannels) {
            final ThingBuilder thingBuilder = editThing();
            thingBuilder.withChannels(updatedChannels);
            updateThing(thingBuilder.build());
        }
    }

    /**
     * Method called by the scheduled task that checks for the active status of the ups.
     */
    private void refreshStatus() {
        try {
            final String state = upsStatusCache.getValue();
            final ExpiringCache<Boolean> localVariablesRefreshCache = refreshVariablesCache;

            if (!lastUpsStatus.equals(state)) {
                if (isLinked(upsStatusChannelUID)) {
                    updateState(upsStatusChannelUID, state == null ? UnDefType.UNDEF : new StringType(state));
                }
                lastUpsStatus = state == null ? "" : state;
                if (localVariablesRefreshCache != null) {
                    localVariablesRefreshCache.invalidateValue();
                }
            }
            // Just call a get on variables. If the cache is expired it will trigger an update of the channels.
            if (localVariablesRefreshCache != null) {
                localVariablesRefreshCache.getValue();
            }
        } catch (final RuntimeException e) {
            logger.debug("Updating ups status failed: ", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    /**
     * This method is triggered when the cache {@link #refreshVariablesCache} is expired.
     *
     * @return returns true if success and false on error
     */
    private boolean updateRefreshVariables() {
        logger.trace("Calling updateRefreshVariables {}", thing.getUID());
        try {
            final Map<String, String> variables = variablesCache.getValue();

            if (variables == null) {
                logger.trace("No data from NUT server received.");
                return false;
            }
            logger.trace("Updating status of linked channels.");
            for (final Channel channel : getThing().getChannels()) {
                final ChannelUID uid = channel.getUID();

                if (isLinked(uid)) {
                    updateChannel(channel, variables);
                }
            }
            // Call getValue to trigger cache refreshing
            refreshPropertiesCache.getValue();
            if ((thing.getStatus() == ThingStatus.OFFLINE || thing.getStatus() == ThingStatus.UNKNOWN)
                    && thing.getStatus() != ThingStatus.ONLINE) {
                updateStatus(ThingStatus.ONLINE);
            }
            return true;
        } catch (final RuntimeException e) {
            logger.debug("Refresh Network UPS Tools failed: ", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            return false;
        }
    }

    /**
     * Method that retrieves the ups status from the NUT server.
     *
     * @return status of the UPS or null if it couldn't be determined
     */
    private @Nullable String retrieveUpsStatus() {
        logger.trace("Get UPS status from server for thing: {}({})", thing.getLabel(), thing.getUID());
        final NutApi localNutApi = nutApi;

        if (localNutApi == null) {
            return null;
        }
        return wrappedNutApiCall(device -> localNutApi.getVariable(device, NutName.UPS_STATUS.getName()));
    }

    /**
     * Method that retrieves all variables from the NUT server.
     *
     * @return variables retrieved send by the NUT server or null if it couldn't be determined
     */
    private @Nullable Map<String, String> retrieveVariables() {
        logger.trace("Get NUT variables from server for thing: {}({})", thing.getLabel(), thing.getUID());
        final NutApi localNutApi = nutApi;

        if (localNutApi == null) {
            return null;
        }
        return wrappedNutApiCall(localNutApi::getVariables);
    }

    /**
     * Convenience method that wraps the call to the api and handles exceptions.
     *
     * @param <T> Return type of the call to the api
     * @param nutApiFunction function that will be called
     * @return the value returned by the api call or null in case of an error
     */
    private <T> T wrappedNutApiCall(final NutFunction<String, T> nutApiFunction) {
        try {
            final NUTConfiguration localConfig = config;

            if (localConfig == null) {
                return null;
            }
            return nutApiFunction.apply(localConfig.device);
        } catch (final NutException e) {
            logger.debug("Refresh Network UPS Tools failed: ", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
            return null;
        }
    }

    /**
     * Updates the thing properties when the cache {@link #refreshPropertiesCache} is expired.
     *
     * @return returns true if success and false on error
     */
    private Boolean updateProperties() {
        try {
            final Map<String, String> variables = variablesCache.getValue();

            if (variables != null) {
                final Map<String, String> properties = editProperties();

                for (final Parameters param : NUTBindingConstants.Parameters.values()) {
                    final String value = deviceToString(variables, param.getNutName());

                    if (value != null) {
                        properties.put(param.getNutName(), value);
                    }
                }
                updateProperties(properties);
            }
            return Boolean.TRUE;
        } catch (final RuntimeException e) {
            logger.debug("Updating parameters failed: ", e);
            return Boolean.FALSE;
        }
    }

    private void updateChannel(final Channel channel, @Nullable final Map<String, String> variables) {
        try {
            if (variables == null) {
                return;
            }
            final State state;
            final String id = channel.getUID().getId();
            final NutName fixedChannel = NutName.channelIdToNutName(id);

            if (fixedChannel == null) {
                state = getDynamicChannelState(channel, variables);
            } else {
                state = fixedChannel.toState(variables);
            }
            updateState(channel.getUID(), state);
        } catch (final NutException | RuntimeException e) {
            logger.debug("Refresh Network UPS Tools failed: ", e);
        }
    }

    private State getDynamicChannelState(final Channel channel, @Nullable final Map<String, String> variables)
            throws NutException {
        final NUTDynamicChannelConfiguration nutConfig = userChannelToNutMap.get(channel.getUID());
        final String acceptedItemType = channel.getAcceptedItemType();

        if (variables == null || acceptedItemType == null || nutConfig == null) {
            return UnDefType.UNDEF;
        }
        final String value = deviceToString(variables, nutConfig.networkupstools);

        if (value == null) {
            return UnDefType.UNDEF;
        }
        switch (acceptedItemType) {
            case CoreItemFactory.NUMBER:
                return new DecimalType(value);
            case CoreItemFactory.STRING:
                return StringType.valueOf(value);
            case CoreItemFactory.SWITCH:
                return OnOffType.from(value);
            default:
                if (acceptedItemType.startsWith(CoreItemFactory.NUMBER + ':')) {
                    logger.debug("nut:{}, unit:{}, value:{}", nutConfig.networkupstools, nutConfig.unit, value);
                    return new QuantityType<>(value + nutConfig.unit);
                }
                return UnDefType.UNDEF;
        }
    }

    private @Nullable String deviceToString(final Map<String, String> variables, final String nut) {
        final String value = variables.get(nut);

        if (value == null) {
            logger.info("Variabele '{}' was not available in the NUT data of thing {}({})", nut, thing.getLabel(),
                    thing.getUID());
        }
        return value;
    }
}
