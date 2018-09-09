/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.groheondus.internal.handler;

import static org.openhab.binding.groheondus.internal.GroheOndusBindingConstants.*;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.grohe.ondus.api.OndusService;
import org.grohe.ondus.api.model.BaseApplianceData;
import org.grohe.ondus.api.model.SenseAppliance;
import org.grohe.ondus.api.model.SenseApplianceData;
import org.grohe.ondus.api.model.SenseApplianceData.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Florian Schmidt - Add GROHE Sense device
 */
@NonNullByDefault
public class GroheOndusSenseHandler<T> extends GroheOndusBaseHandler<SenseAppliance> {

    private static final int DEFAULT_POLLING_INTERVAL = 900;

    private final Logger logger = LoggerFactory.getLogger(GroheOndusSenseHandler.class);

    public GroheOndusSenseHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        super.channelLinked(channelUID);

        OndusService ondusService = getOndusService();
        if (ondusService == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE,
                    "No initialized OndusService available from bridge.");
            return;
        }
        SenseAppliance appliance = getAppliance(ondusService);
        if (appliance == null) {
            return;
        }
        updateChannel(channelUID, appliance, getLastMeasurement(appliance));
    }

    @Override
    public void updateChannels() {
        OndusService ondusService = getOndusService();
        if (ondusService == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE,
                    "No initialized OndusService available from bridge.");
            return;
        }
        SenseAppliance appliance = getAppliance(ondusService);
        if (appliance == null) {
            return;
        }

        Measurement measurement = getLastMeasurement(appliance);
        getThing().getChannels().forEach(channel -> updateChannel(channel.getUID(), appliance, measurement));

        updateStatus(ThingStatus.ONLINE);
    }

    @Override
    protected int getPollingInterval(SenseAppliance appliance) {
        if (config.pollingInterval > 0) {
            return config.pollingInterval;
        }
        return DEFAULT_POLLING_INTERVAL;
    }

    private void updateChannel(ChannelUID channel, SenseAppliance appliance, Measurement measurement) {
        String channelId = channel.getIdWithoutGroup();
        State newState;
        switch (channelId) {
            case CHANNEL_NAME:
                newState = new StringType(appliance.getName());
                break;
            case CHANNEL_TEMPERATURE:
                newState = new QuantityType<>(measurement.getTemperature(), SIUnits.CELSIUS);
                break;
            case CHANNEL_HUMIDITY:
                newState = new QuantityType<>(measurement.getHumidity(), SmartHomeUnits.PERCENT);
                break;
            default:
                throw new IllegalArgumentException("Channel " + channel + " not supported.");
        }
        if (newState != null) {
            updateState(channel, newState);
        }
    }

    private Measurement getLastMeasurement(SenseAppliance appliance) {
        if (getOndusService() == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE,
                    "No initialized OndusService available from bridge.");
            return new Measurement();
        }

        SenseApplianceData applianceData = getApplianceData(appliance);
        if (applianceData == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Could not load data from API.");
            return new Measurement();
        }
        List<Measurement> measurementList = applianceData.getData().getMeasurement();

        return measurementList.isEmpty() ? new Measurement() : measurementList.get(measurementList.size() - 1);
    }

    private @Nullable SenseApplianceData getApplianceData(SenseAppliance appliance) {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant today = Instant.now();
        OndusService service = getOndusService();
        if (service == null) {
            return null;
        }
        try {
            BaseApplianceData applianceData = service.getApplianceData(appliance, yesterday, today).orElse(null);
            if (applianceData.getType() != SenseAppliance.TYPE) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Thing is not a GROHE SENSE device.");
                return null;
            }
            return (SenseApplianceData) applianceData;
        } catch (IOException e) {
            logger.debug("Could not load appliance data", e);
        }
        return null;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        return;
    }

    @Override
    protected int getType() {
        return SenseAppliance.TYPE;
    }
}
