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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.client.model.HardwareAccelerationType;
import org.openapitools.client.model.TranscodeReason;
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
 * Class holding information on a runnning transcode.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class TranscodingInfo {
  public static final String SERIALIZED_NAME_AUDIO_CODEC = "AudioCodec";
  @SerializedName(SERIALIZED_NAME_AUDIO_CODEC)
  @javax.annotation.Nullable
  private String audioCodec;

  public static final String SERIALIZED_NAME_VIDEO_CODEC = "VideoCodec";
  @SerializedName(SERIALIZED_NAME_VIDEO_CODEC)
  @javax.annotation.Nullable
  private String videoCodec;

  public static final String SERIALIZED_NAME_CONTAINER = "Container";
  @SerializedName(SERIALIZED_NAME_CONTAINER)
  @javax.annotation.Nullable
  private String container;

  public static final String SERIALIZED_NAME_IS_VIDEO_DIRECT = "IsVideoDirect";
  @SerializedName(SERIALIZED_NAME_IS_VIDEO_DIRECT)
  @javax.annotation.Nullable
  private Boolean isVideoDirect;

  public static final String SERIALIZED_NAME_IS_AUDIO_DIRECT = "IsAudioDirect";
  @SerializedName(SERIALIZED_NAME_IS_AUDIO_DIRECT)
  @javax.annotation.Nullable
  private Boolean isAudioDirect;

  public static final String SERIALIZED_NAME_BITRATE = "Bitrate";
  @SerializedName(SERIALIZED_NAME_BITRATE)
  @javax.annotation.Nullable
  private Integer bitrate;

  public static final String SERIALIZED_NAME_FRAMERATE = "Framerate";
  @SerializedName(SERIALIZED_NAME_FRAMERATE)
  @javax.annotation.Nullable
  private Float framerate;

  public static final String SERIALIZED_NAME_COMPLETION_PERCENTAGE = "CompletionPercentage";
  @SerializedName(SERIALIZED_NAME_COMPLETION_PERCENTAGE)
  @javax.annotation.Nullable
  private Double completionPercentage;

  public static final String SERIALIZED_NAME_WIDTH = "Width";
  @SerializedName(SERIALIZED_NAME_WIDTH)
  @javax.annotation.Nullable
  private Integer width;

  public static final String SERIALIZED_NAME_HEIGHT = "Height";
  @SerializedName(SERIALIZED_NAME_HEIGHT)
  @javax.annotation.Nullable
  private Integer height;

  public static final String SERIALIZED_NAME_AUDIO_CHANNELS = "AudioChannels";
  @SerializedName(SERIALIZED_NAME_AUDIO_CHANNELS)
  @javax.annotation.Nullable
  private Integer audioChannels;

  public static final String SERIALIZED_NAME_HARDWARE_ACCELERATION_TYPE = "HardwareAccelerationType";
  @SerializedName(SERIALIZED_NAME_HARDWARE_ACCELERATION_TYPE)
  @javax.annotation.Nullable
  private HardwareAccelerationType hardwareAccelerationType;

  /**
   * Gets or Sets transcodeReasons
   */
  @JsonAdapter(TranscodeReason.Adapter.class)
  public enum TranscodeReason {
    CONTAINER_NOT_SUPPORTED("ContainerNotSupported"),
    
    VIDEO_CODEC_NOT_SUPPORTED("VideoCodecNotSupported"),
    
    AUDIO_CODEC_NOT_SUPPORTED("AudioCodecNotSupported"),
    
    SUBTITLE_CODEC_NOT_SUPPORTED("SubtitleCodecNotSupported"),
    
    AUDIO_IS_EXTERNAL("AudioIsExternal"),
    
    SECONDARY_AUDIO_NOT_SUPPORTED("SecondaryAudioNotSupported"),
    
    VIDEO_PROFILE_NOT_SUPPORTED("VideoProfileNotSupported"),
    
    VIDEO_LEVEL_NOT_SUPPORTED("VideoLevelNotSupported"),
    
    VIDEO_RESOLUTION_NOT_SUPPORTED("VideoResolutionNotSupported"),
    
    VIDEO_BIT_DEPTH_NOT_SUPPORTED("VideoBitDepthNotSupported"),
    
    VIDEO_FRAMERATE_NOT_SUPPORTED("VideoFramerateNotSupported"),
    
    REF_FRAMES_NOT_SUPPORTED("RefFramesNotSupported"),
    
    ANAMORPHIC_VIDEO_NOT_SUPPORTED("AnamorphicVideoNotSupported"),
    
    INTERLACED_VIDEO_NOT_SUPPORTED("InterlacedVideoNotSupported"),
    
    AUDIO_CHANNELS_NOT_SUPPORTED("AudioChannelsNotSupported"),
    
    AUDIO_PROFILE_NOT_SUPPORTED("AudioProfileNotSupported"),
    
    AUDIO_SAMPLE_RATE_NOT_SUPPORTED("AudioSampleRateNotSupported"),
    
    AUDIO_BIT_DEPTH_NOT_SUPPORTED("AudioBitDepthNotSupported"),
    
    CONTAINER_BITRATE_EXCEEDS_LIMIT("ContainerBitrateExceedsLimit"),
    
    VIDEO_BITRATE_NOT_SUPPORTED("VideoBitrateNotSupported"),
    
    AUDIO_BITRATE_NOT_SUPPORTED("AudioBitrateNotSupported"),
    
    UNKNOWN_VIDEO_STREAM_INFO("UnknownVideoStreamInfo"),
    
    UNKNOWN_AUDIO_STREAM_INFO("UnknownAudioStreamInfo"),
    
    DIRECT_PLAY_ERROR("DirectPlayError"),
    
    VIDEO_RANGE_TYPE_NOT_SUPPORTED("VideoRangeTypeNotSupported"),
    
    VIDEO_CODEC_TAG_NOT_SUPPORTED("VideoCodecTagNotSupported");

    private TranscodeReason value;

    TranscodeReason(TranscodeReason value) {
      this.value = value;
    }

    public TranscodeReason getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TranscodeReason fromValue(TranscodeReason value) {
      for (TranscodeReason b : TranscodeReason.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TranscodeReason> {
      @Override
      public void write(final JsonWriter jsonWriter, final TranscodeReason enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TranscodeReason read(final JsonReader jsonReader) throws IOException {
        TranscodeReason value =  jsonReader.nextTranscodeReason();
        return TranscodeReason.fromValue(value);
      }
    }

    public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      TranscodeReason value = jsonElement.getAsTranscodeReason();
      TranscodeReason.fromValue(value);
    }
  }

  public static final String SERIALIZED_NAME_TRANSCODE_REASONS = "TranscodeReasons";
  @SerializedName(SERIALIZED_NAME_TRANSCODE_REASONS)
  @javax.annotation.Nullable
  private TranscodeReasonsEnum transcodeReasons = new ArrayList<>();

  public TranscodingInfo() {
  }

  public TranscodingInfo audioCodec(@javax.annotation.Nullable String audioCodec) {
    this.audioCodec = audioCodec;
    return this;
  }

  /**
   * Gets or sets the thread count used for encoding.
   * @return audioCodec
   */
  @javax.annotation.Nullable
  public String getAudioCodec() {
    return audioCodec;
  }

  public void setAudioCodec(@javax.annotation.Nullable String audioCodec) {
    this.audioCodec = audioCodec;
  }


  public TranscodingInfo videoCodec(@javax.annotation.Nullable String videoCodec) {
    this.videoCodec = videoCodec;
    return this;
  }

  /**
   * Gets or sets the thread count used for encoding.
   * @return videoCodec
   */
  @javax.annotation.Nullable
  public String getVideoCodec() {
    return videoCodec;
  }

  public void setVideoCodec(@javax.annotation.Nullable String videoCodec) {
    this.videoCodec = videoCodec;
  }


  public TranscodingInfo container(@javax.annotation.Nullable String container) {
    this.container = container;
    return this;
  }

  /**
   * Gets or sets the thread count used for encoding.
   * @return container
   */
  @javax.annotation.Nullable
  public String getContainer() {
    return container;
  }

  public void setContainer(@javax.annotation.Nullable String container) {
    this.container = container;
  }


  public TranscodingInfo isVideoDirect(@javax.annotation.Nullable Boolean isVideoDirect) {
    this.isVideoDirect = isVideoDirect;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the video is passed through.
   * @return isVideoDirect
   */
  @javax.annotation.Nullable
  public Boolean getIsVideoDirect() {
    return isVideoDirect;
  }

  public void setIsVideoDirect(@javax.annotation.Nullable Boolean isVideoDirect) {
    this.isVideoDirect = isVideoDirect;
  }


  public TranscodingInfo isAudioDirect(@javax.annotation.Nullable Boolean isAudioDirect) {
    this.isAudioDirect = isAudioDirect;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the audio is passed through.
   * @return isAudioDirect
   */
  @javax.annotation.Nullable
  public Boolean getIsAudioDirect() {
    return isAudioDirect;
  }

  public void setIsAudioDirect(@javax.annotation.Nullable Boolean isAudioDirect) {
    this.isAudioDirect = isAudioDirect;
  }


  public TranscodingInfo bitrate(@javax.annotation.Nullable Integer bitrate) {
    this.bitrate = bitrate;
    return this;
  }

  /**
   * Gets or sets the bitrate.
   * @return bitrate
   */
  @javax.annotation.Nullable
  public Integer getBitrate() {
    return bitrate;
  }

  public void setBitrate(@javax.annotation.Nullable Integer bitrate) {
    this.bitrate = bitrate;
  }


  public TranscodingInfo framerate(@javax.annotation.Nullable Float framerate) {
    this.framerate = framerate;
    return this;
  }

  /**
   * Gets or sets the framerate.
   * @return framerate
   */
  @javax.annotation.Nullable
  public Float getFramerate() {
    return framerate;
  }

  public void setFramerate(@javax.annotation.Nullable Float framerate) {
    this.framerate = framerate;
  }


  public TranscodingInfo completionPercentage(@javax.annotation.Nullable Double completionPercentage) {
    this.completionPercentage = completionPercentage;
    return this;
  }

  /**
   * Gets or sets the completion percentage.
   * @return completionPercentage
   */
  @javax.annotation.Nullable
  public Double getCompletionPercentage() {
    return completionPercentage;
  }

  public void setCompletionPercentage(@javax.annotation.Nullable Double completionPercentage) {
    this.completionPercentage = completionPercentage;
  }


  public TranscodingInfo width(@javax.annotation.Nullable Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Gets or sets the video width.
   * @return width
   */
  @javax.annotation.Nullable
  public Integer getWidth() {
    return width;
  }

  public void setWidth(@javax.annotation.Nullable Integer width) {
    this.width = width;
  }


  public TranscodingInfo height(@javax.annotation.Nullable Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Gets or sets the video height.
   * @return height
   */
  @javax.annotation.Nullable
  public Integer getHeight() {
    return height;
  }

  public void setHeight(@javax.annotation.Nullable Integer height) {
    this.height = height;
  }


  public TranscodingInfo audioChannels(@javax.annotation.Nullable Integer audioChannels) {
    this.audioChannels = audioChannels;
    return this;
  }

  /**
   * Gets or sets the audio channels.
   * @return audioChannels
   */
  @javax.annotation.Nullable
  public Integer getAudioChannels() {
    return audioChannels;
  }

  public void setAudioChannels(@javax.annotation.Nullable Integer audioChannels) {
    this.audioChannels = audioChannels;
  }


  public TranscodingInfo hardwareAccelerationType(@javax.annotation.Nullable HardwareAccelerationType hardwareAccelerationType) {
    this.hardwareAccelerationType = hardwareAccelerationType;
    return this;
  }

  /**
   * Gets or sets the hardware acceleration type.
   * @return hardwareAccelerationType
   */
  @javax.annotation.Nullable
  public HardwareAccelerationType getHardwareAccelerationType() {
    return hardwareAccelerationType;
  }

  public void setHardwareAccelerationType(@javax.annotation.Nullable HardwareAccelerationType hardwareAccelerationType) {
    this.hardwareAccelerationType = hardwareAccelerationType;
  }


  public TranscodingInfo transcodeReasons(@javax.annotation.Nullable TranscodeReasonsEnum transcodeReasons) {
    this.transcodeReasons = transcodeReasons;
    return this;
  }

  public TranscodingInfo addTranscodeReasonsItem(TranscodeReason transcodeReasonsItem) {
    if (this.transcodeReasons == null) {
      this.transcodeReasons = new ArrayList<>();
    }
    this.transcodeReasons.add(transcodeReasonsItem);
    return this;
  }

  /**
   * Gets or sets the transcode reasons.
   * @return transcodeReasons
   */
  @javax.annotation.Nullable
  public TranscodeReasonsEnum getTranscodeReasons() {
    return transcodeReasons;
  }

  public void setTranscodeReasons(@javax.annotation.Nullable TranscodeReasonsEnum transcodeReasons) {
    this.transcodeReasons = transcodeReasons;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranscodingInfo transcodingInfo = (TranscodingInfo) o;
    return Objects.equals(this.audioCodec, transcodingInfo.audioCodec) &&
        Objects.equals(this.videoCodec, transcodingInfo.videoCodec) &&
        Objects.equals(this.container, transcodingInfo.container) &&
        Objects.equals(this.isVideoDirect, transcodingInfo.isVideoDirect) &&
        Objects.equals(this.isAudioDirect, transcodingInfo.isAudioDirect) &&
        Objects.equals(this.bitrate, transcodingInfo.bitrate) &&
        Objects.equals(this.framerate, transcodingInfo.framerate) &&
        Objects.equals(this.completionPercentage, transcodingInfo.completionPercentage) &&
        Objects.equals(this.width, transcodingInfo.width) &&
        Objects.equals(this.height, transcodingInfo.height) &&
        Objects.equals(this.audioChannels, transcodingInfo.audioChannels) &&
        Objects.equals(this.hardwareAccelerationType, transcodingInfo.hardwareAccelerationType) &&
        Objects.equals(this.transcodeReasons, transcodingInfo.transcodeReasons);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(audioCodec, videoCodec, container, isVideoDirect, isAudioDirect, bitrate, framerate, completionPercentage, width, height, audioChannels, hardwareAccelerationType, transcodeReasons);
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
    sb.append("class TranscodingInfo {\n");
    sb.append("    audioCodec: ").append(toIndentedString(audioCodec)).append("\n");
    sb.append("    videoCodec: ").append(toIndentedString(videoCodec)).append("\n");
    sb.append("    container: ").append(toIndentedString(container)).append("\n");
    sb.append("    isVideoDirect: ").append(toIndentedString(isVideoDirect)).append("\n");
    sb.append("    isAudioDirect: ").append(toIndentedString(isAudioDirect)).append("\n");
    sb.append("    bitrate: ").append(toIndentedString(bitrate)).append("\n");
    sb.append("    framerate: ").append(toIndentedString(framerate)).append("\n");
    sb.append("    completionPercentage: ").append(toIndentedString(completionPercentage)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    audioChannels: ").append(toIndentedString(audioChannels)).append("\n");
    sb.append("    hardwareAccelerationType: ").append(toIndentedString(hardwareAccelerationType)).append("\n");
    sb.append("    transcodeReasons: ").append(toIndentedString(transcodeReasons)).append("\n");
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
    openapiFields.add("AudioCodec");
    openapiFields.add("VideoCodec");
    openapiFields.add("Container");
    openapiFields.add("IsVideoDirect");
    openapiFields.add("IsAudioDirect");
    openapiFields.add("Bitrate");
    openapiFields.add("Framerate");
    openapiFields.add("CompletionPercentage");
    openapiFields.add("Width");
    openapiFields.add("Height");
    openapiFields.add("AudioChannels");
    openapiFields.add("HardwareAccelerationType");
    openapiFields.add("TranscodeReasons");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to TranscodingInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!TranscodingInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in TranscodingInfo is not found in the empty JSON string", TranscodingInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!TranscodingInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `TranscodingInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("AudioCodec") != null && !jsonObj.get("AudioCodec").isJsonNull()) && !jsonObj.get("AudioCodec").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `AudioCodec` to be a primitive type in the JSON string but got `%s`", jsonObj.get("AudioCodec").toString()));
      }
      if ((jsonObj.get("VideoCodec") != null && !jsonObj.get("VideoCodec").isJsonNull()) && !jsonObj.get("VideoCodec").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `VideoCodec` to be a primitive type in the JSON string but got `%s`", jsonObj.get("VideoCodec").toString()));
      }
      if ((jsonObj.get("Container") != null && !jsonObj.get("Container").isJsonNull()) && !jsonObj.get("Container").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Container` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Container").toString()));
      }
      // validate the optional field `HardwareAccelerationType`
      if (jsonObj.get("HardwareAccelerationType") != null && !jsonObj.get("HardwareAccelerationType").isJsonNull()) {
        HardwareAccelerationType.validateJsonElement(jsonObj.get("HardwareAccelerationType"));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("TranscodeReasons") != null && !jsonObj.get("TranscodeReasons").isJsonNull() && !jsonObj.get("TranscodeReasons").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `TranscodeReasons` to be an array in the JSON string but got `%s`", jsonObj.get("TranscodeReasons").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!TranscodingInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'TranscodingInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<TranscodingInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(TranscodingInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<TranscodingInfo>() {
           @Override
           public void write(JsonWriter out, TranscodingInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public TranscodingInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of TranscodingInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of TranscodingInfo
   * @throws IOException if the JSON string is invalid with respect to TranscodingInfo
   */
  public static TranscodingInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, TranscodingInfo.class);
  }

  /**
   * Convert an instance of TranscodingInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

