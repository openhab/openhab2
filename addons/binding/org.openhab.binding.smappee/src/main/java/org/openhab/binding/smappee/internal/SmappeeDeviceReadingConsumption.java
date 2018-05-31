/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smappee.internal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The result of a smappee reading, the energy consumption
 *
 * @author Niko Tanghe - Initial contribution
 */
public class SmappeeDeviceReadingConsumption {

    public long timestamp;
    public double consumption;
    public double solar;
    public double alwaysOn;

    public LocalDateTime timestampDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
    }
}
