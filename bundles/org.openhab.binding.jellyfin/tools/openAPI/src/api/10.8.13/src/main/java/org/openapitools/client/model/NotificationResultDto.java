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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.client.model.NotificationDto;

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
 * A list of notifications with the total record count for pagination.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class NotificationResultDto {
  public static final String SERIALIZED_NAME_NOTIFICATIONS = "Notifications";
  @SerializedName(SERIALIZED_NAME_NOTIFICATIONS)
  @javax.annotation.Nullable
  private List<NotificationDto> notifications = new ArrayList<>();

  public static final String SERIALIZED_NAME_TOTAL_RECORD_COUNT = "TotalRecordCount";
  @SerializedName(SERIALIZED_NAME_TOTAL_RECORD_COUNT)
  @javax.annotation.Nullable
  private Integer totalRecordCount;

  public NotificationResultDto() {
  }

  public NotificationResultDto notifications(@javax.annotation.Nullable List<NotificationDto> notifications) {
    this.notifications = notifications;
    return this;
  }

  public NotificationResultDto addNotificationsItem(NotificationDto notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

  /**
   * Gets or sets the current page of notifications.
   * @return notifications
   */
  @javax.annotation.Nullable
  public List<NotificationDto> getNotifications() {
    return notifications;
  }

  public void setNotifications(@javax.annotation.Nullable List<NotificationDto> notifications) {
    this.notifications = notifications;
  }


  public NotificationResultDto totalRecordCount(@javax.annotation.Nullable Integer totalRecordCount) {
    this.totalRecordCount = totalRecordCount;
    return this;
  }

  /**
   * Gets or sets the total number of notifications.
   * @return totalRecordCount
   */
  @javax.annotation.Nullable
  public Integer getTotalRecordCount() {
    return totalRecordCount;
  }

  public void setTotalRecordCount(@javax.annotation.Nullable Integer totalRecordCount) {
    this.totalRecordCount = totalRecordCount;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationResultDto notificationResultDto = (NotificationResultDto) o;
    return Objects.equals(this.notifications, notificationResultDto.notifications) &&
        Objects.equals(this.totalRecordCount, notificationResultDto.totalRecordCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notifications, totalRecordCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationResultDto {\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
    sb.append("    totalRecordCount: ").append(toIndentedString(totalRecordCount)).append("\n");
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
    openapiFields.add("Notifications");
    openapiFields.add("TotalRecordCount");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to NotificationResultDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!NotificationResultDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in NotificationResultDto is not found in the empty JSON string", NotificationResultDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!NotificationResultDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `NotificationResultDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if (jsonObj.get("Notifications") != null && !jsonObj.get("Notifications").isJsonNull()) {
        JsonArray jsonArraynotifications = jsonObj.getAsJsonArray("Notifications");
        if (jsonArraynotifications != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Notifications").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Notifications` to be an array in the JSON string but got `%s`", jsonObj.get("Notifications").toString()));
          }

          // validate the optional field `Notifications` (array)
          for (int i = 0; i < jsonArraynotifications.size(); i++) {
            NotificationDto.validateJsonElement(jsonArraynotifications.get(i));
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!NotificationResultDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'NotificationResultDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<NotificationResultDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(NotificationResultDto.class));

       return (TypeAdapter<T>) new TypeAdapter<NotificationResultDto>() {
           @Override
           public void write(JsonWriter out, NotificationResultDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public NotificationResultDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of NotificationResultDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of NotificationResultDto
   * @throws IOException if the JSON string is invalid with respect to NotificationResultDto
   */
  public static NotificationResultDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, NotificationResultDto.class);
  }

  /**
   * Convert an instance of NotificationResultDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

