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
package org.openhab.binding.oppo.internal.communication;

import java.util.EventListener;
import java.util.EventObject;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Oppo Event Listener interface. Handles incoming Oppo message events
 *
 * @author Michael Lobstein - Initial contribution
 */
@NonNullByDefault
public interface OppoMessageEventListener extends EventListener {

    /**
     * Event handler method for incoming Oppo message events
     *
     * @param event the event object
     */
    public void onNewMessageEvent(EventObject event);
}
