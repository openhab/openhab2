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
package org.openhab.binding.fronius.internal.handler;

import java.util.Optional;

import javax.measure.Unit;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.fronius.internal.FroniusBaseDeviceConfiguration;
import org.openhab.binding.fronius.internal.FroniusBindingConstants;
import org.openhab.binding.fronius.internal.FroniusBridgeConfiguration;
import org.openhab.binding.fronius.internal.FroniusCommunicationException;
import org.openhab.binding.fronius.internal.api.dto.ValueUnit;
import org.openhab.binding.fronius.internal.api.dto.inverter.InverterDeviceStatusDTO;
import org.openhab.binding.fronius.internal.api.dto.inverter.InverterRealtimeBodyDTO;
import org.openhab.binding.fronius.internal.api.dto.inverter.InverterRealtimeBodyDataDTO;
import org.openhab.binding.fronius.internal.api.dto.inverter.InverterRealtimeResponseDTO;
import org.openhab.binding.fronius.internal.api.dto.powerflow.PowerFlowRealtimeBodyDTO;
import org.openhab.binding.fronius.internal.api.dto.powerflow.PowerFlowRealtimeBodyDataDTO;
import org.openhab.binding.fronius.internal.api.dto.powerflow.PowerFlowRealtimeInverterDTO;
import org.openhab.binding.fronius.internal.api.dto.powerflow.PowerFlowRealtimeResponseDTO;
import org.openhab.binding.fronius.internal.api.dto.powerflow.PowerFlowRealtimeSiteDTO;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.State;

/**
 * The {@link FroniusSymoInverterHandler} is responsible for updating the data, which are
 * sent to one of the channels.
 *
 * @author Thomas Rokohl - Initial contribution
 * @author Peter Schraffl - Added device status and error status channels
 * @author Thomas Kordelle - Added inverter power, battery state of charge and PV solar yield
 * @author Jimmy Tanagra - Add powerflow autonomy, self consumption channels
 */
public class FroniusSymoInverterHandler extends FroniusBaseThingHandler {

    private @Nullable InverterRealtimeResponseDTO inverterRealtimeResponseDTO;
    private @Nullable PowerFlowRealtimeResponseDTO powerFlowResponse;
    private FroniusBaseDeviceConfiguration config;

    public FroniusSymoInverterHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected String getDescription() {
        return "Fronius Symo Inverter";
    }

    @Override
    protected void handleRefresh(FroniusBridgeConfiguration bridgeConfiguration) throws FroniusCommunicationException {
        updateData(bridgeConfiguration, config);
        updateChannels();
    }

    @Override
    public void initialize() {
        config = getConfigAs(FroniusBaseDeviceConfiguration.class);
        super.initialize();
    }

