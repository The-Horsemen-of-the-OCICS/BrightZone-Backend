package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);

    List<Person> findAllByTypeEquals(AccountType accountType);

    boolean existsPersonByEmail(String email);
}
