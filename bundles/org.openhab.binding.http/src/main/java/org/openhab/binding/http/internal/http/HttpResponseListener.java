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
package org.openhab.binding.http.internal.http;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link HttpResponseListener} is responsible for processing the result of a HTTP request
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class HttpResponseListener extends BufferingResponseListener {
    private final Logger logger = LoggerFactory.getLogger(HttpResponseListener.class);
    private final CompletableFuture<@Nullable Content> future;
    private final String fallbackEncoding;

    public HttpResponseListener(CompletableFuture<@Nullable Content> future) {
        this(future, null);
    }

    public HttpResponseListener(CompletableFuture<@Nullable Content> future, @Nullable String fallbackEncoding) {
        this.future = future;
        this.fallbackEncoding = fallbackEncoding != null ? fallbackEncoding : StandardCharsets.UTF_8.name();
    }

    @Override
    @SuppressWarnings("null")
    public void onComplete(@NonNullByDefault({}) Result result) {
        Response response = result.getResponse();
        if (logger.isTraceEnabled()) {
            logger.trace("Received from '{}': {}", result.getRequest().getURI(), responseToLogString(response));
        }
        if (!result.isFailed() && response.getStatus() == HttpStatus.OK_200) {
            byte[] content = getContent();
            String encoding = getEncoding();
            if (content != null) {
                future.complete(new Content(content, encoding == null ? fallbackEncoding : encoding, getMediaType()));
            } else {
                future.complete(null);
            }
        } else {
            Request request = result.getRequest();
            if (result.isFailed()) {
                logger.warn("Requesting '{}' (method='{}', content='{}') failed: {}", request.getURI(),
                        request.getMethod(), request.getContent(), result.getFailure().getMessage());
                future.complete(null);
            } else if (result.getResponse().getStatus() == HttpStatus.UNAUTHORIZED_401) {
                logger.debug("Requesting '{}' (method='{}', content='{}') failed: Authorization error",
                        request.getURI(), request.getMethod(), request.getContent());
                future.completeExceptionally(new IllegalStateException("Auth Error"));
            }
        }
    }

    private String responseToLogString(Response response) {
        String logString = "Code = {" + response.getStatus() + "}, Headers = {"
                + response.getHeaders().stream().map(HttpField::toString).collect(Collectors.joining(", "))
                + "}, Content = {" + getContentAsString() + "}";
        return logString;
    }
}
