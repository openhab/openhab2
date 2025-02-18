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
import org.openapitools.client.model.SessionInfo;
import org.openapitools.client.model.UserDto;
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
 * AuthenticationResult
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class AuthenticationResult {
  public static final String SERIALIZED_NAME_USER = "User";
  @SerializedName(SERIALIZED_NAME_USER)
  @javax.annotation.Nullable
  private UserDto user;

  public static final String SERIALIZED_NAME_SESSION_INFO = "SessionInfo";
  @SerializedName(SERIALIZED_NAME_SESSION_INFO)
  @javax.annotation.Nullable
  private SessionInfo sessionInfo;

  public static final String SERIALIZED_NAME_ACCESS_TOKEN = "AccessToken";
  @SerializedName(SERIALIZED_NAME_ACCESS_TOKEN)
  @javax.annotation.Nullable
  private String accessToken;

  public static final String SERIALIZED_NAME_SERVER_ID = "ServerId";
  @SerializedName(SERIALIZED_NAME_SERVER_ID)
  @javax.annotation.Nullable
  private String serverId;

  public AuthenticationResult() {
  }

  public AuthenticationResult user(@javax.annotation.Nullable UserDto user) {
    this.user = user;
    return this;
  }

  /**
   * Class UserDto.
   * @return user
   */
  @javax.annotation.Nullable
  public UserDto getUser() {
    return user;
  }

  public void setUser(@javax.annotation.Nullable UserDto user) {
    this.user = user;
  }


  public AuthenticationResult sessionInfo(@javax.annotation.Nullable SessionInfo sessionInfo) {
    this.sessionInfo = sessionInfo;
    return this;
  }

  /**
   * Class SessionInfo.
   * @return sessionInfo
   */
  @javax.annotation.Nullable
  public SessionInfo getSessionInfo() {
    return sessionInfo;
  }

  public void setSessionInfo(@javax.annotation.Nullable SessionInfo sessionInfo) {
    this.sessionInfo = sessionInfo;
  }


  public AuthenticationResult accessToken(@javax.annotation.Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Get accessToken
   * @return accessToken
   */
  @javax.annotation.Nullable
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(@javax.annotation.Nullable String accessToken) {
    this.accessToken = accessToken;
  }


  public AuthenticationResult serverId(@javax.annotation.Nullable String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * Get serverId
   * @return serverId
   */
  @javax.annotation.Nullable
  public String getServerId() {
    return serverId;
  }

  public void setServerId(@javax.annotation.Nullable String serverId) {
    this.serverId = serverId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthenticationResult authenticationResult = (AuthenticationResult) o;
    return Objects.equals(this.user, authenticationResult.user) &&
        Objects.equals(this.sessionInfo, authenticationResult.sessionInfo) &&
        Objects.equals(this.accessToken, authenticationResult.accessToken) &&
        Objects.equals(this.serverId, authenticationResult.serverId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, sessionInfo, accessToken, serverId);
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
    sb.append("class AuthenticationResult {\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    sessionInfo: ").append(toIndentedString(sessionInfo)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
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
    openapiFields.add("User");
    openapiFields.add("SessionInfo");
    openapiFields.add("AccessToken");
    openapiFields.add("ServerId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AuthenticationResult
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AuthenticationResult.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AuthenticationResult is not found in the empty JSON string", AuthenticationResult.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AuthenticationResult.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AuthenticationResult` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `User`
      if (jsonObj.get("User") != null && !jsonObj.get("User").isJsonNull()) {
        UserDto.validateJsonElement(jsonObj.get("User"));
      }
      // validate the optional field `SessionInfo`
      if (jsonObj.get("SessionInfo") != null && !jsonObj.get("SessionInfo").isJsonNull()) {
        SessionInfo.validateJsonElement(jsonObj.get("SessionInfo"));
      }
      if ((jsonObj.get("AccessToken") != null && !jsonObj.get("AccessToken").isJsonNull()) && !jsonObj.get("AccessToken").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `AccessToken` to be a primitive type in the JSON string but got `%s`", jsonObj.get("AccessToken").toString()));
      }
      if ((jsonObj.get("ServerId") != null && !jsonObj.get("ServerId").isJsonNull()) && !jsonObj.get("ServerId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ServerId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ServerId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AuthenticationResult.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AuthenticationResult' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AuthenticationResult> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AuthenticationResult.class));

       return (TypeAdapter<T>) new TypeAdapter<AuthenticationResult>() {
           @Override
           public void write(JsonWriter out, AuthenticationResult value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AuthenticationResult read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AuthenticationResult given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AuthenticationResult
   * @throws IOException if the JSON string is invalid with respect to AuthenticationResult
   */
  public static AuthenticationResult fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AuthenticationResult.class);
  }

  /**
   * Convert an instance of AuthenticationResult to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

