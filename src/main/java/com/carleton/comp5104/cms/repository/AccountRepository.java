package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    Boolean existsAccountByEmail(String email);
}
