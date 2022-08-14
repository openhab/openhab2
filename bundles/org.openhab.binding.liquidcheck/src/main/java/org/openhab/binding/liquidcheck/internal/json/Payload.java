/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.liquidcheck.internal.json;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * 
 * The {@link Payload} contains the all data from the payload.
 *
 * @author Marcel Goerentz - Initial contribution
 */
@NonNullByDefault
public class Payload {
    public Measure measure = new Measure();
    public Expansion expansion = new Expansion();
    public Device device = new Device();
    public System system = new System();
    public Wifi wifi = new Wifi();
}
