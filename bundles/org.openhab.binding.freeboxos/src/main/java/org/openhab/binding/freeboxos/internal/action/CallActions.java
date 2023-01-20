/**
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
package org.openhab.binding.freeboxos.internal.action;

<<<<<<< Upstream, based on origin/main
<<<<<<< Upstream, based on origin/main
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.freeboxos.internal.handler.CallHandler;
import org.openhab.core.automation.annotation.RuleAction;
import org.openhab.core.thing.binding.ThingActions;
import org.openhab.core.thing.binding.ThingActionsScope;
import org.openhab.core.thing.binding.ThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {FreeplugActions} class is responsible to call corresponding actions on Freeplugs
 *
 * @author Gaël L'hopital - Initial contribution
 */
@ThingActionsScope(name = "freeboxos")
@NonNullByDefault
public class CallActions implements ThingActions {
    private final Logger logger = LoggerFactory.getLogger(CallActions.class);
    private @Nullable CallHandler handler;

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof CallHandler callHandler) {
            this.handler = callHandler;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return handler;
    }

    @RuleAction(label = "clear call queue", description = "Delete all call logged in the queue")
    public void reset() {
        logger.debug("Call log clear called");
        CallHandler localHandler = handler;
        if (localHandler != null) {
            localHandler.emptyQueue();
        } else {
            logger.warn("Call Action service ThingHandler is null");
        }
=======
import java.util.Optional;

=======
>>>>>>> 089708c Switching to addons.xml, headers updated
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.freeboxos.internal.handler.CallHandler;
import org.openhab.core.automation.annotation.RuleAction;
import org.openhab.core.thing.binding.ThingActions;
import org.openhab.core.thing.binding.ThingActionsScope;
import org.openhab.core.thing.binding.ThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {FreeplugActions} class is responsible to call corresponding actions on Freeplugs
 *
 * @author Gaël L'hopital - Initial contribution
 */
@ThingActionsScope(name = "freeboxos")
@NonNullByDefault
public class CallActions implements ThingActions {
    private final Logger logger = LoggerFactory.getLogger(CallActions.class);
    private @Nullable CallHandler handler;

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof CallHandler) {
            this.handler = (CallHandler) handler;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return handler;
    }

    @RuleAction(label = "clear call queue", description = "Delete all call logged in the queue")
    public void reset() {
        logger.debug("Call log clear called");
<<<<<<< Upstream, based on origin/main
        handler.ifPresentOrElse(CallHandler::emptyQueue,
                () -> logger.warn("Call Action service ThingHandler is null!"));
>>>>>>> 006a813 Saving work before instroduction of ArrayListDeserializer
=======
        if (handler != null) {
            handler.emptyQueue();
        } else {
            logger.warn("Call Action service ThingHandler is null");
        }
>>>>>>> 089708c Switching to addons.xml, headers updated
    }
}
