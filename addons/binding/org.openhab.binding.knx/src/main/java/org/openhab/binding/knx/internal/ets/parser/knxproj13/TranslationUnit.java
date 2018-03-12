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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TranslationUnit complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TranslationUnit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TranslationElement" type="{http://knx.org/xml/project/13}TranslationElement" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RefId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Version" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TranslationUnit", propOrder = { "translationElement" })
public class TranslationUnit {

    @XmlElement(name = "TranslationElement")
    protected List<TranslationElement> translationElement;
    @XmlAttribute(name = "RefId")
    protected java.lang.String refId;
    @XmlAttribute(name = "Version")
    protected Byte version;

    /**
     * Gets the value of the translationElement property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the translationElement property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTranslationElement().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TranslationElement }
     *
     *
     */
    public List<TranslationElement> getTranslationElement() {
        if (translationElement == null) {
            translationElement = new ArrayList<TranslationElement>();
        }
        return this.translationElement;
    }

    /**
     * Gets the value of the refId property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getRefId() {
        return refId;
    }

    /**
     * Sets the value of the refId property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setRefId(java.lang.String value) {
        this.refId = value;
    }

    /**
     * Gets the value of the version property.
     *
     * @return
     *         possible object is
     *         {@link Byte }
     * 
     */
    public Byte getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *            allowed object is
     *            {@link Byte }
     * 
     */
    public void setVersion(Byte value) {
        this.version = value;
    }

}
