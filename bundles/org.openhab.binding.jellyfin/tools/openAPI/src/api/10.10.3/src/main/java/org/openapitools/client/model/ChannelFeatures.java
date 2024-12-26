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
import org.openapitools.client.model.ChannelItemSortField;
import org.openapitools.client.model.ChannelMediaContentType;
import org.openapitools.client.model.ChannelMediaType;
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
 * ChannelFeatures
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ChannelFeatures {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private UUID id;

  public static final String SERIALIZED_NAME_CAN_SEARCH = "CanSearch";
  @SerializedName(SERIALIZED_NAME_CAN_SEARCH)
  @javax.annotation.Nullable
  private Boolean canSearch;

  public static final String SERIALIZED_NAME_MEDIA_TYPES = "MediaTypes";
  @SerializedName(SERIALIZED_NAME_MEDIA_TYPES)
  @javax.annotation.Nullable
  private List<ChannelMediaType> mediaTypes = new ArrayList<>();

  public static final String SERIALIZED_NAME_CONTENT_TYPES = "ContentTypes";
  @SerializedName(SERIALIZED_NAME_CONTENT_TYPES)
  @javax.annotation.Nullable
  private List<ChannelMediaContentType> contentTypes = new ArrayList<>();

  public static final String SERIALIZED_NAME_MAX_PAGE_SIZE = "MaxPageSize";
  @SerializedName(SERIALIZED_NAME_MAX_PAGE_SIZE)
  @javax.annotation.Nullable
  private Integer maxPageSize;

  public static final String SERIALIZED_NAME_AUTO_REFRESH_LEVELS = "AutoRefreshLevels";
  @SerializedName(SERIALIZED_NAME_AUTO_REFRESH_LEVELS)
  @javax.annotation.Nullable
  private Integer autoRefreshLevels;

  public static final String SERIALIZED_NAME_DEFAULT_SORT_FIELDS = "DefaultSortFields";
  @SerializedName(SERIALIZED_NAME_DEFAULT_SORT_FIELDS)
  @javax.annotation.Nullable
  private List<ChannelItemSortField> defaultSortFields = new ArrayList<>();

  public static final String SERIALIZED_NAME_SUPPORTS_SORT_ORDER_TOGGLE = "SupportsSortOrderToggle";
  @SerializedName(SERIALIZED_NAME_SUPPORTS_SORT_ORDER_TOGGLE)
  @javax.annotation.Nullable
  private Boolean supportsSortOrderToggle;

  public static final String SERIALIZED_NAME_SUPPORTS_LATEST_MEDIA = "SupportsLatestMedia";
  @SerializedName(SERIALIZED_NAME_SUPPORTS_LATEST_MEDIA)
  @javax.annotation.Nullable
  private Boolean supportsLatestMedia;

  public static final String SERIALIZED_NAME_CAN_FILTER = "CanFilter";
  @SerializedName(SERIALIZED_NAME_CAN_FILTER)
  @javax.annotation.Nullable
  private Boolean canFilter;

  public static final String SERIALIZED_NAME_SUPPORTS_CONTENT_DOWNLOADING = "SupportsContentDownloading";
  @SerializedName(SERIALIZED_NAME_SUPPORTS_CONTENT_DOWNLOADING)
  @javax.annotation.Nullable
  private Boolean supportsContentDownloading;

  public ChannelFeatures() {
  }

  public ChannelFeatures name(@javax.annotation.Nullable String name) {
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


  public ChannelFeatures id(@javax.annotation.Nullable UUID id) {
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


  public ChannelFeatures canSearch(@javax.annotation.Nullable Boolean canSearch) {
    this.canSearch = canSearch;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance can search.
   * @return canSearch
   */
  @javax.annotation.Nullable
  public Boolean getCanSearch() {
    return canSearch;
  }

  public void setCanSearch(@javax.annotation.Nullable Boolean canSearch) {
    this.canSearch = canSearch;
  }


  public ChannelFeatures mediaTypes(@javax.annotation.Nullable List<ChannelMediaType> mediaTypes) {
    this.mediaTypes = mediaTypes;
    return this;
  }

  public ChannelFeatures addMediaTypesItem(ChannelMediaType mediaTypesItem) {
    if (this.mediaTypes == null) {
      this.mediaTypes = new ArrayList<>();
    }
    this.mediaTypes.add(mediaTypesItem);
    return this;
  }

  /**
   * Gets or sets the media types.
   * @return mediaTypes
   */
  @javax.annotation.Nullable
  public List<ChannelMediaType> getMediaTypes() {
    return mediaTypes;
  }

  public void setMediaTypes(@javax.annotation.Nullable List<ChannelMediaType> mediaTypes) {
    this.mediaTypes = mediaTypes;
  }


  public ChannelFeatures contentTypes(@javax.annotation.Nullable List<ChannelMediaContentType> contentTypes) {
    this.contentTypes = contentTypes;
    return this;
  }

  public ChannelFeatures addContentTypesItem(ChannelMediaContentType contentTypesItem) {
    if (this.contentTypes == null) {
      this.contentTypes = new ArrayList<>();
    }
    this.contentTypes.add(contentTypesItem);
    return this;
  }

  /**
   * Gets or sets the content types.
   * @return contentTypes
   */
  @javax.annotation.Nullable
  public List<ChannelMediaContentType> getContentTypes() {
    return contentTypes;
  }

  public void setContentTypes(@javax.annotation.Nullable List<ChannelMediaContentType> contentTypes) {
    this.contentTypes = contentTypes;
  }


  public ChannelFeatures maxPageSize(@javax.annotation.Nullable Integer maxPageSize) {
    this.maxPageSize = maxPageSize;
    return this;
  }

  /**
   * Gets or sets the maximum number of records the channel allows retrieving at a time.
   * @return maxPageSize
   */
  @javax.annotation.Nullable
  public Integer getMaxPageSize() {
    return maxPageSize;
  }

  public void setMaxPageSize(@javax.annotation.Nullable Integer maxPageSize) {
    this.maxPageSize = maxPageSize;
  }


  public ChannelFeatures autoRefreshLevels(@javax.annotation.Nullable Integer autoRefreshLevels) {
    this.autoRefreshLevels = autoRefreshLevels;
    return this;
  }

  /**
   * Gets or sets the automatic refresh levels.
   * @return autoRefreshLevels
   */
  @javax.annotation.Nullable
  public Integer getAutoRefreshLevels() {
    return autoRefreshLevels;
  }

  public void setAutoRefreshLevels(@javax.annotation.Nullable Integer autoRefreshLevels) {
    this.autoRefreshLevels = autoRefreshLevels;
  }


  public ChannelFeatures defaultSortFields(@javax.annotation.Nullable List<ChannelItemSortField> defaultSortFields) {
    this.defaultSortFields = defaultSortFields;
    return this;
  }

  public ChannelFeatures addDefaultSortFieldsItem(ChannelItemSortField defaultSortFieldsItem) {
    if (this.defaultSortFields == null) {
      this.defaultSortFields = new ArrayList<>();
    }
    this.defaultSortFields.add(defaultSortFieldsItem);
    return this;
  }

  /**
   * Gets or sets the default sort orders.
   * @return defaultSortFields
   */
  @javax.annotation.Nullable
  public List<ChannelItemSortField> getDefaultSortFields() {
    return defaultSortFields;
  }

  public void setDefaultSortFields(@javax.annotation.Nullable List<ChannelItemSortField> defaultSortFields) {
    this.defaultSortFields = defaultSortFields;
  }


  public ChannelFeatures supportsSortOrderToggle(@javax.annotation.Nullable Boolean supportsSortOrderToggle) {
    this.supportsSortOrderToggle = supportsSortOrderToggle;
    return this;
  }

  /**
   * Gets or sets a value indicating whether a sort ascending/descending toggle is supported.
   * @return supportsSortOrderToggle
   */
  @javax.annotation.Nullable
  public Boolean getSupportsSortOrderToggle() {
    return supportsSortOrderToggle;
  }

  public void setSupportsSortOrderToggle(@javax.annotation.Nullable Boolean supportsSortOrderToggle) {
    this.supportsSortOrderToggle = supportsSortOrderToggle;
  }


  public ChannelFeatures supportsLatestMedia(@javax.annotation.Nullable Boolean supportsLatestMedia) {
    this.supportsLatestMedia = supportsLatestMedia;
    return this;
  }

  /**
   * Gets or sets a value indicating whether [supports latest media].
   * @return supportsLatestMedia
   */
  @javax.annotation.Nullable
  public Boolean getSupportsLatestMedia() {
    return supportsLatestMedia;
  }

  public void setSupportsLatestMedia(@javax.annotation.Nullable Boolean supportsLatestMedia) {
    this.supportsLatestMedia = supportsLatestMedia;
  }


  public ChannelFeatures canFilter(@javax.annotation.Nullable Boolean canFilter) {
    this.canFilter = canFilter;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance can filter.
   * @return canFilter
   */
  @javax.annotation.Nullable
  public Boolean getCanFilter() {
    return canFilter;
  }

  public void setCanFilter(@javax.annotation.Nullable Boolean canFilter) {
    this.canFilter = canFilter;
  }


  public ChannelFeatures supportsContentDownloading(@javax.annotation.Nullable Boolean supportsContentDownloading) {
    this.supportsContentDownloading = supportsContentDownloading;
    return this;
  }

  /**
   * Gets or sets a value indicating whether [supports content downloading].
   * @return supportsContentDownloading
   */
  @javax.annotation.Nullable
  public Boolean getSupportsContentDownloading() {
    return supportsContentDownloading;
  }

  public void setSupportsContentDownloading(@javax.annotation.Nullable Boolean supportsContentDownloading) {
    this.supportsContentDownloading = supportsContentDownloading;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelFeatures channelFeatures = (ChannelFeatures) o;
    return Objects.equals(this.name, channelFeatures.name) &&
        Objects.equals(this.id, channelFeatures.id) &&
        Objects.equals(this.canSearch, channelFeatures.canSearch) &&
        Objects.equals(this.mediaTypes, channelFeatures.mediaTypes) &&
        Objects.equals(this.contentTypes, channelFeatures.contentTypes) &&
        Objects.equals(this.maxPageSize, channelFeatures.maxPageSize) &&
        Objects.equals(this.autoRefreshLevels, channelFeatures.autoRefreshLevels) &&
        Objects.equals(this.defaultSortFields, channelFeatures.defaultSortFields) &&
        Objects.equals(this.supportsSortOrderToggle, channelFeatures.supportsSortOrderToggle) &&
        Objects.equals(this.supportsLatestMedia, channelFeatures.supportsLatestMedia) &&
        Objects.equals(this.canFilter, channelFeatures.canFilter) &&
        Objects.equals(this.supportsContentDownloading, channelFeatures.supportsContentDownloading);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id, canSearch, mediaTypes, contentTypes, maxPageSize, autoRefreshLevels, defaultSortFields, supportsSortOrderToggle, supportsLatestMedia, canFilter, supportsContentDownloading);
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
    sb.append("class ChannelFeatures {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    canSearch: ").append(toIndentedString(canSearch)).append("\n");
    sb.append("    mediaTypes: ").append(toIndentedString(mediaTypes)).append("\n");
    sb.append("    contentTypes: ").append(toIndentedString(contentTypes)).append("\n");
    sb.append("    maxPageSize: ").append(toIndentedString(maxPageSize)).append("\n");
    sb.append("    autoRefreshLevels: ").append(toIndentedString(autoRefreshLevels)).append("\n");
    sb.append("    defaultSortFields: ").append(toIndentedString(defaultSortFields)).append("\n");
    sb.append("    supportsSortOrderToggle: ").append(toIndentedString(supportsSortOrderToggle)).append("\n");
    sb.append("    supportsLatestMedia: ").append(toIndentedString(supportsLatestMedia)).append("\n");
    sb.append("    canFilter: ").append(toIndentedString(canFilter)).append("\n");
    sb.append("    supportsContentDownloading: ").append(toIndentedString(supportsContentDownloading)).append("\n");
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
    openapiFields.add("CanSearch");
    openapiFields.add("MediaTypes");
    openapiFields.add("ContentTypes");
    openapiFields.add("MaxPageSize");
    openapiFields.add("AutoRefreshLevels");
    openapiFields.add("DefaultSortFields");
    openapiFields.add("SupportsSortOrderToggle");
    openapiFields.add("SupportsLatestMedia");
    openapiFields.add("CanFilter");
    openapiFields.add("SupportsContentDownloading");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ChannelFeatures
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ChannelFeatures.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ChannelFeatures is not found in the empty JSON string", ChannelFeatures.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ChannelFeatures.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ChannelFeatures` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("Id") != null && !jsonObj.get("Id").isJsonNull()) && !jsonObj.get("Id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Id").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("MediaTypes") != null && !jsonObj.get("MediaTypes").isJsonNull() && !jsonObj.get("MediaTypes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `MediaTypes` to be an array in the JSON string but got `%s`", jsonObj.get("MediaTypes").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ContentTypes") != null && !jsonObj.get("ContentTypes").isJsonNull() && !jsonObj.get("ContentTypes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ContentTypes` to be an array in the JSON string but got `%s`", jsonObj.get("ContentTypes").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("DefaultSortFields") != null && !jsonObj.get("DefaultSortFields").isJsonNull() && !jsonObj.get("DefaultSortFields").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `DefaultSortFields` to be an array in the JSON string but got `%s`", jsonObj.get("DefaultSortFields").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ChannelFeatures.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ChannelFeatures' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ChannelFeatures> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ChannelFeatures.class));

       return (TypeAdapter<T>) new TypeAdapter<ChannelFeatures>() {
           @Override
           public void write(JsonWriter out, ChannelFeatures value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ChannelFeatures read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ChannelFeatures given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ChannelFeatures
   * @throws IOException if the JSON string is invalid with respect to ChannelFeatures
   */
  public static ChannelFeatures fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ChannelFeatures.class);
  }

  /**
   * Convert an instance of ChannelFeatures to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

