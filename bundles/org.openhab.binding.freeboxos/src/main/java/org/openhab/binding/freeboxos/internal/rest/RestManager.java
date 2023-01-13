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
package org.openhab.binding.freeboxos.internal.rest;

import static org.eclipse.jetty.http.HttpMethod.*;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.freeboxos.internal.api.ApiConstants.Permission;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.MissingPermissionException;
import org.openhab.binding.freeboxos.internal.api.Response;

/**
 * Base class for the various rest managers available through the API
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class RestManager {
    public class GenericResponse extends Response<Object> {
    }

    private final UriBuilder uriBuilder;
    protected final FreeboxOsSession session;

    public RestManager(FreeboxOsSession session, UriBuilder parentUri, String... pathElements) {
        this.uriBuilder = assemblePath(parentUri, pathElements);
        this.session = session;
    }

    public RestManager(FreeboxOsSession session, String... pathElements) {
        this(session, session.getUriBuilder(), pathElements);
    }

    public RestManager(FreeboxOsSession session, Permission required, String... pathElements) throws FreeboxException {
        this(session, pathElements);
        if (required != Permission.NONE && required != Permission.UNKNOWN && !session.hasPermission(required)) {
            throw new MissingPermissionException(required, "Permission missing : %s", required.toString());
        }
    }

    protected UriBuilder getUriBuilder() {
        return uriBuilder.clone();
    }

    private UriBuilder assemblePath(UriBuilder uriBuilder, String... pathElements) {
        for (String path : pathElements) {
            uriBuilder.path(path);
        }
        return uriBuilder;
    }

    private URI buildUri(String... pathElements) {
        return assemblePath(getUriBuilder(), pathElements).build();
    }

    protected <F, T extends Response<List<F>>> List<F> getList(Class<T> clazz, String... pathElements)
            throws FreeboxException {
        List<F> result = session.execute(buildUri(pathElements), GET, clazz, null);
        // GetList may return null object because API does not return anything for empty lists
        return result != null ? result : List.of();
    }

    protected <F, T extends Response<F>> @Nullable F get(Class<T> clazz) throws FreeboxException {
        return session.execute(getUriBuilder().build(), GET, clazz, null);
    }

    protected <F, T extends Response<F>> @Nullable F get(Class<T> clazz, String... pathElements)
            throws FreeboxException {
        return session.execute(buildUri(pathElements), GET, clazz, null);
    }

    protected void post(Object payload, String... pathElements) throws FreeboxException {
        session.execute(buildUri(pathElements), POST, GenericResponse.class, payload);
    }

    protected void post(String... pathElements) throws FreeboxException {
        session.execute(buildUri(pathElements), POST, GenericResponse.class, null);
    }

    protected <F, T extends Response<F>> @Nullable F post(Class<T> clazz, Object payload, String... pathElements)
            throws FreeboxException {
        return session.execute(buildUri(pathElements), POST, clazz, payload);
    }

    protected <F, T extends Response<F>> @Nullable F put(Class<T> clazz, F payload) throws FreeboxException {
        return session.execute(getUriBuilder().build(), PUT, clazz, payload);
    }

    protected <F, T extends Response<F>> @Nullable F put(Class<T> clazz, F payload, String... pathElements)
            throws FreeboxException {
        return session.execute(buildUri(pathElements), PUT, clazz, payload);
    }
}
