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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.client.model.BaseItemDto;
import org.openapitools.client.model.KeepUntil;
import org.openapitools.client.model.RecordingStatus;
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
 * TimerInfoDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-27T20:32:44.334408221+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class TimerInfoDto {
  public static final String SERIALIZED_NAME_ID = "Id";
  @SerializedName(SERIALIZED_NAME_ID)
  @javax.annotation.Nullable
  private String id;

  public static final String SERIALIZED_NAME_TYPE = "Type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  @javax.annotation.Nullable
  private String type;

  public static final String SERIALIZED_NAME_SERVER_ID = "ServerId";
  @SerializedName(SERIALIZED_NAME_SERVER_ID)
  @javax.annotation.Nullable
  private String serverId;

  public static final String SERIALIZED_NAME_EXTERNAL_ID = "ExternalId";
  @SerializedName(SERIALIZED_NAME_EXTERNAL_ID)
  @javax.annotation.Nullable
  private String externalId;

  public static final String SERIALIZED_NAME_CHANNEL_ID = "ChannelId";
  @SerializedName(SERIALIZED_NAME_CHANNEL_ID)
  @javax.annotation.Nullable
  private UUID channelId;

  public static final String SERIALIZED_NAME_EXTERNAL_CHANNEL_ID = "ExternalChannelId";
  @SerializedName(SERIALIZED_NAME_EXTERNAL_CHANNEL_ID)
  @javax.annotation.Nullable
  private String externalChannelId;

  public static final String SERIALIZED_NAME_CHANNEL_NAME = "ChannelName";
  @SerializedName(SERIALIZED_NAME_CHANNEL_NAME)
  @javax.annotation.Nullable
  private String channelName;

  public static final String SERIALIZED_NAME_CHANNEL_PRIMARY_IMAGE_TAG = "ChannelPrimaryImageTag";
  @SerializedName(SERIALIZED_NAME_CHANNEL_PRIMARY_IMAGE_TAG)
  @javax.annotation.Nullable
  private String channelPrimaryImageTag;

  public static final String SERIALIZED_NAME_PROGRAM_ID = "ProgramId";
  @SerializedName(SERIALIZED_NAME_PROGRAM_ID)
  @javax.annotation.Nullable
  private String programId;

  public static final String SERIALIZED_NAME_EXTERNAL_PROGRAM_ID = "ExternalProgramId";
  @SerializedName(SERIALIZED_NAME_EXTERNAL_PROGRAM_ID)
  @javax.annotation.Nullable
  private String externalProgramId;

  public static final String SERIALIZED_NAME_NAME = "Name";
  @SerializedName(SERIALIZED_NAME_NAME)
  @javax.annotation.Nullable
  private String name;

  public static final String SERIALIZED_NAME_OVERVIEW = "Overview";
  @SerializedName(SERIALIZED_NAME_OVERVIEW)
  @javax.annotation.Nullable
  private String overview;

  public static final String SERIALIZED_NAME_START_DATE = "StartDate";
  @SerializedName(SERIALIZED_NAME_START_DATE)
  @javax.annotation.Nullable
  private OffsetDateTime startDate;

  public static final String SERIALIZED_NAME_END_DATE = "EndDate";
  @SerializedName(SERIALIZED_NAME_END_DATE)
  @javax.annotation.Nullable
  private OffsetDateTime endDate;

  public static final String SERIALIZED_NAME_SERVICE_NAME = "ServiceName";
  @SerializedName(SERIALIZED_NAME_SERVICE_NAME)
  @javax.annotation.Nullable
  private String serviceName;

  public static final String SERIALIZED_NAME_PRIORITY = "Priority";
  @SerializedName(SERIALIZED_NAME_PRIORITY)
  @javax.annotation.Nullable
  private Integer priority;

  public static final String SERIALIZED_NAME_PRE_PADDING_SECONDS = "PrePaddingSeconds";
  @SerializedName(SERIALIZED_NAME_PRE_PADDING_SECONDS)
  @javax.annotation.Nullable
  private Integer prePaddingSeconds;

  public static final String SERIALIZED_NAME_POST_PADDING_SECONDS = "PostPaddingSeconds";
  @SerializedName(SERIALIZED_NAME_POST_PADDING_SECONDS)
  @javax.annotation.Nullable
  private Integer postPaddingSeconds;

  public static final String SERIALIZED_NAME_IS_PRE_PADDING_REQUIRED = "IsPrePaddingRequired";
  @SerializedName(SERIALIZED_NAME_IS_PRE_PADDING_REQUIRED)
  @javax.annotation.Nullable
  private Boolean isPrePaddingRequired;

  public static final String SERIALIZED_NAME_PARENT_BACKDROP_ITEM_ID = "ParentBackdropItemId";
  @SerializedName(SERIALIZED_NAME_PARENT_BACKDROP_ITEM_ID)
  @javax.annotation.Nullable
  private String parentBackdropItemId;

  public static final String SERIALIZED_NAME_PARENT_BACKDROP_IMAGE_TAGS = "ParentBackdropImageTags";
  @SerializedName(SERIALIZED_NAME_PARENT_BACKDROP_IMAGE_TAGS)
  @javax.annotation.Nullable
  private List<String> parentBackdropImageTags;

  public static final String SERIALIZED_NAME_IS_POST_PADDING_REQUIRED = "IsPostPaddingRequired";
  @SerializedName(SERIALIZED_NAME_IS_POST_PADDING_REQUIRED)
  @javax.annotation.Nullable
  private Boolean isPostPaddingRequired;

  public static final String SERIALIZED_NAME_KEEP_UNTIL = "KeepUntil";
  @SerializedName(SERIALIZED_NAME_KEEP_UNTIL)
  @javax.annotation.Nullable
  private KeepUntil keepUntil;

  public static final String SERIALIZED_NAME_STATUS = "Status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  @javax.annotation.Nullable
  private RecordingStatus status;

  public static final String SERIALIZED_NAME_SERIES_TIMER_ID = "SeriesTimerId";
  @SerializedName(SERIALIZED_NAME_SERIES_TIMER_ID)
  @javax.annotation.Nullable
  private String seriesTimerId;

  public static final String SERIALIZED_NAME_EXTERNAL_SERIES_TIMER_ID = "ExternalSeriesTimerId";
  @SerializedName(SERIALIZED_NAME_EXTERNAL_SERIES_TIMER_ID)
  @javax.annotation.Nullable
  private String externalSeriesTimerId;

  public static final String SERIALIZED_NAME_RUN_TIME_TICKS = "RunTimeTicks";
  @SerializedName(SERIALIZED_NAME_RUN_TIME_TICKS)
  @javax.annotation.Nullable
  private Long runTimeTicks;

  public static final String SERIALIZED_NAME_PROGRAM_INFO = "ProgramInfo";
  @SerializedName(SERIALIZED_NAME_PROGRAM_INFO)
  @javax.annotation.Nullable
  private BaseItemDto programInfo;

  public TimerInfoDto() {
  }

  public TimerInfoDto id(@javax.annotation.Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Gets or sets the Id of the recording.
   * @return id
   */
  @javax.annotation.Nullable
  public String getId() {
    return id;
  }

  public void setId(@javax.annotation.Nullable String id) {
    this.id = id;
  }


  public TimerInfoDto type(@javax.annotation.Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @javax.annotation.Nullable
  public String getType() {
    return type;
  }

  public void setType(@javax.annotation.Nullable String type) {
    this.type = type;
  }


  public TimerInfoDto serverId(@javax.annotation.Nullable String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * Gets or sets the server identifier.
   * @return serverId
   */
  @javax.annotation.Nullable
  public String getServerId() {
    return serverId;
  }

  public void setServerId(@javax.annotation.Nullable String serverId) {
    this.serverId = serverId;
  }


  public TimerInfoDto externalId(@javax.annotation.Nullable String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * Gets or sets the external identifier.
   * @return externalId
   */
  @javax.annotation.Nullable
  public String getExternalId() {
    return externalId;
  }

  public void setExternalId(@javax.annotation.Nullable String externalId) {
    this.externalId = externalId;
  }


  public TimerInfoDto channelId(@javax.annotation.Nullable UUID channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Gets or sets the channel id of the recording.
   * @return channelId
   */
  @javax.annotation.Nullable
  public UUID getChannelId() {
    return channelId;
  }

  public void setChannelId(@javax.annotation.Nullable UUID channelId) {
    this.channelId = channelId;
  }


  public TimerInfoDto externalChannelId(@javax.annotation.Nullable String externalChannelId) {
    this.externalChannelId = externalChannelId;
    return this;
  }

  /**
   * Gets or sets the external channel identifier.
   * @return externalChannelId
   */
  @javax.annotation.Nullable
  public String getExternalChannelId() {
    return externalChannelId;
  }

  public void setExternalChannelId(@javax.annotation.Nullable String externalChannelId) {
    this.externalChannelId = externalChannelId;
  }


  public TimerInfoDto channelName(@javax.annotation.Nullable String channelName) {
    this.channelName = channelName;
    return this;
  }

  /**
   * Gets or sets the channel name of the recording.
   * @return channelName
   */
  @javax.annotation.Nullable
  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(@javax.annotation.Nullable String channelName) {
    this.channelName = channelName;
  }


  public TimerInfoDto channelPrimaryImageTag(@javax.annotation.Nullable String channelPrimaryImageTag) {
    this.channelPrimaryImageTag = channelPrimaryImageTag;
    return this;
  }

  /**
   * Get channelPrimaryImageTag
   * @return channelPrimaryImageTag
   */
  @javax.annotation.Nullable
  public String getChannelPrimaryImageTag() {
    return channelPrimaryImageTag;
  }

  public void setChannelPrimaryImageTag(@javax.annotation.Nullable String channelPrimaryImageTag) {
    this.channelPrimaryImageTag = channelPrimaryImageTag;
  }


  public TimerInfoDto programId(@javax.annotation.Nullable String programId) {
    this.programId = programId;
    return this;
  }

  /**
   * Gets or sets the program identifier.
   * @return programId
   */
  @javax.annotation.Nullable
  public String getProgramId() {
    return programId;
  }

  public void setProgramId(@javax.annotation.Nullable String programId) {
    this.programId = programId;
  }


  public TimerInfoDto externalProgramId(@javax.annotation.Nullable String externalProgramId) {
    this.externalProgramId = externalProgramId;
    return this;
  }

  /**
   * Gets or sets the external program identifier.
   * @return externalProgramId
   */
  @javax.annotation.Nullable
  public String getExternalProgramId() {
    return externalProgramId;
  }

  public void setExternalProgramId(@javax.annotation.Nullable String externalProgramId) {
    this.externalProgramId = externalProgramId;
  }


  public TimerInfoDto name(@javax.annotation.Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets or sets the name of the recording.
   * @return name
   */
  @javax.annotation.Nullable
  public String getName() {
    return name;
  }

  public void setName(@javax.annotation.Nullable String name) {
    this.name = name;
  }


  public TimerInfoDto overview(@javax.annotation.Nullable String overview) {
    this.overview = overview;
    return this;
  }

  /**
   * Gets or sets the description of the recording.
   * @return overview
   */
  @javax.annotation.Nullable
  public String getOverview() {
    return overview;
  }

  public void setOverview(@javax.annotation.Nullable String overview) {
    this.overview = overview;
  }


  public TimerInfoDto startDate(@javax.annotation.Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Gets or sets the start date of the recording, in UTC.
   * @return startDate
   */
  @javax.annotation.Nullable
  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@javax.annotation.Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }


  public TimerInfoDto endDate(@javax.annotation.Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Gets or sets the end date of the recording, in UTC.
   * @return endDate
   */
  @javax.annotation.Nullable
  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(@javax.annotation.Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }


  public TimerInfoDto serviceName(@javax.annotation.Nullable String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * Gets or sets the name of the service.
   * @return serviceName
   */
  @javax.annotation.Nullable
  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(@javax.annotation.Nullable String serviceName) {
    this.serviceName = serviceName;
  }


  public TimerInfoDto priority(@javax.annotation.Nullable Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Gets or sets the priority.
   * @return priority
   */
  @javax.annotation.Nullable
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(@javax.annotation.Nullable Integer priority) {
    this.priority = priority;
  }


  public TimerInfoDto prePaddingSeconds(@javax.annotation.Nullable Integer prePaddingSeconds) {
    this.prePaddingSeconds = prePaddingSeconds;
    return this;
  }

  /**
   * Gets or sets the pre padding seconds.
   * @return prePaddingSeconds
   */
  @javax.annotation.Nullable
  public Integer getPrePaddingSeconds() {
    return prePaddingSeconds;
  }

  public void setPrePaddingSeconds(@javax.annotation.Nullable Integer prePaddingSeconds) {
    this.prePaddingSeconds = prePaddingSeconds;
  }


  public TimerInfoDto postPaddingSeconds(@javax.annotation.Nullable Integer postPaddingSeconds) {
    this.postPaddingSeconds = postPaddingSeconds;
    return this;
  }

  /**
   * Gets or sets the post padding seconds.
   * @return postPaddingSeconds
   */
  @javax.annotation.Nullable
  public Integer getPostPaddingSeconds() {
    return postPaddingSeconds;
  }

  public void setPostPaddingSeconds(@javax.annotation.Nullable Integer postPaddingSeconds) {
    this.postPaddingSeconds = postPaddingSeconds;
  }


  public TimerInfoDto isPrePaddingRequired(@javax.annotation.Nullable Boolean isPrePaddingRequired) {
    this.isPrePaddingRequired = isPrePaddingRequired;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance is pre padding required.
   * @return isPrePaddingRequired
   */
  @javax.annotation.Nullable
  public Boolean getIsPrePaddingRequired() {
    return isPrePaddingRequired;
  }

  public void setIsPrePaddingRequired(@javax.annotation.Nullable Boolean isPrePaddingRequired) {
    this.isPrePaddingRequired = isPrePaddingRequired;
  }


  public TimerInfoDto parentBackdropItemId(@javax.annotation.Nullable String parentBackdropItemId) {
    this.parentBackdropItemId = parentBackdropItemId;
    return this;
  }

  /**
   * Gets or sets the Id of the Parent that has a backdrop if the item does not have one.
   * @return parentBackdropItemId
   */
  @javax.annotation.Nullable
  public String getParentBackdropItemId() {
    return parentBackdropItemId;
  }

  public void setParentBackdropItemId(@javax.annotation.Nullable String parentBackdropItemId) {
    this.parentBackdropItemId = parentBackdropItemId;
  }


  public TimerInfoDto parentBackdropImageTags(@javax.annotation.Nullable List<String> parentBackdropImageTags) {
    this.parentBackdropImageTags = parentBackdropImageTags;
    return this;
  }

  public TimerInfoDto addParentBackdropImageTagsItem(String parentBackdropImageTagsItem) {
    if (this.parentBackdropImageTags == null) {
      this.parentBackdropImageTags = new ArrayList<>();
    }
    this.parentBackdropImageTags.add(parentBackdropImageTagsItem);
    return this;
  }

  /**
   * Gets or sets the parent backdrop image tags.
   * @return parentBackdropImageTags
   */
  @javax.annotation.Nullable
  public List<String> getParentBackdropImageTags() {
    return parentBackdropImageTags;
  }

  public void setParentBackdropImageTags(@javax.annotation.Nullable List<String> parentBackdropImageTags) {
    this.parentBackdropImageTags = parentBackdropImageTags;
  }


  public TimerInfoDto isPostPaddingRequired(@javax.annotation.Nullable Boolean isPostPaddingRequired) {
    this.isPostPaddingRequired = isPostPaddingRequired;
    return this;
  }

  /**
   * Gets or sets a value indicating whether this instance is post padding required.
   * @return isPostPaddingRequired
   */
  @javax.annotation.Nullable
  public Boolean getIsPostPaddingRequired() {
    return isPostPaddingRequired;
  }

  public void setIsPostPaddingRequired(@javax.annotation.Nullable Boolean isPostPaddingRequired) {
    this.isPostPaddingRequired = isPostPaddingRequired;
  }


  public TimerInfoDto keepUntil(@javax.annotation.Nullable KeepUntil keepUntil) {
    this.keepUntil = keepUntil;
    return this;
  }

  /**
   * Get keepUntil
   * @return keepUntil
   */
  @javax.annotation.Nullable
  public KeepUntil getKeepUntil() {
    return keepUntil;
  }

  public void setKeepUntil(@javax.annotation.Nullable KeepUntil keepUntil) {
    this.keepUntil = keepUntil;
  }


  public TimerInfoDto status(@javax.annotation.Nullable RecordingStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Gets or sets the status.
   * @return status
   */
  @javax.annotation.Nullable
  public RecordingStatus getStatus() {
    return status;
  }

  public void setStatus(@javax.annotation.Nullable RecordingStatus status) {
    this.status = status;
  }


  public TimerInfoDto seriesTimerId(@javax.annotation.Nullable String seriesTimerId) {
    this.seriesTimerId = seriesTimerId;
    return this;
  }

  /**
   * Gets or sets the series timer identifier.
   * @return seriesTimerId
   */
  @javax.annotation.Nullable
  public String getSeriesTimerId() {
    return seriesTimerId;
  }

  public void setSeriesTimerId(@javax.annotation.Nullable String seriesTimerId) {
    this.seriesTimerId = seriesTimerId;
  }


  public TimerInfoDto externalSeriesTimerId(@javax.annotation.Nullable String externalSeriesTimerId) {
    this.externalSeriesTimerId = externalSeriesTimerId;
    return this;
  }

  /**
   * Gets or sets the external series timer identifier.
   * @return externalSeriesTimerId
   */
  @javax.annotation.Nullable
  public String getExternalSeriesTimerId() {
    return externalSeriesTimerId;
  }

  public void setExternalSeriesTimerId(@javax.annotation.Nullable String externalSeriesTimerId) {
    this.externalSeriesTimerId = externalSeriesTimerId;
  }


  public TimerInfoDto runTimeTicks(@javax.annotation.Nullable Long runTimeTicks) {
    this.runTimeTicks = runTimeTicks;
    return this;
  }

  /**
   * Gets or sets the run time ticks.
   * @return runTimeTicks
   */
  @javax.annotation.Nullable
  public Long getRunTimeTicks() {
    return runTimeTicks;
  }

  public void setRunTimeTicks(@javax.annotation.Nullable Long runTimeTicks) {
    this.runTimeTicks = runTimeTicks;
  }


  public TimerInfoDto programInfo(@javax.annotation.Nullable BaseItemDto programInfo) {
    this.programInfo = programInfo;
    return this;
  }

  /**
   * Gets or sets the program information.
   * @return programInfo
   */
  @javax.annotation.Nullable
  public BaseItemDto getProgramInfo() {
    return programInfo;
  }

  public void setProgramInfo(@javax.annotation.Nullable BaseItemDto programInfo) {
    this.programInfo = programInfo;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimerInfoDto timerInfoDto = (TimerInfoDto) o;
    return Objects.equals(this.id, timerInfoDto.id) &&
        Objects.equals(this.type, timerInfoDto.type) &&
        Objects.equals(this.serverId, timerInfoDto.serverId) &&
        Objects.equals(this.externalId, timerInfoDto.externalId) &&
        Objects.equals(this.channelId, timerInfoDto.channelId) &&
        Objects.equals(this.externalChannelId, timerInfoDto.externalChannelId) &&
        Objects.equals(this.channelName, timerInfoDto.channelName) &&
        Objects.equals(this.channelPrimaryImageTag, timerInfoDto.channelPrimaryImageTag) &&
        Objects.equals(this.programId, timerInfoDto.programId) &&
        Objects.equals(this.externalProgramId, timerInfoDto.externalProgramId) &&
        Objects.equals(this.name, timerInfoDto.name) &&
        Objects.equals(this.overview, timerInfoDto.overview) &&
        Objects.equals(this.startDate, timerInfoDto.startDate) &&
        Objects.equals(this.endDate, timerInfoDto.endDate) &&
        Objects.equals(this.serviceName, timerInfoDto.serviceName) &&
        Objects.equals(this.priority, timerInfoDto.priority) &&
        Objects.equals(this.prePaddingSeconds, timerInfoDto.prePaddingSeconds) &&
        Objects.equals(this.postPaddingSeconds, timerInfoDto.postPaddingSeconds) &&
        Objects.equals(this.isPrePaddingRequired, timerInfoDto.isPrePaddingRequired) &&
        Objects.equals(this.parentBackdropItemId, timerInfoDto.parentBackdropItemId) &&
        Objects.equals(this.parentBackdropImageTags, timerInfoDto.parentBackdropImageTags) &&
        Objects.equals(this.isPostPaddingRequired, timerInfoDto.isPostPaddingRequired) &&
        Objects.equals(this.keepUntil, timerInfoDto.keepUntil) &&
        Objects.equals(this.status, timerInfoDto.status) &&
        Objects.equals(this.seriesTimerId, timerInfoDto.seriesTimerId) &&
        Objects.equals(this.externalSeriesTimerId, timerInfoDto.externalSeriesTimerId) &&
        Objects.equals(this.runTimeTicks, timerInfoDto.runTimeTicks) &&
        Objects.equals(this.programInfo, timerInfoDto.programInfo);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, serverId, externalId, channelId, externalChannelId, channelName, channelPrimaryImageTag, programId, externalProgramId, name, overview, startDate, endDate, serviceName, priority, prePaddingSeconds, postPaddingSeconds, isPrePaddingRequired, parentBackdropItemId, parentBackdropImageTags, isPostPaddingRequired, keepUntil, status, seriesTimerId, externalSeriesTimerId, runTimeTicks, programInfo);
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
    sb.append("class TimerInfoDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    externalChannelId: ").append(toIndentedString(externalChannelId)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    channelPrimaryImageTag: ").append(toIndentedString(channelPrimaryImageTag)).append("\n");
    sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
    sb.append("    externalProgramId: ").append(toIndentedString(externalProgramId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    overview: ").append(toIndentedString(overview)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    prePaddingSeconds: ").append(toIndentedString(prePaddingSeconds)).append("\n");
    sb.append("    postPaddingSeconds: ").append(toIndentedString(postPaddingSeconds)).append("\n");
    sb.append("    isPrePaddingRequired: ").append(toIndentedString(isPrePaddingRequired)).append("\n");
    sb.append("    parentBackdropItemId: ").append(toIndentedString(parentBackdropItemId)).append("\n");
    sb.append("    parentBackdropImageTags: ").append(toIndentedString(parentBackdropImageTags)).append("\n");
    sb.append("    isPostPaddingRequired: ").append(toIndentedString(isPostPaddingRequired)).append("\n");
    sb.append("    keepUntil: ").append(toIndentedString(keepUntil)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    seriesTimerId: ").append(toIndentedString(seriesTimerId)).append("\n");
    sb.append("    externalSeriesTimerId: ").append(toIndentedString(externalSeriesTimerId)).append("\n");
    sb.append("    runTimeTicks: ").append(toIndentedString(runTimeTicks)).append("\n");
    sb.append("    programInfo: ").append(toIndentedString(programInfo)).append("\n");
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
    openapiFields.add("Id");
    openapiFields.add("Type");
    openapiFields.add("ServerId");
    openapiFields.add("ExternalId");
    openapiFields.add("ChannelId");
    openapiFields.add("ExternalChannelId");
    openapiFields.add("ChannelName");
    openapiFields.add("ChannelPrimaryImageTag");
    openapiFields.add("ProgramId");
    openapiFields.add("ExternalProgramId");
    openapiFields.add("Name");
    openapiFields.add("Overview");
    openapiFields.add("StartDate");
    openapiFields.add("EndDate");
    openapiFields.add("ServiceName");
    openapiFields.add("Priority");
    openapiFields.add("PrePaddingSeconds");
    openapiFields.add("PostPaddingSeconds");
    openapiFields.add("IsPrePaddingRequired");
    openapiFields.add("ParentBackdropItemId");
    openapiFields.add("ParentBackdropImageTags");
    openapiFields.add("IsPostPaddingRequired");
    openapiFields.add("KeepUntil");
    openapiFields.add("Status");
    openapiFields.add("SeriesTimerId");
    openapiFields.add("ExternalSeriesTimerId");
    openapiFields.add("RunTimeTicks");
    openapiFields.add("ProgramInfo");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to TimerInfoDto
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!TimerInfoDto.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in TimerInfoDto is not found in the empty JSON string", TimerInfoDto.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!TimerInfoDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `TimerInfoDto` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      if ((jsonObj.get("Id") != null && !jsonObj.get("Id").isJsonNull()) && !jsonObj.get("Id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Id").toString()));
      }
      if ((jsonObj.get("Type") != null && !jsonObj.get("Type").isJsonNull()) && !jsonObj.get("Type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Type").toString()));
      }
      if ((jsonObj.get("ServerId") != null && !jsonObj.get("ServerId").isJsonNull()) && !jsonObj.get("ServerId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ServerId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ServerId").toString()));
      }
      if ((jsonObj.get("ExternalId") != null && !jsonObj.get("ExternalId").isJsonNull()) && !jsonObj.get("ExternalId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ExternalId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ExternalId").toString()));
      }
      if ((jsonObj.get("ChannelId") != null && !jsonObj.get("ChannelId").isJsonNull()) && !jsonObj.get("ChannelId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ChannelId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ChannelId").toString()));
      }
      if ((jsonObj.get("ExternalChannelId") != null && !jsonObj.get("ExternalChannelId").isJsonNull()) && !jsonObj.get("ExternalChannelId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ExternalChannelId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ExternalChannelId").toString()));
      }
      if ((jsonObj.get("ChannelName") != null && !jsonObj.get("ChannelName").isJsonNull()) && !jsonObj.get("ChannelName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ChannelName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ChannelName").toString()));
      }
      if ((jsonObj.get("ChannelPrimaryImageTag") != null && !jsonObj.get("ChannelPrimaryImageTag").isJsonNull()) && !jsonObj.get("ChannelPrimaryImageTag").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ChannelPrimaryImageTag` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ChannelPrimaryImageTag").toString()));
      }
      if ((jsonObj.get("ProgramId") != null && !jsonObj.get("ProgramId").isJsonNull()) && !jsonObj.get("ProgramId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ProgramId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ProgramId").toString()));
      }
      if ((jsonObj.get("ExternalProgramId") != null && !jsonObj.get("ExternalProgramId").isJsonNull()) && !jsonObj.get("ExternalProgramId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ExternalProgramId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ExternalProgramId").toString()));
      }
      if ((jsonObj.get("Name") != null && !jsonObj.get("Name").isJsonNull()) && !jsonObj.get("Name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Name").toString()));
      }
      if ((jsonObj.get("Overview") != null && !jsonObj.get("Overview").isJsonNull()) && !jsonObj.get("Overview").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Overview` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Overview").toString()));
      }
      if ((jsonObj.get("ServiceName") != null && !jsonObj.get("ServiceName").isJsonNull()) && !jsonObj.get("ServiceName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ServiceName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ServiceName").toString()));
      }
      if ((jsonObj.get("ParentBackdropItemId") != null && !jsonObj.get("ParentBackdropItemId").isJsonNull()) && !jsonObj.get("ParentBackdropItemId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ParentBackdropItemId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ParentBackdropItemId").toString()));
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("ParentBackdropImageTags") != null && !jsonObj.get("ParentBackdropImageTags").isJsonNull() && !jsonObj.get("ParentBackdropImageTags").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `ParentBackdropImageTags` to be an array in the JSON string but got `%s`", jsonObj.get("ParentBackdropImageTags").toString()));
      }
      // validate the optional field `KeepUntil`
      if (jsonObj.get("KeepUntil") != null && !jsonObj.get("KeepUntil").isJsonNull()) {
        KeepUntil.validateJsonElement(jsonObj.get("KeepUntil"));
      }
      // validate the optional field `Status`
      if (jsonObj.get("Status") != null && !jsonObj.get("Status").isJsonNull()) {
        RecordingStatus.validateJsonElement(jsonObj.get("Status"));
      }
      if ((jsonObj.get("SeriesTimerId") != null && !jsonObj.get("SeriesTimerId").isJsonNull()) && !jsonObj.get("SeriesTimerId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `SeriesTimerId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("SeriesTimerId").toString()));
      }
      if ((jsonObj.get("ExternalSeriesTimerId") != null && !jsonObj.get("ExternalSeriesTimerId").isJsonNull()) && !jsonObj.get("ExternalSeriesTimerId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ExternalSeriesTimerId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ExternalSeriesTimerId").toString()));
      }
      // validate the optional field `ProgramInfo`
      if (jsonObj.get("ProgramInfo") != null && !jsonObj.get("ProgramInfo").isJsonNull()) {
        BaseItemDto.validateJsonElement(jsonObj.get("ProgramInfo"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!TimerInfoDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'TimerInfoDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<TimerInfoDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(TimerInfoDto.class));

       return (TypeAdapter<T>) new TypeAdapter<TimerInfoDto>() {
           @Override
           public void write(JsonWriter out, TimerInfoDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public TimerInfoDto read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of TimerInfoDto given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of TimerInfoDto
   * @throws IOException if the JSON string is invalid with respect to TimerInfoDto
   */
  public static TimerInfoDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, TimerInfoDto.class);
  }

  /**
   * Convert an instance of TimerInfoDto to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

