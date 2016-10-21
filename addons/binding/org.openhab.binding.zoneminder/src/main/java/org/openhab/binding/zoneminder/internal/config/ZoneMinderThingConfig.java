/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zoneminder.internal.config;

import org.openhab.binding.zoneminder.ZoneMinderConstants;

//import org.apache.commons.lang.builder.ToStringBuilder;
//import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Thing configuration from openHab.
 *
 * @author Martin S. Eskildsen - Initial contribution
 */
public abstract class ZoneMinderThingConfig extends ZoneMinderConfig {

    public abstract String getZoneMinderId();

    @Override
    public String getConfigId() {
        return ZoneMinderConstants.THING_ZONEMINDER_MONITOR;
    }

}
