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
package org.openhab.io.homekit.internal;

import static org.openhab.io.homekit.internal.HomekitAccessoryType.DUMMY;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps an Item with data derived from supported tags defined.
 *
 * @author Andy Lintner - Initial contribution
 */
@NonNullByDefault
public class HomekitTaggedItem {
    private final Logger logger = LoggerFactory.getLogger(HomekitTaggedItem.class);

    /** configuration keywords at items level **/
    public final static String MIN_VALUE = "minValue";
    public final static String MAX_VALUE = "maxValue";
    public final static String STEP = "step";
    public final static String DIMMER_MODE = "dimmerMode";
    public final static String DELAY = "commandDelay";

    private static final Map<Integer, String> CREATED_ACCESSORY_IDS = new ConcurrentHashMap<>();

    // proxy item used to group commands for complex item types like Color or Dimmer
    private final HomekitOHItemProxy proxyItem;

    // type of HomeKit accessory/service, e.g. TemperatureSensor
    private final HomekitAccessoryType homekitAccessoryType;

    // type of HomeKit characteristic, e.g. CurrentTemperature
    private @Nullable HomekitCharacteristicType homekitCharacteristicType;

    // configuration attached to the openHAB Item, e.g. minValue, maxValue, valveType
    private @Nullable Map<String, Object> configuration;

    // link to the groupItem if item is part of a group
    private @Nullable GroupItem parentGroupItem;

    // HomeKit accessory id (aid) which is generated from item name
    private final int id;

    public HomekitTaggedItem(HomekitOHItemProxy item, HomekitAccessoryType homekitAccessoryType,
            @Nullable Map<String, Object> configuration) {
        this.proxyItem = item;
        this.parentGroupItem = null;
        this.configuration = configuration;
        this.homekitAccessoryType = homekitAccessoryType;
        this.homekitCharacteristicType = HomekitCharacteristicType.EMPTY;
        if (homekitAccessoryType != DUMMY) {
            this.id = calculateId(item.getItem());
        } else {
            this.id = 0;
        }
        parseConfiguration();
    }

    public HomekitTaggedItem(HomekitOHItemProxy item, HomekitAccessoryType homekitAccessoryType,
            @Nullable HomekitCharacteristicType homekitCharacteristicType,
            @Nullable Map<String, Object> configuration) {
        this(item, homekitAccessoryType, configuration);
        this.homekitCharacteristicType = homekitCharacteristicType;
    }

    public HomekitTaggedItem(HomekitOHItemProxy item, HomekitAccessoryType homekitAccessoryType,
            @Nullable HomekitCharacteristicType homekitCharacteristicType, @Nullable GroupItem parentGroup,
            @Nullable Map<String, Object> configuration) {
        this(item, homekitAccessoryType, homekitCharacteristicType, configuration);
        this.parentGroupItem = parentGroup;
    }

    public boolean isGroup() {
        return (isAccessory() && (proxyItem.getItem() instanceof GroupItem));
    }

    public HomekitAccessoryType getAccessoryType() {
        return homekitAccessoryType;
    }

    public @Nullable HomekitCharacteristicType getCharacteristicType() {
        return homekitCharacteristicType;
    }

    public @Nullable Map<String, Object> getConfiguration() {
        return configuration;
    }

    /**
     * Returns whether or not this item refers to an item that fully specifies a HomeKit accessory. Mutually
     * exclusive
     * to isCharacteristic(). Primary devices must belong to a root accessory group.
     */
    public boolean isAccessory() {
        return homekitAccessoryType != DUMMY;
    }

    /**
     * Returns whether or not this item is in a group that specifies a HomeKit accessory. It is not possible to be a
     * characteristic and an accessory. Further, all characteristics belong to a
     * root deviceGroup.
     */
    public boolean isCharacteristic() {
        return homekitCharacteristicType != null && homekitCharacteristicType != HomekitCharacteristicType.EMPTY;
    }

