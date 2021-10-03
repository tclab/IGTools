package com.tclab.igtools.commons.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackCommandDto {

  private String token;
  private String team_id;
  private String team_domain;
  private String enterprise_id;
  private String enterprise_name;
  private String channel_id;
  private String channel_name;
  private String user_id;
  private String user_name;
  private String command;
  private String text;
  private String response_url;
  private String trigger_id;
  private String api_app_id;

}
