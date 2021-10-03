package com.tclab.igtools.account.service;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.commons.dto.ResponseDto;
import com.tclab.igtools.commons.dto.ResultPage;
import java.util.List;

public interface AccountService {

  ResponseDto createAccount(AccountDto accountDto);

  ResultPage<AccountDto> findAccount(Integer pageNo, Integer pageSize, AccountDto accountDto);

  ResponseDto updateAccount(AccountDto accountDto);

  ResponseDto deleteAccount(String username);

  AccountDto findById(Long igBusinessAccountId);

  List<AccountDto> findByType(String type);

  List<AccountDto> findAll();

}
