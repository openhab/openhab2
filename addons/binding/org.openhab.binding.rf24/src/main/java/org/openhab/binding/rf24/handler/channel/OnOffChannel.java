package org.openhab.binding.rf24.handler.channel;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.rf24.rf24BindingConstants;
import org.openhab.binding.rf24.wifi.WifiOperator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import pl.grzeslowski.smarthome.proto.common.Basic.BasicMessage;
import pl.grzeslowski.smarthome.proto.sensor.Sensor.OnOff;
import pl.grzeslowski.smarthome.proto.sensor.Sensor.OnOffRequest;
import pl.grzeslowski.smarthome.proto.sensor.Sensor.OnOffResponse;
import pl.grzeslowski.smarthome.proto.sensor.Sensor.RefreshOnOffRequest;
import pl.grzeslowski.smarthome.proto.sensor.Sensor.SensorRequest;
import pl.grzeslowski.smarthome.rf24.helpers.Pipe;

public class OnOffChannel implements Channel {
    private final WifiOperator x;
    private final AtomicInteger messageIdSupplier;
    private final Pipe pipe;

    public OnOffChannel(WifiOperator x, AtomicInteger messageIdSupplier, Pipe pipe) {
        this.x = Preconditions.checkNotNull(x);
        this.messageIdSupplier = Preconditions.checkNotNull(messageIdSupplier);
        this.pipe = Preconditions.checkNotNull(pipe);
    }

    @Override
    public Optional<Consumer<Updatable>> process(ChannelUID channelUID, Command command) {
        if (command instanceof OnOffType) {
            OnOffType onOff = (OnOffType) command;
            SensorRequest cmdToSend = build(onOff);
            x.getWiFi().write(pipe, cmdToSend);

            return Optional.of(Channel.DOING_NOTHING_CONSUMER);
        } else if (command instanceof RefreshType) {
            // @formatter:off
            BasicMessage basic = BasicMessage
                    .newBuilder()
                    .setDeviceId((int) x.geTransmitterId().getId())
                    .setLinuxTimestamp(new Date().getTime())
                    .setMessageId(messageIdSupplier.incrementAndGet())
                    .build();
            // @formatter:on

            // @formatter:off
            SensorRequest request = SensorRequest
                    .newBuilder()
                    .setBasic(basic)
                    .setRefreshOnOffRequest(RefreshOnOffRequest.getDefaultInstance())
                    .build();
            // @formatter:on

            x.getWiFi().write(pipe, request);
            return Optional.of(updatable -> x.getWiFi().read(pipe).ifPresent(response -> {
                if (response.hasOnOffResponse()) {
                    OnOffResponse onOffResponse = response.getOnOffResponse();
                    updatable.updateState(channelUID, findOnOffType(onOffResponse));
                }
            }));
        } else {
            return Optional.empty();
        }
    }

    private OnOffType findOnOffType(OnOffResponse onOffResponse) {
        if (onOffResponse.getOnOff() == OnOff.ON) {
            return OnOffType.ON;
        } else {
            return OnOffType.OFF;
        }
    }

    private SensorRequest build(OnOffType cmd) {
        // @formatter:off
        BasicMessage basic = BasicMessage
                .newBuilder()
                .setDeviceId((int) x.geTransmitterId().getId())
                .setLinuxTimestamp(new Date().getTime())
                .setMessageId(messageIdSupplier.incrementAndGet())
                .build();
        // @formatter:on

        OnOff onOff;
        switch (cmd) {
            case ON:
                onOff = OnOff.ON;
                break;

            case OFF:
                onOff = OnOff.OFF;
                break;
            default:
                throw new RuntimeException("This should never happened [" + cmd + "]!");
        }

        OnOffRequest onOffCommand = OnOffRequest.newBuilder().setOnOff(onOff).build();

        return SensorRequest.newBuilder().setBasic(basic).setOnOffRequest(onOffCommand).build();
    }

    @Override
    public Set<String> whatChannelIdCanProcess() {
        return Sets.newHashSet(rf24BindingConstants.RF24_ON_OFF_CHANNEL);
    }

    @Override
    public String toString() {
        return OnOffChannel.class.getSimpleName();
    }
}
