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
package org.openhab.binding.paradoxalarm.internal.communication.crypto;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.paradoxalarm.internal.util.ParadoxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to encrypt and decrypt communication from/to Paradox system. Singleton pattern.
 *
 * Paradox encryption is using Rijndael 256-key expansion alghoritm.
 * When key is changed the updateKey(byte[]) method needs to be called in front.
 * Encrypt and Decrypt methods use the expandedKey field to do their job.
 *
 * The first packet sent to Paradox is the IP150 password as bytes, extended to 32 bytes with 0xEE.
 * The first response contains the key that will be used for the rest of communication.
 *
 * Most of the coding is copy from Python / rewrite in Java from second link of jpbaracca's PAI repository. Probably
 * some of the variables can be named better but I don't understand this code in it's full scope so I preferred to keep
 * it as it is.
 *
 * @author Konstantin Polihronov - Initial contribution
 *
 * @see <a href=https://www.samiam.org/key-schedule.html>Sam Trendholme's page about AES</a>
 * @see <a href=https://github.com/ParadoxAlarmInterface/pai>Github of jpbaracca's work - ParadoxAlarmInterface in
 *      python</a>
 */
@NonNullByDefault
public class EncryptionHandler {

    private final Logger logger = LoggerFactory.getLogger(EncryptionHandler.class);

    private static final int KEY_ARRAY_LENGTH = 32;
    private static final int TABLE_SIZE = 256;
    private static final int KEY_LENGTH = 240;
    private static final int PAYLOAD_RATE_LENGTH = 16;

    private static EncryptionHandler instance = new EncryptionHandler();

    private int[] lTable = new int[TABLE_SIZE];
    private int[] aTable = new int[TABLE_SIZE];

    private int[] expandedKey = new int[KEY_LENGTH];

    private EncryptionHandler() {
        generateTables();
    }

    public static EncryptionHandler getInstance() {
        return instance;
    }

    public byte[] encrypt(byte[] payload) {
        if (payload.length % 16 != 0) {
            payload = ParadoxUtil.extendArray(payload, PAYLOAD_RATE_LENGTH);
            printArray("Array had to be extended:", payload);
            logger.trace("New payload length={}", payload.length);
        }

        int[] payloadAsIntArray = ParadoxUtil.toIntArray(payload);

        final int[] s = EncryptionHandlerConstants.S;
        byte[] result = new byte[0];
        for (int i = 0, rounds = 14; i < payloadAsIntArray.length / 16; i++) {
            int[] a = Arrays.copyOfRange(payloadAsIntArray, i * 16, (i + 1) * 16);
            keyAddition(a, Arrays.copyOfRange(expandedKey, 0, 16));

            for (int r = 1; r <= rounds; r++) {
                sBox(a, s);
                shiftRow(a, 0);
                if (r != rounds) {
                    mixColumn(a);
                }
                keyAddition(a, Arrays.copyOfRange(expandedKey, r * 16, (r + 1) * 16));
            }

            result = ParadoxUtil.mergeByteArrays(result, ParadoxUtil.toByteArray(a));
        }

        printArray("Encrypted array", result);
        return result;
    }

    public byte[] decrypt(byte[] payload) {
        int[] payloadAsIntArray = ParadoxUtil.toIntArray(payload);

        final int[] si = EncryptionHandlerConstants.Si;
        byte[] result = new byte[0];
        for (int i = 0, rounds = 14; i < payloadAsIntArray.length / 16; i++) {
            int[] a = Arrays.copyOfRange(payloadAsIntArray, i * 16, (i + 1) * 16);

            for (int r = rounds; r > 0; r--) {
                keyAddition(a, Arrays.copyOfRange(expandedKey, r * 16, (r + 1) * 16));
                if (r != rounds) {
                    invMixColumn(a);
                }
                sBox(a, si);
                shiftRow(a, 1);
            }

            keyAddition(a, expandedKey);

            result = ParadoxUtil.mergeByteArrays(result, ParadoxUtil.toByteArray(a));
        }

        printArray("Decrypted array", result);
        return result;
    }

    private void printArray(String description, byte[] array) {
        ParadoxUtil.printByteArray(description, array, array.length);
    }

    private byte[] fillArray(byte[] keyBytes) {
        byte[] byteArray = new byte[keyBytes.length];
        for (int i = 0; i < keyBytes.length; i++) {
            byteArray[i] = (byte) (keyBytes[i] & 0xFF);
        }

        byte[] expandedArray = ParadoxUtil.extendArray(byteArray, KEY_ARRAY_LENGTH);
        return expandedArray;
    }

