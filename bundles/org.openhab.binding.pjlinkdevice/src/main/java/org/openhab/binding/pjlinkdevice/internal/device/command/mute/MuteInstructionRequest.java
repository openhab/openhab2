/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.pjlinkdevice.internal.device.command.mute;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.pjlinkdevice.internal.device.command.Request;

/**
 * The request part of {@link MuteInstructionCommand}
 *
 * @author Nils Schnabel - Initial contribution
 */
@NonNullByDefault
public class MuteInstructionRequest implements Request {

    private MuteInstructionCommand command;

    public MuteInstructionRequest(MuteInstructionCommand command) {
        this.command = command;
    }

    @Override
    public String getRequestString() {
        return "%1AVMT " + this.command.getTargetChannel().getPJLinkRepresentation()
                + this.command.getTargetState().getPJLinkRepresentation();
    }
}
