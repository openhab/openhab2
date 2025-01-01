/**
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.tado.swagger.codegen.api.model;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Static imported copy of the Java file originally created by Swagger Codegen.
 *
 * @author Andrew Fiddian-Green - Initial contribution
 */
@JsonAdapter(ACFanLevel.Adapter.class)
public enum ACFanLevel {

    SILENT("SILENT"),

    LEVEL1("LEVEL1"),

    LEVEL2("LEVEL2"),

    LEVEL3("LEVEL3"),

    LEVEL4("LEVEL4"),

    LEVEL5("LEVEL5"),

    AUTO("AUTO");

    private String value;

    ACFanLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ACFanLevel fromValue(String text) {
        for (ACFanLevel b : ACFanLevel.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    public static class Adapter extends TypeAdapter<ACFanLevel> {
        @Override
        public void write(final JsonWriter jsonWriter, final ACFanLevel enumeration) throws IOException {
            jsonWriter.value(enumeration.getValue());
        }

        @Override
        public ACFanLevel read(final JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            return ACFanLevel.fromValue(String.valueOf(value));
        }
    }
}
