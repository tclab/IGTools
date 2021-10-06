package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;

public interface SlackService {

  void send(SlackMessageBuilder slackMessageDto);

}
