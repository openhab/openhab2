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
package org.openhab.binding.ring.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link AccountConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Ben Rosenblum - Initial contribution
 */
@NonNullByDefault
public class AccountConfiguration {
    public @Nullable String username;
    public @Nullable String password;
    public @Nullable String hardwareId;
    public @Nullable String refreshToken;
    public @Nullable String twofactorCode;
    public int videoRetentionCount;
    public @Nullable String videoStoragePath;
    public int refreshInterval;
}
