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
 * ConfigImageTypes
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ConfigImageTypes {
  public static final String SERIALIZED_NAME_BACKDROP_SIZES = "BackdropSizes";
  @SerializedName(SERIALIZED_NAME_BACKDROP_SIZES)
  @javax.annotation.Nullable
  private List<String> backdropSizes;

  public static final String SERIALIZED_NAME_BASE_URL = "BaseUrl";
  @SerializedName(SERIALIZED_NAME_BASE_URL)
  @javax.annotation.Nullable
  private String baseUrl;

  public static final String SERIALIZED_NAME_LOGO_SIZES = "LogoSizes";
  @SerializedName(SERIALIZED_NAME_LOGO_SIZES)
  @javax.annotation.Nullable
  private List<String> logoSizes;

  public static final String SERIALIZED_NAME_POSTER_SIZES = "PosterSizes";
  @SerializedName(SERIALIZED_NAME_POSTER_SIZES)
  @javax.annotation.Nullable
  private List<String> posterSizes;

  public static final String SERIALIZED_NAME_PROFILE_SIZES = "ProfileSizes";
  @SerializedName(SERIALIZED_NAME_PROFILE_SIZES)
  @javax.annotation.Nullable
  private List<String> profileSizes;

  public static final String SERIALIZED_NAME_SECURE_BASE_URL = "SecureBaseUrl";
  @SerializedName(SERIALIZED_NAME_SECURE_BASE_URL)
  @javax.annotation.Nullable
  private String secureBaseUrl;

  public static final String SERIALIZED_NAME_STILL_SIZES = "StillSizes";
  @SerializedName(SERIALIZED_NAME_STILL_SIZES)
  @javax.annotation.Nullable
  private List<String> stillSizes;

  public ConfigImageTypes() {
  }

  public ConfigImageTypes backdropSizes(@javax.annotation.Nullable List<String> backdropSizes) {
    this.backdropSizes = backdropSizes;
    return this;
  }

  public ConfigImageTypes addBackdropSizesItem(String backdropSizesItem) {
    if (this.backdropSizes == null) {
      this.backdropSizes = new ArrayList<>();
    }
    this.backdropSizes.add(backdropSizesItem);
    return this;
  }

  /**
   * Get backdropSizes
   * @return backdropSizes
   */
  @javax.annotation.Nullable
  public List<String> getBackdropSizes() {
    return backdropSizes;
  }

  public void setBackdropSizes(@javax.annotation.Nullable List<String> backdropSizes) {
    this.backdropSizes = backdropSizes;
  }


  public ConfigImageTypes baseUrl(@javax.annotation.Nullable String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  /**
   * Get baseUrl
   * @return baseUrl
   */
  @javax.annotation.Nullable
  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(@javax.annotation.Nullable String baseUrl) {
    this.baseUrl = baseUrl;
  }


  public ConfigImageTypes logoSizes(@javax.annotation.Nullable List<String> logoSizes) {
    this.logoSizes = logoSizes;
    return this;
  }

  public ConfigImageTypes addLogoSizesItem(String logoSizesItem) {
    if (this.logoSizes == null) {
      this.logoSizes = new ArrayList<>();
    }
    this.logoSizes.add(logoSizesItem);
    return this;
  }

  /**
   * Get logoSizes
   * @return logoSizes
   */
  @javax.annotation.Nullable
  public List<String> getLogoSizes() {
    return logoSizes;
  }

  public void setLogoSizes(@javax.annotation.Nullable List<String> logoSizes) {
    this.logoSizes = logoSizes;
  }


  public ConfigImageTypes posterSizes(@javax.annotation.Nullable List<String> posterSizes) {
    this.posterSizes = posterSizes;
    return this;
  }

  public ConfigImageTypes addPosterSizesItem(String posterSizesItem) {
    if (this.posterSizes == null) {
      this.posterSizes = new ArrayList<>();
    }
    this.posterSizes.add(posterSizesItem);
    return this;
  }

  /**
   * Get posterSizes
   * @return posterSizes
   */
  @javax.annotation.Nullable
  public List<String> getPosterSizes() {
    return posterSizes;
  }

  public void setPosterSizes(@javax.annotation.Nullable List<String> posterSizes) {
    this.posterSizes = posterSizes;
  }


  public ConfigImageTypes profileSizes(@javax.annotation.Nullable List<String> profileSizes) {
    this.profileSizes = profileSizes;
    return this;
  }

  public ConfigImageTypes addProfileSizesItem(String profileSizesItem) {
    if (this.profileSizes == null) {
      this.profileSizes = new ArrayList<>();
    }
    this.profileSizes.add(profileSizesItem);
    return this;
  }

  /**
   * Get profileSizes
   * @return profileSizes
   */
  @javax.annotation.Nullable
  public List<String> getProfileSizes() {
    return profileSizes;
  }

  public void setProfileSizes(@javax.annotation.Nullable List<String> profileSizes) {
    this.profileSizes = profileSizes;
  }


  public ConfigImageTypes secureBaseUrl(@javax.annotation.Nullable String secureBaseUrl) {
    this.secureBaseUrl = secureBaseUrl;
    return this;
  }

  /**
   * Get secureBaseUrl
   * @return secureBaseUrl
   */
  @javax.annotation.Nullable
  public String getSecureBaseUrl() {
    return secureBaseUrl;
  }

  public void setSecureBaseUrl(@javax.annotation.Nullable String secureBaseUrl) {
    this.secureBaseUrl = secureBaseUrl;
  }


  public ConfigImageTypes stillSizes(@javax.annotation.Nullable List<String> stillSizes) {
    this.stillSizes = stillSizes;
    return this;
  }

  public ConfigImageTypes addStillSizesItem(String stillSizesItem) {
    if (this.stillSizes == null) {
      this.stillSizes = new ArrayList<>();
    }
    this.stillSizes.add(stillSizesItem);
    return this;
  }

  /**
   * Get stillSizes
   * @return stillSizes
   */
  @javax.annotation.Nullable
  public List<String> getStillSizes() {
    return stillSizes;
  }

  public void setStillSizes(@javax.annotation.Nullable List<String> stillSizes) {
    this.stillSizes = stillSizes;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigImageTypes configImageTypes = (ConfigImageTypes) o;
    return Objects.equals(this.backdropSizes, configImageTypes.backdropSizes) &&
        Objects.equals(this.baseUrl, configImageTypes.baseUrl) &&
        Objects.equals(this.logoSizes, configImageTypes.logoSizes) &&
        Objects.equals(this.posterSizes, configImageTypes.posterSizes) &&
        Objects.equals(this.profileSizes, configImageTypes.profileSizes) &&
        Objects.equals(this.secureBaseUrl, configImageTypes.secureBaseUrl) &&
        Objects.equals(this.stillSizes, configImageTypes.stillSizes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(backdropSizes, baseUrl, logoSizes, posterSizes, profileSizes, secureBaseUrl, stillSizes);
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
    sb.append("class ConfigImageTypes {\n");
    sb.append("    backdropSizes: ").append(toIndentedString(backdropSizes)).append("\n");
    sb.append("    baseUrl: ").append(toIndentedString(baseUrl)).append("\n");
    sb.append("    logoSizes: ").append(toIndentedString(logoSizes)).append("\n");
    sb.append("    posterSizes: ").append(toIndentedString(posterSizes)).append("\n");
    sb.append("    profileSizes: ").append(toIndentedString(profileSizes)).append("\n");
    sb.append("    secureBaseUrl: ").append(toIndentedString(secureBaseUrl)).append("\n");
    sb.append("    stillSizes: ").append(toIndentedString(stillSizes)).append("\n");
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
    openapiFields.add("BackdropSizes");
    openapiFields.add("BaseUrl");
    openapiFields.add("LogoSizes");
    openapiFields.add("PosterSizes");
    openapiFields.add("ProfileSizes");
    openapiFields.add("SecureBaseUrl");
    openapiFields.add("StillSizes");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ConfigImageTypes
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ConfigImageTypes.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ConfigImageTypes is not found in the empty JSON string", ConfigImageTypes.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ConfigImageTypes.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ConfigImageTypes` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // ensure the optional json data is an array if present
      if (jsonObj.get("BackdropSizes") != null && !jsonObj.get("BackdropSizes").isJsonNull() && !jsonObj.get("BackdropSizes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `BackdropSizes` to be an array in the JSON string but got `%s`", jsonObj.get("BackdropSizes").toString()));
      }
      if ((jsonObj.get("BaseUrl") != null && !jsonObj.get("BaseUrl").isJsonNull()) && !jsonObj.get("BaseUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `BaseUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("BaseUrl").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("LogoSizes") != null && !jsonObj.get("LogoSizes").isJsonNull() && !jsonObj.get("LogoSizes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `LogoSizes` to be an array in the JSON string but got `%s`", jsonObj.get("LogoSizes").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("PosterSizes") != null && !jsonObj.get("PosterSizes").isJsonNull() && !jsonObj.get("PosterSizes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `PosterSizes` to be an array in the JSON string but got `%s`", jsonObj.get("PosterSizes").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ProfileSizes") != null && !jsonObj.get("ProfileSizes").isJsonNull() && !jsonObj.get("ProfileSizes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ProfileSizes` to be an array in the JSON string but got `%s`", jsonObj.get("ProfileSizes").toString()));
      }
      if ((jsonObj.get("SecureBaseUrl") != null && !jsonObj.get("SecureBaseUrl").isJsonNull()) && !jsonObj.get("SecureBaseUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SecureBaseUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SecureBaseUrl").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("StillSizes") != null && !jsonObj.get("StillSizes").isJsonNull() && !jsonObj.get("StillSizes").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `StillSizes` to be an array in the JSON string but got `%s`", jsonObj.get("StillSizes").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ConfigImageTypes.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ConfigImageTypes' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ConfigImageTypes> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ConfigImageTypes.class));

       return (TypeAdapter<T>) new TypeAdapter<ConfigImageTypes>() {
           @Override
           public void write(JsonWriter out, ConfigImageTypes value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ConfigImageTypes read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ConfigImageTypes given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ConfigImageTypes
   * @throws IOException if the JSON string is invalid with respect to ConfigImageTypes
   */
  public static ConfigImageTypes fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ConfigImageTypes.class);
  }

  /**
   * Convert an instance of ConfigImageTypes to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

