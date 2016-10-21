/**
 * Copyright (c) 2014-2016 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zoneminder.internal.data;

import org.openhab.binding.zoneminder.internal.api.MonitorDaemonStatus;
import org.openhab.binding.zoneminder.internal.api.MonitorData;

public class ZoneMinderMonitorData extends ZoneMinderData {

    private MonitorData _monitor = null;
    private MonitorDaemonStatus _captureDaemonStatus = null;
    private MonitorDaemonStatus _analysisDaemonStatus = null;
    private MonitorDaemonStatus _frameDaemonStatus = null;

    public ZoneMinderMonitorData(MonitorData monitor, MonitorDaemonStatus captureDaemonStatus,
            MonitorDaemonStatus analysisDaemonStatus, MonitorDaemonStatus frameDaemonStatus) {

        this._monitor = monitor;
        this._captureDaemonStatus = captureDaemonStatus;
        this._analysisDaemonStatus = analysisDaemonStatus;
        this._frameDaemonStatus = frameDaemonStatus;

    }

    public String getName() {
        return _monitor.getName();
    }

    public String getEnabled() {
        return _monitor.getEnabled();
    }

    public String getFunction() {
        return _monitor.getFunction();
    }

    public Boolean getCaptureDaemonRunningState() {
        return _captureDaemonStatus.getStatus();
    }

    public String getCaptureDaemonStatusText() {
        return _captureDaemonStatus.getStatustext();
    }

    public Boolean getAnalysisDaemonRunningState() {
        return _analysisDaemonStatus.getStatus();
    }

    public String getAnalysisDaemonStatusText() {
        return _analysisDaemonStatus.getStatustext();
    }

    public Boolean getFrameDaemonRunningState() {
        return _frameDaemonStatus.getStatus();
    }

    public String getFrameDaemonStatusText() {
        return _frameDaemonStatus.getStatustext();
    }
}
