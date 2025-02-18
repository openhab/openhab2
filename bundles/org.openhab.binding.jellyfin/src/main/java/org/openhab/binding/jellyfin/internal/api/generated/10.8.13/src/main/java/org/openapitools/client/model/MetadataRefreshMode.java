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
 * Gets or Sets MetadataRefreshMode
 */
@JsonAdapter(MetadataRefreshMode.Adapter.class)
public enum MetadataRefreshMode {
  
  NONE("None"),
  
  VALIDATION_ONLY("ValidationOnly"),
  
  DEFAULT("Default"),
  
  FULL_REFRESH("FullRefresh");

  private String value;

  MetadataRefreshMode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static MetadataRefreshMode fromValue(String value) {
    for (MetadataRefreshMode b : MetadataRefreshMode.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<MetadataRefreshMode> {
    @Override
    public void write(final JsonWriter jsonWriter, final MetadataRefreshMode enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public MetadataRefreshMode read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return MetadataRefreshMode.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    MetadataRefreshMode.fromValue(value);
  }
}

