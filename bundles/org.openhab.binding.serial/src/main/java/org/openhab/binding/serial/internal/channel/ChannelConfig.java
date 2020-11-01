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
package org.openhab.binding.serial.internal.channel;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Class describing the channel user configuration
 *
 * @author Mike Major - Initial contribution
 */
@NonNullByDefault
public class ChannelConfig {
    /**
     * Transform for received data
     */
    public @Nullable String transform;

    /**
     * Transform for command
     */
    public @Nullable String commandTransform;

    /**
     * Format string for command
     */
    public @Nullable String commandFormat;

    /**
     * On value
     */
    public @Nullable String on;

    /**
     * Off value
     */
    public @Nullable String off;

    /**
     * Up value
     */
    public @Nullable String up;

    /**
     * Down value
     */
    public @Nullable String down;

    /**
     * Stop value
     */
    public @Nullable String stop;

    /**
     * Increase value
     */
    public @Nullable String increase;

    /**
     * Decrease value
     */
    public @Nullable String decrease;
}
