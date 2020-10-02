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
package org.openhab.binding.haywardomnilogic.internal.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.haywardomnilogic.internal.HaywardBindingConstants;
import org.openhab.binding.haywardomnilogic.internal.HaywardThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Backyard Handler
 *
 * @author Matt Myers - Initial Contribution
 */
@NonNullByDefault
public class HaywardBackyardHandler extends HaywardThingHandler {
    private final Logger logger = LoggerFactory.getLogger(HaywardBackyardHandler.class);

    public HaywardBackyardHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void getTelemetry(String xmlResponse) throws Exception {
        List<String> data = new ArrayList<>();
        List<String> systemIDs = new ArrayList<>();

        @SuppressWarnings("null")
        HaywardBridgeHandler bridgehandler = (HaywardBridgeHandler) getBridge().getHandler();
        if (bridgehandler != null) {
            systemIDs = bridgehandler.evaluateXPath("//Backyard/@systemId", xmlResponse);
            String thingSystemID = getThing().getProperties().get(HaywardBindingConstants.PROPERTY_SYSTEM_ID);
            for (int i = 0; i < systemIDs.size(); i++) {
                if (systemIDs.get(i).equals(thingSystemID)) {
                    // Air temp
                    data = bridgehandler.evaluateXPath("//Backyard/@airTemp", xmlResponse);
                    updateData(HaywardBindingConstants.CHANNEL_BACKYARD_AIRTEMP, data.get(0));

                    // Status
                    data = bridgehandler.evaluateXPath("//Backyard/@status", xmlResponse);
                    updateData(HaywardBindingConstants.CHANNEL_BACKYARD_STATUS, data.get(0));

                    // State
                    data = bridgehandler.evaluateXPath("//Backyard/@state", xmlResponse);
                    updateData(HaywardBindingConstants.CHANNEL_BACKYARD_STATE, data.get(0));
                }
            }
        }
    }

    public boolean getAlarmList(String systemID) throws Exception {
        List<String> bowID = new ArrayList<>();
        List<String> parameter1 = new ArrayList<>();
        List<String> message = new ArrayList<>();
        String alarmStr;

        @SuppressWarnings("null")
        HaywardBridgeHandler bridgehandler = (HaywardBridgeHandler) getBridge().getHandler();

        // *****Request Alarm List from Hayward server
        @SuppressWarnings("null")
        String urlParameters = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Request><Name>GetAlarmList</Name><Parameters>"
                + "<Parameter name=\"Token\" dataType=\"String\">" + bridgehandler.account.token + "</Parameter>"
                + "<Parameter name=\"MspSystemID\" dataType=\"int\">" + bridgehandler.account.mspSystemID
                + "</Parameter>"
                + "<Parameter name=\"CultureInfoName\" dataType=\"String\">en-us</Parameter></Parameters></Request>";

        String xmlResponse = bridgehandler.httpXmlResponse(urlParameters);

        if (xmlResponse.isEmpty()) {
            logger.error("Hayward getAlarmList XML response was empty");
            return false;
        }

        String status = bridgehandler
                .evaluateXPath("/Response/Parameters//Parameter[@name='Status']/text()", xmlResponse).get(0);

        if (!(status.equals("0"))) {
            logger.error("Hayward getAlarm XML response: {}", xmlResponse);
            return false;
        }

        if (status.equals("0")) {
            bowID = bridgehandler.evaluateXPath("//Property[@name='BowID']/text()", xmlResponse);
            parameter1 = bridgehandler.evaluateXPath("//Property[@name='Parameter1']/text()", xmlResponse);
            message = bridgehandler.evaluateXPath("//Property[@name='Message']/text()", xmlResponse);

            for (int i = 0; i < 5; i++) {
                if (i < bowID.size()) {
                    alarmStr = parameter1.get(i) + ": " + message.get(i);
                } else {
                    alarmStr = "";
                }
                updateData("backyardAlarm" + String.format("%01d", i + 1), alarmStr);
            }
        } else {
            logger.error("Hayward getAlarms XML response: {}", xmlResponse);
            return false;
        }
        return true;
    }
}
