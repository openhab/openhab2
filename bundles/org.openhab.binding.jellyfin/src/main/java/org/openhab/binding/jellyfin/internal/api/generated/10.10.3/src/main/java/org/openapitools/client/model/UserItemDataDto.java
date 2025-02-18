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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;

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
 * Class UserItemDataDto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class UserItemDataDto {
  public static final String SERIALIZED_NAME_RATING = "Rating";
  @SerializedName(SERIALIZED_NAME_RATING)
  @javax.annotation.Nullable
  private Double rating;

  public static final String SERIALIZED_NAME_PLAYED_PERCENTAGE = "PlayedPercentage";
  @SerializedName(SERIALIZED_NAME_PLAYED_PERCENTAGE)
  @javax.annotation.Nullable
  private Double playedPercentage;

  public static final String SERIALIZED_NAME_UNPLAYED_ITEM_COUNT = "UnplayedItemCount";
  @SerializedName(SERIALIZED_NAME_UNPLAYED_ITEM_COUNT)
  @javax.annotation.Nullable
  private Integer unplayedItemCount;

  public static final String SERIALIZED_NAME_PLAYBACK_POSITION_TICKS = "PlaybackPositionTicks";
  @SerializedName(SERIALIZED_NAME_PLAYBACK_POSITION_TICKS)
  @javax.annotation.Nullable
  private Long playbackPositionTicks;

  public static final String SERIALIZED_NAME_PLAY_COUNT = "PlayCount";
  @SerializedName(SERIALIZED_NAME_PLAY_COUNT)
  @javax.annotation.Nullable
  private Integer playCount;

  public static final String SERIALIZED_NAME_IS_FAVORITE = "IsFavorite";
  @SerializedName(SERIALIZED_NAME_IS_FAVORITE)
  @javax.annotation.Nullable
  private Boolean isFavorite;

  public static final String SERIALIZED_NAME_LIKES = "Likes";
  @SerializedName(SERIALIZED_NAME_LIKES)
  @javax.annotation.Nullable
  private Boolean likes;

  public static final String SERIALIZED_NAME_LAST_PLAYED_DATE = "LastPlayedDate";
  @SerializedName(SERIALIZED_NAME_LAST_PLAYED_DATE)
  @javax.annotation.Nullable
  private OffsetDateTime lastPlayedDate;

  public static final String SERIALIZED_NAME_PLAYED = "Played";
  @SerializedName(SERIALIZED_NAME_PLAYED)
  @javax.annotation.Nullable
  private Boolean played;

  public static final String SERIALIZED_NAME_KEY = "Key";
  @SerializedName(SERIALIZED_NAME_KEY)
  @javax.annotation.Nullable
  private String key;

  public static final String SERIALIZED_NAME_ITEM_ID = "ItemId";
  @SerializedName(SERIALIZED_NAME_ITEM_ID)
  @javax.annotation.Nullable
  private UUID itemId;

  public UserItemDataDto() {
  }

  public UserItemDataDto rating(@javax.annotation.Nullable Double rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Gets or sets the rating.
   * @return rating
   */
  @javax.annotation.Nullable
  public Double getRating() {
    return rating;
  }

  public void setRating(@javax.annotation.Nullable Double rating) {
    this.rating = rating;
  }


  public UserItemDataDto playedPercentage(@javax.annotation.Nullable Double playedPercentage) {
    this.playedPercentage = playedPercentage;
    return this;
  }

  /**
   * Gets or sets the played percentage.
   * @return playedPercentage
   */
  @javax.annotation.Nullable
  public Double getPlayedPercentage() {
    return playedPercentage;
  }

  public void setPlayedPercentage(@javax.annotation.Nullable Double playedPercentage) {
    this.playedPercentage = playedPercentage;
  }


  public UserItemDataDto unplayedItemCount(@javax.annotation.Nullable Integer unplayedItemCount) {
    this.unplayedItemCount = unplayedItemCount;
    return this;
  }

  /**
   * Gets or sets the unplayed item count.
   * @return unplayedItemCount
   */
  @javax.annotation.Nullable
  public Integer getUnplayedItemCount() {
    return unplayedItemCount;
  }

  public void setUnplayedItemCount(@javax.annotation.Nullable Integer unplayedItemCount) {
    this.unplayedItemCount = unplayedItemCount;
  }


  public UserItemDataDto playbackPositionTicks(@javax.annotation.Nullable Long playbackPositionTicks) {
    this.playbackPositionTicks = playbackPositionTicks;
    return this;
  }

  /**
   * Gets or sets the playback position ticks.
   * @return playbackPositionTicks
   */
  @javax.annotation.Nullable
  public Long getPlaybackPositionTicks() {
    return playbackPositionTicks;
  }

  public void setPlaybackPositionTicks(@javax.annotation.Nullable Long playbackPositionTicks) {
    this.playbackPositionTicks = playbackPositionTicks;
  }


  public UserItemDataDto playCount(@javax.annotation.Nullable Integer playCount) {
    this.playCount = playCount;
    return this;
  }

  /**
   * Gets or sets the play count.
   * @return playCount
   */
  @javax.annotation.Nullable
  public Integer getPlayCount() {
    return playCount;
  }

  public void setPlayCount(@javax.annotation.Nullable Integer playCount) {
    this.playCount = playCount;
  }


  public UserItemDataDto isFavorite(@javax.annotation.Nullable Boolean isFavorite) {
    this.isFavorite = isFavorite;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance is favorite.
   * @return isFavorite
   */
  @javax.annotation.Nullable
  public Boolean getIsFavorite() {
    return isFavorite;
  }

  public void setIsFavorite(@javax.annotation.Nullable Boolean isFavorite) {
    this.isFavorite = isFavorite;
  }


  public UserItemDataDto likes(@javax.annotation.Nullable Boolean likes) {
    this.likes = likes;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this MediaBrowser.Model.Dto.UserItemDataDto is likes.
   * @return likes
   */
  @javax.annotation.Nullable
  public Boolean getLikes() {
    return likes;
  }

  public void setLikes(@javax.annotation.Nullable Boolean likes) {
    this.likes = likes;
  }


  public UserItemDataDto lastPlayedDate(@javax.annotation.Nullable OffsetDateTime lastPlayedDate) {
    this.lastPlayedDate = lastPlayedDate;
    return this;
  }

  /**
   * Gets or sets the last played date.
   * @return lastPlayedDate
   */
  @javax.annotation.Nullable
  public OffsetDateTime getLastPlayedDate() {
    return lastPlayedDate;
  }

  public void setLastPlayedDate(@javax.annotation.Nullable OffsetDateTime lastPlayedDate) {
    this.lastPlayedDate = lastPlayedDate;
  }


  public UserItemDataDto played(@javax.annotation.Nullable Boolean played) {
    this.played = played;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this MediaBrowser.Model.Dto.UserItemDataDto is played.
   * @return played
   */
  @javax.annotation.Nullable
  public Boolean getPlayed() {
    return played;
  }

  public void setPlayed(@javax.annotation.Nullable Boolean played) {
    this.played = played;
  }


  public UserItemDataDto key(@javax.annotation.Nullable String key) {
    this.key = key;
    return this;
  }

  /**
   * Gets or sets the key.
   * @return key
   */
  @javax.annotation.Nullable
  public String getKey() {
    return key;
  }

  public void setKey(@javax.annotation.Nullable String key) {
    this.key = key;
  }


  public UserItemDataDto itemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Gets or sets the item identifier.
   * @return itemId
   */
  @javax.annotation.Nullable
  public UUID getItemId() {
    return itemId;
  }

  public void setItemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserItemDataDto userItemDataDto = (UserItemDataDto) o;
    return Objects.equals(this.rating, userItemDataDto.rating) &&
        Objects.equals(this.playedPercentage, userItemDataDto.playedPercentage) &&
        Objects.equals(this.unplayedItemCount, userItemDataDto.unplayedItemCount) &&
        Objects.equals(this.playbackPositionTicks, userItemDataDto.playbackPositionTicks) &&
        Objects.equals(this.playCount, userItemDataDto.playCount) &&
        Objects.equals(this.isFavorite, userItemDataDto.isFavorite) &&
        Objects.equals(this.likes, userItemDataDto.likes) &&
        Objects.equals(this.lastPlayedDate, userItemDataDto.lastPlayedDate) &&
        Objects.equals(this.played, userItemDataDto.played) &&
        Objects.equals(this.key, userItemDataDto.key) &&
        Objects.equals(this.itemId, userItemDataDto.itemId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(rating, playedPercentage, unplayedItemCount, playbackPositionTicks, playCount, isFavorite, likes, lastPlayedDate, played, key, itemId);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserItemDataDto {\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    playedPercentage: ").append(toIndentedString(playedPercentage)).append("\n");
    sb.append("    unplayedItemCount: ").append(toIndentedString(unplayedItemCount)).append("\n");
    sb.append("    playbackPositionTicks: ").append(toIndentedString(playbackPositionTicks)).append("\n");
    sb.append("    playCount: ").append(toIndentedString(playCount)).append("\n");
    sb.append("    isFavorite: ").append(toIndentedString(isFavorite)).append("\n");
    sb.append("    likes: ").append(toIndentedString(likes)).append("\n");
    sb.append("    lastPlayedDate: ").append(toIndentedString(lastPlayedDate)).append("\n");
    sb.append("    played: ").append(toIndentedString(played)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
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
    openapiFields.add("Rating");
    openapiFields.add("PlayedPercentage");
    openapiFields.add("UnplayedItemCount");
    openapiFields.add("PlaybackPositionTicks");
    openapiFields.add("PlayCount");
    openapiFields.add("IsFavorite");
    openapiFields.add("Likes");
    openapiFields.add("LastPlayedDate");
    openapiFields.add("Played");
    openapiFields.add("Key");
    openapiFields.add("ItemId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to UserItemDataDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!UserItemDataDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in UserItemDataDto is not found in the empty JSON string", UserItemDataDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!UserItemDataDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UserItemDataDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Key") != null && !jsonObj.get("Key").isJsonNull()) && !jsonObj.get("Key").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Key` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Key").toString()));
      }
      if ((jsonObj.get("ItemId") != null && !jsonObj.get("ItemId").isJsonNull()) && !jsonObj.get("ItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ItemId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UserItemDataDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UserItemDataDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UserItemDataDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UserItemDataDto.class));

       return (TypeAdapter<T>) new TypeAdapter<UserItemDataDto>() {
           @Override
           public void write(JsonWriter out, UserItemDataDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UserItemDataDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of UserItemDataDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of UserItemDataDto
   * @throws IOException if the JSON string is invalid with respect to UserItemDataDto
   */
  public static UserItemDataDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UserItemDataDto.class);
  }

  /**
   * Convert an instance of UserItemDataDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

