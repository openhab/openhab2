/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.live.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Class used to deserialize XML from Telldus Live.
 *
 * @author Jarle Hjortland - Initial contribution
 */
@XmlRootElement(name = "data")
public class DataTypeValue {

    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(value = NameToDataType.class)
    private LiveDataType dataType;

    @XmlAttribute(name = "value")
    private String data;

    private String unit;

    private Integer scale;

    public LiveDataType getName() {
        return dataType;
    }

    public void setName(LiveDataType dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return data;
    }

    public void setValue(String data) {
        this.data = data;
    }

    @XmlAttribute(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlAttribute(name = "scale")
    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "DataTypeValue [dataType=" + dataType + ", data=" + data + ", unit=" + unit + "]";
    }
}
