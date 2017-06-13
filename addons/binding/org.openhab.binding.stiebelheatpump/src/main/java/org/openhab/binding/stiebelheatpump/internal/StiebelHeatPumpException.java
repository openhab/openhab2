/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

/**
 * Exception for Stiebel heat pump errors.
 *
 * @author Peter Kreutzer
 */
public class StiebelHeatPumpException extends Exception {

    public StiebelHeatPumpException() {
        super();
    }

    public StiebelHeatPumpException(String message) {
        super(message);
    }

    public StiebelHeatPumpException(String message, Throwable cause) {
        super(message, cause);
    }

    public StiebelHeatPumpException(Throwable cause) {
        super(cause);
    }

}
