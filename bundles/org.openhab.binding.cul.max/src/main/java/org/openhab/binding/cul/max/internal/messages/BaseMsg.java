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
package org.openhab.binding.cul.max.internal.messages;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.cul.max.internal.message.sequencers.MessageSequencer;
import org.openhab.binding.cul.max.internal.messages.constants.MaxCulMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Message Class acts as the parent to messages sent and received by the
 * CUL device to communicate with the Max! devices.
 *
 * @author Paul Hampson (cyclingengineer) - Initial contribution
 * @author Johannes Goehr (johgoe) - Migration to OpenHab 3.0
 * @since 1.6.0
 */
@NonNullByDefault
public class BaseMsg {
    public byte len;
    public byte msgCount;

    public byte msgFlag;
    public byte msgTypeRaw;
    public MaxCulMsgType msgType;
    public byte[] srcAddr = new byte[3];
    public String srcAddrStr;
    public byte[] dstAddr = new byte[3];
    public String dstAddrStr;
    public byte groupid;
    public byte[] payload = new byte[0];
    public String rawMsg;

    private boolean fastSend = false;

    private boolean flgReadyToSend = false;

    private final Logger logger = LoggerFactory.getLogger(BaseMsg.class);

    /* define number of characters per byte */
    private static final int PKT_CHARS_PER_BYTE = 2;

    /* packet structure in terms of character representations of bytes */
    private static final int PKT_POS_MSG_LEN = 1; /* Account for 'Z' at start */
    private static final int PKT_POS_MSG_LEN_LEN = PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_MSG_START = PKT_POS_MSG_LEN + PKT_POS_MSG_LEN_LEN;

    private static final int PKT_POS_MSG_COUNT = PKT_POS_MSG_LEN + PKT_POS_MSG_LEN_LEN;
    private static final int PKT_POS_MSG_COUNT_LEN = PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_MSG_FLAG = PKT_POS_MSG_COUNT + PKT_POS_MSG_COUNT_LEN;
    private static final int PKT_POS_MSG_FLAG_LEN = PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_MSG_TYPE = PKT_POS_MSG_FLAG + PKT_POS_MSG_FLAG_LEN;
    private static final int PKT_POS_MSG_TYPE_LEN = PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_SRC_ADDR = PKT_POS_MSG_TYPE + PKT_POS_MSG_TYPE_LEN;
    private static final int PKT_POS_SRC_ADDR_LEN = 3 * PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_DST_ADDR = PKT_POS_SRC_ADDR + PKT_POS_SRC_ADDR_LEN;
    private static final int PKT_POS_DST_ADDR_LEN = 3 * PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_GROUP_ID = PKT_POS_DST_ADDR + PKT_POS_DST_ADDR_LEN;
    private static final int PKT_POS_GROUP_ID_LEN = PKT_CHARS_PER_BYTE;

    private static final int PKT_POS_PAYLOAD_START = PKT_POS_GROUP_ID + PKT_POS_GROUP_ID_LEN;

    private @Nullable MessageSequencer msgSequencer = null;

