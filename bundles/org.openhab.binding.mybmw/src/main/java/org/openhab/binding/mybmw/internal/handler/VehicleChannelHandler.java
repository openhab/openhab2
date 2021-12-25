/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.mybmw.internal.handler;

import static org.openhab.binding.mybmw.internal.MyBMWConstants.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.measure.quantity.Length;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.mybmw.internal.MyBMWConstants.VehicleType;
import org.openhab.binding.mybmw.internal.dto.properties.DoorsWindows;
import org.openhab.binding.mybmw.internal.dto.properties.Location;
import org.openhab.binding.mybmw.internal.dto.status.CBSMessage;
import org.openhab.binding.mybmw.internal.dto.status.CCMMessage;
import org.openhab.binding.mybmw.internal.dto.vehicle.Vehicle;
import org.openhab.binding.mybmw.internal.utils.ChargeProfileUtils;
import org.openhab.binding.mybmw.internal.utils.ChargeProfileUtils.TimedChannel;
import org.openhab.binding.mybmw.internal.utils.ChargeProfileWrapper;
import org.openhab.binding.mybmw.internal.utils.ChargeProfileWrapper.ProfileKey;
import org.openhab.binding.mybmw.internal.utils.Constants;
import org.openhab.binding.mybmw.internal.utils.Converter;
import org.openhab.binding.mybmw.internal.utils.RemoteServiceUtils;
import org.openhab.binding.mybmw.internal.utils.VehicleStatusUtils;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.ImperialUnits;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.State;
import org.openhab.core.types.StateOption;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VehicleChannelHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Bernd Weymann - Initial contribution
 * @author Norbert Truchsess - edit & send of charge profile
 */
@NonNullByDefault
public abstract class VehicleChannelHandler extends BaseThingHandler {
    protected final Logger logger = LoggerFactory.getLogger(VehicleChannelHandler.class);
    protected boolean hasFuel = false;
    protected boolean isElectric = false;
    protected boolean isHybrid = false;
    protected String localeLanguage;

    // List Interfaces
    protected List<CBSMessage> serviceList = new ArrayList<CBSMessage>();
    protected String selectedService = Constants.UNDEF;
    protected List<CCMMessage> checkControlList = new ArrayList<CCMMessage>();
    protected String selectedCC = Constants.UNDEF;

    protected MyBMWOptionProvider optionProvider;

    // Data Caches
    protected Optional<String> vehicleStatusCache = Optional.empty();
    protected Optional<byte[]> imageCache = Optional.empty();

    public VehicleChannelHandler(Thing thing, MyBMWOptionProvider op, String type, String langauge) {
        super(thing);
        optionProvider = op;
        localeLanguage = langauge;

        hasFuel = type.equals(VehicleType.CONVENTIONAL.toString()) || type.equals(VehicleType.PLUGIN_HYBRID.toString())
                || type.equals(VehicleType.ELECTRIC_REX.toString());
        isElectric = type.equals(VehicleType.PLUGIN_HYBRID.toString())
                || type.equals(VehicleType.ELECTRIC_REX.toString()) || type.equals(VehicleType.ELECTRIC.toString());
        isHybrid = hasFuel && isElectric;

        setOptions(CHANNEL_GROUP_REMOTE, REMOTE_SERVICE_COMMAND, RemoteServiceUtils.getOptions(isElectric));
    }

    private void setOptions(final String group, final String id, List<StateOption> options) {
        optionProvider.setStateOptions(new ChannelUID(thing.getUID(), group, id), options);
    }

    protected void updateChannel(final String group, final String id, final State state) {
        updateState(new ChannelUID(thing.getUID(), group, id), state);
    }

    protected void updateVehicle(Vehicle v) {
        updateVehicleStatus(v);
    }

