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
package org.openhab.binding.freebox.internal.config;

/**
 * The {@link PlayerConfiguration} is responsible for holding
 * configuration informations needed to access/poll the freebox server
 *
 * @author Gaël L'hopital - Initial contribution
 */
public class PlayerConfiguration extends HostConfiguration {
    public static final String HOST_ADDRESS = "hostAddress";
    public static final String PORT = "port";

    public String hostAddress;
    public Integer port;
}
