package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.enumerator.AccountType;
import com.tclab.igtools.account.service.AccountService;
import com.tclab.igtools.commons.exception.InvalidDataException;
import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto.SlackResponseType;
import com.tclab.igtools.commons.slack.dto.SlackDto;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.service.feign.SlackFeignService;
import com.tclab.igtools.commons.utils.Utils;
import com.tclab.igtools.media.dto.HydrateMediaDto;
import com.tclab.igtools.media.service.MediaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
