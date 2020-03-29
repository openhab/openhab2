/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.nest.internal.data;

/**
 * Interface for uniquely identifiable Nest objects (device or a structure).
 *
 * @author Wouter Born - Initial contribution
 * @author Wouter Born - Simplify working with deviceId and structureId
 */
public interface NestIdentifiable {

    /**
     * Returns the identifier that uniquely identifies the Nest object (deviceId or structureId).
     */
    String getId();
}
