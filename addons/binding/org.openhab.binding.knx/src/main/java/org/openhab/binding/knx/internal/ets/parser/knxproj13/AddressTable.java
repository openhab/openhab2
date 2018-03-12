/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
 * See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
 * Any modifications to this file will be lost upon recompilation of the source schema.
 * Generated on: 2017.03.09 at 08:34:29 PM CET
 */

package org.openhab.binding.knx.internal.ets.parser.knxproj13;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

/**
 * <p>
 * Java class for AddressTable complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AddressTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;any processContents='lax'/>
 *       &lt;/sequence>
 *       &lt;attribute name="CodeSegment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Offset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MaxEntries" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressTable", propOrder = { "any" })
public class AddressTable {

    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAttribute(name = "CodeSegment")
    protected java.lang.String codeSegment;
    @XmlAttribute(name = "Offset")
    protected java.lang.String offset;
    @XmlAttribute(name = "MaxEntries")
    protected java.lang.String maxEntries;

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     *
     *
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    /**
     * Gets the value of the codeSegment property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     *
     */
    public java.lang.String getCodeSegment() {
        return codeSegment;
    }

    /**
     * Sets the value of the codeSegment property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     *
     */
    public void setCodeSegment(java.lang.String value) {
        this.codeSegment = value;
    }

    /**
     * Gets the value of the offset property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     *
     */
    public java.lang.String getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     *
     */
    public void setOffset(java.lang.String value) {
        this.offset = value;
    }

    /**
     * Gets the value of the maxEntries property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     *
     */
    public java.lang.String getMaxEntries() {
        return maxEntries;
    }

    /**
     * Sets the value of the maxEntries property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     *
     */
    public void setMaxEntries(java.lang.String value) {
        this.maxEntries = value;
    }

}
