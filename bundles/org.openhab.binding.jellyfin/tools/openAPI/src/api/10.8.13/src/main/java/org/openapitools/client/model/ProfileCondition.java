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
import org.openapitools.client.model.ProfileConditionType;
import org.openapitools.client.model.ProfileConditionValue;
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
 * ProfileCondition
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ProfileCondition {
  public static final String SERIALIZED_NAME_CONDITION = "Condition";
  @SerializedName(SERIALIZED_NAME_CONDITION)
  @javax.annotation.Nullable
  private ProfileConditionType condition;

  public static final String SERIALIZED_NAME_PROPERTY = "Property";
  @SerializedName(SERIALIZED_NAME_PROPERTY)
  @javax.annotation.Nullable
  private ProfileConditionValue property;

  public static final String SERIALIZED_NAME_VALUE = "Value";
  @SerializedName(SERIALIZED_NAME_VALUE)
  @javax.annotation.Nullable
  private String value;

  public static final String SERIALIZED_NAME_IS_REQUIRED = "IsRequired";
  @SerializedName(SERIALIZED_NAME_IS_REQUIRED)
  @javax.annotation.Nullable
  private Boolean isRequired;

  public ProfileCondition() {
  }

  public ProfileCondition condition(@javax.annotation.Nullable ProfileConditionType condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Get condition
   * @return condition
   */
  @javax.annotation.Nullable
  public ProfileConditionType getCondition() {
    return condition;
  }

  public void setCondition(@javax.annotation.Nullable ProfileConditionType condition) {
    this.condition = condition;
  }


  public ProfileCondition property(@javax.annotation.Nullable ProfileConditionValue property) {
    this.property = property;
    return this;
  }

  /**
   * Get property
   * @return property
   */
  @javax.annotation.Nullable
  public ProfileConditionValue getProperty() {
    return property;
  }

  public void setProperty(@javax.annotation.Nullable ProfileConditionValue property) {
    this.property = property;
  }


  public ProfileCondition value(@javax.annotation.Nullable String value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @javax.annotation.Nullable
  public String getValue() {
    return value;
  }

  public void setValue(@javax.annotation.Nullable String value) {
    this.value = value;
  }


  public ProfileCondition isRequired(@javax.annotation.Nullable Boolean isRequired) {
    this.isRequired = isRequired;
    return this;
  }

  /**
   * Get isRequired
   * @return isRequired
   */
  @javax.annotation.Nullable
  public Boolean getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(@javax.annotation.Nullable Boolean isRequired) {
    this.isRequired = isRequired;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileCondition profileCondition = (ProfileCondition) o;
    return Objects.equals(this.condition, profileCondition.condition) &&
        Objects.equals(this.property, profileCondition.property) &&
        Objects.equals(this.value, profileCondition.value) &&
        Objects.equals(this.isRequired, profileCondition.isRequired);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(condition, property, value, isRequired);
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
    sb.append("class ProfileCondition {\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    isRequired: ").append(toIndentedString(isRequired)).append("\n");
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
    openapiFields.add("Condition");
    openapiFields.add("Property");
    openapiFields.add("Value");
    openapiFields.add("IsRequired");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ProfileCondition
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ProfileCondition.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ProfileCondition is not found in the empty JSON string", ProfileCondition.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ProfileCondition.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ProfileCondition` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `Condition`
      if (jsonObj.get("Condition") != null && !jsonObj.get("Condition").isJsonNull()) {
        ProfileConditionType.validateJsonElement(jsonObj.get("Condition"));
      }
      // validate the optional field `Property`
      if (jsonObj.get("Property") != null && !jsonObj.get("Property").isJsonNull()) {
        ProfileConditionValue.validateJsonElement(jsonObj.get("Property"));
      }
      if ((jsonObj.get("Value") != null && !jsonObj.get("Value").isJsonNull()) && !jsonObj.get("Value").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Value` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Value").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ProfileCondition.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ProfileCondition' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ProfileCondition> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ProfileCondition.class));

       return (TypeAdapter<T>) new TypeAdapter<ProfileCondition>() {
           @Override
           public void write(JsonWriter out, ProfileCondition value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ProfileCondition read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ProfileCondition given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ProfileCondition
   * @throws IOException if the JSON string is invalid with respect to ProfileCondition
   */
  public static ProfileCondition fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ProfileCondition.class);
  }

  /**
   * Convert an instance of ProfileCondition to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

