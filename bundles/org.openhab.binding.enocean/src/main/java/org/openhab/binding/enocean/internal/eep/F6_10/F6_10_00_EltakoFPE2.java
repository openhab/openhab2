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
package org.openhab.binding.enocean.internal.eep.F6_10;

import static org.openhab.binding.enocean.internal.EnOceanBindingConstants.*;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.enocean.internal.eep.Base._RPSMessage;
import org.openhab.binding.enocean.internal.messages.ERP1Message;

/**
 *
 * @author  Holger Englert - Initial contribution
 */
public class F6_10_00_EltakoFPE2 extends _RPSMessage {

    final byte OPEN = 0x10;
    final byte CLOSED = 0x00;

    public F6_10_00_EltakoFPE2() {
        super();
    }

    public F6_10_00_EltakoFPE2(ERP1Message packet) {
        super(packet);
    }

    @Override
    protected State convertToStateImpl(String channelId, String channelTypeId, State currentState, Configuration config) {

        if (channelId.equals(CHANNEL_CONTACT)) {
            return bytes[0] == CLOSED ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
        }

        return UnDefType.UNDEF;
    }

    @Override
    protected boolean validateData(byte[] bytes) {
        return super.validateData(bytes);
    }

}
