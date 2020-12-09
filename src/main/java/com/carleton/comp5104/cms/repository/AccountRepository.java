package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.enums.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    List<Account> findAllByEmailAndUserIdNot(String email, int userId);

    Page<Account> findAllByType(AccountType type, Pageable pageable);

    Page<Account> findAllByName(String name, Pageable pageable);

    Page<Account> findAllByTypeAndName(AccountType type, String name, Pageable pageable);

    ArrayList<Account> findByType(AccountType accountType);

    Boolean existsAccountByEmail(String email);
}
