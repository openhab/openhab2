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
package org.openhab.binding.teleinfo.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.teleinfo.internal.dto.Frame;

/**
 * The {@link TeleinfoControllerHandlerListener} interface defines all events pushed by a
 * {@link TeleinfoAbstractControllerHandler}.
 *
 * @author Nicolas SIBERIL - Initial contribution
 */
@NonNullByDefault
public interface TeleinfoControllerHandlerListener {

    void onFrameReceived(TeleinfoAbstractControllerHandler controllerHandler, final Frame frame);
}
