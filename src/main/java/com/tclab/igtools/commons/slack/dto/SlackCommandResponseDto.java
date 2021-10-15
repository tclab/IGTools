package com.tclab.igtools.commons.slack.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class SlackCommandResponseDto {

  String text;
  String response_type;



  public enum SlackResponseType{

    EPHEMERAL("ephemeral"),
    CHANNEL("in_channel");

    SlackResponseType(String value){
      this.value = value;
    }

    @Getter
    private String value;

  }
}
