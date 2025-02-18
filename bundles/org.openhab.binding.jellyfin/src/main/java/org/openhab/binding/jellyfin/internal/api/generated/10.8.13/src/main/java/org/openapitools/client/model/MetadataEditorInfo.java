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
import org.openapitools.client.model.CountryInfo;
import org.openapitools.client.model.CultureDto;
import org.openapitools.client.model.ExternalIdInfo;
import org.openapitools.client.model.NameValuePair;
import org.openapitools.client.model.ParentalRating;
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
 * MetadataEditorInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class MetadataEditorInfo {
  public static final String SERIALIZED_NAME_PARENTAL_RATING_OPTIONS = "ParentalRatingOptions";
  @SerializedName(SERIALIZED_NAME_PARENTAL_RATING_OPTIONS)
  @javax.annotation.Nullable
  private List<ParentalRating> parentalRatingOptions = new ArrayList<>();

  public static final String SERIALIZED_NAME_COUNTRIES = "Countries";
  @SerializedName(SERIALIZED_NAME_COUNTRIES)
  @javax.annotation.Nullable
  private List<CountryInfo> countries = new ArrayList<>();

  public static final String SERIALIZED_NAME_CULTURES = "Cultures";
  @SerializedName(SERIALIZED_NAME_CULTURES)
  @javax.annotation.Nullable
  private List<CultureDto> cultures = new ArrayList<>();

  public static final String SERIALIZED_NAME_EXTERNAL_ID_INFOS = "ExternalIdInfos";
  @SerializedName(SERIALIZED_NAME_EXTERNAL_ID_INFOS)
  @javax.annotation.Nullable
  private List<ExternalIdInfo> externalIdInfos = new ArrayList<>();

  public static final String SERIALIZED_NAME_CONTENT_TYPE = "ContentType";
  @SerializedName(SERIALIZED_NAME_CONTENT_TYPE)
  @javax.annotation.Nullable
  private String contentType;

  public static final String SERIALIZED_NAME_CONTENT_TYPE_OPTIONS = "ContentTypeOptions";
  @SerializedName(SERIALIZED_NAME_CONTENT_TYPE_OPTIONS)
  @javax.annotation.Nullable
  private List<NameValuePair> contentTypeOptions = new ArrayList<>();

  public MetadataEditorInfo() {
  }

  public MetadataEditorInfo parentalRatingOptions(@javax.annotation.Nullable List<ParentalRating> parentalRatingOptions) {
    this.parentalRatingOptions = parentalRatingOptions;
    return this;
  }

  public MetadataEditorInfo addParentalRatingOptionsItem(ParentalRating parentalRatingOptionsItem) {
    if (this.parentalRatingOptions == null) {
      this.parentalRatingOptions = new ArrayList<>();
    }
    this.parentalRatingOptions.add(parentalRatingOptionsItem);
    return this;
  }

  /**
   * Get parentalRatingOptions
   * @return parentalRatingOptions
   */
  @javax.annotation.Nullable
  public List<ParentalRating> getParentalRatingOptions() {
    return parentalRatingOptions;
  }

  public void setParentalRatingOptions(@javax.annotation.Nullable List<ParentalRating> parentalRatingOptions) {
    this.parentalRatingOptions = parentalRatingOptions;
  }


  public MetadataEditorInfo countries(@javax.annotation.Nullable List<CountryInfo> countries) {
    this.countries = countries;
    return this;
  }

  public MetadataEditorInfo addCountriesItem(CountryInfo countriesItem) {
    if (this.countries == null) {
      this.countries = new ArrayList<>();
    }
    this.countries.add(countriesItem);
    return this;
  }

  /**
   * Get countries
   * @return countries
   */
  @javax.annotation.Nullable
  public List<CountryInfo> getCountries() {
    return countries;
  }

  public void setCountries(@javax.annotation.Nullable List<CountryInfo> countries) {
    this.countries = countries;
  }


  public MetadataEditorInfo cultures(@javax.annotation.Nullable List<CultureDto> cultures) {
    this.cultures = cultures;
    return this;
  }

  public MetadataEditorInfo addCulturesItem(CultureDto culturesItem) {
    if (this.cultures == null) {
      this.cultures = new ArrayList<>();
    }
    this.cultures.add(culturesItem);
    return this;
  }

  /**
   * Get cultures
   * @return cultures
   */
  @javax.annotation.Nullable
  public List<CultureDto> getCultures() {
    return cultures;
  }

  public void setCultures(@javax.annotation.Nullable List<CultureDto> cultures) {
    this.cultures = cultures;
  }


  public MetadataEditorInfo externalIdInfos(@javax.annotation.Nullable List<ExternalIdInfo> externalIdInfos) {
    this.externalIdInfos = externalIdInfos;
    return this;
  }

  public MetadataEditorInfo addExternalIdInfosItem(ExternalIdInfo externalIdInfosItem) {
    if (this.externalIdInfos == null) {
      this.externalIdInfos = new ArrayList<>();
    }
    this.externalIdInfos.add(externalIdInfosItem);
    return this;
  }

  /**
   * Get externalIdInfos
   * @return externalIdInfos
   */
  @javax.annotation.Nullable
  public List<ExternalIdInfo> getExternalIdInfos() {
    return externalIdInfos;
  }

  public void setExternalIdInfos(@javax.annotation.Nullable List<ExternalIdInfo> externalIdInfos) {
    this.externalIdInfos = externalIdInfos;
  }


  public MetadataEditorInfo contentType(@javax.annotation.Nullable String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * Get contentType
   * @return contentType
   */
  @javax.annotation.Nullable
  public String getContentType() {
    return contentType;
  }

  public void setContentType(@javax.annotation.Nullable String contentType) {
    this.contentType = contentType;
  }


  public MetadataEditorInfo contentTypeOptions(@javax.annotation.Nullable List<NameValuePair> contentTypeOptions) {
    this.contentTypeOptions = contentTypeOptions;
    return this;
  }

  public MetadataEditorInfo addContentTypeOptionsItem(NameValuePair contentTypeOptionsItem) {
    if (this.contentTypeOptions == null) {
      this.contentTypeOptions = new ArrayList<>();
    }
    this.contentTypeOptions.add(contentTypeOptionsItem);
    return this;
  }

  /**
   * Get contentTypeOptions
   * @return contentTypeOptions
   */
  @javax.annotation.Nullable
  public List<NameValuePair> getContentTypeOptions() {
    return contentTypeOptions;
  }

  public void setContentTypeOptions(@javax.annotation.Nullable List<NameValuePair> contentTypeOptions) {
    this.contentTypeOptions = contentTypeOptions;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataEditorInfo metadataEditorInfo = (MetadataEditorInfo) o;
    return Objects.equals(this.parentalRatingOptions, metadataEditorInfo.parentalRatingOptions) &&
        Objects.equals(this.countries, metadataEditorInfo.countries) &&
        Objects.equals(this.cultures, metadataEditorInfo.cultures) &&
        Objects.equals(this.externalIdInfos, metadataEditorInfo.externalIdInfos) &&
        Objects.equals(this.contentType, metadataEditorInfo.contentType) &&
        Objects.equals(this.contentTypeOptions, metadataEditorInfo.contentTypeOptions);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(parentalRatingOptions, countries, cultures, externalIdInfos, contentType, contentTypeOptions);
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
    sb.append("class MetadataEditorInfo {\n");
    sb.append("    parentalRatingOptions: ").append(toIndentedString(parentalRatingOptions)).append("\n");
    sb.append("    countries: ").append(toIndentedString(countries)).append("\n");
    sb.append("    cultures: ").append(toIndentedString(cultures)).append("\n");
    sb.append("    externalIdInfos: ").append(toIndentedString(externalIdInfos)).append("\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    contentTypeOptions: ").append(toIndentedString(contentTypeOptions)).append("\n");
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
    openapiFields.add("ParentalRatingOptions");
    openapiFields.add("Countries");
    openapiFields.add("Cultures");
    openapiFields.add("ExternalIdInfos");
    openapiFields.add("ContentType");
    openapiFields.add("ContentTypeOptions");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to MetadataEditorInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!MetadataEditorInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in MetadataEditorInfo is not found in the empty JSON string", MetadataEditorInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!MetadataEditorInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `MetadataEditorInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if (jsonObj.get("ParentalRatingOptions") != null && !jsonObj.get("ParentalRatingOptions").isJsonNull()) {
        JsonArray jsonArrayparentalRatingOptions = jsonObj.getAsJsonArray("ParentalRatingOptions");
        if (jsonArrayparentalRatingOptions != null) {
          // ensure the json data is an array
          if (!jsonObj.get("ParentalRatingOptions").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `ParentalRatingOptions` to be an array in the JSON string but got `%s`", jsonObj.get("ParentalRatingOptions").toString()));
          }

          // validate the optional field `ParentalRatingOptions` (array)
          for (int i = 0; i < jsonArrayparentalRatingOptions.size(); i++) {
            ParentalRating.validateJsonElement(jsonArrayparentalRatingOptions.get(i));
          };
        }
      }
      if (jsonObj.get("Countries") != null && !jsonObj.get("Countries").isJsonNull()) {
        JsonArray jsonArraycountries = jsonObj.getAsJsonArray("Countries");
        if (jsonArraycountries != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Countries").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Countries` to be an array in the JSON string but got `%s`", jsonObj.get("Countries").toString()));
          }

          // validate the optional field `Countries` (array)
          for (int i = 0; i < jsonArraycountries.size(); i++) {
            CountryInfo.validateJsonElement(jsonArraycountries.get(i));
          };
        }
      }
      if (jsonObj.get("Cultures") != null && !jsonObj.get("Cultures").isJsonNull()) {
        JsonArray jsonArraycultures = jsonObj.getAsJsonArray("Cultures");
        if (jsonArraycultures != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Cultures").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Cultures` to be an array in the JSON string but got `%s`", jsonObj.get("Cultures").toString()));
          }

          // validate the optional field `Cultures` (array)
          for (int i = 0; i < jsonArraycultures.size(); i++) {
            CultureDto.validateJsonElement(jsonArraycultures.get(i));
          };
        }
      }
      if (jsonObj.get("ExternalIdInfos") != null && !jsonObj.get("ExternalIdInfos").isJsonNull()) {
        JsonArray jsonArrayexternalIdInfos = jsonObj.getAsJsonArray("ExternalIdInfos");
        if (jsonArrayexternalIdInfos != null) {
          // ensure the json data is an array
          if (!jsonObj.get("ExternalIdInfos").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `ExternalIdInfos` to be an array in the JSON string but got `%s`", jsonObj.get("ExternalIdInfos").toString()));
          }

          // validate the optional field `ExternalIdInfos` (array)
          for (int i = 0; i < jsonArrayexternalIdInfos.size(); i++) {
            ExternalIdInfo.validateJsonElement(jsonArrayexternalIdInfos.get(i));
          };
        }
      }
      if ((jsonObj.get("ContentType") != null && !jsonObj.get("ContentType").isJsonNull()) && !jsonObj.get("ContentType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ContentType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ContentType").toString()));
      }
      if (jsonObj.get("ContentTypeOptions") != null && !jsonObj.get("ContentTypeOptions").isJsonNull()) {
        JsonArray jsonArraycontentTypeOptions = jsonObj.getAsJsonArray("ContentTypeOptions");
        if (jsonArraycontentTypeOptions != null) {
          // ensure the json data is an array
          if (!jsonObj.get("ContentTypeOptions").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `ContentTypeOptions` to be an array in the JSON string but got `%s`", jsonObj.get("ContentTypeOptions").toString()));
          }

          // validate the optional field `ContentTypeOptions` (array)
          for (int i = 0; i < jsonArraycontentTypeOptions.size(); i++) {
            NameValuePair.validateJsonElement(jsonArraycontentTypeOptions.get(i));
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!MetadataEditorInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'MetadataEditorInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<MetadataEditorInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(MetadataEditorInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<MetadataEditorInfo>() {
           @Override
           public void write(JsonWriter out, MetadataEditorInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public MetadataEditorInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of MetadataEditorInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of MetadataEditorInfo
   * @throws IOException if the JSON string is invalid with respect to MetadataEditorInfo
   */
  public static MetadataEditorInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, MetadataEditorInfo.class);
  }

  /**
   * Convert an instance of MetadataEditorInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

