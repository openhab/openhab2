/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The KM200ThingType enum is representing the things
 *
 * @author Markus Eckhardt - Initial contribution
 *
 */
public enum KM200ThingType {
    GATEWAY("/gateway", KM200BindingConstants.THING_TYPE_GATEWAY) {
        @Override
        public List<String> asBridgeProperties() {
            List<String> asProperties = new ArrayList<String>();
            asProperties.add("versionFirmware");
            asProperties.add("instAccess");
            asProperties.add("versionHardware");
            asProperties.add("uuid");
            asProperties.add("instWriteAccess");
            asProperties.add("openIPAccess");
            return asProperties;
        }
    },

    DHWCIRCUIT("/dhwCircuits", KM200BindingConstants.THING_TYPE_DHW_CIRCUIT) {
        @Override
        public List<String> ignoreSubService() {
            List<String> subServices = new ArrayList<String>();
            subServices.add("switchPrograms");
            return subServices;
        }

        @Override
        public String getActiveCheckSubPath() {
            return "status";
        }
    },

    HEATINGCIRCUIT("/heatingCircuits", KM200BindingConstants.THING_TYPE_HEATING_CIRCUIT) {
        @Override
        public List<String> ignoreSubService() {
            List<String> subServices = new ArrayList<String>();
            subServices.add("switchPrograms");
            return subServices;
        }

        @Override
        public String getActiveCheckSubPath() {
            return "status";
        }
    },

    HEATSOURCE("/heatSources", KM200BindingConstants.THING_TYPE_HEAT_SOURCE),

    SOLARCIRCUIT("/solarCircuits", KM200BindingConstants.THING_TYPE_SOLAR_CIRCUIT) {
        @Override
        public String getActiveCheckSubPath() {
            return "status";
        }
    },

    APPLIANCE("/system/appliance", KM200BindingConstants.THING_TYPE_SYSTEM_APPLIANCE),

    HOLIDAYMODES("/system/holidayModes", KM200BindingConstants.THING_TYPE_SYSTEM_HOLIDAYMODES),

    NOTIFICATIONS("/notifications", KM200BindingConstants.THING_TYPE_NOTIFICATION),

    SENSOR("/system/sensors", KM200BindingConstants.THING_TYPE_SYSTEM_SENSOR),

    SYSTEM("/system", KM200BindingConstants.THING_TYPE_SYSTEM) {
        @Override
        public List<String> ignoreSubService() {
            List<String> subServices = new ArrayList<String>();
            subServices.add("sensors");
            subServices.add("appliance");
            subServices.add("holidayModes");
            return subServices;
        }

        @Override
        public List<String> asBridgeProperties() {
            List<String> asProperties = new ArrayList<String>();
            asProperties.add("bus");
            asProperties.add("systemType");
            asProperties.add("brand");
            asProperties.add("info");
            return asProperties;
        }
    },

    SWITCHPROGRAM("", KM200BindingConstants.THING_TYPE_SWITCH_PROGRAM);

    public final String rootPath;

    public final ThingTypeUID thingTypeUID;

    KM200ThingType(String rootPath, ThingTypeUID thingTypeUID) {
        this.rootPath = rootPath;
        this.thingTypeUID = thingTypeUID;
    }

    public String getRootPath() {
        return rootPath;
    }

    public ThingTypeUID getThingTypeUID() {
        return thingTypeUID;
    }

    public List<String> ignoreSubService() {
        return Collections.emptyList();
    }

    public String getActiveCheckSubPath() {
        return null;
    }

    public List<String> asBridgeProperties() {
        return Collections.emptyList();
    }
}
