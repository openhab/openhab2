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
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.JsonElement;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Enum ImageType.
 */
@JsonAdapter(ImageType.Adapter.class)
public enum ImageType {
  
  PRIMARY("Primary"),
  
  ART("Art"),
  
  BACKDROP("Backdrop"),
  
  BANNER("Banner"),
  
  LOGO("Logo"),
  
  THUMB("Thumb"),
  
  DISC("Disc"),
  
  BOX("Box"),
  
  SCREENSHOT("Screenshot"),
  
  MENU("Menu"),
  
  CHAPTER("Chapter"),
  
  BOX_REAR("BoxRear"),
  
  PROFILE("Profile");

  private String value;

  ImageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ImageType fromValue(String value) {
    for (ImageType b : ImageType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<ImageType> {
    @Override
    public void write(final JsonWriter jsonWriter, final ImageType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public ImageType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return ImageType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    ImageType.fromValue(value);
  }
}

