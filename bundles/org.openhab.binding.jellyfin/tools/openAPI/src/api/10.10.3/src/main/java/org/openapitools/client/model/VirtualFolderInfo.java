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
import org.openapitools.client.model.CollectionTypeOptions;
import org.openapitools.client.model.LibraryOptions;
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
 * Used to hold information about a user&#39;s list of configured virtual folders.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class VirtualFolderInfo {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_LOCATIONS = "Locations";
  @SerializedName(SERIALIZED_NAME_LOCATIONS)
  @javax.annotation.Nullable
  private List<String> locations;

  public static final String SERIALIZED_NAME_COLLECTION_TYPE = "CollectionType";
  @SerializedName(SERIALIZED_NAME_COLLECTION_TYPE)
  @javax.annotation.Nullable
  private CollectionTypeOptions collectionType;

  public static final String SERIALIZED_NAME_LIBRARY_OPTIONS = "LibraryOptions";
  @SerializedName(SERIALIZED_NAME_LIBRARY_OPTIONS)
  @javax.annotation.Nullable
  private LibraryOptions libraryOptions;

  public static final String SERIALIZED_NAME_ITEM_ID = "ItemId";
  @SerializedName(SERIALIZED_NAME_ITEM_ID)
  @javax.annotation.Nullable
  private String itemId;

  public static final String SERIALIZED_NAME_PRIMARY_IMAGE_ITEM_ID = "PrimaryImageItemId";
  @SerializedName(SERIALIZED_NAME_PRIMARY_IMAGE_ITEM_ID)
  @javax.annotation.Nullable
  private String primaryImageItemId;

  public static final String SERIALIZED_NAME_REFRESH_PROGRESS = "RefreshProgress";
  @SerializedName(SERIALIZED_NAME_REFRESH_PROGRESS)
  @javax.annotation.Nullable
  private Double refreshProgress;

  public static final String SERIALIZED_NAME_REFRESH_STATUS = "RefreshStatus";
  @SerializedName(SERIALIZED_NAME_REFRESH_STATUS)
  @javax.annotation.Nullable
  private String refreshStatus;

  public VirtualFolderInfo() {
  }

  public VirtualFolderInfo name(@javax.annotation.Nullable String name) {
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


  public VirtualFolderInfo locations(@javax.annotation.Nullable List<String> locations) {
    this.locations = locations;
    return this;
  }

  public VirtualFolderInfo addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Gets or sets the locations.
   * @return locations
   */
  @javax.annotation.Nullable
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(@javax.annotation.Nullable List<String> locations) {
    this.locations = locations;
  }


  public VirtualFolderInfo collectionType(@javax.annotation.Nullable CollectionTypeOptions collectionType) {
    this.collectionType = collectionType;
    return this;
  }

  /**
   * Gets or sets the type of the collection.
   * @return collectionType
   */
  @javax.annotation.Nullable
  public CollectionTypeOptions getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(@javax.annotation.Nullable CollectionTypeOptions collectionType) {
    this.collectionType = collectionType;
  }


  public VirtualFolderInfo libraryOptions(@javax.annotation.Nullable LibraryOptions libraryOptions) {
    this.libraryOptions = libraryOptions;
    return this;
  }

  /**
   * Get libraryOptions
   * @return libraryOptions
   */
  @javax.annotation.Nullable
  public LibraryOptions getLibraryOptions() {
    return libraryOptions;
  }

  public void setLibraryOptions(@javax.annotation.Nullable LibraryOptions libraryOptions) {
    this.libraryOptions = libraryOptions;
  }


  public VirtualFolderInfo itemId(@javax.annotation.Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Gets or sets the item identifier.
   * @return itemId
   */
  @javax.annotation.Nullable
  public String getItemId() {
    return itemId;
  }

  public void setItemId(@javax.annotation.Nullable String itemId) {
    this.itemId = itemId;
  }


  public VirtualFolderInfo primaryImageItemId(@javax.annotation.Nullable String primaryImageItemId) {
    this.primaryImageItemId = primaryImageItemId;
    return this;
  }

  /**
   * Gets or sets the primary image item identifier.
   * @return primaryImageItemId
   */
  @javax.annotation.Nullable
  public String getPrimaryImageItemId() {
    return primaryImageItemId;
  }

  public void setPrimaryImageItemId(@javax.annotation.Nullable String primaryImageItemId) {
    this.primaryImageItemId = primaryImageItemId;
  }


  public VirtualFolderInfo refreshProgress(@javax.annotation.Nullable Double refreshProgress) {
    this.refreshProgress = refreshProgress;
    return this;
  }

  /**
   * Get refreshProgress
   * @return refreshProgress
   */
  @javax.annotation.Nullable
  public Double getRefreshProgress() {
    return refreshProgress;
  }

  public void setRefreshProgress(@javax.annotation.Nullable Double refreshProgress) {
    this.refreshProgress = refreshProgress;
  }


  public VirtualFolderInfo refreshStatus(@javax.annotation.Nullable String refreshStatus) {
    this.refreshStatus = refreshStatus;
    return this;
  }

  /**
   * Get refreshStatus
   * @return refreshStatus
   */
  @javax.annotation.Nullable
  public String getRefreshStatus() {
    return refreshStatus;
  }

  public void setRefreshStatus(@javax.annotation.Nullable String refreshStatus) {
    this.refreshStatus = refreshStatus;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VirtualFolderInfo virtualFolderInfo = (VirtualFolderInfo) o;
    return Objects.equals(this.name, virtualFolderInfo.name) &&
        Objects.equals(this.locations, virtualFolderInfo.locations) &&
        Objects.equals(this.collectionType, virtualFolderInfo.collectionType) &&
        Objects.equals(this.libraryOptions, virtualFolderInfo.libraryOptions) &&
        Objects.equals(this.itemId, virtualFolderInfo.itemId) &&
        Objects.equals(this.primaryImageItemId, virtualFolderInfo.primaryImageItemId) &&
        Objects.equals(this.refreshProgress, virtualFolderInfo.refreshProgress) &&
        Objects.equals(this.refreshStatus, virtualFolderInfo.refreshStatus);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, locations, collectionType, libraryOptions, itemId, primaryImageItemId, refreshProgress, refreshStatus);
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
    sb.append("class VirtualFolderInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    collectionType: ").append(toIndentedString(collectionType)).append("\n");
    sb.append("    libraryOptions: ").append(toIndentedString(libraryOptions)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    primaryImageItemId: ").append(toIndentedString(primaryImageItemId)).append("\n");
    sb.append("    refreshProgress: ").append(toIndentedString(refreshProgress)).append("\n");
    sb.append("    refreshStatus: ").append(toIndentedString(refreshStatus)).append("\n");
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
    openapiFields.add("Locations");
    openapiFields.add("CollectionType");
    openapiFields.add("LibraryOptions");
    openapiFields.add("ItemId");
    openapiFields.add("PrimaryImageItemId");
    openapiFields.add("RefreshProgress");
    openapiFields.add("RefreshStatus");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to VirtualFolderInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!VirtualFolderInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in VirtualFolderInfo is not found in the empty JSON string", VirtualFolderInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!VirtualFolderInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `VirtualFolderInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("Locations") != null && !jsonObj.get("Locations").isJsonNull() && !jsonObj.get("Locations").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `Locations` to be an array in the JSON string but got `%s`", jsonObj.get("Locations").toString()));
      }
      // validate the optional field `CollectionType`
      if (jsonObj.get("CollectionType") != null && !jsonObj.get("CollectionType").isJsonNull()) {
        CollectionTypeOptions.validateJsonElement(jsonObj.get("CollectionType"));
      }
      // validate the optional field `LibraryOptions`
      if (jsonObj.get("LibraryOptions") != null && !jsonObj.get("LibraryOptions").isJsonNull()) {
        LibraryOptions.validateJsonElement(jsonObj.get("LibraryOptions"));
      }
      if ((jsonObj.get("ItemId") != null && !jsonObj.get("ItemId").isJsonNull()) && !jsonObj.get("ItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ItemId").toString()));
      }
      if ((jsonObj.get("PrimaryImageItemId") != null && !jsonObj.get("PrimaryImageItemId").isJsonNull()) && !jsonObj.get("PrimaryImageItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PrimaryImageItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PrimaryImageItemId").toString()));
      }
      if ((jsonObj.get("RefreshStatus") != null && !jsonObj.get("RefreshStatus").isJsonNull()) && !jsonObj.get("RefreshStatus").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `RefreshStatus` to be a primitive type in the JSON string but got `%s`", jsonObj.get("RefreshStatus").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!VirtualFolderInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'VirtualFolderInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<VirtualFolderInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(VirtualFolderInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<VirtualFolderInfo>() {
           @Override
           public void write(JsonWriter out, VirtualFolderInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public VirtualFolderInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of VirtualFolderInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of VirtualFolderInfo
   * @throws IOException if the JSON string is invalid with respect to VirtualFolderInfo
   */
  public static VirtualFolderInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, VirtualFolderInfo.class);
  }

  /**
   * Convert an instance of VirtualFolderInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

