/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.bluetooth.bluegiga.internal.command.connection;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaDeviceCommand;

/**
 * Class to implement the BlueGiga command <b>disconnect</b>.
 * <p>
 * This command disconnects an active connection. Bluetooth When link is disconnected a
 * Disconnected event is produced.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 * @author Pauli Anttila - Added message builder
 */
@NonNullByDefault
public class BlueGigaDisconnectCommand extends BlueGigaDeviceCommand {
    public static int COMMAND_CLASS = 0x03;
    public static int COMMAND_METHOD = 0x00;

    private BlueGigaDisconnectCommand(CommandBuilder builder) {
        super.setConnection(builder.connection);
    }

    @Override
    public int[] serialize() {
        // Serialize the header
        serializeHeader(COMMAND_CLASS, COMMAND_METHOD);

        // Serialize the fields
        serializeUInt8(connection);

        return getPayload();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaDisconnectCommand [connection=");
        builder.append(connection);
        builder.append(']');
        return builder.toString();
    }

    public static class CommandBuilder {
        private int connection;

        /**
         * Set connection handle.
         *
         * @param connection the connection to set as {@link int}
         */
        public final CommandBuilder withConnection(int connection) {
            this.connection = connection;
            return this;
        }

        public BlueGigaDisconnectCommand build() {
            return new BlueGigaDisconnectCommand(this);
        }
    }
}
