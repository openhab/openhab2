/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.pushover.internal.actions;

import static org.openhab.binding.pushover.internal.PushoverBindingConstants.DEFAULT_TITLE;
import static org.openhab.binding.pushover.internal.connection.PushoverMessageBuilder.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.pushover.internal.connection.PushoverMessageBuilder;
import org.openhab.binding.pushover.internal.handler.PushoverAccountHandler;
import org.openhab.core.automation.annotation.ActionInput;
import org.openhab.core.automation.annotation.ActionOutput;
import org.openhab.core.automation.annotation.RuleAction;
import org.openhab.core.thing.binding.ThingActions;
import org.openhab.core.thing.binding.ThingActionsScope;
import org.openhab.core.thing.binding.ThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some automation actions to be used with a {@link PushoverAccountHandler}.
 *
 * @author Christoph Weitkamp - Initial contribution
 */
@ThingActionsScope(name = "pushover")
@NonNullByDefault
public class PushoverActions implements ThingActions {

    private static final String DEFAULT_EMERGENCY_PRIORITY = "2";

    private final Logger logger = LoggerFactory.getLogger(PushoverActions.class);

    private @NonNullByDefault({}) PushoverAccountHandler accountHandler;

    @RuleAction(label = "@text/sendMessageActionLabel", description = "@text/sendMessageActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title) {
        logger.trace("ThingAction 'sendMessage' called with value(s): message='{}', title='{}'", message, title);
        return send(getDefaultPushoverMessageBuilder(message), title);
    }

    public static Boolean sendMessage(ThingActions actions, String message, @Nullable String title) {
        return ((PushoverActions) actions).sendMessage(message, title);
    }

    @RuleAction(label = "@text/sendURLMessageActionLabel", description = "@text/sendURLMessageActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendURLMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title,
            @ActionInput(name = "url", label = "@text/sendMessageActionInputURLLabel", description = "@text/sendMessageActionInputURLDescription", type = "java.lang.String", required = true) String url,
            @ActionInput(name = "urlTitle", label = "@text/sendMessageActionInputURLTitleLabel", description = "@text/sendMessageActionInputURLTitleDescription", type = "java.lang.String") @Nullable String urlTitle) {
        logger.trace(
                "ThingAction 'sendURLMessage' called with value(s): message='{}', url='{}', title='{}', urlTitle='{}'",
                message, url, title, urlTitle);
        if (url == null) {
            throw new IllegalArgumentException("Skip sending message as 'url' is null.");
        }

        PushoverMessageBuilder builder = getDefaultPushoverMessageBuilder(message).withUrl(url);
        if (urlTitle != null) {
            builder.withUrl(urlTitle);
        }
        return send(builder, title);
    }

    public static Boolean sendURLMessage(ThingActions actions, String message, @Nullable String title, String url,
            @Nullable String urlTitle) {
        return ((PushoverActions) actions).sendURLMessage(message, title, url, urlTitle);
    }

    @RuleAction(label = "@text/sendHTMLMessageActionLabel", description = "@text/sendHTMLMessageActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendHtmlMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title) {
        logger.trace("ThingAction 'sendHtmlMessage' called with value(s): message='{}', title='{}'", message, title);
        return send(getDefaultPushoverMessageBuilder(message).withHtmlFormatting(), title);
    }

    public static Boolean sendHtmlMessage(ThingActions actions, String message, @Nullable String title) {
        return ((PushoverActions) actions).sendHtmlMessage(message, title);
    }

    @RuleAction(label = "@text/sendMonospaceMessageActionLabel", description = "@text/sendMonospaceMessageActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendMonospaceMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title) {
        logger.trace("ThingAction 'sendMonospaceMessage' called with value(s): message='{}', title='{}'", message,
                title);
        return send(getDefaultPushoverMessageBuilder(message).withMonospaceFormatting(), title);
    }

    public static Boolean sendMonospaceMessage(ThingActions actions, String message, @Nullable String title) {
        return ((PushoverActions) actions).sendMonospaceMessage(message, title);
    }

    @RuleAction(label = "@text/sendAttachmentMessageActionLabel", description = "@text/sendAttachmentMessageActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendAttachmentMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title,
            @ActionInput(name = "attachment", label = "@text/sendMessageActionInputAttachmentLabel", description = "@text/sendMessageActionInputAttachmentDescription", type = "java.lang.String", required = true) String attachment,
            @ActionInput(name = "contentType", label = "@text/sendMessageActionInputContentTypeLabel", description = "@text/sendMessageActionInputContentTypeDescription", type = "java.lang.String", defaultValue = DEFAULT_CONTENT_TYPE) @Nullable String contentType) {
        logger.trace(
                "ThingAction 'sendAttachmentMessage' called with value(s): message='{}', title='{}', attachment='{}', contentType='{}'",
                message, title, attachment, contentType);
        if (attachment == null) {
            throw new IllegalArgumentException("Skip sending message as 'attachment' is null.");
        }

        PushoverMessageBuilder builder = getDefaultPushoverMessageBuilder(message).withAttachment(attachment);
        if (contentType != null) {
            builder.withContentType(contentType);
        }
        return send(builder, title);
    }

