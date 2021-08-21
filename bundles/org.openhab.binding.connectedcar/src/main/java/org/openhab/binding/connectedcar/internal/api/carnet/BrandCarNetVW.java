/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.connectedcar.internal.api.carnet;

import static org.openhab.binding.connectedcar.internal.api.ApiDataTypesDTO.API_BRAND_VW;
import static org.openhab.binding.connectedcar.internal.api.carnet.CarNetApiConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.connectedcar.internal.api.ApiBrandProperties;
import org.openhab.binding.connectedcar.internal.api.ApiEventListener;
import org.openhab.binding.connectedcar.internal.api.ApiException;
import org.openhab.binding.connectedcar.internal.api.ApiHttpClient;
import org.openhab.binding.connectedcar.internal.api.BrandAuthenticator;
import org.openhab.binding.connectedcar.internal.api.IdentityManager;
import org.openhab.binding.connectedcar.internal.api.carnet.CarNetApiGSonDTO.CarNetImageUrlsVW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BrandCarNetVW} provides the VW specific functions of the API
 *
 * @author Markus Michels - Initial contribution
 */
@NonNullByDefault
public class BrandCarNetVW extends CarNetApi implements BrandAuthenticator {
    private final Logger logger = LoggerFactory.getLogger(BrandCarNetVW.class);

    public BrandCarNetVW(ApiHttpClient httpClient, IdentityManager tokenManager,
            @Nullable ApiEventListener eventListener) {
        super(httpClient, tokenManager, eventListener);
    }

    @Override
    public ApiBrandProperties getProperties() {
        ApiBrandProperties properties = new ApiBrandProperties();
        properties.brand = API_BRAND_VW;
        properties.xcountry = "DE";
        properties.clientId = "9496332b-ea03-4091-a224-8c746b885068@apps_vw-dilab_com";
        properties.xClientId = "38761134-34d0-41f3-9a73-c4be88d7d337";
        properties.authScope = "openid profile mbb cars address";
        properties.apiDefaultUrl = CNAPI_DEFAULT_API_URL;
        properties.tokenUrl = CNAPI_VW_TOKEN_URL;
        properties.tokenRefreshUrl = properties.tokenUrl;
        properties.redirect_uri = "carnet://identity-kit/login";
        properties.xrequest = "de.volkswagen.carnet.eu.eremote";
        properties.responseType = "id_token token";
        properties.xappName = "eRemote";
        properties.xappVersion = "5.1.2";
        properties.xappId = "de.volkswagen.car-net.eu.e-remote";
        return properties;
    }

    @Override
    public String updateAuthorizationUrl(String url) throws ApiException {
        return url + "&prompt=login"; // + "&code_challenge=" + codeChallenge + "&code_challenge_method=S256";
    }

    @Override
    public String[] getImageUrls() throws ApiException {
        if (config.vstatus.imageUrls.length == 0) {
            config.vstatus.imageUrls = super.callApi("",
                    "https://vehicle-image.apps.emea.vwapps.io/vehicleimages/exterior/{2}",
                    fillAppHeaders(tokenManager.createProfileToken(config)), "getImageUrls",
                    CarNetImageUrlsVW.class).imageUrls;
        }
        return config.vstatus.imageUrls;
    }
}
