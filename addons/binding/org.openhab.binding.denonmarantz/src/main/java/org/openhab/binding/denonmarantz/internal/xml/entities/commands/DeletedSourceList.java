/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denonmarantz.internal.xml.entities.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Used to unmarshall <list> items of the <functiondelete> CommandRX.
 *
 * @author Jan-Willem Veldhuis - Initial contribution
 */
@XmlRootElement(name = "list")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeletedSourceList {

    private String name;

    private String funcName;

    private Integer use;

    public String getName() {
        return name;
    }

    public String getFuncName() {
        return funcName;
    }

    public Integer getUse() {
        return use;
    }
}
