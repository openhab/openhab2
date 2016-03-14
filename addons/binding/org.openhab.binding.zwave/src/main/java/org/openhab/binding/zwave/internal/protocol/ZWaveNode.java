/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.zwave.internal.HexToIntegerConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveSecurityCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeInitStage;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeStageAdvancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Z-Wave node class. Represents a node in the Z-Wave network.
 *
 * @author Chris Jackson
 * @author Brian Crosby
 */
@XStreamAlias("node")
public class ZWaveNode {

    @XStreamOmitField
    private static final Logger logger = LoggerFactory.getLogger(ZWaveNode.class);

    private final ZWaveDeviceClass deviceClass;
    @XStreamOmitField
    private ZWaveController controller;
    @XStreamOmitField
    private ZWaveNodeStageAdvancer nodeStageAdvancer;
    @XStreamOmitField
    private ZWaveNodeState nodeState;

    @XStreamConverter(HexToIntegerConverter.class)
    private int homeId = Integer.MAX_VALUE;
    private int nodeId = Integer.MAX_VALUE;
    private int version = Integer.MAX_VALUE;

    @XStreamConverter(HexToIntegerConverter.class)
    private int manufacturer = Integer.MAX_VALUE;
    @XStreamConverter(HexToIntegerConverter.class)
    private int deviceId = Integer.MAX_VALUE;
    @XStreamConverter(HexToIntegerConverter.class)
    private int deviceType = Integer.MAX_VALUE;

    private boolean listening; // i.e. sleeping
    private boolean frequentlyListening;
    private boolean routing;
    private String healState;
    @SuppressWarnings("unused")
    private boolean security;
    @SuppressWarnings("unused")
    private boolean beaming;
    @SuppressWarnings("unused")
    private int maxBaudRate;

    // Keep the NIF - just used for information and debug in the XML
    @SuppressWarnings("unused")
    private List<Integer> nodeInformationFrame = null;

    private Map<CommandClass, ZWaveCommandClass> supportedCommandClasses = new HashMap<CommandClass, ZWaveCommandClass>();
    private final Set<CommandClass> securedCommandClasses = new HashSet<CommandClass>();

    private List<Integer> nodeNeighbors = new ArrayList<Integer>();
    private Date lastSent = null;
    private Date lastReceived = null;

    @XStreamOmitField
    private boolean applicationUpdateReceived = false;

    @XStreamOmitField
    private int resendCount = 0;

    @XStreamOmitField
    private int receiveCount = 0;
    @XStreamOmitField
    private int sendCount = 0;
    @XStreamOmitField
    private int deadCount = 0;
    @XStreamOmitField
    private Date deadTime;
    @XStreamOmitField
    private int retryCount = 0;

    /**
     * Constructor. Creates a new instance of the ZWaveNode class.
     *
     * @param homeId the home ID to use.
     * @param nodeId the node ID to use.
     * @param controller the wave controller instance
     */
    public ZWaveNode(int homeId, int nodeId, ZWaveController controller) {
        nodeState = ZWaveNodeState.INITIALIZING;
        this.homeId = homeId;
        this.nodeId = nodeId;
        this.controller = controller;
        this.nodeStageAdvancer = new ZWaveNodeStageAdvancer(this, controller);
        this.deviceClass = new ZWaveDeviceClass(Basic.NOT_KNOWN, Generic.NOT_KNOWN, Specific.NOT_USED);
    }

    /**
     * Configures the node after it's been restored from file.
     * NOTE: XStream doesn't run any default constructor. So, any initialisation
     * made in a constructor, or statically, won't be performed!!!
     * Set defaults here if it's important!!!
     *
     * @param controller the wave controller instance
     */
    public void setRestoredFromConfigfile(ZWaveController controller) {
        nodeState = ZWaveNodeState.INITIALIZING;

        this.controller = controller;

        // Create the initialisation advancer and tell it we've loaded from file
        this.nodeStageAdvancer = new ZWaveNodeStageAdvancer(this, controller);
        this.nodeStageAdvancer.setRestoredFromConfigfile();
        nodeStageAdvancer.setCurrentStage(ZWaveNodeInitStage.EMPTYNODE);
    }

    /**
     * Gets the node ID.
     *
     * @return the node id
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Gets whether the node is listening.
     *
     * @return boolean indicating whether the node is listening or not.
     */
    public boolean isListening() {
        return listening;
    }

