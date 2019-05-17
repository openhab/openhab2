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
package org.openhab.binding.nibeheatpump.internal.handler;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.Command;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openhab.binding.nibeheatpump.internal.models.PumpModel;
import org.openhab.binding.nibeheatpump.internal.models.VariableInformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Tests cases for {@link NibeHeatPumpHandler}.
 *
 * @author Jevgeni Kiski
 */
@RunWith(Parameterized.class)
public class NibeHeatPumpHandlerCommand2NibeTest {
    private NibeHeatPumpHandler product; // the class under test
    private Method m;
    private static String METHOD_NAME = "convertCommandToNibeValue";
    private Class[] parameterTypes;
    private Object[] parameters;

    private int fCoilAddress;

    private Command fCommand;

    private int fExpected;

    @Parameterized.Parameters(name = "{index}: f({0}, {1})={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 47028, new DecimalType("-1"), (byte)0xFF },
                { 43009, new DecimalType("28.7"), 0x011F },
                { 40004, new DecimalType("-0.1"), (short)0xFFFF },
                { 47418, new DecimalType("75"), 0x004B },
                { 43514, new DecimalType("7"), 0x0007 },
                { 47291, new DecimalType("65535"), 0xFFFF },
                { 43230, new DecimalType("429496729.5"), 0xFFFFFFFF },
                { 43614, new DecimalType("4294967295"), 0xFFFFFFFF },
                { 47041, new StringType("1"), 0x1 },
                { 47371, OnOffType.from(true), 0x1 },
                { 47371, OnOffType.from(false), 0x0 },
        });
    }

    public NibeHeatPumpHandlerCommand2NibeTest(final int coilAddress, final Command command, final int expected) {
        this.fCoilAddress = coilAddress;
        this.fCommand = command;
        this.fExpected = expected;
    }

    @Before
    public void setUp() throws Exception {
        product = new NibeHeatPumpHandler(null, PumpModel.F1X45);
        parameterTypes = new Class[2];
        parameterTypes[0] = VariableInformation.class;
        parameterTypes[1] = Command.class;
        m = product.getClass().getDeclaredMethod(METHOD_NAME, parameterTypes);
        m.setAccessible(true);
        parameters = new Object[2];
    }

    @Test
    public void convertNibeValueToStateTest() throws InvocationTargetException, IllegalAccessException {
        VariableInformation varInfo = VariableInformation.getVariableInfo(PumpModel.F1X45, fCoilAddress);
        parameters[0] = varInfo;
        parameters[1] = fCommand;
        int value = (int) m.invoke(product, parameters);

        assertEquals(fExpected, value);
    }
}
