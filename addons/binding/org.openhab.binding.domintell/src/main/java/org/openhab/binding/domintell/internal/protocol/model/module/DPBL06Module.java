/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.domintell.internal.protocol.model.module;

import org.openhab.binding.domintell.internal.protocol.DomintellConnection;
import org.openhab.binding.domintell.internal.protocol.model.SerialNumber;
import org.openhab.binding.domintell.internal.protocol.model.type.ModuleType;

/**
 * The {@link DPBL06Module} class is handling physical push button module DPBL06
 *
 * @author Gabor Bicskei - Initial contribution
 */
public class DPBL06Module extends PushButtonModule {
    /**
     * Constructor
     *
     * @param connection Connection
     * @param serialNumber Module serial number
     */
    public DPBL06Module(DomintellConnection connection, SerialNumber serialNumber) {
        super(connection, ModuleType.B86, serialNumber, 6);
    }
}
