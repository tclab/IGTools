package com.tclab.igtools.commons.igclient.service;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class FieldsParamBuilder {

  private String fields;
  private String username;
  private Long limit;

  public FieldsParamBuilder processFieldsParamValue() {
    try {
      this.fields = String.format("business_discovery.username(%s){media.limit(%d){id,media_type,comments_count,like_count,media_url,timestamp}}",
          this.username, this.limit);
    } catch (Exception e) {
      log.error("Could not get feeds param value {}", e.getMessage());
    }
    return this;
  }

}
