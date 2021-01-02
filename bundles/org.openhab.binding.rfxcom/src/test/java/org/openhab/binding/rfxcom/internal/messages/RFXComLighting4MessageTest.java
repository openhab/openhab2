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
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openhab.binding.rfxcom.internal.RFXComBindingConstants.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType.LIGHTING4;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.Commands.*;
import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting4Message.SubType.PT2262;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.openhab.binding.rfxcom.internal.config.RFXComDeviceConfiguration;
import org.openhab.binding.rfxcom.internal.config.RFXComDeviceConfigurationBuilder;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.util.HexUtils;

/**
 * Test for RFXCom-binding
 *
 * @author Martin van Wingerden - Initial contribution
 */
@NonNullByDefault
public class RFXComLighting4MessageTest {
    @Test
    public void basicBoundaryCheck() throws RFXComException {
        RFXComLighting4Message message = (RFXComLighting4Message) RFXComMessageFactory.createMessage(LIGHTING4);

        RFXComDeviceConfiguration build = new RFXComDeviceConfigurationBuilder().withDeviceId("90000").withPulse(300)
                .withSubType("PT2262").build();
        message.setConfig(build);
        message.convertFromState(CHANNEL_COMMAND, OnOffType.ON);

        byte[] binaryMessage = message.decodeMessage();
        RFXComLighting4Message msg = (RFXComLighting4Message) RFXComMessageFactory.createMessage(binaryMessage);

        assertEquals("90000", msg.getDeviceId(), "Sensor Id");
    }

    private void testMessage(String hexMsg, RFXComLighting4Message.SubType subType, String deviceId,
            @Nullable Integer pulse, RFXComLighting4Message.Commands command, @Nullable Integer seqNbr, int signalLevel,
            int offCommand, int onCommand) throws RFXComException {
        testMessage(hexMsg, subType, deviceId, pulse, command.toByte(), seqNbr, signalLevel, offCommand, onCommand);
    }

    private void testMessage(String hexMsg, RFXComLighting4Message.SubType subType, String deviceId,
            @Nullable Integer pulse, byte commandByte, @Nullable Integer seqNbr, int signalLevel, int offCommand,
            int onCommand) throws RFXComException {
        RFXComLighting4Message msg = (RFXComLighting4Message) RFXComMessageFactory
                .createMessage(HexUtils.hexToBytes(hexMsg));
        assertEquals(deviceId, msg.getDeviceId(), "Sensor Id");
        assertEquals(commandByte, RFXComTestHelper.getActualIntValue(msg, CHANNEL_COMMAND_ID), "Command");
        if (seqNbr != null) {
            assertEquals(seqNbr.shortValue(), (short) (msg.seqNbr & 0xFF), "Seq Number");
        }
        assertEquals(signalLevel, RFXComTestHelper.getActualIntValue(msg, CHANNEL_SIGNAL_LEVEL), "Signal Level");

        byte[] decoded = msg.decodeMessage();

        assertEquals(hexMsg, HexUtils.bytesToHex(decoded), "Message converted back");

        RFXComTestHelper.checkDiscoveryResult(msg, deviceId, pulse, subType.toString(), offCommand, onCommand);
    }

    @Test
    public void testSomeMessages() throws RFXComException {
        testMessage("091300E1D8AD59018F70", PT2262, "887509", 399, ON_9, 225, 2, 4, 9);
        testMessage("0913005FA9A9C901A170", PT2262, "694940", 417, ON_9, 95, 2, 4, 9);
        testMessage("091300021D155C01E960", PT2262, "119125", 489, ON_12, 2, 2, 4, 12);
        testMessage("091300D345DD99018C50", PT2262, "286169", 396, ON_9, 211, 2, 4, 9);
        testMessage("09130035D149A2017750", PT2262, "857242", 375, OFF_2, 53, 2, 2, 1);
        testMessage("0913000B4E462A012280", PT2262, "320610", 290, ON_10, 11, 3, 4, 10);
        testMessage("09130009232D2E013970", PT2262, "144082", 313, OFF_14, 9, 2, 14, 1);
        testMessage("091300CA0F8D2801AA70", PT2262, "63698", 426, ON_8, 202, 2, 4, 8);
    }

    @Test
    public void testSomeAlarmRemote() throws RFXComException {
        testMessage("0913004A0D8998016E60", PT2262, "55449", 366, ON_8, 74, 2, 4, 8);
    }

    @Test
    public void testCheapPirSensor() throws RFXComException {
        testMessage("091300EF505FC6019670", PT2262, "329212", 406, ON_6, 239, 2, 4, 6);
    }

    @Test
    public void testSomeConradMessages() throws RFXComException {
        testMessage("0913003554545401A150", PT2262, "345413", 417, OFF_4, 53, 2, 4, 1);
    }

    @Test
    public void testPhenixMessages() throws RFXComException {
        List<String> onMessages = Arrays.asList("09130046044551013780", "09130048044551013780", "0913004A044551013980",
                "0913004C044551013780", "0913004E044551013780");

        for (String message : onMessages) {
            testMessage(message, PT2262, "17493", null, ON_1, null, 3, 4, 1);
        }

        List<String> offMessages = Arrays.asList("09130051044554013980", "09130053044554013680", "09130055044554013680",
                "09130057044554013680", "09130059044554013680", "0913005B044554013680", "0913005D044554013480",
                "09130060044554013980", "09130062044554013680", "09130064044554013280");

        for (String message : offMessages) {
            testMessage(message, PT2262, "17493", null, OFF_4, null, 3, 4, 1);
        }
    }
}
