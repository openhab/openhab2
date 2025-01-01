/**
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
package org.openhab.binding.boschshc.internal.services.illuminance;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.boschshc.internal.services.BoschSHCService;
import org.openhab.binding.boschshc.internal.services.illuminance.dto.IlluminanceServiceState;

/**
 * Service for the illuminance state of the motion detector sensor.
 * 
 * @author David Pace - Initial contribution
 *
 */
@NonNullByDefault
public class IlluminanceService extends BoschSHCService<IlluminanceServiceState> {

    public IlluminanceService() {
        super("MultiLevelSensor", IlluminanceServiceState.class);
    }
}
