package com.tclab.igtools.account.dto;

import com.tclab.igtools.account.entity.Account;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

  private Long igBusinessAccountId;
  private String username;
  private String type;
  private Long parentIgBusinessAccountId;
  private Long followers;
  private LocalDateTime creationDate;

  public static AccountDto fromAccount(Account account) {
    return AccountDto.builder()
        .igBusinessAccountId(account.getIgBusinessAccountId())
        .parentIgBusinessAccountId(account.getParentIgBusinessAccountId())
        .username(account.getUsername())
        .type(account.getType())
        .followers(account.getFollowers())
        .creationDate(account.getCreationDate())
        .build();
  }

}
