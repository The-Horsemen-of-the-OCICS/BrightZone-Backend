package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Submission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubmissionRepository  extends CrudRepository<Submission, Integer> {
    List<Submission> findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(int deliverable_id, int student_id);
    List<Submission> findByDeliverableIdOrderBySubmitTimeDesc(int deliverable_id);

    void deleteByDeliverableId(int deliverable_id);

}