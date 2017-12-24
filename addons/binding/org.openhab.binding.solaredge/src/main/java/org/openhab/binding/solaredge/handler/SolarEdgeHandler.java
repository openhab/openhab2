/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.solaredge.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.solaredge.config.SolarEdgeConfiguration;
import org.openhab.binding.solaredge.internal.connector.WebInterface;
import org.openhab.binding.solaredge.internal.model.Channel;

/**
 * public interface of the {@link SolarEdgeHandler}
 *
 * @author Alexander Friese - initial contribution
 *
 */
public interface SolarEdgeHandler extends ThingHandler {
    /**
     * Called from {@link WebInterface#authenticate()} to update
     * the thing status because updateStatus is protected.
     *
     * @param status Bridge status
     * @param statusDetail Bridge status detail
     * @param description Bridge status description
     */
    void setStatusInfo(@NonNull ThingStatus status, @NonNull ThingStatusDetail statusDetail, String description);

    /**
     * Provides the web interface object.
     *
     * @return The web interface object
     */
    WebInterface getWebInterface();

    void updateChannelStatus(Map<String, String> values);

    SolarEdgeConfiguration getConfiguration();

    List<Channel> getChannels();
}
