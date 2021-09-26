package com.tclab.igtools.commons.igclient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaContentResponseDto {

  @JsonProperty("id")
  private String igBusinessId;

  @JsonProperty("business_discovery")
  private BusinessDiscovery businessDiscovery;


  @Builder
  @lombok.Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BusinessDiscovery {
    @JsonProperty("id")
    private Long feedAccountIgBusinessId;
    private Media media;
  }

  @Builder
  @lombok.Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Media {
    private List<Data> data;
    // private Paging paging;
  }

  @Builder
  @lombok.Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Data {
    private Long id;
    private String media_type;
    private Long comments_count;
    private Long like_count;
    private String media_url;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime timestamp;
  }

}
