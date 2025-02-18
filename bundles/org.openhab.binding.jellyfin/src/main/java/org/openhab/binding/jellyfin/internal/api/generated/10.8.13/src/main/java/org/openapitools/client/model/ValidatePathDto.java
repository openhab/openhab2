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
 * Validate path object.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ValidatePathDto {
  public static final String SERIALIZED_NAME_VALIDATE_WRITABLE = "ValidateWritable";
  @SerializedName(SERIALIZED_NAME_VALIDATE_WRITABLE)
  @javax.annotation.Nullable
  private Boolean validateWritable;

  public static final String SERIALIZED_NAME_PATH = "Path";
  @SerializedName(SERIALIZED_NAME_PATH)
  @javax.annotation.Nullable
  private String path;

  public static final String SERIALIZED_NAME_IS_FILE = "IsFile";
  @SerializedName(SERIALIZED_NAME_IS_FILE)
  @javax.annotation.Nullable
  private Boolean isFile;

  public ValidatePathDto() {
  }

  public ValidatePathDto validateWritable(@javax.annotation.Nullable Boolean validateWritable) {
    this.validateWritable = validateWritable;
    return this;
  }

  /**
   * Gets or sets a value indicating whether validate if path is writable.
   * @return validateWritable
   */
  @javax.annotation.Nullable
  public Boolean getValidateWritable() {
    return validateWritable;
  }

  public void setValidateWritable(@javax.annotation.Nullable Boolean validateWritable) {
    this.validateWritable = validateWritable;
  }


  public ValidatePathDto path(@javax.annotation.Nullable String path) {
    this.path = path;
    return this;
  }

  /**
   * Gets or sets the path.
   * @return path
   */
  @javax.annotation.Nullable
  public String getPath() {
    return path;
  }

  public void setPath(@javax.annotation.Nullable String path) {
    this.path = path;
  }


  public ValidatePathDto isFile(@javax.annotation.Nullable Boolean isFile) {
    this.isFile = isFile;
    return this;
  }

  /**
   * Gets or sets is path file.
   * @return isFile
   */
  @javax.annotation.Nullable
  public Boolean getIsFile() {
    return isFile;
  }

  public void setIsFile(@javax.annotation.Nullable Boolean isFile) {
    this.isFile = isFile;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidatePathDto validatePathDto = (ValidatePathDto) o;
    return Objects.equals(this.validateWritable, validatePathDto.validateWritable) &&
        Objects.equals(this.path, validatePathDto.path) &&
        Objects.equals(this.isFile, validatePathDto.isFile);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(validateWritable, path, isFile);
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
    sb.append("class ValidatePathDto {\n");
    sb.append("    validateWritable: ").append(toIndentedString(validateWritable)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    isFile: ").append(toIndentedString(isFile)).append("\n");
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
    openapiFields.add("ValidateWritable");
    openapiFields.add("Path");
    openapiFields.add("IsFile");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ValidatePathDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ValidatePathDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ValidatePathDto is not found in the empty JSON string", ValidatePathDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ValidatePathDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ValidatePathDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Path") != null && !jsonObj.get("Path").isJsonNull()) && !jsonObj.get("Path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Path").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ValidatePathDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ValidatePathDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ValidatePathDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ValidatePathDto.class));

       return (TypeAdapter<T>) new TypeAdapter<ValidatePathDto>() {
           @Override
           public void write(JsonWriter out, ValidatePathDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ValidatePathDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ValidatePathDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ValidatePathDto
   * @throws IOException if the JSON string is invalid with respect to ValidatePathDto
   */
  public static ValidatePathDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ValidatePathDto.class);
  }

  /**
   * Convert an instance of ValidatePathDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

