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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ApplicationPrograms complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ApplicationPrograms">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApplicationProgram" type="{http://knx.org/xml/project/13}ApplicationProgram"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationPrograms", propOrder = { "applicationProgram" })
public class ApplicationPrograms {

    @XmlElement(name = "ApplicationProgram", required = true)
    protected ApplicationProgram applicationProgram;

    /**
     * Gets the value of the applicationProgram property.
     *
     * @return
     *         possible object is
     *         {@link ApplicationProgram }
     * 
     */
    public ApplicationProgram getApplicationProgram() {
        return applicationProgram;
    }

    /**
     * Sets the value of the applicationProgram property.
     *
     * @param value
     *            allowed object is
     *            {@link ApplicationProgram }
     * 
     */
    public void setApplicationProgram(ApplicationProgram value) {
        this.applicationProgram = value;
    }

}
