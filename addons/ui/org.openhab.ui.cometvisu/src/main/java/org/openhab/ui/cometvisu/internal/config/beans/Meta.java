/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.17 at 06:25:15 PM CET 
//


package org.openhab.ui.cometvisu.internal.config.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for meta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="meta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="plugins" type="{}plugins"/&gt;
 *         &lt;element name="icons" type="{}icons"/&gt;
 *         &lt;element name="mappings" type="{}mappings"/&gt;
 *         &lt;element name="stylings" type="{}stylings"/&gt;
 *         &lt;element name="statusbar" type="{}statusbar"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "meta", propOrder = {
    "pluginsOrIconsOrMappings"
})
public class Meta {

    @XmlElements({
        @XmlElement(name = "plugins", type = Plugins.class),
        @XmlElement(name = "icons", type = Icons.class),
        @XmlElement(name = "mappings", type = Mappings.class),
        @XmlElement(name = "stylings", type = Stylings.class),
        @XmlElement(name = "statusbar", type = Statusbar.class)
    })
    protected List<Object> pluginsOrIconsOrMappings;

    /**
     * Gets the value of the pluginsOrIconsOrMappings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pluginsOrIconsOrMappings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPluginsOrIconsOrMappings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Plugins }
     * {@link Icons }
     * {@link Mappings }
     * {@link Stylings }
     * {@link Statusbar }
     * 
     * 
     */
    public List<Object> getPluginsOrIconsOrMappings() {
        if (pluginsOrIconsOrMappings == null) {
            pluginsOrIconsOrMappings = new ArrayList<Object>();
        }
        return this.pluginsOrIconsOrMappings;
    }

}