    protected void updateVehicleStatus(Vehicle v) {
        // Vehicle Status
        updateChannel(CHANNEL_GROUP_STATUS, LOCK, OnOffType.from(v.properties.areDoorsLocked));

        // Service Updates
        updateChannel(CHANNEL_GROUP_STATUS, SERVICE_DATE, DateTimeType.valueOf(
                Converter.getZonedDateTime(VehicleStatusUtils.getNextServiceDate(v.properties.serviceRequired))));

        // [todo] CheckControl Active?
        // updateChannel(CHANNEL_GROUP_STATUS, CHECK_CONTROL,
        // StringType.valueOf(Converter.toTitleCase(VehicleStatusUtils.checkControlActive(vStatus))));
        // last update Time
        updateChannel(CHANNEL_GROUP_STATUS, LAST_UPDATE,
                DateTimeType.valueOf(Converter.getZonedDateTime(v.properties.lastUpdatedAt)));

        updateChannel(CHANNEL_GROUP_STATUS, DOORS, OnOffType.from(v.properties.areDoorsClosed));
        updateDoors(v.properties.doorsAndWindows);
        updateChannel(CHANNEL_GROUP_STATUS, WINDOWS, OnOffType.from(v.properties.areWindowsClosed));
        updateWindows(v.properties.doorsAndWindows);

        boolean imperial = false;
        if (isElectric) {
            String unit = v.properties.electricRange.distance.units;
            imperial = !Constants.KILOMETERS_JSON.equals(unit);
            int rangeElectric = v.properties.electricRange.distance.value;
            QuantityType<Length> qtElectricRange = QuantityType.valueOf(rangeElectric, Constants.KILOMETRE_UNIT);
            QuantityType<Length> qtElectricRadius = QuantityType.valueOf(Converter.guessRangeRadius(rangeElectric),
                    Constants.KILOMETRE_UNIT);

            updateChannel(CHANNEL_GROUP_RANGE, RANGE_ELECTRIC,
                    imperial ? Converter.getMiles(qtElectricRange) : qtElectricRange);
            updateChannel(CHANNEL_GROUP_RANGE, RANGE_RADIUS_ELECTRIC,
                    imperial ? Converter.getMiles(qtElectricRadius) : qtElectricRadius);

        }
        if (hasFuel) {
            String unit = v.properties.combustionRange.distance.units;
            imperial = !Constants.KILOMETERS_JSON.equals(unit);
            int rangeFuel = v.properties.combustionRange.distance.value;
            QuantityType<Length> qtFuelRange = QuantityType.valueOf(rangeFuel, Constants.KILOMETRE_UNIT);
            QuantityType<Length> qtFuelRadius = QuantityType.valueOf(Converter.guessRangeRadius(rangeFuel),
                    Constants.KILOMETRE_UNIT);

            updateChannel(CHANNEL_GROUP_RANGE, RANGE_FUEL, imperial ? Converter.getMiles(qtFuelRange) : qtFuelRange);
            updateChannel(CHANNEL_GROUP_RANGE, RANGE_RADIUS_FUEL,
                    imperial ? Converter.getMiles(qtFuelRadius) : qtFuelRadius);
        }
        if (isHybrid) {
            String unit = v.properties.combinedRange.distance.units;
            imperial = !Constants.KILOMETERS_JSON.equals(unit);
            int rangeCombined = v.properties.combinedRange.distance.value;
            QuantityType<Length> qtHybridRange = QuantityType.valueOf(rangeCombined, Constants.KILOMETRE_UNIT);
            QuantityType<Length> qtHybridRadius = QuantityType.valueOf(Converter.guessRangeRadius(rangeCombined),
                    Constants.KILOMETRE_UNIT);
            updateChannel(CHANNEL_GROUP_RANGE, RANGE_HYBRID,
                    imperial ? Converter.getMiles(qtHybridRange) : qtHybridRange);
            updateChannel(CHANNEL_GROUP_RANGE, RANGE_RADIUS_HYBRID,
                    imperial ? Converter.getMiles(qtHybridRadius) : qtHybridRadius);
        }

        updateChannel(CHANNEL_GROUP_RANGE, MILEAGE, QuantityType.valueOf(v.status.currentMileage.mileage,
                imperial ? ImperialUnits.MILE : Constants.KILOMETRE_UNIT));
        if (isElectric) {
            updateChannel(CHANNEL_GROUP_RANGE, SOC,
                    QuantityType.valueOf(v.properties.chargingState.chargePercentage, Units.PERCENT));
        }
        if (hasFuel) {
            updateChannel(CHANNEL_GROUP_RANGE, REMAINING_FUEL,
                    QuantityType.valueOf(v.properties.fuelLevel.value, Units.LITRE));
        }

        // Charge Values
        if (isElectric) {
            updateChannel(CHANNEL_GROUP_STATUS, PLUG_CONNECTION,
                    OnOffType.from(v.properties.chargingState.isChargerConnected));
            updateChannel(CHANNEL_GROUP_STATUS, CHARGE_STATUS,
                    StringType.valueOf(Converter.toTitleCase(v.properties.chargingState.state)));
            updateChannel(CHANNEL_GROUP_STATUS, CHARGE_TYPE,
                    StringType.valueOf(Converter.toTitleCase(v.properties.chargingState.type)));
        }
    }

