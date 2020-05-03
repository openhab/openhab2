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
package org.openhab.binding.somfymylink.internal.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Chris Johnson - Initial contribution
 */
public class SomfyMyLinkCommandBase {

    public SomfyMyLinkCommandBase() {
        this.id = ThreadLocalRandom.current().nextInt(1, 1000);
    }
        
    int id;
}
