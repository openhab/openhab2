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
 * Common provider model for atmosphere data.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Atmosphere {

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.relative_humidity", converter = ConverterType.PERCENT_INTEGER),
            @Provider(name = ProviderName.Wunderground, property = "avehumidity"),
            @Provider(name = ProviderName.OpenWeatherMap, property = "humidity"),
            @Provider(name = ProviderName.ForecastIO, property = "humidity", converter = ConverterType.FRACTION_INTEGER),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "humidity"),
            @Provider(name = ProviderName.Yahoo, property = "atmosphere.humidity"),
            @Provider(name = ProviderName.HamWeather, property = "humidity"),
            @Provider(name = ProviderName.MeteoBlue, property = "relative_humidity_avg") })
    private Integer humidity;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.visibility_km"),
            @Provider(name = ProviderName.ForecastIO, property = "visibility"),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "visibility"),
            @Provider(name = ProviderName.Yahoo, property = "atmosphere.visibility"),
            @Provider(name = ProviderName.HamWeather, property = "visibilityKM") })
    private Double visibility;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.pressure_mb"),
            @Provider(name = ProviderName.OpenWeatherMap, property = "pressure"),
            @Provider(name = ProviderName.ForecastIO, property = "pressure"),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "pressure"),
            @Provider(name = ProviderName.Yahoo, property = "atmosphere.pressure"),
            @Provider(name = ProviderName.HamWeather, property = "pressureMB"),
            @Provider(name = ProviderName.MeteoBlue, property = "pressure_hpa") })
    private Double pressure;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.pressure_trend", converter = ConverterType.PRESSURE_TREND) })
    private String pressureTrend;

    @ProviderMappings({
            @Provider(name = ProviderName.ForecastIO, property = "ozone") })
    private Integer ozone;

    @ProviderMappings({
            @Provider(name = ProviderName.Wunderground, property = "current_observation.UV"),
            @Provider(name = ProviderName.WorldWeatherOnline, property = "uvIndex"),
            @Provider(name = ProviderName.MeteoBlue, property = "uv_index") })
    private Integer uvIndex;

    /**
     * Returns the humidity in percent.
     */
    public Integer getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity.
     */
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    /**
     * Returns the visibility in kilometers.
     */
    public Double getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility in kilometers.
     */
    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    /**
     * Returns the pressure in millibar.
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * Sets the pressure in millibar.
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * Returns the pressure trend (up, down, equal).
     */
    public String getPressureTrend() {
        return pressureTrend;
    }

    /**
     * Sets the pressure trend.
     */
    public void setPressureTrend(String pressureTrend) {
        this.pressureTrend = pressureTrend;
    }

    /**
     * Returns the ozone in ppm.
     */
    public Integer getOzone() {
        return ozone;
    }

    /**
     * Sets the ozone.
     */
    public void setOzone(Integer ozone) {
        this.ozone = ozone;
    }

    /**
     * Returns the uv Index.
     */
    public Integer getUvIndex() {
        return uvIndex;
    }

    /**
     * Sets the uvIndex.
     */
    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("humidity", humidity)
                .append("visibility", visibility).append("pressure", pressure).append("pressureTrend", pressureTrend)
                .append("ozone", ozone).append("uvIndex", uvIndex).toString();
    }

}
