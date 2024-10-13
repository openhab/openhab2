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
package org.openhab.binding.metofficedatahub.internal;

import static org.openhab.binding.metofficedatahub.internal.MetOfficeDataHubBindingConstants.*;
import static org.openhab.binding.metofficedatahub.internal.MetOfficeDataHubBridgeHandler.getMillisSinceDayStart;
import static org.openhab.core.library.unit.MetricPrefix.MILLI;
import static org.openhab.core.library.unit.SIUnits.METRE;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.Unit;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.openhab.binding.metofficedatahub.internal.dto.responses.SiteApiFeatureCollection;
import org.openhab.binding.metofficedatahub.internal.dto.responses.SiteApiFeatureProperties;
import org.openhab.binding.metofficedatahub.internal.dto.responses.SiteApiTimeSeries;
import org.openhab.core.i18n.LocaleProvider;
import org.openhab.core.i18n.LocationProvider;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.i18n.TranslationProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MetOfficeDataHubSiteApiHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author David Goodyear - Initial contribution
 */
@NonNullByDefault
public class MetOfficeDataHubSiteApiHandler extends BaseThingHandler implements IForecastDataPollable {

    public static final String EXPECTED_TS_FORMAT = "YYYY-MM-dd HH:mm:ss.SSS";

    private final Logger logger = LoggerFactory.getLogger(MetOfficeDataHubSiteApiHandler.class);
    private final Object checkDataRequiredSchedulerLock = new Object();
    private final Object checkDailySchedulerLock = new Object();
    private final TranslationProvider translationProvider;
    private final LocaleProvider localeProvider;
    private final Bundle bundle;
    private final LocationProvider locationProvider;
    private final TimeZoneProvider timeZoneProvider;

    private HttpClient httpClient;
    private boolean requiresDailyData = false;
    private boolean requiresHourlyData = false;

    private volatile MetOfficeDataHubSiteApiConfiguration config = getConfigAs(
            MetOfficeDataHubSiteApiConfiguration.class);
    private volatile boolean authFailed = false;
    private volatile String lastDailyResponse = "";
    private volatile String lastHourlyResponse = "";
    private volatile long lastDailyForecastPoll = -1;
    private volatile long lastHourlyForecastPoll = -1;

    private PointType location = new PointType();
    private @Nullable ScheduledFuture<?> checkDataRequiredScheduler = null;
    private @Nullable ScheduledFuture<?> dailyScheduler = null;

    /**
     * This handles the scheduling of an hourly forecast poll, to be applied with the given delay.
     * When run, if requests the run-time of the next one is calculated and scheduled.
     */
    MetOfficeDelayedExecutor dailyForecastJob = new MetOfficeDelayedExecutor(scheduler);

    /**
     * This handles the scheduling of an hourly forecast poll, to be applied with the given delay.
     * When run, if requests the run-time of the next one is calculated and scheduled.
     */
    MetOfficeDelayedExecutor hourlyForecastJob = new MetOfficeDelayedExecutor(scheduler);

    public MetOfficeDataHubSiteApiHandler(Thing thing, IHttpClientProvider httpClientProvider,
            @Reference LocationProvider locationProvider, @Reference TranslationProvider translationProvider,
            @Reference LocaleProvider localeProvider, @Reference TimeZoneProvider timeZoneProvider) {
        super(thing);
        this.locationProvider = locationProvider;
        this.httpClient = httpClientProvider.getHttpClient();
        this.translationProvider = translationProvider;
        this.localeProvider = localeProvider;
        this.bundle = FrameworkUtil.getBundle(getClass());
        this.timeZoneProvider = timeZoneProvider;
    }

    @Override
    public void dispose() {
        dailyForecastJob.cancelScheduledTask(true);
        hourlyForecastJob.cancelScheduledTask(true);
        cancelDataRequiredCheck();
        cancelScheduleDailyDataPoll(true);
        super.dispose();
    }

    public String getLocalizedText(String key, @Nullable Object @Nullable... arguments) {
        String result = translationProvider.getText(bundle, key, key, localeProvider.getLocale(), arguments);
        return Objects.nonNull(result) ? result : key;
    }

