package org.openhab.binding.onecta.internal.api.dto.units;

import com.google.gson.annotations.SerializedName;

public class ConsumptionData {
    @SerializedName("ref")
    private String ref;
    @SerializedName("settable")
    private boolean settable;
    @SerializedName("value")
    private ConsumptionDataValue value;

    public String getRef() {
        return ref;
    }

    public boolean isSettable() {
        return settable;
    }

    public ConsumptionDataValue getValue() {
        return value;
    }
}
