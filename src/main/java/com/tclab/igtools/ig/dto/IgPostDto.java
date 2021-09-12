package com.tclab.igtools.ig.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IgPostDto {

  private String url;
  private String caption;
  private String igBussinesAccount;

}
