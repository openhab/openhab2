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
package org.openhab.binding.insteon.internal.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.insteon.internal.config.InsteonChannelConfiguration;
import org.openhab.binding.insteon.internal.device.DeviceType.FeatureGroup;
import org.openhab.binding.insteon.internal.driver.Driver;
import org.openhab.binding.insteon.internal.message.FieldException;
import org.openhab.binding.insteon.internal.message.InvalidMessageTypeException;
import org.openhab.binding.insteon.internal.message.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The InsteonDevice class holds known per-device state of a single Insteon device,
 * including the address, what port(modem) to reach it on etc.
 * Note that some Insteon devices de facto consist of two devices (let's say
 * a relay and a sensor), but operate under the same address. Such devices will
 * be represented just by a single InsteonDevice. Their different personalities
 * will then be represented by DeviceFeatures.
 *
 * @author Bernd Pfrommer - Initial contribution
 * @author Rob Nielsen - Port to openHAB 2 insteon binding
 */
@NonNullByDefault
@SuppressWarnings("null")
public class InsteonDevice {
    private final Logger logger = LoggerFactory.getLogger(InsteonDevice.class);

    public static enum DeviceStatus {
        INITIALIZED,
        POLLING
    }

    /** need to wait after query to avoid misinterpretation of duplicate replies */
    private static final int QUIET_TIME_DIRECT_MESSAGE = 2000;
    /** how far to space out poll messages */
    private static final int TIME_BETWEEN_POLL_MESSAGES = 1500;
    private final RequestQueueManager requestQueueManager;
    private final Set<String> ports = new LinkedHashSet<>();
    private final HashMap<String, @Nullable DeviceFeature> features = new HashMap<>();
    private final PriorityQueue<@Nullable QEntry> mrequestQueue = new PriorityQueue<>();

    private InsteonAddress address = new InsteonAddress();
    private long pollInterval = -1L; // in milliseconds
    private @Nullable Driver driver = null;
    private @Nullable String productKey = null;
    private long lastTimePolled = 0L;
    private long lastMsgReceived = 0L;
    private boolean isModem = false;
    private @Nullable DeviceFeature featureQueried = null;
    private long lastQueryTime = 0L;
    private boolean hasModemDBEntry = false;
    private DeviceStatus status = DeviceStatus.INITIALIZED;

    /**
     * Constructor
     */
    public InsteonDevice(RequestQueueManager requestQueueManager) {
        this.requestQueueManager = requestQueueManager;
        this.lastMsgReceived = System.currentTimeMillis();
    }

    // --------------------- simple getters -----------------------------

    public boolean hasProductKey() {
        return productKey != null;
    }

    public @Nullable String getProductKey() {
        return productKey;
    }

