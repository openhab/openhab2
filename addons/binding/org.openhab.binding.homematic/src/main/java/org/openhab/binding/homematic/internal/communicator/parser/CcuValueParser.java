/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.parser;

import java.io.IOException;

import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDatapointInfo;
import org.openhab.binding.homematic.internal.model.TclScriptDataEntry;
import org.openhab.binding.homematic.internal.model.TclScriptDataList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses a TclRega script result containing datapoint values for a channel.
 *
 * @author Gerhard Riegler - Initial contribution
 */
public class CcuValueParser extends CommonRpcParser<TclScriptDataList, Void> {
    private static final Logger logger = LoggerFactory.getLogger(CcuValueParser.class);

    private HmChannel channel;

    public CcuValueParser(HmChannel channel) {
        this.channel = channel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void parse(TclScriptDataList resultList) throws IOException {
        if (resultList.getEntries() != null) {
            for (TclScriptDataEntry entry : resultList.getEntries()) {
                HmDatapointInfo dpInfo = HmDatapointInfo.createValuesInfo(channel, entry.name);
                HmDatapoint dp = channel.getDatapoint(dpInfo);
                if (dp != null) {
                    setDatapointValue(dp, entry.value);
                    adjustRssiValue(dp);
                } else {
                    // should never happen, but in case ...
                    logger.warn("Can't set value for datapoint '{}'", dpInfo);
                }
            }
        }
        return null;
    }
}
