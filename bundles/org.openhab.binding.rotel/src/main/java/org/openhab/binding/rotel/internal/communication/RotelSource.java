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
package org.openhab.binding.rotel.internal.communication;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.types.StateOption;
import org.openhab.binding.rotel.internal.RotelException;

/**
 * Represents the different sources available for the Rotel device
 *
 * @author Laurent Garnier - Initial contribution
 */
@NonNullByDefault
public enum RotelSource {

    CAT0_CD(0, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),

    CAT1_CD(1, "CD", "CD", RotelCommand.SOURCE_CD, RotelCommand.RECORD_SOURCE_CD, RotelCommand.MAIN_ZONE_SOURCE_CD,
            RotelCommand.ZONE2_SOURCE_CD, RotelCommand.ZONE3_SOURCE_CD, RotelCommand.ZONE4_SOURCE_CD),
    CAT1_TUNER(1, "TUNER", "TUNER", RotelCommand.SOURCE_TUNER, RotelCommand.RECORD_SOURCE_TUNER,
            RotelCommand.MAIN_ZONE_SOURCE_TUNER, RotelCommand.ZONE2_SOURCE_TUNER, RotelCommand.ZONE3_SOURCE_TUNER,
            RotelCommand.ZONE4_SOURCE_TUNER),
    CAT1_TAPE(1, "TAPE", "TAPE", RotelCommand.SOURCE_TAPE, RotelCommand.RECORD_SOURCE_TAPE,
            RotelCommand.MAIN_ZONE_SOURCE_TAPE, RotelCommand.ZONE2_SOURCE_TAPE, RotelCommand.ZONE3_SOURCE_TAPE,
            RotelCommand.ZONE4_SOURCE_TAPE),
    CAT1_VIDEO1(1, "VIDEO1", "VIDEO 1", RotelCommand.SOURCE_VIDEO1, RotelCommand.RECORD_SOURCE_VIDEO1,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO1, RotelCommand.ZONE2_SOURCE_VIDEO1, RotelCommand.ZONE3_SOURCE_VIDEO1,
            RotelCommand.ZONE4_SOURCE_VIDEO1),
    CAT1_VIDEO2(1, "VIDEO2", "VIDEO 2", RotelCommand.SOURCE_VIDEO2, RotelCommand.RECORD_SOURCE_VIDEO2,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO2, RotelCommand.ZONE2_SOURCE_VIDEO2, RotelCommand.ZONE3_SOURCE_VIDEO2,
            RotelCommand.ZONE4_SOURCE_VIDEO2),
    CAT1_VIDEO3(1, "VIDEO3", "VIDEO 3", RotelCommand.SOURCE_VIDEO3, RotelCommand.RECORD_SOURCE_VIDEO3,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO3, RotelCommand.ZONE2_SOURCE_VIDEO3, RotelCommand.ZONE3_SOURCE_VIDEO3,
            RotelCommand.ZONE4_SOURCE_VIDEO3),
    CAT1_VIDEO4(1, "VIDEO4", "VIDEO 4", RotelCommand.SOURCE_VIDEO4, RotelCommand.RECORD_SOURCE_VIDEO4,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO4, RotelCommand.ZONE2_SOURCE_VIDEO4, RotelCommand.ZONE3_SOURCE_VIDEO4,
            RotelCommand.ZONE4_SOURCE_VIDEO4),
    CAT1_VIDEO5(1, "VIDEO5", "VIDEO 5", RotelCommand.SOURCE_VIDEO5, RotelCommand.RECORD_SOURCE_VIDEO5,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO5, RotelCommand.ZONE2_SOURCE_VIDEO5, RotelCommand.ZONE3_SOURCE_VIDEO5,
            RotelCommand.ZONE4_SOURCE_VIDEO5),
    CAT1_MULTI(1, "MULTI", "MULTI", RotelCommand.SOURCE_MULTI_INPUT, null, RotelCommand.MAIN_ZONE_SOURCE_MULTI_INPUT,
            null, null, null),
    CAT1_FOLLOW_MAIN(1, "MAIN", "Follow Main Zone Source", null, RotelCommand.RECORD_SOURCE_MAIN, null,
            RotelCommand.ZONE2_SOURCE_MAIN, RotelCommand.ZONE3_SOURCE_MAIN, RotelCommand.ZONE4_SOURCE_MAIN),

