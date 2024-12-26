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
import org.openapitools.client.model.ImageType;
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
 * Class ImageInfo.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-12-26T23:33:44.988406688+01:00[Europe/Zurich]", comments = "Generator version: 7.10.0")
public class ImageInfo {
  public static final String SERIALIZED_NAME_IMAGE_TYPE = "ImageType";
  @SerializedName(SERIALIZED_NAME_IMAGE_TYPE)
  @javax.annotation.Nullable
  private ImageType imageType;

  public static final String SERIALIZED_NAME_IMAGE_INDEX = "ImageIndex";
  @SerializedName(SERIALIZED_NAME_IMAGE_INDEX)
  @javax.annotation.Nullable
  private Integer imageIndex;

  public static final String SERIALIZED_NAME_IMAGE_TAG = "ImageTag";
  @SerializedName(SERIALIZED_NAME_IMAGE_TAG)
  @javax.annotation.Nullable
  private String imageTag;

  public static final String SERIALIZED_NAME_PATH = "Path";
  @SerializedName(SERIALIZED_NAME_PATH)
  @javax.annotation.Nullable
  private String path;

  public static final String SERIALIZED_NAME_BLUR_HASH = "BlurHash";
  @SerializedName(SERIALIZED_NAME_BLUR_HASH)
  @javax.annotation.Nullable
  private String blurHash;

  public static final String SERIALIZED_NAME_HEIGHT = "Height";
  @SerializedName(SERIALIZED_NAME_HEIGHT)
  @javax.annotation.Nullable
  private Integer height;

  public static final String SERIALIZED_NAME_WIDTH = "Width";
  @SerializedName(SERIALIZED_NAME_WIDTH)
  @javax.annotation.Nullable
  private Integer width;

  public static final String SERIALIZED_NAME_SIZE = "Size";
  @SerializedName(SERIALIZED_NAME_SIZE)
  @javax.annotation.Nullable
  private Long size;

  public ImageInfo() {
  }

  public ImageInfo imageType(@javax.annotation.Nullable ImageType imageType) {
    this.imageType = imageType;
    return this;
  }

  /**
   * Gets or sets the type of the image.
   * @return imageType
   */
  @javax.annotation.Nullable
  public ImageType getImageType() {
    return imageType;
  }

  public void setImageType(@javax.annotation.Nullable ImageType imageType) {
    this.imageType = imageType;
  }


  public ImageInfo imageIndex(@javax.annotation.Nullable Integer imageIndex) {
    this.imageIndex = imageIndex;
    return this;
  }

  /**
   * Gets or sets the index of the image.
   * @return imageIndex
   */
  @javax.annotation.Nullable
  public Integer getImageIndex() {
    return imageIndex;
  }

  public void setImageIndex(@javax.annotation.Nullable Integer imageIndex) {
    this.imageIndex = imageIndex;
  }


  public ImageInfo imageTag(@javax.annotation.Nullable String imageTag) {
    this.imageTag = imageTag;
    return this;
  }

  /**
   * Gets or sets the image tag.
   * @return imageTag
   */
  @javax.annotation.Nullable
  public String getImageTag() {
    return imageTag;
  }

  public void setImageTag(@javax.annotation.Nullable String imageTag) {
    this.imageTag = imageTag;
  }


  public ImageInfo path(@javax.annotation.Nullable String path) {
    this.path = path;
    return this;
  }

  /**
   * Gets or sets the path.
   * @return path
   */
  @javax.annotation.Nullable
  public String getPath() {
    return path;
  }

  public void setPath(@javax.annotation.Nullable String path) {
    this.path = path;
  }


  public ImageInfo blurHash(@javax.annotation.Nullable String blurHash) {
    this.blurHash = blurHash;
    return this;
  }

  /**
   * Gets or sets the blurhash.
   * @return blurHash
   */
  @javax.annotation.Nullable
  public String getBlurHash() {
    return blurHash;
  }

  public void setBlurHash(@javax.annotation.Nullable String blurHash) {
    this.blurHash = blurHash;
  }


  public ImageInfo height(@javax.annotation.Nullable Integer height) {
    this.height = height;
    return this;
  }

  /**
   * Gets or sets the height.
   * @return height
   */
  @javax.annotation.Nullable
  public Integer getHeight() {
    return height;
  }

  public void setHeight(@javax.annotation.Nullable Integer height) {
    this.height = height;
  }


