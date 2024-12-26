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
import org.openapitools.client.model.ImageType;
import org.openapitools.client.model.RatingType;
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
 * Class RemoteImageInfo.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class RemoteImageInfo {
  public static final String SERIALIZED_NAME_PROVIDER_NAME = "ProviderName";
  @SerializedName(SERIALIZED_NAME_PROVIDER_NAME)
  @javax.annotation.Nullable
  private String providerName;

  public static final String SERIALIZED_NAME_URL = "Url";
  @SerializedName(SERIALIZED_NAME_URL)
  @javax.annotation.Nullable
  private String url;

  public static final String SERIALIZED_NAME_THUMBNAIL_URL = "ThumbnailUrl";
  @SerializedName(SERIALIZED_NAME_THUMBNAIL_URL)
  @javax.annotation.Nullable
  private String thumbnailUrl;

  public static final String SERIALIZED_NAME_HEIGHT = "Height";
  @SerializedName(SERIALIZED_NAME_HEIGHT)
  @javax.annotation.Nullable
  private Integer height;

  public static final String SERIALIZED_NAME_WIDTH = "Width";
  @SerializedName(SERIALIZED_NAME_WIDTH)
  @javax.annotation.Nullable
  private Integer width;

  public static final String SERIALIZED_NAME_COMMUNITY_RATING = "CommunityRating";
  @SerializedName(SERIALIZED_NAME_COMMUNITY_RATING)
  @javax.annotation.Nullable
  private Double communityRating;

  public static final String SERIALIZED_NAME_VOTE_COUNT = "VoteCount";
  @SerializedName(SERIALIZED_NAME_VOTE_COUNT)
  @javax.annotation.Nullable
  private Integer voteCount;

  public static final String SERIALIZED_NAME_LANGUAGE = "Language";
  @SerializedName(SERIALIZED_NAME_LANGUAGE)
  @javax.annotation.Nullable
  private String language;

  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private ImageType type;

  public static final String SERIALIZED_NAME_RATING_TYPE = "RatingType";
  @SerializedName(SERIALIZED_NAME_RATING_TYPE)
  @javax.annotation.Nullable
  private RatingType ratingType;

  public RemoteImageInfo() {
  }

  public RemoteImageInfo providerName(@javax.annotation.Nullable String providerName) {
    this.providerName = providerName;
    return this;
  }

  /**
   * Gets or sets the name of the provider.
   * @return providerName
   */
  @javax.annotation.Nullable
  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(@javax.annotation.Nullable String providerName) {
    this.providerName = providerName;
  }


  public RemoteImageInfo url(@javax.annotation.Nullable String url) {
    this.url = url;
    return this;
  }

  /**
   * Gets or sets the URL.
   * @return url
   */
  @javax.annotation.Nullable
  public String getUrl() {
    return url;
  }

  public void setUrl(@javax.annotation.Nullable String url) {
    this.url = url;
  }


  public RemoteImageInfo thumbnailUrl(@javax.annotation.Nullable String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
    return this;
  }

  /**
   * Gets or sets a url used for previewing a smaller version.
   * @return thumbnailUrl
   */
  @javax.annotation.Nullable
  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(@javax.annotation.Nullable String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }


  public RemoteImageInfo height(@javax.annotation.Nullable Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Gets or sets the height.
   * @return height
   */
  @javax.annotation.Nullable
  public Integer getHeight() {
    return height;
  }

  public void setHeight(@javax.annotation.Nullable Integer height) {
    this.height = height;
  }


  public RemoteImageInfo width(@javax.annotation.Nullable Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Gets or sets the width.
   * @return width
   */
  @javax.annotation.Nullable
  public Integer getWidth() {
    return width;
  }

  public void setWidth(@javax.annotation.Nullable Integer width) {
    this.width = width;
  }


  public RemoteImageInfo communityRating(@javax.annotation.Nullable Double communityRating) {
    this.communityRating = communityRating;
    return this;
  }

  /**
   * Gets or sets the community rating.
   * @return communityRating
   */
  @javax.annotation.Nullable
  public Double getCommunityRating() {
    return communityRating;
  }

  public void setCommunityRating(@javax.annotation.Nullable Double communityRating) {
    this.communityRating = communityRating;
  }


  public RemoteImageInfo voteCount(@javax.annotation.Nullable Integer voteCount) {
    this.voteCount = voteCount;
    return this;
  }

  /**
   * Gets or sets the vote count.
   * @return voteCount
   */
  @javax.annotation.Nullable
  public Integer getVoteCount() {
    return voteCount;
  }

  public void setVoteCount(@javax.annotation.Nullable Integer voteCount) {
    this.voteCount = voteCount;
  }


  public RemoteImageInfo language(@javax.annotation.Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Gets or sets the language.
   * @return language
   */
  @javax.annotation.Nullable
  public String getLanguage() {
    return language;
  }

  public void setLanguage(@javax.annotation.Nullable String language) {
    this.language = language;
  }


  public RemoteImageInfo type(@javax.annotation.Nullable ImageType type) {
    this.type = type;
    return this;
  }

  /**
   * Gets or sets the type.
   * @return type
   */
  @javax.annotation.Nullable
  public ImageType getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable ImageType type) {
    this.type = type;
  }


  public RemoteImageInfo ratingType(@javax.annotation.Nullable RatingType ratingType) {
    this.ratingType = ratingType;
    return this;
  }

  /**
   * Gets or sets the type of the rating.
   * @return ratingType
   */
  @javax.annotation.Nullable
  public RatingType getRatingType() {
    return ratingType;
  }

  public void setRatingType(@javax.annotation.Nullable RatingType ratingType) {
    this.ratingType = ratingType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoteImageInfo remoteImageInfo = (RemoteImageInfo) o;
    return Objects.equals(this.providerName, remoteImageInfo.providerName) &&
        Objects.equals(this.url, remoteImageInfo.url) &&
        Objects.equals(this.thumbnailUrl, remoteImageInfo.thumbnailUrl) &&
        Objects.equals(this.height, remoteImageInfo.height) &&
        Objects.equals(this.width, remoteImageInfo.width) &&
        Objects.equals(this.communityRating, remoteImageInfo.communityRating) &&
        Objects.equals(this.voteCount, remoteImageInfo.voteCount) &&
        Objects.equals(this.language, remoteImageInfo.language) &&
        Objects.equals(this.type, remoteImageInfo.type) &&
        Objects.equals(this.ratingType, remoteImageInfo.ratingType);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(providerName, url, thumbnailUrl, height, width, communityRating, voteCount, language, type, ratingType);
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
    sb.append("class RemoteImageInfo {\n");
    sb.append("    providerName: ").append(toIndentedString(providerName)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    thumbnailUrl: ").append(toIndentedString(thumbnailUrl)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    communityRating: ").append(toIndentedString(communityRating)).append("\n");
    sb.append("    voteCount: ").append(toIndentedString(voteCount)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    ratingType: ").append(toIndentedString(ratingType)).append("\n");
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
    openapiFields.add("ProviderName");
    openapiFields.add("Url");
    openapiFields.add("ThumbnailUrl");
    openapiFields.add("Height");
    openapiFields.add("Width");
    openapiFields.add("CommunityRating");
    openapiFields.add("VoteCount");
    openapiFields.add("Language");
    openapiFields.add("Type");
    openapiFields.add("RatingType");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to RemoteImageInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!RemoteImageInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in RemoteImageInfo is not found in the empty JSON string", RemoteImageInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!RemoteImageInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `RemoteImageInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("ProviderName") != null && !jsonObj.get("ProviderName").isJsonNull()) && !jsonObj.get("ProviderName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ProviderName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ProviderName").toString()));
      }
      if ((jsonObj.get("Url") != null && !jsonObj.get("Url").isJsonNull()) && !jsonObj.get("Url").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Url` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Url").toString()));
      }
      if ((jsonObj.get("ThumbnailUrl") != null && !jsonObj.get("ThumbnailUrl").isJsonNull()) && !jsonObj.get("ThumbnailUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ThumbnailUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ThumbnailUrl").toString()));
      }
      if ((jsonObj.get("Language") != null && !jsonObj.get("Language").isJsonNull()) && !jsonObj.get("Language").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Language` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Language").toString()));
      }
      // validate the optional field `Type`
      if (jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) {
        ImageType.validateJsonElement(jsonObj.get("Type"));
      }
      // validate the optional field `RatingType`
      if (jsonObj.get("RatingType") != null && !jsonObj.get("RatingType").isJsonNull()) {
        RatingType.validateJsonElement(jsonObj.get("RatingType"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!RemoteImageInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'RemoteImageInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<RemoteImageInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(RemoteImageInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<RemoteImageInfo>() {
           @Override
           public void write(JsonWriter out, RemoteImageInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public RemoteImageInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of RemoteImageInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of RemoteImageInfo
   * @throws IOException if the JSON string is invalid with respect to RemoteImageInfo
   */
  public static RemoteImageInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, RemoteImageInfo.class);
  }

  /**
   * Convert an instance of RemoteImageInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

