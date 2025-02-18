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
import org.openapitools.client.model.ImageType;

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
 * ImageOption
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ImageOption {
  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private ImageType type;

  public static final String SERIALIZED_NAME_LIMIT = "Limit";
  @SerializedName(SERIALIZED_NAME_LIMIT)
  @javax.annotation.Nullable
  private Integer limit;

  public static final String SERIALIZED_NAME_MIN_WIDTH = "MinWidth";
  @SerializedName(SERIALIZED_NAME_MIN_WIDTH)
  @javax.annotation.Nullable
  private Integer minWidth;

  public ImageOption() {
  }

  public ImageOption type(@javax.annotation.Nullable ImageType type) {
    this.type = type;
    return this;
  }

  /**
   * Gets or sets the type.
   * @return type
   */
  @javax.annotation.Nullable
  public ImageType getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable ImageType type) {
    this.type = type;
  }


  public ImageOption limit(@javax.annotation.Nullable Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Gets or sets the limit.
   * @return limit
   */
  @javax.annotation.Nullable
  public Integer getLimit() {
    return limit;
  }

  public void setLimit(@javax.annotation.Nullable Integer limit) {
    this.limit = limit;
  }


  public ImageOption minWidth(@javax.annotation.Nullable Integer minWidth) {
    this.minWidth = minWidth;
    return this;
  }

  /**
   * Gets or sets the minimum width.
   * @return minWidth
   */
  @javax.annotation.Nullable
  public Integer getMinWidth() {
    return minWidth;
  }

  public void setMinWidth(@javax.annotation.Nullable Integer minWidth) {
    this.minWidth = minWidth;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageOption imageOption = (ImageOption) o;
    return Objects.equals(this.type, imageOption.type) &&
        Objects.equals(this.limit, imageOption.limit) &&
        Objects.equals(this.minWidth, imageOption.minWidth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, limit, minWidth);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImageOption {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    minWidth: ").append(toIndentedString(minWidth)).append("\n");
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
    openapiFields.add("Type");
    openapiFields.add("Limit");
    openapiFields.add("MinWidth");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ImageOption
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ImageOption.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ImageOption is not found in the empty JSON string", ImageOption.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ImageOption.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ImageOption` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `Type`
      if (jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) {
        ImageType.validateJsonElement(jsonObj.get("Type"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ImageOption.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ImageOption' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ImageOption> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ImageOption.class));

       return (TypeAdapter<T>) new TypeAdapter<ImageOption>() {
           @Override
           public void write(JsonWriter out, ImageOption value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ImageOption read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ImageOption given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ImageOption
   * @throws IOException if the JSON string is invalid with respect to ImageOption
   */
  public static ImageOption fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ImageOption.class);
  }

  /**
   * Convert an instance of ImageOption to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

