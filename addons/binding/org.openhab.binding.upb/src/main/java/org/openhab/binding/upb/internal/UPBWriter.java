/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upb.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.upb.internal.UPBReader.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to write data to the UPB modem.
 *
 * @author Chris Van Orman
 * @since 1.9.0
 */
public class UPBWriter {

    /**
     * Time in milliseconds to wait for an ACK from the modem after writing a
     * message.
     */
    private static long ACK_TIMEOUT = 1000;

    private final Logger logger = LoggerFactory.getLogger(UPBWriter.class);

    /**
     * Asynchronous queue for writing data to the UPB modem.
     */
    private ExecutorService executor = new ThreadPoolExecutor(0, 1, 1000, TimeUnit.MILLISECONDS,
            new PriorityBlockingQueue<Runnable>());

    /**
     * The UPB modem's OutputStream.
     */
    private OutputStream outputStream;

    /**
     * UPBReader that is monitoring the modem's InputStream.
     */
    private UPBReader upbReader;

    /**
     * Instantiates a new {@link UPBWriter} using the given modem
     * {@link OutputStream}.
     *
     * @param outputStream
     *            the {@link OutputStream} from the UPB modem.
     * @param upbReader
     *            the {@link UPBReader} that is monitoring the same UPB modem.
     */
    public UPBWriter(OutputStream outputStream, UPBReader upbReader) {
        this.outputStream = outputStream;
        this.upbReader = upbReader;
    }

    /**
     * Queues a message to be written to the modem.
     *
     * @param message
     *            the message to be written.
     */
    public void queueMessage(MessageBuilder message) {
        String data = message.build();
        logger.debug("Queueing message {}.", data);
        executor.execute(new FutureMessage(new Message(data.getBytes(), message.getPriority())));
    }

    /**
     * Cancels all queued messages and releases resources. This instance cannot
     * be used again and a new {@link UPBWriter} must be instantiated after
     * calling this method.
     */
    public void shutdown() {
        executor.shutdownNow();

        try {
            outputStream.close();
        } catch (IOException e) {
        }
        logger.debug("UPBWriter shutdown");
    }

    private static class FutureMessage extends FutureTask<FutureMessage> implements Comparable<FutureMessage> {
        private Message message;

        public FutureMessage(Message message) {
            super(message, null);

            this.message = message;
        }

        @Override
        public int compareTo(FutureMessage o) {
            return message.priority.getValue() - o.message.priority.getValue();
        }
    }

    /**
     * {@link Runnable} implementation used to write data to the UPB modem.
     *
     * @author Chris Van Orman
     *
     */
    private class Message implements Runnable, Listener {

        private boolean waitingOnAck = true;
        private boolean ackReceived = false;

        private byte[] data;
        private UPBMessage.Priority priority;

        private Message(byte[] data, UPBMessage.Priority priority) {
            this.data = data;

            this.priority = priority;
        }

        private synchronized void ackReceived(boolean ack) {
            waitingOnAck = false;
            ackReceived = ack;
            notify();
        }

        private synchronized boolean waitForAck(int retryCount) {
            long start = System.currentTimeMillis();
            while (waitingOnAck && (System.currentTimeMillis() - start) < ACK_TIMEOUT) {
                try {
                    wait(ACK_TIMEOUT);
                } catch (InterruptedException e) {

                }

                if (!waitingOnAck) {
                    if (ackReceived) {
                        logger.debug("Message {} ack received.", new String(data));
                    } else {
                        logger.debug("Message {} not ack'd.", new String(data));
                    }
                } else {
                    logger.debug("Message {} ack timed out.", new String(data));
                }
            }

            return ackReceived || retryCount == 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void messageReceived(UPBMessage message) {
            switch (message.getType()) {
                case BUSY:
                case NAK:
                    ackReceived(false);
                    break;
                case ACK:
                    ackReceived(true);
                    break;
                default:
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                upbReader.addListener(this);
                int retryCount = 3;
                do {
                    ackReceived = false;
                    waitingOnAck = true;
                    logger.debug("Writing bytes: {}", new String(data));
                    outputStream.write(0x14);
                    outputStream.write(data);
                    outputStream.write(0x0d);
                } while (!waitForAck(retryCount--));

                if (priority.getDelay() > 0) {
                    Thread.sleep(priority.getDelay());
                }
            } catch (Exception e) {
                logger.debug("Error writing message.", e);
            } finally {
                upbReader.removeListener(this);
            }
        }
    }
}