    /**
     * Constructor based on received message
     *
     * @param rawMsg
     */
    public BaseMsg(String rawMsg) throws MaxCulProtocolException {
        this.rawMsg = rawMsg;

        len = (byte) (Integer.parseInt(rawMsg.substring(1, 3), 16) & 0xFF); /*
                                                                             * length
                                                                             * of
                                                                             * packet
                                                                             */
        if (len != ((rawMsg.length() - 5) / 2)) /*
                                                 * -5 => 'Z' and len byte (2
                                                 * chars) and checksum at
                                                 * end?, div by two as it is
                                                 * a hex string
                                                 */ {
            logger.error("Unable to process packet {}. Length is not correct.", rawMsg);
            len = 0; /* indicate not a valid packet */
            throw new MaxCulProtocolException("Unable to process packet. Length is not correct.");
        }

        msgCount = (byte) (Integer
                .parseInt(rawMsg.substring(PKT_POS_MSG_COUNT, PKT_POS_MSG_COUNT + PKT_POS_MSG_COUNT_LEN), 16) & 0xFF);

        msgFlag = (byte) (Integer.parseInt(rawMsg.substring(PKT_POS_MSG_FLAG, PKT_POS_MSG_FLAG + PKT_POS_MSG_FLAG_LEN),
                16) & 0xFF);

        msgTypeRaw = (byte) (Integer
                .parseInt(rawMsg.substring(PKT_POS_MSG_TYPE, PKT_POS_MSG_TYPE + PKT_POS_MSG_TYPE_LEN), 16) & 0xFF);
        msgType = MaxCulMsgType.fromByte(msgTypeRaw);

        srcAddrStr = rawMsg.substring(PKT_POS_SRC_ADDR, PKT_POS_SRC_ADDR + PKT_POS_SRC_ADDR_LEN);
        for (int idx = PKT_POS_SRC_ADDR; idx < PKT_POS_SRC_ADDR + PKT_POS_SRC_ADDR_LEN; idx += PKT_CHARS_PER_BYTE) {
            srcAddr[(idx - PKT_POS_SRC_ADDR) / PKT_CHARS_PER_BYTE] = (byte) (Integer
                    .parseInt(rawMsg.substring(idx, idx + PKT_CHARS_PER_BYTE), 16) & 0xFF);
        }

        dstAddrStr = rawMsg.substring(PKT_POS_DST_ADDR, PKT_POS_DST_ADDR + PKT_POS_DST_ADDR_LEN);
        for (int idx = PKT_POS_DST_ADDR; idx < PKT_POS_DST_ADDR + PKT_POS_DST_ADDR_LEN; idx += PKT_CHARS_PER_BYTE) {
            dstAddr[(idx - PKT_POS_DST_ADDR) / PKT_CHARS_PER_BYTE] = (byte) (Integer
                    .parseInt(rawMsg.substring(idx, idx + PKT_CHARS_PER_BYTE), 16) & 0xFF);
        }

        groupid = (byte) (Integer.parseInt(rawMsg.substring(PKT_POS_GROUP_ID, PKT_POS_GROUP_ID + PKT_POS_GROUP_ID_LEN),
                16) & 0xFF);

        /*
         * pkt.len accounts for message only (i.e. not first 3 chars) - so
         * offset for characters that precede the message
         */
        int payloadStrLen = ((len) * PKT_CHARS_PER_BYTE) + PKT_POS_MSG_START - PKT_POS_PAYLOAD_START;
        int payloadByteLen = payloadStrLen / PKT_CHARS_PER_BYTE;
        payload = new byte[payloadByteLen];
        for (int payIdx = PKT_POS_PAYLOAD_START; payIdx < (PKT_POS_PAYLOAD_START
                + payloadStrLen); payIdx += PKT_CHARS_PER_BYTE) {
            payload[(payIdx - PKT_POS_PAYLOAD_START) / PKT_CHARS_PER_BYTE] = (byte) (Integer
                    .parseInt(rawMsg.substring(payIdx, payIdx + PKT_CHARS_PER_BYTE), 16) & 0xFF);
        }
    }

    /**
     * This constructor creates an incomplete raw message ready for sending.
     * Payload must be set before sending.
     *
     * @param msgCount
     *            Message Counter
     * @param msgFlag
     *            Message flag
     * @param msgType
     *            the message type
     * @param groupId
     *            Group ID
     * @param srcAddr
     *            Source address of controller
     * @param dstAddr
     *            Dest addr of device
     */
    public BaseMsg(byte msgCount, byte msgFlag, MaxCulMsgType msgType, byte groupId, String srcAddr, String dstAddr) {
        StringBuilder sb = new StringBuilder();

        this.msgCount = msgCount;
        sb.append(String.format("%02x", msgCount).toUpperCase());

        this.msgFlag = msgFlag;
        sb.append(String.format("%02x", msgFlag).toUpperCase());

        this.msgType = msgType;
        this.msgTypeRaw = this.msgType.toByte();
        sb.append(String.format("%02x", this.msgTypeRaw).toUpperCase());

        this.srcAddrStr = srcAddr;
        this.srcAddr[0] = (byte) (Integer.parseInt(srcAddr.substring(0, 2), 16) & 0xFF);
        this.srcAddr[1] = (byte) (Integer.parseInt(srcAddr.substring(2, 4), 16) & 0xFF);
        this.srcAddr[2] = (byte) (Integer.parseInt(srcAddr.substring(4, 6), 16) & 0xFF);
        sb.append(srcAddr.toUpperCase());

        this.dstAddrStr = dstAddr;
        this.dstAddr[0] = (byte) (Integer.parseInt(dstAddr.substring(0, 2), 16) & 0xFF);
        this.dstAddr[1] = (byte) (Integer.parseInt(dstAddr.substring(2, 4), 16) & 0xFF);
        this.dstAddr[2] = (byte) (Integer.parseInt(dstAddr.substring(4, 6), 16) & 0xFF);
        sb.append(dstAddr.toUpperCase());

        this.groupid = groupId;
        sb.append(String.format("%02x", this.groupid).toUpperCase());

        this.rawMsg = sb.toString();
    }

