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
package org.openhab.io.homekit.internal.accessories;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.openhab.io.homekit.internal.HomekitAccessoryUpdater;
import org.openhab.io.homekit.internal.HomekitCharacteristicType;
import org.openhab.io.homekit.internal.HomekitSettings;
import org.openhab.io.homekit.internal.HomekitTaggedItem;

import io.github.hapjava.accessories.MicrophoneAccessory;
import io.github.hapjava.characteristics.HomekitCharacteristicChangeCallback;
import io.github.hapjava.services.impl.MicrophoneService;

/**
 * Implements microphone using an item that provides an On/Off state for Mute.
 *
 * @author Eugen Freiter - Initial contribution
 */
public class HomekitMicrophoneImpl extends AbstractHomekitAccessoryImpl implements MicrophoneAccessory {
    private final BooleanItemReader muteReader;

    public HomekitMicrophoneImpl(HomekitTaggedItem taggedItem, List<HomekitTaggedItem> mandatoryCharacteristics,
            HomekitAccessoryUpdater updater, HomekitSettings settings) throws IncompleteAccessoryException {
        super(taggedItem, mandatoryCharacteristics, updater, settings);
        muteReader = createBooleanReader(HomekitCharacteristicType.MUTE);
        getServices().add(new MicrophoneService(this));
    }

    @Override
    public CompletableFuture<Boolean> isMuted() {
        return CompletableFuture.completedFuture(muteReader.getValue());
    }

    @Override
    public CompletableFuture<Void> setMute(boolean state) {
        muteReader.setValue(state);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void subscribeMuteState(HomekitCharacteristicChangeCallback callback) {
        subscribe(HomekitCharacteristicType.MUTE, callback);
    }

    @Override
    public void unsubscribeMuteState() {
        unsubscribe(HomekitCharacteristicType.MUTE);
    }
}
