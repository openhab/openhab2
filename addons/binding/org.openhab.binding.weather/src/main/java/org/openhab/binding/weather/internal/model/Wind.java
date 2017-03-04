/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Common provider model for wind data.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Wind {

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.wind_kph"),
            @Provider(name = ProviderName.Wunderground, property = "avewind.kph"),
            @Provider(name = ProviderName.OpenWeatherMap, property = "speed"),
            @Provider(name = ProviderName.ForecastIO, property = "windSpeed", converter = ConverterType.WIND_MPS),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "windspeedKmph"),
            @Provider(name = ProviderName.Yahoo, property = "wind.speed"),
            @Provider(name = ProviderName.HamWeather, property = "windSpeedKPH"),
            @Provider(name = ProviderName.MeteoBlue, property = "wind_speed"),
            @Provider(name = ProviderName.MeteoBlue, property = "wind_speed_max") })
    private Double speed;

    @ProviderMappings({
            @Provider(name = ProviderName.WorldWeatherOnline, property = "winddir16Point"),
            @Provider(name = ProviderName.MeteoBlue, property = "wind_direction_dominant") })
    private String direction;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.wind_degrees"),
            @Provider(name = ProviderName.Wunderground, property = "avewind.degrees"),
            @Provider(name = ProviderName.OpenWeatherMap, property = "deg"),
            @Provider(name = ProviderName.ForecastIO, property = "windBearing"),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "winddirDegree"),
            @Provider(name = ProviderName.Yahoo, property = "wind.direction"),
            @Provider(name = ProviderName.HamWeather, property = "windDirDEG") })
    private Integer degree;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.wind_gust_kph"),
            @Provider(name = ProviderName.Wunderground, property = "wind.gust"),
            @Provider(name = ProviderName.HamWeather, property = "windGustKPH"),
            @Provider(name = ProviderName.MeteoBlue, property = "wind_gust_max") })
    private Double gust;

    @ProviderMappings({
            @Provider(name = ProviderName.Yahoo, property = "wind.chill"),
            @Provider(name = ProviderName.Wunderground, property = "windchill_c"),
            @Provider(name = ProviderName.HamWeather, property = "windchillC") })
    private Double chill;

    /**
     * Returns the windspeed in kilometer per hour.
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * Sets the windspeed in kilometer per hour.
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * Returns the wind direction.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the wind direction.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Returns the wind degree.
     */
    public Integer getDegree() {
        return degree;
    }

    /**
     * Returns the wind degree.
     */
    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    /**
     * Returns the wind gust.
     */
    public Double getGust() {
        return gust;
    }

    /**
     * Sets the wind gust.
     */
    public void setGust(Double gust) {
        this.gust = gust;
    }

    /**
     * Returns the wind chill in degrees.
     */
    public Double getChill() {
        return chill;
    }

    /**
     * Sets the wind chill in degrees.
     */
    public void setChill(Double chill) {
        this.chill = chill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("speed", speed)
                .append("direction", direction).append("degree", degree).append("gust", gust).append("chill", chill)
                .toString();
    }

}
