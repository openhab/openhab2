/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neeo.internal.models;

/**
 * The model representing an forward actions request (serialize/deserialize json use only).
 *
 * @author Tim Roberts - Initial contribution
 */
public class NeeoForwardActions {
    /** The host to forward actions to */
    private final String host;

    /** The port to use */
    private final int port;

    /** The path the actions should go to */
    private final String path;

    /**
     * Creates the forward actions from the given parms
     *
     * @param host the host name
     * @param port the port
     * @param path the path
     */
    public NeeoForwardActions(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    /**
     * Returns the host name to forward actions to
     *
     * @return the hostname
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the port number
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the path to use
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "NeeoForwardActions [host=" + host + ", port=" + port + ", path=" + path + "]";
    }
}
