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
package org.openhab.binding.pjlinkdevice.internal.device.command.identification;

import org.openhab.binding.pjlinkdevice.internal.device.command.PrefixedResponse;
import org.openhab.binding.pjlinkdevice.internal.device.command.ResponseException;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * @author Nils Schnabel - Initial contribution
 */
@NonNullByDefault
public class IdentificationResponse extends PrefixedResponse<String> {
    public IdentificationResponse(IdentificationCommand command, String response) throws ResponseException {
        super(command.identificationProperty.getPJLinkCommandPrefix() + "=", response);
    }

    @Override
    protected String parse0(String responseWithoutPrefix) throws ResponseException {
        return responseWithoutPrefix;
    }

}
