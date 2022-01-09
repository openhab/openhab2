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
package org.openhab.binding.bmwconnecteddrive.internal.dto.status;

import org.openhab.binding.bmwconnecteddrive.internal.utils.Constants;

/**
 * The {@link Doors} Data Transfer Object
 *
 * @author Bernd Weymann - Initial contribution
 */
public class Doors {
    public String doorDriverFront = Constants.UNDEF;// ": "CLOSED",
    public String doorDriverRear = Constants.UNDEF;// ": "CLOSED",
    public String doorPassengerFront = Constants.UNDEF;// ": "CLOSED",
    public String doorPassengerRear = Constants.UNDEF;// ": "CLOSED",
    public String trunk = Constants.UNDEF;// ": "CLOSED",
    public String hood = Constants.UNDEF;// ": "CLOSED",
}
