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
package org.openhab.binding.bluetooth.daikinmadoka.internal;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.bluetooth.daikinmadoka.internal.model.commands.ResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author blafois
 *
 */
@NonNullByDefault
public class BRC1HUartProcessor {

    /**
     * In the unlikely event of messages arrive in wrong order, this comparator will sort the queue
     */
    private Comparator<byte[]> chunkSorter = (byte[] m1, byte[] m2) -> m1[0] - m2[0];

    private final Logger logger = LoggerFactory.getLogger(BRC1HUartProcessor.class);

    private ConcurrentSkipListSet<byte[]> uartMessages = new ConcurrentSkipListSet<>(chunkSorter);

    private ResponseListener responseListener;

    public BRC1HUartProcessor(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    /**
     *
     * @return
     */
    private boolean isMessageComplete() {

        int messagesInQueue = this.uartMessages.size();

        if (messagesInQueue <= 0) {
            return false;
        }

        byte[] firstMessageInQueue = uartMessages.first();

        if (firstMessageInQueue.length < 2) {
            return false;
        }

        int expectedChunks = (int) Math.ceil(firstMessageInQueue[1] / 19.0);
        if (expectedChunks != messagesInQueue) {
            return false;
        }

        // Check that we have every single ID
        int expected = 0;
        for (byte[] m : this.uartMessages) {
            if (m.length < 2) {
                return false;
            }

            if (m[0] != expected++) {
                return false;
            }
        }
        return true;
    }

    public void chunkReceived(byte[] byteValue) {

        this.uartMessages.add(byteValue);
        if (isMessageComplete()) {

            // Beyond this point, full message received
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            for (byte[] msg : uartMessages) {
                try {
                    bos.write(Arrays.copyOfRange(msg, 1, msg.length));
                } catch (Exception e) {
                    // should never happen.
                    logger.info("An unexpected error occured while re-assembling message chunks", e);
                }
            }

            this.uartMessages.clear();

            this.responseListener.receivedResponse(bos.toByteArray());
        }
    }

    public void abandon() {
        this.uartMessages.clear();
    }

}
