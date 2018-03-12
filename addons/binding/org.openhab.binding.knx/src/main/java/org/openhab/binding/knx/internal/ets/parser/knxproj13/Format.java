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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Format complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Format">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Bit" type="{http://knx.org/xml/project/13}Bit"/>
 *         &lt;element name="Ref" type="{http://knx.org/xml/project/13}RefType"/>
 *         &lt;element name="UnsignedInteger" type="{http://knx.org/xml/project/13}UnsignedInteger"/>
 *         &lt;element name="String" type="{http://knx.org/xml/project/13}String"/>
 *         &lt;element name="SignedInteger" type="{http://knx.org/xml/project/13}SignedInteger"/>
 *         &lt;element name="Enumeration" type="{http://knx.org/xml/project/13}Enumeration"/>
 *         &lt;element name="Float" type="{http://knx.org/xml/project/13}Float"/>
 *         &lt;element name="Reserved" type="{http://knx.org/xml/project/13}Reserved"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Format", propOrder = { "bitOrRefOrUnsignedInteger" })
public class Format {

    @XmlElements({ @XmlElement(name = "Bit", type = Bit.class), @XmlElement(name = "Ref", type = RefType.class),
            @XmlElement(name = "UnsignedInteger", type = UnsignedInteger.class),
            @XmlElement(name = "String", type = String.class),
            @XmlElement(name = "SignedInteger", type = SignedInteger.class),
            @XmlElement(name = "Enumeration", type = Enumeration.class),
            @XmlElement(name = "Float", type = Float.class), @XmlElement(name = "Reserved", type = Reserved.class) })
    protected List<Object> bitOrRefOrUnsignedInteger;

    /**
     * Gets the value of the bitOrRefOrUnsignedInteger property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bitOrRefOrUnsignedInteger property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getBitOrRefOrUnsignedInteger().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bit }
     * {@link RefType }
     * {@link UnsignedInteger }
     * {@link String }
     * {@link SignedInteger }
     * {@link Enumeration }
     * {@link Float }
     * {@link Reserved }
     *
     *
     */
    public List<Object> getBitOrRefOrUnsignedInteger() {
        if (bitOrRefOrUnsignedInteger == null) {
            bitOrRefOrUnsignedInteger = new ArrayList<Object>();
        }
        return this.bitOrRefOrUnsignedInteger;
    }

}
