package com.tclab.igtools.commons.slack.service;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.service.AccountService;
import com.tclab.igtools.commons.exception.InvalidDataException;
import com.tclab.igtools.commons.slack.dto.SlackCommandDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto;
import com.tclab.igtools.commons.slack.dto.SlackCommandResponseDto.SlackResponseType;
import com.tclab.igtools.commons.utils.Utils;
import com.tclab.igtools.media.dto.HydrateMediaDto;
import com.tclab.igtools.media.service.MediaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tclab.igtools.account.enumerator.AccountType;

@Service
@RequiredArgsConstructor
public class SlackCommand {

  private final AccountService accountService;
  private final MediaService mediaService;

  /**
   * Handle Slack commands
   * /account
   * /post
   * /hydrate
   *
   * Reference: https://api.slack.com/interactivity/slash-commands
   *
   */
  public SlackCommandResponseDto execute(SlackCommandDto params) {
    String command = params.getCommand().substring(1);
    SlackCommandType slackCommand = SlackCommandType.fromValue(command);

    switch (slackCommand) {
      case ACCOUNT:
        return account(params);

      case  HYDRATE:
        return hydrate(params);

      case POST:
        return post(params);

      default:
        return SlackCommandResponseDto.builder()
            .text("Unsupported command!")
            .response_type(SlackResponseType.EPHEMERAL.getValue())
            .build();
    }
  }

  private SlackCommandResponseDto account(SlackCommandDto slackCommandDto) {
    String text = slackCommandDto.getText();
    try {
      // all accounts
      if (Utils.isEmpty(text)) {
        List<AccountDto> accounts = accountService.findAll();
        String accs = accounts.stream().map(AccountDto::getUsername)
            .collect(Collectors.joining("\n-"));

        return SlackCommandResponseDto.builder()
            .text("*Accounts*\n- "+accs)
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

      // account per type
      else {
        AccountType accountType = AccountType.fromValue(text);
        List<AccountDto> accounts = accountService.findByType(accountType.name());
        String accs = accounts.stream().map(AccountDto::getUsername)
            .collect(Collectors.joining("\n-"));

        return SlackCommandResponseDto.builder()
            .text("*Accounts*\n- "+accs)
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

    } catch (Exception e) {
      return SlackCommandResponseDto.builder()
          .text("Error getting accounts!")
          .response_type(SlackResponseType.CHANNEL.getValue())
          .build();
    }
  }

  private SlackCommandResponseDto hydrate(SlackCommandDto slackCommandDto) {
    String text = slackCommandDto.getText();
    try {
      // hydrate all accounts
      if (Utils.isEmpty(text)) {
        mediaService.hydrate(null);

        return SlackCommandResponseDto.builder()
            .text("Hydrate process started: all accounts")
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

      // account per type
      else {
        AccountDto accountDto = accountService.findByUsername(text);
        mediaService.hydrate(HydrateMediaDto.builder()
            .igBusinessAccountId(String.valueOf(accountDto.getIgBusinessAccountId())).build());

        return SlackCommandResponseDto.builder()
            .text(String.format("Hydrate process started: %s", text))
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

    } catch (Exception e) {
      return SlackCommandResponseDto.builder()
          .text("Error starting hydrate process!")
          .response_type(SlackResponseType.CHANNEL.getValue())
          .build();
    }
  }

  private SlackCommandResponseDto post(SlackCommandDto slackCommandDto) {
    return null;
  }


  // Class utils
  public enum SlackCommandType {
    ACCOUNT,
    HYDRATE,
    POST;

    public static SlackCommandType fromValue(String value) {
      return Optional.ofNullable(value)
          .map(String::toUpperCase)
          .map(
              upperValue -> {
                try {
                  return SlackCommandType.valueOf(upperValue);
                } catch (Exception ex) {
                  return null;
                }
              })
          .orElseThrow(() -> new InvalidDataException(String.format("Unknown slack command: %s", value)));
    }
  }

}
