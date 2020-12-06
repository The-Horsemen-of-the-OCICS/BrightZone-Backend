package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    ArrayList<Account> findByType(AccountType accountType);

    Boolean existsAccountByEmail(String email);
}
