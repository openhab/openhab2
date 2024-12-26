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
import org.openapitools.client.model.BaseItemPersonImageBlurHashes;
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
 * This is used by the api to get information about a Person within a BaseItem.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class BaseItemPerson {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private UUID id;

  public static final String SERIALIZED_NAME_ROLE = "Role";
  @SerializedName(SERIALIZED_NAME_ROLE)
  @javax.annotation.Nullable
  private String role;

  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private String type;

  public static final String SERIALIZED_NAME_PRIMARY_IMAGE_TAG = "PrimaryImageTag";
  @SerializedName(SERIALIZED_NAME_PRIMARY_IMAGE_TAG)
  @javax.annotation.Nullable
  private String primaryImageTag;

  public static final String SERIALIZED_NAME_IMAGE_BLUR_HASHES = "ImageBlurHashes";
  @SerializedName(SERIALIZED_NAME_IMAGE_BLUR_HASHES)
  @javax.annotation.Nullable
  private BaseItemPersonImageBlurHashes imageBlurHashes;

  public BaseItemPerson() {
  }

  public BaseItemPerson name(@javax.annotation.Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets or sets the name.
   * @return name
   */
  @javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(@javax.annotation.Nullable String name) {
    this.name = name;
  }


  public BaseItemPerson id(@javax.annotation.Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Gets or sets the identifier.
   * @return id
   */
  @javax.annotation.Nullable
  public UUID getId() {
    return id;
  }

  public void setId(@javax.annotation.Nullable UUID id) {
    this.id = id;
  }


  public BaseItemPerson role(@javax.annotation.Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Gets or sets the role.
   * @return role
   */
  @javax.annotation.Nullable
  public String getRole() {
    return role;
  }

  public void setRole(@javax.annotation.Nullable String role) {
    this.role = role;
  }


  public BaseItemPerson type(@javax.annotation.Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Gets or sets the type.
   * @return type
   */
  @javax.annotation.Nullable
  public String getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable String type) {
    this.type = type;
  }


  public BaseItemPerson primaryImageTag(@javax.annotation.Nullable String primaryImageTag) {
    this.primaryImageTag = primaryImageTag;
    return this;
  }

  /**
   * Gets or sets the primary image tag.
   * @return primaryImageTag
   */
  @javax.annotation.Nullable
  public String getPrimaryImageTag() {
    return primaryImageTag;
  }

  public void setPrimaryImageTag(@javax.annotation.Nullable String primaryImageTag) {
    this.primaryImageTag = primaryImageTag;
  }


  public BaseItemPerson imageBlurHashes(@javax.annotation.Nullable BaseItemPersonImageBlurHashes imageBlurHashes) {
    this.imageBlurHashes = imageBlurHashes;
    return this;
  }

  /**
   * Get imageBlurHashes
   * @return imageBlurHashes
   */
  @javax.annotation.Nullable
  public BaseItemPersonImageBlurHashes getImageBlurHashes() {
    return imageBlurHashes;
  }

  public void setImageBlurHashes(@javax.annotation.Nullable BaseItemPersonImageBlurHashes imageBlurHashes) {
    this.imageBlurHashes = imageBlurHashes;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseItemPerson baseItemPerson = (BaseItemPerson) o;
    return Objects.equals(this.name, baseItemPerson.name) &&
        Objects.equals(this.id, baseItemPerson.id) &&
        Objects.equals(this.role, baseItemPerson.role) &&
        Objects.equals(this.type, baseItemPerson.type) &&
        Objects.equals(this.primaryImageTag, baseItemPerson.primaryImageTag) &&
        Objects.equals(this.imageBlurHashes, baseItemPerson.imageBlurHashes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id, role, type, primaryImageTag, imageBlurHashes);
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
    sb.append("class BaseItemPerson {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    primaryImageTag: ").append(toIndentedString(primaryImageTag)).append("\n");
    sb.append("    imageBlurHashes: ").append(toIndentedString(imageBlurHashes)).append("\n");
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
    openapiFields.add("Name");
    openapiFields.add("Id");
    openapiFields.add("Role");
    openapiFields.add("Type");
    openapiFields.add("PrimaryImageTag");
    openapiFields.add("ImageBlurHashes");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to BaseItemPerson
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!BaseItemPerson.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in BaseItemPerson is not found in the empty JSON string", BaseItemPerson.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!BaseItemPerson.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `BaseItemPerson` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("Id") != null && !jsonObj.get("Id").isJsonNull()) && !jsonObj.get("Id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Id").toString()));
      }
      if ((jsonObj.get("Role") != null && !jsonObj.get("Role").isJsonNull()) && !jsonObj.get("Role").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Role` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Role").toString()));
      }
      if ((jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) && !jsonObj.get("Type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Type").toString()));
      }
      if ((jsonObj.get("PrimaryImageTag") != null && !jsonObj.get("PrimaryImageTag").isJsonNull()) && !jsonObj.get("PrimaryImageTag").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PrimaryImageTag` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PrimaryImageTag").toString()));
      }
      // validate the optional field `ImageBlurHashes`
      if (jsonObj.get("ImageBlurHashes") != null && !jsonObj.get("ImageBlurHashes").isJsonNull()) {
        BaseItemPersonImageBlurHashes.validateJsonElement(jsonObj.get("ImageBlurHashes"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!BaseItemPerson.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'BaseItemPerson' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<BaseItemPerson> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(BaseItemPerson.class));

       return (TypeAdapter<T>) new TypeAdapter<BaseItemPerson>() {
           @Override
           public void write(JsonWriter out, BaseItemPerson value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public BaseItemPerson read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of BaseItemPerson given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of BaseItemPerson
   * @throws IOException if the JSON string is invalid with respect to BaseItemPerson
   */
  public static BaseItemPerson fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, BaseItemPerson.class);
  }

  /**
   * Convert an instance of BaseItemPerson to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

