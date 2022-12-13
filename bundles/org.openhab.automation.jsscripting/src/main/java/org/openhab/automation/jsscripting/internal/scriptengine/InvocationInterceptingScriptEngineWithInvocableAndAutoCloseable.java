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

package org.openhab.automation.jsscripting.internal.scriptengine;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Delegate allowing AOP-style interception of calls, either before Invocation, or upon a {@link ScriptException}.
 * being thrown.
 *
 * @param <T> The delegate class
 * @author Jonathan Gilbert - Initial contribution
 */
public abstract class InvocationInterceptingScriptEngineWithInvocableAndAutoCloseable<T extends ScriptEngine & Invocable & AutoCloseable>
        extends DelegatingScriptEngineWithInvocableAndAutocloseable<T> {

    public InvocationInterceptingScriptEngineWithInvocableAndAutoCloseable(T delegate) {
        super(delegate);
    }

    protected void beforeInvocation() {
    }

    protected Object afterInvocation(Object obj) {
        return obj;
    }

    protected ScriptException afterThrowsInvocation(ScriptException se) {
        return se;
    }

    @Override
    public Object eval(String s, ScriptContext scriptContext) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(s, scriptContext));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(reader, scriptContext));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object eval(String s) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(s));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(reader));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object eval(String s, Bindings bindings) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(s, bindings));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        try {
            beforeInvocation();
            return afterInvocation(super.eval(reader, bindings));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        }
    }

    @Override
    public Object invokeMethod(Object o, String s, Object... objects)
            throws ScriptException, NoSuchMethodException, NullPointerException, IllegalArgumentException {
        try {
            beforeInvocation();
            return afterInvocation(super.invokeMethod(o, s, objects));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        } catch (Exception e) { // Make sure to unlock on exceptions from Invocable.invokeMethod to avoid deadlocks
            // These exceptions shouldn't be handled by the OpenHABGraalJSScriptEngine, so don't use
            // afterThrowsInvocation
            if (e instanceof NoSuchMethodException) {
                throw (NoSuchMethodException) afterInvocation(e);
            } else if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) afterInvocation(e);
            }
            return afterInvocation(e); // Avoid "missing return statement" warnings
        }
    }

    @Override
    public Object invokeFunction(String s, Object... objects)
            throws ScriptException, NoSuchMethodException, NullPointerException {
        try {
            beforeInvocation();
            return afterInvocation(super.invokeFunction(s, objects));
        } catch (ScriptException se) {
            throw afterThrowsInvocation(se);
        } catch (Exception e) { // Make sure to unlock on exceptions from Invocable.invokeFunction to avoid deadlocks
            // These exceptions shouldn't be handled by the OpenHABGraalJSScriptEngine, so don't use
            // afterThrowsInvocation
            if (e instanceof NoSuchMethodException) {
                throw (NoSuchMethodException) afterInvocation(e);
            }
            return afterInvocation(e); // Avoid "missing return statement" warnings
        }
    }
}
