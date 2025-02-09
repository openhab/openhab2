/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.bluetooth.bluegiga.internal.command.system;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.bluegiga.internal.BlueGigaResponse;

/**
 * Class to implement the BlueGiga command <b>bootEvent</b>.
 * <p>
 * This event is produced when the device boots up and is ready to receive commands. This event is
 * not sent over USB interface.
 * <p>
 * This class provides methods for processing BlueGiga API commands.
 * <p>
 * Note that this code is autogenerated. Manual changes may be overwritten.
 *
 * @author Chris Jackson - Initial contribution of Java code generator
 */
@NonNullByDefault
public class BlueGigaBootEvent extends BlueGigaResponse {
    public static final int COMMAND_CLASS = 0x00;
    public static final int COMMAND_METHOD = 0x00;

    /**
     * Major software version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int major;

    /**
     * Minor software version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int minor;

    /**
     * Patch ID
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int patch;

    /**
     * Build version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int build;

    /**
     * Link layer version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int llVersion;

    /**
     * Protocol version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int protocolVersion;

    /**
     * Hardware version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     */
    private int hardware;

    /**
     * Event constructor
     */
    public BlueGigaBootEvent(int[] inputBuffer) {
        // Super creates deserializer and reads header fields
        super(inputBuffer);

        event = (inputBuffer[0] & 0x80) != 0;

        // Deserialize the fields
        major = deserializeUInt16();
        minor = deserializeUInt16();
        patch = deserializeUInt16();
        build = deserializeUInt16();
        llVersion = deserializeUInt16();
        protocolVersion = deserializeUInt16();
        hardware = deserializeUInt16();
    }

    /**
     * Major software version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current major as {@link int}
     */
    public int getMajor() {
        return major;
    }

    /**
     * Minor software version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current minor as {@link int}
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Patch ID
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current patch as {@link int}
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Build version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current build as {@link int}
     */
    public int getBuild() {
        return build;
    }

    /**
     * Link layer version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current ll_version as {@link int}
     */
    public int getLlVersion() {
        return llVersion;
    }

    /**
     * Protocol version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current protocol_version as {@link int}
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Hardware version
     * <p>
     * BlueGiga API type is <i>uint16</i> - Java type is {@link int}
     *
     * @return the current hardware as {@link int}
     */
    public int getHardware() {
        return hardware;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("BlueGigaBootEvent [major=");
        builder.append(major);
        builder.append(", minor=");
        builder.append(minor);
        builder.append(", patch=");
        builder.append(patch);
        builder.append(", build=");
        builder.append(build);
        builder.append(", llVersion=");
        builder.append(llVersion);
        builder.append(", protocolVersion=");
        builder.append(protocolVersion);
        builder.append(", hardware=");
        builder.append(hardware);
        builder.append(']');
        return builder.toString();
    }
}
