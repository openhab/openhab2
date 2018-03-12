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
 * Java class for Options complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Options">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;any processContents='lax'/>
 *       &lt;/sequence>
 *       &lt;attribute name="PreferPartialDownloadIfApplicationLoaded" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="EasyCtrlModeModeStyleEmptyGroupComTables" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SetObjectTableLengthAlwaysToOne" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TextParameterZeroTerminate" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ParameterByteOrder" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyNoPartialDownload" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyNoMemoryVerifyMode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyNoOptimisticWrite" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyDoNotReportPropertyWriteErrors" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyNoBackgroundDownload" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyDoNotCheckManufacturerId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyAlwaysReloadAppIfCoVisibilityChanged" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyNeverReloadAppIfCoVisibilityChanged" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyDoNotSupportUndoDelete" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyAllowPartialDownloadIfAp2Mismatch" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyKeepObjectTableGaps" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PartialDownloadOnlyVisibleParameters" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LegacyProxyCommunicationObjects" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DeviceInfoIgnoreRunState" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DeviceInfoIgnoreLoadedState" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DeviceCompareAllowCompatibleManufacturerId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LineCoupler0912NewProgrammingStyle" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TextParameterEncodingSelector" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DownloadInvisibleParameters" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TextParameterEncoding" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Comparable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Reconstructable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Options", propOrder = { "any" })
public class Options {

    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlAttribute(name = "PreferPartialDownloadIfApplicationLoaded")
    protected java.lang.String preferPartialDownloadIfApplicationLoaded;
    @XmlAttribute(name = "EasyCtrlModeModeStyleEmptyGroupComTables")
    protected java.lang.String easyCtrlModeModeStyleEmptyGroupComTables;
    @XmlAttribute(name = "SetObjectTableLengthAlwaysToOne")
    protected java.lang.String setObjectTableLengthAlwaysToOne;
    @XmlAttribute(name = "TextParameterZeroTerminate")
    protected java.lang.String textParameterZeroTerminate;
    @XmlAttribute(name = "ParameterByteOrder")
    protected java.lang.String parameterByteOrder;
    @XmlAttribute(name = "LegacyNoPartialDownload")
    protected java.lang.String legacyNoPartialDownload;
    @XmlAttribute(name = "LegacyNoMemoryVerifyMode")
    protected java.lang.String legacyNoMemoryVerifyMode;
    @XmlAttribute(name = "LegacyNoOptimisticWrite")
    protected java.lang.String legacyNoOptimisticWrite;
    @XmlAttribute(name = "LegacyDoNotReportPropertyWriteErrors")
    protected java.lang.String legacyDoNotReportPropertyWriteErrors;
    @XmlAttribute(name = "LegacyNoBackgroundDownload")
    protected java.lang.String legacyNoBackgroundDownload;
    @XmlAttribute(name = "LegacyDoNotCheckManufacturerId")
    protected java.lang.String legacyDoNotCheckManufacturerId;
    @XmlAttribute(name = "LegacyAlwaysReloadAppIfCoVisibilityChanged")
    protected java.lang.String legacyAlwaysReloadAppIfCoVisibilityChanged;
    @XmlAttribute(name = "LegacyNeverReloadAppIfCoVisibilityChanged")
    protected java.lang.String legacyNeverReloadAppIfCoVisibilityChanged;
    @XmlAttribute(name = "LegacyDoNotSupportUndoDelete")
    protected java.lang.String legacyDoNotSupportUndoDelete;
    @XmlAttribute(name = "LegacyAllowPartialDownloadIfAp2Mismatch")
    protected java.lang.String legacyAllowPartialDownloadIfAp2Mismatch;
    @XmlAttribute(name = "LegacyKeepObjectTableGaps")
    protected java.lang.String legacyKeepObjectTableGaps;
    @XmlAttribute(name = "PartialDownloadOnlyVisibleParameters")
    protected java.lang.String partialDownloadOnlyVisibleParameters;
    @XmlAttribute(name = "LegacyProxyCommunicationObjects")
    protected java.lang.String legacyProxyCommunicationObjects;
    @XmlAttribute(name = "DeviceInfoIgnoreRunState")
    protected java.lang.String deviceInfoIgnoreRunState;
    @XmlAttribute(name = "DeviceInfoIgnoreLoadedState")
    protected java.lang.String deviceInfoIgnoreLoadedState;
    @XmlAttribute(name = "DeviceCompareAllowCompatibleManufacturerId")
    protected java.lang.String deviceCompareAllowCompatibleManufacturerId;
    @XmlAttribute(name = "LineCoupler0912NewProgrammingStyle")
    protected java.lang.String lineCoupler0912NewProgrammingStyle;
    @XmlAttribute(name = "TextParameterEncodingSelector")
    protected java.lang.String textParameterEncodingSelector;
    @XmlAttribute(name = "DownloadInvisibleParameters")
    protected java.lang.String downloadInvisibleParameters;
    @XmlAttribute(name = "TextParameterEncoding")
    protected java.lang.String textParameterEncoding;
    @XmlAttribute(name = "Comparable")
    protected java.lang.String comparable;
    @XmlAttribute(name = "Reconstructable")
    protected java.lang.String reconstructable;

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
     * Gets the value of the preferPartialDownloadIfApplicationLoaded property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getPreferPartialDownloadIfApplicationLoaded() {
        return preferPartialDownloadIfApplicationLoaded;
    }

