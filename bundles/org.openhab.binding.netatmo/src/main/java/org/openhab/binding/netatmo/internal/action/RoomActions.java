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
package org.openhab.binding.netatmo.internal.action;

import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.netatmo.internal.api.data.NetatmoConstants.SetpointMode;
import org.openhab.binding.netatmo.internal.handler.CommonInterface;
import org.openhab.binding.netatmo.internal.handler.capability.EnergyCapability;
import org.openhab.core.automation.annotation.ActionInput;
import org.openhab.core.automation.annotation.RuleAction;
import org.openhab.core.thing.binding.ThingActions;
import org.openhab.core.thing.binding.ThingActionsScope;
import org.openhab.core.thing.binding.ThingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link RoomActions} defines thing actions for RoomHandler.
 *
 * @author Markus Dillmann - Initial contribution
 */
@ThingActionsScope(name = "netatmo")
@NonNullByDefault
public class RoomActions implements ThingActions {
    private final Logger logger = LoggerFactory.getLogger(RoomActions.class);
    private static final Set<SetpointMode> ALLOWED_MODES = Set.of(SetpointMode.MAX, SetpointMode.MANUAL,
            SetpointMode.HOME);

    private @Nullable CommonInterface handler;
    private Optional<EnergyCapability> energy = Optional.empty();

    public RoomActions() {
        logger.debug("Netatmo RoomActions service created");
    }

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof CommonInterface) {
            CommonInterface commonHandler = (CommonInterface) handler;
            this.handler = commonHandler;
            energy = commonHandler.getHomeCapability(EnergyCapability.class);
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return (ThingHandler) handler;
    }

    @RuleAction(label = "@text/actionSetThermRoomTempSetpointLabel", description = "@text/actionSetThermRoomTempSetpointDesc")
    public void setThermRoomTempSetpoint(
            @ActionInput(name = "temp", label = "@text/actionInputSetpointLabel", description = "@text/actionInputSetpointDesc") @Nullable Double temp,
            @ActionInput(name = "endtime", label = "@text/actionInputEndtimeLabel", description = "@text/actionInputEndtimeDesc") @Nullable Long endTime) {
        CommonInterface roomHandler = handler;
        if (roomHandler == null) {
            logger.info("Handler not set for room thing actions.");
            return;
        } else if (temp == null) {
            logger.info("Temperature is required, action ignored");
            return;
        } else if (endTime == null) {
            logger.info("Temperature provided but no endtime given, action ignored");
            return;
        }

        try {
            String roomId = roomHandler.getId();
            double setpointTemp = temp;
            long setpointEnd = endTime;
            SetpointMode setpointMode = SetpointMode.MANUAL;
            energy.ifPresent(cap -> cap.setRoomThermTemp(roomId, setpointTemp, setpointEnd, setpointMode));
        } catch (IllegalArgumentException e) {
            logger.debug("Ignoring setRoomThermpoint command due to illegal argument exception: {}", e.getMessage());
        }
    }

    @RuleAction(label = "@text/actionSetThermRoomModeSetpointLabel", description = "@text/actionSetThermRoomModeSetpointDesc")
    public void setThermRoomModeSetpoint(
            @ActionInput(name = "mode", label = "@text/actionInputModeLabel", description = "@text/actionInputModeDesc") @Nullable String mode,
            @ActionInput(name = "endtime", label = "@text/actionInputEndtimeLabel", description = "@text/actionInputEndtimeDesc") @Nullable Long endTime) {
        CommonInterface roomHandler = handler;
        if (roomHandler == null) {
            logger.info("Handler not set for room thing actions.");
            return;
        } else if (mode == null) {
            logger.info("Mode is required, action ignored");
            return;
        }

        SetpointMode targetMode = SetpointMode.UNKNOWN;
        try {
            targetMode = SetpointMode.valueOf(mode);
            if (!ALLOWED_MODES.contains(targetMode)) {
                logger.info("Mode can only be MAX, HOME or MANUAL for a room");
                return;
            }
        } catch (IllegalArgumentException e) {
            logger.info("Invalid mode passed : {} - {}", mode, e.getMessage());
            return;
        }

        Long targetEndTime = endTime;
        if (SetpointMode.HOME.equals(targetMode)) {
            targetEndTime = 0L;
        } else if (targetEndTime == null) {
            logger.info("No endtime given, action ignored");
            return;
        }

        try {
            String roomId = roomHandler.getId();
            double setpointTemp = 0;
            long setpointEnd = targetEndTime;
            SetpointMode setpointMode = targetMode;
            energy.ifPresent(cap -> cap.setRoomThermTemp(roomId, setpointTemp, setpointEnd, setpointMode));
        } catch (IllegalArgumentException e) {
            logger.debug("Ignoring setRoomThermpoint command due to illegal argument exception: {}", e.getMessage());
        }
    }

    public static void setThermRoomTempSetpoint(ThingActions actions, @Nullable Double temp, @Nullable Long endTime) {
        ((RoomActions) actions).setThermRoomTempSetpoint(temp, endTime);
    }

    public static void setThermRoomModeSetpoint(ThingActions actions, @Nullable String mode, @Nullable Long endTime) {
        ((RoomActions) actions).setThermRoomModeSetpoint(mode, endTime);
    }
}
