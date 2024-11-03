/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.dirigera.internal.handler.light;

import static org.openhab.binding.dirigera.internal.Constants.CHANNEL_LIGHT_HSB;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.handler.BaseHandler;
import org.openhab.binding.dirigera.internal.model.Model;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ColorLightHandler} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ColorLightHandler extends BaseHandler {
    private final Logger logger = LoggerFactory.getLogger(ColorLightHandler.class);

    private HSBType hsbCurrent;

    public ColorLightHandler(Thing thing, Map<String, String> mapping) {
        super(thing, mapping);
        super.setChildHandler(this);
        PercentType pt = new PercentType(50);
        hsbCurrent = new HSBType(new DecimalType(50), pt, pt);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (super.checkHandler()) {
            JSONObject values = gateway().api().readDevice(config.id);
            handleUpdate(values);
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.trace("DIRIGERA LIGHT_DEVICE handle {} {}", channelUID, command);
        super.handleCommand(channelUID, command);
        String channel = channelUID.getIdWithoutGroup();

        String targetProperty = channel2PropertyMap.get(channel);
        if (targetProperty != null) {
            if (command instanceof HSBType hsb) {
                boolean colorSendToAPI = false;
                if (Math.round(hsb.getHue().intValue()) == Math.round(hsbCurrent.getHue().intValue()) && Math
                        .round(hsb.getSaturation().intValue()) == Math.round(hsbCurrent.getSaturation().intValue())) {
                    logger.trace("DIRIGERA LIGHT_DEVICE hno need to update color, it's the same");
                } else {
                    JSONObject colorAttributes = new JSONObject();
                    colorAttributes.put("colorHue", hsb.getHue().intValue());
                    colorAttributes.put("colorSaturation", hsb.getSaturation().intValue() / 100.0);
                    logger.trace("DIRIGERA LIGHT_DEVICE send to API {}", colorAttributes);
                    gateway().api().sendAttributes(config.id, colorAttributes);
                    colorSendToAPI = true;
                }
                if (hsb.getBrightness().intValue() == hsbCurrent.getBrightness().intValue()) {
                    logger.trace("DIRIGERA LIGHT_DEVICE hno need to update brightness, it's the same");
                } else {
                    if (colorSendToAPI) {
                        // seems that IKEA lamps cannot handle consecutive calls it really short time frame
                        // so give it 100ms pause until next call
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    JSONObject brightnessattributes = new JSONObject();
                    brightnessattributes.put("lightLevel", hsb.getBrightness().intValue());
                    gateway().api().sendAttributes(config.id, brightnessattributes);
                }
            }
        }
    }

    @Override
    public void handleUpdate(JSONObject update) {
        super.handleUpdate(update);
        if (update.has(Model.ATTRIBUTES)) {
            boolean deliverHSB = false;
            JSONObject attributes = update.getJSONObject(Model.ATTRIBUTES);
            Iterator<String> attributesIterator = attributes.keys();
            while (attributesIterator.hasNext()) {
                String key = attributesIterator.next();
                String targetChannel = property2ChannelMap.get(key);
                if (targetChannel != null) {
                    switch (targetChannel) {
                        case CHANNEL_LIGHT_HSB:
                            switch (key) {
                                case "colorHue":
                                    double hueValue = attributes.getInt(key);
                                    hsbCurrent = new HSBType(new DecimalType(hueValue), hsbCurrent.getSaturation(),
                                            hsbCurrent.getBrightness());
                                    deliverHSB = true;
                                    break;
                                case "colorSaturation":
                                    double saturationValue = Math.round(attributes.getDouble(key) * 100);
                                    logger.trace("DIRIGERA LIGHT_DEVICE new Saturation value {} {}", saturationValue,
                                            (int) saturationValue);
                                    hsbCurrent = new HSBType(hsbCurrent.getHue(),
                                            new PercentType((int) saturationValue), hsbCurrent.getBrightness());
                                    deliverHSB = true;
                                    break;
                                case "lightLevel":
                                    int brightnessValue = attributes.getInt(key);
                                    hsbCurrent = new HSBType(hsbCurrent.getHue(), hsbCurrent.getSaturation(),
                                            new PercentType(brightnessValue));
                                    deliverHSB = true;
                                    break;
                            }
                            break;
                    }
                }
            }
            if (deliverHSB) {
                updateState(new ChannelUID(thing.getUID(), "hsb"), hsbCurrent);
            }
        }
    }
}
