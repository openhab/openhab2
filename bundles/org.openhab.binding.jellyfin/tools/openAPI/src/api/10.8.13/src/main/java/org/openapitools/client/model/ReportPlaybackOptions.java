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
 * ReportPlaybackOptions
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:32:08.795345870+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ReportPlaybackOptions {
  public static final String SERIALIZED_NAME_MAX_DATA_AGE = "MaxDataAge";
  @SerializedName(SERIALIZED_NAME_MAX_DATA_AGE)
  @javax.annotation.Nullable
  private Integer maxDataAge;

  public static final String SERIALIZED_NAME_BACKUP_PATH = "BackupPath";
  @SerializedName(SERIALIZED_NAME_BACKUP_PATH)
  @javax.annotation.Nullable
  private String backupPath;

  public static final String SERIALIZED_NAME_MAX_BACKUP_FILES = "MaxBackupFiles";
  @SerializedName(SERIALIZED_NAME_MAX_BACKUP_FILES)
  @javax.annotation.Nullable
  private Integer maxBackupFiles;

  public ReportPlaybackOptions() {
  }

  public ReportPlaybackOptions maxDataAge(@javax.annotation.Nullable Integer maxDataAge) {
    this.maxDataAge = maxDataAge;
    return this;
  }

  /**
   * Get maxDataAge
   * @return maxDataAge
   */
  @javax.annotation.Nullable
  public Integer getMaxDataAge() {
    return maxDataAge;
  }

  public void setMaxDataAge(@javax.annotation.Nullable Integer maxDataAge) {
    this.maxDataAge = maxDataAge;
  }


  public ReportPlaybackOptions backupPath(@javax.annotation.Nullable String backupPath) {
    this.backupPath = backupPath;
    return this;
  }

  /**
   * Get backupPath
   * @return backupPath
   */
  @javax.annotation.Nullable
  public String getBackupPath() {
    return backupPath;
  }

  public void setBackupPath(@javax.annotation.Nullable String backupPath) {
    this.backupPath = backupPath;
  }


  public ReportPlaybackOptions maxBackupFiles(@javax.annotation.Nullable Integer maxBackupFiles) {
    this.maxBackupFiles = maxBackupFiles;
    return this;
  }

  /**
   * Get maxBackupFiles
   * @return maxBackupFiles
   */
  @javax.annotation.Nullable
  public Integer getMaxBackupFiles() {
    return maxBackupFiles;
  }

  public void setMaxBackupFiles(@javax.annotation.Nullable Integer maxBackupFiles) {
    this.maxBackupFiles = maxBackupFiles;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportPlaybackOptions reportPlaybackOptions = (ReportPlaybackOptions) o;
    return Objects.equals(this.maxDataAge, reportPlaybackOptions.maxDataAge) &&
        Objects.equals(this.backupPath, reportPlaybackOptions.backupPath) &&
        Objects.equals(this.maxBackupFiles, reportPlaybackOptions.maxBackupFiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxDataAge, backupPath, maxBackupFiles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportPlaybackOptions {\n");
    sb.append("    maxDataAge: ").append(toIndentedString(maxDataAge)).append("\n");
    sb.append("    backupPath: ").append(toIndentedString(backupPath)).append("\n");
    sb.append("    maxBackupFiles: ").append(toIndentedString(maxBackupFiles)).append("\n");
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
    openapiFields.add("MaxDataAge");
    openapiFields.add("BackupPath");
    openapiFields.add("MaxBackupFiles");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ReportPlaybackOptions
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ReportPlaybackOptions.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ReportPlaybackOptions is not found in the empty JSON string", ReportPlaybackOptions.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ReportPlaybackOptions.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ReportPlaybackOptions` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("BackupPath") != null && !jsonObj.get("BackupPath").isJsonNull()) && !jsonObj.get("BackupPath").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `BackupPath` to be a primitive type in the JSON string but got `%s`", jsonObj.get("BackupPath").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ReportPlaybackOptions.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ReportPlaybackOptions' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ReportPlaybackOptions> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ReportPlaybackOptions.class));

       return (TypeAdapter<T>) new TypeAdapter<ReportPlaybackOptions>() {
           @Override
           public void write(JsonWriter out, ReportPlaybackOptions value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ReportPlaybackOptions read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ReportPlaybackOptions given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ReportPlaybackOptions
   * @throws IOException if the JSON string is invalid with respect to ReportPlaybackOptions
   */
  public static ReportPlaybackOptions fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ReportPlaybackOptions.class);
  }

  /**
   * Convert an instance of ReportPlaybackOptions to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

