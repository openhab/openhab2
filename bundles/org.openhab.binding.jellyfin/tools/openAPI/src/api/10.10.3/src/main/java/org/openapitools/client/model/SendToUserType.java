/*
 * Jellyfin API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 10.8.13
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
 * Gets or Sets SendToUserType
 */
@JsonAdapter(SendToUserType.Adapter.class)
public enum SendToUserType {
  
  ALL("All"),
  
  ADMINS("Admins"),
  
  CUSTOM("Custom");

  private String value;

  SendToUserType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static SendToUserType fromValue(String value) {
    for (SendToUserType b : SendToUserType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<SendToUserType> {
    @Override
    public void write(final JsonWriter jsonWriter, final SendToUserType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public SendToUserType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return SendToUserType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    SendToUserType.fromValue(value);
  }
}

