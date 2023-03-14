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
package org.openhab.binding.hue.internal.console;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hue.internal.HueBindingConstants;
import org.openhab.binding.hue.internal.dto.clip2.Resource;
import org.openhab.binding.hue.internal.dto.clip2.ResourceReference;
import org.openhab.binding.hue.internal.dto.clip2.enums.ResourceType;
import org.openhab.binding.hue.internal.handler.Clip2BridgeHandler;
import org.openhab.binding.hue.internal.handler.HueBridgeHandler;
import org.openhab.binding.hue.internal.handler.HueGroupHandler;
import org.openhab.core.io.console.Console;
import org.openhab.core.io.console.ConsoleCommandCompleter;
import org.openhab.core.io.console.StringsCompleter;
import org.openhab.core.io.console.extensions.AbstractConsoleCommandExtension;
import org.openhab.core.io.console.extensions.ConsoleCommandExtension;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingRegistry;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link HueCommandExtension} is responsible for handling console commands
 *
 * @author Laurent Garnier - Initial contribution
 * @author Andrew Fiddian-Green - Added CLIP 2 console commands
 */

@NonNullByDefault
@Component(service = ConsoleCommandExtension.class)
public class HueCommandExtension extends AbstractConsoleCommandExtension implements ConsoleCommandCompleter {

    private static final String FMT_BRIDGE = "Bridge %s \"Philips Hue Bridge\" [ipAddress=\"%s\", applicationKey=\"%s\"] {";
    private static final String FMT_THING = "    Thing %s %s \"%s\" [resourceId=\"%s\"] // %s";
    private static final String FMT_COMMENT = "    // %s things";
    private static final String FMT_APPKEY = "  - Application key: %s";
    private static final String FMT_SCENE = "  %s '%s'";

    private static final String USER_NAME = "username";
    private static final String SCENES = "scenes";
    private static final String APPLICATION_KEY = "applicationkey";
    private static final String THINGS = "things";

    private static final StringsCompleter SUBCMD_COMPLETER = new StringsCompleter(
            List.of(USER_NAME, SCENES, APPLICATION_KEY, THINGS), false);
    private static final StringsCompleter SCENES_COMPLETER = new StringsCompleter(List.of(SCENES), false);

    private final ThingRegistry thingRegistry;

    public static final Set<ResourceType> SUPPORTED_RESOURCES = Set.of(ResourceType.DEVICE, ResourceType.GROUPED_LIGHT);

    @Activate
    public HueCommandExtension(final @Reference ThingRegistry thingRegistry) {
        super("hue", "Interact with the Hue binding.");
        this.thingRegistry = thingRegistry;
    }

