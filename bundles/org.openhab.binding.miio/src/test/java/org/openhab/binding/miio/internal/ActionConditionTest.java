package org.openhab.binding.miio.internal;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.openhab.binding.miio.internal.basic.ActionConditions;
import org.openhab.binding.miio.internal.basic.MiIoDeviceActionCondition;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ActionConditionTest {

    @Test
    public void assertBrightnessExisting() {
        MiIoDeviceActionCondition condition = new MiIoDeviceActionCondition();
        condition.setName("BrightnessExisting");
        Map<String, Object> deviceVariables = Collections.emptyMap();
        JsonElement value = new JsonPrimitive(1);
        JsonElement resp = ActionConditions.executeAction(condition, deviceVariables, value, null);
        // dimmed to 1
        assertNotNull(resp);
        assertEquals(new JsonPrimitive(1), resp);

        // fully on
        value = new JsonPrimitive(100);
        resp = ActionConditions.executeAction(condition, deviceVariables, value, null);
        assertNotNull(resp);
        assertEquals(new JsonPrimitive(100), resp);

        // >100
        value = new JsonPrimitive(200);
        resp = ActionConditions.executeAction(condition, deviceVariables, value, null);
        assertNotNull(resp);
        assertEquals(new JsonPrimitive(100), resp);

        // switched off = invalid brightness
        value = new JsonPrimitive(0);
        resp = ActionConditions.executeAction(condition, deviceVariables, value, null);
        assertNull(resp);
        assertNotEquals(new JsonPrimitive(0), resp);

        value = new JsonPrimitive(-1);
        resp = ActionConditions.executeAction(condition, deviceVariables, value, null);
        assertNull(resp);
        assertNotEquals(new JsonPrimitive(-1), resp);
    }
}