    public synchronized void updateKey(byte[] newKey) {
        expandedKey = new int[KEY_LENGTH];
        expandKey(newKey);
    }

    private void expandKey(byte[] input) {
        // fill array to 32th byte with 0xEE
        byte[] filledArray = fillArray(input);

        int[] temp = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expandedKey[j * 4 + i] = filledArray[i * 4 + j] & 0xFF;
            }
            for (int j = 0; j < 4; j++) {
                expandedKey[j * 4 + i + 16] = filledArray[i * 4 + j + 16] & 0xFF;
            }
        }

        final int[] s = EncryptionHandlerConstants.S;
        for (int i = 8; i < 60; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = expandedKey[(((i - 1) & 0xfc) << 2) + ((i - 1) & 0x03) + j * 4];
            }

            if (i % 4 == 0) {
                for (int j = 0; j < 4; j++) {
                    temp[j] = s[temp[j]];
                }
            }

            if (i % 8 == 0) {
                int tmp = temp[0];

                for (int j = 1; j < 4; j++) {
                    temp[j - 1] = temp[j];
                }

                temp[3] = tmp;
                temp[0] ^= EncryptionHandlerConstants.RCON[(i / 8 - 1)];
            }

            for (int j = 0; j < 4; j++) {
                expandedKey[((i & 0xfc) << 2) + (i & 0x03)
                        + j * 4] = expandedKey[(((i - 8) & 0xfc) << 2) + ((i - 8) & 0x03) + j * 4] ^ temp[j];
            }
        }
    }

    private int gmul(int c, int b) {
        int s = lTable[c] + lTable[b];
        s %= 255;
        s = aTable[s];
        if (b == 0 || c == 0) {
            s = 0;
        }
        return s;
    }

    private void generateTables() {
        int a = 1;
        int d;
        for (int index = 0; index < 255; index++) {
            aTable[index] = a & 0xFF;
            /* Multiply by three */
            d = (a & 0x80) & 0xFF;
            a <<= 1;
            if (d == 0x80) {
                a ^= 0x1b;
                a &= 0xFF;
            }
            a ^= aTable[index];
            a &= 0xFF;
            /* Set the log table value */
            lTable[aTable[index]] = index & 0xFF;
        }
        aTable[255] = aTable[0];
        lTable[0] = 0;
    }

    private void sBox(int[] a, int[] box) {
        for (int i = 0; i < 16; i++) {
            a[i] = box[a[i]];
        }
    }

    private void mixColumn(int[] a) {
        final int[] xtimetbl = EncryptionHandlerConstants.XTIMETABLE;

        int[] b = new int[] { 0, 0, 0, 0 };
        for (int j = 0; j < 4; j++) {
            int tmp = a[j] ^ a[j + 4] ^ a[j + 8] ^ a[j + 12];
            for (int i = 0; i < 4; i++) {
                b[i] = a[i * 4 + j];
            }
            b[0] ^= xtimetbl[a[j] ^ a[j + 4]] ^ tmp;
            b[1] ^= xtimetbl[a[j + 4] ^ a[j + 8]] ^ tmp;
            b[2] ^= xtimetbl[a[j + 8] ^ a[j + 12]] ^ tmp;
            b[3] ^= xtimetbl[a[j + 12] ^ a[j]] ^ tmp;

            for (int i = 0; i < 4; i++) {
                a[i * 4 + j] = b[i];
            }
        }
    }

    private void invMixColumn(int[] a) {
        int[][] b = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                b[i][j] = gmul(0xe, a[i * 4 + j]) ^ gmul(0xb, a[((i + 1) % 4) * 4 + j])
                        ^ gmul(0xd, a[((i + 2) % 4) * 4 + j]) ^ gmul(0x9, a[((i + 3) % 4) * 4 + j]);
            }
        }

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                a[i * 4 + j] = b[i][j];
            }
        }
    }

    private void shiftRow(int[] a, int d) {
        int[] tmpArray = new int[] { 0, 0, 0, 0 };
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[][][] shifts = EncryptionHandlerConstants.SHIFTS;
                int index = i * 4 + (j + shifts[0][i][d]) % 4;
                tmpArray[j] = a[index];
            }
            for (int j = 0; j < 4; j++) {
                a[i * 4 + j] = tmpArray[j];
            }
        }
    }

    private void keyAddition(int[] a, int[] rk) {
        for (int i = 0; i < 16; i++) {
            a[i] ^= rk[i];
        }
    }
}
