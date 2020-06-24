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
package org.openhab.io.homekit.internal.accessories;

import static org.openhab.io.homekit.internal.HomekitCharacteristicType.SECURITY_SYSTEM_CURRENT_STATE;
import static org.openhab.io.homekit.internal.HomekitCharacteristicType.SECURITY_SYSTEM_TARGET_STATE;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.eclipse.smarthome.core.library.items.StringItem;
import org.eclipse.smarthome.core.library.types.StringType;
import org.openhab.io.homekit.internal.HomekitAccessoryUpdater;
import org.openhab.io.homekit.internal.HomekitCharacteristicType;
import org.openhab.io.homekit.internal.HomekitSettings;
import org.openhab.io.homekit.internal.HomekitTaggedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hapjava.accessories.SecuritySystemAccessory;
import io.github.hapjava.characteristics.HomekitCharacteristicChangeCallback;
import io.github.hapjava.characteristics.impl.securitysystem.CurrentSecuritySystemStateEnum;
import io.github.hapjava.characteristics.impl.securitysystem.TargetSecuritySystemStateEnum;
import io.github.hapjava.services.impl.SecuritySystemService;

/**
 * Implements SecuritySystem as a GroupedAccessory made up of multiple items:
 * <ul>
 * <li>CurrentSecuritySystemState: String type</li>
 * <li>TargetSecuritySystemState: String type</li>
 * </ul>
 * 
 * @author Cody Cutrer - Initial contribution
 */
public class HomekitSecuritySystemImpl extends AbstractHomekitAccessoryImpl implements SecuritySystemAccessory {
    private final Logger logger = LoggerFactory.getLogger(HomekitSecuritySystemImpl.class);
    private final Map<CurrentSecuritySystemStateEnum, String> currentStateMapping = new EnumMap<CurrentSecuritySystemStateEnum, String>(
            CurrentSecuritySystemStateEnum.class) {
        {
            put(CurrentSecuritySystemStateEnum.DISARMED, "DISARMED");
            put(CurrentSecuritySystemStateEnum.AWAY_ARM, "AWAY_ARM");
            put(CurrentSecuritySystemStateEnum.STAY_ARM, "STAY_ARM");
            put(CurrentSecuritySystemStateEnum.NIGHT_ARM, "NIGHT_ARM");
            put(CurrentSecuritySystemStateEnum.TRIGGERED, "TRIGGERED");
        }
    };
    private final Map<TargetSecuritySystemStateEnum, String> targetStateMapping = new EnumMap<TargetSecuritySystemStateEnum, String>(
            TargetSecuritySystemStateEnum.class) {
        {
            put(TargetSecuritySystemStateEnum.DISARM, "DISARM");
            put(TargetSecuritySystemStateEnum.AWAY_ARM, "AWAY_ARM");
            put(TargetSecuritySystemStateEnum.STAY_ARM, "STAY_ARM");
            put(TargetSecuritySystemStateEnum.NIGHT_ARM, "NIGHT_ARM");
        }
    };

    public HomekitSecuritySystemImpl(HomekitTaggedItem taggedItem, List<HomekitTaggedItem> mandatoryCharacteristics,
            HomekitAccessoryUpdater updater, HomekitSettings settings) throws IncompleteAccessoryException {
        super(taggedItem, mandatoryCharacteristics, updater, settings);
        updateMapping(SECURITY_SYSTEM_CURRENT_STATE, currentStateMapping);
        updateMapping(SECURITY_SYSTEM_TARGET_STATE, targetStateMapping);
        getServices().add(new SecuritySystemService(this));
    }

    @Override
    public CompletableFuture<CurrentSecuritySystemStateEnum> getCurrentSecuritySystemState() {
        return CompletableFuture.completedFuture(getKeyFromMapping(SECURITY_SYSTEM_CURRENT_STATE, currentStateMapping,
                CurrentSecuritySystemStateEnum.DISARMED));
    }

    @Override
    public void setTargetSecuritySystemState(final TargetSecuritySystemStateEnum state) {
        final Optional<HomekitTaggedItem> characteristic = getCharacteristic(
                HomekitCharacteristicType.SECURITY_SYSTEM_TARGET_STATE);
        if (characteristic.isPresent()) {
            ((StringItem) characteristic.get().getItem()).send(new StringType(targetStateMapping.get(state)));
        } else {
            logger.warn("Missing mandatory characteristic {}",
                    HomekitCharacteristicType.SECURITY_SYSTEM_TARGET_STATE.getTag());
        }
    }

    @Override
    public CompletableFuture<TargetSecuritySystemStateEnum> getTargetSecuritySystemState() {
        return CompletableFuture.completedFuture(getKeyFromMapping(SECURITY_SYSTEM_TARGET_STATE, targetStateMapping,
                TargetSecuritySystemStateEnum.DISARM));
    }

    @Override
    public void subscribeCurrentSecuritySystemState(final HomekitCharacteristicChangeCallback callback) {
        subscribe(SECURITY_SYSTEM_CURRENT_STATE, callback);
    }

    @Override
    public void unsubscribeCurrentSecuritySystemState() {
        unsubscribe(SECURITY_SYSTEM_CURRENT_STATE);
    }

    @Override
    public void subscribeTargetSecuritySystemState(final HomekitCharacteristicChangeCallback callback) {
        subscribe(SECURITY_SYSTEM_TARGET_STATE, callback);
    }

    @Override
    public void unsubscribeTargetSecuritySystemState() {
        unsubscribe(SECURITY_SYSTEM_TARGET_STATE);
    }
}
