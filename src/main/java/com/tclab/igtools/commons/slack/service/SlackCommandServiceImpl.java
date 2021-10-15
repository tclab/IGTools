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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tclab.igtools.account.enumerator.AccountType;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackCommandServiceImpl implements SlackCommandService {

  private final AccountService accountService;
  private final MediaService mediaService;

  @Override
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
            .collect(Collectors.joining("\n- "));

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
            .collect(Collectors.joining("\n- "));

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
        log.info("Hydrating all accounts...");
        mediaService.hydrate(null);

        return SlackCommandResponseDto.builder()
            .text("Hydrate process started for all accounts")
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

      // account per type
      else {
        log.info("Hydrating account: {}", text);
        AccountDto accountDto = accountService.findByUsername(text);
        mediaService.hydrate(HydrateMediaDto.builder()
            .igBusinessAccountId(String.valueOf(accountDto.getIgBusinessAccountId())).build());

        return SlackCommandResponseDto.builder()
            .text(String.format("Hydrate process started for account %s", text))
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
    String text = slackCommandDto.getText();
    try {
      // post to all accounts
      if (Utils.isEmpty(text)) {
        log.info("Posting to all accounts...");
        List<AccountDto> managedAccounts = accountService.findByType(AccountType.MANAGED.name());
        managedAccounts.forEach(accountDto -> {
          log.info("Posting for account: {}", accountDto.getUsername());
          mediaService.post(accountDto.getIgBusinessAccountId());
        });

        return SlackCommandResponseDto.builder()
            .text("Post process started for all accounts")
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

      // post to a single account
      else {
        log.info("Posting to account: {}", text);
        AccountDto accountDto = accountService.findByUsername(text);
        mediaService.post(accountDto.getIgBusinessAccountId());

        return SlackCommandResponseDto.builder()
            .text(String.format("Post process started for account %s", text))
            .response_type(SlackResponseType.CHANNEL.getValue())
            .build();
      }

    } catch (Exception e) {
      return SlackCommandResponseDto.builder()
          .text("Error starting post process!")
          .response_type(SlackResponseType.CHANNEL.getValue())
          .build();
    }
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
