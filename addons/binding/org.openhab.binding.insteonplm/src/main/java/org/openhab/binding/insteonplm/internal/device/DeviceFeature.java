/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeatureListener.StateChangeType;
import org.openhab.binding.insteonplm.internal.device.commands.NoOpCommandHandler;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A DeviceFeature represents a certain feature (trait) of a given Insteon device, e.g. something
 * operating under a given InsteonAddress that can be manipulated (relay) or read (sensor).
 *
 * The DeviceFeature does the processing of incoming messages, and handles commands for the
 * particular feature it represents.
 *
 * It uses four mechanisms for that:
 *
 * 1) MessageDispatcher: makes high level decisions about an incoming message and then runs the
 * 2) MessageHandler: further processes the message, updates state etc
 * 3) CommandHandler: translates commands from the openhab bus into an Insteon message.
 * 4) PollHandler: creates an Insteon message to query the DeviceFeature
 *
 * Lastly, DeviceFeatureListeners can register with the DeviceFeature to get notifications when
 * the state of a feature has changed. In practice, a DeviceFeatureListener corresponds to an
 * OpenHAB item.
 *
 * The character of a DeviceFeature is thus given by a set of message and command handlers.
 * A FeatureTemplate captures exactly that: it says what set of handlers make up a DeviceFeature.
 *
 * DeviceFeatures are added to a new device by referencing a FeatureTemplate (defined in device_features.xml)
 * from the Device definition file (device_types.xml).
 *
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class DeviceFeature {
    public static enum QueryStatus {
        NEVER_QUERIED,
        QUERY_PENDING,
        QUERY_ANSWERED
    }

    private static final Logger logger = LoggerFactory.getLogger(DeviceFeature.class);

    private InsteonThingHandler m_device = null;
    private String m_name = "INVALID_FEATURE_NAME";
    private boolean m_isStatus = false;
    private int m_directAckTimeout = 6000;
    private QueryStatus m_queryStatus = QueryStatus.NEVER_QUERIED;

    private MessageHandler m_defaultMsgHandler = new MessageHandler.DefaultMsgHandler(this);
    private CommandHandler m_defaultCommandHandler = new NoOpCommandHandler(this);
    private PollHandler m_pollHandler = null;
    private MessageDispatcher m_dispatcher = null;

    private HashMap<Integer, MessageHandler> m_msgHandlers = new HashMap<Integer, MessageHandler>();
    private HashMap<Class<? extends Command>, CommandHandler> m_commandHandlers = new HashMap<Class<? extends Command>, CommandHandler>();
    private ArrayList<DeviceFeatureListener> m_listeners = new ArrayList<DeviceFeatureListener>();
    private ArrayList<DeviceFeature> m_connectedFeatures = new ArrayList<DeviceFeature>();

    /**
     * Constructor
     *
     * @param device Insteon device to which this feature belongs
     * @param name descriptive name for that feature
     */
    public DeviceFeature(InsteonThingHandler device, String name) {
        m_name = name;
        setDevice(device);
    }

    /**
     * Constructor
     *
     * @param name descriptive name of the feature
     */
    public DeviceFeature(String name) {
        m_name = name;
    }

    // various simple getters
    public String getName() {
        return m_name;
    }

    public synchronized QueryStatus getQueryStatus() {
        return m_queryStatus;
    }

    public InsteonThingHandler getDevice() {
        return m_device;
    }

    public boolean isFeatureGroup() {
        return !m_connectedFeatures.isEmpty();
    }

    public boolean isStatusFeature() {
        return m_isStatus;
    }

    public int getDirectAckTimeout() {
        return m_directAckTimeout;
    }

    public MessageHandler getDefaultMsgHandler() {
        return m_defaultMsgHandler;
    }

    public HashMap<Integer, MessageHandler> getMsgHandlers() {
        return this.m_msgHandlers;
    }

    public ArrayList<DeviceFeature> getConnectedFeatures() {
        return (m_connectedFeatures);
    }

    // various simple setters
    public void setStatusFeature(boolean f) {
        m_isStatus = f;
    }

    public void setPollHandler(PollHandler h) {
        m_pollHandler = h;
    }

    public void setDevice(InsteonThingHandler d) {
        m_device = d;
    }

    public void setMessageDispatcher(MessageDispatcher md) {
        m_dispatcher = md;
    }

    public void setDefaultCommandHandler(CommandHandler ch) {
        m_defaultCommandHandler = ch;
    }

    public void setDefaultMsgHandler(MessageHandler mh) {
        m_defaultMsgHandler = mh;
    }

    public synchronized void setQueryStatus(QueryStatus status) {
        logger.trace("{} set query status to: {}", m_name, status);
        m_queryStatus = status;
    }

    public void setTimeout(String s) {
        if (s != null && !s.isEmpty()) {
            try {
                m_directAckTimeout = Integer.parseInt(s);
                logger.trace("ack timeout set to {}", m_directAckTimeout);
            } catch (NumberFormatException e) {
                logger.error("invalid number for timeout: {}", s);
            }
        }
    }

    /**
     * Add a listener (item) to a device feature
     *
     * @param l the listener
     */
    public void addListener(DeviceFeatureListener l) {
        synchronized (m_listeners) {
            for (DeviceFeatureListener m : m_listeners) {
                if (m.getChanelId().equals(l.getChanelId())) {
                    return;
                }
            }
            m_listeners.add(l);
        }
    }

    /**
     * Adds a connected feature such that this DeviceFeature can
     * act as a feature group
     *
     * @param f the device feature related to this feature
     */
    public void addConnectedFeature(DeviceFeature f) {
        m_connectedFeatures.add(f);
    }

    public boolean hasListeners() {
        if (!m_listeners.isEmpty()) {
            return true;
        }
        for (DeviceFeature f : m_connectedFeatures) {
            if (f.hasListeners()) {
                return true;
            }
        }
        return false;
    }

    /**
     * removes a DeviceFeatureListener from this feature
     *
     * @param aItemName name of the item to remove as listener
     * @return true if a listener was removed
     */
    public boolean removeListener(String aItemName) {
        boolean listenerRemoved = false;
        synchronized (m_listeners) {
            for (Iterator<DeviceFeatureListener> it = m_listeners.iterator(); it.hasNext();) {
                DeviceFeatureListener fl = it.next();
                if (fl.getChanelId().equals(aItemName)) {
                    it.remove();
                    listenerRemoved = true;
                }
            }
        }
        return listenerRemoved;
    }

    public boolean isReferencedByItem(String aItemName) {
        synchronized (m_listeners) {
            for (DeviceFeatureListener fl : m_listeners) {
                if (fl.getChanelId().equals(aItemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called when message is incoming. Dispatches message according to message dispatcher
     *
     * @param msg The message to dispatch
     * @param port the port from which the message came
     * @return true if dispatch successful
     */
    public boolean handleMessage(Msg msg, String port) {
        if (m_dispatcher == null) {
            logger.error("{} no dispatcher for msg {}", m_name, msg);
            return false;
        }
        return (m_dispatcher.dispatch(msg, port));
    }

    /**
     * Called when an openhab command arrives for this device feature
     *
     * @param c the channel the command is on
     * @param cmd the command to be exectued
     */
    public void handleCommand(ChannelUID c, Command cmd) {
        Class<? extends Command> key = cmd.getClass();
        CommandHandler h = m_commandHandlers.containsKey(key) ? m_commandHandlers.get(key) : m_defaultCommandHandler;
        logger.trace("{} uses {} to handle command {} for {}", getName(), h.getClass().getSimpleName(),
                key.getSimpleName(), getDevice().getAddress());
        h.handleCommand(c, cmd, getDevice());
    }

    /**
     * Make a poll message using the configured poll message handler
     *
     * @return the poll message
     */
    public Msg makePollMsg() {
        if (m_pollHandler == null) {
            return null;
        }
        logger.trace("{} making poll msg for {} using handler {}", getName(), getDevice().getAddress(),
                m_pollHandler.getClass().getSimpleName());
        Msg m = m_pollHandler.makeMsg(m_device);
        return m;
    }

    /**
     * Publish new state to all device feature listeners, but give them
     * additional dataKey and dataValue information so they can decide
     * whether to publish the data to the bus.
     *
     * @param newState state to be published
     * @param changeType what kind of changes to publish
     * @param dataKey the key on which to filter
     * @param dataValue the value that must be matched
     */
    public void publish(State newState, StateChangeType changeType, String dataKey, String dataValue) {
        logger.debug("{}:{} publishing: {}", this.getDevice().getAddress(), getName(), newState);
        synchronized (m_listeners) {
            for (DeviceFeatureListener listener : m_listeners) {
                listener.stateChanged(newState, changeType, dataKey, dataValue);
            }
        }
    }

    /**
     * Publish new state to all device feature listeners
     *
     * @param newState state to be published
     * @param changeType what kind of changes to publish
     */
    public void publish(State newState, StateChangeType changeType) {
        logger.debug("{}:{} publishing: {}", this.getDevice().getAddress(), getName(), newState);
        synchronized (m_listeners) {
            for (DeviceFeatureListener listener : m_listeners) {
                listener.stateChanged(newState, changeType);
            }
        }
    }

    /**
     * Adds a message handler to this device feature.
     *
     * @param cm1 The insteon cmd1 of the incoming message for which the handler should be used
     * @param handler the handler to invoke
     */
    public void addMessageHandler(int cm1, MessageHandler handler) {
        synchronized (m_msgHandlers) {
            m_msgHandlers.put(cm1, handler);
        }
    }

    /**
     * Adds a command handler to this device feature
     *
     * @param c the command for which this handler is invoked
     * @param handler the handler to call
     */
    public void addCommandHandler(Class<? extends Command> c, CommandHandler handler) {
        synchronized (m_commandHandlers) {
            m_commandHandlers.put(c, handler);
        }
    }

    /**
     * Turn DeviceFeature into String
     */
    @Override
    public String toString() {
        return m_name + "(" + m_listeners.size() + ":" + m_commandHandlers.size() + ":" + m_msgHandlers.size() + ")";
    }
}
