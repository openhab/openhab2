/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc2.internal.ws.datatypes;

/**
 * <p>
 * Java class for WSSceneDimmerValue complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="WSSceneDimmerValue">
 *   &lt;complexContent>
 *     &lt;extension base="{utcs.values}WSResourceValue">
 *       &lt;sequence>
 *         &lt;element name="delayTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="dimmerPercentage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rampTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
/**
 * IHC WSSceneDimmerValue data value.
 *
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class WSSceneDimmerValue extends WSResourceValue {

    protected int delayTime;
    protected int dimmerPercentage;
    protected int rampTime;

    /**
     * Gets the value of the delayTime property.
     *
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * Sets the value of the delayTime property.
     *
     */
    public void setDelayTime(int value) {
        this.delayTime = value;
    }

    /**
     * Gets the value of the dimmerPercentage property.
     *
     */
    public int getDimmerPercentage() {
        return dimmerPercentage;
    }

    /**
     * Sets the value of the dimmerPercentage property.
     *
     */
    public void setDimmerPercentage(int value) {
        this.dimmerPercentage = value;
    }

    /**
     * Gets the value of the rampTime property.
     *
     */
    public int getRampTime() {
        return rampTime;
    }

    /**
     * Sets the value of the rampTime property.
     *
     */
    public void setRampTime(int value) {
        this.rampTime = value;
    }

}
