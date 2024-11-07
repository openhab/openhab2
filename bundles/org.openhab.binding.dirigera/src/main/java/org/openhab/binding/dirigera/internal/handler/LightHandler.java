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
package org.openhab.binding.dirigera.internal.handler;

import static org.openhab.binding.dirigera.internal.Constants.CHANNEL_LIGHT_HSB;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONObject;
import org.openhab.binding.dirigera.internal.model.Model;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LightHandler} basic DeviceHandler for all devices
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class LightHandler extends BaseDeviceHandler {
    private final Logger logger = LoggerFactory.getLogger(LightHandler.class);

    private HSBType hsbCurrent;

    public LightHandler(Thing thing, Map<String, String> mapping) {
        super(thing, mapping);
        super.setChildHandler(this);
        PercentType pt = new PercentType(50);
        hsbCurrent = new HSBType(new DecimalType(50), pt, pt);
    }

    @Override
    public void initialize() {
        // handle general initialize like setting bridge
        super.initialize();
        gateway().registerDevice(this);
        // finally get attributes from model in order to get initial values
        JSONObject values = gateway().model().getAllFor(config.id);
        logger.error("DIRIGERA LIGHT_DEVICE values for initial update {}", values);
        handleUpdate(values);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String channel = channelUID.getIdWithoutGroup();
        logger.trace("DIRIGERA LIGHT_DEVICE handle command {} for {}", command, channel);
        if (command instanceof RefreshType) {
            JSONObject values = gateway().model().getAllFor(config.id);
            if (values.has(Model.ATTRIBUTES)) {
                JSONObject attributes = values.getJSONObject(Model.ATTRIBUTES);
                handleUpdate(attributes);
            }
        } else {
            String targetProperty = channel2PropertyMap.get(channel);
            if (targetProperty != null) {
                if (command instanceof HSBType hsb) {
                    boolean colorSendToAPI = false;
                    if (hsb.getHue().intValue() == hsbCurrent.getHue().intValue()
                            && hsb.getSaturation().intValue() == hsbCurrent.getSaturation().intValue()) {
                        logger.trace("DIRIGERA LIGHT_DEVICE hno need to update color, it's the same");
                    } else {
                        JSONObject colorAttributes = new JSONObject();
                        colorAttributes.put("colorHue", hsb.getHue().doubleValue());
                        colorAttributes.put("colorSaturation", hsb.getSaturation().doubleValue() / 100);
                        JSONObject colorData = new JSONObject();
                        colorData.put(Model.ATTRIBUTES, colorAttributes);
                        logger.trace("DIRIGERA LIGHT_DEVICE send to API {}", colorData);
                        gateway().api().sendPatch(config.id, colorData);
                        colorSendToAPI = true;
                    }
                    if (hsb.getBrightness().intValue() == hsbCurrent.getBrightness().intValue()) {
                        logger.trace("DIRIGERA LIGHT_DEVICE hno need to update brightness, it's the same");
                    } else {
                        if (colorSendToAPI) {
                            // seems that IKEA lamps cannot handle consecutive calls it really short time frame
                            // so give it 100ms pause until next call
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        JSONObject brightnessattributes = new JSONObject();
                        brightnessattributes.put("lightLevel", hsb.getBrightness().intValue());
                        JSONObject brightnesssData = new JSONObject();
                        brightnesssData.put(Model.ATTRIBUTES, brightnessattributes);
                        logger.trace("DIRIGERA LIGHT_DEVICE send to API {}", brightnesssData);
                        gateway().api().sendPatch(config.id, brightnesssData);
                    }
                } else if (command instanceof OnOffType onOff) {
                    JSONObject attributes = new JSONObject();
                    attributes.put(targetProperty, onOff.equals(OnOffType.ON));
                    JSONObject data = new JSONObject();
                    data.put(Model.ATTRIBUTES, attributes);
                    logger.trace("DIRIGERA LIGHT_DEVICE send to API {}", data);
                    gateway().api().sendPatch(config.id, data);
                }
            } else {
                logger.trace("DIRIGERA LIGHT_DEVICE no property found for channel {}", channel);
            }
        }
    }

    @Override
    public void handleUpdate(JSONObject update) {
        // handle reachable flag
        super.handleUpdate(update);
        // now device specific
        if (update.has(Model.ATTRIBUTES)) {
            boolean deliverHSB = false;
            JSONObject attributes = update.getJSONObject(Model.ATTRIBUTES);
            Iterator<String> attributesIterator = attributes.keys();
            logger.trace("DIRIGERA LIGHT_DEVICE update delivered {} attributes", attributes.length());
            while (attributesIterator.hasNext()) {
                String key = attributesIterator.next();
                String targetChannel = property2ChannelMap.get(key);
                if (targetChannel != null) {
                    if (CHANNEL_LIGHT_HSB.equals(targetChannel)) {
                        switch (key) {
                            case "colorHue":
                                double hueValue = attributes.getDouble(key);
                                hsbCurrent = new HSBType(new DecimalType(hueValue), hsbCurrent.getSaturation(),
                                        hsbCurrent.getBrightness());
                                deliverHSB = true;
                                break;
                            case "colorSaturation":
                                double saturationValue = attributes.getDouble(key) * 100;
                                logger.trace("DIRIGERA LIGHT_DEVICE new Saturation value {} {}", saturationValue,
                                        (int) saturationValue);
                                hsbCurrent = new HSBType(hsbCurrent.getHue(), new PercentType((int) saturationValue),
                                        hsbCurrent.getBrightness());
                                deliverHSB = true;
                                break;
                            case "lightLevel":
                                int brightnessValue = attributes.getInt(key);
                                hsbCurrent = new HSBType(hsbCurrent.getHue(), hsbCurrent.getSaturation(),
                                        new PercentType(brightnessValue));
                                deliverHSB = true;
                                break;
                        }
                    } else if ("on".equals(targetChannel)) {
                        updateState(new ChannelUID(thing.getUID(), targetChannel),
                                OnOffType.from(attributes.getBoolean(key)));
                    } else {
                        logger.trace("DIRIGERA LIGHT_DEVICE no channel for {} available", key);
                    }
                } else {
                    logger.trace("DIRIGERA LIGHT_DEVICE no targetChannel for {}", key);
                }
            }
            logger.trace("DIRIGERA LIGHT_DEVICE deliver {} ? {}", hsbCurrent, deliverHSB);
            if (deliverHSB) {
                updateState(new ChannelUID(thing.getUID(), "hsb"), hsbCurrent);
            }
        }
    }
}
