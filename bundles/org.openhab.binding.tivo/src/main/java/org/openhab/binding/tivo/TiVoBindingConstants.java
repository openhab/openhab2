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
package org.openhab.binding.tivo;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link TiVoBinding} class defines common constants that are
 * used across the whole binding.
 *
 * @author Jayson Kubilis (DigitalBytes) - Initial contribution
 * @author Andrew Black (AndyXMB) - Addition of Min / Max Channel and channel scanning properties
 * @author Michael Lobstein - Updated for OH3
 */

@NonNullByDefault
public class TiVoBindingConstants {
    public static final String BINDING_ID = "tivo";
    public static final int CONFIG_SOCKET_TIMEOUT_MS = 1000;
    public static final int INIT_POLLING_DELAY_S = 5;

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_TIVO = new ThingTypeUID(BINDING_ID, "sckt");

    // List of all Channel ids
    public static final String CHANNEL_TIVO_CHANNEL_FORCE = "channelForce";
    public static final String CHANNEL_TIVO_CHANNEL_SET = "channelSet";
    public static final String CHANNEL_TIVO_IS_RECORDING = "isRecording";
    public static final String CHANNEL_TIVO_TELEPORT = "menuTeleport";
    public static final String CHANNEL_TIVO_IRCMD = "irCommand";
    public static final String CHANNEL_TIVO_KBDCMD = "kbdCommand";
    public static final String CHANNEL_TIVO_STATUS = "dvrStatus";

    // List of all configuration Properties
    public static final String CONFIG_ADDRESS = "address";
    public static final String CONFIG_PORT = "tcpPort";
    public static final String CONFIG_CONNECTION_RETRY = "numRetry";
    public static final String CONFIG_KEEP_CONNECTION_OPEN = "keepConActive";
    public static final String CONFIG_POLL_FOR_CHANGES = "pollForChanges";
    public static final String CONFIG_POLL_INTERVAL = "pollInterval";
    public static final String CONFIG_CMD_WAIT_INTERVAL = "cmdWaitInterval";
}
