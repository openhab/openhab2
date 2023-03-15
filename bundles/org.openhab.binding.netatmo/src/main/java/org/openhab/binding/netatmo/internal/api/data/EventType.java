/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.netatmo.internal.api.data;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * This enum describes events generated by webhooks and the type of
 * module they are related to according to API documentation
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public enum EventType {
    UNKNOWN(),

    @SerializedName("person") // When the Indoor Camera detects a face
    PERSON(ModuleType.PERSON, ModuleType.WELCOME),

    @SerializedName("person_away") // When geofencing indicates that the person has left the home
    PERSON_AWAY(ModuleType.PERSON, ModuleType.HOME),

    @SerializedName("person_home") // When the person is declared at home
    PERSON_HOME(ModuleType.PERSON, ModuleType.HOME),

    @SerializedName("outdoor") // When the Outdoor Camera detects a human, a car or an animal
    OUTDOOR(ModuleType.PRESENCE, ModuleType.DOORBELL),

    @SerializedName("daily_summary") // When the Outdoor Camera video summary of the last 24 hours is available
    DAILY_SUMMARY(ModuleType.PRESENCE),

    @SerializedName("movement") // When the Indoor Camera detects motion
    MOVEMENT(ModuleType.WELCOME),

    @SerializedName("human") // When the camera detects human motion
    HUMAN(ModuleType.WELCOME, ModuleType.OUTDOOR, ModuleType.DOORBELL),

    @SerializedName("animal") // When the camera detects animal motion
    ANIMAL(ModuleType.WELCOME, ModuleType.OUTDOOR),

    @SerializedName("vehicle") // When the Outdoor Camera detects a car
    VEHICLE(ModuleType.OUTDOOR),

    @SerializedName("new_module") // A new Module has been paired with the Indoor Camera
    NEW_MODULE(ModuleType.WELCOME),

    @SerializedName("module_connect") // Module is connected with the Indoor Camera
    MODULE_CONNECT(ModuleType.WELCOME),

    @SerializedName("module_disconnect") // Module lost its connection with the Indoor Camera
    MODULE_DISCONNECT(ModuleType.WELCOME),

    @SerializedName("module_low_battery") // Module's battery is low
    MODULE_LOW_BATTERY(ModuleType.WELCOME),

    @SerializedName("module_end_update") // Module's firmware update is over
    MODULE_END_UPDATE(ModuleType.WELCOME),

    @SerializedName("connection") // When the camera connects to Netatmo servers
    CONNECTION(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("disconnection") // When the camera loses connection with Netatmo servers
    DISCONNECTION(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("on") // When Camera Monitoring is resumed
    ON(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("off") // When Camera Monitoring is turned off
    OFF(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("boot") // When the Camera is booting
    BOOT(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("sd") // When Camera SD Card status changes
    SD(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("alim") // When Camera power supply status changes
    ALIM(ModuleType.WELCOME, ModuleType.PRESENCE),

    @SerializedName("accepted_call") // When a call is incoming
    ACCEPTED_CALL(ModuleType.DOORBELL),

    @SerializedName("incoming_call") // When a call as been answered by a user
    INCOMING_CALL(ModuleType.DOORBELL),

    @SerializedName("rtc") // Button pressed
    RTC(ModuleType.DOORBELL),

    @SerializedName("missed_call") // When a call has not been answered by anyone
    MISSED_CALL(ModuleType.DOORBELL),

    @SerializedName("hush") // When the smoke detection is activated or deactivated
    HUSH(ModuleType.SMOKE_DETECTOR),

    @SerializedName("smoke") // When smoke is detected or smoke is cleared
    SMOKE(ModuleType.SMOKE_DETECTOR),

    @SerializedName("tampered") // When smoke detector is ready or tampered
    TAMPERED(ModuleType.SMOKE_DETECTOR, ModuleType.CO_DETECTOR),

    @SerializedName("wifi_status") // When wifi status is updated
    WIFI_STATUS(ModuleType.SMOKE_DETECTOR, ModuleType.CO_DETECTOR),

    @SerializedName("battery_status") // When battery status is too low
    BATTERY_STATUS(ModuleType.SMOKE_DETECTOR, ModuleType.CO_DETECTOR),

    @SerializedName("detection_chamber_status") // When the detection chamber is dusty or clean
    DETECTION_CHAMBER_STATUS(ModuleType.SMOKE_DETECTOR),

    @SerializedName("sound_test") // Sound test result
    SOUND_TEST(ModuleType.SMOKE_DETECTOR, ModuleType.CO_DETECTOR),

    @SerializedName("new_device")
    NEW_DEVICE(ModuleType.HOME),

    @SerializedName("co_detected")
    CO_DETECTED(ModuleType.CO_DETECTOR);

    public static final EnumSet<EventType> AS_SET = EnumSet.allOf(EventType.class);

    private final Set<ModuleType> appliesTo;

    EventType(ModuleType... appliesTo) {
        this.appliesTo = Set.of(appliesTo);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public boolean validFor(ModuleType searched) {
        return appliesTo.contains(searched);
    }
}
