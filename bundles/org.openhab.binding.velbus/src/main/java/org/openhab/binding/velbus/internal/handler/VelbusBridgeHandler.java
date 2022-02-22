/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.velbus.internal.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.velbus.internal.VelbusClockAlarm;
import org.openhab.binding.velbus.internal.VelbusClockAlarmConfiguration;
import org.openhab.binding.velbus.internal.VelbusPacketInputStream;
import org.openhab.binding.velbus.internal.VelbusPacketListener;
import org.openhab.binding.velbus.internal.config.VelbusBridgeConfig;
import org.openhab.binding.velbus.internal.discovery.VelbusThingDiscoveryService;
import org.openhab.binding.velbus.internal.packets.VelbusSetDatePacket;
import org.openhab.binding.velbus.internal.packets.VelbusSetDaylightSavingsStatusPacket;
import org.openhab.binding.velbus.internal.packets.VelbusSetLocalClockAlarmPacket;
import org.openhab.binding.velbus.internal.packets.VelbusSetRealtimeClockPacket;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link VelbusBridgeHandler} is an abstract handler for a Velbus interface and connects it to
 * the framework.
 *
 * @author Cedric Boon - Initial contribution
 * @author Daniel Rosengarten - Add global alarm configuration from bridge (removed from modules), reduces bus flooding
 *         on alarm value update
 */
@NonNullByDefault
public abstract class VelbusBridgeHandler extends BaseBridgeHandler {
    private final Logger logger = LoggerFactory.getLogger(VelbusBridgeHandler.class);

    private long lastPacketTimeMillis;

    protected @Nullable VelbusPacketListener defaultPacketListener;
    protected Map<Byte, VelbusPacketListener> packetListeners = new HashMap<>();

    private @NonNullByDefault({}) VelbusBridgeConfig bridgeConfig;
    private @Nullable ScheduledFuture<?> timeUpdateJob;
    private @Nullable ScheduledFuture<?> reconnectionHandler;

    private @NonNullByDefault({}) OutputStream outputStream;
    private @NonNullByDefault({}) VelbusPacketInputStream inputStream;

    private boolean listenerStopped;

    private final ChannelUID clockAlarm1Enabled = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm1Enabled");
    private final ChannelUID clockAlarm1WakeupHour = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm1WakeupHour");
    private final ChannelUID clockAlarm1WakeupMinute = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm1WakeupMinute");
    private final ChannelUID clockAlarm1BedtimeHour = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm1BedtimeHour");
    private final ChannelUID clockAlarm1BedtimeMinute = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm1BedtimeMinute");
    private final ChannelUID clockAlarm2Enabled = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm2Enabled");
    private final ChannelUID clockAlarm2WakeupHour = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm2WakeupHour");
    private final ChannelUID clockAlarm2WakeupMinute = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm2WakeupMinute");
    private final ChannelUID clockAlarm2BedtimeHour = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm2BedtimeHour");
    private final ChannelUID clockAlarm2BedtimeMinute = new ChannelUID(thing.getUID(), "bridgeClockAlarm",
            "clockAlarm2BedtimeMinute");

    private VelbusClockAlarmConfiguration alarmClockConfiguration = new VelbusClockAlarmConfiguration();

    private long lastUpdateAlarm1TimeMillis;
    private long lastUpdateAlarm2TimeMillis;

    public VelbusBridgeHandler(Bridge velbusBridge) {
        super(velbusBridge);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing velbus bridge handler.");

        bridgeConfig = getConfigAs(VelbusBridgeConfig.class);

        connect();
        initializeTimeUpdate();
    }

    private void initializeTimeUpdate() {
        int timeUpdateInterval = bridgeConfig.timeUpdateInterval;

        if (timeUpdateInterval > 0) {
            startTimeUpdates(timeUpdateInterval);
        }
    }

    private void startTimeUpdates(int timeUpdatesInterval) {
        timeUpdateJob = scheduler.scheduleWithFixedDelay(this::updateDateTime, 0, timeUpdatesInterval,
                TimeUnit.MINUTES);
    }

