package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Clazz;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ClazzRepository extends CrudRepository<Clazz, Integer> {
    ArrayList<Clazz> findAllByCourseId(Integer courseId);
}
