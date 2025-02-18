/*
 * Jellyfin API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 10.10.3
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Gets or Sets LogLevel
 */
@JsonAdapter(LogLevel.Adapter.class)
public enum LogLevel {
  
  TRACE("Trace"),
  
  DEBUG("Debug"),
  
  INFORMATION("Information"),
  
  WARNING("Warning"),
  
  ERROR("Error"),
  
  CRITICAL("Critical"),
  
  NONE("None");

  private String value;

  LogLevel(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static LogLevel fromValue(String value) {
    for (LogLevel b : LogLevel.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<LogLevel> {
    @Override
    public void write(final JsonWriter jsonWriter, final LogLevel enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public LogLevel read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return LogLevel.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    LogLevel.fromValue(value);
  }
}

