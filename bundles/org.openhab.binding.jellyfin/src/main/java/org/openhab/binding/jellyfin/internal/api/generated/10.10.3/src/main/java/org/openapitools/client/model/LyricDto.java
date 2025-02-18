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
import org.openapitools.client.model.LyricLine;
import org.openapitools.client.model.LyricMetadata;

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
 * LyricResponse model.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class LyricDto {
  public static final String SERIALIZED_NAME_METADATA = "Metadata";
  @SerializedName(SERIALIZED_NAME_METADATA)
  @javax.annotation.Nullable
  private LyricMetadata metadata;

  public static final String SERIALIZED_NAME_LYRICS = "Lyrics";
  @SerializedName(SERIALIZED_NAME_LYRICS)
  @javax.annotation.Nullable
  private List<LyricLine> lyrics = new ArrayList<>();

  public LyricDto() {
  }

  public LyricDto metadata(@javax.annotation.Nullable LyricMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Gets or sets Metadata for the lyrics.
   * @return metadata
   */
  @javax.annotation.Nullable
  public LyricMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(@javax.annotation.Nullable LyricMetadata metadata) {
    this.metadata = metadata;
  }


  public LyricDto lyrics(@javax.annotation.Nullable List<LyricLine> lyrics) {
    this.lyrics = lyrics;
    return this;
  }

  public LyricDto addLyricsItem(LyricLine lyricsItem) {
    if (this.lyrics == null) {
      this.lyrics = new ArrayList<>();
    }
    this.lyrics.add(lyricsItem);
    return this;
  }

  /**
   * Gets or sets a collection of individual lyric lines.
   * @return lyrics
   */
  @javax.annotation.Nullable
  public List<LyricLine> getLyrics() {
    return lyrics;
  }

  public void setLyrics(@javax.annotation.Nullable List<LyricLine> lyrics) {
    this.lyrics = lyrics;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LyricDto lyricDto = (LyricDto) o;
    return Objects.equals(this.metadata, lyricDto.metadata) &&
        Objects.equals(this.lyrics, lyricDto.lyrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadata, lyrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LyricDto {\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    lyrics: ").append(toIndentedString(lyrics)).append("\n");
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
    openapiFields.add("Metadata");
    openapiFields.add("Lyrics");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to LyricDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!LyricDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in LyricDto is not found in the empty JSON string", LyricDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!LyricDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `LyricDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `Metadata`
      if (jsonObj.get("Metadata") != null && !jsonObj.get("Metadata").isJsonNull()) {
        LyricMetadata.validateJsonElement(jsonObj.get("Metadata"));
      }
      if (jsonObj.get("Lyrics") != null && !jsonObj.get("Lyrics").isJsonNull()) {
        JsonArray jsonArraylyrics = jsonObj.getAsJsonArray("Lyrics");
        if (jsonArraylyrics != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Lyrics").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Lyrics` to be an array in the JSON string but got `%s`", jsonObj.get("Lyrics").toString()));
          }

          // validate the optional field `Lyrics` (array)
          for (int i = 0; i < jsonArraylyrics.size(); i++) {
            LyricLine.validateJsonElement(jsonArraylyrics.get(i));
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!LyricDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'LyricDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<LyricDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(LyricDto.class));

       return (TypeAdapter<T>) new TypeAdapter<LyricDto>() {
           @Override
           public void write(JsonWriter out, LyricDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public LyricDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of LyricDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of LyricDto
   * @throws IOException if the JSON string is invalid with respect to LyricDto
   */
  public static LyricDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, LyricDto.class);
  }

  /**
   * Convert an instance of LyricDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

