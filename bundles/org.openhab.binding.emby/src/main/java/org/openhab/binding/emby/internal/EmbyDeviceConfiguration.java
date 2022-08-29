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
package org.openhab.binding.emby.internal;

/**
 * The {@link EmbyDeviceConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Zachary Christiansen - Initial contribution
 */
public class EmbyDeviceConfiguration {

    public String deviceID;
    public String imageMaxWidth;
    public String imageMaxHeight;
    public boolean imagePercentPlayed;
    public String imageImageType;

    public EmbyDeviceConfiguration(String setDeviceID) {
        deviceID = setDeviceID;
        imageMaxWidth = null;
        imageMaxHeight = null;
        imagePercentPlayed = false;
        imageImageType = null;
    }
}
