package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;
import com.tclab.igtools.commons.slack.dto.SlackDto;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.service.SlackCommand.SlackCommandType;
import com.tclab.igtools.commons.slack.service.feign.SlackFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackServiceImpl implements SlackService{

  @Value("${api.slack.pss}")
  private String token;

  private final SlackFeignService slackFeignService;
  private final SlackCommand slackCommand;

  @Override
  public void send(SlackMessageBuilder slackMessageDto) {
    slackFeignService.sendMessage(token, SlackDto.builder().text(slackMessageDto.buildMessage()).build());
  }

  @Override
  public SlackCommandResponseDto command(SlackCommandDto slackCommandDto) {
    return slackCommand.execute(slackCommandDto);
  }

}
