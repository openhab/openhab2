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
package org.openhab.binding.netatmo.internal.handler.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.netatmo.internal.api.RestManager;
import org.openhab.binding.netatmo.internal.api.dto.Device;
import org.openhab.binding.netatmo.internal.api.dto.Module;
import org.openhab.binding.netatmo.internal.api.dto.NAObject;
import org.openhab.binding.netatmo.internal.deserialization.NAObjectMap;
import org.openhab.binding.netatmo.internal.handler.CommonInterface;

/**
 * The {@link RestCapability} is the base class for handler capabilities
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public abstract class RestCapability<T extends RestManager> extends ModuleCapability {
    protected Optional<T> api = Optional.empty();

    RestCapability(CommonInterface handler) {
        super(handler);
    }

    @Override
    protected void updateNADevice(Device newData) {
        super.updateNADevice(newData);
        NAObjectMap<Module> modules = newData.getModules();
        handler.getActiveChildren().forEach(child -> child.setNewData(modules.get(child.getId())));
    }

    @Override
    public List<NAObject> updateReadings() {
        List<NAObject> result = new ArrayList<>();
        api.ifPresent(api -> result.addAll(updateReadings(api)));
        return result;
    }

    protected abstract List<NAObject> updateReadings(T api);
}
