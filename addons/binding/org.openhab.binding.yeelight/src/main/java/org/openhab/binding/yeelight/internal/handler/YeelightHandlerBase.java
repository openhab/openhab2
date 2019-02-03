/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.yeelight.internal.handler;

import static org.openhab.binding.yeelight.internal.YeelightBindingConstants.*;

import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.yeelight.internal.YeelightBindingConstants;
import org.openhab.binding.yeelight.internal.lib.device.ConnectState;
import org.openhab.binding.yeelight.internal.lib.device.DeviceBase;
import org.openhab.binding.yeelight.internal.lib.device.DeviceFactory;
import org.openhab.binding.yeelight.internal.lib.device.DeviceStatus;
import org.openhab.binding.yeelight.internal.lib.enums.DeviceAction;
import org.openhab.binding.yeelight.internal.lib.enums.DeviceMode;
import org.openhab.binding.yeelight.internal.lib.enums.DeviceType;
import org.openhab.binding.yeelight.internal.lib.listeners.DeviceConnectionStateListener;
import org.openhab.binding.yeelight.internal.lib.listeners.DeviceStatusChangeListener;
import org.openhab.binding.yeelight.internal.lib.services.DeviceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link YeelightHandlerBase} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Coaster Li - Initial contribution
 * @author Joe Ho - Added Duration Thing parameter
 */
