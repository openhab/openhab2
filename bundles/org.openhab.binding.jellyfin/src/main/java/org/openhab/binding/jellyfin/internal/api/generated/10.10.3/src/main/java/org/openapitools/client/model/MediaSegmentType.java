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
 * Defines the types of content an individual Jellyfin.Data.Entities.MediaSegment represents.
 */
@JsonAdapter(MediaSegmentType.Adapter.class)
public enum MediaSegmentType {
  
  UNKNOWN("Unknown"),
  
  COMMERCIAL("Commercial"),
  
  PREVIEW("Preview"),
  
  RECAP("Recap"),
  
  OUTRO("Outro"),
  
  INTRO("Intro");

  private String value;

  MediaSegmentType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static MediaSegmentType fromValue(String value) {
    for (MediaSegmentType b : MediaSegmentType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<MediaSegmentType> {
    @Override
    public void write(final JsonWriter jsonWriter, final MediaSegmentType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public MediaSegmentType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return MediaSegmentType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    MediaSegmentType.fromValue(value);
  }
}