    /**
     * Update the channel from the last data retrieved
     *
     * @param channelId the id identifying the channel to be updated
     * @return the last retrieved data
     */
    @Override
    protected State getValue(String channelId) {
        final String[] fields = channelId.split("#");
        if (fields.length < 1) {
            return null;
        }
        final String fieldName = fields[0];

        InverterRealtimeBodyDataDTO inverterData = getInverterData();
        if (inverterData == null) {
            return null;
        }

        switch (fieldName) {
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_PAC:
                return getQuantityOrZero(inverterData.getPac(), Units.WATT);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_FAC:
                return getQuantityOrZero(inverterData.getFac(), Units.HERTZ);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_IAC:
                return getQuantityOrZero(inverterData.getIac(), Units.AMPERE);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_IDC:
                return getQuantityOrZero(inverterData.getIdc(), Units.AMPERE);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_IDC2:
                return getQuantityOrZero(inverterData.getIdc2(), Units.AMPERE);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_IDC3:
                return getQuantityOrZero(inverterData.getIdc3(), Units.AMPERE);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_UAC:
                return getQuantityOrZero(inverterData.getUac(), Units.VOLT);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_UDC:
                return getQuantityOrZero(inverterData.getUdc(), Units.VOLT);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_UDC2:
                return getQuantityOrZero(inverterData.getUdc2(), Units.VOLT);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_UDC3:
                return getQuantityOrZero(inverterData.getUdc3(), Units.VOLT);
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_PDC:
                return calculatePower(inverterData.getUdc(), inverterData.getIdc());
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_PDC2:
                return calculatePower(inverterData.getUdc2(), inverterData.getIdc2());
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_PDC3:
                return calculatePower(inverterData.getUdc3(), inverterData.getIdc3());
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_DAY_ENERGY:
                // Convert the unit to kWh for backwards compatibility with non-quantity type
                return getQuantityOrZero(inverterData.getDayEnergy(), Units.KILOWATT_HOUR).toUnit("kWh");
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_TOTAL:
                // Convert the unit to MWh for backwards compatibility with non-quantity type
                return getQuantityOrZero(inverterData.getTotalEnergy(), Units.MEGAWATT_HOUR).toUnit("MWh");
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_YEAR:
                // Convert the unit to MWh for backwards compatibility with non-quantity type
                return getQuantityOrZero(inverterData.getYearEnergy(), Units.MEGAWATT_HOUR).toUnit("MWh");
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_DEVICE_STATUS_ERROR_CODE:
                InverterDeviceStatusDTO deviceStatus = inverterData.getDeviceStatus();
                if (deviceStatus == null) {
                    return null;
                }
                return new DecimalType(deviceStatus.getErrorCode());
            case FroniusBindingConstants.INVERTER_DATA_CHANNEL_DEVICE_STATUS_STATUS_CODE:
                deviceStatus = inverterData.getDeviceStatus();
                if (deviceStatus == null) {
                    return null;
                }
                return new DecimalType(deviceStatus.getStatusCode());
            default:
                break;
        }

        PowerFlowRealtimeBodyDataDTO powerFlowData = getPowerFlowRealtimeData();
        if (powerFlowData == null) {
            return null;
        }
        PowerFlowRealtimeSiteDTO site = powerFlowData.getSite();
        if (site == null) {
            return null;
        }

        return switch (fieldName) {
            case FroniusBindingConstants.POWER_FLOW_P_GRID -> new QuantityType<>(site.getPgrid(), Units.WATT);
            case FroniusBindingConstants.POWER_FLOW_P_LOAD -> new QuantityType<>(site.getPload(), Units.WATT);
            case FroniusBindingConstants.POWER_FLOW_P_AKKU -> new QuantityType<>(site.getPakku(), Units.WATT);
            case FroniusBindingConstants.POWER_FLOW_P_PV -> new QuantityType<>(site.getPpv(), Units.WATT);
            case FroniusBindingConstants.POWER_FLOW_AUTONOMY ->
                new QuantityType<>(site.getRelAutonomy(), Units.PERCENT);
            case FroniusBindingConstants.POWER_FLOW_SELF_CONSUMPTION ->
                new QuantityType<>(site.getRelSelfConsumption(), Units.PERCENT);
            // Kept second channels for backwards compatibility
            case FroniusBindingConstants.POWER_FLOW_INVERTER_POWER,
                    FroniusBindingConstants.POWER_FLOW_INVERTER_1_POWER -> {
                PowerFlowRealtimeInverterDTO inverter = getInverter(config.deviceId);
                if (inverter == null) {
                    yield null;
                }
                yield new QuantityType<>(inverter.getP(), Units.WATT);
            }
            case FroniusBindingConstants.POWER_FLOW_INVERTER_SOC, FroniusBindingConstants.POWER_FLOW_INVERTER_1_SOC -> {
                PowerFlowRealtimeInverterDTO inverter = getInverter(config.deviceId);
                if (inverter == null) {
                    yield null;
                }
                yield new QuantityType<>(inverter.getSoc(), Units.PERCENT);
            }

            default -> null;
        };
    }

