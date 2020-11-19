package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.enums.ClassStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ClazzRepository extends CrudRepository<Clazz, Integer> {
    Set<Clazz> findAllByClassStatus(ClassStatus classStatus);
}
