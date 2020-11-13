package com.carleton.comp5104.cms.repository;

import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.enums.EnrollmentStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
    List<Enrollment> findByClassIdAndStudentIdAndStatus(int class_id, int student_id, EnrollmentStatus status);
    List<Enrollment> findByClassIdAndStatus(int class_id, EnrollmentStatus status);

    Set<Enrollment> findByStudentIdAndStatus(int studentId, EnrollmentStatus status);

}
