package com.tclab.igtools.media.enumerator;

import java.util.Optional;

public enum MediaType {
  IMAGE,
  VIDEO;

  public static MediaType fromValue(String value) throws Exception {
    return Optional.ofNullable(value)
        .map(String::toUpperCase)
        .map(
            upperValue -> {
              try {
                return MediaType.valueOf(upperValue);
              } catch (Exception ex) {
                return null;
              }
            })
        .orElseThrow(() -> new Exception(String.format("Media type not handled: %s", value)));
  }
}
