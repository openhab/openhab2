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
package org.openhab.binding.netatmo.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * {@link CameraAddress} handles the data to address a camera (VPN and local address).
 *
 * @author Sven Strohschein - Initial contribution
 */
@NonNullByDefault
public class CameraAddress {

    private final String vpnURL;
    private final boolean local;
    private @Nullable String localURL;

    CameraAddress(String vpnURL, boolean isLocal, @Nullable String localURL) {
        this.vpnURL = vpnURL;
        this.local = isLocal;
        this.localURL = localURL;
    }

    public String getVpnURL() {
        return vpnURL;
    }

    public @Nullable String getLocalURL() {
        return localURL;
    }

    public boolean isLocal() {
        return local;
    }

    public String getStreamUrl(String videoId) {
        return String.format("%s/vod/%s/%s.m3u8", vpnURL, videoId, local ? "index_local" : "index");
    }

    /**
     * Checks if the VPN URL was changed / isn't equal to the given VPN-URL.
     *
     * @param vpnURL old / known VPN URL
     * @return true, when the VPN URL isn't equal given VPN URL, otherwise false
     */
    public boolean vpnURLChanged(String vpnURL) {
        return !vpnURL.equals(this.vpnURL);
    }
}
