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
package org.openhab.binding.openuv.internal.handler;

import static org.openhab.binding.openuv.internal.OpenUVBindingConstants.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Angle;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.openuv.internal.config.ReportConfiguration;
import org.openhab.binding.openuv.internal.config.SafeExposureConfiguration;
import org.openhab.binding.openuv.internal.json.OpenUVResult;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.SmartHomeUnits;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenUVReportHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class OpenUVReportHandler extends BaseThingHandler {
    private final Logger logger = LoggerFactory.getLogger(OpenUVReportHandler.class);

    private @NonNullByDefault({}) OpenUVBridgeHandler bridgeHandler;
    private @Nullable ScheduledFuture<?> refreshJob;
    private @Nullable ScheduledFuture<?> uvMaxJob;
    private boolean suspendUpdates = false;

    public OpenUVReportHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing OpenUV handler.");

        ReportConfiguration config = getConfigAs(ReportConfiguration.class);

        if (config.refresh < 3) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Parameter 'refresh' must be higher than 3 minutes to stay in free API plan");
        } else {
            Bridge bridge = getBridge();
            if (bridge == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Invalid bridge");
            } else {
                bridgeHandler = (OpenUVBridgeHandler) bridge.getHandler();
                updateStatus(ThingStatus.UNKNOWN);
                startAutomaticRefresh();
            }
        }
    }

    /**
     * Start the job screening UV Max reached
     *
     * @param openUVData
     */
    private void scheduleUVMaxEvent(OpenUVResult openUVData) {
        ScheduledFuture<?> job = this.uvMaxJob;
        if ((job == null || job.isCancelled())) {
            State uvMaxTime = openUVData.getUVMaxTime();
            if (uvMaxTime != UnDefType.NULL) {
                ZonedDateTime uvMaxZdt = ((DateTimeType) uvMaxTime).getZonedDateTime();
                long timeDiff = ChronoUnit.MINUTES.between(ZonedDateTime.now(ZoneId.systemDefault()), uvMaxZdt);
                if (timeDiff > 0) {
                    logger.debug("Scheduling {} in {} minutes", UV_MAX_EVENT, timeDiff);
                    uvMaxJob = scheduler.schedule(() -> {
                        triggerChannel(UV_MAX_EVENT);
                        uvMaxJob = null;
                    }, timeDiff, TimeUnit.MINUTES);
                }
            }
        }
    }

    /**
     * Start the job refreshing the data
     */
    private void startAutomaticRefresh() {
        ScheduledFuture<?> job = this.refreshJob;
        if (job == null || job.isCancelled()) {
            ReportConfiguration config = getConfigAs(ReportConfiguration.class);
            refreshJob = scheduler.scheduleWithFixedDelay(() -> {
                if (!suspendUpdates) {
                    updateChannels(config);
                }
            }, 0, config.refresh, TimeUnit.MINUTES);
        }
    }

    private void updateChannels(ReportConfiguration config) {
        ThingStatusInfo bridgeStatusInfo = bridgeHandler.getThing().getStatusInfo();
        if (bridgeStatusInfo.getStatus() == ThingStatus.ONLINE) {
            PointType location = new PointType(config.location);
            OpenUVResult openUVData = bridgeHandler.getUVData(location.getLatitude().toString(),
                    location.getLongitude().toString(), location.getAltitude().toString());
            if (openUVData != null) {
                scheduleUVMaxEvent(openUVData);
                getThing().getChannels().forEach(channel -> {
                    updateChannel(channel.getUID(), openUVData);
                });
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE, bridgeStatusInfo.getStatusDetail(),
                        bridgeStatusInfo.getDescription());
            }
        }
    }

    @Override
    public void dispose() {
        logger.debug("Disposing the OpenUV handler.");
        ScheduledFuture<?> refresh = this.refreshJob;
        if (refresh != null && !refresh.isCancelled()) {
            refresh.cancel(true);
            refreshJob = null;
        }
        ScheduledFuture<?> uxMax = this.uvMaxJob;
        if (uxMax != null && !uxMax.isCancelled()) {
            uxMax.cancel(true);
            uvMaxJob = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (command instanceof RefreshType) {
            scheduler.execute(() -> {
                ReportConfiguration config = getConfigAs(ReportConfiguration.class);
                updateChannels(config);
            });
        } else if (ELEVATION.equals(channelUID.getId()) && command instanceof QuantityType) {
            QuantityType<?> qtty = (QuantityType<?>) command;
            if ("°".equals(qtty.getUnit().toString())) {
                suspendUpdates = ((QuantityType<Angle>) qtty).doubleValue() < 0;
            } else {
                logger.info("The OpenUV Report handles Sun Elevation of Number:Angle type, {} does not fit.", command);
            }
        } else {
            logger.info("The OpenUV Report Thing handles Refresh or Sun Elevation command and not '{}'", command);
        }
    }

    /**
     * Update the channel from the last OpenUV data retrieved
     *
     * @param channelUID the id identifying the channel to be updated
     * @param openUVData
     *
     */
    private void updateChannel(ChannelUID channelUID, OpenUVResult openUVData) {
        Channel channel = getThing().getChannel(channelUID.getId());
        if (channel != null && isLinked(channelUID)) {
            ChannelTypeUID channelTypeUID = channel.getChannelTypeUID();
            if (channelTypeUID != null) {
                switch (channelTypeUID.getId()) {
                    case UV_INDEX:
                        updateState(channelUID, openUVData.getUv());
                        break;
                    case UV_COLOR:
                        updateState(channelUID, getAsHSB(openUVData.getUv().intValue()));
                        break;
                    case UV_MAX:
                        updateState(channelUID, openUVData.getUvMax());
                        break;
                    case OZONE:
                        updateState(channelUID, new QuantityType<>(openUVData.getOzone(), SmartHomeUnits.DOBSON_UNIT));
                        break;
                    case OZONE_TIME:
                        updateState(channelUID, openUVData.getOzoneTime());
                        break;
                    case UV_MAX_TIME:
                        updateState(channelUID, openUVData.getUVMaxTime());
                        break;
                    case UV_TIME:
                        updateState(channelUID, openUVData.getUVTime());
                        break;
                    case SAFE_EXPOSURE:
                        SafeExposureConfiguration configuration = channel.getConfiguration()
                                .as(SafeExposureConfiguration.class);
                        if (configuration.index != -1) {
                            updateState(channelUID,
                                    openUVData.getSafeExposureTime().getSafeExposure(configuration.index));
                        }
                        break;
                }
            }
        }
    }

    private State getAsHSB(int uv) {
        if (uv >= 11) {
            return HSBType.fromRGB(106, 27, 154);
        } else if (uv >= 8) {
            return HSBType.fromRGB(183, 28, 28);
        } else if (uv >= 6) {
            return HSBType.fromRGB(239, 108, 0);
        } else if (uv >= 3) {
            return HSBType.fromRGB(249, 168, 37);
        } else {
            return HSBType.fromRGB(85, 139, 47);
        }
    }
}
