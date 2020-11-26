package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public boolean isExist(int personId) {
        return findById(personId) != null;
    }

    @Override
    public Person findById(int personId) {
        Optional<Person> person = personRepository.findById(personId);
        return person.orElse(null);
    }
}
