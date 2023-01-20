/**
<<<<<<< Upstream, based on origin/main
<<<<<<< Upstream, based on origin/main
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
package org.openhab.binding.freeboxos.internal.handler;

import static org.openhab.binding.freeboxos.internal.FreeboxOsBindingConstants.*;
import static org.openhab.core.library.unit.Units.PERCENT;

<<<<<<< Upstream, based on origin/main
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.rest.LcdManager;
import org.openhab.binding.freeboxos.internal.api.rest.LcdManager.Config;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RevolutionHandler} is responsible for handling take care of revolution server specifics
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class RevolutionHandler extends ServerHandler {
    private final Logger logger = LoggerFactory.getLogger(RevolutionHandler.class);

    public RevolutionHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected boolean internalHandleCommand(String channelId, Command command) throws FreeboxException {
        LcdManager manager = getManager(LcdManager.class);
        Config config = manager.getConfig();
        switch (channelId) {
            case LCD_BRIGHTNESS:
                setBrightness(manager, config, command);
                internalPoll();
                return true;
            case LCD_ORIENTATION:
                setOrientation(manager, config, command);
                internalPoll();
                return true;
            case LCD_FORCED:
                setForced(manager, config, command);
                internalPoll();
                return true;
        }
        return super.internalHandleCommand(channelId, command);
    }

    @Override
    protected void internalPoll() throws FreeboxException {
        super.internalPoll();
        Config config = getManager(LcdManager.class).getConfig();
        updateChannelQuantity(DISPLAY, LCD_BRIGHTNESS, config.brightness(), PERCENT);
        updateChannelDecimal(DISPLAY, LCD_ORIENTATION, config.orientation());
        updateChannelOnOff(DISPLAY, LCD_FORCED, config.orientationForced());
    }

    private void setOrientation(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (command instanceof DecimalType) {
            manager.setOrientation(((DecimalType) command).intValue());
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_ORIENTATION);
        }
    }

    private void setForced(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (ON_OFF_CLASSES.contains(command.getClass())) {
            manager.setOrientationForced(TRUE_COMMANDS.contains(command));
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_FORCED);
        }
    }

    private void setBrightness(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (command instanceof IncreaseDecreaseType) {
            manager.setBrightness(() -> config.brightness() + (command == IncreaseDecreaseType.INCREASE ? 1 : -1));
        } else if (command instanceof OnOffType) {
            manager.setBrightness(() -> command == OnOffType.ON ? 100 : 0);
        } else if (command instanceof QuantityType) {
            manager.setBrightness(() -> ((QuantityType<?>) command).intValue());
        } else if (command instanceof DecimalType || command instanceof PercentType) {
            manager.setBrightness(() -> ((DecimalType) command).intValue());
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_BRIGHTNESS);
=======
 * Copyright (c) 2010-2022 Contributors to the openHAB project
=======
 * Copyright (c) 2010-2023 Contributors to the openHAB project
>>>>>>> 006a813 Saving work before instroduction of ArrayListDeserializer
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
package org.openhab.binding.freeboxos.internal.handler;

import static org.openhab.binding.freeboxos.internal.FreeboxOsBindingConstants.*;
import static org.openhab.core.library.unit.Units.PERCENT;

import java.util.concurrent.Callable;

=======
>>>>>>> e4ef5cc Switching to Java 17 records
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.rest.LcdManager;
import org.openhab.binding.freeboxos.internal.api.rest.LcdManager.Config;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RevolutionHandler} is responsible for handling take care of revolution server specifics
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class RevolutionHandler extends ServerHandler {
    private final Logger logger = LoggerFactory.getLogger(RevolutionHandler.class);

    public RevolutionHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected boolean internalHandleCommand(String channelId, Command command) throws FreeboxException {
        LcdManager manager = getManager(LcdManager.class);
        Config config = manager.getConfig();
        switch (channelId) {
            case LCD_BRIGHTNESS:
                setBrightness(manager, config, command);
                internalPoll();
                return true;
            case LCD_ORIENTATION:
                setOrientation(manager, config, command);
                internalPoll();
                return true;
            case LCD_FORCED:
                setForced(manager, config, command);
                internalPoll();
                return true;
        }
        return super.internalHandleCommand(channelId, command);
    }

    @Override
    protected void internalPoll() throws FreeboxException {
        super.internalPoll();
        Config config = getManager(LcdManager.class).getConfig();
        updateChannelQuantity(DISPLAY, LCD_BRIGHTNESS, config.brightness(), PERCENT);
        updateChannelDecimal(DISPLAY, LCD_ORIENTATION, config.orientation());
        updateChannelOnOff(DISPLAY, LCD_FORCED, config.orientationForced());
    }

    private void setOrientation(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (command instanceof DecimalType) {
            manager.setOrientation(((DecimalType) command).intValue());
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_ORIENTATION);
        }
    }

    private void setForced(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (ON_OFF_CLASSES.contains(command.getClass())) {
            manager.setOrientationForced(TRUE_COMMANDS.contains(command));
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_FORCED);
        }
    }

    private void setBrightness(LcdManager manager, Config config, Command command) throws FreeboxException {
        if (command instanceof IncreaseDecreaseType) {
            manager.setBrightness(() -> config.brightness() + (command == IncreaseDecreaseType.INCREASE ? 1 : -1));
        } else if (command instanceof OnOffType) {
            manager.setBrightness(() -> command == OnOffType.ON ? 100 : 0);
        } else if (command instanceof QuantityType) {
            manager.setBrightness(() -> ((QuantityType<?>) command).intValue());
        } else if (command instanceof DecimalType || command instanceof PercentType) {
            manager.setBrightness(() -> ((DecimalType) command).intValue());
        } else {
            logger.warn("Invalid command {} from channel {}", command, LCD_BRIGHTNESS);
        }
    }
<<<<<<< Upstream, based on origin/main

<<<<<<< Upstream, based on origin/main
    private void changeLcdBrightness(LcdManager manager, Callable<Integer> function) throws FreeboxException {
        LcdConfig config = manager.getConfig();
        try {
            int newValue = function.call();
            manager.setConfig(config.withBrightness(newValue));
        } catch (Exception e) {
            throw new FreeboxException(e, "Error setting brightness");
>>>>>>> 46dadb1 SAT warnings handling
        }
    }
=======
>>>>>>> e4ef5cc Switching to Java 17 records
=======
>>>>>>> 089708c Switching to addons.xml, headers updated
}
