package com.tclab.igtools.account.repository;

import com.tclab.igtools.account.entity.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {

  Account getAccountByIgBusinessAccountId(Long igBusinessAccountId);
  List<Account> getAccountByType(String type);

}
