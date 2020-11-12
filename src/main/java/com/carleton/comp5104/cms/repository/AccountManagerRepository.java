package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AccountManagerRepository extends JpaRepository<Account, Integer> {}
