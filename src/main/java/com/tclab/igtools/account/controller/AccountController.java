package com.tclab.igtools.account.controller;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.entity.Account;
import com.tclab.igtools.account.service.AccountService;
import com.tclab.igtools.commons.dto.ResponseDto;
import com.tclab.igtools.commons.dto.ResultPage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ig/account")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto accountDto) {
    return ResponseEntity.ok(accountService.createAccount(accountDto));
  }

  @PutMapping
  public ResponseEntity<ResponseDto> updateAccount(@RequestBody AccountDto accountDto) {
    return ResponseEntity.ok(accountService.updateAccount(accountDto));
  }

  @PostMapping("/search")
  public ResponseEntity<ResultPage<AccountDto>> searchAccount(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "100") Integer size,
      @RequestBody AccountDto accountDto) {
    return ResponseEntity.ok(accountService.findAccount(page, size, accountDto));
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<ResponseDto> deleteAccount(@PathVariable String username) {
    return ResponseEntity.ok(accountService.deleteAccount(username));
  }



}