    CAT2_CD(2, "CD", "CD", RotelCommand.SOURCE_CD, RotelCommand.RECORD_SOURCE_CD, RotelCommand.MAIN_ZONE_SOURCE_CD,
            RotelCommand.ZONE2_SOURCE_CD, RotelCommand.ZONE3_SOURCE_CD, RotelCommand.ZONE4_SOURCE_CD),
    CAT2_TUNER(2, "TUNER", "TUNER", RotelCommand.SOURCE_TUNER, RotelCommand.RECORD_SOURCE_TUNER,
            RotelCommand.MAIN_ZONE_SOURCE_TUNER, RotelCommand.ZONE2_SOURCE_TUNER, RotelCommand.ZONE3_SOURCE_TUNER,
            RotelCommand.ZONE4_SOURCE_TUNER),
    CAT2_VIDEO1(2, "VIDEO1", "VIDEO 1", RotelCommand.SOURCE_VIDEO1, RotelCommand.RECORD_SOURCE_VIDEO1,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO1, RotelCommand.ZONE2_SOURCE_VIDEO1, RotelCommand.ZONE3_SOURCE_VIDEO1,
            RotelCommand.ZONE4_SOURCE_VIDEO1),
    CAT2_VIDEO2(2, "VIDEO2", "VIDEO 2", RotelCommand.SOURCE_VIDEO2, RotelCommand.RECORD_SOURCE_VIDEO2,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO2, RotelCommand.ZONE2_SOURCE_VIDEO2, RotelCommand.ZONE3_SOURCE_VIDEO2,
            RotelCommand.ZONE4_SOURCE_VIDEO2),
    CAT2_VIDEO3(2, "VIDEO3", "VIDEO 3", RotelCommand.SOURCE_VIDEO3, RotelCommand.RECORD_SOURCE_VIDEO3,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO3, RotelCommand.ZONE2_SOURCE_VIDEO3, RotelCommand.ZONE3_SOURCE_VIDEO3,
            RotelCommand.ZONE4_SOURCE_VIDEO3),
    CAT2_VIDEO4(2, "VIDEO4", "VIDEO 4", RotelCommand.SOURCE_VIDEO4, RotelCommand.RECORD_SOURCE_VIDEO4,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO4, RotelCommand.ZONE2_SOURCE_VIDEO4, RotelCommand.ZONE3_SOURCE_VIDEO4,
            RotelCommand.ZONE4_SOURCE_VIDEO4),
    CAT2_VIDEO5(2, "VIDEO5", "VIDEO 5", RotelCommand.SOURCE_VIDEO5, RotelCommand.RECORD_SOURCE_VIDEO5,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO5, RotelCommand.ZONE2_SOURCE_VIDEO5, RotelCommand.ZONE3_SOURCE_VIDEO5,
            RotelCommand.ZONE4_SOURCE_VIDEO5),
    CAT2_VIDEO6(2, "VIDEO6", "VIDEO 6", RotelCommand.SOURCE_VIDEO6, RotelCommand.RECORD_SOURCE_VIDEO6,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO6, RotelCommand.ZONE2_SOURCE_VIDEO6, RotelCommand.ZONE3_SOURCE_VIDEO6,
            RotelCommand.ZONE4_SOURCE_VIDEO6),
    CAT2_USB(2, "USB", "USB", RotelCommand.SOURCE_USB, RotelCommand.RECORD_SOURCE_USB,
            RotelCommand.MAIN_ZONE_SOURCE_USB, RotelCommand.ZONE2_SOURCE_USB, RotelCommand.ZONE3_SOURCE_USB,
            RotelCommand.ZONE4_SOURCE_USB),
    CAT2_MULTI(2, "MULTI", "MULTI", RotelCommand.SOURCE_MULTI_INPUT, null, RotelCommand.MAIN_ZONE_SOURCE_MULTI_INPUT,
            null, null, null),
    CAT2_FOLLOW_MAIN(2, "MAIN", "Follow Main Zone Source", null, RotelCommand.RECORD_SOURCE_MAIN, null,
            RotelCommand.ZONE2_SOURCE_MAIN, RotelCommand.ZONE3_SOURCE_MAIN, RotelCommand.ZONE4_SOURCE_MAIN),

