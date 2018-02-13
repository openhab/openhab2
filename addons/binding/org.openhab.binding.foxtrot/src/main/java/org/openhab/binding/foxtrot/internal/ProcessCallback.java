/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.foxtrot.internal;

/**
 * ProcessCallback.
 *
 * @author Radovan Sninsky
 * @since 2018-02-12 23:15
 */
public interface ProcessCallback {

    void process(String name);
}
