/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.doorbird.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link ControllerConfiguration} class contains fields mapping thing configuration parameters
 * for the Doorbird A1081 Controller..
 *
 * @author Mark Hilbush - Initial contribution
 */
@NonNullByDefault
public class ControllerConfiguration {
    /**
     * Hostname or IP address of the Doorbird doorbell to which the controller is assigned
     */
    public @Nullable String doorbirdHost;

    /**
     * User ID of the Doorbird doorbell to which the controller is assigned
     */
    public @Nullable String userId;

    /**
     * Password of the Doorbird doorbell to which the controller is assigned
     */
    public @Nullable String userPassword;

    /**
     * Id of the Doorbird device
     */
    public @Nullable String controllerId;
}
