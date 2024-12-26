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
import java.util.Arrays;
import org.openapitools.client.model.PlayMethod;
import org.openapitools.client.model.RepeatMode;
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
 * PlayerStateInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class PlayerStateInfo {
  public static final String SERIALIZED_NAME_POSITION_TICKS = "PositionTicks";
  @SerializedName(SERIALIZED_NAME_POSITION_TICKS)
  @javax.annotation.Nullable
  private Long positionTicks;

  public static final String SERIALIZED_NAME_CAN_SEEK = "CanSeek";
  @SerializedName(SERIALIZED_NAME_CAN_SEEK)
  @javax.annotation.Nullable
  private Boolean canSeek;

  public static final String SERIALIZED_NAME_IS_PAUSED = "IsPaused";
  @SerializedName(SERIALIZED_NAME_IS_PAUSED)
  @javax.annotation.Nullable
  private Boolean isPaused;

  public static final String SERIALIZED_NAME_IS_MUTED = "IsMuted";
  @SerializedName(SERIALIZED_NAME_IS_MUTED)
  @javax.annotation.Nullable
  private Boolean isMuted;

  public static final String SERIALIZED_NAME_VOLUME_LEVEL = "VolumeLevel";
  @SerializedName(SERIALIZED_NAME_VOLUME_LEVEL)
  @javax.annotation.Nullable
  private Integer volumeLevel;

  public static final String SERIALIZED_NAME_AUDIO_STREAM_INDEX = "AudioStreamIndex";
  @SerializedName(SERIALIZED_NAME_AUDIO_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer audioStreamIndex;

  public static final String SERIALIZED_NAME_SUBTITLE_STREAM_INDEX = "SubtitleStreamIndex";
  @SerializedName(SERIALIZED_NAME_SUBTITLE_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer subtitleStreamIndex;

  public static final String SERIALIZED_NAME_MEDIA_SOURCE_ID = "MediaSourceId";
  @SerializedName(SERIALIZED_NAME_MEDIA_SOURCE_ID)
  @javax.annotation.Nullable
  private String mediaSourceId;

  public static final String SERIALIZED_NAME_PLAY_METHOD = "PlayMethod";
  @SerializedName(SERIALIZED_NAME_PLAY_METHOD)
  @javax.annotation.Nullable
  private PlayMethod playMethod;

  public static final String SERIALIZED_NAME_REPEAT_MODE = "RepeatMode";
  @SerializedName(SERIALIZED_NAME_REPEAT_MODE)
  @javax.annotation.Nullable
  private RepeatMode repeatMode;

  public static final String SERIALIZED_NAME_LIVE_STREAM_ID = "LiveStreamId";
  @SerializedName(SERIALIZED_NAME_LIVE_STREAM_ID)
  @javax.annotation.Nullable
  private String liveStreamId;

  public PlayerStateInfo() {
  }

  public PlayerStateInfo positionTicks(@javax.annotation.Nullable Long positionTicks) {
    this.positionTicks = positionTicks;
    return this;
  }

  /**
   * Gets or sets the now playing position ticks.
   * @return positionTicks
   */
  @javax.annotation.Nullable
  public Long getPositionTicks() {
    return positionTicks;
  }

  public void setPositionTicks(@javax.annotation.Nullable Long positionTicks) {
    this.positionTicks = positionTicks;
  }


  public PlayerStateInfo canSeek(@javax.annotation.Nullable Boolean canSeek) {
    this.canSeek = canSeek;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance can seek.
   * @return canSeek
   */
  @javax.annotation.Nullable
  public Boolean getCanSeek() {
    return canSeek;
  }

  public void setCanSeek(@javax.annotation.Nullable Boolean canSeek) {
    this.canSeek = canSeek;
  }


  public PlayerStateInfo isPaused(@javax.annotation.Nullable Boolean isPaused) {
    this.isPaused = isPaused;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance is paused.
   * @return isPaused
   */
  @javax.annotation.Nullable
  public Boolean getIsPaused() {
    return isPaused;
  }

  public void setIsPaused(@javax.annotation.Nullable Boolean isPaused) {
    this.isPaused = isPaused;
  }


  public PlayerStateInfo isMuted(@javax.annotation.Nullable Boolean isMuted) {
    this.isMuted = isMuted;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance is muted.
   * @return isMuted
   */
  @javax.annotation.Nullable
  public Boolean getIsMuted() {
    return isMuted;
  }

  public void setIsMuted(@javax.annotation.Nullable Boolean isMuted) {
    this.isMuted = isMuted;
  }


  public PlayerStateInfo volumeLevel(@javax.annotation.Nullable Integer volumeLevel) {
    this.volumeLevel = volumeLevel;
    return this;
  }

  /**
   * Gets or sets the volume level.
   * @return volumeLevel
   */
  @javax.annotation.Nullable
  public Integer getVolumeLevel() {
    return volumeLevel;
  }

  public void setVolumeLevel(@javax.annotation.Nullable Integer volumeLevel) {
    this.volumeLevel = volumeLevel;
  }


  public PlayerStateInfo audioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
    return this;
  }

  /**
   * Gets or sets the index of the now playing audio stream.
   * @return audioStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getAudioStreamIndex() {
    return audioStreamIndex;
  }

  public void setAudioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
  }


  public PlayerStateInfo subtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
    return this;
  }

  /**
   * Gets or sets the index of the now playing subtitle stream.
   * @return subtitleStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getSubtitleStreamIndex() {
    return subtitleStreamIndex;
  }

  public void setSubtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
  }


  public PlayerStateInfo mediaSourceId(@javax.annotation.Nullable String mediaSourceId) {
    this.mediaSourceId = mediaSourceId;
    return this;
  }

  /**
   * Gets or sets the now playing media version identifier.
   * @return mediaSourceId
   */
  @javax.annotation.Nullable
  public String getMediaSourceId() {
    return mediaSourceId;
  }

  public void setMediaSourceId(@javax.annotation.Nullable String mediaSourceId) {
    this.mediaSourceId = mediaSourceId;
  }


  public PlayerStateInfo playMethod(@javax.annotation.Nullable PlayMethod playMethod) {
    this.playMethod = playMethod;
    return this;
  }

  /**
   * Gets or sets the play method.
   * @return playMethod
   */
  @javax.annotation.Nullable
  public PlayMethod getPlayMethod() {
    return playMethod;
  }

  public void setPlayMethod(@javax.annotation.Nullable PlayMethod playMethod) {
    this.playMethod = playMethod;
  }


  public PlayerStateInfo repeatMode(@javax.annotation.Nullable RepeatMode repeatMode) {
    this.repeatMode = repeatMode;
    return this;
  }

  /**
   * Gets or sets the repeat mode.
   * @return repeatMode
   */
  @javax.annotation.Nullable
  public RepeatMode getRepeatMode() {
    return repeatMode;
  }

  public void setRepeatMode(@javax.annotation.Nullable RepeatMode repeatMode) {
    this.repeatMode = repeatMode;
  }


  public PlayerStateInfo liveStreamId(@javax.annotation.Nullable String liveStreamId) {
    this.liveStreamId = liveStreamId;
    return this;
  }

  /**
   * Gets or sets the now playing live stream identifier.
   * @return liveStreamId
   */
  @javax.annotation.Nullable
  public String getLiveStreamId() {
    return liveStreamId;
  }

  public void setLiveStreamId(@javax.annotation.Nullable String liveStreamId) {
    this.liveStreamId = liveStreamId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerStateInfo playerStateInfo = (PlayerStateInfo) o;
    return Objects.equals(this.positionTicks, playerStateInfo.positionTicks) &&
        Objects.equals(this.canSeek, playerStateInfo.canSeek) &&
        Objects.equals(this.isPaused, playerStateInfo.isPaused) &&
        Objects.equals(this.isMuted, playerStateInfo.isMuted) &&
        Objects.equals(this.volumeLevel, playerStateInfo.volumeLevel) &&
        Objects.equals(this.audioStreamIndex, playerStateInfo.audioStreamIndex) &&
        Objects.equals(this.subtitleStreamIndex, playerStateInfo.subtitleStreamIndex) &&
        Objects.equals(this.mediaSourceId, playerStateInfo.mediaSourceId) &&
        Objects.equals(this.playMethod, playerStateInfo.playMethod) &&
        Objects.equals(this.repeatMode, playerStateInfo.repeatMode) &&
        Objects.equals(this.liveStreamId, playerStateInfo.liveStreamId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(positionTicks, canSeek, isPaused, isMuted, volumeLevel, audioStreamIndex, subtitleStreamIndex, mediaSourceId, playMethod, repeatMode, liveStreamId);
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
    sb.append("class PlayerStateInfo {\n");
    sb.append("    positionTicks: ").append(toIndentedString(positionTicks)).append("\n");
    sb.append("    canSeek: ").append(toIndentedString(canSeek)).append("\n");
    sb.append("    isPaused: ").append(toIndentedString(isPaused)).append("\n");
    sb.append("    isMuted: ").append(toIndentedString(isMuted)).append("\n");
    sb.append("    volumeLevel: ").append(toIndentedString(volumeLevel)).append("\n");
    sb.append("    audioStreamIndex: ").append(toIndentedString(audioStreamIndex)).append("\n");
    sb.append("    subtitleStreamIndex: ").append(toIndentedString(subtitleStreamIndex)).append("\n");
    sb.append("    mediaSourceId: ").append(toIndentedString(mediaSourceId)).append("\n");
    sb.append("    playMethod: ").append(toIndentedString(playMethod)).append("\n");
    sb.append("    repeatMode: ").append(toIndentedString(repeatMode)).append("\n");
    sb.append("    liveStreamId: ").append(toIndentedString(liveStreamId)).append("\n");
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
    openapiFields.add("PositionTicks");
    openapiFields.add("CanSeek");
    openapiFields.add("IsPaused");
    openapiFields.add("IsMuted");
    openapiFields.add("VolumeLevel");
    openapiFields.add("AudioStreamIndex");
    openapiFields.add("SubtitleStreamIndex");
    openapiFields.add("MediaSourceId");
    openapiFields.add("PlayMethod");
    openapiFields.add("RepeatMode");
    openapiFields.add("LiveStreamId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to PlayerStateInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!PlayerStateInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in PlayerStateInfo is not found in the empty JSON string", PlayerStateInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!PlayerStateInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `PlayerStateInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("MediaSourceId") != null && !jsonObj.get("MediaSourceId").isJsonNull()) && !jsonObj.get("MediaSourceId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MediaSourceId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MediaSourceId").toString()));
      }
      // validate the optional field `PlayMethod`
      if (jsonObj.get("PlayMethod") != null && !jsonObj.get("PlayMethod").isJsonNull()) {
        PlayMethod.validateJsonElement(jsonObj.get("PlayMethod"));
      }
      // validate the optional field `RepeatMode`
      if (jsonObj.get("RepeatMode") != null && !jsonObj.get("RepeatMode").isJsonNull()) {
        RepeatMode.validateJsonElement(jsonObj.get("RepeatMode"));
      }
      if ((jsonObj.get("LiveStreamId") != null && !jsonObj.get("LiveStreamId").isJsonNull()) && !jsonObj.get("LiveStreamId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `LiveStreamId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("LiveStreamId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!PlayerStateInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'PlayerStateInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<PlayerStateInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(PlayerStateInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<PlayerStateInfo>() {
           @Override
           public void write(JsonWriter out, PlayerStateInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public PlayerStateInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of PlayerStateInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of PlayerStateInfo
   * @throws IOException if the JSON string is invalid with respect to PlayerStateInfo
   */
  public static PlayerStateInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, PlayerStateInfo.class);
  }

  /**
   * Convert an instance of PlayerStateInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

