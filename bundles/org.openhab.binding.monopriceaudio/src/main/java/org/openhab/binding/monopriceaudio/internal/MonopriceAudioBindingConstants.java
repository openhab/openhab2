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
package org.openhab.binding.monopriceaudio.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link MonopriceAudioBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Michael Lobstein - Initial contribution
 */
@NonNullByDefault
public class MonopriceAudioBindingConstants {

    private static final String BINDING_ID = "monopriceaudio";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_AMP = new ThingTypeUID(BINDING_ID, "amplifier");

    // List of all Channel types
    public static final String CHANNEL_TYPE_POWER = "power";
    public static final String CHANNEL_TYPE_SOURCE = "source";
    public static final String CHANNEL_TYPE_VOLUME = "volume";
    public static final String CHANNEL_TYPE_MUTE = "mute";
    public static final String CHANNEL_TYPE_TREBLE = "treble";
    public static final String CHANNEL_TYPE_BASS = "bass";
    public static final String CHANNEL_TYPE_BALANCE = "balance";
    public static final String CHANNEL_TYPE_DND = "dnd";
    public static final String CHANNEL_TYPE_PAGE = "page";
    public static final String CHANNEL_TYPE_KEYPAD = "keypad";
    public static final String CHANNEL_TYPE_ALLON = "allon";
    public static final String CHANNEL_TYPE_ALLOFF = "alloff";
    public static final String CHANNEL_TYPE_ALLSOURCE = "allsource";
    public static final String CHANNEL_TYPE_ALLVOLUME = "allvolume";
    public static final String CHANNEL_TYPE_ALLMUTE = "allmute";

    // make a list of all channel types
    public static ArrayList<String> channelTypes = new ArrayList<String>( 
            Arrays.asList(CHANNEL_TYPE_POWER, CHANNEL_TYPE_SOURCE, CHANNEL_TYPE_VOLUME,
                          CHANNEL_TYPE_MUTE, CHANNEL_TYPE_TREBLE, CHANNEL_TYPE_BASS,
                          CHANNEL_TYPE_BALANCE, CHANNEL_TYPE_DND, CHANNEL_TYPE_PAGE,
                          CHANNEL_TYPE_KEYPAD));

}
