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
package org.openhab.binding.netatmo.internal.camera;

import io.swagger.client.model.NAWelcomeCamera;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.netatmo.internal.handler.NetatmoModuleHandler;

import static org.openhab.binding.netatmo.internal.ChannelTypeUtils.toOnOffType;
import static org.openhab.binding.netatmo.internal.ChannelTypeUtils.toStringType;
import static org.openhab.binding.netatmo.internal.NetatmoBindingConstants.*;

/**
 * {@link NACameraHandler} is the class used to handle Camera Data
 *
 * @author Sven Strohschein (partly moved code from NAWelcomeCameraHandler to introduce inheritance, see NAWelcomeCameraHandler)
 *
 */
public class NACameraHandler extends NetatmoModuleHandler<NAWelcomeCamera> {

    private static final String LIVE_PICTURE = "/live/snapshot_720.jpg";

    public NACameraHandler(@NonNull Thing thing) {
        super(thing);
    }

    @Override
    protected void updateProperties(NAWelcomeCamera moduleData) {
        updateProperties(null, moduleData.getType());
    }

    @SuppressWarnings("null")
    @Override
    protected State getNAThingProperty(String channelId) {
        switch (channelId) {
            case CHANNEL_CAMERA_STATUS:
                return getStatusState();
            case CHANNEL_CAMERA_SDSTATUS:
                return getSdStatusState();
            case CHANNEL_CAMERA_ALIMSTATUS:
                return getAlimStatusState();
            case CHANNEL_CAMERA_ISLOCAL:
                return getIsLocalState();
            case CHANNEL_CAMERA_LIVEPICTURE_URL:
                return getLivePictureURLState();
            case CHANNEL_CAMERA_LIVEPICTURE:
                return getLivePictureState();
            case CHANNEL_CAMERA_LIVESTREAM_URL:
                return getLiveStreamState();
        }
        return super.getNAThingProperty(channelId);
    }

    protected State getStatusState() {
        return module != null ? toOnOffType(module.getStatus()) : UnDefType.UNDEF;
    }

    protected State getSdStatusState() {
        return module != null ? toOnOffType(module.getSdStatus()) : UnDefType.UNDEF;
    }

    protected State getAlimStatusState() {
        return module != null ? toOnOffType(module.getAlimStatus()) : UnDefType.UNDEF;
    }

    protected State getIsLocalState() {
        return (module == null || module.getIsLocal() == null) ? UnDefType.UNDEF
                : module.getIsLocal() ? OnOffType.ON : OnOffType.OFF;
    }

    protected State getLivePictureURLState() {
        return getLivePictureURL() == null ? UnDefType.UNDEF : toStringType(getLivePictureURL());
    }

    protected State getLivePictureState() {
        return getLivePictureURL() == null ? UnDefType.UNDEF : HttpUtil.downloadImage(getLivePictureURL());
    }

    protected State getLiveStreamState() {
        return getLiveStreamURL() == null ? UnDefType.UNDEF : new StringType(getLiveStreamURL());
    }

    /**
     * Get the url for the live snapshot
     *
     * @return Url of the live snapshot
     */
    private String getLivePictureURL() {
        String result = getVpnUrl();
        if (result != null) {
            result += LIVE_PICTURE;
        }
        return result;
    }

    /**
     * Get the url for the live stream depending wether local or not
     *
     * @return Url of the live stream
     */
    private String getLiveStreamURL() {
        String result = getVpnUrl();
        if (result != null) {
            result += "/live/index";
            result += isLocal() ? "_local" : "";
            result += ".m3u8";
        }
        return result;
    }

    @SuppressWarnings("null")
    private String getVpnUrl() {
        return (module == null) ? null : module.getVpnUrl();
    }

    public String getStreamURL(String videoId) {
        String result = getVpnUrl();
        if (result != null) {
            result += "/vod/" + videoId + "/index";
            result += isLocal() ? "_local" : "";
            result += ".m3u8";
        }
        return result;
    }

    @SuppressWarnings("null")
    private boolean isLocal() {
        return (module == null || module.getIsLocal() == null) ? false : module.getIsLocal().booleanValue();
    }
}