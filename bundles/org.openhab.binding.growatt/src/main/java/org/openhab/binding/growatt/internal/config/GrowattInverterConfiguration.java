/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.growatt.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link GrowattInverterConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Andrew Fiddian-Green - Initial contribution
 */
@NonNullByDefault
public class GrowattInverterConfiguration {

    public static final String DEVICE_ID = "deviceId";
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    public static final String PLANT_ID = "plantId";

    // required
    public String deviceId = "";

    // optional
    public @Nullable String userName;
    public @Nullable String password;

    // optional for tests only
    public @Nullable String userId;
    public @Nullable String plantId;
}
