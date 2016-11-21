/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.imperihome.internal.model.device;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.io.imperihome.internal.model.param.DeviceParam;
import org.openhab.io.imperihome.internal.model.param.ParamType;

/**
 * Abstraction of devices that are trippable, i.e. DevDoor, DevFlood, DevMotion, DevSmoke, DevCO2Alert.
 *
 * @author Pepijn de Geus - Initial contribution
 */
public class TrippableDevice extends AbstractDevice {

    public TrippableDevice(DeviceType type, Item item) {
        super(type, item);

        addParam(new DeviceParam(ParamType.ARMABLE, "0"));
        addParam(new DeviceParam(ParamType.ARMED, "1"));
        addParam(new DeviceParam(ParamType.ACKABLE, "0"));
    }

    @Override
    public void stateUpdated(Item item, State newState) {
        super.stateUpdated(item, newState);

        boolean tripped = false;

        List<Class<? extends State>> acceptedDataTypes = item.getAcceptedDataTypes();
        if (acceptedDataTypes.contains(OpenClosedType.class)) {
            OpenClosedType state = (OpenClosedType) item.getStateAs(OpenClosedType.class);
            tripped = state == OpenClosedType.CLOSED;
        } else if (acceptedDataTypes.contains(OnOffType.class)) {
            OnOffType state = (OnOffType) item.getStateAs(OnOffType.class);
            tripped = state == OnOffType.ON;
        } else if (acceptedDataTypes.contains(DecimalType.class)) {
            DecimalType state = (DecimalType) item.getStateAs(DecimalType.class);
            tripped = state.intValue() != 0;
        } else if (acceptedDataTypes.contains(StringType.class)) {
            StringType state = (StringType) item.getStateAs(StringType.class);
            tripped = StringUtils.isNotBlank(state.toString()) && !state.toString().trim().equals("ok");
        } else {
            logger.warn("Can't interpret state {} as tripped status", item.getState());
        }

        addParam(new DeviceParam(ParamType.TRIPPED, tripped ^ isInverted() ? "1" : "0"));

        if (tripped) {
            addParam(new DeviceParam(ParamType.LAST_TRIP, String.valueOf(System.currentTimeMillis())));
        }
    }

}
