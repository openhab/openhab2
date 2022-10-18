/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.saicismart.internal;

import static org.openhab.binding.saicismart.internal.SAICiSMARTBindingConstants.CHANNEL_ENGINE;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.openhab.binding.saicismart.internal.asn1.Util;
import org.openhab.binding.saicismart.internal.asn1.v2_1.MP_DispatcherBody;
import org.openhab.binding.saicismart.internal.asn1.v2_1.MP_DispatcherHeader;
import org.openhab.binding.saicismart.internal.asn1.v2_1.Message;
import org.openhab.binding.saicismart.internal.asn1.v2_1.MessageCoder;
import org.openhab.binding.saicismart.internal.asn1.v2_1.OTA_RVMVehicleStatusReq;
import org.openhab.binding.saicismart.internal.asn1.v2_1.OTA_RVMVehicleStatusResp25857;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.ThingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;

/**
 *
 * @author Markus Heberling - Initial contribution
 */
class VehicleStateUpdater implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(VehicleStateUpdater.class);

    private final SAICiSMARTHandler saiCiSMARTHandler;

    public VehicleStateUpdater(SAICiSMARTHandler saiCiSMARTHandler) {
        this.saiCiSMARTHandler = saiCiSMARTHandler;
    }

    @Override
    public void run() {
        try {
            Message<OTA_RVMVehicleStatusReq> chargingStatusMessage = new Message<>(new MP_DispatcherHeader(),
                    new byte[16], new MP_DispatcherBody(), new OTA_RVMVehicleStatusReq());
            Util.fillReserved(chargingStatusMessage.getReserved());

            chargingStatusMessage.getBody().setApplicationID("511");
            chargingStatusMessage.getBody().setTestFlag(2);
            chargingStatusMessage.getBody().setVin(saiCiSMARTHandler.config.vin);
            chargingStatusMessage.getBody().setUid(saiCiSMARTHandler.getBridgeHandler().getUid());
            chargingStatusMessage.getBody().setToken(saiCiSMARTHandler.getBridgeHandler().getToken());
            chargingStatusMessage.getBody().setMessageID(1);
            chargingStatusMessage.getBody().setEventCreationTime((int) Instant.now().getEpochSecond());
            chargingStatusMessage.getBody().setApplicationDataProtocolVersion(25857);
            chargingStatusMessage.getBody().setEventID(0);

            chargingStatusMessage.getApplicationData().setVehStatusReqType(1);

            String chargingStatusRequestMessage = new MessageCoder<>(OTA_RVMVehicleStatusReq.class)
                    .encodeRequest(chargingStatusMessage);

            String chargingStatusResponse = saiCiSMARTHandler.getBridgeHandler()
                    .sendRequest(chargingStatusRequestMessage, "https://tap-eu.soimt.com/TAP.Web/ota.mpv21");

            Message<OTA_RVMVehicleStatusResp25857> chargingStatusResponseMessage = new MessageCoder<>(
                    OTA_RVMVehicleStatusResp25857.class).decodeResponse(chargingStatusResponse);

            // we get an eventId back...
            chargingStatusMessage.getBody().setEventID(chargingStatusResponseMessage.getBody().getEventID());
            // ... use that to request the data again, until we have it
            // TODO: check for real errors (result!=0 and/or errorMessagePresent)
            while (chargingStatusResponseMessage.getApplicationData() == null) {

                chargingStatusMessage.getBody().setUid(saiCiSMARTHandler.getBridgeHandler().getUid());
                chargingStatusMessage.getBody().setToken(saiCiSMARTHandler.getBridgeHandler().getToken());

                Util.fillReserved(chargingStatusMessage.getReserved());

                chargingStatusRequestMessage = new MessageCoder<>(OTA_RVMVehicleStatusReq.class)
                        .encodeRequest(chargingStatusMessage);

                chargingStatusResponse = saiCiSMARTHandler.getBridgeHandler().sendRequest(chargingStatusRequestMessage,
                        "https://tap-eu.soimt.com/TAP.Web/ota.mpv21");

                chargingStatusResponseMessage = new MessageCoder<>(OTA_RVMVehicleStatusResp25857.class)
                        .decodeResponse(chargingStatusResponse);

            }

            logger.info("Got message: {}", new GsonBuilder().setPrettyPrinting().create()
                    .toJson(chargingStatusResponseMessage.getApplicationData()));

            saiCiSMARTHandler.updateState(CHANNEL_ENGINE, OnOffType.from(
                    chargingStatusResponseMessage.getApplicationData().getBasicVehicleStatus().getEngineStatus() == 1));

            saiCiSMARTHandler.updateState(SAICiSMARTBindingConstants.CHANNEL_TYRE_PRESSURE_FRONT_LEFT,
                    new QuantityType<>(chargingStatusResponseMessage.getApplicationData().getBasicVehicleStatus()
                            .getFrontLeftTyrePressure() * 4 / 100.d, Units.BAR));
            saiCiSMARTHandler.updateState(SAICiSMARTBindingConstants.CHANNEL_TYRE_PRESSURE_FRONT_RIGHT,
                    new QuantityType<>(chargingStatusResponseMessage.getApplicationData().getBasicVehicleStatus()
                            .getFrontRrightTyrePressure() * 4 / 100.d, Units.BAR));
            saiCiSMARTHandler.updateState(SAICiSMARTBindingConstants.CHANNEL_TYRE_PRESSURE_REAR_LEFT,
                    new QuantityType<>(chargingStatusResponseMessage.getApplicationData().getBasicVehicleStatus()
                            .getRearLeftTyrePressure() * 4 / 100.d, Units.BAR));
            saiCiSMARTHandler.updateState(SAICiSMARTBindingConstants.CHANNEL_TYRE_PRESSURE_REAR_RIGHT,
                    new QuantityType<>(chargingStatusResponseMessage.getApplicationData().getBasicVehicleStatus()
                            .getRearRightTyrePressure() * 4 / 100.d, Units.BAR));
            saiCiSMARTHandler.updateStatus(ThingStatus.ONLINE);
        } catch (URISyntaxException | ExecutionException | InterruptedException | TimeoutException e) {
            saiCiSMARTHandler.updateStatus(ThingStatus.OFFLINE);
            logger.error("Could not get vehicle data for {}", saiCiSMARTHandler.config.vin, e);
        }
    }
}