    protected void updateCheckControls(List<CCMMessage> ccl) {
        if (ccl.isEmpty()) {
            // No Check Control available - show not active
            CCMMessage ccm = new CCMMessage();
            // [todo]
            // ccm.ccmDescriptionLong = Constants.NO_ENTRIES;
            // ccm.ccmDescriptionShort = Constants.NO_ENTRIES;
            // ccm.ccmId = -1;
            // ccm.ccmMileage = -1;
            ccl.add(ccm);
        }

        // add all elements to options
        checkControlList = ccl;
        List<StateOption> ccmDescriptionOptions = new ArrayList<>();
        List<StateOption> ccmDetailsOptions = new ArrayList<>();
        List<StateOption> ccmMileageOptions = new ArrayList<>();
        boolean isSelectedElementIn = false;
        int index = 0;
        for (CCMMessage ccEntry : checkControlList) {
            // [todo]
            // ccmDescriptionOptions.add(new StateOption(Integer.toString(index), ccEntry.ccmDescriptionShort));
            // ccmDetailsOptions.add(new StateOption(Integer.toString(index), ccEntry.ccmDescriptionLong));
            // ccmMileageOptions.add(new StateOption(Integer.toString(index), Integer.toString(ccEntry.ccmMileage)));
            // if (selectedCC.equals(ccEntry.ccmDescriptionShort)) {
            // isSelectedElementIn = true;
            // }
            index++;
        }
        setOptions(CHANNEL_GROUP_CHECK_CONTROL, NAME, ccmDescriptionOptions);
        setOptions(CHANNEL_GROUP_CHECK_CONTROL, DETAILS, ccmDetailsOptions);
        setOptions(CHANNEL_GROUP_CHECK_CONTROL, MILEAGE, ccmMileageOptions);

        // if current selected item isn't anymore in the list select first entry
        if (!isSelectedElementIn) {
            selectCheckControl(0);
        }
    }

    protected void selectCheckControl(int index) {
        if (index >= 0 && index < checkControlList.size()) {
            CCMMessage ccEntry = checkControlList.get(index);
            // [todo]
            // selectedCC = ccEntry.ccmDescriptionShort;
            // updateChannel(CHANNEL_GROUP_CHECK_CONTROL, NAME, StringType.valueOf(ccEntry.ccmDescriptionShort));
            // updateChannel(CHANNEL_GROUP_CHECK_CONTROL, DETAILS, StringType.valueOf(ccEntry.ccmDescriptionLong));
            // updateChannel(CHANNEL_GROUP_CHECK_CONTROL, MILEAGE, QuantityType.valueOf(
            // Converter.round(ccEntry.ccmMileage), imperial ? ImperialUnits.MILE : Constants.KILOMETRE_UNIT));
        }
    }

