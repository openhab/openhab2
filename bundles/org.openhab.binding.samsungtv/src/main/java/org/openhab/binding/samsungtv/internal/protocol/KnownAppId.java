/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.samsungtv.internal.protocol;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link KnownAppId} lists all the known app IDs for Samsung TV's
 * 
 *
 * @author Nick Waterton - Initial contribution
 */
@NonNullByDefault
public enum KnownAppId {

    $111477000094,
    $111299001912,
    $3201606009684,
    $3201907018807,
    $3201901017640,
    $3201907018784,
    $3201506003488,
    $11091000000,
    $3201910019365,
    $3201909019271,
    $111399000741,
    $3201601007250,
    $3201807016597,
    $3201705012392,
    $111299001563,
    $3201703012065,
    $3201512006963,
    $111199000333,
    $3201506003486,
    $3201608010191,
    $111399002220,
    $3201710015037,
    $3201601007492,
    $3201412000690,
    $3201908019041,
    $3201602007865,
    $141299000100,
    $3201504001965,
    $111299002012,
    $3201503001543,
    $3201611010983,
    $3201910019378,
    $3202001019933,
    $3201909019241,
    $121299000101,
    $3201606009761,
    $11101302013,
    $3201906018525,
    $3201711015117,
    $3201810017104,
    $3201602007756,
    $3201809016888,
    $3201912019909,
    $3201503001544,
    $3201711015226,
    $3201505002589,
    $3201710014896,
    $3201706014180,
    $111477000821,
    $3201803015852,
    $3201811017268,
    $3201812017547,
    $3201710014874,
    $111199000385,
    $3201904018282,
    $3201601007494,
    $3202004020643,
    $3201910019457,
    $11111358501,
    $3202001020081,
    $3201801015627,
    $3201905018484,
    $3201803015859,
    $11101265008,
    $3201709014773,
    $3201610010879,
    $3201707014498,
    $3201708014527,
    $141477000022,
    $3201801015626,
    $3201807016587,
    $3201711015231,
    $3201807016674,
    $3201511006542,
    $3201508004704,
    $3201801015538,
    $3201704012154,
    $3201703011982,
    $3201706012493,
    $111399002178,
    $3201603008210,
    $3201806016381,
    $3201509005146,
    $3201811017353,
    $3201710014863,
    $3201607009975,
    $3201512006945,
    $3202006021142,
    $3201705012304,
    $3201805016367,
    $3201808016760,
    $3202003020459,
    $3201803015991,
    $3201903017912,
    $3202011022252,
    $3201411000389,
    $3201807016618,
    $3201906018623,
    $111299000513,
    $3201904018194,
    $3201807016684,
    $3201902017876,
    $3201604008870,
    $3202002020129,
    $111399001123,
    $111399000688,
    $3201511006183,
    $3201711015236,
    $3201809016920,
    $3202102022877,
    $3201703012085,
    $3201711015135,
    $3201902017816,
    $3202001019931,
    $3201703012072,
    $3201705012342,
    $3201911019690,
    $3201506003515,
    $3201707014361,
    $3201710014949,
    $3201801015605,
    $3201809016944,
    $3201904018177,
    $3202004020488,
    $3202012022500,
    $111477001084,
    $3202005020803,
    $3202009021746,
    $3201509005241,
    $3201901017667,
    $3201902017805,
    $3201906018589,
    $3201910019513,
    $3202002020207,
    $3202011022316,
    $3201806016390,
    $3201511006303,
    $3201503001595,
    $3201808016753,
    $3201802015704,
    $3201510005851,
    $111399000614,
    $111477000321,
    $3201611011182,
    $3201710015010,
    $3201711015270,
    $3201906018592,
    $111399001818,
    $111477001142,
    $3201502001401,
    $3201702011856,
    $3202006021030,
    $3201601007242,
    $3201710014880,
    $3201805016238,
    $3201807016667,
    $3201810017123,
    $3201812017448,
    $3201901017681,
    $3201903017932,
    $3201903018099,
    $3201904018148,
    $3201906018571,
    $3202011022310,
    $3201709014853,
    $3201710014943,
    $3201804016166,
    $3201806016457,
    $3201811017306,
    $3201903018024,
    $3201906018671,
    $3201907018724,
    $3201908019017,
    $3201909019144,
    $3202007021295,
    $3202104023386,
    $3201603008165,
    $3201802015822,
    $3201804016080,
    $3201907018838,
    $3201908019025,
    $3201908019062,
    $3201910019499,
    $3201911019575,
    $3202001020086,
    $3202002020178,
    $3202012022492,
    $3202102022872,
    $3202103023104,
    $3202106024080,
    $3201604009179,
    $11101300901,
    $111399002250,
    $3202002020229,
    $3201505002443,
    $3201802015746,
    $3201508004622,
    $3201806016406,
    $3201905018447,
    $3201603008706,
    $3201806016479,
    $3201905018474,
    $3202007021398,
    $111199000508,
    $3201504002232,
    $3201507004202,
    $3201803015935,
    $3201812017585,
    $3201907018731,
    $3202009021808,
    $3202101022764,
    $3201703012087,
    $3201712015352,
    $3201802015699,
    $3201803016004,
    $3201805016320,
    $3201806016427,
    $3201807016539,
    $3201808016755,
    $3201809016984,
    $3201811017219,
    $3201811017276,
    $3201812017467,
    $3201904018227,
    $3201904018291,
    $3201905018501,
    $3201906018593,
    $3201907018732,
    $3202005020759,
    $3202010022023,
    $111477000722,
    $3201506003105,
    $3201506003414,
    $3201509005084,
    $3201704012267,
    $3201705012355,
    $3201707014446,
    $3201708014611,
    $3201708014652,
    $3201709014747,
    $3201712015402,
    $3201801015628,
    $3201809016892,
    $3201809016985,
    $3201811017183,
    $3201811017190,
    $3201812017384,
    $3201812017444,
    $3201812017553,
    $3201903018100,
    $3201904018119,
    $3201906018622,
    $3201908018930,
    $3201909019268,
    $3201911019579,
    $3202003020389,
    $3202006020897,
    $3202009021792,
    $3202102022907,
    $111477000567,
    $3201509005087,
    $3201512006941,
    $3201512007023,
    $3201605009390,
    $3201606009782,
    $3201606009783,
    $3201606009887,
    $3201607010167,
    $3201610010753,
    $3201704012271,
    $3201705012435,
    $3201706012513,
    $3201706014294,
    $3201708014531,
    $3201708014677,
    $3201801015505,
    $3201801015599,
    $3201802015810,
    $3201804016078,
    $3201811017191,
    $3201812017437,
    $3201812017447,
    $3201902017790,
    $3201902017811,
    $3201903018023,
    $3201904018165,
    $3201905018373,
    $3201905018405,
    $3201906018530,
    $3201906018558,
    $3201906018560,
    $3201906018596,
    $3201906018620,
    $3201908018992,
    $3201908019034,
    $3201909019229,
    $3201911019572,
    $3201911019711,
    $3201912019798,
    $3201912019850,
    $3202001019936,
    $3202002020105,
    $3202002020248,
    $3202003020417,
    $3202004020552,
    $3202004020578,
    $3202005020752,
    $3202005020804,
    $3202006021035,
    $3202007021160,
    $3202007021420,
    $3202008021578,
    $3202009021791,
    $3202011022262,
    $3202011022315,
    $3202012022373,
    $3202012022431,
    $3202012022473,
    $3202012022481,
    $3202012022558,
    $3202012022577,
    $3202101022640,
    $3202101022656,
    $3202101022721,
    $3202101022755,
    $3202101022788,
    $3202102022932,
    $3202102023007,
    $3202102023056,
    $3202103023211,
    $3202103023338,
    $3202104023388,
    $3202104023522,
    $3202105023716,
    $3202105023733,
    $3202106024097,
    $3202107024412,
    $3202004020674,
    $3202004020626;

    private final String value;

    KnownAppId() {
        value = "";
    }

    KnownAppId(String value) {
        this.value = value.replace("$", "");
    }

    KnownAppId(KnownAppId otherAppId) {
        this(otherAppId.getValue());
    }

    public String getValue() {
        if ("".equals(value)) {
            return this.name().replace("$", "");
        }
        return value.replace("$", "");
    }

    public static Stream<String> stream() {
        return Stream.of(KnownAppId.values()).map(a -> a.getValue());
    }
}
