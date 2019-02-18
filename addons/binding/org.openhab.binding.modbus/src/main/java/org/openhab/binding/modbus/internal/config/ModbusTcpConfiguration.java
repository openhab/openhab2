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
package org.openhab.binding.modbus.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Configuration for tcp thing
 *
 * @author Sami Salonen - Initial contribution
 *
 */
@NonNullByDefault
public class ModbusTcpConfiguration {
    @Nullable
    private String host;
    private int port;
    private int id;
    private int timeBetweenTransactionsMillis;
    private int timeBetweenReconnectMillis;
    private int connectMaxTries;
    private int reconnectAfterMillis;
    private int connectTimeoutMillis;

    public @Nullable String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeBetweenTransactionsMillis() {
        return timeBetweenTransactionsMillis;
    }

    public void setTimeBetweenTransactionsMillis(int timeBetweenTransactionsMillis) {
        this.timeBetweenTransactionsMillis = timeBetweenTransactionsMillis;
    }

    public int getTimeBetweenReconnectMillis() {
        return timeBetweenReconnectMillis;
    }

    public void setTimeBetweenReconnectMillis(int timeBetweenReconnectMillis) {
        this.timeBetweenReconnectMillis = timeBetweenReconnectMillis;
    }

    public int getConnectMaxTries() {
        return connectMaxTries;
    }

    public void setConnectMaxTries(int connectMaxTries) {
        this.connectMaxTries = connectMaxTries;
    }

    public int getReconnectAfterMillis() {
        return reconnectAfterMillis;
    }

    public void setReconnectAfterMillis(int reconnectAfterMillis) {
        this.reconnectAfterMillis = reconnectAfterMillis;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

}
