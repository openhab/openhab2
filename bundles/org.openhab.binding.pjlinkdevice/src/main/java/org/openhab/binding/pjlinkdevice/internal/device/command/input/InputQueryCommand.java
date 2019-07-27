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
package org.openhab.binding.pjlinkdevice.internal.device.command.input;

import org.openhab.binding.pjlinkdevice.internal.device.PJLinkDevice;
import org.openhab.binding.pjlinkdevice.internal.device.command.AbstractCommand;
import org.openhab.binding.pjlinkdevice.internal.device.command.ResponseException;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * This command is used for retrieving the currently selected input of the device as described in [PJLinkSpec] chapter 4.4. Input switch query
 *
 * @author Nils Schnabel - Initial contribution
 */
@NonNullByDefault
public class InputQueryCommand extends AbstractCommand<InputQueryRequest, InputQueryResponse> {

    public InputQueryCommand(PJLinkDevice pjLinkDevice) {
        super(pjLinkDevice);
    }

    @Override
    public InputQueryRequest createRequest() {
        return new InputQueryRequest();
    }

    @Override
    public InputQueryResponse parseResponse(String response) throws ResponseException {
        InputQueryResponse result = new InputQueryResponse(response);
        return result;
    }
}
