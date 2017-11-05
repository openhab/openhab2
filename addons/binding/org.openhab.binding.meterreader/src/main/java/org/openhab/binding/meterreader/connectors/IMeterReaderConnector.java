/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.meterreader.connectors;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Specifies the generic method to retrieve SML values from a device
 *
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public interface IMeterReaderConnector<T> {

    /**
     * Establishes the connection against the device and reads native encoded SML informations.
     *
     * @param serialParmeter
     *
     * @return native encoded SML informations from a device.
     * @throws IOException
     */
    T getMeterValues(byte[] initMessage) throws IOException;

    void addValueChangeListener(Consumer<T> changeListener);

    void removeValueChangeListener(Consumer<T> changeListener);

    /**
     * Open connection.
     *
     * @throws IOException
     *
     */
    void openConnection() throws IOException;

    /**
     * Close connection.
     *
     * @throws ConnectorException
     *
     */
    void closeConnection();
}
