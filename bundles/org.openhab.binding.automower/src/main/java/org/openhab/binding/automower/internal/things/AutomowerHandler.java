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
package org.openhab.binding.automower.internal.things;

import static org.openhab.binding.automower.internal.AutomowerBindingConstants.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.automower.internal.AutomowerBindingConstants;
import org.openhab.binding.automower.internal.actions.AutomowerActions;
import org.openhab.binding.automower.internal.bridge.AutomowerBridge;
import org.openhab.binding.automower.internal.bridge.AutomowerBridgeHandler;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.CalendarTask;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.Capabilities;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.Headlight;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.HeadlightMode;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.Mower;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.MowerStayOutZoneAttributes;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.MowerWorkAreaAttributes;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.Position;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.RestrictedReason;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.Settings;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.State;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.StayOutZone;
import org.openhab.binding.automower.internal.rest.api.automowerconnect.dto.WorkArea;
import org.openhab.binding.automower.internal.rest.exceptions.AutomowerCommunicationException;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.SIUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AutomowerHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Markus Pfleger - Initial contribution
 * @author Marcin Czeczko - Added support for planner and calendar data
 */
@NonNullByDefault
public class AutomowerHandler extends BaseThingHandler {
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Set.of(THING_TYPE_AUTOMOWER);
    private static final String NO_ID = "NO_ID";
    private static final long DEFAULT_COMMAND_DURATION_MIN = 60;
    private static final long DEFAULT_POLLING_INTERVAL_S = TimeUnit.MINUTES.toSeconds(10);

    private final Logger logger = LoggerFactory.getLogger(AutomowerHandler.class);
    private final TimeZoneProvider timeZoneProvider;
    private ZoneId mowerZoneId;

    private AtomicReference<String> automowerId = new AtomicReference<>(NO_ID);
    private long lastQueryTimeMs = 0L;

    private @Nullable ScheduledFuture<?> automowerPollingJob;
    // Max 1 request per second and appKey.
    private long maxQueryFrequencyNanos = TimeUnit.SECONDS.toNanos(1);

    private @Nullable Mower mowerState;

    private Runnable automowerPollingRunnable = () -> {
        Bridge bridge = getBridge();
        if (bridge != null && bridge.getStatus() == ThingStatus.ONLINE) {
            updateAutomowerState();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        }
    };

    public AutomowerHandler(Thing thing, TimeZoneProvider timeZoneProvider) {
        super(thing);
        this.timeZoneProvider = timeZoneProvider;
        this.mowerZoneId = timeZoneProvider.getTimeZone(); // default initializer
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (RefreshType.REFRESH == command) {
            logger.debug("Refreshing channel '{}'", channelUID);
            refreshChannels(channelUID);
        } else if (CHANNEL_CALENDARTASK.contains(channelUID.getId())) {
            sendAutomowerCalendarTask(command, channelUID.getId());
        } else if (CHANNEL_STAYOUTZONE.contains(channelUID.getId())) {
            String[] channelIDSplit = channelUID.getId().split("-");
            int index = Integer.parseInt(channelIDSplit[0].substring(GROUP_STAYOUTZONE.length())) - 1;
            if ("enabled".equals(channelIDSplit[0])) {
                if (command instanceof OnOffType cmd) {
                    sendAutomowerStayOutZone(index, ((cmd == OnOffType.ON) ? true : false));
                }
            }
        } else if (CHANNEL_WORKAREA.contains(channelUID.getId())) {
            String[] channelIDSplit = channelUID.getId().split("-");
            int index = Integer.parseInt(channelIDSplit[0].substring(GROUP_WORKAREA.length())) - 1;
            if ("enabled".equals(channelIDSplit[0])) {
                if (command instanceof OnOffType cmd) {
                    sendAutomowerWorkAreaEnable(index, ((cmd == OnOffType.ON) ? true : false));
                }
            } else if ("cutting-height".equals(channelIDSplit[0])) {
                if (command instanceof QuantityType cmd) {
                    cmd = cmd.toUnit("%");
                    if (cmd != null) {
                        sendAutomowerWorkAreaCuttingHeight(index, cmd.byteValue());
                    }
                } else if (command instanceof DecimalType cmd) {
                    sendAutomowerWorkAreaCuttingHeight(index, cmd.byteValue());
                }
            }
        } else if (channelUID.getId().equals(CHANNEL_SETTING_CUTTING_HEIGHT)) {
            if (command instanceof DecimalType cmd) {
                sendAutomowerSettingsCuttingHeight(cmd.byteValue());
            }
        } else if (channelUID.getId().equals(CHANNEL_SETTING_HEADLIGHT_MODE)) {
            if (command instanceof StringType cmd) {
                sendAutomowerSettingsHeadlightMode(cmd.toString());
            }
        } else if (channelUID.getId().equals(CHANNEL_STATUS_ERROR_CODE)) {
            if (command instanceof DecimalType cmd) {
                if (cmd.equals(new DecimalType(0))) {
                    sendAutomowerConfirmError();
                }
            }
        } else if (channelUID.getId().equals(CHANNEL_STATISTIC_CUTTING_BLADE_USAGE_TIME)) {
            if (command instanceof DecimalType cmd) {
                if (cmd.equals(new DecimalType(0))) {
                    sendAutomowerResetCuttingBladeUsageTime();
                }
            } else if (command instanceof QuantityType cmd) {
                if (cmd.intValue() == 0) {
                    sendAutomowerResetCuttingBladeUsageTime();
                }
            }
        } else {
            AutomowerCommand.fromChannelUID(channelUID).ifPresent(commandName -> {
                logger.debug("Sending command '{}'", commandName);
                getCommandValue(command).ifPresentOrElse(duration -> sendAutomowerCommand(commandName, duration),
                        () -> sendAutomowerCommand(commandName));

                updateState(channelUID, OnOffType.OFF);
            });
        }
    }

