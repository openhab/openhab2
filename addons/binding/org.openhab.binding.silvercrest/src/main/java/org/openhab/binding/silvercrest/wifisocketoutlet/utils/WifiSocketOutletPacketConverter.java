package org.openhab.binding.silvercrest.wifisocketoutlet.utils;

import java.net.DatagramPacket;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.openhab.binding.silvercrest.exceptions.PacketIntegrityErrorException;
import org.openhab.binding.silvercrest.wifisocketoutlet.entities.WifiSocketOutletRequest;
import org.openhab.binding.silvercrest.wifisocketoutlet.entities.WifiSocketOutletResponse;
import org.openhab.binding.silvercrest.wifisocketoutlet.enums.WifiSocketOutletResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms the the received datagram packet to one
 *
 * @author Jaime Vaz - Initial contribution
 *
 */
public class WifiSocketOutletPacketConverter {

    private static final Logger LOG = LoggerFactory.getLogger(WifiSocketOutletPacketConverter.class);

    private static String REQUEST_PREFIX = "01";
    private static String RESPONSE_PREFIX = "0142";
    private static String LOCK_STATUS = "40";
    /* encryptDataLength */
    private static String ENCRYPT_PREFIX = "00";
    private static String PACKET_NUMBER = "FFFF";
    private static String COMPANY_CODE = "C1";
    private static String DEVICE_TYPE = "11";
    private static String AUTH_CODE = "7150";

    private static String ENCRIPTION_KEY = "0123456789abcdef";

    private Cipher silvercrestEncryptCipher;
    private Cipher silvercrestDecryptCipher;

    /**
     * START_OF_RECEIVED_PACKET.
     * STX - pkt nbr - CompanyCode - device - authCode
     *
     * 00 -- 0029 -- C1 -- 11 -- 7150
     */
    private static String REGEX_START_OF_RECEIVED_PACKET = "00([A-F0-9]{4})C1117150";
    private static String REGEX_HEXADECIMAL_PAIRS = "([A-F0-9]{2})*";

    private static String REGEX_START_OF_RECEIVED_PACKET_SEARCH_MAC_ADDRESS = REGEX_START_OF_RECEIVED_PACKET + "23"
            + REGEX_HEXADECIMAL_PAIRS;
    private static String REGEX_START_OF_RECEIVED_PACKET_HEART_BEAT = REGEX_START_OF_RECEIVED_PACKET + "61"
            + REGEX_HEXADECIMAL_PAIRS;
    private static String REGEX_START_OF_RECEIVED_PACKET_CMD_GPIO_EVENT = REGEX_START_OF_RECEIVED_PACKET + "06"
            + REGEX_HEXADECIMAL_PAIRS;
    private static String REGEX_START_OF_RECEIVED_PACKET_QUERY_STATUS = REGEX_START_OF_RECEIVED_PACKET + "02"
            + REGEX_HEXADECIMAL_PAIRS;
    private static String REGEX_START_OF_RECEIVED_PACKET_RESPONSE_GPIO_CHANGE_REQUEST = REGEX_START_OF_RECEIVED_PACKET
            + "01" + REGEX_HEXADECIMAL_PAIRS;

