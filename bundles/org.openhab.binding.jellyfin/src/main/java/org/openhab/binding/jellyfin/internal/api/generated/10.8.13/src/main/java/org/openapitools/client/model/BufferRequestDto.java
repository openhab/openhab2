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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openapitools.client.JSON;

/**
 * Class BufferRequestDto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class BufferRequestDto {
  public static final String SERIALIZED_NAME_WHEN = "When";
  @SerializedName(SERIALIZED_NAME_WHEN)
  @javax.annotation.Nullable
  private OffsetDateTime when;

  public static final String SERIALIZED_NAME_POSITION_TICKS = "PositionTicks";
  @SerializedName(SERIALIZED_NAME_POSITION_TICKS)
  @javax.annotation.Nullable
  private Long positionTicks;

  public static final String SERIALIZED_NAME_IS_PLAYING = "IsPlaying";
  @SerializedName(SERIALIZED_NAME_IS_PLAYING)
  @javax.annotation.Nullable
  private Boolean isPlaying;

  public static final String SERIALIZED_NAME_PLAYLIST_ITEM_ID = "PlaylistItemId";
  @SerializedName(SERIALIZED_NAME_PLAYLIST_ITEM_ID)
  @javax.annotation.Nullable
  private UUID playlistItemId;

  public BufferRequestDto() {
  }

  public BufferRequestDto when(@javax.annotation.Nullable OffsetDateTime when) {
    this.when = when;
    return this;
  }

  /**
   * Gets or sets when the request has been made by the client.
   * @return when
   */
  @javax.annotation.Nullable
  public OffsetDateTime getWhen() {
    return when;
  }

  public void setWhen(@javax.annotation.Nullable OffsetDateTime when) {
    this.when = when;
  }


  public BufferRequestDto positionTicks(@javax.annotation.Nullable Long positionTicks) {
    this.positionTicks = positionTicks;
    return this;
  }

  /**
   * Gets or sets the position ticks.
   * @return positionTicks
   */
  @javax.annotation.Nullable
  public Long getPositionTicks() {
    return positionTicks;
  }

  public void setPositionTicks(@javax.annotation.Nullable Long positionTicks) {
    this.positionTicks = positionTicks;
  }


  public BufferRequestDto isPlaying(@javax.annotation.Nullable Boolean isPlaying) {
    this.isPlaying = isPlaying;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the client playback is unpaused.
   * @return isPlaying
   */
  @javax.annotation.Nullable
  public Boolean getIsPlaying() {
    return isPlaying;
  }

  public void setIsPlaying(@javax.annotation.Nullable Boolean isPlaying) {
    this.isPlaying = isPlaying;
  }


  public BufferRequestDto playlistItemId(@javax.annotation.Nullable UUID playlistItemId) {
    this.playlistItemId = playlistItemId;
    return this;
  }

  /**
   * Gets or sets the playlist item identifier of the playing item.
   * @return playlistItemId
   */
  @javax.annotation.Nullable
  public UUID getPlaylistItemId() {
    return playlistItemId;
  }

  public void setPlaylistItemId(@javax.annotation.Nullable UUID playlistItemId) {
    this.playlistItemId = playlistItemId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BufferRequestDto bufferRequestDto = (BufferRequestDto) o;
    return Objects.equals(this.when, bufferRequestDto.when) &&
        Objects.equals(this.positionTicks, bufferRequestDto.positionTicks) &&
        Objects.equals(this.isPlaying, bufferRequestDto.isPlaying) &&
        Objects.equals(this.playlistItemId, bufferRequestDto.playlistItemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(when, positionTicks, isPlaying, playlistItemId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BufferRequestDto {\n");
    sb.append("    when: ").append(toIndentedString(when)).append("\n");
    sb.append("    positionTicks: ").append(toIndentedString(positionTicks)).append("\n");
    sb.append("    isPlaying: ").append(toIndentedString(isPlaying)).append("\n");
    sb.append("    playlistItemId: ").append(toIndentedString(playlistItemId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("When");
    openapiFields.add("PositionTicks");
    openapiFields.add("IsPlaying");
    openapiFields.add("PlaylistItemId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to BufferRequestDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!BufferRequestDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in BufferRequestDto is not found in the empty JSON string", BufferRequestDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!BufferRequestDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `BufferRequestDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("PlaylistItemId") != null && !jsonObj.get("PlaylistItemId").isJsonNull()) && !jsonObj.get("PlaylistItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PlaylistItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PlaylistItemId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!BufferRequestDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'BufferRequestDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<BufferRequestDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(BufferRequestDto.class));

       return (TypeAdapter<T>) new TypeAdapter<BufferRequestDto>() {
           @Override
           public void write(JsonWriter out, BufferRequestDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public BufferRequestDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of BufferRequestDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of BufferRequestDto
   * @throws IOException if the JSON string is invalid with respect to BufferRequestDto
   */
  public static BufferRequestDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, BufferRequestDto.class);
  }

  /**
   * Convert an instance of BufferRequestDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

