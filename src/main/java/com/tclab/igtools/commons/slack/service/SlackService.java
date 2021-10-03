package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import org.springframework.http.ResponseEntity;

public interface SlackService {

  void send(SlackMessageBuilder slackMessageDto);
  SlackCommandResponseDto command(SlackCommandDto slackCommandDto);

}
