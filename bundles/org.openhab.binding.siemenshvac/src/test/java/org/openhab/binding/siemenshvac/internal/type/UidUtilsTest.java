/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.siemenshvac.internal.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Sönke Küper - Initial contribution
 */
@NonNullByDefault
@Disabled("These tests use the real website which may not always be available")
public class UidUtilsTest {

    @Test
    public void testSanetizeId() throws Exception {
        assertEquals(UidUtils.sanetizeId("Début heure été"), "Debut_heure_ete");
        assertEquals(UidUtils.sanetizeId("App.Ambiance 1"), "App_Ambiance_1");
        assertEquals(UidUtils.sanetizeId("Appareil d'ambiance P"), "Appareil_d_ambiance_P");
    }
}
