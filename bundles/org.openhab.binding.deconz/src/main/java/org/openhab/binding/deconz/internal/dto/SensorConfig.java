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
package org.openhab.binding.deconz.internal.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link SensorConfig} is send by the the Rest API.
 * It is part of a {@link SensorMessage}.
 *
 * This should be in sync with the supported sensors from
 * https://dresden-elektronik.github.io/deconz-rest-doc/sensors/.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class SensorConfig {
    public boolean on = true;
    public boolean reachable = true;
    public @Nullable Integer battery;
    public @Nullable Float temperature;
    public @Nullable Integer heatsetpoint;
    public @Nullable String mode;
    public @Nullable Integer offset;
}
