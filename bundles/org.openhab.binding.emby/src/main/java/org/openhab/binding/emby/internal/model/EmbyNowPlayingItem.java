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
package org.openhab.binding.emby.internal.model;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * embyPlayState - part of the model for the json object received from the server
 *
 * @author Zachary Christiansen - Initial Contribution
 *
 */
public class EmbyNowPlayingItem {

    @SerializedName("Name")
    private String name;
    @SerializedName("OriginalTitle")
    private String originalTitle;
    @SerializedName("Id")
    private String id;
    @SerializedName("RunTimeTicks")
    private BigDecimal runTimeTicks;
    @SerializedName("Overview")
    private String overview;
    @SerializedName("SeasonId")
    private String seasonId;
    @SerializedName("Type")
    private String nowPlayingType;
    @SerializedName("CurrentProgram")
    private EmbyNowPlayingCurrentProgram currentProgram;
    private final Logger logger = LoggerFactory.getLogger(EmbyNowPlayingItem.class);

    String getName() {
        return name;
    }

    String getSeasonId() {
        return this.seasonId;
    }

    String getNowPlayingType() {
        return nowPlayingType;
    }

    String getOriginalTitle() {
        if (originalTitle.isEmpty()) {
            return name;
        } else {
            return originalTitle;
        }
    }

    /**
     * @return the media source id of the now playing item
     */
    String getId() {
        return this.id;
    }

    /**
     * @return the total runtime ticks of the currently playing item, returns a BigDecimal 0 if this value is not set or
     *         Unknown
     */
    BigDecimal getRunTimeTicks() {
        logger.debug("the media type is {}", nowPlayingType);
        BigDecimal returnValue = new BigDecimal(0);
        switch (nowPlayingType) {
            case "TvChannel":
                returnValue = currentProgram.getRunTimeTicks();
                break;
            case "Recording":

                break;

            case "Episode":
            case "Movie":
                returnValue = runTimeTicks;
                break;
        }
        return returnValue;
    }

    String getOverview() {
        return overview;
    }
}
