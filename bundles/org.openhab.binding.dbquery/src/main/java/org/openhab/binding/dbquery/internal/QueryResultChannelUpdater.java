package org.openhab.binding.dbquery.internal;

import org.openhab.binding.dbquery.internal.error.UnnexpectedCondition;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryResultChannelUpdater {
    private final Logger logger = LoggerFactory.getLogger(QueryResultChannelUpdater.class);

    private final ChannelStateUpdater channelStateUpdater;
    private final ChannelsToUpdate channels2Update;
    private final Value2StateConverter value2StateConverter;

    public QueryResultChannelUpdater(ChannelStateUpdater channelStateUpdater, ChannelsToUpdate channelsToUpdate) {
        this.channelStateUpdater = channelStateUpdater;
        this.channels2Update = channelsToUpdate;
        this.value2StateConverter = new Value2StateConverter();
    }

    public void clearChannelResults() {
        for (Channel channel : channels2Update.getChannels()) {
            channelStateUpdater.updateChannelState(channel, UnDefType.NULL);
        }
    }

    public void updateChannelResults(Object extractedResult) {
        for (Channel channel : channels2Update.getChannels()) {
            Class<? extends State> targetType = calculateItemType(channel);
            State state = value2StateConverter.convertValue(extractedResult, targetType);
            channelStateUpdater.updateChannelState(channel, state);
            logger.trace("Channel {} state updated to {}", channel.getUID(), state);
        }
    }

    private Class<? extends State> calculateItemType(Channel channel) {
        ChannelTypeUID channelTypeUID = channel.getChannelTypeUID();
        String channelID = channelTypeUID != null ? channelTypeUID.getId()
                : DBQueryBindingConstants.RESULT_STRING_CHANNEL_TYPE;
        switch (channelID) {
            case DBQueryBindingConstants.RESULT_STRING_CHANNEL_TYPE:
                return StringType.class;
            case DBQueryBindingConstants.RESULT_NUMBER_CHANNEL_TYPE:
                return DecimalType.class;
            case DBQueryBindingConstants.RESULT_DATETIME_CHANNEL_TYPE:
                return DateTimeType.class;
            case DBQueryBindingConstants.RESULT_SWITCH_CHANNEL_TYPE:
                return OnOffType.class;
            case DBQueryBindingConstants.RESULT_CONTACT_CHANNEL_TYPE:
                return OpenClosedType.class;
            default:
                throw new UnnexpectedCondition("Unexpected channel type " + channelTypeUID);
        }
    }
}
