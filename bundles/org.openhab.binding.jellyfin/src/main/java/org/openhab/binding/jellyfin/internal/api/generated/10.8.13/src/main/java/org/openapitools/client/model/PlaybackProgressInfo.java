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
import org.openapitools.client.model.BaseItemDto;
import org.openapitools.client.model.PlayMethod;
import org.openapitools.client.model.QueueItem;
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
 * Class PlaybackProgressInfo.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class PlaybackProgressInfo {
  public static final String SERIALIZED_NAME_CAN_SEEK = "CanSeek";
  @SerializedName(SERIALIZED_NAME_CAN_SEEK)
  @javax.annotation.Nullable
  private Boolean canSeek;

  public static final String SERIALIZED_NAME_ITEM = "Item";
  @SerializedName(SERIALIZED_NAME_ITEM)
  @javax.annotation.Nullable
  private BaseItemDto item;

  public static final String SERIALIZED_NAME_ITEM_ID = "ItemId";
  @SerializedName(SERIALIZED_NAME_ITEM_ID)
  @javax.annotation.Nullable
  private UUID itemId;

  public static final String SERIALIZED_NAME_SESSION_ID = "SessionId";
  @SerializedName(SERIALIZED_NAME_SESSION_ID)
  @javax.annotation.Nullable
  private String sessionId;

  public static final String SERIALIZED_NAME_MEDIA_SOURCE_ID = "MediaSourceId";
  @SerializedName(SERIALIZED_NAME_MEDIA_SOURCE_ID)
  @javax.annotation.Nullable
  private String mediaSourceId;

  public static final String SERIALIZED_NAME_AUDIO_STREAM_INDEX = "AudioStreamIndex";
  @SerializedName(SERIALIZED_NAME_AUDIO_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer audioStreamIndex;

  public static final String SERIALIZED_NAME_SUBTITLE_STREAM_INDEX = "SubtitleStreamIndex";
  @SerializedName(SERIALIZED_NAME_SUBTITLE_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer subtitleStreamIndex;

  public static final String SERIALIZED_NAME_IS_PAUSED = "IsPaused";
  @SerializedName(SERIALIZED_NAME_IS_PAUSED)
  @javax.annotation.Nullable
  private Boolean isPaused;

  public static final String SERIALIZED_NAME_IS_MUTED = "IsMuted";
  @SerializedName(SERIALIZED_NAME_IS_MUTED)
  @javax.annotation.Nullable
  private Boolean isMuted;

  public static final String SERIALIZED_NAME_POSITION_TICKS = "PositionTicks";
  @SerializedName(SERIALIZED_NAME_POSITION_TICKS)
  @javax.annotation.Nullable
  private Long positionTicks;

  public static final String SERIALIZED_NAME_PLAYBACK_START_TIME_TICKS = "PlaybackStartTimeTicks";
  @SerializedName(SERIALIZED_NAME_PLAYBACK_START_TIME_TICKS)
  @javax.annotation.Nullable
  private Long playbackStartTimeTicks;

  public static final String SERIALIZED_NAME_VOLUME_LEVEL = "VolumeLevel";
  @SerializedName(SERIALIZED_NAME_VOLUME_LEVEL)
  @javax.annotation.Nullable
  private Integer volumeLevel;

  public static final String SERIALIZED_NAME_BRIGHTNESS = "Brightness";
  @SerializedName(SERIALIZED_NAME_BRIGHTNESS)
  @javax.annotation.Nullable
  private Integer brightness;

  public static final String SERIALIZED_NAME_ASPECT_RATIO = "AspectRatio";
  @SerializedName(SERIALIZED_NAME_ASPECT_RATIO)
  @javax.annotation.Nullable
  private String aspectRatio;

  public static final String SERIALIZED_NAME_PLAY_METHOD = "PlayMethod";
  @SerializedName(SERIALIZED_NAME_PLAY_METHOD)
  @javax.annotation.Nullable
  private PlayMethod playMethod;

  public static final String SERIALIZED_NAME_LIVE_STREAM_ID = "LiveStreamId";
  @SerializedName(SERIALIZED_NAME_LIVE_STREAM_ID)
  @javax.annotation.Nullable
  private String liveStreamId;

  public static final String SERIALIZED_NAME_PLAY_SESSION_ID = "PlaySessionId";
  @SerializedName(SERIALIZED_NAME_PLAY_SESSION_ID)
  @javax.annotation.Nullable
  private String playSessionId;

  public static final String SERIALIZED_NAME_REPEAT_MODE = "RepeatMode";
  @SerializedName(SERIALIZED_NAME_REPEAT_MODE)
  @javax.annotation.Nullable
  private RepeatMode repeatMode;

  public static final String SERIALIZED_NAME_NOW_PLAYING_QUEUE = "NowPlayingQueue";
  @SerializedName(SERIALIZED_NAME_NOW_PLAYING_QUEUE)
  @javax.annotation.Nullable
  private List<QueueItem> nowPlayingQueue;

  public static final String SERIALIZED_NAME_PLAYLIST_ITEM_ID = "PlaylistItemId";
  @SerializedName(SERIALIZED_NAME_PLAYLIST_ITEM_ID)
  @javax.annotation.Nullable
  private String playlistItemId;

  public PlaybackProgressInfo() {
  }

  public PlaybackProgressInfo canSeek(@javax.annotation.Nullable Boolean canSeek) {
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


  public PlaybackProgressInfo item(@javax.annotation.Nullable BaseItemDto item) {
    this.item = item;
    return this;
  }

  /**
   * Gets or sets the item.
   * @return item
   */
  @javax.annotation.Nullable
  public BaseItemDto getItem() {
    return item;
  }

  public void setItem(@javax.annotation.Nullable BaseItemDto item) {
    this.item = item;
  }


  public PlaybackProgressInfo itemId(@javax.annotation.Nullable UUID itemId) {
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


  public PlaybackProgressInfo sessionId(@javax.annotation.Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Gets or sets the session id.
   * @return sessionId
   */
  @javax.annotation.Nullable
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@javax.annotation.Nullable String sessionId) {
    this.sessionId = sessionId;
  }


  public PlaybackProgressInfo mediaSourceId(@javax.annotation.Nullable String mediaSourceId) {
    this.mediaSourceId = mediaSourceId;
    return this;
  }

  /**
   * Gets or sets the media version identifier.
   * @return mediaSourceId
   */
  @javax.annotation.Nullable
  public String getMediaSourceId() {
    return mediaSourceId;
  }

  public void setMediaSourceId(@javax.annotation.Nullable String mediaSourceId) {
    this.mediaSourceId = mediaSourceId;
  }


  public PlaybackProgressInfo audioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
    return this;
  }

  /**
   * Gets or sets the index of the audio stream.
   * @return audioStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getAudioStreamIndex() {
    return audioStreamIndex;
  }

  public void setAudioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
  }


  public PlaybackProgressInfo subtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
    return this;
  }

  /**
   * Gets or sets the index of the subtitle stream.
   * @return subtitleStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getSubtitleStreamIndex() {
    return subtitleStreamIndex;
  }

  public void setSubtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
  }


  public PlaybackProgressInfo isPaused(@javax.annotation.Nullable Boolean isPaused) {
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


  public PlaybackProgressInfo isMuted(@javax.annotation.Nullable Boolean isMuted) {
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


  public PlaybackProgressInfo positionTicks(@javax.annotation.Nullable Long positionTicks) {
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


  public PlaybackProgressInfo playbackStartTimeTicks(@javax.annotation.Nullable Long playbackStartTimeTicks) {
    this.playbackStartTimeTicks = playbackStartTimeTicks;
    return this;
  }

  /**
   * Get playbackStartTimeTicks
   * @return playbackStartTimeTicks
   */
  @javax.annotation.Nullable
  public Long getPlaybackStartTimeTicks() {
    return playbackStartTimeTicks;
  }

  public void setPlaybackStartTimeTicks(@javax.annotation.Nullable Long playbackStartTimeTicks) {
    this.playbackStartTimeTicks = playbackStartTimeTicks;
  }


  public PlaybackProgressInfo volumeLevel(@javax.annotation.Nullable Integer volumeLevel) {
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


  public PlaybackProgressInfo brightness(@javax.annotation.Nullable Integer brightness) {
    this.brightness = brightness;
    return this;
  }

  /**
   * Get brightness
   * @return brightness
   */
  @javax.annotation.Nullable
  public Integer getBrightness() {
    return brightness;
  }

  public void setBrightness(@javax.annotation.Nullable Integer brightness) {
    this.brightness = brightness;
  }


  public PlaybackProgressInfo aspectRatio(@javax.annotation.Nullable String aspectRatio) {
    this.aspectRatio = aspectRatio;
    return this;
  }

  /**
   * Get aspectRatio
   * @return aspectRatio
   */
  @javax.annotation.Nullable
  public String getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(@javax.annotation.Nullable String aspectRatio) {
    this.aspectRatio = aspectRatio;
  }


  public PlaybackProgressInfo playMethod(@javax.annotation.Nullable PlayMethod playMethod) {
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


  public PlaybackProgressInfo liveStreamId(@javax.annotation.Nullable String liveStreamId) {
    this.liveStreamId = liveStreamId;
    return this;
  }

  /**
   * Gets or sets the live stream identifier.
   * @return liveStreamId
   */
  @javax.annotation.Nullable
  public String getLiveStreamId() {
    return liveStreamId;
  }

  public void setLiveStreamId(@javax.annotation.Nullable String liveStreamId) {
    this.liveStreamId = liveStreamId;
  }


  public PlaybackProgressInfo playSessionId(@javax.annotation.Nullable String playSessionId) {
    this.playSessionId = playSessionId;
    return this;
  }

  /**
   * Gets or sets the play session identifier.
   * @return playSessionId
   */
  @javax.annotation.Nullable
  public String getPlaySessionId() {
    return playSessionId;
  }

  public void setPlaySessionId(@javax.annotation.Nullable String playSessionId) {
    this.playSessionId = playSessionId;
  }


  public PlaybackProgressInfo repeatMode(@javax.annotation.Nullable RepeatMode repeatMode) {
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


  public PlaybackProgressInfo nowPlayingQueue(@javax.annotation.Nullable List<QueueItem> nowPlayingQueue) {
    this.nowPlayingQueue = nowPlayingQueue;
    return this;
  }

  public PlaybackProgressInfo addNowPlayingQueueItem(QueueItem nowPlayingQueueItem) {
    if (this.nowPlayingQueue == null) {
      this.nowPlayingQueue = new ArrayList<>();
    }
    this.nowPlayingQueue.add(nowPlayingQueueItem);
    return this;
  }

  /**
   * Get nowPlayingQueue
   * @return nowPlayingQueue
   */
  @javax.annotation.Nullable
  public List<QueueItem> getNowPlayingQueue() {
    return nowPlayingQueue;
  }

  public void setNowPlayingQueue(@javax.annotation.Nullable List<QueueItem> nowPlayingQueue) {
    this.nowPlayingQueue = nowPlayingQueue;
  }


  public PlaybackProgressInfo playlistItemId(@javax.annotation.Nullable String playlistItemId) {
    this.playlistItemId = playlistItemId;
    return this;
  }

  /**
   * Get playlistItemId
   * @return playlistItemId
   */
  @javax.annotation.Nullable
  public String getPlaylistItemId() {
    return playlistItemId;
  }

  public void setPlaylistItemId(@javax.annotation.Nullable String playlistItemId) {
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
    PlaybackProgressInfo playbackProgressInfo = (PlaybackProgressInfo) o;
    return Objects.equals(this.canSeek, playbackProgressInfo.canSeek) &&
        Objects.equals(this.item, playbackProgressInfo.item) &&
        Objects.equals(this.itemId, playbackProgressInfo.itemId) &&
        Objects.equals(this.sessionId, playbackProgressInfo.sessionId) &&
        Objects.equals(this.mediaSourceId, playbackProgressInfo.mediaSourceId) &&
        Objects.equals(this.audioStreamIndex, playbackProgressInfo.audioStreamIndex) &&
        Objects.equals(this.subtitleStreamIndex, playbackProgressInfo.subtitleStreamIndex) &&
        Objects.equals(this.isPaused, playbackProgressInfo.isPaused) &&
        Objects.equals(this.isMuted, playbackProgressInfo.isMuted) &&
        Objects.equals(this.positionTicks, playbackProgressInfo.positionTicks) &&
        Objects.equals(this.playbackStartTimeTicks, playbackProgressInfo.playbackStartTimeTicks) &&
        Objects.equals(this.volumeLevel, playbackProgressInfo.volumeLevel) &&
        Objects.equals(this.brightness, playbackProgressInfo.brightness) &&
        Objects.equals(this.aspectRatio, playbackProgressInfo.aspectRatio) &&
        Objects.equals(this.playMethod, playbackProgressInfo.playMethod) &&
        Objects.equals(this.liveStreamId, playbackProgressInfo.liveStreamId) &&
        Objects.equals(this.playSessionId, playbackProgressInfo.playSessionId) &&
        Objects.equals(this.repeatMode, playbackProgressInfo.repeatMode) &&
        Objects.equals(this.nowPlayingQueue, playbackProgressInfo.nowPlayingQueue) &&
        Objects.equals(this.playlistItemId, playbackProgressInfo.playlistItemId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(canSeek, item, itemId, sessionId, mediaSourceId, audioStreamIndex, subtitleStreamIndex, isPaused, isMuted, positionTicks, playbackStartTimeTicks, volumeLevel, brightness, aspectRatio, playMethod, liveStreamId, playSessionId, repeatMode, nowPlayingQueue, playlistItemId);
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
    sb.append("class PlaybackProgressInfo {\n");
    sb.append("    canSeek: ").append(toIndentedString(canSeek)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    mediaSourceId: ").append(toIndentedString(mediaSourceId)).append("\n");
    sb.append("    audioStreamIndex: ").append(toIndentedString(audioStreamIndex)).append("\n");
    sb.append("    subtitleStreamIndex: ").append(toIndentedString(subtitleStreamIndex)).append("\n");
    sb.append("    isPaused: ").append(toIndentedString(isPaused)).append("\n");
    sb.append("    isMuted: ").append(toIndentedString(isMuted)).append("\n");
    sb.append("    positionTicks: ").append(toIndentedString(positionTicks)).append("\n");
    sb.append("    playbackStartTimeTicks: ").append(toIndentedString(playbackStartTimeTicks)).append("\n");
    sb.append("    volumeLevel: ").append(toIndentedString(volumeLevel)).append("\n");
    sb.append("    brightness: ").append(toIndentedString(brightness)).append("\n");
    sb.append("    aspectRatio: ").append(toIndentedString(aspectRatio)).append("\n");
    sb.append("    playMethod: ").append(toIndentedString(playMethod)).append("\n");
    sb.append("    liveStreamId: ").append(toIndentedString(liveStreamId)).append("\n");
    sb.append("    playSessionId: ").append(toIndentedString(playSessionId)).append("\n");
    sb.append("    repeatMode: ").append(toIndentedString(repeatMode)).append("\n");
    sb.append("    nowPlayingQueue: ").append(toIndentedString(nowPlayingQueue)).append("\n");
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
    openapiFields.add("CanSeek");
    openapiFields.add("Item");
    openapiFields.add("ItemId");
    openapiFields.add("SessionId");
    openapiFields.add("MediaSourceId");
    openapiFields.add("AudioStreamIndex");
    openapiFields.add("SubtitleStreamIndex");
    openapiFields.add("IsPaused");
    openapiFields.add("IsMuted");
    openapiFields.add("PositionTicks");
    openapiFields.add("PlaybackStartTimeTicks");
    openapiFields.add("VolumeLevel");
    openapiFields.add("Brightness");
    openapiFields.add("AspectRatio");
    openapiFields.add("PlayMethod");
    openapiFields.add("LiveStreamId");
    openapiFields.add("PlaySessionId");
    openapiFields.add("RepeatMode");
    openapiFields.add("NowPlayingQueue");
    openapiFields.add("PlaylistItemId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to PlaybackProgressInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!PlaybackProgressInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in PlaybackProgressInfo is not found in the empty JSON string", PlaybackProgressInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!PlaybackProgressInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `PlaybackProgressInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `Item`
      if (jsonObj.get("Item") != null && !jsonObj.get("Item").isJsonNull()) {
        BaseItemDto.validateJsonElement(jsonObj.get("Item"));
      }
      if ((jsonObj.get("ItemId") != null && !jsonObj.get("ItemId").isJsonNull()) && !jsonObj.get("ItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ItemId").toString()));
      }
      if ((jsonObj.get("SessionId") != null && !jsonObj.get("SessionId").isJsonNull()) && !jsonObj.get("SessionId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SessionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SessionId").toString()));
      }
      if ((jsonObj.get("MediaSourceId") != null && !jsonObj.get("MediaSourceId").isJsonNull()) && !jsonObj.get("MediaSourceId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MediaSourceId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MediaSourceId").toString()));
      }
      if ((jsonObj.get("AspectRatio") != null && !jsonObj.get("AspectRatio").isJsonNull()) && !jsonObj.get("AspectRatio").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `AspectRatio` to be a primitive type in the JSON string but got `%s`", jsonObj.get("AspectRatio").toString()));
      }
      // validate the optional field `PlayMethod`
      if (jsonObj.get("PlayMethod") != null && !jsonObj.get("PlayMethod").isJsonNull()) {
        PlayMethod.validateJsonElement(jsonObj.get("PlayMethod"));
      }
      if ((jsonObj.get("LiveStreamId") != null && !jsonObj.get("LiveStreamId").isJsonNull()) && !jsonObj.get("LiveStreamId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `LiveStreamId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("LiveStreamId").toString()));
      }
      if ((jsonObj.get("PlaySessionId") != null && !jsonObj.get("PlaySessionId").isJsonNull()) && !jsonObj.get("PlaySessionId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PlaySessionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PlaySessionId").toString()));
      }
      // validate the optional field `RepeatMode`
      if (jsonObj.get("RepeatMode") != null && !jsonObj.get("RepeatMode").isJsonNull()) {
        RepeatMode.validateJsonElement(jsonObj.get("RepeatMode"));
      }
      if (jsonObj.get("NowPlayingQueue") != null && !jsonObj.get("NowPlayingQueue").isJsonNull()) {
        JsonArray jsonArraynowPlayingQueue = jsonObj.getAsJsonArray("NowPlayingQueue");
        if (jsonArraynowPlayingQueue != null) {
          // ensure the json data is an array
          if (!jsonObj.get("NowPlayingQueue").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `NowPlayingQueue` to be an array in the JSON string but got `%s`", jsonObj.get("NowPlayingQueue").toString()));
          }

          // validate the optional field `NowPlayingQueue` (array)
          for (int i = 0; i < jsonArraynowPlayingQueue.size(); i++) {
            QueueItem.validateJsonElement(jsonArraynowPlayingQueue.get(i));
          };
        }
      }
      if ((jsonObj.get("PlaylistItemId") != null && !jsonObj.get("PlaylistItemId").isJsonNull()) && !jsonObj.get("PlaylistItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PlaylistItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PlaylistItemId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!PlaybackProgressInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'PlaybackProgressInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<PlaybackProgressInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(PlaybackProgressInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<PlaybackProgressInfo>() {
           @Override
           public void write(JsonWriter out, PlaybackProgressInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public PlaybackProgressInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of PlaybackProgressInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of PlaybackProgressInfo
   * @throws IOException if the JSON string is invalid with respect to PlaybackProgressInfo
   */
  public static PlaybackProgressInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, PlaybackProgressInfo.class);
  }

  /**
   * Convert an instance of PlaybackProgressInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

