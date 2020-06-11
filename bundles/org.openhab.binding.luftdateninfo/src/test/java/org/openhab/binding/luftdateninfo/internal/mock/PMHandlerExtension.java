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
package org.openhab.binding.luftdateninfo.internal.mock;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.luftdateninfo.internal.handler.PMHandler;

/**
 * The {@link PMHandlerExtension} Test Particualte Matter Handler Extension with additonal state queries
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class PMHandlerExtension extends PMHandler {

    public PMHandlerExtension(Thing thing) {
        super(thing);
        // TODO Auto-generated constructor stub
    }

    public int getConfigStatus() {
        return configStatus;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public @Nullable State getPM25Cache() {
        return pm25Cache;
    }

    public @Nullable State getPM100Cache() {
        return pm100Cache;
    }
}
