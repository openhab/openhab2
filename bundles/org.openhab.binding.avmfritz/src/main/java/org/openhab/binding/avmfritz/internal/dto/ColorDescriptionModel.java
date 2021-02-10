/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.avmfritz.internal.dto;

import javax.xml.bind.annotation.*;

/**
 * This JAXB model class is part of the XML response to an <b>getcolordefaults</b>
 * command on a FRITZ!Box device. As of today, this class is able to to bind the
 * devicelist version 1 (currently used by AVM) response:
 *
 * <pre>
 *
 * @author Joshua Bacher - Initial contribution
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@XmlRootElement(name = "color")
public class ColorDescriptionModel {

    @XmlAttribute(name = "sat_index")
    public short sat_index; // whatever...

    @XmlAttribute(name = "sat")
    public short saturation; // whatever...

    @XmlAttribute(name = "hue")
    public short hue; // whatever...

    @XmlAttribute(name = "val")
    public short brightness; // i hope that's correct.

}
