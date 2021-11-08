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
package org.openhab.binding.tapocontrol.internal.helpers;

import javax.measure.Unit;
import javax.measure.quantity.Time;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;

/**
 * {@link TapoUtils} TapoUtils -
 * Utility Helper Functions
 *
 * @author Christian Wild - Initial Initial contribution
 */
@NonNullByDefault
public class TapoUtils {

    /************************************
     * CALCULATION UTILS
     ***********************************/
    /**
     * Limit Value between limits
     * 
     * @param value Integer
     * @param lowerLimit
     * @param upperLimit
     * @return
     */
    public static Integer limitVal(@Nullable Integer value, Integer lowerLimit, Integer upperLimit) {
        if (value == null || value < lowerLimit) {
            return lowerLimit;
        } else if (value > upperLimit) {
            return upperLimit;
        }
        return value;
    }

    /************************************
     * FORMAT UTILS
     ***********************************/
    /**
     * return value or default val if it's null
     * 
     * @param <T> Type of value
     * @param value value
     * @param defaultValue defaut value
     * @return
     */
    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Format MAC-Address replacing old division chars and add new one
     * 
     * @param mac unformated mac-Address
     * @param newDivisionChar new division char (e.g. ":","-" )
     * @return new formated mac-Address
     */
    public static String formatMac(String mac, char newDivisionChar) {
        String unformatedMac = unformatMac(mac);
        String formatedMac = unformatedMac.replaceAll("(.{2})", "$1" + newDivisionChar).substring(0, 17);
        return formatedMac;
    }

    /**
     * unformat MAC-Address replace all division chars
     * 
     * @param mac
     * @return
     */
    public static String unformatMac(String mac) {
        mac = mac.replace("-", "");
        mac = mac.replace(":", "");
        mac = mac.replace(".", "");
        return mac;
    }

    /**
     * HEX-STRING to byte convertion
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            }
        } catch (Exception e) {
        }
        return data;
    }

    /**
     * Return Boolean from string
     * 
     * @param s - string to be converted
     * @param defVal - Default Value
     */
    public Boolean stringToBool(@Nullable String s, boolean defVal) {
        if (s == null) {
            return defVal;
        }
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return defVal;
        }
    }

    /**
     * Return Integer from string
     * 
     * @param s - string to be converted
     * @param defVal - Default Value
     */
    public Integer stringToInteger(@Nullable String s, Integer defVal) {
        if (s == null) {
            return defVal;
        }
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return defVal;
        }
    }

    /************************************
     * TYPE UTILS
     ***********************************/

    /**
     * Return OnOffType from bool
     * 
     * @param boolVal
     */
    public static OnOffType getOnOffType(@Nullable Boolean boolVal) {
        return (boolVal != null ? boolVal ? OnOffType.ON : OnOffType.OFF : OnOffType.OFF);
    }

    /**
     * Return OnOffType from bool
     * 
     * @param boolVal
     */
    public static OnOffType getOnOffType(Integer intVal) {
        return intVal == 0 ? OnOffType.OFF : OnOffType.ON;
    }

    /**
     * Return StringType from String
     * 
     * @param strVal
     */
    public static StringType getStringType(@Nullable String strVal) {
        return new StringType(strVal != null ? strVal : "");
    }

    /**
     * Return DecimalType from Double
     * 
     * @param numVal
     */
    public static DecimalType getDecimalType(@Nullable Double numVal) {
        return new DecimalType((numVal != null ? numVal : 0));
    }

    /**
     * Return DecimalType from Integer
     * 
     * @param numVal
     */
    public static DecimalType getDecimalType(@Nullable Integer numVal) {
        return new DecimalType((numVal != null ? numVal : 0));
    }

    /**
     * Return DecimalType from Long
     * 
     * @param numVal
     */
    public static DecimalType getDecimalTypel(@Nullable Long numVal) {
        return new DecimalType((numVal != null ? numVal : 0));
    }

    /**
     * 
     * @param numVal value 0-100
     * @return PercentType
     */
    public static PercentType getPercentType(@Nullable Integer numVal) {
        Integer val = limitVal(numVal, 0, 100);
        return new PercentType(val);
    }

    /**
     * Return HSBType from integers
     * 
     * @param hue integer hue-color
     * @param saturation integer saturation
     * @param brightness integer brightness
     * @return HSBType
     */
    public static HSBType getHSBType(Integer hue, Integer saturation, Integer brightness) {
        DecimalType h = new DecimalType(hue);
        PercentType s = new PercentType(saturation);
        PercentType b = new PercentType(brightness);
        return new HSBType(h, s, b);
    }

    /**
     * Return QuantityType with Time
     * 
     * @param numVal Number with value
     * @param unit TimeUnit (Unit<Time>)
     * @return QuantityTime<Time>
     */
    public static QuantityType<Time> getQuantityType(@Nullable Number numVal, Unit<Time> unit) {
        return new QuantityType<>((numVal != null ? numVal : 0), unit);
    }
}
