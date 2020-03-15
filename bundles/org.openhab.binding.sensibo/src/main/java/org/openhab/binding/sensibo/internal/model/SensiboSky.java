/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.sensibo.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.measure.Unit;
import javax.measure.quantity.Temperature;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.unit.ImperialUnits;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.openhab.binding.sensibo.internal.dto.poddetails.ModeCapability;
import org.openhab.binding.sensibo.internal.dto.poddetails.PodDetails;

/**
 * The {@link SensiboSky} represents a Sensibo Sky unit
 *
 * @author Arne Seime - Initial contribution
 */
@NonNullByDefault
public class SensiboSky extends Pod {
    private final String macAddress;
    private final String firmwareVersion;
    private final String firmwareType;
    private final String serialNumber;
    private final String productModel;
    private final String roomName;
    private final Map<String, ModeCapability> remoteCapabilities;
    private Unit<Temperature> temperatureUnit = SIUnits.CELSIUS;;
    private String originalTemperatureUnit = "C";
    private Optional<AcState> acState = Optional.empty();
    private Double temperature;
    private Double humidity;
    private boolean alive = false;
    private Schedule[] schedules = new Schedule[0];
    private Optional<Timer> timer = Optional.empty();

    public SensiboSky(final PodDetails dto) {
        this.id = dto.id;
        this.macAddress = StringUtils.remove(dto.macAddress, ':');
        this.firmwareVersion = dto.firmwareVersion;
        this.firmwareType = dto.firmwareType;
        this.serialNumber = dto.serialNumber;
        this.originalTemperatureUnit = dto.temperatureUnit;
        if (dto.temperatureUnit != null) {
            switch (originalTemperatureUnit) {
                case "C":
                    this.temperatureUnit = SIUnits.CELSIUS;
                    break;
                case "F":
                    this.temperatureUnit = ImperialUnits.FAHRENHEIT;
                    break;
                default:
                    throw new IllegalArgumentException("Do not understand temperature unit " + temperatureUnit);

            }
        }
        this.productModel = dto.productModel;

        if (dto.acState != null) {
            this.acState = Optional.of(new AcState(dto.acState));
        }

        if (dto.timer != null) {
            this.timer = Optional.of(new Timer(dto.timer));
        }

        this.temperature = dto.lastMeasurement.temperature;
        this.humidity = dto.lastMeasurement.humidity;

        this.alive = dto.isAlive();
        if (dto.getRemoteCapabilities() != null) {
            this.remoteCapabilities = dto.getRemoteCapabilities();
        } else {
            this.remoteCapabilities = new HashMap<>();
        }
        this.roomName = dto.getRoomName();

        if (dto.schedules != null) {
            schedules = Arrays.asList(dto.schedules).stream().map(e -> new Schedule(e)).collect(Collectors.toList())
                    .toArray(new Schedule[0]);
        }
    }

    public String getOriginalTemperatureUnit() {
        return originalTemperatureUnit;
    }

    public String getRoomName() {
        return roomName;
    }

    public Schedule[] getSchedules() {
        return schedules;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getFirmwareType() {
        return firmwareType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Unit<Temperature> getTemperatureUnit() {
        return temperatureUnit;
    }

    public String getProductModel() {
        return productModel;
    }

    public Optional<AcState> getAcState() {
        return acState;
    }

    public String getProductName() {
        switch (productModel) {
            case "skyv2":
                return String.format("Sensibo Sky %s", roomName);
            default:
                return String.format("%s %s", productModel, roomName);
        }
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public boolean isAlive() {
        return alive;
    }

    public Map<String, ModeCapability> getRemoteCapabilities() {
        return remoteCapabilities;
    }

    public Optional<ModeCapability> getCurrentModeCapabilities() {
        if (acState.isPresent() && acState.get().getMode() != null) {
            return Optional.ofNullable(remoteCapabilities.get(acState.get().getMode()));
        } else {
            return Optional.empty();
        }
    }

    public List<Integer> getTargetTemperatures() {
        Optional<ModeCapability> currentModeCapabilities = getCurrentModeCapabilities();
        if (currentModeCapabilities.isPresent()) {
            org.openhab.binding.sensibo.internal.dto.poddetails.Temperature selectedTemperatureRange = currentModeCapabilities
                    .get().temperatures.get(originalTemperatureUnit);
            if (selectedTemperatureRange != null) {
                return selectedTemperatureRange.validValues;
            }
        }
        return Collections.emptyList();
    }

    /**
     * @param newAcState an updated ac state
     */
    public void updateAcState(AcState newAcState) {
        this.acState = Optional.of(newAcState);
    }

    public Optional<Timer> getTimer() {
        return timer;
    }
}
