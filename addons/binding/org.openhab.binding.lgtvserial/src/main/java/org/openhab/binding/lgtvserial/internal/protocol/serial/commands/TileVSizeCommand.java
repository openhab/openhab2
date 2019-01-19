/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtvserial.internal.protocol.serial.commands;

/**
 * This class handles the tile-v-size D/H command.
 * 
 * @author Richard Lavoie - Initial contribution
 *
 */
public class TileVSizeCommand extends BasePercentCommand {

    protected TileVSizeCommand(int setId) {
        super('d', 'h', setId);
    }

}
