/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal.protocol;

import java.io.IOException;

/**
 * To offer a method to retrieve a specific state, a protocol part would extend this interface.
 *
 * @author David Graeff - Initial contribution
 *
 */
public interface IStateUpdatable {
    /**
     * Updates the corresponding state. This method is blocking.
     *
     * @throws IOException If the device is offline this exception will be thrown
     * @throws ReceivedMessageParseException If the response cannot be parsed correctly this exception is thrown
     */
    void update() throws IOException, ReceivedMessageParseException;
}
