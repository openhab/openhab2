/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.handler;

import static org.eclipse.smarthome.core.thing.ThingStatus.*;
import static org.openhab.binding.plugwise.internal.protocol.field.DeviceType.STICK;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.plugwise.internal.PlugwiseCommunicationHandler;
import org.openhab.binding.plugwise.internal.PlugwiseCommunicationHandler.MessagePriority;
import org.openhab.binding.plugwise.internal.PlugwiseDeviceTask;
import org.openhab.binding.plugwise.internal.PlugwiseInitializationException;
import org.openhab.binding.plugwise.internal.PlugwiseUtils;
import org.openhab.binding.plugwise.internal.config.PlugwiseStickConfig;
import org.openhab.binding.plugwise.internal.listener.PlugwiseMessageListener;
import org.openhab.binding.plugwise.internal.listener.PlugwiseStickStatusListener;
import org.openhab.binding.plugwise.internal.protocol.AcknowledgementMessage;
import org.openhab.binding.plugwise.internal.protocol.AcknowledgementMessage.ExtensionCode;
import org.openhab.binding.plugwise.internal.protocol.InformationRequestMessage;
import org.openhab.binding.plugwise.internal.protocol.InformationResponseMessage;
import org.openhab.binding.plugwise.internal.protocol.Message;
import org.openhab.binding.plugwise.internal.protocol.NetworkStatusRequestMessage;
import org.openhab.binding.plugwise.internal.protocol.NetworkStatusResponseMessage;
import org.openhab.binding.plugwise.internal.protocol.field.DeviceType;
import org.openhab.binding.plugwise.internal.protocol.field.MACAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The {@link PlugwiseStickHandler} handles channel updates and commands for a Plugwise Stick device.
 * </p>
 * <p>
 * The Stick is an USB ZigBee controller that communicates with the Circle+. It is a {@link Bridge} to the devices on a
 * Plugwise ZigBee mesh network.
 * </p>
 *
 * @author Karel Goderis
 * @author Wouter Born - Initial contribution
 */
public class PlugwiseStickHandler extends BaseBridgeHandler implements PlugwiseMessageListener {

    private final PlugwiseDeviceTask onlineStateUpdateTask = new PlugwiseDeviceTask("Online state update", scheduler) {
        @Override
        public int getConfiguredInterval() {
            return 20;
        }

        @Override
        public void runTask() {
            initialize();
        }

        @Override
        public boolean shouldBeScheduled() {
            return thing.getStatus() == OFFLINE;
        }
    };

    private final Logger logger = LoggerFactory.getLogger(PlugwiseStickHandler.class);

    private final PlugwiseCommunicationHandler communicationHandler = new PlugwiseCommunicationHandler();
    private PlugwiseStickConfig configuration;
    private List<PlugwiseStickStatusListener> statusListeners = new CopyOnWriteArrayList<>();

    private MACAddress circlePlusMAC;
    private MACAddress stickMAC;

    public PlugwiseStickHandler(Bridge bridge) {
        super(bridge);
    }

    public void addMessageListener(PlugwiseMessageListener listener) {
        communicationHandler.addMessageListener(listener);
    }

    public void addMessageListener(PlugwiseMessageListener listener, MACAddress macAddress) {
        communicationHandler.addMessageListener(listener, macAddress);
    }

    public void addStickStatusListener(PlugwiseStickStatusListener listener) {
        statusListeners.add(listener);
        listener.stickStatusChanged(thing.getStatus());
    }

    @Override
    public void dispose() {
        communicationHandler.stop();
        communicationHandler.removeMessageListener(this);
        onlineStateUpdateTask.stop();
    }

    public MACAddress getCirclePlusMAC() {
        return circlePlusMAC;
    }

    public MACAddress getStickMAC() {
        return stickMAC;
    }