    protected void updateServices(List<CBSMessage> sl) {
        // if list is empty add "undefined" element
        if (sl.isEmpty()) {
            CBSMessage cbsm = new CBSMessage();
            cbsm.title = Constants.NO_ENTRIES;
            cbsm.longDescription = Constants.NO_ENTRIES;
            sl.add(cbsm);
        }

        // add all elements to options
        serviceList = sl;
        List<StateOption> serviceNameOptions = new ArrayList<>();
        List<StateOption> serviceDetailsOptions = new ArrayList<>();
        List<StateOption> serviceDateOptions = new ArrayList<>();
        boolean isSelectedElementIn = false;
        int index = 0;
        for (CBSMessage serviceEntry : serviceList) {
            // create StateOption with "value = list index" and "label = human readable string"
            serviceNameOptions.add(new StateOption(Integer.toString(index), serviceEntry.title));
            serviceDetailsOptions.add(new StateOption(Integer.toString(index), serviceEntry.longDescription));
            serviceDateOptions.add(new StateOption(Integer.toString(index), serviceEntry.subtitle));
            if (selectedService.equals(serviceEntry.title)) {
                isSelectedElementIn = true;
            }
            index++;
        }
        setOptions(CHANNEL_GROUP_SERVICE, NAME, serviceNameOptions);
        setOptions(CHANNEL_GROUP_SERVICE, DETAILS, serviceDetailsOptions);
        setOptions(CHANNEL_GROUP_SERVICE, DATE, serviceDateOptions);

        // if current selected item isn't anymore in the list select first entry
        if (!isSelectedElementIn) {
            selectService(0);
        }
    }

    protected void selectService(int index) {
        if (index >= 0 && index < serviceList.size()) {
            CBSMessage serviceEntry = serviceList.get(index);
            selectedService = serviceEntry.title;
            updateChannel(CHANNEL_GROUP_SERVICE, NAME, StringType.valueOf(Converter.toTitleCase(serviceEntry.title)));
            updateChannel(CHANNEL_GROUP_SERVICE, DETAILS,
                    StringType.valueOf(Converter.toTitleCase(serviceEntry.longDescription)));
            updateChannel(CHANNEL_GROUP_SERVICE, DATE,
                    DateTimeType.valueOf(Converter.getZonedDateTime(serviceEntry.subtitle)));
        }
    }

    protected void updateChargeProfileFromContent(String content) {
        ChargeProfileWrapper.fromJson(content).ifPresent(this::updateChargeProfile);
    }

    protected void updateChargeProfile(ChargeProfileWrapper wrapper) {
        updateChannel(CHANNEL_GROUP_CHARGE, CHARGE_PROFILE_PREFERENCE,
                StringType.valueOf(Converter.toTitleCase(wrapper.getPreference())));
        updateChannel(CHANNEL_GROUP_CHARGE, CHARGE_PROFILE_MODE,
                StringType.valueOf(Converter.toTitleCase(wrapper.getMode())));
        final Boolean climate = wrapper.isEnabled(ProfileKey.CLIMATE);
        updateChannel(CHANNEL_GROUP_CHARGE, CHARGE_PROFILE_CLIMATE,
                climate == null ? UnDefType.UNDEF : OnOffType.from(climate));
        updateTimedState(wrapper, ProfileKey.WINDOWSTART);
        updateTimedState(wrapper, ProfileKey.WINDOWEND);
        updateTimedState(wrapper, ProfileKey.TIMER1);
        updateTimedState(wrapper, ProfileKey.TIMER2);
        updateTimedState(wrapper, ProfileKey.TIMER3);
        updateTimedState(wrapper, ProfileKey.OVERRIDE);
    }

