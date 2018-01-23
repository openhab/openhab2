/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.discovery;

import static org.openhab.binding.homematic.HomematicBindingConstants.THING_TYPE_BRIDGE;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.concurrent.Future;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.homematic.internal.discovery.eq3udp.Eq3UdpRequest;
import org.openhab.binding.homematic.internal.discovery.eq3udp.Eq3UdpResponse;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovers Homematic CCU's and adds the results to the inbox.
 *
 * @author Gerhard Riegler - Initial contribution
 */
@Component(immediate = true, service = DiscoveryService.class)
public class CcuDiscoveryService extends AbstractDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(CcuDiscoveryService.class);

    private static final int RECEIVE_TIMEOUT_MSECS = 3000;
    private InetAddress broadcastAddress;
    private MulticastSocket socket;
    private Future<?> scanFuture;

    public CcuDiscoveryService() {
        super(Collections.singleton(THING_TYPE_BRIDGE), 5, true);
    }

    @Override
    protected void startScan() {
        if (scanFuture == null) {
            scanFuture = scheduler.submit(this::startDiscovery);
        } else {
            logger.debug("Homematic CCU background discovery scan in progress");
        }
    }

    @Override
    protected void stopScan() {
        if (scanFuture != null) {
            scanFuture.cancel(false);
            scanFuture = null;
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        // only start once at startup
        startScan();
    }

    @Override
    protected void stopBackgroundDiscovery() {
        stopScan();
    }

    private synchronized void startDiscovery() {
        try {
            logger.debug("Starting Homematic CCU discovery scan");
            broadcastAddress = InetAddress.getByName("255.255.255.255");
            socket = new MulticastSocket();
            socket.setBroadcast(true);
            socket.setTimeToLive(5);
            socket.setSoTimeout(800);

            sendBroadcast();
            receiveResponses();
        } catch (Exception ex) {
            logger.error("{}", ex.getMessage(), ex);
        } finally {
            scanFuture = null;
        }
    }

    /**
     * Sends a UDP hello broadcast message for CCU gateways.
     */
    private void sendBroadcast() throws IOException {
        Eq3UdpRequest hello = new Eq3UdpRequest();
        byte[] data = hello.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, broadcastAddress, 43439);
        socket.send(packet);
    }

    /**
     * Receives the UDP responses to the hello messages.
     */
    private void receiveResponses() throws IOException {
        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        while (currentTime - startTime < RECEIVE_TIMEOUT_MSECS) {
            extractGatewayInfos();
            currentTime = System.currentTimeMillis();
        }
        socket.close();
    }

    /**
     * Extracts the CCU infos from the UDP response.
     */
    private void extractGatewayInfos() throws IOException {
        try {
            DatagramPacket packet = new DatagramPacket(new byte[265], 256);
            socket.receive(packet);

            Eq3UdpResponse response = new Eq3UdpResponse(packet.getData());
            logger.trace("{}", response.toString());
            if (response.isValid()) {
                logger.debug("Discovered a CCU gateway with serial number '{}'", response.getSerialNumber());

                String address = packet.getAddress().getHostAddress();
                ThingUID thingUid = new ThingUID(THING_TYPE_BRIDGE, response.getSerialNumber());
                thingDiscovered(DiscoveryResultBuilder.create(thingUid).withProperty("gatewayAddress", address)
                        .withRepresentationProperty("gatewayAddress")
                        .withLabel(response.getDeviceTypeId() + " - " + address).build());
            }
        } catch (SocketTimeoutException ex) {
            // ignore
        }
    }

}