    private void buildHeader(byte msgCount, byte msgFlag, MaxCulMsgType msgType, byte groupId, String srcAddr,
            String dstAddr) {
        BaseMsg copy = new BaseMsg(msgCount, msgFlag, msgType, groupId, srcAddr, dstAddr);
        this.msgCount = copy.msgCount;
        this.msgFlag = copy.msgFlag;
        this.msgType = copy.msgType;
        this.msgTypeRaw = copy.msgTypeRaw;
        this.srcAddrStr = copy.srcAddrStr;
        this.srcAddr = copy.srcAddr;
        this.dstAddrStr = copy.dstAddrStr;
        this.dstAddr = copy.dstAddr;
        this.groupid = copy.groupid;
        this.rawMsg = copy.rawMsg;
    }

    protected void appendPayload(byte[] payload) {
        if (this.flgReadyToSend) {
            /* clear out old payload - we're updating */
            this.flgReadyToSend = false;
            this.buildHeader(msgCount, msgFlag, msgType, groupid, srcAddrStr, dstAddrStr);
        }
        StringBuilder sb = new StringBuilder(this.rawMsg);
        this.flgReadyToSend = true;
        this.payload = payload;
        for (int byteIdx = 0; byteIdx < payload.length; byteIdx++) {
            sb.append(String.format("%02X", payload[byteIdx]).toUpperCase());
        }

        /* prepend length & Z command */
        byte len = (byte) ((sb.length() / PKT_CHARS_PER_BYTE) & 0xFF);
        if (len * PKT_CHARS_PER_BYTE != sb.length()) {
            this.flgReadyToSend = false;
            logger.error("Unable to build raw message. Length is not correct");
        }
        if (isFastSend()) {
            sb.insert(0, String.format("Zf%02X", len));
        } else {
            sb.insert(0, String.format("Zs%02X", len));
        }

        this.len = len;
        this.rawMsg = sb.toString();
        this.flgReadyToSend = true;
    }

    private static boolean pktLenOk(String rawMsg) {
        int len = (byte) (Integer.parseInt(rawMsg.substring(PKT_POS_MSG_LEN, PKT_POS_MSG_LEN + PKT_POS_MSG_LEN_LEN), 16)
                & 0xFF); /* length of packet */
        if (len != ((rawMsg.length() - (PKT_POS_MSG_START + PKT_CHARS_PER_BYTE)) / PKT_CHARS_PER_BYTE)) /*
                                                                                                         * account
                                                                                                         * for
                                                                                                         * preceding
                                                                                                         * characters
                                                                                                         * in
                                                                                                         * the
                                                                                                         * message
                                                                                                         * and
                                                                                                         * a
                                                                                                         * byte
                                                                                                         * at
                                                                                                         * the
                                                                                                         * end
                                                                                                         * which
                                                                                                         * i
                                                                                                         * think
                                                                                                         * is
                                                                                                         * a
                                                                                                         * checksum
                                                                                                         */ {
            return false;
        }
        return true;
    }

    /**
     * Extract flag from a byte
     *
     * @param b
     *            Byte to look at
     * @param pos
     *            Zero indexed position
     * @return true if bit is 1, false if it is 0, false if pos>7
     */
    protected boolean extractBitFromByte(byte b, int pos) {
        if (pos > 7) {
            return false;
        }
        return (((b & (0x1 << pos)) >> pos) == 1);
    }

