/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.solarlog.internal;

/**
 * The {@link SolarLogChannel} Enum defines common constants, which are
 * used across the whole binding.
 *
 * @author Johann Richard - Initial contribution
 */
public enum SolarLogChannel {

    CHANNEL_LASTUPDATETIME("lastupdate", "100"),
    CHANNEL_PAC("pac", "101"),
    CHANNEL_PDC("pdc", "102"),
    CHANNEL_UAC("uac", "103"),
    CHANNEL_UDC("udc", "104"),
    CHANNEL_YIELDDAY("yieldday", "105"),
    CHANNEL_YIELDYESTERDAY("yieldyesterday", "106"),
    CHANNEL_YIELDMONTH("yieldmonth", "107"),
    CHANNEL_YIELDYEAR("yieldyear", "108"),
    CHANNEL_YIELDTOTAL("yieldtotal", "109"),
    CHANNEL_CONSPAC("conspac", "110"),
    CHANNEL_CONSYIELDDAY("consyieldday", "111"),
    CHANNEL_CONSYIELDYESTERDAY("consyieldyesterday", "112"),
    CHANNEL_CONSYIELDMONTH("consyieldmonth", "113"),
    CHANNEL_CONSYIELDYEAR("consyieldyear", "114"),
    CHANNEL_CONSYIELDTOTAL("consyieldtotal", "115"),
    CHANNEL_TOTALPOWER("totalpower", "116");

    private final String id;
    private final String index;

    SolarLogChannel(String id, String index) {
        this.id = id;
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public String getIndex() {
        return index;
    }
}
