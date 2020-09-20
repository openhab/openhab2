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
package org.openhab.binding.automower.internal.bridge;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link AutomowerBridgeConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Markus Pfleger - Initial contribution
 */
@NonNullByDefault
public final class AutomowerBridgeConfiguration {
    private @Nullable String appKey;
    private @Nullable String userName;
    private @Nullable String password;

    private @Nullable Integer pollingInterval;

    /**
     * @return The polling interval for the automower state in s
     */
    public @Nullable Integer getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(Integer pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public @Nullable String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public @Nullable String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