    /**
     * Sets whether the node is listening.
     *
     * @param listening
     */
    public void setListening(boolean listening) {
        this.listening = listening;
    }

    /**
     * Gets whether the node is frequently listening.
     * Frequently listening is responding to a beam signal. Apart from
     * increased latency, nothing else is noticeable from the serial api
     * side.
     *
     * @return boolean indicating whether the node is frequently
     *         listening or not.
     */
    public boolean isFrequentlyListening() {
        return frequentlyListening;
    }

    /**
     * Sets whether the node is frequently listening.
     * Frequently listening is responding to a beam signal. Apart from
     * increased latency, nothing else is noticeable from the serial api
     * side.
     *
     * @param frequentlyListening indicating whether the node is frequently
     *            listening or not.
     */
    public void setFrequentlyListening(boolean frequentlyListening) {
        this.frequentlyListening = frequentlyListening;
    }

    /**
     * Gets the Heal State of the node.
     *
     * @return String indicating the node Heal State.
     */
    public String getHealState() {
        return healState;
    }

    /**
     * Sets the Heal State of the node.
     *
     * @param healState
     */
    public void setHealState(String healState) {
        this.healState = healState;
    }

    /**
     * Gets whether the node is dead.
     *
     * @return
     */
    public boolean isDead() {
        if (nodeState == ZWaveNodeState.DEAD || nodeState == ZWaveNodeState.FAILED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the node to be 'undead'.
     */
    public void setNodeState(ZWaveNodeState state) {
        if (nodeId == 8) {
            nodeId = 8;
        }
        // Make sure we only handle real state changes
        if (state == nodeState) {
            return;
        }

        switch (state) {
            case ALIVE:
                logger.debug("NODE {}: Node is ALIVE. Init stage is {}:{}.", nodeId,
                        this.getNodeInitializationStage().toString());

                // Reset the resend counter
                this.resendCount = 0;
                break;

            case DEAD:
                // If the node is failed, then we don't allow transitions to DEAD
                // The only valid state change from FAILED is to ALIVE
                if (nodeState == ZWaveNodeState.FAILED) {
                    return;
                }
            case FAILED:
                this.deadCount++;
                this.deadTime = Calendar.getInstance().getTime();
                logger.debug("NODE {}: Node is DEAD.", this.nodeId);
                break;
            case INITIALIZING:
                break;
        }

        // Don't alert state changes while we're still initialising
        // if (nodeStageAdvancer.isInitializationComplete() == true) {
        ZWaveEvent zEvent = new ZWaveNodeStatusEvent(this.getNodeId(), state);
        controller.notifyEventListeners(zEvent);
        // } else {
        // logger.debug("NODE {}: Initialisation incomplete, not signalling state change.", this.nodeId);
        // }

        nodeState = state;
    }

    /**
     * Gets the home ID
     *
     * @return the homeId
     */
    public Integer getHomeId() {
        return homeId;
    }

    /**
     * Gets the manufacturer of the node.
     *
     * @return the manufacturer. If not set Integer.MAX_VALUE is returned.
     */
    public int getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the manufacturer of the node.
     *
     * @param tempMan the manufacturer to set
     */
    public void setManufacturer(int tempMan) {
        this.manufacturer = tempMan;
    }

    /**
     * Gets the device id of the node.
     *
     * @return the deviceId. If not set Integer.MAX_VALUE is returned.
     */
    public int getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the device id of the node.
     *
     * @param deviceId the device to set
     */
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Gets the device type of the node.
     *
     * @return the deviceType. If not set Integer.MAX_VALUE is returned.
     */
    public int getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the device type of the node.
     *
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * Get the date/time the node was last updated (ie a frame was received from it).
     *
     * @return the lastUpdated time
     */
    public Date getLastReceived() {
        return lastReceived;
    }

    /**
     * Get the date/time we last sent a frame to the node.
     *
     * @return the lastSent
     */
    public Date getLastSent() {
        return lastSent;
    }

    /**
     * Gets the node state.
     *
     * @return the nodeState
     */
    public ZWaveNodeState getNodeState() {
        return this.nodeState;
    }

    /**
     * Gets the node stage.
     *
     * @return the nodeStage
     */
    public ZWaveNodeInitStage getNodeInitializationStage() {
        return this.nodeStageAdvancer.getCurrentStage();
    }

    /**
     * Gets the initialization state
     *
     * @return true if initialization has been completed
     */
    public boolean isInitializationComplete() {
        return this.nodeStageAdvancer.isInitializationComplete();
    }

    /**
     * Sets the node stage.
     *
     * @param nodeStage the nodeStage to set
     */
    public void setNodeStage(ZWaveNodeInitStage nodeStage) {
        nodeStageAdvancer.setCurrentStage(nodeStage);
    }

    /**
     * Gets the node version
     *
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the node version.
     *
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Gets the node application firmware version
     *
     * @return the version
     */
    public String getApplicationVersion() {
        ZWaveVersionCommandClass versionCmdClass = (ZWaveVersionCommandClass) this
                .getCommandClass(CommandClass.VERSION);
        if (versionCmdClass == null) {
            return "0.0";
        }

        String appVersion = versionCmdClass.getApplicationVersion();
        if (appVersion == null) {
            logger.trace("NODE {}: App version requested but version is unknown", this.getNodeId());
            return "0.0";
        }

        return appVersion;
    }

    /**
     * Gets whether the node is routing messages.
     *
     * @return the routing
     */
    public boolean isRouting() {
        return routing;
    }

    /**
     * Sets whether the node is routing messages.
     *
     * @param routing the routing to set
     */
    public void setRouting(boolean routing) {
        this.routing = routing;
    }

    /**
     * Gets the time stamp the node was last queried.
     *
     * @return the queryStageTimeStamp
     */
    public Date getQueryStageTimeStamp() {
        return this.nodeStageAdvancer.getQueryStageTimeStamp();
    }

    /**
     * Increments the resend counter.
     * On three increments the node stage is set to DEAD and no
     * more messages will be sent.
     * This is only used for SendData messages.
     */
    public void incrementResendCount() {
        if (++resendCount >= 3) {
            setNodeState(ZWaveNodeState.DEAD);
        }
        this.retryCount++;
    }

    /**
     * Resets the resend counter and possibly resets the
     * node stage to DONE when previous initialization was
     * complete.
     * Note that if the node is DEAD, then the nodeStage stays DEAD
     */
    public void resetResendCount() {
        this.resendCount = 0;
        if (this.nodeStageAdvancer.isInitializationComplete() && this.isDead() == false) {
            nodeStageAdvancer.setCurrentStage(ZWaveNodeInitStage.DONE);
        }
    }

    /**
     * Returns the device class of the node.
     *
     * @return the deviceClass
     */
    public ZWaveDeviceClass getDeviceClass() {
        return deviceClass;
    }

    /**
     * Returns the Command classes this node implements.
     *
     * @return the command classes.
     */
    public Collection<ZWaveCommandClass> getCommandClasses() {
        return supportedCommandClasses.values();
    }

    /**
     * Returns a commandClass object this node implements.
     * Returns null if command class is not supported by this node.
     *
     * @param commandClass The command class to get.
     * @return the command class.
     */
    public ZWaveCommandClass getCommandClass(CommandClass commandClass) {
        return supportedCommandClasses.get(commandClass);
    }

    /**
     * Returns whether a node supports this command class.
     *
     * @param commandClass the command class to check
     * @return true if the command class is supported, false otherwise.
     */
    public boolean supportsCommandClass(CommandClass commandClass) {
        return supportedCommandClasses.containsKey(commandClass);
    }

    /**
     * Adds a command class to the list of supported command classes by this node.
     * Does nothing if command class is already added.
     *
     * @param commandClass the command class instance to add.
     */
    public void addCommandClass(ZWaveCommandClass commandClass) {
        CommandClass key = commandClass.getCommandClass();

        if (!supportedCommandClasses.containsKey(key)) {
            logger.debug("NODE {}: Adding command class {} to the list of supported command classes.", nodeId,
                    commandClass.getCommandClass().getLabel());
            supportedCommandClasses.put(key, commandClass);

            if (commandClass instanceof ZWaveEventListener) {
                this.controller.addEventListener((ZWaveEventListener) commandClass);
            }
        }
    }

    /**
     * Removes a command class from the node.
     * This is used to remove classes that a node may report it supports
     * but it doesn't respond to.
     *
     * @param commandClass The command class key
     */
    public void removeCommandClass(CommandClass commandClass) {
        supportedCommandClasses.remove(commandClass);
    }

    /**
     * Resolves a command class for this node. First endpoint is checked.
     * If endpoint == 0 or (endpoint != 1 and version of the multi instance
     * command == 1) then return a supported command class on the node itself.
     * If endpoint != 1 and version of the multi instance command == 2 then
     * first try command classes of endpoints. If not found the return a
     * supported command class on the node itself.
     * Returns null if a command class is not found.
     *
     * @param commandClass The command class to resolve.
     * @param endpointId the endpoint / instance to resolve this command class for.
     * @return the command class.
     */
    public ZWaveCommandClass resolveCommandClass(CommandClass commandClass, int endpointId) {
        if (commandClass == null) {
            return null;
        }

        if (endpointId == 0) {
            return getCommandClass(commandClass);
        }

        ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) supportedCommandClasses
                .get(CommandClass.MULTI_INSTANCE);
        if (multiInstanceCommandClass == null) {
            return null;
        } else if (multiInstanceCommandClass.getVersion() == 2) {
            ZWaveEndpoint endpoint = multiInstanceCommandClass.getEndpoint(endpointId);

            if (endpoint != null) {
                ZWaveCommandClass result = endpoint.getCommandClass(commandClass);
                if (result != null) {
                    return result;
                }
            }
        } else if (multiInstanceCommandClass.getVersion() == 1) {
            ZWaveCommandClass result = getCommandClass(commandClass);
            if (result != null && endpointId <= result.getInstances()) {
                return result;
            }
        } else {
            logger.warn("NODE {}: Unsupported multi instance command version: {}.", nodeId,
                    multiInstanceCommandClass.getVersion());
        }

        return null;
    }

    /**
     * Initialise the node
     */
    public void initialiseNode() {
        this.nodeStageAdvancer.startInitialisation();
    }

    /**
     * Encapsulates a serial message for sending to a
     * multi-instance instance/ multi-channel endpoint on
     * a node.
     *
     * @param serialMessage the serial message to encapsulate
     * @param commandClass the command class used to generate the message.
     * @param endpointId the instance / endpoint to encapsulate the message for
     * @param node the destination node.
     * @return SerialMessage on success, null on failure.
     */
    public SerialMessage encapsulate(SerialMessage serialMessage, ZWaveCommandClass commandClass, int endpointId) {
        ZWaveMultiInstanceCommandClass multiInstanceCommandClass;

        if (serialMessage == null) {
            return null;
        }

        // no encapsulation necessary.
        if (endpointId == 0) {
            return serialMessage;
        }

        multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) this.getCommandClass(CommandClass.MULTI_INSTANCE);

        if (multiInstanceCommandClass != null) {
            logger.debug("NODE {}: Encapsulating message, instance / endpoint {}", this.getNodeId(), endpointId);
            switch (multiInstanceCommandClass.getVersion()) {
                case 2:
                    if (commandClass.getEndpoint() != null) {
                        serialMessage = multiInstanceCommandClass.getMultiChannelEncapMessage(serialMessage,
                                commandClass.getEndpoint());
                        return serialMessage;
                    }
                    break;
                case 1:
                default:
                    if (commandClass.getInstances() >= endpointId) {
                        serialMessage = multiInstanceCommandClass.getMultiInstanceEncapMessage(serialMessage,
                                endpointId);
                        return serialMessage;
                    }
                    break;
            }
        }

        logger.warn("NODE {}: Encapsulating message, instance / endpoint {} failed, will discard message.",
                this.getNodeId(), endpointId);
        return null;
    }