    private Optional<Integer> getCommandValue(Type type) {
        if (type instanceof DecimalType command) {
            return Optional.of(command.intValue());
        }
        return Optional.empty();
    }

    private void refreshChannels(ChannelUID channelUID) {
        updateAutomowerState();
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Set.of(AutomowerActions.class);
    }

    @Override
    public void initialize() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            AutomowerConfiguration currentConfig = getConfigAs(AutomowerConfiguration.class);
            final String configMowerId = currentConfig.getMowerId();
            final Integer pollingIntervalS = currentConfig.getPollingInterval();
            final String configMowerZoneId = currentConfig.getMowerZoneId();
            if ((configMowerZoneId != null) && !configMowerZoneId.isBlank()) {
                try {
                    mowerZoneId = ZoneId.of(configMowerZoneId);
                } catch (DateTimeException e) {
                    logger.warn("Invalid configuration mowerZoneId: {}, Error: {}", mowerZoneId, e.getMessage());
                    mowerZoneId = timeZoneProvider.getTimeZone(); // wrong config, use System TimeZone
                }
            } else {
                mowerZoneId = timeZoneProvider.getTimeZone(); // not configured, use System TimeZone
            }

            if (configMowerId == null) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "@text/conf-error-no-mower-id");
            } else if (pollingIntervalS != null && pollingIntervalS < 1) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "@text/conf-error-invalid-polling-interval");
            } else {
                automowerId.set(configMowerId);
                startAutomowerPolling(pollingIntervalS);
            }
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        }
    }

    @Nullable
    private AutomowerBridge getAutomowerBridge() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            ThingHandler handler = bridge.getHandler();
            if (handler instanceof AutomowerBridgeHandler bridgeHandler) {
                return bridgeHandler.getAutomowerBridge();
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        if (!automowerId.get().equals(NO_ID)) {
            stopAutomowerPolling();
            automowerId.set(NO_ID);
        }
    }

    private void startAutomowerPolling(@Nullable Integer pollingIntervalS) {
        if (automowerPollingJob == null) {
            final long pollingIntervalToUse = pollingIntervalS == null ? DEFAULT_POLLING_INTERVAL_S : pollingIntervalS;
            automowerPollingJob = scheduler.scheduleWithFixedDelay(automowerPollingRunnable, 1, pollingIntervalToUse,
                    TimeUnit.SECONDS);
        }
    }

    private void stopAutomowerPolling() {
        if (automowerPollingJob != null) {
            automowerPollingJob.cancel(true);
            automowerPollingJob = null;
        }
    }

    private boolean isValidResult(@Nullable Mower mower) {
        return ((mower != null && mower.getAttributes() != null) && (mower.getAttributes().getMetadata() != null)
                && (mower.getAttributes().getBattery() != null) && (mower.getAttributes().getSystem() != null)
                && (mower.getAttributes().getCalendar() != null)
                && (mower.getAttributes().getCalendar().getTasks() != null)
                && (mower.getAttributes().getCapabilities() != null) && (mower.getAttributes().getMower() != null)
                && (mower.getAttributes().getPlanner() != null)
                && (mower.getAttributes().getPlanner().getOverride() != null)
                && (mower.getAttributes().getSettings() != null) && (mower.getAttributes().getStatistics() != null));
    }

    private boolean isConnected(@Nullable Mower mower) {
        return mower != null && mower.getAttributes() != null && mower.getAttributes().getMetadata() != null
                && mower.getAttributes().getMetadata().isConnected();
    }

    private synchronized void updateAutomowerState() {
        String id = automowerId.get();
        try {
            AutomowerBridge automowerBridge = getAutomowerBridge();
            if (automowerBridge != null) {
                if (mowerState == null || (System.nanoTime() - lastQueryTimeMs > maxQueryFrequencyNanos)) {
                    lastQueryTimeMs = System.nanoTime();
                    mowerState = automowerBridge.getAutomowerStatus(id);
                }
                if (isValidResult(mowerState)) {
                    initializeProperties(mowerState);

                    updateChannelState(mowerState);

                    if (isConnected(mowerState)) {
                        updateStatus(ThingStatus.ONLINE);
                    } else {
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                                "@text/comm-error-mower-not-connected-to-cloud");
                    }
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/comm-error-query-mower-failed");
                }
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "@text/conf-error-no-bridge");
            }
        } catch (AutomowerCommunicationException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "@text/comm-error-query-mower-failed");
            logger.warn("Unable to query automower status for:  {}. Error: {}", id, e.getMessage());
        }
    }

    /**
     * Sends a command to the automower with the default duration of 60min
     *
     * @param command The command that should be sent. Valid values are: "Start", "ResumeSchedule", "Pause", "Park",
     *            "ParkUntilNextSchedule", "ParkUntilFurtherNotice"
     */
    public void sendAutomowerCommand(AutomowerCommand command) {
        sendAutomowerCommand(command, DEFAULT_COMMAND_DURATION_MIN);
    }

    /**
     * Sends a command to the automower with the given duration
     *
     * @param command The command that should be sent. Valid values are: "Start", "ResumeSchedule", "Pause", "Park",
     *            "ParkUntilNextSchedule", "ParkUntilFurtherNotice"
     * @param commandDurationMinutes The duration of the command in minutes. This is only evaluated for "Start" and
     *            "Park" commands
     */
    public void sendAutomowerCommand(AutomowerCommand command, long commandDurationMinutes) {
        logger.debug("Sending command '{} {}'", command.getCommand(), commandDurationMinutes);
        String id = automowerId.get();
        try {
            AutomowerBridge automowerBridge = getAutomowerBridge();
            if (automowerBridge != null) {
                automowerBridge.sendAutomowerCommand(id, command, commandDurationMinutes);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "@text/conf-error-no-bridge");
            }
        } catch (AutomowerCommunicationException e) {
            logger.warn("Unable to send Command to automower: {}, Error: {}", id, e.getMessage());
        }
        updateAutomowerState();
    }

    /**
     * Sends a CalendarTask to the automower
     *
     * @param command The command that should be sent. E.g. a duration in min for the Start channel
     * @param channelID The triggering channel
     */
    public void sendAutomowerCalendarTask(@Nullable Long workAreaId, short[] start, short[] duration, boolean[] monday,
            boolean[] tuesday, boolean[] wednesday, boolean[] thursday, boolean[] friday, boolean[] saturday,
            boolean[] sunday) {
        if (isValidResult(mowerState)) {
            List<CalendarTask> calendarTaskArray = new ArrayList<>();

            for (int i = 0; (i < start.length) && (i < duration.length) && (i < monday.length) && (i < tuesday.length)
                    && (i < wednesday.length) && (i < thursday.length) && (i < friday.length) && (i < saturday.length)
                    && (i < sunday.length); i++) {
                CalendarTask calendarTask = new CalendarTask();
                calendarTask.setStart(start[i]);
                calendarTask.setDuration(duration[i]);
                calendarTask.setMonday(monday[i]);
                calendarTask.setTuesday(tuesday[i]);
                calendarTask.setWednesday(wednesday[i]);
                calendarTask.setThursday(thursday[i]);
                calendarTask.setFriday(friday[i]);
                calendarTask.setSaturday(saturday[i]);
                calendarTask.setSunday(sunday[i]);
                if (workAreaId != null) {
                    calendarTask.setWorkAreaId(workAreaId);
                }
                calendarTaskArray.add(calendarTask);
            }

            String id = automowerId.get();
            try {
                AutomowerBridge automowerBridge = getAutomowerBridge();
                if (automowerBridge != null) {
                    automowerBridge.sendAutomowerCalendarTask(id,
                            mowerState.getAttributes().getCapabilities().hasWorkAreas(), workAreaId, calendarTaskArray);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/conf-error-no-bridge");
                }
            } catch (AutomowerCommunicationException e) {
                logger.warn("Unable to send CalendarTask to automower: {}, Error: {}", id, e.getMessage());
            }
        }
    }

    /**
     * Sends a CalendarTask to the automower
     *
     * @param command The command that should be sent. E.g. a duration in min for the Start channel
     * @param channelID The triggering channel
     */
    public void sendAutomowerCalendarTask(Command command, String channelID) {
        String[] channelIDSplit = channelID.split("-");
        int index = Integer.parseInt(channelIDSplit[0].substring("calendartasks".length())) - 1;
        String param = channelIDSplit[1];
        logger.debug("Sending CalendarTask '{}', index '{}', param '{}', command '{}'", channelID, index, param,
                command.toString());

        if (isValidResult(mowerState)) {
            List<CalendarTask> calendarTasksFiltered;
            List<CalendarTask> calendarTasksAll = mowerState.getAttributes().getCalendar().getTasks();
            int indexFiltered = 0;
            if (mowerState.getAttributes().getCapabilities().hasWorkAreas()) {
                // only set the Tasks of the current WorkArea
                calendarTasksFiltered = new ArrayList<>();
                int i = 0;
                for (CalendarTask calendarTask : calendarTasksAll) {
                    if (calendarTask.getWorkAreaId().equals(calendarTasksAll.get(index).getWorkAreaId())) {
                        if (index == i) {
                            // remember index and create deep copy
                            indexFiltered = calendarTasksFiltered.size();

                            CalendarTask calendarTask2 = new CalendarTask();
                            calendarTask2.setStart(calendarTask.getStart());
                            calendarTask2.setDuration(calendarTask.getDuration());
                            calendarTask2.setMonday(calendarTask.getMonday());
                            calendarTask2.setTuesday(calendarTask.getTuesday());
                            calendarTask2.setWednesday(calendarTask.getWednesday());
                            calendarTask2.setThursday(calendarTask.getThursday());
                            calendarTask2.setFriday(calendarTask.getFriday());
                            calendarTask2.setSaturday(calendarTask.getSaturday());
                            calendarTask2.setSunday(calendarTask.getSunday());
                            calendarTask2.setWorkAreaId(calendarTask.getWorkAreaId());
                            calendarTasksFiltered.add(calendarTask2);
                        } else {
                            // no deep copy required for the lines that are not updated
                            calendarTasksFiltered.add(calendarTask);
                        }
                    }
                    i++;
                }
            } else {
                indexFiltered = index;
                calendarTasksFiltered = calendarTasksAll;
            }

            CalendarTask calendarTask = calendarTasksFiltered.get(indexFiltered);

            if (command instanceof DecimalType cmd) {
                if ("start".equals(param)) {
                    calendarTask.setStart(cmd.shortValue());
                } else if ("duration".equals(param)) {
                    calendarTask.setDuration(cmd.shortValue());
                }
            } else if (command instanceof QuantityType cmd) {
                cmd = cmd.toUnit("min");
                if (cmd != null) {
                    if ("start".equals(param)) {
                        calendarTask.setStart(cmd.shortValue());
                    } else if ("duration".equals(param)) {
                        calendarTask.setDuration(cmd.shortValue());
                    }
                }
            } else if (command instanceof OnOffType cmd) {
                boolean day = ((cmd == OnOffType.ON) ? true : false);

                if ("monday".equals(param)) {
                    calendarTask.setMonday(day);
                } else if ("tuesday".equals(param)) {
                    calendarTask.setTuesday(day);
                } else if ("wednesday".equals(param)) {
                    calendarTask.setWednesday(day);
                } else if ("thursday".equals(param)) {
                    calendarTask.setThursday(day);
                } else if ("friday".equals(param)) {
                    calendarTask.setFriday(day);
                } else if ("saturday".equals(param)) {
                    calendarTask.setSaturday(day);
                } else if ("sunday".equals(param)) {
                    calendarTask.setSunday(day);
                }

                String id = automowerId.get();
                try {
                    AutomowerBridge automowerBridge = getAutomowerBridge();
                    if (automowerBridge != null) {
                        automowerBridge.sendAutomowerCalendarTask(id,
                                mowerState.getAttributes().getCapabilities().hasWorkAreas(),
                                calendarTask.getWorkAreaId(), calendarTasksFiltered);
                    } else {
                        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                                "@text/conf-error-no-bridge");
                    }
                } catch (AutomowerCommunicationException e) {
                    logger.warn("Unable to send CalendarTask to automower: {}, Error: {}", id, e.getMessage());
                }
            }
        }
        updateAutomowerState();
    }

    /**
     * Sends StayOutZone Setting to the automower
     *
     * @param index Index of zone
     * @param enable Zone enabled or disabled
     */
    public void sendAutomowerStayOutZone(int index, boolean enable) {
        if (isValidResult(mowerState)) {
            sendAutomowerStayOutZone(mowerState.getAttributes().getStayOutZones().getZones().get(index).getId(),
                    enable);
        }
    }

    /**
     * Sends StayOutZone Setting to the automower
     *
     * @param zoneId Id of zone
     * @param enable Zone enabled or disabled
     */
    public void sendAutomowerStayOutZone(String zoneId, boolean enable) {
        logger.debug("Sending StayOutZone: zoneId {}, enable {}", zoneId, enable);
        if (isValidResult(mowerState)) {
            MowerStayOutZoneAttributes attributes = new MowerStayOutZoneAttributes();
            attributes.setEnable(enable);

            String id = automowerId.get();
            try {
                AutomowerBridge automowerBridge = getAutomowerBridge();
                if (automowerBridge != null) {
                    automowerBridge.sendAutomowerStayOutZone(id, zoneId, attributes);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/conf-error-no-bridge");
                }
            } catch (AutomowerCommunicationException e) {
                logger.warn("Unable to send StayOutZone to automower: {}, Error: {}", id, e.getMessage());
            }

            updateAutomowerState();
        }
    }

    /**
     * Sends WorkArea enable Setting to the automower
     *
     * @param index Index of WorkArea
     * @param enable WorkArea enabled or disabled
     * 
     */
    public void sendAutomowerWorkAreaEnable(int index, boolean enable) {
        if (isValidResult(mowerState)) {
            sendAutomowerWorkArea(mowerState.getAttributes().getWorkAreas().get(index).getWorkAreaId(), enable,
                    mowerState.getAttributes().getWorkAreas().get(index).getCuttingHeight());
        }
    }

    /**
     * Sends WorkArea CuttingHeight Setting to the automower
     * 
     * @param index Index of WorkArea
     * @param cuttingHeight CuttingHeight of the WorkArea
     * 
     */
    public void sendAutomowerWorkAreaCuttingHeight(int index, byte cuttingHeight) {
        if (isValidResult(mowerState)) {
            sendAutomowerWorkArea(mowerState.getAttributes().getWorkAreas().get(index).getWorkAreaId(),
                    mowerState.getAttributes().getWorkAreas().get(index).isEnabled(), cuttingHeight);
        }
    }

    /**
     * Sends WorkArea Settings to the automower
     *
     * @param workAreaId Id of WorkArea
     * @param enable Work area enable or disabled
     * @param cuttingHeight CuttingHeight of the WorkArea
     */
    public void sendAutomowerWorkArea(long workAreaId, boolean enable, byte cuttingHeight) {
        logger.debug("Sending WorkArea: workAreaId {}, enable {}, cuttingHeight {}", workAreaId, enable, cuttingHeight);
        if (isValidResult(mowerState)) {
            MowerWorkAreaAttributes workAreaAttributes = new MowerWorkAreaAttributes();
            workAreaAttributes.setEnable(enable);
            workAreaAttributes.setCuttingHeight(cuttingHeight);

            String id = automowerId.get();
            try {
                AutomowerBridge automowerBridge = getAutomowerBridge();
                if (automowerBridge != null) {
                    automowerBridge.sendAutomowerWorkArea(id, workAreaId, workAreaAttributes);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/conf-error-no-bridge");
                }
            } catch (AutomowerCommunicationException e) {
                logger.warn("Unable to send WorkArea to automower: {}, Error: {}", id, e.getMessage());
            }

            updateAutomowerState();
        }
    }

    /**
     * Sends CuttingHeight Setting to the automower
     *
     * @param cuttingHeight The cuttingHeight to be sent
     */
    public void sendAutomowerSettingsCuttingHeight(byte cuttingHeight) {
        if (isValidResult(mowerState)) {
            sendAutomowerSettings(cuttingHeight, null);
        }
    }

    /**
     * Sends HeadlightMode Setting to the automower
     *
     * @param headlightMode Headlight mode as string to be sent
     */
    public void sendAutomowerSettingsHeadlightMode(String headlightMode) {
        if (isValidResult(mowerState)) {
            try {
                sendAutomowerSettings(null, HeadlightMode.valueOf(headlightMode));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid HeadlightMode: {}, Error: {}", headlightMode, e.getMessage());
            }
        }
    }

    /**
     * Sends a set of Settings to the automower
     *
     * @param cuttingHeight The cuttingHeight to be sent
     * @param headlightMode Headlight mode as string to be sent
     */
    public void sendAutomowerSettings(@Nullable Byte cuttingHeight, @Nullable HeadlightMode headlightMode) {
        logger.debug("Sending Settings: cuttingHeight {}, headlightMode {}", cuttingHeight,
                ((headlightMode != null) ? headlightMode.toString() : "null"));
        if (isValidResult(mowerState)) {
            Settings settings = new Settings();
            if (cuttingHeight != null) {
                settings.setCuttingHeight(cuttingHeight);
            }
            if (headlightMode != null) {
                Headlight headlight = new Headlight();
                headlight.setHeadlightMode(headlightMode);
                settings.setHeadlight(headlight);
            }

            String id = automowerId.get();
            try {
                AutomowerBridge automowerBridge = getAutomowerBridge();
                if (automowerBridge != null) {
                    automowerBridge.sendAutomowerSettings(id, settings);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/conf-error-no-bridge");
                }
            } catch (AutomowerCommunicationException e) {
                logger.warn("Unable to send SettingCuttingHeight to automower: {}, Error: {}", id, e.getMessage());
            }

            updateAutomowerState();
        }
    }

    /**
     * Confirm current non fatal error on the mower
     */
    public void sendAutomowerConfirmError() {
        logger.debug("Sending ConfirmError");
        if (isValidResult(mowerState) && (mowerState.getAttributes().getCapabilities().canConfirmError())
                && (mowerState.getAttributes().getMower().getIsErrorConfirmable())) {
            String id = automowerId.get();
            try {
                AutomowerBridge automowerBridge = getAutomowerBridge();
                if (automowerBridge != null) {
                    automowerBridge.sendAutomowerConfirmError(id);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                            "@text/conf-error-no-bridge");
                }
            } catch (AutomowerCommunicationException e) {
                logger.warn("Unable to send ConfirmError to automower: {}, Error: {}", id, e.getMessage());
            }
            updateAutomowerState();
        }
    }

    /**
     * Reset the cutting blade usage time
     */
    public void sendAutomowerResetCuttingBladeUsageTime() {
        logger.debug("Sending ResetCuttingBladeUsageTime");
        String id = automowerId.get();
        try {
            AutomowerBridge automowerBridge = getAutomowerBridge();
            if (automowerBridge != null) {
                automowerBridge.sendAutomowerResetCuttingBladeUsageTime(id);
            } else {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "@text/conf-error-no-bridge");
            }
        } catch (AutomowerCommunicationException e) {
            logger.warn("Unable to send ResetCuttingBladeUsageTime to automower: {}, Error: {}", id, e.getMessage());
        }
        updateAutomowerState();
    }

    private String restrictedState(RestrictedReason reason) {
        return "RESTRICTED_" + reason.name();
    }

    private @Nullable String getErrorMessage(int errorCode) {
        return ERROR.get(errorCode);
    }

    private @Nullable WorkArea getWorkAreaById(@Nullable Mower mower, long workAreaId) {
        if (mower != null) {
            List<WorkArea> workAreas = mower.getAttributes().getWorkAreas();
            for (WorkArea workArea : workAreas) {
                if (workArea.getWorkAreaId() == workAreaId) {
                    return workArea;
                }
            }
        }
        return null;
    }

    private void updateChannelState(@Nullable Mower mower) {
        if (isValidResult(mower)) {
            Capabilities capabilities = mower.getAttributes().getCapabilities();

            updateState(CHANNEL_STATUS_NAME, new StringType(mower.getAttributes().getSystem().getName()));

            updateState(CHANNEL_STATUS_MODE, new StringType(mower.getAttributes().getMower().getMode().name()));
            updateState(CHANNEL_STATUS_ACTIVITY, new StringType(mower.getAttributes().getMower().getActivity().name()));
            updateState(CHANNEL_STATUS_INACTIVE_REASON,
                    new StringType(mower.getAttributes().getMower().getInactiveReason().name()));

            if (mower.getAttributes().getMower().getState() != State.RESTRICTED) {
                updateState(CHANNEL_STATUS_STATE, new StringType(mower.getAttributes().getMower().getState().name()));
            } else {
                updateState(CHANNEL_STATUS_STATE,
                        new StringType(restrictedState(mower.getAttributes().getPlanner().getRestrictedReason())));
            }

            if (capabilities.hasWorkAreas()) {
                Long workAreaId = mower.getAttributes().getMower().getWorkAreaId();
                if (workAreaId != null) {
                    createAndUpdateChannel(CHANNEL_STATUS_WORK_AREA_ID, CHANNEL_TYPE_STATUS_WORK_AREA_ID, "Number",
                            new DecimalType(workAreaId));
                    WorkArea workArea = getWorkAreaById(mower, workAreaId);
                    if ((workArea != null) && (workArea.getName() != null)) {
                        if ((workAreaId.equals(0L)) && workArea.getName().isBlank()) {
                            createAndUpdateChannel(CHANNEL_STATUS_WORK_AREA, CHANNEL_TYPE_STATUS_WORK_AREA, "String",
                                    new StringType("main area"));
                        } else {
                            createAndUpdateChannel(CHANNEL_STATUS_WORK_AREA, CHANNEL_TYPE_STATUS_WORK_AREA, "String",
                                    new StringType(workArea.getName()));
                        }
                    } else {
                        createAndUpdateChannel(CHANNEL_STATUS_WORK_AREA, CHANNEL_TYPE_STATUS_WORK_AREA, "String",
                                UnDefType.NULL);
                    }
                } else {
                    createAndUpdateChannel(CHANNEL_STATUS_WORK_AREA_ID, CHANNEL_TYPE_STATUS_WORK_AREA_ID, "Number",
                            UnDefType.NULL);
                }
            } else {
                removeChannel(CHANNEL_STATUS_WORK_AREA_ID);
                removeChannel(CHANNEL_STATUS_WORK_AREA);
            }

            updateState(CHANNEL_STATUS_LAST_UPDATE, new DateTimeType(
                    toZonedDateTime(mower.getAttributes().getMetadata().getStatusTimestamp(), ZoneId.of("UTC"))));
            updateState(CHANNEL_STATUS_BATTERY,
                    new QuantityType<>(mower.getAttributes().getBattery().getBatteryPercent(), Units.PERCENT));

            int errorCode = mower.getAttributes().getMower().getErrorCode();
            updateState(CHANNEL_STATUS_ERROR_CODE, new DecimalType(errorCode));
            String errorMessage = getErrorMessage(errorCode);
            if (errorMessage != null) {
                updateState(CHANNEL_STATUS_ERROR_MESSAGE, new StringType(errorMessage));
            } else {
                updateState(CHANNEL_STATUS_ERROR_MESSAGE, UnDefType.NULL);
            }

            long errorCodeTimestamp = mower.getAttributes().getMower().getErrorCodeTimestamp();
            if (errorCodeTimestamp == 0L) {
                updateState(CHANNEL_STATUS_ERROR_TIMESTAMP, UnDefType.NULL);
            } else {
                updateState(CHANNEL_STATUS_ERROR_TIMESTAMP,
                        new DateTimeType(toZonedDateTime(errorCodeTimestamp, mowerZoneId)));
            }

            if (capabilities.canConfirmError()) {
                createAndUpdateChannel(CHANNEL_STATUS_ERROR_CONFIRMABLE, CHANNEL_TYPE_STATUS_ERROR_CONFIRMABLE,
                        "Switch", OnOffType.from(mower.getAttributes().getMower().getIsErrorConfirmable()));
            } else {
                removeChannel(CHANNEL_STATUS_ERROR_CONFIRMABLE);
            }

            long nextStartTimestamp = mower.getAttributes().getPlanner().getNextStartTimestamp();
            // If next start timestamp is 0 it means the mower should start now, so using current timestamp
            if (nextStartTimestamp == 0L) {
                updateState(CHANNEL_STATUS_NEXT_START, UnDefType.NULL);
            } else {
                updateState(CHANNEL_STATUS_NEXT_START,
                        new DateTimeType(toZonedDateTime(nextStartTimestamp, mowerZoneId)));
            }
            updateState(CHANNEL_STATUS_OVERRIDE_ACTION,
                    new StringType(mower.getAttributes().getPlanner().getOverride().getAction().name()));
            RestrictedReason restrictedReason = mower.getAttributes().getPlanner().getRestrictedReason();
            if (restrictedReason != null) {
                updateState(CHANNEL_STATUS_RESTRICTED_REASON, new StringType(restrictedReason.name()));
            } else {
                updateState(CHANNEL_STATUS_RESTRICTED_REASON, UnDefType.NULL);
            }

            updateState(CHANNEL_STATUS_EXTERNAL_REASON,
                    new DecimalType(mower.getAttributes().getPlanner().getExternalReason()));

            updateState(CHANNEL_SETTING_CUTTING_HEIGHT,
                    new DecimalType(mower.getAttributes().getSettings().getCuttingHeight()));

            if (capabilities.hasHeadlights()) {
                Headlight headlight = mower.getAttributes().getSettings().getHeadlight();
                if (headlight != null) {
                    createAndUpdateChannel(CHANNEL_SETTING_HEADLIGHT_MODE, CHANNEL_TYPE_SETTING_HEADLIGHT_MODE,
                            "String", new StringType(headlight.getHeadlightMode().name()));
                } else {
                    createAndUpdateChannel(CHANNEL_SETTING_HEADLIGHT_MODE, CHANNEL_TYPE_SETTING_HEADLIGHT_MODE,
                            "String", UnDefType.NULL);
                }
            } else {
                removeChannel(CHANNEL_SETTING_HEADLIGHT_MODE);
            }

            updateState(CHANNEL_STATISTIC_CUTTING_BLADE_USAGE_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getCuttingBladeUsageTime(), Units.SECOND));
            updateState(CHANNEL_STATISTIC_DOWN_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getDownTime(), Units.SECOND));
            updateState(CHANNEL_STATISTIC_NUMBER_OF_CHARGING_CYCLES,
                    new DecimalType(mower.getAttributes().getStatistics().getNumberOfChargingCycles()));
            updateState(CHANNEL_STATISTIC_NUMBER_OF_COLLISIONS,
                    new DecimalType(mower.getAttributes().getStatistics().getNumberOfCollisions()));
            updateState(CHANNEL_STATISTIC_TOTAL_CHARGING_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getTotalChargingTime(), Units.SECOND));
            updateState(CHANNEL_STATISTIC_TOTAL_CUTTING_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getTotalCuttingTime(), Units.SECOND));
            updateState(CHANNEL_STATISTIC_TOTAL_DRIVE_DISTANCE,
                    new QuantityType<>(mower.getAttributes().getStatistics().getTotalDriveDistance(), SIUnits.METRE));
            updateState(CHANNEL_STATISTIC_TOTAL_RUNNING_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getTotalRunningTime(), Units.SECOND));
            updateState(CHANNEL_STATISTIC_TOTAL_SEARCHING_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getTotalSearchingTime(), Units.SECOND));

            if (mower.getAttributes().getStatistics().getTotalRunningTime() != 0) {
                updateState(CHANNEL_STATISTIC_TOTAL_CUTTING_PERCENT,
                        new QuantityType<>(
                                (float) mower.getAttributes().getStatistics().getTotalCuttingTime()
                                        / (float) mower.getAttributes().getStatistics().getTotalRunningTime() * 100.0,
                                Units.PERCENT));
                updateState(CHANNEL_STATISTIC_TOTAL_SEARCHING_PERCENT,
                        new QuantityType<>(
                                (float) mower.getAttributes().getStatistics().getTotalSearchingTime()
                                        / (float) mower.getAttributes().getStatistics().getTotalRunningTime() * 100.0,
                                Units.PERCENT));
            } else {
                updateState(CHANNEL_STATISTIC_TOTAL_CUTTING_PERCENT, new QuantityType<>(0, Units.PERCENT));
                updateState(CHANNEL_STATISTIC_TOTAL_SEARCHING_PERCENT, new QuantityType<>(0, Units.PERCENT));
            }
            updateState(CHANNEL_STATISTIC_UP_TIME,
                    new QuantityType<>(mower.getAttributes().getStatistics().getUpTime(), Units.SECOND));

            if (capabilities.hasPosition()) {
                // if (mower.getAttributes().getLastPosition() != null) {
                createAndUpdateChannel(CHANNEL_POSITION_LAST, CHANNEL_TYPE_POSITION_LAST, "Location",
                        new PointType(new DecimalType(mower.getAttributes().getLastPosition().getLatitude()),
                                new DecimalType(mower.getAttributes().getLastPosition().getLongitude())));
                List<Position> positions = mower.getAttributes().getPositions();
                for (int i = 0; i < positions.size(); i++) {
                    createAndUpdateIndexedChannel(GROUP_POSITION, i + 1, "", CHANNEL_TYPE_POSITION, "Location",
                            new PointType(new DecimalType(positions.get(i).getLatitude()),
                                    new DecimalType(positions.get(i).getLongitude())));
                }
            } else {
                removeChannel(CHANNEL_POSITION_LAST);
                removeConsecutiveIndexedChannels(GROUP_POSITION, 1, "");
            }

            List<CalendarTask> calendarTasks = mower.getAttributes().getCalendar().getTasks();
            int i;
            for (i = 0; i < calendarTasks.size(); i++) {
                int j = 0;
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Number:Time",
                        new QuantityType<>(calendarTasks.get(i).getStart(), Units.MINUTE));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Number:Time",
                        new QuantityType<>(calendarTasks.get(i).getDuration(), Units.MINUTE));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch", OnOffType.from(calendarTasks.get(i).getMonday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch",
                        OnOffType.from(calendarTasks.get(i).getTuesday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch",
                        OnOffType.from(calendarTasks.get(i).getWednesday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch",
                        OnOffType.from(calendarTasks.get(i).getThursday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch", OnOffType.from(calendarTasks.get(i).getFriday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch",
                        OnOffType.from(calendarTasks.get(i).getSaturday()));
                createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                        CHANNEL_TYPE_CALENDARTASK.get(j++), "Switch", OnOffType.from(calendarTasks.get(i).getSunday()));
                if (capabilities.hasWorkAreas()) {
                    createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                            CHANNEL_TYPE_CALENDARTASK.get(j++), "Number",
                            new DecimalType(calendarTasks.get(i).getWorkAreaId()));
                    WorkArea workArea = getWorkAreaById(mower, calendarTasks.get(i).getWorkAreaId());
                    if (workArea != null) {
                        if ((calendarTasks.get(i).getWorkAreaId().equals(0L)) && workArea.getName().isBlank()) {
                            createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                                    CHANNEL_TYPE_CALENDARTASK.get(j++), "String", new StringType("main area"));
                        } else {
                            createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                                    CHANNEL_TYPE_CALENDARTASK.get(j++), "String", new StringType(workArea.getName()));
                        }
                    } else {
                        createAndUpdateIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j),
                                CHANNEL_TYPE_CALENDARTASK.get(j++), "String", UnDefType.NULL);
                    }
                } else {
                    removeIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j++));
                    removeIndexedChannel(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j++));
                }
            }

            // remove all consecutive channels that are no longer required
            for (int j = 0; j < CHANNEL_CALENDARTASK.size(); j++) {
                removeConsecutiveIndexedChannels(GROUP_CALENDARTASK, i + 1, CHANNEL_CALENDARTASK.get(j));
            }

            i = 0;
            if (capabilities.hasStayOutZones()) {
                createAndUpdateChannel(CHANNEL_STAYOUTZONES_DIRTY, CHANNEL_TYPE_STAYOUTZONES_DIRTY, "Switch",
                        OnOffType.from(mower.getAttributes().getStayOutZones().isDirty()));

                if (mower.getAttributes().getStayOutZones() != null) {
                    List<StayOutZone> stayOutZones = mower.getAttributes().getStayOutZones().getZones();
                    if (stayOutZones != null) {
                        for (; i < stayOutZones.size(); i++) {
                            int j = 0;
                            createAndUpdateIndexedChannel(GROUP_STAYOUTZONE, i + 1, CHANNEL_STAYOUTZONE.get(j),
                                    CHANNEL_TYPE_STAYOUTZONE.get(j++), "String",
                                    new StringType(stayOutZones.get(i).getId()));
                            createAndUpdateIndexedChannel(GROUP_STAYOUTZONE, i + 1, CHANNEL_STAYOUTZONE.get(j),
                                    CHANNEL_TYPE_STAYOUTZONE.get(j++), "String",
                                    new StringType(stayOutZones.get(i).getName()));
                            createAndUpdateIndexedChannel(GROUP_STAYOUTZONE, i + 1, CHANNEL_STAYOUTZONE.get(j),
                                    CHANNEL_TYPE_STAYOUTZONE.get(j++), "Switch",
                                    OnOffType.from(stayOutZones.get(i).isEnabled()));
                        }
                    }
                }
            } else {
                removeChannel(CHANNEL_STAYOUTZONES_DIRTY);
            }
            // remove all consecutive channels that are no longer required
            for (int j = 0; j < CHANNEL_STAYOUTZONE.size(); j++) {
                removeConsecutiveIndexedChannels(GROUP_STAYOUTZONE, i + 1, CHANNEL_STAYOUTZONE.get(j));
            }

            i = 0;
            if (capabilities.hasWorkAreas()) {
                List<WorkArea> workAreas = mower.getAttributes().getWorkAreas();
                if (workAreas != null) {
                    for (; i < workAreas.size(); i++) {
                        int j = 0;
                        WorkArea workArea = workAreas.get(i);
                        createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                CHANNEL_TYPE_WORKAREA.get(j++), "Number", new DecimalType(workArea.getWorkAreaId()));

                        if ((workArea.getWorkAreaId() == 0L) && workArea.getName().isBlank()) {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "String", new StringType("main area"));
                        } else {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "String", new StringType(workArea.getName()));
                        }
                        createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                CHANNEL_TYPE_WORKAREA.get(j++), "Number:Dimensionless",
                                new QuantityType<>(workArea.getCuttingHeight(), Units.PERCENT));
                        createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                CHANNEL_TYPE_WORKAREA.get(j++), "Switch", OnOffType.from(workArea.isEnabled()));
                        if (workArea.getProgress() != null) {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "Number:Dimensionless",
                                    new QuantityType<>(workArea.getProgress(), Units.PERCENT));
                        } else {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "Number:Dimensionless", UnDefType.NULL);
                        }
                        if ((workArea.getLastTimeCompleted() != null) && (workArea.getLastTimeCompleted() != 0)) {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "DateTime",
                                    new DateTimeType(toZonedDateTime(workArea.getLastTimeCompleted(), mowerZoneId)));
                        } else {
                            createAndUpdateIndexedChannel(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j),
                                    CHANNEL_TYPE_WORKAREA.get(j++), "DateTime", UnDefType.NULL);
                        }
                    }
                }
            }
            // remove all consecutive channels that are no longer required
            for (int j = 0; j < CHANNEL_WORKAREA.size(); j++) {
                removeConsecutiveIndexedChannels(GROUP_WORKAREA, i + 1, CHANNEL_WORKAREA.get(j));
            }
        }
    }

    private void initializeProperties(@Nullable Mower mower) {
        if (isValidResult(mower)) {
            Map<String, String> properties = editProperties();
            properties.put(AutomowerBindingConstants.AUTOMOWER_ID, mower.getId());

            properties.put(AutomowerBindingConstants.AUTOMOWER_SERIAL_NUMBER,
                    mower.getAttributes().getSystem().getSerialNumber());
            properties.put(AutomowerBindingConstants.AUTOMOWER_MODEL, mower.getAttributes().getSystem().getModel());
            properties.put(AutomowerBindingConstants.AUTOMOWER_NAME, mower.getAttributes().getSystem().getName());

            Capabilities capabilities = mower.getAttributes().getCapabilities();
            properties.put(AutomowerBindingConstants.AUTOMOWER_CAN_CONFIRM_ERROR,
                    (capabilities.canConfirmError() ? "yes" : "no"));
            properties.put(AutomowerBindingConstants.AUTOMOWER_HAS_HEADLIGHTS,
                    (capabilities.hasHeadlights() ? "yes" : "no"));
            properties.put(AutomowerBindingConstants.AUTOMOWER_HAS_POSITION,
                    (capabilities.hasPosition() ? "yes" : "no"));
            properties.put(AutomowerBindingConstants.AUTOMOWER_HAS_STAY_OUT_ZONES,
                    (capabilities.hasStayOutZones() ? "yes" : "no"));
            properties.put(AutomowerBindingConstants.AUTOMOWER_HAS_WORK_AREAS,
                    (capabilities.hasWorkAreas() ? "yes" : "no"));

            updateProperties(properties);
        }
    }

    /**
     * Converts timestamp returned by the Automower API into ZonedDateTime of the specified timezone.
     * Timestamp returned by the API is a mix of timezone specified via zoneId and UTC timezone.
     * Method builds a valid ZonedDateTime object according to the provided zoneId parameter.
     *
     * @param timestamp - Automower API timestamp
     * @param zoneId - Intended timezone of the timestamp
     * @return ZonedDateTime using provided timezone
     */
    private ZonedDateTime toZonedDateTime(long timestamp, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC")).withZoneSameLocal(zoneId);
    }

    private String getIndexedChannel(String ChannelGroup, int id, String channel) {
        // 2 digits as default and all digits for the unrealistic case of more than 99 channels
        String format = ((id >= 100) ? "%d" : "%02d");
        return ChannelGroup + String.format(format, id) + (channel.isBlank() ? "" : "-" + channel);
    }

    private void createAndUpdateIndexedChannel(String ChannelGroup, int id, String channel,
            ChannelTypeUID channelTypeUID, String itemType, org.openhab.core.types.State state) {
        String indexedChannel = getIndexedChannel(ChannelGroup, id, channel);
        createChannel(indexedChannel, channelTypeUID, itemType);
        updateState(indexedChannel, state);
    }

    private void createAndUpdateChannel(String channel, ChannelTypeUID channelTypeUID, String itemType,
            org.openhab.core.types.State state) {
        createChannel(channel, channelTypeUID, itemType);
        updateState(channel, state);
    }

    private void createChannel(String channel, ChannelTypeUID channelTypeUID, String itemType) {
        ChannelUID channelUid = new ChannelUID(thing.getUID(), channel);
        if (thing.getChannel(channelUid) == null) {
            updateThing(editThing()
                    .withChannel(ChannelBuilder.create(channelUid, itemType).withType(channelTypeUID).build()).build());
        }
    }

    private void removeIndexedChannel(String ChannelGroup, int id, String channel) {
        String indexedChannel = getIndexedChannel(ChannelGroup, id, channel);
        removeChannel(indexedChannel);
    }

    private void removeConsecutiveIndexedChannels(String ChannelGroup, int id, String channel) {
        String indexedChannel = getIndexedChannel(ChannelGroup, id, channel);
        if (removeChannel(indexedChannel)) {
            // remove next channel recursively
            removeConsecutiveIndexedChannels(ChannelGroup, id + 1, channel);
        }
    }

    private boolean removeChannel(String channel) {
        ChannelUID channelUid = new ChannelUID(thing.getUID(), channel);
        if (thing.getChannel(channelUid) != null) {
            updateThing(editThing().withoutChannel(channelUid).build());
            return true;
        } else {
            return false;
        }
    }
}
