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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * MusicVideoInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class MusicVideoInfo {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_ORIGINAL_TITLE = "OriginalTitle";
  @SerializedName(SERIALIZED_NAME_ORIGINAL_TITLE)
  @javax.annotation.Nullable
  private String originalTitle;

  public static final String SERIALIZED_NAME_PATH = "Path";
  @SerializedName(SERIALIZED_NAME_PATH)
  @javax.annotation.Nullable
  private String path;

  public static final String SERIALIZED_NAME_METADATA_LANGUAGE = "MetadataLanguage";
  @SerializedName(SERIALIZED_NAME_METADATA_LANGUAGE)
  @javax.annotation.Nullable
  private String metadataLanguage;

  public static final String SERIALIZED_NAME_METADATA_COUNTRY_CODE = "MetadataCountryCode";
  @SerializedName(SERIALIZED_NAME_METADATA_COUNTRY_CODE)
  @javax.annotation.Nullable
  private String metadataCountryCode;

  public static final String SERIALIZED_NAME_PROVIDER_IDS = "ProviderIds";
  @SerializedName(SERIALIZED_NAME_PROVIDER_IDS)
  @javax.annotation.Nullable
  private Map<String, String> providerIds;

  public static final String SERIALIZED_NAME_YEAR = "Year";
  @SerializedName(SERIALIZED_NAME_YEAR)
  @javax.annotation.Nullable
  private Integer year;

  public static final String SERIALIZED_NAME_INDEX_NUMBER = "IndexNumber";
  @SerializedName(SERIALIZED_NAME_INDEX_NUMBER)
  @javax.annotation.Nullable
  private Integer indexNumber;

  public static final String SERIALIZED_NAME_PARENT_INDEX_NUMBER = "ParentIndexNumber";
  @SerializedName(SERIALIZED_NAME_PARENT_INDEX_NUMBER)
  @javax.annotation.Nullable
  private Integer parentIndexNumber;

  public static final String SERIALIZED_NAME_PREMIERE_DATE = "PremiereDate";
  @SerializedName(SERIALIZED_NAME_PREMIERE_DATE)
  @javax.annotation.Nullable
  private OffsetDateTime premiereDate;

  public static final String SERIALIZED_NAME_IS_AUTOMATED = "IsAutomated";
  @SerializedName(SERIALIZED_NAME_IS_AUTOMATED)
  @javax.annotation.Nullable
  private Boolean isAutomated;

  public static final String SERIALIZED_NAME_ARTISTS = "Artists";
  @SerializedName(SERIALIZED_NAME_ARTISTS)
  @javax.annotation.Nullable
  private List<String> artists;

  public MusicVideoInfo() {
  }

  public MusicVideoInfo name(@javax.annotation.Nullable String name) {
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


  public MusicVideoInfo originalTitle(@javax.annotation.Nullable String originalTitle) {
    this.originalTitle = originalTitle;
    return this;
  }

  /**
   * Gets or sets the original title.
   * @return originalTitle
   */
  @javax.annotation.Nullable
  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(@javax.annotation.Nullable String originalTitle) {
    this.originalTitle = originalTitle;
  }


  public MusicVideoInfo path(@javax.annotation.Nullable String path) {
    this.path = path;
    return this;
  }

  /**
   * Gets or sets the path.
   * @return path
   */
  @javax.annotation.Nullable
  public String getPath() {
    return path;
  }

  public void setPath(@javax.annotation.Nullable String path) {
    this.path = path;
  }


  public MusicVideoInfo metadataLanguage(@javax.annotation.Nullable String metadataLanguage) {
    this.metadataLanguage = metadataLanguage;
    return this;
  }

  /**
   * Gets or sets the metadata language.
   * @return metadataLanguage
   */
  @javax.annotation.Nullable
  public String getMetadataLanguage() {
    return metadataLanguage;
  }

  public void setMetadataLanguage(@javax.annotation.Nullable String metadataLanguage) {
    this.metadataLanguage = metadataLanguage;
  }


  public MusicVideoInfo metadataCountryCode(@javax.annotation.Nullable String metadataCountryCode) {
    this.metadataCountryCode = metadataCountryCode;
    return this;
  }

  /**
   * Gets or sets the metadata country code.
   * @return metadataCountryCode
   */
  @javax.annotation.Nullable
  public String getMetadataCountryCode() {
    return metadataCountryCode;
  }

  public void setMetadataCountryCode(@javax.annotation.Nullable String metadataCountryCode) {
    this.metadataCountryCode = metadataCountryCode;
  }


  public MusicVideoInfo providerIds(@javax.annotation.Nullable Map<String, String> providerIds) {
    this.providerIds = providerIds;
    return this;
  }

  public MusicVideoInfo putProviderIdsItem(String key, String providerIdsItem) {
    if (this.providerIds == null) {
      this.providerIds = new HashMap<>();
    }
    this.providerIds.put(key, providerIdsItem);
    return this;
  }

  /**
   * Gets or sets the provider ids.
   * @return providerIds
   */
  @javax.annotation.Nullable
  public Map<String, String> getProviderIds() {
    return providerIds;
  }

  public void setProviderIds(@javax.annotation.Nullable Map<String, String> providerIds) {
    this.providerIds = providerIds;
  }


  public MusicVideoInfo year(@javax.annotation.Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Gets or sets the year.
   * @return year
   */
  @javax.annotation.Nullable
  public Integer getYear() {
    return year;
  }

  public void setYear(@javax.annotation.Nullable Integer year) {
    this.year = year;
  }


  public MusicVideoInfo indexNumber(@javax.annotation.Nullable Integer indexNumber) {
    this.indexNumber = indexNumber;
    return this;
  }

  /**
   * Get indexNumber
   * @return indexNumber
   */
  @javax.annotation.Nullable
  public Integer getIndexNumber() {
    return indexNumber;
  }

  public void setIndexNumber(@javax.annotation.Nullable Integer indexNumber) {
    this.indexNumber = indexNumber;
  }


  public MusicVideoInfo parentIndexNumber(@javax.annotation.Nullable Integer parentIndexNumber) {
    this.parentIndexNumber = parentIndexNumber;
    return this;
  }

  /**
   * Get parentIndexNumber
   * @return parentIndexNumber
   */
  @javax.annotation.Nullable
  public Integer getParentIndexNumber() {
    return parentIndexNumber;
  }

  public void setParentIndexNumber(@javax.annotation.Nullable Integer parentIndexNumber) {
    this.parentIndexNumber = parentIndexNumber;
  }


  public MusicVideoInfo premiereDate(@javax.annotation.Nullable OffsetDateTime premiereDate) {
    this.premiereDate = premiereDate;
    return this;
  }

  /**
   * Get premiereDate
   * @return premiereDate
   */
  @javax.annotation.Nullable
  public OffsetDateTime getPremiereDate() {
    return premiereDate;
  }

  public void setPremiereDate(@javax.annotation.Nullable OffsetDateTime premiereDate) {
    this.premiereDate = premiereDate;
  }


  public MusicVideoInfo isAutomated(@javax.annotation.Nullable Boolean isAutomated) {
    this.isAutomated = isAutomated;
    return this;
  }

  /**
   * Get isAutomated
   * @return isAutomated
   */
  @javax.annotation.Nullable
  public Boolean getIsAutomated() {
    return isAutomated;
  }

  public void setIsAutomated(@javax.annotation.Nullable Boolean isAutomated) {
    this.isAutomated = isAutomated;
  }


  public MusicVideoInfo artists(@javax.annotation.Nullable List<String> artists) {
    this.artists = artists;
    return this;
  }

  public MusicVideoInfo addArtistsItem(String artistsItem) {
    if (this.artists == null) {
      this.artists = new ArrayList<>();
    }
    this.artists.add(artistsItem);
    return this;
  }

  /**
   * Get artists
   * @return artists
   */
  @javax.annotation.Nullable
  public List<String> getArtists() {
    return artists;
  }

  public void setArtists(@javax.annotation.Nullable List<String> artists) {
    this.artists = artists;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MusicVideoInfo musicVideoInfo = (MusicVideoInfo) o;
    return Objects.equals(this.name, musicVideoInfo.name) &&
        Objects.equals(this.originalTitle, musicVideoInfo.originalTitle) &&
        Objects.equals(this.path, musicVideoInfo.path) &&
        Objects.equals(this.metadataLanguage, musicVideoInfo.metadataLanguage) &&
        Objects.equals(this.metadataCountryCode, musicVideoInfo.metadataCountryCode) &&
        Objects.equals(this.providerIds, musicVideoInfo.providerIds) &&
        Objects.equals(this.year, musicVideoInfo.year) &&
        Objects.equals(this.indexNumber, musicVideoInfo.indexNumber) &&
        Objects.equals(this.parentIndexNumber, musicVideoInfo.parentIndexNumber) &&
        Objects.equals(this.premiereDate, musicVideoInfo.premiereDate) &&
        Objects.equals(this.isAutomated, musicVideoInfo.isAutomated) &&
        Objects.equals(this.artists, musicVideoInfo.artists);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, originalTitle, path, metadataLanguage, metadataCountryCode, providerIds, year, indexNumber, parentIndexNumber, premiereDate, isAutomated, artists);
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
    sb.append("class MusicVideoInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    originalTitle: ").append(toIndentedString(originalTitle)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    metadataLanguage: ").append(toIndentedString(metadataLanguage)).append("\n");
    sb.append("    metadataCountryCode: ").append(toIndentedString(metadataCountryCode)).append("\n");
    sb.append("    providerIds: ").append(toIndentedString(providerIds)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    indexNumber: ").append(toIndentedString(indexNumber)).append("\n");
    sb.append("    parentIndexNumber: ").append(toIndentedString(parentIndexNumber)).append("\n");
    sb.append("    premiereDate: ").append(toIndentedString(premiereDate)).append("\n");
    sb.append("    isAutomated: ").append(toIndentedString(isAutomated)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
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
    openapiFields.add("OriginalTitle");
    openapiFields.add("Path");
    openapiFields.add("MetadataLanguage");
    openapiFields.add("MetadataCountryCode");
    openapiFields.add("ProviderIds");
    openapiFields.add("Year");
    openapiFields.add("IndexNumber");
    openapiFields.add("ParentIndexNumber");
    openapiFields.add("PremiereDate");
    openapiFields.add("IsAutomated");
    openapiFields.add("Artists");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to MusicVideoInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!MusicVideoInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in MusicVideoInfo is not found in the empty JSON string", MusicVideoInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!MusicVideoInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `MusicVideoInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("OriginalTitle") != null && !jsonObj.get("OriginalTitle").isJsonNull()) && !jsonObj.get("OriginalTitle").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `OriginalTitle` to be a primitive type in the JSON string but got `%s`", jsonObj.get("OriginalTitle").toString()));
      }
      if ((jsonObj.get("Path") != null && !jsonObj.get("Path").isJsonNull()) && !jsonObj.get("Path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Path").toString()));
      }
      if ((jsonObj.get("MetadataLanguage") != null && !jsonObj.get("MetadataLanguage").isJsonNull()) && !jsonObj.get("MetadataLanguage").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MetadataLanguage` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MetadataLanguage").toString()));
      }
      if ((jsonObj.get("MetadataCountryCode") != null && !jsonObj.get("MetadataCountryCode").isJsonNull()) && !jsonObj.get("MetadataCountryCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MetadataCountryCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MetadataCountryCode").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("Artists") != null && !jsonObj.get("Artists").isJsonNull() && !jsonObj.get("Artists").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `Artists` to be an array in the JSON string but got `%s`", jsonObj.get("Artists").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!MusicVideoInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'MusicVideoInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<MusicVideoInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(MusicVideoInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<MusicVideoInfo>() {
           @Override
           public void write(JsonWriter out, MusicVideoInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public MusicVideoInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of MusicVideoInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of MusicVideoInfo
   * @throws IOException if the JSON string is invalid with respect to MusicVideoInfo
   */
  public static MusicVideoInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, MusicVideoInfo.class);
  }

  /**
   * Convert an instance of MusicVideoInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

