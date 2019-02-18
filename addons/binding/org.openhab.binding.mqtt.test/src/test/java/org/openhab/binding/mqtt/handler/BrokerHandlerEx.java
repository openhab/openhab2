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
package org.openhab.binding.mqtt.handler;

import static org.mockito.Mockito.verify;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.io.transport.mqtt.MqttBrokerConnection;
import org.mockito.Mockito;
import org.openhab.binding.mqtt.handler.BrokerHandler;

/**
 * Overwrite BrokerHandler to return our mocked/extended MqttBrokerConnection in
 * {@link #createBrokerConnection()}.
 *
 * @author David Graeff - Initial contribution
 */
public class BrokerHandlerEx extends BrokerHandler {
    final MqttBrokerConnectionEx e;

    public BrokerHandlerEx(Bridge thing, MqttBrokerConnectionEx e) {
        super(thing);
        this.e = e;
    }

    @Override
    protected @NonNull MqttBrokerConnection createBrokerConnection() throws IllegalArgumentException {
        return e;
    }

    public static void verifyCreateBrokerConnection(BrokerHandler handler, int times) {
        verify(handler, Mockito.times(times)).createBrokerConnection();
    }
}