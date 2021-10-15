package com.tclab.igtools.media.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaResponseDto {

  private Integer status;
  private String message;

}