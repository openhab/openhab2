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
package org.openhab.binding.salus.internal.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Martin Grześlowski - Initial contribution
 */
@NonNullByDefault
public interface RestClient {
    /**
     * GET request to server
     * 
     * @param url to send request
     * @param headers to send
     * @return response from server
     */
    Response<@Nullable String> get(String url, @Nullable Header... headers)
            throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * POST request to server
     * 
     * @param url to send request
     * @param headers to send
     * @return response from server
     */
    Response<@Nullable String> post(String url, Content content, @Nullable Header... headers)
            throws ExecutionException, InterruptedException, TimeoutException;

    record Content(String body, String type) {
        public Content(String body) {
            this(body, "application/json");
        }
    }

    record Header(String name, List<String> values) {
        public Header(String name, String value) {
            this(name, List.of(value));
        }
    }

    record Response<T> (int statusCode, T body) {
        public <Y> Response<Y> map(Function<T, Y> mapper) {
            return new Response<>(statusCode, mapper.apply(body));
        }
    }
}
