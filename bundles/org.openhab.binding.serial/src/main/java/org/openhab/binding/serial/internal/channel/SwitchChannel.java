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
package org.openhab.binding.serial.internal.channel;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;

/**
 * The {@link SwitchChannel} channel provides mappings for the ON and OFF commands
 *
 * @author Mike Major - Initial contribution
 */
@NonNullByDefault
public class SwitchChannel extends DeviceChannel {

    public SwitchChannel(final BundleContext bundleContext, final ChannelConfig config) {
        super(bundleContext, config);
    }

    @Override
    public @Nullable String mapCommand(final Command command) {
        String data = null;

        if (config.on != null && OnOffType.ON.equals(command)) {
            data = config.on;
        } else if (config.off != null && OnOffType.OFF.equals(command)) {
            data = config.off;
        }

        logger.debug("Mapped command is '{}'", data);

        return data;
    }
}
