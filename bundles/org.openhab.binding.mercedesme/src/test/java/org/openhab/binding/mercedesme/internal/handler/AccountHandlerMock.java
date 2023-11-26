/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.mercedesme.internal.handler;

import static org.mockito.Mockito.mock;

import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.openhab.binding.mercedesme.internal.config.AccountConfiguration;
import org.openhab.binding.mercedesme.internal.discovery.MercedesMeDiscoveryService;
import org.openhab.core.i18n.LocaleProvider;
import org.openhab.core.storage.StorageService;
import org.openhab.core.thing.Bridge;

import com.daimler.mbcarkit.proto.Client.ClientMessage;

/**
 * {@link AccountHandlerMock} to retrieve and collect commands from {@link VehicleHandler}
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class AccountHandlerMock extends AccountHandler {
    private static LocaleProvider localeProvider = new LocaleProvider() {

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }
    };

    JSONObject command = new JSONObject();

    public AccountHandlerMock() {
        super(mock(Bridge.class), mock(MercedesMeDiscoveryService.class), mock(HttpClient.class),
                mock(LocaleProvider.class), mock(StorageService.class));
    }

    public AccountHandlerMock(Bridge b, @Nullable String storedObject) {
        super(b, mock(MercedesMeDiscoveryService.class), mock(HttpClient.class), localeProvider,
                new StorageServiceMock<String>(storedObject));
    }

    @Override
    public void registerVin(String vin, VehicleHandler handler) {
    }

    @Override
    public void getVehicleCapabilities(String vin) {
    }

    @Override
    public void sendCommand(@Nullable ClientMessage cm) {
        // System.out.println(cm.getAllFields());
        if (cm != null) {
            command = ProtoConverter.clientMessage2Json(cm);
        }
    }

    public AccountConfiguration getConfigT() {
        return new AccountConfiguration();
    }

    public JSONObject getCommand() {
        return command;
    }

    public void connect() {
        super.ws.onConnect(mock(Session.class));
    }
}
