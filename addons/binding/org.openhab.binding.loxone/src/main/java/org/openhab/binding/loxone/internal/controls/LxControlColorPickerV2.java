/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.loxone.internal.controls;

import static org.openhab.binding.loxone.internal.LxBindingConstants.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.loxone.internal.LxServerHandler;
import org.openhab.binding.loxone.internal.types.LxCategory;
import org.openhab.binding.loxone.internal.types.LxContainer;
import org.openhab.binding.loxone.internal.types.LxTemperatureHSBType;
import org.openhab.binding.loxone.internal.types.LxUuid;

/**
 * A Color Picker V2 type of control on Loxone Miniserver.
 * <p>
 * According to Loxone API documentation, a color picker control covers:
 * <ul>
 * <li>Color (Hue/Saturation/Brightness)
 * </ul>
 *
 * @author Michael Mattan - initial contribution
 *
 */
public class LxControlColorPickerV2 extends LxControl {

    static class Factory extends LxControlInstance {
        @Override
        LxControl create(LxUuid uuid) {
            return new LxControlColorPickerV2(uuid);
        }

        @Override
        String getType() {
            return TYPE_NAME;
        }
    }

    /**
     * A name by which Miniserver refers to color picker v2 controls
     */
    private static final String TYPE_NAME = "colorpickerv2";

    /**
     * Color state
     */
    private static final String STATE_COLOR = "color";

    LxControlColorPickerV2(LxUuid uuid) {
        super(uuid);
    }

    @Override
    public void initialize(LxServerHandler thingHandler, LxContainer room, LxCategory category) {
        super.initialize(thingHandler, room, category);
        addChannel("Color", new ChannelTypeUID(BINDING_ID, MINISERVER_CHANNEL_TYPE_COLORPICKER), defaultChannelLabel,
                "Color Picker", tags, this::handleCommands, this::getColor);
    }

    private void handleCommands(Command command) throws IOException {
        if (command instanceof HSBType) {
            setColor((HSBType) command);
        } else if (command instanceof OnOffType) {
            if (command == OnOffType.ON) {
                on();
            } else {
                off();
            }
        } else if (command instanceof DecimalType) {
            setBrightness((DecimalType) command);
        } else if (command instanceof PercentType) {
            setBrightness((PercentType) command);
        } else if (command instanceof IncreaseDecreaseType) {
            if (((IncreaseDecreaseType) command).equals(IncreaseDecreaseType.INCREASE)) {
                increaseDecreaseBrightness(1);
            } else {
                increaseDecreaseBrightness(-1);
            }
        }
    }

    /**
     * Gets the current Loxone color in HSBType format
     *
     * @return the HSBType color
     */
    private HSBType getColor() {
        HSBType hsbColor = null;
        String color = getStateTextValue(STATE_COLOR);
        if (color != null) {
            hsbColor = this.mapLoxoneToOH(color);
        }
        return hsbColor;
    }

    /**
     * Sets the color of the color picker
     *
     * @param hsb the color to set
     * @throws IOException error communicating with the Miniserver
     */
    private void setColor(HSBType hsb) throws IOException {
        HSBType currentColor = getColor();
        if (currentColor == null || !currentColor.toString().equals(hsb.toString())) {
            // only update the color when it changed
            // this prevents a mood switch in the Light Controller when the color did not change anyway
            sendAction("hsv(" + hsb.toString() + ")");
        }
    }

    /**
     * Sets the color picker to on
     *
     * @throws IOException error communicating with the Miniserver
     */
    private void on() throws IOException {
        HSBType currentColor = getColor();
        if (currentColor != null) {
            setColor(new HSBType(currentColor.getHue(), currentColor.getSaturation(), PercentType.HUNDRED));
        }
    }

    /**
     * Sets the color picker to off
     *
     * @throws IOException error communicating with the Miniserver
     */
    private void off() throws IOException {
        HSBType currentColor = getColor();
        if (currentColor != null) {
            setColor(new HSBType(currentColor.getHue(), currentColor.getSaturation(), PercentType.ZERO));
        }
    }

    /**
     * set the brightness level
     *
     * @param p the brightness percentage
     * @throws IOException error communicating with the Miniserver
     */
    private void setBrightness(PercentType p) throws IOException {
        HSBType currentColor = this.getColor();
        if (currentColor != null) {
            setColor(new HSBType(currentColor.getHue(), currentColor.getSaturation(), p));
        }
    }

    /**
     * set the brightness level from a decimal type
     *
     * @param d the brightness in decimal
     * @throws IOException error communicating with the Miniserver
     */
    private void setBrightness(DecimalType d) throws IOException {
        setBrightness(new PercentType(d.toBigDecimal()));
    }

    /**
     * Increases/decreases the brightness with a given step
     *
     * @param step the amount to increase/decrease
     * @throws IOException error communicating with the Miniserver
     */
    private void increaseDecreaseBrightness(int step) throws IOException {
        HSBType currentColor = this.getColor();
        if (currentColor != null) {
            setBrightness(new PercentType(currentColor.getBrightness().intValue() + step));
        }
    }

    /**
     * Map Loxone color to OpenHab HSBType
     *
     * @param color color in format hsb(h,s,v) or temp(brightness,kelvin)
     * @return HSBType
     */
    private HSBType mapLoxoneToOH(String color) {
        HSBType hsbColor = null;

        try {
            Pattern hsvPattern = Pattern.compile("^hsv\\([0-9]\\d{0,2},[0-9]\\d{0,2},[0-9]\\d{0,2}\\)");
            Pattern tempPattern = Pattern.compile("^temp\\([0-9]\\d{0,2},[0-9]\\d{0,4}\\)");
            Matcher valueMatcher = Pattern.compile("\\((.*?)\\)").matcher(color);

            if (hsvPattern.matcher(color).matches() && valueMatcher.find()) {
                // we have a hsv(hue,saturation,value) pattern
                hsbColor = new HSBType(valueMatcher.group(1));
            } else if (tempPattern.matcher(color).matches() && valueMatcher.find()) {
                // we have a temp(brightness,kelvin) pattern
                hsbColor = LxTemperatureHSBType.fromBrightnessTemperature(valueMatcher.group(1));
            }
        } catch (IllegalArgumentException e) {
            // an error happened during HSBType creation, we return null
            hsbColor = null;
        }
        return hsbColor;
    }
}
