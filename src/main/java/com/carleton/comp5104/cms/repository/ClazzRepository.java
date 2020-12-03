package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.enums.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ClazzRepository extends JpaRepository<Clazz, Integer> {
    List<Clazz> findAllByClassStatus(ClassStatus classStatus);

    ArrayList<Clazz> findAllByCourseId(Integer courseId);

    List<Clazz> findByProfId(int prof_id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Clazz>  findByClassId(int classId);
}
