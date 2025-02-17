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
package org.openhab.binding.lgthinq.lgservices.model;

import static org.openhab.binding.lgthinq.lgservices.model.FeatureDefinition.NULL_DEFINITION;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The {@link AbstractCapability}
 *
 * @author Nemer Daud - Initial contribution
 */
@NonNullByDefault
@SuppressWarnings("unchecked")
public abstract class AbstractCapability<C extends CapabilityDefinition> implements CapabilityDefinition {
    // Define if the device supports sending setup commands before monitoring
    // This is to control result 400 for some devices that doesn't support or permit setup commands before monitoring
    boolean isBeforeCommandSupporter = true;

    public boolean isBeforeCommandSupported() {
        return isBeforeCommandSupporter;
    }

    public void setBeforeCommandSupported(boolean beforeCommandSupporter) {
        isBeforeCommandSupporter = beforeCommandSupporter;
    }

    // default result format
    protected Map<String, Function<C, FeatureDefinition>> featureDefinitionMap = new HashMap<>();

    protected String modelName = "";
    final Class<C> realClass;

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    protected AbstractCapability() {
        this.realClass = (Class<C>) ((ParameterizedType) Objects.requireNonNull(getClass().getGenericSuperclass()))
                .getActualTypeArguments()[0];
    }

    protected DeviceTypes deviceType = DeviceTypes.UNKNOWN;
    protected LGAPIVerion version = LGAPIVerion.UNDEF;
    private MonitoringResultFormat monitoringDataFormat = MonitoringResultFormat.UNKNOWN_FORMAT;

    private List<MonitoringBinaryProtocol> monitoringBinaryProtocol = new ArrayList<>();

    @Override
    public MonitoringResultFormat getMonitoringDataFormat() {
        return monitoringDataFormat;
    }

    @Override
    public void setMonitoringDataFormat(MonitoringResultFormat monitoringDataFormat) {
        this.monitoringDataFormat = monitoringDataFormat;
    }

    public void setFeatureDefinitionMap(Map<String, Function<C, FeatureDefinition>> featureDefinitionMap) {
        this.featureDefinitionMap = featureDefinitionMap;
    }

    @Override
    public List<MonitoringBinaryProtocol> getMonitoringBinaryProtocol() {
        return monitoringBinaryProtocol;
    }

    @Override
    public void setMonitoringBinaryProtocol(List<MonitoringBinaryProtocol> monitoringBinaryProtocol) {
        this.monitoringBinaryProtocol = monitoringBinaryProtocol;
    }

    @Override
    public void setDeviceType(DeviceTypes deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public void setDeviceVersion(LGAPIVerion version) {
        this.version = version;
    }

    @Override
    public DeviceTypes getDeviceType() {
        return deviceType;
    }

    @Override
    public LGAPIVerion getDeviceVersion() {
        return version;
    }

    private Map<String, Object> rawData = new HashMap<>();

    @JsonIgnore
    public Map<String, Object> getRawData() {
        return rawData;
    }

    public Map<String, Map<String, Object>> getFeatureValuesRawData() {
        switch (getDeviceVersion()) {
            case V1_0:
                return Objects.requireNonNullElse((Map<String, Map<String, Object>>) getRawData().get("Value"),
                        Collections.emptyMap());
            case V2_0:
                return Objects.requireNonNullElse(
                        (Map<String, Map<String, Object>>) getRawData().get("MonitoringValue"), Collections.emptyMap());
            default:
                throw new IllegalStateException("Invalid version 'UNDEF' to get capability feature monitoring values");
        }
    }

    public void setRawData(Map<String, Object> rawData) {
        this.rawData = rawData;
    }

    @Override
    public FeatureDefinition getFeatureDefinition(String featureName) {
        Function<C, FeatureDefinition> f = featureDefinitionMap.get(featureName);
        return f != null ? f.apply(realClass.cast(this)) : NULL_DEFINITION;
    }
}
