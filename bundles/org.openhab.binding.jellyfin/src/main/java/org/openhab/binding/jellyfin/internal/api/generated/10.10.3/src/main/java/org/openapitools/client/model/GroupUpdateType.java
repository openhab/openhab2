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
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Enum GroupUpdateType.
 */
@JsonAdapter(GroupUpdateType.Adapter.class)
public enum GroupUpdateType {
  
  USER_JOINED("UserJoined"),
  
  USER_LEFT("UserLeft"),
  
  GROUP_JOINED("GroupJoined"),
  
  GROUP_LEFT("GroupLeft"),
  
  STATE_UPDATE("StateUpdate"),
  
  PLAY_QUEUE("PlayQueue"),
  
  NOT_IN_GROUP("NotInGroup"),
  
  GROUP_DOES_NOT_EXIST("GroupDoesNotExist"),
  
  CREATE_GROUP_DENIED("CreateGroupDenied"),
  
  JOIN_GROUP_DENIED("JoinGroupDenied"),
  
  LIBRARY_ACCESS_DENIED("LibraryAccessDenied");

  private String value;

  GroupUpdateType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static GroupUpdateType fromValue(String value) {
    for (GroupUpdateType b : GroupUpdateType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<GroupUpdateType> {
    @Override
    public void write(final JsonWriter jsonWriter, final GroupUpdateType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public GroupUpdateType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return GroupUpdateType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    GroupUpdateType.fromValue(value);
  }
}

