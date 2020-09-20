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
package org.openhab.binding.powermax.internal.console;

import java.util.Arrays;
import java.util.List;

import org.openhab.binding.powermax.internal.handler.PowermaxBridgeHandler;
import org.openhab.core.io.console.Console;
import org.openhab.core.io.console.extensions.AbstractConsoleCommandExtension;
import org.openhab.core.io.console.extensions.ConsoleCommandExtension;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link PowermaxCommandExtension} is responsible for handling console commands
 *
 * @author Laurent Garnier - Initial contribution
 */
@Component(service = ConsoleCommandExtension.class)
public class PowermaxCommandExtension extends AbstractConsoleCommandExtension {

    private static final String INFO_SETUP = "info_setup";
    private static final String DOWNLOAD_SETUP = "download_setup";

    private ThingRegistry thingRegistry;

    public PowermaxCommandExtension() {
        super("powermax", "Interact with the Powermax binding.");
    }

    @Override
    public void execute(String[] args, Console console) {
        if (args.length >= 2) {
            Thing thing = null;
            try {
                ThingUID thingUID = new ThingUID(args[0]);
                thing = thingRegistry.get(thingUID);
            } catch (IllegalArgumentException e) {
                thing = null;
            }
            ThingHandler thingHandler = null;
            PowermaxBridgeHandler handler = null;
            if (thing != null) {
                thingHandler = thing.getHandler();
                if (thingHandler instanceof PowermaxBridgeHandler) {
                    handler = (PowermaxBridgeHandler) thingHandler;
                }
            }
            if (thing == null) {
                console.println("Bad thing id '" + args[0] + "'");
                printUsage(console);
            } else if (thingHandler == null) {
                console.println("No handler initialized for the thing id '" + args[0] + "'");
                printUsage(console);
            } else if (handler == null) {
                console.println("'" + args[0] + "' is not a powermax bridge id");
                printUsage(console);
            } else {
                switch (args[1]) {
                    case INFO_SETUP:
                        for (String line : handler.getInfoSetup().split("\n")) {
                            console.println(line);
                        }
                        break;
                    case DOWNLOAD_SETUP:
                        handler.downloadSetup();
                        console.println("Command '" + args[1] + "' handled.");
                        break;
                    default:
                        console.println("Unknown Powermax sub command '" + args[1] + "'");
                        printUsage(console);
                        break;
                }
            }
        } else {
            printUsage(console);
        }
    }

    @Override
    public List<String> getUsages() {
        return Arrays.asList(new String[] { buildCommandUsage("<bridgeUID> " + INFO_SETUP, "information on setup"),
                buildCommandUsage("<bridgeUID> " + DOWNLOAD_SETUP, "download setup") });
    }

    @Reference
    protected void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
    }

    protected void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }
}
