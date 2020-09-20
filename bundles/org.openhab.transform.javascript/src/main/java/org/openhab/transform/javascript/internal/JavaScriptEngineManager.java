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
package org.openhab.transform.javascript.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.transform.TransformationException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple cache for compiled JavaScript files.
 *
 * @author Thomas Kordelle - pre compiled scripts
 *
 */
@NonNullByDefault
@Component(service = JavaScriptEngineManager.class)
public class JavaScriptEngineManager {

    private final Logger logger = LoggerFactory.getLogger(JavaScriptEngineManager.class);
    private final ScriptEngineManager manager = new ScriptEngineManager();
    /* keep memory foot print low. max 2 concurrent threads are estimated */
    private final Map<String, CompiledScript> compiledScriptMap = new ConcurrentHashMap<>(4, 0.5f, 2);

    /**
     * Get a pre compiled script {@link CompiledScript} from cache. If it is not in the cache, then load it from
     * storage and put a pre compiled version into the cache.
     *
     * @param filename name of the JavaScript file to load
     * @return a pre compiled script {@link CompiledScript}
     * @throws TransformationException if compile of JavaScript failed
     */
    protected CompiledScript getScript(final String filename) throws TransformationException {
        synchronized (compiledScriptMap) {
            if (compiledScriptMap.containsKey(filename)) {
                logger.debug("Loading JavaScript {} from cache.", filename);
                return compiledScriptMap.get(filename);
            } else {
                final String path = TransformationScriptWatcher.TRANSFORM_FOLDER + File.separator + filename;
                logger.debug("Loading script {} from storage ", path);
                try (final Reader reader = new InputStreamReader(new FileInputStream(path))) {
                    final ScriptEngine engine = manager.getEngineByName("javascript");
                    final CompiledScript cScript = ((Compilable) engine).compile(reader);
                    logger.debug("Putting compiled JavaScript {} to cache.", cScript);
                    compiledScriptMap.put(filename, cScript);
                    return cScript;
                } catch (IOException | ScriptException e) {
                    throw new TransformationException("An error occurred while loading JavaScript. " + e.getMessage(),
                            e);
                }
            }
        }
    }

    /**
     * remove a pre compiled script from cache.
     *
     * @param fileName name of the script file to remove
     */
    protected void removeFromCache(String fileName) {
        logger.debug("Removing JavaScript {} from cache.", fileName);
        compiledScriptMap.remove(fileName);
    }
}
