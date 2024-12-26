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
import java.util.UUID;
import org.openapitools.client.model.DynamicDayOfWeek;

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
 * An entity representing a user&#39;s access schedule.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class AccessSchedule {
  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private Integer id;

  public static final String SERIALIZED_NAME_USER_ID = "UserId";
  @SerializedName(SERIALIZED_NAME_USER_ID)
  @javax.annotation.Nullable
  private UUID userId;

  public static final String SERIALIZED_NAME_DAY_OF_WEEK = "DayOfWeek";
  @SerializedName(SERIALIZED_NAME_DAY_OF_WEEK)
  @javax.annotation.Nullable
  private DynamicDayOfWeek dayOfWeek;

  public static final String SERIALIZED_NAME_START_HOUR = "StartHour";
  @SerializedName(SERIALIZED_NAME_START_HOUR)
  @javax.annotation.Nullable
  private Double startHour;

  public static final String SERIALIZED_NAME_END_HOUR = "EndHour";
  @SerializedName(SERIALIZED_NAME_END_HOUR)
  @javax.annotation.Nullable
  private Double endHour;

  public AccessSchedule() {
  }

  public AccessSchedule(
     Integer id
  ) {
    this();
    this.id = id;
  }

  /**
   * Gets the id of this instance.
   * @return id
   */
  @javax.annotation.Nullable
  public Integer getId() {
    return id;
  }



  public AccessSchedule userId(@javax.annotation.Nullable UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Gets the id of the associated user.
   * @return userId
   */
  @javax.annotation.Nullable
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(@javax.annotation.Nullable UUID userId) {
    this.userId = userId;
  }


  public AccessSchedule dayOfWeek(@javax.annotation.Nullable DynamicDayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    return this;
  }

  /**
   * Gets or sets the day of week.
   * @return dayOfWeek
   */
  @javax.annotation.Nullable
  public DynamicDayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(@javax.annotation.Nullable DynamicDayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }


  public AccessSchedule startHour(@javax.annotation.Nullable Double startHour) {
    this.startHour = startHour;
    return this;
  }

  /**
   * Gets or sets the start hour.
   * @return startHour
   */
  @javax.annotation.Nullable
  public Double getStartHour() {
    return startHour;
  }

  public void setStartHour(@javax.annotation.Nullable Double startHour) {
    this.startHour = startHour;
  }


  public AccessSchedule endHour(@javax.annotation.Nullable Double endHour) {
    this.endHour = endHour;
    return this;
  }

  /**
   * Gets or sets the end hour.
   * @return endHour
   */
  @javax.annotation.Nullable
  public Double getEndHour() {
    return endHour;
  }

  public void setEndHour(@javax.annotation.Nullable Double endHour) {
    this.endHour = endHour;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccessSchedule accessSchedule = (AccessSchedule) o;
    return Objects.equals(this.id, accessSchedule.id) &&
        Objects.equals(this.userId, accessSchedule.userId) &&
        Objects.equals(this.dayOfWeek, accessSchedule.dayOfWeek) &&
        Objects.equals(this.startHour, accessSchedule.startHour) &&
        Objects.equals(this.endHour, accessSchedule.endHour);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, dayOfWeek, startHour, endHour);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccessSchedule {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    dayOfWeek: ").append(toIndentedString(dayOfWeek)).append("\n");
    sb.append("    startHour: ").append(toIndentedString(startHour)).append("\n");
    sb.append("    endHour: ").append(toIndentedString(endHour)).append("\n");
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
    openapiFields.add("Id");
    openapiFields.add("UserId");
    openapiFields.add("DayOfWeek");
    openapiFields.add("StartHour");
    openapiFields.add("EndHour");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to AccessSchedule
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!AccessSchedule.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AccessSchedule is not found in the empty JSON string", AccessSchedule.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!AccessSchedule.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AccessSchedule` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("UserId") != null && !jsonObj.get("UserId").isJsonNull()) && !jsonObj.get("UserId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `UserId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("UserId").toString()));
      }
      // validate the optional field `DayOfWeek`
      if (jsonObj.get("DayOfWeek") != null && !jsonObj.get("DayOfWeek").isJsonNull()) {
        DynamicDayOfWeek.validateJsonElement(jsonObj.get("DayOfWeek"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AccessSchedule.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AccessSchedule' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AccessSchedule> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AccessSchedule.class));

       return (TypeAdapter<T>) new TypeAdapter<AccessSchedule>() {
           @Override
           public void write(JsonWriter out, AccessSchedule value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AccessSchedule read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of AccessSchedule given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of AccessSchedule
   * @throws IOException if the JSON string is invalid with respect to AccessSchedule
   */
  public static AccessSchedule fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AccessSchedule.class);
  }

  /**
   * Convert an instance of AccessSchedule to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

