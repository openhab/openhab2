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
package org.openhab.binding.paradoxalarm.internal.parsers;

import org.openhab.binding.paradoxalarm.internal.model.PartitionState;
import org.openhab.binding.paradoxalarm.internal.model.ZoneState;
import org.openhab.binding.paradoxalarm.internal.model.ZoneStateFlags;
import org.openhab.binding.paradoxalarm.internal.util.ParadoxUtil;

/**
 * The {@link EvoParser} Implementation of parser interface for EVO type panels
 *
 * @author Konstantin Polihronov - Initial contribution
 */
public class EvoParser extends AbstractParser {

    @Override
    public PartitionState calculatePartitionState(byte[] partitionFlags) {
        byte firstByte = partitionFlags[0];
        PartitionState state = new PartitionState();
        state.setArmed(ParadoxUtil.isBitSet(firstByte, 0));
        state.setArmedInAway(ParadoxUtil.isBitSet(firstByte, 1));
        state.setArmedInStay(ParadoxUtil.isBitSet(firstByte, 2));
        state.setArmedInNoEntry(ParadoxUtil.isBitSet(firstByte, 3));

        state.setInAlarm(ParadoxUtil.isBitSet(firstByte, 4));
        state.setInSilentAlarm(ParadoxUtil.isBitSet(firstByte, 5));
        state.setInAudibleAlarm(ParadoxUtil.isBitSet(firstByte, 6));
        state.setInFireAlarm(ParadoxUtil.isBitSet(firstByte, 7));

        byte secondByte = partitionFlags[1];
        state.setReadyToArm(ParadoxUtil.isBitSet(secondByte, 0));
        state.setInExitDelay(ParadoxUtil.isBitSet(secondByte, 1));
        state.setInEntryDelay(ParadoxUtil.isBitSet(secondByte, 2));
        state.setInTrouble(ParadoxUtil.isBitSet(secondByte, 3));
        state.setHasAlarmInMemory(ParadoxUtil.isBitSet(secondByte, 4));
        state.setInZoneBypass(ParadoxUtil.isBitSet(secondByte, 5));

        byte thirdByte = partitionFlags[2];
        state.setHasZoneInTamperTrouble(ParadoxUtil.isBitSet(thirdByte, 4));
        state.setHasZoneInLowBatteryTrouble(ParadoxUtil.isBitSet(thirdByte, 5));
        state.setHasZoneInFireLoopTrouble(ParadoxUtil.isBitSet(thirdByte, 6));
        state.setHasZoneInSupervisionTrouble(ParadoxUtil.isBitSet(thirdByte, 7));

        byte sixthByte = partitionFlags[5];
        state.setStayInstantReady(ParadoxUtil.isBitSet(sixthByte, 0));
        state.setForceReady(ParadoxUtil.isBitSet(sixthByte, 1));
        state.setBypassReady(ParadoxUtil.isBitSet(sixthByte, 2));
        state.setInhibitReady(ParadoxUtil.isBitSet(sixthByte, 3));
        state.setAllZoneClosed(ParadoxUtil.isBitSet(sixthByte, 4));

        return state;
    }

    @Override
    public ZoneState calculateZoneState(int id, ZoneStateFlags zoneStateFlags) {
        int index = (id - 1) / 8;
        int bitNumber = id % 8 - 1;

        byte[] zonesOpened = zoneStateFlags.getZonesOpened();
        boolean isOpened = ParadoxUtil.isBitSet(zonesOpened[index], bitNumber);

        byte[] zonesTampered = zoneStateFlags.getZonesTampered();
        boolean isTampered = ParadoxUtil.isBitSet(zonesTampered[index], bitNumber);

        byte[] zonesLowBattery = zoneStateFlags.getZonesLowBattery();
        boolean hasLowBattery = ParadoxUtil.isBitSet(zonesLowBattery[index], bitNumber);

        // TODO when we retrieve the ZoneFlags from the communicator, ensure that we model all possible flag states in
        // the ZoneState object
        // (they are 8 states - each bit represents a state see Evo Technical Info 1.0.5 xls, sheet RAM_MAP DETAIL)
        // Zone Flags:
        // 0 = Zone supervision trouble
        // 1 = Zone in TX delay
        // 2 = Zone shutted down
        // 3 = Zone bypassed
        // 4 = Zone activated intellizone delay
        // 5 = Zone activated entry delay
        // 6 = Zone presently in alarm
        // 7 = Zone generated an alarm

        return new ZoneState(isOpened, isTampered, hasLowBattery);
    }
}
