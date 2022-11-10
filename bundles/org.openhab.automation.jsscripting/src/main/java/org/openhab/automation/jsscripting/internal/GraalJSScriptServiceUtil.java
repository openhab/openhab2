/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.automation.jsscripting.internal;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.automation.module.script.action.ScriptExecution;
import org.openhab.core.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Utility class for providing easy access to script services.
 *
 * @author Florian Hotze - Initial contribution
 */
@Component(immediate = true)
@NonNullByDefault
public class GraalJSScriptServiceUtil {
    private static @Nullable Scheduler scheduler;
    private static @Nullable ScriptExecution scriptExecution;

    @Activate
    public GraalJSScriptServiceUtil(final @Reference Scheduler scheduler,
            final @Reference ScriptExecution scriptExecution) {
        GraalJSScriptServiceUtil.scheduler = scheduler;
        GraalJSScriptServiceUtil.scriptExecution = scriptExecution;
    }

    public static Scheduler getScheduler() {
        return Objects.requireNonNull(scheduler);
    }

    public static ScriptExecution getScriptExecution() {
        return Objects.requireNonNull(scriptExecution);
    }
}
