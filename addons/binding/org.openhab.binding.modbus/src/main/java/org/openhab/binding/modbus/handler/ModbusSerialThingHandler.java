/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.handler;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.openhab.binding.modbus.internal.config.ModbusSerialConfiguration;
import org.openhab.io.transport.modbus.ModbusManager;
import org.openhab.io.transport.modbus.endpoint.EndpointPoolConfiguration;
import org.openhab.io.transport.modbus.endpoint.ModbusSerialSlaveEndpoint;

/**
 * Endpoint thing handler for serial slaves
 *
 * @author Sami Salonen - Initial contribution
 */
@NonNullByDefault
public class ModbusSerialThingHandler
        extends AbstractModbusEndpointThingHandler<ModbusSerialSlaveEndpoint, ModbusSerialConfiguration> {

    public ModbusSerialThingHandler(Bridge bridge, Supplier<ModbusManager> managerRef) {
        super(bridge, managerRef);
    }

    @Override
    protected void configure() {
        ModbusSerialConfiguration config = getConfigAs(ModbusSerialConfiguration.class);
        String port = Objects.requireNonNull(config.getPort(), "port must not be null");
        Integer baud = Objects.requireNonNull(config.getBaud(), "baud must not be null");
        String flowControlIn = Objects.requireNonNull(config.getFlowControlIn(), "flowControlIn must not be null");
        String flowControlOut = Objects.requireNonNull(config.getFlowControlOut(), "flowControlOut must not be null");
        String stopBits = Objects.requireNonNull(config.getStopBits(), "stopBits must not be null");
        String parity = Objects.requireNonNull(config.getParity(), "parity must not be null");
        String encoding = Objects.requireNonNull(config.getEncoding(), "encoding must not be null");
        if (port == null || baud == null || flowControlIn == null || flowControlOut == null || stopBits == null
                || parity == null || encoding == null) {
            // Just to make compiler happy (null checks), NullPointerException has been raised above already
            throw new IllegalArgumentException();
        }

        this.config = config;

        EndpointPoolConfiguration poolConfiguration = new EndpointPoolConfiguration();
        this.poolConfiguration = poolConfiguration;
        poolConfiguration.setConnectMaxTries(config.getConnectMaxTries());
        poolConfiguration.setConnectTimeoutMillis(config.getConnectTimeoutMillis());
        poolConfiguration.setInterTransactionDelayMillis(config.getTimeBetweenTransactionsMillis());

        // Never reconnect serial connections "automatically"
        poolConfiguration.setInterConnectDelayMillis(1000);
        poolConfiguration.setReconnectAfterMillis(-1);

        endpoint = new ModbusSerialSlaveEndpoint(port, baud, flowControlIn, flowControlOut, config.getDataBits(),
                stopBits, parity, encoding, config.isEcho(), config.getReceiveTimeoutMillis());
    }

    @Override
    protected String formatConflictingParameterError(@Nullable EndpointPoolConfiguration otherPoolConfig) {
        return String.format(
                "Endpoint '%s' has conflicting parameters: parameters of this thing (%s '%s') %s are different from some other things parameter: %s. Ensure that all endpoints pointing to serial port '%s' have same parameters.",
                endpoint, thing.getUID(), this.thing.getLabel(), this.poolConfiguration, otherPoolConfig,
                Optional.ofNullable(this.endpoint).map(e -> e.getPortName()).orElse("<null>"));
    }

    @Override
    public int getSlaveId() {
        ModbusSerialConfiguration config = this.config;
        if (config == null) {
            throw new IllegalStateException("Poller not configured, but slave id is queried!");
        }
        return config.getId();
    }

}