  public ImageInfo width(@javax.annotation.Nullable Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Gets or sets the width.
   * @return width
   */
  @javax.annotation.Nullable
  public Integer getWidth() {
    return width;
  }

  public void setWidth(@javax.annotation.Nullable Integer width) {
    this.width = width;
  }


  public ImageInfo size(@javax.annotation.Nullable Long size) {
    this.size = size;
    return this;
  }

  /**
   * Gets or sets the size.
   * @return size
   */
  @javax.annotation.Nullable
  public Long getSize() {
    return size;
  }

  public void setSize(@javax.annotation.Nullable Long size) {
    this.size = size;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageInfo imageInfo = (ImageInfo) o;
    return Objects.equals(this.imageType, imageInfo.imageType) &&
        Objects.equals(this.imageIndex, imageInfo.imageIndex) &&
        Objects.equals(this.imageTag, imageInfo.imageTag) &&
        Objects.equals(this.path, imageInfo.path) &&
        Objects.equals(this.blurHash, imageInfo.blurHash) &&
        Objects.equals(this.height, imageInfo.height) &&
        Objects.equals(this.width, imageInfo.width) &&
        Objects.equals(this.size, imageInfo.size);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageType, imageIndex, imageTag, path, blurHash, height, width, size);
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
    sb.append("class ImageInfo {\n");
    sb.append("    imageType: ").append(toIndentedString(imageType)).append("\n");
    sb.append("    imageIndex: ").append(toIndentedString(imageIndex)).append("\n");
    sb.append("    imageTag: ").append(toIndentedString(imageTag)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    blurHash: ").append(toIndentedString(blurHash)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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
    openapiFields.add("ImageType");
    openapiFields.add("ImageIndex");
    openapiFields.add("ImageTag");
    openapiFields.add("Path");
    openapiFields.add("BlurHash");
    openapiFields.add("Height");
    openapiFields.add("Width");
    openapiFields.add("Size");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

  /**
   * Validates the JSON Element and throws an exception if issues found
   *
   * @param jsonElement JSON Element
   * @throws IOException if the JSON Element is invalid with respect to ImageInfo
   */
  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
      if (jsonElement == null) {
        if (!ImageInfo.openapiRequiredFields.isEmpty()) { // has required fields but JSON element is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in ImageInfo is not found in the empty JSON string", ImageInfo.openapiRequiredFields.toString()));
        }
      }

      Set<Map.Entry<String, JsonElement>> entries = jsonElement.getAsJsonObject().entrySet();
      // check to see if the JSON string contains additional fields
      for (Map.Entry<String, JsonElement> entry : entries) {
        if (!ImageInfo.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `ImageInfo` properties. JSON: %s", entry.getKey(), jsonElement.toString()));
        }
      }
        JsonObject jsonObj = jsonElement.getAsJsonObject();
      // validate the optional field `ImageType`
      if (jsonObj.get("ImageType") != null && !jsonObj.get("ImageType").isJsonNull()) {
        ImageType.validateJsonElement(jsonObj.get("ImageType"));
      }
      if ((jsonObj.get("ImageTag") != null && !jsonObj.get("ImageTag").isJsonNull()) && !jsonObj.get("ImageTag").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ImageTag` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ImageTag").toString()));
      }
      if ((jsonObj.get("Path") != null && !jsonObj.get("Path").isJsonNull()) && !jsonObj.get("Path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `Path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("Path").toString()));
      }
      if ((jsonObj.get("BlurHash") != null && !jsonObj.get("BlurHash").isJsonNull()) && !jsonObj.get("BlurHash").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `BlurHash` to be a primitive type in the JSON string but got `%s`", jsonObj.get("BlurHash").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!ImageInfo.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'ImageInfo' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<ImageInfo> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(ImageInfo.class));

       return (TypeAdapter<T>) new TypeAdapter<ImageInfo>() {
           @Override
           public void write(JsonWriter out, ImageInfo value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public ImageInfo read(JsonReader in) throws IOException {
             JsonElement jsonElement = elementAdapter.read(in);
             validateJsonElement(jsonElement);
             return thisAdapter.fromJsonTree(jsonElement);
           }

       }.nullSafe();
    }
  }

  /**
   * Create an instance of ImageInfo given an JSON string
   *
   * @param jsonString JSON string
   * @return An instance of ImageInfo
   * @throws IOException if the JSON string is invalid with respect to ImageInfo
   */
  public static ImageInfo fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, ImageInfo.class);
  }

  /**
   * Convert an instance of ImageInfo to an JSON string
   *
   * @return JSON string
   */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

