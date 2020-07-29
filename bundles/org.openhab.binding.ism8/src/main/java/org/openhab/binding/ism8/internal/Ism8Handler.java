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
package org.openhab.binding.ism8.internal;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.ism8.server.DataPointChangedEvent;
import org.openhab.binding.ism8.server.IDataPoint;
import org.openhab.binding.ism8.server.IDataPointChangeListener;
import org.openhab.binding.ism8.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Ism8Handler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Hans-Reiner Hoffmann - Initial contribution
 */
@NonNullByDefault
public class Ism8Handler extends BaseThingHandler implements IDataPointChangeListener {
    private final Logger logger = LoggerFactory.getLogger(Ism8Handler.class);

    private @Nullable Ism8Configuration config;
    private @Nullable Server server;

    public Ism8Handler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        this.logger.debug("Ism8: Handle command = {} {}", channelUID.getId(), command);
        Channel channel = getThing().getChannel(channelUID);
        Server svr = this.server;
        if (channel != null && svr != null) {
            if (channel.getConfiguration().containsKey("id") && channel.getConfiguration().containsKey("write")
                    && channel.getConfiguration().get("write").toString().equalsIgnoreCase("true")) {
                int id = Integer.parseInt(channel.getConfiguration().get("id").toString());
                this.logger.debug("Channel '{}' writting into ID '{}'", channel.getUID().getId(), id);
                this.updateState(channelUID, new QuantityType<>(command.toString()));

                IDataPoint dataPoint = null;
                for (IDataPoint dp : svr.getDataPoints()) {
                    if (dp.getId() == id) {
                        dataPoint = dp;
                        break;
                    }
                }

                if (dataPoint != null) {
                    try {
                        svr.sendData(dataPoint.createWriteData(command));
                    } catch (IOException e) {
                        this.logger.warn("Writting to ISM DataPoint '{}' failed. '{}'", dataPoint.getId(),
                                e.getMessage(), e);
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        Server svr = this.server;
        if (svr != null) {
            svr.stopServerThread();
        }
    }

    @Override
    public void initialize() {
        this.config = getConfigAs(Ism8Configuration.class);
        try {
            Ism8Configuration cfg = this.config;
            if (cfg != null) {
                Server svr = new Server(cfg.getPortNumber());
                for (Channel channel : getThing().getChannels()) {
                    if (channel.getConfiguration().containsKey("id")
                            && channel.getConfiguration().containsKey("type")) {
                        int id = Integer.parseInt(channel.getConfiguration().get("id").toString());
                        String type = channel.getConfiguration().get("type").toString();
                        String description = channel.getLabel();
                        svr.addDataPoint(id, type, description);
                    } else {
                        this.logger.warn("Ism8: ID or type missing - Channel={}  Cfg={}", channel.getLabel(),
                                channel.getConfiguration());
                    }
                    this.logger.debug("Ism8: Channel={}", channel.getConfiguration().toString());
                }

                this.updateStatus(ThingStatus.UNKNOWN);
                svr.addDataPointChangeListener(this);
                scheduler.execute(svr::start);
                this.server = svr;
            }
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, e.getMessage());
            this.logger.warn("Ism8 Initialize: {}", e.getMessage(), e);
        }
    }

    @Override
    public void dataPointChanged(@Nullable DataPointChangedEvent e) {
        if (e != null) {
            IDataPoint dataPoint = e.getDataPoint();
            if (dataPoint != null) {
                this.logger.debug("Ism8: dataPointChanged {}", dataPoint.toString());
                this.updateDataPoint(dataPoint);
            }
        }
    }

    @Override
    public void connectionStatusChanged(ThingStatus status) {
        this.updateStatus(status);
    }

    private void updateDataPoint(IDataPoint dataPoint) {
        this.updateStatus(ThingStatus.ONLINE);
        for (Channel channel : getThing().getChannels()) {
            try {
                if (channel.getConfiguration().containsKey("id")
                        && Integer.parseInt(channel.getConfiguration().get("id").toString()) == dataPoint.getId()) {
                    logger.debug("Ism8 updateDataPoint ID:{} {}", dataPoint.getId(), dataPoint.getValueText());
                    updateState(channel.getUID(), new QuantityType<>(dataPoint.getValueObject().toString()));
                }
            } catch (Exception e) {
                logger.warn("Ism8 updateDataPoint: {}", e.getMessage(), e);
            }
        }
    }
}
