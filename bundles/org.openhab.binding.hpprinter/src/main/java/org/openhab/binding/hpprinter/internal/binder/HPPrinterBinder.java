/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.hpprinter.internal.binder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.openhab.binding.hpprinter.internal.HPPrinterConfiguration;
import org.openhab.binding.hpprinter.internal.api.HPServerResult;
import org.openhab.binding.hpprinter.internal.api.HPStatus;
import org.openhab.binding.hpprinter.internal.api.HPType;
import org.openhab.binding.hpprinter.internal.api.HPUsage;
import org.openhab.binding.hpprinter.internal.api.HPWebServerClient;
import org.openhab.binding.hpprinter.internal.api.HPServerResult.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openhab.binding.hpprinter.internal.HPPrinterBindingConstants.*;

/**
 * The {@link HPPrinterBinder} connects the binding to the Web Server Client
 * classes.
 *
 * @author Stewart Cossey - Initial contribution
 */
@NonNullByDefault
public class HPPrinterBinder {

    private final Logger logger = LoggerFactory.getLogger(HPPrinterBinder.class);

    private HPWebServerClient printerClient;
    private ScheduledExecutorService scheduler;

    private int statusCheckInterval = 4;
    private int usageCheckInterval = 30;
    private int offlineCheckInterval = 15;

    private HPPrinterBinderEvent handler;
    @Nullable
    private ScheduledFuture<?> statusScheduler = null;
    @Nullable
    private ScheduledFuture<?> usageScheduler = null;
    @Nullable
    private ScheduledFuture<?> offlineScheduler = null;

    /**
     * Creates a new HP Printer Binder object
     * 
     * @param handler    {HPPrinterBinderEvent} The Event handler for the binder.
     * @param httpClient {HttpClient} The HttpClient object to use to perform HTTP
     *                   requests.
     * @param scheduler  {ScheduledExecutorService} The scheduler service object.
     * @param config     {HPPrinterConfiguration} The configuration object.
     */
    public HPPrinterBinder(HPPrinterBinderEvent handler, @Nullable HttpClient httpClient,
            ScheduledExecutorService scheduler, @Nullable HPPrinterConfiguration config) {
        this.handler = handler;
        this.scheduler = scheduler;
        if (config != null) {
            usageCheckInterval = config.usageInterval;
            statusCheckInterval = config.statusInterval;
        }
        printerClient = new HPWebServerClient(httpClient, config.ipAddress);
    }

    /**
     * Dynamically add channels to the Thing based on the Embedded Web Server Usage Feed
     */
    public void dynamicallyAddChannels(ThingUID thingUid) {
        HPServerResult<HPType> result = printerClient.getType();

        logger.debug("Building dynamic channels based on printer");

        if (result.getStatus() == RequestStatus.SUCCESS) {
            final List<Channel> channels = new ArrayList<>();

            switch (result.getData().getType()) {
            case SINGLECOLOR:
                channels.add(ChannelBuilder.create(new ChannelUID(thingUid, CGROUP_INK, CHANNEL_COLOR_LEVEL), "Number")
                        .withLabel("Color Level").withDescription("Shows the amount of Colour Ink/Toner remaining")
                        .withType(new ChannelTypeUID("hpprinter:inkLevel")).build());

                channels.add(ChannelBuilder
                        .create(new ChannelUID(thingUid, CGROUP_USAGE, CHANNEL_TOTAL_COLORPAGES), "Number")
                        .withLabel("Total Colour Pages").withDescription("The amount of colour pages printed")
                        .withType(new ChannelTypeUID("hpprinter:totals")).build());

                channels.add(ChannelBuilder
                        .create(new ChannelUID(thingUid, CGROUP_USAGE, CHANNEL_TOTAL_MONOPAGES), "Number")
                        .withLabel("Total Monochrome Pages").withDescription("The amount of monochrome pages printed")
                        .withType(new ChannelTypeUID("hpprinter:totals")).build());

                break;

            case MULTICOLOR:
                channels.add(ChannelBuilder.create(new ChannelUID(thingUid, CGROUP_INK, CHANNEL_CYAN_LEVEL), "Number")
                        .withLabel("Cyan Level").withDescription("Shows the amount of Cyan Ink/Toner remaining")
                        .withType(new ChannelTypeUID("hpprinter:inkLevel")).build());

                channels.add(ChannelBuilder
                        .create(new ChannelUID(thingUid, CGROUP_INK, CHANNEL_MAGENTA_LEVEL), "Number")
                        .withLabel("Magenta Level").withDescription("Shows the amount of Magenta Ink/Toner remaining")
                        .withType(new ChannelTypeUID("hpprinter:inkLevel")).build());

                channels.add(ChannelBuilder.create(new ChannelUID(thingUid, CGROUP_INK, CHANNEL_YELLOW_LEVEL), "Number")
                        .withLabel("Yellow Level").withDescription("Shows the amount of Yellow Ink/Toner remaining")
                        .withType(new ChannelTypeUID("hpprinter:inkLevel")).build());

                channels.add(ChannelBuilder
                        .create(new ChannelUID(thingUid, CGROUP_USAGE, CHANNEL_TOTAL_COLORPAGES), "Number")
                        .withLabel("Total Colour Pages").withDescription("The amount of colour pages printed")
                        .withType(new ChannelTypeUID("hpprinter:totals")).build());

                channels.add(ChannelBuilder
                        .create(new ChannelUID(thingUid, CGROUP_USAGE, CHANNEL_TOTAL_MONOPAGES), "Number")
                        .withLabel("Total Monochrome Pages").withDescription("The amount of monochrome pages printed")
                        .withType(new ChannelTypeUID("hpprinter:totals")).build());

                break;

            default:
            }

            handler.binderAddChannels(channels);
        }
    }