    CAT3_CD(3, "CD", "CD", RotelCommand.SOURCE_CD, RotelCommand.RECORD_SOURCE_CD, RotelCommand.MAIN_ZONE_SOURCE_CD,
            RotelCommand.ZONE2_SOURCE_CD, RotelCommand.ZONE3_SOURCE_CD, RotelCommand.ZONE4_SOURCE_CD),
    CAT3_TUNER(3, "TUNER", "TUNER", RotelCommand.SOURCE_TUNER, RotelCommand.RECORD_SOURCE_TUNER,
            RotelCommand.MAIN_ZONE_SOURCE_TUNER, RotelCommand.ZONE2_SOURCE_TUNER, RotelCommand.ZONE3_SOURCE_TUNER,
            RotelCommand.ZONE4_SOURCE_TUNER),
    CAT3_TAPE(3, "TAPE", "TAPE", RotelCommand.SOURCE_TAPE, RotelCommand.RECORD_SOURCE_TAPE,
            RotelCommand.MAIN_ZONE_SOURCE_TAPE, RotelCommand.ZONE2_SOURCE_TAPE, RotelCommand.ZONE3_SOURCE_TAPE,
            RotelCommand.ZONE4_SOURCE_TAPE),
    CAT3_VIDEO1(3, "VIDEO1", "VIDEO 1", RotelCommand.SOURCE_VIDEO1, RotelCommand.RECORD_SOURCE_VIDEO1,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO1, RotelCommand.ZONE2_SOURCE_VIDEO1, RotelCommand.ZONE3_SOURCE_VIDEO1,
            RotelCommand.ZONE4_SOURCE_VIDEO1),
    CAT3_VIDEO2(3, "VIDEO2", "VIDEO 2", RotelCommand.SOURCE_VIDEO2, RotelCommand.RECORD_SOURCE_VIDEO2,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO2, RotelCommand.ZONE2_SOURCE_VIDEO2, RotelCommand.ZONE3_SOURCE_VIDEO2,
            RotelCommand.ZONE4_SOURCE_VIDEO2),
    CAT3_VIDEO3(3, "VIDEO3", "VIDEO 3", RotelCommand.SOURCE_VIDEO3, RotelCommand.RECORD_SOURCE_VIDEO3,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO3, RotelCommand.ZONE2_SOURCE_VIDEO3, RotelCommand.ZONE3_SOURCE_VIDEO3,
            RotelCommand.ZONE4_SOURCE_VIDEO3),
    CAT3_VIDEO4(3, "VIDEO4", "VIDEO 4", RotelCommand.SOURCE_VIDEO4, RotelCommand.RECORD_SOURCE_VIDEO4,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO4, RotelCommand.ZONE2_SOURCE_VIDEO4, RotelCommand.ZONE3_SOURCE_VIDEO4,
            RotelCommand.ZONE4_SOURCE_VIDEO4),
    CAT3_VIDEO5(3, "VIDEO5", "VIDEO 5", RotelCommand.SOURCE_VIDEO5, RotelCommand.RECORD_SOURCE_VIDEO5,
            RotelCommand.MAIN_ZONE_SOURCE_VIDEO5, RotelCommand.ZONE2_SOURCE_VIDEO5, RotelCommand.ZONE3_SOURCE_VIDEO5,
            RotelCommand.ZONE4_SOURCE_VIDEO5),
    CAT3_MULTI(3, "MULTI", "MULTI", RotelCommand.SOURCE_MULTI_INPUT, null, RotelCommand.MAIN_ZONE_SOURCE_MULTI_INPUT,
            null, null, null),

