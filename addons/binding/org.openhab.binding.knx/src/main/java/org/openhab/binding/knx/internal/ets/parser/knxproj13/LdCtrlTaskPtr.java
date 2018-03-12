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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * Java class for LdCtrlTaskPtr complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LdCtrlTaskPtr">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="LsmIdx" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="InitPtr" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="SavePtr" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="SerialPtr" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LdCtrlTaskPtr", propOrder = { "value" })
public class LdCtrlTaskPtr {

    @XmlValue
    protected java.lang.String value;
    @XmlAttribute(name = "LsmIdx")
    protected Byte lsmIdx;
    @XmlAttribute(name = "InitPtr")
    protected Short initPtr;
    @XmlAttribute(name = "SavePtr")
    protected Short savePtr;
    @XmlAttribute(name = "SerialPtr")
    protected Byte serialPtr;

    /**
     * Gets the value of the value property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    /**
     * Gets the value of the lsmIdx property.
     *
     * @return
     *         possible object is
     *         {@link Byte }
     * 
     */
    public Byte getLsmIdx() {
        return lsmIdx;
    }

    /**
     * Sets the value of the lsmIdx property.
     *
     * @param value
     *            allowed object is
     *            {@link Byte }
     * 
     */
    public void setLsmIdx(Byte value) {
        this.lsmIdx = value;
    }

    /**
     * Gets the value of the initPtr property.
     *
     * @return
     *         possible object is
     *         {@link Short }
     * 
     */
    public Short getInitPtr() {
        return initPtr;
    }

    /**
     * Sets the value of the initPtr property.
     *
     * @param value
     *            allowed object is
     *            {@link Short }
     * 
     */
    public void setInitPtr(Short value) {
        this.initPtr = value;
    }

    /**
     * Gets the value of the savePtr property.
     *
     * @return
     *         possible object is
     *         {@link Short }
     * 
     */
    public Short getSavePtr() {
        return savePtr;
    }

    /**
     * Sets the value of the savePtr property.
     *
     * @param value
     *            allowed object is
     *            {@link Short }
     * 
     */
    public void setSavePtr(Short value) {
        this.savePtr = value;
    }

    /**
     * Gets the value of the serialPtr property.
     *
     * @return
     *         possible object is
     *         {@link Byte }
     * 
     */
    public Byte getSerialPtr() {
        return serialPtr;
    }

    /**
     * Sets the value of the serialPtr property.
     *
     * @param value
     *            allowed object is
     *            {@link Byte }
     * 
     */
    public void setSerialPtr(Byte value) {
        this.serialPtr = value;
    }

}
