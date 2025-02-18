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
 * Gets or Sets RecommendationType
 */
@JsonAdapter(RecommendationType.Adapter.class)
public enum RecommendationType {
  
  SIMILAR_TO_RECENTLY_PLAYED("SimilarToRecentlyPlayed"),
  
  SIMILAR_TO_LIKED_ITEM("SimilarToLikedItem"),
  
  HAS_DIRECTOR_FROM_RECENTLY_PLAYED("HasDirectorFromRecentlyPlayed"),
  
  HAS_ACTOR_FROM_RECENTLY_PLAYED("HasActorFromRecentlyPlayed"),
  
  HAS_LIKED_DIRECTOR("HasLikedDirector"),
  
  HAS_LIKED_ACTOR("HasLikedActor");

  private String value;

  RecommendationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static RecommendationType fromValue(String value) {
    for (RecommendationType b : RecommendationType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<RecommendationType> {
    @Override
    public void write(final JsonWriter jsonWriter, final RecommendationType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public RecommendationType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return RecommendationType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    RecommendationType.fromValue(value);
  }
}