    /**
     * Return the type of message that has been received given the message
     * string
     *
     * @param rawMsg
     *            Message string from CUL device
     * @return MaxCulMsgType extracted from the message
     */
    public static MaxCulMsgType getMsgType(String rawMsg) {
        if (!pktLenOk(rawMsg)) {
            return MaxCulMsgType.UNKNOWN;
        }

        return MaxCulMsgType.fromByte((byte) (Integer
                .parseInt(rawMsg.substring(PKT_POS_MSG_TYPE, PKT_POS_MSG_TYPE + PKT_POS_MSG_TYPE_LEN), 16) & 0xff));
    }

    public static boolean isForUs(String rawMsg, String addr) {
        if (!pktLenOk(rawMsg)) {
            return false; // length is wrong ignore packet
        }

        return addr.equalsIgnoreCase(rawMsg.substring(PKT_POS_DST_ADDR, PKT_POS_DST_ADDR + PKT_POS_DST_ADDR_LEN));
    }

    public int requiredCredit() {
        /*
         * length in bits = amount of credit needed length in bits = num chars *
         * 4 This is because each char represents 4 bits of a hex number. RawMsg
         * length is decremented by one to account for the 'Z[s|f]'
         */
        int credit = (this.rawMsg.length() - 2) * 4;

        /* credit is in 10ms units, round up */
        credit = (int) Math.ceil(credit / 10.0);

        return credit;
    }

    /**
     * Indicate if this packet is complete
     *
     * @return true if packet is ready to send
     */
    public boolean readyToSend() {
        return this.flgReadyToSend;
    }

    /**
     * Print the payload out for debug
     */
    protected void printDebugPayload() {
        for (int i = 0; i < payload.length; i++) {
            logger.debug("\t{} byte[{}] => 0x{}", this.msgType, i, Integer.toHexString(0xff & payload[i]));
        }
    }

    protected void printMessageHeader() {
        logger.debug("Raw Message: {}", this.rawMsg);
        logger.debug("\tLength    => {}", Integer.toString(0xff & this.len));
        logger.debug("\tMsg Count => 0x{}", Integer.toHexString(0xff & this.msgCount));
        logger.debug("\tMsg Flag  => 0x{}", Integer.toHexString(0xff & this.msgFlag));
        logger.debug("\tMsg Type  => {}", this.msgType);
        logger.debug("\tSrc Addr  => {}", this.srcAddrStr);
        logger.debug("\tDst Addr  => {}", this.dstAddrStr);
        logger.debug("\tGroup ID  => {}", Integer.toString(0xff & this.groupid));
    }

    /**
     * Print output as decoded fields
     */
    protected void printFormattedPayload() {
        logger.debug("\tPrinting raw payload:");
        printDebugPayload();
    }

    /**
     * Print the full message out to debug
     */
    public void printMessage() {
        printMessageHeader();
        printFormattedPayload();
    }

    /**
     * Set this message to be part of a message sequence, also checks if it
     * should be using fast send for this message or not
     *
     * @param msgSeq
     *            MessageSequence to associate with message
     */
    public void setMessageSequencer(@Nullable MessageSequencer msgSeq) {
        msgSequencer = msgSeq;
        if (msgSeq != null) {
            this.setFastSend(msgSeq.useFastSend());
        }
    }

    /**
     * Get the Message Sequencer associated with this message
     *
     * @return MessageSequencer associated with message
     */
    public @Nullable MessageSequencer getMessageSequencer() {
        return msgSequencer;
    }

    /**
     * Check if this message is part of a sequence
     *
     * @return true if part of sequence
     */
    public boolean isPartOfSequence() {
        return (msgSequencer != null);
    }

    /**
     * Set fast send flag manually
     *
     * @param useFastSend
     *            value to set fast send flag to
     */
    public void setFastSend(boolean useFastSend) {
        this.fastSend = useFastSend;
        if (this.flgReadyToSend) {
            logger.debug("Reconfiguring message to {}", (fastSend ? "FAST" : "SLOW"));
            if (fastSend) {
                this.rawMsg = rawMsg.replaceFirst("Zs", "Zf"); // replace slow
                // with fast
            } else {
                this.rawMsg = rawMsg.replaceFirst("Zf", "Zs"); // replace fast
                // with slow
            }
        }
    }

    /**
     * Get fast send status
     *
     * @return true if message is fastSend
     */
    public boolean isFastSend() {
        return fastSend;
    }
}
