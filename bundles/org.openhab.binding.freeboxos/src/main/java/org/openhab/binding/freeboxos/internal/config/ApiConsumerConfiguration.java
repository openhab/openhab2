/**
<<<<<<< Upstream, based on origin/main
<<<<<<< Upstream, based on origin/main
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.freeboxos.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class ApiConsumerConfiguration {
    // public static final String PASSWORD = "password";
    // public static final String PORT = "port";
    public static final String REFRESH_INTERVAL = "refreshInterval";

    public int refreshInterval = 30;
    public String password = "";
    // public int port = 24322;
    public boolean acceptAllMp3 = true;
=======
 * Copyright (c) 2010-2022 Contributors to the openHAB project
=======
 * Copyright (c) 2010-2023 Contributors to the openHAB project
>>>>>>> 006a813 Saving work before instroduction of ArrayListDeserializer
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
package org.openhab.binding.freeboxos.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class ApiConsumerConfiguration {
    public int refreshInterval = 30;
>>>>>>> 46dadb1 SAT warnings handling
}
