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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * Class RemoveFromPlaylistRequestDto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class RemoveFromPlaylistRequestDto {
  public static final String SERIALIZED_NAME_PLAYLIST_ITEM_IDS = "PlaylistItemIds";
  @SerializedName(SERIALIZED_NAME_PLAYLIST_ITEM_IDS)
  @javax.annotation.Nullable
  private List<UUID> playlistItemIds = new ArrayList<>();

  public static final String SERIALIZED_NAME_CLEAR_PLAYLIST = "ClearPlaylist";
  @SerializedName(SERIALIZED_NAME_CLEAR_PLAYLIST)
  @javax.annotation.Nullable
  private Boolean clearPlaylist;

  public static final String SERIALIZED_NAME_CLEAR_PLAYING_ITEM = "ClearPlayingItem";
  @SerializedName(SERIALIZED_NAME_CLEAR_PLAYING_ITEM)
  @javax.annotation.Nullable
  private Boolean clearPlayingItem;

  public RemoveFromPlaylistRequestDto() {
  }

  public RemoveFromPlaylistRequestDto playlistItemIds(@javax.annotation.Nullable List<UUID> playlistItemIds) {
    this.playlistItemIds = playlistItemIds;
    return this;
  }

  public RemoveFromPlaylistRequestDto addPlaylistItemIdsItem(UUID playlistItemIdsItem) {
    if (this.playlistItemIds == null) {
      this.playlistItemIds = new ArrayList<>();
    }
    this.playlistItemIds.add(playlistItemIdsItem);
    return this;
  }

  /**
   * Gets or sets the playlist identifiers ot the items. Ignored when clearing the playlist.
   * @return playlistItemIds
   */
  @javax.annotation.Nullable
  public List<UUID> getPlaylistItemIds() {
    return playlistItemIds;
  }

  public void setPlaylistItemIds(@javax.annotation.Nullable List<UUID> playlistItemIds) {
    this.playlistItemIds = playlistItemIds;
  }


  public RemoveFromPlaylistRequestDto clearPlaylist(@javax.annotation.Nullable Boolean clearPlaylist) {
    this.clearPlaylist = clearPlaylist;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the entire playlist should be cleared.
   * @return clearPlaylist
   */
  @javax.annotation.Nullable
  public Boolean getClearPlaylist() {
    return clearPlaylist;
  }

  public void setClearPlaylist(@javax.annotation.Nullable Boolean clearPlaylist) {
    this.clearPlaylist = clearPlaylist;
  }


  public RemoveFromPlaylistRequestDto clearPlayingItem(@javax.annotation.Nullable Boolean clearPlayingItem) {
    this.clearPlayingItem = clearPlayingItem;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the playing item should be removed as well. Used only when clearing the playlist.
   * @return clearPlayingItem
   */
  @javax.annotation.Nullable
  public Boolean getClearPlayingItem() {
    return clearPlayingItem;
  }

  public void setClearPlayingItem(@javax.annotation.Nullable Boolean clearPlayingItem) {
    this.clearPlayingItem = clearPlayingItem;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoveFromPlaylistRequestDto removeFromPlaylistRequestDto = (RemoveFromPlaylistRequestDto) o;
    return Objects.equals(this.playlistItemIds, removeFromPlaylistRequestDto.playlistItemIds) &&
        Objects.equals(this.clearPlaylist, removeFromPlaylistRequestDto.clearPlaylist) &&
        Objects.equals(this.clearPlayingItem, removeFromPlaylistRequestDto.clearPlayingItem);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playlistItemIds, clearPlaylist, clearPlayingItem);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoveFromPlaylistRequestDto {\n");
    sb.append("    playlistItemIds: ").append(toIndentedString(playlistItemIds)).append("\n");
    sb.append("    clearPlaylist: ").append(toIndentedString(clearPlaylist)).append("\n");
    sb.append("    clearPlayingItem: ").append(toIndentedString(clearPlayingItem)).append("\n");
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
    openapiFields.add("PlaylistItemIds");
    openapiFields.add("ClearPlaylist");
    openapiFields.add("ClearPlayingItem");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to RemoveFromPlaylistRequestDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!RemoveFromPlaylistRequestDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in RemoveFromPlaylistRequestDto is not found in the empty JSON string", RemoveFromPlaylistRequestDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!RemoveFromPlaylistRequestDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `RemoveFromPlaylistRequestDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // ensure the optional json data is an array if present
      if (jsonObj.get("PlaylistItemIds") != null && !jsonObj.get("PlaylistItemIds").isJsonNull() && !jsonObj.get("PlaylistItemIds").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `PlaylistItemIds` to be an array in the JSON string but got `%s`", jsonObj.get("PlaylistItemIds").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!RemoveFromPlaylistRequestDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'RemoveFromPlaylistRequestDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<RemoveFromPlaylistRequestDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(RemoveFromPlaylistRequestDto.class));

       return (TypeAdapter<T>) new TypeAdapter<RemoveFromPlaylistRequestDto>() {
           @Override
           public void write(JsonWriter out, RemoveFromPlaylistRequestDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public RemoveFromPlaylistRequestDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of RemoveFromPlaylistRequestDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of RemoveFromPlaylistRequestDto
   * @throws IOException if the JSON string is invalid with respect to RemoveFromPlaylistRequestDto
   */
  public static RemoveFromPlaylistRequestDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, RemoveFromPlaylistRequestDto.class);
  }

  /**
   * Convert an instance of RemoveFromPlaylistRequestDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

