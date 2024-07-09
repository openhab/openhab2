/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.siemenshvac.internal.type;

import java.net.URI;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.config.core.ConfigDescription;
import org.openhab.core.config.core.ConfigDescriptionProvider;

/**
 * Extends the ConfigDescriptionProvider to manually add a ConfigDescription.
 *
 * @author Laurent Arnal - Initial contribution
 */
@NonNullByDefault
public interface SiemensHvacConfigDescriptionProvider extends ConfigDescriptionProvider {

    /**
     * Adds the ConfigDescription to this provider.
     */
    void addConfigDescription(ConfigDescription configDescription);

    /**
     * Provides a {@link ConfigDescription} for the given URI.
     *
     * @param uri uri of the config description
     * @param locale locale
     *
     * @return config description or null if no config description could be found
     */
    @Override
    @Nullable
    ConfigDescription getConfigDescription(URI uri, @Nullable Locale locale);

    /**
     * Use this method to lookup a ConfigDescription which was generated by the
     * siemenshvac binding.
     *
     */
    @Nullable
    ConfigDescription getInternalConfigDescription(URI uri);

    void invalidate();
}
