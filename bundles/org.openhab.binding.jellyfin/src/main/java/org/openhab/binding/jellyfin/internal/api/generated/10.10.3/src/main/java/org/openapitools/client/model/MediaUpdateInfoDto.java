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
import org.openapitools.client.model.MediaUpdateInfoPathDto;

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
 * Media Update Info Dto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class MediaUpdateInfoDto {
  public static final String SERIALIZED_NAME_UPDATES = "Updates";
  @SerializedName(SERIALIZED_NAME_UPDATES)
  @javax.annotation.Nullable
  private List<MediaUpdateInfoPathDto> updates = new ArrayList<>();

  public MediaUpdateInfoDto() {
  }

  public MediaUpdateInfoDto updates(@javax.annotation.Nullable List<MediaUpdateInfoPathDto> updates) {
    this.updates = updates;
    return this;
  }

  public MediaUpdateInfoDto addUpdatesItem(MediaUpdateInfoPathDto updatesItem) {
    if (this.updates == null) {
      this.updates = new ArrayList<>();
    }
    this.updates.add(updatesItem);
    return this;
  }

  /**
   * Gets or sets the list of updates.
   * @return updates
   */
  @javax.annotation.Nullable
  public List<MediaUpdateInfoPathDto> getUpdates() {
    return updates;
  }

  public void setUpdates(@javax.annotation.Nullable List<MediaUpdateInfoPathDto> updates) {
    this.updates = updates;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaUpdateInfoDto mediaUpdateInfoDto = (MediaUpdateInfoDto) o;
    return Objects.equals(this.updates, mediaUpdateInfoDto.updates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaUpdateInfoDto {\n");
    sb.append("    updates: ").append(toIndentedString(updates)).append("\n");
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
    openapiFields.add("Updates");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to MediaUpdateInfoDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!MediaUpdateInfoDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in MediaUpdateInfoDto is not found in the empty JSON string", MediaUpdateInfoDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!MediaUpdateInfoDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `MediaUpdateInfoDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if (jsonObj.get("Updates") != null && !jsonObj.get("Updates").isJsonNull()) {
        JsonArray jsonArrayupdates = jsonObj.getAsJsonArray("Updates");
        if (jsonArrayupdates != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Updates").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Updates` to be an array in the JSON string but got `%s`", jsonObj.get("Updates").toString()));
          }

          // validate the optional field `Updates` (array)
          for (int i = 0; i < jsonArrayupdates.size(); i++) {
            MediaUpdateInfoPathDto.validateJsonElement(jsonArrayupdates.get(i));
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!MediaUpdateInfoDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'MediaUpdateInfoDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<MediaUpdateInfoDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(MediaUpdateInfoDto.class));

       return (TypeAdapter<T>) new TypeAdapter<MediaUpdateInfoDto>() {
           @Override
           public void write(JsonWriter out, MediaUpdateInfoDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public MediaUpdateInfoDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of MediaUpdateInfoDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of MediaUpdateInfoDto
   * @throws IOException if the JSON string is invalid with respect to MediaUpdateInfoDto
   */
  public static MediaUpdateInfoDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, MediaUpdateInfoDto.class);
  }

  /**
   * Convert an instance of MediaUpdateInfoDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

