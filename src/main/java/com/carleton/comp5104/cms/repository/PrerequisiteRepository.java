package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Prerequisite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface PrerequisiteRepository extends CrudRepository<Prerequisite,Integer> {
    Set<Prerequisite> findByCourseId(int courseId);

}
