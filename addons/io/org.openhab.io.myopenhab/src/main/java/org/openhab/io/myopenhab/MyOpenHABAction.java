/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.myopenhab;

import org.eclipse.smarthome.core.scriptengine.action.ActionDoc;
import org.openhab.io.myopenhab.internal.MyOpenHABService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules
 * for using my.openHAB functionality.
 *
 * @author Victor Belov - Initial contribution
 * @author Kai Kreuzer - migrated code to ESH APIs
 *
 */

public class MyOpenHABAction {

    private static final Logger logger = LoggerFactory.getLogger(MyOpenHABAction.class);

    public static MyOpenHABService myOpenHABService = null;

    /**
     * Sends a simple push notification to mobile devices of user
     *
     * @param userId the my.openHAB user id of the recipient
     * @param message the body of the notification
     *
     */
    @ActionDoc(text = "Sends a push notification to mobile devices of user with userId")
    static public void sendNotification(String userId, String message) {
        sendNotification(userId, message, null, null);
    }

    /**
     * Sends an advanced push notification to mobile devices of user
     *
     * @param userId the my.openHAB user id of the recipient
     * @param message the body of the notification
     * @param icon name for the notification
     * @param severity category for the notification
     *
     */
    @ActionDoc(text = "Sends a push notification to mobile devices of user with userId")
    static public void sendNotification(String userId, String message, String icon, String severity) {
        logger.debug("sending notification '{}' to user {}", message, userId);
        if (myOpenHABService != null)
            myOpenHABService.sendNotification(userId, message, icon, severity);
    }

    /**
     * Sends an SMS to mobile phone of user
     *
     * @param phone the user's phone number in international format like +49XXXXXXXXXX
     * @param message the body of the sms
     *
     */
    @ActionDoc(text = "Sends an SMS to mobile phone of user")
    static public void sendSms(String phone, String message) {
        logger.debug("sending SMS to phone {}", phone);
        if (myOpenHABService != null)
            myOpenHABService.sendSMS(phone, message);
    }

}
