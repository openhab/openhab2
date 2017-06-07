/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
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

package org.openhab.binding.knx.internal.parser.knxproj13;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="AddressSpace" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="StartAddress" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="PtrResource" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="InterfaceObjectRef" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="PropertyID" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="Occurrence" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", propOrder = {
    "value"
})
public class Location {

    @XmlValue
    protected java.lang.String value;
    @XmlAttribute(name = "AddressSpace")
    protected java.lang.String addressSpace;
    @XmlAttribute(name = "StartAddress")
    protected Integer startAddress;
    @XmlAttribute(name = "PtrResource")
    protected java.lang.String ptrResource;
    @XmlAttribute(name = "InterfaceObjectRef")
    protected Byte interfaceObjectRef;
    @XmlAttribute(name = "PropertyID")
    protected Byte propertyID;
    @XmlAttribute(name = "Occurrence")
    protected Byte occurrence;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    /**
     * Gets the value of the addressSpace property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getAddressSpace() {
        return addressSpace;
    }

    /**
     * Sets the value of the addressSpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setAddressSpace(java.lang.String value) {
        this.addressSpace = value;
    }

    /**
     * Gets the value of the startAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStartAddress() {
        return startAddress;
    }

    /**
     * Sets the value of the startAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStartAddress(Integer value) {
        this.startAddress = value;
    }

    /**
     * Gets the value of the ptrResource property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getPtrResource() {
        return ptrResource;
    }

    /**
     * Sets the value of the ptrResource property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setPtrResource(java.lang.String value) {
        this.ptrResource = value;
    }

    /**
     * Gets the value of the interfaceObjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getInterfaceObjectRef() {
        return interfaceObjectRef;
    }

    /**
     * Sets the value of the interfaceObjectRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setInterfaceObjectRef(Byte value) {
        this.interfaceObjectRef = value;
    }

    /**
     * Gets the value of the propertyID property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getPropertyID() {
        return propertyID;
    }

    /**
     * Sets the value of the propertyID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setPropertyID(Byte value) {
        this.propertyID = value;
    }

    /**
     * Gets the value of the occurrence property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getOccurrence() {
        return occurrence;
    }

    /**
     * Sets the value of the occurrence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setOccurrence(Byte value) {
        this.occurrence = value;
    }

}
