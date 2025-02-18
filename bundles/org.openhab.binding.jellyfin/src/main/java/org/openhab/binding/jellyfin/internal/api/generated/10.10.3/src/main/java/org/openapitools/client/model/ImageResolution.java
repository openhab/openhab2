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
 * Enum ImageResolution.
 */
@JsonAdapter(ImageResolution.Adapter.class)
public enum ImageResolution {
  
  MATCH_SOURCE("MatchSource"),
  
  P144("P144"),
  
  P240("P240"),
  
  P360("P360"),
  
  P480("P480"),
  
  P720("P720"),
  
  P1080("P1080"),
  
  P1440("P1440"),
  
  P2160("P2160");

  private String value;

  ImageResolution(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ImageResolution fromValue(String value) {
    for (ImageResolution b : ImageResolution.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<ImageResolution> {
    @Override
    public void write(final JsonWriter jsonWriter, final ImageResolution enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public ImageResolution read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return ImageResolution.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    ImageResolution.fromValue(value);
  }
}

