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
import org.eclipse.jdt.annotation.Nullable;

/**
 * Model for content of a group
 *
 * @author Christian Kittel - Initial contribution
 */
@NonNullByDefault
public class GroupContent {

    public int action;

    public int groupId;

    public String groupName = "";

    public List<Thermostat> thermostats = new ArrayList<Thermostat>();

    public int regulationMode;

    public @Nullable Schedule schedule;

    public int comfortSetpoint;

    public String comfortEndTime = "";

    public int manualModeSetpoint;

    public boolean vacationEnabled;

    public String vacationBeginDay = "";

    public String vacationEndDay = "";

    public int vacationTemperature;

    public boolean lastPrimaryModeIsAuto;

    public String boostEndTime = "";

    public int frostProtectionTemperature;
}
