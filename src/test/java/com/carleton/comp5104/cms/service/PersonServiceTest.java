package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void isExistTest() {
        // scenario 1: personId doesn't exist
        int personId = 3000500;
        Optional<Person> optionalPerson = personRepository.findById(personId);
        Assertions.assertFalse(optionalPerson.isPresent());
        boolean exist = personService.isExist(personId);
        Assertions.assertFalse(exist);

        // scenario 2: personId exist
        personId = 3000499;
        optionalPerson = personRepository.findById(personId);
        Assertions.assertTrue(optionalPerson.isPresent());
        exist = personService.isExist(personId);
        Assertions.assertTrue(exist);
    }

    @Test
    void findByIdTest() {
        // scenario 1: personId doesn't exist
        int personId = 3000500;
        Optional<Person> optionalPerson = personRepository.findById(personId);
        Assertions.assertFalse(optionalPerson.isPresent());
        Person person = personService.findById(personId);
        Assertions.assertNull(person);

        // scenario 2: personId exist
        personId = 3000499;
        optionalPerson = personRepository.findById(personId);
        Assertions.assertTrue(optionalPerson.isPresent());
        person = personService.findById(personId);
        Assertions.assertNotNull(person);
    }

}
