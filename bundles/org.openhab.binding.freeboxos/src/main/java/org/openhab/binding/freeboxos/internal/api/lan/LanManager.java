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
package org.openhab.binding.freeboxos.internal.api.lan;

import static org.openhab.binding.freeboxos.internal.api.ApiConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.lan.LanResponses.ConfigResponse;
import org.openhab.binding.freeboxos.internal.api.lan.browser.LanBrowserManager;
import org.openhab.binding.freeboxos.internal.rest.ConfigurableRest;
import org.openhab.binding.freeboxos.internal.rest.FreeboxOsSession;

/**
 * The {@link LanManager} is the Java class used to handle api requests related to lan
 *
 * https://dev.freebox.fr/sdk/os/system/#
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class LanManager extends ConfigurableRest<LanConfig, ConfigResponse> {

    public LanManager(FreeboxOsSession session) throws FreeboxException {
        super(session, ConfigResponse.class, LAN_SUB_PATH, CONFIG_SUB_PATH);
        session.addManager(LanBrowserManager.class, new LanBrowserManager(session, getUriBuilder()));
    }

}
