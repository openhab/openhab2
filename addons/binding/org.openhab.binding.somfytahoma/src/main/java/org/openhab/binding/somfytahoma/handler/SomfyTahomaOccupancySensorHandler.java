/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.somfytahoma.handler;

import org.eclipse.smarthome.core.thing.Thing;

import java.util.HashMap;

import static org.openhab.binding.somfytahoma.SomfyTahomaBindingConstants.CONTACT;

/**
 * The {@link SomfyTahomaOccupancySensorHandler} is responsible for handling commands,
 * which are sent to one of the channels of the occupancy sensor thing.
 *
 * @author Ondrej Pecta - Initial contribution
 */
public class SomfyTahomaOccupancySensorHandler extends SomfyTahomaContactSensorHandler {

    public SomfyTahomaOccupancySensorHandler(Thing thing) {
        super(thing);
        stateNames = new HashMap<String, String>() {{
            put(CONTACT, "core:OccupancyState");
        }};
    }

}
