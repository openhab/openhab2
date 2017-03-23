/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.virtual;

import static org.openhab.binding.homematic.internal.misc.HomematicConstants.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.internal.misc.HomematicClientException;
import org.openhab.binding.homematic.internal.misc.MiscUtils;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDatapointConfig;
import org.openhab.binding.homematic.internal.model.HmDatapointInfo;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmParamsetType;
import org.openhab.binding.homematic.internal.model.HmValueType;

/**
 * The {@link DisplayTextVirtualDatapoint} adds multiple virtual datapoints to the HM-Dis-WM55 and HM-Dis-EP-WM55
 * devices to easily handle colored text, icons, the led and the beeper of the display.
 *
 * @author Gerhard Riegler - Initial contribution
 */

public class DisplayTextVirtualDatapoint extends AbstractVirtualDatapointHandler {

    private static final String DATAPOINT_NAME_DISPLAY_LINE = "DISPLAY_LINE_";
    private static final String DATAPOINT_NAME_DISPLAY_COLOR = "DISPLAY_COLOR_";
    private static final String DATAPOINT_NAME_DISPLAY_ICON = "DISPLAY_ICON_";
    private static final String DATAPOINT_NAME_DISPLAY_LED = "DISPLAY_LED";
    private static final String DATAPOINT_NAME_DISPLAY_BEEPER = "DISPLAY_BEEPER";
    private static final String DATAPOINT_NAME_DISPLAY_SUBMIT = "DISPLAY_SUBMIT";

    private static final String START = "0x02";
    private static final String STOP = "0x03";
    private static final String LINE = "0x12";
    private static final String COLOR = "0x11";
    private static final String LF = "0x0a";
    private static final String ICON = "0x13";

    private static final String BEEPER_START = "0x14";
    private static final String BEEPER_END = "0x1c";

    private static Map<String, String> replaceMap = new HashMap<String, String>();

    // replace special chars while encoding
    static {
        replaceMap.put("d6", "23");
        replaceMap.put("dc", "24");
        replaceMap.put("3d", "27");
        replaceMap.put("c4", "5b");
        replaceMap.put("df", "5f");
        replaceMap.put("e4", "7b");
        replaceMap.put("f6", "7c");
        replaceMap.put("fc", "7d");
    }

    /**
     * Available text colors.
     */
    private enum Color {
        NONE(""),
        WHITE("0x80"),
        RED("0x81"),
        ORANGE("0x82"),
        YELLOW("0x83"),
        GREEN("0x84"),
        BLUE("0x85");

        private final String code;

        private Color(String code) {
            this.code = code;
        }

        protected String getCode() {
            return code;
        }

