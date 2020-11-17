package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByEmail(String email);
}
