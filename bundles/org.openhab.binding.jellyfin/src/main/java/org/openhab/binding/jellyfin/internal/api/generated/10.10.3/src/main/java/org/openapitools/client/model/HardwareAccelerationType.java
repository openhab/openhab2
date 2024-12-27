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
 * Enum containing hardware acceleration types.
 */
@JsonAdapter(HardwareAccelerationType.Adapter.class)
public enum HardwareAccelerationType {
  
  NONE("none"),
  
  AMF("amf"),
  
  QSV("qsv"),
  
  NVENC("nvenc"),
  
  V4L2M2M("v4l2m2m"),
  
  VAAPI("vaapi"),
  
  VIDEOTOOLBOX("videotoolbox"),
  
  RKMPP("rkmpp");

  private String value;

  HardwareAccelerationType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static HardwareAccelerationType fromValue(String value) {
    for (HardwareAccelerationType b : HardwareAccelerationType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

  public static class Adapter extends TypeAdapter<HardwareAccelerationType> {
    @Override
    public void write(final JsonWriter jsonWriter, final HardwareAccelerationType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public HardwareAccelerationType read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return HardwareAccelerationType.fromValue(value);
    }
  }

  public static void validateJsonElement(JsonElement jsonElement) throws IOException {
    String value = jsonElement.getAsString();
    HardwareAccelerationType.fromValue(value);
  }
}

