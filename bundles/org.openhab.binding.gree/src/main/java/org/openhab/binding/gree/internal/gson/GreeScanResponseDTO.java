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
package org.openhab.binding.gree.internal.gson;

/**
 *
 * The GreeScanResponseDTO class is used by Gson to hold values returned by
 * the Air Conditioner during Scan Requests to the Air Conditioner.
 *
 * @author John Cunha - Initial contribution
 */
public class GreeScanResponseDTO extends GreeResponseBaseDTO {
    public transient GreeScanResponsePackDTO packJson = null;
}
