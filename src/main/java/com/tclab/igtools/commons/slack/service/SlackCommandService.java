package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;

public interface SlackCommandService {

  SlackCommandResponseDto execute(SlackCommandDto params);

}
