package com.tclab.igtools.account.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.enumerator.AccountType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ACCOUNT")
@ToString
@Builder
public class Account {

  @Id
  private String username;

  @Column(unique = true, name = "ig_business_account_id")
  private Long igBusinessAccountId;

  @Column(name="parent_ig_business_account_id")
  private Long parentIgBusinessAccountId;

  @Column
  private String type;

  @Column
  private Long followers;

  @Column(name = "creation_date")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime creationDate;


  // Class UTILS
  public static Account fromDto(AccountDto accountDto) throws Exception {
    return Account.builder()
        .igBusinessAccountId(accountDto.getIgBusinessAccountId())
        .parentIgBusinessAccountId(accountDto.getParentIgBusinessAccountId())
        .type(AccountType.fromValue(accountDto.getType()).name())
        .username(accountDto.getUsername())
        .creationDate(LocalDateTime.now())
        .build();
  }

}
