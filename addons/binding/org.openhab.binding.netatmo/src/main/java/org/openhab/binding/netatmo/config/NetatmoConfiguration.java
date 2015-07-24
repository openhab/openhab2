/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.config;

/**
 * The {@link NetatmoConfiguration} is responsible for holding configuration
 * informations needed to access Netatmo API
 *
 * @author Gaël L'hopital - Initial contribution
 */
public class NetatmoConfiguration {
    public Integer refreshInterval;
    public String clientId;
    public String clientSecret;
    public String refreshToken;
    public String userId;
}
