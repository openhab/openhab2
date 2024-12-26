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
 * Class CultureDto.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class CultureDto {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_DISPLAY_NAME = "DisplayName";
  @SerializedName(SERIALIZED_NAME_DISPLAY_NAME)
  @javax.annotation.Nullable
  private String displayName;

  public static final String SERIALIZED_NAME_TWO_LETTER_I_S_O_LANGUAGE_NAME = "TwoLetterISOLanguageName";
  @SerializedName(SERIALIZED_NAME_TWO_LETTER_I_S_O_LANGUAGE_NAME)
  @javax.annotation.Nullable
  private String twoLetterISOLanguageName;

  public static final String SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAME = "ThreeLetterISOLanguageName";
  @SerializedName(SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAME)
  @javax.annotation.Nullable
  private String threeLetterISOLanguageName;

  public static final String SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAMES = "ThreeLetterISOLanguageNames";
  @SerializedName(SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAMES)
  @javax.annotation.Nullable
  private List<String> threeLetterISOLanguageNames = new ArrayList<>();

  public CultureDto() {
  }

  public CultureDto(
     String threeLetterISOLanguageName
  ) {
    this();
    this.threeLetterISOLanguageName = threeLetterISOLanguageName;
  }

  public CultureDto name(@javax.annotation.Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets the name.
   * @return name
   */
  @javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(@javax.annotation.Nullable String name) {
    this.name = name;
  }


  public CultureDto displayName(@javax.annotation.Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Gets the display name.
   * @return displayName
   */
  @javax.annotation.Nullable
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@javax.annotation.Nullable String displayName) {
    this.displayName = displayName;
  }


  public CultureDto twoLetterISOLanguageName(@javax.annotation.Nullable String twoLetterISOLanguageName) {
    this.twoLetterISOLanguageName = twoLetterISOLanguageName;
    return this;
  }

  /**
   * Gets the name of the two letter ISO language.
   * @return twoLetterISOLanguageName
   */
  @javax.annotation.Nullable
  public String getTwoLetterISOLanguageName() {
    return twoLetterISOLanguageName;
  }

  public void setTwoLetterISOLanguageName(@javax.annotation.Nullable String twoLetterISOLanguageName) {
    this.twoLetterISOLanguageName = twoLetterISOLanguageName;
  }


  /**
   * Gets the name of the three letter ISO language.
   * @return threeLetterISOLanguageName
   */
  @javax.annotation.Nullable
  public String getThreeLetterISOLanguageName() {
    return threeLetterISOLanguageName;
  }



  public CultureDto threeLetterISOLanguageNames(@javax.annotation.Nullable List<String> threeLetterISOLanguageNames) {
    this.threeLetterISOLanguageNames = threeLetterISOLanguageNames;
    return this;
  }

  public CultureDto addThreeLetterISOLanguageNamesItem(String threeLetterISOLanguageNamesItem) {
    if (this.threeLetterISOLanguageNames == null) {
      this.threeLetterISOLanguageNames = new ArrayList<>();
    }
    this.threeLetterISOLanguageNames.add(threeLetterISOLanguageNamesItem);
    return this;
  }

  /**
   * Get threeLetterISOLanguageNames
   * @return threeLetterISOLanguageNames
   */
  @javax.annotation.Nullable
  public List<String> getThreeLetterISOLanguageNames() {
    return threeLetterISOLanguageNames;
  }

  public void setThreeLetterISOLanguageNames(@javax.annotation.Nullable List<String> threeLetterISOLanguageNames) {
    this.threeLetterISOLanguageNames = threeLetterISOLanguageNames;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CultureDto cultureDto = (CultureDto) o;
    return Objects.equals(this.name, cultureDto.name) &&
        Objects.equals(this.displayName, cultureDto.displayName) &&
        Objects.equals(this.twoLetterISOLanguageName, cultureDto.twoLetterISOLanguageName) &&
        Objects.equals(this.threeLetterISOLanguageName, cultureDto.threeLetterISOLanguageName) &&
        Objects.equals(this.threeLetterISOLanguageNames, cultureDto.threeLetterISOLanguageNames);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, displayName, twoLetterISOLanguageName, threeLetterISOLanguageName, threeLetterISOLanguageNames);
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
    sb.append("class CultureDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    twoLetterISOLanguageName: ").append(toIndentedString(twoLetterISOLanguageName)).append("\n");
    sb.append("    threeLetterISOLanguageName: ").append(toIndentedString(threeLetterISOLanguageName)).append("\n");
    sb.append("    threeLetterISOLanguageNames: ").append(toIndentedString(threeLetterISOLanguageNames)).append("\n");
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
    openapiFields.add("DisplayName");
    openapiFields.add("TwoLetterISOLanguageName");
    openapiFields.add("ThreeLetterISOLanguageName");
    openapiFields.add("ThreeLetterISOLanguageNames");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to CultureDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!CultureDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in CultureDto is not found in the empty JSON string", CultureDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!CultureDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `CultureDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("DisplayName") != null && !jsonObj.get("DisplayName").isJsonNull()) && !jsonObj.get("DisplayName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `DisplayName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("DisplayName").toString()));
      }
      if ((jsonObj.get("TwoLetterISOLanguageName") != null && !jsonObj.get("TwoLetterISOLanguageName").isJsonNull()) && !jsonObj.get("TwoLetterISOLanguageName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `TwoLetterISOLanguageName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("TwoLetterISOLanguageName").toString()));
      }
      if ((jsonObj.get("ThreeLetterISOLanguageName") != null && !jsonObj.get("ThreeLetterISOLanguageName").isJsonNull()) && !jsonObj.get("ThreeLetterISOLanguageName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ThreeLetterISOLanguageName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ThreeLetterISOLanguageName").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ThreeLetterISOLanguageNames") != null && !jsonObj.get("ThreeLetterISOLanguageNames").isJsonNull() && !jsonObj.get("ThreeLetterISOLanguageNames").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ThreeLetterISOLanguageNames` to be an array in the JSON string but got `%s`", jsonObj.get("ThreeLetterISOLanguageNames").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!CultureDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'CultureDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<CultureDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(CultureDto.class));

       return (TypeAdapter<T>) new TypeAdapter<CultureDto>() {
           @Override
           public void write(JsonWriter out, CultureDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public CultureDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of CultureDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of CultureDto
   * @throws IOException if the JSON string is invalid with respect to CultureDto
   */
  public static CultureDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, CultureDto.class);
  }

  /**
   * Convert an instance of CultureDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

