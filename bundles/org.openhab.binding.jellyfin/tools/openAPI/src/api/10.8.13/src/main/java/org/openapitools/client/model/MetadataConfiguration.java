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
 * MetadataConfiguration
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class MetadataConfiguration {
  public static final String SERIALIZED_NAME_USE_FILE_CREATION_TIME_FOR_DATE_ADDED = "UseFileCreationTimeForDateAdded";
  @SerializedName(SERIALIZED_NAME_USE_FILE_CREATION_TIME_FOR_DATE_ADDED)
  @javax.annotation.Nullable
  private Boolean useFileCreationTimeForDateAdded;

  public MetadataConfiguration() {
  }

  public MetadataConfiguration useFileCreationTimeForDateAdded(@javax.annotation.Nullable Boolean useFileCreationTimeForDateAdded) {
    this.useFileCreationTimeForDateAdded = useFileCreationTimeForDateAdded;
    return this;
  }

  /**
   * Get useFileCreationTimeForDateAdded
   * @return useFileCreationTimeForDateAdded
   */
  @javax.annotation.Nullable
  public Boolean getUseFileCreationTimeForDateAdded() {
    return useFileCreationTimeForDateAdded;
  }

  public void setUseFileCreationTimeForDateAdded(@javax.annotation.Nullable Boolean useFileCreationTimeForDateAdded) {
    this.useFileCreationTimeForDateAdded = useFileCreationTimeForDateAdded;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataConfiguration metadataConfiguration = (MetadataConfiguration) o;
    return Objects.equals(this.useFileCreationTimeForDateAdded, metadataConfiguration.useFileCreationTimeForDateAdded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useFileCreationTimeForDateAdded);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetadataConfiguration {\n");
    sb.append("    useFileCreationTimeForDateAdded: ").append(toIndentedString(useFileCreationTimeForDateAdded)).append("\n");
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
    openapiFields.add("UseFileCreationTimeForDateAdded");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to MetadataConfiguration
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!MetadataConfiguration.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in MetadataConfiguration is not found in the empty JSON string", MetadataConfiguration.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!MetadataConfiguration.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `MetadataConfiguration` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!MetadataConfiguration.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'MetadataConfiguration' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<MetadataConfiguration> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(MetadataConfiguration.class));

       return (TypeAdapter<T>) new TypeAdapter<MetadataConfiguration>() {
           @Override
           public void write(JsonWriter out, MetadataConfiguration value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public MetadataConfiguration read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of MetadataConfiguration given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of MetadataConfiguration
   * @throws IOException if the JSON string is invalid with respect to MetadataConfiguration
   */
  public static MetadataConfiguration fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, MetadataConfiguration.class);
  }

  /**
   * Convert an instance of MetadataConfiguration to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

