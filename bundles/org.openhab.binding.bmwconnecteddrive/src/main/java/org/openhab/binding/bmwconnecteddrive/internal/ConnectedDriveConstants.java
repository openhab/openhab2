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
package org.openhab.binding.bmwconnecteddrive.internal;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link ConnectedDriveConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ConnectedDriveConstants {

    private static final String BINDING_ID = "bmwconnecteddrive";

    // See constants from bimmer-connected
    // https://github.com/bimmerconnected/bimmer_connected/blob/master/bimmer_connected/vehicle.py
    public enum CarType {
        CONVENTIONAL("CONV"),
        PLUGIN_HYBRID("PHEV"),
        ELECTRIC_REX("BEV_REX"),
        ELECTRIC("BEV");

        private final String type;

        CarType(String s) {
            type = s;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public static final Set<String> FUEL_CARS = new HashSet<String>() {
        {
            add(CarType.CONVENTIONAL.toString());
            add(CarType.PLUGIN_HYBRID.toString());
            add(CarType.ELECTRIC_REX.toString());
        }
    };
    public static final Set<String> ELECTRIC_CARS = new HashSet<String>() {
        {
            add(CarType.ELECTRIC.toString());
            add(CarType.PLUGIN_HYBRID.toString());
            add(CarType.ELECTRIC_REX.toString());
        }
    };

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_CONNECTED_DRIVE_ACCOUNT = new ThingTypeUID(BINDING_ID, "account");
    public static final ThingTypeUID THING_TYPE_CONV = new ThingTypeUID(BINDING_ID, CarType.CONVENTIONAL.toString());
    public static final ThingTypeUID THING_TYPE_PHEV = new ThingTypeUID(BINDING_ID, CarType.PLUGIN_HYBRID.toString());
    public static final ThingTypeUID THING_TYPE_BEV_REX = new ThingTypeUID(BINDING_ID, CarType.ELECTRIC_REX.toString());
    public static final ThingTypeUID THING_TYPE_BEV = new ThingTypeUID(BINDING_ID, CarType.ELECTRIC.toString());
    public static final Set<ThingTypeUID> SUPPORTED_THING_SET = new HashSet<ThingTypeUID>() {
        {
            add(THING_TYPE_CONNECTED_DRIVE_ACCOUNT);
            add(THING_TYPE_CONV);
            add(THING_TYPE_PHEV);
            add(THING_TYPE_BEV_REX);
            add(THING_TYPE_BEV);
        }
    };

    // Bridge Channels
    public static final String DISCOVERY_TRIGGER = "discovery-trigger";
    public static final String DISCOVERY_FINGERPRINT = "discovery-fingerprint";

    // Group definitions
    public static final String CHANNEL_GROUP_PROPERTIES = "properties";
    public static final String CHANNEL_GROUP_RANGE = "range";
    public static final String CHANNEL_GROUP_CAR_STATUS = "status";
    public static final String CHANNEL_GROUP_CAR_IMAGE = "image";

    public static final String PROPERTIES_BRAND = "brand";
    public static final String PROPERTIES_MODEL = "model";
    public static final String PROPERTIES_DRIVETRAIN = "drivetrain";
    public static final String PROPERTIES_BODYTYPE = "body";
    public static final String PROPERTIES_COLOR = "color";
    public static final String PROPERTIES_CONSTRUCTION_YEAR = "construction-year";
    public static final String PROPERTIES_COMMUNITY = "community-statistics";
    public static final String PROPERTIES_ALARM = "alarm";
    public static final String PROPERTIES_DEALER_NAME = "dealer-name";
    public static final String PROPERTIES_DEALER_ADDRESS = "dealer-address";
    public static final String PROPERTIES_DEALER_PHONE = "dealer-phone";
    public static final String PROPERTIES_BREAKDOWN_PHONE = "breakdown-phone";
    public static final String PROPERTIES_ACTIVATED_SERVICES = "activated-services";
    public static final String PROPERTIES_DEACTIVATED_SERVICES = "deactivated-services";
    public static final String PROPERTIES_SUPPORTED_SERVICES = "supported-services";
    public static final String PROPERTIES_NOT_SUPPORTED_SERVICES = "not-supported-services";
    public static final String PROPERTIES_CHARGING_MODES = "charging-modes";

    public static final String IMAGE = "rendered";
    public static final String IMAGE_SIZE = "size";
    public static final String IMAGE_VIEW_DIRECTION = "view-direction";

    public static final String MILEAGE = "mileage";
    public static final String REMAINING_RANGE = "remaining-range";
    public static final String REMAINING_RANGE_ELECTRIC = "remaining-range-electric";
    public static final String REMAINING_SOC = "remaining-soc";
    public static final String REMAINING_RANGE_FUEL = "remaining-range-fuel";
    public static final String REMAINING_FUEL = "remaining-fuel";
    public static final String LAST_UPDATE = "last-update";
}
