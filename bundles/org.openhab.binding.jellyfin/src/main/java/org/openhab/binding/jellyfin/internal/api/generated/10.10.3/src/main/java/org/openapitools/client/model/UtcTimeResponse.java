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
import java.time.OffsetDateTime;
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
 * Class UtcTimeResponse.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class UtcTimeResponse {
  public static final String SERIALIZED_NAME_REQUEST_RECEPTION_TIME = "RequestReceptionTime";
  @SerializedName(SERIALIZED_NAME_REQUEST_RECEPTION_TIME)
  @javax.annotation.Nullable
  private OffsetDateTime requestReceptionTime;

  public static final String SERIALIZED_NAME_RESPONSE_TRANSMISSION_TIME = "ResponseTransmissionTime";
  @SerializedName(SERIALIZED_NAME_RESPONSE_TRANSMISSION_TIME)
  @javax.annotation.Nullable
  private OffsetDateTime responseTransmissionTime;

  public UtcTimeResponse() {
  }

  public UtcTimeResponse requestReceptionTime(@javax.annotation.Nullable OffsetDateTime requestReceptionTime) {
    this.requestReceptionTime = requestReceptionTime;
    return this;
  }

  /**
   * Gets the UTC time when request has been received.
   * @return requestReceptionTime
   */
  @javax.annotation.Nullable
  public OffsetDateTime getRequestReceptionTime() {
    return requestReceptionTime;
  }

  public void setRequestReceptionTime(@javax.annotation.Nullable OffsetDateTime requestReceptionTime) {
    this.requestReceptionTime = requestReceptionTime;
  }


  public UtcTimeResponse responseTransmissionTime(@javax.annotation.Nullable OffsetDateTime responseTransmissionTime) {
    this.responseTransmissionTime = responseTransmissionTime;
    return this;
  }

  /**
   * Gets the UTC time when response has been sent.
   * @return responseTransmissionTime
   */
  @javax.annotation.Nullable
  public OffsetDateTime getResponseTransmissionTime() {
    return responseTransmissionTime;
  }

  public void setResponseTransmissionTime(@javax.annotation.Nullable OffsetDateTime responseTransmissionTime) {
    this.responseTransmissionTime = responseTransmissionTime;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtcTimeResponse utcTimeResponse = (UtcTimeResponse) o;
    return Objects.equals(this.requestReceptionTime, utcTimeResponse.requestReceptionTime) &&
        Objects.equals(this.responseTransmissionTime, utcTimeResponse.responseTransmissionTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestReceptionTime, responseTransmissionTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtcTimeResponse {\n");
    sb.append("    requestReceptionTime: ").append(toIndentedString(requestReceptionTime)).append("\n");
    sb.append("    responseTransmissionTime: ").append(toIndentedString(responseTransmissionTime)).append("\n");
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
    openapiFields.add("RequestReceptionTime");
    openapiFields.add("ResponseTransmissionTime");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to UtcTimeResponse
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!UtcTimeResponse.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in UtcTimeResponse is not found in the empty JSON string", UtcTimeResponse.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!UtcTimeResponse.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `UtcTimeResponse` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!UtcTimeResponse.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'UtcTimeResponse' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<UtcTimeResponse> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(UtcTimeResponse.class));

       return (TypeAdapter<T>) new TypeAdapter<UtcTimeResponse>() {
           @Override
           public void write(JsonWriter out, UtcTimeResponse value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public UtcTimeResponse read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of UtcTimeResponse given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of UtcTimeResponse
   * @throws IOException if the JSON string is invalid with respect to UtcTimeResponse
   */
  public static UtcTimeResponse fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, UtcTimeResponse.class);
  }

  /**
   * Convert an instance of UtcTimeResponse to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

