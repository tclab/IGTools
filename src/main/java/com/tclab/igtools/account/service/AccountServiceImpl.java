package com.tclab.igtools.account.service;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.entity.Account;
import com.tclab.igtools.account.repository.AccountRepository;
import com.tclab.igtools.account.specification.AccountSearchSpecification;
import com.tclab.igtools.commons.dto.ResponseDto;
import com.tclab.igtools.commons.dto.ResultPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

  private final AccountRepository accountRepository;

  @Override
  public ResponseDto createAccount(AccountDto accountDto) {
    try {
      if (accountRepository.existsById(accountDto.getUsername())) {
        log.error(String.format("Error trying to create account %s: Account already created!", accountDto));
        return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to create account %s: Account already created!", accountDto));
      }

      Account account = accountRepository.save(Account.fromDto(accountDto));
      log.info(String.format("Account %s was created!", account));
      return response(HttpStatus.OK, String.format("Account %s was created!", account));
    } catch (Exception e) {
      log.error(String.format("Error trying to save account %s", accountDto.toString()) + ": " + e.getMessage());
      return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to save account %s: %s", accountDto, e.getMessage()));
    }
  }

  @Override
  public ResultPage<AccountDto> findAccount(Integer pageNo, Integer pageSize, AccountDto accountDto) {
    Pageable paging = PageRequest.of(pageNo, pageSize);
    Specification<Account> searchSpecification = new AccountSearchSpecification(accountDto);
    Page<Account> pagedResult = accountRepository.findAll(searchSpecification, paging);
    return ResultPage.of(pagedResult.map(AccountDto::fromAccount));
  }

  @Override
  public AccountDto findById(Long igBusinessAccountId) {
    return AccountDto.fromAccount(accountRepository.getAccountByIgBusinessAccountId(igBusinessAccountId));
  }

  @Override
  public ResponseDto updateAccount(AccountDto accountDto) {
    try {
      if (!accountRepository.existsById(accountDto.getUsername())) {
        log.error(String.format("Error trying to update account %s: Account does not exist!", accountDto));
        return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to update account %s: Account does not exist!", accountDto));
      }

      Account account = accountRepository.save(Account.fromDto(accountDto));
      log.info(String.format("Account %s was updated!", account));
      return response(HttpStatus.OK, String.format("Account %s was updated!", account));
    } catch (Exception e) {
      log.error(String.format("Error trying to update account %s", accountDto.toString()) + ": " + e.getMessage());
      return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to update account %s: %s", accountDto, e.getMessage()));
    }
  }

  @Override
  public ResponseDto deleteAccount(String username) {
    if (!accountRepository.existsById(username)) {
      log.error(String.format("Error trying to delete account %s: Account does not exist!", username));
      return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to delete account %s: Account does not exist!", username));
    }

    try {
      accountRepository.deleteById(username);
      log.info(String.format("Account %s was deleted!", username));
      return response(HttpStatus.OK, String.format("Account %s was deleted!!", username));
    } catch (Exception e) {
      log.error(String.format("Error trying to delete account %s: %s", username, e.getMessage()));
      return response(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Error trying to delete account %s: %s", username, e.getMessage()));
    }
  }




  // Class UTILS
  private Sort.Direction getSortDirection(String direction) {
    if (direction.equalsIgnoreCase(Sort.Direction.ASC.name())) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
  }

  private ResponseDto response(HttpStatus status, String message) {
    return ResponseDto.builder()
        .status(status.value())
        .message(message)
        .build();
  }
}
