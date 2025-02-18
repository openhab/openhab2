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
import org.openapitools.client.model.ThemeMediaResult;
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
 * AllThemeMediaResult
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class AllThemeMediaResult {
  public static final String SERIALIZED_NAME_THEME_VIDEOS_RESULT = "ThemeVideosResult";
  @SerializedName(SERIALIZED_NAME_THEME_VIDEOS_RESULT)
  @javax.annotation.Nullable
  private ThemeMediaResult themeVideosResult;

  public static final String SERIALIZED_NAME_THEME_SONGS_RESULT = "ThemeSongsResult";
  @SerializedName(SERIALIZED_NAME_THEME_SONGS_RESULT)
  @javax.annotation.Nullable
  private ThemeMediaResult themeSongsResult;

  public static final String SERIALIZED_NAME_SOUNDTRACK_SONGS_RESULT = "SoundtrackSongsResult";
  @SerializedName(SERIALIZED_NAME_SOUNDTRACK_SONGS_RESULT)
  @javax.annotation.Nullable
  private ThemeMediaResult soundtrackSongsResult;

  public AllThemeMediaResult() {
  }

  public AllThemeMediaResult themeVideosResult(@javax.annotation.Nullable ThemeMediaResult themeVideosResult) {
    this.themeVideosResult = themeVideosResult;
    return this;
  }

  /**
   * Class ThemeMediaResult.
   * @return themeVideosResult
   */
  @javax.annotation.Nullable
  public ThemeMediaResult getThemeVideosResult() {
    return themeVideosResult;
  }

  public void setThemeVideosResult(@javax.annotation.Nullable ThemeMediaResult themeVideosResult) {
    this.themeVideosResult = themeVideosResult;
  }


  public AllThemeMediaResult themeSongsResult(@javax.annotation.Nullable ThemeMediaResult themeSongsResult) {
    this.themeSongsResult = themeSongsResult;
    return this;
  }

  /**
   * Class ThemeMediaResult.
   * @return themeSongsResult
   */
  @javax.annotation.Nullable
  public ThemeMediaResult getThemeSongsResult() {
    return themeSongsResult;
  }

  public void setThemeSongsResult(@javax.annotation.Nullable ThemeMediaResult themeSongsResult) {
    this.themeSongsResult = themeSongsResult;
  }


  public AllThemeMediaResult soundtrackSongsResult(@javax.annotation.Nullable ThemeMediaResult soundtrackSongsResult) {
    this.soundtrackSongsResult = soundtrackSongsResult;
    return this;
  }

  /**
   * Class ThemeMediaResult.
   * @return soundtrackSongsResult
   */
  @javax.annotation.Nullable
  public ThemeMediaResult getSoundtrackSongsResult() {
    return soundtrackSongsResult;
  }

  public void setSoundtrackSongsResult(@javax.annotation.Nullable ThemeMediaResult soundtrackSongsResult) {
    this.soundtrackSongsResult = soundtrackSongsResult;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllThemeMediaResult allThemeMediaResult = (AllThemeMediaResult) o;
    return Objects.equals(this.themeVideosResult, allThemeMediaResult.themeVideosResult) &&
        Objects.equals(this.themeSongsResult, allThemeMediaResult.themeSongsResult) &&
        Objects.equals(this.soundtrackSongsResult, allThemeMediaResult.soundtrackSongsResult);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(themeVideosResult, themeSongsResult, soundtrackSongsResult);
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
    sb.append("class AllThemeMediaResult {\n");
    sb.append("    themeVideosResult: ").append(toIndentedString(themeVideosResult)).append("\n");
    sb.append("    themeSongsResult: ").append(toIndentedString(themeSongsResult)).append("\n");
    sb.append("    soundtrackSongsResult: ").append(toIndentedString(soundtrackSongsResult)).append("\n");
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
    openapiFields.add("ThemeVideosResult");
    openapiFields.add("ThemeSongsResult");
    openapiFields.add("SoundtrackSongsResult");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AllThemeMediaResult
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AllThemeMediaResult.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AllThemeMediaResult is not found in the empty JSON string", AllThemeMediaResult.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AllThemeMediaResult.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AllThemeMediaResult` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `ThemeVideosResult`
      if (jsonObj.get("ThemeVideosResult") != null && !jsonObj.get("ThemeVideosResult").isJsonNull()) {
        ThemeMediaResult.validateJsonElement(jsonObj.get("ThemeVideosResult"));
      }
      // validate the optional field `ThemeSongsResult`
      if (jsonObj.get("ThemeSongsResult") != null && !jsonObj.get("ThemeSongsResult").isJsonNull()) {
        ThemeMediaResult.validateJsonElement(jsonObj.get("ThemeSongsResult"));
      }
      // validate the optional field `SoundtrackSongsResult`
      if (jsonObj.get("SoundtrackSongsResult") != null && !jsonObj.get("SoundtrackSongsResult").isJsonNull()) {
        ThemeMediaResult.validateJsonElement(jsonObj.get("SoundtrackSongsResult"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AllThemeMediaResult.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AllThemeMediaResult' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AllThemeMediaResult> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AllThemeMediaResult.class));

       return (TypeAdapter<T>) new TypeAdapter<AllThemeMediaResult>() {
           @Override
           public void write(JsonWriter out, AllThemeMediaResult value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AllThemeMediaResult read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AllThemeMediaResult given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AllThemeMediaResult
   * @throws IOException if the JSON string is invalid with respect to AllThemeMediaResult
   */
  public static AllThemeMediaResult fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AllThemeMediaResult.class);
  }

  /**
   * Convert an instance of AllThemeMediaResult to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