    /**
     * Default constructor of the packet converter.
     */
    public WifiSocketOutletPacketConverter() {
        // init cipher
        try {
            byte[] encriptionKeyBytes;
            encriptionKeyBytes = ENCRIPTION_KEY.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(encriptionKeyBytes, "AES");
            IvParameterSpec IvKey = new IvParameterSpec(encriptionKeyBytes);

            this.silvercrestEncryptCipher = Cipher.getInstance("AES/CBC/NoPadding");
            this.silvercrestEncryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, IvKey);

            this.silvercrestDecryptCipher = Cipher.getInstance("AES/CBC/NoPadding");
            this.silvercrestDecryptCipher.init(Cipher.DECRYPT_MODE, secretKey, IvKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that transforms one {@link WifiSocketOutletRequest} to one byte array to be ready to be transmitted.
     *
     * @param requestPacket the {@link WifiSocketOutletRequest}.
     * @return the byte array with the message.
     */
    public byte[] transformToByteMessage(final WifiSocketOutletRequest requestPacket) {
        byte[] requestDatagram = null;
        String fullCommand = ENCRYPT_PREFIX + PACKET_NUMBER + COMPANY_CODE + DEVICE_TYPE + AUTH_CODE
                + String.format(requestPacket.getType().getCommand(), requestPacket.getMacAddress());

        byte[] inputByte = hexStringToByteArray(fullCommand);
        byte[] bEncrypted;
        try {
            bEncrypted = this.silvercrestEncryptCipher.doFinal(inputByte);
            int encryptDataLength = bEncrypted.length;

            LOG.trace("Encrypted data={" + byteArrayToHexString(inputByte) + "}");
            LOG.trace("Decrypted data={" + byteArrayToHexString(bEncrypted) + "}");
            String cryptedCommand = byteArrayToHexString(bEncrypted);

            String packetString = REQUEST_PREFIX + LOCK_STATUS + requestPacket.getMacAddress()
                    + Integer.toHexString(encryptDataLength) + cryptedCommand;

            LOG.trace("Request Packet: {}", packetString);
            LOG.trace("Request packet decrypted data: [{}] with lenght: {}", fullCommand);
            requestDatagram = hexStringToByteArray(packetString);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            LOG.debug("Failure processing the build of the request packet for mac '{}' and type '{}'",
                    requestPacket.getMacAddress(), requestPacket.getType());
        }
        return requestDatagram;
    }

    /**
     * Decrypts one response {@link DatagramPacket}.
     *
     * @param packet the {@link DatagramPacket}
     * @return the {@link WifiSocketOutletResponse} is successfully decrypted.
     * @throws PacketIntegrityErrorException if the message has some integrity error.
     */
    public WifiSocketOutletResponse decryptResponsePacket(final DatagramPacket packet)
            throws PacketIntegrityErrorException {
        WifiSocketOutletResponse responsePacket = this.decryptResponsePacket(
                WifiSocketOutletPacketConverter.byteArrayToHexString(packet.getData(), packet.getLength()));

        responsePacket.setHostAddress(packet.getAddress().getHostAddress());

        return responsePacket;
    }

    /**
     * STX - pkt nbr - CompanyCode - device - authCode
     *
     * 00 -- 0029 -- C1 -- 11 -- 7150
     *
     * @param hexPacket
     * @return
     * @throws PacketIntegrityErrorException
     */
    private WifiSocketOutletResponse decryptResponsePacket(final String hexPacket)
            throws PacketIntegrityErrorException {

        if (!Pattern.matches(RESPONSE_PREFIX + REGEX_HEXADECIMAL_PAIRS, hexPacket)) {
            throw new PacketIntegrityErrorException(
                    "The packet received is not one response! \nPacket:[" + hexPacket + "]");
        }

        LOG.trace("Response packet: {}", hexPacket);
        String macAddress = hexPacket.substring(4, 16);
        LOG.trace("The mac address of the sender of the packet is: {}", macAddress);
        String decryptedData = this.decrypt(hexPacket.substring(18, hexPacket.length()));

        LOG.trace("Response packet decrypted data: [{}] with lenght: {}", decryptedData, decryptedData.length());

        WifiSocketOutletResponseType responseType;
        // check packet integrity
        if (Pattern.matches(REGEX_START_OF_RECEIVED_PACKET_SEARCH_MAC_ADDRESS, decryptedData)) {
            responseType = WifiSocketOutletResponseType.DISCOVERY;
            LOG.trace("Received answer of mac address search! lenght:{}", decryptedData.length());

        } else if (Pattern.matches(REGEX_START_OF_RECEIVED_PACKET_HEART_BEAT, decryptedData)) {
            responseType = WifiSocketOutletResponseType.ACK;
            LOG.trace("Received heart beat!");

        } else if (Pattern.matches(REGEX_START_OF_RECEIVED_PACKET_CMD_GPIO_EVENT, decryptedData)) {
            LOG.trace("Received gpio event!");
            String status = decryptedData.substring(20, 22);
            responseType = "FF".equalsIgnoreCase(status) ? WifiSocketOutletResponseType.ON
                    : WifiSocketOutletResponseType.OFF;
            LOG.trace("Socket status: {}", responseType);

        } else if (Pattern.matches(REGEX_START_OF_RECEIVED_PACKET_RESPONSE_GPIO_CHANGE_REQUEST, decryptedData)) {
            LOG.trace("Received response from a gpio change request!");
            String status = decryptedData.substring(20, 22);
            responseType = "FF".equalsIgnoreCase(status) ? WifiSocketOutletResponseType.ON
                    : WifiSocketOutletResponseType.OFF;
            LOG.trace("Socket status: {}", responseType);

        } else if (Pattern.matches(REGEX_START_OF_RECEIVED_PACKET_QUERY_STATUS, decryptedData)) {
            LOG.trace("Received response from status query!");
            String status = decryptedData.substring(20, 22);
            responseType = "FF".equalsIgnoreCase(status) ? WifiSocketOutletResponseType.ON
                    : WifiSocketOutletResponseType.OFF;
            LOG.trace("Socket status: {}", responseType);

        } else {
            throw new PacketIntegrityErrorException("The packet decrypted is with wrong format. \nPacket:[" + hexPacket
                    + "]  \nDecryptedPacket:[" + decryptedData + "]");
        }

        LOG.trace("Decrypt success. Packet is from socket with mac address [{}] and type is [{}]", macAddress,
                responseType);
        return new WifiSocketOutletResponse(macAddress, responseType);
    }

    /**
     * Decrypts one received message with the correct cypher.
     *
     * @param inputData the cyphered message
     * @return the decrypted message.
     */
    private String decrypt(final String inputData) {
        byte[] inputByte = hexStringToByteArray(inputData);
        byte[] bDecrypted;
        try {
            bDecrypted = this.silvercrestDecryptCipher.doFinal(inputByte);
            LOG.trace("Encrypted data={" + byteArrayToHexString(inputByte) + "}");
            LOG.trace("Decrypted data={" + byteArrayToHexString(bDecrypted) + "}");
            return byteArrayToHexString(bDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // String/Array/Hex manipulation
    /**
     * Converts one hexadecimal string to one byte array.
     *
     * @param str the string to convert.
     * @return the byte array.
     */
    private static byte[] hexStringToByteArray(final String str) {
        byte[] b = new byte[str.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(str.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    /**
     * Converts one full byte array to one hexadecimal string.
     *
     * @param array the byte array to convert.
     * @return the hexadecimal string.
     */
    private static String byteArrayToHexString(final byte[] array) {
        return byteArrayToHexString(array, array.length);
    }

    /**
     * Converts one partial byte array to one hexadecimal string.
     *
     * @param array the byte array to convert.
     * @param length the length to convert.
     * @return the hexadecimal string.
     */
    private static String byteArrayToHexString(final byte[] array, final int length) {
        if ((array == null) || (array.length == 0)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String hex = "";

        for (int i = 0; i < length; i++) {
            hex = Integer.toHexString(0xFF & array[i]).toUpperCase();
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            builder.append(hex);
        }
        return builder.toString();
    }

}
