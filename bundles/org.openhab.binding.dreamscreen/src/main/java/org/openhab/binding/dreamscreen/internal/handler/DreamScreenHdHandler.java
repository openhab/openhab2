/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.dreamscreen.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.dreamscreen.internal.DreamScreenServer;
import org.openhab.binding.dreamscreen.internal.message.RefreshTvMessage;

/**
 * The {@link DreamScreenHdHandler} is the Thing Handler for the DreamScreen HD device.
 *
 * @author Bruce Brouwer - Initial contribution
 */

@NonNullByDefault
public class DreamScreenHdHandler extends DreamScreenBaseTvHandler {
    public final static byte PRODUCT_ID = 0x01;

    public DreamScreenHdHandler(DreamScreenServer server, Thing thing,
            DreamScreenInputDescriptionProvider descriptionProvider) {
        super(server, thing, descriptionProvider);
    }

    @Override
    protected boolean refreshTvMsg(final RefreshTvMessage msg) {
        if (msg.getProductId() == PRODUCT_ID) {
            return super.refreshTvMsg(msg);
        }
        return false;
    }
}
