package com.ebike.repository;

import com.ebike.model.Account;
import com.ebike.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountCode(String accountCode);
    List<Account> findByAccountType(AccountType accountType);
}
