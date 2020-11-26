package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Person;

public interface PersonService {

    boolean isExist(int personId);

    Person findById(int personId);

}
