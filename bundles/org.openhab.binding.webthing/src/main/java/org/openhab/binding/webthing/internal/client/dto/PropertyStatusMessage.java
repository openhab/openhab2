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
package org.openhab.binding.webthing.internal.client.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Map;

/**
 * Web Thing WebSocket API property status message. Refer https://iot.mozilla.org/wot/#propertystatus-message
 *
 * @author Gregor Roth - Initial contribution
 */
@NonNullByDefault
public class PropertyStatusMessage {

    public @Nullable String messageType;

    public @Nullable Map<String, Object> data = Map.of();

    @Override
    public String toString() {
        return "PropertyStatusMessage{" +
                "messageType='" + messageType + '\'' +
                ", data=" + data +
                '}';
    }
}