    public static Boolean sendAttachmentMessage(ThingActions actions, String message, @Nullable String title,
            String attachment, @Nullable String contentType) {
        return ((PushoverActions) actions).sendAttachmentMessage(message, title, attachment, contentType);
    }

    @RuleAction(label = "@text/sendPriorityMessageActionLabel", description = "@text/sendPriorityMessageActionDescription")
    public @ActionOutput(name = "receipt", label = "@text/sendPriorityMessageActionOutputLabel", description = "@text/sendPriorityMessageActionOutputDescription", type = "java.lang.String") String sendPriorityMessage(
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title,
            @ActionInput(name = "priority", label = "@text/sendMessageActionInputPriorityLabel", description = "@text/sendMessageActionInputPriorityDescription", type = "java.lang.Integer", defaultValue = DEFAULT_EMERGENCY_PRIORITY) @Nullable Integer priority) {
        logger.trace("ThingAction 'sendPriorityMessage' called with value(s): message='{}', title='{}', priority='{}'",
                message, title, priority);
        PushoverMessageBuilder builder = getDefaultPushoverMessageBuilder(message)
                .withPriority(priority == null ? EMERGENCY_PRIORITY : priority.intValue());

        if (title != null) {
            builder.withTitle(title);
        }
        return accountHandler.sendPriorityMessage(builder);
    }

    public static String sendPriorityMessage(ThingActions actions, String message, @Nullable String title,
            @Nullable Integer priority) {
        return ((PushoverActions) actions).sendPriorityMessage(message, title, priority);
    }

    @RuleAction(label = "@text/cancelPriorityMessageActionLabel", description = "@text/cancelPriorityMessageActionDescription")
    public @ActionOutput(name = "canceled", label = "@text/cancelPriorityMessageActionOutputLabel", description = "@text/cancelPriorityMessageActionOutputDescription", type = "java.lang.Boolean") Boolean cancelPriorityMessage(
            @ActionInput(name = "receipt", label = "@text/cancelPriorityMessageActionInputReceiptLabel", description = "@text/cancelPriorityMessageActionInputReceiptDescription", type = "java.lang.String", required = true) String receipt) {
        logger.trace("ThingAction 'cancelPriorityMessage' called with value(s): '{}'", receipt);
        if (accountHandler == null) {
            throw new RuntimeException("PushoverAccountHandler is null!");
        }

        if (receipt == null) {
            throw new IllegalArgumentException("Skip canceling message as 'receipt' is null.");
        }

        return accountHandler.cancelPriorityMessage(receipt);
    }

    public static Boolean cancelPriorityMessage(ThingActions actions, String receipt) {
        return ((PushoverActions) actions).cancelPriorityMessage(receipt);
    }

    @RuleAction(label = "@text/sendMessageToDeviceActionLabel", description = "@text/sendMessageToDeviceActionDescription")
    public @ActionOutput(name = "sent", label = "@text/sendMessageActionOutputLabel", description = "@text/sendMessageActionOutputDescription", type = "java.lang.Boolean") Boolean sendMessageToDevice(
            @ActionInput(name = "device", label = "@text/sendMessageActionInputDeviceLabel", description = "@text/sendMessageActionInputDeviceDescription", type = "java.lang.String", required = true) String device,
            @ActionInput(name = "message", label = "@text/sendMessageActionInputMessageLabel", description = "@text/sendMessageActionInputMessageDescription", type = "java.lang.String", required = true) String message,
            @ActionInput(name = "title", label = "@text/sendMessageActionInputTitleLabel", description = "@text/sendMessageActionInputTitleDescription", type = "java.lang.String", defaultValue = DEFAULT_TITLE) @Nullable String title) {
        logger.trace("ThingAction 'sendMessageToDevice' called with value(s): device='{}', message='{}', title='{}'",
                device, message, title);
        if (device == null) {
            throw new IllegalArgumentException("Skip sending message as 'device' is null.");
        }

        return send(getDefaultPushoverMessageBuilder(message).withDevice(device), title);
    }

    public static Boolean sendMessageToDevice(ThingActions actions, String device, String message,
            @Nullable String title) {
        return ((PushoverActions) actions).sendMessageToDevice(device, message, title);
    }

    private PushoverMessageBuilder getDefaultPushoverMessageBuilder(String message) {
        if (accountHandler == null) {
            throw new RuntimeException("PushoverAccountHandler is null!");
        }

        if (message == null) {
            throw new IllegalArgumentException("Skip sending message as 'message' is null.");
        }

        return accountHandler.getDefaultPushoverMessageBuilder(message);
    }

    private Boolean send(PushoverMessageBuilder builder, @Nullable String title) {
        if (title != null) {
            builder.withTitle(title);
        }
        return accountHandler.sendMessage(builder);
    }

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        this.accountHandler = (PushoverAccountHandler) handler;
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return accountHandler;
    }
}