    /**
     * Return a list with the nodes neighbors
     *
     * @return list of node IDs
     */
    public List<Integer> getNeighbors() {
        return nodeNeighbors;
    }

    /**
     * Clear the neighbor list
     */
    public void clearNeighbors() {
        nodeNeighbors.clear();
    }

    /**
     * Updates a nodes routing information
     * Generation of routes uses associations
     *
     * @param nodeId
     */
    public ArrayList<Integer> getRoutingList() {
        logger.debug("NODE {}: Update return routes", nodeId);

        // Create a list of nodes this device is configured to talk to
        ArrayList<Integer> routedNodes = new ArrayList<Integer>();

        // Only update routes if this is a routing node
        if (isRouting() == false) {
            logger.debug("NODE {}: Node is not a routing node. No routes can be set.", nodeId);
            return null;
        }

        // Get the number of association groups reported by this node
        ZWaveAssociationCommandClass associationCmdClass = (ZWaveAssociationCommandClass) getCommandClass(
                CommandClass.ASSOCIATION);
        if (associationCmdClass == null) {
            logger.debug("NODE {}: Node has no association class. No routes can be set.", nodeId);
            return null;
        }

        int groups = associationCmdClass.getGroupCount();
        if (groups != 0) {
            // Loop through each association group and add the node ID to the list
            for (int group = 1; group <= groups; group++) {
                for (ZWaveAssociation associationNode : associationCmdClass.getGroupMembers(group).getAssociations()) {
                    routedNodes.add(associationNode.getNode());
                }
            }
        }

        // Add the wakeup destination node to the list for battery devices
        ZWaveWakeUpCommandClass wakeupCmdClass = (ZWaveWakeUpCommandClass) getCommandClass(CommandClass.WAKE_UP);
        if (wakeupCmdClass != null) {
            Integer wakeupNodeId = wakeupCmdClass.getTargetNodeId();
            routedNodes.add(wakeupNodeId);
        }

        // Are there any nodes to which we need to set routes?
        if (routedNodes.size() == 0) {
            logger.debug("NODE {}: No return routes required.", nodeId);
            return null;
        }

        return routedNodes;
    }

