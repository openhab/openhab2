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
import org.openapitools.client.model.NameValuePair;
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
 * ListingsProviderInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ListingsProviderInfo {
  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private String id;

  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private String type;

  public static final String SERIALIZED_NAME_USERNAME = "Username";
  @SerializedName(SERIALIZED_NAME_USERNAME)
  @javax.annotation.Nullable
  private String username;

  public static final String SERIALIZED_NAME_PASSWORD = "Password";
  @SerializedName(SERIALIZED_NAME_PASSWORD)
  @javax.annotation.Nullable
  private String password;

  public static final String SERIALIZED_NAME_LISTINGS_ID = "ListingsId";
  @SerializedName(SERIALIZED_NAME_LISTINGS_ID)
  @javax.annotation.Nullable
  private String listingsId;

  public static final String SERIALIZED_NAME_ZIP_CODE = "ZipCode";
  @SerializedName(SERIALIZED_NAME_ZIP_CODE)
  @javax.annotation.Nullable
  private String zipCode;

  public static final String SERIALIZED_NAME_COUNTRY = "Country";
  @SerializedName(SERIALIZED_NAME_COUNTRY)
  @javax.annotation.Nullable
  private String country;

  public static final String SERIALIZED_NAME_PATH = "Path";
  @SerializedName(SERIALIZED_NAME_PATH)
  @javax.annotation.Nullable
  private String path;

  public static final String SERIALIZED_NAME_ENABLED_TUNERS = "EnabledTuners";
  @SerializedName(SERIALIZED_NAME_ENABLED_TUNERS)
  @javax.annotation.Nullable
  private List<String> enabledTuners;

  public static final String SERIALIZED_NAME_ENABLE_ALL_TUNERS = "EnableAllTuners";
  @SerializedName(SERIALIZED_NAME_ENABLE_ALL_TUNERS)
  @javax.annotation.Nullable
  private Boolean enableAllTuners;

  public static final String SERIALIZED_NAME_NEWS_CATEGORIES = "NewsCategories";
  @SerializedName(SERIALIZED_NAME_NEWS_CATEGORIES)
  @javax.annotation.Nullable
  private List<String> newsCategories;

  public static final String SERIALIZED_NAME_SPORTS_CATEGORIES = "SportsCategories";
  @SerializedName(SERIALIZED_NAME_SPORTS_CATEGORIES)
  @javax.annotation.Nullable
  private List<String> sportsCategories;

  public static final String SERIALIZED_NAME_KIDS_CATEGORIES = "KidsCategories";
  @SerializedName(SERIALIZED_NAME_KIDS_CATEGORIES)
  @javax.annotation.Nullable
  private List<String> kidsCategories;

  public static final String SERIALIZED_NAME_MOVIE_CATEGORIES = "MovieCategories";
  @SerializedName(SERIALIZED_NAME_MOVIE_CATEGORIES)
  @javax.annotation.Nullable
  private List<String> movieCategories;

  public static final String SERIALIZED_NAME_CHANNEL_MAPPINGS = "ChannelMappings";
  @SerializedName(SERIALIZED_NAME_CHANNEL_MAPPINGS)
  @javax.annotation.Nullable
  private List<NameValuePair> channelMappings;

  public static final String SERIALIZED_NAME_MOVIE_PREFIX = "MoviePrefix";
  @SerializedName(SERIALIZED_NAME_MOVIE_PREFIX)
  @javax.annotation.Nullable
  private String moviePrefix;

  public static final String SERIALIZED_NAME_PREFERRED_LANGUAGE = "PreferredLanguage";
  @SerializedName(SERIALIZED_NAME_PREFERRED_LANGUAGE)
  @javax.annotation.Nullable
  private String preferredLanguage;

  public static final String SERIALIZED_NAME_USER_AGENT = "UserAgent";
  @SerializedName(SERIALIZED_NAME_USER_AGENT)
  @javax.annotation.Nullable
  private String userAgent;

  public ListingsProviderInfo() {
  }

  public ListingsProviderInfo id(@javax.annotation.Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @javax.annotation.Nullable
  public String getId() {
    return id;
  }

  public void setId(@javax.annotation.Nullable String id) {
    this.id = id;
  }


  public ListingsProviderInfo type(@javax.annotation.Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @javax.annotation.Nullable
  public String getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable String type) {
    this.type = type;
  }


  public ListingsProviderInfo username(@javax.annotation.Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  @javax.annotation.Nullable
  public String getUsername() {
    return username;
  }

  public void setUsername(@javax.annotation.Nullable String username) {
    this.username = username;
  }


  public ListingsProviderInfo password(@javax.annotation.Nullable String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  @javax.annotation.Nullable
  public String getPassword() {
    return password;
  }

  public void setPassword(@javax.annotation.Nullable String password) {
    this.password = password;
  }


  public ListingsProviderInfo listingsId(@javax.annotation.Nullable String listingsId) {
    this.listingsId = listingsId;
    return this;
  }

  /**
   * Get listingsId
   * @return listingsId
   */
  @javax.annotation.Nullable
  public String getListingsId() {
    return listingsId;
  }

  public void setListingsId(@javax.annotation.Nullable String listingsId) {
    this.listingsId = listingsId;
  }


  public ListingsProviderInfo zipCode(@javax.annotation.Nullable String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  /**
   * Get zipCode
   * @return zipCode
   */
  @javax.annotation.Nullable
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(@javax.annotation.Nullable String zipCode) {
    this.zipCode = zipCode;
  }


  public ListingsProviderInfo country(@javax.annotation.Nullable String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
   */
  @javax.annotation.Nullable
  public String getCountry() {
    return country;
  }

  public void setCountry(@javax.annotation.Nullable String country) {
    this.country = country;
  }


  public ListingsProviderInfo path(@javax.annotation.Nullable String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
   */
  @javax.annotation.Nullable
  public String getPath() {
    return path;
  }

  public void setPath(@javax.annotation.Nullable String path) {
    this.path = path;
  }


  public ListingsProviderInfo enabledTuners(@javax.annotation.Nullable List<String> enabledTuners) {
    this.enabledTuners = enabledTuners;
    return this;
  }

  public ListingsProviderInfo addEnabledTunersItem(String enabledTunersItem) {
    if (this.enabledTuners == null) {
      this.enabledTuners = new ArrayList<>();
    }
    this.enabledTuners.add(enabledTunersItem);
    return this;
  }

  /**
   * Get enabledTuners
   * @return enabledTuners
   */
  @javax.annotation.Nullable
  public List<String> getEnabledTuners() {
    return enabledTuners;
  }

  public void setEnabledTuners(@javax.annotation.Nullable List<String> enabledTuners) {
    this.enabledTuners = enabledTuners;
  }


  public ListingsProviderInfo enableAllTuners(@javax.annotation.Nullable Boolean enableAllTuners) {
    this.enableAllTuners = enableAllTuners;
    return this;
  }

  /**
   * Get enableAllTuners
   * @return enableAllTuners
   */
  @javax.annotation.Nullable
  public Boolean getEnableAllTuners() {
    return enableAllTuners;
  }

  public void setEnableAllTuners(@javax.annotation.Nullable Boolean enableAllTuners) {
    this.enableAllTuners = enableAllTuners;
  }


  public ListingsProviderInfo newsCategories(@javax.annotation.Nullable List<String> newsCategories) {
    this.newsCategories = newsCategories;
    return this;
  }

  public ListingsProviderInfo addNewsCategoriesItem(String newsCategoriesItem) {
    if (this.newsCategories == null) {
      this.newsCategories = new ArrayList<>();
    }
    this.newsCategories.add(newsCategoriesItem);
    return this;
  }

  /**
   * Get newsCategories
   * @return newsCategories
   */
  @javax.annotation.Nullable
  public List<String> getNewsCategories() {
    return newsCategories;
  }

  public void setNewsCategories(@javax.annotation.Nullable List<String> newsCategories) {
    this.newsCategories = newsCategories;
  }


  public ListingsProviderInfo sportsCategories(@javax.annotation.Nullable List<String> sportsCategories) {
    this.sportsCategories = sportsCategories;
    return this;
  }

  public ListingsProviderInfo addSportsCategoriesItem(String sportsCategoriesItem) {
    if (this.sportsCategories == null) {
      this.sportsCategories = new ArrayList<>();
    }
    this.sportsCategories.add(sportsCategoriesItem);
    return this;
  }

  /**
   * Get sportsCategories
   * @return sportsCategories
   */
  @javax.annotation.Nullable
  public List<String> getSportsCategories() {
    return sportsCategories;
  }

  public void setSportsCategories(@javax.annotation.Nullable List<String> sportsCategories) {
    this.sportsCategories = sportsCategories;
  }


  public ListingsProviderInfo kidsCategories(@javax.annotation.Nullable List<String> kidsCategories) {
    this.kidsCategories = kidsCategories;
    return this;
  }

  public ListingsProviderInfo addKidsCategoriesItem(String kidsCategoriesItem) {
    if (this.kidsCategories == null) {
      this.kidsCategories = new ArrayList<>();
    }
    this.kidsCategories.add(kidsCategoriesItem);
    return this;
  }

  /**
   * Get kidsCategories
   * @return kidsCategories
   */
  @javax.annotation.Nullable
  public List<String> getKidsCategories() {
    return kidsCategories;
  }

  public void setKidsCategories(@javax.annotation.Nullable List<String> kidsCategories) {
    this.kidsCategories = kidsCategories;
  }


  public ListingsProviderInfo movieCategories(@javax.annotation.Nullable List<String> movieCategories) {
    this.movieCategories = movieCategories;
    return this;
  }

  public ListingsProviderInfo addMovieCategoriesItem(String movieCategoriesItem) {
    if (this.movieCategories == null) {
      this.movieCategories = new ArrayList<>();
    }
    this.movieCategories.add(movieCategoriesItem);
    return this;
  }

  /**
   * Get movieCategories
   * @return movieCategories
   */
  @javax.annotation.Nullable
  public List<String> getMovieCategories() {
    return movieCategories;
  }

  public void setMovieCategories(@javax.annotation.Nullable List<String> movieCategories) {
    this.movieCategories = movieCategories;
  }


  public ListingsProviderInfo channelMappings(@javax.annotation.Nullable List<NameValuePair> channelMappings) {
    this.channelMappings = channelMappings;
    return this;
  }

  public ListingsProviderInfo addChannelMappingsItem(NameValuePair channelMappingsItem) {
    if (this.channelMappings == null) {
      this.channelMappings = new ArrayList<>();
    }
    this.channelMappings.add(channelMappingsItem);
    return this;
  }

  /**
   * Get channelMappings
   * @return channelMappings
   */
  @javax.annotation.Nullable
  public List<NameValuePair> getChannelMappings() {
    return channelMappings;
  }

  public void setChannelMappings(@javax.annotation.Nullable List<NameValuePair> channelMappings) {
    this.channelMappings = channelMappings;
  }


  public ListingsProviderInfo moviePrefix(@javax.annotation.Nullable String moviePrefix) {
    this.moviePrefix = moviePrefix;
    return this;
  }

  /**
   * Get moviePrefix
   * @return moviePrefix
   */
  @javax.annotation.Nullable
  public String getMoviePrefix() {
    return moviePrefix;
  }

  public void setMoviePrefix(@javax.annotation.Nullable String moviePrefix) {
    this.moviePrefix = moviePrefix;
  }


  public ListingsProviderInfo preferredLanguage(@javax.annotation.Nullable String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
    return this;
  }

  /**
   * Get preferredLanguage
   * @return preferredLanguage
   */
  @javax.annotation.Nullable
  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(@javax.annotation.Nullable String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }


  public ListingsProviderInfo userAgent(@javax.annotation.Nullable String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  /**
   * Get userAgent
   * @return userAgent
   */
  @javax.annotation.Nullable
  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(@javax.annotation.Nullable String userAgent) {
    this.userAgent = userAgent;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListingsProviderInfo listingsProviderInfo = (ListingsProviderInfo) o;
    return Objects.equals(this.id, listingsProviderInfo.id) &&
        Objects.equals(this.type, listingsProviderInfo.type) &&
        Objects.equals(this.username, listingsProviderInfo.username) &&
        Objects.equals(this.password, listingsProviderInfo.password) &&
        Objects.equals(this.listingsId, listingsProviderInfo.listingsId) &&
        Objects.equals(this.zipCode, listingsProviderInfo.zipCode) &&
        Objects.equals(this.country, listingsProviderInfo.country) &&
        Objects.equals(this.path, listingsProviderInfo.path) &&
        Objects.equals(this.enabledTuners, listingsProviderInfo.enabledTuners) &&
        Objects.equals(this.enableAllTuners, listingsProviderInfo.enableAllTuners) &&
        Objects.equals(this.newsCategories, listingsProviderInfo.newsCategories) &&
        Objects.equals(this.sportsCategories, listingsProviderInfo.sportsCategories) &&
        Objects.equals(this.kidsCategories, listingsProviderInfo.kidsCategories) &&
        Objects.equals(this.movieCategories, listingsProviderInfo.movieCategories) &&
        Objects.equals(this.channelMappings, listingsProviderInfo.channelMappings) &&
        Objects.equals(this.moviePrefix, listingsProviderInfo.moviePrefix) &&
        Objects.equals(this.preferredLanguage, listingsProviderInfo.preferredLanguage) &&
        Objects.equals(this.userAgent, listingsProviderInfo.userAgent);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, username, password, listingsId, zipCode, country, path, enabledTuners, enableAllTuners, newsCategories, sportsCategories, kidsCategories, movieCategories, channelMappings, moviePrefix, preferredLanguage, userAgent);
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
    sb.append("class ListingsProviderInfo {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    listingsId: ").append(toIndentedString(listingsId)).append("\n");
    sb.append("    zipCode: ").append(toIndentedString(zipCode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    enabledTuners: ").append(toIndentedString(enabledTuners)).append("\n");
    sb.append("    enableAllTuners: ").append(toIndentedString(enableAllTuners)).append("\n");
    sb.append("    newsCategories: ").append(toIndentedString(newsCategories)).append("\n");
    sb.append("    sportsCategories: ").append(toIndentedString(sportsCategories)).append("\n");
    sb.append("    kidsCategories: ").append(toIndentedString(kidsCategories)).append("\n");
    sb.append("    movieCategories: ").append(toIndentedString(movieCategories)).append("\n");
    sb.append("    channelMappings: ").append(toIndentedString(channelMappings)).append("\n");
    sb.append("    moviePrefix: ").append(toIndentedString(moviePrefix)).append("\n");
    sb.append("    preferredLanguage: ").append(toIndentedString(preferredLanguage)).append("\n");
    sb.append("    userAgent: ").append(toIndentedString(userAgent)).append("\n");
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
    openapiFields.add("Id");
    openapiFields.add("Type");
    openapiFields.add("Username");
    openapiFields.add("Password");
    openapiFields.add("ListingsId");
    openapiFields.add("ZipCode");
    openapiFields.add("Country");
    openapiFields.add("Path");
    openapiFields.add("EnabledTuners");
    openapiFields.add("EnableAllTuners");
    openapiFields.add("NewsCategories");
    openapiFields.add("SportsCategories");
    openapiFields.add("KidsCategories");
    openapiFields.add("MovieCategories");
    openapiFields.add("ChannelMappings");
    openapiFields.add("MoviePrefix");
    openapiFields.add("PreferredLanguage");
    openapiFields.add("UserAgent");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ListingsProviderInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ListingsProviderInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ListingsProviderInfo is not found in the empty JSON string", ListingsProviderInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ListingsProviderInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ListingsProviderInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Id") != null && !jsonObj.get("Id").isJsonNull()) && !jsonObj.get("Id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Id").toString()));
      }
      if ((jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) && !jsonObj.get("Type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Type").toString()));
      }
      if ((jsonObj.get("Username") != null && !jsonObj.get("Username").isJsonNull()) && !jsonObj.get("Username").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Username` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Username").toString()));
      }
      if ((jsonObj.get("Password") != null && !jsonObj.get("Password").isJsonNull()) && !jsonObj.get("Password").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Password` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Password").toString()));
      }
      if ((jsonObj.get("ListingsId") != null && !jsonObj.get("ListingsId").isJsonNull()) && !jsonObj.get("ListingsId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ListingsId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ListingsId").toString()));
      }
      if ((jsonObj.get("ZipCode") != null && !jsonObj.get("ZipCode").isJsonNull()) && !jsonObj.get("ZipCode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ZipCode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ZipCode").toString()));
      }
      if ((jsonObj.get("Country") != null && !jsonObj.get("Country").isJsonNull()) && !jsonObj.get("Country").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Country` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Country").toString()));
      }
      if ((jsonObj.get("Path") != null && !jsonObj.get("Path").isJsonNull()) && !jsonObj.get("Path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Path").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("EnabledTuners") != null && !jsonObj.get("EnabledTuners").isJsonNull() && !jsonObj.get("EnabledTuners").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `EnabledTuners` to be an array in the JSON string but got `%s`", jsonObj.get("EnabledTuners").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("NewsCategories") != null && !jsonObj.get("NewsCategories").isJsonNull() && !jsonObj.get("NewsCategories").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `NewsCategories` to be an array in the JSON string but got `%s`", jsonObj.get("NewsCategories").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("SportsCategories") != null && !jsonObj.get("SportsCategories").isJsonNull() && !jsonObj.get("SportsCategories").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `SportsCategories` to be an array in the JSON string but got `%s`", jsonObj.get("SportsCategories").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("KidsCategories") != null && !jsonObj.get("KidsCategories").isJsonNull() && !jsonObj.get("KidsCategories").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `KidsCategories` to be an array in the JSON string but got `%s`", jsonObj.get("KidsCategories").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("MovieCategories") != null && !jsonObj.get("MovieCategories").isJsonNull() && !jsonObj.get("MovieCategories").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `MovieCategories` to be an array in the JSON string but got `%s`", jsonObj.get("MovieCategories").toString()));
      }
      if (jsonObj.get("ChannelMappings") != null && !jsonObj.get("ChannelMappings").isJsonNull()) {
        JsonArray jsonArraychannelMappings = jsonObj.getAsJsonArray("ChannelMappings");
        if (jsonArraychannelMappings != null) {
          // ensure the json data is an array
          if (!jsonObj.get("ChannelMappings").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `ChannelMappings` to be an array in the JSON string but got `%s`", jsonObj.get("ChannelMappings").toString()));
          }

          // validate the optional field `ChannelMappings` (array)
          for (int i = 0; i < jsonArraychannelMappings.size(); i++) {
            NameValuePair.validateJsonElement(jsonArraychannelMappings.get(i));
          };
        }
      }
      if ((jsonObj.get("MoviePrefix") != null && !jsonObj.get("MoviePrefix").isJsonNull()) && !jsonObj.get("MoviePrefix").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MoviePrefix` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MoviePrefix").toString()));
      }
      if ((jsonObj.get("PreferredLanguage") != null && !jsonObj.get("PreferredLanguage").isJsonNull()) && !jsonObj.get("PreferredLanguage").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PreferredLanguage` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PreferredLanguage").toString()));
      }
      if ((jsonObj.get("UserAgent") != null && !jsonObj.get("UserAgent").isJsonNull()) && !jsonObj.get("UserAgent").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `UserAgent` to be a primitive type in the JSON string but got `%s`", jsonObj.get("UserAgent").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ListingsProviderInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ListingsProviderInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ListingsProviderInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ListingsProviderInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<ListingsProviderInfo>() {
           @Override
           public void write(JsonWriter out, ListingsProviderInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ListingsProviderInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ListingsProviderInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ListingsProviderInfo
   * @throws IOException if the JSON string is invalid with respect to ListingsProviderInfo
   */
  public static ListingsProviderInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ListingsProviderInfo.class);
  }

  /**
   * Convert an instance of ListingsProviderInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

