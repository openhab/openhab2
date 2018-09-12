/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpstracker.internal.profile;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.profiles.ProfileCallback;
import org.eclipse.smarthome.core.thing.profiles.ProfileContext;
import org.eclipse.smarthome.core.thing.profiles.ProfileTypeUID;
import org.eclipse.smarthome.core.thing.profiles.TriggerProfile;
import org.eclipse.smarthome.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openhab.binding.gpstracker.internal.GPSTrackerBindingConstants.CONFIG_REGION_NAME;

/**
 * The {@link GPSTrackerTriggerSwitchProfile} class implements the behavior when being linked to a Switch item.
 *
 * @author Gabor Bicskei - Initial contribution
 */
@NonNullByDefault
public class GPSTrackerTriggerSwitchProfile implements TriggerProfile {
    /**
     * Class logger
     */
    private final Logger logger = LoggerFactory.getLogger(GPSTrackerTriggerSwitchProfile.class);

    /**
     * Callback
     */
    private ProfileCallback callback;

    /**
     * Link region name
     */
    private String regionName;

    /**
     * Constructor.
     *
     * @param callback Callback
     * @param context Context
     */
    GPSTrackerTriggerSwitchProfile(ProfileCallback callback, ProfileContext context) {
        this.callback = callback;
        this.regionName = (String) context.getConfiguration().get(CONFIG_REGION_NAME);
        logger.debug("Trigger switch profile created for region {}", regionName);
    }

    @Override
    public ProfileTypeUID getProfileTypeUID() {
        return GPSTrackerProfileFactory.UID_TRIGGER_SWITCH;
    }

    @Override
    public void onStateUpdateFromItem(State state) {
    }

    @Override
    public void onTriggerFromHandler(String payload) {
        if (payload.startsWith(regionName)) {
            callback.sendCommand(payload.endsWith("enter") ? OnOffType.ON : OnOffType.OFF);
            logger.debug("Transition trigger {} handled for region {} by profile.", payload, regionName);
        }
    }
}
