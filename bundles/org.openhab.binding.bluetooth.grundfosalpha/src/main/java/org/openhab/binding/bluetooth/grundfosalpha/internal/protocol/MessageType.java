/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.bluetooth.grundfosalpha.internal.protocol;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * This represents the different types of messages that can be requested and received.
 *
 * @author Jacob Laursen - Initial contribution
 */
@NonNullByDefault
public enum MessageType {
    FlowHead(new byte[] { 0x1f, 0x00, 0x01, 0x30, 0x01, 0x00, 0x00, 0x18 },
            new byte[] { (byte) 0x27, (byte) 0x07, (byte) 0xe7, (byte) 0xf8, (byte) 0x0a, (byte) 0x03, (byte) 0x5d,
                    (byte) 0x01, (byte) 0x21, (byte) 0x52, (byte) 0x1f }),
    Power(new byte[] { 0x2c, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x25 },
            new byte[] { (byte) 0x27, (byte) 0x07, (byte) 0xe7, (byte) 0xf8, (byte) 0x0a, (byte) 0x03, (byte) 0x57,
                    (byte) 0x00, (byte) 0x45, (byte) 0x8a, (byte) 0xcd });

    private final byte[] identifier;
    private final byte[] request;

    MessageType(byte[] identifier, byte[] request) {
        this.identifier = identifier;
        this.request = request;
    }

    public byte[] identifier() {
        return identifier;
    }

    public byte[] request() {
        return request;
    }
}
