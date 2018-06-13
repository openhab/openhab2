/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc2.internal.ws;

import java.util.EventListener;
import java.util.EventObject;

import org.openhab.binding.ihc2.internal.ws.datatypes.WSControllerState;
import org.openhab.binding.ihc2.internal.ws.datatypes.WSResourceValue;

/**
 * This interface defines interface to receive updates from IHC controller.
 *
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface Ihc2EventListener extends EventListener {

    /**
     * Event for receive status update from IHC controller.
     *
     * @param status
     *            Received status update from controller.
     */
    void statusUpdateReceived(EventObject event, WSControllerState status);

    /**
     * Event for receive resource value updates from IHC controller.
     *
     * @param value
     *            Received value update from controller.
     */
    void resourceValueUpdateReceived(EventObject event, WSResourceValue value);

    /**
     * Event for fatal error on communication to IHC controller.
     *
     * @param e
     *            IhcException occurred.
     */
    void errorOccured(EventObject event, Ihc2Execption e);

}
