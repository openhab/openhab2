/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.bluetooth.bluegiga.internal.command.gap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaCommand;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.GapConnectableMode;
import org.openhab.binding.bluetooth.bluegiga.internal.enumeration.GapDiscoverableMode;

/**
 * Class to implement the BlueGiga command <b>setMode</b>.
 * <p>
 * This command configures the current GAP discoverability and connectability modes. It can
 * be used to enable advertisements and/or allow connection. The command is also meant to fully
 * stop advertising, when using gap_non_discoverable and gap_non_connectable.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 * @author Pauli Anttila - Added message builder
 */
@NonNullByDefault
public class BlueGigaSetModeCommand extends BlueGigaCommand {
    public static final int COMMAND_CLASS = 0x06;
    public static final int COMMAND_METHOD = 0x01;

    private BlueGigaSetModeCommand(CommandBuilder builder) {
        this.discover = builder.discover;
        this.connect = builder.connect;
    }

    /**
     * see:GAP Discoverable Mode
     * <p>
     * BlueGiga API type is <i>GapDiscoverableMode</i> - Java type is {@link GapDiscoverableMode}
     */
    private GapDiscoverableMode discover;

    /**
     * see:GAP Connectable Mode
     * <p>
     * BlueGiga API type is <i>GapConnectableMode</i> - Java type is {@link GapConnectableMode}
     */
    private GapConnectableMode connect;

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);

        // Serialize the fields
        serializeGapDiscoverableMode(discover);
        serializeGapConnectableMode(connect);

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaSetModeCommand [discover=");
        builder.append(discover);
        builder.append(", connect=");
        builder.append(connect);
        builder.append(']');
        return builder.toString();
    }

    public static class CommandBuilder {
        private GapDiscoverableMode discover = GapDiscoverableMode.UNKNOWN;
        private GapConnectableMode connect = GapConnectableMode.UNKNOWN;

        /**
         * see:GAP Discoverable Mode
         *
         * @param discover the discover to set as {@link GapDiscoverableMode}
         */
        public CommandBuilder withDiscover(GapDiscoverableMode discover) {
            this.discover = discover;
            return this;
        }

        /**
         * see:GAP Connectable Mode
         *
         * @param connect the connect to set as {@link GapConnectableMode}
         */
        public CommandBuilder withConnect(GapConnectableMode connect) {
            this.connect = connect;
            return this;
        }

        public BlueGigaSetModeCommand build() {
            return new BlueGigaSetModeCommand(this);
        }
    }
}
