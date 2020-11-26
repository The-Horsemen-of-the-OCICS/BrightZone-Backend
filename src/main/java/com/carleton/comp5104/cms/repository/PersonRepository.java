package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);

    List<Person> findAllByTypeEquals(AccountType accountType);
}
