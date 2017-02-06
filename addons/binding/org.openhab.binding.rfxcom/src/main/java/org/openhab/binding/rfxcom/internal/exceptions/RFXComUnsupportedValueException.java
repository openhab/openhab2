/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.exceptions;

/**
 * Exception for when RFXCOM messages have a value that we don't understand.
 *
 * @author James Hewitt-Thomas - Initial contribution
 */
public class RFXComUnsupportedValueException extends RFXComException {

    private static final long serialVersionUID = 402781611495845169L;

    public RFXComUnsupportedValueException(Class<?> enumeration, String value) {
        super("Unsupported value '" + value + "' for " + enumeration.getSimpleName());
    }

    public RFXComUnsupportedValueException(Class<?> enumeration, int value) {
        this(enumeration, String.valueOf(value));
    }
}
