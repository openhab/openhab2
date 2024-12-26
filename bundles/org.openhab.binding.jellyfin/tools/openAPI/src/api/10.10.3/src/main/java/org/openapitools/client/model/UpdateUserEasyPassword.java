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
 * The update user easy password request body.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class UpdateUserEasyPassword {
  public static final String SERIALIZED_NAME_NEW_PASSWORD = "NewPassword";
  @SerializedName(SERIALIZED_NAME_NEW_PASSWORD)
  @javax.annotation.Nullable
  private String newPassword;

  public static final String SERIALIZED_NAME_NEW_PW = "NewPw";
  @SerializedName(SERIALIZED_NAME_NEW_PW)
  @javax.annotation.Nullable
  private String newPw;

  public static final String SERIALIZED_NAME_RESET_PASSWORD = "ResetPassword";
  @SerializedName(SERIALIZED_NAME_RESET_PASSWORD)
  @javax.annotation.Nullable
  private Boolean resetPassword;

  public UpdateUserEasyPassword() {
  }

  public UpdateUserEasyPassword newPassword(@javax.annotation.Nullable String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Gets or sets the new sha1-hashed password.
   * @return newPassword
   */
  @javax.annotation.Nullable
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(@javax.annotation.Nullable String newPassword) {
    this.newPassword = newPassword;
  }


  public UpdateUserEasyPassword newPw(@javax.annotation.Nullable String newPw) {
    this.newPw = newPw;
    return this;
  }

  /**
   * Gets or sets the new password.
   * @return newPw
   */
  @javax.annotation.Nullable
  public String getNewPw() {
    return newPw;
  }

  public void setNewPw(@javax.annotation.Nullable String newPw) {
    this.newPw = newPw;
  }


  public UpdateUserEasyPassword resetPassword(@javax.annotation.Nullable Boolean resetPassword) {
    this.resetPassword = resetPassword;
    return this;
  }

  /**
   * Gets or sets a value indicating whether to reset the password.
   * @return resetPassword
   */
  @javax.annotation.Nullable
  public Boolean getResetPassword() {
    return resetPassword;
  }

  public void setResetPassword(@javax.annotation.Nullable Boolean resetPassword) {
    this.resetPassword = resetPassword;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateUserEasyPassword updateUserEasyPassword = (UpdateUserEasyPassword) o;
    return Objects.equals(this.newPassword, updateUserEasyPassword.newPassword) &&
        Objects.equals(this.newPw, updateUserEasyPassword.newPw) &&
        Objects.equals(this.resetPassword, updateUserEasyPassword.resetPassword);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPassword, newPw, resetPassword);
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
    sb.append("class UpdateUserEasyPassword {\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    newPw: ").append(toIndentedString(newPw)).append("\n");
    sb.append("    resetPassword: ").append(toIndentedString(resetPassword)).append("\n");
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
    openapiFields.add("NewPassword");
    openapiFields.add("NewPw");
    openapiFields.add("ResetPassword");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to UpdateUserEasyPassword
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!UpdateUserEasyPassword.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in UpdateUserEasyPassword is not found in the empty JSON string", UpdateUserEasyPassword.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!UpdateUserEasyPassword.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UpdateUserEasyPassword` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("NewPassword") != null && !jsonObj.get("NewPassword").isJsonNull()) && !jsonObj.get("NewPassword").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `NewPassword` to be a primitive type in the JSON string but got `%s`", jsonObj.get("NewPassword").toString()));
      }
      if ((jsonObj.get("NewPw") != null && !jsonObj.get("NewPw").isJsonNull()) && !jsonObj.get("NewPw").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `NewPw` to be a primitive type in the JSON string but got `%s`", jsonObj.get("NewPw").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UpdateUserEasyPassword.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UpdateUserEasyPassword' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UpdateUserEasyPassword> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UpdateUserEasyPassword.class));

       return (TypeAdapter<T>) new TypeAdapter<UpdateUserEasyPassword>() {
           @Override
           public void write(JsonWriter out, UpdateUserEasyPassword value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UpdateUserEasyPassword read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of UpdateUserEasyPassword given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of UpdateUserEasyPassword
   * @throws IOException if the JSON string is invalid with respect to UpdateUserEasyPassword
   */
  public static UpdateUserEasyPassword fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UpdateUserEasyPassword.class);
  }

  /**
   * Convert an instance of UpdateUserEasyPassword to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

