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
package org.openhab.binding.e3dc.internal;

/**
 * The {@link E3DCConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Bernd Weymann - Initial contribution
 */
public class E3DCConfiguration {

    /**
     * Refresh interval in seconds
     */
    public long refreshInterval_sec = 2;

    public int maxTries = 3;// backwards compatibility and tests

    /**
     * Base address of the block to parse. Only used at manual setup
     */
    public int address;

}
