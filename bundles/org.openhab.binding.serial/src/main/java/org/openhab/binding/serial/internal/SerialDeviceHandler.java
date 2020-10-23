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
package org.openhab.binding.serial.internal;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;

/**
 * The {@link SerialDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Mike Major - Initial contribution
 */
@NonNullByDefault
public class SerialDeviceHandler extends BaseThingHandler {

    private @Nullable SerialDeviceConfiguration config;

    private @Nullable Pattern devicePattern;

    public SerialDeviceHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void initialize() {
        config = getConfigAs(SerialDeviceConfiguration.class);

        try {
            devicePattern = Pattern.compile(config.patternMatch);
        } catch (PatternSyntaxException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Invalid device pattern" + e.getMessage());
            return;
        }

        updateStatus(ThingStatus.UNKNOWN);
        bridgeStatusChanged(getBridgeStatus());
    }

    public void handleData(String data) {
        if (devicePattern.matcher(StringUtils.chomp(data)).matches()) {
            if (isLinked(SerialBindingConstants.STRING_CHANNEL)) {
                updateState(SerialBindingConstants.STRING_CHANNEL, new StringType(StringUtils.chomp(data)));
            }
        }
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        if (getThing().getStatusInfo().getStatusDetail() == ThingStatusDetail.CONFIGURATION_ERROR) {
            return;
        }

        if (bridgeStatusInfo.getStatus() == ThingStatus.ONLINE && getThing().getStatus() == ThingStatus.UNKNOWN) {
            updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE);
            return;
        }

        super.bridgeStatusChanged(bridgeStatusInfo);
    }

    /**
     * Return the bridge status.
     */
    public ThingStatusInfo getBridgeStatus() {
        Bridge b = getBridge();
        if (b != null) {
            return b.getStatusInfo();
        } else {
            return new ThingStatusInfo(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, null);
        }
    }
}
