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
package org.openhab.binding.mybmw.internal.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mybmw.internal.dto.vehicle.Vehicle;
import org.openhab.binding.mybmw.internal.util.FileReader;
import org.openhab.binding.mybmw.internal.utils.Converter;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link ConnectedDriveTest} Test json responses from ConnectedDrive Portal
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ConnectedDriveTest {

    @Test
    public void testUserInfo() {
        String content = FileReader.readFileInString("src/test/resources/responses/I01_REX/vehicles.json");
        List<Vehicle> vl = Converter.getVehicleList(content);

        assertEquals(1, vl.size(), "Number of Vehicles");
        Vehicle v = vl.get(0);
        assertEquals("MY_REAL_VIN", v.vin, "VIN");
        assertEquals("i3 94 (+ REX)", v.model, "Model");
        assertEquals("BEV_REX", v.driveTrain, "DriveTrain");
        assertEquals("BMW_I", v.brand, "Brand");
        assertEquals(2017, v.year, "Year of Construction");
    }

    @Test
    public void testChannelUID() {
        ThingTypeUID thingTypePHEV = new ThingTypeUID("mybmw", "plugin-hybrid-vehicle");
        assertEquals("plugin-hybrid-vehicle", thingTypePHEV.getId(), "Vehicle Type");
    }
}
