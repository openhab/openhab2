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
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.client.model.BookInfo;
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
 * BookInfoRemoteSearchQuery
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class BookInfoRemoteSearchQuery {
  public static final String SERIALIZED_NAME_SEARCH_INFO = "SearchInfo";
  @SerializedName(SERIALIZED_NAME_SEARCH_INFO)
  @javax.annotation.Nullable
  private BookInfo searchInfo;

  public static final String SERIALIZED_NAME_ITEM_ID = "ItemId";
  @SerializedName(SERIALIZED_NAME_ITEM_ID)
  @javax.annotation.Nullable
  private UUID itemId;

  public static final String SERIALIZED_NAME_SEARCH_PROVIDER_NAME = "SearchProviderName";
  @SerializedName(SERIALIZED_NAME_SEARCH_PROVIDER_NAME)
  @javax.annotation.Nullable
  private String searchProviderName;

  public static final String SERIALIZED_NAME_INCLUDE_DISABLED_PROVIDERS = "IncludeDisabledProviders";
  @SerializedName(SERIALIZED_NAME_INCLUDE_DISABLED_PROVIDERS)
  @javax.annotation.Nullable
  private Boolean includeDisabledProviders;

  public BookInfoRemoteSearchQuery() {
  }

  public BookInfoRemoteSearchQuery searchInfo(@javax.annotation.Nullable BookInfo searchInfo) {
    this.searchInfo = searchInfo;
    return this;
  }

  /**
   * Get searchInfo
   * @return searchInfo
   */
  @javax.annotation.Nullable
  public BookInfo getSearchInfo() {
    return searchInfo;
  }

  public void setSearchInfo(@javax.annotation.Nullable BookInfo searchInfo) {
    this.searchInfo = searchInfo;
  }


  public BookInfoRemoteSearchQuery itemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @javax.annotation.Nullable
  public UUID getItemId() {
    return itemId;
  }

  public void setItemId(@javax.annotation.Nullable UUID itemId) {
    this.itemId = itemId;
  }


  public BookInfoRemoteSearchQuery searchProviderName(@javax.annotation.Nullable String searchProviderName) {
    this.searchProviderName = searchProviderName;
    return this;
  }

  /**
   * Gets or sets the provider name to search within if set.
   * @return searchProviderName
   */
  @javax.annotation.Nullable
  public String getSearchProviderName() {
    return searchProviderName;
  }

  public void setSearchProviderName(@javax.annotation.Nullable String searchProviderName) {
    this.searchProviderName = searchProviderName;
  }


  public BookInfoRemoteSearchQuery includeDisabledProviders(@javax.annotation.Nullable Boolean includeDisabledProviders) {
    this.includeDisabledProviders = includeDisabledProviders;
    return this;
  }

  /**
   * Gets or sets a value indicating whether disabled providers should be included.
   * @return includeDisabledProviders
   */
  @javax.annotation.Nullable
  public Boolean getIncludeDisabledProviders() {
    return includeDisabledProviders;
  }

  public void setIncludeDisabledProviders(@javax.annotation.Nullable Boolean includeDisabledProviders) {
    this.includeDisabledProviders = includeDisabledProviders;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookInfoRemoteSearchQuery bookInfoRemoteSearchQuery = (BookInfoRemoteSearchQuery) o;
    return Objects.equals(this.searchInfo, bookInfoRemoteSearchQuery.searchInfo) &&
        Objects.equals(this.itemId, bookInfoRemoteSearchQuery.itemId) &&
        Objects.equals(this.searchProviderName, bookInfoRemoteSearchQuery.searchProviderName) &&
        Objects.equals(this.includeDisabledProviders, bookInfoRemoteSearchQuery.includeDisabledProviders);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(searchInfo, itemId, searchProviderName, includeDisabledProviders);
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
    sb.append("class BookInfoRemoteSearchQuery {\n");
    sb.append("    searchInfo: ").append(toIndentedString(searchInfo)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    searchProviderName: ").append(toIndentedString(searchProviderName)).append("\n");
    sb.append("    includeDisabledProviders: ").append(toIndentedString(includeDisabledProviders)).append("\n");
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
    openapiFields.add("SearchInfo");
    openapiFields.add("ItemId");
    openapiFields.add("SearchProviderName");
    openapiFields.add("IncludeDisabledProviders");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to BookInfoRemoteSearchQuery
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!BookInfoRemoteSearchQuery.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in BookInfoRemoteSearchQuery is not found in the empty JSON string", BookInfoRemoteSearchQuery.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!BookInfoRemoteSearchQuery.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `BookInfoRemoteSearchQuery` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `SearchInfo`
      if (jsonObj.get("SearchInfo") != null && !jsonObj.get("SearchInfo").isJsonNull()) {
        BookInfo.validateJsonElement(jsonObj.get("SearchInfo"));
      }
      if ((jsonObj.get("ItemId") != null && !jsonObj.get("ItemId").isJsonNull()) && !jsonObj.get("ItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ItemId").toString()));
      }
      if ((jsonObj.get("SearchProviderName") != null && !jsonObj.get("SearchProviderName").isJsonNull()) && !jsonObj.get("SearchProviderName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SearchProviderName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SearchProviderName").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!BookInfoRemoteSearchQuery.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'BookInfoRemoteSearchQuery' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<BookInfoRemoteSearchQuery> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(BookInfoRemoteSearchQuery.class));

       return (TypeAdapter<T>) new TypeAdapter<BookInfoRemoteSearchQuery>() {
           @Override
           public void write(JsonWriter out, BookInfoRemoteSearchQuery value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public BookInfoRemoteSearchQuery read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of BookInfoRemoteSearchQuery given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of BookInfoRemoteSearchQuery
   * @throws IOException if the JSON string is invalid with respect to BookInfoRemoteSearchQuery
   */
  public static BookInfoRemoteSearchQuery fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, BookInfoRemoteSearchQuery.class);
  }

  /**
   * Convert an instance of BookInfoRemoteSearchQuery to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

