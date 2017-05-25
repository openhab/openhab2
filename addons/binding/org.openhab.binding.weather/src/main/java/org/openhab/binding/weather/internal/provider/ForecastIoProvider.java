/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.CommonIdHandler;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * ForecastIO weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ForecastIoProvider extends AbstractWeatherProvider {
    private static final String URL = "https://api.darksky.net/forecast/[API_KEY]/[LATITUDE],[LONGITUDE]?units=[UNITS]&lang=[LANGUAGE]&exclude=hourly,flags";

    public ForecastIoProvider(CommonIdHandler commonIdHandler) {
        super(new JsonWeatherParser(commonIdHandler));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.ForecastIO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
