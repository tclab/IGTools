package com.tclab.igtools.commons.slack.controller;

import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.service.SlackCommandService;
import com.tclab.igtools.commons.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/slack")
@RequiredArgsConstructor
public class SlackController {

  private final SlackService slackService;
  private final SlackCommandService slackCommandService;

  @PostMapping
  public void postResourceManually(@RequestBody SlackMessageBuilder slackMessageDto) {
    slackService.send(slackMessageDto);
  }

  @PostMapping(value = "/command"
      , consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
      , produces = {MediaType.APPLICATION_JSON_VALUE})
  public SlackCommandResponseDto commands(SlackCommandDto slackCommandDto) {
    return slackCommandService.execute(slackCommandDto);
  }
}
