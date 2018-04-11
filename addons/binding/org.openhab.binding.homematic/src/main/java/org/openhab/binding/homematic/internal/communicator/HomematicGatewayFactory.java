/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import java.io.IOException;

import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.communicator.client.RpcClient;
import org.openhab.binding.homematic.internal.communicator.client.XmlRpcClient;

/**
 * Factory which evaluates the type of the Homematic gateway and instantiates the appropriate class.
 *
 * @author Gerhard Riegler - Initial contribution
 */
public class HomematicGatewayFactory {

    /**
     * Creates the HomematicGateway.
     */
    public static HomematicGateway createGateway(String id, HomematicConfig config,
            HomematicGatewayAdapter gatewayAdapter, HttpClient httpClient)
            throws IOException {
        loadGatewayInfo(config, id, httpClient);
        if (config.getGatewayInfo().isCCU()) {
            return new CcuGateway(id, config, gatewayAdapter, httpClient);
        } else if (config.getGatewayInfo().isHomegear()) {
            return new HomegearGateway(id, config, gatewayAdapter, httpClient);
        } else {
            return new DefaultGateway(id, config, gatewayAdapter, httpClient);
        }
    }

    /**
     * Loads some metadata about the type of the Homematic gateway.
     */
    private static void loadGatewayInfo(HomematicConfig config, String id, HttpClient httpClient) throws IOException {
        RpcClient<String> rpcClient = new XmlRpcClient(config, httpClient);
        try {
            config.setGatewayInfo(rpcClient.getGatewayInfo(id));
        } finally {
            rpcClient.dispose();
        }
    }

}
