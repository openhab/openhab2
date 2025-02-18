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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Class LibraryUpdateInfo.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class LibraryUpdateInfo {
  public static final String SERIALIZED_NAME_FOLDERS_ADDED_TO = "FoldersAddedTo";
  @SerializedName(SERIALIZED_NAME_FOLDERS_ADDED_TO)
  @javax.annotation.Nullable
  private List<String> foldersAddedTo = new ArrayList<>();

  public static final String SERIALIZED_NAME_FOLDERS_REMOVED_FROM = "FoldersRemovedFrom";
  @SerializedName(SERIALIZED_NAME_FOLDERS_REMOVED_FROM)
  @javax.annotation.Nullable
  private List<String> foldersRemovedFrom = new ArrayList<>();

  public static final String SERIALIZED_NAME_ITEMS_ADDED = "ItemsAdded";
  @SerializedName(SERIALIZED_NAME_ITEMS_ADDED)
  @javax.annotation.Nullable
  private List<String> itemsAdded = new ArrayList<>();

  public static final String SERIALIZED_NAME_ITEMS_REMOVED = "ItemsRemoved";
  @SerializedName(SERIALIZED_NAME_ITEMS_REMOVED)
  @javax.annotation.Nullable
  private List<String> itemsRemoved = new ArrayList<>();

  public static final String SERIALIZED_NAME_ITEMS_UPDATED = "ItemsUpdated";
  @SerializedName(SERIALIZED_NAME_ITEMS_UPDATED)
  @javax.annotation.Nullable
  private List<String> itemsUpdated = new ArrayList<>();

  public static final String SERIALIZED_NAME_COLLECTION_FOLDERS = "CollectionFolders";
  @SerializedName(SERIALIZED_NAME_COLLECTION_FOLDERS)
  @javax.annotation.Nullable
  private List<String> collectionFolders = new ArrayList<>();

  public static final String SERIALIZED_NAME_IS_EMPTY = "IsEmpty";
  @SerializedName(SERIALIZED_NAME_IS_EMPTY)
  @javax.annotation.Nullable
  private Boolean isEmpty;

  public LibraryUpdateInfo() {
  }

  public LibraryUpdateInfo(
     Boolean isEmpty
  ) {
    this();
    this.isEmpty = isEmpty;
  }

  public LibraryUpdateInfo foldersAddedTo(@javax.annotation.Nullable List<String> foldersAddedTo) {
    this.foldersAddedTo = foldersAddedTo;
    return this;
  }

  public LibraryUpdateInfo addFoldersAddedToItem(String foldersAddedToItem) {
    if (this.foldersAddedTo == null) {
      this.foldersAddedTo = new ArrayList<>();
    }
    this.foldersAddedTo.add(foldersAddedToItem);
    return this;
  }

  /**
   * Gets or sets the folders added to.
   * @return foldersAddedTo
   */
  @javax.annotation.Nullable
  public List<String> getFoldersAddedTo() {
    return foldersAddedTo;
  }

  public void setFoldersAddedTo(@javax.annotation.Nullable List<String> foldersAddedTo) {
    this.foldersAddedTo = foldersAddedTo;
  }


  public LibraryUpdateInfo foldersRemovedFrom(@javax.annotation.Nullable List<String> foldersRemovedFrom) {
    this.foldersRemovedFrom = foldersRemovedFrom;
    return this;
  }

  public LibraryUpdateInfo addFoldersRemovedFromItem(String foldersRemovedFromItem) {
    if (this.foldersRemovedFrom == null) {
      this.foldersRemovedFrom = new ArrayList<>();
    }
    this.foldersRemovedFrom.add(foldersRemovedFromItem);
    return this;
  }

  /**
   * Gets or sets the folders removed from.
   * @return foldersRemovedFrom
   */
  @javax.annotation.Nullable
  public List<String> getFoldersRemovedFrom() {
    return foldersRemovedFrom;
  }

  public void setFoldersRemovedFrom(@javax.annotation.Nullable List<String> foldersRemovedFrom) {
    this.foldersRemovedFrom = foldersRemovedFrom;
  }


  public LibraryUpdateInfo itemsAdded(@javax.annotation.Nullable List<String> itemsAdded) {
    this.itemsAdded = itemsAdded;
    return this;
  }

  public LibraryUpdateInfo addItemsAddedItem(String itemsAddedItem) {
    if (this.itemsAdded == null) {
      this.itemsAdded = new ArrayList<>();
    }
    this.itemsAdded.add(itemsAddedItem);
    return this;
  }

  /**
   * Gets or sets the items added.
   * @return itemsAdded
   */
  @javax.annotation.Nullable
  public List<String> getItemsAdded() {
    return itemsAdded;
  }

  public void setItemsAdded(@javax.annotation.Nullable List<String> itemsAdded) {
    this.itemsAdded = itemsAdded;
  }


  public LibraryUpdateInfo itemsRemoved(@javax.annotation.Nullable List<String> itemsRemoved) {
    this.itemsRemoved = itemsRemoved;
    return this;
  }

  public LibraryUpdateInfo addItemsRemovedItem(String itemsRemovedItem) {
    if (this.itemsRemoved == null) {
      this.itemsRemoved = new ArrayList<>();
    }
    this.itemsRemoved.add(itemsRemovedItem);
    return this;
  }

  /**
   * Gets or sets the items removed.
   * @return itemsRemoved
   */
  @javax.annotation.Nullable
  public List<String> getItemsRemoved() {
    return itemsRemoved;
  }

  public void setItemsRemoved(@javax.annotation.Nullable List<String> itemsRemoved) {
    this.itemsRemoved = itemsRemoved;
  }


  public LibraryUpdateInfo itemsUpdated(@javax.annotation.Nullable List<String> itemsUpdated) {
    this.itemsUpdated = itemsUpdated;
    return this;
  }

  public LibraryUpdateInfo addItemsUpdatedItem(String itemsUpdatedItem) {
    if (this.itemsUpdated == null) {
      this.itemsUpdated = new ArrayList<>();
    }
    this.itemsUpdated.add(itemsUpdatedItem);
    return this;
  }

  /**
   * Gets or sets the items updated.
   * @return itemsUpdated
   */
  @javax.annotation.Nullable
  public List<String> getItemsUpdated() {
    return itemsUpdated;
  }

  public void setItemsUpdated(@javax.annotation.Nullable List<String> itemsUpdated) {
    this.itemsUpdated = itemsUpdated;
  }


  public LibraryUpdateInfo collectionFolders(@javax.annotation.Nullable List<String> collectionFolders) {
    this.collectionFolders = collectionFolders;
    return this;
  }

  public LibraryUpdateInfo addCollectionFoldersItem(String collectionFoldersItem) {
    if (this.collectionFolders == null) {
      this.collectionFolders = new ArrayList<>();
    }
    this.collectionFolders.add(collectionFoldersItem);
    return this;
  }

  /**
   * Get collectionFolders
   * @return collectionFolders
   */
  @javax.annotation.Nullable
  public List<String> getCollectionFolders() {
    return collectionFolders;
  }

  public void setCollectionFolders(@javax.annotation.Nullable List<String> collectionFolders) {
    this.collectionFolders = collectionFolders;
  }


  /**
   * Get isEmpty
   * @return isEmpty
   */
  @javax.annotation.Nullable
  public Boolean getIsEmpty() {
    return isEmpty;
  }




  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LibraryUpdateInfo libraryUpdateInfo = (LibraryUpdateInfo) o;
    return Objects.equals(this.foldersAddedTo, libraryUpdateInfo.foldersAddedTo) &&
        Objects.equals(this.foldersRemovedFrom, libraryUpdateInfo.foldersRemovedFrom) &&
        Objects.equals(this.itemsAdded, libraryUpdateInfo.itemsAdded) &&
        Objects.equals(this.itemsRemoved, libraryUpdateInfo.itemsRemoved) &&
        Objects.equals(this.itemsUpdated, libraryUpdateInfo.itemsUpdated) &&
        Objects.equals(this.collectionFolders, libraryUpdateInfo.collectionFolders) &&
        Objects.equals(this.isEmpty, libraryUpdateInfo.isEmpty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(foldersAddedTo, foldersRemovedFrom, itemsAdded, itemsRemoved, itemsUpdated, collectionFolders, isEmpty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LibraryUpdateInfo {\n");
    sb.append("    foldersAddedTo: ").append(toIndentedString(foldersAddedTo)).append("\n");
    sb.append("    foldersRemovedFrom: ").append(toIndentedString(foldersRemovedFrom)).append("\n");
    sb.append("    itemsAdded: ").append(toIndentedString(itemsAdded)).append("\n");
    sb.append("    itemsRemoved: ").append(toIndentedString(itemsRemoved)).append("\n");
    sb.append("    itemsUpdated: ").append(toIndentedString(itemsUpdated)).append("\n");
    sb.append("    collectionFolders: ").append(toIndentedString(collectionFolders)).append("\n");
    sb.append("    isEmpty: ").append(toIndentedString(isEmpty)).append("\n");
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
    openapiFields.add("FoldersAddedTo");
    openapiFields.add("FoldersRemovedFrom");
    openapiFields.add("ItemsAdded");
    openapiFields.add("ItemsRemoved");
    openapiFields.add("ItemsUpdated");
    openapiFields.add("CollectionFolders");
    openapiFields.add("IsEmpty");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to LibraryUpdateInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!LibraryUpdateInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in LibraryUpdateInfo is not found in the empty JSON string", LibraryUpdateInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!LibraryUpdateInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `LibraryUpdateInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // ensure the optional json data is an array if present
      if (jsonObj.get("FoldersAddedTo") != null && !jsonObj.get("FoldersAddedTo").isJsonNull() && !jsonObj.get("FoldersAddedTo").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `FoldersAddedTo` to be an array in the JSON string but got `%s`", jsonObj.get("FoldersAddedTo").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("FoldersRemovedFrom") != null && !jsonObj.get("FoldersRemovedFrom").isJsonNull() && !jsonObj.get("FoldersRemovedFrom").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `FoldersRemovedFrom` to be an array in the JSON string but got `%s`", jsonObj.get("FoldersRemovedFrom").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ItemsAdded") != null && !jsonObj.get("ItemsAdded").isJsonNull() && !jsonObj.get("ItemsAdded").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemsAdded` to be an array in the JSON string but got `%s`", jsonObj.get("ItemsAdded").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ItemsRemoved") != null && !jsonObj.get("ItemsRemoved").isJsonNull() && !jsonObj.get("ItemsRemoved").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemsRemoved` to be an array in the JSON string but got `%s`", jsonObj.get("ItemsRemoved").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ItemsUpdated") != null && !jsonObj.get("ItemsUpdated").isJsonNull() && !jsonObj.get("ItemsUpdated").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemsUpdated` to be an array in the JSON string but got `%s`", jsonObj.get("ItemsUpdated").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("CollectionFolders") != null && !jsonObj.get("CollectionFolders").isJsonNull() && !jsonObj.get("CollectionFolders").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `CollectionFolders` to be an array in the JSON string but got `%s`", jsonObj.get("CollectionFolders").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!LibraryUpdateInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'LibraryUpdateInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<LibraryUpdateInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(LibraryUpdateInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<LibraryUpdateInfo>() {
           @Override
           public void write(JsonWriter out, LibraryUpdateInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public LibraryUpdateInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of LibraryUpdateInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of LibraryUpdateInfo
   * @throws IOException if the JSON string is invalid with respect to LibraryUpdateInfo
   */
  public static LibraryUpdateInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, LibraryUpdateInfo.class);
  }

  /**
   * Convert an instance of LibraryUpdateInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

