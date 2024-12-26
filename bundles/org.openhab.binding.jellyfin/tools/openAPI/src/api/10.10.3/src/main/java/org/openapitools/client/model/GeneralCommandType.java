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
 * This exists simply to identify a set of known commands.
 */
@JsonAdapter(GeneralCommandType.Adapter.class)
public enum GeneralCommandType {
  
  MOVE_UP("MoveUp"),
  
  MOVE_DOWN("MoveDown"),
  
  MOVE_LEFT("MoveLeft"),
  
  MOVE_RIGHT("MoveRight"),
  
  PAGE_UP("PageUp"),
  
  PAGE_DOWN("PageDown"),
  
  PREVIOUS_LETTER("PreviousLetter"),
  
  NEXT_LETTER("NextLetter"),
  
  TOGGLE_OSD("ToggleOsd"),
  
  TOGGLE_CONTEXT_MENU("ToggleContextMenu"),
  
  SELECT("Select"),
  
  BACK("Back"),
  
  TAKE_SCREENSHOT("TakeScreenshot"),
  
  SEND_KEY("SendKey"),
  
  SEND_STRING("SendString"),
  
  GO_HOME("GoHome"),
  
  GO_TO_SETTINGS("GoToSettings"),
  
  VOLUME_UP("VolumeUp"),
  
  VOLUME_DOWN("VolumeDown"),
  
  MUTE("Mute"),
  
  UNMUTE("Unmute"),
  
  TOGGLE_MUTE("ToggleMute"),
  
  SET_VOLUME("SetVolume"),
  
  SET_AUDIO_STREAM_INDEX("SetAudioStreamIndex"),
  
  SET_SUBTITLE_STREAM_INDEX("SetSubtitleStreamIndex"),
  
  TOGGLE_FULLSCREEN("ToggleFullscreen"),
  
  DISPLAY_CONTENT("DisplayContent"),
  
  GO_TO_SEARCH("GoToSearch"),
  
  DISPLAY_MESSAGE("DisplayMessage"),
  
  SET_REPEAT_MODE("SetRepeatMode"),
  
  CHANNEL_UP("ChannelUp"),
  
  CHANNEL_DOWN("ChannelDown"),
  
  GUIDE("Guide"),
  
  TOGGLE_STATS("ToggleStats"),
  
  PLAY_MEDIA_SOURCE("PlayMediaSource"),
  
  PLAY_TRAILERS("PlayTrailers"),
  
  SET_SHUFFLE_QUEUE("SetShuffleQueue"),
  
  PLAY_STATE("PlayState"),
  
  PLAY_NEXT("PlayNext"),
  
  TOGGLE_OSD_MENU("ToggleOsdMenu"),
  
  PLAY("Play"),
  
  SET_MAX_STREAMING_BITRATE("SetMaxStreamingBitrate");

  private String value;

  GeneralCommandType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static GeneralCommandType fromValue(String value) {
    for (GeneralCommandType b : GeneralCommandType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<GeneralCommandType> {
    @Override
    public void write(final JsonWriter jsonWriter, final GeneralCommandType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public GeneralCommandType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return GeneralCommandType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    GeneralCommandType.fromValue(value);
  }
}