public abstract class YeelightHandlerBase extends BaseThingHandler
        implements DeviceConnectionStateListener, DeviceStatusChangeListener {

    private final Logger logger = LoggerFactory.getLogger(YeelightHandlerBase.class);
    protected DeviceBase mDevice;

    // Reading the deviceId from the properties map.
    private String deviceId = getThing().getConfiguration().get(YeelightBindingConstants.PARAMETER_DEVICE_ID)
            .toString();

    public YeelightHandlerBase(Thing thing) {
        super(thing);
    }

    protected void updateUI(DeviceStatus status) {
    }

    @Override
    public void initialize() {
        logger.debug("Initializing, Device ID: {}", deviceId);
        mDevice = DeviceFactory.build(getDeviceModel(getThing().getThingTypeUID()).name(), deviceId);
        mDevice.setDeviceName(getThing().getLabel());
        mDevice.setAutoConnect(true);
        DeviceManager.getInstance().addDevice(mDevice);
        mDevice.registerConnectStateListener(this);
        mDevice.registerStatusChangedListener(this);
        updateStatusHelper(mDevice.getConnectionState());
        DeviceManager.getInstance().startDiscovery(15 * 1000);
    }

    private DeviceType getDeviceModel(ThingTypeUID typeUID) {
        if (typeUID.equals(YeelightBindingConstants.THING_TYPE_CEILING)) {
            return DeviceType.ceiling;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_CEILING3)) {
            return DeviceType.ceiling3;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_WONDER)) {
            return DeviceType.color;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_DOLPHIN)) {
            return DeviceType.mono;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_CTBULB)) {
            return DeviceType.ct_bulb;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_STRIPE)) {
            return DeviceType.stripe;
        } else if (typeUID.equals(YeelightBindingConstants.THING_TYPE_DESKLAMP)) {
            return DeviceType.desklamp;
        } else {
            return null;
        }
    }

    @Override
    public void onConnectionStateChanged(ConnectState connectState) {
        logger.debug("onConnectionStateChanged -> {}", connectState.name());
        updateStatusHelper(connectState);
    }

    public void updateStatusHelper(ConnectState connectState) {
        switch (connectState) {
            case DISCONNECTED:
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Device is offline!");
                if (mDevice.isAutoConnect()) {
                    DeviceManager.sInstance.startDiscovery(5 * 1000);
                    logger.debug("Thing OFFLINE. Initiated discovery");
                }
                break;
            case CONNECTED:
                updateStatus(ThingStatus.ONLINE);
                mDevice.queryStatus();
                break;
            default:
                updateStatus(ThingStatus.UNKNOWN);
                break;
        }
    }

    @Override
    public void channelLinked(ChannelUID channelUID) {
        logger.debug("ChannelLinked -> {}", channelUID.getId());
        super.channelLinked(channelUID);

        Runnable task = () -> {
            mDevice.queryStatus();
        };
        scheduler.schedule(task, 500, TimeUnit.MILLISECONDS);
    }

    public void handleCommandHelper(ChannelUID channelUID, Command command, String logInfo) {
        logger.debug(logInfo, command);

        // If device is disconnected, start discovery to reconnect.
        if (mDevice.isAutoConnect() && mDevice.getConnectionState() != ConnectState.CONNECTED) {
            DeviceManager.getInstance().startDiscovery(5 * 1000);
        }
        if (command instanceof RefreshType) {
            DeviceManager.getInstance().startDiscovery(5 * 1000);
            DeviceStatus s = mDevice.getDeviceStatus();
            switch (channelUID.getId()) {
                case YeelightBindingConstants.CHANNEL_BRIGHTNESS:
                    updateState(channelUID, new PercentType(s.getBrightness()));
                    break;
                case YeelightBindingConstants.CHANNEL_COLOR:
                    HSBType hsb = new HSBType();
                    updateState(channelUID, HSBType.fromRGB(s.getR(), s.getG(), s.getB()));
                    break;
                case YeelightBindingConstants.CHANNEL_COLOR_TEMPERATURE:
                    updateState(channelUID, new PercentType(s.getCt()));
                    break;
                default:
                    break;
            }
            return;
        }
        switch (channelUID.getId()) {
            case YeelightBindingConstants.CHANNEL_BRIGHTNESS:
                if (command instanceof PercentType) {
                    handlePercentMessage((PercentType) command);
                } else if (command instanceof OnOffType) {
                    handleOnOffCommand((OnOffType) command);
                } else if (command instanceof IncreaseDecreaseType) {
                    handleIncreaseDecreaseBrightnessCommand((IncreaseDecreaseType) command);
                }
                break;
            case YeelightBindingConstants.CHANNEL_COLOR:
                if (command instanceof HSBType) {
                    HSBType hsbCommand = (HSBType) command;
                    if (hsbCommand.getBrightness().intValue() == 0) {
                        handleOnOffCommand(OnOffType.OFF);
                    } else {
                        handleHSBCommand(hsbCommand);
                    }
                } else if (command instanceof PercentType) {
                    handlePercentMessage((PercentType) command);
                } else if (command instanceof OnOffType) {
                    handleOnOffCommand((OnOffType) command);
                } else if (command instanceof IncreaseDecreaseType) {
                    handleIncreaseDecreaseBrightnessCommand((IncreaseDecreaseType) command);
                }
                break;
            case YeelightBindingConstants.CHANNEL_COLOR_TEMPERATURE:
                if (command instanceof PercentType) {
                    handleColorTemperatureCommand((PercentType) command);
                } else if (command instanceof IncreaseDecreaseType) {
                    handleIncreaseDecreaseBrightnessCommand((IncreaseDecreaseType) command);
                }
                break;
            default:
                break;
        }
    }

    void handlePercentMessage(PercentType brightness) {
        DeviceAction pAction;
        if (brightness.intValue() == 0) {
            pAction = DeviceAction.close;
            pAction.putDuration(getDuration());
            DeviceManager.getInstance().doAction(deviceId, pAction);
        } else {
            if (mDevice.getDeviceStatus().isPowerOff()) {
                pAction = DeviceAction.open;
                // hard coded to fast open, the duration should apply to brightness increase only
                pAction.putDuration(0);
                DeviceManager.getInstance().doAction(deviceId, pAction);
            }
            pAction = DeviceAction.brightness;
            pAction.putValue(brightness.intValue());
            pAction.putDuration(getDuration());
            DeviceManager.getInstance().doAction(deviceId, pAction);
        }
    }

    void handleIncreaseDecreaseBrightnessCommand(IncreaseDecreaseType increaseDecrease) {
        DeviceAction idbAcation = increaseDecrease == IncreaseDecreaseType.INCREASE ? DeviceAction.increase_bright
                : DeviceAction.decrease_bright;
        idbAcation.putDuration(getDuration());
        DeviceManager.getInstance().doAction(deviceId, idbAcation);
    }

    void handleIncreaseDecreaseColorTemperatureCommand(IncreaseDecreaseType increaseDecrease) {
        DeviceAction idctAcation = increaseDecrease == IncreaseDecreaseType.INCREASE ? DeviceAction.increase_ct
                : DeviceAction.decrease_ct;
        idctAcation.putDuration(getDuration());
        DeviceManager.getInstance().doAction(deviceId, idctAcation);
    }

    void handleOnOffCommand(OnOffType onoff) {
        DeviceAction ofAction = onoff == OnOffType.ON ? DeviceAction.open : DeviceAction.close;
        ofAction.putDuration(getDuration());
        DeviceManager.getInstance().doAction(deviceId, ofAction);
    }

    void handleHSBCommand(HSBType color) {
        DeviceAction caction = DeviceAction.color;
        caction.putValue(color.getRGB() & 0xFFFFFF);
        caction.putDuration(getDuration());
        DeviceManager.getInstance().doAction(deviceId, caction);
    }

    void handleColorTemperatureCommand(PercentType ct) {
        DeviceAction ctaction = DeviceAction.colortemperature;
        ctaction.putValue(COLOR_TEMPERATURE_STEP * ct.intValue() + COLOR_TEMPERATURE_MINIMUM);
        ctaction.putDuration(getDuration());
        DeviceManager.getInstance().doAction(deviceId, ctaction);
    }

    @Override
    public void onStatusChanged(String prop, DeviceStatus status) {
        logger.debug("UpdateState->{}", status);
        updateUI(status);
    }

    void updateBrightnessAndColorUI(DeviceStatus status) {
        if (status.isPowerOff()) {
            updateState(YeelightBindingConstants.CHANNEL_BRIGHTNESS, new PercentType(0));
        } else {
            updateState(YeelightBindingConstants.CHANNEL_BRIGHTNESS, new PercentType(status.getBrightness()));
            HSBType hsbType = null;
            if (status.getMode() == DeviceMode.MODE_COLOR) {
                hsbType = HSBType.fromRGB(status.getR(), status.getG(), status.getB());
            } else if (status.getMode() == DeviceMode.MODE_HSV) {
                hsbType = new HSBType(new DecimalType(status.getHue()), new PercentType(status.getSat()),
                        new PercentType(1));
            }
            if (hsbType != null) {
                updateState(YeelightBindingConstants.CHANNEL_COLOR, hsbType);
            }
            updateState(YeelightBindingConstants.CHANNEL_COLOR_TEMPERATURE,
                    new PercentType((status.getCt() - COLOR_TEMPERATURE_MINIMUM) / COLOR_TEMPERATURE_STEP));
        }
    }

    int getDuration() {
        // Duration should not be null, but just in case do a null check.
        try {
            return getThing().getConfiguration().get(YeelightBindingConstants.PARAMETER_Duration) == null ? 500
                    : ((Number) getThing().getConfiguration().get(YeelightBindingConstants.PARAMETER_Duration))
                            .intValue();
        } catch (Exception e) {
            logger.error("Unable to get Thing duration, default to 500. Device ID: {}", deviceId);
            return 500;
        }
    }
}
