/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelink.internal.config;

/**
 * Configuration for a LaCrossTemperatureSensorHandler.
 *
 * @author Volker Bier - Initial contribution
 */
public class LaCrosseTemperatureSensorConfig extends JeeLinkSensorConfig {
    public float minTemp;
    public float maxTemp;
    public float maxDiff;
}