    /**
     * return openHAB item responsible for the HomeKit item
     * 
     * @return openHAB item
     */
    public Item getItem() {
        return proxyItem.getItem();
    }

    /**
     * return proxy item which is used to group commands.
     * 
     * @return proxy item
     */
    public HomekitOHItemProxy getProxyItem() {
        return proxyItem;
    }

    /**
     * send openHAB item command via proxy item, which allows to group commands.
     * e.g. sendCommandProxy(hue), sendCommandProxy(brightness) would lead to one openHAB command that updates hue and
     * brightness at once
     *
     * @param commandType type of the command, e.g. OHItemProxy.HUE_COMMAND
     * @param command command/state
     */
    public void sendCommandProxy(String commandType, State command) {
        proxyItem.sendCommandProxy(commandType, command);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return proxyItem.getItem().getName();
    }

    /**
     * Returns the RootDevice GroupItem to which this item belongs.
     * Returns null if not in a group.
     */
    public @Nullable GroupItem getRootDeviceGroupItem() {
        return parentGroupItem;
    }

    /**
     * Returns whether or not this item belongs to a HomeKit accessory group.
     *
     * Characteristic devices must belong to a HomeKit accessory group.
     */
    public boolean isMemberOfAccessoryGroup() {
        return parentGroupItem != null;
    }

    private void parseConfiguration() {
        if (configuration != null) {
            Object dimmerModeConfig = configuration.get(DIMMER_MODE);
            if (dimmerModeConfig instanceof String) {
                final String dimmerModeConfigStr = (String) dimmerModeConfig;
                if (dimmerModeConfigStr.equalsIgnoreCase("none")) {
                    proxyItem.setDimmerMode(HomekitOHItemProxy.DIMMER_MODE_NONE);
                } else if (dimmerModeConfigStr.equalsIgnoreCase("filterOn")) {
                    proxyItem.setDimmerMode(HomekitOHItemProxy.DIMMER_MODE_FILTER_ON);
                } else if (dimmerModeConfigStr.equalsIgnoreCase("filterBrightness100")) {
                    proxyItem.setDimmerMode(HomekitOHItemProxy.DIMMER_MODE_FILTER_BRIGHTNESS_100);
                } else if (dimmerModeConfigStr.equalsIgnoreCase("filterOnExceptBrightness100")) {
                    proxyItem.setDimmerMode(HomekitOHItemProxy.DIMMER_MODE_FILTER_ON_EXCEPT_BRIGHTNESS_100);
                }
            }
            Object delayConfig = configuration.get(DELAY);
            if (delayConfig instanceof BigDecimal) {
                proxyItem.setDelay(((BigDecimal) delayConfig).intValue());
            }
        }
    }

    private int calculateId(Item item) {
        // magic number 629 is the legacy from apache HashCodeBuilder (17*37)
        int id = 629 + item.getName().hashCode();
        if (id < 0) {
            id += Integer.MAX_VALUE;
        }
        if (id < 2) {
            id = 2; // 0 and 1 are reserved
        }
        if (CREATED_ACCESSORY_IDS.containsKey(id)) {
            if (!CREATED_ACCESSORY_IDS.get(id).equals(item.getName())) {
                logger.warn(
                        "Could not create HomeKit accessory {} because its hash conflicts with {}. This is a 1:1,000,000 chance occurrence. Change one of the names and consider playing the lottery. See https://github.com/openhab/openhab-addons/issues/257#issuecomment-125886562",
                        item.getName(), CREATED_ACCESSORY_IDS.get(id));
                return 0;
            }
        } else {
            CREATED_ACCESSORY_IDS.put(id, item.getName());
        }
        return id;
    }

    public String toString() {
        return "Item:" + proxyItem + "  HomeKit type:" + homekitAccessoryType + " HomeKit characteristic:"
                + homekitCharacteristicType;
    }
}
