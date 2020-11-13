package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Deliverable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliverableRepository extends CrudRepository<Deliverable, Integer> {
    List<Deliverable> findByClassId(int class_id);

    void deleteByClassId(int class_id);

}
