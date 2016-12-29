/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.conf;

/**
 * Configuration class for {@link TellstickBridge} bridge used to connect to the
 * Tellus Live service.
 *
 * @author Jarle Hjortland - Initial contribution
 */
public class TelldusLiveConfiguration {
    public String publicKey;
    public String privateKey;
    public String token;
    public String tokenSecret;
    public long refreshInterval;
}
