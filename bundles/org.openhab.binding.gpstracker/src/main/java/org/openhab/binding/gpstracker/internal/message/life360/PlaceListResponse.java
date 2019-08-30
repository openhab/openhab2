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
package org.openhab.binding.gpstracker.internal.message.life360;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The {@link PlaceListResponse} is a Life360 message POJO
 *
 * @author Gabor Bicskei - Initial contribution
 */
public class PlaceListResponse {

    @SerializedName("places")
    private List<PlacesItem> places;

    public void setPlaces(List<PlacesItem> places) {
        this.places = places;
    }

    public List<PlacesItem> getPlaces() {
        return places;
    }

    @Override
    public String toString() {
        return
                "PlaceListResponse{" +
                        "places = '" + places + '\'' +
                        "}";
    }
}
