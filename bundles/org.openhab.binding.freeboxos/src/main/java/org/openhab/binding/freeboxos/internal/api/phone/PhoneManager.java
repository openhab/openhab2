/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.freeboxos.internal.api.phone;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.FreeboxOsSession;
import org.openhab.binding.freeboxos.internal.api.Response;
import org.openhab.binding.freeboxos.internal.api.RestManager;
import org.openhab.binding.freeboxos.internal.api.login.Session.Permission;

/**
 * The {@link PhoneManager} is the Java class used to handle api requests
 * related to phone and calls
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class PhoneManager extends RestManager {
    private static final String PHONE_SUB_PATH = "phone";

    public PhoneManager(FreeboxOsSession session) throws FreeboxException {
        super(PHONE_SUB_PATH, session, Permission.CALLS);
    }

    public List<PhoneStatus> getPhoneStatuses() throws FreeboxException {
        return getList(PhoneStatusResponse.class, getUriBuilder().build());
    }

    public Optional<PhoneStatus> getStatus(int id) throws FreeboxException {
        List<PhoneStatus> statuses = getPhoneStatuses();
        return statuses.stream().filter(status -> status.getId() == id).findFirst();
    }

    public PhoneConfig getConfig() throws FreeboxException {
        return get(PhoneConfigResponse.class, CONFIG_SUB_PATH);
    }

    public void ring(boolean startIt) throws FreeboxException {
        post(String.format("fxs_ring_%s", (startIt ? "start" : "stop")));
    }

    public void activateDect(boolean status) throws FreeboxException {
        PhoneConfig config = getConfig();
        config.setDectEnabled(status);
        put(PhoneConfigResponse.class, CONFIG_SUB_PATH, config);
    }

    public void alternateRing(boolean status) throws FreeboxException {
        PhoneConfig config = getConfig();
        config.setDectRingOnOff(status);
        put(PhoneConfigResponse.class, CONFIG_SUB_PATH, config);
    }

    private class PhoneConfigResponse extends Response<PhoneConfig> {
    }

    private static class PhoneStatusResponse extends Response<List<PhoneStatus>> {
    }
}
