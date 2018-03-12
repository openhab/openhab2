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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ManufacturerMaster complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ManufacturerMaster">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PublicKeys" type="{http://knx.org/xml/project/13}PublicKeys" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="KnxManufacturerId" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DefaultLanguage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ImportRestriction" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ImportGroup" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="CompatibilityGroup" type="{http://www.w3.org/2001/XMLSchema}short" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManufacturerMaster", propOrder = { "content" })
public class ManufacturerMaster {

    @XmlElementRef(name = "PublicKeys", namespace = "http://knx.org/xml/project/13", type = JAXBElement.class, required = false)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "Id")
    protected java.lang.String id;
    @XmlAttribute(name = "KnxManufacturerId")
    protected Short knxManufacturerId;
    @XmlAttribute(name = "Name")
    protected java.lang.String name;
    @XmlAttribute(name = "DefaultLanguage")
    protected java.lang.String defaultLanguage;
    @XmlAttribute(name = "ImportRestriction")
    protected java.lang.String importRestriction;
    @XmlAttribute(name = "ImportGroup")
    protected java.lang.String importGroup;
    @XmlAttribute(name = "CompatibilityGroup")
    protected Short compatibilityGroup;

    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getContent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String }
     * {@link JAXBElement }{@code <}{@link PublicKeys }{@code >}
     *
     *
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

    /**
     * Gets the value of the knxManufacturerId property.
     *
     * @return
     *         possible object is
     *         {@link Short }
     * 
     */
    public Short getKnxManufacturerId() {
        return knxManufacturerId;
    }

    /**
     * Sets the value of the knxManufacturerId property.
     *
     * @param value
     *            allowed object is
     *            {@link Short }
     * 
     */
    public void setKnxManufacturerId(Short value) {
        this.knxManufacturerId = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setName(java.lang.String value) {
        this.name = value;
    }

    /**
     * Gets the value of the defaultLanguage property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Sets the value of the defaultLanguage property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setDefaultLanguage(java.lang.String value) {
        this.defaultLanguage = value;
    }

    /**
     * Gets the value of the importRestriction property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getImportRestriction() {
        return importRestriction;
    }

    /**
     * Sets the value of the importRestriction property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setImportRestriction(java.lang.String value) {
        this.importRestriction = value;
    }

    /**
     * Gets the value of the importGroup property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getImportGroup() {
        return importGroup;
    }

    /**
     * Sets the value of the importGroup property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setImportGroup(java.lang.String value) {
        this.importGroup = value;
    }

    /**
     * Gets the value of the compatibilityGroup property.
     *
     * @return
     *         possible object is
     *         {@link Short }
     * 
     */
    public Short getCompatibilityGroup() {
        return compatibilityGroup;
    }

    /**
     * Sets the value of the compatibilityGroup property.
     *
     * @param value
     *            allowed object is
     *            {@link Short }
     * 
     */
    public void setCompatibilityGroup(Short value) {
        this.compatibilityGroup = value;
    }

}
