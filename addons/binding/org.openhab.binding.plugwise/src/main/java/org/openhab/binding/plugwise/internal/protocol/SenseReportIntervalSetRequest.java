/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal.protocol;

import static org.openhab.binding.plugwise.internal.protocol.field.MessageType.SENSE_REPORT_INTERVAL_SET_REQUEST;

import org.openhab.binding.plugwise.internal.protocol.field.MACAddress;

/**
 * Sets the Sense temperature and humidity measurement report interval. Based on this interval, periodically a
 * {@link SenseReportRequestMessage} is sent.
 *
 * @author Wouter Born - Initial contribution
 */
public class SenseReportIntervalSetRequest extends Message {

    private int reportInterval;

    public SenseReportIntervalSetRequest(MACAddress macAddress, int reportInterval) {
        super(SENSE_REPORT_INTERVAL_SET_REQUEST, macAddress);
        this.reportInterval = reportInterval;
    }

    @Override
    protected String payloadToHexString() {
        return String.format("%02X", reportInterval);
    }

}