    @Override
    public void execute(String[] args, Console console) {
        if (args.length == 2) {
            Thing thing = getThing(args[0]);
            ThingHandler thingHandler = null;
            HueBridgeHandler bridgeHandler = null;
            HueGroupHandler groupHandler = null;
            Clip2BridgeHandler clip2BridgeHandler = null;
            if (thing != null) {
                thingHandler = thing.getHandler();
                if (thingHandler instanceof Clip2BridgeHandler) {
                    clip2BridgeHandler = (Clip2BridgeHandler) thingHandler;
                } else if (thingHandler instanceof HueBridgeHandler) {
                    bridgeHandler = (HueBridgeHandler) thingHandler;
                } else if (thingHandler instanceof HueGroupHandler) {
                    groupHandler = (HueGroupHandler) thingHandler;
                }
            }
            if (thing == null) {
                console.println("Bad thing id '" + args[0] + "'");
                printUsage(console);
            } else if (thingHandler == null) {
                console.println("No handler initialized for the thingUID '" + args[0] + "'");
                printUsage(console);
            } else if (bridgeHandler == null && groupHandler == null && clip2BridgeHandler == null) {
                console.println("'" + args[0] + "' is neither a Hue BridgeUID nor a Hue groupThingUID");
                printUsage(console);
            } else {
                if (bridgeHandler != null) {
                    switch (args[1]) {
                        case USER_NAME:
                            String userName = bridgeHandler.getUserName();
                            console.println("Your user name is " + (userName != null ? userName : "undefined"));
                            return;
                        case SCENES:
                            bridgeHandler.listScenesForConsole().forEach(console::println);
                            return;
                    }
                } else if (groupHandler != null) {
                    switch (args[1]) {
                        case SCENES:
                            groupHandler.listScenesForConsole().forEach(console::println);
                            return;
                    }
                } else if (clip2BridgeHandler != null) {
                    String applicationKey = clip2BridgeHandler.consoleGetApplicationKey();
                    applicationKey = Objects.nonNull(applicationKey) ? applicationKey : "undefined";

                    String ipAddress = clip2BridgeHandler.consoleGetIpAddress();
                    ipAddress = Objects.nonNull(ipAddress) ? ipAddress : "undefined";

                    switch (args[1]) {
                        case APPLICATION_KEY:
                            console.println(String.format(FMT_APPKEY, applicationKey));
                            return;

                        case SCENES:
                            console.println(String.format(FMT_BRIDGE, thing.getUID(), ipAddress, applicationKey));
                            List<Resource> scenes = clip2BridgeHandler
                                    .consoleGetResources(new ResourceReference().setType(ResourceType.SCENE));
                            if (scenes.isEmpty()) {
                                console.println("no scenes found");
                            } else {
                                scenes.forEach(scene -> console
                                        .println(String.format(FMT_SCENE, scene.getId(), scene.getName())));
                            }
                            console.println("}");
                            return;

                        case THINGS:
                            console.println(String.format(FMT_BRIDGE, thing.getUID(), ipAddress, applicationKey));
                            final Clip2BridgeHandler clip2BridgeHandlerFinal = clip2BridgeHandler;

                            for (ResourceType resourceType : SUPPORTED_RESOURCES) {
                                List<Resource> resources = clip2BridgeHandlerFinal
                                        .consoleGetResources(new ResourceReference().setType(resourceType));

                                if (!resources.isEmpty()) {
                                    console.println(String.format(FMT_COMMENT, resourceType.toString()));
                                    Map<String, String> lines = new TreeMap<>();

                                    resources.forEach(resource -> {
                                        String label = resource.getName();
                                        String comment = resource.getProductName();

                                        if (ResourceType.GROUPED_LIGHT == resourceType) {
                                            ResourceReference owner = resource.getOwner();
                                            if (Objects.nonNull(owner)) {
                                                Optional<Resource> ownerResource = clip2BridgeHandlerFinal
                                                        .consoleGetResources(owner).stream().findFirst();
                                                if (ownerResource.isPresent()) {
                                                    Resource ownerRes = ownerResource.get();
                                                    ResourceType ownerType = ownerRes.getType();
                                                    if (ownerType == ResourceType.BRIDGE_HOME) {
                                                        label = "Bridge (Home)";
                                                    } else {
                                                        label = String.format("%s (%s)", ownerRes.getName(),
                                                                ownerType.toString());
                                                    }
                                                }
                                            }
                                        }
                                        lines.put(label,
                                                String.format(FMT_THING,
                                                        resourceType.name().replace("_", "").toLowerCase(),
                                                        resource.getId(), label, resource.getId(), comment));
                                    });
                                    lines.entrySet().forEach(entry -> console.println(entry.getValue()));
                                }
                            }
                            console.println("}");
                            return;
                    }
                }
            }
        }
        printUsage(console);
    }

    @Override
    public List<String> getUsages() {
        return Arrays.asList(new String[] { buildCommandUsage("<bridgeUID> " + USER_NAME, "show the user name"),
                buildCommandUsage("<bridgeUID> " + APPLICATION_KEY, "show the API v2 application key"),
                buildCommandUsage("<bridgeUID> " + SCENES, "list all the scenes with their id"),
                buildCommandUsage("<bridgeUID> " + THINGS,
                        "list all the API v2 device and grouped light things with their id"),
                buildCommandUsage("<groupThingUID> " + SCENES, "list all the scenes from this group with their id") });
    }

    @Override
    public @Nullable ConsoleCommandCompleter getCompleter() {
        return this;
    }

    @Override
    public boolean complete(String[] args, int cursorArgumentIndex, int cursorPosition, List<String> candidates) {
        if (cursorArgumentIndex <= 0) {
            return new StringsCompleter(thingRegistry.getAll().stream()
                    .filter(t -> HueBindingConstants.THING_TYPE_BRIDGE.equals(t.getThingTypeUID())
                            || HueBindingConstants.THING_TYPE_CLIP2.equals(t.getThingTypeUID())
                            || HueBindingConstants.THING_TYPE_GROUP.equals(t.getThingTypeUID()))
                    .map(t -> t.getUID().getAsString()).collect(Collectors.toList()), true).complete(args,
                            cursorArgumentIndex, cursorPosition, candidates);
        } else if (cursorArgumentIndex == 1) {
            Thing thing = getThing(args[0]);
            if (thing != null && (HueBindingConstants.THING_TYPE_BRIDGE.equals(thing.getThingTypeUID())
                    || HueBindingConstants.THING_TYPE_CLIP2.equals(thing.getThingTypeUID()))) {
                return SUBCMD_COMPLETER.complete(args, cursorArgumentIndex, cursorPosition, candidates);
            } else if (thing != null && HueBindingConstants.THING_TYPE_GROUP.equals(thing.getThingTypeUID())) {
                return SCENES_COMPLETER.complete(args, cursorArgumentIndex, cursorPosition, candidates);
            }
        }
        return false;
    }

    private @Nullable Thing getThing(String uid) {
        Thing thing = null;
        try {
            ThingUID thingUID = new ThingUID(uid);
            thing = thingRegistry.get(thingUID);
        } catch (IllegalArgumentException e) {
            thing = null;
        }
        return thing;
    }
}