    /**
     * Add a node ID to the neighbor list
     *
     * @param nodeId the node to add
     */
    public void addNeighbor(Integer nodeId) {
        nodeNeighbors.add(nodeId);
    }

    /**
     * Gets the number of times the node has been determined as DEAD
     *
     * @return dead count
     */
    public int getDeadCount() {
        return deadCount;
    }

    /**
     * Gets the number of times the node has been determined as DEAD
     *
     * @return dead count
     */
    public Date getDeadTime() {
        return deadTime;
    }

    /**
     * Gets the number of packets that have been resent to the node
     *
     * @return retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Increments the sent packet counter and records the last sent time
     * This is simply used for statistical purposes to assess the health
     * of a node.
     */
    public void incrementSendCount() {
        sendCount++;
        this.lastSent = Calendar.getInstance().getTime();
    }

    /**
     * Increments the received packet counter and records the last received time
     * This is simply used for statistical purposes to assess the health
     * of a node.
     */
    public void incrementReceiveCount() {
        receiveCount++;
        this.lastReceived = Calendar.getInstance().getTime();
    }

    /**
     * Gets the number of packets sent to the node
     *
     * @return send count
     */
    public int getSendCount() {
        return sendCount;
    }

    /**
     * Gets the applicationUpdateReceived flag.
     * This is set to indicate that we have received the required information from the device
     *
     * @return true if information received
     */
    public boolean getApplicationUpdateReceived() {
        return applicationUpdateReceived;
    }

