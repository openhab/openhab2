/**
 * Copyright (c) 2010-2025 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velux.internal.bridge.common;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * <B>Common bridge communication message scheme supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * Message semantic will be defined by the implementations according to the different comm paths.
 * <P>
 * In addition to the common methods defined by {@link BridgeCommunicationProtocol}
 * each protocol-specific implementation has to provide the following methods:
 * <UL>
 * <LI>{@link #setActuatorIdAndMinimumLimitation} for defining the intended actuator and the minimum limitation
 * value,</LI>
 * <LI>{@link #setActuatorIdAndMaximumLimitation} for defining the intended actuator and the maximum limitation
 * value.</LI>
 * </UL>
 *
 * @see BridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 */
@NonNullByDefault
public abstract class SetProductLimitation implements BridgeCommunicationProtocol {

    /**
     * Set the intended node identifier to be queried
     *
     * @param nodeId Gateway internal node identifier (zero to 199).
     * @param limitationMinimum Minimum Restriction value.
     */
    public abstract void setActuatorIdAndMinimumLimitation(int nodeId, int limitationMinimum);

    /**
     * Set the intended node identifier to be queried
     *
     * @param nodeId Gateway internal node identifier (zero to 199).
     * @param limitationMaximum Maximum Restriction value.
     */
    public abstract void setActuatorIdAndMaximumLimitation(int nodeId, int limitationMaximum);
}
