/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcontrol.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 * @author Mark Herwege - Initial contribution
 */
@NonNullByDefault
class UpnpEntryRes {

    private String protocolInfo;
    private @Nullable Long size;
    private String duration;
    private String importUri;
    private String res = "";

    UpnpEntryRes(String protocolInfo, @Nullable Long size, @Nullable String duration, @Nullable String importUri) {
        this.protocolInfo = protocolInfo;
        this.size = size;
        this.duration = (duration == null) ? "" : duration;
        this.importUri = (importUri == null) ? "" : importUri;
    }

    /**
     * @return the res
     */
    public String getRes() {
        return res;
    }

    /**
     * @param res the res to set
     */
    public void setRes(String res) {
        this.res = res;
    }

    public String getProtocolInfo() {
        return protocolInfo;
    }

    /**
     * @return the size
     */
    public @Nullable Long getSize() {
        return size;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @return the importUri
     */
    public String getImportUri() {
        return importUri;
    }

    /**
     * @return true if this resource defines a thumbnail as specified in the DLNA specs
     */
    public boolean isThumbnailRes() {
        return getProtocolInfo().toLowerCase().contains("dlna.org_pn=jpeg_tn");
    }
}
