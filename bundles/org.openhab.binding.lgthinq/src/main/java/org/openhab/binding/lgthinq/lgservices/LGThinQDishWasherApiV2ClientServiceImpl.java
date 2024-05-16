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
package org.openhab.binding.lgthinq.lgservices;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.lgthinq.internal.errors.LGThinqApiException;
import org.openhab.binding.lgthinq.lgservices.model.DevicePowerState;
import org.openhab.binding.lgthinq.lgservices.model.devices.dishwasher.DishWasherCapability;
import org.openhab.binding.lgthinq.lgservices.model.devices.dishwasher.DishWasherSnapshot;

/**
 * The {@link LGThinQDishWasherApiV2ClientServiceImpl}
 *
 * @author Nemer Daud - Initial contribution
 */
@NonNullByDefault
public class LGThinQDishWasherApiV2ClientServiceImpl
        extends LGThinQAbstractApiV2ClientService<DishWasherCapability, DishWasherSnapshot>
        implements LGThinQDishWasherApiClientService {

    protected LGThinQDishWasherApiV2ClientServiceImpl(HttpClient httpClient) {
        super(DishWasherCapability.class, DishWasherSnapshot.class, httpClient);
    }

    @Override
    protected void beforeGetDataDevice(@NonNull String bridgeName, @NonNull String deviceId) {
        // TODO - Analise what to do here
    }

    @Override
    public void turnDevicePower(String bridgeName, String deviceId, DevicePowerState newPowerState)
            throws LGThinqApiException {
        throw new UnsupportedOperationException("Unsupported for this device");
    }

    @Override
    public void remoteStart(String bridgeName, DishWasherCapability cap, String deviceId, Map<String, Object> data)
            throws LGThinqApiException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void wakeUp(String bridgeName, String deviceId, Boolean wakeUp) throws LGThinqApiException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