    /**
     * Opens the connection to the Embedded Web Server
     */
    public void open() {
        goneOffline();
    }

    /**
     * Close the connection to the Embedded Web Server
     */
    public void close() {
        stopBackgroundSchedules();

        if (offlineScheduler != null) {
            offlineScheduler.cancel(true);
            offlineScheduler = null;
        }
    }

    /**
     * The device has gone offline
     */
    public void goneOffline() {
        handler.binderStatus(ThingStatus.OFFLINE);

        stopBackgroundSchedules();

        if (offlineScheduler != null) {
            offlineScheduler.cancel(true);
            offlineScheduler = null;
        }
        offlineScheduler = scheduler.scheduleWithFixedDelay(() -> {
            checkOnline();
        }, 0, offlineCheckInterval, TimeUnit.SECONDS);

    }

    private void stopBackgroundSchedules() {
        logger.debug("Stopping Interval Refreshes");
        if (usageScheduler != null) {
            usageScheduler.cancel(true);
            usageScheduler = null;
        }

        if (statusScheduler != null) {
            statusScheduler.cancel(true);
            statusScheduler = null;
        }
    }

    private void startBackgroundSchedules() {
        handler.binderStatus(ThingStatus.ONLINE);

        logger.debug("Starting Interval Refreshes");

        if (usageScheduler != null) {
            usageScheduler.cancel(true);
            usageScheduler = null;
        }
        usageScheduler = scheduler.scheduleWithFixedDelay(() -> {
            checkUsage();
        }, 0, usageCheckInterval, TimeUnit.SECONDS);

        if (statusScheduler != null) {
            statusScheduler.cancel(true);
            statusScheduler = null;
        }
        statusScheduler = scheduler.scheduleWithFixedDelay(() -> {
            checkStatus();
        }, 0, statusCheckInterval, TimeUnit.SECONDS);
    }

    private void checkStatus() {
        HPServerResult<HPStatus> result = printerClient.getStatus();

        if (result.getStatus() == RequestStatus.SUCCESS) {
            handler.binderChannel(CGROUP_STATUS, CHANNEL_STATUS, new StringType(result.getData().getPrinterStatus()));

        } else {
            goneOffline();
        }
    }

    private void checkUsage() {
        HPServerResult<HPUsage> result = printerClient.getUsage();

        if (result.getStatus() == RequestStatus.SUCCESS) {
            // Inks
            handler.binderChannel(CGROUP_INK, CHANNEL_BLACK_LEVEL, new DecimalType(result.getData().getInkBlack()));
            handler.binderChannel(CGROUP_INK, CHANNEL_COLOR_LEVEL, new DecimalType(result.getData().getInkColor()));
            handler.binderChannel(CGROUP_INK, CHANNEL_CYAN_LEVEL, new DecimalType(result.getData().getInkCyan()));
            handler.binderChannel(CGROUP_INK, CHANNEL_MAGENTA_LEVEL, new DecimalType(result.getData().getInkMagenta()));
            handler.binderChannel(CGROUP_INK, CHANNEL_YELLOW_LEVEL, new DecimalType(result.getData().getInkYellow()));

            handler.binderChannel(CGROUP_USAGE, CHANNEL_JAM_EVENTS, new DecimalType(result.getData().getJamEvents()));
            handler.binderChannel(CGROUP_USAGE, CHANNEL_SUBSCRIPTION,
                    new DecimalType(result.getData().getTotalSubscriptionImpressions()));
            handler.binderChannel(CGROUP_USAGE, CHANNEL_TOTAL_COLORPAGES,
                    new DecimalType(result.getData().getTotalColorImpressions()));
            handler.binderChannel(CGROUP_USAGE, CHANNEL_TOTAL_MONOPAGES,
                    new DecimalType(result.getData().getTotalMonochromeImpressions()));
            handler.binderChannel(CGROUP_USAGE, CHANNEL_TOTAL_PAGES,
                    new DecimalType(result.getData().getTotalImpressions()));
        } else {
            goneOffline();
        }
    }

    private void goneOnline() {
        handler.binderStatus(ThingStatus.ONLINE);

        if (offlineScheduler != null) {
            offlineScheduler.cancel(true);
            offlineScheduler = null;
        }

        startBackgroundSchedules();
    }

    private void checkOnline() {
        HPServerResult<HPStatus> result = printerClient.getStatus();

        if (result.getStatus() == RequestStatus.SUCCESS) {
            goneOnline();
        }
    }

}
