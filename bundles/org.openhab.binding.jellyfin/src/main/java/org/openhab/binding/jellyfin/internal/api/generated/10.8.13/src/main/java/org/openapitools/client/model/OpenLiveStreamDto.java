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
import org.openapitools.client.model.DeviceProfile;
import org.openapitools.client.model.MediaProtocol;
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
 * Open live stream dto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class OpenLiveStreamDto {
  public static final String SERIALIZED_NAME_OPEN_TOKEN = "OpenToken";
  @SerializedName(SERIALIZED_NAME_OPEN_TOKEN)
  @javax.annotation.Nullable
  private String openToken;

  public static final String SERIALIZED_NAME_USER_ID = "UserId";
  @SerializedName(SERIALIZED_NAME_USER_ID)
  @javax.annotation.Nullable
  private UUID userId;

  public static final String SERIALIZED_NAME_PLAY_SESSION_ID = "PlaySessionId";
  @SerializedName(SERIALIZED_NAME_PLAY_SESSION_ID)
  @javax.annotation.Nullable
  private String playSessionId;

  public static final String SERIALIZED_NAME_MAX_STREAMING_BITRATE = "MaxStreamingBitrate";
  @SerializedName(SERIALIZED_NAME_MAX_STREAMING_BITRATE)
  @javax.annotation.Nullable
  private Integer maxStreamingBitrate;

  public static final String SERIALIZED_NAME_START_TIME_TICKS = "StartTimeTicks";
  @SerializedName(SERIALIZED_NAME_START_TIME_TICKS)
  @javax.annotation.Nullable
  private Long startTimeTicks;

  public static final String SERIALIZED_NAME_AUDIO_STREAM_INDEX = "AudioStreamIndex";
  @SerializedName(SERIALIZED_NAME_AUDIO_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer audioStreamIndex;

  public static final String SERIALIZED_NAME_SUBTITLE_STREAM_INDEX = "SubtitleStreamIndex";
  @SerializedName(SERIALIZED_NAME_SUBTITLE_STREAM_INDEX)
  @javax.annotation.Nullable
  private Integer subtitleStreamIndex;

  public static final String SERIALIZED_NAME_MAX_AUDIO_CHANNELS = "MaxAudioChannels";
  @SerializedName(SERIALIZED_NAME_MAX_AUDIO_CHANNELS)
  @javax.annotation.Nullable
  private Integer maxAudioChannels;

  public static final String SERIALIZED_NAME_ITEM_ID = "ItemId";
  @SerializedName(SERIALIZED_NAME_ITEM_ID)
  @javax.annotation.Nullable
  private UUID itemId;

  public static final String SERIALIZED_NAME_ENABLE_DIRECT_PLAY = "EnableDirectPlay";
  @SerializedName(SERIALIZED_NAME_ENABLE_DIRECT_PLAY)
  @javax.annotation.Nullable
  private Boolean enableDirectPlay;

  public static final String SERIALIZED_NAME_ENABLE_DIRECT_STREAM = "EnableDirectStream";
  @SerializedName(SERIALIZED_NAME_ENABLE_DIRECT_STREAM)
  @javax.annotation.Nullable
  private Boolean enableDirectStream;

  public static final String SERIALIZED_NAME_DEVICE_PROFILE = "DeviceProfile";
  @SerializedName(SERIALIZED_NAME_DEVICE_PROFILE)
  @javax.annotation.Nullable
  private DeviceProfile deviceProfile;

  public static final String SERIALIZED_NAME_DIRECT_PLAY_PROTOCOLS = "DirectPlayProtocols";
  @SerializedName(SERIALIZED_NAME_DIRECT_PLAY_PROTOCOLS)
  @javax.annotation.Nullable
  private List<MediaProtocol> directPlayProtocols = new ArrayList<>();

  public OpenLiveStreamDto() {
  }

  public OpenLiveStreamDto openToken(@javax.annotation.Nullable String openToken) {
    this.openToken = openToken;
    return this;
  }

  /**
   * Gets or sets the open token.
   * @return openToken
   */
  @javax.annotation.Nullable
  public String getOpenToken() {
    return openToken;
  }

  public void setOpenToken(@javax.annotation.Nullable String openToken) {
    this.openToken = openToken;
  }


  public OpenLiveStreamDto userId(@javax.annotation.Nullable UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Gets or sets the user id.
   * @return userId
   */
  @javax.annotation.Nullable
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(@javax.annotation.Nullable UUID userId) {
    this.userId = userId;
  }


  public OpenLiveStreamDto playSessionId(@javax.annotation.Nullable String playSessionId) {
    this.playSessionId = playSessionId;
    return this;
  }

  /**
   * Gets or sets the play session id.
   * @return playSessionId
   */
  @javax.annotation.Nullable
  public String getPlaySessionId() {
    return playSessionId;
  }

  public void setPlaySessionId(@javax.annotation.Nullable String playSessionId) {
    this.playSessionId = playSessionId;
  }


  public OpenLiveStreamDto maxStreamingBitrate(@javax.annotation.Nullable Integer maxStreamingBitrate) {
    this.maxStreamingBitrate = maxStreamingBitrate;
    return this;
  }

  /**
   * Gets or sets the max streaming bitrate.
   * @return maxStreamingBitrate
   */
  @javax.annotation.Nullable
  public Integer getMaxStreamingBitrate() {
    return maxStreamingBitrate;
  }

  public void setMaxStreamingBitrate(@javax.annotation.Nullable Integer maxStreamingBitrate) {
    this.maxStreamingBitrate = maxStreamingBitrate;
  }


  public OpenLiveStreamDto startTimeTicks(@javax.annotation.Nullable Long startTimeTicks) {
    this.startTimeTicks = startTimeTicks;
    return this;
  }

  /**
   * Gets or sets the start time in ticks.
   * @return startTimeTicks
   */
  @javax.annotation.Nullable
  public Long getStartTimeTicks() {
    return startTimeTicks;
  }

  public void setStartTimeTicks(@javax.annotation.Nullable Long startTimeTicks) {
    this.startTimeTicks = startTimeTicks;
  }


  public OpenLiveStreamDto audioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
    return this;
  }

  /**
   * Gets or sets the audio stream index.
   * @return audioStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getAudioStreamIndex() {
    return audioStreamIndex;
  }

  public void setAudioStreamIndex(@javax.annotation.Nullable Integer audioStreamIndex) {
    this.audioStreamIndex = audioStreamIndex;
  }


  public OpenLiveStreamDto subtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
    return this;
  }

  /**
   * Gets or sets the subtitle stream index.
   * @return subtitleStreamIndex
   */
  @javax.annotation.Nullable
  public Integer getSubtitleStreamIndex() {
    return subtitleStreamIndex;
  }

  public void setSubtitleStreamIndex(@javax.annotation.Nullable Integer subtitleStreamIndex) {
    this.subtitleStreamIndex = subtitleStreamIndex;
  }


  public OpenLiveStreamDto maxAudioChannels(@javax.annotation.Nullable Integer maxAudioChannels) {
    this.maxAudioChannels = maxAudioChannels;
    return this;
  }

  /**
   * Gets or sets the max audio channels.
   * @return maxAudioChannels
   */
  @javax.annotation.Nullable
  public Integer getMaxAudioChannels() {
    return maxAudioChannels;
  }

  public void setMaxAudioChannels(@javax.annotation.Nullable Integer maxAudioChannels) {
    this.maxAudioChannels = maxAudioChannels;
  }


  public OpenLiveStreamDto itemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Gets or sets the item id.
   * @return itemId
   */
  @javax.annotation.Nullable
  public UUID getItemId() {
    return itemId;
  }

  public void setItemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
  }


  public OpenLiveStreamDto enableDirectPlay(@javax.annotation.Nullable Boolean enableDirectPlay) {
    this.enableDirectPlay = enableDirectPlay;
    return this;
  }

  /**
   * Gets or sets a value indicating whether to enable direct play.
   * @return enableDirectPlay
   */
  @javax.annotation.Nullable
  public Boolean getEnableDirectPlay() {
    return enableDirectPlay;
  }

  public void setEnableDirectPlay(@javax.annotation.Nullable Boolean enableDirectPlay) {
    this.enableDirectPlay = enableDirectPlay;
  }


  public OpenLiveStreamDto enableDirectStream(@javax.annotation.Nullable Boolean enableDirectStream) {
    this.enableDirectStream = enableDirectStream;
    return this;
  }

  /**
   * Gets or sets a value indicating whether to enale direct stream.
   * @return enableDirectStream
   */
  @javax.annotation.Nullable
  public Boolean getEnableDirectStream() {
    return enableDirectStream;
  }

  public void setEnableDirectStream(@javax.annotation.Nullable Boolean enableDirectStream) {
    this.enableDirectStream = enableDirectStream;
  }


  public OpenLiveStreamDto deviceProfile(@javax.annotation.Nullable DeviceProfile deviceProfile) {
    this.deviceProfile = deviceProfile;
    return this;
  }

  /**
   * A MediaBrowser.Model.Dlna.DeviceProfile represents a set of metadata which determines which content a certain device is able to play.  &lt;br /&gt;  Specifically, it defines the supported &lt;see cref&#x3D;\&quot;P:MediaBrowser.Model.Dlna.DeviceProfile.ContainerProfiles\&quot;&gt;containers&lt;/see&gt; and  &lt;see cref&#x3D;\&quot;P:MediaBrowser.Model.Dlna.DeviceProfile.CodecProfiles\&quot;&gt;codecs&lt;/see&gt; (video and/or audio, including codec profiles and levels)  the device is able to direct play (without transcoding or remuxing),  as well as which &lt;see cref&#x3D;\&quot;P:MediaBrowser.Model.Dlna.DeviceProfile.TranscodingProfiles\&quot;&gt;containers/codecs to transcode to&lt;/see&gt; in case it isn&#39;t.
   * @return deviceProfile
   */
  @javax.annotation.Nullable
  public DeviceProfile getDeviceProfile() {
    return deviceProfile;
  }

  public void setDeviceProfile(@javax.annotation.Nullable DeviceProfile deviceProfile) {
    this.deviceProfile = deviceProfile;
  }


  public OpenLiveStreamDto directPlayProtocols(@javax.annotation.Nullable List<MediaProtocol> directPlayProtocols) {
    this.directPlayProtocols = directPlayProtocols;
    return this;
  }

  public OpenLiveStreamDto addDirectPlayProtocolsItem(MediaProtocol directPlayProtocolsItem) {
    if (this.directPlayProtocols == null) {
      this.directPlayProtocols = new ArrayList<>();
    }
    this.directPlayProtocols.add(directPlayProtocolsItem);
    return this;
  }

  /**
   * Gets or sets the device play protocols.
   * @return directPlayProtocols
   */
  @javax.annotation.Nullable
  public List<MediaProtocol> getDirectPlayProtocols() {
    return directPlayProtocols;
  }

  public void setDirectPlayProtocols(@javax.annotation.Nullable List<MediaProtocol> directPlayProtocols) {
    this.directPlayProtocols = directPlayProtocols;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OpenLiveStreamDto openLiveStreamDto = (OpenLiveStreamDto) o;
    return Objects.equals(this.openToken, openLiveStreamDto.openToken) &&
        Objects.equals(this.userId, openLiveStreamDto.userId) &&
        Objects.equals(this.playSessionId, openLiveStreamDto.playSessionId) &&
        Objects.equals(this.maxStreamingBitrate, openLiveStreamDto.maxStreamingBitrate) &&
        Objects.equals(this.startTimeTicks, openLiveStreamDto.startTimeTicks) &&
        Objects.equals(this.audioStreamIndex, openLiveStreamDto.audioStreamIndex) &&
        Objects.equals(this.subtitleStreamIndex, openLiveStreamDto.subtitleStreamIndex) &&
        Objects.equals(this.maxAudioChannels, openLiveStreamDto.maxAudioChannels) &&
        Objects.equals(this.itemId, openLiveStreamDto.itemId) &&
        Objects.equals(this.enableDirectPlay, openLiveStreamDto.enableDirectPlay) &&
        Objects.equals(this.enableDirectStream, openLiveStreamDto.enableDirectStream) &&
        Objects.equals(this.deviceProfile, openLiveStreamDto.deviceProfile) &&
        Objects.equals(this.directPlayProtocols, openLiveStreamDto.directPlayProtocols);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(openToken, userId, playSessionId, maxStreamingBitrate, startTimeTicks, audioStreamIndex, subtitleStreamIndex, maxAudioChannels, itemId, enableDirectPlay, enableDirectStream, deviceProfile, directPlayProtocols);
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
    sb.append("class OpenLiveStreamDto {\n");
    sb.append("    openToken: ").append(toIndentedString(openToken)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    playSessionId: ").append(toIndentedString(playSessionId)).append("\n");
    sb.append("    maxStreamingBitrate: ").append(toIndentedString(maxStreamingBitrate)).append("\n");
    sb.append("    startTimeTicks: ").append(toIndentedString(startTimeTicks)).append("\n");
    sb.append("    audioStreamIndex: ").append(toIndentedString(audioStreamIndex)).append("\n");
    sb.append("    subtitleStreamIndex: ").append(toIndentedString(subtitleStreamIndex)).append("\n");
    sb.append("    maxAudioChannels: ").append(toIndentedString(maxAudioChannels)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    enableDirectPlay: ").append(toIndentedString(enableDirectPlay)).append("\n");
    sb.append("    enableDirectStream: ").append(toIndentedString(enableDirectStream)).append("\n");
    sb.append("    deviceProfile: ").append(toIndentedString(deviceProfile)).append("\n");
    sb.append("    directPlayProtocols: ").append(toIndentedString(directPlayProtocols)).append("\n");
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
    openapiFields.add("OpenToken");
    openapiFields.add("UserId");
    openapiFields.add("PlaySessionId");
    openapiFields.add("MaxStreamingBitrate");
    openapiFields.add("StartTimeTicks");
    openapiFields.add("AudioStreamIndex");
    openapiFields.add("SubtitleStreamIndex");
    openapiFields.add("MaxAudioChannels");
    openapiFields.add("ItemId");
    openapiFields.add("EnableDirectPlay");
    openapiFields.add("EnableDirectStream");
    openapiFields.add("DeviceProfile");
    openapiFields.add("DirectPlayProtocols");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to OpenLiveStreamDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!OpenLiveStreamDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in OpenLiveStreamDto is not found in the empty JSON string", OpenLiveStreamDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!OpenLiveStreamDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `OpenLiveStreamDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("OpenToken") != null && !jsonObj.get("OpenToken").isJsonNull()) && !jsonObj.get("OpenToken").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `OpenToken` to be a primitive type in the JSON string but got `%s`", jsonObj.get("OpenToken").toString()));
      }
      if ((jsonObj.get("UserId") != null && !jsonObj.get("UserId").isJsonNull()) && !jsonObj.get("UserId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `UserId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("UserId").toString()));
      }
      if ((jsonObj.get("PlaySessionId") != null && !jsonObj.get("PlaySessionId").isJsonNull()) && !jsonObj.get("PlaySessionId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PlaySessionId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PlaySessionId").toString()));
      }
      if ((jsonObj.get("ItemId") != null && !jsonObj.get("ItemId").isJsonNull()) && !jsonObj.get("ItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ItemId").toString()));
      }
      // validate the optional field `DeviceProfile`
      if (jsonObj.get("DeviceProfile") != null && !jsonObj.get("DeviceProfile").isJsonNull()) {
        DeviceProfile.validateJsonElement(jsonObj.get("DeviceProfile"));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("DirectPlayProtocols") != null && !jsonObj.get("DirectPlayProtocols").isJsonNull() && !jsonObj.get("DirectPlayProtocols").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `DirectPlayProtocols` to be an array in the JSON string but got `%s`", jsonObj.get("DirectPlayProtocols").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!OpenLiveStreamDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'OpenLiveStreamDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<OpenLiveStreamDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(OpenLiveStreamDto.class));

       return (TypeAdapter<T>) new TypeAdapter<OpenLiveStreamDto>() {
           @Override
           public void write(JsonWriter out, OpenLiveStreamDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public OpenLiveStreamDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of OpenLiveStreamDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of OpenLiveStreamDto
   * @throws IOException if the JSON string is invalid with respect to OpenLiveStreamDto
   */
  public static OpenLiveStreamDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, OpenLiveStreamDto.class);
  }

  /**
   * Convert an instance of OpenLiveStreamDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

