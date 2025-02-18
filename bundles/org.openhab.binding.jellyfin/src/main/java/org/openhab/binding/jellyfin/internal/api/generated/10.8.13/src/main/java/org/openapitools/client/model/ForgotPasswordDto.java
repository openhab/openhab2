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
 * Forgot Password request body DTO.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ForgotPasswordDto {
  public static final String SERIALIZED_NAME_ENTERED_USERNAME = "EnteredUsername";
  @SerializedName(SERIALIZED_NAME_ENTERED_USERNAME)
  @javax.annotation.Nonnull
  private String enteredUsername;

  public ForgotPasswordDto() {
  }

  public ForgotPasswordDto enteredUsername(@javax.annotation.Nonnull String enteredUsername) {
    this.enteredUsername = enteredUsername;
    return this;
  }

  /**
   * Gets or sets the entered username to have its password reset.
   * @return enteredUsername
   */
  @javax.annotation.Nonnull
  public String getEnteredUsername() {
    return enteredUsername;
  }

  public void setEnteredUsername(@javax.annotation.Nonnull String enteredUsername) {
    this.enteredUsername = enteredUsername;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForgotPasswordDto forgotPasswordDto = (ForgotPasswordDto) o;
    return Objects.equals(this.enteredUsername, forgotPasswordDto.enteredUsername);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enteredUsername);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForgotPasswordDto {\n");
    sb.append("    enteredUsername: ").append(toIndentedString(enteredUsername)).append("\n");
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
    openapiFields.add("EnteredUsername");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
    openapiRequiredFields.add("EnteredUsername");
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ForgotPasswordDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ForgotPasswordDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ForgotPasswordDto is not found in the empty JSON string", ForgotPasswordDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ForgotPasswordDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ForgotPasswordDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }

      // check to make sure all required properties/fields are present in the JSON string
      for (String requiredField : ForgotPasswordDto.openapiRequiredFields) {
        if (jsonElement.getAsJsonObject().get(requiredField) == null) {
          throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if (!jsonObj.get("EnteredUsername").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `EnteredUsername` to be a primitive type in the JSON string but got `%s`", jsonObj.get("EnteredUsername").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ForgotPasswordDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ForgotPasswordDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ForgotPasswordDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ForgotPasswordDto.class));

       return (TypeAdapter<T>) new TypeAdapter<ForgotPasswordDto>() {
           @Override
           public void write(JsonWriter out, ForgotPasswordDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ForgotPasswordDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ForgotPasswordDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ForgotPasswordDto
   * @throws IOException if the JSON string is invalid with respect to ForgotPasswordDto
   */
  public static ForgotPasswordDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ForgotPasswordDto.class);
  }

  /**
   * Convert an instance of ForgotPasswordDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

