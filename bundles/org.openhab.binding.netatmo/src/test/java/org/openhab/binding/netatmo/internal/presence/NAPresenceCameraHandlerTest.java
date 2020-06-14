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
package org.openhab.binding.netatmo.internal.presence;

import io.swagger.client.model.NAWelcomeCamera;
import org.eclipse.smarthome.core.internal.i18n.I18nProviderImpl;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.internal.ThingImpl;
import org.eclipse.smarthome.core.types.UnDefType;
import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.netatmo.internal.NetatmoBindingConstants;

import static org.junit.Assert.assertEquals;

/**
 * @author Sven Strohschein
 */
public class NAPresenceCameraHandlerTest {

    private Thing presenceCameraThing;
    private NAWelcomeCamera presenceCamera;
    private NAPresenceCameraHandler handler;

    @Before
    public void before() {
        presenceCameraThing = new ThingImpl(new ThingTypeUID("netatmo", "NOC"), "1");
        presenceCamera = new NAWelcomeCamera();

        handler = new NAPresenceCameraHandler(presenceCameraThing, new I18nProviderImpl()) {
            {
                module = presenceCamera;
            }
        };
    }

    @Test
    public void testGetNAThingProperty_Common_Channel() {
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_STATUS));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_On() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.ON);
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_Off() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.OFF);
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_Auto() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.AUTO);
        //When the floodlight is set to auto-mode it is currently off.
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_without_LightModeState() {
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_Module_NULL() {
        NAPresenceCameraHandler handlerWithoutModule = new NAPresenceCameraHandler(presenceCameraThing, new I18nProviderImpl());
        assertEquals(UnDefType.UNDEF, handlerWithoutModule.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_FloodlightAutoMode_Floodlight_Auto() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.AUTO);
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
    }

    @Test
    public void testGetNAThingProperty_FloodlightAutoMode_Floodlight_On() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.ON);
        //When the floodlight is initially on (on starting the binding), there is no information about if the auto-mode
        // was set before. Therefore the auto-mode is detected as deactivated / off.
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
    }

    @Test
    public void testGetNAThingProperty_FloodlightAutoMode_Floodlight_Off() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.ON);
        //When the floodlight is initially off (on starting the binding), the auto-mode isn't set.
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_Scenario_with_AutoMode() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.AUTO);
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));

        //The auto-mode was initially set, after that the floodlight was switched on by the user.
        // In this case the binding should still know that the auto-mode is/was set.
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.ON);
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));

        //After that the user switched off the floodlight.
        // In this case the binding should still know that the auto-mode is/was set.
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.OFF);
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_Floodlight_Scenario_without_AutoMode() {
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.OFF);
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));

        //The auto-mode wasn't set, after that the floodlight was switched on by the user.
        // In this case the binding should still know that the auto-mode isn't/wasn't set.
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.ON);
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.ON, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));

        //After that the user switched off the floodlight.
        // In this case the binding should still know that the auto-mode isn't/wasn't set.
        presenceCamera.setLightModeStatus(NAWelcomeCamera.LightModeStatusEnum.OFF);
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
        assertEquals(OnOffType.OFF, handler.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT));
    }

    @Test
    public void testGetNAThingProperty_FloodlightAutoMode_Module_NULL() {
        NAPresenceCameraHandler handlerWithoutModule = new NAPresenceCameraHandler(presenceCameraThing, new I18nProviderImpl());
        assertEquals(UnDefType.UNDEF, handlerWithoutModule.getNAThingProperty(NetatmoBindingConstants.CHANNEL_CAMERA_FLOODLIGHT_AUTO_MODE));
    }
}
