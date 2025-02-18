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
import java.util.Arrays;
import org.openapitools.client.model.SubtitleDeliveryMethod;
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
 * A class for subtitle profile information.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class SubtitleProfile {
  public static final String SERIALIZED_NAME_FORMAT = "Format";
  @SerializedName(SERIALIZED_NAME_FORMAT)
  @javax.annotation.Nullable
  private String format;

  public static final String SERIALIZED_NAME_METHOD = "Method";
  @SerializedName(SERIALIZED_NAME_METHOD)
  @javax.annotation.Nullable
  private SubtitleDeliveryMethod method;

  public static final String SERIALIZED_NAME_DIDL_MODE = "DidlMode";
  @SerializedName(SERIALIZED_NAME_DIDL_MODE)
  @javax.annotation.Nullable
  private String didlMode;

  public static final String SERIALIZED_NAME_LANGUAGE = "Language";
  @SerializedName(SERIALIZED_NAME_LANGUAGE)
  @javax.annotation.Nullable
  private String language;

  public static final String SERIALIZED_NAME_CONTAINER = "Container";
  @SerializedName(SERIALIZED_NAME_CONTAINER)
  @javax.annotation.Nullable
  private String container;

  public SubtitleProfile() {
  }

  public SubtitleProfile format(@javax.annotation.Nullable String format) {
    this.format = format;
    return this;
  }

  /**
   * Gets or sets the format.
   * @return format
   */
  @javax.annotation.Nullable
  public String getFormat() {
    return format;
  }

  public void setFormat(@javax.annotation.Nullable String format) {
    this.format = format;
  }


  public SubtitleProfile method(@javax.annotation.Nullable SubtitleDeliveryMethod method) {
    this.method = method;
    return this;
  }

  /**
   * Gets or sets the delivery method.
   * @return method
   */
  @javax.annotation.Nullable
  public SubtitleDeliveryMethod getMethod() {
    return method;
  }

  public void setMethod(@javax.annotation.Nullable SubtitleDeliveryMethod method) {
    this.method = method;
  }


  public SubtitleProfile didlMode(@javax.annotation.Nullable String didlMode) {
    this.didlMode = didlMode;
    return this;
  }

  /**
   * Gets or sets the DIDL mode.
   * @return didlMode
   */
  @javax.annotation.Nullable
  public String getDidlMode() {
    return didlMode;
  }

  public void setDidlMode(@javax.annotation.Nullable String didlMode) {
    this.didlMode = didlMode;
  }


  public SubtitleProfile language(@javax.annotation.Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Gets or sets the language.
   * @return language
   */
  @javax.annotation.Nullable
  public String getLanguage() {
    return language;
  }

  public void setLanguage(@javax.annotation.Nullable String language) {
    this.language = language;
  }


  public SubtitleProfile container(@javax.annotation.Nullable String container) {
    this.container = container;
    return this;
  }

  /**
   * Gets or sets the container.
   * @return container
   */
  @javax.annotation.Nullable
  public String getContainer() {
    return container;
  }

  public void setContainer(@javax.annotation.Nullable String container) {
    this.container = container;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubtitleProfile subtitleProfile = (SubtitleProfile) o;
    return Objects.equals(this.format, subtitleProfile.format) &&
        Objects.equals(this.method, subtitleProfile.method) &&
        Objects.equals(this.didlMode, subtitleProfile.didlMode) &&
        Objects.equals(this.language, subtitleProfile.language) &&
        Objects.equals(this.container, subtitleProfile.container);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(format, method, didlMode, language, container);
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
    sb.append("class SubtitleProfile {\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    didlMode: ").append(toIndentedString(didlMode)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    container: ").append(toIndentedString(container)).append("\n");
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
    openapiFields.add("Format");
    openapiFields.add("Method");
    openapiFields.add("DidlMode");
    openapiFields.add("Language");
    openapiFields.add("Container");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to SubtitleProfile
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!SubtitleProfile.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in SubtitleProfile is not found in the empty JSON string", SubtitleProfile.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!SubtitleProfile.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `SubtitleProfile` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Format") != null && !jsonObj.get("Format").isJsonNull()) && !jsonObj.get("Format").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Format` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Format").toString()));
      }
      // validate the optional field `Method`
      if (jsonObj.get("Method") != null && !jsonObj.get("Method").isJsonNull()) {
        SubtitleDeliveryMethod.validateJsonElement(jsonObj.get("Method"));
      }
      if ((jsonObj.get("DidlMode") != null && !jsonObj.get("DidlMode").isJsonNull()) && !jsonObj.get("DidlMode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `DidlMode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("DidlMode").toString()));
      }
      if ((jsonObj.get("Language") != null && !jsonObj.get("Language").isJsonNull()) && !jsonObj.get("Language").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Language` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Language").toString()));
      }
      if ((jsonObj.get("Container") != null && !jsonObj.get("Container").isJsonNull()) && !jsonObj.get("Container").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Container` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Container").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!SubtitleProfile.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'SubtitleProfile' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<SubtitleProfile> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(SubtitleProfile.class));

       return (TypeAdapter<T>) new TypeAdapter<SubtitleProfile>() {
           @Override
           public void write(JsonWriter out, SubtitleProfile value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public SubtitleProfile read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of SubtitleProfile given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of SubtitleProfile
   * @throws IOException if the JSON string is invalid with respect to SubtitleProfile
   */
  public static SubtitleProfile fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, SubtitleProfile.class);
  }

  /**
   * Convert an instance of SubtitleProfile to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