    private void updateDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), TimeZone.getDefault().toZoneId());

        updateDate(zonedDateTime);
        updateTime(zonedDateTime);
        updateDaylightSavingsStatus(zonedDateTime);
    }

    private void updateTime(ZonedDateTime zonedDateTime) {
        VelbusSetRealtimeClockPacket packet = new VelbusSetRealtimeClockPacket((byte) 0x00, zonedDateTime);

        byte[] packetBytes = packet.getBytes();
        this.sendPacket(packetBytes);
    }

    private void updateDate(ZonedDateTime zonedDateTime) {
        VelbusSetDatePacket packet = new VelbusSetDatePacket((byte) 0x00, zonedDateTime);

        byte[] packetBytes = packet.getBytes();
        this.sendPacket(packetBytes);
    }

    private void updateDaylightSavingsStatus(ZonedDateTime zonedDateTime) {
        VelbusSetDaylightSavingsStatusPacket packet = new VelbusSetDaylightSavingsStatusPacket((byte) 0x00,
                zonedDateTime);

        byte[] packetBytes = packet.getBytes();
        this.sendPacket(packetBytes);
    }

    protected void initializeStreams(OutputStream outputStream, InputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = new VelbusPacketInputStream(inputStream);
    }

    @Override
    public void dispose() {
        final ScheduledFuture<?> timeUpdateJob = this.timeUpdateJob;
        if (timeUpdateJob != null) {
            timeUpdateJob.cancel(true);
        }
        disconnect();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (isAlarmClockChannel(channelUID)) {
            byte alarmNumber = determineAlarmNumber(channelUID);
            VelbusClockAlarm alarmClock = alarmClockConfiguration.getAlarmClock(alarmNumber);

            alarmClock.setLocal(false);

            if ((channelUID.equals(clockAlarm1Enabled) || channelUID.equals(clockAlarm2Enabled))
                    && command instanceof OnOffType) {
                boolean enabled = command == OnOffType.ON;
                alarmClock.setEnabled(enabled);
            } else if (channelUID.equals(clockAlarm1WakeupHour)
                    || channelUID.equals(clockAlarm2WakeupHour) && command instanceof DecimalType) {
                byte wakeupHour = ((DecimalType) command).byteValue();
                alarmClock.setWakeupHour(wakeupHour);
            } else if (channelUID.equals(clockAlarm1WakeupMinute)
                    || channelUID.equals(clockAlarm2WakeupMinute) && command instanceof DecimalType) {
                byte wakeupMinute = ((DecimalType) command).byteValue();
                alarmClock.setWakeupMinute(wakeupMinute);
            } else if (channelUID.equals(clockAlarm1BedtimeHour)
                    || channelUID.equals(clockAlarm2BedtimeHour) && command instanceof DecimalType) {
                byte bedTimeHour = ((DecimalType) command).byteValue();
                alarmClock.setBedtimeHour(bedTimeHour);
            } else if (channelUID.equals(clockAlarm1BedtimeMinute)
                    || channelUID.equals(clockAlarm2BedtimeMinute) && command instanceof DecimalType) {
                byte bedTimeMinute = ((DecimalType) command).byteValue();
                alarmClock.setBedtimeMinute(bedTimeMinute);
            }

            if (alarmNumber == 1)
                lastUpdateAlarm1TimeMillis = System.currentTimeMillis();
            else
                lastUpdateAlarm2TimeMillis = System.currentTimeMillis();

            VelbusSetLocalClockAlarmPacket packet = new VelbusSetLocalClockAlarmPacket((byte) 0x00, alarmNumber,
                    alarmClock);
            byte[] packetBytes = packet.getBytes();

            // Schedule the send of the packet to see if there is another update in less than 10 secondes (reduce
            // flooding of the bus)
            scheduler.schedule(() -> {
                sendAlarmPacket(alarmNumber, packetBytes);
            }, 10000, TimeUnit.MILLISECONDS);
        } else {
            logger.debug("The command '{}' is not supported by this handler.", command.getClass());
        }
    }

    public synchronized void sendAlarmPacket(int alarmNumber, byte[] packetBytes) {
        long timeSinceLastUpdate;

        if (alarmNumber == 1)
            timeSinceLastUpdate = System.currentTimeMillis() - lastUpdateAlarm1TimeMillis;
        else
            timeSinceLastUpdate = System.currentTimeMillis() - lastUpdateAlarm2TimeMillis;

        // If a value of the alarm has been updated, discard this old update
        if (timeSinceLastUpdate < 10000)
            return;

        sendPacket(packetBytes);
    }

    public synchronized void sendPacket(byte[] packet) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeSinceLastPacket = currentTimeMillis - lastPacketTimeMillis;

        if (timeSinceLastPacket < 60) {
            // When sending you need a delay of 60ms between each packet (to prevent flooding the VMB1USB).
            long timeToDelay = 60 - timeSinceLastPacket;

            scheduler.schedule(() -> {
                sendPacket(packet);
            }, timeToDelay, TimeUnit.MILLISECONDS);

            return;
        }

        writePacket(packet);

        lastPacketTimeMillis = System.currentTimeMillis();
    }

    private void readPacket(byte[] packet) {
        byte address = packet[2];

        if (packetListeners.containsKey(address)) {
            VelbusPacketListener packetListener = packetListeners.get(address);
            packetListener.onPacketReceived(packet);
        } else {
            final VelbusPacketListener defaultPacketListener = this.defaultPacketListener;
            if (defaultPacketListener != null) {
                defaultPacketListener.onPacketReceived(packet);
            }
        }
    }

    protected void readPackets() {
        if (inputStream == null) {
            onConnectionLost();
            return;
        }

        byte[] packet;

        listenerStopped = false;

        try {
            while (!listenerStopped & ((packet = inputStream.readPacket()).length > 0)) {
                readPacket(packet);
            }
        } catch (IOException e) {
            if (!listenerStopped) {
                onConnectionLost();
            }
        }
    }

    private void writePacket(byte[] packet) {
        if (outputStream == null) {
            onConnectionLost();
            return;
        }

        try {
            outputStream.write(packet);
            outputStream.flush();
        } catch (IOException e) {
            onConnectionLost();
        }
    }

    protected void onConnectionLost() {
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                "A network communication error occurred.");
        disconnect();
        startReconnectionHandler();
    }

    /**
     * Makes a connection to the Velbus system.
     *
     * @return True if the connection succeeded, false if the connection did not succeed.
     */
    protected abstract boolean connect();

    protected void disconnect() {
        listenerStopped = true;

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            logger.debug("Error while closing output stream", e);
        }

        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            logger.debug("Error while closing input stream", e);
        }
    }

    public void startReconnectionHandler() {
        final ScheduledFuture<?> reconnectionHandler = this.reconnectionHandler;
        if (reconnectionHandler == null || reconnectionHandler.isCancelled()) {
            int reconnectionInterval = bridgeConfig.reconnectionInterval;
            if (reconnectionInterval > 0) {
                this.reconnectionHandler = scheduler.scheduleWithFixedDelay(() -> {
                    final ScheduledFuture<?> currentReconnectionHandler = this.reconnectionHandler;
                    if (connect() && currentReconnectionHandler != null) {
                        currentReconnectionHandler.cancel(false);
                    }
                }, reconnectionInterval, reconnectionInterval, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(VelbusThingDiscoveryService.class);
    }

    public void setDefaultPacketListener(VelbusPacketListener velbusPacketListener) {
        defaultPacketListener = velbusPacketListener;
    }

    public void clearDefaultPacketListener() {
        defaultPacketListener = null;
    }

    public void registerPacketListener(byte address, VelbusPacketListener packetListener) {
        packetListeners.put(Byte.valueOf(address), packetListener);
    }

    public void unregisterRelayStatusListener(byte address) {
        packetListeners.remove(Byte.valueOf(address));
    }

    protected boolean isAlarmClockChannel(ChannelUID channelUID) {
        return channelUID.equals(clockAlarm1Enabled) || channelUID.equals(clockAlarm1WakeupHour)
                || channelUID.equals(clockAlarm1WakeupMinute) || channelUID.equals(clockAlarm1BedtimeHour)
                || channelUID.equals(clockAlarm1BedtimeMinute) || channelUID.equals(clockAlarm2Enabled)
                || channelUID.equals(clockAlarm2WakeupHour) || channelUID.equals(clockAlarm2WakeupMinute)
                || channelUID.equals(clockAlarm2BedtimeHour) || channelUID.equals(clockAlarm2BedtimeMinute);
    }

    protected byte determineAlarmNumber(ChannelUID channelUID) {
        if (channelUID.equals(clockAlarm1Enabled) || channelUID.equals(clockAlarm1WakeupHour)
                || channelUID.equals(clockAlarm1WakeupMinute) || channelUID.equals(clockAlarm1BedtimeHour)
                || channelUID.equals(clockAlarm1BedtimeMinute)) {
            return 1;
        } else if (channelUID.equals(clockAlarm2Enabled) || channelUID.equals(clockAlarm2WakeupHour)
                || channelUID.equals(clockAlarm2WakeupMinute) || channelUID.equals(clockAlarm2BedtimeHour)
                || channelUID.equals(clockAlarm2BedtimeMinute)) {
            return 2;
        } else {
            throw new IllegalArgumentException("The given channelUID is not an alarm clock channel: " + channelUID);
        }
    }
}
