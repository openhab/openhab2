/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.solarforecast.internal.solcast.config;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.solarforecast.internal.SolarForecastBindingConstants;

/**
 * The {@link SolcastPlaneConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class SolcastPlaneConfiguration {
    public String resourceId = SolarForecastBindingConstants.EMPTY;
    public long refreshInterval = 120;
    public boolean forecastOnly = false;

    @Override
    public String toString() {
        return "SolcastPlaneConfiguration [resourceId=" + resourceId + ", refreshInterval=" + refreshInterval
                + ", forecastOnly=" + forecastOnly + "]";
    }
}
