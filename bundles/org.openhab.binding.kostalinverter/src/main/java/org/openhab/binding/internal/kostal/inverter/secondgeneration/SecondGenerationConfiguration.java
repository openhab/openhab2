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

package org.openhab.binding.internal.kostal.inverter.secondgeneration;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link SecondGenerationConfiguration} class contains fields mapping thing configuration parameters,
 * used in the second generation part of the binding.
 *
 * @author Örjan Backsell - Initial contribution Piko1020, Piko New Generation
 *
 */
@NonNullByDefault
public class SecondGenerationConfiguration {

    public static final long REFRESHINTERVAL = 60;

    public Object url = "";
    public String username = "";
    public String password = "";
    public String dxsIdConf = "";
    public String valueConf = "";
}
