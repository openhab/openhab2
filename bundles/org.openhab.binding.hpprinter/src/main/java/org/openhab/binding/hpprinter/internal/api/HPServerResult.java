/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.hpprinter.internal.api;

 /**
 * The {@link HPEmbeddedWebServerResult} is responsible for returning the reading of data from the HP Embedded Web Server
 *
 * @author Stewart Cossey - Initial contribution
 */
public class HPServerResult<result> {

    public HPServerResult(requestStatus status) {
        this.status = status;
        this.data = null;
    }

    public HPServerResult(result data) {
        this.status = requestStatus.SUCCESS;
        this.data = data;
    }
    
    public enum requestStatus {
        SUCCESS,
        TIMEOUT,
        ERROR
    }

    private result data;
    public result getData() {
        return data;
    }

    private requestStatus status;
    public requestStatus getStatus() {
        return status;
    }

}