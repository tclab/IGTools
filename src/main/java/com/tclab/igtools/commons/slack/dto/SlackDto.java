package com.tclab.igtools.commons.slack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlackDto {

  private String text;
}
