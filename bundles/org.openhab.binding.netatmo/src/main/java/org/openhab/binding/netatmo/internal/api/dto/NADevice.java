/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.netatmo.internal.api.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.netatmo.internal.deserialization.NAThingMap;

/**
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public class NADevice extends NAThing {
    private NAThingMap modules = new NAThingMap();
    private long dateSetup;
    private long lastUpgrade;
    private @Nullable NAPlace place;

    public NAThingMap getModules() {
        return modules;
    }

    public long getDateSetup() {
        return dateSetup;
    }

    public long getLastUpgrade() {
        return lastUpgrade;
    }

    public @Nullable NAPlace getPlace() {
        return place;
    }

    public void setPlace(@Nullable NAPlace place) {
        this.place = place;
    }
}
