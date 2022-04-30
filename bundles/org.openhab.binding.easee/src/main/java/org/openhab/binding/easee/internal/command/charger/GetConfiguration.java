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
package org.openhab.binding.easee.internal.command.charger;

import static org.openhab.binding.easee.internal.EaseeBindingConstants.*;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.easee.internal.command.AbstractCommand;
import org.openhab.binding.easee.internal.command.EaseeCommand;
import org.openhab.binding.easee.internal.handler.EaseeHandler;

/**
 * implements the state api call of the charger.
 *
 * @author Alexander Friese - initial contribution
 */
@NonNullByDefault
public class GetConfiguration extends AbstractCommand implements EaseeCommand {

    public GetConfiguration(EaseeHandler handler) {
        // retry does not make much sense as it is a polling command, command should always succeed therefore update
        // handler on failure.
        super(handler, false, true);
    }

    @Override
    protected @NonNull Request prepareRequest(@NonNull Request requestToPrepare) {
        requestToPrepare.method(HttpMethod.GET);
        return requestToPrepare;
    }

    @Override
    protected @NonNull String getURL() {
        String url = GET_CONFIGURATION_URL;
        url = url.replaceAll("\\{id\\}", handler.getConfiguration().getWallboxId());
        return url;
    }

    @Override
    protected String getChannelGroup() {
        return CHANNEL_GROUP_CHARGER_CONFIG;
    }
}
