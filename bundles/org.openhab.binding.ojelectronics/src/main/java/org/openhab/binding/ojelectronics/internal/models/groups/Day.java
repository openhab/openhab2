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
package org.openhab.binding.ojelectronics.internal.models.groups;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for a day
 *
 * @author Christian Kittel - Initial contribution
 */
@NonNullByDefault
public class Day {

    @SerializedName("WeekDayGrpNo")
    @Expose
    public Integer weekDayGrpNo = 0;
    @SerializedName("Events")
    @Expose
    public List<Event> events = new ArrayList<Event>();

}
