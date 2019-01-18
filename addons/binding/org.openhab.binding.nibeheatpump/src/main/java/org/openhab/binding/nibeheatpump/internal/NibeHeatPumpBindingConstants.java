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
package org.openhab.binding.nibeheatpump.internal;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link NibeHeatPumpBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Pauli Anttila - Initial contribution
 */
public class NibeHeatPumpBindingConstants {

    private static final String BINDING_ID = "nibeheatpump";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_F1X45_UDP = new ThingTypeUID(BINDING_ID, "f1x45-udp");
    public final static ThingTypeUID THING_TYPE_F1X45_SERIAL = new ThingTypeUID(BINDING_ID, "f1x45-serial");
    public final static ThingTypeUID THING_TYPE_F1X45_SIMULATOR = new ThingTypeUID(BINDING_ID, "f1x45-simulator");

    public final static ThingTypeUID THING_TYPE_F750_UDP = new ThingTypeUID(BINDING_ID, "f750-udp");
    public final static ThingTypeUID THING_TYPE_F750_SERIAL = new ThingTypeUID(BINDING_ID, "f750-serial");
    public final static ThingTypeUID THING_TYPE_F750_SIMULATOR = new ThingTypeUID(BINDING_ID, "f750-simulator");

    public final static ThingTypeUID THING_TYPE_F470_UDP = new ThingTypeUID(BINDING_ID, "f470-udp");
    public final static ThingTypeUID THING_TYPE_F470_SERIAL = new ThingTypeUID(BINDING_ID, "f470-serial");
    public final static ThingTypeUID THING_TYPE_F470_SIMULATOR = new ThingTypeUID(BINDING_ID, "f470-simulator");

    /**
     * Presents all supported thing types by NibeHeatPump binding.
     */
    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream.of(THING_TYPE_F1X45_UDP,
            THING_TYPE_F1X45_SERIAL, THING_TYPE_F1X45_SIMULATOR, THING_TYPE_F750_UDP, THING_TYPE_F750_SERIAL,
            THING_TYPE_F750_SIMULATOR, THING_TYPE_F470_UDP, THING_TYPE_F470_SERIAL, THING_TYPE_F470_SIMULATOR)
            .collect(Collectors.toSet());
}
