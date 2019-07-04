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
package org.openhab.binding.snmp.internal.config;

import org.openhab.binding.snmp.internal.SnmpChannelMode;
import org.openhab.binding.snmp.internal.SnmpDatatype;

/**
 * The {@link SnmpChannelConfiguration} class contains fields mapping channel configuration parameters.
 *
 * @author Jan N. Klug - Initial contribution
 */
public class SnmpChannelConfiguration {
    public String oid;
    public SnmpChannelMode mode = SnmpChannelMode.READ;
    public SnmpDatatype datatype;

    public String onvalue;
    public String offvalue;
}
