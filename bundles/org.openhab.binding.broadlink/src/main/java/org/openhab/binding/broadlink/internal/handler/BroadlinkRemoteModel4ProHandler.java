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
package org.openhab.binding.broadlink.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.broadlink.internal.BroadlinkRemoteDynamicCommandDescriptionProvider;
import org.openhab.core.storage.StorageService;
import org.openhab.core.thing.Thing;

/**
 * Extension for the RM 4 Pro
 *
 * @author Anton Jansen - Initial contribution
 */

@NonNullByDefault
public class BroadlinkRemoteModel4ProHandler extends BroadlinkRemoteModel4MiniHandler {

    public BroadlinkRemoteModel4ProHandler(Thing thing,
            BroadlinkRemoteDynamicCommandDescriptionProvider commandDescriptionProvider,
            StorageService storageService) {
        super(thing, commandDescriptionProvider, storageService);
    }
}