    /**
     * Sets the applicationUpdateReceived flag.
     * This is set to indicate that we have received the required information from the device
     *
     * @param received true if received
     */
    public void setApplicationUpdateReceived(boolean received) {
        applicationUpdateReceived = received;
    }

    @Override
    public String toString() {
        return String.format("Node %d. Manufacturer %04X, Type %04X, Id %04X", nodeId, manufacturer, deviceType,
                deviceId);
    }

    public void updateNIF(List<Integer> nif) {
        nodeInformationFrame = nif;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    /**
     * Gets whether the node supports beaming
     *
     * @return true if the node supports beaming
     */
    public boolean isBeaming() {
        return beaming;
    }

    /**
     * Sets whether the node supports beaming.
     *
     * @param beaming true if beaming is supported
     */
    public void setBeaming(boolean beaming) {
        this.beaming = beaming;
    }

    public void setMaxBaud(int maxBaudRate) {
        this.maxBaudRate = maxBaudRate;
    }

    /**
     * Invoked by {@link ZWaveSecurityCommandClass} when a
     * {@link ZWaveSecurityCommandClass#SECURITY_SUPPORTED_REPORT} is received.
     *
     * @param data the class id for each class which must be encrypted in transmission
     */
    public void setSecuredClasses(byte[] data) {
        logger.info("NODE {}:  Setting secured command classes for node with {}", this.getNodeId(),
                SerialMessage.bb2hex(data));
        boolean afterMark = false;
        securedCommandClasses.clear(); // reset the existing list
        for (final byte aByte : data) {
            // TODO: DB support extended commandClass format by checking for 0xF1 - 0xFF
            if (ZWaveSecurityCommandClass.bytesAreEqual(aByte, ZWaveSecurityCommandClass.COMMAND_CLASS_MARK)) {
                /**
                 * Marks the end of the list of supported command classes. The remaining classes are those
                 * that can be controlled by the device. These classes are created without values.
                 * Messages received cause notification events instead.
                 */
                afterMark = true;
                continue;
            }

            // Check if this is a commandClass that is already registered with the node
            final CommandClass commandClass = CommandClass.getCommandClass((aByte & 0xFF));
            if (commandClass == null) {
                // Not supported by OpenHab
                logger.error(
                        "NODE {}:  setSecuredClasses requested secure "
                                + "class NOT supported by OpenHab: {}   afterMark={}",
                        this.getNodeId(), commandClass, afterMark);
            } else {
                // Sometimes security will be transmitted as a secure class, but it
                // can't be set that way since it's the one doing the encryption work So ignore that.
                if (commandClass == CommandClass.SECURITY) {
                    continue;
                } else if (afterMark) {
                    // Nothing to do, we don't track devices that control other devices
                    logger.info("NODE {}:  is after mark for commandClass {}", this.getNodeId(), commandClass);
                    break;
                } else {
                    if (!this.supportsCommandClass(commandClass)) {
                        logger.info(
                                "NODE {}:  Adding secured command class to supported that wasn't in original list {}",
                                this.getNodeId(), commandClass.getLabel());
                        final ZWaveCommandClass classInstance = ZWaveCommandClass.getInstance((aByte & 0xFF), this,
                                controller);
                        if (classInstance != null) {
                            addCommandClass(classInstance);
                        }
                    }
                    securedCommandClasses.add(commandClass);
                    logger.info("NODE {}:  (Secured) {}", this.getNodeId(), commandClass.getLabel());
                }
            }
        }
        if (logger.isInfoEnabled()) {
            // show which classes are still insecure after the update
            final StringBuilder buf = new StringBuilder(
                    "NODE " + this.getNodeId() + ": After update, INSECURE command classes are: ");
            for (final ZWaveCommandClass zwCommandClass : this.getCommandClasses()) {
                if (!securedCommandClasses.contains(zwCommandClass.getCommandClass())) {
                    buf.append(zwCommandClass.getCommandClass() + ", ");
                }
            }
            logger.info(buf.toString().substring(0, buf.toString().length() - 1));
        }
    }

    public boolean doesMessageRequireSecurityEncapsulation(SerialMessage serialMessage) {
        boolean result = false;
        if (serialMessage.getMessageClass() != SerialMessageClass.SendData) {
            result = false;
        } else if (!supportedCommandClasses.containsKey(CommandClass.SECURITY)) {
            // Does this node support security at all?
            result = false;
        } else {
            final int commandClassCode = (byte) serialMessage.getMessagePayloadByte(2) & 0xFF;
            final CommandClass commandClassOfMessage = CommandClass.getCommandClass(commandClassCode);
            if (commandClassOfMessage == null) {
                // not sure how we would ever get here
                logger.warn(String.format("NODE %s: CommandClass not found for 0x%02X so treating as INSECURE %s",
                        getNodeId(), commandClassCode, serialMessage));
                result = false;
            } else if (CommandClass.SECURITY == commandClassOfMessage) {
                // CommandClass.SECURITY is a special case because only <b>some</b> commands get encrypted
                final Byte messageCode = Byte.valueOf((byte) (serialMessage.getMessagePayloadByte(3) & 0xFF));
                result = ZWaveSecurityCommandClass.doesCommandRequireSecurityEncapsulation(messageCode);
            } else if (commandClassOfMessage == CommandClass.NO_OPERATION) { // TODO: DB
                // On controller startup, PING seems to fail whenever it's encrypted, so don't
                // TODO: DB try again
                result = false;
            } else {
                result = securedCommandClasses.contains(commandClassOfMessage);
                if (!result) {
                    // Certain messages must always be sent securely per the zwave spec
                    if (commandClassOfMessage == CommandClass.DOOR_LOCK
                            || commandClassOfMessage == CommandClass.USER_CODE) { // TODO: DB what else?
                        logger.warn("NODE {}: CommandClass {} is not marked as secure but should be, forcing secure",
                                getNodeId(), commandClassOfMessage);
                        result = true;
                    }
                }
            }
            if (result) {
                logger.trace("NODE {}: Message {} requires security encapsulation", getNodeId(), serialMessage);
            }
        }
        return result;
    }

    public ZWaveController getController() {
        return controller;
    }
}
