/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.dsmr.internal.device;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.dsmr.internal.TelegramReaderUtil;
import org.openhab.binding.dsmr.internal.device.p1telegram.P1TelegramListener;
import org.openhab.binding.dsmr.internal.device.p1telegram.P1TelegramParser;

/**
 * Test class for the {@link SmartyDecrypter}.
 *
 * @author Hilbrand Bouwkamp - Initial contribution
 */
@NonNullByDefault
public class SmartyDecrypterTest {

    private static final String KEY = "D491470F47126332B07D1923B3504188";
    private static final int[] TELEGRAM = new int[] {
            // Start byte
            0xDB,
            // System title length
            0x08,
            // System title
            0x53, 0x41, 0x47, 0x67, 0x70, 0x01, 0xBD, 0x54,
            // Undocumented byte
            0x82,
            // Length of the subsequent bytes
            0x02, 0xBA,
            // Undocumented byte
            0x30,
            // Frame Counter
            0x00, 0x05, 0xA8, 0xE3,
            // Cypher text
            0x80, 0x6E, 0xE6, 0xE6, 0x39, 0x27, 0x4C, 0x7B, 0xC5, 0x70, 0x95, 0xF8, 0x72, 0xB0, 0x8D, 0xDE, 0x62, 0x1F,
            0xB7, 0x4E, 0xE8, 0x1E, 0x5E, 0xBE, 0x34, 0x2C, 0x93, 0xD8, 0xE7, 0x37, 0x81, 0xFB, 0x2A, 0x1E, 0xB8, 0x71,
            0x00, 0x74, 0xA5, 0x4F, 0xC5, 0x7A, 0xA7, 0xD1, 0xD9, 0x92, 0x36, 0xC4, 0x2E, 0x2E, 0xC0, 0x1A, 0x03, 0x24,
            0xEF, 0xC7, 0xF0, 0x2E, 0x3B, 0xA2, 0xFA, 0x43, 0x19, 0x6C, 0xA6, 0x03, 0x83, 0x8A, 0xB8, 0x32, 0x2D, 0xFF,
            0xA8, 0x3F, 0x7E, 0x83, 0x93, 0x2E, 0x60, 0x07, 0x40, 0x6B, 0x11, 0x2B, 0x41, 0x74, 0x5F, 0x38, 0x80, 0x56,
            0x46, 0xD5, 0x3F, 0xEA, 0x7A, 0x02, 0x9F, 0xA7, 0x9C, 0x36, 0xD9, 0xD7, 0x77, 0xDA, 0x8B, 0x46, 0x3D, 0xD3,
            0x3E, 0xDF, 0xE3, 0x8C, 0xDE, 0xDD, 0xC1, 0x90, 0x7E, 0x48, 0xC0, 0xBE, 0x10, 0x58, 0x22, 0x8B, 0x1F, 0xF1,
            0x6E, 0xDF, 0x9F, 0x6F, 0x14, 0xDB, 0x9C, 0x3D, 0xEC, 0x32, 0x0C, 0x89, 0x8B, 0x30, 0xC7, 0x64, 0x11, 0x8D,
            0xA8, 0x3A, 0x77, 0xBE, 0xFF, 0x44, 0x43, 0x7A, 0xE2, 0x56, 0x16, 0x76, 0xB0, 0x09, 0x0A, 0x65, 0x24, 0x09,
            0x41, 0x9A, 0xA0, 0x1D, 0xB7, 0x8D, 0x95, 0xA1, 0x01, 0x3A, 0xD5, 0x2D, 0x3A, 0x46, 0x52, 0x9B, 0x2C, 0x1A,
            0xA9, 0xD5, 0xAA, 0xB8, 0xAE, 0x54, 0xFD, 0x86, 0x45, 0x6F, 0x23, 0xFE, 0xB2, 0x79, 0xDE, 0x06, 0x88, 0x10,
            0xAF, 0x56, 0xBE, 0x1E, 0x9D, 0xE0, 0x56, 0x93, 0x73, 0x29, 0x18, 0x13, 0x56, 0xB5, 0xA0, 0xF2, 0xBF, 0xE0,
            0xFD, 0x19, 0xD6, 0x85, 0xC8, 0xC2, 0xD8, 0x96, 0x92, 0x31, 0x5D, 0xCD, 0xA8, 0xE5, 0x31, 0xC8, 0xA6, 0x0F,
            0x5B, 0xA1, 0xD4, 0x8C, 0x0C, 0xFC, 0x85, 0xA9, 0xAD, 0xB2, 0xEE, 0xCA, 0x0E, 0x79, 0x56, 0x51, 0x35, 0x83,
            0xCB, 0xA7, 0x56, 0xB6, 0x5A, 0x8E, 0xC6, 0x80, 0x9B, 0xD5, 0xA8, 0xC9, 0xE0, 0xC5, 0xF8, 0x9F, 0xC9, 0x02,
            0xB1, 0x2E, 0xAD, 0xD8, 0xCE, 0x3B, 0x93, 0xAC, 0xE4, 0x5C, 0x36, 0xC0, 0xD1, 0x39, 0x81, 0x65, 0x8E, 0x29,
            0xC2, 0x42, 0xE5, 0x58, 0xA5, 0xCB, 0xE8, 0xBD, 0x33, 0xBB, 0x68, 0x8A, 0x93, 0xE5, 0x4C, 0x03, 0x03, 0xAB,
            0x6F, 0xFC, 0xFB, 0xD5, 0xDC, 0x46, 0xAA, 0xED, 0x53, 0xAC, 0x3D, 0xF6, 0x7A, 0xA4, 0xC4, 0x06, 0xAB, 0xA6,
            0xA3, 0x41, 0xEF, 0x8E, 0xA1, 0x52, 0xAA, 0xCD, 0x7E, 0x10, 0x98, 0x2D, 0x0D, 0x43, 0x8F, 0xBF, 0x08, 0x5E,
            0x48, 0xB0, 0xD4, 0xF8, 0x0B, 0x1F, 0x2A, 0x3F, 0xE9, 0x5B, 0xE1, 0xCE, 0x88, 0x00, 0x62, 0xED, 0xA7, 0xAD,
            0x94, 0x25, 0xC6, 0x05, 0x07, 0x56, 0x0C, 0xFF, 0x45, 0x78, 0x79, 0x8E, 0xD7, 0x65, 0xD3, 0x27, 0x85, 0xB4,
            0xE8, 0xF5, 0xD2, 0xA9, 0x43, 0x4D, 0x99, 0x93, 0xB1, 0x51, 0xCB, 0xF8, 0x74, 0x78, 0x3E, 0x64, 0x4A, 0xB7,
            0x30, 0x60, 0x70, 0x60, 0x8D, 0x68, 0xD2, 0x6D, 0x2B, 0xAE, 0x84, 0x95, 0xA7, 0x03, 0xFA, 0xD4, 0x0B, 0x24,
            0x69, 0xB2, 0xE4, 0xA6, 0x69, 0x12, 0xA6, 0x27, 0x2D, 0x3B, 0x0A, 0xFC, 0x60, 0xA1, 0xFE, 0x38, 0x94, 0x4A,
            0xEA, 0xDF, 0x75, 0x4B, 0xC6, 0xF2, 0x2F, 0x2B, 0x9C, 0x32, 0xD3, 0xD2, 0x1E, 0x34, 0xD8, 0xF7, 0x82, 0x04,
            0x83, 0x03, 0xBC, 0x71, 0x53, 0x6F, 0x0E, 0x54, 0x2C, 0x99, 0x51, 0xCC, 0x75, 0xE3, 0xAA, 0xAB, 0x95, 0x5A,
            0xA3, 0x67, 0xE1, 0x09, 0x0E, 0x40, 0x56, 0x6F, 0x56, 0x1F, 0xEA, 0x24, 0x74, 0xDE, 0x5D, 0x13, 0xF2, 0x13,
            0x16, 0x74, 0x41, 0x06, 0xD1, 0xE1, 0x02, 0x01, 0x0F, 0x16, 0xD5, 0xF2, 0xA6, 0x63, 0x41, 0x0B, 0xBE, 0x4A,
            0x85, 0x8A, 0x5C, 0x32, 0x0B, 0xEC, 0x4C, 0xA4, 0x28, 0x6C, 0xC5, 0x3C, 0x55, 0x5A, 0x57, 0xE6, 0x84, 0x33,
            0x57, 0x14, 0x8E, 0x93, 0xB5, 0x7C, 0x12, 0x86, 0xDD, 0xBB, 0x07, 0xDB, 0xDD, 0x3A, 0x38, 0x1C, 0xD6, 0xBC,
            0x39, 0xA9, 0xA9, 0xF5, 0x12, 0xDF, 0xF6, 0x5E, 0xBD, 0x6E, 0xB5, 0x4B, 0xD5, 0x54, 0xEA, 0xA3, 0x46, 0xB4,
            0x96, 0xD6, 0xAD, 0x58, 0x45, 0x1B, 0xB2, 0x9B, 0x71, 0xD7, 0xBD, 0x5E, 0xE1, 0xF1, 0x06, 0x99, 0xAF, 0x0B,
            0x75, 0x4D, 0x41, 0x05, 0x6E, 0x43, 0x76, 0xC2, 0x21, 0x14, 0x37, 0x4E, 0x84, 0xCC, 0x08, 0xAD, 0x2B, 0x04,
            0xF6, 0xF5, 0xC4, 0x9D, 0x6B, 0xF7, 0xF9, 0x2C, 0x6E, 0x6C, 0x26, 0x5B, 0x4B, 0x55, 0xB7, 0x04, 0xD3, 0x68,
            0x21, 0xDC, 0x19, 0x2F, 0x3A, 0x90, 0x1C, 0x3E, 0x7C, 0x07, 0x16, 0x05, 0x53, 0x70, 0xCE, 0x9A, 0xD1, 0x45,
            0x78, 0x39, 0x3B, 0x43, 0xF5, 0x59, 0x80, 0x24, 0xE3, 0x7D, 0x2B, 0x8C, 0x0A, 0xE1, 0xEC, 0xCF, 0xEA, 0x99,
            0xB9, 0xE3, 0x57, 0xCD, 0xDA, 0xF1, 0x98, 0x8D, 0xCD, 0x6E, 0xE9, 0x57, 0xDE, 0x50, 0x6C, 0x8A, 0x15, 0xB8,
            0xE8, 0x72, 0xC4, 0x1D, 0xD3, 0xB2, 0x9F, 0x0B, 0x8E, 0x40, 0x15, 0xD4, 0xCC, 0xAD, 0x9C,
            // GCM Tag (12 byte)
            0xE6, 0x8A, 0xEC, 0x23, 0x21, 0x1C, 0x87, 0x60, 0x78, 0xAE, 0x3F, 0x03 };

    /**
     * Tests decrypting of a single complete Smarty telegram.
     */
    @Test
    public void testSmartyDecrypter() {
        final AtomicReference<String> telegramResult = new AtomicReference<>("");
        final P1TelegramListener telegramListener = telegram -> telegramResult.set(telegram.getRawTelegram());
        final SmartyDecrypter decoder = new SmartyDecrypter(new P1TelegramParser(telegramListener),
                new DSMRTelegramListener(KEY, ""), KEY, "");
        decoder.setLenientMode(true);
        final byte[] data = new byte[TELEGRAM.length];

        for (int i = 0; i < TELEGRAM.length; i++) {
            data[i] = (byte) TELEGRAM[i];
        }

        decoder.parse(data, data.length);
        final String expected = new String(TelegramReaderUtil.readRawTelegram("smarty_long"), StandardCharsets.UTF_8);

        assertThat("Should have correctly decrypted the telegram", telegramResult.get(), is(equalTo(expected)));
    }
}
