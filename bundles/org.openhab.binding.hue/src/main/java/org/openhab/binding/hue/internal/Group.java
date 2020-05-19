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
package org.openhab.binding.hue.internal;

/**
 * Basic group information.
 *
 * @author Q42 - Initial contribution
 * @author Denis Dudnik - moved Jue library source code inside the smarthome Hue binding
 * @author Laurent Garnier - field type added
 */
public class Group {
    private String id;
    private String name;
    private String type;

    Group() {
        this.id = "0";
        this.name = "Lightset 0";
        this.type = "LightGroup";
    }

    void setName(String name) {
        this.name = name;
    }

    void setId(String id) {
        this.id = id;
    }

    void setType(String type) {
        this.type = type;
    }

    /**
     * Returns if the group can be modified.
     * Currently only returns false for the all lights pseudo group.
     *
     * @return modifiability of group
     */
    public boolean isModifiable() {
        return !id.equals("0");
    }

    /**
     * Returns the id of the group.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the group.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the tyoe of the group.
     *
     * @return type
     */
    public String getType() {
        return type;
    }
}