    private void handleAcknowledgement(AcknowledgementMessage acknowledge) {
        if (acknowledge.isExtended() && acknowledge.getExtensionCode() == ExtensionCode.CIRCLE_PLUS) {
            circlePlusMAC = acknowledge.getMACAddress();
            logger.debug("Received extended acknowledgement, Circle+ MAC: {}", circlePlusMAC);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling command, channelUID: {}, command: {}", channelUID, command);
    }

    private void handleDeviceInformationResponse(InformationResponseMessage message) {
        if (message.getDeviceType() == STICK) {
            updateProperties(message);
        }
    }

    private void handleNetworkStatusResponse(NetworkStatusResponseMessage message) {
        stickMAC = message.getMACAddress();
        if (message.isOnline()) {
            circlePlusMAC = message.getCirclePlusMAC();
            logger.debug("The network is online: circlePlusMAC={}, stickMAC={}", circlePlusMAC, stickMAC);
            updateStatus(ONLINE);
            sendMessage(new InformationRequestMessage(stickMAC));

        } else {
            logger.debug("The network is offline: circlePlusMAC={}, stickMAC={}", circlePlusMAC, stickMAC);
            updateStatus(OFFLINE);
        }
    }

    @Override
    public void handleReponseMessage(Message message) {
        switch (message.getType()) {
            case ACKNOWLEDGEMENT_V1:
            case ACKNOWLEDGEMENT_V2:
                handleAcknowledgement((AcknowledgementMessage) message);
                break;
            case DEVICE_INFORMATION_RESPONSE:
                handleDeviceInformationResponse((InformationResponseMessage) message);
                break;
            case NETWORK_STATUS_RESPONSE:
                handleNetworkStatusResponse((NetworkStatusResponseMessage) message);
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize() {
        configuration = getConfigAs(PlugwiseStickConfig.class);
        communicationHandler.setConfiguration(configuration);
        communicationHandler.addMessageListener(this);

        try {
            communicationHandler.start();
            sendMessage(new NetworkStatusRequestMessage());
        } catch (PlugwiseInitializationException e) {
            communicationHandler.stop();
            communicationHandler.removeMessageListener(this);
            updateStatus(OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    public void removeMessageListener(PlugwiseMessageListener listener) {
        communicationHandler.removeMessageListener(listener);
    }

    public void removeMessageListener(PlugwiseMessageListener listener, MACAddress macAddress) {
        communicationHandler.addMessageListener(listener, macAddress);
    }

    public void removeStickStatusListener(PlugwiseStickStatusListener listener) {
        statusListeners.remove(listener);
    }

    private void sendMessage(Message message) {
        sendMessage(message, MessagePriority.UPDATE_AND_DISCOVERY);
    }

    public void sendMessage(Message message, MessagePriority priority) {
        try {
            communicationHandler.sendMessage(message, priority);
        } catch (IOException e) {
            communicationHandler.stop();
            communicationHandler.removeMessageListener(this);
            updateStatus(OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
        }
    }

    protected void updateProperties(InformationResponseMessage message) {
        Map<String, String> properties = editProperties();
        boolean update = PlugwiseUtils.updateProperties(properties, message);

        if (update) {
            updateProperties(properties);
        }
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail detail, String comment) {
        super.updateStatus(status, detail, comment);
        logger.debug("Updating listeners with status {}", status);
        for (PlugwiseStickStatusListener listener : statusListeners) {
            listener.stickStatusChanged(status);
        }
        updateTask(onlineStateUpdateTask);
    }

    protected void updateTask(PlugwiseDeviceTask task) {
        if (task.shouldBeScheduled()) {
            if (!task.isScheduled() || task.getConfiguredInterval() != task.getInterval()) {
                if (task.isScheduled()) {
                    task.stop();
                }
                task.update(DeviceType.STICK, getStickMAC());
                task.start();
            }
        } else if (!task.shouldBeScheduled() && task.isScheduled()) {
            task.stop();
        }
    }

}
