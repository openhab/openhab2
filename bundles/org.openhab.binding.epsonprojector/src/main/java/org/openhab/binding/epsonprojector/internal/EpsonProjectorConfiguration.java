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
package org.openhab.binding.epsonprojector.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link EpsonProjectorConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Yannick Schaus - Initial contribution
 */
@NonNullByDefault
public class EpsonProjectorConfiguration {

    /**
     * Serial port used for communication.
     */
    public @Nullable String serialPort;

    /**
     * Host or IP address used for communication over a TCP link (if serialPort is not set).
     */
    public @Nullable String host;

    /**
     * Port used for communication over a TCP link (if serialPort is not set).
     */
    public int port;

    /**
     * Polling interval to refresh states.
     */
    public int pollingInterval;
}
