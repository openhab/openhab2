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
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.client.model.GroupInfoDto;
import org.openapitools.client.model.GroupUpdateType;

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
 * Class GroupUpdate.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class GroupInfoDtoGroupUpdate {
  public static final String SERIALIZED_NAME_GROUP_ID = "GroupId";
  @SerializedName(SERIALIZED_NAME_GROUP_ID)
  @javax.annotation.Nullable
  private UUID groupId;

  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private GroupUpdateType type;

  public static final String SERIALIZED_NAME_DATA = "Data";
  @SerializedName(SERIALIZED_NAME_DATA)
  @javax.annotation.Nullable
  private GroupInfoDto data;

  public GroupInfoDtoGroupUpdate() {
  }

  public GroupInfoDtoGroupUpdate(
     UUID groupId
  ) {
    this();
    this.groupId = groupId;
  }

  /**
   * Gets the group identifier.
   * @return groupId
   */
  @javax.annotation.Nullable
  public UUID getGroupId() {
    return groupId;
  }



  public GroupInfoDtoGroupUpdate type(@javax.annotation.Nullable GroupUpdateType type) {
    this.type = type;
    return this;
  }

  /**
   * Gets the update type.
   * @return type
   */
  @javax.annotation.Nullable
  public GroupUpdateType getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable GroupUpdateType type) {
    this.type = type;
  }


  public GroupInfoDtoGroupUpdate data(@javax.annotation.Nullable GroupInfoDto data) {
    this.data = data;
    return this;
  }

  /**
   * Gets the update data.
   * @return data
   */
  @javax.annotation.Nullable
  public GroupInfoDto getData() {
    return data;
  }

  public void setData(@javax.annotation.Nullable GroupInfoDto data) {
    this.data = data;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupInfoDtoGroupUpdate groupInfoDtoGroupUpdate = (GroupInfoDtoGroupUpdate) o;
    return Objects.equals(this.groupId, groupInfoDtoGroupUpdate.groupId) &&
        Objects.equals(this.type, groupInfoDtoGroupUpdate.type) &&
        Objects.equals(this.data, groupInfoDtoGroupUpdate.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupId, type, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupInfoDtoGroupUpdate {\n");
    sb.append("    groupId: ").append(toIndentedString(groupId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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
    openapiFields.add("GroupId");
    openapiFields.add("Type");
    openapiFields.add("Data");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to GroupInfoDtoGroupUpdate
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!GroupInfoDtoGroupUpdate.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in GroupInfoDtoGroupUpdate is not found in the empty JSON string", GroupInfoDtoGroupUpdate.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!GroupInfoDtoGroupUpdate.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `GroupInfoDtoGroupUpdate` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("GroupId") != null && !jsonObj.get("GroupId").isJsonNull()) && !jsonObj.get("GroupId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `GroupId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("GroupId").toString()));
      }
      // validate the optional field `Type`
      if (jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) {
        GroupUpdateType.validateJsonElement(jsonObj.get("Type"));
      }
      // validate the optional field `Data`
      if (jsonObj.get("Data") != null && !jsonObj.get("Data").isJsonNull()) {
        GroupInfoDto.validateJsonElement(jsonObj.get("Data"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!GroupInfoDtoGroupUpdate.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'GroupInfoDtoGroupUpdate' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<GroupInfoDtoGroupUpdate> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(GroupInfoDtoGroupUpdate.class));

       return (TypeAdapter<T>) new TypeAdapter<GroupInfoDtoGroupUpdate>() {
           @Override
           public void write(JsonWriter out, GroupInfoDtoGroupUpdate value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public GroupInfoDtoGroupUpdate read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of GroupInfoDtoGroupUpdate given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of GroupInfoDtoGroupUpdate
   * @throws IOException if the JSON string is invalid with respect to GroupInfoDtoGroupUpdate
   */
  public static GroupInfoDtoGroupUpdate fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, GroupInfoDtoGroupUpdate.class);
  }

  /**
   * Convert an instance of GroupInfoDtoGroupUpdate to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