    private @Nullable InverterRealtimeBodyDataDTO getInverterData() {
        InverterRealtimeResponseDTO localInverterRealtimeResponseDTO = inverterRealtimeResponseDTO;
        if (localInverterRealtimeResponseDTO == null) {
            return null;
        }
        InverterRealtimeBodyDTO inverterBody = localInverterRealtimeResponseDTO.getBody();
        if (inverterBody == null) {
            return null;
        }
        return inverterBody.getData();
    }

    private @Nullable PowerFlowRealtimeBodyDataDTO getPowerFlowRealtimeData() {
        PowerFlowRealtimeResponseDTO localPowerFlowResponse = powerFlowResponse;
        if (localPowerFlowResponse == null) {
            return null;
        }
        PowerFlowRealtimeBodyDTO powerFlowBody = localPowerFlowResponse.getBody();
        if (powerFlowBody == null) {
            return null;
        }
        return powerFlowBody.getData();
    }

    /**
     * get flow data for a specific inverter.
     *
     * @param number The inverter object of the given index
     * @return a PowerFlowRealtimeInverterDTO object.
     */
    private @Nullable PowerFlowRealtimeInverterDTO getInverter(final int number) {
        PowerFlowRealtimeBodyDataDTO powerFlowData = getPowerFlowRealtimeData();
        if (powerFlowData == null) {
            return null;
        }
        return powerFlowData.getInverters().get(Integer.toString(number));
    }

    /**
     * Return the value as QuantityType with the unit extracted from ValueUnit
     * or a zero QuantityType with the given unit argument when value is null
     * 
     * @param value The ValueUnit data
     * @param unit The default unit to use when value is null
     * @return a QuantityType from the given value
     */
    private QuantityType<?> getQuantityOrZero(ValueUnit value, Unit unit) {
        return Optional.ofNullable(value).map(val -> val.asQuantityType().toUnit(unit))
                .orElse(new QuantityType<>(0, unit));
    }

    /**
     * Get new data
     */
    private void updateData(FroniusBridgeConfiguration bridgeConfiguration, FroniusBaseDeviceConfiguration config)
            throws FroniusCommunicationException {
        inverterRealtimeResponseDTO = getRealtimeData(bridgeConfiguration.hostname, config.deviceId);
        powerFlowResponse = getPowerFlowRealtime(bridgeConfiguration.hostname);
    }

    /**
     * Make the PowerFlowRealtimeDataRequest
     *
     * @param ip address of the device
     * @return {PowerFlowRealtimeResponseDTO} the object representation of the json response
     */
    private PowerFlowRealtimeResponseDTO getPowerFlowRealtime(String ip) throws FroniusCommunicationException {
        String location = FroniusBindingConstants.getPowerFlowDataUrl(ip);
        return collectDataFromUrl(PowerFlowRealtimeResponseDTO.class, location);
    }

    /**
     * Make the InverterRealtimeDataRequest
     *
     * @param ip address of the device
     * @param deviceId of the device
     * @return {InverterRealtimeResponseDTO} the object representation of the json response
     */
    private InverterRealtimeResponseDTO getRealtimeData(String ip, int deviceId) throws FroniusCommunicationException {
        String location = FroniusBindingConstants.getInverterDataUrl(ip, deviceId);
        return collectDataFromUrl(InverterRealtimeResponseDTO.class, location);
    }

    /**
     * Calculate the power value from the given voltage and current channels
     * 
     * @param voltage the voltage ValueUnit
     * @param current the current ValueUnit
     * @return {QuantityType<>} the power value calculated by multiplying voltage and current
     */
    private QuantityType<?> calculatePower(ValueUnit voltage, ValueUnit current) {
        QuantityType<?> qtyVoltage = getQuantityOrZero(voltage, Units.VOLT);
        QuantityType<?> qtyCurrent = getQuantityOrZero(current, Units.AMPERE);
        return qtyVoltage.multiply(qtyCurrent).toUnit(Units.WATT);
    }
}
