package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Preclusion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface PreclusionRepository extends CrudRepository<Preclusion, Integer> {
    Set<Preclusion> findByCourseId(int courseId);

    Set<Preclusion> findAllByCourseId(int courseId);

    Integer deleteAllByCourseId(int courseId);

    Integer deleteByCourseIdAndPreclusionId(int courseId, int preclusionId);

    boolean existsPreclusionByCourseIdAndPreclusionId(int courseId, int preclusionId);

}
