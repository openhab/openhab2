package org.openhab.binding.insteonplm.internal.device.commands;

import java.io.IOException;

import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.insteonplm.handler.InsteonThingHandler;
import org.openhab.binding.insteonplm.internal.device.CommandHandler;
import org.openhab.binding.insteonplm.internal.device.DeviceFeature;
import org.openhab.binding.insteonplm.internal.device.InsteonThing;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler to increase of decrease the value of the iterm.
 *
 * @author Daniel Pfrommer
 * @author Bernd Pfrommer
 */
public class IncreaseDecreaseCommandHandler extends CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(IncreaseDecreaseCommandHandler.class);

    IncreaseDecreaseCommandHandler(DeviceFeature f) {
        super(f);
    }

    @Override
    public void handleCommand(InsteonThingHandler conf, Command cmd, InsteonThing dev) {
        try {
            if (cmd == IncreaseDecreaseType.INCREASE) {
                Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x15, (byte) 0x00);
                dev.enqueueMessage(m, getFeature());
                logger.info("{}: sent msg to brighten {}", nm(), dev.getAddress());
            } else if (cmd == IncreaseDecreaseType.DECREASE) {
                Msg m = dev.makeStandardMessage((byte) 0x0f, (byte) 0x16, (byte) 0x00);
                dev.enqueueMessage(m, getFeature());
                logger.info("{}: sent msg to dimm {}", nm(), dev.getAddress());
            }
        } catch (IOException e) {
            logger.error("{}: command send i/o error: ", nm(), e);
        } catch (FieldException e) {
            logger.error("{}: command send message creation error ", nm(), e);
        }
    }
}
