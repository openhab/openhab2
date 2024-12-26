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
import java.util.UUID;
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
 * The configuration page info.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ConfigurationPageInfo {
  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_ENABLE_IN_MAIN_MENU = "EnableInMainMenu";
  @SerializedName(SERIALIZED_NAME_ENABLE_IN_MAIN_MENU)
  @javax.annotation.Nullable
  private Boolean enableInMainMenu;

  public static final String SERIALIZED_NAME_MENU_SECTION = "MenuSection";
  @SerializedName(SERIALIZED_NAME_MENU_SECTION)
  @javax.annotation.Nullable
  private String menuSection;

  public static final String SERIALIZED_NAME_MENU_ICON = "MenuIcon";
  @SerializedName(SERIALIZED_NAME_MENU_ICON)
  @javax.annotation.Nullable
  private String menuIcon;

  public static final String SERIALIZED_NAME_DISPLAY_NAME = "DisplayName";
  @SerializedName(SERIALIZED_NAME_DISPLAY_NAME)
  @javax.annotation.Nullable
  private String displayName;

  public static final String SERIALIZED_NAME_PLUGIN_ID = "PluginId";
  @SerializedName(SERIALIZED_NAME_PLUGIN_ID)
  @javax.annotation.Nullable
  private UUID pluginId;

  public ConfigurationPageInfo() {
  }

  public ConfigurationPageInfo name(@javax.annotation.Nullable String name) {
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


  public ConfigurationPageInfo enableInMainMenu(@javax.annotation.Nullable Boolean enableInMainMenu) {
    this.enableInMainMenu = enableInMainMenu;
    return this;
  }

  /**
   * Gets or sets a value indicating whether the configurations page is enabled in the main menu.
   * @return enableInMainMenu
   */
  @javax.annotation.Nullable
  public Boolean getEnableInMainMenu() {
    return enableInMainMenu;
  }

  public void setEnableInMainMenu(@javax.annotation.Nullable Boolean enableInMainMenu) {
    this.enableInMainMenu = enableInMainMenu;
  }


  public ConfigurationPageInfo menuSection(@javax.annotation.Nullable String menuSection) {
    this.menuSection = menuSection;
    return this;
  }

  /**
   * Gets or sets the menu section.
   * @return menuSection
   */
  @javax.annotation.Nullable
  public String getMenuSection() {
    return menuSection;
  }

  public void setMenuSection(@javax.annotation.Nullable String menuSection) {
    this.menuSection = menuSection;
  }


  public ConfigurationPageInfo menuIcon(@javax.annotation.Nullable String menuIcon) {
    this.menuIcon = menuIcon;
    return this;
  }

  /**
   * Gets or sets the menu icon.
   * @return menuIcon
   */
  @javax.annotation.Nullable
  public String getMenuIcon() {
    return menuIcon;
  }

  public void setMenuIcon(@javax.annotation.Nullable String menuIcon) {
    this.menuIcon = menuIcon;
  }


  public ConfigurationPageInfo displayName(@javax.annotation.Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Gets or sets the display name.
   * @return displayName
   */
  @javax.annotation.Nullable
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@javax.annotation.Nullable String displayName) {
    this.displayName = displayName;
  }


  public ConfigurationPageInfo pluginId(@javax.annotation.Nullable UUID pluginId) {
    this.pluginId = pluginId;
    return this;
  }

  /**
   * Gets or sets the plugin id.
   * @return pluginId
   */
  @javax.annotation.Nullable
  public UUID getPluginId() {
    return pluginId;
  }

  public void setPluginId(@javax.annotation.Nullable UUID pluginId) {
    this.pluginId = pluginId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurationPageInfo configurationPageInfo = (ConfigurationPageInfo) o;
    return Objects.equals(this.name, configurationPageInfo.name) &&
        Objects.equals(this.enableInMainMenu, configurationPageInfo.enableInMainMenu) &&
        Objects.equals(this.menuSection, configurationPageInfo.menuSection) &&
        Objects.equals(this.menuIcon, configurationPageInfo.menuIcon) &&
        Objects.equals(this.displayName, configurationPageInfo.displayName) &&
        Objects.equals(this.pluginId, configurationPageInfo.pluginId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, enableInMainMenu, menuSection, menuIcon, displayName, pluginId);
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
    sb.append("class ConfigurationPageInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enableInMainMenu: ").append(toIndentedString(enableInMainMenu)).append("\n");
    sb.append("    menuSection: ").append(toIndentedString(menuSection)).append("\n");
    sb.append("    menuIcon: ").append(toIndentedString(menuIcon)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    pluginId: ").append(toIndentedString(pluginId)).append("\n");
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
    openapiFields.add("EnableInMainMenu");
    openapiFields.add("MenuSection");
    openapiFields.add("MenuIcon");
    openapiFields.add("DisplayName");
    openapiFields.add("PluginId");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ConfigurationPageInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ConfigurationPageInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ConfigurationPageInfo is not found in the empty JSON string", ConfigurationPageInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ConfigurationPageInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ConfigurationPageInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("MenuSection") != null && !jsonObj.get("MenuSection").isJsonNull()) && !jsonObj.get("MenuSection").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MenuSection` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MenuSection").toString()));
      }
      if ((jsonObj.get("MenuIcon") != null && !jsonObj.get("MenuIcon").isJsonNull()) && !jsonObj.get("MenuIcon").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MenuIcon` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MenuIcon").toString()));
      }
      if ((jsonObj.get("DisplayName") != null && !jsonObj.get("DisplayName").isJsonNull()) && !jsonObj.get("DisplayName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `DisplayName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("DisplayName").toString()));
      }
      if ((jsonObj.get("PluginId") != null && !jsonObj.get("PluginId").isJsonNull()) && !jsonObj.get("PluginId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `PluginId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("PluginId").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ConfigurationPageInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ConfigurationPageInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ConfigurationPageInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ConfigurationPageInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<ConfigurationPageInfo>() {
           @Override
           public void write(JsonWriter out, ConfigurationPageInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ConfigurationPageInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ConfigurationPageInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ConfigurationPageInfo
   * @throws IOException if the JSON string is invalid with respect to ConfigurationPageInfo
   */
  public static ConfigurationPageInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ConfigurationPageInfo.class);
  }

  /**
   * Convert an instance of ConfigurationPageInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

