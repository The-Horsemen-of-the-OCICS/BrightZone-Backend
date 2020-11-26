package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.enums.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface ClazzRepository extends JpaRepository<Clazz, Integer> {
    Set<Clazz> findAllByClassStatus(ClassStatus classStatus);

    ArrayList<Clazz> findAllByCourseId(Integer courseId);

    List<Clazz> findByProfId(int prof_id);
}
