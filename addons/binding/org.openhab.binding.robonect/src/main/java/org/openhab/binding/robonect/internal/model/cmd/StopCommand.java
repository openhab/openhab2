/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.robonect.internal.model.cmd;

import org.openhab.binding.robonect.internal.RobonectClient;

/**
 * Stops the mower if it was started.
 * 
 * @author Marco Meyer - Initial contribution
 */
public class StopCommand implements Command {

    /**
     * {@inheritDoc}
     * @param baseURL - will be passed by the {@link RobonectClient} in the form 
     *                http://xxx.xxx.xxx/json?
     * @return
     */
    @Override
    public String toCommandURL(String baseURL) {
        return baseURL + "?cmd=stop";
    }
}
