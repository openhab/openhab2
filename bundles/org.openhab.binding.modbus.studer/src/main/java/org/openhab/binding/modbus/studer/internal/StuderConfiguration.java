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
package org.openhab.binding.modbus.studer.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link StuderConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Giovanni Mirulla - Initial contribution
 */
@NonNullByDefault
public class StuderConfiguration {

    /**
     * Refresh interval in seconds
     */
    private static int refresh = 5;

    /**
     * Max tries for one register
     */
    public int maxTries = 3;

    /**
     * Get refresh period
     */
    public static int getRefresh() {
        return refresh;
    }

    /**
     * Set refresh period
     */
    public void setRefresh(int refresh) {
        StuderConfiguration.refresh = refresh;
    }

}
