/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.command;

/**
 * Command class for command that clear troubles memory.
 *
 * @author Krzysztof Goworek - Initial contribution
 * @since 1.7.0
 */
public class ClearTroublesCommand extends ControlCommand {

    public static final byte COMMAND_CODE = (byte) 0x8b;

    /**
     * Creates new command class instance.
     *
     * @param userCode
     *            code of the user on behalf the control is made
     */
    public ClearTroublesCommand(String userCode) {
        super(COMMAND_CODE, userCodeToBytes(userCode));
    }

}
