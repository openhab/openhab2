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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Project" type="{http://knx.org/xml/project/13}Project"/>
 *         &lt;element name="ManufacturerData" type="{http://knx.org/xml/project/13}ManufacturerData"/>
 *         &lt;element name="MasterData" type="{http://knx.org/xml/project/13}MasterData"/>
 *       &lt;/choice>
 *       &lt;attribute name="CreatedBy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ToolVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "project",
    "manufacturerData",
    "masterData"
})
@XmlRootElement(name = "KNX")
public class KNX {

    @XmlElement(name = "Project")
    protected Project project;
    @XmlElement(name = "ManufacturerData")
    protected ManufacturerData manufacturerData;
    @XmlElement(name = "MasterData")
    protected MasterData masterData;
    @XmlAttribute(name = "CreatedBy")
    protected java.lang.String createdBy;
    @XmlAttribute(name = "ToolVersion")
    protected java.lang.String toolVersion;

    /**
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link Project }
     *     
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link Project }
     *     
     */
    public void setProject(Project value) {
        this.project = value;
    }

    /**
     * Gets the value of the manufacturerData property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturerData }
     *     
     */
    public ManufacturerData getManufacturerData() {
        return manufacturerData;
    }

    /**
     * Sets the value of the manufacturerData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturerData }
     *     
     */
    public void setManufacturerData(ManufacturerData value) {
        this.manufacturerData = value;
    }

    /**
     * Gets the value of the masterData property.
     * 
     * @return
     *     possible object is
     *     {@link MasterData }
     *     
     */
    public MasterData getMasterData() {
        return masterData;
    }

    /**
     * Sets the value of the masterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MasterData }
     *     
     */
    public void setMasterData(MasterData value) {
        this.masterData = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setCreatedBy(java.lang.String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the toolVersion property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getToolVersion() {
        return toolVersion;
    }

    /**
     * Sets the value of the toolVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setToolVersion(java.lang.String value) {
        this.toolVersion = value;
    }

}
