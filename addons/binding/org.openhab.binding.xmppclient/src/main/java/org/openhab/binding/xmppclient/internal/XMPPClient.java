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
package org.openhab.binding.xmppclient.internal;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.openhab.binding.xmppclient.handler.PublishTriggerChannel;
import org.openhab.binding.xmppclient.handler.XMPPClientMessageSubscriber;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link XMPPClient} is lib foe handle XMPP connection and messagingю
 *
 * @author Pavel Gololobov - Initial contribution
 */
public class XMPPClient implements IncomingChatMessageListener {

    private final Logger logger = LoggerFactory.getLogger(XMPPClient.class);

    private AbstractXMPPConnection connection;

    private Set<XMPPClientMessageSubscriber> subscribers = new HashSet<>();

    public void subscribe(XMPPClientMessageSubscriber channel) {
        logger.info("Channel {} subscribed", channel.getName());
        subscribers.add(channel);
    }

    public void unsubscribe(XMPPClientMessageSubscriber channel) {
        logger.info("Channel {} unsubscribed", channel.getName());
        subscribers.remove(channel);
    }

    public void connect(String host, Integer port, String login, String password, String resource) {
        disconnect();
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(login, password)
                    .setXmppDomain(host)
                    .setResource(resource)
                    .setPort(port)
                    .build();

            connection = new XMPPTCPConnection(config);

            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.enableAutomaticReconnection();

            ReconnectionManager.setEnabledPerDefault(true);

            connection.connect().login();

            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this);
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            logger.error("XMPP CONNECTION ERROR", e);
        }
    }

    public void disconnect() {
        if(connection != null) {
            connection.disconnect();
        }
    }

    public void sendMessage(String to, String message) {
        if(connection == null) {
            logger.error("XMPP CONNECTION IS NULL");
            return;
        }
        try {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this);

            EntityBareJid jid = JidCreate.entityBareFrom(to);
            Chat chat = chatManager.chatWith(jid);
            chat.send(message);
        } catch (XmppStringprepException | SmackException.NotConnectedException | InterruptedException e) {
            logger.error("XMPP SENDING ERROR", e);
        }
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        logger.info("XMPP {} says {}", from.asBareJid().toString(), message.getBody());
        for(XMPPClientMessageSubscriber subscriber : subscribers) {
            logger.info("Push to subscriber {}", subscriber.getName());
            subscriber.processMessage(from.asBareJid().toString(), message.getBody());
        }
    }

}