    public boolean hasModemDBEntry() {
        return hasModemDBEntry;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public InsteonAddress getAddress() {
        return (address);
    }

    public @Nullable Driver getDriver() {
        return driver;
    }

    public boolean hasValidPorts() {
        return (!ports.isEmpty());
    }

    public long getPollInterval() {
        return pollInterval;
    }

    public boolean isModem() {
        return isModem;
    }

    public @Nullable DeviceFeature getFeature(String f) {
        return features.get(f);
    }

    public HashMap<String, @Nullable DeviceFeature> getFeatures() {
        return features;
    }

    public byte getX10HouseCode() {
        return (address.getX10HouseCode());
    }

    public byte getX10UnitCode() {
        return (address.getX10UnitCode());
    }

    public boolean hasProductKey(String key) {
        return productKey != null && productKey.equals(key);
    }

    public boolean hasValidPollingInterval() {
        return (pollInterval > 0);
    }

    public long getPollOverDueTime() {
        return (lastTimePolled - lastMsgReceived);
    }

    public String getPort() throws IOException {
        if (ports.isEmpty()) {
            throw new IOException("no ports configured for instrument " + getAddress());
        }
        return (ports.iterator().next());
    }

    public boolean hasAnyListeners() {
        synchronized (features) {
            for (DeviceFeature f : features.values()) {
                if (f.hasListeners()) {
                    return true;
                }
            }
        }
        return false;
    }
    // --------------------- simple setters -----------------------------

    public void setStatus(DeviceStatus aI) {
        status = aI;
    }

    public void setHasModemDBEntry(boolean b) {
        hasModemDBEntry = b;
    }

    public void setAddress(InsteonAddress ia) {
        address = ia;
    }

    public void setDriver(Driver d) {
        driver = d;
    }

    public void setIsModem(boolean f) {
        isModem = f;
    }

    public void setProductKey(String pk) {
        productKey = pk;
    }

    public void setPollInterval(long pi) {
        logger.trace("setting poll interval for {} to {} ", address, pi);
        if (pi > 0) {
            pollInterval = pi;
        }
    }

    public void setFeatureQueried(@Nullable DeviceFeature f) {
        synchronized (mrequestQueue) {
            featureQueried = f;
        }
    };

    public @Nullable DeviceFeature getFeatureQueried() {
        synchronized (mrequestQueue) {
            return (featureQueried);
        }
    };

    /**
     * Add a port. Currently only a single port is being used.
     *
     * @param p the port to add
     */
    public void addPort(@Nullable String p) {
        if (p == null) {
            return;
        }
        ports.add(p);
    }

    /**
     * Removes feature listener from this device
     *
     * @param aItemName name of the feature listener to remove
     * @return true if a feature listener was successfully removed
     */
    public boolean removeFeatureListener(String aItemName) {
        boolean removedListener = false;
        synchronized (features) {
            for (Iterator<Entry<String, @Nullable DeviceFeature>> it = features.entrySet().iterator(); it.hasNext();) {
                DeviceFeature f = it.next().getValue();
                if (f.removeListener(aItemName)) {
                    removedListener = true;
                }
            }
        }
        return removedListener;
    }

    /**
     * Invoked to process an openHAB command
     *
     * @param driver The driver to use
     * @param c The item configuration
     * @param command The actual command to execute
     */
    public void processCommand(Driver driver, InsteonChannelConfiguration c, Command command) {
        logger.debug("processing command {} features: {}", command, features.size());
        synchronized (features) {
            for (DeviceFeature i : features.values()) {
                if (i.isReferencedByItem(c.getChannelName())) {
                    i.handleCommand(c, command);
                }
            }
        }
    }

    /**
     * Execute poll on this device: create an array of messages,
     * add them to the request queue, and schedule the queue
     * for processing.
     *
     * @param delay scheduling delay (in milliseconds)
     */
    public void doPoll(long delay) {
        long now = System.currentTimeMillis();
        ArrayList<QEntry> l = new ArrayList<QEntry>();
        synchronized (features) {
            int spacing = 0;
            for (DeviceFeature i : features.values()) {
                if (i.hasListeners()) {
                    Msg m = i.makePollMsg();
                    if (m != null) {
                        l.add(new QEntry(i, m, now + delay + spacing));
                        spacing += TIME_BETWEEN_POLL_MESSAGES;
                    }
                }
            }
        }
        if (l.isEmpty()) {
            return;
        }
        synchronized (mrequestQueue) {
            for (QEntry e : l) {
                mrequestQueue.add(e);
            }
        }
        requestQueueManager.addQueue(this, now + delay);

        if (!l.isEmpty()) {
            lastTimePolled = now;
        }
    }

    /**
     * Handle incoming message for this device by forwarding
     * it to all features that this device supports
     *
     * @param fromPort port from which the message come in
     * @param msg the incoming message
     */
    public void handleMessage(String fromPort, Msg msg) {
        lastMsgReceived = System.currentTimeMillis();

        synchronized (features) {
            // first update all features that are
            // not status features
            for (DeviceFeature f : features.values()) {
                if (!f.isStatusFeature()) {
                    logger.debug("----- applying message to feature: {}", f.getName());
                    if (f.handleMessage(msg, fromPort)) {
                        // handled a reply to a query,
                        // mark it as processed
                        logger.trace("handled reply of direct: {}", f);
                        setFeatureQueried(null);
                        break;
                    }
                }
            }
            // then update all the status features,
            // e.g. when the device was last updated
            for (DeviceFeature f : features.values()) {
                if (f.isStatusFeature()) {
                    f.handleMessage(msg, fromPort);
                }
            }
        }
    }

    /**
     * Helper method to make standard message
     *
     * @param flags
     * @param cmd1
     * @param cmd2
     * @return standard message
     * @throws FieldException
     * @throws IOException
     */
    public Msg makeStandardMessage(byte flags, byte cmd1, byte cmd2)
            throws FieldException, InvalidMessageTypeException {
        return (makeStandardMessage(flags, cmd1, cmd2, -1));
    }

    /**
     * Helper method to make standard message, possibly with group
     *
     * @param flags
     * @param cmd1
     * @param cmd2
     * @param group (-1 if not a group message)
     * @return standard message
     * @throws FieldException
     * @throws IOException
     */
    public Msg makeStandardMessage(byte flags, byte cmd1, byte cmd2, int group)
            throws FieldException, InvalidMessageTypeException {
        Msg m = Msg.makeMessage("SendStandardMessage");
        InsteonAddress addr = null;
        byte f = flags;
        if (group != -1) {
            f |= 0xc0; // mark message as group message
            // and stash the group number into the address
            addr = new InsteonAddress((byte) 0, (byte) 0, (byte) (group & 0xff));
        } else {
            addr = getAddress();
        }
        m.setAddress("toAddress", addr);
        m.setByte("messageFlags", f);
        m.setByte("command1", cmd1);
        m.setByte("command2", cmd2);
        return m;
    }

    public Msg makeX10Message(byte rawX10, byte X10Flag) throws FieldException, InvalidMessageTypeException {
        Msg m = Msg.makeMessage("SendX10Message");
        m.setByte("rawX10", rawX10);
        m.setByte("X10Flag", X10Flag);
        m.setQuietTime(300L);
        return m;
    }

    /**
     * Helper method to make extended message
     *
     * @param flags
     * @param cmd1
     * @param cmd2
     * @return extended message
     * @throws FieldException
     * @throws IOException
     */
    public Msg makeExtendedMessage(byte flags, byte cmd1, byte cmd2)
            throws FieldException, InvalidMessageTypeException {
        return makeExtendedMessage(flags, cmd1, cmd2, new byte[] {});
    }

    /**
     * Helper method to make extended message
     *
     * @param flags
     * @param cmd1
     * @param cmd2
     * @param data array with userdata
     * @return extended message
     * @throws FieldException
     * @throws IOException
     */
    public Msg makeExtendedMessage(byte flags, byte cmd1, byte cmd2, byte[] data)
            throws FieldException, InvalidMessageTypeException {
        Msg m = Msg.makeMessage("SendExtendedMessage");
        m.setAddress("toAddress", getAddress());
        m.setByte("messageFlags", (byte) (((flags & 0xff) | 0x10) & 0xff));
        m.setByte("command1", cmd1);
        m.setByte("command2", cmd2);
        m.setUserData(data);
        m.setCRC();
        return m;
    }

    /**
     * Helper method to make extended message, but with different CRC calculation
     *
     * @param flags
     * @param cmd1
     * @param cmd2
     * @param data array with user data
     * @return extended message
     * @throws FieldException
     * @throws IOException
     */
    public Msg makeExtendedMessageCRC2(byte flags, byte cmd1, byte cmd2, byte[] data)
            throws FieldException, InvalidMessageTypeException {
        Msg m = Msg.makeMessage("SendExtendedMessage");
        m.setAddress("toAddress", getAddress());
        m.setByte("messageFlags", (byte) (((flags & 0xff) | 0x10) & 0xff));
        m.setByte("command1", cmd1);
        m.setByte("command2", cmd2);
        m.setUserData(data);
        m.setCRC2();
        return m;
    }

    /**
     * Called by the RequestQueueManager when the queue has expired
     *
     * @param timeNow
     * @return time when to schedule the next message (timeNow + quietTime)
     */
    public long processRequestQueue(long timeNow) {
        synchronized (mrequestQueue) {
            if (mrequestQueue.isEmpty()) {
                return 0L;
            }
            if (featureQueried != null) {
                // A feature has been queried, but
                // the response has not been digested yet.
                // Must wait for the query to be processed.
                long dt = timeNow - (lastQueryTime + featureQueried.getDirectAckTimeout());
                if (dt < 0) {
                    logger.debug("still waiting for query reply from {} for another {} usec", address, -dt);
                    return (timeNow + 2000L); // retry soon
                } else {
                    logger.debug("gave up waiting for query reply from device {}", address);
                }
            }
            QEntry qe = mrequestQueue.poll(); // take it off the queue!
            if (!qe.getMsg().isBroadcast()) {
                logger.debug("qe taken off direct: {} {}", qe.getFeature(), qe.getMsg());
                lastQueryTime = timeNow;
                // mark feature as pending
                qe.getFeature().setQueryStatus(DeviceFeature.QueryStatus.QUERY_PENDING);
                // also mark this queue as pending so there is no doubt
                featureQueried = qe.getFeature();
            } else {
                logger.debug("qe taken off bcast: {} {}", qe.getFeature(), qe.getMsg());
            }
            long quietTime = qe.getMsg().getQuietTime();
            qe.getMsg().setQuietTime(500L); // rate limiting downstream!
            try {
                writeMessage(qe.getMsg());
            } catch (IOException e) {
                logger.warn("message write failed for msg {}", qe.getMsg(), e);
            }
            // figure out when the request queue should be checked next
            QEntry qnext = mrequestQueue.peek();
            long nextExpTime = (qnext == null ? 0L : qnext.getExpirationTime());
            long nextTime = Math.max(timeNow + quietTime, nextExpTime);
            logger.debug("next request queue processed in {} msec, quiettime = {}", nextTime - timeNow, quietTime);
            return (nextTime);
        }
    }

    /**
     * Enqueues message to be sent at the next possible time
     *
     * @param m message to be sent
     * @param f device feature that sent this message (so we can associate the response message with it)
     */
    public void enqueueMessage(Msg m, DeviceFeature f) {
        enqueueDelayedMessage(m, f, 0);
    }

    /**
     * Enqueues message to be sent after a delay
     *
     * @param m message to be sent
     * @param f device feature that sent this message (so we can associate the response message with it)
     * @param d time (in milliseconds)to delay before enqueuing message
     */
    public void enqueueDelayedMessage(Msg m, DeviceFeature f, long delay) {
        long now = System.currentTimeMillis();
        synchronized (mrequestQueue) {
            mrequestQueue.add(new QEntry(f, m, now + delay));
        }
        if (!m.isBroadcast()) {
            m.setQuietTime(QUIET_TIME_DIRECT_MESSAGE);
        }
        logger.trace("enqueing direct message with delay {}", delay);
        requestQueueManager.addQueue(this, now + delay);
    }

    private void writeMessage(Msg m) throws IOException {
        driver.writeMessage(getPort(), m);
    }

    private void instantiateFeatures(@Nullable DeviceType dt) {
        for (Entry<String, String> fe : dt.getFeatures().entrySet()) {
            DeviceFeature f = DeviceFeature.makeDeviceFeature(this, fe.getValue());
            if (f == null) {
                logger.warn("device type {} references unknown feature: {}", dt, fe.getValue());
            } else {
                addFeature(fe.getKey(), f);
            }
        }
        for (Entry<String, FeatureGroup> fe : dt.getFeatureGroups().entrySet()) {
            FeatureGroup fg = fe.getValue();
            @Nullable
            DeviceFeature f = DeviceFeature.makeDeviceFeature(this, fg.getType());
            if (f == null) {
                logger.warn("device type {} references unknown feature group: {}", dt, fg.getType());
            } else {
                addFeature(fe.getKey(), f);
                connectFeatures(fe.getKey(), f, fg.getFeatures());
            }
        }
    }

    private void connectFeatures(String gn, DeviceFeature fg, ArrayList<String> fgFeatures) {
        for (String fs : fgFeatures) {
            @Nullable
            DeviceFeature f = features.get(fs);
            if (f == null) {
                logger.warn("feature group {} references unknown feature {}", gn, fs);
            } else {
                logger.debug("{} connected feature: {}", gn, f);
                fg.addConnectedFeature(f);
            }
        }
    }

    private void addFeature(String name, DeviceFeature f) {
        synchronized (features) {
            features.put(name, f);
        }
    }

    @Override
    public String toString() {
        String s = address.toString();
        for (Entry<String, @Nullable DeviceFeature> f : features.entrySet()) {
            s += "|" + f.getKey() + "->" + f.getValue().toString();
        }
        return s;
    }

    /**
     * Factory method
     *
     * @param dt device type after which to model the device
     * @return newly created device
     */
    public static InsteonDevice makeDevice(@Nullable DeviceType dt, RequestQueueManager requestQueueManager) {
        InsteonDevice dev = new InsteonDevice(requestQueueManager);
        dev.instantiateFeatures(dt);
        return dev;
    }

    /**
     * Queue entry helper class
     *
     * @author Bernd Pfrommer - Initial contribution
     */
    @NonNullByDefault
    public static class QEntry implements Comparable<QEntry> {
        private DeviceFeature feature;
        private Msg msg;
        private long expirationTime;

        public DeviceFeature getFeature() {
            return feature;
        }

        public Msg getMsg() {
            return msg;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        QEntry(DeviceFeature f, Msg m, long t) {
            feature = f;
            msg = m;
            expirationTime = t;
        }

        @Override
        public int compareTo(QEntry a) {
            return (int) (expirationTime - a.expirationTime);
        }
    }
}
