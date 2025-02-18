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
import org.openapitools.client.model.ListingsProviderInfo;
import org.openapitools.client.model.TunerHostInfo;
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
 * LiveTvOptions
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class LiveTvOptions {
  public static final String SERIALIZED_NAME_GUIDE_DAYS = "GuideDays";
  @SerializedName(SERIALIZED_NAME_GUIDE_DAYS)
  @javax.annotation.Nullable
  private Integer guideDays;

  public static final String SERIALIZED_NAME_RECORDING_PATH = "RecordingPath";
  @SerializedName(SERIALIZED_NAME_RECORDING_PATH)
  @javax.annotation.Nullable
  private String recordingPath;

  public static final String SERIALIZED_NAME_MOVIE_RECORDING_PATH = "MovieRecordingPath";
  @SerializedName(SERIALIZED_NAME_MOVIE_RECORDING_PATH)
  @javax.annotation.Nullable
  private String movieRecordingPath;

  public static final String SERIALIZED_NAME_SERIES_RECORDING_PATH = "SeriesRecordingPath";
  @SerializedName(SERIALIZED_NAME_SERIES_RECORDING_PATH)
  @javax.annotation.Nullable
  private String seriesRecordingPath;

  public static final String SERIALIZED_NAME_ENABLE_RECORDING_SUBFOLDERS = "EnableRecordingSubfolders";
  @SerializedName(SERIALIZED_NAME_ENABLE_RECORDING_SUBFOLDERS)
  @javax.annotation.Nullable
  private Boolean enableRecordingSubfolders;

  public static final String SERIALIZED_NAME_ENABLE_ORIGINAL_AUDIO_WITH_ENCODED_RECORDINGS = "EnableOriginalAudioWithEncodedRecordings";
  @SerializedName(SERIALIZED_NAME_ENABLE_ORIGINAL_AUDIO_WITH_ENCODED_RECORDINGS)
  @javax.annotation.Nullable
  private Boolean enableOriginalAudioWithEncodedRecordings;

  public static final String SERIALIZED_NAME_TUNER_HOSTS = "TunerHosts";
  @SerializedName(SERIALIZED_NAME_TUNER_HOSTS)
  @javax.annotation.Nullable
  private List<TunerHostInfo> tunerHosts;

  public static final String SERIALIZED_NAME_LISTING_PROVIDERS = "ListingProviders";
  @SerializedName(SERIALIZED_NAME_LISTING_PROVIDERS)
  @javax.annotation.Nullable
  private List<ListingsProviderInfo> listingProviders;

  public static final String SERIALIZED_NAME_PRE_PADDING_SECONDS = "PrePaddingSeconds";
  @SerializedName(SERIALIZED_NAME_PRE_PADDING_SECONDS)
  @javax.annotation.Nullable
  private Integer prePaddingSeconds;

  public static final String SERIALIZED_NAME_POST_PADDING_SECONDS = "PostPaddingSeconds";
  @SerializedName(SERIALIZED_NAME_POST_PADDING_SECONDS)
  @javax.annotation.Nullable
  private Integer postPaddingSeconds;

  public static final String SERIALIZED_NAME_MEDIA_LOCATIONS_CREATED = "MediaLocationsCreated";
  @SerializedName(SERIALIZED_NAME_MEDIA_LOCATIONS_CREATED)
  @javax.annotation.Nullable
  private List<String> mediaLocationsCreated;

  public static final String SERIALIZED_NAME_RECORDING_POST_PROCESSOR = "RecordingPostProcessor";
  @SerializedName(SERIALIZED_NAME_RECORDING_POST_PROCESSOR)
  @javax.annotation.Nullable
  private String recordingPostProcessor;

  public static final String SERIALIZED_NAME_RECORDING_POST_PROCESSOR_ARGUMENTS = "RecordingPostProcessorArguments";
  @SerializedName(SERIALIZED_NAME_RECORDING_POST_PROCESSOR_ARGUMENTS)
  @javax.annotation.Nullable
  private String recordingPostProcessorArguments;

  public LiveTvOptions() {
  }

  public LiveTvOptions guideDays(@javax.annotation.Nullable Integer guideDays) {
    this.guideDays = guideDays;
    return this;
  }

  /**
   * Get guideDays
   * @return guideDays
   */
  @javax.annotation.Nullable
  public Integer getGuideDays() {
    return guideDays;
  }

  public void setGuideDays(@javax.annotation.Nullable Integer guideDays) {
    this.guideDays = guideDays;
  }


  public LiveTvOptions recordingPath(@javax.annotation.Nullable String recordingPath) {
    this.recordingPath = recordingPath;
    return this;
  }

  /**
   * Get recordingPath
   * @return recordingPath
   */
  @javax.annotation.Nullable
  public String getRecordingPath() {
    return recordingPath;
  }

  public void setRecordingPath(@javax.annotation.Nullable String recordingPath) {
    this.recordingPath = recordingPath;
  }


  public LiveTvOptions movieRecordingPath(@javax.annotation.Nullable String movieRecordingPath) {
    this.movieRecordingPath = movieRecordingPath;
    return this;
  }

  /**
   * Get movieRecordingPath
   * @return movieRecordingPath
   */
  @javax.annotation.Nullable
  public String getMovieRecordingPath() {
    return movieRecordingPath;
  }

  public void setMovieRecordingPath(@javax.annotation.Nullable String movieRecordingPath) {
    this.movieRecordingPath = movieRecordingPath;
  }


  public LiveTvOptions seriesRecordingPath(@javax.annotation.Nullable String seriesRecordingPath) {
    this.seriesRecordingPath = seriesRecordingPath;
    return this;
  }

  /**
   * Get seriesRecordingPath
   * @return seriesRecordingPath
   */
  @javax.annotation.Nullable
  public String getSeriesRecordingPath() {
    return seriesRecordingPath;
  }

  public void setSeriesRecordingPath(@javax.annotation.Nullable String seriesRecordingPath) {
    this.seriesRecordingPath = seriesRecordingPath;
  }


  public LiveTvOptions enableRecordingSubfolders(@javax.annotation.Nullable Boolean enableRecordingSubfolders) {
    this.enableRecordingSubfolders = enableRecordingSubfolders;
    return this;
  }

  /**
   * Get enableRecordingSubfolders
   * @return enableRecordingSubfolders
   */
  @javax.annotation.Nullable
  public Boolean getEnableRecordingSubfolders() {
    return enableRecordingSubfolders;
  }

  public void setEnableRecordingSubfolders(@javax.annotation.Nullable Boolean enableRecordingSubfolders) {
    this.enableRecordingSubfolders = enableRecordingSubfolders;
  }


  public LiveTvOptions enableOriginalAudioWithEncodedRecordings(@javax.annotation.Nullable Boolean enableOriginalAudioWithEncodedRecordings) {
    this.enableOriginalAudioWithEncodedRecordings = enableOriginalAudioWithEncodedRecordings;
    return this;
  }

  /**
   * Get enableOriginalAudioWithEncodedRecordings
   * @return enableOriginalAudioWithEncodedRecordings
   */
  @javax.annotation.Nullable
  public Boolean getEnableOriginalAudioWithEncodedRecordings() {
    return enableOriginalAudioWithEncodedRecordings;
  }

  public void setEnableOriginalAudioWithEncodedRecordings(@javax.annotation.Nullable Boolean enableOriginalAudioWithEncodedRecordings) {
    this.enableOriginalAudioWithEncodedRecordings = enableOriginalAudioWithEncodedRecordings;
  }


  public LiveTvOptions tunerHosts(@javax.annotation.Nullable List<TunerHostInfo> tunerHosts) {
    this.tunerHosts = tunerHosts;
    return this;
  }

  public LiveTvOptions addTunerHostsItem(TunerHostInfo tunerHostsItem) {
    if (this.tunerHosts == null) {
      this.tunerHosts = new ArrayList<>();
    }
    this.tunerHosts.add(tunerHostsItem);
    return this;
  }

  /**
   * Get tunerHosts
   * @return tunerHosts
   */
  @javax.annotation.Nullable
  public List<TunerHostInfo> getTunerHosts() {
    return tunerHosts;
  }

  public void setTunerHosts(@javax.annotation.Nullable List<TunerHostInfo> tunerHosts) {
    this.tunerHosts = tunerHosts;
  }


  public LiveTvOptions listingProviders(@javax.annotation.Nullable List<ListingsProviderInfo> listingProviders) {
    this.listingProviders = listingProviders;
    return this;
  }

  public LiveTvOptions addListingProvidersItem(ListingsProviderInfo listingProvidersItem) {
    if (this.listingProviders == null) {
      this.listingProviders = new ArrayList<>();
    }
    this.listingProviders.add(listingProvidersItem);
    return this;
  }

  /**
   * Get listingProviders
   * @return listingProviders
   */
  @javax.annotation.Nullable
  public List<ListingsProviderInfo> getListingProviders() {
    return listingProviders;
  }

  public void setListingProviders(@javax.annotation.Nullable List<ListingsProviderInfo> listingProviders) {
    this.listingProviders = listingProviders;
  }


  public LiveTvOptions prePaddingSeconds(@javax.annotation.Nullable Integer prePaddingSeconds) {
    this.prePaddingSeconds = prePaddingSeconds;
    return this;
  }

  /**
   * Get prePaddingSeconds
   * @return prePaddingSeconds
   */
  @javax.annotation.Nullable
  public Integer getPrePaddingSeconds() {
    return prePaddingSeconds;
  }

  public void setPrePaddingSeconds(@javax.annotation.Nullable Integer prePaddingSeconds) {
    this.prePaddingSeconds = prePaddingSeconds;
  }


  public LiveTvOptions postPaddingSeconds(@javax.annotation.Nullable Integer postPaddingSeconds) {
    this.postPaddingSeconds = postPaddingSeconds;
    return this;
  }

  /**
   * Get postPaddingSeconds
   * @return postPaddingSeconds
   */
  @javax.annotation.Nullable
  public Integer getPostPaddingSeconds() {
    return postPaddingSeconds;
  }

  public void setPostPaddingSeconds(@javax.annotation.Nullable Integer postPaddingSeconds) {
    this.postPaddingSeconds = postPaddingSeconds;
  }


  public LiveTvOptions mediaLocationsCreated(@javax.annotation.Nullable List<String> mediaLocationsCreated) {
    this.mediaLocationsCreated = mediaLocationsCreated;
    return this;
  }

  public LiveTvOptions addMediaLocationsCreatedItem(String mediaLocationsCreatedItem) {
    if (this.mediaLocationsCreated == null) {
      this.mediaLocationsCreated = new ArrayList<>();
    }
    this.mediaLocationsCreated.add(mediaLocationsCreatedItem);
    return this;
  }

  /**
   * Get mediaLocationsCreated
   * @return mediaLocationsCreated
   */
  @javax.annotation.Nullable
  public List<String> getMediaLocationsCreated() {
    return mediaLocationsCreated;
  }

  public void setMediaLocationsCreated(@javax.annotation.Nullable List<String> mediaLocationsCreated) {
    this.mediaLocationsCreated = mediaLocationsCreated;
  }


  public LiveTvOptions recordingPostProcessor(@javax.annotation.Nullable String recordingPostProcessor) {
    this.recordingPostProcessor = recordingPostProcessor;
    return this;
  }

  /**
   * Get recordingPostProcessor
   * @return recordingPostProcessor
   */
  @javax.annotation.Nullable
  public String getRecordingPostProcessor() {
    return recordingPostProcessor;
  }

  public void setRecordingPostProcessor(@javax.annotation.Nullable String recordingPostProcessor) {
    this.recordingPostProcessor = recordingPostProcessor;
  }


  public LiveTvOptions recordingPostProcessorArguments(@javax.annotation.Nullable String recordingPostProcessorArguments) {
    this.recordingPostProcessorArguments = recordingPostProcessorArguments;
    return this;
  }

  /**
   * Get recordingPostProcessorArguments
   * @return recordingPostProcessorArguments
   */
  @javax.annotation.Nullable
  public String getRecordingPostProcessorArguments() {
    return recordingPostProcessorArguments;
  }

  public void setRecordingPostProcessorArguments(@javax.annotation.Nullable String recordingPostProcessorArguments) {
    this.recordingPostProcessorArguments = recordingPostProcessorArguments;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiveTvOptions liveTvOptions = (LiveTvOptions) o;
    return Objects.equals(this.guideDays, liveTvOptions.guideDays) &&
        Objects.equals(this.recordingPath, liveTvOptions.recordingPath) &&
        Objects.equals(this.movieRecordingPath, liveTvOptions.movieRecordingPath) &&
        Objects.equals(this.seriesRecordingPath, liveTvOptions.seriesRecordingPath) &&
        Objects.equals(this.enableRecordingSubfolders, liveTvOptions.enableRecordingSubfolders) &&
        Objects.equals(this.enableOriginalAudioWithEncodedRecordings, liveTvOptions.enableOriginalAudioWithEncodedRecordings) &&
        Objects.equals(this.tunerHosts, liveTvOptions.tunerHosts) &&
        Objects.equals(this.listingProviders, liveTvOptions.listingProviders) &&
        Objects.equals(this.prePaddingSeconds, liveTvOptions.prePaddingSeconds) &&
        Objects.equals(this.postPaddingSeconds, liveTvOptions.postPaddingSeconds) &&
        Objects.equals(this.mediaLocationsCreated, liveTvOptions.mediaLocationsCreated) &&
        Objects.equals(this.recordingPostProcessor, liveTvOptions.recordingPostProcessor) &&
        Objects.equals(this.recordingPostProcessorArguments, liveTvOptions.recordingPostProcessorArguments);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(guideDays, recordingPath, movieRecordingPath, seriesRecordingPath, enableRecordingSubfolders, enableOriginalAudioWithEncodedRecordings, tunerHosts, listingProviders, prePaddingSeconds, postPaddingSeconds, mediaLocationsCreated, recordingPostProcessor, recordingPostProcessorArguments);
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
    sb.append("class LiveTvOptions {\n");
    sb.append("    guideDays: ").append(toIndentedString(guideDays)).append("\n");
    sb.append("    recordingPath: ").append(toIndentedString(recordingPath)).append("\n");
    sb.append("    movieRecordingPath: ").append(toIndentedString(movieRecordingPath)).append("\n");
    sb.append("    seriesRecordingPath: ").append(toIndentedString(seriesRecordingPath)).append("\n");
    sb.append("    enableRecordingSubfolders: ").append(toIndentedString(enableRecordingSubfolders)).append("\n");
    sb.append("    enableOriginalAudioWithEncodedRecordings: ").append(toIndentedString(enableOriginalAudioWithEncodedRecordings)).append("\n");
    sb.append("    tunerHosts: ").append(toIndentedString(tunerHosts)).append("\n");
    sb.append("    listingProviders: ").append(toIndentedString(listingProviders)).append("\n");
    sb.append("    prePaddingSeconds: ").append(toIndentedString(prePaddingSeconds)).append("\n");
    sb.append("    postPaddingSeconds: ").append(toIndentedString(postPaddingSeconds)).append("\n");
    sb.append("    mediaLocationsCreated: ").append(toIndentedString(mediaLocationsCreated)).append("\n");
    sb.append("    recordingPostProcessor: ").append(toIndentedString(recordingPostProcessor)).append("\n");
    sb.append("    recordingPostProcessorArguments: ").append(toIndentedString(recordingPostProcessorArguments)).append("\n");
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
    openapiFields.add("GuideDays");
    openapiFields.add("RecordingPath");
    openapiFields.add("MovieRecordingPath");
    openapiFields.add("SeriesRecordingPath");
    openapiFields.add("EnableRecordingSubfolders");
    openapiFields.add("EnableOriginalAudioWithEncodedRecordings");
    openapiFields.add("TunerHosts");
    openapiFields.add("ListingProviders");
    openapiFields.add("PrePaddingSeconds");
    openapiFields.add("PostPaddingSeconds");
    openapiFields.add("MediaLocationsCreated");
    openapiFields.add("RecordingPostProcessor");
    openapiFields.add("RecordingPostProcessorArguments");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to LiveTvOptions
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!LiveTvOptions.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in LiveTvOptions is not found in the empty JSON string", LiveTvOptions.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!LiveTvOptions.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `LiveTvOptions` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("RecordingPath") != null && !jsonObj.get("RecordingPath").isJsonNull()) && !jsonObj.get("RecordingPath").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `RecordingPath` to be a primitive type in the JSON string but got `%s`", jsonObj.get("RecordingPath").toString()));
      }
      if ((jsonObj.get("MovieRecordingPath") != null && !jsonObj.get("MovieRecordingPath").isJsonNull()) && !jsonObj.get("MovieRecordingPath").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `MovieRecordingPath` to be a primitive type in the JSON string but got `%s`", jsonObj.get("MovieRecordingPath").toString()));
      }
      if ((jsonObj.get("SeriesRecordingPath") != null && !jsonObj.get("SeriesRecordingPath").isJsonNull()) && !jsonObj.get("SeriesRecordingPath").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SeriesRecordingPath` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SeriesRecordingPath").toString()));
      }
      if (jsonObj.get("TunerHosts") != null && !jsonObj.get("TunerHosts").isJsonNull()) {
        JsonArray jsonArraytunerHosts = jsonObj.getAsJsonArray("TunerHosts");
        if (jsonArraytunerHosts != null) {
          // ensure the json data is an array
          if (!jsonObj.get("TunerHosts").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `TunerHosts` to be an array in the JSON string but got `%s`", jsonObj.get("TunerHosts").toString()));
          }

          // validate the optional field `TunerHosts` (array)
          for (int i = 0; i < jsonArraytunerHosts.size(); i++) {
            TunerHostInfo.validateJsonElement(jsonArraytunerHosts.get(i));
          };
        }
      }
      if (jsonObj.get("ListingProviders") != null && !jsonObj.get("ListingProviders").isJsonNull()) {
        JsonArray jsonArraylistingProviders = jsonObj.getAsJsonArray("ListingProviders");
        if (jsonArraylistingProviders != null) {
          // ensure the json data is an array
          if (!jsonObj.get("ListingProviders").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `ListingProviders` to be an array in the JSON string but got `%s`", jsonObj.get("ListingProviders").toString()));
          }

          // validate the optional field `ListingProviders` (array)
          for (int i = 0; i < jsonArraylistingProviders.size(); i++) {
            ListingsProviderInfo.validateJsonElement(jsonArraylistingProviders.get(i));
          };
        }
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("MediaLocationsCreated") != null && !jsonObj.get("MediaLocationsCreated").isJsonNull() && !jsonObj.get("MediaLocationsCreated").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `MediaLocationsCreated` to be an array in the JSON string but got `%s`", jsonObj.get("MediaLocationsCreated").toString()));
      }
      if ((jsonObj.get("RecordingPostProcessor") != null && !jsonObj.get("RecordingPostProcessor").isJsonNull()) && !jsonObj.get("RecordingPostProcessor").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `RecordingPostProcessor` to be a primitive type in the JSON string but got `%s`", jsonObj.get("RecordingPostProcessor").toString()));
      }
      if ((jsonObj.get("RecordingPostProcessorArguments") != null && !jsonObj.get("RecordingPostProcessorArguments").isJsonNull()) && !jsonObj.get("RecordingPostProcessorArguments").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `RecordingPostProcessorArguments` to be a primitive type in the JSON string but got `%s`", jsonObj.get("RecordingPostProcessorArguments").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!LiveTvOptions.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'LiveTvOptions' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<LiveTvOptions> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(LiveTvOptions.class));

       return (TypeAdapter<T>) new TypeAdapter<LiveTvOptions>() {
           @Override
           public void write(JsonWriter out, LiveTvOptions value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public LiveTvOptions read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of LiveTvOptions given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of LiveTvOptions
   * @throws IOException if the JSON string is invalid with respect to LiveTvOptions
   */
  public static LiveTvOptions fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, LiveTvOptions.class);
  }

  /**
   * Convert an instance of LiveTvOptions to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

