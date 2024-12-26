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
import java.util.UUID;
import org.openapitools.client.model.VersionInfo;
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
 * Class PackageInfo.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class PackageInfo {
  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_DESCRIPTION = "description";
  @SerializedName(SERIALIZED_NAME_DESCRIPTION)
  @javax.annotation.Nullable
  private String description;

  public static final String SERIALIZED_NAME_OVERVIEW = "overview";
  @SerializedName(SERIALIZED_NAME_OVERVIEW)
  @javax.annotation.Nullable
  private String overview;

  public static final String SERIALIZED_NAME_OWNER = "owner";
  @SerializedName(SERIALIZED_NAME_OWNER)
  @javax.annotation.Nullable
  private String owner;

  public static final String SERIALIZED_NAME_CATEGORY = "category";
  @SerializedName(SERIALIZED_NAME_CATEGORY)
  @javax.annotation.Nullable
  private String category;

  public static final String SERIALIZED_NAME_GUID = "guid";
  @SerializedName(SERIALIZED_NAME_GUID)
  @javax.annotation.Nullable
  private UUID guid;

  public static final String SERIALIZED_NAME_VERSIONS = "versions";
  @SerializedName(SERIALIZED_NAME_VERSIONS)
  @javax.annotation.Nullable
  private List<VersionInfo> versions = new ArrayList<>();

  public static final String SERIALIZED_NAME_IMAGE_URL = "imageUrl";
  @SerializedName(SERIALIZED_NAME_IMAGE_URL)
  @javax.annotation.Nullable
  private String imageUrl;

  public PackageInfo() {
  }

  public PackageInfo name(@javax.annotation.Nullable String name) {
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


  public PackageInfo description(@javax.annotation.Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Gets or sets a long description of the plugin containing features or helpful explanations.
   * @return description
   */
  @javax.annotation.Nullable
  public String getDescription() {
    return description;
  }

  public void setDescription(@javax.annotation.Nullable String description) {
    this.description = description;
  }


  public PackageInfo overview(@javax.annotation.Nullable String overview) {
    this.overview = overview;
    return this;
  }

  /**
   * Gets or sets a short overview of what the plugin does.
   * @return overview
   */
  @javax.annotation.Nullable
  public String getOverview() {
    return overview;
  }

  public void setOverview(@javax.annotation.Nullable String overview) {
    this.overview = overview;
  }


  public PackageInfo owner(@javax.annotation.Nullable String owner) {
    this.owner = owner;
    return this;
  }

  /**
   * Gets or sets the owner.
   * @return owner
   */
  @javax.annotation.Nullable
  public String getOwner() {
    return owner;
  }

  public void setOwner(@javax.annotation.Nullable String owner) {
    this.owner = owner;
  }


  public PackageInfo category(@javax.annotation.Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Gets or sets the category.
   * @return category
   */
  @javax.annotation.Nullable
  public String getCategory() {
    return category;
  }

  public void setCategory(@javax.annotation.Nullable String category) {
    this.category = category;
  }


  public PackageInfo guid(@javax.annotation.Nullable UUID guid) {
    this.guid = guid;
    return this;
  }

  /**
   * Gets or sets the guid of the assembly associated with this plugin.  This is used to identify the proper item for automatic updates.
   * @return guid
   */
  @javax.annotation.Nullable
  public UUID getGuid() {
    return guid;
  }

  public void setGuid(@javax.annotation.Nullable UUID guid) {
    this.guid = guid;
  }


  public PackageInfo versions(@javax.annotation.Nullable List<VersionInfo> versions) {
    this.versions = versions;
    return this;
  }

  public PackageInfo addVersionsItem(VersionInfo versionsItem) {
    if (this.versions == null) {
      this.versions = new ArrayList<>();
    }
    this.versions.add(versionsItem);
    return this;
  }

  /**
   * Gets or sets the versions.
   * @return versions
   */
  @javax.annotation.Nullable
  public List<VersionInfo> getVersions() {
    return versions;
  }

  public void setVersions(@javax.annotation.Nullable List<VersionInfo> versions) {
    this.versions = versions;
  }


  public PackageInfo imageUrl(@javax.annotation.Nullable String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * Gets or sets the image url for the package.
   * @return imageUrl
   */
  @javax.annotation.Nullable
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(@javax.annotation.Nullable String imageUrl) {
    this.imageUrl = imageUrl;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PackageInfo packageInfo = (PackageInfo) o;
    return Objects.equals(this.name, packageInfo.name) &&
        Objects.equals(this.description, packageInfo.description) &&
        Objects.equals(this.overview, packageInfo.overview) &&
        Objects.equals(this.owner, packageInfo.owner) &&
        Objects.equals(this.category, packageInfo.category) &&
        Objects.equals(this.guid, packageInfo.guid) &&
        Objects.equals(this.versions, packageInfo.versions) &&
        Objects.equals(this.imageUrl, packageInfo.imageUrl);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, overview, owner, category, guid, versions, imageUrl);
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
    sb.append("class PackageInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    overview: ").append(toIndentedString(overview)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    guid: ").append(toIndentedString(guid)).append("\n");
    sb.append("    versions: ").append(toIndentedString(versions)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
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
    openapiFields.add("name");
    openapiFields.add("description");
    openapiFields.add("overview");
    openapiFields.add("owner");
    openapiFields.add("category");
    openapiFields.add("guid");
    openapiFields.add("versions");
    openapiFields.add("imageUrl");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to PackageInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!PackageInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in PackageInfo is not found in the empty JSON string", PackageInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!PackageInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `PackageInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("description") != null && !jsonObj.get("description").isJsonNull()) && !jsonObj.get("description").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `description` to be a primitive type in the JSON string but got `%s`", jsonObj.get("description").toString()));
      }
      if ((jsonObj.get("overview") != null && !jsonObj.get("overview").isJsonNull()) && !jsonObj.get("overview").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `overview` to be a primitive type in the JSON string but got `%s`", jsonObj.get("overview").toString()));
      }
      if ((jsonObj.get("owner") != null && !jsonObj.get("owner").isJsonNull()) && !jsonObj.get("owner").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `owner` to be a primitive type in the JSON string but got `%s`", jsonObj.get("owner").toString()));
      }
      if ((jsonObj.get("category") != null && !jsonObj.get("category").isJsonNull()) && !jsonObj.get("category").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `category` to be a primitive type in the JSON string but got `%s`", jsonObj.get("category").toString()));
      }
      if ((jsonObj.get("guid") != null && !jsonObj.get("guid").isJsonNull()) && !jsonObj.get("guid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `guid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("guid").toString()));
      }
      if (jsonObj.get("versions") != null && !jsonObj.get("versions").isJsonNull()) {
        JsonArray jsonArrayversions = jsonObj.getAsJsonArray("versions");
        if (jsonArrayversions != null) {
          // ensure the json data is an array
          if (!jsonObj.get("versions").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `versions` to be an array in the JSON string but got `%s`", jsonObj.get("versions").toString()));
          }

          // validate the optional field `versions` (array)
          for (int i = 0; i < jsonArrayversions.size(); i++) {
            VersionInfo.validateJsonElement(jsonArrayversions.get(i));
          };
        }
      }
      if ((jsonObj.get("imageUrl") != null && !jsonObj.get("imageUrl").isJsonNull()) && !jsonObj.get("imageUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `imageUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("imageUrl").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!PackageInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'PackageInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<PackageInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(PackageInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<PackageInfo>() {
           @Override
           public void write(JsonWriter out, PackageInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public PackageInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of PackageInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of PackageInfo
   * @throws IOException if the JSON string is invalid with respect to PackageInfo
   */
  public static PackageInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, PackageInfo.class);
  }

  /**
   * Convert an instance of PackageInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

