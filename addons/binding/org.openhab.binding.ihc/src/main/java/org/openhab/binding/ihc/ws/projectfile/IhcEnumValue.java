/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.ws.projectfile;

/**
 * Class to store IHC / ELKO LS controller's enum value information.
 *
 * @author Pauli Anttila - Initial contribution
 */
public class IhcEnumValue {
    private int id;
    private String name;

    public IhcEnumValue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("[ id=%d, name='%s' ]", id, name);
    }
}
