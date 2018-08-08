/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.station;

import io.rudolph.netatmo.api.aircare.model.DashboardData;
import io.rudolph.netatmo.api.common.model.ClimateModule;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.netatmo.handler.NetatmoModuleHandler;

import static org.openhab.binding.netatmo.NetatmoBindingConstants.*;
import static org.openhab.binding.netatmo.internal.ChannelTypeUtils.toDateTimeType;
import static org.openhab.binding.netatmo.internal.ChannelTypeUtils.toQuantityType;

/**
 * {@link NAModule2Handler} is the class used to handle the wind module
 * capable of reporting wind angle and strength
 *
 * @author Gaël L'hopital - Initial contribution OH2 version
 *
 */
public class NAModule2Handler extends NetatmoModuleHandler<ClimateModule> {

    public NAModule2Handler(@NonNull Thing thing) {
        super(thing);
    }

    @Override
    protected void updateProperties(ClimateModule moduleData) {
        updateProperties(moduleData.getFirmware(), moduleData.getType().getValue());
    }

    @Override
    protected State getNAThingProperty(String channelId) {
        if (module != null) {
            DashboardData dashboardData = module.getDashboardData();
            switch (channelId) {
                case CHANNEL_WIND_ANGLE:
                    return toQuantityType(dashboardData.getWindAngle(), API_WIND_DIRECTION_UNIT);
                case CHANNEL_WIND_STRENGTH:
                    return toQuantityType(dashboardData.getWindStrength(), API_WIND_SPEED_UNIT);
                case CHANNEL_GUST_ANGLE:
                    return toQuantityType(dashboardData.getGustAngle(), API_WIND_DIRECTION_UNIT);
                case CHANNEL_GUST_STRENGTH:
                    return toQuantityType(dashboardData.getGustStrength(), API_WIND_SPEED_UNIT);
                case CHANNEL_TIMEUTC:
                    return toDateTimeType(dashboardData.getTimeUtc());
                case CHANNEL_MAX_WIND_STRENGTH:
                    return toQuantityType(dashboardData.getMaxWindStr(), API_WIND_SPEED_UNIT);
                case CHANNEL_DATE_MAX_WIND_STRENGTH:
                    return toDateTimeType(dashboardData.getDateMaxWindStr());
            }
        }
        return super.getNAThingProperty(channelId);
    }

}
