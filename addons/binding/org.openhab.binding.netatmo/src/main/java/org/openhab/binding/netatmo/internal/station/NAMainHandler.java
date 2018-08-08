/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.station;

import io.rudolph.netatmo.api.aircare.model.DashboardData;
import io.rudolph.netatmo.api.common.model.Device;
import io.rudolph.netatmo.api.common.model.StationResults;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.netatmo.handler.BaseDeviceHandler;
import org.openhab.binding.netatmo.internal.WeatherUtils;

import static org.openhab.binding.netatmo.NetatmoBindingConstants.*;
import static org.openhab.binding.netatmo.internal.ChannelTypeUtils.*;

/**
 * {@link NAMainHandler} is the base class for all current Netatmo
 * weather station equipments (both modules and devices)
 *
 * @author Gaël L'hopital - Initial contribution OH2 version
 */
public class NAMainHandler extends BaseDeviceHandler {

    public NAMainHandler(@NonNull Thing thing) {
        super(thing);
    }

    @Override
    protected Device updateReadings() {
        Device result = null;
        StationResults stationDataBody = getBridgeHandler().api
                .getWeatherApi()
                .getStationData(getId(), false)
                .executeSync();
        if (stationDataBody != null) {
            result = stationDataBody.getDevices().stream().filter(device -> device.getId().equalsIgnoreCase(getId()))
                    .findFirst().orElse(null);
            if (result != null) {
                result.getModules().forEach(child -> childs.put(child.getId(), child));
            }
        }
        return result;
    }

    @Override
    protected void updateProperties(Device deviceData) {
        updateProperties(deviceData.getFirmware(), deviceData.getType().getValue());
    }

    @Override
    protected State getNAThingProperty(String channelId) {
        if (device != null) {
            DashboardData dashboardData = device.getDashboardData();
            switch (channelId) {
                case CHANNEL_CO2:
                    return toQuantityType(dashboardData.getCO2(), API_CO2_UNIT);
                case CHANNEL_TEMPERATURE:
                    return toQuantityType(dashboardData.getTemperature(), API_TEMPERATURE_UNIT);
                case CHANNEL_MIN_TEMP:
                    return toQuantityType(dashboardData.getMinTemp(), API_TEMPERATURE_UNIT);
                case CHANNEL_MAX_TEMP:
                    return toQuantityType(dashboardData.getMaxTemp(), API_TEMPERATURE_UNIT);
                case CHANNEL_TEMP_TREND:
                    return toStringType(dashboardData.getTempTrend());
                case CHANNEL_NOISE:
                    return toQuantityType(dashboardData.getNoise(), API_NOISE_UNIT);
                case CHANNEL_PRESSURE:
                    return toQuantityType(dashboardData.getPressure(), API_PRESSURE_UNIT);
                case CHANNEL_PRESS_TREND:
                    return toStringType(dashboardData.getPressureTrend());
                case CHANNEL_ABSOLUTE_PRESSURE:
                    return toQuantityType(dashboardData.getAbsolutePressure(), API_PRESSURE_UNIT);
                case CHANNEL_TIMEUTC:
                    return toDateTimeType(dashboardData.getTimeUtc());
                case CHANNEL_DATE_MIN_TEMP:
                    return toDateTimeType(dashboardData.getDateMinTemp());
                case CHANNEL_DATE_MAX_TEMP:
                    return toDateTimeType(dashboardData.getDateMaxTemp());
                case CHANNEL_HUMIDITY:
                    return toQuantityType(dashboardData.getHumidity(), API_HUMIDITY_UNIT);
                case CHANNEL_HUMIDEX:
                    return toDecimalType(
                            WeatherUtils.getHumidex(dashboardData.getTemperature(), dashboardData.getHumidity()));
                case CHANNEL_HEATINDEX:
                    return toQuantityType(
                            WeatherUtils.getHeatIndex(dashboardData.getTemperature(), dashboardData.getHumidity()),
                            API_TEMPERATURE_UNIT);
                case CHANNEL_DEWPOINT:
                    return toQuantityType(
                            WeatherUtils.getDewPoint(dashboardData.getTemperature(), dashboardData.getHumidity()),
                            API_TEMPERATURE_UNIT);
                case CHANNEL_DEWPOINTDEP:
                    Double dewPoint = WeatherUtils.getDewPoint(dashboardData.getTemperature(),
                            dashboardData.getHumidity());
                    return toQuantityType(WeatherUtils.getDewPointDep(dashboardData.getTemperature(), dewPoint),
                            API_TEMPERATURE_UNIT);
            }
        }
        return super.getNAThingProperty(channelId);
    }
}
