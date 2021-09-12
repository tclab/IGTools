package com.tclab.igtools.ig.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IgResponseDto {

  private Integer status;
  private String message;

}