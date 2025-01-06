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
package org.openhab.binding.jablotron.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link JablotronGetServiceData} class defines the data object for the
 * getServiceList response
 *
 * @author Ondrej Pecta - Initial contribution
 */
@NonNullByDefault
public class JablotronGetServiceData {

    List<JablotronDiscoveredService> services = new ArrayList<>();

    public List<JablotronDiscoveredService> getServices() {
        return services;
    }
}