    protected State getQuantityTypeState(@Nullable Number value, Unit<?> unit) {
        return (value == null) ? UnDefType.UNDEF : new QuantityType<>(value, unit);
    }

    protected State getDecimalTypeState(@Nullable Number value) {
        return (value == null) ? UnDefType.UNDEF : new DecimalType(value);
    }

    private void checkDataRequired() {
        final List<@Nullable String> activeGroups = getThing().getChannels().stream().filter(x -> isLinked(x.getUID()))
                .map(x -> x.getUID().getGroupId()).distinct().toList();

        if (activeGroups.stream().anyMatch(g -> g != null && g.startsWith(GROUP_PREFIX_DAILY_FORECAST))) {
            boolean repollRequired = !requiresDailyData;
            if (!requiresDailyData) {
                logger.trace("Daily data poll potentially required if older than required poll window");
            }

            requiresDailyData = true;
            if (repollRequired) {
                reconfigureDailyPolling();
            }
            logger.trace("Daily data poll required");
        } else {
            requiresDailyData = false;
        }

        if (activeGroups.stream().anyMatch(g -> g != null && g.startsWith(GROUP_PREFIX_HOURS_FORECAST))) {
            boolean repollRequired = !requiresHourlyData;
            if (!requiresHourlyData) {
                logger.trace("Hourly data poll potentially required if older than required poll window");
            }

            requiresHourlyData = true;
            if (repollRequired) {
                reconfigureHourlyPolling();
            }
            logger.trace("Hourly data poll required");
        } else {
            requiresHourlyData = false;
        }

        if (!requiresHourlyData && !requiresDailyData) {
            logger.debug("No data required");
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        super.channelLinked(channelUID);

        scheduleDataRequiredCheck();

        // can be overridden by subclasses
        // standard behavior is to refresh the linked channel,
        // so the newly linked items will receive a state update.
        handleCommand(channelUID, RefreshType.REFRESH);
    }

    @Override
    public void channelUnlinked(ChannelUID channelUID) {
        // can be overridden by subclasses
        scheduleDataRequiredCheck();
    }

    public static String getLastHour() {
        long timeRoundedToLastHour = System.currentTimeMillis();
        timeRoundedToLastHour -= timeRoundedToLastHour % 3600000;
        final Instant instant = new Date(timeRoundedToLastHour).toInstant();
        return instant.toString().substring(0, 16) + "Z";
    }

    public static String getStartOfDay() {
        long timeRoundedToDayStart = System.currentTimeMillis();
        timeRoundedToDayStart -= timeRoundedToDayStart % 86400000;
        final Instant instant = new Date(timeRoundedToDayStart).toInstant();
        return instant.toString().substring(0, 16) + "Z";
    }

    public void pollForDataHourlyData(String responseContent) {
        final SiteApiFeatureCollection response = GSON.fromJson(responseContent, SiteApiFeatureCollection.class);

        if (response == null) {
            return;
        }

        final SiteApiFeatureProperties props = response.getFirstProperties();
        if (props == null) {
            return;
        }

        final String startOfHour = MetOfficeDataHubSiteApiHandler.getLastHour();
        final int forecastForthisHour = props.getHourlyTimeSeriesPositionForCurrentHour(startOfHour);

        for (int hrOffset = 0; hrOffset <= 24; ++hrOffset) {
            // Calculate the correct array position for the data
            final int dataIdx = forecastForthisHour + hrOffset;
            final SiteApiTimeSeries data = props.getTimeSeries(dataIdx);

            final String channelPrefix = MetOfficeDataHubSiteApiHandler.calculatePrefix(GROUP_PREFIX_HOURS_FORECAST,
                    hrOffset);

            updateState(channelPrefix + SITE_TIMESTAMP, new DateTimeType(data.getTime()).toLocaleZone());

            updateState(channelPrefix + SITE_HOURLY_FORECAST_SCREEN_TEMPERATURE,
                    getQuantityTypeState(data.getScreenTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_HOURLY_FORECAST_MIN_SCREEN_TEMPERATURE,
                    getQuantityTypeState(data.getMinScreenTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_HOURLY_FORECAST_MAX_SCREEN_TEMPERATURE,
                    getQuantityTypeState(data.getMaxScreenTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_HOURLY_FEELS_LIKE_TEMPERATURE,
                    getQuantityTypeState(data.getFeelsLikeTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_HOURLY_SCREEN_RELATIVE_HUMIDITY,
                    getQuantityTypeState(data.getScreenRelativeHumidity(), Units.PERCENT));

            updateState(channelPrefix + SITE_HOURLY_VISIBILITY, getQuantityTypeState(data.getVisibility(), METRE));

            updateState(channelPrefix + SITE_HOURLY_PROBABILITY_OF_PRECIPITATION,
                    getQuantityTypeState(data.getProbOfPrecipitation(), Units.PERCENT));

            updateState(channelPrefix + SITE_HOURLY_PRECIPITATION_RATE,
                    getQuantityTypeState(data.getPrecipitationRate(), Units.MILLIMETRE_PER_HOUR));

            updateState(channelPrefix + SITE_HOURLY_TOTAL_PRECIPITATION_AMOUNT,
                    getQuantityTypeState(data.getTotalPrecipAmount(), MILLI(METRE)));

            updateState(channelPrefix + SITE_HOURLY_TOTAL_SNOW_AMOUNT,
                    getQuantityTypeState(data.getTotalSnowAmount(), MILLI(METRE)));

            updateState(channelPrefix + SITE_HOURLY_PRESSURE, getQuantityTypeState(data.getPressure(), SIUnits.PASCAL));

            updateState(channelPrefix + SITE_HOURLY_WIND_SPEED_10M,
                    getQuantityTypeState(data.getWindSpeed10m(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_HOURLY_MAX_10M_WIND_GUST,
                    getQuantityTypeState(data.getMax10mWindGust(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_HOURLY_WIND_GUST_SPEED_10M,
                    getQuantityTypeState(data.getWindGustSpeed10m(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_HOURLY_SCREEN_DEW_POINT_TEMPERATURE,
                    getQuantityTypeState(data.getScreenDewPointTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_HOURLY_UV_INDEX, getDecimalTypeState(data.getUvIndex()));

            updateState(channelPrefix + SITE_HOURLY_WIND_DIRECTION_FROM_10M,
                    getQuantityTypeState(data.getWindDirectionFrom10m(), Units.DEGREE_ANGLE));
        }
    }

    public void pollForDataDailyData(final String responseData) {
        final SiteApiFeatureCollection response = GSON.fromJson(responseData, SiteApiFeatureCollection.class);

        if (response == null) {
            return;
        }

        final SiteApiFeatureProperties props = response.getFirstProperties();
        if (props == null) {
            return;
        }

        final String startOfHour = MetOfficeDataHubSiteApiHandler.getStartOfDay();
        final int forecastForthisHour = props.getHourlyTimeSeriesPositionForCurrentHour(startOfHour);

        for (int dayOffset = 0; dayOffset <= 6; ++dayOffset) {
            // Calculate the correct array position for the data
            final int dataIdx = forecastForthisHour + dayOffset;

            final String channelPrefix = MetOfficeDataHubSiteApiHandler.calculatePrefix(GROUP_PREFIX_DAILY_FORECAST,
                    dayOffset);

            final SiteApiTimeSeries data = props.getTimeSeries(dataIdx);

            updateState(channelPrefix + SITE_TIMESTAMP, new DateTimeType(data.getTime()).toLocaleZone());

            updateState(channelPrefix + SITE_DAILY_MIDDAY_WIND_SPEED_10M,
                    getQuantityTypeState(data.getMidday10MWindSpeed(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_WIND_SPEED_10M,
                    getQuantityTypeState(data.getMidnight10MWindSpeed(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_DAILY_MIDDAY_WIND_DIRECTION_10M,
                    getQuantityTypeState(data.getMidday10MWindDirection(), Units.DEGREE_ANGLE));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_WIND_DIRECTION_10M,
                    getQuantityTypeState(data.getMidnight10MWindDirection(), Units.DEGREE_ANGLE));

            updateState(channelPrefix + SITE_DAILY_MIDDAY_WIND_GUST_10M,
                    getQuantityTypeState(data.getMidday10MWindGust(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_WIND_GUST_10M,
                    getQuantityTypeState(data.getMidnight10MWindGust(), Units.METRE_PER_SECOND));

            updateState(channelPrefix + SITE_DAILY_MIDDAY_VISIBILITY,
                    getQuantityTypeState(data.getMiddayVisibility(), METRE));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_VISIBILITY,
                    getQuantityTypeState(data.getMidnightVisibility(), METRE));

            updateState(channelPrefix + SITE_DAILY_MIDDAY_REL_HUMIDITY,
                    getQuantityTypeState(data.getMiddayRelativeHumidity(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_REL_HUMIDITY,
                    getQuantityTypeState(data.getMidnightRelativeHumidity(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_MIDDAY_PRESSURE,
                    getQuantityTypeState(data.getMiddayPressure(), SIUnits.PASCAL));

            updateState(channelPrefix + SITE_DAILY_MIDNIGHT_PRESSURE,
                    getQuantityTypeState(data.getMidnightPressure(), SIUnits.PASCAL));

            updateState(channelPrefix + SITE_DAILY_DAY_MAX_UV_INDEX, getDecimalTypeState(data.getMaxUvIndex()));

            updateState(channelPrefix + SITE_DAILY_DAY_UPPER_BOUND_MAX_TEMP,
                    getQuantityTypeState(data.getDayUpperBoundMaxTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_LOWER_BOUND_MAX_TEMP,
                    getQuantityTypeState(data.getDayLowerBoundMaxTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_UPPER_BOUND_MAX_TEMP,
                    getQuantityTypeState(data.getNightUpperBoundMinTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_LOWER_BOUND_MAX_TEMP,
                    getQuantityTypeState(data.getNightLowerBoundMinTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_FEELS_LIKE_MIN_TEMP,
                    getQuantityTypeState(data.getNightMinFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_FEELS_LIKE_MAX_TEMP,
                    getQuantityTypeState(data.getDayMaxFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_LOWER_BOUND_MIN_TEMP,
                    getQuantityTypeState(data.getNightLowerBoundMinTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_MAX_FEELS_LIKE_TEMP,
                    getQuantityTypeState(data.getDayMaxFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_LOWER_BOUND_MIN_FEELS_LIKE_TEMP,
                    getQuantityTypeState(data.getNightLowerBoundMinFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_LOWER_BOUND_MAX_FEELS_LIKE_TEMP,
                    getQuantityTypeState(data.getDayLowerBoundMaxFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_UPPER_BOUND_MAX_FEELS_LIKE_TEMP,
                    getQuantityTypeState(data.getDayUpperBoundMaxFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_UPPER_BOUND_MIN_FEELS_LIKE_TEMP,
                    getQuantityTypeState(data.getNightUpperBoundMinFeelsLikeTemp(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_MAX_SCREEN_TEMPERATURE,
                    getQuantityTypeState(data.getDayMaxScreenTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_NIGHT_MIN_SCREEN_TEMPERATURE,
                    getQuantityTypeState(data.getNightMinScreenTemperature(), SIUnits.CELSIUS));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_PRECIPITATION,
                    getQuantityTypeState(data.getDayProbabilityOfPrecipitation(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_PRECIPITATION,
                    getQuantityTypeState(data.getNightProbabilityOfPrecipitation(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_SNOW,
                    getQuantityTypeState(data.getDayProbabilityOfSnow(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_SNOW,
                    getQuantityTypeState(data.getNightProbabilityOfSnow(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_HEAVY_SNOW,
                    getQuantityTypeState(data.getDayProbabilityOfHeavySnow(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_HEAVY_SNOW,
                    getQuantityTypeState(data.getNightProbabilityOfHeavySnow(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_RAIN,
                    getQuantityTypeState(data.getDayProbabilityOfRain(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_RAIN,
                    getQuantityTypeState(data.getNightProbabilityOfRain(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_HEAVY_RAIN,
                    getQuantityTypeState(data.getDayProbabilityOfHeavyRain(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_HEAVY_RAIN,
                    getQuantityTypeState(data.getNightProbabilityOfHeavyRain(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_HAIL,
                    getQuantityTypeState(data.getDayProbabilityOfHail(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_HAIL,
                    getQuantityTypeState(data.getNightProbabilityOfHail(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_DAY_PROBABILITY_OF_SFERICS,
                    getQuantityTypeState(data.getDayProbabilityOfSferics(), Units.PERCENT));

            updateState(channelPrefix + SITE_DAILY_NIGHT_PROBABILITY_OF_SFERICS,
                    getQuantityTypeState(data.getNightProbabilityOfSferics(), Units.PERCENT));
        }
    }

    private void handleNon200Response(final Response resp) {
        // Handle failed credentials
        if (resp.getStatus() == HttpStatus.UNAUTHORIZED_401) {
            // Remove this once the status is updated accordingly
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    getLocalizedText("bridge.error.site-specific.auth-issue"));
            authFailed = true;
        }
    }

    private @Nullable MetOfficeDataHubBridgeHandler getMetOfficeDataHubBridge() {
        Bridge baseBridge = getBridge();

        if (baseBridge != null && baseBridge.getHandler() instanceof MetOfficeDataHubBridgeHandler bridgeHandler) {
            return bridgeHandler;
        } else {
            return null;
        }
    }

    private void sendForecastRequest(final boolean daily) {
        MetOfficeDataHubBridgeHandler uplinkBridge = getMetOfficeDataHubBridge();
        if (uplinkBridge == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    getLocalizedText("site.error.no-bridge"));
            return;
        }

        final boolean authFailedPreviously = authFailed;

        if (!getThing().getStatus().equals(ThingStatus.ONLINE) && !authFailedPreviously) {
            logger.debug("Disabled requesting data - this thing is not ONLINE");
            return;
        }

        if (!authFailed
                && uplinkBridge.forecastDataLimiter.getRequestCountIfAvailable() == RequestLimiter.INVALID_REQUEST_ID) {
            logger.debug("Disabled requesting data - request limit has been hit");
            return;
        } else {
            uplinkBridge.updateLimiterStats();
        }

        authFailed = false;
        String url = ((daily) ? GET_FORECAST_URL_DAILY : GET_FORECAST_URL_HOURLY)
                .replace("<LATITUDE>", location.getLongitude().toString())
                .replace("<LONGITUDE>", location.getLongitude().toString());

        Request request = httpClient.newRequest(url).method(HttpMethod.GET)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_TYPE.toString())
                .header("apikey", uplinkBridge.getApiKey()).timeout(3, TimeUnit.SECONDS);

        request.send(new BufferingResponseListener() { // 4.5kb buffer will cover both requests
            @Override
            public void onComplete(@Nullable Result result) {
                if (result != null && !result.isFailed()) {
                    final String response = getContentAsString();
                    if (response != null) {
                        scheduler.execute(() -> {
                            logger.trace("Processing response");
                            if (daily) {
                                lastDailyResponse = response;
                                pollForDataDailyData(lastDailyResponse);
                            } else {
                                lastHourlyResponse = response;
                                pollForDataHourlyData(lastHourlyResponse);
                            }
                        });
                        if (authFailedPreviously && getThing().getStatus().equals(ThingStatus.ONLINE)) {
                            updateStatus(ThingStatus.ONLINE);
                        }
                    }
                } else {
                    logger.debug("Failed to get latest MET office forecast");
                    // Clear the web servers thread
                    if (result != null) {
                        final Response resp = result.getResponse();
                        if (resp != null) {
                            scheduler.execute(() -> {
                                handleNon200Response(resp);
                            });
                        }
                    }
                }
            }
        });
    }

    private static String calculatePrefix(final String prefix, final int plusOffset) {
        final StringBuilder strBldr = new StringBuilder(26);
        strBldr.append(prefix);
        if (plusOffset > 0) {
            strBldr.append(GROUP_POSTFIX_BOTH_FORECASTS);
            if (plusOffset < 10) {
                strBldr.append("0");
            }
            strBldr.append(plusOffset);
        }
        strBldr.append(GROUP_PREFIX_TO_ITEM);
        return strBldr.toString();
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);

        config = getConfigAs(MetOfficeDataHubSiteApiConfiguration.class);

        if (config.location.isBlank()) {
            @Nullable
            PointType userLocation = locationProvider.getLocation();
            if (userLocation == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        getLocalizedText("site.error.no-user-location"));
                return;
            } else {
                location = userLocation;
            }
        } else {
            try {
                location = new PointType(config.location);
            } catch (Exception e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        getLocalizedText("site.error.invalid-location"));
                return;
            }
        }

        /**
         * Setup the initial device's status
         */
        scheduler.execute(() -> {
            updateStatus(ThingStatus.ONLINE);
            checkDataRequired();
            reconfigurePolling();
        });
    }

    private void reconfigurePolling() {
        reconfigureHourlyPolling();
        reconfigureDailyPolling();
    }

    private void reconfigureHourlyPolling() {
        final long millisSinceDayStart = getMillisSinceDayStart();
        long pollRateMillis = config.hourlyForecastPollRate * 3600000L;
        long initialDelayTimeToFirstCycle = ((millisSinceDayStart - (millisSinceDayStart % pollRateMillis))
                + pollRateMillis) - millisSinceDayStart;
        long lastPollExpectedTime = System.currentTimeMillis() - (pollRateMillis - initialDelayTimeToFirstCycle);

        LocalDateTime cvDate = Instant.ofEpochMilli(lastPollExpectedTime).atZone(timeZoneProvider.getTimeZone())
                .toLocalDateTime();
        logger.trace("Last hourly poll expected time should have been : {}",
                cvDate.format(DateTimeFormatter.ofPattern(EXPECTED_TS_FORMAT)));

        LocalDateTime cvDate2 = Instant.ofEpochMilli(lastHourlyForecastPoll).atZone(timeZoneProvider.getTimeZone())
                .toLocalDateTime();
        logger.trace("Last hourly daily poll time should have been : {}",
                cvDate2.format(DateTimeFormatter.ofPattern(EXPECTED_TS_FORMAT)));

        // Poll if a poll hasn't been done before, or if the previous poll was before what would be now the new
        // poll intervals last poll time then a poll should be run now.
        if (lastHourlyForecastPoll == -1 || lastPollExpectedTime > lastHourlyForecastPoll) {
            logger.debug("Running hourly data poll for re-sync");
            pollHourlyForecast();
        } else {
            if (requiresHourlyData && !lastHourlyResponse.isEmpty()) {
                logger.trace("Using cached hourly forecast response data");
                pollForDataHourlyData(lastHourlyResponse);
            }
        }

        scheduleNextHourlyForecastPoll();
        scheduleDailyDataPoll();
    }

    private void reconfigureDailyPolling() {
        final long millisSinceDayStart = getMillisSinceDayStart();
        long pollRateMillis = config.dailyForecastPollRate * 3600000L;
        long initialDelayTimeToFirstCycle = ((millisSinceDayStart - (millisSinceDayStart % pollRateMillis))
                + pollRateMillis) - millisSinceDayStart;
        long lastPollExpectedTime = System.currentTimeMillis() - (pollRateMillis - initialDelayTimeToFirstCycle);

        LocalDateTime cvDate = Instant.ofEpochMilli(lastPollExpectedTime).atZone(timeZoneProvider.getTimeZone())
                .toLocalDateTime();
        logger.trace("Last daily poll expected time should have been : {}",
                cvDate.format(DateTimeFormatter.ofPattern(EXPECTED_TS_FORMAT)));

        LocalDateTime cvDate2 = Instant.ofEpochMilli(lastDailyForecastPoll).atZone(timeZoneProvider.getTimeZone())
                .toLocalDateTime();
        logger.trace("Last daily poll time should have been : {}",
                cvDate2.format(DateTimeFormatter.ofPattern(EXPECTED_TS_FORMAT)));

        // Poll if a poll hasn't been done before, or if the previous poll was before what would be now the new
        // poll intervals last poll time then a poll should be run now.
        if (lastDailyForecastPoll == -1 || lastPollExpectedTime > lastDailyForecastPoll) {
            pollDailyForecast();
        } else {
            if (requiresDailyData && !lastDailyResponse.isEmpty()) {
                logger.trace("Using cached daily forecast response data");
                pollForDataDailyData(lastDailyResponse);
            }
        }
        scheduleNextDailyForecastPoll();
        scheduleDailyDataPoll();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        scheduler.execute(() -> {
            if (RefreshType.REFRESH.equals(command)) {
                scheduleDataRequiredCheck();
                if (requiresDailyData) {
                    if (!lastDailyResponse.isEmpty()) {
                        logger.trace("Using cached DAILY forecast response data");
                        pollForDataDailyData(lastDailyResponse);
                    } else {
                        logger.trace("Starting poll sequence for DAILY forecast data");
                        reconfigureDailyPolling();
                    }
                }
                if (requiresHourlyData) {
                    if (!lastDailyResponse.isEmpty()) {
                        logger.trace("Using cached HOURLY forecast response data");
                        pollForDataHourlyData(lastHourlyResponse);
                    } else {
                        logger.trace("Starting poll sequence for HOURLY forecast data");
                        reconfigureHourlyPolling();
                    }
                }
            }
        });
    }

    /**
     * Poll after midnight every-day, to ensure minimal snapshot for that day
     * (offset by random number of seconds up to 1 min - so there is not a burst at the same time to the API's from
     * multiple openhab clients).
     * Cancel currently scheduled polls - re-schedule for time window given from that point onwards
     */

    @Override
    public void pollHourlyForecast() {
        if (requiresHourlyData) {
            logger.debug("Doing a POLL for the HOURLY forecast");
            lastHourlyForecastPoll = System.currentTimeMillis();
            sendForecastRequest(false);
        } else {
            logger.debug("Skipping a POLL for the HOURLY forecast");
        }
    }

    @Override
    public void pollDailyForecast() {
        if (requiresDailyData) {
            logger.debug("Doing a POLL for the DAILY forecast");
            lastDailyForecastPoll = System.currentTimeMillis();
            sendForecastRequest(true);
        } else {
            logger.debug("Skipping a POLL for the DAILY forecast");
        }
    }

    /**
     * Scheduler to evaluate which data is required to be polled from
     * the APIs
     */

    private void scheduleDataRequiredCheck() {
        synchronized (checkDataRequiredSchedulerLock) {
            cancelDataRequiredCheck();
            checkDataRequiredScheduler = scheduler.schedule(this::checkDataRequired, 3, TimeUnit.SECONDS);
        }
    }

    private void cancelDataRequiredCheck() {
        synchronized (checkDataRequiredSchedulerLock) {
            ScheduledFuture<?> job = checkDataRequiredScheduler;
            if (job != null) {
                job.cancel(true);
                checkDataRequiredScheduler = null;
            }
        }
    }

    /**
     * This runs every day at the start of the day
     */
    private void scheduleDailyDataPoll() {
        long delayToNextExecution = DAY_IN_MILLIS - getMillisSinceDayStart();
        delayToNextExecution += RANDOM_GENERATOR.nextInt(60000); // Randomise the poll's to be within a min
        logger.debug("Delay in millis till next daily cycle is {} at {}", delayToNextExecution,
                millisToLocalDateTime(System.currentTimeMillis() + delayToNextExecution));
        synchronized (checkDailySchedulerLock) {
            cancelScheduleDailyDataPoll(true);
            dailyScheduler = scheduler.scheduleWithFixedDelay(() -> {
                logger.debug("Performing daily start poll");
                // cancelScheduleHourlyForecastPoll();
                // Schedule new poll's for each of the cycles
                pollHourlyForecast(); // Poll now for the initial daily poll, then the repeating cycle will kick in
                scheduleNextHourlyForecastPoll();
                scheduleNextDailyForecastPoll();
                // scheduleHourlyForecastPoll(0); // We are starting from 00:00 approx - no delay apart from the
                // Rescheduling will allow for clock changes to be handled the following midnight
                scheduleDailyDataPoll();
            }, delayToNextExecution, DAY_IN_MILLIS, TimeUnit.MILLISECONDS);
        }
    }

    private String millisToLocalDateTime(final long milliseconds) {
        LocalDateTime cvDate = Instant.ofEpochMilli(milliseconds).atZone(timeZoneProvider.getTimeZone())
                .toLocalDateTime();
        return cvDate.format(DateTimeFormatter.ofPattern(EXPECTED_TS_FORMAT));
    }

    private void cancelScheduleDailyDataPoll(final boolean allowInterrupt) {
        synchronized (checkDailySchedulerLock) {
            ScheduledFuture<?> job = dailyScheduler;
            if (job != null) {
                job.cancel(allowInterrupt);
                dailyScheduler = null;
            }
        }
    }

    /**
     * This handles the calculation of the delay until the next hourly forecast data should be retrieved.
     * This calculates using a multiple since midnight to calculate when the next poll should occur.
     * If the next poll is past midnight it is not scheduled as the daily synchronisation should reschedule a poll
     * at that time.
     */
    private void scheduleNextHourlyForecastPoll() {
        final long millisSinceDayStart = getMillisSinceDayStart();
        long pollRateMillis = config.hourlyForecastPollRate * 3600000L;
        long initialDelayTimeToFirstCycle = ((millisSinceDayStart - (millisSinceDayStart % pollRateMillis))
                + pollRateMillis) - millisSinceDayStart;
        initialDelayTimeToFirstCycle += RANDOM_GENERATOR.nextInt(60000);

        if (initialDelayTimeToFirstCycle + millisSinceDayStart >= DAY_IN_MILLIS) {
            logger.debug("Not scheduling poll after next daily cycle reset");
        } else {
            logger.debug("Can scheduling next Hourly forecast data poll to be in {} milliseconds at {}",
                    initialDelayTimeToFirstCycle,
                    millisToLocalDateTime(System.currentTimeMillis() + initialDelayTimeToFirstCycle));

            // Schedule the first poll to occur after the given delay
            hourlyForecastJob.scheduleExecution(initialDelayTimeToFirstCycle, () -> {
                pollHourlyForecast();
                scheduleNextHourlyForecastPoll();
            });
        }
    }

    /**
     * This handles the calculation of the delay until the next hourly forecast data should be retrieved.
     * This calculates using a multiple since midnight to calculate when the next poll should occur.
     * If the next poll is past midnight it is not scheduled as the daily synchronisation should reschedule a poll
     * at that time.
     */
    private void scheduleNextDailyForecastPoll() {
        final long millisSinceDayStart = getMillisSinceDayStart();
        long pollRateMillis = config.dailyForecastPollRate * 3600000L;
        long initialDelayTimeToFirstCycle = ((millisSinceDayStart - (millisSinceDayStart % pollRateMillis))
                + pollRateMillis) - millisSinceDayStart;
        if (initialDelayTimeToFirstCycle + millisSinceDayStart > DAY_IN_MILLIS) {
            logger.debug("Not scheduling poll after next daily cycle reset");
        } else {
            logger.debug("Scheduling next Daily forecast data poll to be in {} milliseconds at {}",
                    initialDelayTimeToFirstCycle,
                    millisToLocalDateTime(System.currentTimeMillis() + initialDelayTimeToFirstCycle));

            initialDelayTimeToFirstCycle += RANDOM_GENERATOR.nextInt(60000);
            // Schedule the first poll to occur after the given delay
            dailyForecastJob.scheduleExecution(initialDelayTimeToFirstCycle, () -> {
                pollDailyForecast();
                scheduleNextDailyForecastPoll();
            });
        }
    }
}
