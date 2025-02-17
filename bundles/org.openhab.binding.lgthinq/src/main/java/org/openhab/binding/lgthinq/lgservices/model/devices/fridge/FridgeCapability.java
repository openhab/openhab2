/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.lgthinq.lgservices.model.devices.fridge;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.lgthinq.lgservices.model.CapabilityDefinition;
import org.openhab.binding.lgthinq.lgservices.model.CommandDefinition;

/**
 * The {@link FridgeCapability}
 *
 * @author Nemer Daud - Initial contribution
 */
@NonNullByDefault
public interface FridgeCapability extends CapabilityDefinition {

    Map<String, String> getFridgeTempCMap();

    Map<String, String> getFridgeTempFMap();

    Map<String, String> getFreezerTempCMap();

    Map<String, String> getFreezerTempFMap();

    Map<String, String> getTempUnitMap();

    Map<String, String> getIcePlusMap();

    Map<String, String> getFreshAirFilterMap();

    Map<String, String> getWaterFilterMap();

    Map<String, String> getExpressFreezeModeMap();

    Map<String, String> getSmartSavingMap();

    Map<String, String> getActiveSavingMap();

    Map<String, String> getAtLeastOneDoorOpenMap();

    Map<String, CommandDefinition> getCommandsDefinition();

    boolean isExpressCoolModePresent();

    void setExpressCoolModePresent(boolean isPresent);

    boolean isEcoFriendlyModePresent();

    void setEcoFriendlyModePresent(boolean isPresent);
}
