package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.commons.slack.dto.SlackDto;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.service.feign.SlackFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService{

  @Value("${api.slack.pss}")
  private String token;

  private final SlackFeignService slackFeignService;

  @Override
  public void send(SlackMessageBuilder slackMessageDto) {
    slackFeignService.sendMessage(token, SlackDto.builder().text(slackMessageDto.buildMessage()).build());
  }

}
