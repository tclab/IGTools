package com.tclab.igtools.commons.slack.controller;

import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/slack")
@RequiredArgsConstructor
public class SlackController {

  private final SlackService slackService;

  @PostMapping
  public void postResourceManually(@RequestBody SlackMessageBuilder slackMessageDto) {
    slackService.send(slackMessageDto);
  }
}
