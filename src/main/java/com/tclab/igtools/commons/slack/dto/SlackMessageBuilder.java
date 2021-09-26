package com.tclab.igtools.commons.slack.dto;

import com.tclab.igtools.commons.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackMessageBuilder {

  // basic
  private String service;
  private MessageType type;
  private String message;

  // [IGTools] - post
  private String account;
  private String igBusinessAccountId;
  private String resource;

  // error
  private String stackTrace;


  public String buildMessage() {

    String slackMessage = String.format(
        "*Service*: %s \n*Action*: %s \n*Message*: %s",
        this.service,
        this.type.name(),
        this.message);

    // [IGTools] - post
    if (!Utils.isEmpty(this.account)) slackMessage += "\n*Account*: " + this.account;
    if (!Utils.isEmpty(this.igBusinessAccountId)) slackMessage += "\n*Account id*: " + this.igBusinessAccountId;
    if (!Utils.isEmpty(this.resource)) slackMessage += "\n*Resource*: " + this.resource;

    // error
    if (!Utils.isEmpty(this.stackTrace)) slackMessage += "\n*StackTrace*: " + this.stackTrace;

    return slackMessage;
  }



  // Message type
  public enum MessageType {
    INFO,
    POST,
    HYDRATE,
    ERROR;
  }

}

/*
  Sample request json:

  {
    "service" : ""
    "type" : ""
    "message" : ""
    "igBusinessAccountId" : ""
    "resource" : ""
    "stackTrace" : ""
  }



 */
