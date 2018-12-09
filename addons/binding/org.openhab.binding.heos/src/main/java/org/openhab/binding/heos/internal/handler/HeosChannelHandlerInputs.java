/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heos.internal.handler;

import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.heos.handler.HeosBridgeHandler;
import org.openhab.binding.heos.internal.api.HeosFacade;

/**
 * The {@link HeosChannelHandlerInputs} handles the Input channel command
 * from the implementing thing.
 *
 * @author Johannes Einig - Initial contribution
 *
 */
public class HeosChannelHandlerInputs extends HeosChannelHandler {

    public HeosChannelHandlerInputs(HeosBridgeHandler bridge, HeosFacade api) {
        super(bridge, api);
    }

    @Override
    protected void handleCommandPlayer() {
        handleCommand();
    }

    @Override
    protected void handleCommandGroup() {
        handleCommand();
    }

    @Override
    protected void handleCommandBridge() {
        // not used on bridge
    }

    private void handleCommand() {
        if (command instanceof RefreshType) {
            api.getNowPlayingMedia(id);
            return;
        }
        if (bridge.getSelectedPlayer().isEmpty()) {
            api.playInputSource(id, command.toString());
        } else if (bridge.getSelectedPlayer().size() > 1) {
            logger.debug("Only one source can be selected for HEOS Input. Selected amount of sources: {} ",
                    bridge.getSelectedPlayer().size());
        } else {
            for (String sourcePid : bridge.getSelectedPlayer().keySet()) {
                api.playInputSource(id, sourcePid, command.toString());
            }
        }
        bridge.getSelectedPlayer().clear();
    }
}
