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
package org.openhab.binding.ftpupload.internal;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link FtpUploadBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Pauli Anttila - Initial contribution
 */
public class FtpUploadBindingConstants {

    public static final String BINDING_ID = "ftpupload";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_IMAGERECEIVER = new ThingTypeUID(BINDING_ID, "imagereceiver");

    // List of all Channel ids
    public final static String IMAGE = "image";
    public final static String IMAGE_RECEIVED_TRIGGER = "image-received";

    // List of all channel parameters
    public final static String PARAM_FILENAME_PATTERN = "filename";

    public final static String EVENT_IMAGE_RECEIVED = "IMAGE_RECEIVED";
}