    CAT4_CD(4, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT4_AUX1(4, "AUX1", "Aux 1", RotelCommand.SOURCE_AUX1, null, null, null, null, null),
    CAT4_TUNER(4, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT4_PHONO(4, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT4_BLUETOOTH(4, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),

    CAT5_CD(5, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT5_COAX1(5, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT5_COAX2(5, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT5_OPTICAL1(5, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT5_OPTICAL2(5, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT5_AUX1(5, "AUX1", "Aux 1", RotelCommand.SOURCE_AUX1, null, null, null, null, null),
    CAT5_AUX2(5, "AUX2", "Aux 2", RotelCommand.SOURCE_AUX2, null, null, null, null, null),
    CAT5_TUNER(5, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT5_PHONO(5, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT5_USB(5, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT5_PCUSB(5, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT5_BLUETOOTH(5, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),

    CAT6_RCD(6, "RCD", "Rotel CD", RotelCommand.SOURCE_ROTEL_CD, null, null, null, null, null),
    CAT6_CD(6, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT6_COAX1(6, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT6_COAX2(6, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT6_OPTICAL1(6, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT6_OPTICAL2(6, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT6_AUX1(6, "AUX1", "Aux 1", RotelCommand.SOURCE_AUX1, null, null, null, null, null),
    CAT6_AUX2(6, "AUX2", "Aux 2", RotelCommand.SOURCE_AUX2, null, null, null, null, null),
    CAT6_TUNER(6, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT6_PHONO(6, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT6_USB(6, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),

    CAT7_RCD(7, "RCD", "Rotel CD", RotelCommand.SOURCE_ROTEL_CD, null, null, null, null, null),
    CAT7_CD(7, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT7_COAX1(7, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT7_COAX2(7, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT7_OPTICAL1(7, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT7_OPTICAL2(7, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT7_AUX1(7, "AUX1", "Aux 1", RotelCommand.SOURCE_AUX1, null, null, null, null, null),
    CAT7_AUX2(7, "AUX2", "Aux 2", RotelCommand.SOURCE_AUX2, null, null, null, null, null),
    CAT7_TUNER(7, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT7_PHONO(7, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT7_USB(7, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT7_PCUSB(7, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT7_XLR(7, "XLR", "XLR", RotelCommand.SOURCE_XLR, null, null, null, null, null),

    CAT8_CD(8, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT8_COAX1(8, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT8_COAX2(8, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT8_OPTICAL1(8, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT8_OPTICAL2(8, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT8_AUX(8, "AUX", "Aux", RotelCommand.SOURCE_AUX, null, null, null, null, null),
    CAT8_TUNER(8, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT8_PHONO(8, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT8_USB(8, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT8_PCUSB(8, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT8_BLUETOOTH(8, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),
    CAT8_XLR(8, "XLR", "XLR", RotelCommand.SOURCE_XLR, null, null, null, null, null),

    CAT9_RCD(9, "RCD", "Rotel CD", RotelCommand.SOURCE_ROTEL_CD, null, null, null, null, null),
    CAT9_CD(9, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT9_COAX1(9, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT9_COAX2(9, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT9_COAX3(9, "COAX3", "Coax 3", RotelCommand.SOURCE_COAX3, null, null, null, null, null),
    CAT9_OPTICAL1(9, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT9_OPTICAL2(9, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT9_OPTICAL3(9, "OPTICAL3", "Optical 3", RotelCommand.SOURCE_OPT3, null, null, null, null, null),
    CAT9_AUX(9, "AUX", "Aux", RotelCommand.SOURCE_AUX, null, null, null, null, null),
    CAT9_TUNER(9, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT9_PHONO(9, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT9_USB(9, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT9_PCUSB(9, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT9_BLUETOOTH(9, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),
    CAT9_XLR(9, "XLR", "XLR", RotelCommand.SOURCE_XLR, null, null, null, null, null),

    CAT10_CD(10, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT10_TUNER(10, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT10_PHONO(10, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT10_VIDEO1(10, "VIDEO1", "VIDEO 1", RotelCommand.SOURCE_VIDEO1, null, null, null, null, null),
    CAT10_VIDEO2(10, "VIDEO2", "VIDEO 2", RotelCommand.SOURCE_VIDEO2, null, null, null, null, null),
    CAT10_VIDEO3(10, "VIDEO3", "VIDEO 3", RotelCommand.SOURCE_VIDEO3, null, null, null, null, null),
    CAT10_VIDEO4(10, "VIDEO4", "VIDEO 4", RotelCommand.SOURCE_VIDEO4, null, null, null, null, null),
    CAT10_VIDEO5(10, "VIDEO5", "VIDEO 5", RotelCommand.SOURCE_VIDEO5, null, null, null, null, null),
    CAT10_VIDEO6(10, "VIDEO6", "VIDEO 6", RotelCommand.SOURCE_VIDEO6, null, null, null, null, null),
    CAT10_VIDEO7(10, "VIDEO7", "VIDEO 7", RotelCommand.SOURCE_VIDEO7, null, null, null, null, null),
    CAT10_USB(10, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT10_PCUSB(10, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT10_BLUETOOTH(10, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),
    CAT10_XLR(10, "XLR", "XLR", RotelCommand.SOURCE_XLR, null, null, null, null, null),
    CAT10_MULTI(10, "MULTI", "MULTI", RotelCommand.SOURCE_MULTI_INPUT, null, null, null, null, null),

    CAT11_CD(11, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT11_TUNER(11, "TUNER", "Tuner", RotelCommand.SOURCE_TUNER, null, null, null, null, null),
    CAT11_PHONO(11, "PHONO", "Phono", RotelCommand.SOURCE_PHONO, null, null, null, null, null),
    CAT11_VIDEO1(11, "VIDEO1", "VIDEO 1", RotelCommand.SOURCE_VIDEO1, null, null, null, null, null),
    CAT11_VIDEO2(11, "VIDEO2", "VIDEO 2", RotelCommand.SOURCE_VIDEO2, null, null, null, null, null),
    CAT11_VIDEO3(11, "VIDEO3", "VIDEO 3", RotelCommand.SOURCE_VIDEO3, null, null, null, null, null),
    CAT11_VIDEO4(11, "VIDEO4", "VIDEO 4", RotelCommand.SOURCE_VIDEO4, null, null, null, null, null),
    CAT11_VIDEO5(11, "VIDEO5", "VIDEO 5", RotelCommand.SOURCE_VIDEO5, null, null, null, null, null),
    CAT11_VIDEO6(11, "VIDEO6", "VIDEO 6", RotelCommand.SOURCE_VIDEO6, null, null, null, null, null),
    CAT11_VIDEO7(11, "VIDEO7", "VIDEO 7", RotelCommand.SOURCE_VIDEO7, null, null, null, null, null),
    CAT11_VIDEO8(11, "VIDEO8", "VIDEO 8", RotelCommand.SOURCE_VIDEO8, null, null, null, null, null),
    CAT11_USB(11, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT11_PCUSB(11, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),
    CAT11_BLUETOOTH(11, "BLUETOOTH", "Bluetooth", RotelCommand.SOURCE_BLUETOOTH, null, null, null, null, null),
    CAT11_XLR(11, "XLR", "XLR", RotelCommand.SOURCE_XLR, null, null, null, null, null),
    CAT11_MULTI(11, "MULTI", "MULTI", RotelCommand.SOURCE_MULTI_INPUT, null, null, null, null, null),

    CAT12_FM(12, "FM", "FM", RotelCommand.SOURCE_FM, null, null, null, null, null),
    CAT12_DAB(12, "DAB", "DAB", RotelCommand.SOURCE_DAB, null, null, null, null, null),

    CAT13_FM(13, "FM", "FM", RotelCommand.SOURCE_FM, null, null, null, null, null),
    CAT13_DAB(13, "DAB", "DAB", RotelCommand.SOURCE_DAB, null, null, null, null, null),
    CAT13_PLAYFI(13, "PLAYFI", "PlayFi", RotelCommand.SOURCE_PLAYFI, null, null, null, null, null),

    CAT14_FM(14, "FM", "FM", RotelCommand.SOURCE_FM, null, null, null, null, null),
    CAT14_DAB(14, "DAB", "DAB", RotelCommand.SOURCE_DAB, null, null, null, null, null),
    CAT14_IRADIO(14, "IRADIO", "iRadio", RotelCommand.SOURCE_IRADIO, null, null, null, null, null),
    CAT14_NETWORK(14, "NETWORK", "Network", RotelCommand.SOURCE_NETWORK, null, null, null, null, null),

    CAT15_COAX1(15, "COAX1", "Coax 1", RotelCommand.SOURCE_COAX1, null, null, null, null, null),
    CAT15_COAX2(15, "COAX2", "Coax 2", RotelCommand.SOURCE_COAX2, null, null, null, null, null),
    CAT15_OPTICAL1(15, "OPTICAL1", "Optical 1", RotelCommand.SOURCE_OPT1, null, null, null, null, null),
    CAT15_OPTICAL2(15, "OPTICAL2", "Optical 2", RotelCommand.SOURCE_OPT2, null, null, null, null, null),
    CAT15_USB(15, "USB", "Front USB", RotelCommand.SOURCE_USB, null, null, null, null, null),
    CAT15_PCUSB(15, "PCUSB", "PC USB", RotelCommand.SOURCE_PCUSB, null, null, null, null, null),

    CAT16_IRADIO(16, "IRADIO", "iRadio", RotelCommand.SOURCE_IRADIO, null, null, null, null, null),
    CAT16_NETWORK(16, "NETWORK", "Network", RotelCommand.SOURCE_NETWORK, null, null, null, null, null),
    CAT16_AUX1_COAX(16, "AUX1_COAX", "Aux 1 Coax", RotelCommand.SOURCE_AUX1_COAX, null, null, null, null, null),
    CAT16_AUX1_OPTICAL(16, "AUX1_OPTICAL", "Aux 1 Optical", RotelCommand.SOURCE_AUX1_OPT, null, null, null, null, null),
    CAT16_FM(16, "FM", "FM", RotelCommand.SOURCE_FM, null, null, null, null, null),
    CAT16_DAB(16, "DAB", "DAB", RotelCommand.SOURCE_DAB, null, null, null, null, null),
    CAT16_USB(16, "USB", "USB", RotelCommand.SOURCE_USB, null, null, null, null, null),

    CAT17_CD(17, "CD", "CD", RotelCommand.SOURCE_CD, null, null, null, null, null),
    CAT17_IRADIO(17, "IRADIO", "iRadio", RotelCommand.SOURCE_IRADIO, null, null, null, null, null),
    CAT17_NETWORK(17, "NETWORK", "Network", RotelCommand.SOURCE_NETWORK, null, null, null, null, null),
    CAT17_AUX1_COAX(17, "AUX1_COAX", "Aux 1 Coax", RotelCommand.SOURCE_AUX1_COAX, null, null, null, null, null),
    CAT17_AUX1_OPTICAL(17, "AUX1_OPTICAL", "Aux 1 Optical", RotelCommand.SOURCE_AUX1_OPT, null, null, null, null, null),
    CAT17_AUX2(17, "AUX2", "Aux 2", RotelCommand.SOURCE_AUX2, null, null, null, null, null),
    CAT17_FM(17, "FM", "FM", RotelCommand.SOURCE_FM, null, null, null, null, null),
    CAT17_DAB(17, "DAB", "DAB", RotelCommand.SOURCE_DAB, null, null, null, null, null),
    CAT17_USB(17, "USB", "USB", RotelCommand.SOURCE_USB, null, null, null, null, null);

    private int category;
    private String name;
    private String label;
    private @Nullable RotelCommand command;
    private @Nullable RotelCommand recordCommand;
    private @Nullable RotelCommand mainZoneCommand;
    private @Nullable RotelCommand zone2Command;
    private @Nullable RotelCommand zone3Command;
    private @Nullable RotelCommand zone4Command;

    /**
     * Constructor
     *
     * @param category a category of models for which the source is available
     * @param name the name of the source
     * @param label the label of the source
     * @param command the command to select the source
     * @param recordCommand the command to select the source as source to be recorded
     * @param mainZoneCommand the command to select the source in the main zone
     * @param zone2Command the command to select the source in the zone 2
     * @param zone3Command the command to select the source in the zone 3
     * @param zone4Command the command to select the source in the zone 4
     */
    private RotelSource(int category, String name, String label, @Nullable RotelCommand command,
            @Nullable RotelCommand recordCommand, @Nullable RotelCommand mainZoneCommand,
            @Nullable RotelCommand zone2Command, @Nullable RotelCommand zone3Command,
            @Nullable RotelCommand zone4Command) {
        this.category = category;
        this.name = name;
        this.label = label;
        this.command = command;
        this.recordCommand = recordCommand;
        this.mainZoneCommand = mainZoneCommand;
        this.zone2Command = zone2Command;
        this.zone3Command = zone3Command;
        this.zone4Command = zone4Command;
    }

    /**
     * Get the category of models for the source
     *
     * @return the category of models
     */
    public int getCategory() {
        return category;
    }

    /**
     * Get the name of the source
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the label of the source
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label of the source
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the command to select the source
     *
     * @return the command
     */
    public @Nullable RotelCommand getCommand() {
        return command;
    }

    /**
     * Get the command to select the source as source to be recorded
     *
     * @return the command
     */
    public @Nullable RotelCommand getRecordCommand() {
        return recordCommand;
    }

    /**
     * Get the command to select the source in the main zone
     *
     * @return the command
     */
    public @Nullable RotelCommand getMainZoneCommand() {
        return mainZoneCommand;
    }

    /**
     * Get the command to select the source in the zone 2
     *
     * @return the command
     */
    public @Nullable RotelCommand getZone2Command() {
        return zone2Command;
    }

    /**
     * Get the command to select the source in the zone 3
     *
     * @return the command
     */
    public @Nullable RotelCommand getZone3Command() {
        return zone3Command;
    }

    /**
     * Get the command to select the source in the zone 4
     *
     * @return the command
     */
    public @Nullable RotelCommand getZone4Command() {
        return zone4Command;
    }

    /**
     * Get the list of {@link StateOption} associated to the available sources for a particular category of models
     *
     * @param category a category of models
     * @param type a source type (0 for global source, 1 for main zone, 2 for zone 2, 3 for zone 3, 4 for zone 4 and 5
     *            for record source)
     *
     * @return the list of {@link StateOption} associated to the available sources in a zone for a provided category of
     *         models
     */
    public static List<StateOption> getStateOptions(int category, int type) {
        List<StateOption> options = new ArrayList<>();
        for (RotelSource value : RotelSource.values()) {
            if (value.getCategory() == category && ((type == 0 && value.getCommand() != null)
                    || (type == 1 && value.getMainZoneCommand() != null)
                    || (type == 2 && value.getZone2Command() != null) || (type == 3 && value.getZone3Command() != null)
                    || (type == 4 && value.getZone4Command() != null)
                    || (type == 5 && value.getRecordCommand() != null))) {
                options.add(new StateOption(value.getName(), value.getLabel()));
            }
        }
        return options;
    }

    /**
     * Get the source associated to a name for a particular category of models
     *
     * @param category a category of models
     * @param name the name used to identify the source
     *
     * @return the source associated to the searched name for the provided category of models
     *
     * @throws RotelException - If no source is associated to the searched name for the provided category
     */
    public static RotelSource getFromName(int category, String name) throws RotelException {
        for (RotelSource value : RotelSource.values()) {
            if (value.getCategory() == category && value.getName().equals(name)) {
                return value;
            }
        }
        throw new RotelException("Invalid name for a source: " + name);
    }

    /**
     * Get the source associated to a label displayed on the Rotel front panel for a particular category of models
     * Search for the source with the longest matching label
     *
     * @param category a category of models
     * @param label the label used to identify the source
     *
     * @return the source associated to the searched label for the provided category of models
     *
     * @throws RotelException - If no source is associated to the searched label for the provided category
     */
    public static RotelSource getFromLabel(int category, String label) throws RotelException {
        RotelSource result = null;
        for (RotelSource value : RotelSource.values()) {
            if (value.getCategory() == category && label.startsWith(value.getLabel())) {
                if (result == null || result.getLabel().length() < value.getLabel().length()) {
                    result = value;
                }
            }
        }
        if (result != null) {
            return result;
        }
        throw new RotelException("Invalid label for a source: " + label);
    }

    /**
     * Get the source associated to a command for a particular category of models
     *
     * @param category a category of models
     * @param command the command used to identify the source
     * @param type a source type (0 for global source, 1 for main zone, 2 for zone 2, 3 for zone 3, 4 for zone 4 and 5
     *            for record source)
     *
     * @return the source associated to the searched command for the provided category of models
     *
     * @throws RotelException - If no source is associated to the searched command for the provided category
     */
    public static RotelSource getFromCommand(int category, RotelCommand command, int type) throws RotelException {
        for (RotelSource value : RotelSource.values()) {
            if (value.getCategory() == category && ((type == 0 && value.getCommand() == command)
                    || (type == 1 && value.getMainZoneCommand() == command)
                    || (type == 2 && value.getZone2Command() == command)
                    || (type == 3 && value.getZone3Command() == command)
                    || (type == 4 && value.getZone4Command() == command)
                    || (type == 5 && value.getRecordCommand() == command))) {
                return value;
            }
        }
        throw new RotelException("Invalid command for a source: " + command.getName());
    }
}
