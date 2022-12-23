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
package org.openhab.automation.jsscripting.internal.fs.watch;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import org.openhab.automation.jsscripting.internal.GraalJSScriptEngineFactory;
import org.openhab.core.automation.module.script.ScriptDependencyTracker;
import org.openhab.core.automation.module.script.ScriptEngineManager;
import org.openhab.core.automation.module.script.rulesupport.loader.AbstractScriptFileWatcher;
import org.openhab.core.service.ReadyService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Monitors <openHAB-conf>/automation/js for Javascript files, but not libraries
 *
 * @author Jonathan Gilbert - Initial contribution
 */
@Component(immediate = true, service = ScriptDependencyTracker.Listener.class)
public class JSScriptFileWatcher extends AbstractScriptFileWatcher {
    private static final String FILE_DIRECTORY = "automation" + File.separator + "js";

    private final String ignorePath;

    @Activate
    public JSScriptFileWatcher(final @Reference ScriptEngineManager manager,
            final @Reference ReadyService readyService) {
        super(manager, readyService, FILE_DIRECTORY);

        ignorePath = pathToWatch + File.separator + "node_modules";
    }

    @Override
    protected Optional<String> getScriptType(Path scriptFilePath) {
        if (!scriptFilePath.startsWith(ignorePath) && "js".equals(super.getScriptType(scriptFilePath).orElse(null))) {
            return Optional.of(GraalJSScriptEngineFactory.MIME_TYPE);
        } else {
            return Optional.empty();
        }
    }

    @Override
    protected boolean watchSubDirectories() {
        return false;
    }
}