    protected void updateTimedState(ChargeProfileWrapper profile, ProfileKey key) {
        final TimedChannel timed = ChargeProfileUtils.getTimedChannel(key);
        if (timed != null) {
            final LocalTime time = profile.getTime(key);
            updateChannel(CHANNEL_GROUP_CHARGE, timed.time, time == null ? UnDefType.UNDEF
                    : new DateTimeType(ZonedDateTime.of(Constants.EPOCH_DAY, time, ZoneId.systemDefault())));
            if (timed.timer != null) {
                final Boolean enabled = profile.isEnabled(key);
                updateChannel(CHANNEL_GROUP_CHARGE, timed.timer + CHARGE_ENABLED,
                        enabled == null ? UnDefType.UNDEF : OnOffType.from(enabled));
                if (timed.hasDays) {
                    final Set<DayOfWeek> days = profile.getDays(key);
                    updateChannel(CHANNEL_GROUP_CHARGE, timed.timer + CHARGE_DAYS,
                            days == null ? UnDefType.UNDEF : StringType.valueOf(ChargeProfileUtils.formatDays(days)));
                    EnumSet.allOf(DayOfWeek.class).forEach(day -> {
                        updateChannel(CHANNEL_GROUP_CHARGE, timed.timer + ChargeProfileUtils.getDaysChannel(day),
                                days == null ? UnDefType.UNDEF : OnOffType.from(days.contains(day)));
                    });
                }
            }
        }
    }

    protected void updateDoors(DoorsWindows dw) {
        updateChannel(CHANNEL_GROUP_DOORS, DOOR_DRIVER_FRONT,
                StringType.valueOf(Converter.toTitleCase(dw.doors.driverFront)));
        updateChannel(CHANNEL_GROUP_DOORS, DOOR_DRIVER_REAR,
                StringType.valueOf(Converter.toTitleCase(dw.doors.driverRear)));
        updateChannel(CHANNEL_GROUP_DOORS, DOOR_PASSENGER_FRONT,
                StringType.valueOf(Converter.toTitleCase(dw.doors.passengerFront)));
        updateChannel(CHANNEL_GROUP_DOORS, DOOR_PASSENGER_REAR,
                StringType.valueOf(Converter.toTitleCase(dw.doors.passengerRear)));
        updateChannel(CHANNEL_GROUP_DOORS, TRUNK, StringType.valueOf(Converter.toTitleCase(dw.trunk)));
        updateChannel(CHANNEL_GROUP_DOORS, HOOD, StringType.valueOf(Converter.toTitleCase(dw.hood)));
    }

    protected void updateWindows(DoorsWindows dw) {
        updateChannel(CHANNEL_GROUP_DOORS, WINDOW_DOOR_DRIVER_FRONT,
                StringType.valueOf(Converter.toTitleCase(dw.windows.driverFront)));
        updateChannel(CHANNEL_GROUP_DOORS, WINDOW_DOOR_DRIVER_REAR,
                StringType.valueOf(Converter.toTitleCase(dw.windows.driverRear)));
        updateChannel(CHANNEL_GROUP_DOORS, WINDOW_DOOR_PASSENGER_FRONT,
                StringType.valueOf(Converter.toTitleCase(dw.windows.passengerFront)));
        updateChannel(CHANNEL_GROUP_DOORS, WINDOW_DOOR_PASSENGER_REAR,
                StringType.valueOf(Converter.toTitleCase(dw.windows.passengerRear)));
        // updateChannel(CHANNEL_GROUP_DOORS, WINDOW_REAR,
        // StringType.valueOf(Converter.toTitleCase(windowState.rearWindow)));
        updateChannel(CHANNEL_GROUP_DOORS, SUNROOF, StringType.valueOf(Converter.toTitleCase(dw.moonroof)));
    }

    protected void updatePosition(Location pos) {
        updateChannel(CHANNEL_GROUP_LOCATION, GPS, PointType
                .valueOf(Double.toString(pos.coordinates.latitude) + "," + Double.toString(pos.coordinates.longitude)));
        updateChannel(CHANNEL_GROUP_LOCATION, HEADING, QuantityType.valueOf(pos.heading, Units.DEGREE_ANGLE));
    }
}
