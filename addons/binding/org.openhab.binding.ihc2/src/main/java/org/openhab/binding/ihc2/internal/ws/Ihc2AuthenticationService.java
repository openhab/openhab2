/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc2.internal.ws;

import org.openhab.binding.ihc2.internal.ws.datatypes.WSLoginResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle IHC / ELKO LS Controller's authentication service.
 *
 * Communication to controller need to be authenticated. On successful
 * authentication Controller returns session id which need to be used further
 * communication.
 *
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class Ihc2AuthenticationService extends Ihc2HttpsClient {

    private final Logger logger = LoggerFactory.getLogger(Ihc2AuthenticationService.class);

    private String url;
    private int timeout;

    Ihc2AuthenticationService(String host) {
        url = "https://" + host + "/ws/AuthenticationService";
    }

    Ihc2AuthenticationService(String host, int timeout) {
        this(host);
        this.timeout = timeout;
        super.setConnectTimeout(timeout);
    }

    public WSLoginResult authenticate(String username, String password, String application) throws Ihc2Execption {
        logger.debug("Open connection");

        final String soapQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<soapenv:Body>" + " <authenticate1 xmlns=\"utcs\">" + "  <password>%s</password>"
                + "  <username>%s</username>" + "  <application>%s</application>" + " </authenticate1>"
                + "</soapenv:Body>" + "</soapenv:Envelope>";

        String query = String.format(soapQuery, password, username, application);

        openConnection(url);
        String response = sendQuery(query, timeout);
        WSLoginResult loginResult = new WSLoginResult();
        loginResult.encodeData(response);
        return loginResult;
    }
}
