/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikohomecontrol.internal.protocol;

/**
 * Class {@link NHCBaseMessage} used as base class for output from gson for cmd or event feedback from Niko Home
 * Control. This class only contains the common base fields required for the deserializer {@link NikoHomeControlMessageDeserializer}
 * to select the specific formats implemented in {@link NHCMessageMap}, {@link NHCMessageListMap}, {@link NHCCmd}.
 * <p>
 *
 * @author Mark Herwege
 */
public class NHCBaseMessage {

    private String cmd;
    private String event;

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
