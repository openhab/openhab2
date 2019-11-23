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
package org.openhab.binding.boschshc.internal;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a device status update as represented by the Smart Home Controller.
 *
 * @author Stefan Kästle
 *
 */
public class DeviceStatusUpdate {

    /**
     * {"result":[
     * ..{
     * ...."path":"/devices/hdm:HomeMaticIP:3014F711A0001916D859A8A9/services/PowerSwitch",
     * ...."@type":"DeviceServiceData",
     * ...."id":"PowerSwitch",
     * ...."state":{
     * ......"@type":"powerSwitchState",
     * ......"automaticPowerOffTime":0,
     * ......"switchState":"ON"
     * ....},
     * ...."deviceId":"hdm:HomeMaticIP:3014F711A0001916D859A8A9"}
     * ],"jsonrpc":"2.0"}
     */

    String path;

    @SerializedName("@type")
    String type;

    String id;

    DeviceState state;

    String deviceId;

    @Override
    public String toString() {

        return this.deviceId + "state: " + this.state.type + "/" + this.state.switchState;
    }
}
