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
import java.time.OffsetDateTime;
import java.util.Arrays;
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
 * RemoteSubtitleInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:56.699980679+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class RemoteSubtitleInfo {
  public static final String SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAME = "ThreeLetterISOLanguageName";
  @SerializedName(SERIALIZED_NAME_THREE_LETTER_I_S_O_LANGUAGE_NAME)
  @javax.annotation.Nullable
  private String threeLetterISOLanguageName;

  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private String id;

  public static final String SERIALIZED_NAME_PROVIDER_NAME = "ProviderName";
  @SerializedName(SERIALIZED_NAME_PROVIDER_NAME)
  @javax.annotation.Nullable
  private String providerName;

  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_FORMAT = "Format";
  @SerializedName(SERIALIZED_NAME_FORMAT)
  @javax.annotation.Nullable
  private String format;

  public static final String SERIALIZED_NAME_AUTHOR = "Author";
  @SerializedName(SERIALIZED_NAME_AUTHOR)
  @javax.annotation.Nullable
  private String author;

  public static final String SERIALIZED_NAME_COMMENT = "Comment";
  @SerializedName(SERIALIZED_NAME_COMMENT)
  @javax.annotation.Nullable
  private String comment;

  public static final String SERIALIZED_NAME_DATE_CREATED = "DateCreated";
  @SerializedName(SERIALIZED_NAME_DATE_CREATED)
  @javax.annotation.Nullable
  private OffsetDateTime dateCreated;

  public static final String SERIALIZED_NAME_COMMUNITY_RATING = "CommunityRating";
  @SerializedName(SERIALIZED_NAME_COMMUNITY_RATING)
  @javax.annotation.Nullable
  private Float communityRating;

  public static final String SERIALIZED_NAME_FRAME_RATE = "FrameRate";
  @SerializedName(SERIALIZED_NAME_FRAME_RATE)
  @javax.annotation.Nullable
  private Float frameRate;

  public static final String SERIALIZED_NAME_DOWNLOAD_COUNT = "DownloadCount";
  @SerializedName(SERIALIZED_NAME_DOWNLOAD_COUNT)
  @javax.annotation.Nullable
  private Integer downloadCount;

  public static final String SERIALIZED_NAME_IS_HASH_MATCH = "IsHashMatch";
  @SerializedName(SERIALIZED_NAME_IS_HASH_MATCH)
  @javax.annotation.Nullable
  private Boolean isHashMatch;

  public static final String SERIALIZED_NAME_AI_TRANSLATED = "AiTranslated";
  @SerializedName(SERIALIZED_NAME_AI_TRANSLATED)
  @javax.annotation.Nullable
  private Boolean aiTranslated;

  public static final String SERIALIZED_NAME_MACHINE_TRANSLATED = "MachineTranslated";
  @SerializedName(SERIALIZED_NAME_MACHINE_TRANSLATED)
  @javax.annotation.Nullable
  private Boolean machineTranslated;

  public static final String SERIALIZED_NAME_FORCED = "Forced";
  @SerializedName(SERIALIZED_NAME_FORCED)
  @javax.annotation.Nullable
  private Boolean forced;

  public static final String SERIALIZED_NAME_HEARING_IMPAIRED = "HearingImpaired";
  @SerializedName(SERIALIZED_NAME_HEARING_IMPAIRED)
  @javax.annotation.Nullable
  private Boolean hearingImpaired;

  public RemoteSubtitleInfo() {
  }

  public RemoteSubtitleInfo threeLetterISOLanguageName(@javax.annotation.Nullable String threeLetterISOLanguageName) {
    this.threeLetterISOLanguageName = threeLetterISOLanguageName;
    return this;
  }

  /**
   * Get threeLetterISOLanguageName
   * @return threeLetterISOLanguageName
   */
  @javax.annotation.Nullable
  public String getThreeLetterISOLanguageName() {
    return threeLetterISOLanguageName;
  }

  public void setThreeLetterISOLanguageName(@javax.annotation.Nullable String threeLetterISOLanguageName) {
    this.threeLetterISOLanguageName = threeLetterISOLanguageName;
  }


  public RemoteSubtitleInfo id(@javax.annotation.Nullable String id) {
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


  public RemoteSubtitleInfo providerName(@javax.annotation.Nullable String providerName) {
    this.providerName = providerName;
    return this;
  }

  /**
   * Get providerName
   * @return providerName
   */
  @javax.annotation.Nullable
  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(@javax.annotation.Nullable String providerName) {
    this.providerName = providerName;
  }


  public RemoteSubtitleInfo name(@javax.annotation.Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(@javax.annotation.Nullable String name) {
    this.name = name;
  }


  public RemoteSubtitleInfo format(@javax.annotation.Nullable String format) {
    this.format = format;
    return this;
  }

  /**
   * Get format
   * @return format
   */
  @javax.annotation.Nullable
  public String getFormat() {
    return format;
  }

  public void setFormat(@javax.annotation.Nullable String format) {
    this.format = format;
  }


  public RemoteSubtitleInfo author(@javax.annotation.Nullable String author) {
    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   */
  @javax.annotation.Nullable
  public String getAuthor() {
    return author;
  }

  public void setAuthor(@javax.annotation.Nullable String author) {
    this.author = author;
  }


  public RemoteSubtitleInfo comment(@javax.annotation.Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  @javax.annotation.Nullable
  public String getComment() {
    return comment;
  }

  public void setComment(@javax.annotation.Nullable String comment) {
    this.comment = comment;
  }


  public RemoteSubtitleInfo dateCreated(@javax.annotation.Nullable OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
    return this;
  }

  /**
   * Get dateCreated
   * @return dateCreated
   */
  @javax.annotation.Nullable
  public OffsetDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(@javax.annotation.Nullable OffsetDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }


  public RemoteSubtitleInfo communityRating(@javax.annotation.Nullable Float communityRating) {
    this.communityRating = communityRating;
    return this;
  }

  /**
   * Get communityRating
   * @return communityRating
   */
  @javax.annotation.Nullable
  public Float getCommunityRating() {
    return communityRating;
  }

  public void setCommunityRating(@javax.annotation.Nullable Float communityRating) {
    this.communityRating = communityRating;
  }


  public RemoteSubtitleInfo frameRate(@javax.annotation.Nullable Float frameRate) {
    this.frameRate = frameRate;
    return this;
  }

  /**
   * Get frameRate
   * @return frameRate
   */
  @javax.annotation.Nullable
  public Float getFrameRate() {
    return frameRate;
  }

  public void setFrameRate(@javax.annotation.Nullable Float frameRate) {
    this.frameRate = frameRate;
  }


  public RemoteSubtitleInfo downloadCount(@javax.annotation.Nullable Integer downloadCount) {
    this.downloadCount = downloadCount;
    return this;
  }

  /**
   * Get downloadCount
   * @return downloadCount
   */
  @javax.annotation.Nullable
  public Integer getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(@javax.annotation.Nullable Integer downloadCount) {
    this.downloadCount = downloadCount;
  }


  public RemoteSubtitleInfo isHashMatch(@javax.annotation.Nullable Boolean isHashMatch) {
    this.isHashMatch = isHashMatch;
    return this;
  }

  /**
   * Get isHashMatch
   * @return isHashMatch
   */
  @javax.annotation.Nullable
  public Boolean getIsHashMatch() {
    return isHashMatch;
  }

  public void setIsHashMatch(@javax.annotation.Nullable Boolean isHashMatch) {
    this.isHashMatch = isHashMatch;
  }


  public RemoteSubtitleInfo aiTranslated(@javax.annotation.Nullable Boolean aiTranslated) {
    this.aiTranslated = aiTranslated;
    return this;
  }

  /**
   * Get aiTranslated
   * @return aiTranslated
   */
  @javax.annotation.Nullable
  public Boolean getAiTranslated() {
    return aiTranslated;
  }

  public void setAiTranslated(@javax.annotation.Nullable Boolean aiTranslated) {
    this.aiTranslated = aiTranslated;
  }


  public RemoteSubtitleInfo machineTranslated(@javax.annotation.Nullable Boolean machineTranslated) {
    this.machineTranslated = machineTranslated;
    return this;
  }

  /**
   * Get machineTranslated
   * @return machineTranslated
   */
  @javax.annotation.Nullable
  public Boolean getMachineTranslated() {
    return machineTranslated;
  }

  public void setMachineTranslated(@javax.annotation.Nullable Boolean machineTranslated) {
    this.machineTranslated = machineTranslated;
  }


  public RemoteSubtitleInfo forced(@javax.annotation.Nullable Boolean forced) {
    this.forced = forced;
    return this;
  }

  /**
   * Get forced
   * @return forced
   */
  @javax.annotation.Nullable
  public Boolean getForced() {
    return forced;
  }

  public void setForced(@javax.annotation.Nullable Boolean forced) {
    this.forced = forced;
  }


  public RemoteSubtitleInfo hearingImpaired(@javax.annotation.Nullable Boolean hearingImpaired) {
    this.hearingImpaired = hearingImpaired;
    return this;
  }

  /**
   * Get hearingImpaired
   * @return hearingImpaired
   */
  @javax.annotation.Nullable
  public Boolean getHearingImpaired() {
    return hearingImpaired;
  }

  public void setHearingImpaired(@javax.annotation.Nullable Boolean hearingImpaired) {
    this.hearingImpaired = hearingImpaired;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoteSubtitleInfo remoteSubtitleInfo = (RemoteSubtitleInfo) o;
    return Objects.equals(this.threeLetterISOLanguageName, remoteSubtitleInfo.threeLetterISOLanguageName) &&
        Objects.equals(this.id, remoteSubtitleInfo.id) &&
        Objects.equals(this.providerName, remoteSubtitleInfo.providerName) &&
        Objects.equals(this.name, remoteSubtitleInfo.name) &&
        Objects.equals(this.format, remoteSubtitleInfo.format) &&
        Objects.equals(this.author, remoteSubtitleInfo.author) &&
        Objects.equals(this.comment, remoteSubtitleInfo.comment) &&
        Objects.equals(this.dateCreated, remoteSubtitleInfo.dateCreated) &&
        Objects.equals(this.communityRating, remoteSubtitleInfo.communityRating) &&
        Objects.equals(this.frameRate, remoteSubtitleInfo.frameRate) &&
        Objects.equals(this.downloadCount, remoteSubtitleInfo.downloadCount) &&
        Objects.equals(this.isHashMatch, remoteSubtitleInfo.isHashMatch) &&
        Objects.equals(this.aiTranslated, remoteSubtitleInfo.aiTranslated) &&
        Objects.equals(this.machineTranslated, remoteSubtitleInfo.machineTranslated) &&
        Objects.equals(this.forced, remoteSubtitleInfo.forced) &&
        Objects.equals(this.hearingImpaired, remoteSubtitleInfo.hearingImpaired);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(threeLetterISOLanguageName, id, providerName, name, format, author, comment, dateCreated, communityRating, frameRate, downloadCount, isHashMatch, aiTranslated, machineTranslated, forced, hearingImpaired);
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
    sb.append("class RemoteSubtitleInfo {\n");
    sb.append("    threeLetterISOLanguageName: ").append(toIndentedString(threeLetterISOLanguageName)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    providerName: ").append(toIndentedString(providerName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    dateCreated: ").append(toIndentedString(dateCreated)).append("\n");
    sb.append("    communityRating: ").append(toIndentedString(communityRating)).append("\n");
    sb.append("    frameRate: ").append(toIndentedString(frameRate)).append("\n");
    sb.append("    downloadCount: ").append(toIndentedString(downloadCount)).append("\n");
    sb.append("    isHashMatch: ").append(toIndentedString(isHashMatch)).append("\n");
    sb.append("    aiTranslated: ").append(toIndentedString(aiTranslated)).append("\n");
    sb.append("    machineTranslated: ").append(toIndentedString(machineTranslated)).append("\n");
    sb.append("    forced: ").append(toIndentedString(forced)).append("\n");
    sb.append("    hearingImpaired: ").append(toIndentedString(hearingImpaired)).append("\n");
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
    openapiFields.add("ThreeLetterISOLanguageName");
    openapiFields.add("Id");
    openapiFields.add("ProviderName");
    openapiFields.add("Name");
    openapiFields.add("Format");
    openapiFields.add("Author");
    openapiFields.add("Comment");
    openapiFields.add("DateCreated");
    openapiFields.add("CommunityRating");
    openapiFields.add("FrameRate");
    openapiFields.add("DownloadCount");
    openapiFields.add("IsHashMatch");
    openapiFields.add("AiTranslated");
    openapiFields.add("MachineTranslated");
    openapiFields.add("Forced");
    openapiFields.add("HearingImpaired");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to RemoteSubtitleInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!RemoteSubtitleInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in RemoteSubtitleInfo is not found in the empty JSON string", RemoteSubtitleInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!RemoteSubtitleInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `RemoteSubtitleInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("ThreeLetterISOLanguageName") != null && !jsonObj.get("ThreeLetterISOLanguageName").isJsonNull()) && !jsonObj.get("ThreeLetterISOLanguageName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ThreeLetterISOLanguageName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ThreeLetterISOLanguageName").toString()));
      }
      if ((jsonObj.get("Id") != null && !jsonObj.get("Id").isJsonNull()) && !jsonObj.get("Id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Id").toString()));
      }
      if ((jsonObj.get("ProviderName") != null && !jsonObj.get("ProviderName").isJsonNull()) && !jsonObj.get("ProviderName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ProviderName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ProviderName").toString()));
      }
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("Format") != null && !jsonObj.get("Format").isJsonNull()) && !jsonObj.get("Format").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Format` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Format").toString()));
      }
      if ((jsonObj.get("Author") != null && !jsonObj.get("Author").isJsonNull()) && !jsonObj.get("Author").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Author` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Author").toString()));
      }
      if ((jsonObj.get("Comment") != null && !jsonObj.get("Comment").isJsonNull()) && !jsonObj.get("Comment").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Comment` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Comment").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!RemoteSubtitleInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'RemoteSubtitleInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<RemoteSubtitleInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(RemoteSubtitleInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<RemoteSubtitleInfo>() {
           @Override
           public void write(JsonWriter out, RemoteSubtitleInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public RemoteSubtitleInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of RemoteSubtitleInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of RemoteSubtitleInfo
   * @throws IOException if the JSON string is invalid with respect to RemoteSubtitleInfo
   */
  public static RemoteSubtitleInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, RemoteSubtitleInfo.class);
  }

  /**
   * Convert an instance of RemoteSubtitleInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

