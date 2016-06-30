/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.openhab.binding.homematic.internal.common.HomematicConfig;

/**
 * Waits for a message from the Homematic gateway and starts the RpcCallbackHandler to handle the message.
 *
 * @author Gerhard Riegler - Initial contribution
 */
public class BinRpcNetworkService implements Runnable {
    private ServerSocket serverSocket;
    private boolean accept = true;
    private RpcEventListener listener;
    private HomematicConfig config;

    /**
     * Creates the socket for listening to events from the Homematic gateway.
     */
    public BinRpcNetworkService(RpcEventListener listener, HomematicConfig config) throws IOException {
        this.listener = listener;
        this.config = config;

        serverSocket = new ServerSocket(config.getCallbackPort());
        serverSocket.setReuseAddress(true);
    }

    /**
     * Listening for events and starts the callbackHandler if a event received.
     */
    @Override
    public void run() {
        while (accept) {
            try {
                Socket cs = serverSocket.accept();
                BinRpcCallbackHandler rpcHandler = new BinRpcCallbackHandler(cs, listener, config.getEncoding());
                config.getScheduledPool().execute(rpcHandler);
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    /**
     * Stops the listening.
     */
    public void shutdown() {
        accept = false;
        try {
            serverSocket.close();
        } catch (IOException ioe) {
            // ignore
        }
    }

}
