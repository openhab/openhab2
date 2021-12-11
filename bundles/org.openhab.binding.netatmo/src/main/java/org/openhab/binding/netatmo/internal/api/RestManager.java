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
package org.openhab.binding.netatmo.internal.api;

import static org.openhab.binding.netatmo.internal.api.data.NetatmoConstants.*;

import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.netatmo.internal.api.data.NetatmoConstants.FeatureArea;
import org.openhab.binding.netatmo.internal.api.data.NetatmoConstants.Scope;

/**
 * Base class for all various rest managers
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public abstract class RestManager {
    private static final UriBuilder API_BASE_BUILDER = UriBuilder.fromUri(URL_API);
    private static final UriBuilder API_URI_BUILDER = API_BASE_BUILDER.clone().path(PATH_API);
    private static final UriBuilder APP_URI_BUILDER = UriBuilder.fromUri(URL_APP).path(PATH_API);
    protected static final URI OAUTH_URI = API_BASE_BUILDER.clone().path(PATH_OAUTH).build();

    private final Set<Scope> requiredScopes;
    protected final ApiBridge apiBridge;

    public RestManager(ApiBridge apiHandler, FeatureArea features) {
        this.apiBridge = apiHandler;
        this.requiredScopes = features.scopes;
    }

    public <T extends ApiResponse<?>> T get(UriBuilder uriBuilder, Class<T> classOfT) throws NetatmoException {
        return executeUri(uriBuilder, HttpMethod.GET, classOfT, null);
    }

    public <T extends ApiResponse<?>> T post(UriBuilder uriBuilder, Class<T> classOfT, @Nullable String payload)
            throws NetatmoException {
        return executeUri(uriBuilder, HttpMethod.POST, classOfT, payload);
    }

    private <T extends ApiResponse<?>> T executeUri(UriBuilder uriBuilder, HttpMethod method, Class<T> classOfT,
            @Nullable String payload) throws NetatmoException {
        if (ConnectionStatus.SUCCESS.equals(apiBridge.getConnectionStatus()) || requiredScopes.isEmpty()) {
            T response = apiBridge.executeUri(uriBuilder.build(), method, classOfT, payload);
            if (response instanceof ApiResponse.Ok) {
                ApiResponse.Ok okResponse = (ApiResponse.Ok) response;
                if (!okResponse.isSuccess()) {
                    throw new NetatmoException(String.format("Unsuccessful command : %s for uri : %s",
                            response.getStatus(), uriBuilder.build().toString()));
                }
            }
            return response;
        }
        throw new NetatmoException("Request cancelled : API bridge is not connected.");
    }

    private UriBuilder appendParams(UriBuilder builder, @Nullable Object... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("appendParams : params count must be even");
        }
        for (int i = 0; i < params.length && params.length > 0; i += 2) {
            Object query = params[i];
            if (query instanceof String) {
                Object param = params[i + 1];
                if (param != null) {
                    builder.queryParam((String) params[i], param);
                }
            } else {
                throw new IllegalArgumentException("appendParams : even parameters must be Strings");
            }
        }
        return builder;
    }

    protected UriBuilder getApiUriBuilder(String path, @Nullable Object... params) {
        return appendParams(API_URI_BUILDER.clone().path(path), params);
    }

    protected UriBuilder getAppUriBuilder(String path, @Nullable Object... params) {
        return appendParams(APP_URI_BUILDER.clone().path(path), params);
    }

    public Set<Scope> getRequiredScopes() {
        return requiredScopes;
    }
}
