package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.entity.Submission;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import com.carleton.comp5104.cms.service.DeliverableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class DeliverableServiceImpl implements DeliverableService {

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public boolean submitDeliverable(int studentId, int deliverableId) {
        Optional<Deliverable> deliverable = deliverableRepository.findById(deliverableId);
        if (!deliverable.isPresent()) {
            return false;
        }

        if (System.currentTimeMillis() > deliverable.get().getDeadLine().getTime()) {
            return false;
        }

        Submission submission = new Submission();
        submission.setDeliverableId(deliverableId);
        submission.setStudentId(studentId);
        submission.setSubmitTime(new Timestamp(System.currentTimeMillis()));

        submissionRepository.save(submission);
        return true;
    }
}