        /**
         * Returns the color code.
         */
        public static String getCode(String name) {
            try {
                return valueOf(name).getCode();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Available icons.
     */
    private enum Icon {
        NONE(""),
        OFF("0x80"),
        ON("0x81"),
        OPEN("0x82"),
        CLOSED("0x83"),
        ERROR("0x84"),
        OK("0x85"),
        INFO("0x86"),
        NEW_MESSAGE("0x87"),
        SERVICE("0x88"),
        SIGNAL_GREEN("0x89"),
        SIGNAL_YELLOW("0x8a"),
        SIGNAL_RED("0x8b");

        private final String code;

        private Icon(String code) {
            this.code = code;
        }

        protected String getCode() {
            return code;
        }

        /**
         * Returns the color code.
         */
        public static String getCode(String name) {
            try {
                return valueOf(name).getCode();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Available Beeper codes.
     */
    private enum Beeper {
        OFF("0xc0"),
        LONG_LONG("0xc1"),
        LONG_SHORT("0xc2"),
        LONG_SHORT_SHORT("0xc3"),
        SHORT("0xc4"),
        SHORT_SHORT("0xc5"),
        LONG("0xc6");

        private final String code;

        private Beeper(String code) {
            this.code = code;
        }

        protected String getCode() {
            return code;
        }

        /**
         * Returns the color code.
         */
        public static String getCode(String name) {
            try {
                return valueOf(name).getCode();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Available LED colors.
     */
    private enum Led {
        OFF("0xf0"),
        RED("0xf1"),
        GREEN("0xf2"),
        ORANGE("0xf3");

        private final String code;

        private Led(String code) {
            this.code = code;
        }

        protected String getCode() {
            return code;
        }

        /**
         * Returns the color code.
         */
        public static String getCode(String name) {
            try {
                return valueOf(name).getCode();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return DATAPOINT_NAME_DISPLAY_SUBMIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(HmDevice device) {
        boolean isEpDisplay = isEpDisplay(device);
        if (device.getType().equals(DEVICE_TYPE_STATUS_DISPLAY) || isEpDisplay) {
            for (HmChannel channel : device.getChannels()) {
                if (channel.hasDatapoint(new HmDatapointInfo(HmParamsetType.VALUES, channel, DATAPOINT_NAME_SUBMIT))) {
                    for (int i = 1; i <= getLineCount(device); i++) {
                        addDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_LINE + i, HmValueType.STRING,
                                null, false);

                        addEnumDisplayDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_ICON + i,
                                Icon.class);

                        if (!isEpDisplay) {
                            addEnumDisplayDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_COLOR + i,
                                    Color.class);
                        }
                    }
                    if (isEpDisplay) {
                        addEnumDisplayDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_BEEPER,
                                Beeper.class);
                        addEnumDisplayDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_LED, Led.class);
                    }
                    addDatapoint(device, channel.getNumber(), DATAPOINT_NAME_DISPLAY_SUBMIT, HmValueType.BOOL, false,
                            false);
                }
            }
        }
    }

    /**
     * Adds a Datapoint to the device with the values of the given enum.
     */
    private void addEnumDisplayDatapoint(HmDevice device, int channelNumber, String datapointName,
            Class<? extends Enum<?>> e) {
        HmDatapoint dpEnum = addDatapoint(device, channelNumber, datapointName, HmValueType.ENUM, null, false);
        dpEnum.setOptions(getEnumNames(e));
        dpEnum.setMinValue(0);
        dpEnum.setMaxValue(e.getEnumConstants().length);
    }

    /**
     * Returns a string array with all the constants in the Enum.
     */
    private String[] getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }

    /**
     * Returns the number of lines of the display.
     */
    private int getLineCount(HmDevice device) {
        return (DEVICE_TYPE_STATUS_DISPLAY.equals(device.getType()) ? 5 : 3);
    }

    /**
     * Returns true, if the display is a EP display.
     */
    private boolean isEpDisplay(HmDevice device) {
        return DEVICE_TYPE_EP_STATUS_DISPLAY.equals(device.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandleCommand(HmDatapoint dp, Object value) {
        HmDevice device = dp.getChannel().getDevice();
        return (device.getType().equals(DEVICE_TYPE_STATUS_DISPLAY) || isEpDisplay(device))
                && (getName().equals(dp.getName()) || dp.getName().startsWith(DATAPOINT_NAME_DISPLAY_LINE)
                        || dp.getName().startsWith(DATAPOINT_NAME_DISPLAY_COLOR)
                        || dp.getName().startsWith(DATAPOINT_NAME_DISPLAY_ICON)
                        || dp.getName().equals(DATAPOINT_NAME_DISPLAY_LED)
                        || dp.getName().equals(DATAPOINT_NAME_DISPLAY_BEEPER));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleCommand(VirtualGateway gateway, HmDatapoint dp, HmDatapointConfig dpConfig, Object value)
            throws IOException, HomematicClientException {
        dp.setValue(value);

        if (DATAPOINT_NAME_DISPLAY_SUBMIT.equals(dp.getName()) && MiscUtils.isTrueValue(dp.getValue())) {
            HmChannel channel = dp.getChannel();

            List<String> message = new ArrayList<String>();
            message.add(START);
            message.add(LF);

            for (int i = 1; i <= getLineCount(channel.getDevice()); i++) {
                String line = ObjectUtils.toString(
                        channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_DISPLAY_LINE + i).getValue());
                if (StringUtils.isEmpty(line)) {
                    line = " ";
                }
                message.add(LINE);
                message.add(encodeText(line));
                String color = channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_DISPLAY_COLOR + i)
                        .getOptionValue();
                String icon = channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_DISPLAY_ICON + i)
                        .getOptionValue();

                message.add(COLOR);
                String colorCode = Color.getCode(color);
                message.add(StringUtils.isBlank(colorCode) ? Color.WHITE.getCode() : colorCode);

                String iconCode = Icon.getCode(icon);
                if (StringUtils.isNotBlank(iconCode)) {
                    message.add(ICON);
                    message.add(iconCode);
                }
                message.add(LF);
            }

            if (isEpDisplay(channel.getDevice())) {
                String beeper = channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_DISPLAY_BEEPER)
                        .getOptionValue();
                String beeperCode = Beeper.getCode(beeper);
                if (StringUtils.isNotBlank(beeperCode)) {
                    message.add(BEEPER_START);
                    message.add(beeperCode);
                    message.add(BEEPER_END);
                }

                String led = channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_DISPLAY_LED).getOptionValue();
                String ledCode = Led.getCode(led);
                if (StringUtils.isNotBlank(ledCode)) {
                    message.add(ledCode);
                }
            }

            message.add(STOP);

            gateway.sendDatapoint(channel.getDatapoint(HmParamsetType.VALUES, DATAPOINT_NAME_SUBMIT),
                    new HmDatapointConfig(), StringUtils.join(message, ","));
        }
    }

    /**
     * Encodes the given text for the display.
     */
    private String encodeText(String text) {
        final byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (byte b : bytes) {
            sb.append("0x");
            String hexValue = String.format("%02x", b);
            sb.append(replaceMap.containsKey(hexValue) ? replaceMap.get(hexValue) : hexValue);
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
