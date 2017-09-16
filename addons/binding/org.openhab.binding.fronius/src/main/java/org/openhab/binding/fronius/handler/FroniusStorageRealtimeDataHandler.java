package org.openhab.binding.fronius.handler;

import static org.openhab.binding.fronius.FroniusBindingConstants.*;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.openhab.binding.fronius.internal.model.StorageRealtimeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FroniusStorageRealtimeDataHandler extends FroniusDeviceThingHandler {

    private final Logger logger = LoggerFactory.getLogger(FroniusStorageRealtimeDataHandler.class);
    private final JsonParser parser = new JsonParser();

    private String url;

    public FroniusStorageRealtimeDataHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected String getServiceDescription() {
        return STORAGE_REALTIME_DATA_DESCRIPTION;
    }

    @Override
    protected String getServiceUrl() {
        if (null == url) {
            StringBuilder sb = new StringBuilder();
            sb.append("http://");
            sb.append(getHostname());
            sb.append(STORAGE_REALTIME_DATA_URL);
            sb.append("?Scope=Device");
            sb.append("&DeviceId=");
            sb.append(getDevice());
            url = sb.toString();
        }

        logger.debug("{} URL: {}", getServiceDescription(), url);

        return url;
    }

    @Override
    protected void updateData(String data) {
        logger.debug("Refresh data {}", data);
        final JsonObject json = parser.parse(data).getAsJsonObject();
        final StorageRealtimeData model = StorageRealtimeData.createStorageRealtimeData(json);
        if (model.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
        } else {
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STORAGE_CURRENT), model.getCurrent());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STORAGE_VOLTAGE), model.getVoltage());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STORAGE_CHARGE), model.getCharge());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STORAGE_CAPACITY), model.getCapacity());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STORAGE_TEMPERATURE), model.getTemperature());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_STATUS_CODE), model.getCode());
            updateState(new ChannelUID(getThing().getUID(), CHANNEL_TIMESTAMP), model.getTimestamp());
        }
    }

}
