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
package org.openhab.binding.amazonechocontrol.internal.dto.request;

import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link AnnouncementDisplayTO} encapsulates the display part of an announcement
 *
 * @author Jan N. Klug - Initial contribution
 */
public class AnnouncementDisplayTO {
    public String title;
    public String body;

    @Override
    public @NonNull String toString() {
        return "AnnouncementDisplayTO{title='" + title + "', body='" + body + "'}";
    }
}
