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
package org.openhab.binding.opensprinkler.internal.api;

import org.openhab.binding.opensprinkler.internal.api.exception.CommunicationApiException;
import org.openhab.binding.opensprinkler.internal.api.exception.GeneralApiException;

/**
 * The {@link OpenSprinklerApiFactory} class is used for creating instances of
 * the OpenSprinkler API classes to interact with the OpenSprinklers HTTP or
 * GPIO API's.
 *
 * @author Chris Graham - Initial contribution
 */
public class OpenSprinklerApiFactory {

    /**
     * Factory method used to determine what version of the API is in use at the
     * OpenSprinkler API and return the proper class for control of the device.
     *
     * @param hostname Hostname or IP address as a String of the OpenSprinkler device.
     * @param port The port number the OpenSprinkler API is listening on.
     * @param password Admin password for the OpenSprinkler device.
     * @param basicUsername Used when basic auth is required
     * @param basicPassword Used when basic auth is required
     * @return OpenSprinkler HTTP API class for control of the device.
     * @throws Exception
     */
    public static OpenSprinklerApi getHttpApi(String hostname, int port, String password, String basicUsername,
            String basicPassword) throws CommunicationApiException, GeneralApiException {
        int version = -1;

        OpenSprinklerApi lowestSupportedApi = new OpenSprinklerHttpApiV100(hostname, port, password, basicUsername,
                basicPassword);
        try {
            version = lowestSupportedApi.getFirmwareVersion();
        } catch (Exception exp) {
            throw new CommunicationApiException(
                    "There was a problem in the HTTP communication with the OpenSprinkler API: " + exp.getMessage());
        }

        if (version >= 210 && version < 213) {
            return new OpenSprinklerHttpApiV210(hostname, port, password, basicUsername, basicPassword);
        } else if (version >= 213) {
            return new OpenSprinklerHttpApiV213(hostname, port, password, basicUsername, basicPassword);
        } else {
            /* Need to make sure we have an older OpenSprinkler device by checking the first station. */
            try {
                lowestSupportedApi.isStationOpen(0);
            } catch (Exception exp) {
                throw new CommunicationApiException(
                        "There was a problem in the HTTP communication with the OpenSprinkler API: "
                                + exp.getMessage());
            }

            return lowestSupportedApi;
        }
    }

    /**
     * Factory method returns an OpenSprnkler PI GPIO class for control.
     *
     * @param numberOfStations The number of stations to control on the OpenSprinkler PI device.
     * @return OpenSprinkler GPIO class for control of the device.
     */
    public static OpenSprinklerApi getGpioApi(int numberOfStations) {
        return new OpenSprinklerGpioApi(numberOfStations);
    }
}
