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
package org.openhab.binding.somfytahoma.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link SomfyTahomaSetup} holds information about devices bound
 * to TahomaLink account.
 *
 * @author Ondrej Pecta - Initial contribution
 */
@NonNullByDefault
public class SomfyTahomaSetup {

    private List<SomfyTahomaDevice> devices = new ArrayList<>();

    private List<SomfyTahomaGateway> gateways = new ArrayList<>();

    public List<SomfyTahomaDevice> getDevices() {
        return devices;
    }

    public List<SomfyTahomaGateway> getGateways() {
        return gateways;
    }
}
