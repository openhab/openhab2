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
 * The {@link GroupAction} is send by the websocket connection as well as the Rest API.
 * It is part of a {@link GroupMessage}.
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class GroupAction {
    public @Nullable Boolean on;
    public @Nullable Boolean toggle;
    public @Nullable Integer bri;
    public @Nullable Integer hue;
    public @Nullable Integer sat;
    public @Nullable Integer ct;
    public double @Nullable [] xy;
    public @Nullable String alert;
    public @Nullable String effect;
    public @Nullable Integer colorloopspeed;
    public @Nullable Integer transitiontime;
}
