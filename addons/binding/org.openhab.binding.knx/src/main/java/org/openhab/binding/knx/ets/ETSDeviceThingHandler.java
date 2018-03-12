/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.ets;

import static org.openhab.binding.knx.KNXBindingConstants.CHANNEL_RESET;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.Type;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.knx.KNXBindingConstants;
import org.openhab.binding.knx.KNXTypeMapper;
import org.openhab.binding.knx.client.InboundSpec;
import org.openhab.binding.knx.client.OutboundSpec;
import org.openhab.binding.knx.handler.AbstractKNXThingHandler;
import org.openhab.binding.knx.internal.channel.AbstractKNXChannelType;
import org.openhab.binding.knx.internal.client.AbstractKNXClient;
import org.openhab.binding.knx.internal.config.DeviceConfig;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.binding.knx.internal.ets.channel.ETSChannelTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.KNXException;
import tuwien.auto.calimero.KNXFormatException;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * The {@link ETSDeviceThingHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Karel Goderis - Initial contribution
 */
public class ETSDeviceThingHandler extends AbstractKNXThingHandler {

    private final Logger logger = LoggerFactory.getLogger(ETSDeviceThingHandler.class);

    private final KNXTypeMapper typeHelper = new KNXCoreTypeMapper();
    private final Set<GroupAddress> groupAddresses = new HashSet<>();
    private final Map<GroupAddress, @Nullable ScheduledFuture<?>> readFutures = new HashMap<>();
    private final Map<ChannelUID, @Nullable ScheduledFuture<?>> channelFutures = new HashMap<>();
    private @Nullable IndividualAddress address;
    private int readInterval;

    // group addresses read out from the device's firmware tables
    protected Set<GroupAddress> foundGroupAddresses = new HashSet<GroupAddress>();

    private static final long POLLING_INTERVAL = 60000;
    private static final long OPERATION_TIMEOUT = 5000;
    private static final long OPERATION_INTERVAL = 2000;
    private static final Random RANDOM_GENERATOR = new Random();
    private boolean filledDescription = false;

    // Memory addresses for device information
    static final int MEM_DOA = 0x0102; // length 2
    static final int MEM_MANUFACTURERID = 0x0104;
    static final int MEM_DEVICETYPE = 0x0105; // length 2
    static final int MEM_VERSION = 0x0107;
    static final int MEM_PEI = 0x0109;
    static final int MEM_RUNERROR = 0x010d;
    static final int MEM_GROUPOBJECTABLEPTR = 0x0112;
    static final int MEM_PROGRAMPTR = 0x0114;
    static final int MEM_GROUPADDRESSTABLE = 0x0116; // max. length 233

    // Interface Object indexes
    private static final int DEVICE_OBJECT = 0; // Device Object
    private static final int ADDRESS_TABLE_OBJECT = 1; // Addresstable Object
    private static final int ASSOCIATION_TABLE_OBJECT = 2; // Associationtable Object
    private static final int APPLICATION_PROGRAM_TABLE = 3; // Application Program Object
    private static final int INTERFACE_PROGRAM_OBJECT = 4; // Interface Program Object
    private static final int GROUPOBJECT_OBJECT = 9; // Group Object Object
    private static final int KNXNET_IP_OBJECT = 11; // KNXnet/IP Parameter Object

    // Property IDs for device information;
    private static final int HARDWARE_TYPE = 78;

    public ETSDeviceThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        super.initialize();
        DeviceConfig config = getConfigAs(DeviceConfig.class);
        readInterval = config.getReadInterval().intValue();
        initializeGroupAddresses();
    }

    private void initializeGroupAddresses() {
        forAllChannels((selector, channelConfiguration) -> {
            groupAddresses.addAll(selector.getReadAddresses(channelConfiguration));
            groupAddresses.addAll(selector.getWriteAddresses(channelConfiguration));
            groupAddresses.addAll(selector.getListenAddresses(channelConfiguration));
        });
    }

    @Override
    protected void cancelReadFutures() {
        for (ScheduledFuture<?> future : readFutures.values()) {
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
        }
        readFutures.clear();
    }

    @FunctionalInterface
    private interface ChannelFunction {
        void apply(AbstractKNXChannelType channelType, Configuration configuration) throws KNXException;
    }

    private void withKNXType(ChannelUID channelUID, ChannelFunction function) {
        Channel channel = getThing().getChannel(channelUID.getId());
        if (channel == null) {
            logger.warn("Channel '{}' does not exist", channelUID);
            return;
        }
        withKNXType(channel, function);
    }

    private void withKNXType(Channel channel, ChannelFunction function) {
        try {
            AbstractKNXChannelType selector = getKNXChannelType(channel);
            function.apply(selector, channel.getConfiguration());
        } catch (KNXException e) {
            logger.warn("An error occurred on channel {}: {}", channel.getUID(), e.getMessage(), e);
        }
    }

    private void forAllChannels(ChannelFunction function) {
        for (Channel channel : getThing().getChannels()) {
            withKNXType(channel, function);
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        withKNXType(channelUID, (selector, configuration) -> {
            scheduleRead(selector, configuration);
        });
    }

    @Override
    protected void scheduleReadJobs() {
        cancelReadFutures();
        for (Channel channel : getThing().getChannels()) {
            // if (isLinked(channel.getUID().getId()) && !isControl(channel.getUID())) {
            if (isLinked(channel.getUID().getId())) {
                withKNXType(channel, (selector, configuration) -> {
                    scheduleRead(selector, configuration);
                });
            }
        }
    }

    private void scheduleRead(AbstractKNXChannelType selector, Configuration configuration) throws KNXFormatException {
        List<InboundSpec> readSpecs = selector.getReadSpec(configuration);
        for (InboundSpec readSpec : readSpecs) {
            for (GroupAddress groupAddress : readSpec.getGroupAddresses()) {
                scheduleReadJob(groupAddress, readSpec.getDPT());
            }
        }
    }

    private void scheduleReadJob(GroupAddress groupAddress, String dpt) {
        if (readInterval > 0) {
            ScheduledFuture<?> future = readFutures.get(groupAddress);
            if (future == null || future.isDone() || future.isCancelled()) {
                future = getScheduler().scheduleWithFixedDelay(() -> readDatapoint(groupAddress, dpt), 0, readInterval,
                        TimeUnit.SECONDS);
                readFutures.put(groupAddress, future);
            }
        } else {
            getScheduler().submit(() -> readDatapoint(groupAddress, dpt));
        }
    }

    private void readDatapoint(GroupAddress groupAddress, String dpt) {
        if (getClient().isConnected()) {
            if (!isDPTSupported(dpt)) {
                logger.warn("DPT '{}' is not supported by the KNX binding", dpt);
                return;
            }
            Datapoint datapoint = new CommandDP(groupAddress, getThing().getUID().toString(), 0, dpt);
            getClient().readDatapoint(datapoint);
        }
    }

    @Override
    public boolean listensTo(GroupAddress destination) {
        return groupAddresses.contains(destination);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("Handling command '{}' for channel '{}'", command, channelUID);
        // if (command instanceof RefreshType && !isControl(channelUID)) {
        if (command instanceof RefreshType) {
            logger.debug("Refreshing channel '{}'", channelUID);
            withKNXType(channelUID, (selector, configuration) -> {
                scheduleRead(selector, configuration);
            });
        } else {
            switch (channelUID.getId()) {
                case CHANNEL_RESET:
                    if (address != null) {
                        restart();
                    }
                    break;
                default:
                    withKNXType(channelUID, (selector, channelConfiguration) -> {
                        OutboundSpec commandSpec = selector.getCommandSpec(channelConfiguration, typeHelper, command);
                        if (commandSpec != null) {
                            getClient().writeToKNX(commandSpec);
                        } else {
                            logger.debug(
                                    "None of the configured GAs on channel '{}' could handle the command '{}' of type '{}'",
                                    channelUID, command, command.getClass().getSimpleName());
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void handleUpdate(ChannelUID channelUID, State state) {
        logger.trace("Handling state '{}' for channel '{}'", state, channelUID);

        switch (channelUID.getId()) {
            case CHANNEL_RESET:
                if (address != null) {
                    restart();
                }
                break;
            default:
                withKNXType(channelUID, (selector, channelConfiguration) -> {
                    OutboundSpec commandSpec = selector.getCommandSpec(channelConfiguration, typeHelper, state);
                    if (commandSpec != null) {
                        getClient().writeToKNX(commandSpec);
                    } else {
                        logger.debug(
                                "None of the configured GAs on channel '{}' could handle the command '{}' of type '{}'",
                                channelUID, state, state.getClass().getSimpleName());
                    }
                });
                break;
        }

    }

    private ChannelTypeUID getChannelTypeUID(ChannelUID channelUID) {
        Channel channel = getThing().getChannel(channelUID.getId());
        Objects.requireNonNull(channel);
        ChannelTypeUID channelTypeUID = channel.getChannelTypeUID();
        Objects.requireNonNull(channelTypeUID);
        return channelTypeUID;
    }

    @Override
    public void onGroupRead(AbstractKNXClient client, IndividualAddress source, GroupAddress destination, byte[] asdu) {
        logger.trace("Thing '{}' received a Group Read Request telegram from '{}' for destination '{}'",
                getThing().getUID(), source, destination);
    }

    @Override
    public void onGroupReadResponse(AbstractKNXClient client, IndividualAddress source, GroupAddress destination,
            byte[] asdu) {
        // Group Read Responses are treated the same as Group Write telegrams
        onGroupWrite(client, source, destination, asdu);
    }

    @Override
    public void onGroupWrite(AbstractKNXClient client, IndividualAddress source, GroupAddress destination,
            byte[] asdu) {
        logger.debug("Thing '{}' received a Group Write telegram from '{}' for destination '{}'", getThing().getUID(),
                source, destination);

        for (Channel channel : getThing().getChannels()) {
            withKNXType(channel, (selector, configuration) -> {
                InboundSpec listenSpec = selector.getListenSpec(configuration, destination);
                if (listenSpec != null) {
                    logger.trace("Thing '{}' processes a Group Write telegram for destination '{}' for channel '{}'",
                            getThing().getUID(), destination, channel.getUID());
                    processDataReceived(destination, asdu, listenSpec, channel.getUID());
                }
            });
        }
    }

    private void processDataReceived(GroupAddress destination, byte[] asdu, InboundSpec listenSpec,
            ChannelUID channelUID) {
        if (!isDPTSupported(listenSpec.getDPT())) {
            logger.warn("DPT '{}' is not supported by the KNX binding.", listenSpec.getDPT());
            return;
        }

        Datapoint datapoint = new CommandDP(destination, getThing().getUID().toString(), 0, listenSpec.getDPT());
        Type type = typeHelper.toType(datapoint, asdu);

        if (type != null) {
            // if (isControl(channelUID)) {
            Channel channel = getThing().getChannel(channelUID.getId());
            Object repeat = channel != null ? channel.getConfiguration().get(KNXBindingConstants.REPEAT_FREQUENCY)
                    : null;
            int frequency = repeat != null ? ((BigDecimal) repeat).intValue() : 0;
            if (KNXBindingConstants.CHANNEL_DIMMER_CONTROL.equals(getChannelTypeUID(channelUID).getId())
                    && (type instanceof UnDefType || type instanceof IncreaseDecreaseType) && frequency > 0) {
                // continuous dimming by the binding
                if (UnDefType.UNDEF.equals(type)) {
                    ScheduledFuture<?> future = channelFutures.remove(channelUID);
                    if (future != null) {
                        future.cancel(false);
                    }
                } else if (type instanceof IncreaseDecreaseType) {
                    ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(() -> {
                        postCommand(channelUID, (Command) type);
                    }, 0, frequency, TimeUnit.MILLISECONDS);
                    ScheduledFuture<?> previousFuture = channelFutures.put(channelUID, future);
                    if (previousFuture != null) {
                        previousFuture.cancel(true);
                    }
                }
            } else {
                if (type instanceof Command) {
                    postCommand(channelUID, (Command) type);
                } else if (type instanceof State) {
                    updateState(channelUID, (State) type);
                }
            }
        } else {
            String s = asduToHex(asdu);
            logger.warn(
                    "Ignoring KNX bus data: couldn't transform to any Type (destination='{}', datapoint='{}', data='{}')",
                    destination, datapoint, s);
        }
    }

    private boolean isDPTSupported(@Nullable String dpt) {
        return typeHelper.toTypeClass(dpt) != null;
    }

    private AbstractKNXChannelType getKNXChannelType(Channel channel) {
        return ETSChannelTypes.getType(channel.getChannelTypeUID());
    }
}
