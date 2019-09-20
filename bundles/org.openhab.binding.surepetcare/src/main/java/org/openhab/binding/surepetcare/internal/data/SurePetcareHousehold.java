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
package org.openhab.binding.surepetcare.internal.data;

import java.util.Date;

/**
 * The {@link SurePetcareHousehold} is the Java class used as a DTO to represent a Sure Petcare Household.
 *
 * @author Rene Scherer - Initial contribution
 */
public class SurePetcareHousehold extends SurePetcareBaseObject {

    // Example:
    // {
    // 'id':34452,
    // 'name':'My Home',
    // 'share_code':'HDghsHj7D22sG2sP',
    // 'timezone_id':340,
    // 'version':'MA==',
    // 'created_at':'2019-09-02T08:20:45+00:00',
    // 'updated_at':'2019-09-02T08:20:48+00:00',
    // 'timezone':{
    // 'id':340,
    // 'name':'(UTC+01:00) Europe/London',
    // 'timezone':'Europe/London',
    // 'utc_offset':3600,
    // 'created_at':'2017-08-03T08:35:34+00:00',
    // 'updated_at':'2017-08-03T08:37:15+00:00'
    // }
    // }

    // Commented members indicate properties returned by the API not used by the binding

    public class Timezone {
        public Integer id;
        public String name;
        public String timezone;
        public Integer utc_offset;
        public Date created_at;
        public Date updated_at;
    }

    private String name;
    private String share_code;
    private Integer timezone_id;
    private Timezone timezone = new Timezone();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public Integer getTimezone_id() {
        return timezone_id;
    }

    public void setTimezone_id(Integer timezone_id) {
        this.timezone_id = timezone_id;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "SurePetcareHousehold [id=" + id + ", name=" + name + "]";
    }

}