    /**
     * Sets the value of the preferPartialDownloadIfApplicationLoaded property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setPreferPartialDownloadIfApplicationLoaded(java.lang.String value) {
        this.preferPartialDownloadIfApplicationLoaded = value;
    }

    /**
     * Gets the value of the easyCtrlModeModeStyleEmptyGroupComTables property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getEasyCtrlModeModeStyleEmptyGroupComTables() {
        return easyCtrlModeModeStyleEmptyGroupComTables;
    }

    /**
     * Sets the value of the easyCtrlModeModeStyleEmptyGroupComTables property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setEasyCtrlModeModeStyleEmptyGroupComTables(java.lang.String value) {
        this.easyCtrlModeModeStyleEmptyGroupComTables = value;
    }

    /**
     * Gets the value of the setObjectTableLengthAlwaysToOne property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getSetObjectTableLengthAlwaysToOne() {
        return setObjectTableLengthAlwaysToOne;
    }

    /**
     * Sets the value of the setObjectTableLengthAlwaysToOne property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setSetObjectTableLengthAlwaysToOne(java.lang.String value) {
        this.setObjectTableLengthAlwaysToOne = value;
    }

    /**
     * Gets the value of the textParameterZeroTerminate property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getTextParameterZeroTerminate() {
        return textParameterZeroTerminate;
    }

    /**
     * Sets the value of the textParameterZeroTerminate property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setTextParameterZeroTerminate(java.lang.String value) {
        this.textParameterZeroTerminate = value;
    }

    /**
     * Gets the value of the parameterByteOrder property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getParameterByteOrder() {
        return parameterByteOrder;
    }

    /**
     * Sets the value of the parameterByteOrder property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setParameterByteOrder(java.lang.String value) {
        this.parameterByteOrder = value;
    }

    /**
     * Gets the value of the legacyNoPartialDownload property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyNoPartialDownload() {
        return legacyNoPartialDownload;
    }

    /**
     * Sets the value of the legacyNoPartialDownload property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyNoPartialDownload(java.lang.String value) {
        this.legacyNoPartialDownload = value;
    }

    /**
     * Gets the value of the legacyNoMemoryVerifyMode property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyNoMemoryVerifyMode() {
        return legacyNoMemoryVerifyMode;
    }

    /**
     * Sets the value of the legacyNoMemoryVerifyMode property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyNoMemoryVerifyMode(java.lang.String value) {
        this.legacyNoMemoryVerifyMode = value;
    }

    /**
     * Gets the value of the legacyNoOptimisticWrite property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyNoOptimisticWrite() {
        return legacyNoOptimisticWrite;
    }

    /**
     * Sets the value of the legacyNoOptimisticWrite property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyNoOptimisticWrite(java.lang.String value) {
        this.legacyNoOptimisticWrite = value;
    }

    /**
     * Gets the value of the legacyDoNotReportPropertyWriteErrors property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyDoNotReportPropertyWriteErrors() {
        return legacyDoNotReportPropertyWriteErrors;
    }

    /**
     * Sets the value of the legacyDoNotReportPropertyWriteErrors property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyDoNotReportPropertyWriteErrors(java.lang.String value) {
        this.legacyDoNotReportPropertyWriteErrors = value;
    }

    /**
     * Gets the value of the legacyNoBackgroundDownload property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyNoBackgroundDownload() {
        return legacyNoBackgroundDownload;
    }

    /**
     * Sets the value of the legacyNoBackgroundDownload property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyNoBackgroundDownload(java.lang.String value) {
        this.legacyNoBackgroundDownload = value;
    }

    /**
     * Gets the value of the legacyDoNotCheckManufacturerId property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyDoNotCheckManufacturerId() {
        return legacyDoNotCheckManufacturerId;
    }

    /**
     * Sets the value of the legacyDoNotCheckManufacturerId property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyDoNotCheckManufacturerId(java.lang.String value) {
        this.legacyDoNotCheckManufacturerId = value;
    }

    /**
     * Gets the value of the legacyAlwaysReloadAppIfCoVisibilityChanged property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyAlwaysReloadAppIfCoVisibilityChanged() {
        return legacyAlwaysReloadAppIfCoVisibilityChanged;
    }

    /**
     * Sets the value of the legacyAlwaysReloadAppIfCoVisibilityChanged property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyAlwaysReloadAppIfCoVisibilityChanged(java.lang.String value) {
        this.legacyAlwaysReloadAppIfCoVisibilityChanged = value;
    }

    /**
     * Gets the value of the legacyNeverReloadAppIfCoVisibilityChanged property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyNeverReloadAppIfCoVisibilityChanged() {
        return legacyNeverReloadAppIfCoVisibilityChanged;
    }

    /**
     * Sets the value of the legacyNeverReloadAppIfCoVisibilityChanged property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyNeverReloadAppIfCoVisibilityChanged(java.lang.String value) {
        this.legacyNeverReloadAppIfCoVisibilityChanged = value;
    }

    /**
     * Gets the value of the legacyDoNotSupportUndoDelete property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyDoNotSupportUndoDelete() {
        return legacyDoNotSupportUndoDelete;
    }

    /**
     * Sets the value of the legacyDoNotSupportUndoDelete property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyDoNotSupportUndoDelete(java.lang.String value) {
        this.legacyDoNotSupportUndoDelete = value;
    }

    /**
     * Gets the value of the legacyAllowPartialDownloadIfAp2Mismatch property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyAllowPartialDownloadIfAp2Mismatch() {
        return legacyAllowPartialDownloadIfAp2Mismatch;
    }

    /**
     * Sets the value of the legacyAllowPartialDownloadIfAp2Mismatch property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyAllowPartialDownloadIfAp2Mismatch(java.lang.String value) {
        this.legacyAllowPartialDownloadIfAp2Mismatch = value;
    }

    /**
     * Gets the value of the legacyKeepObjectTableGaps property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyKeepObjectTableGaps() {
        return legacyKeepObjectTableGaps;
    }

    /**
     * Sets the value of the legacyKeepObjectTableGaps property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyKeepObjectTableGaps(java.lang.String value) {
        this.legacyKeepObjectTableGaps = value;
    }

    /**
     * Gets the value of the partialDownloadOnlyVisibleParameters property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getPartialDownloadOnlyVisibleParameters() {
        return partialDownloadOnlyVisibleParameters;
    }

    /**
     * Sets the value of the partialDownloadOnlyVisibleParameters property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setPartialDownloadOnlyVisibleParameters(java.lang.String value) {
        this.partialDownloadOnlyVisibleParameters = value;
    }

    /**
     * Gets the value of the legacyProxyCommunicationObjects property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLegacyProxyCommunicationObjects() {
        return legacyProxyCommunicationObjects;
    }

    /**
     * Sets the value of the legacyProxyCommunicationObjects property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLegacyProxyCommunicationObjects(java.lang.String value) {
        this.legacyProxyCommunicationObjects = value;
    }

    /**
     * Gets the value of the deviceInfoIgnoreRunState property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getDeviceInfoIgnoreRunState() {
        return deviceInfoIgnoreRunState;
    }

    /**
     * Sets the value of the deviceInfoIgnoreRunState property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setDeviceInfoIgnoreRunState(java.lang.String value) {
        this.deviceInfoIgnoreRunState = value;
    }

    /**
     * Gets the value of the deviceInfoIgnoreLoadedState property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getDeviceInfoIgnoreLoadedState() {
        return deviceInfoIgnoreLoadedState;
    }

    /**
     * Sets the value of the deviceInfoIgnoreLoadedState property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setDeviceInfoIgnoreLoadedState(java.lang.String value) {
        this.deviceInfoIgnoreLoadedState = value;
    }

    /**
     * Gets the value of the deviceCompareAllowCompatibleManufacturerId property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getDeviceCompareAllowCompatibleManufacturerId() {
        return deviceCompareAllowCompatibleManufacturerId;
    }

    /**
     * Sets the value of the deviceCompareAllowCompatibleManufacturerId property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setDeviceCompareAllowCompatibleManufacturerId(java.lang.String value) {
        this.deviceCompareAllowCompatibleManufacturerId = value;
    }

    /**
     * Gets the value of the lineCoupler0912NewProgrammingStyle property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getLineCoupler0912NewProgrammingStyle() {
        return lineCoupler0912NewProgrammingStyle;
    }

    /**
     * Sets the value of the lineCoupler0912NewProgrammingStyle property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setLineCoupler0912NewProgrammingStyle(java.lang.String value) {
        this.lineCoupler0912NewProgrammingStyle = value;
    }

    /**
     * Gets the value of the textParameterEncodingSelector property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getTextParameterEncodingSelector() {
        return textParameterEncodingSelector;
    }

    /**
     * Sets the value of the textParameterEncodingSelector property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setTextParameterEncodingSelector(java.lang.String value) {
        this.textParameterEncodingSelector = value;
    }

    /**
     * Gets the value of the downloadInvisibleParameters property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getDownloadInvisibleParameters() {
        return downloadInvisibleParameters;
    }

    /**
     * Sets the value of the downloadInvisibleParameters property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setDownloadInvisibleParameters(java.lang.String value) {
        this.downloadInvisibleParameters = value;
    }

    /**
     * Gets the value of the textParameterEncoding property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getTextParameterEncoding() {
        return textParameterEncoding;
    }

    /**
     * Sets the value of the textParameterEncoding property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setTextParameterEncoding(java.lang.String value) {
        this.textParameterEncoding = value;
    }

    /**
     * Gets the value of the comparable property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getComparable() {
        return comparable;
    }

    /**
     * Sets the value of the comparable property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setComparable(java.lang.String value) {
        this.comparable = value;
    }

    /**
     * Gets the value of the reconstructable property.
     *
     * @return
     *         possible object is
     *         {@link java.lang.String }
     * 
     */
    public java.lang.String getReconstructable() {
        return reconstructable;
    }

    /**
     * Sets the value of the reconstructable property.
     *
     * @param value
     *            allowed object is
     *            {@link java.lang.String }
     * 
     */
    public void setReconstructable(java.lang.String value) {
        this.reconstructable = value;
    }

}
