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
package org.openhab.binding.zm.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.binding.BaseDynamicStateDescriptionProvider;
import org.eclipse.smarthome.core.thing.i18n.ChannelTypeI18nLocalizationService;
import org.eclipse.smarthome.core.thing.type.DynamicStateDescriptionProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link ZmStateDescriptionOptionsProvider} is used to populate custom state options
 * on the Zoneminder channel(s).
 *
 * @author Mark Hilbush - Initial contribution
 */
@Component(service = { DynamicStateDescriptionProvider.class, ZmStateDescriptionOptionsProvider.class })
@NonNullByDefault
public class ZmStateDescriptionOptionsProvider extends BaseDynamicStateDescriptionProvider {

    @Activate
    public ZmStateDescriptionOptionsProvider(
            @Reference ChannelTypeI18nLocalizationService channelTypeI18nLocalizationService) {
        this.channelTypeI18nLocalizationService = channelTypeI18nLocalizationService;
    }
}
