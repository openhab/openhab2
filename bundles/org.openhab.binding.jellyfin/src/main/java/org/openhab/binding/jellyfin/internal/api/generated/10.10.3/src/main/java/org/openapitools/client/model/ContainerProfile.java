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
import org.openapitools.client.model.DlnaProfileType;
import org.openapitools.client.model.ProfileCondition;
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
 * Defines the MediaBrowser.Model.Dlna.ContainerProfile.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ContainerProfile {
  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private DlnaProfileType type;

  public static final String SERIALIZED_NAME_CONDITIONS = "Conditions";
  @SerializedName(SERIALIZED_NAME_CONDITIONS)
  @javax.annotation.Nullable
  private List<ProfileCondition> conditions = new ArrayList<>();

  public static final String SERIALIZED_NAME_CONTAINER = "Container";
  @SerializedName(SERIALIZED_NAME_CONTAINER)
  @javax.annotation.Nullable
  private String container;

  public static final String SERIALIZED_NAME_SUB_CONTAINER = "SubContainer";
  @SerializedName(SERIALIZED_NAME_SUB_CONTAINER)
  @javax.annotation.Nullable
  private String subContainer;

  public ContainerProfile() {
  }

  public ContainerProfile type(@javax.annotation.Nullable DlnaProfileType type) {
    this.type = type;
    return this;
  }

  /**
   * Gets or sets the MediaBrowser.Model.Dlna.DlnaProfileType which this container must meet.
   * @return type
   */
  @javax.annotation.Nullable
  public DlnaProfileType getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable DlnaProfileType type) {
    this.type = type;
  }


  public ContainerProfile conditions(@javax.annotation.Nullable List<ProfileCondition> conditions) {
    this.conditions = conditions;
    return this;
  }

  public ContainerProfile addConditionsItem(ProfileCondition conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Gets or sets the list of MediaBrowser.Model.Dlna.ProfileCondition which this container will be applied to.
   * @return conditions
   */
  @javax.annotation.Nullable
  public List<ProfileCondition> getConditions() {
    return conditions;
  }

  public void setConditions(@javax.annotation.Nullable List<ProfileCondition> conditions) {
    this.conditions = conditions;
  }


  public ContainerProfile container(@javax.annotation.Nullable String container) {
    this.container = container;
    return this;
  }

  /**
   * Gets or sets the container(s) which this container must meet.
   * @return container
   */
  @javax.annotation.Nullable
  public String getContainer() {
    return container;
  }

  public void setContainer(@javax.annotation.Nullable String container) {
    this.container = container;
  }


  public ContainerProfile subContainer(@javax.annotation.Nullable String subContainer) {
    this.subContainer = subContainer;
    return this;
  }

  /**
   * Gets or sets the sub container(s) which this container must meet.
   * @return subContainer
   */
  @javax.annotation.Nullable
  public String getSubContainer() {
    return subContainer;
  }

  public void setSubContainer(@javax.annotation.Nullable String subContainer) {
    this.subContainer = subContainer;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContainerProfile containerProfile = (ContainerProfile) o;
    return Objects.equals(this.type, containerProfile.type) &&
        Objects.equals(this.conditions, containerProfile.conditions) &&
        Objects.equals(this.container, containerProfile.container) &&
        Objects.equals(this.subContainer, containerProfile.subContainer);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, conditions, container, subContainer);
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
    sb.append("class ContainerProfile {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    container: ").append(toIndentedString(container)).append("\n");
    sb.append("    subContainer: ").append(toIndentedString(subContainer)).append("\n");
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
    openapiFields.add("Type");
    openapiFields.add("Conditions");
    openapiFields.add("Container");
    openapiFields.add("SubContainer");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ContainerProfile
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ContainerProfile.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ContainerProfile is not found in the empty JSON string", ContainerProfile.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ContainerProfile.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ContainerProfile` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `Type`
      if (jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) {
        DlnaProfileType.validateJsonElement(jsonObj.get("Type"));
      }
      if (jsonObj.get("Conditions") != null && !jsonObj.get("Conditions").isJsonNull()) {
        JsonArray jsonArrayconditions = jsonObj.getAsJsonArray("Conditions");
        if (jsonArrayconditions != null) {
          // ensure the json data is an array
          if (!jsonObj.get("Conditions").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `Conditions` to be an array in the JSON string but got `%s`", jsonObj.get("Conditions").toString()));
          }

          // validate the optional field `Conditions` (array)
          for (int i = 0; i < jsonArrayconditions.size(); i++) {
            ProfileCondition.validateJsonElement(jsonArrayconditions.get(i));
          };
        }
      }
      if ((jsonObj.get("Container") != null && !jsonObj.get("Container").isJsonNull()) && !jsonObj.get("Container").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Container` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Container").toString()));
      }
      if ((jsonObj.get("SubContainer") != null && !jsonObj.get("SubContainer").isJsonNull()) && !jsonObj.get("SubContainer").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SubContainer` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SubContainer").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ContainerProfile.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ContainerProfile' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ContainerProfile> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ContainerProfile.class));

       return (TypeAdapter<T>) new TypeAdapter<ContainerProfile>() {
           @Override
           public void write(JsonWriter out, ContainerProfile value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ContainerProfile read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ContainerProfile given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ContainerProfile
   * @throws IOException if the JSON string is invalid with respect to ContainerProfile
   */
  public static ContainerProfile fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ContainerProfile.class);
  }

  /**
   * Convert an instance of ContainerProfile to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

