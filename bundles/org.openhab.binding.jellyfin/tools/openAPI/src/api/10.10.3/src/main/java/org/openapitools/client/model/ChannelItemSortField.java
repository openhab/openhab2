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
 * Gets or Sets ChannelItemSortField
 */
@JsonAdapter(ChannelItemSortField.Adapter.class)
public enum ChannelItemSortField {
  
  NAME("Name"),
  
  COMMUNITY_RATING("CommunityRating"),
  
  PREMIERE_DATE("PremiereDate"),
  
  DATE_CREATED("DateCreated"),
  
  RUNTIME("Runtime"),
  
  PLAY_COUNT("PlayCount"),
  
  COMMUNITY_PLAY_COUNT("CommunityPlayCount");

  private String value;

  ChannelItemSortField(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ChannelItemSortField fromValue(String value) {
    for (ChannelItemSortField b : ChannelItemSortField.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<ChannelItemSortField> {
    @Override
    public void write(final JsonWriter jsonWriter, final ChannelItemSortField enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public ChannelItemSortField read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return ChannelItemSortField.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    ChannelItemSortField.fromValue(value);
  }
}

