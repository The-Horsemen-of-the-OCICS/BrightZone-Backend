package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.entity.Submission;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import com.carleton.comp5104.cms.repository.DeliverableRepository;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import com.carleton.comp5104.cms.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private DeliverableRepository deliverableRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Optional<Deliverable> getDeliverable(int id) {
        return deliverableRepository.findById(id);
    }

    @Transactional
    public int deleteAllDeliverable(int class_id) {
        int result = -1;
        try {
            List<Deliverable> deliverables = deliverableRepository.findByClassId(class_id);
            if (deliverables.isEmpty()) {
                System.out.println("NO DELIVERABLES");
                result = -1;
            } else {
                for (Deliverable d : deliverables) {
                    this.deleteDeliverable(d.getDeliverableId());
                }
                result = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        return result;
    }

    // ----------------------- Use case: submit a deliverable ----------------------- //
    public int submitDeliverable(Deliverable deliverable) {
        int result = -1;
        // check if deadline is reasonable
        if (deliverable.getDeadLine().before(new Timestamp(System.currentTimeMillis()))) {
            return result;
        }

        try {
            deliverable = deliverableRepository.save(deliverable);
            result = deliverable.getDeliverableId();
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

    @Transactional
    public int deleteDeliverable(int deliverable_id) {
        int result = -1;
        try {
            Optional<Deliverable> deliverableOptional = deliverableRepository.findById(deliverable_id);
            if (deliverableOptional.isPresent()) {
                submissionRepository.deleteByDeliverableId(deliverable_id);
                deliverableRepository.deleteById(deliverable_id);
                result = 0;
            }
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

    // ----------------------- Use case: submit grade for a submission of a deliverable ----------------------- //
    public int submitDeliverableGrade(int submission_id, float score) {
        int result = -1;
        try {
            Optional<Submission> submissionOptional = submissionRepository.findById(submission_id);
            if (submissionOptional.isEmpty()) {
                System.out.println("NO SUCH SUBMISSION");
                result = -1;
            } else {
                Submission curSubmission = submissionOptional.get();
                curSubmission.setGrade(score);
                submissionRepository.save(curSubmission);
                result = 0;
            }
        } catch (Exception ex) {
            return -1;
        }
        return result;
    }

    // ----------------------- Use case: submit final grade for a course of a student ----------------------- //
    public int submitFinalGrade(int class_id, int student_id) {
        int result = -1;
        try {
            List<Enrollment> enrollments = enrollmentRepository.findByClassIdAndStudentIdAndStatus(class_id, student_id, EnrollmentStatus.ongoing);
            if (enrollments.isEmpty()) {
                System.out.println("NO SUCH ENROLLMENT");
                result = -1;
            } else {
                float finalGrade = calculateGrade(class_id, student_id);
                Enrollment curEnrollment = enrollments.get(0);
                curEnrollment.setGrade(finalGrade);
                curEnrollment.setStatus(finalGrade >= 0.5 ? EnrollmentStatus.passed : EnrollmentStatus.failed);
                enrollmentRepository.save(curEnrollment);
                result = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        return result;
    }

    public float calculateGrade(int class_id, int student_id) {
        float grade = 0f;
        float percent_sum = 0f;

        List<Deliverable> deliverables = deliverableRepository.findByClassId(class_id);
        if (deliverables.isEmpty()) {
            System.out.println("NO DELIVERABLES");
        } else {
            for (Deliverable d : deliverables) {
                percent_sum += d.getPercent();
                List<Submission> submissions = submissionRepository.findByDeliverableIdAndStudentIdOrderBySubmitTimeDesc(d.getDeliverableId(), student_id);
                if (!submissions.isEmpty() && submissions.get(0).getSubmitTime().before(d.getDeadLine())) {
                    grade += submissions.get(0).getGrade() * d.getPercent();
                }
            }
        }

        if (percent_sum != 0f) {
            grade = grade / percent_sum;
        }
        return grade;
    }


}
